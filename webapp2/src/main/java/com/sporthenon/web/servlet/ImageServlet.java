package com.sporthenon.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.meta.Picture;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.ServletHelper;

@WebServlet(
	name = "ImageServlet",
	urlPatterns = {"/ImageServlet"}
)
public class ImageServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> mapParams = ServletHelper.getParams(request);
			String entity = String.valueOf(mapParams.get("entity"));
			if (mapParams.containsKey("upload-photo")) {
				Object id = mapParams.get("id");
				boolean embedded = String.valueOf(mapParams.get("embedded")).equals("1");				
				String source = String.valueOf(mapParams.get("source"));
				Picture p = new Picture();
				p.setEntity(entity);
				p.setIdItem(Integer.parseInt(String.valueOf(id)));
				p.setSource(StringUtils.notEmpty(source) ? source : null);
				if (embedded) {
					String value = String.valueOf(mapParams.get("value"));
					value = value/*.replaceFirst("\\\"\\/\\/", "\"http://")*/.replaceFirst("\\&caption\\=true", "");
					Document doc = Jsoup.parse(value);
					doc.getElementsByTag("div").get(1).remove();
					value = doc.body().html();
					p.setEmbedded(true);
					p.setValue(value);
				}
				else {
					byte[] content = null;
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					Collection<FileItem> items = upload.parseRequest(request);
					String name = null;
					for (FileItem fitem : items) {
						if (!fitem.isFormField() && fitem.getFieldName().equalsIgnoreCase("photo-file")) {
							name = fitem.getName();
							content = fitem.get();
						}
					}
					String ext = "." + name.substring(name.lastIndexOf(".") + 1).toLowerCase();
					String fileName = "P" + StringUtils.encode(entity + "-" + id) + ext;
					ImageUtils.uploadImage(null, fileName, content, ConfigUtils.getCredFile());
					p.setEmbedded(false);
					p.setValue(fileName);
				}
				DatabaseManager.saveEntity(p, null);
			}
			else if (mapParams.containsKey("remove-photo")) {
				try {
					String id = String.valueOf(mapParams.get("id"));
					String name = String.valueOf(mapParams.get("name"));
					ImageUtils.removeImage(name, ConfigUtils.getCredFile());
					Picture pic = new Picture();
					pic.setId(StringUtils.toInt(id));
					DatabaseManager.removeEntity(pic);
				}
				catch (Exception e_) {
					log.log(Level.WARNING, e_.getMessage(), e_);
				}
			}
			else if (mapParams.containsKey("load")) {
				if (mapParams.containsKey("directid")) {
					Picture p = (Picture) DatabaseManager.loadEntity(Picture.class, mapParams.get("directid"));
					if (p != null) {
						ServletHelper.writeText(response, p.getValue());
					}
				}
				else {
					String id = String.valueOf(mapParams.get("id"));
					StringBuffer sb = new StringBuffer();
					String sql = "SELECT * FROM _picture WHERE entity = ? AND id_item = ? ORDER BY id";
					for (Picture p : (List<Picture>) DatabaseManager.executeSelect(sql, Arrays.asList(entity, Integer.valueOf(id)), Picture.class)) {
						sb.append("<li id='currentphoto-" + p.getId() + "' class='img'>");
						if (p.isEmbedded()) {
							sb.append(p.getValue());
						}
						else {
							sb.append("<a target='_blank' href='" + ImageUtils.getUrl() + p.getValue() + "' title='" + p.getValue() + "'>");
							sb.append("<img alt='' src='" + ImageUtils.getUrl() + p.getValue() + "'/></a>");
						}
						sb.append("</li><li id='currentphoto-rm-" + p.getId() + "'><a href='javascript:removePhoto(" + p.getId() + ", \"" + (!p.isEmbedded() ? p.getValue() : "") + "\");' title='" + ResourceUtils.getText("remove", getLocale(request)) + "'>[X]</a></li>");
					}
					ServletHelper.writeText(response, sb.toString());
				}
			}
			else if (mapParams.containsKey("add")) { // Refresh map remotely
				String key = String.valueOf(mapParams.get("key"));
				String file = String.valueOf(mapParams.get("file"));
				ImageUtils.addImageToMap(key, file);
			}
			else if (mapParams.containsKey("remove")) { // Refresh map remotely
				String file = String.valueOf(mapParams.get("file"));
				ImageUtils.removeImageFromMap(file);
			}
			else if (mapParams.containsKey("url")) {
				String id = String.valueOf(mapParams.get("id"));
				String size = String.valueOf(mapParams.get("size"));
				String s = HtmlUtils.writeImage(ImageUtils.getIndex(entity), StringUtils.toInt(id), size.charAt(0), null, null);
				ServletHelper.writeText(response, s.replaceAll(".*src\\=\\'", "").replaceAll("\\'\\/\\>", ""));
			}
			else if (mapParams.containsKey("nopic")) {
				String id = String.valueOf(mapParams.get("id"));
				String value = String.valueOf(mapParams.get("value"));
				Object o = DatabaseManager.loadEntity(DatabaseManager.getClassFromAlias(entity), id);
				Method m = o.getClass().getMethod("setNopic", Boolean.class);
				m.invoke(o, value.equals("1"));
				DatabaseManager.saveEntity(o, null);
			}
			else if (mapParams.containsKey("missing")) {
				response.setContentType("text/plain");
				response.setCharacterEncoding("utf-8");
				response.setHeader("Content-Disposition", "attachment;filename=Sporthenon_missing_pics.csv");
				response.getWriter().write(ImageUtils.getMissingPictures());
				response.flushBuffer();
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}

}