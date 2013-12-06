package com.sporthenon.web.servlet;

import java.io.File;
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

public class ImageServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			String entity = String.valueOf(hParams.get("entity"));
			if (hParams.containsKey("upload")) {
				String id = null;
				byte[] b = null;
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				Collection<FileItem> items = upload.parseRequest(request);
				for (FileItem fitem : items) {
					if (!fitem.isFormField() && fitem.getFieldName().equalsIgnoreCase(entity + "-file"))
						b = fitem.get();
					else if (StringUtils.notEmpty(fitem.getFieldName()) && fitem.getFieldName().equalsIgnoreCase(entity + "-id"))
						id = fitem.getString();
				}
				String ext = (entity.toUpperCase().matches("CP|EV|OL|SP|TM") ? ".png" : ".gif");
				String fileName = ImageUtils.getIndex(entity.toUpperCase()) + "-" + id + "-" + hParams.get("size");
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
			}
			else if (hParams.containsKey("url")) {
				String type = String.valueOf(hParams.get("type"));
				String id = String.valueOf(hParams.get("id"));
				String size = String.valueOf(hParams.get("size"));
				String s = HtmlUtils.writeImage(Short.valueOf(type), Integer.valueOf(id), size.charAt(0), false);
				ServletHelper.writeText(response, s.replaceAll(".*src\\=\\'", "").replaceAll("\\'\\/\\>", ""));
			}
			else if (hParams.containsKey("data")) {} // OBSOLETE
			else if (hParams.containsKey("missing")) {
				StringBuffer sbResult = new StringBuffer();
				for (String entity_ : new String[]{"CP", "EV", "SP", "CN", "OL", "TM"}) {
					String label = null;
					if (entity_.equalsIgnoreCase(Olympics.alias))
						label = "concat(concat(year.label, ' - '), city.label)";
					else if (entity_.equalsIgnoreCase(Team.alias))
						label = "concat(concat(label, ' - '), sport.label)";
					Collection<PicklistBean> lst = DatabaseHelper.getEntityPicklist(DatabaseHelper.getClassFromAlias(entity_), label, null);
					int n = 0;
					for (PicklistBean o : lst) {
						String ext = (entity_.toUpperCase().matches("CP|EV|OL|SP|TM") ? ".png" : ".gif");
						String fileName = ImageUtils.getIndex(entity_.toUpperCase()) + "-" + o.getValue() + "-L" + ext;
						File f = new File(ConfigUtils.getProperty("img.folder") + fileName);
						if (!f.exists())
							sbResult.append(entity_).append(";").append(++n).append(";").append(o.getValue()).append(";").append(o.getText()).append("|");
					}
				}
				response.setContentType("text/plain");
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(sbResult.toString());
			}
		}
		catch (Exception e) {
			handleException(e);
		}
	}

}