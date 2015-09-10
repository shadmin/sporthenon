package com.sporthenon.web.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.PicklistBean;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Team;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;

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
				Object id = hParams.get("id");
				String ext = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
				String fileName = "photo" + StringUtils.encode(entity + "-" + id) + "." + ext;
				File f = new File(ConfigUtils.getProperty("img.folder") + fileName);
				if (f.exists())
					f.delete();
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(b);
				fos.close();
				ImageUtils.getImgFiles().add(f.getName());
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
				File f = new File(ConfigUtils.getProperty("img.folder") + fileName + ext);
				if (f.exists()) {
					int i = 1;
					while (f.exists()) {
						f = new File(ConfigUtils.getProperty("img.folder") + fileName + "_" + i + ext);
						i++;
					}
				}
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(b);
				fos.close();
				ImageUtils.getImgFiles().add(f.getName());
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
				String fname = String.valueOf(hParams.get("name"));
				File f = new File(ConfigUtils.getProperty("img.folder") + fname);
				if (f.exists()) {
					f.delete();
					ImageUtils.getImgFiles().remove(f.getName());
				}
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
			else if (hParams.containsKey("data")) {} // OBSOLETE
			else if (hParams.containsKey("missing")) {
				StringBuffer sbResult = new StringBuffer();
				for (String entity_ : new String[]{"CP", "EV", "SP", "CN", "OL", "TM"}) {
					String label = null;
					if (entity_.equals(Olympics.alias))
						label = "concat(concat(year.label, ' - '), city.label)";
					else if (entity_.equals(Team.alias))
						label = "concat(concat(label, ' - '), sport.label)";
					Collection<PicklistBean> lst = DatabaseHelper.getEntityPicklist(DatabaseHelper.getClassFromAlias(entity_), label, null, ResourceUtils.LGDEFAULT);
					int n = 0;
					for (PicklistBean o : lst) {
//						if (entity_.equals(Team.alias) && o.getText().contains("Sailing"))
//							continue;
						Collection list = ImageUtils.getImageList(ImageUtils.getIndex(entity_.toUpperCase()), o.getValue(), ImageUtils.SIZE_LARGE);
						if (list == null || list.isEmpty())
							sbResult.append(entity_).append(";").append(++n).append(";").append(o.getValue()).append(";").append(o.getText()).append("|");						
					}
				}
				response.setContentType("text/plain");
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(sbResult.toString());
				response.flushBuffer();
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}

}