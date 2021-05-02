package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.HtmlConverter;
import com.sporthenon.web.ServletHelper;

@WebServlet(
    name = "SearchServlet",
    urlPatterns = {"/SearchServlet", "/Search"}
)
public class SearchServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			HashMap<String, Object> mapParams = ServletHelper.getParams(request);
			String lang = getLocale(request);
			if (mapParams.containsKey("entity")) { // Direct search from entity
				String[] t = StringUtils.decode(String.valueOf(mapParams.get("p"))).split("\\-");
				Integer id = Integer.parseInt(t[1]);
				String url = HtmlUtils.writeLink(t[0], id, null, DatabaseManager.getEntityLabel(t[0], id));
				redirect(request, response, url, false);
			}
			else if (mapParams.containsKey("p2") && mapParams.get("p2").equals("ajax")) { // Ajax autocompletion
				final int LIMIT = 10;
				String value = String.valueOf(mapParams.get("value"));
				value = "^" + value.replaceAll("'", "''").replaceAll("_", ".").replaceAll("\\*", ".*");
				List<Object> params = new ArrayList<Object>();
				params.add(value);
				params.add(".");
				params.add((short)LIMIT);
				params.add(false);
				params.add(ResourceUtils.getLocaleParam(lang));
				StringBuffer html = new StringBuffer("<ul>");
				Collection<RefItem> list = (Collection<RefItem>) DatabaseManager.callFunctionSelect("search", params, RefItem.class, "count_ref DESC", "10");
				for (Object obj : list) {
					RefItem item = (RefItem) obj;
					String label = item.getLabel() + (item.getEntity().equals(City.alias) ? " (" + item.getLabelRel3() + ")" : (item.getEntity().equals(Complex.alias) ? " (" + item.getLabelRel1() + ")" : ""));
					if (item.getEntity().equals(Athlete.alias)) {
						String[] t = label.split("\\,\\s");
						String cn = (StringUtils.notEmpty(item.getLabelRel1()) ? item.getLabelRel1().substring(item.getLabelRel1().lastIndexOf("(") + 1, item.getLabelRel1().length() - 1) : null);
						label = StringUtils.toFullName(t[0], t.length > 1 && StringUtils.notEmpty(t[1]) ? t[1] : "", cn, false) + (StringUtils.notEmpty(cn) ? " (" + cn + ")" : "");
					}
					String details = "<div class='ajxdetails'>" + ResourceUtils.getText("entity." + item.getEntity() + ".1", lang) + (StringUtils.notEmpty(item.getLabelRel2()) ? "/" + (item.getEntity().equals(Olympics.alias) ? ResourceUtils.getText(item.getLabelRel2().equals("1") ? "summer" : "winter", lang) : item.getLabelRel2()) : "") + " (" + (item.getCountRef() != null ? item.getCountRef() : 0) + " ref.)</div>";
					html.append("<li id='" + StringUtils.encode(item.getEntity() + "-" + item.getIdItem()) + "'>" + label + details + "</li>");
				}
				html.append("<li class='ajaxlastrow' id=\"LR\">" + ResourceUtils.getText("search.for", lang) + " : \"" + mapParams.get("value") + "\"</li>");
				ServletHelper.writeText(response, html.append("</ul>").toString());
			}
			else if (request.getRequestURI().contains("/Search")) {
				if (mapParams.containsKey("p")) {
					String p = String.valueOf(mapParams.get("p"));
					String[] t = p.split("\\-");
					if (t.length == 1) { // Direct search
						mapParams.put("pattern", t[0]);
						mapParams.put("scope", ".");
					}
					else {
						p = StringUtils.decode(p);
						t = p.split("\\-");
						mapParams.put("pattern", t[0].replaceAll("\\.\\*", "").substring(1));
						mapParams.put("scope", t[1]);
					}
				}
				String pattern = String.valueOf(mapParams.get("pattern"));
				String scope = String.valueOf(mapParams.get("scope"));
				Short max = (mapParams.get("max") != null ? Short.valueOf(String.valueOf(mapParams.get("max"))) : Short.MAX_VALUE);
				Boolean match = String.valueOf(mapParams.get("match")).equals("on");
				pattern = pattern.replaceAll("'", "''").replaceAll("_", ".").replaceAll("\\*", ".*");
				pattern = "^" + pattern;
				List<Object> params = new ArrayList<Object>();
				params.add(pattern);
				params.add(scope);
				params.add(max);
				params.add(match);
				params.add(ResourceUtils.getLocaleParam(lang));
				StringBuffer html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_SEARCH, params, getUser(request), lang);
				html.append(HtmlConverter.convertSearch(request, DatabaseManager.callFunctionSelect("search", params, RefItem.class, "count_ref DESC", max), String.valueOf(mapParams.get("pattern")), lang));
					
				// Load HTML results or export
				HtmlUtils.setHeadInfo(request, html.toString());
				if (mapParams.containsKey("export")) {
					ExportUtils.export(response, html, String.valueOf(mapParams.get("export")), lang);
				}
				else {
					request.setAttribute("menu", "search");
					ServletHelper.writePageHtml(request, response, html, lang, mapParams.containsKey("print"));
				}
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
}