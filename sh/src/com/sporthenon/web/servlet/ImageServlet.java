package com.sporthenon.web.servlet;

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

import com.sporthenon.utils.ConfigUtils;
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
				String fileName = ImageUtils.getIndex(entity.toUpperCase()) + "-" + id + "-" + hParams.get("size") + ext;
				FileOutputStream fos = new FileOutputStream(ConfigUtils.getProperty("img.folder") + fileName);
				fos.write(b);
				fos.close();
				StringBuffer sb = new StringBuffer();
				sb.append("<html><head><script type='text/javascript'>");
				sb.append("function callBack(){window.parent.uploadCallBack('" + entity + "', '" + fileName + "'); }");
				sb.append("</script></head><body onload='callBack()'></body></html>");
				ServletHelper.writeHtml(response, sb);
			}
			else if (hParams.containsKey("data")) {
			}
			else if (hParams.containsKey("missing")) {
//				String label = null;
//				if (entity.equalsIgnoreCase(Olympics.alias))
//					label = "concat(concat(year.label, ' - '), city.label)";
//				else if (entity.equalsIgnoreCase(Team.alias))
//					label = "concat(concat(label, ' - '), sport.label)";
//				Collection<PicklistBean> lst = DatabaseHelper.getEntityPicklist(UpdateServlet.getClassFromAlias(entity), label, null);
//				StringBuffer sb = new StringBuffer();
//				int n = 0;
//				for (PicklistBean o : lst) {
//					String ext = (entity.toUpperCase().matches("CP|EV|OL|SP|TM") ? ".png" : ".gif");
//					String fileName = ImageUtils.getIndex(entity.toUpperCase()) + "-" + o.getValue() + "-" + hParams.get("size") + ext;
//					File f = new File(ConfigUtils.getProperty("img.folder") + fileName);
//					if (!f.exists())
//						sb.append(++n + ". " + o.getText() + "<br/>");
//				}
//				ServletHelper.writeText(response, sb.toString());
			}
		}
		catch (Exception e) {
			handleException(e);
		}
	}

}
