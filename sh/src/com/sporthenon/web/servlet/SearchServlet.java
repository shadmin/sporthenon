package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.HtmlConverter;

public class SearchServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	public SearchServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			if (hParams.containsKey("entity")) { // Direct search from entity
				String[] t = StringUtils.decode(String.valueOf(hParams.get("p"))).split("\\-");
				Integer id = Integer.parseInt(t[1]);
				String url = HtmlUtils.writeLink(t[0], id, null, DatabaseHelper.getEntityName(t[0], id));
				response.sendRedirect(url);
			}
			else if (hParams.containsKey("p2") && hParams.get("p2").equals("ajax")) { // Ajax autocompletion
				String value = String.valueOf(hParams.get("value"));
				value = "^" + value.replaceAll("'", "''").replaceAll("_", ".").replaceAll("\\*", ".*");
				ArrayList<Object> lFuncParams = new ArrayList<Object>();
				lFuncParams.add(value);
				lFuncParams.add(".");
				lFuncParams.add((short)10);
				lFuncParams.add("_" + getLocale(request));
				StringBuffer html = new StringBuffer("<ul>");
				for (Object obj : DatabaseHelper.call("Search", lFuncParams)) {
					RefItem item = (RefItem) obj;
					String label = item.getLabel();
					label += (item.getEntity().equals(City.alias) ? " (" + item.getLabelRel3() + ")" : (item.getEntity().equals(Complex.alias) ? " (" + item.getLabelRel1() + ")" : ""));
					html.append("<li id='" + StringUtils.encode(item.getEntity() + "-" + item.getIdItem()) + "'>" + label + "</li>");
				}
				ServletHelper.writeText(response, html.append("</ul>").toString());
			}
			else if (hParams.containsKey("run")) { // Run search
				boolean isLink = false;
				if (hParams.containsKey("p")) {
					String p = String.valueOf(hParams.get("p"));
					String[] t = p.split("\\-");
					if (t.length == 1) { // Direct search
						hParams.put("pattern", t[0]);
						hParams.put("scope", ".");
					}
					else {
						p = StringUtils.decode(p);
						t = p.split("\\-");
						hParams.put("pattern", t[0].replaceAll("\\.\\*", "").substring(1));
						hParams.put("scope", t[1]);
					}
					isLink = true;
				}
				String pattern = String.valueOf(hParams.get("pattern"));
				String scope = String.valueOf(hParams.get("scope"));
				Boolean match = (String.valueOf(hParams.get("match")).equals("on"));
				pattern = pattern.replaceAll("'", "''").replaceAll("_", ".").replaceAll("\\*", ".*")/*.replaceAll("\\s+", "|")*/;
				pattern = "^" + pattern + (match ? "$" : ".*");
				ArrayList<Object> lFuncParams = new ArrayList<Object>();
				lFuncParams.add(pattern);
				lFuncParams.add(scope);
				lFuncParams.add((short)0);
				lFuncParams.add("_" + getLocale(request));
				StringBuffer html = null;
				if (scope.equals("."))
					html = new StringBuffer("<div class='searchtitle'>" + ResourceUtils.getText("search.results", getLocale(request)) + "&nbsp;:&nbsp;<b>" + String.valueOf(hParams.get("pattern")) + "</b></div>");
				else
					html = HtmlConverter.getHeader(HtmlConverter.HEADER_SEARCH, lFuncParams, getUser(request), getLocale(request));
				html.append(HtmlConverter.convertSearch(DatabaseHelper.call("Search", lFuncParams), String.valueOf(hParams.get("pattern")), getLocale(request)));
				if (isLink) {
					HtmlUtils.setTitle(request, ResourceUtils.getText("menu.search", getLocale(request)));
					if (hParams.containsKey("export"))
						ExportUtils.export(response, html, String.valueOf(hParams.get("export")));
					else
						ServletHelper.writePageHtml(request, response, html, hParams.containsKey("print"));
				}
				else
					ServletHelper.writeTabHtml(response, html, getLocale(request));
			}
		}
		catch (Exception e) {
			handleException(e);
		}
	}
	
}