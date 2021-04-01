package com.sporthenon.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.PicklistItem;
import com.sporthenon.db.entity.Calendar;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.OlympicRanking;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.InactiveItem;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.db.entity.meta.TreeItem;
import com.sporthenon.db.function.HallOfFameBean;
import com.sporthenon.db.function.OlympicMedalsBean;
import com.sporthenon.db.function.OlympicRankingsBean;
import com.sporthenon.db.function.ResultsBean;
import com.sporthenon.db.function.RetiredNumberBean;
import com.sporthenon.db.function.TeamStadiumBean;
import com.sporthenon.db.function.USChampionshipsBean;
import com.sporthenon.db.function.USRecordsBean;
import com.sporthenon.db.function.YearlyStatsBean;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.HtmlConverter;
import com.sporthenon.web.ServletHelper;

@WebServlet(
	name = "AndroidServlet",
	urlPatterns = {"/AndroidServlet"}
)
@SuppressWarnings("unchecked")
public class AndroidServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;

	public AndroidServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			String lang = (hParams.containsKey("lang") ? String.valueOf(hParams.get("lang")) : ResourceUtils.LGDEFAULT);
			String p = String.valueOf(hParams.get("p"));
			String p2 = String.valueOf(hParams.get("p2"));
			Document doc = DocumentFactory.getInstance().createDocument();
	        Element root = doc.addElement("picklist");
	        root.addAttribute("id", p2);
	        
			if (p2.equalsIgnoreCase(Result.alias)) // RESULTS
				processResults(doc, root, p.split("\\-"), lang);
			else if (p2.equalsIgnoreCase(Calendar.alias)) // CALENDAR
				processCalendar(doc, root, p.split("\\-"), lang);
			else if (p2.equalsIgnoreCase(Olympics.alias)) // OLYMPICS
				processOlympics(doc, root, p.split("\\-"), lang);
			else if (p2.equalsIgnoreCase("US")) // US LEAGUES
				processUSLeagues(doc, root, p.split("\\-"), lang);
			
	        response.setContentType("text/xml");
	        response.setCharacterEncoding("utf-8");
	        XMLWriter writer = new XMLWriter(response.getOutputStream(), OutputFormat.createPrettyPrint());
	        writer.write(doc);
	        writer.flush();
	        response.flushBuffer();
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	private void processResults(Document doc, Element root, String[] t, String lang) throws Exception {
		String code = t[0];
		String label = "label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? "_" + lang : "");
        //Inactive Events
		List<String> lInactive = new ArrayList<String>();
		if (code.matches("(?i:" + Event.alias + "|SE|SE2)")) {
			for (InactiveItem item : (List<InactiveItem>) DatabaseManager.executeSelect("SELECT * FROM inactive_tem where id_sport=" + t[1], InactiveItem.class))
				lInactive.add(item.getIdChampionship() + "-" + item.getIdEvent() + (item.getIdSubevent() != null ? "-" + item.getIdSubevent() : "") + (item.getIdSubevent2() != null ? "-" + item.getIdSubevent2() : ""));
		}
		
		if (code.equalsIgnoreCase(Sport.alias)) {
			String sql = "SELECT id, " + label + " FROM sport ORDER BY " + label;
			addItems(doc, root, ImageUtils.INDEX_SPORT, DatabaseManager.getPicklist(sql, null), null, null, null, null);
		}
        else if (code.equalsIgnoreCase(Championship.alias)) {
        	String sql = "SELECT CP.id, CP." + label
					+ " FROM championship CP "
					+ " WHERE CP.id IN (SELECT id_championship FROM result WHERE id_sport = ?) "
					+ " ORDER by CP.index, CP." + label;
        	addItems(doc, root, ImageUtils.INDEX_SPORT_CHAMPIONSHIP, DatabaseManager.getPicklist(sql, Arrays.asList(t[1])), null, t[1], null, null);
        }
        else if (code.equalsIgnoreCase(Event.alias)) {
        	String sql = "SELECT EV.id, EV." + label
					+ " FROM event EV "
					+ " WHERE EV.id IN (SELECT id_event FROM result WHERE id_sport = ? AND id_championship = ?) "
					+ " ORDER by EV.index, EV." + label;
        	addItems(doc, root, ImageUtils.INDEX_SPORT_EVENT, DatabaseManager.getPicklist(sql, Arrays.asList(t[1], t[2])), lInactive, t[1], t[2], "SELECT COUNT(DISTINCT id_subevent) from result WHERE id_sport=" + t[1] + " and id_championship=" + t[2] + " and id_event=#ID#");
        }
        else if (code.equalsIgnoreCase("SE")) {
        	String sql = "SELECT EV.id, EV." + label
					+ " FROM event EV "
					+ " WHERE EV.id IN (SELECT id_subevent FROM result WHERE id_sport = ? AND id_championship = ? AND id_event = ?) "
					+ " ORDER by EV.index, EV." + label;
        	addItems(doc, root, ImageUtils.INDEX_SPORT_EVENT, DatabaseManager.getPicklist(sql, Arrays.asList(t[1], t[2], t[3])), lInactive, t[1], t[2] + "-" + t[3], "SELECT COUNT(DISTINCT id_subevent2) FROM result WHERE id_sport = " + t[1] + " AND id_championship = " + t[2] + " AND id_event = " + t[3] + " AND id_subevent = #ID#");
        }
        else if (code.equalsIgnoreCase("SE2")) {
        	String sql = "SELECT EV.id, EV." + label
					+ " FROM event EV "
					+ " WHERE EV.id IN (SELECT id_subevent2 FROM result WHERE id_sport = ? AND id_championship = ? AND id_event = ? AND id_subevent = ?) "
					+ " ORDER by EV.index, EV." + label;
        	addItems(doc, root, ImageUtils.INDEX_SPORT_EVENT, DatabaseManager.getPicklist(sql, Arrays.asList(t[1], t[2], t[3], t[4])), lInactive, t[1], t[2] + "-" + t[3] + "-" + t[4], null);
        }
        else if (code.equalsIgnoreCase(Result.alias)) {
        	Integer sp = StringUtils.toInt(t[1]);
        	Integer cp = StringUtils.toInt(t[2]);
        	Integer ev = StringUtils.toInt(t[3]);
        	Integer se = StringUtils.toInt(t.length > 4 ? t[4] : "0");
        	Integer se2 = StringUtils.toInt(t.length > 5 ? t[5] : "0");
        	
        	List<Object> params = new ArrayList<Object>();
			params.add(sp);
			params.add(cp);
			params.add(ev);
			params.add(se);
			params.add(se2);
			params.add("0");
			params.add(0);
			params.add("_" + lang);
			Event ev_ = (Event) DatabaseManager.loadEntity(Event.class, (se2 > 0 ? se2 : (se > 0 ? se : ev)));
			addResultItems(doc, root, ev_, (Collection<ResultsBean>) DatabaseManager.callFunction("get_results", params, ResultsBean.class), lang);
        }
        else if (code.equalsIgnoreCase("R1")) {
        	final int MAX_RANKS = 20;
        	Result r = (Result) DatabaseManager.loadEntity(Result.class, t[1]);
        	Element sp = root.addElement("sport");
        	sp.addAttribute("id", String.valueOf(r.getSport().getId()));
        	sp.addAttribute("img", getImage(ImageUtils.INDEX_SPORT, r.getSport().getId(), ImageUtils.SIZE_SMALL, null, null));
        	sp.addText(r.getSport().getLabel(lang));
        	Element cp = root.addElement("championship");
        	cp.addAttribute("id", String.valueOf(r.getChampionship().getId()));
        	cp.addAttribute("img", getImage(ImageUtils.INDEX_SPORT_CHAMPIONSHIP, r.getSport().getId() + "-" + r.getChampionship().getId(), ImageUtils.SIZE_SMALL, null, null));
        	cp.addText(r.getChampionship().getLabel(lang));
        	Element ev = root.addElement("event");
        	ev.addAttribute("id", String.valueOf(r.getEvent().getId()));
        	ev.addAttribute("img", getImage(ImageUtils.INDEX_SPORT_EVENT, r.getSport().getId() + "-" + r.getEvent().getId(), ImageUtils.SIZE_SMALL, null, null));
        	ev.addText(r.getEvent().getLabel(lang));
        	if (r.getSubevent() != null) {
        		Element se = root.addElement("subevent");
        		se.addAttribute("id", String.valueOf(r.getSubevent().getId()));
	        	se.addAttribute("img", getImage(ImageUtils.INDEX_SPORT_EVENT, r.getSport().getId() + "-" + r.getSubevent().getId(), ImageUtils.SIZE_SMALL, null, null));
	        	se.addText(r.getSubevent().getLabel(lang));
        	}
        	if (r.getSubevent2() != null) {
        		Element se2 = root.addElement("subevent2");
        		se2.addAttribute("id", String.valueOf(r.getSubevent2().getId()));
	        	se2.addAttribute("img", getImage(ImageUtils.INDEX_SPORT_EVENT, r.getSport().getId() + "-" + r.getSubevent2().getId(), ImageUtils.SIZE_SMALL, null, null));
	        	se2.addText(r.getSubevent2().getLabel(lang));
        	}
        	if (StringUtils.notEmpty(r.getDate2())) {
        		Element dates = root.addElement("dates");
        		if (StringUtils.notEmpty(r.getDate1()))
        			dates.addAttribute("date1", StringUtils.toTextDate(r.getDate1(), lang, "d MMMM yyyy"));
        		dates.addAttribute("date2", StringUtils.toTextDate(r.getDate2(), lang, "d MMMM yyyy"));
        	}
			if (StringUtils.notEmpty(r.getComplex2()) || StringUtils.notEmpty(r.getCity2())) {
				String pl1 = null;
				String pl2 = null;
				Integer cn1 = null;
				Integer cn2 = null;
				if (r.getComplex1() != null) {
					Complex cx = r.getComplex1();
					pl1 = HtmlConverter.getPlace(cx.getId(), cx.getCity().getId(), cx.getCity().getState() != null ? cx.getCity().getState().getId() : null, cx.getCity().getCountry().getId(), cx.getLabel(), cx.getCity().getLabel(lang), cx.getCity().getState() != null ? cx.getCity().getState().getLabel(lang) : null, cx.getCity().getCountry().getLabel(lang), cx.getLabel(), cx.getCity().getLabel(), cx.getCity().getState() != null ? cx.getCity().getState().getLabel() : null, cx.getCity().getCountry().getLabel(), r.getYear().getLabel());
					cn1 = cx.getCity().getCountry().getId();
				}
				else if (r.getCity1() != null) {
					City ct = r.getCity1();
					pl1 = HtmlConverter.getPlace(null, ct.getId(), ct.getState() != null ? ct.getState().getId() : null, ct.getCountry().getId(), null, ct.getLabel(lang), ct.getState() != null ? ct.getState().getLabel(lang) : null, ct.getCountry().getLabel(lang), null, ct.getLabel(), ct.getState() != null ? ct.getState().getLabel() : null, ct.getCountry().getLabel(), r.getYear().getLabel());
					cn1 = ct.getCountry().getId();
				}
				if (r.getComplex2() != null) {
					Complex cx = r.getComplex2();
					pl2 = HtmlConverter.getPlace(cx.getId(), cx.getCity().getId(), cx.getCity().getState() != null ? cx.getCity().getState().getId() : null, cx.getCity().getCountry().getId(), cx.getLabel(), cx.getCity().getLabel(lang), cx.getCity().getState() != null ? cx.getCity().getState().getLabel(lang) : null, cx.getCity().getCountry().getLabel(lang), cx.getLabel(), cx.getCity().getLabel(), cx.getCity().getState() != null ? cx.getCity().getState().getLabel() : null, cx.getCity().getCountry().getLabel(), r.getYear().getLabel());
					cn2 = cx.getCity().getCountry().getId();
				}
				else if (r.getCity2() != null) {
					City ct = r.getCity2();
					pl2 = HtmlConverter.getPlace(null, ct.getId(), ct.getState() != null ? ct.getState().getId() : null, ct.getCountry().getId(), null, ct.getLabel(lang), ct.getState() != null ? ct.getState().getLabel(lang) : null, ct.getCountry().getLabel(lang), null, ct.getLabel(), ct.getState() != null ? ct.getState().getLabel() : null, ct.getCountry().getLabel(), r.getYear().getLabel());
					cn2 = ct.getCountry().getId();
				}
				if (StringUtils.notEmpty(pl1)) {
					Element place1 = root.addElement("place1");
					if (cn1 != null) {
						place1.addAttribute("id", String.valueOf(cn1));
						place1.addAttribute("img", getImage(ImageUtils.INDEX_COUNTRY, cn1, ImageUtils.SIZE_SMALL, r.getYear().getLabel(), null));	
					}
					place1.addText(StringUtils.removeTags(pl1));
				}
				Element place2 = root.addElement("place2");
				if (cn2 != null) {
					place2.addAttribute("id", String.valueOf(cn2));
					place2.addAttribute("img", getImage(ImageUtils.INDEX_COUNTRY, cn2, ImageUtils.SIZE_SMALL, r.getYear().getLabel(), null));	
				}
				place2.addText(StringUtils.removeTags(pl2));
			}
		
			// Result
			List<Object> params = new ArrayList<Object>();
			params.add(0);
			params.add(0);
			params.add(0);
			params.add(0);
			params.add(0);
			params.add("0");
			params.add(r.getId());
			params.add("_" + lang);
			List<ResultsBean> list = (List<ResultsBean>) DatabaseManager.callFunction("get_results", params, ResultsBean.class);
			if (list != null && !list.isEmpty()) {
				ResultsBean bean = list.get(0);
				String[] tEntity = new String[MAX_RANKS];
				String[] tEntityRel = new String[MAX_RANKS];
				String[] tEntityImg = new String[MAX_RANKS];
				String[] tResult = new String[MAX_RANKS];
				Event ev_ = (Event) DatabaseManager.loadEntity(Event.class, (r.getSubevent2() != null ? r.getSubevent2().getId() : (r.getSubevent() != null ? r.getSubevent().getId() : r.getEvent().getId())));
				int type_ = ev_.getType().getNumber();
				if (bean.getRsRank1() != null) {
					tEntity[0] = HtmlConverter.getResultsEntity(type_, bean.getRsRank1(), bean.getEn1Str1(), bean.getEn1Str2(), bean.getEn1Str3(), bean.getEn1Rel2Code(), bean.getYrLabel(), null);
					tEntityRel[0] = HtmlConverter.getResultsEntityRel(bean.getEn1Rel1Id(), bean.getEn1Rel1Label(), bean.getEn1Rel1Label(), bean.getEn1Rel2Id(), bean.getEn1Rel2Label(), bean.getEn1Rel2Label(), bean.getEn1Rel2LabelEN(), true, true, bean.getYrLabel());
					tResult[0] = bean.getRsResult1();
				}
				if (bean.getRsRank2() != null) {
					tEntity[1] = HtmlConverter.getResultsEntity(type_, bean.getRsRank2(), bean.getEn2Str1(), bean.getEn2Str2(), bean.getEn2Str3(), bean.getEn2Rel2Code(), bean.getYrLabel(), null);
					tEntityRel[1] = HtmlConverter.getResultsEntityRel(bean.getEn2Rel1Id(), bean.getEn2Rel1Label(), bean.getEn2Rel1Label(), bean.getEn2Rel2Id(), bean.getEn2Rel2Label(), bean.getEn2Rel2Label(), bean.getEn2Rel2LabelEN(), true, true, bean.getYrLabel());
					tResult[1] = bean.getRsResult2();
				}
				if (bean.getRsRank3() != null) {
					tEntity[2] = HtmlConverter.getResultsEntity(type_, bean.getRsRank3(), bean.getEn3Str1(), bean.getEn3Str2(), bean.getEn3Str3(), bean.getEn3Rel2Code(), bean.getYrLabel(), null);
					tEntityRel[2] = HtmlConverter.getResultsEntityRel(bean.getEn3Rel1Id(), bean.getEn3Rel1Label(), bean.getEn3Rel1Label(), bean.getEn3Rel2Id(), bean.getEn3Rel2Label(), bean.getEn3Rel2Label(), bean.getEn3Rel2LabelEN(), true, true, bean.getYrLabel());
					tResult[2] = bean.getRsResult3();
				}
				if (bean.getRsRank4() != null) {
					tEntity[3] = HtmlConverter.getResultsEntity(type_, bean.getRsRank4(), bean.getEn4Str1(), bean.getEn4Str2(), bean.getEn4Str3(), bean.getEn4Rel2Code(), bean.getYrLabel(), null);
					tEntityRel[3] = HtmlConverter.getResultsEntityRel(bean.getEn3Rel1Id(), bean.getEn4Rel1Label(), bean.getEn4Rel1Label(), bean.getEn4Rel2Id(), bean.getEn4Rel2Label(), bean.getEn4Rel2Label(), bean.getEn4Rel2LabelEN(), true, true, bean.getYrLabel());
					tResult[3] = bean.getRsResult4();
				}
				if (bean.getRsRank5() != null) {
					tEntity[4] = HtmlConverter.getResultsEntity(type_, bean.getRsRank5(), bean.getEn5Str1(), bean.getEn5Str2(), bean.getEn5Str3(), bean.getEn5Rel2Code(), bean.getYrLabel(), null);
					tEntityRel[4] = HtmlConverter.getResultsEntityRel(bean.getEn5Rel1Id(), bean.getEn5Rel1Label(), bean.getEn5Rel1Label(), bean.getEn5Rel2Id(), bean.getEn5Rel2Label(), bean.getEn5Rel2Label(), bean.getEn5Rel2LabelEN(), true, true, bean.getYrLabel());
					tResult[4] = bean.getRsResult5();
				}
				if (bean.getRsRank6() != null) {
					tEntity[5] = HtmlConverter.getResultsEntity(type_, bean.getRsRank6(), bean.getEn6Str1(), bean.getEn6Str2(), bean.getEn6Str3(), bean.getEn6Rel2Code(), bean.getYrLabel(), null);
					tEntityRel[5] = HtmlConverter.getResultsEntityRel(bean.getEn6Rel1Id(), bean.getEn6Rel1Label(), bean.getEn6Rel1Label(), bean.getEn6Rel2Id(), bean.getEn6Rel2Label(), bean.getEn6Rel2Label(), bean.getEn6Rel2LabelEN(), true, true, bean.getYrLabel());
				}
				if (bean.getRsRank7() != null) {
					tEntity[6] = HtmlConverter.getResultsEntity(type_, bean.getRsRank7(), bean.getEn7Str1(), bean.getEn7Str2(), bean.getEn7Str3(), bean.getEn7Rel2Code(), bean.getYrLabel(), null);
					tEntityRel[6] = HtmlConverter.getResultsEntityRel(bean.getEn7Rel1Id(), bean.getEn7Rel1Label(), bean.getEn7Rel1Label(), bean.getEn7Rel2Id(), bean.getEn7Rel2Label(), bean.getEn7Rel2Label(), bean.getEn7Rel2LabelEN(), true, true, bean.getYrLabel());
				}
				if (bean.getRsRank8() != null) {
					tEntity[7] = HtmlConverter.getResultsEntity(type_, bean.getRsRank8(), bean.getEn8Str1(), bean.getEn8Str2(), bean.getEn8Str3(), bean.getEn8Rel2Code(), bean.getYrLabel(), null);
					tEntityRel[7] = HtmlConverter.getResultsEntityRel(bean.getEn8Rel1Id(), bean.getEn8Rel1Label(), bean.getEn8Rel1Label(), bean.getEn8Rel2Id(), bean.getEn8Rel2Label(), bean.getEn8Rel2Label(), bean.getEn8Rel2LabelEN(), true, true, bean.getYrLabel());
				}
				if (bean.getRsRank9() != null) {
					tEntity[8] = HtmlConverter.getResultsEntity(type_, bean.getRsRank9(), bean.getEn9Str1(), bean.getEn9Str2(), bean.getEn9Str3(), bean.getEn9Rel2Code(), bean.getYrLabel(), null);
					tEntityRel[8] = HtmlConverter.getResultsEntityRel(bean.getEn9Rel1Id(), bean.getEn9Rel1Label(), bean.getEn9Rel1Label(), bean.getEn9Rel2Id(), bean.getEn9Rel2Label(), bean.getEn9Rel2Label(), bean.getEn9Rel2LabelEN(), true, true, bean.getYrLabel());
				}
				boolean isDouble = (type_ == 4 || (bean.getRsComment() != null && bean.getRsComment().equals("#DOUBLE#")));
				boolean isTriple = (type_ == 5 || (bean.getRsComment() != null && bean.getRsComment().equals("#TRIPLE#")));
				HtmlConverter.setTies(HtmlConverter.getTieList(isDouble, isTriple, bean.getRsExa()), type_, tEntity, tEntityRel, null);
				if (isTriple || isDouble) {
					tEntity = StringUtils.removeNulls(tEntity);
					tEntityRel = StringUtils.removeNulls(tEntityRel);
				}
				org.jsoup.nodes.Document d = null;
				for (int i = 0 ; i < MAX_RANKS ; i++) {
					if (StringUtils.notEmpty(tEntity[i])) {
						d = Jsoup.parse(tEntity[i]);
						StringBuffer sb = new StringBuffer();
						for (org.jsoup.nodes.Element e : d.getElementsByTag("a"))
							sb.append(sb.toString().length() > 0 ? "|" : "").append(e.text());
						tEntity[i] = sb.toString();
						Elements imgs = d.getElementsByTag("img");
						if (imgs != null && imgs.size() > 0)
							tEntityImg[i] = imgs.get(0).attr("src");
						
						if (StringUtils.notEmpty(tEntityRel[i])) {
							d = Jsoup.parse(tEntityRel[i]);
							if (tEntityImg[i] == null) {
								sb = new StringBuffer();
								for (org.jsoup.nodes.Element e : d.getElementsByTag("img"))
									sb.append(sb.toString().length() > 0 ? "|" : "").append(e.attr("src"));
								tEntityImg[i] = sb.toString();	
							}
							sb = new StringBuffer();
							for (org.jsoup.nodes.Element e : d.getElementsByTag("a"))
								sb.append(sb.toString().length() > 0 ? "|" : "").append(e.text());
							tEntityRel[i] = sb.toString();
						}
							
						Element rank = root.addElement("rank" + (i + 1));
						rank.addAttribute("img", tEntityImg[i]);
						rank.addAttribute("result", tResult[i]);
						rank.addAttribute("rel", tEntityRel[i]);
						rank.addText(tEntity[i]);
					}
				}
			}
        }
	}
	
	private void processCalendar(Document doc, Element root, String[] t, String lang) throws Exception {
		String code = t[0];
//		String label = "label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? "_" + lang : "");

		if (code.equalsIgnoreCase(Year.alias)) {
			String sql = "SELECT id, label FROM year ORDER BY id DESC";
        	addItems(doc, root, (short)-1, DatabaseManager.getPicklist(sql, null), null, null, null, null);
		}
		else if (code.equalsIgnoreCase("MT")) {
			ArrayList<PicklistItem> picklist = new ArrayList<PicklistItem>();
			for (int i = 1 ; i <= 12 ; i++)
				picklist.add(new PicklistItem(i, ResourceUtils.getText("month." + i, lang)));
        	addItems(doc, root, (short)-1, picklist, null, null, null, null);
		}
		else if (code.equalsIgnoreCase(Calendar.alias)) {
			List<Object> params = new ArrayList<Object>();
			params.add(t[1]);
			params.add(t[2]);
			params.add(0);
			params.add("_" + lang);
			Collection<RefItem> c = (Collection<RefItem>) DatabaseManager.callFunctionSelect("get_calendar_results", params, RefItem.class);
			for (RefItem item : c) {
				Element item_ = root.addElement("item");
				item_.addAttribute("id", String.valueOf(item.getIdItem()));
				item_.addAttribute("sport", item.getLabelRel2());
				item_.addAttribute("event", item.getLabelRel3() + (StringUtils.notEmpty(item.getLabelRel4()) ? "|" + item.getLabelRel4() : "") + (StringUtils.notEmpty(item.getLabelRel5()) ? "|" + item.getLabelRel5() : "") + (StringUtils.notEmpty(item.getLabelRel18()) ? "|" + item.getLabelRel18() : ""));
				item_.addAttribute("dates", ((StringUtils.notEmpty(item.getDate1()) ? StringUtils.toTextDate(item.getDate1(), lang, "d MMM yyyy") + "-" : "") + (StringUtils.notEmpty(item.getDate2()) ? StringUtils.toTextDate(item.getDate2(), lang, "d MMM yyyy") : "")));
			}
		}
	}
	
	private void processOlympics(Document doc, Element root, String[] t, String lang) throws Exception {
		String code = t[0];
		String label = "label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? "_" + lang : "");
		if (code.equalsIgnoreCase(Olympics.alias)) {
			String sql = "SELECT OL.id, CT." + label + " || ', ' || YR.label "
					+ " FROM olympics OL "
					+ " JOIN city CT ON CT.id_city = OL.id_city"
					+ " JOIN year YR ON YR.id_year = OL.id_year"
					+ " where OL.type = ? ORDER BY OL.id_year desc";
        	addItems(doc, root, ImageUtils.INDEX_OLYMPICS, DatabaseManager.getPicklist(sql, Arrays.asList(t[1])), null, null, null, null);
        }
		else if (code.matches("SP|EV|SE|SE2")) {
			String where = "WHERE CP.id=1 AND SP.type=" + t[1] + " AND OL.id = " + t[2];
			if (code.matches("EV|SE|SE2"))
				where += " AND SP.id = " + t[3];
			if (code.matches("SE|SE2"))
				where += " AND EV.id = " + t[4];
			if (code.matches("SE2"))
				where += " AND SE.id = " + t[5];
			List<Object> params = new ArrayList<Object>();
			params.add(where);
			params.add("_" + lang);
			ArrayList<PicklistItem> lPicklist = new ArrayList<PicklistItem>();
			for (TreeItem item : (List<TreeItem>) DatabaseManager.callFunctionSelect("tree_results", params, TreeItem.class))
				if ((item.getLevel() == 1 && code.equalsIgnoreCase(Sport.alias)) || (item.getLevel() == 3 && code.equalsIgnoreCase(Event.alias)) || (item.getLevel() == 4 && code.equalsIgnoreCase("SE")) || (item.getLevel() == 5 && code.equalsIgnoreCase("SE2")))
					lPicklist.add(new PicklistItem(item.getIdItem(), item.getLabel()));
			addItems(doc, root, code.equalsIgnoreCase(Sport.alias) ? ImageUtils.INDEX_SPORT : ImageUtils.INDEX_EVENT, lPicklist, null, null, null, null);
		}
		else if (code.equalsIgnoreCase(Result.alias)) {
			List<Object> params = new ArrayList<Object>();
			params.add(t[2]);
			params.add(Integer.parseInt(t[3]));
			params.add(t.length > 4 ? t[4] : "0");
			params.add(t.length > 5 ? t[5] : "0");
			params.add(t.length > 6 ? t[6] : "0");
			params.add("_" + lang);
			addOlympicMedalsItems(doc, root, (Collection<OlympicMedalsBean>) DatabaseManager.callFunction("get_olympic_medals", params, OlympicMedalsBean.class), lang);
		}
		else if (code.equalsIgnoreCase(OlympicRanking.alias)) {
			List<Object> params = new ArrayList<Object>();
			params.add(t[2]);
			params.add("0");
			params.add("_" + lang);
			for (OlympicRankingsBean bean : (Collection<OlympicRankingsBean>) DatabaseManager.callFunction("get_olympic_rankings", params, OlympicRankingsBean.class)) {
				Element item = root.addElement("item");
				item.addAttribute("country", bean.getCn1Label());
				item.addAttribute("gold", String.valueOf(bean.getOrCountGold()));
				item.addAttribute("silver", String.valueOf(bean.getOrCountSilver()));
				item.addAttribute("bronze", String.valueOf(bean.getOrCountBronze()));
			}
		}
	}
	
	private void processUSLeagues(Document doc, Element root, String[] t, String lang) throws Exception {
		String code = t[0];
		short league = Short.parseShort(t[1]);
		String uslStatEvLabel = ConfigUtils.getValue("USL_STATS_EVENT_LABEL");

		if (code.equalsIgnoreCase(Team.alias)) {
			String sql = "SELECT TM.id, TM.label"
					+ " FROM team TM "
					+ " WHERE TM.id IN (SELECT id_team FROM retired_number WHERE id_league = ?) "
					+ " ORDER by TM.label";
			addItems(doc, root, ImageUtils.INDEX_TEAM, DatabaseManager.getPicklist(sql, Arrays.asList(league)), null, null, null, null);
		}
		else if (code.equalsIgnoreCase(Year.alias)) {
			if (t[2].equals(USLeaguesServlet.TYPE_HOF)) {
				String sql = "SELECT YR.id, YR.label"
						+ " FROM year YR "
						+ " WHERE YR.id IN (SELECT id_year FROM hall_of_fame WHERE id_league = ?) "
						+ " ORDER by YR.id DESC";
				addItems(doc, root, (short)-1, DatabaseManager.getPicklist(sql, Arrays.asList(league)), null, null, null, null);
			}
			else if (t[2].equals(USLeaguesServlet.TYPE_CHAMPIONSHIP)) {
				String sql = "SELECT YR.id, YR.label"
						+ " FROM year YR "
						+ " WHERE YR.id IN (SELECT id_year FROM result WHERE id_championship = ?) "
						+ " ORDER by YR.id DESC";
				addItems(doc, root, (short)-1, DatabaseManager.getPicklist(sql, Arrays.asList(USLeaguesServlet.HLEAGUES.get(league))), null, null, null, null);
			}
			else if (t[2].equals(USLeaguesServlet.TYPE_STATS)) {
				String sql = "SELECT YR.id, YR.label"
						+ " FROM year YR "
						+ " WHERE YR.id IN (SELECT id_year FROM result RS JOIN event EV ON EV.id_event = RS.id_event WHERE id_championship = ? AND EV.label LIKE ?) "
						+ " ORDER by YR.id DESC";
				addItems(doc, root, (short)-1, DatabaseManager.getPicklist(sql, Arrays.asList(USLeaguesServlet.HLEAGUES.get(league), "%" + uslStatEvLabel + "%")), null, null, null, null);
			}
		}
		else if (code.equalsIgnoreCase(Event.alias)) {
			if (t[2].equals(USLeaguesServlet.TYPE_RECORD)) {
				String filter = (t[3].equals("i") ? " AND type1 = 'Individual'" : (t[3].equals("t") ? " AND type1 = 'Team'" : ""));
				String sql = "SELECT SE.id, SE.label"
						+ " FROM event SE "
						+ " WHERE SE.id IN (SELECT id_subevent FROM record WHERE id_championship = ?" + filter + ")"
						+ " ORDER by SE.label DESC";
				addItems(doc, root, ImageUtils.INDEX_EVENT, DatabaseManager.getPicklist(sql, Arrays.asList(USLeaguesServlet.HLEAGUES.get(league))), null, null, null, null);
			}
			else if (t[2].equals(USLeaguesServlet.TYPE_STATS)) {
				String filter = " AND EV.label LIKE ? AND TP.number" + (t[3].equals("i") ? " < 10" : " = 50");
				String sql = "SELECT SE2.id, SE2.label"
						+ " FROM event SE2 "
						+ " JOIN type TP ON TP.id_type = SE2.id_type "
						+ " WHERE SE2.id IN (SELECT id_subevent2 FROM result RS JOIN event EV ON EV.id_event = RS.id_event WHERE id_championship = ?" + filter + ")"
						+ " ORDER by SE2.label DESC";
				addItems(doc, root, ImageUtils.INDEX_EVENT, DatabaseManager.getPicklist(sql, Arrays.asList(USLeaguesServlet.HLEAGUES.get(league), "%" + uslStatEvLabel + "%")), null, null, null, null);
			}
		}
		else if (code.equalsIgnoreCase(USLeaguesServlet.TYPE_RETNUM)) {
			List<Object> params = new ArrayList<Object>();
			params.add(league);
			params.add(t[2]);
			params.add(t.length > 3 && StringUtils.notEmpty(t[3]) ? Short.valueOf(String.valueOf(t[3])) : -1);
			for (RetiredNumberBean bean : (Collection<RetiredNumberBean>) DatabaseManager.callFunction("get_retired_numbers", params, RetiredNumberBean.class)) {
				Element item = root.addElement("item");
				item.addAttribute("team", bean.getTmLabel());
				item.addAttribute("number", String.valueOf(bean.getRnNumber()));
				item.addAttribute("name", StringUtils.toFullName(bean.getPrLastName(), bean.getPrFirstName(), null, true));
				item.addAttribute("year", bean.getYrLabel());
			}
		}
		else if (code.equalsIgnoreCase(USLeaguesServlet.TYPE_TEAMSTADIUM)) {
			List<Object> params = new ArrayList<Object>();
			params.add(league);
			params.add(t[2]);
			params.add("_" + lang);
			for (TeamStadiumBean bean : (Collection<TeamStadiumBean>) DatabaseManager.callFunction("get_team_stadiums", params, TeamStadiumBean.class)) {
				Element item = root.addElement("item");
				item.addAttribute("team", bean.getTmLabel());
				item.addAttribute("complex", bean.getCxLabel());
				item.addAttribute("city", bean.getCtLabel() + (bean.getStId() != null ? ", " + bean.getStCode() : "") + ", " + bean.getCnCode());
				item.addAttribute("year1", bean.getTsDate1() != null ? String.valueOf(bean.getTsDate1()) : "");
				item.addAttribute("year2", bean.getTsDate2() != null ? String.valueOf(bean.getTsDate2()) : "");
				item.addAttribute("renamed", bean.getTsRenamed() != null && bean.getTsRenamed() ? "1" : "0");
			}
		}
		else if (code.equalsIgnoreCase(USLeaguesServlet.TYPE_STATS)) {
			List<Object> params = new ArrayList<Object>();
			params.add(USLeaguesServlet.HLEAGUES.get(league));
			params.add(t[2]);
			params.add(t[3]);
			params.add(t.length > 4 && String.valueOf(t[4]).equals("1") ? true : false);
			params.add(t.length > 5 && String.valueOf(t[5]).equals("1") ? true : false);
			params.add("_en");
			for (YearlyStatsBean bean : (Collection<YearlyStatsBean>) DatabaseManager.callFunction("get_yearly_stats", params, YearlyStatsBean.class)) {
				Element item = root.addElement("item");
				if (bean.getTpLabel().equalsIgnoreCase("individual")) {
					item.addAttribute("individual", "1");
					item.addAttribute("rank1", StringUtils.toFullName(bean.getRsPerson1LastName(), bean.getRsPerson1FirstName(), null, true) + (bean.getRsIdPrTeam1() != null ? " (" + bean.getRsPrTeam1() + ")" : ""));
					item.addAttribute("rank2", StringUtils.toFullName(bean.getRsPerson2LastName(), bean.getRsPerson2FirstName(), null, true) + (bean.getRsIdPrTeam2() != null ? " (" + bean.getRsPrTeam2() + ")" : ""));
					item.addAttribute("rank3", StringUtils.toFullName(bean.getRsPerson3LastName(), bean.getRsPerson3FirstName(), null, true) + (bean.getRsIdPrTeam3() != null ? " (" + bean.getRsPrTeam3() + ")" : ""));
					item.addAttribute("rank4", StringUtils.toFullName(bean.getRsPerson4LastName(), bean.getRsPerson4FirstName(), null, true) + (bean.getRsIdPrTeam4() != null ? " (" + bean.getRsPrTeam4() + ")" : ""));
					item.addAttribute("rank5", StringUtils.toFullName(bean.getRsPerson5LastName(), bean.getRsPerson5FirstName(), null, true) + (bean.getRsIdPrTeam5() != null ? " (" + bean.getRsPrTeam5() + ")" : ""));
					item.addAttribute("rank6", StringUtils.toFullName(bean.getRsPerson6LastName(), bean.getRsPerson6FirstName(), null, true) + (bean.getRsIdPrTeam6() != null ? " (" + bean.getRsPrTeam6() + ")" : ""));
				}
				else {
					item.addAttribute("individual", "0");
					item.addAttribute("rank1", bean.getRsTeam1());
					item.addAttribute("rank2", bean.getRsTeam2());
					item.addAttribute("rank3", bean.getRsTeam3());
					item.addAttribute("rank4", bean.getRsTeam4());
					item.addAttribute("rank5", bean.getRsTeam5());
					item.addAttribute("rank6", bean.getRsTeam6());
				}
				item.addAttribute("result1", bean.getResult1());
				item.addAttribute("result2", bean.getResult2());
				item.addAttribute("result3", bean.getResult3());
				item.addAttribute("type", bean.getTpLabel());
				item.addAttribute("year", bean.getYrLabel());
				item.addAttribute("city", bean.getCtLabel());
			}
		}
		else if (code.equalsIgnoreCase(USLeaguesServlet.TYPE_HOF)) {
			List<Object> params = new ArrayList<Object>();
			params.add(league);
			params.add(t[2]);
			params.add(t.length > 3 && StringUtils.notEmpty(t[3]) ? Short.valueOf(String.valueOf(t[3])) : "");
			for (HallOfFameBean bean : (Collection<HallOfFameBean>) DatabaseManager.callFunction("get_hall_of_fame", params, HallOfFameBean.class)) {
				Element item = root.addElement("item");
				item.addAttribute("year", bean.getYrLabel());
				item.addAttribute("name", StringUtils.toFullName(bean.getPrLastName(), bean.getPrFirstName(), null, true));
				item.addAttribute("pos", String.valueOf(bean.getHfPosition()));
				item.addAttribute("year", bean.getYrLabel());
			}
		}
		else if (code.equalsIgnoreCase(USLeaguesServlet.TYPE_CHAMPIONSHIP)) {
			List<Object> params = new ArrayList<Object>();
			params.add(USLeaguesServlet.HLEAGUES.get(league));
			params.add(t[2]);
			params.add("_en");
			for (USChampionshipsBean bean : (Collection<USChampionshipsBean>) DatabaseManager.callFunction("get_us_championships", params, USChampionshipsBean.class)) {
				Element item = root.addElement("item");
				item.addAttribute("year", bean.getYrLabel());
				item.addAttribute("rank1", bean.getRsTeam1());
				item.addAttribute("img1", getImage(ImageUtils.INDEX_TEAM, bean.getRsRank1(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null));	
				item.addAttribute("rank2", bean.getRsTeam2());
				item.addAttribute("img2", getImage(ImageUtils.INDEX_TEAM, bean.getRsRank2(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null));
				item.addAttribute("result", bean.getRsResult());
				item.addAttribute("date1", StringUtils.toTextDate(bean.getRsDate1(), lang, null));
				item.addAttribute("date2", StringUtils.toTextDate(bean.getRsDate2(), lang, null));
			}
		}
		else if (code.equalsIgnoreCase(USLeaguesServlet.TYPE_RECORD)) {
			List<Object> params = new ArrayList<Object>();
			params.add(USLeaguesServlet.HLEAGUES.get(league));
			params.add(String.valueOf(t[2]).equals("1") ? "0" : "495");
			params.add(StringUtils.notEmpty(t[3]) ? t[3] : "0");
			params.add(StringUtils.notEmpty(t[4]) ? t[4] : "i");
			params.add(StringUtils.notEmpty(t[5]) && !t[5].equals("a") ? t[5] : "-");
			params.add("_en");
			params.set(3, USLeaguesServlet.HTYPE1.get(params.get(3)));
			params.set(4, USLeaguesServlet.HTYPE2.get(params.get(4)));
			if (String.valueOf(params.get(3)).matches(".*Team.*") && !String.valueOf(params.get(2)).equals("0")) {
				String sql = "SELECT id FROM event EV "
						+ " JOIN type TP ON TP.id_type = EV.id_type "
						+ " WHERE TP.number <= 50 AND EV.label IN (SELECT EV2.label FROM event EV2 WHERE EV2.id IN (" + String.valueOf(t[3]) + "))";
				List<String> lstSe = new ArrayList<String>();
				for (Integer i : (List<Integer>) DatabaseManager.executeSelect(sql, Integer.class))
					lstSe.add(String.valueOf(i));
				params.set(2, StringUtils.join(lstSe , ","));
			}
			for (USRecordsBean bean : (Collection<USRecordsBean>) DatabaseManager.callFunction("get_us_records", params, USRecordsBean.class)) {
				Element item = root.addElement("item");
				boolean isIndividual = bean.getRcType1().equalsIgnoreCase("individual");
				item.addAttribute("label", bean.getRcLabel());
				item.addAttribute("type1", bean.getRcType1());
				item.addAttribute("type2", bean.getRcType2());
				item.addAttribute("record", bean.getRcRecord1());
				if (isIndividual) {
					item.addAttribute("individual", "1");
					item.addAttribute("rank1", StringUtils.toFullName(bean.getRcPerson1LastName(), bean.getRcPerson1FirstName(), null, true) + (bean.getRcIdPrTeam1() != null ? " (" + bean.getRcPrTeam1() + ")" : ""));
					item.addAttribute("rank2", StringUtils.toFullName(bean.getRcPerson2LastName(), bean.getRcPerson2FirstName(), null, true) + (bean.getRcIdPrTeam2() != null ? " (" + bean.getRcPrTeam2() + ")" : ""));
					item.addAttribute("rank3", StringUtils.toFullName(bean.getRcPerson3LastName(), bean.getRcPerson3FirstName(), null, true) + (bean.getRcIdPrTeam3() != null ? " (" + bean.getRcPrTeam3() + ")" : ""));
					item.addAttribute("rank4", StringUtils.toFullName(bean.getRcPerson4LastName(), bean.getRcPerson4FirstName(), null, true) + (bean.getRcIdPrTeam4() != null ? " (" + bean.getRcPrTeam4() + ")" : ""));
					item.addAttribute("rank5", StringUtils.toFullName(bean.getRcPerson5LastName(), bean.getRcPerson5FirstName(), null, true) + (bean.getRcIdPrTeam5() != null ? " (" + bean.getRcPrTeam5() + ")" : ""));
				}
				else {
					item.addAttribute("individual", "0");
					item.addAttribute("rank1", bean.getRcTeam1());
					item.addAttribute("rank2", bean.getRcTeam2());
					item.addAttribute("rank3", bean.getRcTeam3());
					item.addAttribute("rank4", bean.getRcTeam4());
					item.addAttribute("rank5", bean.getRcTeam5());
				}
				item.addAttribute("date1", (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + " " : "") + (bean.getRcDate1() != null ? bean.getRcDate1() : StringUtils.EMPTY));
				item.addAttribute("date2", (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + " " : "") + (bean.getRcDate2() != null ? bean.getRcDate2() : StringUtils.EMPTY));
				item.addAttribute("date3", (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + " " : "") + (bean.getRcDate3() != null ? bean.getRcDate3() : StringUtils.EMPTY));
				item.addAttribute("date4", (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + " " : "") + (bean.getRcDate4() != null ? bean.getRcDate4() : StringUtils.EMPTY));
				item.addAttribute("date5", (bean.getEvLabel() != null && bean.getEvLabel().equals("Super Bowl") ? bean.getEvLabel() + " " : "") + (bean.getRcDate5() != null ? bean.getRcDate5() : StringUtils.EMPTY));
			}
		}
	}
	
	public static String getImage(short type, Object id, char size, String year, String title) {
		String html = HtmlUtils.writeImage(type, id, size, year, title);
		return html.replaceAll(".*\\ssrc\\=\\'|\\'\\/\\>", "");
	}
	
	private static String toImgURL(String s) {
		return (s != null ? s.replaceAll(".*src\\='|'\\/\\>", "") : "");
	}
	
	private void addItems(Document doc, Element root, short index, Collection<PicklistItem> picklist, List<String> lInactive, Object spid, String currentPath, String subcountSQL) throws Exception {
		if (picklist != null && picklist.size() > 0) {
			for (PicklistItem plb : picklist) {
				Element item = root.addElement("item");
				int id = plb.getValue();
				String text = plb.getText();
				if (lInactive != null && lInactive.contains(currentPath + "-" + id))
					text = "+" + text;
				item.addAttribute("value", String.valueOf(id));
				item.addAttribute("text", text);
				if (index > -1) {
					String img = HtmlUtils.writeImage(index, (spid != null ? spid + "-" : "") + plb.getValue(), ImageUtils.SIZE_LARGE, null, null);
					item.addAttribute("img", toImgURL(img));
				}
				Integer n = 1;
				if (subcountSQL != null) {
					List<Integer> ln = (List<Integer>) DatabaseManager.executeSelect(subcountSQL.replace("#ID#", String.valueOf(id)), Integer.class);
					n = ln.get(0);
				}
				item.addAttribute("subcount", String.valueOf(n));
			}
		}
	}
	
	private void addResultItems(Document doc, Element root, Event ev, Collection<ResultsBean> list, String lang) {
		if (list != null && list.size() > 0) {
			List<String> lIds = new ArrayList<String>();
			Integer tp = ev.getType().getNumber();
			for (ResultsBean bean : list) {
				boolean isDouble = (tp == 4 || (bean.getRsComment() != null && bean.getRsComment().equals("#DOUBLE#")));
				boolean isTriple = (tp == 5 || (bean.getRsComment() != null && bean.getRsComment().equals("#TRIPLE#")));
				Element item = root.addElement("item");
				lIds.add(String.valueOf(bean.getRsId()));
				item.addAttribute("id", String.valueOf(bean.getRsId()));
				item.addAttribute("year", bean.getYrLabel());
				item.addAttribute("type", String.valueOf(tp));
				item.addAttribute("str1", bean.getEn1Str1()); item.addAttribute("str2", bean.getEn1Str2()); item.addAttribute("str3", bean.getEn1Str3());
				item.addAttribute("str4", bean.getEn2Str1()); item.addAttribute("str5", bean.getEn2Str2()); item.addAttribute("str6", bean.getEn2Str3());
				item.addAttribute("str7", bean.getEn3Str1()); item.addAttribute("str8", bean.getEn3Str2()); item.addAttribute("str9", bean.getEn3Str3());
				item.addAttribute("tie1", isDouble ? "1" : "0");
				item.addAttribute("tie2", isTriple ? "1" : "0");
				item.addAttribute("rs1", bean.getRsResult1()); item.addAttribute("rs2", bean.getRsResult2()); item.addAttribute("rs3", bean.getRsResult3());
				item.addAttribute("score", bean.getRsRank1() != null && bean.getRsRank2() != null && StringUtils.notEmpty(bean.getRsResult1()) && !StringUtils.notEmpty(bean.getRsResult2()) && !StringUtils.notEmpty(bean.getRsResult3()) && !StringUtils.notEmpty(bean.getRsResult4()) && !StringUtils.notEmpty(bean.getRsResult5()) ? "1" : "0");
				try {
					StringBuffer sbCode = new StringBuffer();
					StringBuffer sbImg = new StringBuffer();
					for (int i = 1 ; i <= 3 ; i++) {
						Method m = ResultsBean.class.getMethod("getRsRank" + i);
						Object o = m.invoke(bean);
						if (o != null) {
							Integer id = (Integer) o;
							String img = null;
							if (tp < 10) {
								Integer tm = StringUtils.toInt(ResultsBean.class.getMethod("getEn" + i + "Rel1Id").invoke(bean));
								Integer cn = StringUtils.toInt(ResultsBean.class.getMethod("getEn" + i + "Rel2Id").invoke(bean));
								if (tm != null && tm> 0)
									img = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, tm, ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
								else {
									img = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, cn, ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
									sbCode.append(sbCode.toString().length() > 0 ? "|" : "").append(ResultsBean.class.getMethod("getEn" + i + "Rel2Code").invoke(bean));
								}
							}
							else if (tp == 50)
								img = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, id, ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
							else if (tp == 99)
								img = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, id, ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
							sbImg.append(sbImg.toString().length() > 0 ? "|" : "").append(toImgURL(img));
							
						}
					}
					item.addAttribute("code", sbCode.toString());
					item.addAttribute("img", sbImg.toString());
				}
				catch (Exception e) {
					log.log(Level.WARNING, e.getMessage(), e);
				}
			}
			// Win records
			try {
				List<Object> lParams = new ArrayList<Object>();
				lParams.add(StringUtils.join(lIds, ","));
				lParams.add("_" + lang);
				List<RefItem> list_ = (List<RefItem>) DatabaseManager.callFunctionSelect("win_records", lParams, RefItem.class);
				if (list_ != null && list_.size() > 0) {
					RefItem item = list_.get(0);
					String str = item.getLabel();
					if (item.getIdRel1() < 10) {
						String[] t = str.split("\\,\\s", -1);
						str = StringUtils.toFullName(t[0], t[1], item.getLabelRel1(), true);
					}
					root.addAttribute("winrec-name", str);
					root.addAttribute("winrec-count", String.valueOf(item.getCount1()));
				}
			}
			catch (Exception e) {
				log.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}

	private void addOlympicMedalsItems(Document doc, Element root, Collection<OlympicMedalsBean> list, String lang) {
		if (list != null && list.size() > 0) {
			for (OlympicMedalsBean bean : list) {
				Element item = root.addElement("item");
				boolean isIndividual = ((bean.getTp2Number() != null ? bean.getTp2Number() : bean.getTp1Number()) <= 10);
				item.addAttribute("id", String.valueOf(bean.getRsId()));
				item.addAttribute("event1", bean.getEvLabel());
				item.addAttribute("event2", StringUtils.notEmpty(bean.getSeLabel()) ? bean.getSeLabel() : "");
				item.addAttribute("event3", StringUtils.notEmpty(bean.getSe2Label()) ? bean.getSe2Label() : "");
				item.addAttribute("rank1", isIndividual ? StringUtils.toFullName(bean.getPr1LastName(), bean.getPr1FirstName(), bean.getPr1CnCode(), true) : bean.getCn1Label());
				item.addAttribute("rank2", isIndividual ? StringUtils.toFullName(bean.getPr2LastName(), bean.getPr2FirstName(), bean.getPr2CnCode(), true) : bean.getCn2Label());
				item.addAttribute("rank3", isIndividual ? StringUtils.toFullName(bean.getPr3LastName(), bean.getPr3FirstName(), bean.getPr3CnCode(), true) : bean.getCn3Label());
				item.addAttribute("img1", toImgURL(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, isIndividual ? bean.getPr1CnId() : bean.getRsRank1(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null)));
				item.addAttribute("img2", toImgURL(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, isIndividual ? bean.getPr2CnId() : bean.getRsRank2(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null)));
				item.addAttribute("img3", toImgURL(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, isIndividual ? bean.getPr3CnId() : bean.getRsRank3(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null)));
				item.addAttribute("prcn1", isIndividual ? bean.getPr1CnCode() : "");
				item.addAttribute("prcn2", isIndividual ? bean.getPr2CnCode() : "");
				item.addAttribute("prcn3", isIndividual ? bean.getPr3CnCode() : "");
				item.addAttribute("venue", bean.getCxId() != null ? bean.getCxLabel() + ", " + bean.getCt1Label() + ", " + bean.getCn1Code_() : "");
				item.addAttribute("venueimg", bean.getCxId() != null ? toImgURL(HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getCn1Id(), ImageUtils.SIZE_SMALL, null, null)) : "");
			}
		}
	}
	
}