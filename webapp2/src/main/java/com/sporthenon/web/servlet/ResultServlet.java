package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.PicklistItem;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.meta.FolderHistory;
import com.sporthenon.db.function.ResultsBean;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.HtmlConverter;
import com.sporthenon.web.ServletHelper;

@WebServlet(
    name = "ResultServlet",
    urlPatterns = {"/ResultServlet", "/SearchResults", "/BrowseResults"}
)
public class ResultServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String PICKLIST_ID_YEAR = "pl-yr";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			HashMap<String, Object> mapParams = ServletHelper.getParams(request);
			String lang = getLocale(request);
			if (request.getRequestURI().matches(".*\\/(Search|Browse)Results.*")) {
				if (mapParams.containsKey("p") && !mapParams.containsKey("redirect")) {
					String p = String.valueOf(mapParams.get("p"));
					p = StringUtils.decode(p);
					String[] t = p.split("\\-");
					mapParams.put("sp", t[0]);
					mapParams.put("cp", t.length > 1 ? t[1] : "0");
					mapParams.put("ev", t.length > 2 ? t[2] : "0");
					mapParams.put("se", t.length > 3 ? t[3] : "0");
					mapParams.put("se2", t.length > 4 ? t[4] : "0");
					mapParams.put("yr", t.length > 5 ? t[5] : "0");
				}
				List<Object> params = new ArrayList<Object>();
				params.add(StringUtils.notEmpty(mapParams.get("sp")) ? StringUtils.toInt(mapParams.get("sp")) : 0);
				params.add(StringUtils.notEmpty(mapParams.get("cp")) ? StringUtils.toInt(mapParams.get("cp")) : 0);
				params.add(StringUtils.notEmpty(mapParams.get("ev")) ? StringUtils.toInt(mapParams.get("ev")) : 0);
				params.add(StringUtils.notEmpty(mapParams.get("se")) ? StringUtils.toInt(mapParams.get("se")) : 0);
				params.add(StringUtils.notEmpty(mapParams.get("se2")) ? StringUtils.toInt(mapParams.get("se2")) : 0);
				params.add(StringUtils.notEmpty(mapParams.get("yr")) ? String.valueOf(mapParams.get("yr")) : "0");
				params.add(0);
				params.add("_" + lang);
				Collection<Result> c = (Collection<Result>) DatabaseManager.callFunction("get_results", params, ResultsBean.class);
				boolean isRedirect = false;
				if (c == null || c.isEmpty()) { // Check in folders history
					String sql = "SELECT * FROM _folder_history WHERE previous_params = ? ORDER BY id";
					String previousParams = params.get(0) + "-" + params.get(1) + "-" + params.get(2) + (!params.get(3).equals(0) ? "-" + params.get(3) : "") + (!params.get(4).equals(0) ? "-" + params.get(4) : "");
					FolderHistory fh = (FolderHistory) DatabaseManager.loadEntity(sql, Arrays.asList(previousParams), FolderHistory.class);
					if (fh != null) {
						isRedirect = true;
						redirect(request, response, "/results/" + StringUtils.urlEscape(fh.getCurrentPath()) + "/" + StringUtils.encode(fh.getCurrentParams()), false);
					}
				}
				if (!isRedirect) {
					Championship oCp = (Championship) DatabaseManager.loadEntity(Championship.class, params.get(1));
					Event oEv = (Event) DatabaseManager.loadEntity(Event.class, !String.valueOf(params.get(4)).equals("0") ? String.valueOf(params.get(4)) : (!String.valueOf(params.get(3)).equals("0") ? String.valueOf(params.get(3)) : String.valueOf(params.get(2))));
					StringBuffer html = new StringBuffer();
					html.append(HtmlConverter.getHeader(request, HtmlConverter.HEADER_RESULTS, params, getUser(request), lang));
					html.append(HtmlConverter.convertResults(request, c, oCp, oEv, getUser(request), lang));

					// Load HTML results or export
					HtmlUtils.setHeadInfo(request, html.toString());
					if (mapParams.containsKey("export")) {
						ExportUtils.export(response, html, String.valueOf(mapParams.get("export")), lang);
					}
					else if (request.getRequestURI().contains("/SearchResults")) {
						ServletHelper.writePageHtml(request, response, html, lang, mapParams.containsKey("print"));
					}
					else if (request.getRequestURI().contains("/BrowseResults")) {
						ServletHelper.writeHtmlResponse(request, response, html, lang);
					}
				}
			}
			else { // Picklists
				Collection<PicklistItem> cPicklist = new ArrayList<PicklistItem>();
				String plId = null;
				boolean isEv = StringUtils.notEmpty(mapParams.get("ev"));
				boolean isSe = StringUtils.notEmpty(mapParams.get("se"));
				boolean isSe2 = StringUtils.notEmpty(mapParams.get("se2"));
				if (mapParams.containsKey(PICKLIST_ID_YEAR)) {
					StringBuilder filter = new StringBuilder("id_sport = " + mapParams.get("sp"));
					filter.append(" AND id_championship = " + mapParams.get("cp"));
					if (isEv || isSe) {
						if (isEv)
							filter.append(" AND id_event = " + mapParams.get("ev"));
						if (isSe)
							filter.append(" and id_subevent = " + mapParams.get("se"));
						if (isSe2)
							filter.append(" and id_subevent2 = " + mapParams.get("se2"));

						String sql = "SELECT YR.id, YR.label "
								+ " FROM year YR "
								+ " WHERE YR.id IN (SELECT id_year FROM result WHERE " + filter.toString() + ") "
								+ " ORDER by YR.id";
						cPicklist.add(new PicklistItem(0, "--- " + ResourceUtils.getText("all.years", lang) + " ---"));
						cPicklist.addAll(DatabaseManager.getPicklist(sql, null));
					}
					plId = PICKLIST_ID_YEAR;
				}
				ServletHelper.writePicklist(response, cPicklist, plId);
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}

}