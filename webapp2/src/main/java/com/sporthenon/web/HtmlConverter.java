package com.sporthenon.web;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sporthenon.db.DatabaseManager;
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
import com.sporthenon.db.entity.Round;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Contribution;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.entity.meta.ErrorReport;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.db.entity.meta.TreeItem;
import com.sporthenon.db.function.HallOfFameBean;
import com.sporthenon.db.function.LastUpdateBean;
import com.sporthenon.db.function.OlympicMedalsBean;
import com.sporthenon.db.function.OlympicRankingsBean;
import com.sporthenon.db.function.PersonListBean;
import com.sporthenon.db.function.ResultsBean;
import com.sporthenon.db.function.RetiredNumberBean;
import com.sporthenon.db.function.RoundsBean;
import com.sporthenon.db.function.TeamStadiumBean;
import com.sporthenon.db.function.USChampionshipsBean;
import com.sporthenon.db.function.USRecordsBean;
import com.sporthenon.db.function.YearlyStatsBean;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.servlet.USLeaguesServlet;

@SuppressWarnings("unchecked")
public class HtmlConverter {

	private static final Logger log = Logger.getLogger(HtmlConverter.class.getName());
	
	public static final short HEADER_RESULTS = 0;
	public static final short HEADER_CALENDAR = 11;
	public static final short HEADER_OLYMPICS_INDIVIDUAL = 1;
	public static final short HEADER_OLYMPICS_COUNTRY = 2;
	public static final short HEADER_US_LEAGUES_RETNUM = 3;
	public static final short HEADER_US_LEAGUES_TEAMSTADIUM = 4;
	public static final short HEADER_US_LEAGUES_STATS = 5;
	public static final short HEADER_US_LEAGUES_HOF = 6;
	public static final short HEADER_US_LEAGUES_CHAMPIONSHIP = 7;
	public static final short HEADER_US_LEAGUES_RECORD = 8;
	public static final short HEADER_SEARCH = 9;
	public static final short HEADER_REF = 10;
	
	private static final String HIGHLIGHT_COLOR = "transparent";

	public static String getResultsEntity(int type, Integer rank, String str1, String str2, String str3, String country, String year, String plist) {
		String s = null;
		if (rank != null && rank > 0) {
			String expand = "";
			if (type > 10 && plist != null)  {
				expand = "<img alt='+' src='/img/render/plus.png?v=" + ConfigUtils.getProperty("version") + "' style='cursor:pointer;padding:3px 1px;" + (type == 50 ? "margin-top:6px;" : "") + "' onclick=\"togglePlist(this, '" + plist + "');\"/>";
			}
			if (type < 10) {
				s = HtmlUtils.writeLink(Athlete.alias, rank, StringUtils.toFullName(str1, str2, country, true), StringUtils.isRevertName(country, str1 + " " + str2) ? (StringUtils.notEmpty(str1) ? str1 + " " : "") + str2 : (StringUtils.notEmpty(str2) ? str2 + " " : "") + str1);
			}
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

	public static String getResultsEntityRel(Integer relTMId, String relTMCode, String relTMLabel, Integer relCNId, String relCNCode, String relCNLabel, String relCNLabelEN, boolean isRelTM, boolean isRelCN, String year) {
		StringBuffer html = new StringBuffer();
		String tmpImg = null;
		if (isRelCN && relCNId != null && relCNId > 0) {
			tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, relCNId, ImageUtils.SIZE_SMALL, year, null);
			String s = HtmlUtils.writeLink(Country.alias, relCNId, relCNCode, relCNLabelEN);
			html.append("<td>").append(HtmlUtils.writeImgTable(tmpImg, s)).append("</td>");
		}
		else if (isRelCN)
			html.append("<td>" + StringUtils.EMPTY + "</td>");
		if (isRelTM && relTMId != null && relTMId > 0) {
			tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, relTMId, ImageUtils.SIZE_SMALL, year, null);
			String s = HtmlUtils.writeLink(Team.alias, relTMId, relTMLabel, null);
			String s_ = HtmlUtils.writeImgTable(tmpImg, s);
			if (s_.indexOf("<img") == -1)
				s_ = "<table><tr><td>" + s_ + "</td></tr></table>";
			html.append("<td>").append(s_).append("</td>");
		}
		else if (isRelTM)
			html.append("<td>" + StringUtils.EMPTY + "</td>");
		return html.toString();
	}
	
	public static int setTies(List<Integer> tieList, int type, String[] tEntity, String[] tEntityRel, List<List<String>> lTies) {
		int ties = 1;
		if (tieList != null && !tieList.isEmpty()) {
			Integer idx = tieList.get(0) - 1;
			List<String> l = new ArrayList<String>();
			for (int i = 1 ; i < tieList.size() ; i++) {
				if (idx == null) {
					idx = tieList.get(i) - 1;
					l = new ArrayList<String>();
				}
				else {
					if (tieList.get(i) == -1)
						idx = null;
					if (idx != null) {
						StringBuffer sb = new StringBuffer();
						if (tEntity[idx] != null && tEntity[tieList.get(i) - 1] != null) {
							sb.append("<td" + (idx == 0 ? " style='font-weight:bold;'" : "") + ">" + tEntity[tieList.get(i) - 1] + "</td>\r\n");
							tEntity[tieList.get(i) - 1] = null;
						}
						if (tEntityRel != null && tEntityRel[idx] != null && tEntityRel[tieList.get(i) - 1] != null) {
							String s = tEntityRel[tieList.get(i) - 1];
							if (idx == 0)
								s = s.replaceAll("\\<td\\>", "<td style='font-weight:bold;'>");
							sb.append(s);
							tEntityRel[tieList.get(i) - 1] = null;
						}
						l.add(sb.toString());
						if (lTies != null) {
							lTies.set(idx, l);
							if (l.size() + 1 > ties)
								ties = l.size() + 1;
						}
					}
				}
			}
		}
		return ties;
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
			sb.append(HtmlUtils.writeLink(Complex.alias, idcx, cx, cxEN)).append(", ");
		if (idct != null)
			sb.append(HtmlUtils.writeLink(City.alias, idct, ct, ctEN));
		if (idst != null) {
			sb.append(", " + HtmlUtils.writeLink(State.alias, idst, st, stEN));
			img2 = HtmlUtils.writeImage(ImageUtils.INDEX_STATE, idst, ImageUtils.SIZE_SMALL, yr, stEN);
		}
		sb.append((sb.length() > 0 ? ", " : "") + HtmlUtils.writeLink(Country.alias, idcn, cn, cnEN)); 
		String p = sb.toString();
		if (img2 != null)
			p = HtmlUtils.writeImgTable(img2, p);
		p = HtmlUtils.writeImgTable(img1, p);
		return p;
	}
	
