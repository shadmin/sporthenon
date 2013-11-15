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
import com.sporthenon.db.converter.HtmlConverter;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Result;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.web.RenderOptions;

public class ResultServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String PICKLIST_ID_SPORT = "pl-sp";
	private static final String PICKLIST_ID_CHAMPIONSHIP = "pl-cp";
	private static final String PICKLIST_ID_EVENT = "pl-ev";
	private static final String PICKLIST_ID_SUBEVENT = "pl-se";
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
				if (hParams.containsKey("p")) {
					String[] t = String.valueOf(hParams.get("p")).split("\\-");
					hParams.put("sp", t[0]);
					hParams.put("cp", t[1]);
					hParams.put("ev", t[2]);
					hParams.put("se", t[3]);
					hParams.put("yr", t[4]);
					isLink = true;
				}
				ArrayList<Object> lFuncParams = new ArrayList<Object>();
				lFuncParams.add(StringUtils.notEmpty(hParams.get("sp")) ? new Integer(String.valueOf(hParams.get("sp"))) : 0);
				lFuncParams.add(StringUtils.notEmpty(hParams.get("cp")) ? new Integer(String.valueOf(hParams.get("cp"))) : 0);
				lFuncParams.add(StringUtils.notEmpty(hParams.get("ev")) ? new Integer(String.valueOf(hParams.get("ev"))) : 0);
				lFuncParams.add(StringUtils.notEmpty(hParams.get("se")) ? new Integer(String.valueOf(hParams.get("se"))) : 0);
				lFuncParams.add(StringUtils.notEmpty(hParams.get("yr")) ? String.valueOf(hParams.get("yr")) : "0");
				Championship oCp = (Championship) DatabaseHelper.loadEntity(Championship.class, new Integer(String.valueOf(lFuncParams.get(1))));
				Event oEv = (Event) DatabaseHelper.loadEntity(Event.class, new Integer(String.valueOf(lFuncParams.get(3)).equals("0") ? String.valueOf(lFuncParams.get(2)) : String.valueOf(lFuncParams.get(3))));
				RenderOptions opts = ServletHelper.buildOptions(hParams);
				StringBuffer html = new StringBuffer();
				html.append(HtmlConverter.getHeader(HtmlConverter.HEADER_RESULTS, lFuncParams, opts));
				html.append(HtmlConverter.convertResults(DatabaseHelper.call("GetResults", lFuncParams), oCp, oEv, opts));
				if (isLink) {
					if (hParams.containsKey("export"))
						ExportUtils.export(response, html, String.valueOf(hParams.get("export")));
					else
						ServletHelper.writeLinkHtml(request, response, html);
				}
				else
					ServletHelper.writeHtml(response, html.append(isLink ? "</div>" : ""));
			}
			else { // Picklists
				Collection<PicklistBean> cPicklist = new ArrayList<PicklistBean>();
				String plId = null;
				String filter = null;
				boolean isSp = StringUtils.notEmpty(hParams.get("sp"));
				boolean isCp = StringUtils.notEmpty(hParams.get("cp"));
				boolean isEv = StringUtils.notEmpty(hParams.get("ev"));
				boolean isSe = StringUtils.notEmpty(hParams.get("se"));
				if (hParams.containsKey(PICKLIST_ID_SPORT)) {
					cPicklist.addAll(DatabaseHelper.getPicklist(Result.class, "sport", null, null, 2));
					plId = PICKLIST_ID_SPORT;
				}
				else if (hParams.containsKey(PICKLIST_ID_CHAMPIONSHIP)) {
					filter = "sport.id=" + hParams.get("sp");
					if (isSp)
						cPicklist.addAll(DatabaseHelper.getPicklist(Result.class, "championship", filter, "x.championship.inactive || '-'", "x.championship.inactive, x.championship.index, x.championship.label"));
					plId = PICKLIST_ID_CHAMPIONSHIP;
				}
				else if (hParams.containsKey(PICKLIST_ID_EVENT)) {
					filter = "sport.id=" + hParams.get("sp");
					filter += " and championship.id=" + hParams.get("cp");
					if (isSp && isCp)
						cPicklist.addAll(DatabaseHelper.getPicklist(Result.class, "event", filter, "x.event.inactive || '-'", "x.event.inactive, x.event.index, x.event.label"));
					plId = PICKLIST_ID_EVENT;
				}
				else if (hParams.containsKey(PICKLIST_ID_SUBEVENT)) {
					filter = "sport.id=" + hParams.get("sp");
					filter += " and championship.id=" + hParams.get("cp");
					filter += " and event.id=" + hParams.get("ev");
					if (isSp && isCp && isEv)
						cPicklist.addAll(DatabaseHelper.getPicklist(Result.class, "subevent", filter, "x.subevent.inactive || '-'", "x.subevent.inactive, x.subevent.index, x.subevent.label"));
					plId = PICKLIST_ID_SUBEVENT;
				}
				else if (hParams.containsKey(PICKLIST_ID_YEAR)) {
					filter = "sport.id=" + hParams.get("sp");
					filter += " and championship.id=" + hParams.get("cp");
					if (isEv || isSe) {
						if (isEv)
							filter += " and event.id=" + hParams.get("ev");
						if (isSe)
							filter += " and subevent=" + hParams.get("se");
						cPicklist.add(new PicklistBean(0, "---&nbsp;All Years&nbsp;---"));
						cPicklist.addAll(DatabaseHelper.getPicklist(Result.class, "year", filter, null, (short) 1));	
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
