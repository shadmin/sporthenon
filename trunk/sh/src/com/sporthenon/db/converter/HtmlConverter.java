package com.sporthenon.db.converter;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Draw;
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
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.db.entity.meta.TreeItem;
import com.sporthenon.db.function.DrawBean;
import com.sporthenon.db.function.HallOfFameBean;
import com.sporthenon.db.function.OlympicMedalsBean;
import com.sporthenon.db.function.OlympicRankingsBean;
import com.sporthenon.db.function.ResultsBean;
import com.sporthenon.db.function.RetiredNumberBean;
import com.sporthenon.db.function.TeamStadiumBean;
import com.sporthenon.db.function.USChampionshipsBean;
import com.sporthenon.db.function.USRecordsBean;
import com.sporthenon.db.function.WinLossBean;
import com.sporthenon.db.function.WinRecordsBean;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.RenderOptions;
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

	private static String getResultsEntity(int type, Integer rank, String str1, String str2, boolean picDisabled) {
		String s = null;
		if (type < 10)
			s = HtmlUtils.writeLink(Athlete.alias, rank, str1 + (StringUtils.notEmpty(str2) ? "," + HtmlUtils.SPACE + str2 : ""));
		else if (type == 50) {
			String img = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, rank, ImageUtils.SIZE_SMALL, picDisabled);
			s = HtmlUtils.writeLink(Team.alias, rank, str2);
			s = HtmlUtils.writeImgTable(img, s);
		}
		else if (type == 99) {
			String img = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, rank, ImageUtils.SIZE_SMALL, picDisabled);
			s = HtmlUtils.writeLink(Country.alias, rank, str2);
			s = HtmlUtils.writeImgTable(img, s);
		}
		return s;
	}

	private static String getResultsEntityRel(Integer rel1Id, String rel1Code, String rel1Label, Integer rel2Id, String rel2Code, String rel2Label, boolean isRel1, boolean isRel2, boolean picDisabled) {
		StringBuffer html = new StringBuffer();
		String tmpImg = null;
		if (rel2Id != null && rel2Id > 0) {
			tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, rel2Id, ImageUtils.SIZE_SMALL, picDisabled);
			String s = HtmlUtils.writeLink(Country.alias, rel2Id, rel2Code);
			html.append("<td>").append(HtmlUtils.writeImgTable(tmpImg, s)).append("</td>");
		}
		else if (isRel2)
			html.append("<td>" + StringUtils.EMPTY + "</td>");
		if (rel1Id != null && rel1Id > 0) {
			tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, rel1Id, ImageUtils.SIZE_SMALL, picDisabled);
			String s = HtmlUtils.writeLink(Team.alias, rel1Id, rel1Label);
			String s_ = HtmlUtils.writeImgTable(tmpImg, s);
			if (!s_.matches(".*\\<img.*"))
				s_ = "<table><tr><td>" + s_ + "</td></tr></table>";
			html.append("<td>").append(s_).append("</td>");
		}
		else if (isRel1)
			html.append("<td>" + StringUtils.EMPTY + "</td>");
		return html.toString();
	}

	public static StringBuffer getHeader(short type, Collection<Object> params, RenderOptions opts) throws Exception {
		ArrayList<Object> lstParams = new ArrayList<Object>(params);
		HashMap<String, String> hHeader = new HashMap<String, String>();
		Logger.getLogger("sh").debug("Header - " + type + " - " + params);
		hHeader.put("info", "#INFO#");
		if (type == HEADER_RESULTS) {
			Sport sp = (Sport) DatabaseHelper.loadEntity(Sport.class, lstParams.get(0));
			Championship cp = (Championship) DatabaseHelper.loadEntity(Championship.class, lstParams.get(1));
			Event ev = (Event) DatabaseHelper.loadEntity(Event.class, lstParams.get(2));
			Event se = (Event) DatabaseHelper.loadEntity(Event.class, lstParams.get(3));
			ArrayList<String> lstYears = DatabaseHelper.loadLabels(Year.class, String.valueOf(lstParams.get(4)));
			String logoSp = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, sp.getId(), ImageUtils.SIZE_LARGE, false);
			String logoCp = HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, cp.getId(), ImageUtils.SIZE_LARGE, false);
			String logoEv = HtmlUtils.writeImage(ImageUtils.INDEX_EVENT, ev.getId(), ImageUtils.SIZE_LARGE, false);
			String logoSe = (se != null ? HtmlUtils.writeImage(ImageUtils.INDEX_EVENT, se.getId(), ImageUtils.SIZE_LARGE, false) : "");
			if (StringUtils.notEmpty(sp.getWebsite()))
				logoSp = "<a href='http://" + sp.getWebsite() + "' target='_blank'>" + logoSp + "</a>";
			if (StringUtils.notEmpty(cp.getWebsite()))
				logoCp = "<a href='http://" + cp.getWebsite() + "' target='_blank'>" + logoCp + "</a>";
			if (ev != null && StringUtils.notEmpty(ev.getWebsite()))
				logoEv = "<a href='http://" + ev.getWebsite() + "' target='_blank'>" + logoEv + "</a>";
			if (se != null && StringUtils.notEmpty(se.getWebsite()))
				logoSe = "<a href='http://" + se.getWebsite() + "' target='_blank'>" + logoSe + "</a>";
			hHeader.put("tabshorttitle", sp.getLabel() + "&nbsp;-&nbsp;" + cp.getLabel() + (ev != null ? "&nbsp;-&nbsp;" + ev.getLabel() + (se != null ? "&nbsp;-&nbsp;" + se.getLabel() : "") : ""));
			hHeader.put("title", "RESULTS");
			hHeader.put("url", HtmlUtils.writeURL("results", lstParams.toString()));
			hHeader.put("logos", logoSp + logoCp + logoEv + logoSe);
			hHeader.put("item1", HtmlUtils.writeLink(Sport.alias, sp.getId(), sp.getLabel()));
			hHeader.put("item2", HtmlUtils.writeLink(Championship.alias, cp.getId(), cp.getLabel()));
			if (ev != null)
				hHeader.put("item3", HtmlUtils.writeLink(Event.alias, ev.getId(), ev.getLabel()) + (se != null ? "<br/>" + HtmlUtils.writeLink(Event.alias, se.getId(), se.getLabel()) : ""));
			hHeader.put("item4", (lstYears.isEmpty() ? "Years: All" : (lstYears.size() == 1 ? "Year: " + lstYears.get(0) : "Years: " + HtmlUtils.writeTip(Year.alias, lstYears))));
		}
		else if (type == HEADER_OLYMPICS_INDIVIDUAL) {
			ArrayList<String> lstOlympics = DatabaseHelper.loadLabelsFromQuery("select concat(concat(ol.year.label, ' - '), ol.city.label) from Olympics ol where ol.id in (" + lstParams.get(0) + ")");
			Sport sp = (Sport) DatabaseHelper.loadEntity(Sport.class, lstParams.get(1));
			Event ev = (Event) DatabaseHelper.loadEntity(Event.class, lstParams.get(2));
			ArrayList<String> lstEvents = DatabaseHelper.loadLabels(Event.class, String.valueOf(lstParams.get(2)));
			hHeader.put("tabshorttitle", "Olympics&nbsp;-&nbsp;" + sp.getLabel() + (ev != null ? "&nbsp;-&nbsp;" + ev.getLabel() : ""));
			hHeader.put("title", "OLYMPICS");
			hHeader.put("url", HtmlUtils.writeURL("olympics", "ind, " + lstParams.toString()));
			hHeader.put("logos", HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, sp.getId(), ImageUtils.SIZE_LARGE, false) + HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, 1, ImageUtils.SIZE_LARGE, false) + "<img alt='' title='Event Results' src='" + ImageUtils.getRenderUrl() + "ol-podium.png'/>");
			hHeader.put("item1", "Events Results)");
			hHeader.put("item2", (lstOlympics.isEmpty() ? "Games: All" : (lstOlympics.size() == 1 ? "Games: " + lstOlympics.get(0) : "Games: " + HtmlUtils.writeTip(Olympics.alias, lstOlympics))));
			hHeader.put("item3", HtmlUtils.writeLink(Sport.alias, sp.getId(), sp.getLabel()));
			hHeader.put("item4", (lstEvents.isEmpty() ? "Events: All" : (lstEvents.size() == 1 ? "Event: " + lstEvents.get(0) : "Events: " + HtmlUtils.writeTip(Event.alias, lstEvents))));
		}
		else if (type == HEADER_OLYMPICS_COUNTRY) {
			ArrayList<String> lstOlympics = DatabaseHelper.loadLabelsFromQuery("select concat(concat(ol.year.label, ' - '), ol.city.label) from Olympics ol where ol.id in (" + lstParams.get(0) + ")");
			ArrayList<String> lstCountries = DatabaseHelper.loadLabels(Country.class, String.valueOf(lstParams.get(1)));
			hHeader.put("tabshorttitle", "Olympics&nbsp;-&nbsp;Medals&nbsp;Table");
			hHeader.put("title", "OLYMPICS");
			hHeader.put("url", HtmlUtils.writeURL("olympics", "cnt, " + lstParams.toString()));
			hHeader.put("logos", HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, 1, ImageUtils.SIZE_LARGE, false) + "<img alt='' title='Medals Tables' src='" + ImageUtils.getRenderUrl() + "ol-medals.png'/>");
			hHeader.put("item1", "Medals Tables");
			hHeader.put("item2", (lstOlympics.isEmpty() ? "Games: All" : (lstOlympics.size() == 1 ? "Games: " + lstOlympics.get(0) : "Games: " + HtmlUtils.writeTip(Olympics.alias, lstOlympics))));
			hHeader.put("item3", (lstCountries.isEmpty() ? "Countries: All" : (lstCountries.size() == 1 ? "Country: " + lstCountries.get(0) : "Countries: " + HtmlUtils.writeTip(Country.alias, lstCountries))));
		}
		else if (type == HEADER_US_LEAGUES_RETNUM || type == HEADER_US_LEAGUES_CHAMPIONSHIP || type == HEADER_US_LEAGUES_HOF || type == HEADER_US_LEAGUES_RECORD || type == HEADER_US_LEAGUES_TEAMSTADIUM || type == HEADER_US_LEAGUES_WINLOSS) {
			String league = String.valueOf(lstParams.get(0));
			String leagueLabel = (league.equals("1") ? "NFL" : (league.equals("2") ? "NBA" : (league.equals("3") ? "NHL" : "MLB")));
			String typeLabel = (type == HEADER_US_LEAGUES_RETNUM ? "Retired Numbers" : (type == HEADER_US_LEAGUES_CHAMPIONSHIP ? "Championships" : (type == HEADER_US_LEAGUES_HOF ? "Hall of Fame" : (type == HEADER_US_LEAGUES_RECORD ? "Records" : (type == HEADER_US_LEAGUES_TEAMSTADIUM ? "Team Stadiums" : "Wins/Losses")))));
			String imgSp = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, (league.equals("1") ? 23 : (league.equals("2") ? 24 : (league.equals("3") ? 25 : 26))), ImageUtils.SIZE_LARGE, false);
			String imgCp = HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, (league.equals("1") ? 51 : (league.equals("2") ? 54 : (league.equals("3") ? 55 : 56))), ImageUtils.SIZE_LARGE, false);
			hHeader.put("title", "US LEAGUES");
			hHeader.put("tabshorttitle", leagueLabel + "&nbsp;-&nbsp;" + typeLabel);
			hHeader.put("item1", HtmlUtils.writeLink(Championship.alias, (league.equals("1") ? 51 : (league.equals("2") ? 54 : (league.equals("3") ? 55 : 56))), leagueLabel));
			hHeader.put("item2", typeLabel);
			hHeader.put("logos", imgSp + imgCp);
			if (type == HEADER_US_LEAGUES_RETNUM) {
				hHeader.put("url", HtmlUtils.writeURL("usleagues", USLeaguesServlet.TYPE_RETNUM + ", " + lstParams.toString()));
				ArrayList<String> lstTeams = DatabaseHelper.loadLabels(Team.class, String.valueOf(lstParams.get(1)));
				hHeader.put("item3", (lstTeams.isEmpty() ? "Teams: All" : (lstTeams.size() == 1 ? "Team: " + lstTeams.get(0) : "Teams: " + HtmlUtils.writeTip(Team.alias, lstTeams))));
			}
			else if (type == HEADER_US_LEAGUES_TEAMSTADIUM) {
				hHeader.put("url", HtmlUtils.writeURL("usleagues", USLeaguesServlet.TYPE_TEAMSTADIUM + ", " + lstParams.toString()));
				ArrayList<String> lstTeams = DatabaseHelper.loadLabels(Team.class, String.valueOf(lstParams.get(1)));
				hHeader.put("item3", (lstTeams.isEmpty() ? "Teams: All" : (lstTeams.size() == 1 ? "Team: " + lstTeams.get(0) : "Teams: " + HtmlUtils.writeTip(Team.alias, lstTeams))));
			}
			else if (type == HEADER_US_LEAGUES_WINLOSS) {
				hHeader.put("url", HtmlUtils.writeURL("usleagues", USLeaguesServlet.TYPE_WINLOSS + ", " + lstParams.toString()));
				ArrayList<String> lstTeams = DatabaseHelper.loadLabels(Team.class, String.valueOf(lstParams.get(1)));
				hHeader.put("item3", (lstTeams.isEmpty() ? "Teams: All" : (lstTeams.size() == 1 ? "Team: " + lstTeams.get(0) : "Teams: " + HtmlUtils.writeTip(Team.alias, lstTeams))));
			}
			else if (type == HEADER_US_LEAGUES_HOF) {
				hHeader.put("url", HtmlUtils.writeURL("usleagues", USLeaguesServlet.TYPE_HOF + ", " + lstParams.toString()));
				ArrayList<String> lstYears = DatabaseHelper.loadLabels(Year.class, String.valueOf(lstParams.get(1)));
				hHeader.put("item3", (lstYears.isEmpty() ? "Years: All" : (lstYears.size() == 1 ? "Year: " + lstYears.get(0) : "Years: " + HtmlUtils.writeTip(Year.alias, lstYears))));
			}
			else if (type == HEADER_US_LEAGUES_CHAMPIONSHIP) {
				hHeader.put("url", HtmlUtils.writeURL("usleagues", USLeaguesServlet.TYPE_CHAMPIONSHIP + ", " + lstParams.toString()));
				ArrayList<String> lstYears = DatabaseHelper.loadLabels(Year.class, String.valueOf(lstParams.get(2)));
				hHeader.put("item3", (lstYears.isEmpty() ? "Years: All" : (lstYears.size() == 1 ? "Year: " + lstYears.get(0) : "Years: " + HtmlUtils.writeTip(Year.alias, lstYears))));
			}
			else if (type == HEADER_US_LEAGUES_RECORD) {
				hHeader.put("url", HtmlUtils.writeURL("usleagues", USLeaguesServlet.TYPE_RECORD + ", " + lstParams.toString()));
				ArrayList<String> lstCategories = DatabaseHelper.loadLabels(Event.class, String.valueOf(lstParams.get(3)));
				ArrayList<String> lstScopes = DatabaseHelper.loadLabels(Event.class, String.valueOf(lstParams.get(2)));
				hHeader.put("item3", (lstCategories.isEmpty() ? "Categories: All" : (lstCategories.size() == 1 ? "Category: " + lstCategories.get(0) : "Categories: " + HtmlUtils.writeTip(Event.alias, lstCategories))));
				hHeader.put("item4", (lstScopes.isEmpty() ? "Scopes: All" : (lstScopes.size() == 1 ? "Scope: " + lstScopes.get(0) : "Scopes: " + HtmlUtils.writeTip(Event.alias, lstScopes))));
			}
		}
		else if (type == HEADER_SEARCH) {
			String pattern = String.valueOf(lstParams.get(0));
			pattern = pattern.replaceAll("\\.\\*", "\\*").replaceAll("\\.", "_").replaceAll("\\|", "&nbsp;").replaceAll("\\^", "").replaceAll("\\$", "");
			String scope = String.valueOf(lstParams.get(1));
			ArrayList<String> lstScope = new ArrayList<String>();
			for (String s : scope.split(","))
				lstScope.add(ResourceUtils.get("entity." + s));
			hHeader.put("tabshorttitle", "Search&nbsp;Results&nbsp;[" + pattern + "]");
			hHeader.put("title", "SEARCH");
			hHeader.put("url", HtmlUtils.writeURL("search", lstParams.toString()));
			hHeader.put("logos", "<img src='" + ImageUtils.getRenderUrl() + "search.png'/>");
			hHeader.put("item1", "Pattern: [" + pattern + "]");
			hHeader.put("item2", "Scope: " + HtmlUtils.writeTip("SC", lstScope));
		}
		return HtmlUtils.writeHeader(hHeader, opts.isHeaderDisabled(), opts.isPicturesDisabled());
	}

	public static StringBuffer getWinRecords(String results) throws Exception {
		ArrayList<Object> lParams = new ArrayList<Object>();
		lParams.add(results);
		Collection<WinRecordsBean> list = DatabaseHelper.call("WinRecords", lParams);
		return HtmlUtils.writeWinRecTable(list);
	}

	public static StringBuffer getRecordInfo(String type, int id, RenderOptions opts) throws Exception {
		LinkedHashMap<String, String> hInfo = new LinkedHashMap<String, String>();
		hInfo.put("url", HtmlUtils.writeURL("ref", type + ", " + id));
		hInfo.put("info", "#INFO#");
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
			Vector<Integer> vTm = new Vector<Integer>();
			Vector<Integer> vCn = new Vector<Integer>();
			StringBuffer sbTm = new StringBuffer();
			StringBuffer sbCn = new StringBuffer();
			for (Athlete a : lAthlete) {
				if (a.getTeam() != null) {
					if (!vTm.contains(a.getTeam().getId())) {
						String s = HtmlUtils.writeLink(Team.alias, a.getTeam().getId(), a.getTeam().getLabel());
						s = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, a.getTeam().getId(), ImageUtils.SIZE_SMALL, false), s);
						if (StringUtils.notEmpty(sbTm.toString()))
							s = s.replaceAll("\\<table\\>", "<table style='margin-top:2px;'>");
						sbTm.append(s);
						vTm.add(a.getTeam().getId());
					}
				}
				if (a.getCountry() != null) {
					if (!vCn.contains(a.getCountry().getId())) {
						String s = HtmlUtils.writeLink(Country.alias, a.getCountry().getId(), a.getCountry().getLabel());
						s = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, a.getCountry().getId(), ImageUtils.SIZE_SMALL, false), s);
						if (StringUtils.notEmpty(sbCn.toString()))
							s = s.replaceAll("\\<table\\>", "<table style='margin-top:2px;'>");
						sbCn.append(s);
						vCn.add(a.getCountry().getId());
					}
				}
			}
			String tm = sbTm.toString();
			String cn = sbCn.toString();
			String sp = null;
			if (e.getSport() != null) {
				sp = HtmlUtils.writeLink(Sport.alias, e.getSport().getId(), e.getSport().getLabel());
				sp = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, e.getSport().getId(), ImageUtils.SIZE_SMALL, false), sp);
			}
			hInfo.put("tabtitle", e.getLastName() + ", " + e.getFirstName() + (e.getCountry() != null ? " (" + e.getCountry().getCode() + ")" : "") + "&nbsp;[Athlete #" + id + "]");
			hInfo.put("title", "ATHLETE #" + id);
			if (e.getCountry() != null && e.getCountry().getCode().matches("CHN|KOR|PRK|TPE"))
				hInfo.put("name", "<b>" + e.getLastName().toUpperCase() + (StringUtils.notEmpty(e.getFirstName()) ? " " + e.getFirstName() : "") + "</b>");
			else
				hInfo.put("name", "<b>" + (StringUtils.notEmpty(e.getFirstName()) ? e.getFirstName() + " " : "") + e.getLastName().toUpperCase() + "</b>");
			if (StringUtils.notEmpty(cn))
				hInfo.put("country", cn);
			if (StringUtils.notEmpty(tm))
				hInfo.put("team", tm);
			hInfo.put("sport", (sp != null ? sp : StringUtils.EMPTY));
		}
		else if (type.equals(Championship.alias)) {
			Championship e = (Championship) DatabaseHelper.loadEntity(Championship.class, id);
			hInfo.put("tabtitle", e.getLabel() + "&nbsp;[Championship #" + id + "]");
			hInfo.put("logo", HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, e.getId(), 'L', opts.isPicturesDisabled()));
			hInfo.put("title", "CHAMPIONSHIP #" + id);
			hInfo.put("name", e.getLabel());
			hInfo.put("website", (e.getWebsite() != null ? "<a href='http://" + e.getWebsite() + "' target='_blank'>" + e.getWebsite() + "</a>" : StringUtils.EMPTY));
			hInfo.put("comment", (e.getComment() != null ? e.getComment() : StringUtils.EMPTY));
		}
		else if (type.equals(City.alias)) {
			City e = (City) DatabaseHelper.loadEntity(City.class, id);
			String st = null;
			String cn = null;
			if (e.getState() != null) {
				st = HtmlUtils.writeLink(State.alias, e.getState().getId(), e.getState().getLabel());
				st = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_STATE, e.getState().getId(), ImageUtils.SIZE_SMALL, false), st);
			}
			if (e.getCountry() != null) {
				cn = HtmlUtils.writeLink(Country.alias, e.getCountry().getId(), e.getCountry().getLabel());
				cn = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, e.getCountry().getId(), ImageUtils.SIZE_SMALL, false), cn);
			}
			hInfo.put("tabtitle", e.getLabel() + (e.getCountry() != null ? ", " + e.getCountry().getCode() : "") + "&nbsp;[City #" + id + "]");
			hInfo.put("title", "CITY #" + id);
			hInfo.put("name", e.getLabel());
			hInfo.put("state", (st != null ? st : StringUtils.EMPTY));
			hInfo.put("country", (cn != null ? cn : StringUtils.EMPTY));
		}
		else if (type.equals(Complex.alias)) {
			Complex e = (Complex) DatabaseHelper.loadEntity(Complex.class, id);
			String st = null;
			String cn = null;
			if (e.getCity().getState() != null) {
				st = HtmlUtils.writeLink(State.alias, e.getCity().getState().getId(), e.getCity().getState().getLabel());
				st = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_STATE, e.getCity().getState().getId(), ImageUtils.SIZE_SMALL, false), st);
			}
			if (e.getCity().getCountry() != null) {
				cn = HtmlUtils.writeLink(Country.alias, e.getCity().getCountry().getId(), e.getCity().getCountry().getLabel());
				cn = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, e.getCity().getCountry().getId(), ImageUtils.SIZE_SMALL, false), cn);
			}
			hInfo.put("tabtitle", e.getLabel() + "&nbsp;[Complex #" + id + "]");
			hInfo.put("title", "COMPLEX #" + id);
			hInfo.put("name", e.getLabel());
			hInfo.put("city", HtmlUtils.writeLink(City.alias, e.getCity().getId(), e.getCity().getLabel()));
			hInfo.put("state", (st != null ? st : StringUtils.EMPTY));
			hInfo.put("country", (cn != null ? cn : StringUtils.EMPTY));
		}
		else if (type.equals(Country.alias)) {
			Country e = (Country) DatabaseHelper.loadEntity(Country.class, id);
			hInfo.put("tabtitle", e.getLabel() + " (" + e.getCode() + ")" + "&nbsp;[Country #" + id + "]");
			hInfo.put("logo", HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, e.getId(), 'L', opts.isPicturesDisabled()));
			hInfo.put("title", "COUNTRY #" + id);
			hInfo.put("name", e.getLabel());
			hInfo.put("code", e.getCode());
		}
		else if (type.equals(Draw.alias)) {
			StringBuffer html = new StringBuffer();
			ArrayList<Object> lFuncParams = new ArrayList<Object>();
			lFuncParams.add(id);
			List<DrawBean> lDraw = (List<DrawBean>) DatabaseHelper.call("GetDraw", lFuncParams);
			if (lDraw != null) {
				DrawBean bean = (DrawBean) lDraw.get(0);
				html.append("<div class='shorttitle'>" + bean.getYrLabel() + "&nbsp;Draw</div>");
				html.append("<div class='draw'>");
				String[] tLevel = {"Qf1", "Qf2", "Qf3", "Qf4", "Sf1", "Sf2", "F"};
				HashMap<String, String> hLvlLabel = new HashMap<String, String>();
				hLvlLabel.put("Qf1", "Quarterfinal #1");
				hLvlLabel.put("Qf2", "Quarterfinal #2");
				hLvlLabel.put("Qf3", "Quarterfinal #3");
				hLvlLabel.put("Qf4", "Quarterfinal #4");
				hLvlLabel.put("Sf1", "Semifinal #1");
				hLvlLabel.put("Sf2", "Semifinal #2");
				hLvlLabel.put("F", "FINAL");
				for (String level : tLevel) {
					Method m1 = DrawBean.class.getMethod("getEn1" + level + "Str1");
					Method m2 = DrawBean.class.getMethod("getEn1" + level + "Str2");
					Method m3 = DrawBean.class.getMethod("getEn2" + level + "Str1");
					Method m4 = DrawBean.class.getMethod("getEn2" + level + "Str2");
					Method m5 = DrawBean.class.getMethod("getEn1" + level + "Id");
					Method m6 = DrawBean.class.getMethod("getEn2" + level + "Id");
					Method mRel1 = DrawBean.class.getMethod("getEn1" + level + "Rel1Id");
					Method mRel2 = DrawBean.class.getMethod("getEn1" + level + "Rel1Code");
					Method mRel3 = DrawBean.class.getMethod("getEn1" + level + "Rel1Label");
					Method mRel4 = DrawBean.class.getMethod("getEn1" + level + "Rel2Id");
					Method mRel5 = DrawBean.class.getMethod("getEn1" + level + "Rel2Code");
					Method mRel6 = DrawBean.class.getMethod("getEn1" + level + "Rel2Label");
					Method mRel7 = DrawBean.class.getMethod("getEn2" + level + "Rel1Id");
					Method mRel8 = DrawBean.class.getMethod("getEn2" + level + "Rel1Code");
					Method mRel9 = DrawBean.class.getMethod("getEn2" + level + "Rel1Label");
					Method mRel10 = DrawBean.class.getMethod("getEn2" + level + "Rel2Id");
					Method mRel11 = DrawBean.class.getMethod("getEn2" + level + "Rel2Code");
					Method mRel12 = DrawBean.class.getMethod("getEn2" + level + "Rel2Label");
					html.append("<div class='box " + level.toLowerCase() + "'><table><tr><th colspan='3'>" + hLvlLabel.get(level) + "</th></tr>");
					String e = getResultsEntity(bean.getDrType(), StringUtils.toInt(m5.invoke(bean)), String.valueOf(m1.invoke(bean)), String.valueOf(m2.invoke(bean)), opts.isPicturesDisabled());
					String r = getResultsEntityRel(StringUtils.toInt(mRel1.invoke(bean)), String.valueOf(mRel2.invoke(bean)), String.valueOf(mRel3.invoke(bean)), StringUtils.toInt(mRel4.invoke(bean)), String.valueOf(mRel5.invoke(bean)), String.valueOf(mRel6.invoke(bean)), false, false, opts.isPicturesDisabled());
					html.append("<td style='font-weight:bold;'>" + e + "</td>" + (r != null ? r : ""));
					html.append("<td rowspan='2'>" + DrawBean.class.getMethod("get" + (level.equalsIgnoreCase("F") ? "Rs" : "Dr") + "Result" + level).invoke(bean) + "</td></tr>");
					e = getResultsEntity(bean.getDrType(), StringUtils.toInt(m6.invoke(bean)), String.valueOf(m3.invoke(bean)), String.valueOf(m4.invoke(bean)), opts.isPicturesDisabled());
					r = getResultsEntityRel(StringUtils.toInt(mRel7.invoke(bean)), String.valueOf(mRel8.invoke(bean)), String.valueOf(mRel9.invoke(bean)), StringUtils.toInt(mRel10.invoke(bean)), String.valueOf(mRel11.invoke(bean)), String.valueOf(mRel12.invoke(bean)), false, false, opts.isPicturesDisabled());
					html.append("<td>" + e + "</td>" + (r != null ? r : ""));
					html.append("</table></div>");
				}
				html.append("</div>");
			}
			return html;
		}
		else if (type.equals(Event.alias)) {
			Event e = (Event) DatabaseHelper.loadEntity(Event.class, id);
			hInfo.put("tabtitle", e.getLabel() + "&nbsp;[Event #" + id + "]");
			hInfo.put("title", "EVENT #" + id);
			hInfo.put("name", e.getLabel());
			hInfo.put("website", (e.getWebsite() != null ? "<a href='http://" + e.getWebsite() + "' target='_blank'>" + e.getWebsite() + "</a>" : StringUtils.EMPTY));
			hInfo.put("comment", (e.getComment() != null ? e.getComment() : StringUtils.EMPTY));
		}
		else if (type.equals(Olympics.alias)) {
			Olympics e = (Olympics) DatabaseHelper.loadEntity(Olympics.class, id);
			String st = null;
			String cn = null;
			if (e.getCity().getState() != null) {
				st = HtmlUtils.writeLink(State.alias, e.getCity().getState().getId(), e.getCity().getState().getLabel());
				st = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_STATE, e.getCity().getState().getId(), ImageUtils.SIZE_SMALL, false), st);
			}
			if (e.getCity().getCountry() != null) {
				cn = HtmlUtils.writeLink(Country.alias, e.getCity().getCountry().getId(), e.getCity().getCountry().getLabel());
				cn = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, e.getCity().getCountry().getId(), ImageUtils.SIZE_SMALL, false), cn);
			}
			hInfo.put("tabtitle", e.getYear().getLabel() + " - " + e.getCity().getLabel() + "&nbsp;[Olympics #" + id + "]");
			hInfo.put("logo", HtmlUtils.writeImage(ImageUtils.INDEX_OLYMPICS, e.getId(), 'L', opts.isPicturesDisabled()));
			hInfo.put("title", "OLYMPICS #" + id);
			hInfo.put("type", ResourceUtils.get(e.getType() == 0 ? "summer" : "winter") + " " + ResourceUtils.get("olympic.games"));
			hInfo.put("year", HtmlUtils.writeLink(Year.alias, e.getYear().getId(), e.getYear().getLabel()));
			hInfo.put("city", HtmlUtils.writeLink(City.alias, e.getCity().getId(), e.getCity().getLabel()));
			hInfo.put("state", (st != null ? st : StringUtils.EMPTY));
			hInfo.put("country", (cn != null ? cn : StringUtils.EMPTY));
			hInfo.put("start.date", e.getDate1());
			hInfo.put("end.date", e.getDate2());
			hInfo.put("sports", String.valueOf(e.getCountSport()));
			hInfo.put("events", String.valueOf(e.getCountEvent()));
			hInfo.put("countries", String.valueOf(e.getCountCountry()));
			hInfo.put("athletes", String.valueOf(e.getCountPerson()));
		}
		else if (type.equals(Sport.alias)) {
			Sport e = (Sport) DatabaseHelper.loadEntity(Sport.class, id);
			hInfo.put("tabtitle", e.getLabel() + "&nbsp;[Sport #" + id + "]");
			hInfo.put("logo", HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, e.getId(), 'L', opts.isPicturesDisabled()));
			hInfo.put("title", "SPORT #" + id);
			hInfo.put("name", e.getLabel());
			hInfo.put("website", (e.getWebsite() != null ? "<a href='http://" + e.getWebsite() + "' target='_blank'>" + e.getWebsite() + "</a>" : StringUtils.EMPTY));
		}
		else if (type.equals(State.alias)) {
			State e = (State) DatabaseHelper.loadEntity(State.class, id);
			hInfo.put("tabtitle", e.getLabel() + "&nbsp;[State #" + id + "]");
			hInfo.put("logo", HtmlUtils.writeImage(ImageUtils.INDEX_STATE, e.getId(), 'L', opts.isPicturesDisabled()));
			hInfo.put("title", "STATE #" + id);
			hInfo.put("name", e.getLabel());
			hInfo.put("code", e.getCode());
			hInfo.put("capital", e.getCapital());
		}
		else if (type.equals(Team.alias)) {
			Team e = (Team) DatabaseHelper.loadEntity(Team.class, id);
			String cn = null;
			if (e.getCountry() != null) {
				cn = HtmlUtils.writeLink(Country.alias, e.getCountry().getId(), e.getCountry().getLabel());
				cn = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, e.getCountry().getId(), ImageUtils.SIZE_SMALL, false), cn);
			}
			hInfo.put("tabtitle", e.getLabel() + "&nbsp;[Team #" + id + "]");
			hInfo.put("logo", HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, e.getId(), 'L', opts.isPicturesDisabled()));
			hInfo.put("title", "TEAM #" + id);
			hInfo.put("name", e.getLabel());
			//hInfo.put("code", e.getCode());
			if (e.getConference() != null) {
				hInfo.put("conference", e.getConference());
				hInfo.put("division", e.getDivision());
			}
			hInfo.put("country", (cn != null ? cn : StringUtils.EMPTY));
			hInfo.put("sport", HtmlUtils.writeLink(Sport.alias, e.getSport().getId(), e.getSport().getLabel()));
		}
		else if (type.equals(Year.alias)) {
			Year e = (Year) DatabaseHelper.loadEntity(Year.class, id);
			hInfo.put("tabtitle", e.getLabel() + "&nbsp;[Year #" + id + "]");
			hInfo.put("title", "YEAR #" + id);
			hInfo.put("name", e.getLabel());
		}
		return HtmlUtils.writeInfoHeader(hInfo, opts.isPicturesDisabled());
	}

	public static StringBuffer getRecordRef(ArrayList<Object> params, Collection<Object> coll, RenderOptions opts) throws Exception {
		String type = String.valueOf(params.get(0));
		boolean isAllRef = !StringUtils.notEmpty(params.get(2));
		StringBuffer html = new StringBuffer();
		if (isAllRef)
			html.append("<fieldset class='ref'><legend>References (" + coll.size() + ")</legend><table class='tsort'>");
		HashMap<String, Integer> hCount = new HashMap<String, Integer>();
		// Evaluate items
		for (Object obj : coll) {
			String s = ((RefItem) obj).getEntity();
			if (!hCount.containsKey(s))
				hCount.put(s, 0);
			hCount.put(s, hCount.get(s) + 1);
		}
		// Write items
		String currentEntity = "";
		int colspan = 0;
		int count = 0;
		final int ITEM_LIMIT = 10;
		final String HTML_SEE_FULL = "<tr class='refseefull' onclick='refSeeFull(this, \"" + params.get(0) + "-" + params.get(1) + "-#ENTITY#\");'><td colspan='#COLSPAN#'></td></tr>";
		String c1 = null, c2 = null, c3 = null, c4 = null, c5 = null, c6 = null, c7 = null;
		for (Object obj : coll) {
			RefItem item = (RefItem) obj;
			String en = item.getEntity();
			if (!en.equals(currentEntity)) {
				String id = en + "-" + System.currentTimeMillis();
				StringBuffer cols = new StringBuffer();
				if (en.equals(City.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>Name</th><th onclick='sort(\"" + id + "\", this, 1);'>State</th><th onclick='sort(\"" + id + "\", this, 2);'>Country</th>");
				else if (en.equals(Complex.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>Name</th><th onclick='sort(\"" + id + "\", this, 1);'>City</th><th onclick='sort(\"" + id + "\", this, 2);'>State</th><th onclick='sort(\"" + id + "\", this, 3);'>Country</th>");
				else if (en.equals(HallOfFame.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>League</th><th onclick='sort(\"" + id + "\", this, 1);'>Year</th><th onclick='sort(\"" + id + "\", this, 2);'>Inductee</th>");
				else if (en.equals(Olympics.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'></th><th onclick='sort(\"" + id + "\", this, 1);'>Type</th><th onclick='sort(\"" + id + "\", this, 2);'>Year</th><th onclick='sort(\"" + id + "\", this, 3);'>City</th><th onclick='sort(\"" + id + "\", this, 4);'>State</th><th onclick='sort(\"" + id + "\", this, 5);'>Country</th>");
				else if (en.equals(OlympicRanking.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>Olympics</th><th onclick='sort(\"" + id + "\", this, 1);'>Country</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ImageUtils.getGoldHeader() + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ImageUtils.getSilverHeader() + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ImageUtils.getBronzeHeader() + "</th>");
				else if (en.equals(Athlete.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>Name</th><th onclick='sort(\"" + id + "\", this, 1);'>Sport</th><th onclick='sort(\"" + id + "\", this, 2);'>Team</th><th onclick='sort(\"" + id + "\", this, 3);'>Country</th>");
				else if (en.equals(Record.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>Sport</th><th onclick='sort(\"" + id + "\", this, 1);'>Category</th><th onclick='sort(\"" + id + "\", this, 2);'>Type</th><th onclick='sort(\"" + id + "\", this, 3);'>Record</th>" + (type.matches("PR|TM|CN") ? "<th onclick='sort(\"" + id + "\", this, 4);'>Rank</th>" : ""));
				else if (en.equals(Result.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>Sport</th><th onclick='sort(\"" + id + "\", this, 1);'>Championship</th><th onclick='sort(\"" + id + "\", this, 2);'>Event</th><th onclick='sort(\"" + id + "\", this, 3);'>Year</th><th onclick='sort(\"" + id + "\", this, 4);'>Result</th>");
				else if (en.equals(RetiredNumber.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>League</th><th onclick='sort(\"" + id + "\", this, 1);'>Team</th><th onclick='sort(\"" + id + "\", this, 2);'>Name</th><th onclick='sort(\"" + id + "\", this, 3);'>Number</th>");
				else if (en.equals(Team.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>Name</th><th onclick='sort(\"" + id + "\", this, 1);'>Country</th><th onclick='sort(\"" + id + "\", this, 2);'>Sport</th>");
				else if (en.equals(TeamStadium.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>League</th><th onclick='sort(\"" + id + "\", this, 1);'>Team</th><th onclick='sort(\"" + id + "\", this, 2);'>Complex</th><th onclick='sort(\"" + id + "\", this, 3);'>City</th><th onclick='sort(\"" + id + "\", this, 4);'>State</th><th onclick='sort(\"" + id + "\", this, 5);'>Country</th><th onclick='sort(\"" + id + "\", this, 6);'>Timespan</th>");
				else if (en.equals(WinLoss.alias))
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>League</th><th onclick='sort(\"" + id + "\", this, 1);'>Team</th><th onclick='sort(\"" + id + "\", this, 2);'>Type</th><th onclick='sort(\"" + id + "\", this, 3);'>W/L</th>");
				if (isAllRef && count > ITEM_LIMIT)
					html.append(HTML_SEE_FULL.replaceAll("#ENTITY#", currentEntity).replaceAll("#COLSPAN#", String.valueOf(colspan)));
				colspan = StringUtils.countIn(cols.toString(), "<th");
				html.append(StringUtils.notEmpty(currentEntity) ? "</tbody></table><table class='tsort'>" : "");
				count = 0;
				html.append("<thead><tr><th colspan='" + colspan + "'>" + HtmlUtils.writeToggleTitle(ResourceUtils.get("entity." + en) + "&nbsp;(" + hCount.get(en) + ")") + "</th></tr>");
				html.append("<tr class='rsort'>" + cols.toString() + "</tr></thead><tbody id='tb-" + id + "'>");
				currentEntity = en;
			}
			c1 = null;
			c2 = null;
			c3 = null;
			c4 = null;
			c5 = null;
			c6 = null;
			c7 = null;
			if (en.equals(City.alias)) {
				c1 = HtmlUtils.writeLink(City.alias, item.getIdItem(), item.getLabel());
				c2 = (item.getIdRel1() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_STATE, item.getIdRel1(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(State.alias, item.getIdRel1(), item.getLabelRel1())) : StringUtils.EMPTY);
				c3 = (item.getIdRel2() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel2(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Country.alias, item.getIdRel2(), item.getLabelRel2())) : StringUtils.EMPTY);
			}
			else if (en.equals(Complex.alias)) {
				c1 = HtmlUtils.writeLink(Complex.alias, item.getIdItem(), item.getLabel());
				c2 = HtmlUtils.writeLink(City.alias, item.getIdRel1(), item.getLabelRel1());
				c3 = (item.getIdRel2() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_STATE, item.getIdRel2(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(State.alias, item.getIdRel2(), item.getLabelRel2())) : StringUtils.EMPTY);
				c4 = (item.getIdRel3() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel3(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Country.alias, item.getIdRel3(), item.getLabelRel3())) : StringUtils.EMPTY);
			}
			else if (en.equals(HallOfFame.alias)) {
				c1 = item.getComment();
				c2 = HtmlUtils.writeLink(Year.alias, item.getIdRel1(), item.getLabelRel1());
				c3 = HtmlUtils.writeLink(Athlete.alias, item.getIdRel2(), item.getLabelRel2());
			}
			else if (en.equals(Olympics.alias)) {
				c1 = HtmlUtils.writeLink(Olympics.alias, item.getIdItem(), HtmlUtils.writeImage(ImageUtils.INDEX_OLYMPICS, item.getIdItem(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()));
				c2 = (item.getComment().equals("0") ? "Summer" : "Winter") + " Olympic Games";
				c3 = HtmlUtils.writeLink(Year.alias, item.getIdRel1(), item.getLabelRel1());
				c4 = HtmlUtils.writeLink(City.alias, item.getIdRel2(), item.getLabelRel2());
				c5 = (item.getIdRel3() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_STATE, item.getIdRel3(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(State.alias, item.getIdRel3(), item.getLabelRel3())) : StringUtils.EMPTY);
				c6 = (item.getIdRel4() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel4(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Country.alias, item.getIdRel4(), item.getLabelRel4())) : StringUtils.EMPTY);
			}
			else if (en.equals(OlympicRanking.alias)) {
				String[] t = item.getComment().split(",");
				c1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_OLYMPICS, item.getIdRel1(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Year.alias, item.getIdRel2(), item.getLabelRel2()) + " - " + HtmlUtils.writeLink(City.alias, item.getIdRel3(), item.getLabelRel3()));
				c2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel4(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Country.alias, item.getIdRel4(), item.getLabelRel4()));
				c3 = t[0];
				c4 = t[1];
				c5 = t[2];
			}
			else if (en.equals(Athlete.alias)) {
				c1 = HtmlUtils.writeLink(Athlete.alias, item.getIdItem(), item.getLabel());
				c2 = HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2());
				c3 = (item.getIdRel3() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdRel3(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Team.alias, item.getIdRel3(), item.getLabelRel3())) : StringUtils.EMPTY);
				c4 = (item.getIdRel1() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel1(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Country.alias, item.getIdRel1(), item.getLabelRel1())) : StringUtils.EMPTY);
			}
			else if (en.equals(Record.alias)) {
				c1 = HtmlUtils.writeLink(Sport.alias, item.getIdRel1(), item.getLabelRel1());
				c2 = (item.getIdRel2() != null ? HtmlUtils.writeLink(Championship.alias, item.getIdRel2(), item.getLabelRel2()) + " - " + HtmlUtils.writeLink(Event.alias, item.getIdRel3(), item.getLabelRel3() + (item.getIdRel4() != null ? " - " + HtmlUtils.writeLink(Event.alias, item.getIdRel4(), item.getLabelRel4()) : "")) : StringUtils.EMPTY);
				c3 = (item.getTxt1() != null ? item.getTxt1() + " - " + item.getTxt2() : StringUtils.EMPTY);
				c4 = item.getLabel();
				c5 = (item.getComment() != null ? ResourceUtils.get("rank." + (item.getComment().equals("1") ? "rec." : "") + item.getComment()) : null);
			}
			else if (en.equals(Result.alias)) {
				c1 = HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2());
				c2 = HtmlUtils.writeLink(Championship.alias, item.getIdRel3(), item.getLabelRel3());
				c3 = HtmlUtils.writeLink(Event.alias, item.getIdRel4(), item.getLabelRel4() + (item.getIdRel5() != null ? " - " + HtmlUtils.writeLink(Event.alias, item.getIdRel5(), item.getLabelRel5()) : ""));
				c4 = HtmlUtils.writeLink(Year.alias, item.getIdRel1(), item.getLabelRel1());
//				c5 = (item.getComment() != null ? ResourceUtils.get("rank." + item.getComment()) : null);
//				if (item.getComment() != null && (item.getIdRel3() == 1 || item.getIdRel3() == 3 || item.getIdRel3() == 4)) {
//					c5 = (item.getComment().equals("1") ? ImageUtils.getGoldHeader() : (item.getComment().equals("1") ? ImageUtils.getSilverHeader() : ImageUtils.getBronzeHeader()));
//					c5 = c5.replaceAll("class='bold'", "");
//				}
				if (item.getIdRel6() != null && item.getLabelRel6() != null) {
					String s = item.getLabelRel6().toLowerCase();
					String alias = (s.matches(StringUtils.PATTERN_COUNTRY) ? Country.alias : (s.matches(StringUtils.PATTERN_ATHLETE) ? Athlete.alias : Team.alias));
					boolean isScore = (item.getIdRel6() != null && item.getIdRel7() != null && StringUtils.notEmpty(item.getTxt1()) && !StringUtils.notEmpty(item.getTxt2()));
					boolean isMedal = String.valueOf(item.getIdRel3()).matches("1|3|4");
					if (isScore && !isMedal)
						c5 = HtmlUtils.writeLink(alias, item.getIdRel6(), item.getLabelRel6()) + "&nbsp;def.&nbsp;" + HtmlUtils.writeLink(alias, item.getIdRel7(), item.getLabelRel7()) + "&nbsp;" + item.getTxt1();
					else {
						StringBuffer sb = new StringBuffer("<table><tr>");
						sb.append("<td>" + (isMedal ? "<img src='img/render/gold-mini.png'/>" : "1.") + "&nbsp;</td><td>" + HtmlUtils.writeLink(alias, item.getIdRel6(), item.getLabelRel6()) + "&nbsp;</td>");
						if (item.getIdRel7() != null)
							sb.append("<td>" + (isMedal ? "<img src='img/render/silver-mini.png'/>" : "2.") + "&nbsp;</td><td>" + HtmlUtils.writeLink(alias, item.getIdRel7(), item.getLabelRel7()) + "&nbsp;</td>");
						if (item.getIdRel8() != null)
							sb.append("<td>" + (isMedal ? "<img src='img/render/bronze-mini.png'/>" : "3.") + "&nbsp;</td><td>" + HtmlUtils.writeLink(alias, item.getIdRel8(), item.getLabelRel8()) + "</td>");
						c5 = sb.append("</tr></table>").toString();
					}
				}
			}
			else if (en.equals(RetiredNumber.alias)) {
				c1 = item.getComment();
				c2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdRel1(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Team.alias, item.getIdRel1(), item.getLabelRel1()));
				c3 = HtmlUtils.writeLink(Athlete.alias, item.getIdRel2(), item.getLabelRel2());
				c4 = "#" + item.getIdRel3();
			}
			else if (en.equals(Team.alias)) {
				c1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdItem(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Team.alias, item.getIdItem(), item.getLabel()));
				c2 = (item.getIdRel1() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel1(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Country.alias, item.getIdRel1(), item.getLabelRel1())) : StringUtils.EMPTY);
				c3 = HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2());
			}
			else if (en.equals(TeamStadium.alias)) {
				c1 = item.getComment();
				c2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdRel1(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Team.alias, item.getIdRel1(), item.getLabelRel1()));
				c3 = HtmlUtils.writeLink(Complex.alias, item.getIdRel2(), item.getLabelRel2());
				c4 = HtmlUtils.writeLink(City.alias, item.getIdRel3(), item.getLabelRel3());
				c5 = (item.getIdRel4() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_STATE, item.getIdRel4(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(State.alias, item.getIdRel4(), item.getLabelRel4())) : StringUtils.EMPTY);
				c6 = (item.getIdRel5() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel5(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Country.alias, item.getIdRel5(), item.getLabelRel5())) : StringUtils.EMPTY);
				c7 = item.getTxt1() + "-" + (item.getTxt2() != null && !item.getTxt2().equals("0") ? item.getTxt2() : "");
			}
			else if (en.equals(WinLoss.alias)) {
				c1 = item.getComment();
				c2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdRel1(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Team.alias, item.getIdRel1(), item.getLabelRel1()));
				c3 = item.getTxt1();
				c4 = item.getTxt2();
			}
			if (!isAllRef || count++ < ITEM_LIMIT) {
				html.append("<tr>" + (c1 != null ? "<td class='srt'>" + c1 + "</td>" : "") + (c2 != null ? "<td class='srt'>" + c2 + "</td>" : ""));
				html.append((c3 != null ? "<td class='srt'>" + c3 + "</td>" : "") + (c4 != null ? "<td class='srt'>" + c4 + "</td>" : ""));
				html.append((c5 != null ? "<td class='srt'>" + c5 + "</td>" : "") + (c6 != null ? "<td class='srt'>" + c6 + "</td>" : ""));
				html.append((c7 != null ? "<td class='srt'>" + c7 + "</td>" : "") + "</tr>");
			}
		}
		if (isAllRef && count > ITEM_LIMIT)
			html.append(HTML_SEE_FULL.replaceAll("#ENTITY#", currentEntity).replaceAll("#COLSPAN#", String.valueOf(colspan)));
		html.append(isAllRef ? "</tbody></table></fieldset>" : "");
		return html;
	}

	public static StringBuffer convertResults(Collection<Object> coll, Championship cp, Event ev, RenderOptions opts) throws Exception {
		// Evaluate columns
		short entityCount = 0;
		short[] tColspan = {1, 1, 1, 1, 1, 1, 1, 1, 1};
		boolean[] tIsEntityRel1 = {false, false, false, false, false, false, false, false, false};
		boolean[] tIsEntityRel2 = {false, false, false, false, false, false, false, false, false};
		boolean[] tIsResult = {false, false, false, false, false, false, false, false, false};
		boolean isDates = false;
		boolean isPlace = false;
		boolean isDraw = false;
		boolean isComment = false;
		int type = ev.getType().getNumber();
		boolean isDoubles = (type == 4);
		boolean isMedal = String.valueOf(cp.getId()).matches("1|3|4");
		ArrayList<Integer> listEqDoubles = new ArrayList<Integer>();
		listEqDoubles.add(1);listEqDoubles.add(2);listEqDoubles.add(-1);
		listEqDoubles.add(3);listEqDoubles.add(4);listEqDoubles.add(-1);
		listEqDoubles.add(5);listEqDoubles.add(6);listEqDoubles.add(-1);
		listEqDoubles.add(7);listEqDoubles.add(8);
		for (Object obj : coll) {
			ResultsBean bean = (ResultsBean) obj;
			List<Integer> listEq = StringUtils.tieList(bean.getRsExa());
			String sListEq = listEq.toString();
			entityCount = (entityCount < 1 && bean.getRsRank1() != null ? 1 : entityCount);
			entityCount = (entityCount < 2 && bean.getRsRank2() != null && (listEq.indexOf(2) <= 0 || StringUtils.countIn(sListEq, "-1") >= 1) ? 2 : entityCount);
			entityCount = (entityCount < 3 && bean.getRsRank3() != null && (listEq.indexOf(3) <= 0 || StringUtils.countIn(sListEq, "-1") >= 2) ? 3 : entityCount);
			entityCount = (entityCount < 4 && bean.getRsRank4() != null && (listEq.indexOf(4) <= 0 || StringUtils.countIn(sListEq, "-1") >= 3) ? 4 : entityCount);
			entityCount = (entityCount < 5 && bean.getRsRank5() != null && (listEq.indexOf(5) <= 0 || StringUtils.countIn(sListEq, "-1") >= 4) ? 5 : entityCount);
			entityCount = (entityCount < 6 && bean.getRsRank6() != null && (listEq.indexOf(6) <= 0 || StringUtils.countIn(sListEq, "-1") >= 5) ? 6 : entityCount);
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
			isPlace |= (bean.getCxId() != null || bean.getCt2Id() != null);
			isDraw |= (bean.getDrId() != null);
			isComment |= StringUtils.notEmpty(bean.getRsComment());
		}
		entityCount /= (isDoubles ? 2 : 1);
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

		long id = System.currentTimeMillis();
		ArrayList<String> lIds = new ArrayList<String>();
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		html.append("<thead><tr class='rsort'><th" + (isDraw || isComment ? " colspan='" + (isDraw && isComment ? 3 : 2) + "'" : "") + " onclick='sort(\"" + id + "\", this, 0);'>Year</th>");
		for (int i = 1 ; i <= entityCount ; i++)
			html.append(i == 2 && isScore ? "<th onclick='sort(\"" + id + "\", this, " + i + ");'>Score</th>" : "").append("<th colspan='" + tColspan[i - 1] + "' onclick='sort(\"" + id + "\", this, " + i + ");'>" + (isMedal ? (i == 1 ? ImageUtils.getGoldHeader() : (i == 2 ? ImageUtils.getSilverHeader() : ImageUtils.getBronzeHeader())) : ResourceUtils.get("rank." + i)) + "</th>");
		if (isDates)
			html.append("<th onclick='sort(\"" + id + "\", this, " + (entityCount + 1) + ");'>Date</th>");
		if (isPlace)
			html.append("<th onclick='sort(\"" + id + "\", this, " + (entityCount + (isDates ? 1 : 0) + 1) + ");'>Place</th>");
		html.append("</tr></thead><tbody id='tb-" + id + "'>");
		for (Object obj : coll) {
			ResultsBean bean = (ResultsBean) obj;
			lIds.add(String.valueOf(bean.getRsId()));

			// Evaluate bean
			String draw = "<a><img src='img/render/draw.gif' title='Draw' style='cursor:pointer;' onclick='javascript:info(\"DR-" + bean.getRsId() + "\")'/></a>";
			String year = HtmlUtils.writeLink(Year.alias, bean.getYrId(), bean.getYrLabel());
			String dates = (StringUtils.notEmpty(bean.getRsDate1()) ? StringUtils.toTextDate(bean.getRsDate1(), Locale.ENGLISH) + "&nbsp;&rarr;&nbsp;" : "") + (StringUtils.notEmpty(bean.getRsDate2()) ? StringUtils.toTextDate(bean.getRsDate2(), Locale.ENGLISH) : "");
			String place = null;
			String comment = (StringUtils.notEmpty(bean.getRsComment()) ? bean.getRsComment().replaceAll("\\|", "<br/>") : null);
			if (bean.getCxId() != null) {
				String img1 = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn1Id(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
				String img2 = null;
				place = HtmlUtils.writeLink(Complex.alias, bean.getCxId(), bean.getCxLabel());
				place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(City.alias, bean.getCt1Id(), bean.getCt1Label());
				if (bean.getSt1Id() != null) {
					place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(State.alias, bean.getSt1Id(), bean.getSt1Code());
					img2 = HtmlUtils.writeImage(ImageUtils.INDEX_STATE, bean.getSt1Id(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
				}
				place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(Country.alias, bean.getCn1Id(), bean.getCn1Code()); 
				if (img2 != null)
					place = HtmlUtils.writeImgTable(img2, place);
				place = HtmlUtils.writeImgTable(img1, place);
			}
			else if (bean.getCt2Id() != null) {
				String img1 = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn2Id(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
				String img2 = null;
				place = HtmlUtils.writeLink(City.alias, bean.getCt2Id(), bean.getCt2Label());
				if (bean.getSt2Id() != null) {
					place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(State.alias, bean.getSt2Id(), bean.getSt2Code());
					img2 = HtmlUtils.writeImage(ImageUtils.INDEX_STATE, bean.getSt2Id(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
				}
				place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(Country.alias, bean.getCn2Id(), bean.getCn2Code());
				if (img2 != null)
					place = HtmlUtils.writeImgTable(img2, place);
				place = HtmlUtils.writeImgTable(img1, place);
			}
			List<Integer> listEq = (isDoubles ? listEqDoubles : StringUtils.tieList(bean.getRsExa()));
			String[] tEntity = {null, null, null, null, null, null, null, null, null};
			String[] tEntityRel = {null, null, null, null, null, null, null, null, null};
			String[] tEntityHtml = {null, null, null, null, null, null, null, null, null};
			String[] tResult = {null, null, null, null, null, null, null, null, null};
			if (bean.getRsRank1() != null) {
				tEntity[0] = getResultsEntity(type, bean.getRsRank1(), bean.getEn1Str1(), bean.getEn1Str2(), opts.isPicturesDisabled());
				tEntityRel[0] = getResultsEntityRel(bean.getEn1Rel1Id(), bean.getEn1Rel1Code(), bean.getEn1Rel1Label(), bean.getEn1Rel2Id(), bean.getEn1Rel2Code(), bean.getEn1Rel2Label(), tIsEntityRel1[0], tIsEntityRel2[0], opts.isPicturesDisabled());
				tResult[0] = bean.getRsResult1();
			}
			if (bean.getRsRank2() != null) {
				tEntity[1] = getResultsEntity(type, bean.getRsRank2(), bean.getEn2Str1(), bean.getEn2Str2(), opts.isPicturesDisabled());
				tEntityRel[1] = getResultsEntityRel(bean.getEn2Rel1Id(), bean.getEn2Rel1Code(), bean.getEn2Rel1Label(), bean.getEn2Rel2Id(), bean.getEn2Rel2Code(), bean.getEn2Rel2Label(), tIsEntityRel1[1], tIsEntityRel2[1], opts.isPicturesDisabled());
				tResult[1] = bean.getRsResult2();
			}
			if (bean.getRsRank3() != null) {
				tEntity[2] = getResultsEntity(type, bean.getRsRank3(), bean.getEn3Str1(), bean.getEn3Str2(), opts.isPicturesDisabled());
				tEntityRel[2] = getResultsEntityRel(bean.getEn3Rel1Id(), bean.getEn3Rel1Code(), bean.getEn3Rel1Label(), bean.getEn3Rel2Id(), bean.getEn3Rel2Code(), bean.getEn3Rel2Label(), tIsEntityRel1[2], tIsEntityRel2[2], opts.isPicturesDisabled());
				tResult[2] = bean.getRsResult3();
			}
			if (bean.getRsRank4() != null) {
				tEntity[3] = getResultsEntity(type, bean.getRsRank4(), bean.getEn4Str1(), bean.getEn4Str2(), opts.isPicturesDisabled());
				tEntityRel[3] = getResultsEntityRel(bean.getEn3Rel1Id(), bean.getEn4Rel1Code(), bean.getEn4Rel1Label(), bean.getEn4Rel2Id(), bean.getEn4Rel2Code(), bean.getEn4Rel2Label(), tIsEntityRel1[3], tIsEntityRel2[3], opts.isPicturesDisabled());
				tResult[3] = bean.getRsResult4();
			}
			if (bean.getRsRank5() != null) {
				tEntity[4] = getResultsEntity(type, bean.getRsRank5(), bean.getEn5Str1(), bean.getEn5Str2(), opts.isPicturesDisabled());
				tEntityRel[4] = getResultsEntityRel(bean.getEn5Rel1Id(), bean.getEn5Rel1Code(), bean.getEn5Rel1Label(), bean.getEn5Rel2Id(), bean.getEn5Rel2Code(), bean.getEn5Rel2Label(), tIsEntityRel1[4], tIsEntityRel2[4], opts.isPicturesDisabled());
				tResult[4] = bean.getRsResult5();
			}
			if (bean.getRsRank6() != null) {
				tEntity[5] = getResultsEntity(type, bean.getRsRank6(), bean.getEn6Str1(), bean.getEn6Str2(), opts.isPicturesDisabled());
				tEntityRel[5] = getResultsEntityRel(bean.getEn6Rel1Id(), bean.getEn6Rel1Code(), bean.getEn6Rel1Label(), bean.getEn6Rel2Id(), bean.getEn6Rel2Code(), bean.getEn6Rel2Label(), tIsEntityRel1[5], tIsEntityRel2[5], opts.isPicturesDisabled());
			}
			if (bean.getRsRank7() != null) {
				tEntity[6] = getResultsEntity(type, bean.getRsRank7(), bean.getEn7Str1(), bean.getEn7Str2(), opts.isPicturesDisabled());
				tEntityRel[6] = getResultsEntityRel(bean.getEn7Rel1Id(), bean.getEn7Rel1Code(), bean.getEn7Rel1Label(), bean.getEn7Rel2Id(), bean.getEn7Rel2Code(), bean.getEn7Rel2Label(), tIsEntityRel1[6], tIsEntityRel2[6], opts.isPicturesDisabled());
			}
			if (bean.getRsRank8() != null) {
				tEntity[7] = getResultsEntity(type, bean.getRsRank8(), bean.getEn8Str1(), bean.getEn8Str2(), opts.isPicturesDisabled());
				tEntityRel[7] = getResultsEntityRel(bean.getEn8Rel1Id(), bean.getEn8Rel1Code(), bean.getEn8Rel1Label(), bean.getEn8Rel2Id(), bean.getEn8Rel2Code(), bean.getEn8Rel2Label(), tIsEntityRel1[7], tIsEntityRel2[7], opts.isPicturesDisabled());
			}
			if (bean.getRsRank9() != null) {
				tEntity[8] = getResultsEntity(type, bean.getRsRank9(), bean.getEn9Str1(), bean.getEn9Str2(), opts.isPicturesDisabled());
				tEntityRel[8] = getResultsEntityRel(bean.getEn9Rel1Id(), bean.getEn9Rel1Code(), bean.getEn9Rel1Label(), bean.getEn9Rel2Id(), bean.getEn9Rel2Code(), bean.getEn9Rel2Label(), tIsEntityRel1[8], tIsEntityRel2[8], opts.isPicturesDisabled());
			}
			if (listEq != null && !listEq.isEmpty()) {
				Integer idx = listEq.get(0) - 1;
				for (int i = 1 ; i < listEq.size() ; i++) {
					if (idx == null)
						idx = listEq.get(i) - 1;
					else {
						if (listEq.get(i) == -1)
							idx = null;
						if (idx != null) {
							if (tEntity[idx] != null && tEntity[listEq.get(i) - 1] != null) {
								tEntity[idx] = tEntity[idx].concat((type < 10 ? "<br/>" : "") + tEntity[listEq.get(i) - 1].replaceAll("<table>", "<table class='margintop'>"));
								tEntity[listEq.get(i) - 1] = null;
							}
							if (tEntityRel[idx] != null && tEntityRel[listEq.get(i) - 1] != null) {
								tEntityRel[idx] = tEntityRel[idx].concat(tEntityRel[listEq.get(i) - 1]).replaceAll("\\<\\/td\\>\\<td\\>\\<table\\>", "<table class='margintop'>");
								tEntityRel[listEq.get(i) - 1] = null;
								if (tEntityRel[idx].matches(".*CN\\-\\d+.*") && tEntityRel[idx].matches(".*TM\\-\\d+.*")) {
									String[] t = tEntityRel[idx].split("</table>");
									StringBuffer sbCN = new StringBuffer();
									StringBuffer sbTM = new StringBuffer();
									for (String s : t) {
										if (s.matches(".*CN\\-\\d+.*"))
											sbCN.append(s.replaceAll("^\\<td\\>|\\<\\/td\\>$|\\<\\/td\\>\\<td\\>| class='margintop'", "").replaceAll("\\<table\\>", "<table class='marginbottom'>")).append("</table>");
										if (s.matches(".*TM\\-\\d+.*"))
											sbTM.append(s.replaceAll("^\\<td\\>|\\<\\/td\\>$|\\<\\/td\\>\\<td\\>| class='margintop'", "").replaceAll("\\<table\\>", "<table class='marginbottom'>")).append("</table>");
									}
									tEntityRel[idx] = "<td>" + sbCN.toString() + "</td><td>" + sbTM.toString() + "</td>";
								}
							}
						}
					}
				}
				tEntity = StringUtils.removeNulls(tEntity);
				tEntityRel = StringUtils.removeNulls(tEntityRel);
			}
			for (int i = 0 ; i < 9 ; i++)
				if (tEntity[i] != null)
					tEntityHtml[i] = ("<td class='srt'" + (i == 0 ? " style='font-weight:bold;'" : "") + ">" + tEntity[i] + "</td>" + (StringUtils.notEmpty(tEntityRel[i]) ?  tEntityRel[i] : (tIsEntityRel1[i] ? "<td></td>" : "") + (tIsEntityRel2[i] ? "<td></td>" : ""))) + (StringUtils.notEmpty(tResult[i]) ? "<td" + (isScore && i == 0 ? " class='centered'" : "") + ">" + tResult[i] + "</td>" : (tIsResult[i] ? "<td></td>" : ""));
			String commentTitle = null;
			String commentColor = null;
			if (comment != null && comment.matches("^\\#\\#(Clay|Decoturf|Grass|Gravel|Hard|Rebound|Snow|Tarmac).*")) {
				commentTitle = comment.substring(2);
				commentColor = StringUtils.getCommentColor(commentTitle);
				comment = null;
			}
			
			// Write line
			html.append("<tr>" + (isDraw ? "<td>" + (bean.getDrId() != null ? draw : "") + "</td>" : ""));
			html.append(isComment ? "<td" + (StringUtils.notEmpty(commentTitle) ? " title='" + commentTitle + "' style='width:15px;background-color:" + commentColor + "';" : "") + ">" + (comment != null ? HtmlUtils.writeComment(bean.getRsId(), comment) : "") + "</td>" : "");
			html.append("<td class='srt'>" + year + "</td>");
			for (int i = 0 ; i < 9 ; i++)
				html.append(tEntityHtml[i] != null ? tEntityHtml[i] : (entityCount > i ? "<td class='srt' colspan=" + tColspan[i] + ">" + StringUtils.EMPTY + "</td>" : ""));
			html.append(isDates ? "<td class='srt'>" + dates + "</td>" : "");
			html.append((isPlace ? "<td class='srt'>" + place + "</td>" : "") + "</tr>");
		}
		html.append(getWinRecords(StringUtils.implode(lIds, ",")));
		html.append("</tbody></table>");
		return html;
	}

	public static void convertTreeJSON(Collection<Object> coll, Writer writer) throws IOException {
		writer.write("var treeItems=[['',null,");
		ArrayList<Object> lst = new ArrayList<Object>(coll);
		int i, j, k, l;
		for (i = 0 ; i < lst.size() ; i++) {
			TreeItem item = (TreeItem) lst.get(i);
			writer.write(i > 0 ? "," : "");
			writer.write("['" + StringUtils.toTree(item.getLabel()) + "','" + item.getIdItem() + "',");
			for (j = i + 1 ; j < lst.size() ; j++) {
				TreeItem item2 = (TreeItem) lst.get(j);
				if (item2.getLevel() < 2) {j--; break;}
				writer.write(j > i + 1 ? "," : "");
				writer.write("['" + StringUtils.toTree(item2.getLabel()) + "','" + item.getIdItem() + "_" + item2.getIdItem() + "',");
				for (k = j + 1 ; k < lst.size() ; k++) {
					TreeItem item3 = (TreeItem) lst.get(k);
					if (item3.getLevel() < 3) {k--; break;}
					writer.write(k > j + 1 ? "," : "");
					writer.write("['" + StringUtils.toTree(item3.getLabel()) + "','" + item.getIdItem() + "_" + item2.getIdItem() + "_" + item3.getIdItem() + "',");
					for (l = k + 1 ; l < lst.size() ; l++) {
						TreeItem item4 = (TreeItem) lst.get(l);
						if (item4.getLevel() < 4) {l--; break;}
						writer.write(l > k + 1 ? "," : "");
						writer.write("['" + StringUtils.toTree(item4.getLabel()) + "','" + item.getIdItem() + "_" + item2.getIdItem() + "_" + item3.getIdItem() + "_" + item4.getIdItem() + "']");
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
		writer.write("]];");
	}

	public static StringBuffer convertSearch(Collection<Object> coll, String pattern, boolean isRef, RenderOptions opts) throws Exception {
		if (coll == null || coll.isEmpty())
			return new StringBuffer("<div class='noresult'>No result found matching your criteria.</div>");
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		String currentEntity = "";
		HashMap<String, Integer> hCount = new HashMap<String, Integer>();
		// Evaluate items
		for (Object obj : coll) {
			String s = ((RefItem) obj).getEntity();
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
			if (!en.equals(currentEntity)) {
				String id = en + "-" + System.currentTimeMillis();
				int sortIndex = 0;
				html.append(StringUtils.notEmpty(currentEntity) ? "</tbody></table><table class='tsort'>" : "");
				StringBuffer cols = new StringBuffer("<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>Name</th>");
				if (en.matches("PR|TM"))
					cols.append("<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>Country</th><th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>Sport</th>" + (en.matches("PR") ? "<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>Team</th>" : ""));
				else if (en.matches("CX|CT"))
					cols.append((en.matches("CX") ? "<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>City</th>" : "") + "<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>State</th><th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>Country</th>");
				html.append("<thead><tr><th colspan=" + (StringUtils.countIn(cols.toString(), "<th") + 1) + ">");
				html.append(HtmlUtils.writeToggleTitle(ResourceUtils.get("entity." + en) + "&nbsp;(" + hCount.get(en) + ")") + "</th></tr>");
				html.append("<tr class='rsort'>" + cols.toString() + (isRef ? "<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>References</th>" : "") + "</tr></thead><tbody id='tb-" + id + "'>");
				currentEntity = en;
			}
			String name = item.getLabel();
			int p1 = name.toLowerCase().indexOf(pattern.toLowerCase());
			if (p1 > -1) {
				int p2 = p1 + pattern.length();
				name = name.substring(0, p1) + "<b>" + name.substring(p1, p2) + "</b>" + name.substring(p2);
			}
			name = HtmlUtils.writeLink(en, item.getIdItem(), name);
			if (en.matches("CP|CN|OL|SP|ST|TM")) {
				img = HtmlUtils.writeImage(ImageUtils.getIndex(en), item.getIdItem(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
				name = HtmlUtils.writeImgTable(img, name);
			}
			// Handle relations
			String rel1 = "";
			String rel2 = "";
			String rel3 = "";
			if (en.matches("PR|TM")) {
				if(en.equals("PR") && item.getLink() != null && item.getLink() == 0) {
					int currentRel1Id = 0;
					int currentRel2Id = 0;
					int currentRel3Id = 0;
					StringBuffer sbRel1 = new StringBuffer();
					StringBuffer sbRel2 = new StringBuffer();
					StringBuffer sbRel3 = new StringBuffer();
					for (Athlete a : (List<Athlete>) DatabaseHelper.execute("from Athlete where id = " + item.getIdItem() + " or link = " + item.getIdItem() + " order by id")) {
						if (a.getCountry() != null && a.getCountry().getId() != currentRel1Id) {
							link = HtmlUtils.writeLink(Country.alias, a.getCountry().getId(), a.getCountry().getLabel() + " (" + a.getCountry().getCode() + ")");
							img = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, a.getCountry().getId(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
							sbRel1.append(HtmlUtils.writeImgTable(img, link));
							currentRel1Id = a.getCountry().getId();
						}
						if (a.getSport() != null && a.getSport().getId() != currentRel2Id) {
							link = HtmlUtils.writeLink(Country.alias, a.getSport().getId(), a.getSport().getLabel());
							img = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, a.getSport().getId(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
							sbRel2.append(HtmlUtils.writeImgTable(img, link));
							currentRel2Id = a.getSport().getId();
						}
						if (a.getTeam() != null && a.getTeam().getId() != currentRel3Id) {
							link = HtmlUtils.writeLink(Country.alias, a.getTeam().getId(), a.getTeam().getLabel());
							img = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, a.getTeam().getId(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
							sbRel3.append(HtmlUtils.writeImgTable(img, link));
							currentRel3Id = a.getTeam().getId();
						}
					}
					rel1 = sbRel1.toString().replaceAll("</table><table>", "</table><table class='margintop'>");
					rel2 = sbRel2.toString().replaceAll("</table><table>", "</table><table class='margintop'>");
					rel3 = sbRel3.toString().replaceAll("</table><table>", "</table><table class='margintop'>");
				}
				else {
					if (item.getIdRel1() != null) {
						link = HtmlUtils.writeLink(Country.alias, item.getIdRel1(), item.getLabelRel1());
						img = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel1(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
						rel1 = HtmlUtils.writeImgTable(img, link);
					}
					if (item.getIdRel2() != null) {
						link = HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2());
						img = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, item.getIdRel2(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
						rel2 = HtmlUtils.writeImgTable(img, link);
					}
					if (item.getIdRel3() != null) {
						link = HtmlUtils.writeLink(Team.alias, item.getIdRel3(), item.getLabelRel3());
						img = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdRel3(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
						rel3 = HtmlUtils.writeImgTable(img, link);
					}
				}
			}
			else if (en.matches("CX|CT")) {
				if (item.getIdRel1() != null) {
					link = HtmlUtils.writeLink(City.alias, item.getIdRel1(), item.getLabelRel1());
					rel1 = link;
				}
				if (item.getIdRel2() != null) {
					link = HtmlUtils.writeLink(State.alias, item.getIdRel2(), item.getLabelRel2());
					img = HtmlUtils.writeImage(ImageUtils.INDEX_STATE, item.getIdRel2(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
					rel2 = HtmlUtils.writeImgTable(img, link);
				}
				if (item.getIdRel3() != null) {
					link = HtmlUtils.writeLink(Country.alias, item.getIdRel3(), item.getLabelRel3());
					img = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel3(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
					rel3 = HtmlUtils.writeImgTable(img, link);
				}
			}
			if (en.matches("PR|TM|CX|CT")) {
				rel1 = (!en.equals("CT") ? "<td class='srt'>" + (StringUtils.notEmpty(rel1) ? rel1 : StringUtils.EMPTY) + "</td>" : "");
				rel2 = "<td class='srt'>" + (StringUtils.notEmpty(rel2) ? rel2 : StringUtils.EMPTY) + "</td>";
				rel3 = (!en.equals("TM") ? "<td class='srt'>" + (StringUtils.notEmpty(rel3) ? rel3 : StringUtils.EMPTY) + "</td>" : "");
			}

			// Write line
			html.append("<tr><td class='srt'>" + name + "</td>" + rel1 + rel2 + rel3);
			if (isRef)
				html.append("<td class='centered srt'>" + item.getCountRef() + "</td>");
			html.append("</tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertOlympicMedals(Collection<Object> coll, RenderOptions opts) throws Exception {
		int currentOlympics = 0;
		boolean isIndividual = false;
		boolean isResult = false;
		// Evaluate columns
		for (Object obj : coll) {
			OlympicMedalsBean bean = (OlympicMedalsBean) obj;
			isResult |= (bean.getRsResult1() != null);
			isIndividual |= ((bean.getTp2Number() != null ? bean.getTp2Number() : bean.getTp1Number()) <= 10);
		}
		int colspan = (isIndividual ? 2 : 1) + (isResult ? 1 : 0);
		String olympics = null;
		String tmpImg = null;
		long id = System.currentTimeMillis();
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		html.append("<table class='tsort'><thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>Year</th><th onclick='sort(\"" + id + "\", this, 1);'>Event</th>");
		html.append("<th colspan='" + colspan + "' onclick='sort(\"" + id + "\", this, 2);'>" + ImageUtils.getGoldHeader() + "</th>");
		html.append("<th colspan='" + colspan + "' onclick='sort(\"" + id + "\", this, 3);'>" + ImageUtils.getSilverHeader() + "</th>");
		html.append("<th colspan='" + colspan + "' onclick='sort(\"" + id + "\", this, 4);'>" + ImageUtils.getBronzeHeader() + "</th>");
		html.append("</tr></thead><tbody id='tb-" + id + "'>");
		for (Object obj : coll) {
			OlympicMedalsBean bean = (OlympicMedalsBean) obj;
			boolean isIndividual_ = ((bean.getTp2Number() != null ? bean.getTp2Number() : bean.getTp1Number()) <= 10);
			if (!bean.getYrId().equals(currentOlympics)) {
				tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn1Id() != null ? bean.getCn1Id() : bean.getCn2Id(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
				olympics = HtmlUtils.writeLink(Olympics.alias, bean.getOlId(), bean.getYrLabel() + "&nbsp;-&nbsp;" + bean.getOlCity());
				olympics = HtmlUtils.writeImgTable(tmpImg, olympics);
				currentOlympics = bean.getYrId();
			}

			// Evaluate bean
			String entity1 = null, entityCn1 = null;
			String entity2 = null, entityCn2 = null;
			String entity3 = null, entityCn3 = null;
			String place = null;
			if (bean.getRsRank1() != null) {
				if (isIndividual_) {
					entity1 = HtmlUtils.writeLink(Athlete.alias, bean.getRsRank1(), bean.getPr1LastName() + "," + HtmlUtils.SPACE + bean.getPr1FirstName());
					entityCn1 = getResultsEntityRel(null, null, null, bean.getPr1CnId(), bean.getPr1CnCode(), bean.getPr1CnLabel(), false, false, opts.isPicturesDisabled());
				}
				else
					entity1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getRsRank1(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Country.alias, bean.getRsRank1(), bean.getCn1Code()));
			}
			if (bean.getRsRank2() != null) {
				if (isIndividual_) {
					entity2 = HtmlUtils.writeLink(Athlete.alias, bean.getRsRank2(), bean.getPr2LastName() + "," + HtmlUtils.SPACE + bean.getPr2FirstName());
					entityCn2 = getResultsEntityRel(null, null, null, bean.getPr2CnId(), bean.getPr2CnCode(), bean.getPr2CnLabel(), false, false, opts.isPicturesDisabled());
				}
				else
					entity2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getRsRank2(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Country.alias, bean.getRsRank2(), bean.getCn2Code()));
			}
			if (bean.getRsRank3() != null) {
				if (isIndividual_) {
					entity3 = HtmlUtils.writeLink(Athlete.alias, bean.getRsRank3(), bean.getPr3LastName() + "," + HtmlUtils.SPACE + bean.getPr3FirstName());
					entityCn3 = getResultsEntityRel(null, null, null, bean.getPr3CnId(), bean.getPr3CnCode(), bean.getPr3CnLabel(), false, false, opts.isPicturesDisabled());
				}
				else
					entity3 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getRsRank3(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()), HtmlUtils.writeLink(Country.alias, bean.getRsRank3(), bean.getCn3Code()));
			}
			if (bean.getCxId() != null) {
				tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn1Id(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());				
				place = HtmlUtils.writeLink(Complex.alias, bean.getCxId(), bean.getCxLabel());
				place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(City.alias, bean.getCt1Id(), bean.getCt1Label());
				if (bean.getSt1Id() != null)
					place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(State.alias, bean.getSt1Id(), bean.getSt1Code());
				place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(Country.alias, bean.getCn1Id(), bean.getCn1Code_()); 
				place = HtmlUtils.writeImgTable(tmpImg, place);
			}
			else if (bean.getCt2Id() != null) {
				tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn2Id(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
				place = HtmlUtils.writeLink(City.alias, bean.getCt2Id(), bean.getCt2Label());
				if (bean.getSt2Id() != null)
					place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(State.alias, bean.getSt2Id(), bean.getSt2Code());
				place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(Country.alias, bean.getCn2Id(), bean.getCn2Code_()); 
				place = HtmlUtils.writeImgTable(tmpImg, place);
			}

			// Write line
			html.append("<tr><td class='srt'>" + olympics + "</td><td class='srt'>" + bean.getEvLabel() + (StringUtils.notEmpty(bean.getSeLabel()) ? " - " + bean.getSeLabel() : "") + "</td>");
			html.append(entity1 != null ? "<td class='srt'" + (!isIndividual_ && isIndividual ? " colspan='2'" : "") + ">" + entity1 + "</td>" + (entityCn1 != null ? entityCn1 : "") + (isResult ? "<td>" + (bean.getRsResult1() != null ? bean.getRsResult1() : "") + "</td>" : "") : "<td colspan=" + colspan + ">" + StringUtils.EMPTY + "</td>");
			html.append(entity2 != null ? "<td class='srt'" + (!isIndividual_ && isIndividual ? " colspan='2'" : "") + ">" + entity2 + "</td>" + (entityCn2 != null ? entityCn2 : "") + (isResult ? "<td>" + (bean.getRsResult2() != null ? bean.getRsResult2() : "") + "</td>" : "") : "<td colspan=" + colspan + ">" + StringUtils.EMPTY + "</td>");
			html.append(entity3 != null ? "<td class='srt'" + (!isIndividual_ && isIndividual ? " colspan='2'" : "") + ">" + entity3 + "</td>" + (entityCn3 != null ? entityCn3 : "") + (isResult ? "<td>" + (bean.getRsResult3() != null ? bean.getRsResult3() : "") + "</td>" : "") : "<td colspan=" + colspan + ">" + StringUtils.EMPTY + "</td>");
			html.append("</tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertOlympicRankings(Collection<Object> coll, RenderOptions opts) throws Exception {
		int currentOlympics = 0;
		String olympics = null;
		String tmpImg = null;
		long id = System.currentTimeMillis();
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		html.append("<table class='tsort'><thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>Year</th><th onclick='sort(\"" + id + "\", this, 1);'>Country</th>");
		html.append("<th onclick='sort(\"" + id + "\", this, 2);'>" + ImageUtils.getGoldHeader() + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ImageUtils.getSilverHeader() + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ImageUtils.getBronzeHeader() + "</th>");
		html.append("</tr></thead><tbody id='tb-" + id + "'>");
		for (Object obj : coll) {
			OlympicRankingsBean bean = (OlympicRankingsBean) obj;
			if (!bean.getYrId().equals(currentOlympics)) {
				tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn2Id(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
				olympics = HtmlUtils.writeLink(Olympics.alias, bean.getOlId(), bean.getYrLabel() + "&nbsp;-&nbsp;" + bean.getCtLabel());
				olympics = HtmlUtils.writeImgTable(tmpImg, olympics);
				currentOlympics = bean.getYrId();
			}

			// Evaluate bean
			String country = HtmlUtils.writeLink(Country.alias, bean.getCn1Id(), bean.getCn1Label());
			tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn1Id(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
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

	public static StringBuffer convertHallOfFame(Collection<Object> coll, RenderOptions opts) throws Exception {
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		long id = System.currentTimeMillis();
		html.append("<tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>Year</th><th onclick='sort(\"" + id + "\", this, 1);'>Inductee</th><th onclick='sort(\"" + id + "\", this, 2);'>Position</th></tr></thead><tbody id='tb-" + id + "'>");
		for (Object obj : coll) {
			HallOfFameBean bean = (HallOfFameBean) obj;

			// Evaluate bean
			String year = HtmlUtils.writeLink(Year.alias, bean.getYrId(), bean.getYrLabel());
			String name = HtmlUtils.writeLink(Athlete.alias, bean.getPrId(), bean.getPrLastName() + "," + HtmlUtils.SPACE + bean.getPrFirstName());
			String position = (StringUtils.notEmpty(bean.getHfPosition()) ? bean.getHfPosition() : "-");
			
			// Write line
			html.append("<tr><td class='srt'>" + year + "</td><td class='srt'>" + name + "</td><td class='srt'>" + position + "</td></tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertRetiredNumber(Collection<Object> coll, RenderOptions opts) throws Exception {
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		long id = System.currentTimeMillis();
		html.append("<tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>Team</th><th onclick='sort(\"" + id + "\", this, 1);'>Number</th><th onclick='sort(\"" + id + "\", this, 2);'>Name</th><th onclick='sort(\"" + id + "\", this, 3);'>Year</th></tr></thead><tbody id='tb-" + id + "'>");
		for (Object obj : coll) {
			RetiredNumberBean bean = (RetiredNumberBean) obj;

			// Evaluate bean
			String teamImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getTmId(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
			String team = HtmlUtils.writeLink(Team.alias, bean.getTmId(), bean.getTmLabel());
			team = HtmlUtils.writeImgTable(teamImg, team);
			String name = HtmlUtils.writeLink(Athlete.alias, bean.getPrId(), bean.getPrLastName() + "," + HtmlUtils.SPACE + bean.getPrFirstName());
			String number = String.valueOf(bean.getRnNumber());
			String year = (bean.getYrId() != null ? HtmlUtils.writeLink(Year.alias, bean.getYrId(), bean.getYrLabel()) : "-");
			
			// Write line
			html.append("<tr><td class='srt'>" + team + "</td><td class='srt' width='50'>" + number + "</td><td class='srt'>" + name + "</td><td class='srt'>" + year + "</td></tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertTeamStadium(Collection<Object> coll, RenderOptions opts) throws Exception {
		int currentTeam = 0;
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		for (Object obj : coll) {
			TeamStadiumBean bean = (TeamStadiumBean) obj;
			if (!bean.getTmId().equals(currentTeam)) {
				long id = System.currentTimeMillis();
				String teamHeader = "<table><tr><td class='bordered'>" + HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getTmId(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()) + "</td>";
				teamHeader += "<td class='bold'>" + bean.getTmLabel() + "</td></tr></table>";
				html.append(currentTeam > 0 ? "</tbody></table><table class='tsort'>" : "");
				html.append("<thead><tr><th colspan='3'>" + teamHeader + "</th></tr>");
				html.append("<tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>Complex</th><th onclick='sort(\"" + id + "\", this, 1);'>City</th><th onclick='sort(\"" + id + "\", this, 2);'>Years</th></tr></thead><tbody id='tb-" + id + "'>");
				currentTeam = bean.getTmId();
			}

			// Evaluate bean
			String tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCnId(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
			String complex = HtmlUtils.writeLink(Complex.alias, bean.getCxId(), bean.getCxLabel());
			String city = HtmlUtils.SPACE + HtmlUtils.writeLink(City.alias, bean.getCtId(), bean.getCtLabel());
			if (bean.getStId() != null)
				city += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(State.alias, bean.getStId(), bean.getStCode());
			city += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(Country.alias, bean.getCnId(), bean.getCnCode()); 
			city = HtmlUtils.writeImgTable(tmpImg, city);	
			String years = bean.getTsDate1() + "&nbsp;&rarr;&nbsp;" + (!bean.getTsDate2().equals("0") ? bean.getTsDate2() : "Today");

			// Write line
			html.append("<tr><td class='srt'>" + complex + (bean.getTsRenamed() ? "<b>*</b>" : "") + "</td><td class='srt'>" + city + "</td><td class='srt'>" + years + "</td></tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertUSChampionships(Collection<Object> coll, RenderOptions opts) throws Exception {
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		boolean isDate = false;
		boolean isPlace = false;
		for (Object obj : coll) {
			USChampionshipsBean bean = (USChampionshipsBean) obj;
			isDate |= StringUtils.notEmpty(bean.getRsDate2());
			isPlace |= (bean.getCxId() != null);
		}
		long id = System.currentTimeMillis();
		html.append("<thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>Year</th><th onclick='sort(\"" + id + "\", this, 1);'>Champion</th><th onclick='sort(\"" + id + "\", this, 2);'>Score</th><th onclick='sort(\"" + id + "\", this, 3);'>Runner-up</th>");
		html.append((isDate ? "<th onclick='sort(\"" + id + "\", this, 4);'>Date</th>" : "") + (isPlace ? "<th onclick='sort(\"" + id + "\", this, " + (isDate ? 5 : 4) + ");'>Place</th>" : "") + "</tr></thead><tbody id='tb-" + id + "'>");
		for (Object obj : coll) {
			USChampionshipsBean bean = (USChampionshipsBean) obj;

			// Evaluate bean
			String champion = null;
			String runnerup = null;
			if (bean.getRsRank1() != null) {
				String championImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank1(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
				champion = HtmlUtils.writeLink(Team.alias, bean.getRsRank1(), bean.getRsTeam1());
				champion = HtmlUtils.writeImgTable(championImg, champion);	
			}
			if (bean.getRsRank2() != null) {
				String runnerupImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank2(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
				runnerup = HtmlUtils.writeLink(Team.alias, bean.getRsRank2(), bean.getRsTeam2());
				runnerup = HtmlUtils.writeImgTable(runnerupImg, runnerup);
			}
			String year = HtmlUtils.writeLink(Year.alias, bean.getYrId(), bean.getYrLabel());
			String date = (StringUtils.notEmpty(bean.getRsDate1()) ? StringUtils.toTextDate(bean.getRsDate1(), Locale.ENGLISH) + "&nbsp;&rarr;&nbsp;" : "") + (StringUtils.notEmpty(bean.getRsDate2()) ? StringUtils.toTextDate(bean.getRsDate2(), Locale.ENGLISH) : StringUtils.EMPTY);
			String place = StringUtils.EMPTY;
			if (bean.getCxId() != null) {
				String tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCnId(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
				place = HtmlUtils.writeLink(Complex.alias, bean.getCxId(), bean.getCxLabel());
				place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(City.alias, bean.getCtId(), bean.getCtLabel());
				if (bean.getStId() != null)
					place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(State.alias, bean.getStId(), bean.getStCode());
				place += "," + HtmlUtils.SPACE + HtmlUtils.writeLink(Country.alias, bean.getCnId(), bean.getCnCode()); 
				place = HtmlUtils.writeImgTable(tmpImg, place);
			}

			// Write line
			html.append("<tr><td class='srt'>" + year + "</td><td class='srt'>" + (champion != null ? champion : StringUtils.EMPTY) + "</td><td class='srt'>" + (StringUtils.notEmpty(bean.getRsResult()) ? bean.getRsResult() : "") + "</td>");
			html.append("<td class='srt'>" + (runnerup != null ? runnerup : StringUtils.EMPTY) + "</td>" + (isDate ? "<td class='srt'>" + date + "</td>" : "") + (isPlace ? "<td class='srt'>" + place + "</td>" : "") + "</tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertUSRecords(Collection<Object> coll, RenderOptions opts) throws Exception {
		int currentCategory = 0;
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		boolean isRank2 = false;
		boolean isRank3 = false;
		boolean isRank4 = false;
		boolean isRank5 = false;
		boolean isDate1 = false;
		boolean isDate2 = false;
		boolean isDate3 = false;
		boolean isDate4 = false;
		boolean isDate5 = false;
		ArrayList<Object> lstBeans = new ArrayList<Object>(coll);
		int i = 0;
		int colspan_ = 0;
		long id = 0;
		for (Object obj : coll) {
			USRecordsBean bean = (USRecordsBean) obj;
			if (bean.getSeId() != currentCategory) {
				id = System.currentTimeMillis();
				isRank2 = false;
				isRank3 = false;
				isRank4 = false;
				isRank5 = false;
				isDate1 = false;
				isDate2 = false;
				isDate3 = false;
				isDate4 = false;
				isDate5 = false;
				for (int j = i ; j < lstBeans.size() ; j++) {
					USRecordsBean bean_ = (USRecordsBean) lstBeans.get(j);
					if (bean_.getSeId() != bean.getSeId())
						break;
					isRank2 |= (bean_.getRcRank2() != null);
					isRank3 |= (bean_.getRcRank3() != null);
					isRank4 |= (bean_.getRcRank4() != null);
					isRank5 |= (bean_.getRcRank5() != null);
					isDate1 |= (bean_.getRcDate1() != null);
					isDate2 |= (bean_.getRcDate2() != null);
					isDate3 |= (bean_.getRcDate3() != null);
					isDate4 |= (bean_.getRcDate4() != null);
					isDate5 |= (bean_.getRcDate5() != null);
				}
				/**
				long id = System.currentTimeMillis();
				String teamHeader = "<table><tr><td class='bordered'>" + HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getTmId(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled()) + "</td>";
				teamHeader += "<td class='bold'>" + bean.getTmLabel() + "</td></tr></table>";
				html.append(currentTeam > 0 ? "</tbody></table><table class='tsort'>" : "");
				html.append("<thead><tr><th colspan='3'>" + teamHeader + "</th></tr>");
				html.append("<tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>Complex</th><th onclick='sort(\"" + id + "\", this, 1);'>City</th><th onclick='sort(\"" + id + "\", this, 2);'>Years</th></tr></thead><tbody id='tb-" + id + "'>");
				currentTeam = bean.getTmId();
				 */
				colspan_ = 4 + (isRank2 ? 2 : 0) + (isRank3 ? 2 : 0) + (isRank4 ? 2 : 0) + (isRank5 ? 2 : 0) + (isDate1 ? 1 : 0) + (isDate2 ? 1 : 0) + (isDate3 ? 1 : 0) + (isDate4 ? 1 : 0) + (isDate5 ? 1 : 0);
				html.append(currentCategory > 0 ? "</tbody></table><table class='tsort'>" : "");
				html.append("<thead><tr><th colspan=" + colspan_ + ">" + HtmlUtils.writeToggleTitle(bean.getSeLabel().toUpperCase() + " (" + bean.getEvLabel().toUpperCase() + ")") + "</th></tr>");
				html.append("<tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>Scope</th><th onclick='sort(\"" + id + "\", this, 1);'>Record</th><th colspan=" + (isDate1 ? "3" : "2") + " onclick='sort(\"" + id + "\", this, 2);'>Holder</th>" + (isRank2 ? "<th colspan=" + (isDate2 ? "3" : "2") + " onclick='sort(\"" + id + "\", this, 3);'>2nd</th>" : ""));
				html.append((isRank3 ? "<th colspan=" + (isDate3 ? "3" : "2") + " onclick='sort(\"" + id + "\", this, 4);'>3rd</th>" : "") + (isRank4 ? "<th colspan=" + (isDate4 ? "3" : "2") + " onclick='sort(\"" + id + "\", this, 5);'>4th</th>" : "") + (isRank5 ? "<th colspan=" + (isDate5 ? "3" : "2") + " onclick='sort(\"" + id + "\", this, 6);'>5th</th>" : ""));
				html.append("</tr></thead><tbody id='tb-" + id + "'>");
				currentCategory = bean.getSeId();
			}
			i++;

			// Evaluate bean
			boolean isPerson = (bean.getRcNumber2() < 50);
			String holder = null;
			String rank2 = null;
			String rank3 = null;
			String rank4 = null;
			String rank5 = null;
			if (isPerson && bean.getRcPerson1() != null)
				holder = HtmlUtils.writeLink(Athlete.alias, bean.getRcRank1(), bean.getRcPerson1());
			else if (bean.getRcTeam1() != null)
				holder = HtmlUtils.writeLink(Team.alias, bean.getRcRank1(), bean.getRcTeam1());
			if (isPerson && bean.getRcPerson2() != null)
				rank2 = HtmlUtils.writeLink(Athlete.alias, bean.getRcRank2(), bean.getRcPerson2());
			else if (bean.getRcTeam2() != null)
				rank2 = HtmlUtils.writeLink(Team.alias, bean.getRcRank2(), bean.getRcTeam2());
			if (isPerson && bean.getRcPerson3() != null)
				rank3 = HtmlUtils.writeLink(Athlete.alias, bean.getRcRank3(), bean.getRcPerson3());
			else if (bean.getRcTeam3() != null)
				rank3 = HtmlUtils.writeLink(Team.alias, bean.getRcRank3(), bean.getRcTeam3());
			if (isPerson && bean.getRcPerson4() != null)
				rank4 = HtmlUtils.writeLink(Athlete.alias, bean.getRcRank4(), bean.getRcPerson4());
			else if (bean.getRcTeam4() != null)
				rank4 = HtmlUtils.writeLink(Team.alias, bean.getRcRank4(), bean.getRcTeam4());
			if (isPerson && bean.getRcPerson5() != null)
				rank5 = HtmlUtils.writeLink(Athlete.alias, bean.getRcRank5(), bean.getRcPerson5());
			else if (bean.getRcTeam5() != null)
				rank5 = HtmlUtils.writeLink(Team.alias, bean.getRcRank5(), bean.getRcTeam5());

			// Write line
			html.append("<tr><td class='srt'>" + bean.getRcType1() + "&nbsp;-&nbsp;" + bean.getRcType2() + "</td><td class='srt'>" + bean.getRcLabel() + "</td><td class='srt'><b>" + holder + "</b></td><td class='srt'><b>" + bean.getRcRecord1() + "</b></td>");
			html.append(isDate1 ? "<td class='srt'>" + (StringUtils.notEmpty(bean.getRcDate1()) ? bean.getRcDate1() : StringUtils.EMPTY) + "</td>" : "");
			if (isRank2)
				html.append(rank2 != null ? "<td class='srt'>" + rank2 + "</td><td class='srt'>" + bean.getRcRecord2() + "</td>" : "<td colspan=" + (isDate2 ? "3" : "2") + ">" + StringUtils.EMPTY + "</td>")
				.append(rank2 != null && isDate2 ? "<td class='srt'>" + (StringUtils.notEmpty(bean.getRcDate2()) ? bean.getRcDate2() : StringUtils.EMPTY) + "</td>" : "");
			if (isRank3)
				html.append(rank3 != null ? "<td class='srt'>" + rank3 + "</td><td class='srt'>" + bean.getRcRecord3() + "</td>" : "<td colspan=" + (isDate3 ? "3" : "2") + ">" + StringUtils.EMPTY + "</td>")
				.append(rank3 != null && isDate3 ? "<td class='srt'>" + (StringUtils.notEmpty(bean.getRcDate3()) ? bean.getRcDate3() : StringUtils.EMPTY) + "</td>" : "");
			if (isRank4)
				html.append(rank4 != null ? "<td class='srt'>" + rank4 + "</td><td class='srt'>" + bean.getRcRecord4() + "</td>" : "<td colspan=" + (isDate4 ? "3" : "2") + ">" + StringUtils.EMPTY + "</td>")
				.append(rank4 != null && isDate4 ? "<td class='srt'>" + (StringUtils.notEmpty(bean.getRcDate4()) ? bean.getRcDate4() : StringUtils.EMPTY) + "</td>" : "");
			if (isRank5)
				html.append(rank5 != null ? "<td class='srt'>" + rank5 + "</td><td class='srt'>" + bean.getRcRecord5() + "</td>" : "<td colspan=" + (isDate5 ? "3" : "2") + ">" + StringUtils.EMPTY + "</td>")
				.append(rank5 != null && isDate5 ? "<td class='srt'>" + (StringUtils.notEmpty(bean.getRcDate5()) ? bean.getRcDate5() : StringUtils.EMPTY) + "</td>" : "");
			html.append("</tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

	public static StringBuffer convertWinLoss(Collection<Object> coll, RenderOptions opts) throws Exception {
		StringBuffer html = new StringBuffer("<table class='tsort'>");
		long id = System.currentTimeMillis();
		html.append("<tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>Team</th><th onclick='sort(\"" + id + "\", this, 1);'>Type</th><th onclick='sort(\"" + id + "\", this, 2);'>Wins</th><th onclick='sort(\"" + id + "\", this, 3);'>Losses</th><th onclick='sort(\"" + id + "\", this, 4);'>Ties</th><th onclick='sort(\"" + id + "\", this, 5);'>%</th></tr></thead><tbody id='tb-" + id + "'>");
		boolean isTie = false;
		boolean isOtloss = false;
		for (Object obj : coll) {
			WinLossBean bean = (WinLossBean) obj;
			isTie |= (bean.getWlCountTie() != null);
			isOtloss |= (bean.getWlCountOtloss() != null);
		}
		for (Object obj : coll) {
			WinLossBean bean = (WinLossBean) obj;

			// Evaluate bean
			String teamImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getTmId(), ImageUtils.SIZE_SMALL, opts.isPicturesDisabled());
			String team = HtmlUtils.writeLink(Team.alias, bean.getTmId(), bean.getTmLabel());
			team = HtmlUtils.writeImgTable(teamImg, team);
			String stats = "<td class='srt'>" + bean.getWlCountWin() + "</td><td class='srt'>" + bean.getWlCountLoss() + "</td>";
			if (isTie)
				stats += "<td class='srt'>" + (bean.getWlCountTie() != null ? bean.getWlCountTie() : StringUtils.EMPTY) + "</td>";
			if (isOtloss)
				stats += "<td class='srt'>" + (bean.getWlCountOtloss() != null ? bean.getWlCountOtloss() : StringUtils.EMPTY) + "</td>";
			stats += "<td class='srt'>" + bean.getWlAverage() + "</td>";

			// Write line
			html.append("<tr><td class='srt'>" + team + "</td><td class='srt'>" + bean.getWlType() + "</td>" + stats + "</tr>");
		}
		html.append("</tbody></table>");
		return html;
	}

}