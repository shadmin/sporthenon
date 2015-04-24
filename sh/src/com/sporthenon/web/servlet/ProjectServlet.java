package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.function.ContributorBean;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.StringUtils;

public class ProjectServlet extends AbstractServlet {

private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			// Contributors
			StringBuffer html = new StringBuffer();
			Collection c = DatabaseHelper.call("Contributors", null);
			for (Object obj : c) {
				ContributorBean bean = (ContributorBean) obj;
				html.append("<tr><td><a href='" + HtmlUtils.writeLink(Contributor.alias, bean.getId(), null, bean.getLogin()) + "'>" + bean.getLogin() + "</a></td>");
				html.append("<td>" + (StringUtils.notEmpty(bean.getName()) ? bean.getName() : "-") + "</td>");
				html.append("<td>" + bean.getCount() + "</td></tr>");
			}
			request.setAttribute("contributors", html.toString());
			request.setAttribute("t2", System.currentTimeMillis());
			request.getRequestDispatcher("/jsp/project.jsp").forward(request, response);
			
			// Statistics
//			HashMap<String, Object> hParams = ServletHelper.getParams(request);
//			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
//			Document doc = docBuilder.newDocument();
//			Element root = doc.createElement("stats");
//			doc.appendChild(root);
//			
//			String index = String.valueOf(hParams.get("index"));
//			ArrayList<Object> lFuncParams = new ArrayList<Object>();
//			lFuncParams.add(Short.valueOf(index));
//			int i = 0;
//			for (Object o : DatabaseHelper.call("StatRequest", lFuncParams)) {
//				StatRequestBean srb = (StatRequestBean) o;
//				Element item = doc.createElement("stat");
//				String key = srb.getKey();
//				if (index.equals("0"))
//					key = key.replaceAll("RS", "Results").replaceAll("OL", "Olympics").replaceAll("US", "US Leagues").replaceAll("SC", "Search").replaceAll("IF", "Info");
//				item.setAttribute("key", key); item.setAttribute("value", String.valueOf(srb.getValue()));
//				root.appendChild(item);
//				if (++i == 12)
//					break;
//			}
//
//			response.setContentType("text/xml");
//			response.setCharacterEncoding("utf-8");
//			XMLSerializer serializer = new XMLSerializer();
//			serializer.setOutputCharStream(response.getWriter());
//			serializer.serialize(doc);
		}
		catch (Exception e) {
			handleException(e);
		}
	}
	
}