	public static StringBuffer getHeader(HttpServletRequest request, short type, Collection<Object> params, Contributor cb, String lang) throws Exception {
		List<Object> lstParams = new ArrayList<Object>(params);
		HashMap<String, String> hHeader = new HashMap<String, String>();
		hHeader.put("info", "#INFO#");
		Integer spid = null;
		String ogimg = null;
		if (type == HEADER_RESULTS) {
			Sport sp = (Sport) DatabaseManager.loadEntity(Sport.class, lstParams.get(0));
			Championship cp = (Championship) DatabaseManager.loadEntity(Championship.class, lstParams.get(1));
			Event ev = (Event) DatabaseManager.loadEntity(Event.class, lstParams.get(2));
			Event se = (Event) DatabaseManager.loadEntity(Event.class, lstParams.get(3));
			Event se2 = (Event) DatabaseManager.loadEntity(Event.class, lstParams.get(4));
			List<String> lstYears = (List<String>) DatabaseManager.loadLabels(Year.class, String.valueOf(lstParams.get(5)), lang);
			hHeader.put("title", sp.getLabel(lang) + " " + StringUtils.SEP1 + " " + cp.getLabel(lang) + (ev != null ? " " + StringUtils.SEP1 + " " + ev.getLabel(lang) + (se != null ? " " + StringUtils.SEP1 + " " + se.getLabel(lang) : "") + (se2 != null ? " " + StringUtils.SEP1 + " " + se2.getLabel(lang) : "") : ""));
			hHeader.put("desc", ResourceUtils.getText("desc.results.page", lang) + " (" + sp.getLabel(lang).toLowerCase() + ")");
			hHeader.put("url", HtmlUtils.writeURL("/results", lstParams.toString(), sp.getLabel() + "/" + cp.getLabel() + (ev != null ? "/" + ev.getLabel() + (se != null ? "/" + se.getLabel() : "") + (se2 != null ? "/" + se2.getLabel() : "") : "")));
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
			ogimg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, sp.getId(), ImageUtils.SIZE_LARGE, null, null);
		}
		else if (type == HEADER_CALENDAR) {
			Sport sp = (Sport) DatabaseManager.loadEntity(Sport.class, lstParams.get(2));
			if (String.valueOf(lstParams.get(0)).length() > 4) {
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				Timestamp dt1 = new Timestamp(df.parse(String.valueOf(lstParams.get(0))).getTime());
				Timestamp dt2 = new Timestamp(df.parse(String.valueOf(lstParams.get(1))).getTime());
				String s1 = StringUtils.toTextDate(dt1, lang, "d MMMM yyyy");
				String s2 = StringUtils.toTextDate(dt2, lang, "d MMMM yyyy");
				boolean is1Date = s1.equals(s2);
				hHeader.put("title", s1 + (!is1Date ? " " + StringUtils.SEP1 + " " + s2 : ""));
				hHeader.put("url", HtmlUtils.writeURL("/calendar", lstParams.toString(), StringUtils.toTextDate(dt1, lang, "yyyy-MM-dd") + (!is1Date ? "/" + StringUtils.toTextDate(dt2, lang, "yyyy-MM-dd") : "")));
				hHeader.put("item1", HtmlUtils.writeDateLink(null, dt1, s1) + (!is1Date ? " " + StringUtils.SEP1 + " " + HtmlUtils.writeDateLink(null, dt2, s2) : ""));
			}
			else {
				String yr = String.valueOf(lstParams.get(0));
				hHeader.put("title", yr + (sp != null ? " " + StringUtils.SEP1 + " " + sp.getLabel(lang) : ""));
				hHeader.put("url", HtmlUtils.writeURL("/calendar", lstParams.toString(), (sp != null ? sp.getLabel() + "/" : "") + yr));
				hHeader.put("item1", HtmlUtils.writeDateLink(null, yr, yr));
			}
			hHeader.put("desc", ResourceUtils.getText("desc.calendar.page", lang));
			hHeader.put("item0", "<table><tr><td style='padding-right:3px;'><img alt='Calendar' src='/img/menu/dbcalendar.png'/></td><td> <a href='/calendar'>" + ResourceUtils.getText("menu.calendar", lang) + "</a></td></tr></table>");
			if (sp != null) {
				hHeader.put("item2", HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, sp.getId(), ImageUtils.SIZE_SMALL, null, sp.getLabel(lang)), HtmlUtils.writeLink(Sport.alias, sp.getId(), sp.getLabel(lang), sp.getLabel())));
				ogimg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, sp.getId(), ImageUtils.SIZE_LARGE, null, null);
			}
		}
		else if (type == HEADER_OLYMPICS_INDIVIDUAL) {
			String olId = String.valueOf(lstParams.get(0));
			String sql = "SELECT YR.label || ' ' || CT.label" + ResourceUtils.getLocaleParam(lang)
					+ " FROM olympics OL "
					+ " JOIN year YR ON YR.id = OL.id_year "
					+ " JOIN city CT ON CT.id = OL.id_city "
					+ " WHERE OL.id IN (" + olId + ")";
			List<String> lstOlympics = (List<String>) DatabaseManager.executeSelect(sql, String.class);
			Sport sp = (Sport) DatabaseManager.loadEntity(Sport.class, lstParams.get(1));
			Event ev = (Event) DatabaseManager.loadEntity(Event.class, lstParams.get(2));
			List<String> lstEvents = (List<String>) DatabaseManager.loadLabels(Event.class, String.valueOf(lstParams.get(2)), lang);
			List<String> lstSubevents = (List<String>) DatabaseManager.loadLabels(Event.class, String.valueOf(lstParams.get(3)), lang);
			hHeader.put("title", ResourceUtils.getText("entity.OL", lang) + " " + StringUtils.SEP1 + " " + sp.getLabel(lang) + (ev != null ? " " + StringUtils.SEP1 + " " + ev.getLabel(lang) : ""));
			hHeader.put("desc", ResourceUtils.getText("desc.olympics1.page", lang));
			hHeader.put("url", HtmlUtils.writeURL("/olympics", "ind, " + lstParams.toString(), "results/" + sp.getLabel() + (lstEvents != null && lstEvents.size() == 1 ? "/" + ev.getLabel() : "")));
			hHeader.put("item1", ResourceUtils.getText("event.results", lang));
			hHeader.put("item2", (lstOlympics.isEmpty() ? ResourceUtils.getText("all.olympic.games", lang) : (lstOlympics.size() == 1 ? HtmlUtils.writeLink(Olympics.alias, StringUtils.toInt(olId), lstOlympics.get(0), null) : HtmlUtils.writeTip(Olympics.alias, lstOlympics) + " " + ResourceUtils.getText("x.olympic.games", lang))));
			hHeader.put("item3", HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, sp.getId(), ImageUtils.SIZE_SMALL, null, sp.getLabel(lang)), HtmlUtils.writeLink(Sport.alias, sp.getId(), sp.getLabel(lang), sp.getLabel())));
			hHeader.put("item4", (lstEvents.isEmpty() ? ResourceUtils.getText("all.events", lang) : (lstEvents.size() == 1 ? lstEvents.get(0) : HtmlUtils.writeTip(Event.alias, lstEvents) + " " + ResourceUtils.getText("x.events", lang))));
			if (!lstSubevents.isEmpty())
				hHeader.put("item5", (lstSubevents.isEmpty() ? ResourceUtils.getText("all.events", lang) : (lstSubevents.size() == 1 ? lstSubevents.get(0) : HtmlUtils.writeTip(Event.alias, lstSubevents) + " " + ResourceUtils.getText("x.events", lang))));
			ogimg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, sp.getId(), ImageUtils.SIZE_LARGE, null, null);
		}
		else if (type == HEADER_OLYMPICS_COUNTRY) {
			String olId = String.valueOf(lstParams.get(0));
			String cnId = String.valueOf(lstParams.get(1));
			String sql = "SELECT YR.label || ' ' || CT.label" + ResourceUtils.getLocaleParam(lang) 
					+ " FROM olympics OL "
					+ " JOIN year YR ON YR.id = OL.id_year "
					+ " JOIN city CT ON CT.id = OL.id_city "
					+ " WHERE OL.id IN (" + olId + ")";
			List<String> lstOlympics = (List<String>) DatabaseManager.executeSelect(sql, String.class);
			List<String> lstCountries = (List<String>) DatabaseManager.loadLabels(Country.class, cnId, lang);
			hHeader.put("title", ResourceUtils.getText("entity.OL", lang) + " " + StringUtils.SEP1 + " " + ResourceUtils.getText("medals.tables", lang));
			hHeader.put("desc", ResourceUtils.getText("desc.olympics2.page", lang));
			hHeader.put("url", HtmlUtils.writeURL("/olympics", "cnt, " + lstParams.toString(), "tables"));
			hHeader.put("item1", ResourceUtils.getText("medals.tables", lang));
			hHeader.put("item2", (lstOlympics.isEmpty() ? ResourceUtils.getText("all.olympic.games", lang) : (lstOlympics.size() == 1 ? HtmlUtils.writeLink(Olympics.alias, StringUtils.toInt(olId), lstOlympics.get(0), null) : HtmlUtils.writeTip(Olympics.alias, lstOlympics) + " " + ResourceUtils.getText("x.olympic.games", lang))));
			hHeader.put("item3", (lstCountries.isEmpty() ? ResourceUtils.getText("all.countries", lang) : (lstCountries.size() == 1 ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, StringUtils.toInt(cnId), ImageUtils.SIZE_SMALL, null, lstCountries.get(0)), HtmlUtils.writeLink(Country.alias, StringUtils.toInt(cnId), lstCountries.get(0), null)) : HtmlUtils.writeTip(Country.alias, lstCountries) + " " + ResourceUtils.getText("x.countries", lang))));
		}
		else if (type == HEADER_US_LEAGUES_RETNUM || type == HEADER_US_LEAGUES_CHAMPIONSHIP || type == HEADER_US_LEAGUES_HOF || type == HEADER_US_LEAGUES_RECORD || type == HEADER_US_LEAGUES_TEAMSTADIUM || type == HEADER_US_LEAGUES_STATS) {
			String league = String.valueOf(lstParams.get(0));
			String leagueLabel = (league.equals("1") ? "NFL" : (league.equals("2") ? "NBA" : (league.equals("3") ? "NHL" : "MLB")));
			String typeLabel = (type == HEADER_US_LEAGUES_RETNUM ? ResourceUtils.getText("retired.numbers", "en") : (type == HEADER_US_LEAGUES_CHAMPIONSHIP ? ResourceUtils.getText("championships", "en") : (type == HEADER_US_LEAGUES_HOF ? ResourceUtils.getText("hall.fame", "en") : (type == HEADER_US_LEAGUES_RECORD ? ResourceUtils.getText("records", "en") : (type == HEADER_US_LEAGUES_TEAMSTADIUM ? ResourceUtils.getText("team.stadiums", "en") : ResourceUtils.getText("yearly.stats", "en"))))));
			Integer cpId = (league.equals("1") ? 51 : (league.equals("2") ? 54 : (league.equals("3") ? 55 : 56)));
			String title = leagueLabel + " " + StringUtils.SEP1 + " " + typeLabel;
			hHeader.put("desc", leagueLabel + " : " +  ResourceUtils.getText("desc.usleagues.page", lang));
			hHeader.put("item1", HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, cpId, ImageUtils.SIZE_SMALL, null, leagueLabel), HtmlUtils.writeLink(Championship.alias, cpId, leagueLabel, null)));
			hHeader.put("item2", typeLabel);
			if (type == HEADER_US_LEAGUES_RETNUM) {
				List<String> lstTeams = (List<String>) DatabaseManager.loadLabels(Team.class, String.valueOf(lstParams.get(1)), "en");
				hHeader.put("item3", (lstTeams.isEmpty() ? ResourceUtils.getText("all.teams", "en") : (lstTeams.size() == 1 ? lstTeams.get(0) : HtmlUtils.writeTip(Team.alias, lstTeams) + " " + ResourceUtils.getText("x.teams", "en"))));
				title += (lstTeams.size() == 1 ? " " + StringUtils.SEP1 + " " + lstTeams.get(0) : "");
				hHeader.put("url", HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_RETNUM + "-" + lstParams.toString(), title));
			}
			else if (type == HEADER_US_LEAGUES_TEAMSTADIUM) {
				List<String> lstTeams = (List<String>) DatabaseManager.loadLabels(Team.class, String.valueOf(lstParams.get(1)), "en");
				hHeader.put("item3", (lstTeams.isEmpty() ? ResourceUtils.getText("all.teams", "en") : (lstTeams.size() == 1 ? lstTeams.get(0) : HtmlUtils.writeTip(Team.alias, lstTeams) + " " + ResourceUtils.getText("x.teams", "en"))));
				title += (lstTeams.size() == 1 ? " " + StringUtils.SEP1 + " " + lstTeams.get(0) : "");
				hHeader.put("url", HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_TEAMSTADIUM + "-" + lstParams.toString(), title));
			}
			else if (type == HEADER_US_LEAGUES_STATS) {
				List<String> lstCategories = (List<String>) DatabaseManager.loadLabels(Event.class, String.valueOf(lstParams.get(2)), "en");
				List<String> lstYears = (List<String>) DatabaseManager.loadLabels(Year.class, String.valueOf(lstParams.get(1)), "en");
				hHeader.put("item3", (lstCategories.isEmpty() ? ResourceUtils.getText("all.categories", "en") : (lstCategories.size() == 1 ? lstCategories.get(0) : HtmlUtils.writeTip(Year.alias, lstCategories) + " " + ResourceUtils.getText("x.categories", "en"))));
				hHeader.put("item4", (lstYears.isEmpty() ? ResourceUtils.getText("all.years", "en") : (lstYears.size() == 1 ? lstYears.get(0) : HtmlUtils.writeTip(Year.alias, lstYears) + " " + ResourceUtils.getText("x.years", "en"))));
				title += (lstCategories.size() == 1 ? " " + StringUtils.SEP1 + " " + lstCategories.get(0) : "");
				title += (lstYears.size() == 1 ? " " + StringUtils.SEP1 + " " + lstYears.get(0) : "");
				hHeader.put("url", HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_STATS + "-" + lstParams.toString(), title));
			}
			else if (type == HEADER_US_LEAGUES_HOF) {
				List<String> lstYears = (List<String>) DatabaseManager.loadLabels(Year.class, String.valueOf(lstParams.get(1)), "en");
				hHeader.put("item3", (lstYears.isEmpty() ? ResourceUtils.getText("all.years", "en") : (lstYears.size() == 1 ? lstYears.get(0) : HtmlUtils.writeTip(Year.alias, lstYears) + " " + ResourceUtils.getText("x.years", "en"))));
				title += (lstYears.size() == 1 ? " " + StringUtils.SEP1 + " " + lstYears.get(0) : "");
				hHeader.put("url", HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_HOF + "-" + lstParams.toString(), title));
			}
			else if (type == HEADER_US_LEAGUES_CHAMPIONSHIP) {
				List<String> lstYears = (List<String>) DatabaseManager.loadLabels(Year.class, String.valueOf(lstParams.get(2)), "en");
				hHeader.put("item3", (lstYears.isEmpty() ? ResourceUtils.getText("all.years", "en") : (lstYears.size() == 1 ? lstYears.get(0) : HtmlUtils.writeTip(Year.alias, lstYears) + " " + ResourceUtils.getText("x.years", "en"))));
				title += (lstYears.size() == 1 ? " " + StringUtils.SEP1 + " " + lstYears.get(0) : "");
				hHeader.put("url", HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_CHAMPIONSHIP + "-" + lstParams.toString(), title));
			}
			else if (type == HEADER_US_LEAGUES_RECORD) {
				List<String> lstCategories = (List<String>) DatabaseManager.loadLabels(Event.class, String.valueOf(lstParams.get(3)), "en");
				if (lstCategories.size() == 1)
					hHeader.put("title", title + " " + StringUtils.SEP1 + " " + lstCategories.get(0));
				String scope = String.valueOf(lstParams.get(4));
				String scope2 = String.valueOf(lstParams.get(5));
				hHeader.put("item3", (lstCategories.isEmpty() ? ResourceUtils.getText("all.categories", "en") : (lstCategories.size() == 1 ? lstCategories.get(0) : HtmlUtils.writeTip(Event.alias, lstCategories) + " " + ResourceUtils.getText("x.categories", "en"))));
				hHeader.put("item4", (scope.equalsIgnoreCase("it") ? ResourceUtils.getText("all.scopes", "en") : USLeaguesServlet.HTYPE1.get(scope).replaceAll("'", "")));
				hHeader.put("item5", (scope2.equalsIgnoreCase("-") ? ResourceUtils.getText("all.scopes", "en") : USLeaguesServlet.HTYPE2.get(scope2).replaceAll("'", "")));
				title += (lstCategories.size() == 1 ? " " + StringUtils.SEP1 + " " + lstCategories.get(0) : "");
				hHeader.put("url", HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_RECORD + "-" + lstParams.toString(), title));
			}
			hHeader.put("title", title);
			ogimg = HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, cpId, ImageUtils.SIZE_LARGE, null, null);
		}
		else if (type == HEADER_SEARCH) {
			String pattern = String.valueOf(lstParams.get(0)).replaceAll("^\\^|\\.\\*", "");
			hHeader.put("title", ResourceUtils.getText("menu.search.2", lang) + " (" + pattern + ")");
			hHeader.put("desc", ResourceUtils.getText("desc.search", lang));
			hHeader.put("url", HtmlUtils.writeURL("/search", null, null) + "?p=" + pattern + "&amp;max=" + lstParams.get(2));
			hHeader.put("item0", "<table><tr><td style='padding-right:3px;'><img alt='Advanced Search' src='/img/menu/dbsearch.png'/></td><td> <a href='/search'>" + ResourceUtils.getText("menu.search", lang).toUpperCase() + "</a></td></tr></table>");
			hHeader.put("item1", ResourceUtils.getText("search.results", lang) + " : <b>" + pattern + "</b>");
		}
		else if (type == HEADER_REF) {
			String entity = String.valueOf(lstParams.get(0));
			hHeader.put("title", String.valueOf(lstParams.get(6)));
			hHeader.put("desc", ResourceUtils.getText("details", lang) + " " + ResourceUtils.getText("entity." + entity + ".1", lang) + " : " + hHeader.get("title"));
			hHeader.put("item0", "<table><tr><td style='padding-right:3px;'><img alt='Ref' src='/img/menu/dbref.png'/></td><td> " + ResourceUtils.getText("entity." + entity, lang) + "</td></tr></table>");
			if (entity.equals(Result.alias)) {
				Result r = (Result) DatabaseManager.loadEntity(Result.class, lstParams.get(1));
				String path = r.getSport().getLabel() + "/" + r.getChampionship().getLabel() + "/" + r.getEvent().getLabel() + (r.getSubevent() != null ? "/" + r.getSubevent().getLabel() : "") + (r.getSubevent2() != null ? "/" + r.getSubevent2().getLabel() : "");
				hHeader.put("item1", HtmlUtils.writeLink(Year.alias, r.getYear().getId(), r.getYear().getLabel(), null));
				hHeader.put("item2", "<a href='" + HtmlUtils.writeURL("/results", r.getSport().getId() + "-" + r.getChampionship().getId() + "-" + r.getEvent().getId() + (r.getSubevent() != null ? "-" + r.getSubevent().getId() : "") + (r.getSubevent2() != null ? "-" + r.getSubevent2().getId() : ""), path) + "'>" + (r.getSport().getLabel(lang) + " " + StringUtils.SEP1 + " " + r.getChampionship().getLabel(lang) + " " + StringUtils.SEP1 + " " + r.getEvent().getLabel(lang) + (r.getSubevent() != null ? " " + StringUtils.SEP1 + " " + r.getSubevent().getLabel(lang) : "") + (r.getSubevent2() != null ? " " + StringUtils.SEP1 + " " + r.getSubevent2().getLabel(lang) : "")) + "</a>");
				hHeader.put("url", HtmlUtils.writeURL("/result", entity + "-" + lstParams.get(1) + "-1", r.getYear().getLabel() + "/" + path));
				spid = r.getSport().getId();
				ogimg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, spid, ImageUtils.SIZE_LARGE, null, null);
			}
			else if (entity.equals(Sport.alias))
				ogimg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, Integer.parseInt(String.valueOf(lstParams.get(1))), ImageUtils.SIZE_LARGE, null, null);
			else
				hHeader.put("item1", hHeader.get("title"));
		}
		if (ogimg != null) {
			ogimg = ogimg.replaceFirst(".*\\ssrc=\\'", "").replaceFirst("\\'\\/\\>$", "");
			request.setAttribute("ogimg", "http:" + ogimg);
		}
		// Reported errors
		try {
			String url = String.valueOf(request.getAttribute("url") != null ? request.getAttribute("url") : hHeader.get("url"));
			url = url.replaceAll("\\'", "''").substring(url.lastIndexOf("/"));
			List<ErrorReport> list = (List<ErrorReport>) DatabaseManager.executeSelect("SELECT * FROM _error_report WHERE url LIKE '%" + url + "' ORDER BY id", ErrorReport.class);
			if (list != null && list.size() > 0) {
				StringBuffer sb = new StringBuffer();
				sb.append("<a class='ertip' href='#ereport'>" + list.size() + " " + ResourceUtils.getText("error" + (list.size() > 1 ? "s" : ""), lang) + " " + ResourceUtils.getText("on.this.page", lang) + "</a>");
				sb.append("<div id='ereport' class='rendertip' style='display:none;'>");
				for (ErrorReport er : list) {
					sb.append(StringUtils.SEP1 + " " + ResourceUtils.getText("reported.on", lang) + " <b>" + StringUtils.toTextDate(er.getDate(), lang, "dd/MM/yyyy, HH:mm") + "</b> :<br/>" + er.getText().replaceAll("\r\n|\n", "<br/>")).append("<br/>").append(list.size() > 1 ? "<br/>" : "");
				}
				sb.append("</div>");
				hHeader.put("errors", sb.toString());
			}
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
		finally {
			request.removeAttribute("url");
		}
		return HtmlUtils.writeHeader(hHeader, spid, cb, lang);
	}

	private static Map<Integer, List<StringBuffer>> getPersonLists(String results, String lang) throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(results);
		List<PersonListBean> list = (List<PersonListBean>) DatabaseManager.callFunction("get_person_list", params, PersonListBean.class);
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
				boolean isCpt = false;
				String index = "";
				if (StringUtils.notEmpty(bean.getPlIndex())) {
					isCpt = bean.getPlIndex().contains("(c)");
					index = bean.getPlIndex().replaceAll("\\(c\\)", "");
					String cfg = ConfigUtils.getValue("plist_" + index);
					if (StringUtils.notEmpty(cfg))
						index = cfg.split("\\|")[lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? 0 : 1];
					index = index + " ";
				}
				String s = index + HtmlUtils.writeLink(Athlete.alias, bean.getPrId(), StringUtils.toFullName(bean.getPrLastName(), bean.getPrFirstName(), bean.getPrCountryCode(), true), (StringUtils.notEmpty(bean.getPrFirstName()) ? bean.getPrFirstName() + " " : "") + bean.getPrLastName());
				if (isCpt)
					s += " (" + ResourceUtils.getText("captain", lang) + ")";
				sb.append("<tr><th><img alt='L' src='/img/component/treeview/join" + (!isLast ? "bottom" : "") + ".gif'/></th><td>" + s + "</td></tr>");
				i++;
			}
		}
		return m;
	}
	
	private static StringBuffer getWinRecords(String results, String lang) throws Exception {
		final int MAX_WINREC = 10;
		List<Object> params = new ArrayList<Object>();
		params.add(results);
		params.add(ResourceUtils.getLocaleParam(lang));
		Collection<RefItem> list = (Collection<RefItem>) DatabaseManager.callFunctionSelect("win_records", params, RefItem.class, "count1 DESC, label");
		StringBuffer html = new StringBuffer();
		html.append("<table id='winrec'><thead><tr><th colspan='3'>" + ResourceUtils.getText("win.records", lang) + "</th></tr></thead><tbody class='tby'>");
		int max = -1;
		int i = 0;
		for (RefItem item : list) {
			max = (max == -1 ? item.getCount1() : max);
			String str = item.getLabel();
			String strEN = item.getLabelEN();
			if (item.getIdRel1() < 10) {
				String[] t = str.split("\\,\\s", -1);
				str = StringUtils.toFullName(t[0], t[1], item.getLabelRel1(), true);
				strEN = (StringUtils.isRevertName(item.getLabelRel1(), t[0] + " " + t[1]) ? (StringUtils.notEmpty(t[0]) ? t[0] + " " : "") + t[1] : (StringUtils.notEmpty(t[1]) ? t[1] + " " : "") + t[0]);
			}
			html.append("<tr" + (++i > MAX_WINREC ? " class='hidden'" : "") + "><td class='caption'>" + HtmlUtils.writeLink(item.getIdRel1() < 10 ? Athlete.alias : (item.getIdRel1() == 50 ? Team.alias : Country.alias), item.getIdItem(), str, strEN) + "</td>");
			html.append("<td><table><tr><td class='bar1'></td>");
			html.append("<td class='bar2' style='width:" + (int)((item.getCount1() * 100) / max) + "px;'></td>");
			html.append("<td class='bar3'></td></tr></table></td>");
			html.append("<td class='count'>" + item.getCount1() + "</td></tr>");
		}
		if (list.size() > MAX_WINREC) {
			html.append("<tr id='winrecmore'><td colspan='3' style='padding-bottom:5px;'><a href='javascript:$$(\"#winrec tr\").each(function(el){$(el).removeClassName(\"hidden\");});$(\"winrecmore\").hide();' class='otherimglink' style='float:right;'>" + ResourceUtils.getText("more", lang) + "</a></td></tr>");
		}
		return html.append("</tbody></table>");
	}

	public static StringBuffer getRecordInfo(HttpServletRequest request, String type, int id, String lang) throws Exception {
		LinkedHashMap<String, String> hInfo = new LinkedHashMap<String, String>();
		hInfo.put("info", "#INFO#");
		Timestamp lastUpdate = null;
		Integer ref = 0;
		if (type.equals(Athlete.alias)) {
			List<Athlete> lAthlete = new ArrayList<Athlete>();
			Athlete e = (Athlete) DatabaseManager.loadEntity(Athlete.class, id);
			if (e == null)
				return null;
			if (e.getLink() != null && e.getLink() >= 0) {
				Athlete e_ = (Athlete) DatabaseManager.loadEntity(Athlete.class, e.getLink());
				String wId = "(-1" + (e_ != null && e_.getId() > 0 ? "," + e_.getId() : "") + (e_ != null && e_.getLink() > 0 ? "," + e_.getLink() : "") + (e.getId() > 0 ? "," + e.getId() : "") + (e.getLink() > 0 ? "," + e.getLink() : "") + ")";
				lAthlete.addAll((List<Athlete>)DatabaseManager.executeSelect(Athlete.query + " WHERE T.id IN " + wId + " OR T.link IN " + wId + " ORDER BY T.id", Athlete.class));
				id = lAthlete.get(0).getId();
			}
			else
				lAthlete.add(e);
			String titlename = "";
			Vector<String> vNm = new Vector<String>();
			Vector<Integer> vTm = new Vector<Integer>();
			Vector<Integer> vCn = new Vector<Integer>();
			Vector<Integer> vSp = new Vector<Integer>();
			StringBuffer sbNm = new StringBuffer();
			StringBuffer sbTm = new StringBuffer();
			StringBuffer sbCn = new StringBuffer();
			StringBuffer sbSp = new StringBuffer();
			ArrayList<Integer> lId = new ArrayList<Integer>();
			for (Athlete a : lAthlete) {
				lId.add(a.getId());
				ref += (a.getRef() != null ? a.getRef() : 0);
				if (StringUtils.notEmpty(a.getLastName())) {
					String fullName = null;
					if (StringUtils.isRevertName(a.getCountry() != null ? a.getCountry().getCode() : "", a.getFirstName() + " " + a.getLastName()))
						fullName = a.getLastName().toUpperCase() + (StringUtils.notEmpty(a.getFirstName()) ? " " + a.getFirstName() : "");
					else
						fullName = (StringUtils.notEmpty(a.getFirstName()) ? a.getFirstName() + " " : "") + a.getLastName().toUpperCase();
					if (a.getId() == id)
						titlename = fullName;
					else if (!vNm.contains(fullName) && !fullName.equalsIgnoreCase(titlename)) {
						sbNm.append(sbNm.toString().length() > 0 ? ", " : "").append(fullName);
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

			if (StringUtils.isRevertName(e.getCountry() != null ? e.getCountry().getCode() : "", e.getFirstName() + " " + e.getLastName()))
				hInfo.put("title", e.getLastName() + (StringUtils.notEmpty(e.getFirstName()) ? " " + e.getFirstName() : ""));
			else
				hInfo.put("title", (StringUtils.notEmpty(e.getFirstName()) ? e.getFirstName() + " " : "") + e.getLastName());
			hInfo.put("titlename", titlename.toUpperCase());
			if (StringUtils.notEmpty(sbNm.toString()))
				hInfo.put("titlename2", sbNm.toString());
			if (StringUtils.notEmpty(cn))
				hInfo.put(vCn.size() > 1 ? "countries" : "country", cn);
			if (StringUtils.notEmpty(sp))
				hInfo.put(vSp.size() > 1 ? "sports" : "sport", sp);
			if (StringUtils.notEmpty(tm))
				hInfo.put(vTm.size() > 1 ? "teams" : "team", tm);
			// Record
			List<Object> params = new ArrayList<Object>();
			params.add("PR");
			params.add(e.getSport().getId());
			params.add(lId.toString().replaceAll("\\[|\\]", "").replaceAll("\\,\\s", "-"));
			Collection<RefItem> cRecord = (Collection<RefItem>) DatabaseManager.callFunctionSelect("get_medal_count", params, RefItem.class);
			if (!cRecord.isEmpty())
				hInfo.put("record", HtmlUtils.writeRecordItems(cRecord, lang));
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Championship.alias)) {
			Championship e = (Championship) DatabaseManager.loadEntity(Championship.class, id);
			if (e == null)
				return null;
			hInfo.put("title", e.getLabel(lang));
			hInfo.put("titleEN", e.getLabel());
			hInfo.put("titlename", "<b>" + e.getLabel(lang).toUpperCase() + "</b>");
			hInfo.put("logo", HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, e.getId(), ImageUtils.SIZE_LARGE, null, null));
			hInfo.put("extlinks", HtmlUtils.writeExternalLinks(type, id, lang));
			ref = e.getRef();
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(City.alias)) {
			List<City> lCity = new ArrayList<City>();
			City e = (City) DatabaseManager.loadEntity(City.class, id);
			if (e == null)
				return null;
			if (e.getLink() != null && e.getLink() >= 0) {
				City e_ = (City) DatabaseManager.loadEntity(City.class, e.getLink());
				String wId = "(-1" + (e_ != null && e_.getId() > 0 ? "," + e_.getId() : "") + (e_ != null && e_.getLink() > 0 ? "," + e_.getLink() : "") + (e.getId() > 0 ? "," + e.getId() : "") + (e.getLink() > 0 ? "," + e.getLink() : "") + ")";
				lCity.addAll((List<City>)DatabaseManager.executeSelect(City.query + " WHERE T.id IN " + wId + " OR T.link IN " + wId + " ORDER BY T.id", City.class));
				id = lCity.get(0).getId();
			}
			else
				lCity.add(e);
			String titlename = "";
			Vector<String> vNm = new Vector<String>();
			Vector<Integer> vSt = new Vector<Integer>();
			Vector<Integer> vCn = new Vector<Integer>();
			StringBuffer sbNm = new StringBuffer();
			StringBuffer sbSt = new StringBuffer();
			StringBuffer sbCn = new StringBuffer();
			ArrayList<Integer> lId = new ArrayList<Integer>();
			for (City c : lCity) {
				lId.add(c.getId());
				ref += (c.getRef() != null ? c.getRef() : 0);
				if (c.getId() == id)
					titlename = c.getLabel(lang);
				else if (!vNm.contains(c.getLabel(lang))) {
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
			hInfo.put("titlename", titlename.toUpperCase());
			if (StringUtils.notEmpty(sbNm.toString()))
				hInfo.put("titlename2", sbNm.toString());
			if (StringUtils.notEmpty(st))
				hInfo.put("state", st);
			if (StringUtils.notEmpty(cn))
				hInfo.put("country", cn);
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Complex.alias)) {
			List<Complex> lComplex = new ArrayList<Complex>();
			Complex e = (Complex) DatabaseManager.loadEntity(Complex.class, id);
			if (e == null)
				return null;
			if (e.getLink() != null && e.getLink() >= 0) {
				Complex e_ = (Complex) DatabaseManager.loadEntity(Complex.class, e.getLink());
				String wId = "(-1" + (e_ != null && e_.getId() > 0 ? "," + e_.getId() : "") + (e_ != null && e_.getLink() > 0 ? "," + e_.getLink() : "") + (e.getId() > 0 ? "," + e.getId() : "") + (e.getLink() > 0 ? "," + e.getLink() : "") + ")";
				lComplex.addAll((List<Complex>)DatabaseManager.executeSelect(Complex.query + " WHERE T.id IN " + wId + " OR T.link IN " + wId + " ORDER BY T.id", Complex.class));
				id = lComplex.get(0).getId();
			}
			else
				lComplex.add(e);
			String titlename = "";
			Vector<String> vNm = new Vector<String>();
			Vector<String> vCt = new Vector<String>();
			Vector<Integer> vSt = new Vector<Integer>();
			Vector<Integer> vCn = new Vector<Integer>();
			StringBuffer sbNm = new StringBuffer();
			StringBuffer sbCt = new StringBuffer();
			StringBuffer sbSt = new StringBuffer();
			StringBuffer sbCn = new StringBuffer();
			ArrayList<Integer> lId = new ArrayList<Integer>();
			for (Complex c : lComplex) {
				lId.add(c.getId());
				ref += (c.getRef() != null ? c.getRef() : 0);
				if (c.getId() == id)
					titlename = c.getLabel();
				else if (!vNm.contains(c.getLabel())) {
					sbNm.append(sbNm.toString().length() > 0 ? "<br/>" : "").append(c.getLabel().toUpperCase());
					vNm.add(c.getLabel());
				}
				if (!vCt.contains(c.getCity().getLabel(lang))) {
					sbCt.append(sbCt.toString().length() > 0 ? "<br/>" : "").append(c.getCity().getLabel(lang));
					vCt.add(c.getLabel());
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
			
			hInfo.put("title", e.getLabel());
			hInfo.put("titleEN", e.getLabel());
			hInfo.put("titlename", titlename.toUpperCase());
			if (StringUtils.notEmpty(sbNm.toString()))
				hInfo.put("titlename2", sbNm.toString());
			if (StringUtils.notEmpty(ct))
				hInfo.put("city", ct);
			if (StringUtils.notEmpty(st))
				hInfo.put("state", st);
			if (StringUtils.notEmpty(cn))
				hInfo.put("country", cn);
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Contributor.alias)) {
			Contributor e = (Contributor) DatabaseManager.loadEntity(Contributor.class, id);
			if (e == null)
				return null;
			hInfo.put("title", e.getLogin());
			hInfo.put("ID", "<b>" + e.getLogin().toUpperCase() + "</b>");
			hInfo.put("name", e.getPublicName());
			hInfo.put("admin", ResourceUtils.getText(e.getAdmin() != null && e.getAdmin() ? "yes" : "no", lang));
			if (StringUtils.notEmpty(e.getSports())) {
				StringBuffer sb = new StringBuffer();
				String sql = "SELECT * FROM sport WHERE id IN (" + e.getSports() + ") ORDER BY label" + ResourceUtils.getLocaleParam(lang);
				for (Sport sp : (List<Sport>) DatabaseManager.executeSelect(sql, Sport.class))
					sb.append(HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, sp.getId(), ImageUtils.SIZE_SMALL, null, null), sp.getLabel(lang)));
				hInfo.put("entity.SP", sb.toString());	
			}
		}
		else if (type.equals(Country.alias)) {
			Country e = (Country) DatabaseManager.loadEntity(Country.class, id);
			if (e == null)
				return null;
			String currentLogo = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, e.getId(), ImageUtils.SIZE_LARGE, null, null);
			Collection<String> lAllLogos = ImageUtils.getImages(ImageUtils.INDEX_COUNTRY, e.getId(), ImageUtils.SIZE_LARGE);
			hInfo.put("title", e.getLabel(lang));
			hInfo.put("titleEN", e.getLabel());
			hInfo.put("titlename", "<b>" + e.getLabel(lang).toUpperCase() + "</b>");
			hInfo.put("flag", currentLogo);
			hInfo.put("code", e.getCode());
			StringBuffer sbOtherFlags = new StringBuffer();
			if (lAllLogos != null && lAllLogos.size() > 1) {
				int nof = 0;
				for (String s : lAllLogos)
					if (s.matches(".*\\d{4}\\-\\d{4}(\\_\\d+|)\\.png$")) {
						String s_ = s.substring(s.indexOf("_") + 1, s.lastIndexOf(".")).replaceAll("\\_\\d+$", "");
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
			List<Object> params = new ArrayList<Object>();
			params.add("CN");
			params.add(0);
			params.add(String.valueOf(e.getId()));
			Collection<RefItem> cRecord = (Collection<RefItem>) DatabaseManager.callFunctionSelect("get_medal_count", params, RefItem.class);
			if (!cRecord.isEmpty())
				hInfo.put("record", HtmlUtils.writeRecordItems(cRecord, lang));
			ref = e.getRef();
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.matches("\\d{8}")) {
			hInfo.put("title", type);
			hInfo.put("name", type);
		}
		else if (type.equals(Event.alias)) {
			Event e = (Event) DatabaseManager.loadEntity(Event.class, id);
			if (e == null)
				return null;
			hInfo.put("title", e.getLabel(lang));
			hInfo.put("titleEN", e.getLabel());
			hInfo.put("titlename", "<b>" + e.getLabel(lang).toUpperCase() + "</b>");
			hInfo.put("logo", HtmlUtils.writeImage(ImageUtils.INDEX_EVENT, e.getId(), ImageUtils.SIZE_LARGE, null, null));
			hInfo.put("extlinks", HtmlUtils.writeExternalLinks(type, id, lang));
			ref = e.getRef();
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Olympics.alias)) {
			Olympics e = (Olympics) DatabaseManager.loadEntity(Olympics.class, id);
			if (e == null)
				return null;
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
			hInfo.put("title", e.getCity().getLabel(lang) + " " + e.getYear().getLabel());
			hInfo.put("titleEN", e.getCity().getLabel() + " " + e.getYear().getLabel());
			hInfo.put("titlename", HtmlUtils.writeLink(City.alias, e.getCity().getId(), e.getCity().getLabel(lang).toUpperCase(), e.getCity().getLabel()) + " " + HtmlUtils.writeLink(Year.alias, e.getYear().getId(), e.getYear().getLabel(), null));
			hInfo.put("logo", HtmlUtils.writeImage(ImageUtils.INDEX_OLYMPICS, e.getId(), ImageUtils.SIZE_LARGE, null, null));
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
			Result r = (Result) DatabaseManager.loadEntity(Result.class, id);
			if (r == null)
				return null;
			Map<Integer, List<StringBuffer>> mpl = getPersonLists(String.valueOf(id), lang);
			List<StringBuffer> plist = mpl.get(id);
			int ns = 1;
			boolean isUSLeague = String.valueOf(r.getChampionship().getId()).matches(USLeaguesServlet.CHAMPIONSHIP_NFL + "|" + USLeaguesServlet.CHAMPIONSHIP_NBA + "|" + USLeaguesServlet.CHAMPIONSHIP_NHL + "|" + USLeaguesServlet.CHAMPIONSHIP_MLB);
			StringBuffer summary = new StringBuffer();
			// Info
			Integer eventId = (r.getSubevent2() != null ? r.getSubevent2().getId() : (r.getSubevent() != null ? r.getSubevent().getId() : r.getEvent().getId()));
			html.append("<span class='title'>" + r.getYear().getLabel() + " " + StringUtils.SEP1 + " " + r.getSport().getLabel(lang) + " " + StringUtils.SEP1 + " " + r.getChampionship().getLabel(lang) + (r.getEvent() != null ? " " + StringUtils.SEP1 + " " + r.getEvent().getLabel(lang) + (r.getSubevent() != null ? " " + StringUtils.SEP1 + " " + r.getSubevent().getLabel(lang) : "") + (r.getSubevent2() != null ? " " + StringUtils.SEP1 + " " + r.getSubevent2().getLabel(lang) : "") : "") + "</span>");
			html.append("<span class='url'>" + HtmlUtils.writeLink(type, id, null, r.getYear().getLabel() + "/" + r.getSport().getLabel() + "/" + r.getChampionship().getLabel() + "/" + r.getEvent().getLabel() + (r.getSubevent() != null ? "/" + r.getSubevent().getLabel() : "") + (r.getSubevent2() != null ? "/" + r.getSubevent2().getLabel() : "")) + "</span>");
			html.append("<div class='mobile100'>");
			html.append("<ul class='uinfo'><li>");
			html.append("<table class='info'><thead><tr><th id='titlename' colspan='2'>" + ResourceUtils.getText("entity.RS.1", lang).toUpperCase() + "</th></tr></thead><tbody class='tby'>");
			StringBuffer ph = ImageUtils.getPhotos(type, id, lang);
			if (ph != null) {
				html.append("<tr><td colspan='2' class='photo'><ul>");
				html.append(ph);
				html.append("</ul></td></tr>");
			}
			html.append("<tr><th class='caption'>" + ResourceUtils.getText("entity.SP.1", lang) + "</th><td>" + HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, r.getSport().getId(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Sport.alias, r.getSport().getId(), r.getSport().getLabel(lang), r.getSport().getLabel())) + "</td></tr>");
			html.append("<tr><th class='caption'>" + ResourceUtils.getText("entity.YR.1", lang) + "</th><td>" + HtmlUtils.writeLink(Year.alias, r.getYear().getId(), r.getYear().getLabel(lang), r.getYear().getLabel()) + "</td></tr>");
			html.append("<tr><th class='caption'>" + ResourceUtils.getText("entity.CP.1", lang) + "</th><td>" + HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_CHAMPIONSHIP, r.getSport().getId() + "-" + r.getChampionship().getId(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Championship.alias, r.getChampionship().getId(), r.getChampionship().getLabel(lang), r.getChampionship().getLabel())) + "</td></tr>");
			String evlink = r.getSport().getLabel() + "/" + r.getChampionship().getLabel() + "/" + r.getEvent().getLabel() + (r.getSubevent() != null ? "/" + r.getSubevent().getLabel() : "") + (r.getSubevent2() != null ? "/" + r.getSubevent2().getLabel() : "");
			evlink = "<a href='" + HtmlUtils.writeURL("/results", r.getSport().getId() + "-" + r.getChampionship().getId() + "-" + r.getEvent().getId() + (r.getSubevent() != null ? "-" + r.getSubevent().getId() : "") + (r.getSubevent2() != null ? "-" + r.getSubevent2().getId() : ""), evlink) + "'>" + (r.getEvent().getLabel(lang) + (r.getSubevent() != null ? " " + StringUtils.SEP1 + " " + r.getSubevent().getLabel(lang) : "") + (r.getSubevent2() != null ? " " + StringUtils.SEP1 + " " + r.getSubevent2().getLabel(lang) : "")) + "</a>";			
			html.append("<tr><th class='caption'>" + ResourceUtils.getText("entity.EV.1", lang) + "</th><td>" + HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_EVENT, r.getSport().getId() + "-" + eventId, ImageUtils.SIZE_SMALL, null, null), evlink) + "</td></tr>");
			if (StringUtils.notEmpty(r.getDate2()))
				html.append("<tr><th class='caption'>" + ResourceUtils.getText(StringUtils.notEmpty(r.getDate1()) ? "dates" : "date", lang) + "</th><td>" + (StringUtils.notEmpty(r.getDate1()) ? HtmlUtils.writeDateLink(null, r.getDate1(), StringUtils.toTextDate(r.getDate1(), lang, "EEEE d MMMM yyyy")) + " " + StringUtils.SEP1 + " " : "") + HtmlUtils.writeDateLink(null, r.getDate2(), StringUtils.toTextDate(r.getDate2(), lang, "EEEE d MMMM yyyy")) + "</td></tr>");
			if (StringUtils.notEmpty(r.getComplex2()) || StringUtils.notEmpty(r.getCity2()) || StringUtils.notEmpty(r.getCountry2())) {
				String p1 = null;
				String p2 = null;
				if (r.getComplex1() != null) {
					Complex cx = r.getComplex1();
					p1 = getPlace(cx.getId(), cx.getCity().getId(), cx.getCity().getState() != null ? cx.getCity().getState().getId() : null, cx.getCity().getCountry().getId(), cx.getLabel(), cx.getCity().getLabel(lang), cx.getCity().getState() != null ? cx.getCity().getState().getCode() : null, cx.getCity().getCountry().getLabel(lang), cx.getLabel(), cx.getCity().getLabel(), cx.getCity().getState() != null ? cx.getCity().getState().getLabel() : null, cx.getCity().getCountry().getLabel(), r.getYear().getLabel());
				}
				else if (r.getCity1() != null) {
					City ct = r.getCity1();
					p1 = getPlace(null, ct.getId(), ct.getState() != null ? ct.getState().getId() : null, ct.getCountry().getId(), null, ct.getLabel(lang), ct.getState() != null ? ct.getState().getCode() : null, ct.getCountry().getLabel(lang), null, ct.getLabel(), ct.getState() != null ? ct.getState().getLabel() : null, ct.getCountry().getLabel(), r.getYear().getLabel());
				}
				else if (r.getCountry1() != null) {
					Country cn = r.getCountry1();
					p1 = getPlace(null, null, null, cn.getId(), null, cn.getLabel(lang), null, cn.getLabel(lang), null, null, null, cn.getLabel(), r.getYear().getLabel());
				}
				if (r.getComplex2() != null) {
					Complex cx = r.getComplex2();
					p2 = getPlace(cx.getId(), cx.getCity().getId(), cx.getCity().getState() != null ? cx.getCity().getState().getId() : null, cx.getCity().getCountry().getId(), cx.getLabel(), cx.getCity().getLabel(lang), cx.getCity().getState() != null ? cx.getCity().getState().getCode() : null, cx.getCity().getCountry().getLabel(lang), cx.getLabel(), cx.getCity().getLabel(), cx.getCity().getState() != null ? cx.getCity().getState().getLabel() : null, cx.getCity().getCountry().getLabel(), r.getYear().getLabel());
				}
				else if (r.getCity2() != null) {
					City ct = r.getCity2();
					p2 = getPlace(null, ct.getId(), ct.getState() != null ? ct.getState().getId() : null, ct.getCountry().getId(), null, ct.getLabel(lang), ct.getState() != null ? ct.getState().getCode() : null, ct.getCountry().getLabel(lang), null, ct.getLabel(), ct.getState() != null ? ct.getState().getLabel() : null, ct.getCountry().getLabel(), r.getYear().getLabel());
				}
				else if (r.getCountry2() != null) {
					Country cn = r.getCountry2();
					p2 = getPlace(null, null, null, cn.getId(), null, cn.getLabel(lang), null, cn.getLabel(lang), null, null, null, cn.getLabel(), r.getYear().getLabel());
				}
				String places = null;
				if (p1 != null)
					places = "<table><tr><td>" + p1 + "</td><td> " + StringUtils.SEP1 + " </td><td>" + p2 + "</td></tr></table>";
				else if (p2 != null)
					places = p2;
				html.append("<tr><th class='caption'>" + ResourceUtils.getText(StringUtils.notEmpty(p1) ? "places" : "place", lang) + "</th><td>" + places + "</td></tr>");
			}
			String extlinks = HtmlUtils.writeExternalLinks(type, id, lang);
			if (StringUtils.notEmpty(extlinks)) {
				html.append("<tr><th class='caption'>" + ResourceUtils.getText("extlinks", lang) + "</th><td class='extlinks'>" + extlinks + "</td></tr>");
			}
			if (StringUtils.notEmpty(r.getComment()) && !r.getComment().matches("\\#(DOUBLE|TRIPLE)\\#")) {
				String commentCaption = null;
				String commentInfo = HtmlUtils.getCommentInfo(r.getComment(), lang);
				String commentColor = null;
				if (commentInfo != null && commentInfo.contains("|")) {
					String[] tComment = commentInfo.split("\\|", -1);
					commentCaption = tComment[0];
					commentInfo = tComment[1];
					commentColor = tComment[2];
				}
				html.append("<tr><th class='caption'>" + commentCaption + "</th>");
				html.append("<td" + (StringUtils.notEmpty(commentColor) ? " style='background-color:" + commentColor + ";'" : "") + ">" + StringUtils.getComment(commentInfo, lang).replaceAll("^\\#\\#|\\}\\}", "").replaceAll("\\{\\{", "<br/>") + "</td></tr>");
			}
			// Result
			int type_ = -1;
			final int MAX_RANKS = 20;
			List<Object> params = new ArrayList<Object>();
			params.add(0);
			params.add(0);
			params.add(0);
			params.add(0);
			params.add(0);
			params.add("0");
			params.add(r.getId());
			params.add(ResourceUtils.getLocaleParam(lang));
			List<ResultsBean> list = (List<ResultsBean>) DatabaseManager.callFunction("get_results", params, ResultsBean.class);
			if (list != null && !list.isEmpty()) {
				ResultsBean bean = list.get(0);
				String[] tEntity = new String[MAX_RANKS];
				String[] tEntityRel = new String[MAX_RANKS];
				String[] tEntityHtml = new String[MAX_RANKS];
				String[] tResult = new String[MAX_RANKS];
				List<List<String>> lTies = new ArrayList<List<String>>();
				for (int i = 0 ; i < MAX_RANKS ; i++) {
					lTies.add(new ArrayList<String>());
				}
				Event ev_ = (Event) DatabaseManager.loadEntity(Event.class, (r.getSubevent2() != null ? r.getSubevent2().getId() : (r.getSubevent() != null ? r.getSubevent().getId() : r.getEvent().getId())));
				type_ = ev_.getType().getNumber();
				boolean isScore = (bean.getRsRank1() != null && bean.getRsRank2() != null && StringUtils.notEmpty(bean.getRsResult1()) && !StringUtils.notEmpty(bean.getRsResult2()) && !StringUtils.notEmpty(bean.getRsResult3()) && !StringUtils.notEmpty(bean.getRsExa()));
				for (int i = 1 ; i <= MAX_RANKS ; i++) {
					Integer idRank = StringUtils.toInt(ResultsBean.class.getMethod("getRsRank" + i).invoke(bean));
					if (idRank != null && idRank > 0) {
						Object result = ResultsBean.class.getMethod("getRsResult" + i).invoke(bean);
						String str1 = (String) ResultsBean.class.getMethod("getEn" + i + "Str1").invoke(bean);
						String str2 = (String) ResultsBean.class.getMethod("getEn" + i + "Str2").invoke(bean);
						String str3 = (String) ResultsBean.class.getMethod("getEn" + i + "Str3").invoke(bean);
						Integer rel1Id = StringUtils.toInt(ResultsBean.class.getMethod("getEn" + i + "Rel1Id").invoke(bean));
						String rel1Label = (String) ResultsBean.class.getMethod("getEn" + i + "Rel1Label").invoke(bean);
						Integer rel2Id = StringUtils.toInt(ResultsBean.class.getMethod("getEn" + i + "Rel2Id").invoke(bean));
						String rel2Label = (String) ResultsBean.class.getMethod("getEn" + i + "Rel2Label").invoke(bean);
						String rel2LabelEN = (String) ResultsBean.class.getMethod("getEn" + i + "Rel2LabelEN").invoke(bean);
						String rel2Code = (String) ResultsBean.class.getMethod("getEn" + i + "Rel2Code").invoke(bean);
						tEntity[i - 1] = getResultsEntity(type_, idRank, str1, str2, str3, rel2Code, bean.getYrLabel(), plist != null && plist.size() > 0 ? "plist-" + id + "-" + (i - 1) : null);
						tEntityRel[i - 1] = getResultsEntityRel(rel1Id, rel1Label, rel1Label, rel2Id, rel2Label, rel2Label, rel2LabelEN, type_ < 10, type_ <= 50 && !isUSLeague, bean.getYrLabel()).replaceAll("\\<td\\>\\-\\<\\/td\\>", "");
						tResult[i - 1] = StringUtils.formatResult(result, lang);
					}
				}
				boolean isSingle = (bean.getRsComment() != null && bean.getRsComment().equals("#SINGLE#"));
				boolean isDouble = ((type_ == 4 && !isSingle) || (bean.getRsComment() != null && bean.getRsComment().equals("#DOUBLE#")));
				boolean isTriple = ((type_ == 5 && !isSingle) || (bean.getRsComment() != null && bean.getRsComment().equals("#TRIPLE#")));
				setTies(getTieList(isDouble, isTriple, bean.getRsExa()), type_, tEntity, tEntityRel, lTies);
				if (isTriple || isDouble) {
					tEntity = StringUtils.removeNulls(tEntity);
					tEntityRel = StringUtils.removeNulls(tEntityRel);
					for (int i = lTies.size() - 1 ; i >= 0 ; i--) {
						if (lTies.get(i).isEmpty()) {
							lTies.remove(i);
						}
					}
				}
				String rspan = null;
				for (int i = 0 ; i < MAX_RANKS ; i++) {
					if (StringUtils.notEmpty(tEntity[i])) {
						rspan = (!lTies.get(i).isEmpty() ? " rowspan='" + (lTies.get(i).size() + 1) + "' class='vatop'" : "");
						tEntityHtml[i] = ("<td>" + tEntity[i] + (plist != null && plist.size() > i ? "<table id='plist-" + id + "-" + i + "' class='plist' style='display:none;'>" + plist.get(i).toString() + "</table>" : "") + "</td>" + tEntityRel[i] + (StringUtils.notEmpty(tResult[i]) && !isScore ? "<td" + rspan + ">" + tResult[i] + "</td>" : ""));
					}
				}
				rspan = (!lTies.get(0).isEmpty() ? " rowspan='" + (lTies.get(0).size() + 1) + "' class='vatop'" : "");
				html.append("</tbody></table>");
				html.append("</li></ul></div>");
				boolean isMedal = String.valueOf(r.getChampionship().getId()).matches(ConfigUtils.getValue("cp_medal_pattern"));
				String anchor = ResourceUtils.getText("final.result", lang).replaceAll("\\s|\\/", "_");
				summary.append("<a href='#" + anchor + "'>" + ++ns + ". " + ResourceUtils.getText("final.result", lang) + "</a><br/>");
				html.append("<div id='" + anchor + "' class='chaptertitle'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("final.result", lang), false) + "</div>");
				html.append("<table id='finalresult'><tr style='font-weight:bold;'>" + (!isScore ? "<th" + rspan + ">" + (isMedal ? ImageUtils.getGoldMedImg(lang) : "1. ") + "</th>" : "") + (tEntityHtml[0] != null ? tEntityHtml[0] : "<td>" + ResourceUtils.getText("none", lang) + "</td>")).append("</tr>");
				if (isScore) {
					html.append("<tr><td colspan='" + (StringUtils.countIn(tEntityHtml[0], "<td")) + "' class='finalscore centered'>" + tResult[0] + "</td></tr>");
				}
				if (!lTies.get(0).isEmpty()) {
					html.append("<tr>" + StringUtils.join(lTies.get(0), "</tr><tr>") + "</tr>");
				}
				for (int i = 1 ; i < MAX_RANKS ; i++)
					if (StringUtils.notEmpty(tEntityHtml[i])) {
						rspan = (!lTies.get(i).isEmpty() ? " rowspan='" + (lTies.get(i).size() + 1) + "' class='vatop'" : "");
						html.append("<tr>");
						if (!isScore) {
							html.append("<th" + rspan + ">" + (isMedal && i < 3 ? (i == 1 ? ImageUtils.getSilverMedImg(lang) : ImageUtils.getBronzeMedImg(lang)) : (i + 1) + ".") + "</th>");
						}
						html.append(tEntityHtml[i] + "</tr>");
						if (!lTies.get(i).isEmpty()) {
							html.append("<tr>" + StringUtils.join(lTies.get(i), "</tr><tr>") + "</tr>");
						}
					}
				html.append("</table>");
			}
			// Other events
			String l_ = "label" + ResourceUtils.getLocaleParam(lang);
			String sql = "SELECT RS.id, RS." + (r.getSubevent2() != null ? "id_subevent2" : (r.getSubevent() != null ? "id_subevent" : "id_event")) + ", EV." + l_ + ", EV.label, EV.index, II.id, RS.comment, RS.date2 FROM result RS";
			sql += " LEFT JOIN event EV ON RS." + (r.getSubevent2() != null ? "id_subevent2" : (r.getSubevent() != null ? "id_subevent" : "id_event")) + "=EV.id LEFT JOIN _inactive_item II ON (RS.id_sport = II.id_sport AND RS.id_championship = II.id_championship AND RS.id_event = II.id_event AND (RS.id_subevent = II.id_subevent OR RS.id_subevent IS NULL) AND (RS.id_subevent2 = II.id_subevent2 OR RS.id_subevent2 IS NULL))";
			sql += " WHERE RS.id_year = " + r.getYear().getId() + " AND RS.id_sport = " + r.getSport().getId() + " AND RS.id_championship = " + r.getChampionship().getId();
			if (r.getSubevent() == null)
				sql += " AND RS.id_subevent IS NULL AND RS.id_subevent2 IS NULL";
			else if (r.getSubevent2() == null)
				sql += " AND RS.id_event = " + r.getEvent().getId() + " AND RS.id_subevent2 IS NULL";
			else
				sql += " AND RS.id_event = " + r.getEvent().getId() + " AND RS.id_subevent = " + r.getSubevent().getId();
			sql += " ORDER BY EV.index, EV." + l_;
			StringBuffer sbOtherEvents = new StringBuffer();
			List<Object[]> l = (List<Object[]>) DatabaseManager.executeSelect(sql);
			if (l != null && l.size() > 1) {
				sbOtherEvents.append("<span>" + r.getYear().getLabel() + "</span><hr/>");
				int n = 0;
				List<String> le = new ArrayList<String>();
				for (Object[] t : l) {
					Integer id_ = (Integer) t[0];
					String event = String.valueOf(t[2]);
					String path = r.getYear().getLabel() + "/" + r.getSport().getLabel() + "/" + r.getChampionship().getLabel() + (r.getSubevent() != null ? "/" + r.getEvent().getLabel() : "") + (r.getSubevent2() != null ? "/" + r.getSubevent().getLabel() : "");
					path += "/" + t[3];
					String comment = String.valueOf(t[6]);
					String date2 = String.valueOf(t[7]);
					String s = "";
					if (le.contains(event) || (n < l.size() - 1 && StringUtils.notEmpty(l.get(n + 1)[2]) && l.get(n + 1)[2].equals(event))) {
						if (StringUtils.notEmpty(date2) && !date2.equals("null"))
							s = StringUtils.toTextDate(date2, lang, "MMMM");
						if (StringUtils.notEmpty(comment) && comment.startsWith("##"))
							s += (StringUtils.notEmpty(s) ? ", " : "") + comment.replaceAll("\\{\\{.*\\}\\}|##", "");
						s = " (" + s + ")";
					}
					if (!r.getId().equals(id_))
						sbOtherEvents.append(HtmlUtils.writeLink(Result.alias, id_, event + s, path)).append("<br/>");
					else
						sbOtherEvents.append("<b>" + event + s + "</b><br/>");
					le.add(event);
					n++;
				}
			}
			// Other years
			sql = "SELECT RS.id, YR.label, comment, date2 FROM result RS "
					+ " JOIN year YR ON YR.id = RS.id_year "
					+ " WHERE id_sport = " + r.getSport().getId() + " AND id_championship = " + r.getChampionship().getId() + " AND id_event = " + r.getEvent().getId() + (r.getSubevent() != null ? " AND id_subevent = " + r.getSubevent().getId() : "") + (r.getSubevent2() != null ? " AND id_subevent2 = " + r.getSubevent2().getId() : "") + " ORDER BY RS.id_year, RS.id";
			l = (List<Object[]>) DatabaseManager.executeSelect(sql);
			if (l != null && l.size() > 1) {
				String path = r.getSport().getLabel() + "/" + r.getChampionship().getLabel() + "/" + r.getEvent().getLabel() + (r.getSubevent() != null ? "/" + r.getSubevent().getLabel() : "") + (r.getSubevent2() != null ? "/" + r.getSubevent2().getLabel() : "");
				StringBuffer sbOtherYears = new StringBuffer();
				int n = 0;
				List<String> ly = new ArrayList<String>();
				for (Object[] t : l) {
					Integer id_ = (Integer) t[0];
					String label = String.valueOf(t[1]);
					String comment = String.valueOf(t[2]);
					String date2 = String.valueOf(t[3]);
					String s = "";
					if (ly.contains(label) || (n < l.size() - 1 && l.get(n + 1)[1].equals(label))) {
						if (StringUtils.notEmpty(date2) && !date2.equals("null"))
							s = StringUtils.toTextDate(date2, lang, "MMM");
						if (StringUtils.notEmpty(comment) && comment.startsWith("##"))
							s += (StringUtils.notEmpty(s) ? ", " : "") + comment.replaceAll("\\{\\{.*\\}\\}|##", "");
						s = " (" + s + ")";
					}
					if (!id_.equals(r.getId()))
						sbOtherYears.append(HtmlUtils.writeLink(Result.alias, id_, label + s, label + "/" + path));
					else
						sbOtherYears.append("<b>" + label + s + "</b>");
					if (++n != l.size()) {
						sbOtherYears.append("<span class='bullet'>&#8226;</span>");
						if (n % 10 == 0)
							sbOtherYears.append("<br/>");
					}
					ly.add(label);
				}
				String anchor = ResourceUtils.getText("entity.YR", lang).replaceAll("\\s|\\/", "_");
				summary.append("<a href='#" + anchor + "'>" + ++ns + ". " + ResourceUtils.getText("entity.YR", lang) + "</a><br/>");
				html.append("<div id='" + anchor + "' class='chaptertitle'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity.YR", lang), false) + "</div>");
				html.append("<div id='othyears' class='border1 centered mobile100'>").append(sbOtherYears.toString()).append("</div>");
			}
			// Rounds
			params = new ArrayList<Object>();
			params.add(id);
			params.add(ResourceUtils.getLocaleParam(lang));
			List<RoundsBean> lRounds = (List<RoundsBean>) DatabaseManager.callFunction("get_rounds", params, RoundsBean.class);
			if (lRounds != null && !lRounds.isEmpty()) {
				StringBuffer rdlistHtml = new StringBuffer();
				StringBuffer drawHtml = new StringBuffer();
				String rk1 = null; String rel1 = null; String rel1_ = null;
				String rk2 = null; String rel2 = null; String rel2_ = null;
				String rk3 = null; String rel3 = null;
				String rk4 = null; String rel4 = null;
				String rk5 = null; String rel5 = null;
				String pl1 = null; String pl2 = null;
				boolean isComment = false;
				for (RoundsBean rb : lRounds)
					isComment |= StringUtils.notEmpty(rb.getRdComment());
				for (RoundsBean rb : lRounds) {
					rk1 = getResultsEntity(rb.getRdResultType(), rb.getRk1Id(), rb.getRk1Str1(), rb.getRk1Str2(), rb.getRk1Str3(), rb.getRk1Rel2Code(), r.getYear().getLabel(), null);
					rk2 = getResultsEntity(rb.getRdResultType(), rb.getRk2Id(), rb.getRk2Str1(), rb.getRk2Str2(), rb.getRk2Str3(), rb.getRk2Rel2Code(), r.getYear().getLabel(), null);
					rk3 = getResultsEntity(rb.getRdResultType(), rb.getRk3Id(), rb.getRk3Str1(), rb.getRk3Str2(), rb.getRk3Str3(), rb.getRk3Rel2Code(), r.getYear().getLabel(), null);
					rk4 = getResultsEntity(rb.getRdResultType(), rb.getRk4Id(), rb.getRk4Str1(), rb.getRk4Str2(), rb.getRk4Str3(), rb.getRk4Rel2Code(), r.getYear().getLabel(), null);
					rk5 = getResultsEntity(rb.getRdResultType(), rb.getRk5Id(), rb.getRk5Str1(), rb.getRk5Str2(), rb.getRk5Str3(), rb.getRk5Rel2Code(), r.getYear().getLabel(), null);
					rel1 = (rb.getRk1Rel2Id() != null && !isUSLeague ? HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, rb.getRk1Rel2Id(), ImageUtils.SIZE_SMALL, r.getYear().getLabel(), rb.getRk1Rel2Label()) : null);
					rel2 = (rb.getRk2Rel2Id() != null && !isUSLeague ? HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, rb.getRk2Rel2Id(), ImageUtils.SIZE_SMALL, r.getYear().getLabel(), rb.getRk2Rel2Label()) : null);
					rel3 = (rb.getRk3Rel2Id() != null && !isUSLeague ? HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, rb.getRk3Rel2Id(), ImageUtils.SIZE_SMALL, r.getYear().getLabel(), rb.getRk3Rel2Label()) : null);
					rel4 = (rb.getRk4Rel2Id() != null && !isUSLeague ? HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, rb.getRk4Rel2Id(), ImageUtils.SIZE_SMALL, r.getYear().getLabel(), rb.getRk4Rel2Label()) : null);
					rel5 = (rb.getRk5Rel2Id() != null && !isUSLeague ? HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, rb.getRk5Rel2Id(), ImageUtils.SIZE_SMALL, r.getYear().getLabel(), rb.getRk5Rel2Label()) : null);
					rel1_ = getResultsEntityRel(rb.getRk1Rel1Id(), rb.getRk1Rel1Code(), rb.getRk1Rel1Label(), rb.getRk1Rel2Id(), rb.getRk1Rel2Code(), rb.getRk1Rel2Label(), rb.getRk1Rel2LabelEN(), type_ < 10 && rb.getRk1Rel1Id() != null, type_ <= 50 && !isUSLeague, r.getYear().getLabel());
					rel2_ = getResultsEntityRel(rb.getRk2Rel1Id(), rb.getRk2Rel1Code(), rb.getRk2Rel1Label(), rb.getRk2Rel2Id(), rb.getRk2Rel2Code(), rb.getRk2Rel2Label(), rb.getRk2Rel2LabelEN(), type_ < 10 && rb.getRk2Rel1Id() != null, type_ <= 50 && !isUSLeague, r.getYear().getLabel());
					String d1 = rb.getRdDate1();
					String d2 = rb.getRdDate2();
					String dates = "";
					if (StringUtils.notEmpty(d1) && StringUtils.notEmpty(d2)) {
						if (d1.substring(3).equals(d2.substring(3)))
							dates = HtmlUtils.writeDateLink(d1, d2, d1.substring(0, 2).replaceFirst("^0", "") + StringUtils.SEP1 + StringUtils.toTextDate(d2, lang, "d MMMM yyyy"));
						else
							dates = HtmlUtils.writeDateLink(d1, d2, StringUtils.toTextDate(d1, lang, "d MMMM yyyy") + StringUtils.SEP1 + StringUtils.toTextDate(d2, lang, "d MMMM yyyy"));
					}
					else if (StringUtils.notEmpty(d2))
						dates = HtmlUtils.writeDateLink(null, d2, StringUtils.toTextDate(d2, lang, "d MMMM yyyy"));
					if (rb.getCx1Id() != null)
						pl1 = getPlace(rb.getCx1Id(), rb.getCt1Id(), rb.getSt1Id(), rb.getCn1Id(), rb.getCx1Label(), rb.getCt1Label(), rb.getSt1Code(), rb.getCn1Code(), rb.getCx1Label(), rb.getCt1LabelEN(), rb.getSt1LabelEN(), rb.getCn1LabelEN(), r.getYear().getLabel());
					else if (rb.getCt2Id() != null)
						pl1 = getPlace(null, rb.getCt2Id(), rb.getSt2Id(), rb.getCn2Id(), null, rb.getCt2Label(), rb.getSt2Code(), rb.getCn2Code(), null, rb.getCt2LabelEN(), rb.getSt2LabelEN(), rb.getCn2LabelEN(), r.getYear().getLabel());
					else
						pl1 = null;
					if (rb.getCx2Id() != null)
						pl2 = getPlace(rb.getCx2Id(), rb.getCt3Id(), rb.getSt3Id(), rb.getCn3Id(), rb.getCx2Label(), rb.getCt3Label(), rb.getSt3Code(), rb.getCn3Code(), rb.getCx2Label(), rb.getCt3LabelEN(), rb.getSt3LabelEN(), rb.getCn3LabelEN(), r.getYear().getLabel());
					else if (rb.getCt4Id() != null)
						pl2 = getPlace(null, rb.getCt4Id(), rb.getSt4Id(), rb.getCn4Id(), null, rb.getCt4Label(), rb.getSt4Code(), rb.getCn4Code(), null, rb.getCt4LabelEN(), rb.getSt4LabelEN(), rb.getCn4LabelEN(), r.getYear().getLabel());
					else
						pl2 = null;
					if (pl1 != null)
						pl2 = "<table><tr><td>" + pl1 + "</td><td> " + StringUtils.SEP1 + " </td><td>" + pl2 + "</td></tr></table>";
					String commentInfo = HtmlUtils.getCommentInfo(rb.getRdComment(), lang);
					String commentColor = null;
					if (commentInfo != null && commentInfo.contains("|")) {
						String[] tComment = commentInfo.split("\\|", -1);
						commentInfo = tComment[1];
						commentColor = tComment[2];
					}
					if (isComment) {
						commentInfo = (StringUtils.notEmpty(commentInfo) ? "<td" + (StringUtils.notEmpty(commentColor) ? " style='white-space:nowrap;background-color:" + commentColor + ";'" : "") + ">" + HtmlUtils.writeComment(rb.getRdId(), StringUtils.getComment(commentInfo, lang)) + "</td>" : "<td></td>");
					}
					else {
						commentInfo = "";
					}
					if (type_ == 4) {
						if (rk1 != null && rk2 != null) {
							if (rel1 != null && rel2 != null)
								rel1 += "</th><th>" + rel2;
							rk1 += "</td><td>/</td><td>" + rk2;
							rk2 = null;
						}
						if (rk3 != null && rk4 != null) {
							if (rel3 != null && rel4 != null)
								rel3 += "</th><th>" + rel4;
							rk3 += "</td><td>/</td><td>" + rk4;
							rk4 = null;
						}
						rk5 = null;
					}
					rdlistHtml.append("<tr>").append(commentInfo);
					rdlistHtml.append("<td>" + rb.getRtLabel() + "</td>");
					rdlistHtml.append("<td><table><tr>" + (rel1 != null ? "<th>" + rel1 + "</th>" : "") + "<td>" + rk1 + "</td>");
					rdlistHtml.append("<td>&nbsp;" + StringUtils.formatResult(rb.getRdResult1(), lang) + "&nbsp;</td>");
					if (rk2 != null) {
						rdlistHtml.append(rel2 != null ? "<th>" + rel2 + "</th>" : "").append("<td>" + rk2 + "</td>");
						if (StringUtils.notEmpty(rb.getRdResult2()) && type_ != 4)
							rdlistHtml.append("<td>&nbsp;" + StringUtils.formatResult(rb.getRdResult2(), lang) + "&nbsp;</td>");
					}
					if (rk3 != null) {
						rdlistHtml.append(rel3 != null ? "<th>" + rel3 + "</th>" : "").append("<td>" + rk3 + "</td>");
						if (StringUtils.notEmpty(rb.getRdResult3()) && type_ != 4)
							rdlistHtml.append("<td>&nbsp;" + StringUtils.formatResult(rb.getRdResult3(), lang) + "&nbsp;</td>");
					}
					if (rk4 != null) {
						rdlistHtml.append(rel4 != null ? "<th>" + rel4 + "</th>" : "").append("<td>" + rk4 + "</td>");
						if (StringUtils.notEmpty(rb.getRdResult4()) && type_ != 4)
							rdlistHtml.append("<td>&nbsp;" + StringUtils.formatResult(rb.getRdResult4(), lang) + "&nbsp;</td>");
					}
					if (rk5 != null) {
						rdlistHtml.append(rel5 != null ? "<th>" + rel5 + "</th>" : "").append("<td>" + rk5 + "</td>");
						if (StringUtils.notEmpty(rb.getRdResult5()) && type_ != 4)
							rdlistHtml.append("<td>&nbsp;" + StringUtils.formatResult(rb.getRdResult5(), lang) + "&nbsp;</td>");
					}
					rdlistHtml.append("</tr></table></td>");
					rdlistHtml.append("<td>" + (StringUtils.notEmpty(dates) ? dates : "") + "</td>");
					rdlistHtml.append("<td>" + (pl2 != null ? pl2 : "") + "</td></tr>");
					if (rk1 != null && rb.getRtIndex() > 0 && rb.getRtIndex() <= 8) {
						drawHtml.append("<div class='box box" + Math.round(rb.getRtIndex()) + "'><table><tr><th colspan='" + (rb.getRdResultType() < 10 || (rb.getRdResultType() == 50 && !isUSLeague) ? 3 : 2) + "'>" + rb.getRtLabel() + "</th></tr>");
						drawHtml.append("<tr><td style='font-weight:bold;'>" + rk1 + "</td>" + (rel1_ != null ? rel1_ : ""));
						drawHtml.append("<td rowspan='2' class='boxresult'>" + StringUtils.formatResult(rb.getRdResult1(), lang).replaceAll("\\&nbsp\\;", " ") + "</td></tr>");
						drawHtml.append("<tr><td>" + rk2 + "</td>" + (rel2_ != null ? rel2_ : "") + "</tr>");
						drawHtml.append("</table></div>");
					}
				}
				if (lRounds.size() >= 7 && drawHtml.length() > 0) {
					String anchor = ResourceUtils.getText("draw", lang).replaceAll("\\s|\\/", "_");
					summary.append("<a href='#" + anchor + "'>" + ++ns + ". " + ResourceUtils.getText("draw", lang) + "</a><br/>");
					html.append("<div id='" + anchor + "' class='chaptertitle'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("draw", lang), false) + "</div>");
					html.append("<div class='draw centered'>").append(drawHtml).append("</div>");
				}
				if (rdlistHtml.length() > 0) {
					String anchor = ResourceUtils.getText("entity.RD", lang).replaceAll("\\s|\\/", "_");
					summary.append("<a href='#" + anchor + "'>" + ++ns + ". " + ResourceUtils.getText("entity.RD", lang) + "</a><br/>");
					html.append("<div id='" + anchor + "' class='chaptertitle'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity.RD", lang), false) + "</div>");
					html.append("<div class='rounds centered'><table><tr>" + (isComment ? "<th>" + ResourceUtils.getText("note", lang) + "</th>" : "") + "<th>" + ResourceUtils.getText("description", lang) + "</th><th>" + ResourceUtils.getText("entity.RS.1", lang) + "</th><th>" + ResourceUtils.getText("date", lang) + "</th><th>" + ResourceUtils.getText("place", lang) + "</th></tr>");
					html.append(rdlistHtml).append("</table></div>");
				}
			}
			if (summary.length() > 0) {
				html.append("<div id='summary'><a href='#Header'>1. " + ResourceUtils.getText("header", lang) + "</a><br/>" + summary.toString() + "</div>");
			}
			if (sbOtherEvents.length() > 0) {
				html.append("<div id='summary2'>" + sbOtherEvents.toString() + "</div>");
			}
			request.setAttribute("lastupdate", StringUtils.toTextDate(r.getMetadata().getLastUpdate(), lang, "d MMM yyyy, HH:mm"));
			return html;
		}
		else if (type.equals(Sport.alias)) {
			Sport e = (Sport) DatabaseManager.loadEntity(Sport.class, id);
			if (e == null)
				return null;
			hInfo.put("_sport_", "1");
			hInfo.put("width", "280");
			hInfo.put("title", e.getLabel(lang));
			hInfo.put("titleEN", e.getLabel());
			hInfo.put("logosport", HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, e.getId(), ImageUtils.SIZE_LARGE, null, null));
			hInfo.put("titlename", e.getLabel(lang).toUpperCase());
			hInfo.put("references", "<b>" + ResourceUtils.getText("references", lang) + "</b> : " + String.valueOf(e.getRef()));
			hInfo.put("extlinks", HtmlUtils.writeExternalLinks(type, id, lang));
			lastUpdate = e.getMetadata().getLastUpdate();
			
			int y = Calendar.getInstance().get(Calendar.YEAR);
			int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
			List<Object> params = new ArrayList<Object>();
			params.add(y + (m < 10 ? "0" : "") + m + "01");
			m++;
			if (m > 12) {
				m = 1;
				y++;
			}
			params.add(y + (m < 10 ? "0" : "") + m + "01");
			params.add(id);
			params.add(ResourceUtils.getLocaleParam(lang));
			int n = 0;
			StringBuffer sb = new StringBuffer();
			for (RefItem item : (Collection<RefItem>) DatabaseManager.callFunctionSelect("get_calendar_results", params, RefItem.class, "entity DESC, date2 DESC")) {
				String event = "<a href='" + HtmlUtils.writeURL("/results", item.getIdRel2() + "-" + item.getIdRel3() + (item.getIdRel4() != null ? "-" + item.getIdRel4() : "") + (item.getIdRel5() != null ? "-" + item.getIdRel5() : "") + (item.getIdRel18() != null ? "-" + item.getIdRel18() : ""), item.getLabelRel12() + "/" + item.getLabelRel13() + (item.getIdRel4() != null ? "/" + item.getLabelRel14() : "") + (item.getIdRel5() != null ? "/" + item.getLabelRel15() : "") + (item.getIdRel18() != null ? "/" + item.getLabelRel16() : "")) + "'>" + (item.getLabelRel3() + (item.getIdRel4() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel4() : "") + (item.getIdRel5() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel5() : "") + (item.getIdRel18() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel18() : "")) + "</a>";
				if (StringUtils.notEmpty(item.getTxt5()))
					event += " [" + item.getTxt5() + "]";
				String d1 = StringUtils.toTextDate(item.getDate1(), lang, "dd/MM/yyyy");
				String d2 = StringUtils.toTextDate(item.getDate2(), lang, "dd/MM/yyyy");
				String dates_ = "";
				if (StringUtils.notEmpty(d1) && StringUtils.notEmpty(d2)) {
					if (d1.substring(3).equals(d2.substring(3)))
						dates_ = HtmlUtils.writeDateLink(d1, d2, d1.substring(0, 2).replaceFirst("^0", "") + StringUtils.SEP1 + StringUtils.toTextDate(d2, lang, "d MMM"));
					else
						dates_ = HtmlUtils.writeDateLink(d1, d2, StringUtils.toTextDate(d1, lang, "d MMM") + StringUtils.SEP1 + StringUtils.toTextDate(d2, lang, "d MMM"));
				}
				else if (StringUtils.notEmpty(d2))
					dates_ = HtmlUtils.writeDateLink(null, d2, StringUtils.toTextDate(d2, lang, "d MMM"));
				sb.append("<li" + (++n > 3 ? " style='display:none;'" : "") + ">").append(dates_);
				sb.append(" : ").append(event).append("</li>");
			}
			if (sb.length() > 0)
				hInfo.put("calendar", ResourceUtils.getText("this.month", lang) + ":<br/><ul id='calmonth'>" + sb.toString() + "</ul>" + (sb.toString().contains("display:none") ? "<a id=\"calmore\" href='javascript:$$(\"#calmonth li\").each(function(el){$(el).show();});$(\"calmore\").hide();' class='otherimglink' style='float:right;padding-right:0px;'>" + ResourceUtils.getText("more", lang) + "</a>" : ""));
			StringWriter sw = new StringWriter();
			params = new ArrayList<Object>();
			params.add("WHERE SP.id=" + id);
			params.add(ResourceUtils.getLocaleParam(lang));
			HtmlConverter.convertTreeArray(DatabaseManager.callFunctionSelect("tree_results", params, TreeItem.class), sw, true, lang);
			hInfo.put("tree", "<div class='treediv'><div id='treeview' class='collapsed'><table><tr><td>\r\n<script><!--\r\nvar " + sw.toString() + "new Tree(treeItems, treeTemplate);\r\n--></script>\r\n</td></tr></table></div></div>");
		}
		else if (type.equals(State.alias)) {
			State e = (State) DatabaseManager.loadEntity(State.class, id);
			if (e == null)
				return null;
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
			Team e = (Team) DatabaseManager.loadEntity(Team.class, id);
			if (e == null)
				return null;
			if (e.getLink() != null && e.getLink() >= 0) {
				Team e_ = (Team) DatabaseManager.loadEntity(Team.class, e.getLink());
				String wId = "(-1" + (e_ != null && e_.getId() > 0 ? "," + e_.getId() : "") + (e_ != null && e_.getLink() > 0 ? "," + e_.getLink() : "") + (e.getId() > 0 ? "," + e.getId() : "") + (e.getLink() > 0 ? "," + e.getLink() : "") + ")";
				lTeam.addAll((List<Team>)DatabaseManager.executeSelect(Team.query + " WHERE T.id IN " + wId + " OR T.link IN " + wId + " ORDER BY T.year1 desc, T.label", Team.class));
				id = lTeam.get(0).getId();
			}
			else
				lTeam.add(e);
			String titlename = "";
			String currentLogo = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, e.getId(), ImageUtils.SIZE_LARGE, null, null);
			Collection<String> lAllLogos = ImageUtils.getImages(ImageUtils.INDEX_TEAM, e.getId(), ImageUtils.SIZE_LARGE);
			ArrayList<Integer> lId = new ArrayList<Integer>();
			Vector<String> vNm = new Vector<String>();
			Vector<Integer> vSp = new Vector<Integer>();
			StringBuffer sbTm = new StringBuffer();
			StringBuffer sbSp = new StringBuffer();
			StringBuffer sbTmFH = new StringBuffer();
			int n = 0;
			for (Team t : lTeam) {
				lId.add(t.getId());
				ref += (t.getRef() != null && (t.getId().equals(currentId) || !StringUtils.notEmpty(t.getYear1())) ? t.getRef() : 0);
				if (t.getId() == id)
					titlename = t.getLabel();
				else if (!vNm.contains(t.getLabel())) {
					if (n == 5)
						sbTm.append("<br id='onbr'/><a id='onlink' href='javascript:$(\"onbr\").hide();$(\"onlink\").hide();$(\"othernames\").show();'>[...]</a><br/><span id='othernames' style='display:none;'>");
					sbTm.append(sbTm.toString().length() > 0 && n != 5 ? "<br/>" : "").append(t.getLabel().toUpperCase());
					vNm.add(t.getLabel());
					n++;
				}
				sbTmFH.append((t.getInactive() != null && t.getInactive() ? "&dagger; " : "") + (t.getId() == currentId ? "<b>" : "") + HtmlUtils.writeLink(Team.alias, t.getId(), t.getLabel(), null) + " (" + t.getYear1() + " " + StringUtils.SEP1 + " " + (StringUtils.notEmpty(t.getYear2()) ? t.getYear2() : ResourceUtils.getText("today", lang)) + ")" + (t.getId() == currentId ? "</b>" : "") + "<br/>");
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
			if (n > 5)
				sbTm.append("</span>");
			String cn = null;
			if (e.getCountry() != null) {
				cn = HtmlUtils.writeLink(Country.alias, e.getCountry().getId(), e.getCountry().getLabel(lang), e.getCountry().getLabel());
				cn = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, e.getCountry().getId(), ImageUtils.SIZE_SMALL, null, null), cn);
			}
			String sp = sbSp.toString();
			hInfo.put("title", e.getLabel());
			hInfo.put("titlename", "<b>" + (StringUtils.notEmpty(e.getYear1()) ? e.getLabel().toUpperCase() : titlename.toUpperCase()) + "</b>");
			hInfo.put("logo", currentLogo);
			if (StringUtils.notEmpty(sbTm.toString()) && !StringUtils.notEmpty(e.getYear1()))
				hInfo.put("titlename2", sbTm.toString());
			StringBuffer sbOtherLogos = new StringBuffer();
			if (lAllLogos != null && lAllLogos.size() > 1) {
				int nol = 0;
				for (String s : lAllLogos)
					if (s.matches(".*\\d{4}\\-\\d{4}(\\_\\d+|)\\.png$")) {
						String s_ = s.substring(s.indexOf("_") + 1, s.lastIndexOf(".")).replaceAll("\\_\\d+$", "");
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
			if (e.getLeague() != null) {
				Short cp = USLeaguesServlet.HLEAGUES.get(Short.valueOf(e.getLeague().getId().toString()));
				hInfo.put("league", HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, cp, ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Championship.alias, cp, e.getLeague().getLabel(), null)));
			}
			if (StringUtils.notEmpty(e.getConference()))
				hInfo.put("conference", e.getConference());
			if (StringUtils.notEmpty(e.getDivision()))
				hInfo.put("division", e.getDivision());
			if (StringUtils.notEmpty(e.getYear1()))
				hInfo.put("franchist", sbTmFH.toString());
			// Record
			List<Object> params = new ArrayList<Object>();
			params.add("TM");
			params.add(e.getSport().getId());
			params.add(lId.toString().replaceAll("\\[|\\]", "").replaceAll("\\,\\s", "-"));
			Collection<RefItem> cRecord = (Collection<RefItem>) DatabaseManager.callFunctionSelect("get_medal_count", params, RefItem.class);
			if (!cRecord.isEmpty())
				hInfo.put("record", HtmlUtils.writeRecordItems(cRecord, lang));
			// Retired Numbers
			List<RetiredNumber> lRn = (List<RetiredNumber>) DatabaseManager.executeSelect(RetiredNumber.query + " WHERE T.id_team = ? ORDER BY T.number", Arrays.asList(e.getId()), RetiredNumber.class);
			if (lRn != null && !lRn.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				for (RetiredNumber rn : lRn) {
					String name = HtmlUtils.writeLink(Athlete.alias, rn.getPerson().getId(), StringUtils.toFullName(rn.getPerson().getLastName(), rn.getPerson().getFirstName(), null, true), rn.getPerson().getFirstName() + " " + rn.getPerson().getLastName());
					sb.append("<i>" + rn.getNumber() + "</i> " + StringUtils.SEP1 + " " + name).append("<br/>");
				}
				hInfo.put("entity.RN", sb.toString());
			}
			// Team Stadiums
			List<TeamStadium> lTs = (List<TeamStadium>) DatabaseManager.executeSelect(TeamStadium.query + " WHERE T.id_team = ? ORDER BY T.date1 DESC", Arrays.asList(e.getId()), TeamStadium.class);
			if (lTs != null && !lTs.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				for (TeamStadium ts : lTs) {
					City ct = ts.getComplex().getCity();
					sb.append("<table><tr" + (ts.getDate2() == 0 ? " class='bold'" : "") + ">");
					sb.append("<th>" + HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, ct.getCountry().getId(), ImageUtils.SIZE_SMALL, null, ct.getCountry().getLabel(lang)) + "</th>");
					if (ct.getState() != null)
						sb.append("<th>" + HtmlUtils.writeImage(ImageUtils.INDEX_STATE, ct.getState().getId(), ImageUtils.SIZE_SMALL, null, ct.getState().getLabel(lang)) + "</th>");
					sb.append("<td>" + HtmlUtils.writeLink(Complex.alias, ts.getComplex().getId(), ts.getComplex().getLabel(), ts.getComplex().getLabel()) + "</td>");
					sb.append("<td>, </td><td>" + HtmlUtils.writeLink(City.alias, ct.getId(), ct.getLabel(lang), ct.getLabel()) + "</td>");
					if (ct.getState() != null)
						sb.append("<td>, </td><td>" + HtmlUtils.writeLink(State.alias, ct.getState().getId(), ct.getState().getCode(), ct.getState().getLabel()) + "</td>");
					sb.append("<td>, </td><td>" + HtmlUtils.writeLink(Country.alias, ct.getCountry().getId(), ct.getCountry().getCode(), ct.getCountry().getLabel()) + "</td>");
					sb.append("<td> (" + ts.getDate1() + " " + StringUtils.SEP1 + " " + (ts.getDate2() != 0 ? ts.getDate2() : ResourceUtils.getText("today", lang)) + ")</td>");
					sb.append("</tr></table>");
				}
				hInfo.put("entity.TS", sb.toString());
			}
			lastUpdate = e.getMetadata().getLastUpdate();
		}
		else if (type.equals(Year.alias)) {
			Year e = (Year) DatabaseManager.loadEntity(Year.class, id);
			if (e == null)
				return null;
			hInfo.put("_year_", "1");
			hInfo.put("width", "280");
			hInfo.put("title", e.getLabel());
			hInfo.put("titlename", e.getLabel(lang));
			ref = e.getRef();
			lastUpdate = e.getMetadata().getLastUpdate();
			StringWriter sw = new StringWriter();
			List<Object> params = new ArrayList<Object>();
			params.add("YR.id=" + id);
			params.add(ResourceUtils.getLocaleParam(lang));
			HtmlConverter.convertTreeArray(DatabaseManager.callFunctionSelect("tree_months", params, TreeItem.class), sw, false, lang);
			hInfo.put("tree", "<div class='treediv'><div id='treeview' class='collapsed'><table><tr><td><script>var " + sw.toString() + "new Tree(treeItems, treeTemplate);</script></td></tr></table></div></div>");
		}
		hInfo.put("url", "http://" + request.getServerName() + HtmlUtils.writeLink(type, id, null, hInfo.containsKey("titleEN") ? hInfo.get("titleEN") : hInfo.get("title")));
		if (!type.matches(Contributor.alias + "|" + Sport.alias + "|" + Year.alias)) {
			hInfo.put("references", String.valueOf(ref));
			hInfo.put("extlinks", HtmlUtils.writeExternalLinks(type, id, lang));
		}
		if (type.matches(Athlete.alias + "|" + City.alias + "|" + Complex.alias)) {
			StringBuffer ph = ImageUtils.getPhotos(type, id, lang);
			if (ph != null)
				hInfo.put("photos", ph.toString());
		}
		if (lastUpdate != null)
			request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return HtmlUtils.writeInfoHeader(hInfo, lang);
	}

	public static StringBuffer getRecordRef(HttpServletRequest request, List<Object> params, Collection<?> coll, boolean isExport, Contributor m, String lang) throws Exception {
		String etype = String.valueOf(params.get(0));
		Integer itemId = (Integer) params.get(1);
		boolean isAllRef = (isExport || !StringUtils.notEmpty(params.get(2)));
		final int ITEM_LIMIT = Integer.parseInt(ConfigUtils.getValue("default_ref_limit"));
		String limit = (params.size() > 3 ? String.valueOf(params.get(3)) : String.valueOf(ITEM_LIMIT));
		Integer offset = (params.size() > 4 ? StringUtils.toInt(params.get(4)) : 0);
		boolean isPRTMCN = etype.matches(Athlete.alias + "|" + Team.alias + "|" + Country.alias);
		StringBuffer html = new StringBuffer();

		// Get linked entities
		ArrayList<Integer> eList = new ArrayList<Integer>();
		if (etype.matches(Athlete.alias + "|" + Team.alias)) {
			Integer itemLink = -1;
			if (etype.equals(Athlete.alias)) {
				Athlete a = (Athlete) DatabaseManager.loadEntity(Athlete.class, itemId);
				itemLink = (a.getLink() != null && a.getLink() > 0 ? a.getLink() : -1);
			}
			else if (etype.equals(Team.alias)) {
				Team t = (Team) DatabaseManager.loadEntity(Team.class, itemId);
				itemLink = (t.getLink() != null && t.getLink() > 0 ? t.getLink() : -1);
			}
			String sql = "SELECT id FROM " + (etype.equals(Athlete.alias) ? "athlete" : "team") + " WHERE id IN (" + itemId + ", " + itemLink + ") OR link IN (" + itemId + ", " + itemLink + ")";
			for (Integer id_ : (List<Integer>) DatabaseManager.executeSelect(sql, Arrays.asList(), Integer.class))
				eList.add(id_);
		}
		else
			eList.add(itemId);
		
		// Write items
		String currentEntity = "";
		int colspan = 0;
		int count = 0;
		StringBuffer summary = new StringBuffer();
		if (isAllRef) {
			summary.append("<a href='#Header'>1. " + ResourceUtils.getText("header", lang) + "</a><br/>");
		}
		int ns = 1;
		final String MORE_ITEMS = "<tr class='moreitems'#STYLE#><td colspan='#COLSPAN#'><div class='sfdiv1' onclick='moreItems(this, \"#P1#\");'> (+" + ITEM_LIMIT + ") </div><div class='sfdiv2' onclick='moreItems(this, \"#P2#\");'> (+100) </div><div class='sfdiv3' onclick='moreItems(this, \"#P3#\");'> (" + ResourceUtils.getText("all", lang) + ") </div></td></tr>";
		String c1 = null, c2 = null, c3 = null, c4 = null, c5 = null, c6 = null, c7 = null;
		String c6_id = null;
		Timestamp lastUpdate = null;
		for (Object obj : coll) {
			RefItem item = (RefItem) obj;
			if (lastUpdate == null || item.getDate3().compareTo(lastUpdate) > 0) {
				lastUpdate = item.getDate3();
			}
			String en = item.getEntity();
			if (!en.equals(currentEntity)) {
				String id = en + "-" + System.currentTimeMillis();
				StringBuffer cols = new StringBuffer();
				String tableName = ResourceUtils.getText("entity." + en, lang);
				if (en.equals(Athlete.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("country", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("sport", lang) + "</th>");
				}
				else if (en.equals(City.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("country", lang) + "</th>");
				}
				else if (en.equals(Complex.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("city", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("country", lang) + "</th>");
				}
				else if (en.equals(Contribution.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("year", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("sport", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("event", lang)  + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("type", lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("date", lang) + "</th>");
				}
				else if (en.equals(HallOfFame.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("league", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>Year</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("inducted.as", lang) + "</th>");
				}
				else if (en.equals(Olympics.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'></th><th onclick='sort(\"" + id + "\", this, 1);'>Type</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("year", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("city", lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("country", lang) + "</th>");
				}
				else if (en.equals(OlympicRanking.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("entity.OL", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("country", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ImageUtils.getGoldHeader(lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ImageUtils.getSilverHeader(lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ImageUtils.getBronzeHeader(lang) + "</th><th onclick='sort(\"" + id + "\", this, 5);'>" + ResourceUtils.getText("total", lang) + "</th>");
				}
				else if (en.equals(Record.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("sport", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("category", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("scope", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("type", lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("record2", lang) + "</th>");				
				}
				else if (en.equals(Result.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("sport", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("event", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("year", lang)  + "</th>" + (isPRTMCN ? "<th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("rank", lang) + "</th>" : "") + "<th onclick='sort(\"" + id + "\", this, " + (isPRTMCN ? 4 : 3) + ");'>" + ResourceUtils.getText("entity.RS.1", lang) + "</th><th onclick='sort(\"" + id + "\", this, " + (isPRTMCN ? 5 : 4) + ");'>" + ResourceUtils.getText("date", lang) + "</th>");
					tableName = ResourceUtils.getText("final.results", lang);
				}
				else if (en.equals(RetiredNumber.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("league", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("team", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("number", lang) + "</th>");
				}
				else if (en.equals(Round.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("sport", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("event", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("year", lang)  + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("description", lang)  + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("entity.RS.1", lang) + "</th><th onclick='sort(\"" + id + "\", this, 5);'>" + ResourceUtils.getText("date", lang) + "</th>");
					tableName = ResourceUtils.getText("entity.RS", lang);
				}
				else if (en.equals(Team.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("country", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("sport", lang) + "</th>");
				}
				else if (en.equals(TeamStadium.alias)) {
					cols.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("league", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("team", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("complex", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("city", lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("state", lang) + "</th><th onclick='sort(\"" + id + "\", this, 5);'>Country</th><th onclick='sort(\"" + id + "\", this, 6);'>" + ResourceUtils.getText("timespan", lang) + "</th>");
				}
				if (!isExport && limit != null && !limit.equalsIgnoreCase("ALL") && count >= Integer.parseInt(limit)) {
					String p = params.get(0) + "-" + params.get(1) + "-" + currentEntity + "-#LIMIT#-" + (offset + (!limit.equalsIgnoreCase("all") ? Integer.parseInt(limit) : 0));
					html.append(MORE_ITEMS.replaceAll("#STYLE#", "").replaceAll("#P1#", StringUtils.encode(p.replaceAll("#LIMIT#", String.valueOf(ITEM_LIMIT)))).replaceAll("#P2#", StringUtils.encode(p.replaceAll("#LIMIT#", "100"))).replaceAll("#P3#", StringUtils.encode(p.replaceAll("#LIMIT#", "ALL"))).replaceAll("#COLSPAN#", String.valueOf(colspan)));
				}
				colspan = StringUtils.countIn(cols.toString(), "<th");
				html.append(StringUtils.notEmpty(currentEntity) ? "</tbody></table></div>" : "");
				count = 0;
				summary.append("<a href='#" + tableName.replaceAll("\\s|\\/", "_") + "'>" + ++ns + ". " + tableName + "</a><br/>");
				if (isAllRef) {
					html.append("<div id='" + tableName.replaceAll("\\s|\\/", "_") + "' class='chaptertitle'>" + HtmlUtils.writeToggleTitle(tableName, false) + "</div>");
					html.append("<div class='mobile100'><table class='tsort'>");
					html.append("<thead>");
					if (cols.length() > 0) {
						html.append("<tr class='rsort'>" + cols.toString() + "</tr>");
					}
					html.append("</thead><tbody class='tby' id='tb-" + id + "'>");
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
				if (StringUtils.notEmpty(item.getTxt3())) {
					List<Integer> lId = new ArrayList<Integer>();
					lId.add(item.getIdRel1());
					for (String s : item.getTxt3().split("\\|")) {
						String[] t = s.split("\\,", -1);
						if (!lId.contains(Integer.parseInt(t[0]))) {
							c2 += (item.getIdRel1() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, Integer.parseInt(t[0]), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Country.alias, Integer.parseInt(t[0]), lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? t[1] : t[2], t[1])) : StringUtils.EMPTY);
							lId.add(Integer.parseInt(t[0]));
						}
					}
				}
				c3 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, item.getIdRel2(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel4()));
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
				c1 = HtmlUtils.writeLink(Year.alias, item.getIdRel1(), item.getLabelRel1() + (m != null && m.isSport(item.getIdRel2()) ? " <a href='" + HtmlUtils.writeURL("/update/results", "RS-" + item.getIdItem(), null) + "'><img alt='modify' title='" + ResourceUtils.getText("button.modify", lang) + "' src='/img/component/button/modify.png'/></a>" : ""), null);
				c2 = HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel6());
				c3 = "<a href='" + HtmlUtils.writeURL("/results", item.getIdRel2() + "-" + item.getIdRel3() + "-" + item.getIdRel4() + (item.getIdRel5() != null ? "-" + item.getIdRel5() : "") + (item.getIdRel18() != null ? "-" + item.getIdRel18() : ""), item.getLabelRel6() + "/" + item.getLabelRel7() + "/" + item.getLabelRel8() + (item.getIdRel5() != null ? "/" + item.getLabelRel9() : "") + (item.getIdRel18() != null ? "/" + item.getLabelRel10() : "")) + "'>" + (item.getLabelRel3() + " " + StringUtils.SEP1 + " " + item.getLabelRel4() + (item.getIdRel5() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel5() : "") + (item.getIdRel18() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel18() : "")) + "</a>";
				if (item.getTxt1().equals("A"))
					c4 = "<img style='vertical-align:middle;padding-bottom:2px;' alt='add' src='/img/home/adds.png'/> " + ResourceUtils.getText("co.add", lang);
				else
					c4 = "<img style='vertical-align:middle;padding-bottom:2px;' alt='update' src='/img/home/updates.png'/> " + ResourceUtils.getText("co.update", lang);
				c5 = StringUtils.toTextDate(item.getDate1(), lang, "d MMMM yyyy HH:mm");
			}
			else if (en.equals(Event.alias)) {
				boolean inactive = (item.getIdRel2().equals(item.getIdRel6()) && item.getIdRel3().equals(item.getIdRel7()) && (item.getIdRel4() == null || item.getIdRel4().equals(item.getIdRel8())) && (item.getIdRel5() == null || item.getIdRel5().equals(item.getIdRel9())));
				String path1 = item.getLabelRel1() + " " + StringUtils.SEP1 + " " + item.getLabelRel3() + " " + StringUtils.SEP1 + " " + item.getLabelRel5() + (item.getIdRel4() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel7() : "") + (item.getIdRel5() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel9() : "");
				String path2 = item.getLabelRel2() + "/" + item.getLabelRel4() + "/" + item.getLabelRel6() + (item.getIdRel4() != null ? "/" + item.getLabelRel8() : "") + (item.getIdRel5() != null ? "/" + item.getLabelRel10() : "");
				if (inactive)
					path1 = "<span style=\"color:#666;\">&dagger; <i>" + path1 + "</i></span>";
				c1 = "<a href='" + HtmlUtils.writeURL("/results", item.getIdRel1() + "-" + item.getIdRel2() + "-" + item.getIdRel3() + (item.getIdRel4() != null ? "-" + item.getIdRel4() : "") + (item.getIdRel5() != null ? "-" + item.getIdRel5() : ""), path2) + "'>" + path1 + "</a>";
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
						sbPos.append((!sbPos.toString().isEmpty() ? " / " : "") + StringUtils.getUSPosition(item.getIdRel3(), s));
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
				c1 = HtmlUtils.writeLink(Year.alias, item.getIdRel2(), item.getLabelRel2(), null) + " " + HtmlUtils.writeLink(City.alias, item.getIdRel3(), item.getLabelRel3(), item.getLabelRel5());
				c2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel4(), ImageUtils.SIZE_SMALL, item.getLabelRel2(), null), HtmlUtils.writeLink(Country.alias, item.getIdRel4(), item.getLabelRel4(), item.getLabelRel6()));
				c3 = t[0];
				c4 = t[1];
				c5 = t[2];
				c6 = String.valueOf(Integer.parseInt(t[0]) + Integer.parseInt(t[1]) + Integer.parseInt(t[2]));
			}
			else if (en.equals(Record.alias)) {
				Integer lgId = (item.getIdRel2() != null ? (item.getIdRel2() == 51 ? 1 : (item.getIdRel2() == 54 ? 2 : (item.getIdRel2() == 55 ? 3 : 4))) : 0);
				String url = HtmlUtils.writeURL("/usleagues", USLeaguesServlet.TYPE_RECORD + "-" + lgId + "-" + item.getIdRel2() + "-" + (item.getIdRel3() != null && item.getIdRel3() == 496 ? "1" : "0") + "-" + item.getIdRel4() + "-" + (item.getTxt1() != null ? item.getTxt1().toLowerCase().charAt(0) : "it") + "-0-" + lang, USLeaguesServlet.TYPE_RECORD + "/" + item.getLabelRel6() + "/" + item.getLabelRel7() + "/" + item.getLabelRel8());
				c1 = HtmlUtils.writeLink(Sport.alias, item.getIdRel1(), item.getLabelRel1(), item.getLabelRel5());
				c2 = "<a href='" + url + "'>" + item.getLabelRel2() + " " + StringUtils.SEP1 + " " + item.getLabelRel3() + " " + StringUtils.SEP1 + " " + item.getLabelRel4() + "</a>";
				c3 = (item.getTxt2() != null ? item.getTxt2() : "-");
				c4 = (item.getTxt1() != null ? item.getTxt1() : "-");
				c5 = item.getLabel() + " " + StringUtils.SEP1 + " <b>" + item.getTxt3() + "</b>";
			}
			else if (en.equals(Result.alias)) {
				Integer idResult = item.getIdItem();
				String path = item.getLabelRel1() + "/" + item.getLabelRel12() + "/" + item.getLabelRel13() + "/" + item.getLabelRel14() + (item.getIdRel5() != null ? "/" + item.getLabelRel15() : "") + (item.getIdRel18() != null ? "/" + item.getLabelRel16() : "");
				c1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, item.getIdRel2(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel12()));
				c2 = "<a href='" + HtmlUtils.writeURL("/results", item.getIdRel2() + "-" + item.getIdRel3() + "-" + item.getIdRel4() + (item.getIdRel5() != null ? "-" + item.getIdRel5() : "") + (item.getIdRel18() != null ? "-" + item.getIdRel18() : ""), item.getLabelRel12() + "/" + item.getLabelRel13() + "/" + item.getLabelRel14() + (item.getIdRel5() != null ? "/" + item.getLabelRel15() : "") + (item.getIdRel18() != null ? "/" + item.getLabelRel16() : "")) + "'>" + (item.getLabelRel3() + " " + StringUtils.SEP1 + " " + item.getLabelRel4() + (item.getIdRel5() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel5() : "") + (item.getIdRel18() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel18() : "")) + "</a>";
				String alias = item.getComment();
				boolean isMedal = String.valueOf(item.getIdRel3()).matches(ConfigUtils.getValue("cp_medal_pattern"));
				String[] tEntity = new String[6];
				String title1 = (item.getLabelRel6() != null ? item.getLabelRel6().replaceAll(".*\\|", "") : null);
				String title2 = (item.getLabelRel7() != null ? item.getLabelRel7().replaceAll(".*\\|", "") : null);
				String title3 = (item.getLabelRel8() != null ? item.getLabelRel8().replaceAll(".*\\|", "") : null);
				String title4 = (item.getLabelRel9() != null ? item.getLabelRel9().replaceAll(".*\\|", "") : null);
				String title5 = (item.getLabelRel10() != null ? item.getLabelRel10().replaceAll(".*\\|", "") : null);
				String title6 = (item.getLabelRel11() != null ? item.getLabelRel11().replaceAll(".*\\|", "") : null);
		
				tEntity[0] = (item.getIdRel6() != null ? HtmlUtils.writeLink(alias, item.getIdRel6(), StringUtils.getShortName(item.getLabelRel6()), item.getLabelRel20()) : null);
				tEntity[1] = (item.getIdRel7() != null ? HtmlUtils.writeLink(alias, item.getIdRel7(), StringUtils.getShortName(item.getLabelRel7()), item.getLabelRel21()) : null);
				tEntity[2] = (item.getIdRel8() != null ? HtmlUtils.writeLink(alias, item.getIdRel8(), StringUtils.getShortName(item.getLabelRel8()), item.getLabelRel22()) : null);
				tEntity[3] = (item.getIdRel9() != null ? HtmlUtils.writeLink(alias, item.getIdRel9(), StringUtils.getShortName(item.getLabelRel9()), item.getLabelRel23()) : null);
				tEntity[4] = (item.getIdRel10() != null ? HtmlUtils.writeLink(alias, item.getIdRel10(), StringUtils.getShortName(item.getLabelRel10()), item.getLabelRel24()) : null);
				tEntity[5] = (item.getIdRel11() != null ? HtmlUtils.writeLink(alias, item.getIdRel11(), StringUtils.getShortName(item.getLabelRel11()), item.getLabelRel25()) : null);
				short index = (alias.equals(Athlete.alias) || alias.equals(Country.alias) ? ImageUtils.INDEX_COUNTRY : ImageUtils.INDEX_TEAM);
				tEntity[0] = (tEntity[0] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel12() != null ? item.getIdRel12() : item.getIdRel6(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), title1), tEntity[0]) : null);
				tEntity[1] = (tEntity[1] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel13() != null ? item.getIdRel13() : item.getIdRel7(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), title2), tEntity[1]) : null);
				tEntity[2] = (tEntity[2] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel14() != null ? item.getIdRel14() : item.getIdRel8(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), title3), tEntity[2]) : null);
				tEntity[3] = (tEntity[3] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel15() != null ? item.getIdRel15() : item.getIdRel9(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), title4), tEntity[3]) : null);
				tEntity[4] = (tEntity[4] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel16() != null ? item.getIdRel16() : item.getIdRel10(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), title5), tEntity[4]) : null);
				tEntity[5] = (tEntity[5] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel17() != null ? item.getIdRel17() : item.getIdRel11(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), title6), tEntity[5]) : null);
				// Highlight entity
				if (item.getIdRel6() != null && (eList.contains(item.getIdRel6()) || (item.getCount1() != null && item.getCount1() == 1)))
					tEntity[0] = tEntity[0].replaceFirst("table", "table class='highlight'");
				if (item.getIdRel7() != null && (eList.contains(item.getIdRel7()) || (item.getCount1() != null && item.getCount1() == 2)))
					tEntity[1] = tEntity[1].replaceFirst("table", "table class='highlight'");
				if (item.getIdRel8() != null && (eList.contains(item.getIdRel8()) || (item.getCount1() != null && item.getCount1() == 3)))
					tEntity[2] = tEntity[2].replaceFirst("table", "table class='highlight'");
				if (item.getIdRel9() != null && (eList.contains(item.getIdRel9()) || (item.getCount1() != null && item.getCount1() == 4)))
					tEntity[3] = tEntity[3].replaceFirst("table", "table class='highlight'");
				if (item.getIdRel10() != null && (eList.contains(item.getIdRel10()) || (item.getCount1() != null && item.getCount1() == 5)))
					tEntity[4] = tEntity[4].replaceFirst("table", "table class='highlight'");
				if (item.getIdRel11() != null && (eList.contains(item.getIdRel11()) || (item.getCount1() != null && item.getCount1() == 6)))
					tEntity[5] = tEntity[5].replaceFirst("table", "table class='highlight'");
				// Manage ties
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
									tEntity[idx] = "<table><tr><td>" + tEntity[idx] + "</td><td> / </td><td>" + tEntity[tieList.get(i) - 1] + "</td></tr></table>";
									tEntity[tieList.get(i) - 1] = null;
								}
							}
						}
					}
					tEntity = StringUtils.removeNulls(tEntity);
				}
				boolean isScore = (!isMedal && StringUtils.notEmpty(tEntity[0]) && StringUtils.notEmpty(tEntity[1]) && StringUtils.notEmpty(item.getTxt1()) && !StringUtils.notEmpty(item.getTxt2()) && !StringUtils.notEmpty(item.getTxt5()));
				String p1 = (StringUtils.notEmpty(tEntity[1]) ? "<td>1.&nbsp;</td>" : "");
				String p2 = "<td>&nbsp;" + (item.getTxt4() != null && item.getTxt4().startsWith("1") ? "3" : "2") + ".&nbsp;</td>";
				String p3 = "<td>&nbsp;3.&nbsp;</td>";
				if (isMedal) {
					p1 = "<td>" + ImageUtils.getGoldMedImg(lang) + "</td>";
					p2 = "<td> " + ImageUtils.getSilverMedImg(lang) + "</td>";
					p3 = "<td> " + ImageUtils.getBronzeMedImg(lang) + "</td>";
				}
				if (isScore) {
					p1 = "";
					p2 = "";
					p3 = "";
				}
				if (isPRTMCN && item.getCount1() != null) {
					String rk = String.valueOf(item.getCount1());
					if (item.getCount1() == 1)
						rk = "<b>" + rk + "</b>";
					if (isMedal && item.getCount1() <= 3)
						c4 = "<div class='medal " + (item.getCount1() == 1 ? "gold" : (item.getCount1() == 2 ? "silver" : "bronze")) + "'>" + rk + "</div>";
					else
						c4 = rk;			
				}
				if (!isPRTMCN) {
					c4 = c5;
					c5 = null;
				}
				c3 = HtmlUtils.writeLink(Year.alias, item.getIdRel1(), item.getLabelRel1(), null);
				c5 = "<span class='details'>" + HtmlUtils.writeLink(Result.alias, idResult, "<img alt='details' title='" +  ResourceUtils.getText("details", lang) + "' src='/img/render/details.png'/>", path) + "</span><table style='margin-right:20px;'><tr>" + p1 + "<td>" + (StringUtils.notEmpty(tEntity[0]) ? tEntity[0] : "-") + "</td>" + (isScore ? "<td style='padding-left:3px;padding-right:3px;'>" + StringUtils.formatResult(item.getTxt1(), lang) + "</td>" : "") + (StringUtils.notEmpty(tEntity[1]) ? p2 + "<td>" + tEntity[1] + "</td>" : "") + (StringUtils.notEmpty(tEntity[2]) && !isScore ? p3 + "<td>" + tEntity[2] + "</td>" : "") + "</tr></table>";
				c6 = (StringUtils.notEmpty(item.getDate2()) ? HtmlUtils.writeDateLink(null, item.getDate2(), StringUtils.toTextDate(item.getDate2(), lang, "d MMM yyyy")) : "");
				c6_id = (StringUtils.notEmpty(item.getDate2()) ? "dt-" + new SimpleDateFormat("yyyyMMdd").format(item.getDate2()) + "-" + item.getIdItem() : null);
			}
			else if (en.equals(RetiredNumber.alias)) {
				Short cp = USLeaguesServlet.HLEAGUES.get(Short.valueOf(item.getIdRel3().toString()));
				c1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, cp, ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Championship.alias, cp, item.getComment(), null));
				c2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdRel1(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Team.alias, item.getIdRel1(), item.getLabelRel1(), null));
				c3 = HtmlUtils.writeLink(Athlete.alias, item.getIdRel2(), StringUtils.toFullName(item.getLabelRel2(), item.getLabelRel3(), null, true), item.getLabelRel3() + " " + item.getLabelRel2());
				c4 = String.valueOf(item.getIdRel4());
			}
			else if (en.equals(Round.alias)) {
				c1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, item.getIdRel2(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel12()));
				c2 = "<a href='" + HtmlUtils.writeURL("/results", item.getIdRel2() + "-" + item.getIdRel3() + "-" + item.getIdRel4() + (item.getIdRel5() != null ? "-" + item.getIdRel5() : "") + (item.getIdRel18() != null ? "-" + item.getIdRel18() : ""), item.getLabelRel12() + "/" + item.getLabelRel13() + "/" + item.getLabelRel14() + (item.getIdRel5() != null ? "/" + item.getLabelRel15() : "") + (item.getIdRel18() != null ? "/" + item.getLabelRel16() : "")) + "'>" + (item.getLabelRel3() + " " + StringUtils.SEP1 + " " + item.getLabelRel4() + (item.getIdRel5() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel5() : "") + (item.getIdRel18() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel18() : "")) + "</a>";
				String alias = item.getComment();
				String[] tEntity = new String[6];
				tEntity[0] = (item.getIdRel6() != null ? HtmlUtils.writeLink(alias, item.getIdRel6(), StringUtils.getShortName(item.getLabelRel6()), item.getLabelRel20()) : null);
				tEntity[1] = (item.getIdRel7() != null ? HtmlUtils.writeLink(alias, item.getIdRel7(), StringUtils.getShortName(item.getLabelRel7()), item.getLabelRel21()) : null);
				tEntity[2] = (item.getIdRel8() != null ? HtmlUtils.writeLink(alias, item.getIdRel8(), StringUtils.getShortName(item.getLabelRel8()), item.getLabelRel22()) : null);
				short index = (alias.equals(Athlete.alias) || alias.equals(Country.alias) ? ImageUtils.INDEX_COUNTRY : ImageUtils.INDEX_TEAM);
				tEntity[0] = (tEntity[0] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel12() != null ? item.getIdRel12() : item.getIdRel6(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), null), tEntity[0]) : null);
				tEntity[1] = (tEntity[1] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel13() != null ? item.getIdRel13() : item.getIdRel7(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), null), tEntity[1]) : null);
				tEntity[2] = (tEntity[2] != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(index, alias.equals(Athlete.alias) && item.getIdRel14() != null ? item.getIdRel14() : item.getIdRel8(), ImageUtils.SIZE_SMALL, item.getLabelRel1(), null), tEntity[2]) : null);
				// Highlight entity
				if (item.getIdRel6() != null && eList.contains(item.getIdRel6()))
					tEntity[0] = tEntity[0].replaceFirst("table", "table class='highlight'");
				if (item.getIdRel7() != null && eList.contains(item.getIdRel7()))
					tEntity[1] = tEntity[1].replaceFirst("table", "table class='highlight'");
				if (item.getIdRel8() != null && eList.contains(item.getIdRel8()))
					tEntity[2] = tEntity[2].replaceFirst("table", "table class='highlight'");
				c3 = HtmlUtils.writeLink(Year.alias, item.getIdRel1(), item.getLabelRel1(), null);
				c4 = item.getLabel();
				c5 = "<table><tr><td style='padding-right:3px;'>" + tEntity[0] + "</td><td style='padding-right:3px;'>" + StringUtils.formatResult(item.getTxt1(), lang) + " </td>" + (StringUtils.notEmpty(tEntity[1]) ? "<td style='padding-right:3px;'>" + tEntity[1] + " </td>" : "") + (StringUtils.notEmpty(tEntity[2]) ? "<td>" + tEntity[2] + "</td>" : "") + "</tr></table>";
				c6 = (StringUtils.notEmpty(item.getDate2()) ? HtmlUtils.writeDateLink(null, item.getDate2(), StringUtils.toTextDate(item.getDate2(), lang, "d MMM yyyy")) : "");
				c6_id = (StringUtils.notEmpty(item.getDate2()) ? "dt-" + new SimpleDateFormat("yyyyMMdd").format(item.getDate2()) + "-" + item.getIdItem() : null);
			}
			else if (en.equals(Team.alias)) {
				c1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdItem(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Team.alias, item.getIdItem(), item.getLabel(), null));
				c2 = (item.getIdRel1() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel1(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Country.alias, item.getIdRel1(), item.getLabelRel1(), item.getLabelRel3())) : StringUtils.EMPTY);
				c3 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, item.getIdRel2(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel4()));
			}
			else if (en.equals(TeamStadium.alias)) {
				Short cp = USLeaguesServlet.HLEAGUES.get(Short.valueOf(item.getIdRel6().toString()));
				c1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_CHAMPIONSHIP, cp, ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Championship.alias, cp, item.getComment(), null));
				c2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, item.getIdRel1(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Team.alias, item.getIdRel1(), item.getLabelRel1(), null));
				c3 = HtmlUtils.writeLink(Complex.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel6());
				c4 = HtmlUtils.writeLink(City.alias, item.getIdRel3(), item.getLabelRel3(), item.getLabelRel7());
				c5 = (item.getIdRel4() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_STATE, item.getIdRel4(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(State.alias, item.getIdRel4(), item.getLabelRel4(), item.getLabelRel8())) : StringUtils.EMPTY);
				c6 = (item.getIdRel5() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel5(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Country.alias, item.getIdRel5(), item.getLabelRel5(), item.getLabelRel9())) : StringUtils.EMPTY);
				c7 = item.getTxt1() + " " + StringUtils.SEP1 + " " + (StringUtils.notEmpty(item.getTxt2()) && !item.getTxt2().equals("0") ? item.getTxt2() : ResourceUtils.getText("today", lang));
			}
			if (isExport || !isAllRef || count < ITEM_LIMIT) {
				html.append("<tr>" + (c1 != null ? "<td class='srt'>" + c1 + "</td>" : "") + (c2 != null ? "<td class='srt'>" + c2 + "</td>" : ""));
				html.append(c3 != null ? "<td class='srt'>" + c3 + "</td>" : "");
				html.append(c4 != null ? "<td class='srt'" + (c4.contains("'highlight'") ? " style='background-color:" + HIGHLIGHT_COLOR + ";'" : "") + ">" + c4 + "</td>" : "");
				html.append(c5 != null ? "<td class='srt'" + (c5.contains("'highlight'") ? " style='background-color:" + HIGHLIGHT_COLOR + ";'" : "") + ">" + c5 + "</td>" : "");
				html.append(c6 != null ? "<td" + (c6_id != null ? " id='" + c6_id + "'" : "") + " class='srt'" + (c6.contains("'highlight'") ? " style='background-color:" + HIGHLIGHT_COLOR + ";'" : "") + ">" + c6 + "</td>" : "");
				html.append(c7 != null ? "<td class='srt'" + (c7.contains("'highlight'") ? " style='background-color:" + HIGHLIGHT_COLOR + ";'" : "") + ">" + c7 + "</td>" : "");
				html.append("</tr>");
			}
			count++;
		}
		if (!isExport && limit != null && !limit.equalsIgnoreCase("ALL") && count >= Integer.parseInt(limit)) {
			String p = params.get(0) + "-" + params.get(1) + "-" + currentEntity + "-#LIMIT#-" + (offset + (!limit.equalsIgnoreCase("all") ? Integer.parseInt(limit) : 0));
			html.append(MORE_ITEMS.replaceAll("#STYLE#", "").replaceAll("#P1#", StringUtils.encode(p.replaceAll("#LIMIT#", String.valueOf(ITEM_LIMIT)))).replaceAll("#P2#", StringUtils.encode(p.replaceAll("#LIMIT#", "100"))).replaceAll("#P3#", StringUtils.encode(p.replaceAll("#LIMIT#", "ALL"))).replaceAll("#COLSPAN#", String.valueOf(colspan)));
		}
		if (isAllRef) {
			html.append("</tbody></table></div>");
			if (!isExport)
				html.append("<div id='summary'>" + summary.toString() + "</div>");	
		}
		request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return html;
	}

	public static StringBuffer convertResults(HttpServletRequest request, Collection<?> coll, Championship cp, Event ev, Contributor m, String lang) throws Exception {
		if (coll == null || coll.isEmpty()) {
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		}
		final int MAX_RANKS = 3;
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
		boolean isMedal = String.valueOf(cp.getId()).matches(ConfigUtils.getValue("cp_medal_pattern"));
		boolean isUSLeague = String.valueOf(cp.getId()).matches(USLeaguesServlet.CHAMPIONSHIP_NFL + "|" + USLeaguesServlet.CHAMPIONSHIP_NBA + "|" + USLeaguesServlet.CHAMPIONSHIP_NHL + "|" + USLeaguesServlet.CHAMPIONSHIP_MLB);
		Timestamp lastUpdate = null;
		List<String> lIds = new ArrayList<String>();
		Result rs = null;
		// Evaluate columns
		for (Object obj : coll) {
			ResultsBean bean = (ResultsBean) obj;
			if (lastUpdate == null || bean.getRsLastUpdate().compareTo(lastUpdate) > 0)
				lastUpdate = bean.getRsLastUpdate();
			lIds.add(String.valueOf(bean.getRsId()));
			if (rs == null)
				rs = (Result) DatabaseManager.loadEntity(Result.class, bean.getRsId());
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
			tIsEntityRel1[0] |= (bean.getEn1Rel1Id() != null);tIsEntityRel2[0] |= (bean.getEn1Rel2Id() != null && !isUSLeague);
			tIsEntityRel1[1] |= (bean.getEn2Rel1Id() != null);tIsEntityRel2[1] |= (bean.getEn2Rel2Id() != null && !isUSLeague);
			tIsEntityRel1[2] |= (bean.getEn3Rel1Id() != null);tIsEntityRel2[2] |= (bean.getEn3Rel2Id() != null && !isUSLeague);
			tIsEntityRel1[3] |= (bean.getEn4Rel1Id() != null);tIsEntityRel2[3] |= (bean.getEn4Rel2Id() != null && !isUSLeague);
			tIsEntityRel1[4] |= (bean.getEn5Rel1Id() != null);tIsEntityRel2[4] |= (bean.getEn5Rel2Id() != null && !isUSLeague);
			tIsEntityRel1[5] |= (bean.getEn6Rel1Id() != null);tIsEntityRel2[5] |= (bean.getEn6Rel2Id() != null && !isUSLeague);
			tIsEntityRel1[6] |= (bean.getEn7Rel1Id() != null);tIsEntityRel2[6] |= (bean.getEn7Rel2Id() != null && !isUSLeague);
			tIsEntityRel1[7] |= (bean.getEn8Rel1Id() != null);tIsEntityRel2[7] |= (bean.getEn8Rel2Id() != null && !isUSLeague);
			tIsEntityRel1[8] |= (bean.getEn9Rel1Id() != null);tIsEntityRel2[8] |= (bean.getEn9Rel2Id() != null && !isUSLeague);
			tIsResult[0] |= (StringUtils.notEmpty(bean.getRsResult1()));
			tIsResult[1] |= (StringUtils.notEmpty(bean.getRsResult2()));
			tIsResult[2] |= (StringUtils.notEmpty(bean.getRsResult3()));
			tIsResult[3] |= (StringUtils.notEmpty(bean.getRsResult4()));
			tIsResult[4] |= (StringUtils.notEmpty(bean.getRsResult5()));
			isDates |= StringUtils.notEmpty(bean.getRsDate2());
			isPlace |= (bean.getCx1Id() != null || bean.getCx2Id() != null || bean.getCt2Id() != null || bean.getCt4Id() != null || bean.getCn5Id() != null || bean.getCn6Id() != null);
			isComment |= (StringUtils.notEmpty(bean.getRsComment()) && !bean.getRsComment().matches("\\#(SINGLE|DOUBLE|TRIPLE)\\#") && bean.getRsRank1() != null);
		}
		entityCount = (entityCount > MAX_RANKS ? MAX_RANKS : entityCount);
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
		boolean isScore_ = isScore;
		String path = rs.getSport().getLabel() + "/" + rs.getChampionship().getLabel() + "/" + rs.getEvent().getLabel() + (rs.getSubevent() != null ? "/" + rs.getSubevent().getLabel() : "") + (rs.getSubevent2() != null ? "/" + rs.getSubevent2().getLabel() : "");

		long id = System.currentTimeMillis();
		Map<Integer, List<StringBuffer>> mpl = getPersonLists(StringUtils.join(lIds, ","), lang);
		StringBuffer html = new StringBuffer("<div class='mobile100'><table class='tsort'>");
		html.append("<thead><tr class='rsort'><th></th>" + (isComment ? "<th>" + ResourceUtils.getText("note", lang) + "</th>" : "") + "<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("year", lang) + "</th>");
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
			String details = HtmlUtils.writeLink(Result.alias, bean.getRsId(), "<img alt='details' title='" + ResourceUtils.getText("details", lang) + " (" + bean.getYrLabel() + ")' src='/img/render/details.png'/>" + ResourceUtils.getText("details", lang), bean.getYrLabel() + "/" + path) + (m != null && m.isSport(rs.getSport().getId()) ? " <a href='" + HtmlUtils.writeURL("/update/results", "RS-" + bean.getRsId(), null) + "'><img alt='modify' title='" + ResourceUtils.getText("button.modify", lang) + "' src='/img/component/button/modify.png'/></a>" : "");
			String year = HtmlUtils.writeLink(Year.alias, bean.getYrId(), bean.getYrLabel(), null);
			String d1 = bean.getRsDate1();
			String d2 = bean.getRsDate2();
			String dates = "";
			if (StringUtils.notEmpty(d1) && StringUtils.notEmpty(d2)) {
				if (d1.substring(3).equals(d2.substring(3)))
					dates = HtmlUtils.writeDateLink(d1, d2, d1.substring(0, 2).replaceFirst("^0", "") + StringUtils.SEP1 + StringUtils.toTextDate(d2, lang, "d MMM"));
				else
					dates = HtmlUtils.writeDateLink(d1, d2, StringUtils.toTextDate(d1, lang, "d MMM") + StringUtils.SEP1 + StringUtils.toTextDate(d2, lang, "d MMM"));
			}
			else if (StringUtils.notEmpty(d2))
				dates = HtmlUtils.writeDateLink(null, d2, StringUtils.toTextDate(d2, lang, "d MMM"));
			d2 = (StringUtils.notEmpty(d2) ? StringUtils.toTextDate(d2.replaceFirst("\\d\\d\\d\\d$", "1900"), lang, "yyyyMMdd") : "");
			String place1 = null, place2 = null;
			String comment = StringUtils.getComment(bean.getRsComment(), lang);
			boolean isResultEmpty = (bean.getRsRank1() == null && bean.getRsRank2() == null && bean.getRsRank3() == null && bean.getRsRank4() == null && bean.getRsRank5() == null);
			isScore = (entityCount > 1 && StringUtils.notEmpty(bean.getRsResult1()) && !StringUtils.notEmpty(bean.getRsResult2()) && !StringUtils.notEmpty(bean.getRsResult3()) && !StringUtils.notEmpty(bean.getRsResult4()) && !StringUtils.notEmpty(bean.getRsResult5()));
			List<StringBuffer> plist = mpl.get(bean.getRsId());
			if (bean.getCx1Id() != null)
				place1 = getPlace(bean.getCx1Id(), bean.getCt1Id(), bean.getSt1Id(), bean.getCn1Id(), bean.getCx1Label(), bean.getCt1Label(), bean.getSt1Code(), bean.getCn1Code(), bean.getCx1Label(), bean.getCt1LabelEN(), bean.getSt1LabelEN(), bean.getCn1LabelEN(), bean.getYrLabel());
			else if (bean.getCt2Id() != null) 
				place1 = getPlace(null, bean.getCt2Id(), bean.getSt2Id(), bean.getCn2Id(), null, bean.getCt2Label(), bean.getSt2Code(), bean.getCn2Code(), null, bean.getCt2LabelEN(), bean.getSt2LabelEN(), bean.getCn2LabelEN(), bean.getYrLabel());
			else if (bean.getCn5Id() != null) 
				place1 = getPlace(null, null, null, bean.getCn5Id(), null, null, null, bean.getCn5Label(), null, null, null, bean.getCn5LabelEN(), bean.getYrLabel());
			if (bean.getCx2Id() != null)
				place2 = getPlace(bean.getCx2Id(), bean.getCt3Id(), bean.getSt3Id(), bean.getCn3Id(), bean.getCx2Label(), bean.getCt3Label(), bean.getSt3Code(), bean.getCn3Code(), bean.getCx2Label(), bean.getCt3LabelEN(), bean.getSt3LabelEN(), bean.getCn3LabelEN(), bean.getYrLabel());
			else if (bean.getCt4Id() != null)
				place2 = getPlace(null, bean.getCt4Id(), bean.getSt4Id(), bean.getCn4Id(), null, bean.getCt4Label(), bean.getSt4Code(), bean.getCn4Code(), null, bean.getCt4LabelEN(), bean.getSt4LabelEN(), bean.getCn4LabelEN(), bean.getYrLabel());				
			else if (bean.getCn6Id() != null)
				place2 = getPlace(null, null, null, bean.getCn6Id(), null, null, null, bean.getCn6Label(), null, null, null, bean.getCn6LabelEN(), bean.getYrLabel());
			String places = null;
			if (place1 != null)
				places = "<table><tr><td>" + place1 + "</td><td> - </td><td>" + place2 + "</td></tr></table>";
			else if (place2 != null)
				places = place2;
			boolean isSingle = (bean.getRsComment() != null && bean.getRsComment().equals("#SINGLE#"));
			boolean isTriple = (bean.getRsComment() != null && bean.getRsComment().equals("#TRIPLE#") ? true : (type == 5 && !isSingle));
			isDouble = (bean.getRsComment() != null && bean.getRsComment().equals("#DOUBLE#") ? true : (type == 4 && !isSingle));
			List<Integer> tieList = getTieList(isDouble, isTriple, bean.getRsExa());
			String[] tEntity = {null, null, null, null, null, null, null, null, null};
			String[] tEntityRel = {null, null, null, null, null, null, null, null, null};
			String[] tEntityHtml = {null, null, null, null, null, null, null, null, null};
			String[] tResult = {null, null, null, null, null, null, null, null, null};
			String[] tLN = {null, null, null};
			List<List<String>> lTies = new ArrayList<List<String>>();
			for (int i = 0 ; i < 9 ; i++)
				lTies.add(new ArrayList<String>());
			if (bean.getRsRank1() != null) {
				tEntity[0] = getResultsEntity(type, bean.getRsRank1(), bean.getEn1Str1(), bean.getEn1Str2(), bean.getEn1Str3(), bean.getEn1Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 0 ? "plist-" + bean.getRsId() + "-0" : null);
				tEntityRel[0] = getResultsEntityRel(bean.getEn1Rel1Id(), bean.getEn1Rel1Code(), bean.getEn1Rel1Label(), bean.getEn1Rel2Id(), bean.getEn1Rel2Code(), bean.getEn1Rel2Label(), bean.getEn1Rel2LabelEN(), tIsEntityRel1[0], tIsEntityRel2[0], bean.getYrLabel());
				tResult[0] = StringUtils.formatResult(bean.getRsResult1(), lang);
				tLN[0] = (type < 10 ? bean.getEn1Str1() + "-" + bean.getRsId() : null);
			}
			if (bean.getRsRank2() != null) {
				tEntity[1] = getResultsEntity(type, bean.getRsRank2(), bean.getEn2Str1(), bean.getEn2Str2(), bean.getEn2Str3(), bean.getEn2Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 1 ? "plist-" + bean.getRsId() + "-1" : null);
				tEntityRel[1] = getResultsEntityRel(bean.getEn2Rel1Id(), bean.getEn2Rel1Code(), bean.getEn2Rel1Label(), bean.getEn2Rel2Id(), bean.getEn2Rel2Code(), bean.getEn2Rel2Label(), bean.getEn2Rel2LabelEN(), tIsEntityRel1[1], tIsEntityRel2[1], bean.getYrLabel());
				tResult[1] = StringUtils.formatResult(bean.getRsResult2(), lang);
				tLN[1] = (type < 10 ? bean.getEn2Str1() + "-" + bean.getRsId() : null);
			}
			if (bean.getRsRank3() != null) {
				tEntity[2] = getResultsEntity(type, bean.getRsRank3(), bean.getEn3Str1(), bean.getEn3Str2(), bean.getEn3Str3(), bean.getEn3Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 2 ? "plist-" + bean.getRsId() + "-2" : null);
				tEntityRel[2] = getResultsEntityRel(bean.getEn3Rel1Id(), bean.getEn3Rel1Code(), bean.getEn3Rel1Label(), bean.getEn3Rel2Id(), bean.getEn3Rel2Code(), bean.getEn3Rel2Label(), bean.getEn3Rel2LabelEN(), tIsEntityRel1[2], tIsEntityRel2[2], bean.getYrLabel());
				tResult[2] = StringUtils.formatResult(bean.getRsResult3(), lang);
				tLN[2] = (type < 10 ? bean.getEn3Str1() + "-" + bean.getRsId() : null);
			}
			if (bean.getRsRank4() != null) {
				tEntity[3] = getResultsEntity(type, bean.getRsRank4(), bean.getEn4Str1(), bean.getEn4Str2(), bean.getEn4Str3(), bean.getEn4Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 3 ? "plist-" + bean.getRsId() + "-3" : null);
				tEntityRel[3] = getResultsEntityRel(bean.getEn3Rel1Id(), bean.getEn4Rel1Code(), bean.getEn4Rel1Label(), bean.getEn4Rel2Id(), bean.getEn4Rel2Code(), bean.getEn4Rel2Label(), bean.getEn4Rel2LabelEN(), tIsEntityRel1[3], tIsEntityRel2[3], bean.getYrLabel());
				tResult[3] = StringUtils.formatResult(bean.getRsResult4(), lang);
			}
			if (bean.getRsRank5() != null) {
				tEntity[4] = getResultsEntity(type, bean.getRsRank5(), bean.getEn5Str1(), bean.getEn5Str2(), bean.getEn5Str3(), bean.getEn5Rel2Code(), bean.getYrLabel(), plist != null && plist.size() > 4 ? "plist-" + bean.getRsId() + "-4" : null);
				tEntityRel[4] = getResultsEntityRel(bean.getEn5Rel1Id(), bean.getEn5Rel1Code(), bean.getEn5Rel1Label(), bean.getEn5Rel2Id(), bean.getEn5Rel2Code(), bean.getEn5Rel2Label(), bean.getEn5Rel2LabelEN(), tIsEntityRel1[4], tIsEntityRel2[4], bean.getYrLabel());
				tResult[4] = StringUtils.formatResult(bean.getRsResult5(), lang);
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
			int ties = setTies(tieList, type, tEntity, tEntityRel, lTies);
			String rspan = (ties > 1 ? " rowspan='" + ties + "'" : "");
			if (isTriple || isDouble) {
				tEntity = StringUtils.removeNulls(tEntity);
				tEntityRel = StringUtils.removeNulls(tEntityRel);
				for (int i = lTies.size() - 1 ; i >= 0 ; i--)
					if (lTies.get(i).isEmpty())
						lTies.remove(i);
			}
			for (int i = 0 ; i < MAX_RANKS ; i++)
				if (tEntity[i] != null) {
					String rspan_ = (lTies.get(i).isEmpty() ? rspan : "");
					String entityRel = tEntityRel[i];
					entityRel = (lTies.get(i).isEmpty() ? entityRel.replaceAll("\\<td", "<td" + rspan_) : entityRel);
					if (entityRel != null && i == 0)
						entityRel = entityRel.replaceAll("\\<td", "<td style='font-weight:bold;' ");
					tEntityHtml[i] = ("<td" + rspan_ + (i < 3 && StringUtils.notEmpty(tLN[i]) ? " id=\"" + tLN[i].replaceAll("\\s", "-") + "\"" : "") + " class='srt'" + (i == 0 ? " style='font-weight:bold;'" : "") + ">" + tEntity[i] + (plist != null && plist.size() > i ? "<table id='plist-" + bean.getRsId() + "-" + i + "' class='plist' style='display:none;'>" + plist.get(i).toString() + "</table>" : "")  + "</td>" + (StringUtils.notEmpty(tEntityRel[i]) ?  entityRel : (tIsEntityRel1[i] ? "<td" + rspan_ + "></td>" : "") + (tIsEntityRel2[i] ? "<td" + rspan_ + "></td>" : ""))) + (StringUtils.notEmpty(tResult[i]) ? "<td" + rspan + (isScore && i == 0 ? " class='centered nowrap'" : " class='nowrap'") + ">" + tResult[i] + "</td>" : (tIsResult[i] ? "<td" + rspan_ + "></td>" : ""));
				}
			String commentInfo = HtmlUtils.getCommentInfo(comment, lang);
			String commentColor = null;
			if (commentInfo != null && commentInfo.contains("|")) {
				String[] tComment = commentInfo.split("\\|", -1);
				commentInfo = tComment[1];
				commentColor = tComment[2];
			}
			
			// Write line
			html.append("<tr><td" + rspan + " class='nowrap'>" + details + "</td>");
			html.append(isComment ? "<td" + rspan + (StringUtils.notEmpty(commentColor) ? " style='width:15px;white-space:nowrap;background-color:" + commentColor + ";'" : "") + ">" + (StringUtils.notEmpty(commentInfo) && !isResultEmpty ? HtmlUtils.writeComment(bean.getRsId(), commentInfo) : "") + "</td>" : "");
			html.append("<td" + rspan + " class='srt'>" + year + "</td>");
			if (isResultEmpty && StringUtils.notEmpty(bean.getRsComment()))
				html.append("<td" + rspan + " colspan='" + (tColspan[0] + (entityCount > 1 ? tColspan[1] : 0) + (entityCount > 2 ? tColspan[2] : 0) + (isDates ? 1 : 0) + (isPlace ? 1 : 0) + (isScore_ ? 1 : 0)) + "'>" + bean.getRsComment().replaceAll("\r\n|\\|", "<br/>") + "</td></tr>");
			else {
				for (int i = 0 ; i < 9 ; i++)
					html.append(tEntityHtml[i] != null ? tEntityHtml[i] : (entityCount > i ? "<td" + rspan + " class='srt'" + (tColspan[i] > 1 ? " colspan='" + tColspan[i] + "'" : "") + ">" + StringUtils.EMPTY + "</td>" + (isScore && i == 0 ? "<td" + rspan + " class='srt'>" + StringUtils.EMPTY + "</td>" : "") : ""));
				html.append(isDates ? "<td" + rspan + " id='dt-" + d2 + "-" + bean.getRsId() + "' class='srt nowrap'>" + (StringUtils.notEmpty(dates) ? dates : "") + "</td>" : "");
				html.append((isPlace ? "<td" + rspan + " class='srt'>" + (StringUtils.notEmpty(places) ? places : "") + "</td>" : "") + "</tr>");				
			}
			if (ties > 1) {
				for (int i = 0 ; i < ties - 1 ; i++) {
					StringBuffer sb = new StringBuffer();
					for (int j = 0 ; j < 9 ; j++)
						if (lTies.size() > j) {
							if (lTies.get(j).size() > i)
								sb.append(lTies.get(j).get(i));
							else if (i > 0 && lTies.get(j).size() > 0)
								sb.append("<td colspan='" + tColspan[j] + "'></td>");
						}
					if (sb.length() > 0)
						html.append("<tr>").append(sb).append("</tr>");
				}
			}
		}
		html.append("</tbody></table></div>");
		html.append(getWinRecords(StringUtils.join(lIds, ","), lang));
		request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return html;
	}

	public static StringBuffer convertCalendarResults(HttpServletRequest request, Collection<RefItem> coll, Contributor m, String lang) throws Exception {
		if (coll == null || coll.isEmpty()) {
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		}
		Timestamp lastUpdate = null;
		String currentEntity = null;
		StringBuffer html = new StringBuffer();
		for (RefItem item : coll) {
			boolean isResult = item.getEntity().equals(Result.alias);
			if (!isResult) {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(item.getDate2().getTime());
				if (Calendar.getInstance().after(c)) {
					continue;
				}
			}
			if (currentEntity == null || !item.getEntity().equals(currentEntity)) {
				long id = System.currentTimeMillis();
				html.append("<div class='chaptertitle'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText((isResult ? "past" : "future") + ".events", lang), false) + "</div>");
				html.append("<div class='mobile100'><table class='tsort'>");
				html.append("<thead><tr class='rsort'>");
				if (item.getEntity().equals(Result.alias)) {
					html.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("sport", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("event", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("year", lang)  + "</th>" + "<th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("entity.RS.1", lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("date", lang) + "</th>");
				}
				else {
					html.append("<th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("sport", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("event", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("dates", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("place", lang) + "</th>");
				}
				html.append("</tr></thead><tbody class='tby' id='tb-" + id + "'>");
				currentEntity = item.getEntity();
			}
			if (lastUpdate == null || item.getDate3().compareTo(lastUpdate) > 0) {
				lastUpdate = item.getDate3();
			}
			String path = item.getLabelRel1() + "/" + item.getLabelRel12() + "/" + item.getLabelRel13() + "/" + item.getLabelRel14() + (item.getIdRel5() != null ? "/" + item.getLabelRel15() : "") + (item.getIdRel18() != null ? "/" + item.getLabelRel16() : "");
			String sport = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, item.getIdRel2(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Sport.alias, item.getIdRel2(), item.getLabelRel2(), item.getLabelRel12()));
			String event = "<a href='" + HtmlUtils.writeURL("/results", item.getIdRel2() + "-" + item.getIdRel3() + (item.getIdRel4() != null ? "-" + item.getIdRel4() : "") + (item.getIdRel5() != null ? "-" + item.getIdRel5() : "") + (item.getIdRel18() != null ? "-" + item.getIdRel18() : ""), item.getLabelRel12() + "/" + item.getLabelRel13() + (item.getIdRel4() != null ? "/" + item.getLabelRel14() : "") + (item.getIdRel5() != null ? "/" + item.getLabelRel15() : "") + (item.getIdRel18() != null ? "/" + item.getLabelRel16() : "")) + "'>" + (item.getLabelRel3() + (item.getIdRel4() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel4() : "") + (item.getIdRel5() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel5() : "") + (item.getIdRel18() != null ? " " + StringUtils.SEP1 + " " + item.getLabelRel18() : "")) + "</a>";
			String d1 = StringUtils.toTextDate(item.getDate1(), lang, "dd/MM/yyyy");
			String d2 = StringUtils.toTextDate(item.getDate2(), lang, "dd/MM/yyyy");
			String dates = "";
			if (StringUtils.notEmpty(d1) && StringUtils.notEmpty(d2)) {
				if (d1.substring(3).equals(d2.substring(3))) {
					dates = HtmlUtils.writeDateLink(d1, d2, d1.substring(0, 2).replaceFirst("^0", "") + StringUtils.SEP1 + StringUtils.toTextDate(d2, lang, "d MMMM"));
				}
				else {
					dates = HtmlUtils.writeDateLink(d1, d2, StringUtils.toTextDate(d1, lang, "d MMMM") + StringUtils.SEP1 + StringUtils.toTextDate(d2, lang, "d MMMM"));
				}
			}
			else if (StringUtils.notEmpty(d2)) {
				dates = HtmlUtils.writeDateLink(null, d2, StringUtils.toTextDate(d2, lang, "d MMMM"));
			}
			d2 = (StringUtils.notEmpty(d2) ? StringUtils.toTextDate(d2.replaceFirst("\\d\\d\\d\\d$", "1900"), lang, "yyyyMMdd") : "");
			String alias = item.getComment();
			if (isResult) { // Past events (final results+rounds)
				boolean isRound = StringUtils.notEmpty(item.getTxt5());
				String year = HtmlUtils.writeLink(Year.alias, item.getIdRel1(), item.getLabelRel1(), null);
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
				// Manage ties
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
									tEntity[idx] = tEntity[idx].concat("<td> / </td>").concat("<td>" + tEntity[tieList.get(i) - 1] + "</td>");
									tEntity[tieList.get(i) - 1] = null;
								}
							}
						}
					}
					tEntity = StringUtils.removeNulls(tEntity);
				}
				StringBuffer result = new StringBuffer(isRound ? "" : "<span class='details'>" + HtmlUtils.writeLink(Result.alias, item.getIdItem(), "<img alt='details' title='" +  ResourceUtils.getText("details", lang) + "' src='/img/render/details.png'/>", path) + "</span>");
				result.append("<table style='margin-right:20px;'><tr><td style='font-weight:bold;'>" + (StringUtils.notEmpty(tEntity[0]) ? tEntity[0] : "-") + "</td>");
				if (StringUtils.notEmpty(item.getTxt6()) && !StringUtils.notEmpty(item.getTxt2()))
					result.append("<td>&nbsp;" + StringUtils.formatResult(item.getTxt6(), lang) + "</td>");
				result.append("<td style='padding-left:3px;'>" + (StringUtils.notEmpty(tEntity[1]) ? tEntity[1] : "") + "</td>");
				result.append("<td style='padding-left:3px;'>" + (StringUtils.notEmpty(tEntity[2]) ? tEntity[2] : "") + "</td></tr></table>");
				html.append("<tr><td class='srt'>" + sport + "</td><td class='srt'>" + event + (isRound ? " (" + item.getTxt5() + ")" : "") + "</td>");
				html.append("<td class='srt'>" + year + "</td><td class='srt'>" + result.toString() + "</td><td id='dt-" + d2 + "-" + item.getIdItem() + "' class='srt'>" + dates + "</td></tr>");
			}
			else { // Future events
				String place = "";
				if (item.getIdRel6() != null) {
					place = HtmlUtils.writeLink(Complex.alias, item.getIdRel6(), item.getLabelRel6() + ", " + item.getLabelRel7(), item.getLabelRel9());
					place = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel10(), ImageUtils.SIZE_SMALL, null, null), place);
				}
				else if (item.getIdRel8() != null) {
					place = HtmlUtils.writeLink(City.alias, item.getIdRel8(), item.getLabelRel8(), item.getLabelRel11());
					place = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel11(), ImageUtils.SIZE_SMALL, null, null), place);
				}
				else if (item.getIdRel9() != null) {
					place = HtmlUtils.writeLink(Country.alias, item.getIdRel9(), item.getLabelRel19(), item.getLabelRel20());
					place = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, item.getIdRel9(), ImageUtils.SIZE_SMALL, null, null), place);
				}
				html.append("<tr><td class='srt'>" + sport + "</td><td class='srt'>" + event + "</td>");
				html.append("<td class='srt'>" + dates + "</td><td class='srt'>" + place + "</td></tr>");	
			}
		}
		html.append("</tbody></table></div>");
		request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return html;
	}
	
	public static void convertTreeArray(Collection<?> coll, Writer writer, boolean encode, String lang) throws IOException {
		writer.write("treeItems=[" + (!encode ? "['',null," : ""));
		List<Object> lst = new ArrayList<Object>(coll);
		int i, j, k, l, m;
		boolean isMonths = false;
		String yr = null;
		String mh = null;
		int[] tm = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		String calLink = null;
		for (i = 0 ; i < lst.size() ; i++) {
			TreeItem item = (TreeItem) lst.get(i);
			isMonths = (item.getLabel().matches("\\d{4}|\\d{2}\\/\\d{4}"));
			if (isMonths) {
				if (item.getIdItem() > 0) {
					yr = item.getLabel().substring(3);
					mh = (item.getIdItem() < 10 ? "0" : "") + item.getIdItem();
					item.setLabel(ResourceUtils.getText("month." + item.getIdItem(), lang) + " " + yr);
				}
				else {
					yr = item.getLabel();
					mh = null;
					item.setLabel("(" + ResourceUtils.getText("not.dated", lang) + ")");
				}
			}
			writer.write(i > 0 ? "," : "");
			writer.write("['" + StringUtils.toTree(item.getLabel()) + "','" + item.getIdItem() + "',");
			for (j = i + 1 ; j < lst.size() ; j++) {
				TreeItem item2 = (TreeItem) lst.get(j);
				if (item2.getLevel() < 2) {j--; break;}
				if (isMonths) {
					if (mh != null) {
						calLink = StringUtils.urlEscape(item2.getLabelEN()) + "/" + yr + "-" + mh + "-01/" + yr + "-" + mh + "-" + tm[item.getIdItem()] + "/" + StringUtils.encode(yr + mh + "01-" + yr + mh + tm[item.getIdItem()] + "-" + item2.getIdItem());
					}
					else {
						calLink = StringUtils.urlEscape(item2.getLabelEN()) + "/" + yr + "/" + StringUtils.encode(yr + "-0-" + item2.getIdItem());
					}
				}
				writer.write(j > i + 1 ? "," : "");
				writer.write("['" + StringUtils.toTree(item2.getLabel()) + "','" + (isMonths ? "calendar-" + calLink : (encode ? "link-" + StringUtils.urlEscape(item.getLabelEN() + "/" + item2.getLabelEN()) + "/" + StringUtils.encode(item.getIdItem() + "-" + item2.getIdItem()) : item.getIdItem() + "_" + item2.getIdItem())) + "',");
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

	public static StringBuffer convertSearch(HttpServletRequest request, Collection<?> coll, String pattern, String lang) throws Exception {
		if (coll == null || coll.isEmpty()) {
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		}
		StringBuffer html = new StringBuffer();
		String currentEntity = "";
		Timestamp lastUpdate = null;
		HashMap<String, Integer> hCount = new HashMap<String, Integer>();
		// Evaluate items
		for (Object obj : coll) {
			String s = ((RefItem) obj).getEntity();
			s = (s.equals(Championship.alias) ? Event.alias : s);
			if (!hCount.containsKey(s)) {
				hCount.put(s, 0);
			}
			hCount.put(s, hCount.get(s) + 1);
		}
		// Write items
		String img = null;
		String link = null;
		for (Object obj : coll) {
			RefItem item = (RefItem) obj;
			if (lastUpdate == null || item.getDate3().compareTo(lastUpdate) > 0) {
				lastUpdate = item.getDate3();
			}
			String en = item.getEntity();
			en = (en.equals(Championship.alias) ? Event.alias : en);
			if (!en.equals(currentEntity)) {
				String id = en + "-" + System.currentTimeMillis();
				int sortIndex = 0;
				StringBuffer cols = new StringBuffer("<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("name", lang) + "</th>");
				if (en.matches("PR")) {
					cols.append("<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("country", lang) + "</th><th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("sport", lang) + "</th>");				
				}
				else if (en.matches("TM")) {
					cols.append("<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("sport", lang) + "</th>");
				}
				else if (en.matches("CX|CT")) {
					cols.append((en.matches("CX") ? "<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("city", lang) + "</th>" : "") + "<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("country", lang) + "</th>");
				}
				html.append(StringUtils.notEmpty(currentEntity) ? "</tbody></table></div>" : "");
				html.append("<div class='chaptertitle'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity." + en, lang) + " (" + hCount.get(en) + ")", false) + "</div>");
				html.append("<div class='mobile100'><table class='tsort'>");
				html.append("<thead><tr class='rsort'>" + cols.toString() + "<th onclick='sort(\"" + id + "\", this, " + sortIndex++ + ");'>" + ResourceUtils.getText("references", lang) + "</th></tr></thead><tbody class='tby' id='tb-" + id + "'>");
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
					for (Athlete a : (List<Athlete>) DatabaseManager.executeSelect(Athlete.query + " WHERE T.id = ? or T.link = ? ORDER BY T.id", Arrays.asList(item.getIdItem(), item.getIdItem()), Athlete.class)) {
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
		html.append("</tbody></table></div>");
		request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return html;
	}

	public static StringBuffer convertOlympicMedals(HttpServletRequest request, Collection<?> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty()) {
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		}
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
		Timestamp lastUpdate = null;
		StringBuffer html = new StringBuffer("<div class='mobile100'><table class='tsort'>");
		html.append("<thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("entity.OL", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("event", lang) + "</th>");
		html.append("<th colspan='" + colspan + "' onclick='sort(\"" + id + "\", this, 2);'>" + ImageUtils.getGoldHeader(lang) + "</th>");
		html.append("<th colspan='" + colspan + "' onclick='sort(\"" + id + "\", this, 3);'>" + ImageUtils.getSilverHeader(lang) + "</th>");
		html.append("<th colspan='" + colspan + "' onclick='sort(\"" + id + "\", this, 4);'>" + ImageUtils.getBronzeHeader(lang) + "</th>");
		html.append("<th colspan='" + colspan + "' onclick='sort(\"" + id + "\", this, 5);'>" + ResourceUtils.getText("place", lang) + "</th>");
		html.append("</tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			OlympicMedalsBean bean = (OlympicMedalsBean) obj;
			if (lastUpdate == null || bean.getRsLastUpdate().compareTo(lastUpdate) > 0)
				lastUpdate = bean.getRsLastUpdate();
			boolean isIndividual_ = ((bean.getTp2Number() != null ? bean.getTp2Number() : bean.getTp1Number()) <= 10);
			if (!bean.getYrId().equals(currentOlympics)) {
				tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn1Id() != null ? bean.getCn1Id() : bean.getCn2Id(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
				olympics = HtmlUtils.writeLink(Olympics.alias, bean.getOlId(), bean.getYrLabel() + " " + bean.getOlCity(), bean.getYrLabel() + " " + bean.getOlCityEN());
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
					entityCn1 = getResultsEntityRel(null, null, null, bean.getPr1CnId(), bean.getPr1CnCode(), bean.getPr1CnLabel(), bean.getPr1CnLabelEN(), false, true, bean.getYrLabel());
				}
				else
					entity1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getRsRank1(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), HtmlUtils.writeLink(Country.alias, bean.getRsRank1(), bean.getCn1Code(), bean.getCn1LabelEN()));
			}
			if (bean.getRsRank2() != null) {
				if (isIndividual_) {
					entity2 = HtmlUtils.writeLink(Athlete.alias, bean.getRsRank2(), StringUtils.toFullName(bean.getPr2LastName(), bean.getPr2FirstName(), bean.getPr2CnCode(), true), bean.getPr2FirstName() + " " + bean.getPr2LastName());
					entityCn2 = getResultsEntityRel(null, null, null, bean.getPr2CnId(), bean.getPr2CnCode(), bean.getPr2CnLabel(), bean.getPr2CnLabelEN(), false, true, bean.getYrLabel());
				}
				else
					entity2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getRsRank2(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), HtmlUtils.writeLink(Country.alias, bean.getRsRank2(), bean.getCn2Code(), bean.getCn2LabelEN()));
			}
			if (bean.getRsRank3() != null) {
				if (isIndividual_) {
					entity3 = HtmlUtils.writeLink(Athlete.alias, bean.getRsRank3(), StringUtils.toFullName(bean.getPr3LastName(), bean.getPr3FirstName(), bean.getPr3CnCode(), true), bean.getPr3FirstName() + " " + bean.getPr3LastName());
					entityCn3 = getResultsEntityRel(null, null, null, bean.getPr3CnId(), bean.getPr3CnCode(), bean.getPr3CnLabel(), bean.getPr3CnLabelEN(), false, true, bean.getYrLabel());
				}
				else
					entity3 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getRsRank3(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), HtmlUtils.writeLink(Country.alias, bean.getRsRank3(), bean.getCn3Code(), bean.getCn3LabelEN()));
			}
			if (bean.getCxId() != null) {
				tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn1Id(), ImageUtils.SIZE_SMALL, null, null);				
				venue = HtmlUtils.writeLink(Complex.alias, bean.getCxId(), bean.getCxLabel(), bean.getCxLabel());
//				venue += ", " + HtmlUtils.writeLink(City.alias, bean.getCt1Id(), bean.getCt1Label(), bean.getCt1LabelEN());
//				if (bean.getSt1Id() != null)
//					venue += ", " + HtmlUtils.writeLink(State.alias, bean.getSt1Id(), bean.getSt1Code(), bean.getSt1LabelEN());
//				venue += ", " + HtmlUtils.writeLink(Country.alias, bean.getCn1Id(), bean.getCn1Code_(), bean.getCn1LabelEN_()); 
				venue = HtmlUtils.writeImgTable(tmpImg, venue);
			}
			String event = bean.getEvLabel() + (StringUtils.notEmpty(bean.getSeLabel()) ? " " + StringUtils.SEP1 + " " + bean.getSeLabel() : "");
			
			// Write line
			html.append("<tr><td class='srt'>" + olympics + "</td><td class='srt'><span class='nowrap'>" + event + "</span></td>");
			html.append(entity1 != null ? "<td class='srt'" + (!isIndividual_ && isIndividual ? " colspan='2'" : "") + " style='font-weight:bold;'>" + entity1 + "</td>" + (entityCn1 != null ? entityCn1 : "") + (isResult ? "<td>" + (bean.getRsResult1() != null ? StringUtils.formatResult(bean.getRsResult1(), lang) : "") + "</td>" : "") : "<td colspan='" + colspan + "'>" + StringUtils.EMPTY + "</td>");
			html.append(entity2 != null ? "<td class='srt'" + (!isIndividual_ && isIndividual ? " colspan='2'" : "") + ">" + entity2 + "</td>" + (entityCn2 != null ? entityCn2 : "") + (isResult ? "<td>" + (bean.getRsResult2() != null ? StringUtils.formatResult(bean.getRsResult2(), lang) : "") + "</td>" : "") : "<td colspan='" + colspan + "'>" + StringUtils.EMPTY + "</td>");
			html.append(entity3 != null ? "<td class='srt'" + (!isIndividual_ && isIndividual ? " colspan='2'" : "") + ">" + entity3 + "</td>" + (entityCn3 != null ? entityCn3 : "") + (isResult ? "<td>" + (bean.getRsResult3() != null ? StringUtils.formatResult(bean.getRsResult3(), lang) : "") + "</td>" : "") : "<td colspan='" + colspan + "'>" + StringUtils.EMPTY + "</td>");
			html.append("<td class='srt'>" + (StringUtils.notEmpty(venue) ? venue : "-") + "</td>");
			html.append("</tr>");
		}
		html.append("</tbody></table></div>");
		request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return html;
	}

	public static StringBuffer convertOlympicRankings(HttpServletRequest request, Collection<?> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty()) {
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		}
		String tmpImg = null;
		long id = System.currentTimeMillis();
		Timestamp lastUpdate = null;
		StringBuffer html = new StringBuffer("<div class='mobile100'><table class='tsort'>");
		html.append("<thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("country", lang) + "</th>");
		html.append("<th onclick='sort(\"" + id + "\", this, 1);'>" + ImageUtils.getGoldHeader(lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ImageUtils.getSilverHeader(lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ImageUtils.getBronzeHeader(lang) + "</th><th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("total", lang) + "</th>");
		html.append("</tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			OlympicRankingsBean bean = (OlympicRankingsBean) obj;
			if (lastUpdate == null || bean.getOrLastUpdate().compareTo(lastUpdate) > 0)
				lastUpdate = bean.getOrLastUpdate();
			
			// Evaluate bean
			String country = HtmlUtils.writeLink(Country.alias, bean.getCn1Id(), bean.getCn1Label(), bean.getCn1LabelEN());
			tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn1Id(), ImageUtils.SIZE_SMALL, null, null);
			country = HtmlUtils.writeImgTable(tmpImg, country);

			// Write line
			html.append("<tr><td class='srt'>" + country + "</td>");
			html.append("<td class='srt'>" + bean.getOrCountGold() + "</td>");
			html.append("<td class='srt'>" + bean.getOrCountSilver() + "</td>");
			html.append("<td class='srt'>" + bean.getOrCountBronze() + "</td>");
			html.append("<td class='srt'>" + (bean.getOrCountGold() + bean.getOrCountSilver() + bean.getOrCountBronze()) + "</td></tr>");
		}
		html.append("</tbody></table></div>");
		request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return html;
	}

	public static StringBuffer convertHallOfFame(HttpServletRequest request, Collection<?> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty()) {
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		}
		StringBuffer html = new StringBuffer("<div class='mobile100'><table class='tsort'>");
		long id = System.currentTimeMillis();
		Timestamp lastUpdate = null;
		html.append("<thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("year", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("inductee", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("inducted.as", lang) + "</th></tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			HallOfFameBean bean = (HallOfFameBean) obj;
			if (lastUpdate == null || bean.getHfLastUpdate().compareTo(lastUpdate) > 0)
				lastUpdate = bean.getHfLastUpdate();
			
			// Evaluate bean
			String ln = bean.getPrLastName();
			String year = HtmlUtils.writeLink(Year.alias, bean.getYrId(), bean.getYrLabel(), null);
			String name = HtmlUtils.writeLink(Athlete.alias, bean.getPrId(), StringUtils.toFullName(ln, bean.getPrFirstName(), null, true), bean.getPrFirstName() + " " + bean.getPrLastName());
			String position = "-";
			if (StringUtils.notEmpty(bean.getHfPosition())) {
				StringBuffer sbPos = new StringBuffer();
				for (String s : bean.getHfPosition().split("\\-"))
					sbPos.append((!sbPos.toString().isEmpty() ? " / " : "") + StringUtils.getUSPosition(bean.getLgId(), s));
				position = sbPos.toString();
			}
				
			// Write line
			html.append("<tr><td class='srt'>" + year + "</td><td id='" + ln.replaceAll("\\s", "-") + "-" + bean.getHfId() + "' class='srt'><b>" + name + "</b></td><td class='srt'>" + position + "</td></tr>");
		}
		html.append("</tbody></table></div>");
		request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return html;
	}

	public static StringBuffer convertRetiredNumber(HttpServletRequest request, Collection<?> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty()) {
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		}
		StringBuffer html = new StringBuffer("<div class='mobile100'><table class='tsort'>");
		long id = System.currentTimeMillis();
		Timestamp lastUpdate = null;
		html.append("<thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("team", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("number", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("name", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("year", lang) + "</th></tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			RetiredNumberBean bean = (RetiredNumberBean) obj;
			if (lastUpdate == null || bean.getRnLastUpdate().compareTo(lastUpdate) > 0)
				lastUpdate = bean.getRnLastUpdate();
			
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
		html.append("</tbody></table></div>");
		request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return html;
	}

	public static StringBuffer convertTeamStadium(HttpServletRequest request, Collection<?> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty()) {
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		}
		StringBuffer html = new StringBuffer("<div class='mobile100'><table class='tsort'>");
		long id = System.currentTimeMillis();
		Timestamp lastUpdate = null;
		html.append("<thead><tr class='rsort'><th onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("team", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("complex", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("city", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("years", lang) + "</th></tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			TeamStadiumBean bean = (TeamStadiumBean) obj;
			if (lastUpdate == null || bean.getTsLastUpdate().compareTo(lastUpdate) > 0)
				lastUpdate = bean.getTsLastUpdate();
			
			// Evaluate bean
			String teamImg = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getTmId(), ImageUtils.SIZE_SMALL, bean.getTsDate1() != null ? String.valueOf(bean.getTsDate1()) : "", null);
			String team = HtmlUtils.writeLink(Team.alias, bean.getTmId(), bean.getTmLabel(), null);
			team = HtmlUtils.writeImgTable(teamImg, team);
			String tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCnId(), ImageUtils.SIZE_SMALL, bean.getTsDate1() != null ? String.valueOf(bean.getTsDate1()) : "", null);
			String complex = HtmlUtils.writeLink(Complex.alias, bean.getCxId(), bean.getCxLabel(), bean.getCxLabel());
			String city = HtmlUtils.writeLink(City.alias, bean.getCtId(), bean.getCtLabel(), bean.getCtLabelEN());
			if (bean.getStId() != null)
				city += ", " + HtmlUtils.writeLink(State.alias, bean.getStId(), bean.getStCode(), bean.getStLabelEN());
			city += ", " + HtmlUtils.writeLink(Country.alias, bean.getCnId(), bean.getCnCode(), bean.getCnLabelEN()); 
			city = HtmlUtils.writeImgTable(tmpImg, city);	
			String years = bean.getTsDate1() + " " + StringUtils.SEP1 + " " + (!bean.getTsDate2().equals(0) ? bean.getTsDate2() : ResourceUtils.getText("today", lang));

			// Write line
			html.append("<tr><td class='srt'>" + team + "</td><td class='srt'>" + complex + (bean.getTsRenamed() ? "<b>*</b>" : "") + "</td><td class='srt'>" + city + "</td><td class='srt'>" + years + "</td></tr>");
		}
		html.append("</tbody></table></div>");
		request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return html;
	}

	public static StringBuffer convertUSChampionships(HttpServletRequest request, Collection<?> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty()) {
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		}
		StringBuffer html = new StringBuffer("<div class='mobile100'><table class='tsort'>");
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
		Timestamp lastUpdate = null;
		html.append("<thead><tr class='rsort'><th" + (isComment ? " colspan='2'" : "") + " onclick='sort(\"" + id + "\", this, 0);'>" + ResourceUtils.getText("year", lang) + "</th><th onclick='sort(\"" + id + "\", this, 1);'>" + ResourceUtils.getText("champion", lang) + "</th><th onclick='sort(\"" + id + "\", this, 2);'>" + ResourceUtils.getText("score", lang) + "</th><th onclick='sort(\"" + id + "\", this, 3);'>" + ResourceUtils.getText("runner.up", lang) + "</th>");
		html.append((isDate ? "<th onclick='sort(\"" + id + "\", this, 4);'>" + ResourceUtils.getText("date", lang) + "</th>" : "") + (isPlace ? "<th onclick='sort(\"" + id + "\", this, " + (isDate ? 5 : 4) + ");'>" + ResourceUtils.getText("place", lang) + "</th>" : "") + "</tr></thead><tbody class='tby' id='tb-" + id + "'>");
		for (Object obj : coll) {
			USChampionshipsBean bean = (USChampionshipsBean) obj;
			if (lastUpdate == null || bean.getRsLastUpdate().compareTo(lastUpdate) > 0)
				lastUpdate = bean.getRsLastUpdate();
			
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
			String date = (StringUtils.notEmpty(bean.getRsDate1()) ? HtmlUtils.writeDateLink(bean.getRsDate1(), bean.getRsDate2(), StringUtils.toTextDate(bean.getRsDate1(), lang, null) + StringUtils.SEP1 + StringUtils.toTextDate(bean.getRsDate2(), lang, null)) : StringUtils.EMPTY);
			String place = StringUtils.EMPTY;
			if (bean.getCxId() != null) {
				String tmpImg = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCnId(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
				place = HtmlUtils.writeLink(Complex.alias, bean.getCxId(), bean.getCxLabel(), bean.getCxLabel());
				place += ", " + HtmlUtils.writeLink(City.alias, bean.getCtId(), bean.getCtLabel(), bean.getCtLabelEN());
				if (bean.getStId() != null)
					place += ", " + HtmlUtils.writeLink(State.alias, bean.getStId(), bean.getStCode(), bean.getStLabelEN());
				place += ", " + HtmlUtils.writeLink(Country.alias, bean.getCnId(), bean.getCnCode(), bean.getCnLabelEN()); 
				place = HtmlUtils.writeImgTable(tmpImg, place);
			}

			// Write line
			html.append("<tr>" + (isComment ? "<td>" + HtmlUtils.writeComment(bean.getRsId(), bean.getRsComment()) + "</td>" : "") + "<td class='srt'>" + year + "</td><td class='srt'>" + (champion != null ? champion : StringUtils.EMPTY) + "</td><td class='srt'>" + (StringUtils.notEmpty(bean.getRsResult()) ? StringUtils.formatResult(bean.getRsResult(), lang) : "") + "</td>");
			html.append("<td class='srt'>" + (runnerup != null ? runnerup : StringUtils.EMPTY) + "</td>" + (isDate ? "<td class='srt'>" + date + "</td>" : "") + (isPlace ? "<td class='srt'>" + place + "</td>" : "") + "</tr>");
		}
		html.append("</tbody></table></div>");
		request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return html;
	}

	public static StringBuffer convertUSRecords(HttpServletRequest request, Collection<?> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty()) {
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		}
		Timestamp lastUpdate = null;
		StringBuffer html = new StringBuffer("<div class='mobile100'><table class='tsort'>");
		html.append("<thead><tr class='rsort'><th onclick='sort(\"0\", this, 0);'>" + ResourceUtils.getText("category", lang) + "</th><th onclick='sort(\"0\", this, 1);'>" + ResourceUtils.getText("scope", lang) + "</th><th onclick='sort(\"0\", this, 2);'>" + ResourceUtils.getText("type", lang) + "</th><th onclick='sort(\"0\", this, 3);'>" + ResourceUtils.getText("period", lang) + "</th><th onclick='sort(\"0\", this, 4);'>" + ResourceUtils.getText("record", lang) + "</th><th colspan='2' onclick='sort(\"0\", this, 5);'>" + ResourceUtils.getText("record.holder", lang) + "</th><th onclick='sort(\"0\", this, 6);'>" + ResourceUtils.getText("date", lang) + "</th>");
		html.append("</tr></thead><tbody class='tby' id='tb-0'>");
		for (Object obj : coll) {
			USRecordsBean bean = (USRecordsBean) obj;
			if (lastUpdate == null || bean.getRcLastUpdate().compareTo(lastUpdate) > 0)
				lastUpdate = bean.getRcLastUpdate();
			
			// Evaluate bean
			boolean isIndividual = bean.getRcType1().equalsIgnoreCase("individual");
			boolean isAlltime = bean.getRcType2().matches("^Alltime.*");
			String[] tRank = new String[5];
			String[] tRecord = new String[5];
			String[] tDate = new String[5];
			tRank[0] = (isIndividual && bean.getRcPerson1() != null ? "<b>" + HtmlUtils.writeLink(Athlete.alias, bean.getRcRank1(), StringUtils.toFullName(bean.getRcPerson1LastName(), bean.getRcPerson1FirstName(), null, true), bean.getRcPerson1FirstName() + " " + bean.getRcPerson1LastName()) + (bean.getRcIdPrTeam1() != null && !isAlltime ? "</b> (" + HtmlUtils.writeLink(Team.alias, bean.getRcIdPrTeam1(), bean.getRcPrTeam1(), null) + ")" : "</b>") : (bean.getRcTeam1() != null ? HtmlUtils.writeLink(Team.alias, bean.getRcRank1(), bean.getRcTeam1(), null) : null));
			tRank[1] = (isIndividual && bean.getRcPerson2() != null ? "<b>" + HtmlUtils.writeLink(Athlete.alias, bean.getRcRank2(), StringUtils.toFullName(bean.getRcPerson2LastName(), bean.getRcPerson2FirstName(), null, true), bean.getRcPerson2FirstName() + " " + bean.getRcPerson2LastName()) + (bean.getRcIdPrTeam2() != null && !isAlltime ? "</b> (" + HtmlUtils.writeLink(Team.alias, bean.getRcIdPrTeam2(), bean.getRcPrTeam2(), null) + ")" : "</b>") : (bean.getRcTeam2() != null ? HtmlUtils.writeLink(Team.alias, bean.getRcRank2(), bean.getRcTeam2(), null) : null));
			tRank[2] = (isIndividual && bean.getRcPerson3() != null ? "<b>" + HtmlUtils.writeLink(Athlete.alias, bean.getRcRank3(), StringUtils.toFullName(bean.getRcPerson3LastName(), bean.getRcPerson3FirstName(), null, true), bean.getRcPerson3FirstName() + " " + bean.getRcPerson3LastName()) + (bean.getRcIdPrTeam3() != null && !isAlltime ? "</b> (" + HtmlUtils.writeLink(Team.alias, bean.getRcIdPrTeam3(), bean.getRcPrTeam3(), null) + ")" : "</b>") : (bean.getRcTeam3() != null ? HtmlUtils.writeLink(Team.alias, bean.getRcRank3(), bean.getRcTeam3(), null) : null));
			tRank[3] = (isIndividual && bean.getRcPerson4() != null ? "<b>" + HtmlUtils.writeLink(Athlete.alias, bean.getRcRank4(), StringUtils.toFullName(bean.getRcPerson4LastName(), bean.getRcPerson4FirstName(), null, true), bean.getRcPerson4FirstName() + " " + bean.getRcPerson4LastName()) + (bean.getRcIdPrTeam4() != null && !isAlltime ? "</b> (" + HtmlUtils.writeLink(Team.alias, bean.getRcIdPrTeam4(), bean.getRcPrTeam4(), null) + ")" : "</b>") : (bean.getRcTeam4() != null ? HtmlUtils.writeLink(Team.alias, bean.getRcRank4(), bean.getRcTeam4(), null) : null));
			tRank[4] = (isIndividual && bean.getRcPerson5() != null ? "<b>" + HtmlUtils.writeLink(Athlete.alias, bean.getRcRank5(), StringUtils.toFullName(bean.getRcPerson5LastName(), bean.getRcPerson5FirstName(), null, true), bean.getRcPerson5FirstName() + " " + bean.getRcPerson5LastName()) + (bean.getRcIdPrTeam5() != null && !isAlltime ? "</b> (" + HtmlUtils.writeLink(Team.alias, bean.getRcIdPrTeam5(), bean.getRcPrTeam5(), null) + ")" : "</b>") : (bean.getRcTeam5() != null ? HtmlUtils.writeLink(Team.alias, bean.getRcRank5(), bean.getRcTeam5(), null) : null));
			tRank[0] = (tRank[0] != null && isIndividual && bean.getRcIdPrTeam1() != null && !isAlltime ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcIdPrTeam1(), ImageUtils.SIZE_SMALL, null, bean.getRcPrTeam1()), tRank[0]) : (tRank[0] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcRank1(), ImageUtils.SIZE_SMALL, bean.getRcDate1(), null), tRank[0]) : tRank[0]));
			tRank[1] = (tRank[1] != null && isIndividual && bean.getRcIdPrTeam2() != null && !isAlltime ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcIdPrTeam2(), ImageUtils.SIZE_SMALL, null, bean.getRcPrTeam2()), tRank[1]) : (tRank[1] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcRank2(), ImageUtils.SIZE_SMALL, bean.getRcDate1(), null), tRank[1]) : tRank[1]));
			tRank[2] = (tRank[2] != null && isIndividual && bean.getRcIdPrTeam3() != null && !isAlltime ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcIdPrTeam3(), ImageUtils.SIZE_SMALL, null, bean.getRcPrTeam3()), tRank[2]) : (tRank[2] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcRank3(), ImageUtils.SIZE_SMALL, bean.getRcDate1(), null), tRank[2]) : tRank[2]));
			tRank[3] = (tRank[3] != null && isIndividual && bean.getRcIdPrTeam4() != null && !isAlltime ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcIdPrTeam4(), ImageUtils.SIZE_SMALL, null, bean.getRcPrTeam4()), tRank[3]) : (tRank[3] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcRank4(), ImageUtils.SIZE_SMALL, bean.getRcDate1(), null), tRank[3]) : tRank[3]));
			tRank[4] = (tRank[4] != null && isIndividual && bean.getRcIdPrTeam5() != null && !isAlltime ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcIdPrTeam5(), ImageUtils.SIZE_SMALL, null, bean.getRcPrTeam5()), tRank[4]) : (tRank[4] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRcRank5(), ImageUtils.SIZE_SMALL, bean.getRcDate1(), null), tRank[4]) : tRank[4]));
			tRecord[0] = (bean.getRcRecord1() != null ? bean.getRcRecord1() : null);
			tDate[0] = (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + " " : "") + (bean.getRcDate1() != null ? bean.getRcDate1() : StringUtils.EMPTY);
			tDate[1] = (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + " " : "") + (bean.getRcDate2() != null ? bean.getRcDate2() : StringUtils.EMPTY);
			tDate[2] = (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + " " : "") + (bean.getRcDate3() != null ? bean.getRcDate3() : StringUtils.EMPTY);
			tDate[3] = (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + " " : "") + (bean.getRcDate4() != null ? bean.getRcDate4() : StringUtils.EMPTY);
			tDate[4] = (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + " " : "") + (bean.getRcDate5() != null ? bean.getRcDate5() : StringUtils.EMPTY);
			tDate[0] = (tDate[0] != null && tDate[0].matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d") ? HtmlUtils.writeDateLink(null, tDate[0], StringUtils.toTextDate(tDate[0], lang, "MMM dd, yyyy")) : tDate[0].replaceAll("\\-", StringUtils.SEP1));
			tDate[1] = (tDate[1] != null && tDate[1].matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d") ? HtmlUtils.writeDateLink(null, tDate[1], StringUtils.toTextDate(tDate[1], lang, "MMM dd, yyyy")) : tDate[1].replaceAll("\\-", StringUtils.SEP1));
			tDate[2] = (tDate[2] != null && tDate[2].matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d") ? HtmlUtils.writeDateLink(null, tDate[2], StringUtils.toTextDate(tDate[2], lang, "MMM dd, yyyy")) : tDate[2].replaceAll("\\-", StringUtils.SEP1));
			tDate[3] = (tDate[3] != null && tDate[3].matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d") ? HtmlUtils.writeDateLink(null, tDate[3], StringUtils.toTextDate(tDate[3], lang, "MMM dd, yyyy")) : tDate[3].replaceAll("\\-", StringUtils.SEP1));
			tDate[4] = (tDate[4] != null && tDate[4].matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d") ? HtmlUtils.writeDateLink(null, tDate[4], StringUtils.toTextDate(tDate[4], lang, "MMM dd, yyyy")) : tDate[4].replaceAll("\\-", StringUtils.SEP1));
			int ties = 1;
			StringBuffer sbTies = new StringBuffer();
			if (StringUtils.notEmpty(bean.getRcExa())) {
				List<Integer> lTie = StringUtils.tieList(bean.getRcExa());
				for (int i = 2 ; i <= 5 ; i++) {
					if (lTie.contains(i)) {
						ties++;
						sbTies.append("<tr><td>" + tRank[i - 1] + "</td>");
						sbTies.append("<td>" + tDate[i - 1] + "</td></tr>");
					}
				}
			}

			// Write line
			String rspan = (ties > 1 ? " rowspan='" + ties + "'" : "");
			html.append("<tr><td" + rspan + " class='srt'>" + bean.getSeLabel() + "</td><td" + rspan + " class='srt'>" + bean.getEvLabel() + "</td>");
			html.append("<td" + rspan + " class='srt'>" + bean.getRcType1() + "</td><td" + rspan + " class='srt'>" + (isAlltime ? (isIndividual ? "Career" : "All-time") : bean.getRcType2()) + "</td><td" + rspan + " class='srt'>" + bean.getRcLabel() + "</td>");
			html.append("<td" + rspan + " class='srt'><b>" + tRecord[0] + "</b></td><td class='srt'>" + tRank[0] + "</td>");
			html.append("<td class='srt'>" + tDate[0] + "</td>");
			html.append("</tr>").append(sbTies);
			
		}
		html.append("</tbody></table></div>");
		request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return html;
	}

	public static StringBuffer convertYearlyStats(HttpServletRequest request, Collection<?> coll, String lang) throws Exception {
		if (coll == null || coll.isEmpty()) {
			return new StringBuffer(HtmlUtils.writeNoResult(lang));
		}
		Timestamp lastUpdate = null;
		StringBuffer html = new StringBuffer("<div class='mobile100'><table class='tsort'>");
		html.append("<thead><tr class='rsort'><th onclick='sort(\"0\", this, 0);'>" + ResourceUtils.getText("type", lang) + "</th><th onclick='sort(\"0\", this, 1);'>" + ResourceUtils.getText("category", lang) + "</th><th onclick='sort(\"0\", this, 2);'>" + ResourceUtils.getText("entity.YR.1", lang) + "</th><th onclick='sort(\"0\", this, 3);' colspan='2'>" + ResourceUtils.getText("rank.1", lang) + "</th><th onclick='sort(\"0\", this, 4);' colspan='2'>" + ResourceUtils.getText("rank.2", lang) + "</th><th onclick='sort(\"0\", this, 5);' colspan='2'>" + ResourceUtils.getText("rank.3", lang) + "</th>");
		html.append("</tr></thead><tbody class='tby' id='tb-0'>");
		for (Object obj : coll) {
			YearlyStatsBean bean = (YearlyStatsBean) obj;
			if (lastUpdate == null || bean.getRsLastUpdate().compareTo(lastUpdate) > 0)
				lastUpdate = bean.getRsLastUpdate();
			
			// Evaluate bean
			boolean isIndividual = bean.getTpLabel().equalsIgnoreCase("individual");
			String[] tRank = new String[8];
			tRank[0] = (isIndividual && bean.getRsPerson1() != null ? HtmlUtils.writeLink(Athlete.alias, bean.getRsRank1(), StringUtils.toFullName(bean.getRsPerson1LastName(), bean.getRsPerson1FirstName(), null, true), bean.getRsPerson1FirstName() + " " + bean.getRsPerson1LastName()) + (bean.getRsIdPrTeam1() != null ? " (" + HtmlUtils.writeLink(Team.alias, bean.getRsIdPrTeam1(), bean.getRsPrTeam1(), null) + ")" : "") : (bean.getRsTeam1() != null ? HtmlUtils.writeLink(Team.alias, bean.getRsRank1(), bean.getRsTeam1(), null) : null));
			tRank[1] = (isIndividual && bean.getRsPerson2() != null ? HtmlUtils.writeLink(Athlete.alias, bean.getRsRank2(), StringUtils.toFullName(bean.getRsPerson2LastName(), bean.getRsPerson2FirstName(), null, true), bean.getRsPerson2FirstName() + " " + bean.getRsPerson2LastName()) + (bean.getRsIdPrTeam2() != null ? " (" + HtmlUtils.writeLink(Team.alias, bean.getRsIdPrTeam2(), bean.getRsPrTeam2(), null) + ")" : "") : (bean.getRsTeam2() != null ? HtmlUtils.writeLink(Team.alias, bean.getRsRank2(), bean.getRsTeam2(), null) : null));
			tRank[2] = (isIndividual && bean.getRsPerson3() != null ? HtmlUtils.writeLink(Athlete.alias, bean.getRsRank3(), StringUtils.toFullName(bean.getRsPerson3LastName(), bean.getRsPerson3FirstName(), null, true), bean.getRsPerson3FirstName() + " " + bean.getRsPerson3LastName()) + (bean.getRsIdPrTeam3() != null ? " (" + HtmlUtils.writeLink(Team.alias, bean.getRsIdPrTeam3(), bean.getRsPrTeam3(), null) + ")" : "") : (bean.getRsTeam3() != null ? HtmlUtils.writeLink(Team.alias, bean.getRsRank3(), bean.getRsTeam3(), null) : null));
			tRank[3] = (isIndividual && bean.getRsPerson4() != null ? HtmlUtils.writeLink(Athlete.alias, bean.getRsRank4(), StringUtils.toFullName(bean.getRsPerson4LastName(), bean.getRsPerson4FirstName(), null, true), bean.getRsPerson4FirstName() + " " + bean.getRsPerson4LastName()) + (bean.getRsIdPrTeam4() != null ? " (" + HtmlUtils.writeLink(Team.alias, bean.getRsIdPrTeam4(), bean.getRsPrTeam4(), null) + ")" : "") : (bean.getRsTeam4() != null ? HtmlUtils.writeLink(Team.alias, bean.getRsRank4(), bean.getRsTeam4(), null) : null));
			tRank[4] = (isIndividual && bean.getRsPerson5() != null ? HtmlUtils.writeLink(Athlete.alias, bean.getRsRank5(), StringUtils.toFullName(bean.getRsPerson5LastName(), bean.getRsPerson5FirstName(), null, true), bean.getRsPerson5FirstName() + " " + bean.getRsPerson5LastName()) + (bean.getRsIdPrTeam5() != null ? " (" + HtmlUtils.writeLink(Team.alias, bean.getRsIdPrTeam5(), bean.getRsPrTeam5(), null) + ")" : "") : (bean.getRsTeam5() != null ? HtmlUtils.writeLink(Team.alias, bean.getRsRank5(), bean.getRsTeam5(), null) : null));
			tRank[5] = (isIndividual && bean.getRsPerson6() != null ? HtmlUtils.writeLink(Athlete.alias, bean.getRsRank6(), StringUtils.toFullName(bean.getRsPerson6LastName(), bean.getRsPerson6FirstName(), null, true), bean.getRsPerson6FirstName() + " " + bean.getRsPerson6LastName()) + (bean.getRsIdPrTeam6() != null ? " (" + HtmlUtils.writeLink(Team.alias, bean.getRsIdPrTeam6(), bean.getRsPrTeam6(), null) + ")" : "") : (bean.getRsTeam6() != null ? HtmlUtils.writeLink(Team.alias, bean.getRsRank6(), bean.getRsTeam6(), null) : null));
			tRank[6] = (isIndividual && bean.getRsPerson7() != null ? HtmlUtils.writeLink(Athlete.alias, bean.getRsRank7(), StringUtils.toFullName(bean.getRsPerson7LastName(), bean.getRsPerson7FirstName(), null, true), bean.getRsPerson7FirstName() + " " + bean.getRsPerson7LastName()) + (bean.getRsIdPrTeam7() != null ? " (" + HtmlUtils.writeLink(Team.alias, bean.getRsIdPrTeam7(), bean.getRsPrTeam7(), null) + ")" : "") : (bean.getRsTeam7() != null ? HtmlUtils.writeLink(Team.alias, bean.getRsRank7(), bean.getRsTeam7(), null) : null));
			tRank[7] = (isIndividual && bean.getRsPerson8() != null ? HtmlUtils.writeLink(Athlete.alias, bean.getRsRank8(), StringUtils.toFullName(bean.getRsPerson8LastName(), bean.getRsPerson8FirstName(), null, true), bean.getRsPerson8FirstName() + " " + bean.getRsPerson8LastName()) + (bean.getRsIdPrTeam8() != null ? " (" + HtmlUtils.writeLink(Team.alias, bean.getRsIdPrTeam8(), bean.getRsPrTeam8(), null) + ")" : "") : (bean.getRsTeam8() != null ? HtmlUtils.writeLink(Team.alias, bean.getRsRank8(), bean.getRsTeam8(), null) : null));
			tRank[0] = (tRank[0] != null && isIndividual && bean.getRsIdPrTeam1() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsIdPrTeam1(), ImageUtils.SIZE_SMALL, null, bean.getRsPrTeam1()), tRank[0]) : (tRank[0] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank1(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), tRank[0]) : tRank[0]));
			tRank[1] = (tRank[1] != null && isIndividual && bean.getRsIdPrTeam2() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsIdPrTeam2(), ImageUtils.SIZE_SMALL, null, bean.getRsPrTeam2()), tRank[1]) : (tRank[1] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank2(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), tRank[1]) : tRank[1]));
			tRank[2] = (tRank[2] != null && isIndividual && bean.getRsIdPrTeam3() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsIdPrTeam3(), ImageUtils.SIZE_SMALL, null, bean.getRsPrTeam3()), tRank[2]) : (tRank[2] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank3(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), tRank[2]) : tRank[2]));
			tRank[3] = (tRank[3] != null && isIndividual && bean.getRsIdPrTeam4() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsIdPrTeam4(), ImageUtils.SIZE_SMALL, null, bean.getRsPrTeam4()), tRank[3]) : (tRank[3] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank4(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), tRank[3]) : tRank[3]));
			tRank[4] = (tRank[4] != null && isIndividual && bean.getRsIdPrTeam5() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsIdPrTeam5(), ImageUtils.SIZE_SMALL, null, bean.getRsPrTeam5()), tRank[4]) : (tRank[4] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank5(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), tRank[4]) : tRank[4]));
			tRank[5] = (tRank[5] != null && isIndividual && bean.getRsIdPrTeam6() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsIdPrTeam6(), ImageUtils.SIZE_SMALL, null, bean.getRsPrTeam6()), tRank[5]) : (tRank[5] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank6(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), tRank[5]) : tRank[5]));
			tRank[6] = (tRank[6] != null && isIndividual && bean.getRsIdPrTeam7() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsIdPrTeam7(), ImageUtils.SIZE_SMALL, null, bean.getRsPrTeam7()), tRank[6]) : (tRank[6] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank7(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), tRank[6]) : tRank[6]));
			tRank[7] = (tRank[7] != null && isIndividual && bean.getRsIdPrTeam8() != null ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsIdPrTeam8(), ImageUtils.SIZE_SMALL, null, bean.getRsPrTeam8()), tRank[7]) : (tRank[7] != null && !isIndividual ? HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank8(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null), tRank[7]) : tRank[7]));
			List<List<String>> lTies = new ArrayList<List<String>>();
			for (int i = 0 ; i < 3 ; i++)
				lTies.add(new ArrayList<String>());
			setTies(getTieList(false, false, bean.getRsExa()), 11, tRank, null, lTies);
			for (int i = 0 ; i < 3 ; i++) {
				if (!lTies.get(i).isEmpty())
					for (String s : lTies.get(i)) {
						Document d = Jsoup.parse(s);
						if (!d.getElementsByTag("table").isEmpty())
							tRank[i] += d.getElementsByTag("table").get(0).outerHtml();
						else
							tRank[i] += d.getElementsByTag("body").get(0).html();
					}
			}

			// Write line
			String year = HtmlUtils.writeLink(Year.alias, bean.getYrId(), bean.getYrLabel(), null);
			html.append("<tr><td class='srt nowrap'>" + bean.getTpLabel() + "</td><td class='srt nowrap'>" + bean.getCtLabel() + "</td><td class='srt'>" + year + "</td>");
			html.append("<td class='srt' style='font-weight:bold;'>" + tRank[0] + "</td><td><b>" + (StringUtils.notEmpty(bean.getResult1()) ? bean.getResult1() : "") + "</b></td>");
			html.append("<td class='srt'" + (!StringUtils.notEmpty(bean.getResult2()) ? " colspan='2'" : "") + ">" + (StringUtils.notEmpty(tRank[1]) ? tRank[1] : "") + "</td>" + (StringUtils.notEmpty(bean.getResult2()) ? "<td>" + bean.getResult2() + "</td>" : ""));
			html.append("<td class='srt'" + (!StringUtils.notEmpty(bean.getResult3()) ? " colspan='2'" : "") + ">" + (StringUtils.notEmpty(tRank[2]) ? tRank[2] : "") + "</td>" + (StringUtils.notEmpty(bean.getResult3()) ? "<td>" + bean.getResult3() + "</td>" : ""));
			html.append("</tr>");
		}
		html.append("</tbody></table></div>");
		request.setAttribute("lastupdate", StringUtils.toTextDate(lastUpdate, lang, "d MMM yyyy, HH:mm"));
		return html;
	}
	
	public static StringBuffer convertLastUpdates(Collection<?> coll, Integer sp, Integer limit, Integer offset, String lang) throws Exception {
		final int ITEM_LIMIT = Integer.parseInt(ConfigUtils.getValue("default_lastupdates_limit"));
		StringBuffer html = new StringBuffer();
		int i = 0;
		for (Object obj : coll) {
			LastUpdateBean bean = (LastUpdateBean) obj;
			String pos1 = null;
			String pos2 = null;
			String pos3 = null;
			String pos4 = null;
			Integer number = (bean.getTp3Number() != null ? bean.getTp3Number() : (bean.getTp2Number() != null ? bean.getTp2Number() : bean.getTp1Number()));
			if (number == null)
				continue;
			if (number < 10) {
				if (bean.getPr1LastName() != null) {
					pos1 = (StringUtils.isRevertName(bean.getPr1CountryCode(), bean.getPr1FirstName() + " " + bean.getPr1LastName()) ? (StringUtils.notEmpty(bean.getPr1LastName()) ? bean.getPr1LastName().substring(0, 1) + "." : "") + bean.getPr1FirstName() : (StringUtils.notEmpty(bean.getPr1FirstName()) ? bean.getPr1FirstName().substring(0, 1) + "." : "") + bean.getPr1LastName());
					pos1 = HtmlUtils.writeLink(Athlete.alias, bean.getPr1Id(), pos1, StringUtils.toFullName(bean.getPr1LastName(), bean.getPr1FirstName(), null, false));
					if (bean.getPr1Team() != null)
						pos1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getPr1Team(), ImageUtils.SIZE_SMALL, null, null), pos1);
					if (bean.getPr1Country() != null)
						pos1 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getPr1Country(), ImageUtils.SIZE_SMALL, null, null), pos1);
				}
				if (bean.getPr2LastName() != null) {
					pos2 = (StringUtils.isRevertName(bean.getPr2CountryCode(), bean.getPr2FirstName() + " " + bean.getPr2LastName()) ? (StringUtils.notEmpty(bean.getPr2LastName()) ? bean.getPr2LastName().substring(0, 1) + "." : "") + bean.getPr2FirstName() : (StringUtils.notEmpty(bean.getPr2FirstName()) ? bean.getPr2FirstName().substring(0, 1) + "." : "") + bean.getPr2LastName());
					pos2 = HtmlUtils.writeLink(Athlete.alias, bean.getPr2Id(), pos2, StringUtils.toFullName(bean.getPr2LastName(), bean.getPr2FirstName(), null, false));
					if (bean.getPr2Team() != null)
						pos2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getPr2Team(), ImageUtils.SIZE_SMALL, null, null), pos2);
					if (bean.getPr2Country() != null)
						pos2 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getPr2Country(), ImageUtils.SIZE_SMALL, null, null), pos2);
				}
				if (bean.getPr3LastName() != null) {
					pos3 = (StringUtils.isRevertName(bean.getPr3CountryCode(), bean.getPr3FirstName() + " " + bean.getPr3LastName()) ? (StringUtils.notEmpty(bean.getPr3LastName()) ? bean.getPr3LastName().substring(0, 1) + "." : "") + bean.getPr3FirstName() : (StringUtils.notEmpty(bean.getPr3FirstName()) ? bean.getPr3FirstName().substring(0, 1) + "." : "") + bean.getPr3LastName());
					pos3 = HtmlUtils.writeLink(Athlete.alias, bean.getPr3Id(), pos3, StringUtils.toFullName(bean.getPr3LastName(), bean.getPr3FirstName(), bean.getPr3CountryCode(), false));
					if (bean.getPr3Team() != null)
						pos3 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getPr3Team(), ImageUtils.SIZE_SMALL, null, null), pos3);
					if (bean.getPr3Country() != null)
						pos3 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getPr3Country(), ImageUtils.SIZE_SMALL, null, null), pos3);
				}
				if (bean.getPr4LastName() != null) {
					pos4 = (StringUtils.isRevertName(bean.getPr4CountryCode(), bean.getPr4FirstName() + " " + bean.getPr4LastName()) ? (StringUtils.notEmpty(bean.getPr4LastName()) ? bean.getPr4LastName().substring(0, 1) + "." : "") + bean.getPr4FirstName() : (StringUtils.notEmpty(bean.getPr4FirstName()) ? bean.getPr4FirstName().substring(0, 1) + "." : "") + bean.getPr4LastName());
					pos4 = HtmlUtils.writeLink(Athlete.alias, bean.getPr4Id(), pos4, StringUtils.toFullName(bean.getPr4LastName(), bean.getPr4FirstName(), bean.getPr4CountryCode(), false));
					if (bean.getPr4Team() != null)
						pos4 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getPr4Team(), ImageUtils.SIZE_SMALL, null, null), pos4);
					if (bean.getPr4Country() != null)
						pos4 = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getPr4Country(), ImageUtils.SIZE_SMALL, null, null), pos4);
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
			String year = HtmlUtils.writeLink(Year.alias, bean.getYrId(), bean.getYrLabel(), null);
			String sport = HtmlUtils.writeImgTable(HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, bean.getSpId(), ImageUtils.SIZE_SMALL, null, null), HtmlUtils.writeLink(Sport.alias, bean.getSpId(), bean.getSpLabel(), bean.getSpLabelEN()));
			String date = HtmlUtils.writeDateLink(null, bean.getRsDate(), StringUtils.toTextDate(bean.getRsDate(), lang, "d MMM yyyy"));
			String date_ = new SimpleDateFormat("yyyyMMddHHmm").format(bean.getRsDate());
			String path = bean.getYrLabel() + "/" + bean.getSpLabelEN() + "/" + bean.getCpLabelEN() + "/" + bean.getEvLabelEN() + (bean.getSeId() != null ? "/" + bean.getSeLabelEN() : "") + (bean.getSe2Id() != null ? "/" + bean.getSe2LabelEN() : "");
			String tie = bean.getRsText3();
			boolean isScore = (pos1 != null && pos2 != null && StringUtils.notEmpty(bean.getRsText1()) && !StringUtils.notEmpty(bean.getRsText2()));
			boolean isDouble = (pos1 != null && pos2 != null && (number == 4 || (bean.getRsText4() != null && bean.getRsText4().equals("#DOUBLE#")) || (tie != null && tie.equals("1-2"))));
			boolean isTriple = (pos1 != null && pos2 != null && pos3 != null && (number == 5 || (bean.getRsText4() != null && bean.getRsText4().equals("#TRIPLE#")) || (tie != null && tie.matches("^1\\-(3|4|5|6|7|8|9).*"))));
			String link = "/results/" + StringUtils.urlEscape(bean.getSpLabelEN() + "/" + bean.getCpLabelEN() + "/" + bean.getEvLabelEN() + (bean.getSeId() != null ? "/" + bean.getSeLabelEN() : "") + (bean.getSe2Id() != null ? "/" + bean.getSe2LabelEN() : "")) + "/" + StringUtils.encode(bean.getSpId() + "-" + bean.getCpId() + "-" + bean.getEvId() + "-" + (bean.getSeId() != null ? bean.getSeId() : 0) + "-" + (bean.getSe2Id() != null ? bean.getSe2Id() : 0) + "-0");
			String event = "<a href='" + link + "'>" + bean.getCpLabel() + " " + StringUtils.SEP1 + " " + bean.getEvLabel() + (StringUtils.notEmpty(bean.getSeLabel()) ? " " + StringUtils.SEP1 + " " + bean.getSeLabel() : "") + (StringUtils.notEmpty(bean.getSe2Label()) ? " " + StringUtils.SEP1 + " " + bean.getSe2Label() : "") + "</a>";
			if (StringUtils.notEmpty(bean.getRsText5()))
				event += " [" + bean.getRsText5() + "]";
			String eventImg = null;
			if (!StringUtils.notEmpty(eventImg) && bean.getSe2Id() != null && !bean.getSe2LabelEN().matches("Men|Women"))
				eventImg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_EVENT, bean.getSpId() + "-" + bean.getSe2Id(), ImageUtils.SIZE_SMALL, null, bean.getSe2Label());
			if (!StringUtils.notEmpty(eventImg) && bean.getSeId() != null && !bean.getSeLabelEN().matches("Men|Women"))
				eventImg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_EVENT, bean.getSpId() + "-" + bean.getSeId(), ImageUtils.SIZE_SMALL, null, bean.getSeLabel());
			if (!StringUtils.notEmpty(eventImg) && bean.getEvId() != null && !bean.getEvLabelEN().matches("Men|Women"))
				eventImg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_EVENT, bean.getSpId() + "-" + bean.getEvId(), ImageUtils.SIZE_SMALL, null, bean.getEvLabel());
			if (!StringUtils.notEmpty(eventImg) && bean.getCpId() != null)
				eventImg = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT_CHAMPIONSHIP, bean.getSpId() + "-" + bean.getCpId(), ImageUtils.SIZE_SMALL, null, bean.getCpLabel());
			if (StringUtils.notEmpty(eventImg))
				event = HtmlUtils.writeImgTable(eventImg, event);

			// Write line
			String pos1_ = "<td style='padding-right:3px;font-weight:bold;'>" + pos1 + "</td>";
			String pos2_ = (isTriple ? null : (pos2 != null ? "<td style='padding-right:3px;'>" + pos2 + "</td>" : null));
			String pos3_ = (isDouble || isTriple ? null : (pos3 != null ? "<td style='padding-right:3px;'>" + pos3 + "</td>" : null));
			if (isDouble || isTriple) {
				if (pos2 != null)
					pos1_ = "<td style='font-weight:bold;'>" + pos1 + "</td><td style='font-weight:bold;'> / </td><td style='padding-right:3px;font-weight:bold;'>" + pos2 + "</td>" + (isTriple && pos3 != null ? "<td style='font-weight:bold;'> / </td><td style='font-weight:bold;'>" + pos3 + "</td>" + (tie != null && tie.matches("^1\\-(4|5|6|7|8|9).*") ? "<td style='font-weight:bold;'> / ...</td>" : "") : "");
				if (isDouble && pos3 != null)
					pos2_ = "<td>" + pos3 + "</td>" + (pos4 != null ? "<td> / </td><td>" + pos4 + "</td>" : "");
				else if (isDouble || pos2 == null)
					pos2_ = null;
			}
			else if (tie != null && tie.matches("^2\\-(3|4|5|6|7|8|9).*") && pos2 != null) {
				pos2_ = "<td>" + pos2 + "</td>" + (pos3 != null ? "<td> / </td><td>" + pos3 + "</td>" : "");
				pos3_ = null;
			}
			html.append("<tr><td class='srt'>" + year + "</td><td class='srt'>" + sport + "</td>");
			html.append("<td class='srt wrap'>" + event + "</td>");
			if (pos1 != null)
				html.append("<td class='srt' style='line-height:30px;vertical-align:middle;'><span class='details'>" + HtmlUtils.writeLink(Result.alias, bean.getRsId(), "<img alt='details' title='" +  ResourceUtils.getText("details", lang) + "' src='/img/render/details.png'/>", path) + "</span><table style='margin-right:15px;'><tr>" + pos1_ + (isScore ? "<td style='line-height:15px;padding-left:2px;padding-right:3px;padding-top:3px;'>" + StringUtils.formatResult(bean.getRsText1(), lang) + "</td>" : "") + (pos2_ != null ? pos2_ : "") + (pos3_ != null ? pos3_ : "") + "</tr></table></td>");
			else
				html.append("<td class='srt'>-</td>");
			html.append("<td id='dt-" + date_ + "-" + i + "' class='srt'>" + date + "</td></tr>");
			i++;
		}
		final String MORE_ITEMS = "<tr class='moreitems'><td colspan='5'><div class='sfdiv1' onclick='getLastUpdates(this, \"#P1#\");'> (+" + ITEM_LIMIT + ") </div><div class='sfdiv2' onclick='getLastUpdates(this, \"#P2#\");'> (+50) </div><div class='sfdiv3' onclick='getLastUpdates(this, \"#P3#\");'> (+100) </div></td></tr>";
		String p = sp + "-#LIMIT#-" + (offset + limit);
		html.append(MORE_ITEMS.replaceAll("#P1#", StringUtils.encode(p.replaceAll("#LIMIT#", String.valueOf(ITEM_LIMIT)))).replaceAll("#P2#", StringUtils.encode(p.replaceAll("#LIMIT#", "50"))).replaceAll("#P3#", StringUtils.encode(p.replaceAll("#LIMIT#", "100"))));
		return html;
	}
	
}