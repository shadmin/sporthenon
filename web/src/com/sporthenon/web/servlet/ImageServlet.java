package com.sporthenon.web.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.PicklistBean;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.meta.Picture;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.ServletHelper;

public class ImageServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			String entity = String.valueOf(hParams.get("entity"));
			if (hParams.containsKey("upload-photo")) {
				Object id = hParams.get("id");
				boolean embedded = String.valueOf(hParams.get("embedded")).equals("1");				
				String source = String.valueOf(hParams.get("source"));
				Picture p = new Picture();
				p.setEntity(entity);
				p.setIdItem(Integer.parseInt(String.valueOf(id)));
				p.setSource(StringUtils.notEmpty(source) ? source : null);
				if (embedded) {
					String value = String.valueOf(hParams.get("value"));
					value = value/*.replaceFirst("\\\"\\/\\/", "\"http://")*/.replaceFirst("\\&caption\\=true", "");
					Document doc = Jsoup.parse(value);
					doc.getElementsByTag("div").get(1).remove();
					value = doc.body().html();
					p.setEmbedded(true);
					p.setValue(value);
				}
				else {
					byte[] b = null;
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					Collection<FileItem> items = upload.parseRequest(request);
					String name = null;
					for (FileItem fitem : items) {
						if (!fitem.isFormField() && fitem.getFieldName().equalsIgnoreCase("photo-file")) {
							name = fitem.getName();
							b = fitem.get();
						}
					}
					String ext = "." + name.substring(name.lastIndexOf(".") + 1).toLowerCase();
					String fileName = "P" + StringUtils.encode(entity + "-" + id);
					File f = new File(ConfigUtils.getProperty("img.folder") + fileName + ext);
					if (f.exists()) {
						int i = 1;
						while (f.exists()) {
							f = new File(ConfigUtils.getProperty("img.folder") + fileName + i + ext);
							i++;
						}
					}
					FileOutputStream fos = new FileOutputStream(f);
					fos.write(b);
					fos.close();
					ImageUtils.getImgFiles().add(f.getName());
					Collections.sort(ImageUtils.getImgFiles());
					p.setEmbedded(false);
					p.setValue(f.getName());
				}
				DatabaseHelper.saveEntity(p, null);
			}
			else if (hParams.containsKey("load")) {
				if (hParams.containsKey("directid")) {
					Picture p = (Picture) DatabaseHelper.loadEntity(Picture.class, hParams.get("directid"));
					if (p != null)
						ServletHelper.writeText(response, p.getValue());
				}
				else {
					String id = String.valueOf(hParams.get("id"));
					StringBuffer sb = new StringBuffer();
					for (Picture p : (List<Picture>) DatabaseHelper.execute("from Picture where entity='" + entity + "' and idItem=" + id + "order by id")) {
						sb.append("<li id='currentphoto-" + p.getId() + "' class='img'>");
						if (p.isEmbedded())
							sb.append(p.getValue());
						else {
							sb.append("<a target='_blank' href='" + ImageUtils.getUrl() + p.getValue() + "' title='" + p.getValue() + "'>");
							sb.append("<img alt='' src='" + ImageUtils.getUrl() + p.getValue() + "'/></a>");
						}
						sb.append("</li><li id='currentphoto-rm-" + p.getId() + "'><a href='javascript:removePhoto(" + p.getId() + ", \"" + (!p.isEmbedded() ? p.getValue() : "") + "\");' title='" + ResourceUtils.getText("remove", getLocale(request)) + "'>[X]</a></li>");
					}
					ServletHelper.writeText(response, sb.toString());
				}
			}
			else if (hParams.containsKey("upload")) {
				String id = String.valueOf(hParams.get("id"));
				byte[] b = null;
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				Collection<FileItem> items = upload.parseRequest(request);
				for (FileItem fitem : items) {
					if (!fitem.isFormField() && fitem.getFieldName().equalsIgnoreCase("f"))
						b = fitem.get();
				}
				String y1 = String.valueOf(hParams.get("y1"));
				String y2 = String.valueOf(hParams.get("y2"));
				String ext = ".png";
				String fileName = ImageUtils.getIndex(entity.toUpperCase()) + "-" + id + "-" + hParams.get("size") + (StringUtils.notEmpty(y1) && !y1.equals("null") ? "_" + y1 + "-" + y2 : "");
				int index = -1;
				Collection<String> lExisting = ImageUtils.getImageList(ImageUtils.getIndex(entity.toUpperCase()), id, String.valueOf(hParams.get("size")).charAt(0));
				for (String s : lExisting) {
					if (s.indexOf(fileName) == 0) {
						index = 0;
						if (s.matches(".*\\_\\d+\\.png$"))
							index = Integer.parseInt(s.replaceAll(".*\\_|\\.png$", ""));
						index++;
						break;
					}
				}
				File f = new File(ConfigUtils.getProperty("img.folder") + fileName + (index > -1 ? "_" + index : "") + ext);
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(b);
				fos.close();
				ImageUtils.getImgFiles().add(f.getName());
				Collections.sort(ImageUtils.getImgFiles());
			}
			else if (hParams.containsKey("download")) {
				String fname = String.valueOf(hParams.get("name"));
				File f = new File(ConfigUtils.getProperty("img.folder") + fname);
				response.setHeader("Content-Disposition", "attachment;filename=" + fname);
				response.setContentType("text/html");
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				int i;
				while ((i = in.read()) != -1) {
					out.write(i);
				}
				out.flush();
				out.close();
				in.close();
			}
			else if (hParams.containsKey("remove")) {
				if (StringUtils.notEmpty(hParams.get("name"))) {
					String fname = String.valueOf(hParams.get("name"));
					File f = new File(ConfigUtils.getProperty("img.folder") + fname);
					if (f.exists()) {
						f.delete();
						ImageUtils.getImgFiles().remove(f.getName());
						Collections.sort(ImageUtils.getImgFiles());
					}
				}
				DatabaseHelper.executeUpdate("DELETE FROM \"~Picture\" WHERE ID=" + hParams.get("id"));
			}
			else if (hParams.containsKey("copy")) {
				String id1 = String.valueOf(hParams.get("id1"));
				String id2 = String.valueOf(hParams.get("id2"));
				boolean found = false;
				List<String> lAdded = new ArrayList<String>();
				for (String s : ImageUtils.getImgFiles()) {
					if (s.indexOf(ImageUtils.getIndex(entity) + "-" + id1 + "-") == 0) {
						File f1 = new File(ConfigUtils.getProperty("img.folder") + s);
						File f2 = new File(ConfigUtils.getProperty("img.folder") + s.replaceFirst("\\-" + id1 + "\\-", "-" + id2 + "-"));
						FileUtils.copyFile(f1, f2);
						lAdded.add(f2.getName());
						found = true;
					}
					else if (found)
						break;
				}
				ImageUtils.getImgFiles().addAll(lAdded);
				Collections.sort(ImageUtils.getImgFiles());
			}
			else if (hParams.containsKey("url")) {
				String id = String.valueOf(hParams.get("id"));
				String size = String.valueOf(hParams.get("size"));
				String s = HtmlUtils.writeImage(ImageUtils.getIndex(entity), Integer.valueOf(id), size.charAt(0), null, null);
				ServletHelper.writeText(response, s.replaceAll(".*src\\=\\'", "").replaceAll("\\'\\/\\>", ""));
			}
			else if (hParams.containsKey("list")) {
				String id = String.valueOf(hParams.get("id"));
				String size = String.valueOf(hParams.get("size"));
				StringBuffer sb = new StringBuffer();
				for (String s : ImageUtils.getImageList(ImageUtils.getIndex(entity), id, size.charAt(0)))
					sb.append(s).append(",");
				ServletHelper.writeText(response, sb.toString());
			}
			else if (hParams.containsKey("nopic")) {
				String id = String.valueOf(hParams.get("id"));
				String value = String.valueOf(hParams.get("value"));
				Object o = DatabaseHelper.loadEntity(DatabaseHelper.getClassFromAlias(entity), id);
				Method m = o.getClass().getMethod("setNopic", Boolean.class);
				m.invoke(o, value.equals("1"));
				DatabaseHelper.saveEntity(o, null);
			}
			else if (hParams.containsKey("data")) {} // OBSOLETE
			else if (hParams.containsKey("missing")) {
				StringBuffer sbResult = new StringBuffer();
				for (String entity_ : new String[]{"CP", "EV", "SP", "CN", "OL", "TM"}) {
					String label = null;
					if (entity_.equals(Olympics.alias))
						label = "concat(concat(year.label, ' - '), city.label)";
					else if (entity_.equals(Team.alias))
						label = "concat(concat(label, ' - '), sport.label)";
					Collection<PicklistBean> lst = DatabaseHelper.getEntityPicklist(DatabaseHelper.getClassFromAlias(entity_), label, "nopic", ResourceUtils.LGDEFAULT);
					int n = 0;
					for (PicklistBean o : lst) {
						if (o.getParam() != null && o.getParam().equals("true"))
							continue;
						Collection list = ImageUtils.getImageList(ImageUtils.getIndex(entity_.toUpperCase()), o.getValue(), ImageUtils.SIZE_LARGE);
						if (list == null || list.isEmpty())
							sbResult.append(entity_).append(";").append(++n).append(";").append(o.getValue()).append(";").append(o.getText()).append("\r\n");						
					}
				}
				response.setContentType("text/plain");
				response.setCharacterEncoding("utf-8");
				response.setHeader("Content-Disposition", "attachment;filename=Sporthenon_missing_pics.csv");
				response.getWriter().write(sbResult.toString());
				response.flushBuffer();
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}

}