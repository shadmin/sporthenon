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
import com.sporthenon.db.entity.HallOfFame;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.RetiredNumber;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.entity.WinLoss;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class USLeaguesServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	private static final short LEAGUE_NFL = 1;
	private static final short LEAGUE_NBA = 2;
	private static final short LEAGUE_NHL = 3;
	private static final short LEAGUE_MLB = 4;
	private static final short CHAMPIONSHIP_NFL = 51;
	private static final short CHAMPIONSHIP_NBA = 54;
	private static final short CHAMPIONSHIP_NHL = 55;
	private static final short CHAMPIONSHIP_MLB = 56;
	
	public static final String TYPE_RETNUM = "retnum";
	public static final String TYPE_TEAMSTADIUM = "teamstadium";
	public static final String TYPE_WINLOSS = "winloss";
	public static final String TYPE_HOF = "hof";
	public static final String TYPE_CHAMPIONSHIP = "championship";
	public static final String TYPE_RECORD = "record";
	
	private static final String PICKLIST_ID_RETNUM_TEAM = "pl-retnum-tm";
	private static final String PICKLIST_ID_TEAMSTADIUM_TEAM = "pl-teamstadium-tm";
	private static final String PICKLIST_ID_WINLOSS_TEAM = "pl-winloss-tm";
	private static final String PICKLIST_ID_HOF_YEAR = "pl-hof-yr";
	private static final String PICKLIST_ID_CHAMPIONSHIP_YEAR = "pl-championship-yr";
	private static final String PICKLIST_ID_RECORD_EVENT = "pl-record-ev";
	private static final String PICKLIST_ID_RECORD_SUBEVENT = "pl-record-se";

	public USLeaguesServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			HashMap<Short, Short> hLeagues = new HashMap<Short, Short>();
			hLeagues.put(LEAGUE_NFL, CHAMPIONSHIP_NFL);
			hLeagues.put(LEAGUE_NBA, CHAMPIONSHIP_NBA);
			hLeagues.put(LEAGUE_NHL, CHAMPIONSHIP_NHL);
			hLeagues.put(LEAGUE_MLB, CHAMPIONSHIP_MLB);
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
					else if (t[0].equals(TYPE_WINLOSS)) {
						hParams.put("tm", t[2]);
					}
					else if (t[0].equals(TYPE_HOF)) {
						hParams.put("yr", t[2]);
					}
					else if (t[0].equals(TYPE_CHAMPIONSHIP)) {
						hParams.put("yr", t[3]);
					}
					else if (t[0].equals(TYPE_RECORD)) {
						hParams.put("ev", t[2]);
						hParams.put("se", t[3]);
						hParams.put("tp1", t[4]);
						hParams.put("tp2", t[5]);
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
					html = HtmlConverter.getHeader(HtmlConverter.HEADER_US_LEAGUES_RETNUM, lFuncParams, "en");
					html.append(HtmlConverter.convertRetiredNumber(DatabaseHelper.call("GetRetiredNumber", lFuncParams), "en"));
				}
				else if (type.equals(TYPE_TEAMSTADIUM)) {
					lFuncParams.add(teams);
					lFuncParams.add("_" + "en");
					html = HtmlConverter.getHeader(HtmlConverter.HEADER_US_LEAGUES_TEAMSTADIUM, lFuncParams, "en");
					html.append(HtmlConverter.convertTeamStadium(DatabaseHelper.call("GetTeamStadium", lFuncParams), "en"));
				}
				else if (type.equals(TYPE_WINLOSS)) {
					lFuncParams.add(teams);
					html = HtmlConverter.getHeader(HtmlConverter.HEADER_US_LEAGUES_WINLOSS, lFuncParams, "en");
					html.append(HtmlConverter.convertWinLoss(DatabaseHelper.call("GetWinLoss", lFuncParams), "en"));
				}
				else if (type.equals(TYPE_HOF)) {
					lFuncParams.add(years);
					lFuncParams.add(StringUtils.notEmpty(hParams.get("pos")) ? String.valueOf(hParams.get("pos")) : "");
					html = HtmlConverter.getHeader(HtmlConverter.HEADER_US_LEAGUES_HOF, lFuncParams, "en");
					html.append(HtmlConverter.convertHallOfFame(DatabaseHelper.call("GetHallOfFame", lFuncParams), "en"));
				}
				else if (type.equals(TYPE_CHAMPIONSHIP)) {
					lFuncParams.add(hLeagues.get(Short.valueOf(league)));
					lFuncParams.add(years);
					lFuncParams.add("_" + "en");
					html = HtmlConverter.getHeader(HtmlConverter.HEADER_US_LEAGUES_CHAMPIONSHIP, lFuncParams, "en");
					lFuncParams.remove(0);
					html.append(HtmlConverter.convertUSChampionships(DatabaseHelper.call("GetUSChampionships", lFuncParams), "en"));
				}
				else if (type.equals(TYPE_RECORD)) {
					lFuncParams.add(hLeagues.get(Short.valueOf(league)));
					lFuncParams.add(String.valueOf(hParams.get("pf")).equals("1") ? "0" : "495");
					lFuncParams.add(StringUtils.notEmpty(hParams.get("se")) ? String.valueOf(hParams.get("se")) : "0");
					lFuncParams.add(StringUtils.notEmpty(hParams.get("tp1")) ? String.valueOf(hParams.get("tp1")) : "'Individual'");
					lFuncParams.add(StringUtils.notEmpty(hParams.get("tp2")) ? String.valueOf(hParams.get("tp2")) : "'Career'");
					lFuncParams.add("_" + "en");
					html = HtmlConverter.getHeader(HtmlConverter.HEADER_US_LEAGUES_RECORD, lFuncParams, "en");
					lFuncParams.remove(0);
					if (String.valueOf(lFuncParams.get(3)).matches(".*Team.*") && !String.valueOf(lFuncParams.get(2)).equals("0")) {
						String hql = "select id from Event where type.number<=50 and label in (select label from Event where id in (" + String.valueOf(hParams.get("se")) + "))";
						ArrayList<String> lstSe = new ArrayList<String>();
						for (Integer i : (ArrayList<Integer>) DatabaseHelper.execute(hql))
							lstSe.add(String.valueOf(i));
						lFuncParams.set(2, StringUtils.implode(lstSe , ","));
					}
					html.append(HtmlConverter.convertUSRecords(DatabaseHelper.call("GetUSRecords", lFuncParams), "en"));
				}
				if (isLink) {
					if (hParams.containsKey("export"))
						ExportUtils.export(response, html, String.valueOf(hParams.get("export")));
					else
						ServletHelper.writePageHtml(request, response, html);
				}
				else
					ServletHelper.writeTabHtml(response, html, "en");
			}
			else { // Picklists
				String plId = null;
				Collection<PicklistBean> cPicklist = new ArrayList<PicklistBean>();
				if (hParams.containsKey(PICKLIST_ID_HOF_YEAR)) {
					cPicklist.add(new PicklistBean(0, "---&nbsp;" + ResourceUtils.getText("all.years", "en") + "&nbsp;---"));
					cPicklist.addAll(DatabaseHelper.getPicklist(HallOfFame.class, "year", "league.id=" + league, null, (short)1, "en"));
					plId = PICKLIST_ID_HOF_YEAR;
				}
				else if (hParams.containsKey(PICKLIST_ID_CHAMPIONSHIP_YEAR)) {
					cPicklist.add(new PicklistBean(0, "---&nbsp;" + ResourceUtils.getText("all.years", "en") + "&nbsp;---"));
					cPicklist.addAll(DatabaseHelper.getPicklist(Result.class, "year", "championship.id=" + hLeagues.get(Short.valueOf(league)), null, (short)1, "en"));
					plId = PICKLIST_ID_CHAMPIONSHIP_YEAR;
				}
				else if (hParams.containsKey(PICKLIST_ID_RETNUM_TEAM) || hParams.containsKey(PICKLIST_ID_TEAMSTADIUM_TEAM) || hParams.containsKey(PICKLIST_ID_WINLOSS_TEAM)) {
					boolean isRetnum = hParams.containsKey(PICKLIST_ID_RETNUM_TEAM);
					boolean isTeamStadium = hParams.containsKey(PICKLIST_ID_TEAMSTADIUM_TEAM);
					cPicklist.add(new PicklistBean(0, "---&nbsp;" + ResourceUtils.getText("all.teams", "en") + "&nbsp;---"));
					cPicklist.addAll(DatabaseHelper.getPicklist((isRetnum ? RetiredNumber.class : (isTeamStadium ? TeamStadium.class : WinLoss.class)), "team", "league.id=" + league, "x.team.inactive || '-'", "x.team.inactive, x.team.label", "en"));
					for (PicklistBean plb : cPicklist)
						plb.setText(plb.getText().replaceAll("^false\\-", "").replaceAll("^true\\-", "&dagger;&nbsp;"));
					plId = (isRetnum ? PICKLIST_ID_RETNUM_TEAM : (isTeamStadium ? PICKLIST_ID_TEAMSTADIUM_TEAM : PICKLIST_ID_WINLOSS_TEAM));
				}
				else if (hParams.containsKey(PICKLIST_ID_RECORD_EVENT)) {
					cPicklist.addAll(DatabaseHelper.getPicklist(Record.class, "event", "championship.id=" + hLeagues.get(Short.valueOf(league)), null, "x.event.index, x.event.label", "en"));
					plId = PICKLIST_ID_RECORD_EVENT;
				}
				else if (hParams.containsKey(PICKLIST_ID_RECORD_SUBEVENT)) {
					cPicklist.add(new PicklistBean(0, "---&nbsp;" + ResourceUtils.getText("all.categories", "en") + "&nbsp;---"));
					cPicklist.addAll(DatabaseHelper.getPicklist(Record.class, "subevent", "championship.id=" + hLeagues.get(Short.valueOf(league)) + " and x.type1='Individual'", null, "x.subevent.index, x.subevent.label", "en"));
					plId = PICKLIST_ID_RECORD_SUBEVENT;
				}
				ServletHelper.writePicklist(response, cPicklist, plId);
			}
		}
		catch (Exception e) {
			handleException(e);
		}
	}

}