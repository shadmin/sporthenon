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
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.web.RenderOptions;

public class OlympicsServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;

	private static final String TYPE_SUMMER = "summer";
	//private static final String TYPE_WINTER = "winter";
	private static final String TYPE_INDIVIDUAL = "ind";
	private static final String TYPE_COUNTRY = "cnt";

	private static final String PICKLIST_ID_OLYMPICS = "pl-ol";
	private static final String PICKLIST_ID_SPORT = "pl-sp";
	private static final String PICKLIST_ID_EVENT = "pl-ev";
	private static final String PICKLIST_ID_SUBEVENT = "pl-se";
	private static final String PICKLIST_ID_COUNTRY = "pl-cn";

	public OlympicsServlet() {
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
					hParams.put("type", t[0]);
					hParams.put("ol", t[1]);
					if (t[0].equals(TYPE_INDIVIDUAL)) {
						hParams.put("sp", t[2]);
						hParams.put("ev", t[3]);
						hParams.put("se", t[4]);
					}
					else
						hParams.put("cn", t[2]);
					isLink = true;
				}
				RenderOptions opts = ServletHelper.buildOptions(hParams);
				ArrayList<Object> lFuncParams = new ArrayList<Object>();
				String type = String.valueOf(hParams.get("type"));
				StringBuffer html = null;
				if (type.equals(TYPE_INDIVIDUAL)) {
					lFuncParams.add(StringUtils.notEmpty(hParams.get("ol")) ? String.valueOf(hParams.get("ol")) : "0");
					lFuncParams.add(StringUtils.notEmpty(hParams.get("sp")) ? new Integer(String.valueOf(hParams.get("sp"))) : 0);
					lFuncParams.add(StringUtils.notEmpty(hParams.get("ev")) ? String.valueOf(hParams.get("ev")) : "0");
					lFuncParams.add(StringUtils.notEmpty(hParams.get("se")) ? String.valueOf(hParams.get("se")) : "0");
					html = HtmlConverter.getHeader(HtmlConverter.HEADER_OLYMPICS_INDIVIDUAL, lFuncParams, opts);
					html.append(HtmlConverter.convertOlympicMedals(DatabaseHelper.call("GetOlympicMedals", lFuncParams), opts));
				}
				else if (type.equals(TYPE_COUNTRY)) {
					lFuncParams.add(StringUtils.notEmpty(hParams.get("ol")) ? String.valueOf(hParams.get("ol")) : "0");
					lFuncParams.add(StringUtils.notEmpty(hParams.get("cn")) ? String.valueOf(hParams.get("cn")) : "0");
					html = HtmlConverter.getHeader(HtmlConverter.HEADER_OLYMPICS_COUNTRY, lFuncParams, opts);
					html.append(HtmlConverter.convertOlympicRankings(DatabaseHelper.call("GetOlympicRankings", lFuncParams), opts));
				}
				if (isLink) {
					if (hParams.containsKey("export"))
						ExportUtils.export(response, html, String.valueOf(hParams.get("export")));
					else
						ServletHelper.writeLinkHtml(request, response, html);
				}
				else
					ServletHelper.writeHtml(response, html);
			}
			else { // Picklists
				String ol = String.valueOf(hParams.get("ol"));
				String sp = String.valueOf(hParams.get("sp"));
				String type = String.valueOf(hParams.get("type"));
				//boolean indiv = (StringUtils.notEmpty(hParams.get(TYPE_INDIVIDUAL)) && hParams.get(TYPE_INDIVIDUAL).equals("1"));
				Collection<PicklistBean> cPicklist = new ArrayList<PicklistBean>();
				String plId = null;
				boolean isSp = StringUtils.notEmpty(hParams.get("sp"));
				if (hParams.containsKey(PICKLIST_ID_SPORT)) {
					String sql = "SELECT DISTINCT SP.id, SP.label FROM \"RESULT\" RS";
					sql += " LEFT JOIN \"OLYMPICS\" OL ON OL.id_year = RS.id_year";
					sql += " LEFT JOIN \"SPORT\" SP ON RS.id_sport = SP.id";
					sql += " WHERE SP.type = " + (type.equals(TYPE_SUMMER) ? 1 : 0) + (!ol.equals("0") ? " AND OL.id IN (" + ol + ")" : "") + " AND RS.id_championship = 1";
					sql += " ORDER BY SP.label";
					cPicklist.addAll(DatabaseHelper.getPicklistFromQuery(sql, true));
					plId = type + "-" + PICKLIST_ID_SPORT;
				}
				else if (hParams.containsKey(PICKLIST_ID_COUNTRY)) {
					String sql = "SELECT DISTINCT CN.id, CN.label FROM \"OLYMPIC_RANKING\" OR_";
					sql += " LEFT JOIN \"COUNTRY\" CN ON OR_.id_country = CN.id";
					sql += (!ol.equals("0") ? " WHERE OR_.id_olympics IN (" + ol + ")" : "");
					sql += " ORDER BY CN.label";
					cPicklist.add(new PicklistBean(0, "---&nbsp;All Countries&nbsp;---"));
					cPicklist.addAll(DatabaseHelper.getPicklistFromQuery(sql, true));
					plId = type + "-" + PICKLIST_ID_COUNTRY;
				}
				else if (hParams.containsKey(PICKLIST_ID_EVENT) && isSp) {
					String sql = "SELECT DISTINCT EV.id, EV.label, EV.index FROM \"RESULT\" RS";
					sql += " LEFT JOIN \"OLYMPICS\" OL ON OL.id_year = RS.id_year";
					sql += " LEFT JOIN \"SPORT\" SP ON RS.id_sport = SP.id";
					sql += " LEFT JOIN \"EVENT\" EV ON RS.id_event = EV.id";
					sql += " WHERE SP.id = " + sp + (!ol.equals("0") ? " AND OL.id IN (" + ol + ")" : "") + " AND RS.id_championship = 1";
					sql += " ORDER BY EV.index";
					cPicklist.add(new PicklistBean(0, "---&nbsp;All Events&nbsp;---"));
					cPicklist.addAll(DatabaseHelper.getPicklistFromQuery(sql, true));
					plId = type + "-" + PICKLIST_ID_EVENT;
				}
				else if (hParams.containsKey(PICKLIST_ID_SUBEVENT) && isSp) {
					String sql = "SELECT DISTINCT EV.id, EV.label, EV.index FROM \"RESULT\" RS";
					sql += " LEFT JOIN \"OLYMPICS\" OL ON OL.id_year = RS.id_year";
					sql += " LEFT JOIN \"SPORT\" SP ON RS.id_sport = SP.id";
					sql += " LEFT JOIN \"EVENT\" EV ON RS.id_subevent = EV.id";
					sql += " WHERE SP.id = " + sp + (!ol.equals("0") ? " AND OL.id IN (" + ol + ")" : "") + " AND RS.id_championship = 1";
					sql += " ORDER BY EV.index";
					Collection<PicklistBean> cSubevents = DatabaseHelper.getPicklistFromQuery(sql, true);
					if (cSubevents != null && cSubevents.size() > 0) {
						cPicklist.add(new PicklistBean(0, "---&nbsp;All Events&nbsp;---"));
						cPicklist.addAll(cSubevents);
					}
					plId = type + "-" + PICKLIST_ID_SUBEVENT;
				}
				else {
					String hql = "select ol.id, concat(concat(ol.year.label, ' - '), ol.city.label) ";
					hql += " from Olympics ol where ol.type = " + (type.equals(TYPE_SUMMER) ? 1 : 0) + " order by ol.year.id desc";
					cPicklist.add(new PicklistBean(0, "---&nbsp;All Olympic Games&nbsp;---"));
					cPicklist.addAll(DatabaseHelper.getPicklistFromQuery(hql, false));
					plId = type + "-" + PICKLIST_ID_OLYMPICS;
				}
				ServletHelper.writePicklist(response, cPicklist, plId);
			}
		}
		catch (Exception e) {
			handleException(e);
		}
	}

}