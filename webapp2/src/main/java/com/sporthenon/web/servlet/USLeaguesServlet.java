package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.RetiredNumber;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.function.HallOfFameBean;
import com.sporthenon.db.function.USChampionshipsBean;
import com.sporthenon.db.function.USRecordsBean;
import com.sporthenon.db.function.YearlyStatsBean;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.web.HtmlConverter;
import com.sporthenon.web.ServletHelper;

@WebServlet(
    name = "USLeaguesServlet",
    urlPatterns = {"/USLeaguesServlet", "/SearchUSLeagues"}
)
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			HashMap<String, Object> mapParams = ServletHelper.getParams(request);
			String lang = getLocale(request);
			String league = String.valueOf(mapParams.get("league"));
			if (request.getRequestURI().contains("/SearchUSLeagues")) {
				if (mapParams.containsKey("p")) {
					String p = String.valueOf(mapParams.get("p"));
					p = StringUtils.decode(p);
					String[] t = p.split("\\-");
					mapParams.put("type", t[0]);
					league = t[1];
					if (t[0].equals(TYPE_RETNUM)) {
						mapParams.put("tm", t[2]);
						mapParams.put("num", t[3]);
					}
					else if (t[0].equals(TYPE_TEAMSTADIUM)) {
						mapParams.put("tm", t[2]);
					}
					else if (t[0].equals(TYPE_STATS)) {
						mapParams.put("yr", t[2]);
						mapParams.put("ct", t[3]);
						mapParams.put("tpind", t.length > 4 ? t[4] : "");
						mapParams.put("tptm", t.length > 5 ? t[5] : "");
					}
					else if (t[0].equals(TYPE_HOF)) {
						mapParams.put("yr", t[2]);
						mapParams.put("pos", t.length > 3 ? t[3] : "");
					}
					else if (t[0].equals(TYPE_CHAMPIONSHIP)) {
						mapParams.put("yr", t[3]);
					}
					else if (t[0].equals(TYPE_RECORD)) {
						mapParams.put("pf", t[3]);
						mapParams.put("ct", t[4]);
						mapParams.put("tp1", t[5]);
						mapParams.put("tp2", t.length > 6 ? t[6] : "");
					}
				}
				List<Object> params = new ArrayList<Object>();
				params.add(StringUtils.toInt(league));
				String type = String.valueOf(mapParams.get("type"));
				String teams = StringUtils.notEmpty(mapParams.get("tm")) ? String.valueOf(mapParams.get("tm")) : "0";
				String years = StringUtils.notEmpty(mapParams.get("yr")) ? String.valueOf(mapParams.get("yr")) : "0";
				StringBuffer html = null;
				if (type.equals(TYPE_RETNUM)) {
					params.add(teams);
					params.add(StringUtils.notEmpty(mapParams.get("num")) ? Short.valueOf(String.valueOf(mapParams.get("num"))) : -1);
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_US_LEAGUES_RETNUM, params, getUser(request), lang);
					html.append(HtmlConverter.convertRetiredNumber(request, DatabaseManager.callFunction("get_retired_numbers", params, RetiredNumber.class), "en"));
				}
				else if (type.equals(TYPE_TEAMSTADIUM)) {
					params.add(teams);
					params.add("_en");
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_US_LEAGUES_TEAMSTADIUM, params, getUser(request), lang);
					html.append(HtmlConverter.convertTeamStadium(request, DatabaseManager.callFunction("get_team_stadiums", params, TeamStadium.class), "en"));
				}
				else if (type.equals(TYPE_STATS)) {
					String categories = StringUtils.notEmpty(mapParams.get("ct")) ? String.valueOf(mapParams.get("ct")) : "0";
					params.add(years);
					params.add(categories);
					params.add(String.valueOf(mapParams.get("tpind")));
					params.add(String.valueOf(mapParams.get("tptm")));
					params.add("_en");
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_US_LEAGUES_STATS, params, getUser(request), lang);
					params.set(0, HLEAGUES.get(Short.valueOf(league)));
					params.set(3, params.get(3).equals("1") ? true : false);
					params.set(4, params.get(4).equals("1") ? true : false);
					html.append(HtmlConverter.convertYearlyStats(request, DatabaseManager.callFunction("get_yearly_stats", params, YearlyStatsBean.class), "en"));
				}
				else if (type.equals(TYPE_HOF)) {
					params.add(years);
					params.add(StringUtils.notEmpty(mapParams.get("pos")) ? String.valueOf(mapParams.get("pos")) : "");
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_US_LEAGUES_HOF, params, getUser(request), lang);
					html.append(HtmlConverter.convertHallOfFame(request, DatabaseManager.callFunction("get_hall_of_fame", params, HallOfFameBean.class), "en"));
				}
				else if (type.equals(TYPE_CHAMPIONSHIP)) {
					params.add(HLEAGUES.get(Short.valueOf(league)));
					params.add(years);
					params.add("_en");
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_US_LEAGUES_CHAMPIONSHIP, params, getUser(request), lang);
					params.remove(0);
					html.append(HtmlConverter.convertUSChampionships(request, DatabaseManager.callFunction("get_us_championships", params, USChampionshipsBean.class), "en"));
				}
				else if (type.equals(TYPE_RECORD)) {
					params.add(HLEAGUES.get(Short.valueOf(league)));
					params.add(String.valueOf(mapParams.get("pf")).equals("1") ? "0" : "495");
					params.add(StringUtils.notEmpty(mapParams.get("ct")) ? String.valueOf(mapParams.get("ct")) : "0");
					params.add(StringUtils.notEmpty(mapParams.get("tp1")) ? String.valueOf(mapParams.get("tp1")) : "i");
					params.add(StringUtils.notEmpty(mapParams.get("tp2")) ? String.valueOf(mapParams.get("tp2")) : "-");
					params.add("_en");
					html = HtmlConverter.getHeader(request, HtmlConverter.HEADER_US_LEAGUES_RECORD, params, getUser(request), lang);
					params.set(4, HTYPE1.get(params.get(4)));
					params.set(5, HTYPE2.get(params.get(5)));
					params.remove(0);
					if (String.valueOf(params.get(3)).matches(".*Team.*") && !String.valueOf(params.get(2)).equals("0")) {
						String sql = "SELECT id FROM event WHERE type.number <= 50 AND label IN (SELECT label FROM event WHERE id IN (" + String.valueOf(mapParams.get("ct")) + "))";
						List<String> lstSe = new ArrayList<String>();
						for (Integer i : (ArrayList<Integer>) DatabaseManager.executeSelect(sql, Integer.class))
							lstSe.add(String.valueOf(i));
						params.set(2, StringUtils.join(lstSe , ","));
					}
					html.append(HtmlConverter.convertUSRecords(request, DatabaseManager.callFunction("get_us_records", params, USRecordsBean.class), "en"));
				}
				
				// Load HTML results or export
				HtmlUtils.setHeadInfo(request, html.toString());
				if (mapParams.containsKey("export"))
					ExportUtils.export(response, html, String.valueOf(mapParams.get("export")), lang);
				else {
					request.setAttribute("menu", "usleagues");
					ServletHelper.writePageHtml(request, response, html, lang, mapParams.containsKey("print"));
				}
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}

}