package com.sporthenon.web;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.HallOfFame;
import com.sporthenon.db.entity.OlympicRanking;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.RetiredNumber;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.entity.WinLoss;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Contribution;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.entity.meta.ErrorReport;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.db.entity.meta.TreeItem;
import com.sporthenon.db.function.DrawBean;
import com.sporthenon.db.function.HallOfFameBean;
import com.sporthenon.db.function.LastUpdateBean;
import com.sporthenon.db.function.OlympicMedalsBean;
import com.sporthenon.db.function.OlympicRankingsBean;
import com.sporthenon.db.function.PersonListBean;
import com.sporthenon.db.function.ResultsBean;
import com.sporthenon.db.function.RetiredNumberBean;
import com.sporthenon.db.function.TeamStadiumBean;
import com.sporthenon.db.function.USChampionshipsBean;
import com.sporthenon.db.function.USRecordsBean;
import com.sporthenon.db.function.WinLossBean;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.servlet.USLeaguesServlet;

public class HtmlConverter {

	public static final short HEADER_RESULTS = 0;
	public static final short HEADER_OLYMPICS_INDIVIDUAL = 1;
	public static final short HEADER_OLYMPICS_COUNTRY = 2;
	public static final short HEADER_US_LEAGUES_RETNUM = 3;
	public static final short HEADER_US_LEAGUES_TEAMSTADIUM = 4;
	public static final short HEADER_US_LEAGUES_WINLOSS = 5;
	public static final short HEADER_US_LEAGUES_HOF = 6;
	public static final short HEADER_US_LEAGUES_CHAMPIONSHIP = 7;
	public static final short HEADER_US_LEAGUES_RECORD = 8;
	public static final short HEADER_SEARCH = 9;
	public static final short HEADER_REF = 10;

