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
import com.sporthenon.db.PicklistItem;
import com.sporthenon.db.entity.meta.TreeItem;
import com.sporthenon.db.function.OlympicMedalsBean;
import com.sporthenon.db.function.OlympicRankingsBean;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.HtmlConverter;
import com.sporthenon.web.ServletHelper;

@WebServlet(
    name = "OlympicsServlet",
    urlPatterns = {"/OlympicsServlet", "/SearchOlympics"}
)
public class OlympicsServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;

	private static final String TYPE_SUMMER = "summer";
	//private static final String TYPE_WINTER = "winter";
	private static final String TYPE_INDIVIDUAL = "ind";
	private static final String TYPE_COUNTRY = "cnt";

	private static final String PICKLIST_ID_OLYMPICS = "pl-ol";
	private static final String PICKLIST_ID_COUNTRY = "pl-cn";

	public OlympicsServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			HashMap<String, Object> mapParams = ServletHelper.getParams(request);
			String lang = getLocale(request);
			if (request.getRequestURI().contains("/SearchOlympics")) {
				if (mapParams.containsKey("p")) {
					String p = String.valueOf(mapParams.get("p"));
					p = StringUtils.decode(p);
					String[] t = p.split("\\-");
					mapParams.put("type", t[0]);
					mapParams.put("ol", t[1]);
					if (t[0].equals(TYPE_INDIVIDUAL)) {
						mapParams.put("sp", t[2]);
						mapParams.put("ev", t.length > 3 ? t[3] : "0");
						mapParams.put("se", t.length > 4 ? t[4] : "0");
						mapParams.put("se2", t.length > 5 ? t[5] : "0");
					}
					else
						mapParams.put("cn", t.length > 2 ? t[2] : "0");
				}
				List<Object> params = new ArrayList<Object>();
				String type = String.valueOf(mapParams.get("type"));
				StringBuffer html = null;
				if (type.equals(TYPE_INDIVIDUAL)) {
					params.add(StringUtils.notEmpty(mapParams.get("ol")) ? String.valueOf(mapParams.get("ol")) : "0");
					params.add(StringUtils.notEmpty(mapParams.get("sp")) ? StringUtils.toInt(mapParams.get("sp")) : 0);
					params.add(StringUtils.notEmpty(mapParams.get("ev")) ? String.valueOf(mapParams.get("ev")) : "0");
					params.add(StringUtils.notEmpty(mapParams.get("se")) ? String.valueOf(mapParams.get("se")) : "0");
					params.add(StringUtils.notEmpty(mapParams.get("se2")) ? String.valueOf(mapParams.get("se2")) : "0");
					params.add(ResourceUtils.getLocaleParam(lang));
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_OLYMPICS_INDIVIDUAL, params, getUser(request), lang);
					html.append(HtmlConverter.convertOlympicMedals(request, DatabaseManager.callFunction("get_olympic_medals", params, OlympicMedalsBean.class), lang));
				}
				else if (type.equals(TYPE_COUNTRY)) {
					params.add(StringUtils.notEmpty(mapParams.get("ol")) ? String.valueOf(mapParams.get("ol")) : "0");
					params.add(StringUtils.notEmpty(mapParams.get("cn")) ? String.valueOf(mapParams.get("cn")) : "0");
					params.add(ResourceUtils.getLocaleParam(lang));
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_OLYMPICS_COUNTRY, params, getUser(request), lang);
					html.append(HtmlConverter.convertOlympicRankings(request, DatabaseManager.callFunction("get_olympic_rankings", params, OlympicRankingsBean.class), lang));
				}
				
				// Load HTML results or export
				HtmlUtils.setHeadInfo(request, html.toString());
				if (mapParams.containsKey("export"))
					ExportUtils.export(response, html, String.valueOf(mapParams.get("export")), lang);
				else {
					request.setAttribute("menu", "olympics");
					ServletHelper.writePageHtml(request, response, html, lang, mapParams.containsKey("print"));
				}
			}
			else if (mapParams.containsKey("tree")) { // Tree of sports/events
				String ol = String.valueOf(mapParams.get("ol"));
				String type = String.valueOf(mapParams.get("type"));
				List<Object> params = new ArrayList<Object>();
				params.add("WHERE CP.id=1 AND SP.type=" + (type.equals(TYPE_SUMMER) ? 1 : 0) + (!ol.equals("0") ? " AND OL.id IN (" + ol + ")" : ""));
				params.add(ResourceUtils.getLocaleParam(lang));
				response.setCharacterEncoding("utf-8");
				HtmlConverter.convertTreeArray(DatabaseManager.callFunctionSelect("tree_results", params, TreeItem.class), response.getWriter(), false, lang);
				response.flushBuffer();
			}
			else { // Picklists
				String ol = String.valueOf(mapParams.get("ol"));
				String type = String.valueOf(mapParams.get("type"));
				Collection<PicklistItem> items = new ArrayList<PicklistItem>();
				String plId = null;
				if (mapParams.containsKey(PICKLIST_ID_COUNTRY)) {
					String sql = "SELECT DISTINCT CN.id, CN.label" + ResourceUtils.getLocaleParam(lang) + " FROM olympic_ranking OR_"
						+ " LEFT JOIN country CN ON OR_.id_country = CN.id"
						+ (!ol.equals("0") ? " WHERE OR_.id_olympics IN (" + ol + ")" : "")
						+ " ORDER BY CN.label" + ResourceUtils.getLocaleParam(lang);
					items.add(new PicklistItem(0, "--- " + ResourceUtils.getText("all.countries", lang) + " ---"));
					items.addAll(DatabaseManager.getPicklist(sql, null));
					plId = type + "-" + PICKLIST_ID_COUNTRY;
				}
				else {
					String label = "label" + ResourceUtils.getLocaleParam(lang);
					String sql = "SELECT OL.id, YR.label || ' - ' || CT." + label
						+ " FROM olympics OL JOIN year YR ON YR.id = OL.id_year JOIN city CT ON CT.id = OL.id_city "
						+ " WHERE OL.type = " + (type.equals(TYPE_SUMMER) ? 1 : 0) + " ORDER BY YR.id desc";
					items.add(new PicklistItem(0, "--- " + ResourceUtils.getText("all.olympic.games", lang) + " ---"));
					items.addAll(DatabaseManager.getPicklist(sql, null));
					plId = type + "-" + PICKLIST_ID_OLYMPICS;
				}
				ServletHelper.writePicklist(response, items, plId);
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}

}