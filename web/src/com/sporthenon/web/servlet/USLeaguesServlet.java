package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.web.HtmlConverter;
import com.sporthenon.web.ServletHelper;

public class USLeaguesServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	private static final short LEAGUE_NFL = 1;
	private static final short LEAGUE_NBA = 2;
	private static final short LEAGUE_NHL = 3;
	private static final short LEAGUE_MLB = 4;
	public static final short CHAMPIONSHIP_NFL = 51;
	public static final short CHAMPIONSHIP_NBA = 54;
	public static final short CHAMPIONSHIP_NHL = 55;
	public static final short CHAMPIONSHIP_MLB = 56;
	public static HashMap<Short, Short> HLEAGUES = new HashMap<Short, Short>();
	public static HashMap<String, String> HTYPE1 = new HashMap<String, String>();
	public static HashMap<String, String> HTYPE2 = new HashMap<String, String>();
	
	public static final String TYPE_RETNUM = "retnum";
	public static final String TYPE_TEAMSTADIUM = "teamstadiums";
	public static final String TYPE_STATS = "stats";
	public static final String TYPE_HOF = "hof";
	public static final String TYPE_CHAMPIONSHIP = "championships";
	public static final String TYPE_RECORD = "records";
	
	static {
		HLEAGUES.put(LEAGUE_NFL, CHAMPIONSHIP_NFL);
		HLEAGUES.put(LEAGUE_NBA, CHAMPIONSHIP_NBA);
		HLEAGUES.put(LEAGUE_NHL, CHAMPIONSHIP_NHL);
		HLEAGUES.put(LEAGUE_MLB, CHAMPIONSHIP_MLB);
		HTYPE1.put("i", "'Individual'");
		HTYPE1.put("t", "'Team'");
		HTYPE1.put("it", "'Individual', 'Team'");
		HTYPE2.put("0", "'Alltime/Career'");
		HTYPE2.put("1", "'Season'");
		HTYPE2.put("2", "'Series'");
		HTYPE2.put("3", "'Game'");
		HTYPE2.put("-", "'Alltime/Career', 'Season', 'Series', 'Game'");
	}

	public USLeaguesServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			String lang = getLocale(request);
			String league = String.valueOf(hParams.get("league"));
			if (hParams.containsKey("run")) { // View results
				boolean isLink = false;
				if (hParams.containsKey("p")) {
					String p = String.valueOf(hParams.get("p"));
					p = StringUtils.decode(p);
					String[] t = p.split("\\-");
					hParams.put("type", t[0]);
					league = t[1];
					if (t[0].equals(TYPE_RETNUM)) {
						hParams.put("tm", t[2]);
						hParams.put("num", t[3]);
					}
					else if (t[0].equals(TYPE_TEAMSTADIUM)) {
						hParams.put("tm", t[2]);
					}
					else if (t[0].equals(TYPE_STATS)) {
						hParams.put("yr", t[2]);
						hParams.put("ct", t[3]);
						hParams.put("ind", t[4]);
						hParams.put("tm", t[5]);
					}
					else if (t[0].equals(TYPE_HOF)) {
						hParams.put("yr", t[2]);
					}
					else if (t[0].equals(TYPE_CHAMPIONSHIP)) {
						hParams.put("yr", t[3]);
					}
					else if (t[0].equals(TYPE_RECORD)) {
						hParams.put("pf", t[3]);
						hParams.put("ct", t[4]);
						hParams.put("tp1", t[5]);
						hParams.put("tp2", t.length > 6 ? t[6] : "");
					}
					isLink = true;
				}
				ArrayList<Object> lFuncParams = new ArrayList<Object>();
				lFuncParams.add(new Integer(league));
				String type = String.valueOf(hParams.get("type"));
				String teams = StringUtils.notEmpty(hParams.get("tm")) ? String.valueOf(hParams.get("tm")) : "0";
				String years = StringUtils.notEmpty(hParams.get("yr")) ? String.valueOf(hParams.get("yr")) : "0";
				StringBuffer html = null;
				if (type.equals(TYPE_RETNUM)) {
					lFuncParams.add(teams);
					lFuncParams.add(StringUtils.notEmpty(hParams.get("num")) ? new Short(String.valueOf(hParams.get("num"))) : -1);
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_US_LEAGUES_RETNUM, lFuncParams, getUser(request), lang);
					html.append(HtmlConverter.convertRetiredNumber(DatabaseHelper.call("GetRetiredNumbers", lFuncParams), "en"));
				}
				else if (type.equals(TYPE_TEAMSTADIUM)) {
					lFuncParams.add(teams);
					lFuncParams.add("_en");
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_US_LEAGUES_TEAMSTADIUM, lFuncParams, getUser(request), lang);
					html.append(HtmlConverter.convertTeamStadium(DatabaseHelper.call("GetTeamStadiums", lFuncParams), "en"));
				}
				else if (type.equals(TYPE_STATS)) {
					String categories = StringUtils.notEmpty(hParams.get("ct")) ? String.valueOf(hParams.get("ct")) : "0";
					lFuncParams.add(HLEAGUES.get(Short.valueOf(league)));
					lFuncParams.add(years);
					lFuncParams.add(categories);
					lFuncParams.add(String.valueOf(hParams.get("tpind")).equals("1") ? true : false);
					lFuncParams.add(String.valueOf(hParams.get("tptm")).equals("1") ? true : false);
					lFuncParams.add("_en");
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_US_LEAGUES_STATS, lFuncParams, getUser(request), lang);
					lFuncParams.remove(0);
					html.append(HtmlConverter.convertYearlyStats(DatabaseHelper.call("GetYearlyStats", lFuncParams), "en"));
				}
				else if (type.equals(TYPE_HOF)) {
					lFuncParams.add(years);
					lFuncParams.add(StringUtils.notEmpty(hParams.get("pos")) ? String.valueOf(hParams.get("pos")) : "");
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_US_LEAGUES_HOF, lFuncParams, getUser(request), lang);
					html.append(HtmlConverter.convertHallOfFame(DatabaseHelper.call("GetHallOfFame", lFuncParams), "en"));
				}
				else if (type.equals(TYPE_CHAMPIONSHIP)) {
					lFuncParams.add(HLEAGUES.get(Short.valueOf(league)));
					lFuncParams.add(years);
					lFuncParams.add("_en");
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_US_LEAGUES_CHAMPIONSHIP, lFuncParams, getUser(request), lang);
					lFuncParams.remove(0);
					html.append(HtmlConverter.convertUSChampionships(DatabaseHelper.call("GetUSChampionships", lFuncParams), "en"));
				}
				else if (type.equals(TYPE_RECORD)) {
					lFuncParams.add(HLEAGUES.get(Short.valueOf(league)));
					lFuncParams.add(String.valueOf(hParams.get("pf")).equals("1") ? "0" : "495");
					lFuncParams.add(StringUtils.notEmpty(hParams.get("ct")) ? String.valueOf(hParams.get("ct")) : "0");
					lFuncParams.add(StringUtils.notEmpty(hParams.get("tp1")) ? String.valueOf(hParams.get("tp1")) : "i");
					lFuncParams.add(StringUtils.notEmpty(hParams.get("tp2")) ? String.valueOf(hParams.get("tp2")) : "-");
					lFuncParams.add("_en");
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_US_LEAGUES_RECORD, lFuncParams, getUser(request), lang);
					lFuncParams.set(4, HTYPE1.get(lFuncParams.get(4)));
					lFuncParams.set(5, HTYPE2.get(lFuncParams.get(5)));
					lFuncParams.remove(0);
					if (String.valueOf(lFuncParams.get(3)).matches(".*Team.*") && !String.valueOf(lFuncParams.get(2)).equals("0")) {
						String hql = "select id from Event where type.number<=50 and label in (select label from Event where id in (" + String.valueOf(hParams.get("ct")) + "))";
						ArrayList<String> lstSe = new ArrayList<String>();
						for (Integer i : (ArrayList<Integer>) DatabaseHelper.execute(hql))
							lstSe.add(String.valueOf(i));
						lFuncParams.set(2, StringUtils.join(lstSe , ","));
					}
					html.append(HtmlConverter.convertUSRecords(DatabaseHelper.call("GetUSRecords", lFuncParams), "en"));
				}
				if (isLink) {
					HtmlUtils.setHeadInfo(request, html.toString());
					if (hParams.containsKey("export"))
						ExportUtils.export(response, html, String.valueOf(hParams.get("export")), lang);
					else {
						request.setAttribute("menu", "usleagues");
						ServletHelper.writePageHtml(request, response, html, lang, hParams.containsKey("print"));
					}
				}
				else
					ServletHelper.writeTabHtml(request, response, html, lang);
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}

}