	public static String getResultsEntity(int type, Integer rank, String str1, String str2, String str3, String country, String year, String plist) {
		String s = null;
		if (rank != null && rank > 0) {
			String expand = "";
			if (type > 10 && plist != null)
				expand = "<img alt='+' src='/img/render/expand.gif' style='cursor:pointer;padding:3px 1px;' onclick=\"togglePlist(this, '" + plist + "');\"/>&nbsp;";
			if (type < 10)
				s = HtmlUtils.writeLink(Athlete.alias, rank, StringUtils.toFullName(str1, str2, country, true), country != null && country.matches(StringUtils.PATTERN_REVERT_NAME) ? (StringUtils.notEmpty(str1) ? str1 + " " : "") + str2 : (StringUtils.notEmpty(str2) ? str2 + " " : "") + str1);
			else if (type == 50) {
				String img = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, rank, ImageUtils.SIZE_SMALL, year, null);
				s = HtmlUtils.writeLink(Team.alias, rank, str2, null);
				s = HtmlUtils.writeImgTable(expand + img, s);
			}
			else if (type == 99) {
				String img = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, rank, ImageUtils.SIZE_SMALL, year, null);
				s = HtmlUtils.writeLink(Country.alias, rank, str2, str3);
				s = HtmlUtils.writeImgTable(expand + img, s);
			}
		}
		return s;
	}

	public static String getResultsEntityRel(Integer rel1Id, String rel1Code, String rel1Label, Integer rel2Id, String rel2Code, String rel2Label, String rel2LabelEN, boolean isRel1, boolean isRel2, String year) {
		StringBuffer html = new StringBuffer();
		String tmpImg = null;
		if (rel2Id != null && rel2Id > 0) {
			tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, rel2Id, ImageUtils.SIZE_SMALL, year, null);
			String s = HtmlUtils.writeLink(Country.alias, rel2Id, rel2Code, rel2LabelEN);
			html.append("<td>").append(HtmlUtils.writeImgTable(tmpImg, s)).append("</td>");
		}
		else if (isRel2)
			html.append("<td>" + StringUtils.EMPTY + "</td>");
		if (rel1Id != null && rel1Id > 0) {
			tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, rel1Id, ImageUtils.SIZE_SMALL, year, null);
			String s = HtmlUtils.writeLink(Team.alias, rel1Id, rel1Label, null);
			String s_ = HtmlUtils.writeImgTable(tmpImg, s);
			if (s_.indexOf("<img") == -1)
				s_ = "<table><tr><td>" + s_ + "</td></tr></table>";
			html.append("<td>").append(s_).append("</td>");
		}
		else if (isRel1)
			html.append("<td>" + StringUtils.EMPTY + "</td>");
		return html.toString();
	}
	
	public static void setTies(List<Integer> tieList, int type, String[] tEntity, String[] tEntityRel) {
		if (tieList != null && !tieList.isEmpty()) {
			Integer idx = tieList.get(0) - 1;
			for (int i = 1 ; i < tieList.size() ; i++) {
				if (idx == null)
					idx = tieList.get(i) - 1;
				else {
					if (tieList.get(i) == -1)
						idx = null;
					if (idx != null) {
						if (tEntity[idx] != null && tEntity[tieList.get(i) - 1] != null) {
							tEntity[idx] = tEntity[idx].concat((type < 10 ? "<br/>" : "") + tEntity[tieList.get(i) - 1].replaceAll("<table>", "<table class='margintop'>"));
							tEntity[tieList.get(i) - 1] = null;
						}
						if (tEntityRel[idx] != null && tEntityRel[tieList.get(i) - 1] != null) {
							tEntityRel[idx] = tEntityRel[idx].concat(tEntityRel[tieList.get(i) - 1]).replaceAll("\\<\\/td\\>\\<td\\>\\<table\\>", "<table class='margintop'>");
							tEntityRel[tieList.get(i) - 1] = null;
							if (tEntityRel[idx].matches(".*\\/4\\-\\d+\\-.*") && tEntityRel[idx].matches(".*\\/5\\-\\d+\\-.*")) {
								String[] t = tEntityRel[idx].split("</table>");
								StringBuffer sbCN = new StringBuffer();
								StringBuffer sbTM = new StringBuffer();
								for (String s : t) {
									if (s.matches(".*\\/4\\-\\d+\\-.*"))
										sbCN.append(s.replaceAll("^\\<td\\>|\\<\\/td\\>$|\\<\\/td\\>\\<td\\>| class='margintop'", "").replaceAll("\\<table\\>", "<table class='marginbottom'>")).append("</table>");
									if (s.matches(".*\\/5\\-\\d+\\-.*"))
										sbTM.append(s.replaceAll("^\\<td\\>|\\<\\/td\\>$|\\<\\/td\\>\\<td\\>| class='margintop'", "").replaceAll("\\<table\\>", "<table class='marginbottom'>")).append("</table>");
								}
								tEntityRel[idx] = "<td>" + sbCN.toString() + "</td><td>" + sbTM.toString() + "</td>";
							}
						}
					}
				}
			}
		}
	}
	
	public static List<Integer> getTieList(boolean isDouble, boolean isTriple, String tie) {
		ArrayList<Integer> tieList2 = new ArrayList<Integer>();
		tieList2.add(1);tieList2.add(2);tieList2.add(-1);
		tieList2.add(3);tieList2.add(4);tieList2.add(-1);
		tieList2.add(5);tieList2.add(6);
		if (isDouble && StringUtils.notEmpty(tie) && tie.equals("5-8")) {
			tieList2.add(7);
			tieList2.add(8);
		}
		ArrayList<Integer> tieList3 = new ArrayList<Integer>();
		tieList3.add(1);tieList3.add(2);tieList3.add(3);tieList3.add(-1);
		tieList3.add(4);tieList3.add(5);tieList3.add(6);tieList3.add(-1);
		tieList3.add(7);tieList3.add(8);tieList3.add(9);
		return (isDouble ? tieList2 : (isTriple ? tieList3 : StringUtils.tieList(tie)));
	}

	public static String getPlace(Integer idcx, Integer idct, Integer idst, Integer idcn, String cx, String ct, String st, String cn, String cxEN, String ctEN, String stEN, String cnEN, String yr) {
		String img1 = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, idcn, ImageUtils.SIZE_SMALL, yr, cn);
		String img2 = null;
		StringBuffer sb = new StringBuffer();
		if (idcx != null)
			sb.append(HtmlUtils.writeLink(Complex.alias, idcx, cx, cxEN)).append("," + HtmlUtils.SPACE);
		sb.append(HtmlUtils.writeLink(City.alias, idct, ct, ctEN));
		if (idst != null) {
			sb.append("," + HtmlUtils.SPACE + HtmlUtils.writeLink(State.alias, idst, st, stEN));
			img2 = HtmlUtils.writeImage(ImageUtils.INDEX_STATE, idst, ImageUtils.SIZE_SMALL, yr, stEN);
		}
		sb.append("," + HtmlUtils.SPACE + HtmlUtils.writeLink(Country.alias, idcn, cn, cnEN)); 
		String p = sb.toString();
		if (img2 != null)
			p = HtmlUtils.writeImgTable(img2, p);
		p = HtmlUtils.writeImgTable(img1, p);
		return p;
	}
	
	public static StringBuffer getHeader(HttpServletRequest req, short type, Collection<Object> params, Contributor cb, String lang) throws Exception {
		ArrayList<Object> lstParams = new ArrayList<Object>(params);
		HashMap<String, String> hHeader = new HashMap<String, String>();
		hHeader.put("info", "#INFO#");
		Integer spid = null;
		if (type == HEADER_RESULTS) {
			Sport sp = (Sport) DatabaseHelper.loadEntity(Sport.class, lstParams.get(0));
			Championship cp = (Championship) DatabaseHelper.loadEntity(Championship.class, lstParams.get(1));
			Event ev = (Event) DatabaseHelper.loadEntity(Event.class, lstParams.get(2));
			Event se = (Event) DatabaseHelper.loadEntity(Event.class, lstParams.get(3));
			Event se2 = (Event) DatabaseHelper.loadEntity(Event.class, lstParams.get(4));
			ArrayList<String> lstYears = DatabaseHelper.loadLabels(Year.class, String.valueOf(lstParams.get(5)), lang);
			hHeader.put("title", sp.getLabel(lang) + "&nbsp;-&nbsp;" + cp.getLabel(lang) + (ev != null ? "&nbsp;-&nbsp;" + ev.getLabel(lang) + (se != null ? "&nbsp;-&nbsp;" + se.getLabel(lang) : "") + (se2 != null ? "&nbsp;-&nbsp;" + se2.getLabel(lang) : "") : ""));
			hHeader.put("url", HtmlUtils.writeURL("/results", lstParams.toString(), sp.getLabel() + "/" + cp.getLabel() + (ev != null ? "/" + ev.getLabel() + (se != null ? "/" + se.getLabel() : "") + (se2 != null ? "/" + se2.getLabel() : "") : "")));
			hHeader.put("item0", "<table><tr><td><img alt='Results' src='/img/menu/dbresults.png'/></td><td>&nbsp;<a href='/results'>" + ResourceUtils.getText("menu.results", lang) + "</a></td></tr></table>");
			hHeader.put("item1", HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, sp.getId(), ImageUtils.SIZE_SMALL, null, sp.getLabel(lang)), HtmlUtils.writeLink(Sport.alias, sp.getId(), sp.getLabel(lang), sp.getLabel())));
			hHeader.put("item2", HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_CHAMPIONSHIP, sp.getId() + "-" + cp.getId(), ImageUtils.SIZE_SMALL, null, cp.getLabel(lang)), HtmlUtils.writeLink(Championship.alias, cp.getId(), cp.getLabel(lang), cp.getLabel())));
			if (ev != null)
				hHeader.put("item3", HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_EVENT, sp.getId() + "-" + ev.getId(), ImageUtils.SIZE_SMALL, null, ev.getLabel(lang)), HtmlUtils.writeLink(Event.alias, ev.getId(), ev.getLabel(lang), ev.getLabel())));
			if (se != null)
				hHeader.put("item4", HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_EVENT, sp.getId() + "-" + se.getId(), ImageUtils.SIZE_SMALL, null, se.getLabel(lang)), HtmlUtils.writeLink(Event.alias, se.getId(), se.getLabel(lang), se.getLabel())));
			if (se2 != null)
				hHeader.put("item5", HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_EVENT, sp.getId() + "-" + se2.getId(), ImageUtils.SIZE_SMALL, null, se2.getLabel(lang)), HtmlUtils.writeLink(Event.alias, se2.getId(), se2.getLabel(lang), se2.getLabel())));
			hHeader.put("years", (lstYears.isEmpty() ? "" : (lstYears.size() == 1 ? "(" + lstYears.get(0) + ")" : HtmlUtils.writeTip(Year.alias, lstYears))));
			spid = sp.getId();
		}
		else if (type == HEADER_OLYMPICS_INDIVIDUAL) {
			String olId = String.valueOf(lstParams.get(0));
			ArrayList<String> lstOlympics = DatabaseHelper.loadLabelsFromQuery("select concat(concat(ol.year.label, ' '), ol.city.label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? lang.toUpperCase() : "") + ") from Olympics ol where ol.id in (" + olId + ")");
			Sport sp = (Sport) DatabaseHelper.loadEntity(Sport.class, lstParams.get(1));
			Event ev = (Event) DatabaseHelper.loadEntity(Event.class, lstParams.get(2));
			ArrayList<String> lstEvents = DatabaseHelper.loadLabels(Event.class, String.valueOf(lstParams.get(2)), lang);
			ArrayList<String> lstSubevents = DatabaseHelper.loadLabels(Event.class, String.valueOf(lstParams.get(3)), lang);
			hHeader.put("title", ResourceUtils.getText("entity.OL", lang) + "&nbsp;-&nbsp;" + sp.getLabel(lang) + (ev != null ? "&nbsp;-&nbsp;" + ev.getLabel(lang) : ""));
			hHeader.put("url", HtmlUtils.writeURL("/olympics", "ind, " + lstParams.toString(), "results/" + sp.getLabel() + (lstEvents != null && lstEvents.size() == 1 ? "/" + ev.getLabel() : "")));
			hHeader.put("item0", "<table><tr><td><img alt='Olympics' src='/img/menu/dbolympics.png'/></td><td>&nbsp;<a href='/olympics'>" + ResourceUtils.getText("menu.olympics", lang) + "</a></td></tr></table>");
			hHeader.put("item1", ResourceUtils.getText("event.results", lang));
			hHeader.put("item2", (lstOlympics.isEmpty() ? ResourceUtils.getText("all.olympic.games", lang) : (lstOlympics.size() == 1 ? HtmlUtils.writeLink(Olympics.alias, Integer.valueOf(olId), lstOlympics.get(0), null) : HtmlUtils.writeTip(Olympics.alias, lstOlympics) + " " + ResourceUtils.getText("x.olympic.games", lang))));
			hHeader.put("item3", HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, sp.getId(), ImageUtils.SIZE_SMALL, null, sp.getLabel(lang)), HtmlUtils.writeLink(Sport.alias, sp.getId(), sp.getLabel(lang), sp.getLabel())));
			hHeader.put("item4", (lstEvents.isEmpty() ? ResourceUtils.getText("all.events", lang) : (lstEvents.size() == 1 ? lstEvents.get(0) : HtmlUtils.writeTip(Event.alias, lstEvents) + " " + ResourceUtils.getText("x.events", lang))));
			if (!lstSubevents.isEmpty())
				hHeader.put("item5", (lstSubevents.isEmpty() ? ResourceUtils.getText("all.events", lang) : (lstSubevents.size() == 1 ? lstSubevents.get(0) : HtmlUtils.writeTip(Event.alias, lstSubevents) + " " + ResourceUtils.getText("x.events", lang))));
		}
		else if (type == HEADER_OLYMPICS_COUNTRY) {
			String olId = String.valueOf(lstParams.get(0));
			String cnId = String.valueOf(lstParams.get(1));
			ArrayList<String> lstOlympics = DatabaseHelper.loadLabelsFromQuery("select concat(concat(ol.year.label, ' '), ol.city.label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? lang.toUpperCase() : "") + ") from Olympics ol where ol.id in (" + olId + ")");
			ArrayList<String> lstCountries = DatabaseHelper.loadLabels(Country.class, cnId, lang);
			hHeader.put("title", ResourceUtils.getText("entity.OL", lang) + "&nbsp;-&nbsp;" + ResourceUtils.getText("medals.tables", lang));
			hHeader.put("url", HtmlUtils.writeURL("/olympics", "cnt, " + lstParams.toString(), "tables"));
			hHeader.put("item0", "<table><tr><td><img alt='Olympics' src='/img/menu/dbolympics.png'/></td><td>&nbsp;<a href='/olympics'>" + ResourceUtils.getText("menu.olympics", lang) + "</a></td></tr></table>");
			hHeader.put("item1", ResourceUtils.getText("medals.tables", lang));
			hHeader.put("item2", (lstOlympics.isEmpty() ? ResourceUtils.getText("all.olympic.games", lang) : (lstOlympics.size() == 1 ? HtmlUtils.writeLink(Olympics.alias, Integer.valueOf(olId), lstOlympics.get(0), null) : HtmlUtils.writeTip(Olympics.alias, lstOlympics) + " " + ResourceUtils.getText("x.olympic.games", lang))));
			hHeader.put("item3", (lstCountries.isEmpty() ? ResourceUtils.getText("all.countries", lang) : (lstCountries.size() == 1 ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, Integer.valueOf(cnId), ImageUtils.SIZE_SMALL, null, lstCountries.get(0)), HtmlUtils.writeLink(Country.alias, Integer.valueOf(cnId), lstCountries.get(0), null)) : HtmlUtils.writeTip(Country.alias, lstCountries) + " " + ResourceUtils.getText("x.countries", lang))));
		}
		else if (type == HEADER_US_LEAGUES_RETNUM || type == HEADER_US_LEAGUES_CHAMPIONSHIP || type == HEADER_US_LEAGUES_HOF || type == HEADER_US_LEAGUES_RECORD || type == HEADER_US_LEAGUES_TEAMSTADIUM || type == HEADER_US_LEAGUES_WINLOSS) {
			String league = String.valueOf(lstParams.get(0));
			String leagueLabel = (league.equals("1") ? "NFL" : (league.equals("2") ? "NBA" : (league.equals("3") ? "NHL" : "MLB")));
			String typeLabel = (type == HEADER_US_LEAGUES_RETNUM ? ResourceUtils.getText("retired.numbers", "en") : (type == HEADER_US_LEAGUES_CHAMPIONSHIP ? ResourceUtils.getText("championships", "en") : (type == HEADER_US_LEAGUES_HOF ? ResourceUtils.getText("hall.fame", "en") : (type == HEADER_US_LEAGUES_RECORD ? ResourceUtils.getText("records", "en") : (type == HEADER_US_LEAGUES_TEAMSTADIUM ? ResourceUtils.getText("team.stadiums", "en") : ResourceUtils.getText("wins.losses", "en"))))));
			Integer cpId = (league.equals("1") ? 51 : (league.equals("2") ? 54 : (league.equals("3") ? 55 : 56)));
			hHeader.put("title", leagueLabel + "&nbsp;-&nbsp;" + typeLabel);
			hHeader.put("item0", "<table><tr><td><img alt='US leagues' src='/img/menu/dbusleagues.png'/></td><td>&nbsp;<a href='/usleagues'>" + ResourceUtils.getText("menu.usleagues", lang) + "</a></td></tr></table>");
			hHeader.put("item1", HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, cpId, ImageUtils.SIZE_SMALL, null, leagueLabel), HtmlUtils.writeLink(Championship.alias, cpId, leagueLabel, null)));
			hHeader.put("item2", typeLabel);
			if (type == HEADER_US_LEAGUES_RETNUM) {
				hHeader.put("url", HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_RETNUM + "-" + lstParams.toString(), hHeader.get("title")));
				ArrayList<String> lstTeams = DatabaseHelper.loadLabels(Team.class, String.valueOf(lstParams.get(1)), "en");
				hHeader.put("item3", (lstTeams.isEmpty() ? ResourceUtils.getText("all.teams", "en") : (lstTeams.size() == 1 ? lstTeams.get(0) : HtmlUtils.writeTip(Team.alias, lstTeams) + " " + ResourceUtils.getText("x.teams", "en"))));
			}
			else if (type == HEADER_US_LEAGUES_TEAMSTADIUM) {
				hHeader.put("url", HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_TEAMSTADIUM + "-" + lstParams.toString(), hHeader.get("title")));
				ArrayList<String> lstTeams = DatabaseHelper.loadLabels(Team.class, String.valueOf(lstParams.get(1)), "en");
				hHeader.put("item3", (lstTeams.isEmpty() ? ResourceUtils.getText("all.teams", "en") : (lstTeams.size() == 1 ? lstTeams.get(0) : HtmlUtils.writeTip(Team.alias, lstTeams) + " " + ResourceUtils.getText("x.teams", "en"))));
			}
			else if (type == HEADER_US_LEAGUES_WINLOSS) {
				hHeader.put("url", HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_WINLOSS + "-" + lstParams.toString(), hHeader.get("title")));
				ArrayList<String> lstTeams = DatabaseHelper.loadLabels(Team.class, String.valueOf(lstParams.get(1)), "en");
				hHeader.put("item3", (lstTeams.isEmpty() ? ResourceUtils.getText("all.teams", "en") : (lstTeams.size() == 1 ? lstTeams.get(0) : HtmlUtils.writeTip(Team.alias, lstTeams) + " " + ResourceUtils.getText("x.teams", "en"))));
			}
			else if (type == HEADER_US_LEAGUES_HOF) {
				hHeader.put("url", HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_HOF + "-" + lstParams.toString(), hHeader.get("title")));
				ArrayList<String> lstYears = DatabaseHelper.loadLabels(Year.class, String.valueOf(lstParams.get(1)), "en");
				hHeader.put("item3", (lstYears.isEmpty() ? ResourceUtils.getText("all.years", "en") : (lstYears.size() == 1 ? lstYears.get(0) : HtmlUtils.writeTip(Year.alias, lstYears) + " " + ResourceUtils.getText("x.years", "en"))));
			}
			else if (type == HEADER_US_LEAGUES_CHAMPIONSHIP) {
				hHeader.put("url", HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_CHAMPIONSHIP + "-" + lstParams.toString(), hHeader.get("title")));
				ArrayList<String> lstYears = DatabaseHelper.loadLabels(Year.class, String.valueOf(lstParams.get(2)), "en");
				hHeader.put("item3", (lstYears.isEmpty() ? ResourceUtils.getText("all.years", "en") : (lstYears.size() == 1 ? lstYears.get(0) : HtmlUtils.writeTip(Year.alias, lstYears) + " " + ResourceUtils.getText("x.years", "en"))));
			}
			else if (type == HEADER_US_LEAGUES_RECORD) {
				ArrayList<String> lstCategories = DatabaseHelper.loadLabels(Event.class, String.valueOf(lstParams.get(3)), "en");
				if (lstCategories.size() == 1)
					hHeader.put("title", hHeader.get("title") + "&nbsp;-&nbsp;" + lstCategories.get(0));
				hHeader.put("url", HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_RECORD + "-" + lstParams.toString(), hHeader.get("title")));
				String scope = String.valueOf(lstParams.get(4));
				String scope2 = String.valueOf(lstParams.get(5));
				hHeader.put("item3", (lstCategories.isEmpty() ? ResourceUtils.getText("all.categories", "en") : (lstCategories.size() == 1 ? lstCategories.get(0) : HtmlUtils.writeTip(Event.alias, lstCategories) + " " + ResourceUtils.getText("x.categories", "en"))));
				hHeader.put("item4", (scope.equalsIgnoreCase("it") ? ResourceUtils.getText("all.scopes", "en") : USLeaguesServlet.HTYPE1.get(scope).replaceAll("'", "")));
				hHeader.put("item5", (scope2.equalsIgnoreCase("-") ? ResourceUtils.getText("all.scopes", "en") : USLeaguesServlet.HTYPE2.get(scope2).replaceAll("'", "")));
			}
			}
		else if (type == HEADER_REF) {
			String entity = String.valueOf(lstParams.get(0));
			hHeader.put("title", String.valueOf(lstParams.get(6)));
			hHeader.put("item0", "<table><tr><td><img alt='Ref' src='/img/menu/dbref.png'/></td><td>&nbsp;" + ResourceUtils.getText("entity." + entity, lang) + "</td></tr></table>");
			hHeader.put("item1", hHeader.get("title"));
		}
		// Reported errors
		try {
			List<ErrorReport> list = (List<ErrorReport>) DatabaseHelper.execute("from ErrorReport where url='" + req.getAttribute("url") + "' order by id");
			if (list != null && list.size() > 0) {
				StringBuffer sb = new StringBuffer();
				sb.append("<a style=\"cursor:help;background:url('/img/project/error.png') left top no-repeat;padding:1px;padding-left:20px;\" href='#ereport'>" + list.size() + " " + ResourceUtils.getText("error" + (list.size() > 1 ? "s" : ""), lang) + " " + ResourceUtils.getText("on.this.page", lang) + "</a>");
				sb.append("<div id='ereport' class='rendertip'>");
				for (ErrorReport er : list)
					sb.append("-&nbsp;" + er.getText().replaceAll("\r\n|\n", "<br/>")).append("<br/>");
				sb.append("</div>");
				hHeader.put("errors", sb.toString());
			}
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
		finally {
			req.removeAttribute("url");
		}
		return HtmlUtils.writeHeader(hHeader, spid, cb, lang);
	}

	private static Map<Integer, List<StringBuffer>> getPersonLists(String results) throws Exception {
		ArrayList<Object> lParams = new ArrayList<Object>();
		lParams.add(results);
		List<PersonListBean> list = (List<PersonListBean>) DatabaseHelper.call("GetPersonList", lParams);
		Map<Integer, List<StringBuffer>> m = new HashMap<Integer, List<StringBuffer>>();
		if (list != null) {
			int i = 0;
			for (PersonListBean bean : list) {
				Integer rsid = bean.getRsId();
				Integer rank = bean.getPlRank();
				if (!m.containsKey(rsid))
					m.put(rsid, new ArrayList<StringBuffer>());
				List<StringBuffer> l = m.get(rsid);
				if (l.size() < rank)
					l.add(new StringBuffer());
				StringBuffer sb = l.get(rank - 1);
				boolean isLast = (i == list.size() - 1 || list.get(i + 1).getPlRank().compareTo(rank) != 0);
				String s = HtmlUtils.writeLink(Athlete.alias, bean.getPrId(), StringUtils.toFullName(bean.getPrLastName(), bean.getPrFirstName(), bean.getPrCountryCode(), true), (StringUtils.notEmpty(bean.getPrFirstName()) ? bean.getPrFirstName() + " " : "") + bean.getPrLastName());
				sb.append("<tr><th><img alt='L' src='/img/component/treeview/join" + (!isLast ? "bottom" : "") + ".gif'/></th><td>" + s + "</td></tr>");
				i++;
			}
		}
		return m;
	}
	
	private static StringBuffer getWinRecords(String results, String lang) throws Exception {
		ArrayList<Object> lParams = new ArrayList<Object>();
		lParams.add(results);
		lParams.add("_" + lang);
		Collection<RefItem> list = DatabaseHelper.call("WinRecords", lParams);
		StringBuffer html = new StringBuffer();
		html.append("<table class='winrec'><thead><tr><th colspan='3'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("win.records", lang)) + "</th></tr></thead><tbody class='tby'>");
		int max = -1;
		int i = 0;
		for (RefItem item : list) {
			max = (max == -1 ? item.getCount1() : max);
			String str = item.getLabel();
			String strEN = item.getLabelEN();
			if (item.getIdRel1() < 10) {
				String[] t = str.split("\\,\\s", -1);
				str = StringUtils.toFullName(t[0], t[1], item.getLabelRel1(), true);
				strEN = (item.getLabelRel1() != null && item.getLabelRel1().matches(StringUtils.PATTERN_REVERT_NAME) ? (StringUtils.notEmpty(t[0]) ? t[0] + " " : "") + t[1] : (StringUtils.notEmpty(t[1]) ? t[1] + " " : "") + t[0]);
			}
			html.append("<tr" + (++i > 5 ? " class='hidden'" : "") + "><td class='caption'>" + HtmlUtils.writeLink(item.getIdRel1() < 10 ? Athlete.alias : (item.getIdRel1() == 50 ? Team.alias : Country.alias), item.getIdItem(), str, strEN) + "</td>");
			html.append("<td><table><tr><td class='bar1'>&nbsp;</td>");
			html.append("<td class='bar2' style='width:" + (int)((item.getCount1() * 100) / max) + "px;'>&nbsp;</td>");
			html.append("<td class='bar3'>&nbsp;</td></tr></table></td>");
			html.append("<td class='count'>" + item.getCount1() + "</td></tr>");
		}
		if (i > 5)
			html.append("<tr class='moreitems' onclick='winrecMore(this);'><td colspan='3'></td></tr>");
		return html.append("</tbody></table>");
	}

	public static StringBuffer getRecordInfo(HttpServletRequest request, String type, int id, String lang) throws Exception {
		LinkedHashMap<String, String> hInfo = new LinkedHashMap<String, String>();
		hInfo.put("info", "#INFO#");
		Timestamp lastUpdate = null;
		Integer ref = 0;
		if (type.equals(Athlete.alias)) {
			List<Athlete> lAthlete = new ArrayList<Athlete>();
			Athlete e = (Athlete) DatabaseHelper.loadEntity(Athlete.class, id);
			if (e.getLink() != null && e.getLink() >= 0) {
				Athlete e_ = (Athlete) DatabaseHelper.loadEntity(Athlete.class, e.getLink());
				String wId = "(-1" + (e_ != null && e_.getId() > 0 ? "," + e_.getId() : "") + (e_ != null && e_.getLink() > 0 ? "," + e_.getLink() : "") + (e.getId() > 0 ? "," + e.getId() : "") + (e.getLink() > 0 ? "," + e.getLink() : "") + ")";
				lAthlete.addAll(DatabaseHelper.execute("from Athlete where id in " + wId + " or link in " + wId + " order by id"));
				id = lAthlete.get(0).getId();
			}
			else
				lAthlete.add(e);
			Vector<String> vNm = new Vector<String>();
			Vector<Integer> vTm = new Vector<Integer>();
			Vector<Integer> vCn = new Vector<Integer>();
			Vector<Integer> vSp = new Vector<Integer>();
			StringBuffer sbNm = new StringBuffer();
			StringBuffer sbTm = new StringBuffer();
			StringBuffer sbCn = new StringBuffer();
			StringBuffer sbSp = new StringBuffer();
			String img = null;
			ArrayList<Integer> lId = new ArrayList<Integer>();
			for (Athlete a : lAthlete) {
				lId.add(a.getId());
				ref += (a.getRef() != null ? a.getRef() : 0);
				if (StringUtils.notEmpty(a.getImgUrl()))
					img = a.getImgUrl();
				if (StringUtils.notEmpty(a.getLastName())) {
					String fullName = null;
					if (a.getCountry() != null && a.getCountry().getCode().matches(StringUtils.PATTERN_REVERT_NAME))
						fullName = a.getLastName().toUpperCase() + (StringUtils.notEmpty(a.getFirstName()) ? " " + a.getFirstName() : "");
					else
						fullName = (StringUtils.notEmpty(a.getFirstName()) ? a.getFirstName() + " " : "") + a.getLastName().toUpperCase();
					if (!vNm.contains(fullName)) {
						sbNm.append(sbNm.toString().length() > 0 ? "<br/>" : "").append(fullName);
						vNm.add(fullName);
					}
				}
				if (a.getTeam() != null) {
					if (!vTm.contains(a.getTeam().getId())) {
						String s = HtmlUtils.writeLink(Team.alias, a.getTeam().getId(), a.getTeam().getLabel(), a.getTeam().getLabel());
						s = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, a.getTeam().getId(), ImageUtils.SIZE_SMALL, null, null), s);
						if (StringUtils.notEmpty(sbTm.toString()))
							s = s.replaceAll("\\<table\\>", "<table style='margin-top:2px;'>");
						sbTm.append(s);
						vTm.add(a.getTeam().getId());
					}
				}
				if (a.getCountry() != null) {
					if (!vCn.contains(a.getCountry().getId())) {
						String s = HtmlUtils.writeLink(Country.alias, a.getCountry().getId(), a.getCountry().getLabel(lang), a.getCountry().getLabel());
						s = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, a.getCountry().getId(), ImageUtils.SIZE_SMALL, null, null), s);
						if (StringUtils.notEmpty(sbCn.toString()))
							s = s.replaceAll("\\<table\\>", "<table style='margin-top:2px;'>");
						sbCn.append(s);
						vCn.add(a.getCountry().getId());
					}
				}
				if (a.getSport() != null) {
					if (!vSp.contains(a.getSport().getId())) {
						String s = HtmlUtils.writeLink(Sport.alias, a.getSport().getId(), a.getSport().getLabel(lang), a.getSport().getLabel());
						s = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, a.getSport().getId(), ImageUtils.SIZE_SMALL, null, null), s);
						if (StringUtils.notEmpty(sbSp.toString()))
							s = s.replaceAll("\\<table\\>", "<table style='margin-top:2px;'>");
						sbSp.append(s);
						vSp.add(a.getSport().getId());
					}
				}
			}
			String tm = sbTm.toString();
			String cn = sbCn.toString();
			String sp = sbSp.toString();

			if (e.getCountry() != null && e.getCountry().getCode().matches(StringUtils.PATTERN_REVERT_NAME))
				hInfo.put("title", e.getLastName() + (StringUtils.notEmpty(e.getFirstName()) ? " " + e.getFirstName() : ""));
			else
				hInfo.put("title", (StringUtils.notEmpty(e.getFirstName()) ? e.getFirstName() + " " : "") + e.getLastName());
			hInfo.put("name", "<b>" + sbNm.toString() + "</b>");
			if (StringUtils.notEmpty(cn))
				hInfo.put("country", cn);
			if (StringUtils.notEmpty(sp))
				hInfo.put("sport", sp);
			if (StringUtils.notEmpty(tm))
				hInfo.put("team", tm);
			if (img != null)
				hInfo.put("imgurl", img);
			// Record
			ArrayList<Object> lFuncParams = new ArrayList<Object>();
			lFuncParams.add("PR");
			lFuncParams.add(e.getSport().getId());
			lFuncParams.add(lId.toString().replaceAll("\\[|\\]", "").replaceAll("\\,\\s", "-"));
			Collection<RefItem> cRecord = (Collection<RefItem>) DatabaseHelper.call("GetMedalCount", lFuncParams);
			if (!cRecord.isEmpty())
				hInfo.put("record", HtmlUtils.writeRecordItems(cRecord, lang));
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Championship.alias)) {
			Championship e = (Championship) DatabaseHelper.loadEntity(Championship.class, id);
			hInfo.put("title", e.getLabel(lang));
			hInfo.put("titleEN", e.getLabel());
			hInfo.put("logo", HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, e.getId(), ImageUtils.SIZE_LARGE, null, null));
			hInfo.put("name", "<b>" + e.getLabel(lang).toUpperCase() + "</b>");
			ref = e.getRef();
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(City.alias)) {
			List<City> lCity = new ArrayList<City>();
			City e = (City) DatabaseHelper.loadEntity(City.class, id);
			if (e.getLink() != null && e.getLink() >= 0) {
				City e_ = (City) DatabaseHelper.loadEntity(City.class, e.getLink());
				String wId = "(-1" + (e_ != null && e_.getId() > 0 ? "," + e_.getId() : "") + (e_ != null && e_.getLink() > 0 ? "," + e_.getLink() : "") + (e.getId() > 0 ? "," + e.getId() : "") + (e.getLink() > 0 ? "," + e.getLink() : "") + ")";
				lCity.addAll(DatabaseHelper.execute("from City where id in " + wId + " or link in " + wId + " order by id"));
				id = lCity.get(0).getId();
			}
			else
				lCity.add(e);
			Vector<String> vNm = new Vector<String>();
			Vector<Integer> vSt = new Vector<Integer>();
			Vector<Integer> vCn = new Vector<Integer>();
			StringBuffer sbNm = new StringBuffer();
			StringBuffer sbSt = new StringBuffer();
			StringBuffer sbCn = new StringBuffer();
			String img = null;
			ArrayList<Integer> lId = new ArrayList<Integer>();
			for (City c : lCity) {
				lId.add(c.getId());
				ref += (c.getRef() != null ? c.getRef() : 0);
				if (StringUtils.notEmpty(c.getImgUrl()))
					img = c.getImgUrl();
				if (!vNm.contains(c.getLabel(lang))) {
					sbNm.append(sbNm.toString().length() > 0 ? "<br/>" : "").append(c.getLabel(lang).toUpperCase());
					vNm.add(c.getLabel(lang));
				}
				if (c.getState() != null) {
					if (!vSt.contains(c.getState().getId())) {
						String s = HtmlUtils.writeLink(State.alias, c.getState().getId(), c.getState().getLabel(lang), c.getState().getLabel());
						s = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_STATE, c.getState().getId(), ImageUtils.SIZE_SMALL, null, null), s);
						if (StringUtils.notEmpty(sbSt.toString()))
							s = s.replaceAll("\\<table\\>", "<table style='margin-top:2px;'>");
						sbSt.append(s);
						vSt.add(c.getState().getId());
					}
				}
				if (c.getCountry() != null) {
					if (!vCn.contains(c.getCountry().getId())) {
						String s = HtmlUtils.writeLink(Country.alias, c.getCountry().getId(), c.getCountry().getLabel(lang), c.getCountry().getLabel());
						s = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, c.getCountry().getId(), ImageUtils.SIZE_SMALL, null, null), s);
						if (StringUtils.notEmpty(sbCn.toString()))
							s = s.replaceAll("\\<table\\>", "<table style='margin-top:2px;'>");
						sbCn.append(s);
						vCn.add(c.getCountry().getId());
					}
				}
			}
			String st = sbSt.toString();
			String cn = sbCn.toString();

			hInfo.put("title", e.getLabel(lang));
			hInfo.put("titleEN", e.getLabel());
			hInfo.put("name", "<b>" + sbNm.toString() + "</b>");
			if (StringUtils.notEmpty(st))
				hInfo.put("state", st);
			if (StringUtils.notEmpty(cn))
				hInfo.put("country", cn);
			if (img != null)
				hInfo.put("imgurl", img);
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Complex.alias)) {
			List<Complex> lComplex = new ArrayList<Complex>();
			Complex e = (Complex) DatabaseHelper.loadEntity(Complex.class, id);
			if (e.getLink() != null && e.getLink() >= 0) {
				Complex e_ = (Complex) DatabaseHelper.loadEntity(Complex.class, e.getLink());
				String wId = "(-1" + (e_ != null && e_.getId() > 0 ? "," + e_.getId() : "") + (e_ != null && e_.getLink() > 0 ? "," + e_.getLink() : "") + (e.getId() > 0 ? "," + e.getId() : "") + (e.getLink() > 0 ? "," + e.getLink() : "") + ")";
				lComplex.addAll(DatabaseHelper.execute("from Complex where id in " + wId + " or link in " + wId + " order by id"));
				id = lComplex.get(0).getId();
			}
			else
				lComplex.add(e);
			Vector<String> vNm = new Vector<String>();
			Vector<String> vCt = new Vector<String>();
			Vector<Integer> vSt = new Vector<Integer>();
			Vector<Integer> vCn = new Vector<Integer>();
			StringBuffer sbNm = new StringBuffer();
			StringBuffer sbCt = new StringBuffer();
			StringBuffer sbSt = new StringBuffer();
			StringBuffer sbCn = new StringBuffer();
			String img = null;
			ArrayList<Integer> lId = new ArrayList<Integer>();
			for (Complex c : lComplex) {
				lId.add(c.getId());
				ref += (c.getRef() != null ? c.getRef() : 0);
				if (StringUtils.notEmpty(c.getImgUrl()))
					img = c.getImgUrl();
				if (!vNm.contains(c.getLabel(lang))) {
					sbNm.append(sbNm.toString().length() > 0 ? "<br/>" : "").append(c.getLabel(lang).toUpperCase());
					vNm.add(c.getLabel(lang));
				}
				if (!vCt.contains(c.getCity().getLabel(lang))) {
					sbCt.append(sbCt.toString().length() > 0 ? "<br/>" : "").append(c.getCity().getLabel(lang));
					vCt.add(c.getLabel(lang));
				}
				if (c.getCity().getState() != null) {
					if (!vSt.contains(c.getCity().getState().getId())) {
						String s = HtmlUtils.writeLink(State.alias, c.getCity().getState().getId(), c.getCity().getState().getLabel(lang), c.getCity().getState().getLabel());
						s = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_STATE, c.getCity().getState().getId(), ImageUtils.SIZE_SMALL, null, null), s);
						if (StringUtils.notEmpty(sbSt.toString()))
							s = s.replaceAll("\\<table\\>", "<table style='margin-top:2px;'>");
						sbSt.append(s);
						vSt.add(c.getCity().getState().getId());
					}
				}
				if (c.getCity().getCountry() != null) {
					if (!vCn.contains(c.getCity().getCountry().getId())) {
						String s = HtmlUtils.writeLink(Country.alias, c.getCity().getCountry().getId(), c.getCity().getCountry().getLabel(lang), c.getCity().getCountry().getLabel());
						s = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, c.getCity().getCountry().getId(), ImageUtils.SIZE_SMALL, null, null), s);
						if (StringUtils.notEmpty(sbCn.toString()))
							s = s.replaceAll("\\<table\\>", "<table style='margin-top:2px;'>");
						sbCn.append(s);
						vCn.add(c.getCity().getCountry().getId());
					}
				}
			}
			String ct = sbCt.toString();
			String st = sbSt.toString();
			String cn = sbCn.toString();
			
			hInfo.put("title", e.getLabel(lang));
			hInfo.put("titleEN", e.getLabel());
			hInfo.put("name", "<b>" + sbNm.toString() + "</b>");
			if (StringUtils.notEmpty(ct))
				hInfo.put("city", ct);
			if (StringUtils.notEmpty(st))
				hInfo.put("state", st);
			if (StringUtils.notEmpty(cn))
				hInfo.put("country", cn);
			if (img != null)
				hInfo.put("imgurl", img);
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Contributor.alias)) {
			Contributor e = (Contributor) DatabaseHelper.loadEntity(Contributor.class, id);
			hInfo.put("title", e.getLogin());
			hInfo.put("ID", "<b>" + e.getLogin().toUpperCase() + "</b>");
			hInfo.put("name", e.getPublicName());
		}
		else if (type.equals(Country.alias)) {
			Country e = (Country) DatabaseHelper.loadEntity(Country.class, id);
			String currentLogo = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, e.getId(), ImageUtils.SIZE_LARGE, null, null);
			Collection<String> lAllLogos = ImageUtils.getImageList(ImageUtils.INDEX_COUNTRY, e.getId(), ImageUtils.SIZE_LARGE);
			hInfo.put("title", e.getLabel(lang));
			hInfo.put("titleEN", e.getLabel());
			hInfo.put("name", "<b>" + e.getLabel(lang).toUpperCase() + "</b>");
			hInfo.put("code", e.getCode());
			hInfo.put("flag", currentLogo);
			StringBuffer sbOtherFlags = new StringBuffer();
			if (lAllLogos != null && lAllLogos.size() > 1) {
				int nof = 0;
				for (String s : lAllLogos)
					if (s.matches(".*\\d{4}\\-\\d{4}\\.png$")) {
						String s_ = s.substring(s.indexOf("_") + 1, s.lastIndexOf("."));
						if (s_.substring(0, 4).equals(s_.substring(5)))
							s_ = s_.substring(5);
						sbOtherFlags.append("<div class='of-" + id + "' style='" + (++nof > 3 ? "display:none;" : "") + "float:left;margin:5px;text-align:center;'><img alt='' src='" + ImageUtils.getUrl() + s + "'/>");
						sbOtherFlags.append("<br/><b>" + s_ + "</b></div>");
					}
				if (nof > 3)
					sbOtherFlags.append("<div id='of-" + id + "-link' class='otherimglink' style='padding-top:90px;'><a href='javascript:moreImg(\"of-" + id + "\");'>" + ResourceUtils.getText("more", lang) + "</a></div>");
			}
			hInfo.put("otherflags", sbOtherFlags.toString());
			// Record
			ArrayList<Object> lFuncParams = new ArrayList<Object>();
			lFuncParams.add("CN");
			lFuncParams.add(0);
			lFuncParams.add(String.valueOf(e.getId()));
			Collection<RefItem> cRecord = (Collection<RefItem>) DatabaseHelper.call("GetMedalCount", lFuncParams);
			if (!cRecord.isEmpty())
				hInfo.put("record", HtmlUtils.writeRecordItems(cRecord, lang));
			ref = e.getRef();
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Event.alias)) {
			Event e = (Event) DatabaseHelper.loadEntity(Event.class, id);
			hInfo.put("title", e.getLabel(lang));
			hInfo.put("titleEN", e.getLabel());
			hInfo.put("logo", HtmlUtils.writeImage(ImageUtils.INDEX_EVENT, e.getId(), ImageUtils.SIZE_LARGE, null, null));
			hInfo.put("name", "<b>" + e.getLabel(lang).toUpperCase() + "</b>");
			ref = e.getRef();
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Olympics.alias)) {
			Olympics e = (Olympics) DatabaseHelper.loadEntity(Olympics.class, id);
			String st = null;
			String cn = null;
			if (e.getCity().getState() != null) {
				st = HtmlUtils.writeLink(State.alias, e.getCity().getState().getId(), e.getCity().getState().getLabel(lang), e.getCity().getState().getLabel());
				st = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_STATE, e.getCity().getState().getId(), ImageUtils.SIZE_SMALL, null, null), st);
			}
			if (e.getCity().getCountry() != null) {
				cn = HtmlUtils.writeLink(Country.alias, e.getCity().getCountry().getId(), e.getCity().getCountry().getLabel(lang), e.getCity().getCountry().getLabel());
				cn = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, e.getCity().getCountry().getId(), ImageUtils.SIZE_SMALL, null, null), cn);
			}
			hInfo.put("title", e.getCity().getLabel(lang) + "&nbsp;" + e.getYear().getLabel());
			hInfo.put("titleEN", e.getCity().getLabel() + "&nbsp;" + e.getYear().getLabel());
			hInfo.put("logo", HtmlUtils.writeImage(ImageUtils.INDEX_OLYMPICS, e.getId(), ImageUtils.SIZE_LARGE, null, null));
			hInfo.put("year", HtmlUtils.writeLink(Year.alias, e.getYear().getId(), e.getYear().getLabel(), null));
			hInfo.put("city", HtmlUtils.writeLink(City.alias, e.getCity().getId(), e.getCity().getLabel(lang), e.getCity().getLabel()));
			hInfo.put("country", (cn != null ? cn : StringUtils.EMPTY));
			hInfo.put("start.date", e.getDate1());
			hInfo.put("end.date", e.getDate2());
			hInfo.put("sports", String.valueOf(e.getCountSport()));
			hInfo.put("events", String.valueOf(e.getCountEvent()));
			hInfo.put("countries", String.valueOf(e.getCountCountry()));
			hInfo.put("athletes", String.valueOf(e.getCountPerson()));
			ref = e.getRef();
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Result.alias)) {
			StringBuffer html = new StringBuffer();
			Result r = (Result) DatabaseHelper.loadEntity(Result.class, id);
			Map<Integer, List<StringBuffer>> mpl = getPersonLists(String.valueOf(id));
			List<StringBuffer> plist = mpl.get(id);
			// Info
			Integer eventId = (r.getSubevent2() != null ? r.getSubevent2().getId() : (r.getSubevent() != null ? r.getSubevent().getId() : r.getEvent().getId()));
			html.append("<span class='title'>[" + r.getYear().getLabel() + "] " + r.getSport().getLabel(lang) + "&nbsp;-&nbsp;" + r.getChampionship().getLabel(lang) + (r.getEvent() != null ? "&nbsp;-&nbsp;" + r.getEvent().getLabel(lang) + (r.getSubevent() != null ? "&nbsp;-&nbsp;" + r.getSubevent().getLabel(lang) : "") + (r.getSubevent2() != null ? "&nbsp;-&nbsp;" + r.getSubevent2().getLabel(lang) : "") : "") + "</span>");
			html.append("<span class='url'>" + HtmlUtils.writeLink(type, id, null, r.getYear().getLabel() + "/" + r.getSport().getLabel() + "/" + r.getChampionship().getLabel() + "/" + r.getEvent().getLabel() + (r.getSubevent() != null ? "/" + r.getSubevent().getLabel() : "") + (r.getSubevent2() != null ? "/" + r.getSubevent2().getLabel() : "")) + "</span>");
			html.append("<ul class='uinfo'><li>");
			html.append("<table class='info'><thead><tr><th colspan='2'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity.RS.1", lang).toUpperCase()) + "</th></tr></thead><tbody class='tby'>");
			html.append("<tr><th class='caption'>" + ResourceUtils.getText("entity.SP.1", lang) + "</th><td>" + HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, r.getSport().getId(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Sport.alias, r.getSport().getId(), r.getSport().getLabel(lang), r.getSport().getLabel())) + "</td></tr>");
			html.append("<tr><th class='caption'>" + ResourceUtils.getText("entity.YR.1", lang) + "</th><td>" + HtmlUtils.writeLink(Year.alias, r.getYear().getId(), r.getYear().getLabel(lang), r.getYear().getLabel()) + "</td></tr>");
			html.append("<tr><th class='caption'>" + ResourceUtils.getText("entity.CP.1", lang) + "</th><td>" + HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_CHAMPIONSHIP, r.getSport().getId() + "-" + r.getChampionship().getId(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Championship.alias, r.getChampionship().getId(), r.getChampionship().getLabel(lang), r.getChampionship().getLabel())) + "</td></tr>");
			html.append("<tr><th class='caption'>" + ResourceUtils.getText("entity.EV.1", lang) + "</th><td>" + HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_EVENT, r.getSport().getId() + "-" + eventId, ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Event.alias, r.getEvent().getId(), r.getEvent().getLabel(lang), r.getEvent().getLabel()) + (r.getSubevent() != null ? "&nbsp;-&nbsp;" + HtmlUtils.writeLink(Event.alias, r.getSubevent().getId(), r.getSubevent().getLabel(lang), r.getSubevent().getLabel()) : "") + (r.getSubevent2() != null ? "&nbsp;-&nbsp;" + HtmlUtils.writeLink(Event.alias, r.getSubevent2().getId(), r.getSubevent2().getLabel(lang), r.getSubevent2().getLabel()) : "")) + "</td></tr>");
			if (StringUtils.notEmpty(r.getDate2()))
				html.append("<tr><th class='caption'>" + ResourceUtils.getText(StringUtils.notEmpty(r.getDate1()) ? "dates" : "date", lang) + "</th><td>" + (StringUtils.notEmpty(r.getDate1()) ? StringUtils.toTextDate(r.getDate1(), lang, "d MMMM yyyy") + "&nbsp;&ndash;&nbsp;" : "") + StringUtils.toTextDate(r.getDate2(), lang, "d MMMM yyyy") + "</td></tr>");
			if (StringUtils.notEmpty(r.getComplex2()) || StringUtils.notEmpty(r.getCity2())) {
				String p1 = null;
				String p2 = null;
				if (r.getComplex1() != null) {
					Complex cx = r.getComplex1();
					p1 = getPlace(cx.getId(), cx.getCity().getId(), cx.getCity().getState() != null ? cx.getCity().getState().getId() : null, cx.getCity().getCountry().getId(), cx.getLabel(lang), cx.getCity().getLabel(lang), cx.getCity().getState() != null ? cx.getCity().getState().getCode() : null, cx.getCity().getCountry().getLabel(lang), cx.getLabel(), cx.getCity().getLabel(), cx.getCity().getState() != null ? cx.getCity().getState().getLabel() : null, cx.getCity().getCountry().getLabel(), r.getYear().getLabel());
				}
				else if (r.getCity1() != null) {
					City ct = r.getCity1();
					p1 = getPlace(null, ct.getId(), ct.getState() != null ? ct.getState().getId() : null, ct.getCountry().getId(), null, ct.getLabel(lang), ct.getState() != null ? ct.getState().getCode() : null, ct.getCountry().getLabel(lang), null, ct.getLabel(), ct.getState() != null ? ct.getState().getLabel() : null, ct.getCountry().getLabel(), r.getYear().getLabel());
				}
				if (r.getComplex2() != null) {
					Complex cx = r.getComplex2();
					p2 = getPlace(cx.getId(), cx.getCity().getId(), cx.getCity().getState() != null ? cx.getCity().getState().getId() : null, cx.getCity().getCountry().getId(), cx.getLabel(lang), cx.getCity().getLabel(lang), cx.getCity().getState() != null ? cx.getCity().getState().getCode() : null, cx.getCity().getCountry().getLabel(lang), cx.getLabel(), cx.getCity().getLabel(), cx.getCity().getState() != null ? cx.getCity().getState().getLabel() : null, cx.getCity().getCountry().getLabel(), r.getYear().getLabel());
				}
				else if (r.getCity2() != null) {
					City ct = r.getCity2();
					p2 = getPlace(null, ct.getId(), ct.getState() != null ? ct.getState().getId() : null, ct.getCountry().getId(), null, ct.getLabel(lang), ct.getState() != null ? ct.getState().getCode() : null, ct.getCountry().getLabel(lang), null, ct.getLabel(), ct.getState() != null ? ct.getState().getLabel() : null, ct.getCountry().getLabel(), r.getYear().getLabel());
				}
				if (StringUtils.notEmpty(p1))
					p2 = p2.replaceAll("^\\<table\\>", "<table class='margintop'>");
				html.append("<tr><th class='caption'>" + ResourceUtils.getText(StringUtils.notEmpty(p1) ? "places" : "place", lang) + "</th><td>" + (StringUtils.notEmpty(p1) ? p1 : "") + p2 + "</td></tr>");
			}
			String extlinks = HtmlUtils.writeExternalLinks(type, id, lang);
			if (StringUtils.notEmpty(extlinks))
				html.append("<tr><th class='caption'>" + ResourceUtils.getText("extlinks", lang) + "</th><td class='extlinks'>" + extlinks + "</td></tr>");
			List<Object[]> l = DatabaseHelper.execute("select id, year.label from Result where sport.id=" + r.getSport().getId() + " and championship.id=" + r.getChampionship().getId() + " and event.id=" + r.getEvent().getId() + (r.getSubevent() != null ? " and subevent.id=" + r.getSubevent().getId() : "") + (r.getSubevent2() != null ? " and subevent2.id=" + r.getSubevent2().getId() : "") + " order by year.id");
			if (l != null && l.size() > 1) {
				String path = r.getSport().getLabel() + "/" + r.getChampionship().getLabel() + "/" + r.getEvent().getLabel() + (r.getSubevent() != null ? "/" + r.getSubevent().getLabel() : "") + (r.getSubevent2() != null ? "/" + r.getSubevent2().getLabel() : "");
				StringBuffer sbOtherYears = new StringBuffer();
				for (Object[] t : l) {
					Integer id_ = (Integer) t[0];
					String label = String.valueOf(t[1]);
					if (!id_.equals(r.getId()))
						sbOtherYears.append(HtmlUtils.writeLink(Result.alias, id_, label, label + "/" + path));
					else
						sbOtherYears.append("<b>" + label + "</b>");
					sbOtherYears.append(" ");
				}
				html.append("<tr><th class='caption'>" + ResourceUtils.getText("other.years", lang) + "</th><td style='width:500px;white-space:normal;'>" + sbOtherYears.toString() + "</td></tr>");
			}
			if (StringUtils.notEmpty(r.getComment()) && !r.getComment().startsWith("##") && !r.getComment().matches("\\#(DOUBLE|TRIPLE)\\#"))
				html.append("<tr><th class='caption'>" + ResourceUtils.getText("comment", lang) + "</th><td>" + r.getComment().replaceAll("\r\n|\\|", "<br/>") + "</td></tr>");
			// Result
			ArrayList<Object> lFuncParams = new ArrayList<Object>();
			lFuncParams.add(r.getSport().getId());
			lFuncParams.add(r.getChampionship().getId());
			lFuncParams.add(r.getEvent().getId());
			lFuncParams.add(r.getSubevent() != null ? r.getSubevent().getId() : 0);
			lFuncParams.add(r.getSubevent2() != null ? r.getSubevent2().getId() : 0);
			lFuncParams.add(String.valueOf(r.getYear().getId()));
			lFuncParams.add("_" + lang);
			List<ResultsBean> list = (List<ResultsBean>) DatabaseHelper.call("GetResults", lFuncParams);
			if (list != null && !list.isEmpty()) {
				ResultsBean bean = list.get(0);
				String[] tEntity = {null, null, null, null, null, null, null, null, null};
				String[] tEntityRel = {null, null, null, null, null, null, null, null, null};
				String[] tEntityHtml = {null, null, null, null, null, null, null, null, null};
				String[] tResult = {null, null, null, null, null, null, null, null, null};
				Event ev_ = (Event) DatabaseHelper.loadEntity(Event.class, (r.getSubevent2() != null ? r.getSubevent2().getId() : (r.getSubevent() != null ? r.getSubevent().getId() : r.getEvent().getId())));
				int type_ = ev_.getType().getNumber();
				boolean isScore = (bean.getRsRank1() != null && bean.getRsRank2() != null && StringUtils.notEmpty(bean.getRsResult1()) && !StringUtils.notEmpty(bean.getRsResult2()) && !StringUtils.notEmpty(bean.getRsResult3()));
				if (bean.getRsRank1() != null) {
					tEntity[0] = getResultsEntity(type_, bean.getRsRank1(), bean.getEn1Str1(), bean.getEn1Str2(), bean.getEn1Str3(), bean.getEn1Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 0 ? "plist-" + id + "-0" : null);
					tEntityRel[0] = getResultsEntityRel(bean.getEn1Rel1Id(), bean.getEn1Rel1Label(), bean.getEn1Rel1Label(), bean.getEn1Rel2Id(), bean.getEn1Rel2Label(), bean.getEn1Rel2Label(), bean.getEn1Rel2LabelEN(), false, false, bean.getYrLabel());
					tResult[0] = bean.getRsResult1();
				}
				if (bean.getRsRank2() != null) {
					tEntity[1] = getResultsEntity(type_, bean.getRsRank2(), bean.getEn2Str1(), bean.getEn2Str2(), bean.getEn2Str3(), bean.getEn2Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 1 ? "plist-" + id + "-1" : null);
					tEntityRel[1] = getResultsEntityRel(bean.getEn2Rel1Id(), bean.getEn2Rel1Label(), bean.getEn2Rel1Label(), bean.getEn2Rel2Id(), bean.getEn2Rel2Label(), bean.getEn2Rel2Label(), bean.getEn2Rel2LabelEN(), false, false, bean.getYrLabel());
					tResult[1] = bean.getRsResult2();
				}
				if (bean.getRsRank3() != null) {
					tEntity[2] = getResultsEntity(type_, bean.getRsRank3(), bean.getEn3Str1(), bean.getEn3Str2(), bean.getEn3Str3(), bean.getEn3Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 2 ? "plist-" + id + "-2" : null);
					tEntityRel[2] = getResultsEntityRel(bean.getEn3Rel1Id(), bean.getEn3Rel1Label(), bean.getEn3Rel1Label(), bean.getEn3Rel2Id(), bean.getEn3Rel2Label(), bean.getEn3Rel2Label(), bean.getEn3Rel2LabelEN(), false, false, bean.getYrLabel());
					tResult[2] = bean.getRsResult3();
				}
				if (bean.getRsRank4() != null) {
					tEntity[3] = getResultsEntity(type_, bean.getRsRank4(), bean.getEn4Str1(), bean.getEn4Str2(), bean.getEn4Str3(), bean.getEn4Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 3 ? "plist-" + id + "-3" : null);
					tEntityRel[3] = getResultsEntityRel(bean.getEn3Rel1Id(), bean.getEn4Rel1Label(), bean.getEn4Rel1Label(), bean.getEn4Rel2Id(), bean.getEn4Rel2Label(), bean.getEn4Rel2Label(), bean.getEn4Rel2LabelEN(), false, false, bean.getYrLabel());
					tResult[3] = bean.getRsResult4();
				}
				if (bean.getRsRank5() != null) {
					tEntity[4] = getResultsEntity(type_, bean.getRsRank5(), bean.getEn5Str1(), bean.getEn5Str2(), bean.getEn5Str3(), bean.getEn5Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 4 ? "plist-" + id + "-4" : null);
					tEntityRel[4] = getResultsEntityRel(bean.getEn5Rel1Id(), bean.getEn5Rel1Label(), bean.getEn5Rel1Label(), bean.getEn5Rel2Id(), bean.getEn5Rel2Label(), bean.getEn5Rel2Label(), bean.getEn5Rel2LabelEN(), false, false, bean.getYrLabel());
					tResult[4] = bean.getRsResult5();
				}
				if (bean.getRsRank6() != null) {
					tEntity[5] = getResultsEntity(type_, bean.getRsRank6(), bean.getEn6Str1(), bean.getEn6Str2(), bean.getEn6Str3(), bean.getEn6Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 5 ? "plist-" + id + "-5" : null);
					tEntityRel[5] = getResultsEntityRel(bean.getEn6Rel1Id(), bean.getEn6Rel1Label(), bean.getEn6Rel1Label(), bean.getEn6Rel2Id(), bean.getEn6Rel2Label(), bean.getEn6Rel2Label(), bean.getEn6Rel2LabelEN(), false, false, bean.getYrLabel());
				}
				if (bean.getRsRank7() != null) {
					tEntity[6] = getResultsEntity(type_, bean.getRsRank7(), bean.getEn7Str1(), bean.getEn7Str2(), bean.getEn7Str3(), bean.getEn7Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 6 ? "plist-" + id + "-6" : null);
					tEntityRel[6] = getResultsEntityRel(bean.getEn7Rel1Id(), bean.getEn7Rel1Label(), bean.getEn7Rel1Label(), bean.getEn7Rel2Id(), bean.getEn7Rel2Label(), bean.getEn7Rel2Label(), bean.getEn7Rel2LabelEN(), false, false, bean.getYrLabel());
				}
				if (bean.getRsRank8() != null) {
					tEntity[7] = getResultsEntity(type_, bean.getRsRank8(), bean.getEn8Str1(), bean.getEn8Str2(), bean.getEn8Str3(), bean.getEn8Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 7 ? "plist-" + id + "-7" : null);
					tEntityRel[7] = getResultsEntityRel(bean.getEn8Rel1Id(), bean.getEn8Rel1Label(), bean.getEn8Rel1Label(), bean.getEn8Rel2Id(), bean.getEn8Rel2Label(), bean.getEn8Rel2Label(), bean.getEn8Rel2LabelEN(), false, false, bean.getYrLabel());
				}
				if (bean.getRsRank9() != null) {
					tEntity[8] = getResultsEntity(type_, bean.getRsRank9(), bean.getEn9Str1(), bean.getEn9Str2(), bean.getEn9Str3(), bean.getEn9Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 8 ? "plist-" + id + "-8" : null);
					tEntityRel[8] = getResultsEntityRel(bean.getEn9Rel1Id(), bean.getEn9Rel1Label(), bean.getEn9Rel1Label(), bean.getEn9Rel2Id(), bean.getEn9Rel2Label(), bean.getEn9Rel2Label(), bean.getEn9Rel2LabelEN(), false, false, bean.getYrLabel());
				}
				boolean isDouble = (type_ == 4 || (bean.getRsComment() != null && bean.getRsComment().equals("#DOUBLE#")));
				boolean isTriple = (type_ == 5 || (bean.getRsComment() != null && bean.getRsComment().equals("#TRIPLE#")));
				setTies(getTieList(isDouble, isTriple, bean.getRsExa()), type_, tEntity, tEntityRel);
				if (isTriple || isDouble) {
					tEntity = StringUtils.removeNulls(tEntity);
					tEntityRel = StringUtils.removeNulls(tEntityRel);
				}
				for (int i = 0 ; i < 9 ; i++)
					if (tEntity[i] != null)
						tEntityHtml[i] = ("<td>" + tEntity[i] + (plist != null && plist.size() > i ? "<table id='plist-" + id + "-" + i + "' class='plist' style='display:none;'>" + plist.get(i).toString() + "</table>" : "") + "</td>" + tEntityRel[i] + (StringUtils.notEmpty(tResult[i]) && !isScore ? "<td>" + tResult[i] + "</td>" : ""));
				html.append("<tr><td colspan='2' class='result'>");
				html.append("<table><tr style='font-weight:bold;'><th>" + ResourceUtils.getText("rank.winner", lang) + "&nbsp;</th>" + (tEntityHtml[0] != null ? tEntityHtml[0] : "<td>" + ResourceUtils.getText("none", lang) + "</td>"));
				if (isScore)
					html.append("<td rowspan='2'><b>" + tResult[0] + "</b></td>");
				html.append("</tr>");
				if (StringUtils.notEmpty(tEntityHtml[1]))
					html.append("<tr><th>" + ResourceUtils.getText(isScore ? "runner.up" : "rank.2", lang) + "&nbsp;</th>" + tEntityHtml[1] + "</tr>");
				if (StringUtils.notEmpty(tEntityHtml[2]))
					html.append("<tr><th>" + ResourceUtils.getText("rank.3", lang) + "&nbsp;</th>" + tEntityHtml[2] + "</tr>");
				html.append("</table>");
				html.append("</td></tr>");
				html.append("</tbody></table></li>");
				if (StringUtils.notEmpty(r.getImgUrl())) {
					html.append("<li><table class='photo'><tr><th><img alt='' src='/img/render/photo.png'/>" + ResourceUtils.getText("photo", lang) + "</th></tr><tr><td>");
					html.append("<a href='" + r.getImgUrl() + "' target='_blank' title=\"" + ResourceUtils.getText("enlarge", lang) + "\"><img alt='Photo' height='230px' src='" + r.getImgUrl() + "'/></a>");
					html.append("</td></tr></table></li>");
				}
				html.append("</ul>");
			}
			// Draw
			lFuncParams = new ArrayList<Object>();
			lFuncParams.add(id);
			lFuncParams.add("_" + lang);
			List<DrawBean> lDraw = (List<DrawBean>) DatabaseHelper.call("GetDraw", lFuncParams);
			if (lDraw != null && !lDraw.isEmpty()) {
				DrawBean bean = (DrawBean) lDraw.get(0);
				html.append("<table style='width:100%;margin-bottom:0px;'><thead><tr><th>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity.DR.1", lang).toUpperCase()) + "</th></tr></thead><tbody class='tby'>");
				html.append("<tr><td class='celldraw'><div class='draw'>");
				String[] tLevel = {"Qf1", "Qf2", "Qf3", "Qf4", "Sf1", "Sf2", "F", "Thd"};
				HashMap<String, String> hLvlLabel = StringUtils.getDrawLabels(r.getSport().getId(), r.getChampionship().getId(), r.getEvent().getId(), lang);
				for (String level : tLevel) {
					Method m1 = DrawBean.class.getMethod("getEn1" + level + "Str1");
					Method m2 = DrawBean.class.getMethod("getEn1" + level + "Str2");
					Method m2_ = DrawBean.class.getMethod("getEn1" + level + "Str3");
					Method m3 = DrawBean.class.getMethod("getEn2" + level + "Str1");
					Method m4 = DrawBean.class.getMethod("getEn2" + level + "Str2");
					Method m4_ = DrawBean.class.getMethod("getEn2" + level + "Str3");
					Method m5 = DrawBean.class.getMethod("getEn1" + level + "Id");
					Method m6 = DrawBean.class.getMethod("getEn2" + level + "Id");
					Method mRel1 = DrawBean.class.getMethod("getEn1" + level + "Rel1Id");
					Method mRel2 = DrawBean.class.getMethod("getEn1" + level + "Rel1Code");
					Method mRel3 = DrawBean.class.getMethod("getEn1" + level + "Rel1Label");
//					Method mRel3_ = DrawBean.class.getMethod("getEn1" + level + "Rel1LabelEN");
					Method mRel4 = DrawBean.class.getMethod("getEn1" + level + "Rel2Id");
					Method mRel5 = DrawBean.class.getMethod("getEn1" + level + "Rel2Code");
					Method mRel6 = DrawBean.class.getMethod("getEn1" + level + "Rel2Label");
					Method mRel6_ = DrawBean.class.getMethod("getEn1" + level + "Rel2LabelEN");
					Method mRel7 = DrawBean.class.getMethod("getEn2" + level + "Rel1Id");
					Method mRel8 = DrawBean.class.getMethod("getEn2" + level + "Rel1Code");
					Method mRel9 = DrawBean.class.getMethod("getEn2" + level + "Rel1Label");
//					Method mRel9_ = DrawBean.class.getMethod("getEn2" + level + "Rel1LabelEN");
					Method mRel10 = DrawBean.class.getMethod("getEn2" + level + "Rel2Id");
					Method mRel11 = DrawBean.class.getMethod("getEn2" + level + "Rel2Code");
					Method mRel12 = DrawBean.class.getMethod("getEn2" + level + "Rel2Label");
					Method mRel12_ = DrawBean.class.getMethod("getEn2" + level + "Rel2LabelEN");
					String e = getResultsEntity(bean.getDrType(), StringUtils.toInt(m5.invoke(bean)), String.valueOf(m1.invoke(bean)), String.valueOf(m2.invoke(bean)), String.valueOf(m2_.invoke(bean)), String.valueOf(mRel5.invoke(bean)), bean.getYrLabel(), null);
					if (e != null) {
						html.append("<div class='box " + level.toLowerCase() + "'><table><tr><th colspan='" + (bean.getDrType() < 10 ? 3 : 2) + "'>" + hLvlLabel.get(level) + "</th></tr>");
						String r_ = getResultsEntityRel(StringUtils.toInt(mRel1.invoke(bean)), String.valueOf(mRel2.invoke(bean)), String.valueOf(mRel3.invoke(bean)), StringUtils.toInt(mRel4.invoke(bean)), String.valueOf(mRel5.invoke(bean)), String.valueOf(mRel6.invoke(bean)), String.valueOf(mRel6_.invoke(bean)), false, false, bean.getYrLabel());
						html.append("<tr><td style='font-weight:bold;'>" + e + "</td>" + (r_ != null ? r_ : ""));
						html.append("<td rowspan='2' style='width:33%;'>" + DrawBean.class.getMethod("get" + (level.equalsIgnoreCase("F") ? "Rs" : "Dr") + "Result" + level).invoke(bean) + "</td></tr>");
						e = getResultsEntity(bean.getDrType(), StringUtils.toInt(m6.invoke(bean)), String.valueOf(m3.invoke(bean)), String.valueOf(m4.invoke(bean)), String.valueOf(m4_.invoke(bean)), String.valueOf(mRel11.invoke(bean)), bean.getYrLabel(), null);
						r_ = getResultsEntityRel(StringUtils.toInt(mRel7.invoke(bean)), String.valueOf(mRel8.invoke(bean)), String.valueOf(mRel9.invoke(bean)), StringUtils.toInt(mRel10.invoke(bean)), String.valueOf(mRel11.invoke(bean)), String.valueOf(mRel12.invoke(bean)), String.valueOf(mRel12_.invoke(bean)), false, false, bean.getYrLabel());
						html.append("<tr><td>" + e + "</td>" + (r_ != null ? r_ : "") + "</tr>");
						html.append("</table></div>");
					}
				}
				html.append("</div></td></tr></tbody></table>");
			}
			return html;
		}
		else if (type.equals(Sport.alias)) {
			Sport e = (Sport) DatabaseHelper.loadEntity(Sport.class, id);
			hInfo.put("_sport_", "1");
			hInfo.put("width", "280");
			hInfo.put("title", e.getLabel(lang));
			hInfo.put("titleEN", e.getLabel());
			hInfo.put("logosport", HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, e.getId(), ImageUtils.SIZE_LARGE, null, null));
			hInfo.put("titlename", e.getLabel(lang).toUpperCase());
			hInfo.put("references", "<b>" + ResourceUtils.getText("references", lang) + "</b>&nbsp;:&nbsp;" + String.valueOf(e.getRef()));
			hInfo.put("extlinks", HtmlUtils.writeExternalLinks(type, id, lang));
			if (StringUtils.notEmpty(e.getImgUrl()))
				hInfo.put("imgurl", e.getImgUrl());
			lastUpdate = e.getMetadata().getLastUpdate();
			StringWriter sw = new StringWriter();
			ArrayList<Object> lFuncParams = new ArrayList<Object>();
			lFuncParams.add("WHERE SP.id=" + id);
			lFuncParams.add("_" + lang);
			HtmlConverter.convertTreeArray(DatabaseHelper.call("TreeResults", lFuncParams), sw, true);
			hInfo.put("tree", "<div class='treediv'><div id='treeview' class='collapsed'><table cellpadding='0' cellspacing='0'><tr><td><script type='text/javascript'>var " + sw.toString() + "new Tree(treeItems, treeTemplate);</script></td></tr></table></div></div>");
		}
		else if (type.equals(State.alias)) {
			State e = (State) DatabaseHelper.loadEntity(State.class, id);
			hInfo.put("title", e.getLabel(lang));
			hInfo.put("titleEN", e.getLabel());
			hInfo.put("flag", HtmlUtils.writeImage(ImageUtils.INDEX_STATE, e.getId(), ImageUtils.SIZE_LARGE, null, null));
			hInfo.put("name", "<b>" + e.getLabel(lang).toUpperCase() + "</b>");
			hInfo.put("code", e.getCode());
			hInfo.put("capital", e.getCapital());
			ref = e.getRef();
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Team.alias)) {
			int currentId = id;
			LinkedList<Team> lTeam = new LinkedList<Team>();
			Team e = (Team) DatabaseHelper.loadEntity(Team.class, id);
			if (e.getLink() != null && e.getLink() >= 0) {
				Team e_ = (Team) DatabaseHelper.loadEntity(Team.class, e.getLink());
				String wId = "(-1" + (e_ != null && e_.getId() > 0 ? "," + e_.getId() : "") + (e_ != null && e_.getLink() > 0 ? "," + e_.getLink() : "") + (e.getId() > 0 ? "," + e.getId() : "") + (e.getLink() > 0 ? "," + e.getLink() : "") + ")";
				lTeam.addAll(DatabaseHelper.execute("from Team where id in " + wId + " or link in " + wId + " order by year1 desc, label"));
				id = lTeam.get(0).getId();
			}
			else
				lTeam.add(e);
			String currentLogo = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, e.getId(), ImageUtils.SIZE_LARGE, null, null);
			Collection<String> lAllLogos = ImageUtils.getImageList(ImageUtils.INDEX_TEAM, e.getId(), ImageUtils.SIZE_LARGE);
			ArrayList<Integer> lId = new ArrayList<Integer>();
			Vector<String> vNm = new Vector<String>();
			Vector<Integer> vSp = new Vector<Integer>();
			StringBuffer sbTm = new StringBuffer();
			StringBuffer sbSp = new StringBuffer();
			StringBuffer sbTmFH = new StringBuffer();
			for (Team t : lTeam) {
				lId.add(t.getId());
				ref += (t.getRef() != null ? t.getRef() : 0);
				if (!vNm.contains(t.getLabel())) {
					sbTm.append(sbTm.toString().length() > 0 ? "<br/>" : "").append(t.getLabel().toUpperCase());
					vNm.add(t.getLabel());
				}
				sbTmFH.append((t.getInactive() != null && t.getInactive() ? "&dagger;&nbsp;" : "") + (t.getId() == currentId ? "<b>" : "") + HtmlUtils.writeLink(Team.alias, t.getId(), t.getLabel(), null) + " (" + t.getYear1() + "&nbsp;-&nbsp;" + (StringUtils.notEmpty(t.getYear2()) ? t.getYear2() : ResourceUtils.getText("today", lang)) + ")" + (t.getId() == currentId ? "</b>" : "") + "<br/>");
				if (t.getSport() != null) {
					if (!vSp.contains(t.getSport().getId())) {
						String s = HtmlUtils.writeLink(Sport.alias, t.getSport().getId(), t.getSport().getLabel(lang), t.getSport().getLabel());
						s = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, t.getSport().getId(), ImageUtils.SIZE_SMALL, null, null), s);
						if (StringUtils.notEmpty(sbSp.toString()))
							s = s.replaceAll("\\<table\\>", "<table style='margin-top:2px;'>");
						sbSp.append(s);
						vSp.add(t.getSport().getId());
					}
				}
			}
			String cn = null;
			if (e.getCountry() != null) {
				cn = HtmlUtils.writeLink(Country.alias, e.getCountry().getId(), e.getCountry().getLabel(lang), e.getCountry().getLabel());
				cn = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, e.getCountry().getId(), ImageUtils.SIZE_SMALL, null, null), cn);
			}
			String sp = sbSp.toString();
			hInfo.put("title", e.getLabel());
			hInfo.put("name", "<b>" + (StringUtils.notEmpty(e.getYear1()) ? e.getLabel().toUpperCase() : sbTm.toString()) + "</b>");
			hInfo.put("logo", currentLogo);
			StringBuffer sbOtherLogos = new StringBuffer();
			if (lAllLogos != null && lAllLogos.size() > 1) {
				int nol = 0;
				for (String s : lAllLogos)
					if (s.matches(".*\\d{4}\\-\\d{4}\\.png$")) {
						String s_ = s.substring(s.indexOf("_") + 1, s.lastIndexOf("."));
						if (s_.substring(0, 4).equals(s_.substring(5)))
							s_ = s_.substring(5);
						sbOtherLogos.append("<div class='ol-" + id + "' style='" + (++nol > 3 ? "display:none;" : "") + "float:left;margin:5px;text-align:center;'><img alt='' src='" + ImageUtils.getUrl() + s + "'/>");
						sbOtherLogos.append("<br/><b>" + s_ + "</b></div>");
					}
				if (nol > 3)
					sbOtherLogos.append("<div id='ol-" + id + "-link' class='otherimglink' style='padding-top:110px;'><a href='javascript:moreImg(\"ol-" + id + "\");'>" + ResourceUtils.getText("more", lang) + "</a></div>");
			}
			hInfo.put("otherlogos", sbOtherLogos.toString());
			if (cn != null)
				hInfo.put("country", cn);
			if (sp != null)
				hInfo.put("sport", sp);
			if (StringUtils.notEmpty(e.getConference()))
				hInfo.put("conference", e.getConference());
			if (StringUtils.notEmpty(e.getDivision()))
				hInfo.put("division", e.getDivision());
			if (StringUtils.notEmpty(e.getYear1()))
				hInfo.put("franchist", sbTmFH.toString());
			// Record
			ArrayList<Object> lFuncParams = new ArrayList<Object>();
			lFuncParams.add("TM");
			lFuncParams.add(e.getSport().getId());
			lFuncParams.add(lId.toString().replaceAll("\\[|\\]", "").replaceAll("\\,\\s", "-"));
			Collection<RefItem> cRecord = (Collection<RefItem>) DatabaseHelper.call("GetMedalCount", lFuncParams);
			if (!cRecord.isEmpty())
				hInfo.put("record", HtmlUtils.writeRecordItems(cRecord, lang));
			// Retired Numbers
			List<RetiredNumber> lRn = DatabaseHelper.execute("from RetiredNumber where team.id=" + e.getId() + " order by number");
			if (lRn != null && !lRn.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				for (RetiredNumber rn : lRn) {
					String name = HtmlUtils.writeLink(Athlete.alias, rn.getPerson().getId(), StringUtils.toFullName(rn.getPerson().getLastName(), rn.getPerson().getFirstName(), null, true), rn.getPerson().getFirstName() + " " + rn.getPerson().getLastName());
					sb.append("<i>" + rn.getNumber() + "</i>&nbsp;�&nbsp;" + name).append("<br/>");
				}
				hInfo.put("entity.RN", sb.toString());
			}
			// Team Stadiums
			List<TeamStadium> lTs = DatabaseHelper.execute("from TeamStadium where team.id=" + e.getId() + " order by date1 desc");
			if (lTs != null && !lTs.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				for (TeamStadium ts : lTs) {
					City ct = ts.getComplex().getCity();
					sb.append("<table><tr" + (ts.getDate2() == 0 ? " class='bold'" : "") + ">");
					sb.append("<th>" + HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, ct.getCountry().getId(), ImageUtils.SIZE_SMALL, null, ct.getCountry().getLabel(lang)) + "</th>");
					if (ct.getState() != null)
						sb.append("<th>" + HtmlUtils.writeImage(ImageUtils.INDEX_STATE, ct.getState().getId(), ImageUtils.SIZE_SMALL, null, ct.getState().getLabel(lang)) + "</th>");
					sb.append("<td>" + HtmlUtils.writeLink(Complex.alias, ts.getComplex().getId(), ts.getComplex().getLabel(), ts.getComplex().getLabel()) + "</td>");
					sb.append("<td>,&nbsp;</td><td>" + HtmlUtils.writeLink(City.alias, ct.getId(), ct.getLabel(lang), ct.getLabel()) + "</td>");
					if (ct.getState() != null)
						sb.append("<td>,&nbsp;</td><td>" + HtmlUtils.writeLink(State.alias, ct.getState().getId(), ct.getState().getCode(), ct.getState().getLabel()) + "</td>");
					sb.append("<td>,&nbsp;</td><td>" + HtmlUtils.writeLink(Country.alias, ct.getCountry().getId(), ct.getCountry().getCode(), ct.getCountry().getLabel()) + "</td>");
					sb.append("<td>&nbsp;(" + ts.getDate1() + "&nbsp;&ndash;&nbsp;" + (ts.getDate2() != 0 ? ts.getDate2() : ResourceUtils.getText("today", lang)) + ")</td>");
					sb.append("</tr></table>");
				}
				hInfo.put("entity.TS", sb.toString());
			}
			// Wins/Losses
			List<WinLoss> lWl = DatabaseHelper.execute("from WinLoss where team.id=" + e.getId());
			if (lWl != null && !lWl.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				for (WinLoss wl : lWl)
					sb.append(wl.getType() + "&nbsp;:&nbsp;" + wl.getCountWin() + "-" + wl.getCountLoss() + (wl.getCountTie() != null ? "-" + wl.getCountTie() : "") + (wl.getCountOtloss() != null ? "-" + wl.getCountOtloss() : "")).append("<br/>");
				hInfo.put("entity.WL", sb.toString());
			}
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Year.alias)) {
			Year e = (Year) DatabaseHelper.loadEntity(Year.class, id);
			hInfo.put("title", e.getLabel());
			hInfo.put("name", e.getLabel());
			ref = e.getRef();
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		hInfo.put("url", HtmlUtils.writeLink(type, id, null, hInfo.containsKey("titleEN") ? hInfo.get("titleEN") : hInfo.get("title")));
		if (!type.matches(Contributor.alias + "|" + Sport.alias)) {
			hInfo.put("references", String.valueOf(ref));
			hInfo.put("extlinks", HtmlUtils.writeExternalLinks(type, id, lang));
		}
		if (lastUpdate != null)
			request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "dd/MM/yyyy"));
		return HtmlUtils.writeInfoHeader(hInfo, lang);
	}

	public static StringBuffer getRecordRef(ArrayList<Object> params, Collection<Object> coll, boolean isExport, Contributor m, String lang) throws Exception {
		String etype = String.valueOf(params.get(0));
		boolean isAllRef = (isExport || !StringUtils.notEmpty(params.get(2)));
		String limit = (params.size() > 3 ? String.valueOf(params.get(3)) : "20");
		Integer offset = (params.size() > 4 ? new Integer(String.valueOf(params.get(4))) : 0);
		final int ITEM_LIMIT = 20;
		StringBuffer html = new StringBuffer();
		if (isAllRef)
			html.append("<table class='tsort'>");
		// Resort (results/draws)
		ArrayList<Object> list = new ArrayList<Object>(coll);
		boolean isDraw = false;
		if (list != null && !list.isEmpty()) {
			RefItem item = (RefItem) list.get(0);
			String en = item.getEntity();
			isDraw = (en != null && en.equals(Result.alias) && item.getTxt2() != null && item.getTxt2().matches("(qf|sf|th)(1|2|3|4|d)"));
		}
		if (isDraw) {
			Collections.sort(list, new Comparator<Object>(){
				public int compare(Object o1, Object o2){
					if (((RefItem) o1).getEntity().equals(Result.alias)) {
						int yr1 = ((RefItem) o1).getIdRel1();
						int yr2 = ((RefItem) o2).getIdRel1();
						return (yr1 < yr2 ? 1 : -1);	
					}
					else
						return 0;
				}});
		}

		// Write items
		String currentEntity = "";
		int colspan = 0;
		int count = 0;
		final String MORE_ITEMS = "<tr class='moreitems'><td colspan='#COLSPAN#'><div class='sfdiv1' onclick='moreItems(this, \"#P1#\");'>&nbsp;(+20)&nbsp;</div><div class='sfdiv2' onclick='moreItems(this, \"#P2#\");'>&nbsp;(+100)&nbsp;</div><div class='sfdiv3' onclick='moreItems(this, \"#P3#\");'>&nbsp;(" + ResourceUtils.getText("all", lang) + ")&nbsp;</div></td></tr>";
		String c1 = null, c2 = null, c3 = null, c4 = null, c5 = null, c6 = null, c7 = null;
		for (Object obj : list) {
			RefItem item = (RefItem) obj;
			String en = item.getEntity();
			if (!en.equals(currentEntity)) {
				String id = en + "-" + System.currentTimeMillis();
				StringBuffer cols = new StringBuffer();
				if (en.equals(Athlete.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("country", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("sport", lang) + "</th>");
				if (en.equals(City.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("country", lang) + "</th>");
				else if (en.equals(Complex.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("city", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("country", lang) + "</th>");
				else if (en.equals(Contribution.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("year", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("sport", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("event", lang)  + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("type", lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("date", lang) + "</th>");
				else if (en.equals(HallOfFame.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("league", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>Year</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("inducted.as", lang) + "</th>");
				else if (en.equals(Olympics.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'></th><th onclick='sort(\"" + id + "\", this, 1);'>Type</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("year", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("city", lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("country", lang) + "</th>");
				else if (en.equals(OlympicRanking.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("entity.OL", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("country", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ImageUtils.getGoldHeader(lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ImageUtils.getSilverHeader(lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ImageUtils.getBronzeHeader(lang) + "</th>");
				else if (en.equals(Record.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("sport", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("category", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("scope", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("type", lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("record2", lang) + "</th>");				
				else if (en.equals(Result.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("sport", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("event", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("year", lang)  + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("entity.RS.1", lang) + "</th>" + (etype.matches(Athlete.alias + "|" + Team.alias + "|" + Country.alias) ? "<th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("result.detail", lang) + "</th>" : ""));
				else if (en.equals(RetiredNumber.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("league", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("team", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("number", lang) + "</th>");
				else if (en.equals(Team.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("country", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("sport", lang) + "</th>");
				else if (en.equals(TeamStadium.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("league", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("team", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("complex", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("city", lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("state", lang) + "</th><th onclick='sort(\"" + id + "\", this, 5);'>Country</th><th onclick='sort(\"" + id + "\", this, 6);'>" + ResourceUtils.getText("timespan", lang) + "</th>");
				if (limit != null && !limit.equalsIgnoreCase("ALL") && count >= Integer.parseInt(limit)) {
					String p = params.get(0) + "-" + params.get(1) + "-" + currentEntity + "-#LIMIT#-" + (offset + (!limit.equalsIgnoreCase("all") ? Integer.parseInt(limit) : 0));
					html.append(MORE_ITEMS.replaceAll("#P1#", StringUtils.encode(p.replaceAll("#LIMIT#", "20"))).replaceAll("#P2#", StringUtils.encode(p.replaceAll("#LIMIT#", "100"))).replaceAll("#P3#", StringUtils.encode(p.replaceAll("#LIMIT#", "ALL"))).replaceAll("#COLSPAN#", String.valueOf(colspan)));
				}
				colspan = StringUtils.countIn(cols.toString(), "<th");
				html.append(StringUtils.notEmpty(currentEntity) ? "</tbody></table><table class='tsort'>" : "");
				count = 0;
				if (isAllRef) {
					html.append("<thead><tr><th colspan='" + colspan + "'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText(en.equals(Event.alias) ? "contributions" : "entity." + en, lang).toUpperCase()) + "</th></tr>");
					html.append("<tr class='rsort'>" + cols.toString() + "</tr></thead><tbody class='tby' id='tb-" + id + "'>");	
				}
				currentEntity = en;
			}
			c1 = null;
			c2 = null;
			c3 = null;
			c4 = null;
			c5 = null;
			c6 = null;
			c7 = null;
			if (en.equals(Athlete.alias)) {
				c1 = HtmlUtils.writeLink(Athlete.alias, item.getIdItem(), item.getLabel(), item.getLabelEN());
				c2 = (item.getIdRel1() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel1(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Country.alias, item.getIdRel1(), item.getLabelRel1(), item.getLabelRel3())) : StringUtils.EMPTY);
				c3 = HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel4());
			}
			else if (en.equals(City.alias)) {
				c1 = HtmlUtils.writeLink(City.alias, item.getIdItem(), item.getLabel(), item.getLabelEN());
				c2 = (item.getIdRel1() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel1(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Country.alias, item.getIdRel1(), item.getLabelRel1(), item.getLabelRel2())) : StringUtils.EMPTY);
			}
			else if (en.equals(Complex.alias)) {
				c1 = HtmlUtils.writeLink(Complex.alias, item.getIdItem(), item.getLabel(), item.getLabelEN());
				c2 = HtmlUtils.writeLink(City.alias, item.getIdRel1(), item.getLabelRel1(), item.getLabelRel3());
				c3 = (item.getIdRel2() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel2(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Country.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel4())) : StringUtils.EMPTY);
			}
			else if (en.equals(Contribution.alias)) {
				c1 = HtmlUtils.writeLink(Year.alias, item.getIdRel1(), item.getLabelRel1() + (m != null && m.isSport(item.getIdRel2()) ? "&nbsp;<a href='" + HtmlUtils.writeURL("/update", "RS-" + item.getIdItem(), null) + "'><img alt='modify' title='" + ResourceUtils.getText("button.modify", lang) + "' src='/img/component/button/modify.png'/></a>" : ""), null);
				c2 = HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel6());
				c3 = "<a href='" + HtmlUtils.writeURL("/results", item.getIdRel2() + "-" + item.getIdRel3() + "-" + item.getIdRel4() + (item.getIdRel5() != null ? "-" + item.getIdRel5() : "") + (item.getIdRel18() != null ? "-" + item.getIdRel18() : ""), item.getLabelRel6() + "/" + item.getLabelRel7() + "/" + item.getLabelRel8() + (item.getIdRel5() != null ? "/" + item.getLabelRel9() : "") + (item.getIdRel18() != null ? "/" + item.getLabelRel10() : "")) + "'>" + (item.getLabelRel3() + "&nbsp;-&nbsp;" + item.getLabelRel4() + (item.getIdRel5() != null ? "&nbsp;-&nbsp;" + item.getLabelRel5() : "") + (item.getIdRel18() != null ? "&nbsp;-&nbsp;" + item.getLabelRel18() : "")) + "</a>";
				if (item.getTxt1().equals("A"))
					c4 = "<img style='vertical-align:middle;padding-bottom:2px;' alt='add' src='/img/project/adds.png'/>&nbsp;" + ResourceUtils.getText("co.add", lang);
				else
					c4 = "<img style='vertical-align:middle;padding-bottom:2px;' alt='update' src='/img/project/updates.png'/>&nbsp;" + ResourceUtils.getText("co.update", lang);
				c5 = StringUtils.toTextDate(item.getDate1(), lang, "d MMMM yyyy HH:mm");
			}
			else if (en.equals(HallOfFame.alias)) {
				Short cp = USLeaguesServlet.HLEAGUES.get(Short.valueOf(item.getIdRel3().toString()));
				c1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, cp, ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Championship.alias, cp, item.getComment(), null));
				c2 = HtmlUtils.writeLink(Year.alias, item.getIdRel1(), item.getLabelRel1(), null);
				c3 = HtmlUtils.writeLink(Athlete.alias, item.getIdRel2(), StringUtils.toFullName(item.getLabelRel2(), item.getLabelRel3(), null, true), item.getLabelRel3() + " " + item.getLabelRel2());
				c4 = "-";
				if (StringUtils.notEmpty(item.getTxt1())) {
					StringBuffer sbPos = new StringBuffer();
					for (String s : item.getTxt1().split("\\-"))
						sbPos.append((!sbPos.toString().isEmpty() ? "&nbsp;/&nbsp;" : "") + StringUtils.getUSPosition(item.getIdRel3(), s));
					c4 = sbPos.toString();
				}
			}
			else if (en.equals(Olympics.alias)) {
				c1 = HtmlUtils.writeLink(Olympics.alias, item.getIdItem(), HtmlUtils.writeImage(ImageUtils.INDEX_OLYMPICS, item.getIdItem(), ImageUtils.SIZE_SMALL, null, null), item.getLabelRel1() + " " + item.getLabelRel5());
				c2 = ResourceUtils.getText("olympic.games", lang) + " (" + (item.getComment().equals("1") ? ResourceUtils.getText("summer", lang) : ResourceUtils.getText("winter", lang)) + ")";
				c3 = HtmlUtils.writeLink(Year.alias, item.getIdRel1(), item.getLabelRel1(), null);
				c4 = HtmlUtils.writeLink(City.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel5());
				c5 = (item.getIdRel4() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel4(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), null), HtmlUtils.writeLink(Country.alias, item.getIdRel4(), item.getLabelRel4(), item.getLabelRel6())) : StringUtils.EMPTY);
			}
			else if (en.equals(OlympicRanking.alias)) {
				String[] t = item.getComment().split(",");
				c1 = HtmlUtils.writeLink(Year.alias, item.getIdRel2(), item.getLabelRel2(), null) + "&nbsp;" + HtmlUtils.writeLink(City.alias, item.getIdRel3(), item.getLabelRel3(), item.getLabelRel5());
				c2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel4(), ImageUtils.SIZE_SMALL, item.getLabelRel2(), null), HtmlUtils.writeLink(Country.alias, item.getIdRel4(), item.getLabelRel4(), item.getLabelRel6()));
				c3 = t[0];
				c4 = t[1];
				c5 = t[2];
			}
			else if (en.equals(Record.alias)) {
				Integer lgId = (item.getIdRel2() != null ? (item.getIdRel2() == 51 ? 1 : (item.getIdRel2() == 54 ? 2 : (item.getIdRel2() == 55 ? 3 : 4))) : 0);
				String url = HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_RECORD + "-" + lgId + "-" + item.getIdRel2() + "-" + (item.getIdRel3() == 496 ? "1" : "0") + "-" + item.getIdRel4() + "-" + (item.getTxt1() != null ? item.getTxt1().toLowerCase().charAt(0) : "it") + "-0-" + lang, USLeaguesServlet.TYPE_RECORD + "/" + item.getLabelRel6() + "/" + item.getLabelRel7() + "/" + item.getLabelRel8());
				c1 = HtmlUtils.writeLink(Sport.alias, item.getIdRel1(), item.getLabelRel1(), item.getLabelRel5());
				c2 = "<a href='" + url + "'>" + item.getLabelRel2() + "&nbsp;-&nbsp;" + item.getLabelRel3() + "&nbsp;-&nbsp;" + item.getLabelRel4() + "</a>";
				c3 = (item.getTxt2() != null ? item.getTxt2() : "-");
				c4 = (item.getTxt1() != null ? item.getTxt1() : "-");
				c5 = item.getLabel() + "&nbsp;&ndash;&nbsp;<b>" + item.getTxt3() + "</b>";
			}
			else if (en.equals(Result.alias)) {
				Integer idResult = null;
				String path = item.getLabelRel1() + "/" + item.getLabelRel12() + "/" + item.getLabelRel13() + "/" + item.getLabelRel14() + (item.getIdRel5() != null ? "/" + item.getLabelRel15() : "") + (item.getIdRel18() != null ? "/" + item.getLabelRel16() : "");
				c1 = HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel12());
				c2 = "<a href='" + HtmlUtils.writeURL("/results", item.getIdRel2() + "-" + item.getIdRel3() + "-" + item.getIdRel4() + (item.getIdRel5() != null ? "-" + item.getIdRel5() : "") + (item.getIdRel18() != null ? "-" + item.getIdRel18() : ""), item.getLabelRel12() + "/" + item.getLabelRel13() + "/" + item.getLabelRel14() + (item.getIdRel5() != null ? "/" + item.getLabelRel15() : "") + (item.getIdRel18() != null ? "/" + item.getLabelRel16() : "")) + "'>" + (item.getLabelRel3() + "&nbsp;-&nbsp;" + item.getLabelRel4() + (item.getIdRel5() != null ? "&nbsp;-&nbsp;" + item.getLabelRel5() : "") + (item.getIdRel18() != null ? "&nbsp;-&nbsp;" + item.getLabelRel18() : "")) + "</a>";
				if (item.getIdRel6() != null && item.getTxt2() != null && item.getTxt2().matches("(qf|sf|th)(1|2|3|4|d)")) { // Draw
					idResult = item.getIdRel10();
					String alias = item.getComment();
					String[] tEntity = new String[2];
					String txt2 = item.getTxt2().replaceAll("\\d$", "");
					tEntity[0] = (item.getIdRel6() != null ? HtmlUtils.writeLink(alias, item.getIdRel6(), StringUtils.getShortName(item.getLabelRel6()), item.getLabelRel8()) : null);
					tEntity[1] = (item.getIdRel7() != null ? HtmlUtils.writeLink(alias, item.getIdRel7(), StringUtils.getShortName(item.getLabelRel7()), item.getLabelRel9()) : null);
					short index = (alias.equals(Athlete.alias) || alias.equals(Country.alias) ? ImageUtils.INDEX_COUNTRY : ImageUtils.INDEX_TEAM);
					tEntity[0] = (tEntity[0] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel12() != null ? item.getIdRel12() : item.getIdRel6(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), null), tEntity[0]) : null);
					tEntity[1] = (tEntity[1] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel13() != null ? item.getIdRel13() : item.getIdRel7(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), null), tEntity[1]) : null);
					StringBuffer sb = new StringBuffer("<table><tr>");
					sb.append("<td class='small'>" + ResourceUtils.getText("draw2." + txt2, lang) + "&nbsp;:&nbsp;</td><td style='font-weight:bold;'>" + tEntity[0] + "</td><td>&nbsp;" + item.getTxt1() + "&nbsp;</td><td>" + tEntity[1] + "</td>");
					c5 = sb.append("</tr></table>").toString();
					c4 = ResourceUtils.getText(txt2.equals("thd") ? "rank.3" : "draw3." + txt2, lang);
				}
				else { // Result
					idResult = item.getIdItem();
					String alias = item.getComment();
					boolean isMedal = String.valueOf(item.getIdRel3()).matches("1|3|4");
					String[] tEntity = new String[6];
					tEntity[0] = (item.getIdRel6() != null ? HtmlUtils.writeLink(alias, item.getIdRel6(), StringUtils.getShortName(item.getLabelRel6()), item.getLabelRel20()) : null);
					tEntity[1] = (item.getIdRel7() != null ? HtmlUtils.writeLink(alias, item.getIdRel7(), StringUtils.getShortName(item.getLabelRel7()), item.getLabelRel21()) : null);
					tEntity[2] = (item.getIdRel8() != null ? HtmlUtils.writeLink(alias, item.getIdRel8(), StringUtils.getShortName(item.getLabelRel8()), item.getLabelRel22()) : null);
					tEntity[3] = (item.getIdRel9() != null ? HtmlUtils.writeLink(alias, item.getIdRel9(), StringUtils.getShortName(item.getLabelRel9()), item.getLabelRel23()) : null);
					tEntity[4] = (item.getIdRel10() != null ? HtmlUtils.writeLink(alias, item.getIdRel10(), StringUtils.getShortName(item.getLabelRel10()), item.getLabelRel24()) : null);
					tEntity[5] = (item.getIdRel11() != null ? HtmlUtils.writeLink(alias, item.getIdRel11(), StringUtils.getShortName(item.getLabelRel11()), item.getLabelRel25()) : null);
					short index = (alias.equals(Athlete.alias) || alias.equals(Country.alias) ? ImageUtils.INDEX_COUNTRY : ImageUtils.INDEX_TEAM);
					tEntity[0] = (tEntity[0] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel12() != null ? item.getIdRel12() : item.getIdRel6(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), null), tEntity[0]) : null);
					tEntity[1] = (tEntity[1] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel13() != null ? item.getIdRel13() : item.getIdRel7(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), null), tEntity[1]) : null);
					tEntity[2] = (tEntity[2] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel14() != null ? item.getIdRel14() : item.getIdRel8(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), null), tEntity[2]) : null);
					tEntity[3] = (tEntity[3] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel15() != null ? item.getIdRel15() : item.getIdRel9(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), null), tEntity[3]) : null);
					tEntity[4] = (tEntity[4] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel16() != null ? item.getIdRel16() : item.getIdRel10(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), null), tEntity[4]) : null);
					tEntity[5] = (tEntity[5] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel17() != null ? item.getIdRel17() : item.getIdRel11(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), null), tEntity[5]) : null);
					List<Integer> tieList = getTieList(false, false, item.getTxt4());
					if (tieList != null && !tieList.isEmpty()) {
						Integer idx = tieList.get(0) - 1;
						for (int i = 1 ; i < tieList.size() ; i++) {
							if (idx == null)
								idx = tieList.get(i) - 1;
							else {
								if (tieList.get(i) == -1)
									idx = null;
								if (idx != null) {
									if (idx < tEntity.length && tEntity[idx] != null && (tieList.get(i) - 1) < tEntity.length && tEntity[tieList.get(i) - 1] != null) {
										tEntity[idx] = tEntity[idx].concat(tEntity[tieList.get(i) - 1].replaceAll("<table>", "<table class='margintop'>"));
										tEntity[tieList.get(i) - 1] = null;
									}
								}
							}
						}
						tEntity = StringUtils.removeNulls(tEntity);
					}
					boolean isScore = (tEntity[0] != null && tEntity[1] != null && StringUtils.notEmpty(item.getTxt1()) && !StringUtils.notEmpty(item.getTxt2()));
					//isMedal = false;
					if (isScore && !isMedal) {
						StringBuffer sb = new StringBuffer("<table><tr>");
						sb.append("<td class='small'>" + ResourceUtils.getText("draw2.f", lang) + "&nbsp;:&nbsp;</td><td style='font-weight:bold;'>" + tEntity[0] + "</td><td>&nbsp;" + item.getTxt1() + "&nbsp;</td><td>" + tEntity[1] + "</td>");
						if (tEntity[2] != null)
							sb.append("<td class='small'>&nbsp;" + ResourceUtils.getText("rank.3", lang) + "&nbsp;:&nbsp;</td><td>" + tEntity[2] + "</td>");
						c5 = sb.append("</tr></table>").toString();
					}
					else {
						StringBuffer sb = new StringBuffer("<table><tr>");
						sb.append("<td class='small'>" + (tEntity[0] != null || isMedal ? (isMedal ? ResourceUtils.getText("gold", lang) + "&nbsp;:</td><td class='small'>" + ImageUtils.getGoldMedImg(lang) + "&nbsp;" : (tEntity[1] != null ? ResourceUtils.getText("rank.1", lang) + "&nbsp;:&nbsp;" : "")) : "") + "</td><td style='font-weight:bold;'>" + tEntity[0] + "</td>");
						if (tEntity[1] != null)
							sb.append("<td class='small'>&nbsp;" + (isMedal ? ResourceUtils.getText("silver", lang) + "&nbsp;:</td><td class='small'>" + ImageUtils.getSilverMedImg(lang) : ResourceUtils.getText("rank.2", lang) + ":") + "&nbsp;</td><td>" + tEntity[1] + "</td>");
						if (tEntity[2] != null)
							sb.append("<td class='small'>&nbsp;" + (isMedal ? ResourceUtils.getText("bronze", lang) + "&nbsp;:</td><td class='small'>" + ImageUtils.getBronzeMedImg(lang) : ResourceUtils.getText("rank.3", lang) + ":") + "&nbsp;</td><td>" + tEntity[2] + "</td>");
						c5 = sb.append("</tr></table>").toString();
					}
					if (etype.matches(Athlete.alias + "|" + Team.alias + "|" + Country.alias) && item.getCount1() != null) {
						if (isMedal)
							c4 = "<ul class='vcenter'>" + (item.getCount1() == 1 ? "<li>" + ImageUtils.getGoldMedImg(lang) + "</li><li>&nbsp;" + ResourceUtils.getText("gold", lang) : (item.getCount1() == 2 ? "<li>" + ImageUtils.getSilverMedImg(lang) + "</li><li>&nbsp;" + ResourceUtils.getText("silver", lang) : "<li>" + ImageUtils.getBronzeMedImg(lang) + "</li><li>&nbsp;" + ResourceUtils.getText("bronze", lang))) + "</li></ul>";
						else if (isScore)
							c4 = ResourceUtils.getText(item.getCount1() == 1 ? "rank.winner" : (item.getCount1() == 2 ? "draw3.f" : "rank.3"), lang);
						else
							c4 = ResourceUtils.getText("rank." + item.getCount1(), lang);						
					}
				}
				if (!etype.matches(Athlete.alias + "|" + Team.alias + "|" + Country.alias)) {
					c4 = c5;
					c5 = null;
				}
				c3 = "<table><tr><td>" + HtmlUtils.writeLink(Year.alias, item.getIdRel1(), item.getLabelRel1(), null) + "</td><td>&nbsp;" + HtmlUtils.writeLink(Result.alias, idResult, "<img alt='details' title='" +  ResourceUtils.getText("details", lang) + "' src='/img/render/details.png'/>", path) + "</td><td>" + (m != null && m.isSport(item.getIdRel2()) ? "&nbsp;<a href='" + HtmlUtils.writeURL("/update", "RS-" + idResult, null) + "'><img alt='modify' title='" + ResourceUtils.getText("button.modify", lang) + "' src='/img/component/button/modify.png'/></a>" : "") + "</td></tr></table>";
			}
			else if (en.equals(RetiredNumber.alias)) {
				Short cp = USLeaguesServlet.HLEAGUES.get(Short.valueOf(item.getIdRel3().toString()));
				c1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, cp, ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Championship.alias, cp, item.getComment(), null));
				c2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdRel1(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Team.alias, item.getIdRel1(), item.getLabelRel1(), null));
				c3 = HtmlUtils.writeLink(Athlete.alias, item.getIdRel2(), StringUtils.toFullName(item.getLabelRel2(), item.getLabelRel3(), null, true), item.getLabelRel3() + " " + item.getLabelRel2());
				c4 = String.valueOf(item.getIdRel4());
			}
			else if (en.equals(Team.alias)) {
				c1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdItem(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Team.alias, item.getIdItem(), item.getLabel(), null));
				c2 = (item.getIdRel1() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel1(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Country.alias, item.getIdRel1(), item.getLabelRel1(), item.getLabelRel3())) : StringUtils.EMPTY);
				c3 = HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel4());
			}
			else if (en.equals(TeamStadium.alias)) {
				Short cp = USLeaguesServlet.HLEAGUES.get(Short.valueOf(item.getIdRel6().toString()));
				c1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, cp, ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Championship.alias, cp, item.getComment(), null));
				c2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdRel1(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Team.alias, item.getIdRel1(), item.getLabelRel1(), null));
				c3 = HtmlUtils.writeLink(Complex.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel6());
				c4 = HtmlUtils.writeLink(City.alias, item.getIdRel3(), item.getLabelRel3(), item.getLabelRel7());
				c5 = (item.getIdRel4() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_STATE, item.getIdRel4(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(State.alias, item.getIdRel4(), item.getLabelRel4(), item.getLabelRel8())) : StringUtils.EMPTY);
				c6 = (item.getIdRel5() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel5(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Country.alias, item.getIdRel5(), item.getLabelRel5(), item.getLabelRel9())) : StringUtils.EMPTY);
				c7 = item.getTxt1() + "&nbsp;-&nbsp;" + (StringUtils.notEmpty(item.getTxt2()) && !item.getTxt2().equals("0") ? item.getTxt2() : ResourceUtils.getText("today", lang));
			}
			if (isExport || !isAllRef || count < ITEM_LIMIT * (isDraw ? 2 : 1)) {
				html.append("<tr>" + (c1 != null ? "<td class='srt'>" + c1 + "</td>" : "") + (c2 != null ? "<td class='srt'>" + c2 + "</td>" : ""));
				html.append((c3 != null ? "<td class='srt'>" + c3 + "</td>" : "") + (c4 != null ? "<td class='srt'>" + c4 + "</td>" : ""));
				html.append((c5 != null ? "<td class='srt'>" + c5 + "</td>" : "") + (c6 != null ? "<td class='srt'>" + c6 + "</td>" : ""));
				html.append((c7 != null ? "<td class='srt'>" + c7 + "</td>" : "") + "</tr>");
			}
			count++;
		}
		if (limit != null && !limit.equalsIgnoreCase("ALL") && count >= Integer.parseInt(limit)) {
			String p = params.get(0) + "-" + params.get(1) + "-" + currentEntity + "-#LIMIT#-" + (offset + (!limit.equalsIgnoreCase("all") ? Integer.parseInt(limit) : 0));
			html.append(MORE_ITEMS.replaceAll("#P1#", StringUtils.encode(p.replaceAll("#LIMIT#", "20"))).replaceAll("#P2#", StringUtils.encode(p.replaceAll("#LIMIT#", "100"))).replaceAll("#P3#", StringUtils.encode(p.replaceAll("#LIMIT#", "ALL"))).replaceAll("#COLSPAN#", String.valueOf(colspan)));
		}
		html.append(isAllRef ? "</tbody></table>" : "");
		return html;
	}

	public static StringBuffer convertResults(Collection<Object> coll, Championship cp, Event ev, Contributor m, String lang) throws Exception {
		if (coll == null || coll.isEmpty())
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		// Evaluate columns
		short entityCount = 0;
		short[] tColspan = {1, 1, 1, 1, 1, 1, 1, 1, 1};
		boolean[] tIsEntityRel1 = {false, false, false, false, false, false, false, false, false};
		boolean[] tIsEntityRel2 = {false, false, false, false, false, false, false, false, false};
		boolean[] tIsResult = {false, false, false, false, false, false, false, false, false};
		boolean isDates = false;
		boolean isPlace = false;
		boolean isComment = false;
		int type = ev.getType().getNumber();
		boolean isDouble = (type == 4);
		boolean isMedal = String.valueOf(cp.getId()).matches("1|3|4");
		ArrayList<String> lIds = new ArrayList<String>();
		Result rs = null;
		for (Object obj : coll) {
			ResultsBean bean = (ResultsBean) obj;
			lIds.add(String.valueOf(bean.getRsId()));
			if (rs == null)
				rs = (Result) DatabaseHelper.loadEntity(Result.class, bean.getRsId());
			isDouble = (bean.getRsComment() != null && bean.getRsComment().equals("#DOUBLE#") ? true : (type == 4));
			boolean isTriple = (bean.getRsComment() != null && bean.getRsComment().equals("#TRIPLE#") ? true : (type == 5));
			List<Integer> tieList = getTieList(isDouble, isTriple, bean.getRsExa());
			String sTieList = tieList.toString();
			entityCount = (entityCount < 1 && bean.getRsRank1() != null ? 1 : entityCount);
			entityCount = (entityCount < 2 && bean.getRsRank2() != null && (tieList.indexOf(2) <= 0 || StringUtils.countIn(sTieList, "-1") >= 1) ? 2 : entityCount);
			entityCount = (entityCount < 3 && bean.getRsRank3() != null && (tieList.indexOf(3) <= 0 || StringUtils.countIn(sTieList, "-1") >= 2) ? 3 : entityCount);
			entityCount = (entityCount < 4 && bean.getRsRank4() != null && (tieList.indexOf(4) <= 0 || StringUtils.countIn(sTieList, "-1") >= 3) ? 4 : entityCount);
			entityCount = (entityCount < 5 && bean.getRsRank5() != null && (tieList.indexOf(5) <= 0 || StringUtils.countIn(sTieList, "-1") >= 4) ? 5 : entityCount);
			entityCount = (entityCount < 6 && bean.getRsRank6() != null && (tieList.indexOf(6) <= 0 || StringUtils.countIn(sTieList, "-1") >= 5) ? 6 : entityCount);
			tColspan[0] = (tColspan[0] < 2 && StringUtils.notEmpty(bean.getRsResult1()) ? 2 : tColspan[0]);
			tColspan[1] = (tColspan[1] < 2 && StringUtils.notEmpty(bean.getRsResult2()) ? 2 : tColspan[1]);
			tColspan[2] = (tColspan[2] < 2 && StringUtils.notEmpty(bean.getRsResult3()) ? 2 : tColspan[2]);
			tColspan[3] = (tColspan[3] < 2 && StringUtils.notEmpty(bean.getRsResult4()) ? 2 : tColspan[3]);
			tColspan[4] = (tColspan[4] < 2 && StringUtils.notEmpty(bean.getRsResult5()) ? 2 : tColspan[4]);
			tIsEntityRel1[0] |= (bean.getEn1Rel1Id() != null);tIsEntityRel2[0] |= (bean.getEn1Rel2Id() != null);
			tIsEntityRel1[1] |= (bean.getEn2Rel1Id() != null);tIsEntityRel2[1] |= (bean.getEn2Rel2Id() != null);
			tIsEntityRel1[2] |= (bean.getEn3Rel1Id() != null);tIsEntityRel2[2] |= (bean.getEn3Rel2Id() != null);
			tIsEntityRel1[3] |= (bean.getEn4Rel1Id() != null);tIsEntityRel2[3] |= (bean.getEn4Rel2Id() != null);
			tIsEntityRel1[4] |= (bean.getEn5Rel1Id() != null);tIsEntityRel2[4] |= (bean.getEn5Rel2Id() != null);
			tIsEntityRel1[5] |= (bean.getEn6Rel1Id() != null);tIsEntityRel2[5] |= (bean.getEn6Rel2Id() != null);
			tIsEntityRel1[6] |= (bean.getEn7Rel1Id() != null);tIsEntityRel2[6] |= (bean.getEn7Rel2Id() != null);
			tIsEntityRel1[7] |= (bean.getEn8Rel1Id() != null);tIsEntityRel2[7] |= (bean.getEn8Rel2Id() != null);
			tIsEntityRel1[8] |= (bean.getEn9Rel1Id() != null);tIsEntityRel2[8] |= (bean.getEn9Rel2Id() != null);
			tIsResult[0] |= (StringUtils.notEmpty(bean.getRsResult1()));
			tIsResult[1] |= (StringUtils.notEmpty(bean.getRsResult2()));
			tIsResult[2] |= (StringUtils.notEmpty(bean.getRsResult3()));
			tIsResult[3] |= (StringUtils.notEmpty(bean.getRsResult4()));
			tIsResult[4] |= (StringUtils.notEmpty(bean.getRsResult5()));
			isDates |= StringUtils.notEmpty(bean.getRsDate2());
			isPlace |= (bean.getCx1Id() != null || bean.getCx2Id() != null || bean.getCt2Id() != null || bean.getCt4Id() != null);
			isComment |= (StringUtils.notEmpty(bean.getRsComment()) && !bean.getRsComment().matches("\\#(DOUBLE|TRIPLE)\\#"));
		}
		//entityCount /= (isDouble ? 2 : 1);
		tColspan[0] += (tIsEntityRel1[0] ? 1 : 0) + (tIsEntityRel2[0] ? 1 : 0);
		tColspan[1] += (tIsEntityRel1[1] ? 1 : 0) + (tIsEntityRel2[1] ? 1 : 0);
		tColspan[2] += (tIsEntityRel1[2] ? 1 : 0) + (tIsEntityRel2[2] ? 1 : 0);
		tColspan[3] += (tIsEntityRel1[3] ? 1 : 0) + (tIsEntityRel2[3] ? 1 : 0);
		tColspan[4] += (tIsEntityRel1[4] ? 1 : 0) + (tIsEntityRel2[4] ? 1 : 0);
		tColspan[5] += (tIsEntityRel1[5] ? 1 : 0) + (tIsEntityRel2[5] ? 1 : 0);
		tColspan[6] += (tIsEntityRel1[6] ? 1 : 0) + (tIsEntityRel2[6] ? 1 : 0);
		tColspan[7] += (tIsEntityRel1[7] ? 1 : 0) + (tIsEntityRel2[7] ? 1 : 0);
		tColspan[8] += (tIsEntityRel1[8] ? 1 : 0) + (tIsEntityRel2[8] ? 1 : 0);
		boolean isScore = (entityCount > 1 && tIsResult[0] && !tIsResult[1] && !tIsResult[2] && !tIsResult[3] && !tIsResult[4]);
		tColspan[0] -= (isScore ? 1 : 0);
		String path = rs.getSport().getLabel() + "/" + rs.getChampionship().getLabel() + "/" + rs.getEvent().getLabel() + (rs.getSubevent() != null ? "/" + rs.getSubevent().getLabel() : "") + (rs.getSubevent2() != null ? "/" + rs.getSubevent2().getLabel() : "");

		long id = System.currentTimeMillis();
		Map<Integer, List<StringBuffer>> mpl = getPersonLists(StringUtils.implode(lIds, ","));
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		html.append("<thead><tr class='rsort'>" + (isComment ? "<th/>" : "") + "<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("year", lang) + "</th>");
		for (int i = 1 ; i <= entityCount ; i++)
			html.append(i == 2 && isScore ? "<th onclick='sort(\"" + id + "\", this, " + i + ");'>" + ResourceUtils.getText("score", lang) + "</th>" : "").append("<th colspan='" + tColspan[i - 1] + "' onclick='sort(\"" + id + "\", this, " + i + ");'>" + (isMedal ? (i == 1 ? ImageUtils.getGoldHeader(lang) : (i == 2 ? ImageUtils.getSilverHeader(lang) : ImageUtils.getBronzeHeader(lang))) : ResourceUtils.getText("rank." + i, lang)) + "</th>");
		if (isDates)
			html.append("<th onclick='sort(\"" + id + "\", this, " + (entityCount + 1) + ");'>" + ResourceUtils.getText("date", lang) + "</th>");
		if (isPlace)
			html.append("<th onclick='sort(\"" + id + "\", this, " + (entityCount + (isDates ? 1 : 0) + 1) + ");'>" + ResourceUtils.getText("place", lang) + "</th>");
		html.append("</tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			ResultsBean bean = (ResultsBean) obj;

			// Evaluate bean
			String year = HtmlUtils.writeLink(Year.alias, bean.getYrId(), bean.getYrLabel(), null);
			year = "<table><tr><td>" + year + "</td><td>&nbsp;" + HtmlUtils.writeLink(Result.alias, bean.getRsId(), "<img alt='details' title='" +  ResourceUtils.getText("details", lang) + " (" + bean.getYrLabel() + ")' src='/img/render/details.png'/>", bean.getYrLabel() + "/" + path) + "</td><td>" + (m != null && m.isSport(rs.getSport().getId()) ? "&nbsp;<a href='" + HtmlUtils.writeURL("/update", "RS-" + bean.getRsId(), null) + "'><img alt='modify' title='" + ResourceUtils.getText("button.modify", lang) + "' src='/img/component/button/modify.png'/></a>" : "") + "</td></tr></table>";
			String dates = (StringUtils.notEmpty(bean.getRsDate1()) ? StringUtils.toTextDate(bean.getRsDate1(), lang, "d MMM yyyy") + "<br/>" : "") + (StringUtils.notEmpty(bean.getRsDate2()) ? StringUtils.toTextDate(bean.getRsDate2(), lang, "d MMM yyyy") : "");
			String place1 = null, place2 = null;
			String comment = (StringUtils.notEmpty(bean.getRsComment()) && !bean.getRsComment().matches("\\#(DOUBLE|TRIPLE)\\#") ? bean.getRsComment().replaceAll("\r\n|\\|", "<br/>") : null);
			boolean isResultEmpty = (bean.getRsRank1() == null && bean.getRsRank2() == null && bean.getRsRank3() == null && bean.getRsRank4() == null && bean.getRsRank5() == null);
			List<StringBuffer> plist = mpl.get(bean.getRsId());
			if (bean.getCx1Id() != null)
				place1 = getPlace(bean.getCx1Id(), bean.getCt1Id(), bean.getSt1Id(), bean.getCn1Id(), bean.getCx1Label(), bean.getCt1Label(), bean.getSt1Code(), bean.getCn1Code(), bean.getCx1LabelEN(), bean.getCt1LabelEN(), bean.getSt1LabelEN(), bean.getCn1LabelEN(), bean.getYrLabel());
			else if (bean.getCt2Id() != null) 
				place1 = getPlace(null, bean.getCt2Id(), bean.getSt2Id(), bean.getCn2Id(), null, bean.getCt2Label(), bean.getSt2Code(), bean.getCn2Code(), null, bean.getCt2LabelEN(), bean.getSt2LabelEN(), bean.getCn2LabelEN(), bean.getYrLabel());
			if (bean.getCx2Id() != null) 
				place2 = getPlace(bean.getCx2Id(), bean.getCt3Id(), bean.getSt3Id(), bean.getCn3Id(), bean.getCx2Label(), bean.getCt3Label(), bean.getSt3Code(), bean.getCn3Code(), bean.getCx2LabelEN(), bean.getCt3LabelEN(), bean.getSt3LabelEN(), bean.getCn3LabelEN(), bean.getYrLabel());
			else if (bean.getCt4Id() != null)
				place2 = getPlace(null, bean.getCt4Id(), bean.getSt4Id(), bean.getCn4Id(), null, bean.getCt4Label(), bean.getSt4Code(), bean.getCn4Code(), null, bean.getCt4LabelEN(), bean.getSt4LabelEN(), bean.getCn4LabelEN(), bean.getYrLabel());				
			boolean isTriple = (bean.getRsComment() != null && bean.getRsComment().equals("#TRIPLE#") ? true : (type == 5));
			isDouble = (bean.getRsComment() != null && bean.getRsComment().equals("#DOUBLE#") ? true : (type == 4));
			List<Integer> tieList = getTieList(isDouble, isTriple, bean.getRsExa());
			String[] tEntity = {null, null, null, null, null, null, null, null, null};
			String[] tEntityRel = {null, null, null, null, null, null, null, null, null};
			String[] tEntityHtml = {null, null, null, null, null, null, null, null, null};
			String[] tResult = {null, null, null, null, null, null, null, null, null};
			String[] tLN = {null, null, null};
			if (bean.getRsRank1() != null) {
				tEntity[0] = getResultsEntity(type, bean.getRsRank1(), bean.getEn1Str1(), bean.getEn1Str2(), bean.getEn1Str3(), bean.getEn1Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 0 ? "plist-" + bean.getRsId() + "-0" : null);
				tEntityRel[0] = getResultsEntityRel(bean.getEn1Rel1Id(), bean.getEn1Rel1Code(), bean.getEn1Rel1Label(), bean.getEn1Rel2Id(), bean.getEn1Rel2Code(), bean.getEn1Rel2Label(), bean.getEn1Rel2LabelEN(), tIsEntityRel1[0], tIsEntityRel2[0], bean.getYrLabel());
				tResult[0] = bean.getRsResult1();
				tLN[0] = (type < 10 ? bean.getEn1Str1() + "-" + bean.getRsId() : null);
			}
			if (bean.getRsRank2() != null) {
				tEntity[1] = getResultsEntity(type, bean.getRsRank2(), bean.getEn2Str1(), bean.getEn2Str2(), bean.getEn2Str3(), bean.getEn2Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 1 ? "plist-" + bean.getRsId() + "-1" : null);
				tEntityRel[1] = getResultsEntityRel(bean.getEn2Rel1Id(), bean.getEn2Rel1Code(), bean.getEn2Rel1Label(), bean.getEn2Rel2Id(), bean.getEn2Rel2Code(), bean.getEn2Rel2Label(), bean.getEn2Rel2LabelEN(), tIsEntityRel1[1], tIsEntityRel2[1], bean.getYrLabel());
				tResult[1] = bean.getRsResult2();
				tLN[1] = (type < 10 ? bean.getEn2Str1() + "-" + bean.getRsId() : null);
			}
			if (bean.getRsRank3() != null) {
				tEntity[2] = getResultsEntity(type, bean.getRsRank3(), bean.getEn3Str1(), bean.getEn3Str2(), bean.getEn3Str3(), bean.getEn3Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 2 ? "plist-" + bean.getRsId() + "-2" : null);
				tEntityRel[2] = getResultsEntityRel(bean.getEn3Rel1Id(), bean.getEn3Rel1Code(), bean.getEn3Rel1Label(), bean.getEn3Rel2Id(), bean.getEn3Rel2Code(), bean.getEn3Rel2Label(), bean.getEn3Rel2LabelEN(), tIsEntityRel1[2], tIsEntityRel2[2], bean.getYrLabel());
				tResult[2] = bean.getRsResult3();
				tLN[2] = (type < 10 ? bean.getEn3Str1() + "-" + bean.getRsId() : null);
			}
			if (bean.getRsRank4() != null) {
				tEntity[3] = getResultsEntity(type, bean.getRsRank4(), bean.getEn4Str1(), bean.getEn4Str2(), bean.getEn4Str3(), bean.getEn4Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 3 ? "plist-" + bean.getRsId() + "-3" : null);
				tEntityRel[3] = getResultsEntityRel(bean.getEn3Rel1Id(), bean.getEn4Rel1Code(), bean.getEn4Rel1Label(), bean.getEn4Rel2Id(), bean.getEn4Rel2Code(), bean.getEn4Rel2Label(), bean.getEn4Rel2LabelEN(), tIsEntityRel1[3], tIsEntityRel2[3], bean.getYrLabel());
				tResult[3] = bean.getRsResult4();
			}
			if (bean.getRsRank5() != null) {
				tEntity[4] = getResultsEntity(type, bean.getRsRank5(), bean.getEn5Str1(), bean.getEn5Str2(), bean.getEn5Str3(), bean.getEn5Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 4 ? "plist-" + bean.getRsId() + "-4" : null);
				tEntityRel[4] = getResultsEntityRel(bean.getEn5Rel1Id(), bean.getEn5Rel1Code(), bean.getEn5Rel1Label(), bean.getEn5Rel2Id(), bean.getEn5Rel2Code(), bean.getEn5Rel2Label(), bean.getEn5Rel2LabelEN(), tIsEntityRel1[4], tIsEntityRel2[4], bean.getYrLabel());
				tResult[4] = bean.getRsResult5();
			}
			if (bean.getRsRank6() != null) {
				tEntity[5] = getResultsEntity(type, bean.getRsRank6(), bean.getEn6Str1(), bean.getEn6Str2(), bean.getEn6Str3(), bean.getEn6Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 5 ? "plist-" + bean.getRsId() + "-5" : null);
				tEntityRel[5] = getResultsEntityRel(bean.getEn6Rel1Id(), bean.getEn6Rel1Code(), bean.getEn6Rel1Label(), bean.getEn6Rel2Id(), bean.getEn6Rel2Code(), bean.getEn6Rel2Label(), bean.getEn6Rel2LabelEN(), tIsEntityRel1[5], tIsEntityRel2[5], bean.getYrLabel());
			}
			if (bean.getRsRank7() != null) {
				tEntity[6] = getResultsEntity(type, bean.getRsRank7(), bean.getEn7Str1(), bean.getEn7Str2(), bean.getEn7Str3(), bean.getEn7Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 6 ? "plist-" + bean.getRsId() + "-6" : null);
				tEntityRel[6] = getResultsEntityRel(bean.getEn7Rel1Id(), bean.getEn7Rel1Code(), bean.getEn7Rel1Label(), bean.getEn7Rel2Id(), bean.getEn7Rel2Code(), bean.getEn7Rel2Label(), bean.getEn7Rel2LabelEN(), tIsEntityRel1[6], tIsEntityRel2[6], bean.getYrLabel());
			}
			if (bean.getRsRank8() != null) {
				tEntity[7] = getResultsEntity(type, bean.getRsRank8(), bean.getEn8Str1(), bean.getEn8Str2(), bean.getEn8Str3(), bean.getEn8Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 7 ? "plist-" + bean.getRsId() + "-7" : null);
				tEntityRel[7] = getResultsEntityRel(bean.getEn8Rel1Id(), bean.getEn8Rel1Code(), bean.getEn8Rel1Label(), bean.getEn8Rel2Id(), bean.getEn8Rel2Code(), bean.getEn8Rel2Label(), bean.getEn8Rel2LabelEN(), tIsEntityRel1[7], tIsEntityRel2[7], bean.getYrLabel());
			}
			if (bean.getRsRank9() != null) {
				tEntity[8] = getResultsEntity(type, bean.getRsRank9(), bean.getEn9Str1(), bean.getEn9Str2(), bean.getEn9Str3(), bean.getEn9Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 8 ? "plist-" + bean.getRsId() + "-8" : null);
				tEntityRel[8] = getResultsEntityRel(bean.getEn9Rel1Id(), bean.getEn9Rel1Code(), bean.getEn9Rel1Label(), bean.getEn9Rel2Id(), bean.getEn9Rel2Code(), bean.getEn9Rel2Label(), bean.getEn9Rel2LabelEN(), tIsEntityRel1[8], tIsEntityRel2[8], bean.getYrLabel());
			}
			setTies(tieList, type, tEntity, tEntityRel);
			if (isTriple || isDouble) {
				tEntity = StringUtils.removeNulls(tEntity);
				tEntityRel = StringUtils.removeNulls(tEntityRel);					
			}
			for (int i = 0 ; i < 9 ; i++)
				if (tEntity[i] != null)
					tEntityHtml[i] = ("<td" + (i < 3 && StringUtils.notEmpty(tLN[i]) ? " id=\"" + tLN[i].replaceAll("\\s", "-") + "\"" : "") + " class='srt'" + (i == 0 ? " style='font-weight:bold;'" : "") + ">" + tEntity[i] + (plist != null && plist.size() > i ? "<table id='plist-" + bean.getRsId() + "-" + i + "' class='plist' style='display:none;'>" + plist.get(i).toString() + "</table>" : "")  + "</td>" + (StringUtils.notEmpty(tEntityRel[i]) ?  tEntityRel[i] : (tIsEntityRel1[i] ? "<td></td>" : "") + (tIsEntityRel2[i] ? "<td></td>" : ""))) + (StringUtils.notEmpty(tResult[i]) ? "<td" + (isScore && i == 0 ? " class='centered'" : "") + ">" + tResult[i].replaceAll("\\s", "&nbsp;").replaceAll("\\-", "&#8209;") + "</td>" : (tIsResult[i] ? "<td></td>" : ""));
				
			String commentTitle = null;
			String commentColor = null;
			if (comment != null && comment.matches("^\\#\\#(Clay|Decoturf|Grass|Gravel|Hard|Rebound|Snow|Tarmac).*")) {
				String cmt = comment.substring(2).replaceAll("\\s", "").toLowerCase();
				commentTitle = ResourceUtils.getText("info." + cmt, lang);
				commentColor = StringUtils.getCommentColor(cmt);
				comment = null;
			}
			
			// Write line
			html.append("<tr>");
			html.append(isComment ? "<td" + (StringUtils.notEmpty(commentTitle) ? " title='" + commentTitle + "' style='width:15px;background-color:" + commentColor + ";'" : "") + ">" + (StringUtils.notEmpty(comment) && !isResultEmpty ? HtmlUtils.writeComment(bean.getRsId(), comment) : "") + "</td>" : "");
			html.append("<td class='srt'>" + year + "</td>");
			if (isResultEmpty && StringUtils.notEmpty(bean.getRsComment()))
				html.append("<td colspan='" + (tColspan[0] + (entityCount > 1 ? tColspan[1] : 0) + (entityCount > 2 ? tColspan[2] : 0) + (isDates ? 1 : 0) + (isPlace ? 1 : 0) + (isScore ? 1 : 0)) + "'>" + bean.getRsComment().replaceAll("\r\n|\\|", "<br/>") + "</td>");
			else {
				for (int i = 0 ; i < 9 ; i++)
					html.append(tEntityHtml[i] != null ? tEntityHtml[i] : (entityCount > i ? "<td class='srt'" + (tColspan[i] > 1 ? " colspan='" + tColspan[i] + "'" : "") + ">" + StringUtils.EMPTY + "</td>" + (isScore && i == 0 ? "<td class='srt'>" + StringUtils.EMPTY + "</td>" : "") : ""));
				html.append(isDates ? "<td class='srt'>" + (StringUtils.notEmpty(dates) ? dates : "") + "</td>" : "");
				html.append((isPlace ? "<td class='srt'>" + (StringUtils.notEmpty(place1) ? place1 : "") + (StringUtils.notEmpty(place2) ? place2 : "") + "</td>" : "") + "</tr>");				
			}
		}
		html.append("</tbody></table>");
		html.append(getWinRecords(StringUtils.implode(lIds, ","), lang));
		return html;
	}

	public static void convertTreeArray(Collection<Object> coll, Writer writer, boolean encode) throws IOException {
		writer.write("treeItems=[" + (!encode ? "['',null," : ""));
		ArrayList<Object> lst = new ArrayList<Object>(coll);
		int i, j, k, l, m;
		for (i = 0 ; i < lst.size() ; i++) {
			TreeItem item = (TreeItem) lst.get(i);
			writer.write(i > 0 ? "," : "");
			writer.write("['" + StringUtils.toTree(item.getLabel()) + "','" + item.getIdItem() + "',");
			for (j = i + 1 ; j < lst.size() ; j++) {
				TreeItem item2 = (TreeItem) lst.get(j);
				if (item2.getLevel() < 2) {j--; break;}
				writer.write(j > i + 1 ? "," : "");
				writer.write("['" + StringUtils.toTree(item2.getLabel()) + "','" + (encode ? "link-" + StringUtils.urlEscape(item.getLabelEN() + "/" + item2.getLabelEN()) + "/" + StringUtils.encode(item.getIdItem() + "-" + item2.getIdItem()) : item.getIdItem() + "_" + item2.getIdItem()) + "',");
				for (k = j + 1 ; k < lst.size() ; k++) {
					TreeItem item3 = (TreeItem) lst.get(k);
					if (item3.getLevel() < 3) {k--; break;}
					writer.write(k > j + 1 ? "," : "");
					writer.write("['" + StringUtils.toTree(item3.getLabel()) + "','" + (encode ? "link-" + StringUtils.urlEscape(item.getLabelEN() + "/" + item2.getLabelEN() + "/" + item3.getLabelEN()) + "/" + StringUtils.encode(item.getIdItem() + "-" + item2.getIdItem() + "-" + item3.getIdItem()) : item.getIdItem() + "_" + item2.getIdItem() + "_" + item3.getIdItem()) + "',");
					for (l = k + 1 ; l < lst.size() ; l++) {
						TreeItem item4 = (TreeItem) lst.get(l);
						if (item4.getLevel() < 4) {l--; break;}
						writer.write(l > k + 1 ? "," : "");
						writer.write("['" + StringUtils.toTree(item4.getLabel()) + "','" + (encode ? "link-" + StringUtils.urlEscape(item.getLabelEN() + "/" + item2.getLabelEN() + "/" + item3.getLabelEN() + "/" + item4.getLabelEN()) + "/" + StringUtils.encode(item.getIdItem() + "-" + item2.getIdItem() + "-" + item3.getIdItem() + "-" + item4.getIdItem()) : item.getIdItem() + "_" + item2.getIdItem() + "_" + item3.getIdItem() + "_" + item4.getIdItem()) + "',");
						for (m = l + 1 ; m < lst.size() ; m++) {
							TreeItem item5 = (TreeItem) lst.get(m);
							if (item5.getLevel() < 5) {m--; break;}
							writer.write(m > l + 1 ? "," : "");
							writer.write("['" + StringUtils.toTree(item5.getLabel()) + "','" + (encode ? "link-" + StringUtils.urlEscape(item.getLabelEN() + "/" + item2.getLabelEN() + "/" + item3.getLabelEN() + "/" + item4.getLabelEN() + "/" + item5.getLabelEN()) + "/" + StringUtils.encode(item.getIdItem() + "-" + item2.getIdItem() + "-" + item3.getIdItem() + "-" + item4.getIdItem() + "-" + item5.getIdItem()) : item.getIdItem() + "_" + item2.getIdItem() + "_" + item3.getIdItem() + "_" + item4.getIdItem() + "_" + item5.getIdItem()) + "']");
						}
						l = m;
						writer.write("]");
					}
					k = l;
					writer.write("]");
				}
				j = k;
				writer.write("]");
			}
			i = j;
			writer.write("]");
		}
		writer.write((!encode ? "]" : "") + "];");
	}

	public static StringBuffer convertSearch(Collection<Object> coll, String pattern, String lang) throws Exception {
		if (coll == null || coll.isEmpty())
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		String currentEntity = "";
		HashMap<String, Integer> hCount = new HashMap<String, Integer>();
		// Evaluate items
		for (Object obj : coll) {
			String s = ((RefItem) obj).getEntity();
			s = (s.equals(Championship.alias) ? Event.alias : s);
			if (!hCount.containsKey(s))
				hCount.put(s, 0);
			hCount.put(s, hCount.get(s) + 1);
		}
		// Write items
		String img = null;
		String link = null;
		for (Object obj : coll) {
			RefItem item = (RefItem) obj;
			String en = item.getEntity();
			en = (en.equals(Championship.alias) ? Event.alias : en);
			if (!en.equals(currentEntity)) {
				String id = en + "-" + System.currentTimeMillis();
				int sortIndex = 0;
				html.append(StringUtils.notEmpty(currentEntity) ? "</tbody></table><table class='tsort'>" : "");
				StringBuffer cols = new StringBuffer("<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("name", lang) + "</th>");
				if (en.matches("PR"))
					cols.append("<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("country", lang) + "</th><th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("sport", lang) + "</th>");				
				else if (en.matches("TM"))
					cols.append("<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("sport", lang) + "</th>");
				else if (en.matches("CX|CT"))
					cols.append((en.matches("CX") ? "<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("city", lang) + "</th>" : "") + "<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("country", lang) + "</th>");
				html.append("<thead><tr><th colspan='" + (StringUtils.countIn(cols.toString(), "<th") + 1) + "'>");
				html.append(HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity." + en, lang).toUpperCase() + "&nbsp;(" + hCount.get(en) + ")") + "</th></tr>");
				html.append("<tr class='rsort'>" + cols.toString() + "<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("references", lang) + "</th></tr></thead><tbody class='tby' id='tb-" + id + "'>");
				currentEntity = en;
			}
			String name = item.getLabel();
			String name2 = item.getLabelEN();
			if (en.equals("PR") && name.matches(".*\\,.*")) {
				String[] t = name.split("\\,\\s+", -1);
				name = t[0] + (t.length > 1 && StringUtils.notEmpty(t[1]) ? ", " + t[1] : "");
			}
			int p1 = name.toLowerCase().indexOf(pattern.toLowerCase());
			if (p1 > -1) {
				int p2 = p1 + pattern.length();
				name = name.substring(0, p1) + "<b>" + name.substring(p1, p2) + "</b>" + name.substring(p2);
			}
			name = HtmlUtils.writeLink(item.getEntity(), item.getIdItem(), name, name2);
			if (item.getEntity().matches("CP|EV|CN|OL|SP|ST|TM")) {
				img = HtmlUtils.writeImage(ImageUtils.getIndex(item.getEntity()), item.getIdItem(), ImageUtils.SIZE_SMALL, null, null);
				name = HtmlUtils.writeImgTable(img, name);
			}
			// Handle relations
			String rel1 = "";
			String rel2 = "";
			String rel3 = "";
			if (en.matches("PR|TM")) {
				if(en.equals("PR") && item.getLink() != null && item.getLink() == 0) {
					ArrayList<Integer> lRel1Id = new ArrayList<Integer>();
					ArrayList<Integer> lRel2Id = new ArrayList<Integer>();
					ArrayList<Integer> lRel3Id = new ArrayList<Integer>();
					StringBuffer sbRel1 = new StringBuffer();
					StringBuffer sbRel2 = new StringBuffer();
					StringBuffer sbRel3 = new StringBuffer();
					for (Athlete a : (List<Athlete>) DatabaseHelper.execute("from Athlete where id = " + item.getIdItem() + " or link = " + item.getIdItem() + " order by id")) {
						if (a.getCountry() != null && !lRel1Id.contains(a.getCountry().getId())) {
							link = HtmlUtils.writeLink(Country.alias, a.getCountry().getId(), a.getCountry().getLabel(lang) + " (" + a.getCountry().getCode() + ")", a.getCountry().getLabel());
							img = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, a.getCountry().getId(), ImageUtils.SIZE_SMALL, null, null);
							sbRel1.append(HtmlUtils.writeImgTable(img, link));
							lRel1Id.add(a.getCountry().getId());
						}
						if (a.getSport() != null && !lRel2Id.contains(a.getSport().getId())) {
							link = HtmlUtils.writeLink(Sport.alias, a.getSport().getId(), a.getSport().getLabel(lang), a.getSport().getLabel());
							img = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, a.getSport().getId(), ImageUtils.SIZE_SMALL, null, null);
							sbRel2.append(HtmlUtils.writeImgTable(img, link));
							lRel2Id.add(a.getSport().getId());
						}
						if (a.getTeam() != null && !lRel3Id.contains(a.getTeam().getId())) {
							link = HtmlUtils.writeLink(Team.alias, a.getTeam().getId(), a.getTeam().getLabel(), null);
							img = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, a.getTeam().getId(), ImageUtils.SIZE_SMALL, null, null);
							sbRel3.append(HtmlUtils.writeImgTable(img, link));
							lRel3Id.add(a.getTeam().getId());
						}
					}
					rel1 = sbRel1.toString().replaceAll("</table><table>", "</table><table class='margintop'>");
					rel2 = sbRel2.toString().replaceAll("</table><table>", "</table><table class='margintop'>");
					rel3 = sbRel3.toString().replaceAll("</table><table>", "</table><table class='margintop'>");
				}
				else {
					if (item.getIdRel1() != null && !en.matches("TM")) {
						link = HtmlUtils.writeLink(Country.alias, item.getIdRel1(), item.getLabelRel1(), item.getLabelRel4());
						img = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel1(), ImageUtils.SIZE_SMALL, null, null);
						rel1 = HtmlUtils.writeImgTable(img, link);
					}
					if (item.getIdRel2() != null) {
						link = HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel5());
						img = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, item.getIdRel2(), ImageUtils.SIZE_SMALL, null, null);
						rel2 = HtmlUtils.writeImgTable(img, link);
					}
					if (item.getIdRel3() != null && !en.matches("PR|TM")) {
						link = HtmlUtils.writeLink(Team.alias, item.getIdRel3(), item.getLabelRel3(), null);
						img = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdRel3(), ImageUtils.SIZE_SMALL, null, null);
						rel3 = HtmlUtils.writeImgTable(img, link);
					}
				}
			}
			else if (en.matches("CX|CT")) {
				if (item.getIdRel1() != null && en.matches("CX")) {
					link = HtmlUtils.writeLink(City.alias, item.getIdRel1(), item.getLabelRel1(), item.getLabelRel4());
					rel1 = link;
				}
				if (item.getIdRel3() != null) {
					link = HtmlUtils.writeLink(Country.alias, item.getIdRel3(), item.getLabelRel3(), item.getLabelRel6());
					img = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel3(), ImageUtils.SIZE_SMALL, null, null);
					rel3 = HtmlUtils.writeImgTable(img, link);
				}
			}
			if (en.matches("PR|TM|CX|CT")) {
				rel1 = (!en.matches("CT|TM") ? "<td class='srt'>" + (StringUtils.notEmpty(rel1) ? rel1 : StringUtils.EMPTY) + "</td>" : "");
				rel2 = (!en.matches("CX|CT") ? "<td class='srt'>" + (StringUtils.notEmpty(rel2) ? rel2 : StringUtils.EMPTY) + "</td>" : "");
				rel3 = (!en.matches("PR|TM") ? "<td class='srt'>" + (StringUtils.notEmpty(rel3) ? rel3 : StringUtils.EMPTY) + "</td>" : "");
			}

			// Write line
			html.append("<tr><td class='srt'>" + name + "</td>" + rel1 + rel2 + rel3);
			html.append("<td class='centered srt'>" + (item.getCountRef() != null ? item.getCountRef() : 0) + "</td>");
			html.append("</tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertOlympicMedals(Collection<Object> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty())
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		int currentOlympics = 0;
		boolean isIndividual = false;
		boolean isResult = false;
		boolean isScore = false;
		// Evaluate columns
		for (Object obj : coll) {
			OlympicMedalsBean bean = (OlympicMedalsBean) obj;
			isIndividual |= ((bean.getTp2Number() != null ? bean.getTp2Number() : bean.getTp1Number()) <= 10);
			isScore |= (bean.getRsRank1() != null && bean.getRsRank2() != null && StringUtils.notEmpty(bean.getRsResult1()) && !StringUtils.notEmpty(bean.getRsResult2()) && !StringUtils.notEmpty(bean.getRsResult3()));
			isResult |= (StringUtils.notEmpty(bean.getRsResult1()) && !isScore);
		}
		int colspan = (isIndividual ? 2 : 1) + (isResult ? 1 : 0);
		String olympics = null;
		String tmpImg = null;
		long id = System.currentTimeMillis();
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		html.append("<thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("entity.OL", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("event", lang) + "</th>");
		html.append("<th colspan='" + colspan + "' onclick='sort(\"" + id + "\", this, 2);'>" + ImageUtils.getGoldHeader(lang) + "</th>");
		html.append("<th colspan='" + colspan + "' onclick='sort(\"" + id + "\", this, 3);'>" + ImageUtils.getSilverHeader(lang) + "</th>");
		html.append("<th colspan='" + colspan + "' onclick='sort(\"" + id + "\", this, 4);'>" + ImageUtils.getBronzeHeader(lang) + "</th>");
		html.append("<th colspan='" + colspan + "' onclick='sort(\"" + id + "\", this, 5);'>" + ResourceUtils.getText("place", lang) + "</th>");
		html.append("</tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			OlympicMedalsBean bean = (OlympicMedalsBean) obj;
			boolean isIndividual_ = ((bean.getTp2Number() != null ? bean.getTp2Number() : bean.getTp1Number()) <= 10);
			if (!bean.getYrId().equals(currentOlympics)) {
				tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn1Id() != null ? bean.getCn1Id() : bean.getCn2Id(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
				olympics = HtmlUtils.writeLink(Olympics.alias, bean.getOlId(), bean.getYrLabel() + "&nbsp;" + bean.getOlCity(), bean.getYrLabel() + " " + bean.getOlCityEN());
				olympics = HtmlUtils.writeImgTable(tmpImg, olympics);
				currentOlympics = bean.getYrId();
			}

			// Evaluate bean
			String entity1 = null, entityCn1 = null;
			String entity2 = null, entityCn2 = null;
			String entity3 = null, entityCn3 = null;
			String venue = null;
			if (bean.getRsRank1() != null) {
				if (isIndividual_) {
					entity1 = HtmlUtils.writeLink(Athlete.alias, bean.getRsRank1(), StringUtils.toFullName(bean.getPr1LastName(), bean.getPr1FirstName(), bean.getPr1CnCode(), true), bean.getPr1FirstName() + " " + bean.getPr1LastName());
					entityCn1 = getResultsEntityRel(null, null, null, bean.getPr1CnId(), bean.getPr1CnCode(), bean.getPr1CnLabel(), bean.getPr1CnLabelEN(), false, false, bean.getYrLabel());
				}
				else
					entity1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getRsRank1(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), HtmlUtils.writeLink(Country.alias, bean.getRsRank1(), bean.getCn1Code(), bean.getCn1LabelEN()));
			}
			if (bean.getRsRank2() != null) {
				if (isIndividual_) {
					entity2 = HtmlUtils.writeLink(Athlete.alias, bean.getRsRank2(), StringUtils.toFullName(bean.getPr2LastName(), bean.getPr2FirstName(), bean.getPr2CnCode(), true), bean.getPr2FirstName() + " " + bean.getPr2LastName());
					entityCn2 = getResultsEntityRel(null, null, null, bean.getPr2CnId(), bean.getPr2CnCode(), bean.getPr2CnLabel(), bean.getPr2CnLabelEN(), false, false, bean.getYrLabel());
				}
				else
					entity2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getRsRank2(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), HtmlUtils.writeLink(Country.alias, bean.getRsRank2(), bean.getCn2Code(), bean.getCn2LabelEN()));
			}
			if (bean.getRsRank3() != null) {
				if (isIndividual_) {
					entity3 = HtmlUtils.writeLink(Athlete.alias, bean.getRsRank3(), StringUtils.toFullName(bean.getPr3LastName(), bean.getPr3FirstName(), bean.getPr3CnCode(), true), bean.getPr3FirstName() + " " + bean.getPr3LastName());
					entityCn3 = getResultsEntityRel(null, null, null, bean.getPr3CnId(), bean.getPr3CnCode(), bean.getPr3CnLabel(), bean.getPr3CnLabelEN(), false, false, bean.getYrLabel());
				}
				else
					entity3 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getRsRank3(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), HtmlUtils.writeLink(Country.alias, bean.getRsRank3(), bean.getCn3Code(), bean.getCn3LabelEN()));
			}
			if (bean.getCxId() != null) {
				tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn1Id(), ImageUtils.SIZE_SMALL, null, null);				
				venue = HtmlUtils.writeLink(Complex.alias, bean.getCxId(), bean.getCxLabel(), bean.getCxLabelEN());
				venue += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(City.alias, bean.getCt1Id(), bean.getCt1Label(), bean.getCt1LabelEN());
				if (bean.getSt1Id() != null)
					venue += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(State.alias, bean.getSt1Id(), bean.getSt1Code(), bean.getSt1LabelEN());
				venue += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(Country.alias, bean.getCn1Id(), bean.getCn1Code_(), bean.getCn1LabelEN_()); 
				venue = HtmlUtils.writeImgTable(tmpImg, venue);
			}

			// Write line
			html.append("<tr><td class='srt'>" + olympics + "</td><td class='srt'>" + bean.getEvLabel() + (StringUtils.notEmpty(bean.getSeLabel()) ? " - " + bean.getSeLabel() : "") + "</td>");
			html.append(entity1 != null ? "<td class='srt'" + (!isIndividual_ && isIndividual ? " colspan='2'" : "") + " style='font-weight:bold;'>" + entity1 + "</td>" + (entityCn1 != null ? entityCn1 : "") + (isResult ? "<td>" + (bean.getRsResult1() != null ? bean.getRsResult1().replaceAll("\\s", "&nbsp;").replaceAll("\\-", "&#8209;") : "") + "</td>" : "") : "<td colspan='" + colspan + "'>" + StringUtils.EMPTY + "</td>");
			html.append(entity2 != null ? "<td class='srt'" + (!isIndividual_ && isIndividual ? " colspan='2'" : "") + ">" + entity2 + "</td>" + (entityCn2 != null ? entityCn2 : "") + (isResult ? "<td>" + (bean.getRsResult2() != null ? bean.getRsResult2() : "") + "</td>" : "") : "<td colspan='" + colspan + "'>" + StringUtils.EMPTY + "</td>");
			html.append(entity3 != null ? "<td class='srt'" + (!isIndividual_ && isIndividual ? " colspan='2'" : "") + ">" + entity3 + "</td>" + (entityCn3 != null ? entityCn3 : "") + (isResult ? "<td>" + (bean.getRsResult3() != null ? bean.getRsResult3() : "") + "</td>" : "") : "<td colspan='" + colspan + "'>" + StringUtils.EMPTY + "</td>");
			html.append("<td class='srt'>" + (StringUtils.notEmpty(venue) ? venue : "-") + "</td>");
			html.append("</tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertOlympicRankings(Collection<Object> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty())
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		int currentOlympics = 0;
		String olympics = null;
		String tmpImg = null;
		long id = System.currentTimeMillis();
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		html.append("<thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("year", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("country", lang) + "</th>");
		html.append("<th onclick='sort(\"" + id + "\", this, 2);'>" + ImageUtils.getGoldHeader(lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ImageUtils.getSilverHeader(lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ImageUtils.getBronzeHeader(lang) + "</th>");
		html.append("</tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			OlympicRankingsBean bean = (OlympicRankingsBean) obj;
			if (!bean.getYrId().equals(currentOlympics)) {
				tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn2Id(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
				olympics = HtmlUtils.writeLink(Olympics.alias, bean.getOlId(), bean.getYrLabel() + "&nbsp;" + bean.getCtLabel(), bean.getYrLabel() + " " + bean.getCtLabelEN());
				olympics = HtmlUtils.writeImgTable(tmpImg, olympics);
				currentOlympics = bean.getYrId();
			}

			// Evaluate bean
			String country = HtmlUtils.writeLink(Country.alias, bean.getCn1Id(), bean.getCn1Label(), bean.getCn1LabelEN());
			tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn1Id(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
			country = HtmlUtils.writeImgTable(tmpImg, country);

			// Write line
			html.append("<tr><td class='srt'>" + olympics + "</td><td class='srt'>" + country + "</td>");
			html.append("<td class='srt'>" + bean.getOrCountGold() + "</td>");
			html.append("<td class='srt'>" + bean.getOrCountSilver() + "</td>");
			html.append("<td class='srt'>" + bean.getOrCountBronze() + "</td></tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertHallOfFame(Collection<Object> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty())
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		long id = System.currentTimeMillis();
		html.append("<thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("year", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("inductee", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("inducted.as", lang) + "</th></tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			HallOfFameBean bean = (HallOfFameBean) obj;

			// Evaluate bean
			String ln = bean.getPrLastName();
			String year = HtmlUtils.writeLink(Year.alias, bean.getYrId(), bean.getYrLabel(), null);
			String name = HtmlUtils.writeLink(Athlete.alias, bean.getPrId(), StringUtils.toFullName(ln, bean.getPrFirstName(), null, true), bean.getPrFirstName() + " " + bean.getPrLastName());
			String position = "-";
			if (StringUtils.notEmpty(bean.getHfPosition())) {
				StringBuffer sbPos = new StringBuffer();
				for (String s : bean.getHfPosition().split("\\-"))
					sbPos.append((!sbPos.toString().isEmpty() ? "&nbsp;/&nbsp;" : "") + StringUtils.getUSPosition(bean.getLgId(), s));
				position = sbPos.toString();
			}
				
			// Write line
			html.append("<tr><td class='srt'>" + year + "</td><td id='" + ln.replaceAll("\\s", "-") + "-" + bean.getHfId() + "' class='srt'><b>" + name + "</b></td><td class='srt'>" + position + "</td></tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertRetiredNumber(Collection<Object> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty())
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		long id = System.currentTimeMillis();
		html.append("<thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("team", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("number", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("year", lang) + "</th></tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			RetiredNumberBean bean = (RetiredNumberBean) obj;

			// Evaluate bean
			String teamImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getTmId(), ImageUtils.SIZE_SMALL, null, null);
			String team = HtmlUtils.writeLink(Team.alias, bean.getTmId(), bean.getTmLabel(), null);
			team = HtmlUtils.writeImgTable(teamImg, team);
			String name = HtmlUtils.writeLink(Athlete.alias, bean.getPrId(), StringUtils.toFullName(bean.getPrLastName(), bean.getPrFirstName(), null, true), bean.getPrFirstName() + " " + bean.getPrLastName());
			String number = String.valueOf(bean.getRnNumber());
			String year = (bean.getYrId() != null ? HtmlUtils.writeLink(Year.alias, bean.getYrId(), bean.getYrLabel(), null) : "-");
			
			// Write line
			html.append("<tr><td class='srt'>" + team + "</td><td class='srt' width='50'>" + number + "</td><td class='srt'><b>" + name + "</b></td><td class='srt'>" + year + "</td></tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertTeamStadium(Collection<Object> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty())
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		long id = System.currentTimeMillis();
		html.append("<thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("team", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("complex", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("city", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("years", lang) + "</th></tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			TeamStadiumBean bean = (TeamStadiumBean) obj;

			// Evaluate bean
			String teamImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getTmId(), ImageUtils.SIZE_SMALL, bean.getTsDate1(), null);
			String team = HtmlUtils.writeLink(Team.alias, bean.getTmId(), bean.getTmLabel(), null);
			team = HtmlUtils.writeImgTable(teamImg, team);
			String tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCnId(), ImageUtils.SIZE_SMALL, bean.getTsDate1(), null);
			String complex = HtmlUtils.writeLink(Complex.alias, bean.getCxId(), bean.getCxLabel(), bean.getCxLabelEN());
			String city = HtmlUtils.SPACE + HtmlUtils.writeLink(City.alias, bean.getCtId(), bean.getCtLabel(), bean.getCtLabelEN());
			if (bean.getStId() != null)
				city += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(State.alias, bean.getStId(), bean.getStCode(), bean.getStLabelEN());
			city += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(Country.alias, bean.getCnId(), bean.getCnCode(), bean.getCnLabelEN()); 
			city = HtmlUtils.writeImgTable(tmpImg, city);	
			String years = bean.getTsDate1() + "&nbsp;&ndash;&nbsp;" + (!bean.getTsDate2().equals("0") ? bean.getTsDate2() : ResourceUtils.getText("today", lang));

			// Write line
			html.append("<tr><td class='srt'>" + team + "</td><td class='srt'>" + complex + (bean.getTsRenamed() ? "<b>*</b>" : "") + "</td><td class='srt'>" + city + "</td><td class='srt'>" + years + "</td></tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertUSChampionships(Collection<Object> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty())
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		boolean isDate = false;
		boolean isPlace = false;
		boolean isComment = false;
		for (Object obj : coll) {
			USChampionshipsBean bean = (USChampionshipsBean) obj;
			isDate |= StringUtils.notEmpty(bean.getRsDate2());
			isPlace |= (bean.getCxId() != null);
			isComment |= StringUtils.notEmpty(bean.getRsComment()); 
		}
		long id = System.currentTimeMillis();
		html.append("<thead><tr class='rsort'><th" + (isComment ? " colspan='2'" : "") + " onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("year", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("champion", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("score", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("runner.up", lang) + "</th>");
		html.append((isDate ? "<th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("date", lang) + "</th>" : "") + (isPlace ? "<th onclick='sort(\"" + id + "\", this, " + (isDate ? 5 : 4) + ");'>" + ResourceUtils.getText("place", lang) + "</th>" : "") + "</tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			USChampionshipsBean bean = (USChampionshipsBean) obj;

			// Evaluate bean
			String champion = null;
			String runnerup = null;
			if (bean.getRsRank1() != null) {
				String championImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank1(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
				champion = HtmlUtils.writeLink(Team.alias, bean.getRsRank1(), bean.getRsTeam1(), null);
				champion = HtmlUtils.writeImgTable(championImg, champion);	
			}
			if (bean.getRsRank2() != null) {
				String runnerupImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank2(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
				runnerup = HtmlUtils.writeLink(Team.alias, bean.getRsRank2(), bean.getRsTeam2(), null);
				runnerup = HtmlUtils.writeImgTable(runnerupImg, runnerup);
			}
			String year = HtmlUtils.writeLink(Year.alias, bean.getYrId(), bean.getYrLabel(), null);
			String date = (StringUtils.notEmpty(bean.getRsDate1()) ? StringUtils.toTextDate(bean.getRsDate1(), lang, null) + "&nbsp;&ndash;&nbsp;" : "") + (StringUtils.notEmpty(bean.getRsDate2()) ? StringUtils.toTextDate(bean.getRsDate2(), lang, null) : StringUtils.EMPTY);
			String place = StringUtils.EMPTY;
			if (bean.getCxId() != null) {
				String tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCnId(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
				place = HtmlUtils.writeLink(Complex.alias, bean.getCxId(), bean.getCxLabel(), bean.getCxLabelEN());
				place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(City.alias, bean.getCtId(), bean.getCtLabel(), bean.getCtLabelEN());
				if (bean.getStId() != null)
					place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(State.alias, bean.getStId(), bean.getStCode(), bean.getStLabelEN());
				place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(Country.alias, bean.getCnId(), bean.getCnCode(), bean.getCnLabelEN()); 
				place = HtmlUtils.writeImgTable(tmpImg, place);
			}

			// Write line
			html.append("<tr>" + (isComment ? "<td>" + HtmlUtils.writeComment(bean.getRsId(), bean.getRsComment()) + "</td>" : "") + "<td class='srt'>" + year + "</td><td class='srt'>" + (champion != null ? champion : StringUtils.EMPTY) + "</td><td class='srt'>" + (StringUtils.notEmpty(bean.getRsResult()) ? bean.getRsResult().replaceAll("\\-", "&#8209;") : "") + "</td>");
			html.append("<td class='srt'>" + (runnerup != null ? runnerup : StringUtils.EMPTY) + "</td>" + (isDate ? "<td class='srt'>" + date + "</td>" : "") + (isPlace ? "<td class='srt'>" + place + "</td>" : "") + "</tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertUSRecords(Collection<Object> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty())
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		html.append("<tr class='rsort'><th onclick='sort(\"0\", this, 0);'>" + ResourceUtils.getText("category", lang) + "</th><th onclick='sort(\"0\", this, 1);'>" + ResourceUtils.getText("scope", lang) + "</th><th onclick='sort(\"0\", this, 2);'>" + ResourceUtils.getText("type", lang) + "</th><th onclick='sort(\"0\", this, 3);'>" + ResourceUtils.getText("period", lang) + "</th><th onclick='sort(\"0\", this, 4);'>" + ResourceUtils.getText("record", lang) + "</th><th colspan='2' onclick='sort(\"0\", this, 5);'>" + ResourceUtils.getText("record.holder", lang) + "</th><th onclick='sort(\"0\", this, 6);'>" + ResourceUtils.getText("date", lang) + "</th>");
		html.append("</tr></thead><tbody class='tby' id='tb-0'>");
		for (Object obj : coll) {
			USRecordsBean bean = (USRecordsBean) obj;

			// Evaluate bean
			boolean isIndividual = bean.getRcType1().equalsIgnoreCase("individual");
			boolean isAlltime = bean.getRcType2().matches("^Alltime.*");
			String[] tRank = new String[5];
			String[] tRecord = new String[5];
			String[] tDate = new String[5];
			tRank[0] = (isIndividual && bean.getRcPerson1() != null ? "<b>" + HtmlUtils.writeLink(Athlete.alias, bean.getRcRank1(), StringUtils.toFullName(bean.getRcPerson1LastName(), bean.getRcPerson1FirstName(), null, true), bean.getRcPerson1FirstName() + " " + bean.getRcPerson1LastName()) + (bean.getRcIdPrTeam1() != null && !isAlltime ? "</b>&nbsp;(" + HtmlUtils.writeLink(Team.alias, bean.getRcIdPrTeam1(), bean.getRcPrTeam1(), null) + ")" : "</b>") : (bean.getRcTeam1() != null ? HtmlUtils.writeLink(Team.alias, bean.getRcRank1(), bean.getRcTeam1(), null) : null));
			tRank[1] = (isIndividual && bean.getRcPerson2() != null ? "<b>" + HtmlUtils.writeLink(Athlete.alias, bean.getRcRank2(), StringUtils.toFullName(bean.getRcPerson2LastName(), bean.getRcPerson2FirstName(), null, true), bean.getRcPerson2FirstName() + " " + bean.getRcPerson2LastName()) + (bean.getRcIdPrTeam2() != null && !isAlltime ? "</b>&nbsp;(" + HtmlUtils.writeLink(Team.alias, bean.getRcIdPrTeam2(), bean.getRcPrTeam2(), null) + ")" : "</b>") : (bean.getRcTeam2() != null ? HtmlUtils.writeLink(Team.alias, bean.getRcRank2(), bean.getRcTeam2(), null) : null));
			tRank[2] = (isIndividual && bean.getRcPerson3() != null ? "<b>" + HtmlUtils.writeLink(Athlete.alias, bean.getRcRank3(), StringUtils.toFullName(bean.getRcPerson3LastName(), bean.getRcPerson3FirstName(), null, true), bean.getRcPerson3FirstName() + " " + bean.getRcPerson3LastName()) + (bean.getRcIdPrTeam3() != null && !isAlltime ? "</b>&nbsp;(" + HtmlUtils.writeLink(Team.alias, bean.getRcIdPrTeam3(), bean.getRcPrTeam3(), null) + ")" : "</b>") : (bean.getRcTeam3() != null ? HtmlUtils.writeLink(Team.alias, bean.getRcRank3(), bean.getRcTeam3(), null) : null));
			tRank[3] = (isIndividual && bean.getRcPerson4() != null ? "<b>" + HtmlUtils.writeLink(Athlete.alias, bean.getRcRank4(), StringUtils.toFullName(bean.getRcPerson4LastName(), bean.getRcPerson4FirstName(), null, true), bean.getRcPerson4FirstName() + " " + bean.getRcPerson4LastName()) + (bean.getRcIdPrTeam4() != null && !isAlltime ? "</b>&nbsp;(" + HtmlUtils.writeLink(Team.alias, bean.getRcIdPrTeam4(), bean.getRcPrTeam4(), null) + ")" : "</b>") : (bean.getRcTeam4() != null ? HtmlUtils.writeLink(Team.alias, bean.getRcRank4(), bean.getRcTeam4(), null) : null));
			tRank[4] = (isIndividual && bean.getRcPerson5() != null ? "<b>" + HtmlUtils.writeLink(Athlete.alias, bean.getRcRank5(), StringUtils.toFullName(bean.getRcPerson5LastName(), bean.getRcPerson5FirstName(), null, true), bean.getRcPerson5FirstName() + " " + bean.getRcPerson5LastName()) + (bean.getRcIdPrTeam5() != null && !isAlltime ? "</b>&nbsp;(" + HtmlUtils.writeLink(Team.alias, bean.getRcIdPrTeam5(), bean.getRcPrTeam5(), null) + ")" : "</b>") : (bean.getRcTeam5() != null ? HtmlUtils.writeLink(Team.alias, bean.getRcRank5(), bean.getRcTeam5(), null) : null));
			tRank[0] = (tRank[0] != null && isIndividual && bean.getRcIdPrTeam1() != null && !isAlltime ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcIdPrTeam1(), ImageUtils.SIZE_SMALL, null, bean.getRcPrTeam1()), tRank[0]) : (tRank[0] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcRank1(), ImageUtils.SIZE_SMALL, bean.getRcDate1(), null), tRank[0]) : tRank[0]));
			tRank[1] = (tRank[1] != null && isIndividual && bean.getRcIdPrTeam2() != null && !isAlltime ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcIdPrTeam2(), ImageUtils.SIZE_SMALL, null, bean.getRcPrTeam2()), tRank[1]) : (tRank[1] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcRank2(), ImageUtils.SIZE_SMALL, bean.getRcDate1(), null), tRank[1]) : tRank[1]));
			tRank[2] = (tRank[2] != null && isIndividual && bean.getRcIdPrTeam3() != null && !isAlltime ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcIdPrTeam3(), ImageUtils.SIZE_SMALL, null, bean.getRcPrTeam3()), tRank[2]) : (tRank[2] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcRank3(), ImageUtils.SIZE_SMALL, bean.getRcDate1(), null), tRank[2]) : tRank[2]));
			tRank[3] = (tRank[3] != null && isIndividual && bean.getRcIdPrTeam4() != null && !isAlltime ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcIdPrTeam4(), ImageUtils.SIZE_SMALL, null, bean.getRcPrTeam4()), tRank[3]) : (tRank[3] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcRank4(), ImageUtils.SIZE_SMALL, bean.getRcDate1(), null), tRank[3]) : tRank[3]));
			tRank[4] = (tRank[4] != null && isIndividual && bean.getRcIdPrTeam5() != null && !isAlltime ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcIdPrTeam5(), ImageUtils.SIZE_SMALL, null, bean.getRcPrTeam5()), tRank[4]) : (tRank[4] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcRank5(), ImageUtils.SIZE_SMALL, bean.getRcDate1(), null), tRank[4]) : tRank[4]));
			tRecord[0] = (bean.getRcRecord1() != null ? bean.getRcRecord1() : null);
			tDate[0] = (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + "&nbsp;" : "") + (bean.getRcDate1() != null ? bean.getRcDate1() : StringUtils.EMPTY);
			tDate[1] = (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + "&nbsp;" : "") + (bean.getRcDate2() != null ? bean.getRcDate2() : StringUtils.EMPTY);
			tDate[2] = (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + "&nbsp;" : "") + (bean.getRcDate3() != null ? bean.getRcDate3() : StringUtils.EMPTY);
			tDate[3] = (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + "&nbsp;" : "") + (bean.getRcDate4() != null ? bean.getRcDate4() : StringUtils.EMPTY);
			tDate[4] = (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + "&nbsp;" : "") + (bean.getRcDate5() != null ? bean.getRcDate5() : StringUtils.EMPTY);
			tDate[0] = (tDate[0] != null && tDate[0].matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d") ? StringUtils.toTextDate(tDate[0], lang, "MMM. dd, yyyy") : tDate[0]);
			tDate[1] = (tDate[1] != null && tDate[1].matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d") ? StringUtils.toTextDate(tDate[1], lang, "MMM. dd, yyyy") : tDate[1]);
			tDate[2] = (tDate[2] != null && tDate[2].matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d") ? StringUtils.toTextDate(tDate[2], lang, "MMM. dd, yyyy") : tDate[2]);
			tDate[3] = (tDate[3] != null && tDate[3].matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d") ? StringUtils.toTextDate(tDate[3], lang, "MMM. dd, yyyy") : tDate[3]);
			tDate[4] = (tDate[4] != null && tDate[4].matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d") ? StringUtils.toTextDate(tDate[4], lang, "MMM. dd, yyyy") : tDate[4]);
			if (StringUtils.notEmpty(bean.getRcExa())) {
				List<Integer> lTie = StringUtils.tieList(bean.getRcExa());
				for (int i = 2 ; i <= 5 ; i++) {
					if (lTie.contains(i)) {
						tRank[0] += (!tRank[0].matches(".*\\<table\\>.*") ? "<br/>" : "") + tRank[i - 1];
						tDate[0] += (StringUtils.notEmpty(tDate[i - 1]) ? "<br/>" + tDate[i - 1] : "");
					}
				}
			}

			// Write line
			html.append("<tr><td class='srt'>" + bean.getSeLabel() + "</td><td class='srt'>" + bean.getEvLabel() + "</td><td class='srt'>" + bean.getRcType1() + "</td><td class='srt'>" + bean.getRcType2() + "</td><td class='srt'>" + bean.getRcLabel() + "</td>");
			html.append("<td class='srt'><b>" + tRecord[0] + "</b></td><td class='srt'>" + tRank[0] + "</td>");
			html.append("<td class='srt'>" + tDate[0].replaceAll("\\s", "&nbsp;").replaceAll("\\-", "&#8209;") + "</td>");
			html.append("</tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertWinLoss(Collection<Object> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty())
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		boolean isTie = false;
		boolean isOtloss = false;
		for (Object obj : coll) {
			WinLossBean bean = (WinLossBean) obj;
			isTie |= (bean.getWlCountTie() != null);
			isOtloss |= (bean.getWlCountOtloss() != null);
		}
		long id = System.currentTimeMillis();
		html.append("<tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("team", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("type", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("wins", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("losses", lang) + "</th>" + (isTie ? "<th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("ties", lang) + "</th>" : "") + (isOtloss ? "<th onclick='sort(\"" + id + "\", this, 5);'>" + ResourceUtils.getText("ties", lang) + "</th>" : "") + "<th onclick='sort(\"" + id + "\", this, " + (4 + (isTie ? 1 : 0) + (isOtloss ? 1 : 0)) + ");'>%</th></tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			WinLossBean bean = (WinLossBean) obj;

			// Evaluate bean
			String teamImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getTmId(), ImageUtils.SIZE_SMALL, null, null);
			String team = HtmlUtils.writeLink(Team.alias, bean.getTmId(), bean.getTmLabel(), null);
			team = HtmlUtils.writeImgTable(teamImg, team);
			String stats = "<td class='srt'>" + bean.getWlCountWin() + "</td><td class='srt'>" + bean.getWlCountLoss() + "</td>";
			if (isTie)
				stats += "<td class='srt'>" + (bean.getWlCountTie() != null ? bean.getWlCountTie() : StringUtils.EMPTY) + "</td>";
			if (isOtloss)
				stats += "<td class='srt'>" + (bean.getWlCountOtloss() != null ? bean.getWlCountOtloss() : StringUtils.EMPTY) + "</td>";
			int sumAll = bean.getWlCountWin() + bean.getWlCountLoss() + (bean.getWlCountTie() != null ? bean.getWlCountTie() : 0) + (bean.getWlCountOtloss() != null ? bean.getWlCountOtloss() : 0);
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
			nf.setMinimumFractionDigits(1);
			nf.setMaximumFractionDigits(1);
			stats += "<td class='srt'>" + ((DecimalFormat) nf).format((bean.getWlCountWin() * 100.0d) / sumAll) + "</td>";

			// Write line
			html.append("<tr><td class='srt'>" + team + "</td><td class='srt'>" + bean.getWlType() + "</td>" + stats + "</tr>");
		}
		html.append("</tbody></table>");
		return html;
	}
	
	public static StringBuffer convertLastUpdates(Collection<Object> coll, Integer limit, Integer offset, String lang) throws Exception {
		StringBuffer html = new StringBuffer();
		int i = 0;
		for (Object obj : coll) {
			LastUpdateBean bean = (LastUpdateBean) obj;

			String pos1 = null;
			String pos2 = null;
			String pos3 = null;
			String pos4 = null;
			int number = (bean.getTp3Number() != null ? bean.getTp3Number() : (bean.getTp2Number() != null ? bean.getTp2Number() : bean.getTp1Number()));
			if (number < 10) {
				if (bean.getPr1LastName() != null) {
					pos1 = (bean.getPr1Country() != null && bean.getPr1CountryCode().matches(StringUtils.PATTERN_REVERT_NAME) ? (StringUtils.notEmpty(bean.getPr1LastName()) ? bean.getPr1LastName().substring(0, 1) + "." : "") + bean.getPr1FirstName() : (StringUtils.notEmpty(bean.getPr1FirstName()) ? bean.getPr1FirstName().substring(0, 1) + "." : "") + bean.getPr1LastName());
					if (bean.getPr1Country() != null)
						pos1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getPr1Country(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Athlete.alias, bean.getPr1Id(), pos1, StringUtils.toFullName(bean.getPr1LastName(), bean.getPr1FirstName(), bean.getPr1CountryCode(), false)));
				}
				if (bean.getPr2LastName() != null) {
					pos2 = (bean.getPr2Country() != null && bean.getPr2CountryCode().matches(StringUtils.PATTERN_REVERT_NAME) ? (StringUtils.notEmpty(bean.getPr2LastName()) ? bean.getPr2LastName().substring(0, 1) + "." : "") + bean.getPr2FirstName() : (StringUtils.notEmpty(bean.getPr2FirstName()) ? bean.getPr2FirstName().substring(0, 1) + "." : "") + bean.getPr2LastName());
					if (bean.getPr2Country() != null)
						pos2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getPr2Country(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Athlete.alias, bean.getPr2Id(), pos2, StringUtils.toFullName(bean.getPr2LastName(), bean.getPr2FirstName(), bean.getPr2CountryCode(), false)));
				}
				if (bean.getPr3LastName() != null) {
					pos3 = (bean.getPr3Country() != null && bean.getPr3CountryCode().matches(StringUtils.PATTERN_REVERT_NAME) ? (StringUtils.notEmpty(bean.getPr3LastName()) ? bean.getPr3LastName().substring(0, 1) + "." : "") + bean.getPr3FirstName() : (StringUtils.notEmpty(bean.getPr3FirstName()) ? bean.getPr3FirstName().substring(0, 1) + "." : "") + bean.getPr3LastName());
					if (bean.getPr3Country() != null)
						pos3 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getPr3Country(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Athlete.alias, bean.getPr3Id(), pos3, StringUtils.toFullName(bean.getPr3LastName(), bean.getPr3FirstName(), bean.getPr3CountryCode(), false)));
				}
				if (bean.getPr4LastName() != null) {
					pos4 = (bean.getPr4Country() != null && bean.getPr4CountryCode().matches(StringUtils.PATTERN_REVERT_NAME) ? (StringUtils.notEmpty(bean.getPr4LastName()) ? bean.getPr4LastName().substring(0, 1) + "." : "") + bean.getPr4FirstName() : (StringUtils.notEmpty(bean.getPr4FirstName()) ? bean.getPr4FirstName().substring(0, 1) + "." : "") + bean.getPr4LastName());
					if (bean.getPr4Country() != null)
						pos4 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getPr4Country(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Athlete.alias, bean.getPr4Id(), pos4, StringUtils.toFullName(bean.getPr4LastName(), bean.getPr4FirstName(), bean.getPr4CountryCode(), false)));
				}
			}
			else if (number == 50 && bean.getTm1Id() != null) {
				pos1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getTm1Id(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Team.alias, bean.getTm1Id(), bean.getTm1Label(), null));
				if (bean.getTm2Id() != null)
					pos2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getTm2Id(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Team.alias, bean.getTm2Id(), bean.getTm2Label(), null));
				if (bean.getTm3Id() != null)
					pos3 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getTm3Id(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Team.alias, bean.getTm3Id(), bean.getTm3Label(), null));
				if (bean.getTm4Id() != null)
					pos4 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getTm4Id(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Team.alias, bean.getTm4Id(), bean.getTm4Label(), null));
			}
			else if (number == 99 && bean.getCn1Id() != null) {
				pos1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn1Id(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Country.alias, bean.getCn1Id(), bean.getCn1Label(), bean.getCn1LabelEN()));
				if (bean.getCn2Id() != null)
					pos2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn2Id(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Country.alias, bean.getCn2Id(), bean.getCn2Label(), bean.getCn2LabelEN()));
				if (bean.getCn3Id() != null)
					pos3 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn3Id(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Country.alias, bean.getCn3Id(), bean.getCn3Label(), bean.getCn3LabelEN()));
				if (bean.getCn4Id() != null)
					pos4 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn4Id(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Country.alias, bean.getCn4Id(), bean.getCn4Label(), bean.getCn4LabelEN()));
			}
			String year = "<b>" + bean.getYrLabel() + "</b>";
			String update = StringUtils.toTextDate(bean.getRsUpdate(), lang, "dd MMM yyyy");
			String update2 = new SimpleDateFormat("yyyyMMddHHmm").format(bean.getRsUpdate());
			String path = bean.getYrLabel() + "/" + bean.getSpLabelEN() + "/" + bean.getCpLabelEN() + "/" + bean.getEvLabelEN() + (bean.getSeId() != null ? "/" + bean.getSeLabelEN() : "") + (bean.getSe2Id() != null ? "/" + bean.getSe2LabelEN() : "");
			boolean isScore = (pos1 != null && pos2 != null && StringUtils.notEmpty(bean.getRsText1()) && !StringUtils.notEmpty(bean.getRsText2()));
			boolean isDouble = (pos1 != null && pos2 != null && (number == 4 || (bean.getRsText4() != null && bean.getRsText4().equals("#DOUBLE#")) || (bean.getRsText3() != null && bean.getRsText3().equals("1-2"))));
			boolean isTriple = (pos1 != null && pos2 != null && pos3 != null && (number == 5 || (bean.getRsText4() != null && bean.getRsText4().equals("#TRIPLE#")) || (bean.getRsText3() != null && bean.getRsText3().matches("^1\\-(3|4|5|6|7|8).*"))));
			String link = "/results/" + StringUtils.urlEscape(bean.getSpLabelEN() + "/" + bean.getCpLabelEN() + "/" + bean.getEvLabelEN() + (bean.getSeId() != null ? "/" + bean.getSeLabelEN() : "") + (bean.getSe2Id() != null ? "/" + bean.getSe2LabelEN() : "")) + "/" + StringUtils.encode(bean.getSpId() + "-" + bean.getCpId() + "-" + bean.getEvId() + "-" + (bean.getSeId() != null ? bean.getSeId() : 0) + "-" + (bean.getSe2Id() != null ? bean.getSe2Id() : 0) + "-0");
			String event = "<a href='" + link + "'>" + bean.getCpLabel() + "&nbsp;-&nbsp;" + bean.getEvLabel() + (StringUtils.notEmpty(bean.getSeLabel()) ? "&nbsp;-&nbsp;" + bean.getSeLabel() : "") + (StringUtils.notEmpty(bean.getSe2Label()) ? "&nbsp;-&nbsp;" + bean.getSe2Label() : "") + "</a>";
			String eventImg = null;
			if (bean.getCpId() != null)
				eventImg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_CHAMPIONSHIP, bean.getSpId() + "-" + bean.getCpId(), ImageUtils.SIZE_SMALL, null, bean.getCpLabel());
			if (!StringUtils.notEmpty(eventImg) && bean.getEvId() != null)
				eventImg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_EVENT, bean.getSpId() + "-" + bean.getEvId(), ImageUtils.SIZE_SMALL, null, bean.getEvLabel());
			if (!StringUtils.notEmpty(eventImg) && bean.getSeId() != null)
				eventImg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_EVENT, bean.getSpId() + "-" + bean.getSeId(), ImageUtils.SIZE_SMALL, null, bean.getSeLabel());
			if (!StringUtils.notEmpty(eventImg) && bean.getSe2Id() != null)
				eventImg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_EVENT, bean.getSpId() + "-" + bean.getSe2Id(), ImageUtils.SIZE_SMALL, null, bean.getSe2Label());
			if (StringUtils.notEmpty(eventImg))
				event = HtmlUtils.writeImgTable(eventImg, event);

			// Write line
			String pos1_ = "<td style='padding-right:3px;'>" + pos1 + ((isDouble || isTriple) && pos2 != null ? pos2 : "") + (isTriple && pos3 != null ? pos3 : "") + "</td>";
			String pos2_ = (isTriple ? null : (isDouble && pos3 != null ? "<td style='padding-right:3px;'>" + pos3 + (pos4 != null ? pos4 : "") + "</td>" : (!isDouble && pos2 != null ? "<td style='padding-right:3px;'>" + pos2 + "</td>" : null)));
			String pos3_ = (isDouble || isTriple || isScore ? null : (pos3 != null ? "<td style='padding-right:3px;'>" + pos3 + "</td>" : null));
			
			html.append("<tr><td class='srt'>" + year + "</td><td class='srt'>" + HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, bean.getSpId(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Sport.alias, bean.getSpId(), bean.getSpLabel(), bean.getSpLabelEN())) + "</td>");
			html.append("<td class='srt'>" + event + "</td>");
			html.append("<td class='srt'><table><tr>" + pos1_ + (isScore ? "<td style='padding-left:2px;padding-right:3px;padding-top:3px;'>" + bean.getRsText1().replaceAll("\\s", "&nbsp;") + "</td>" : "") + (pos2_ != null ? pos2_ : "") + (pos3_ != null ? pos3_ : "") + "<td style='padding-left:2px;padding-top:4px;'>" + HtmlUtils.writeLink(Result.alias, bean.getRsId(), "<img alt='details' title='" +  ResourceUtils.getText("details", lang) + "' src='/img/render/details.png'/>", path) + "</td></tr></table></td>");
			html.append("<td id='dt-" + update2 + "-" + i + "' class='srt'>" + update + "</td></tr>");
			i++;
		}
		final String MORE_ITEMS = "<tr class='moreitems'><td colspan='5'><div class='sfdiv1' onclick='moreLastUpdates(this, \"#P1#\");'>&nbsp;(+20)&nbsp;</div><div class='sfdiv2' onclick='moreLastUpdates(this, \"#P2#\");'>&nbsp;(+50)&nbsp;</div><div class='sfdiv3' onclick='moreLastUpdates(this, \"#P3#\");'>&nbsp;(+100)&nbsp;</div></td></tr>";
		String p ="#LIMIT#-" + (offset + limit);
		html.append(MORE_ITEMS.replaceAll("#P1#", StringUtils.encode(p.replaceAll("#LIMIT#", "20"))).replaceAll("#P2#", StringUtils.encode(p.replaceAll("#LIMIT#", "50"))).replaceAll("#P3#", StringUtils.encode(p.replaceAll("#LIMIT#", "100"))));
		return html;
	}
	
}