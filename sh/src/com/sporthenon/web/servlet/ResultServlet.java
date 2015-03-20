package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.PicklistBean;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Result;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.HtmlConverter;

public class ResultServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String PICKLIST_ID_YEAR = "pl-yr";

	public ResultServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			if (hParams.containsKey("run")) { // View results
				boolean isLink = false;
				if (hParams.containsKey("p") && !hParams.containsKey("redirect")) {
					String p = String.valueOf(hParams.get("p"));
					p = StringUtils.decode(p);
					String[] t = p.split("\\-");
					hParams.put("sp", t[0]);
					hParams.put("cp", t.length > 1 ? t[1] : "0");
					hParams.put("ev", t.length > 2 ? t[2] : "0");
					hParams.put("se", t.length > 3 ? t[3] : "0");
					hParams.put("se2", t.length > 4 ? t[4] : "0");
					hParams.put("yr", t.length > 5 ? t[5] : "0");
					isLink = true;
				}
				ArrayList<Object> lFuncParams = new ArrayList<Object>();
				lFuncParams.add(StringUtils.notEmpty(hParams.get("sp")) ? new Integer(String.valueOf(hParams.get("sp"))) : 0);
				lFuncParams.add(StringUtils.notEmpty(hParams.get("cp")) ? new Integer(String.valueOf(hParams.get("cp"))) : 0);
				lFuncParams.add(StringUtils.notEmpty(hParams.get("ev")) ? new Integer(String.valueOf(hParams.get("ev"))) : 0);
				lFuncParams.add(StringUtils.notEmpty(hParams.get("se")) ? new Integer(String.valueOf(hParams.get("se"))) : 0);
				lFuncParams.add(StringUtils.notEmpty(hParams.get("se2")) ? new Integer(String.valueOf(hParams.get("se2"))) : 0);
				lFuncParams.add(StringUtils.notEmpty(hParams.get("yr")) ? String.valueOf(hParams.get("yr")) : "0");
				lFuncParams.add("_" + getLocale(request));
				Championship oCp = (Championship) DatabaseHelper.loadEntity(Championship.class, new Integer(String.valueOf(lFuncParams.get(1))));
				Event oEv = (Event) DatabaseHelper.loadEntity(Event.class, new Integer(String.valueOf(lFuncParams.get(3)).equals("0") ? String.valueOf(lFuncParams.get(2)) : String.valueOf(lFuncParams.get(3))));
				StringBuffer html = new StringBuffer();
				html.append(HtmlConverter.getHeader(HtmlConverter.HEADER_RESULTS, lFuncParams, getUser(request), getLocale(request)));
				html.append(HtmlConverter.convertResults(DatabaseHelper.call("GetResults", lFuncParams), oCp, oEv, getUser(request), getLocale(request)));
				if (isLink) {
					HtmlUtils.setTitle(request, html.toString());
					if (hParams.containsKey("export"))
						ExportUtils.export(response, html, String.valueOf(hParams.get("export")));
					else {
						request.setAttribute("menu", "results");
						ServletHelper.writePageHtml(request, response, html, hParams.containsKey("print"));
					}
				}
				else
					ServletHelper.writeTabHtml(response, html.append(isLink ? "</div>" : ""), getLocale(request));
			}
			else { // Picklists
				Collection<PicklistBean> cPicklist = new ArrayList<PicklistBean>();
				String plId = null;
				String filter = null;
				boolean isEv = StringUtils.notEmpty(hParams.get("ev"));
				boolean isSe = StringUtils.notEmpty(hParams.get("se"));
				boolean isSe2 = StringUtils.notEmpty(hParams.get("se2"));
				if (hParams.containsKey(PICKLIST_ID_YEAR)) {
					filter = "sport.id=" + hParams.get("sp");
					filter += " and championship.id=" + hParams.get("cp");
					if (isEv || isSe) {
						if (isEv)
							filter += " and event.id=" + hParams.get("ev");
						if (isSe)
							filter += " and subevent.id=" + hParams.get("se");
						if (isSe2)
							filter += " and subevent2.id=" + hParams.get("se2");
						cPicklist.add(new PicklistBean(0, "---&nbsp;" + ResourceUtils.getText("all.years", getLocale(request)) + "&nbsp;---"));
						cPicklist.addAll(DatabaseHelper.getPicklist(Result.class, "year", filter, null, (short) 1, getLocale(request)));
					}
					plId = PICKLIST_ID_YEAR;
				}
				for (PicklistBean plb : cPicklist)
					plb.setText(plb.getText().replaceAll("^false\\-", "").replaceAll("^true\\-", "&dagger;&nbsp;"));
				ServletHelper.writePicklist(response, cPicklist, plId);
			}
		}
		catch (Exception e) {
			handleException(e);
		}
	}

}
