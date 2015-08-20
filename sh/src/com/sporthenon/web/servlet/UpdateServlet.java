package com.sporthenon.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Draw;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.League;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.Type;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Config;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.entity.meta.ExternalLink;
import com.sporthenon.db.entity.meta.PersonList;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class UpdateServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	private static final int MAX_RANKS = 20;
	private static final int MAX_AUTOCOMPLETE_RESULTS = 100;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String lang = getLocale(request);
			Contributor user = getUser(request);
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			if (hParams.containsKey("p2") && hParams.get("p2").equals("ajax"))
				ajaxAutocomplete(response, hParams, lang, user);
			else if (hParams.containsKey("p") && hParams.get("p").equals("save"))
				saveResult(response, hParams, lang, user);
			else if (hParams.containsKey("p2") && hParams.get("p2").equals("data"))
				dataTips(response, hParams, lang, user);
			else if (hParams.containsKey("p") && hParams.get("p").equals("load-entity"))
				loadEntity(response, hParams, lang, user);
			else if (hParams.containsKey("p") && hParams.get("p").equals("save-entity"))
				saveEntity(response, hParams, lang, user);
			else if (hParams.containsKey("p") && hParams.get("p").equals("load-overview"))
				loadOverview(response, hParams, lang, user);
			else if (hParams.containsKey("p") && hParams.get("p").equals("save-config"))
				saveConfig(response, hParams, lang, user);
			else if (hParams.containsKey("p") && hParams.get("p").equals("execute-query"))
				executeQuery(response, hParams, lang, user);
			else
				loadResult(request, response, hParams, lang, user);
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
	private static void ajaxAutocomplete(HttpServletResponse response, Map hParams, String lang, Contributor user) throws Exception {
		String field = String.valueOf(hParams.get("p"));
		String field_ = field;
		String value = (hParams.get("value") + "%").replaceAll("\\*", "%").replaceAll("'", "''");
		String sport = null;
		boolean isData = field.matches(".*\\-l$");
		if (isData) {
			field = field.substring(3).replaceAll("\\-l$", "");
			field_ = field_.replaceFirst("\\-l$", "");
			if (field.equals("link"))
				field = (field_.startsWith("pr") ? "pr" : (field_.startsWith("cx") ? "cx" : (field_.startsWith("ct") ? "ct" : (field_.startsWith("tm") ? "tm" : ""))));
		}
		else if (field.matches("(pr|tm|cn)\\-.*")) {
			String[] t = field.split("\\-", -1);
			field = t[0];
			sport = t[1];
			field_ = t[0];
		}
		HashMap<String, String> hTable = new HashMap<String, String>();
		hTable.put("sp", "Sport"); hTable.put("sport", "Sport");
		hTable.put("cp", "Championship"); hTable.put("championship", "Championship");
		hTable.put("ev", "Event"); hTable.put("event", "Event");
		hTable.put("se", "Event"); hTable.put("event", "Event");
		hTable.put("se2", "Event"); hTable.put("event", "Event");
		hTable.put("yr", "Year"); hTable.put("year", "Year");
		hTable.put("pl1", "Complex"); hTable.put("complex", "Complex");
		hTable.put("pl2", "Complex"); hTable.put("complex", "Complex");
		hTable.put("pr", "Athlete"); hTable.put("athlete", "Athlete");
		hTable.put("tm", "Team"); hTable.put("team", "Team");
		hTable.put("cn", "Country"); hTable.put("country", "Country");
		hTable.put("cb", "Contributor"); hTable.put("contributor", "Contributor");
		hTable.put("rs", "Result"); hTable.put("result", "Result");
		String labelHQL = "lower(label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) && !field.matches("tm|yr|team|year") ? lang.toUpperCase() : "") + ")";
		String whereHQL = "";
		if (field.equalsIgnoreCase(Athlete.alias)) {
			labelHQL = "lower(last_name) || ', ' || lower(first_name) || ' (' || lower(country.code) || ')'";
			whereHQL = (sport != null ? " and sport.id=" + sport : "");
		}
		else if (field.equalsIgnoreCase(Team.alias))
			whereHQL = (sport != null ? " and sport.id=" + sport : "");
		else if (field.equalsIgnoreCase(Sport.alias) && user != null && StringUtils.notEmpty(user.getSports()))
			whereHQL = (" and id in (" + user.getSports() + ")");
		else if (field.equalsIgnoreCase(Contributor.alias))
			labelHQL = "lower(login)";
		else if (field.equalsIgnoreCase(Result.alias)) {
			String l = "label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? lang.toUpperCase() : "");
			value = "%" + value;
			// TODO coalesce(subevent." + l + ", ''), coalesce(subevent2." + l + ", '')
			labelHQL = "concat(lower(sport." + l + "), ' - ', lower(championship." + l + "), ' - ', lower(event." + l + "), ' - ', year.label)";
		}
		List<Object> l = DatabaseHelper.execute("from " + hTable.get(field) + " where " + labelHQL + " like '" + value.toLowerCase() + "'" + whereHQL + " order by " + (field.equalsIgnoreCase(Result.alias) ? "year.id desc" : labelHQL));
		if (field.matches("pl\\d|complex"))
			l.addAll(DatabaseHelper.execute("from City where " + labelHQL + " like '" + value.toLowerCase() + "' order by " + labelHQL));
		StringBuffer html = new StringBuffer("<ul>");
		int n = 0;
		for (Object o : l) {
			if (n++ == MAX_AUTOCOMPLETE_RESULTS)
				break;
			Method m1 = o.getClass().getMethod("getId");
			String id = String.valueOf(m1.invoke(o));
			String text = null;
			if (!(o instanceof Athlete) && !(o instanceof Team) && !(o instanceof Result) && !(o instanceof Contributor)) {
				Method m2 = o.getClass().getMethod("getLabel", String.class);
				text = String.valueOf(m2.invoke(o, lang));	
			}
			if (o instanceof Event) {
				Event e = (Event) o;
				text += " (" + e.getType().getLabel(lang) + ")";
			}
			else if (o instanceof Complex) {
				Complex c = (Complex) o;
				text += ", " + c.getCity().getLabel(lang) + (c.getCity().getState() != null ? ", " + c.getCity().getState().getCode() : "") + ", " + c.getCity().getCountry().getCode();
			}
			else if (o instanceof City) {
				City c = (City) o;
				text += (c.getState() != null ? ", " + c.getState().getCode() : "") + ", " + c.getCountry().getCode();
			}
			else if (o instanceof Contributor) {
				Contributor c = (Contributor) o;
				text = c.getLogin();
			}
			else if (o instanceof Athlete) {
				Athlete a = (Athlete) o;
				text = a.toString2() + (isData ? " [#" + a.getId() + "]" : "");
			}
			else if (o instanceof Team) {
				Team t = (Team) o;
				text = t.toString2() + (isData ? (StringUtils.notEmpty(t.getYear1()) ? " [" + t.getYear1() + "]" : "") + (StringUtils.notEmpty(t.getYear2()) ? " [" + t.getYear2() + "]" : "") + " [#" + t.getId() + "]" : "");;
			}
			else if (o instanceof Result) {
				Result r = (Result) o;
				text = r.toString2(lang);
			}
			html.append("<li id='" + field_ + "|" + id + (o instanceof Event ? "|" + ((Event)o).getType().getNumber() : "") + "'>" + text + "</li>");
		}
		ServletHelper.writeText(response, html.append("</ul>").toString());
	}
	
	private static void saveResult(HttpServletResponse response, Map hParams, String lang, Contributor user) throws Exception {
		HashMap<Object, Integer> hInserted = new HashMap<Object, Integer>();
		StringBuffer sbMsg = new StringBuffer();
		try {
			int tp = 0;
			HashMap<String, Type> hType = new HashMap<String, Type>();
			for (Type type : (List<Type>) DatabaseHelper.execute("from Type"))
				hType.put(type.getLabel(lang), type);
			Integer idRS = (StringUtils.notEmpty(hParams.get("id")) ? Integer.valueOf(String.valueOf(hParams.get("id"))) : null);
			Result result = (idRS != null ? (Result)DatabaseHelper.loadEntity(Result.class, idRS) : new Result());
			// Sport
			result.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, hParams.get("sp")));
			if (result.getSport() == null) {
				Sport s = new Sport();
				s.setLabel(String.valueOf(hParams.get("sp-l")));
				s.setLabelFr(s.getLabel());
				s.setType(1);
				s.setIndex(Float.MAX_VALUE);
				s = (Sport) DatabaseHelper.saveEntity(s, user);
				result.setSport(s);
			}
			// Championship
			result.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, hParams.get("cp")));
			if (result.getChampionship() == null) {
				Championship c = new Championship();
				c.setLabel(String.valueOf(hParams.get("cp-l")));
				c.setLabelFr(c.getLabel());
				c.setIndex(Integer.MAX_VALUE);
				c = (Championship) DatabaseHelper.saveEntity(c, user);
				result.setChampionship(c);
			}
			// Event #1
			result.setEvent((Event)DatabaseHelper.loadEntity(Event.class, hParams.get("ev")));
			if (result.getEvent() == null) {
				String label_ = String.valueOf(hParams.get("ev-l"));
				Type type_ = (Type)DatabaseHelper.loadEntity(Type.class, 1);
				if (label_.contains(" (")) {
					String[] t_ = label_.replaceAll("\\)", "").split("\\s\\(");
					label_ = t_[0];
					type_ = (hType.containsKey(t_[1]) ? hType.get(t_[1]) : type_);
				}
				Event e = new Event();
				e.setLabel(label_);
				e.setLabelFr(label_);
				e.setType(type_);
				e.setIndex(Integer.MAX_VALUE);
				e = (Event) DatabaseHelper.saveEntity(e, user);
				result.setEvent(e);
			}
			tp = result.getEvent().getType().getNumber();
			// Event #2
			if (StringUtils.notEmpty(hParams.get("se")) || StringUtils.notEmpty(hParams.get("se-l"))) {
				result.setSubevent((Event)DatabaseHelper.loadEntity(Event.class, hParams.get("se")));
				if (result.getSubevent() == null) {
					String label_ = String.valueOf(hParams.get("se-l"));
					Type type_ = (Type)DatabaseHelper.loadEntity(Type.class, 1);
					if (label_.contains(" (")) {
						String[] t_ = label_.replaceAll("\\)", "").split("\\s\\(");
						label_ = t_[0];
						type_ = (hType.containsKey(t_[1]) ? hType.get(t_[1]) : type_);
					}
					Event e = new Event();
					e.setLabel(label_);
					e.setLabelFr(label_);
					e.setType(type_);
					e.setIndex(Integer.MAX_VALUE);
					e = (Event) DatabaseHelper.saveEntity(e, user);
					result.setSubevent(e);
				}
				tp = result.getSubevent().getType().getNumber();
			}
			// Event #3
			if (StringUtils.notEmpty(hParams.get("se2")) || StringUtils.notEmpty(hParams.get("se2-l"))) {
				result.setSubevent2((Event)DatabaseHelper.loadEntity(Event.class, hParams.get("se2")));
				if (result.getSubevent2() == null) {
					String label_ = String.valueOf(hParams.get("se2-l"));
					Type type_ = (Type)DatabaseHelper.loadEntity(Type.class, 1);
					if (label_.contains(" (")) {
						String[] t_ = label_.replaceAll("\\)", "").split("\\s\\(");
						label_ = t_[0];
						type_ = (hType.containsKey(t_[1]) ? hType.get(t_[1]) : type_);
					}
					Event e = new Event();
					e.setLabel(label_);
					e.setLabelFr(label_);
					e.setType(type_);
					e.setIndex(Integer.MAX_VALUE);
					e = (Event) DatabaseHelper.saveEntity(e, user);
					result.setSubevent2(e);
				}
				tp = result.getSubevent2().getType().getNumber();
			}
			// Year
			result.setYear((Year)DatabaseHelper.loadEntity(Year.class, hParams.get("yr")));
			if (result.getYear() == null) {
				Year y = new Year();
				y.setLabel(String.valueOf(hParams.get("yr-l")));
				y = (Year) DatabaseHelper.saveEntity(y, user);
				result.setYear(y);
			}
			// Places
			for(int i : new int[]{1, 2}) {
				if (StringUtils.notEmpty(hParams.get("pl" + i + "-l"))) {
					String[] t = String.valueOf(hParams.get("pl" + i + "-l")).toLowerCase().split("\\,\\s");
					boolean isComplex = false;
					String st = null;
					if (t.length > 2 && t[t.length - 2].length() == 2)
						st = t[t.length - 2];
					if (t.length > (st != null ? 3 : 2))
						isComplex = true;
					int id = 0;
					if (StringUtils.notEmpty(hParams.get("pl" + i)))
						id = new Integer(String.valueOf(hParams.get("pl" + i)));
					else
						id = DatabaseHelper.insertPlace(0, String.valueOf(hParams.get("pl" + i + "-l")), user, null, lang);
					if (isComplex) {
						if (i == 1) {
							result.setComplex1((Complex)DatabaseHelper.loadEntity(Complex.class, id));
							result.setCity1(null);
						}
						else {
							result.setComplex2((Complex)DatabaseHelper.loadEntity(Complex.class, id));
							result.setCity2(null);
						}
					}
					else {
						if (i == 1) {
							result.setCity1((City)DatabaseHelper.loadEntity(City.class, id));
							result.setComplex1(null);
						}
						else {
							result.setCity2((City)DatabaseHelper.loadEntity(City.class, id));
							result.setComplex2(null);
						}
					}
				}
			}
			result.setDate1(StringUtils.notEmpty(hParams.get("dt1-l")) ? String.valueOf(hParams.get("dt1-l")) : null);
			result.setDate2(StringUtils.notEmpty(hParams.get("dt2-l")) ? String.valueOf(hParams.get("dt2-l")) : null);
			result.setComment(StringUtils.notEmpty(hParams.get("cmt-l")) ? String.valueOf(hParams.get("cmt-l")) : null);
			result.setExa(StringUtils.notEmpty(hParams.get("exa-l")) ? String.valueOf(hParams.get("exa-l")) : null);
			// Rankings
			for (int i = 1 ; i <= MAX_RANKS ; i++) {
				Integer id = (StringUtils.notEmpty(hParams.get("rk" + i)) ? new Integer(String.valueOf(hParams.get("rk" + i))) : 0);
				Object o = hParams.get("rk" + i + "-l");
				if (id == 0 && StringUtils.notEmpty(o)) {
					if (hInserted.keySet().contains(o))
						id = hInserted.get(o);
					else {
						id = DatabaseHelper.insertEntity(0, tp, result.getSport() != null ? result.getSport().getId() : 0, String.valueOf(o), null, user, null, lang);
						hInserted.put(o, id);
					}
				}
				Result.class.getMethod("setIdRank" + i, Integer.class).invoke(result, id > 0 ? id : null);
				Result.class.getMethod("setResult" + i, String.class).invoke(result, StringUtils.notEmpty(hParams.get("rs" + i + "-l")) ? hParams.get("rs" + i + "-l") : null);
			}
			result = (Result) DatabaseHelper.saveEntity(result, user);
			// External links
			if (StringUtils.notEmpty(hParams.get("exl-l")))
				DatabaseHelper.saveExternalLinks(Result.alias, result.getId(), String.valueOf(hParams.get("exl-l")));
			// Person List
			if (hParams.containsKey("rk1list")) {
				int i = 1;
				DatabaseHelper.executeUpdate("DELETE FROM \"~PersonList\" WHERE ID_RESULT=" + result.getId());
				while (hParams.containsKey("rk" + i + "list")) {
					String[] t = String.valueOf(hParams.get("rk" + i + "list")).split("\\|", 0);
					for (String value : t) {
						if (StringUtils.notEmpty(value) && !value.equals("null") && !value.startsWith("Name #")) {
							PersonList plist = new PersonList();
							plist.setIdResult(result.getId());
							plist.setRank(i);
							if (value.matches("\\d+"))
								plist.setIdPerson(Integer.parseInt(value));
							else
								plist.setIdPerson(DatabaseHelper.insertEntity(0, tp, result.getSport() != null ? result.getSport().getId() : 0, value, null, user, null, lang));
							DatabaseHelper.saveEntity(plist, user);
						}
					}
					i++;
				}
			}
			// Draws
			if (StringUtils.notEmpty(hParams.get("qf1w-l"))) {
				Integer idDR = (StringUtils.notEmpty(hParams.get("drid")) ? Integer.valueOf(String.valueOf(hParams.get("drid"))) : null);
				Draw draw = (idDR != null ? (Draw)DatabaseHelper.loadEntity(Draw.class, idDR) : new Draw());
				for (String s : new String[]{"qf1", "qf2", "qf3", "qf4", "sf1", "sf2", "thd"}) {
					Integer id1 = (StringUtils.notEmpty(hParams.get(s + "w")) ? new Integer(String.valueOf(hParams.get(s + "w"))) : 0);
					Integer id2 = (StringUtils.notEmpty(hParams.get(s + "l")) ? new Integer(String.valueOf(hParams.get(s + "l"))) : 0);
					Object ow = hParams.get(s + "w-l");
					Object ol = hParams.get(s + "l-l");
					if (id1 == 0 && StringUtils.notEmpty(ow)) {
						if (hInserted.keySet().contains(ow))
							id1 = hInserted.get(ow);
						else {
							id1 = DatabaseHelper.insertEntity(0, tp, result.getSport() != null ? result.getSport().getId() : 0, String.valueOf(ow), null, user, null, lang);
							hInserted.put(ow, id1);
						}
					}
					if (id2 == 0 && StringUtils.notEmpty(ol)) {
						if (hInserted.keySet().contains(ol))
							id2 = hInserted.get(ol);
						else {
							id2 = DatabaseHelper.insertEntity(0, tp, result.getSport() != null ? result.getSport().getId() : 0, String.valueOf(ol), null, user, null, lang);
							hInserted.put(ol, id2);
						}
					}
					Draw.class.getMethod("setId1" + String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1), Integer.class).invoke(draw, id1 > 0 ? id1 : null);
					Draw.class.getMethod("setId2" + String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1), Integer.class).invoke(draw, id2 > 0 ? id2 : null);
					Draw.class.getMethod("setResult_" + s, String.class).invoke(draw, StringUtils.notEmpty(hParams.get(s + "rs-l")) ? hParams.get(s + "rs-l") : null);
				}
				draw.setIdResult(result.getId());
				draw = (Draw) DatabaseHelper.saveEntity(draw, user);
			}
			sbMsg.append(result.getId() + "#" + ResourceUtils.getText("result." + (idRS != null ? "modified" : "created"), lang));
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void dataTips(HttpServletResponse response, Map hParams, String lang, Contributor user) throws Exception {
		try {
			String lang_ = (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? lang.toUpperCase() : "");
			StringBuffer html = new StringBuffer("<table>");
			HashMap<String, String> hHql = new HashMap<String, String>();
			hHql.put("team", "select label, sport.label" + lang_ + " from Team order by 1");
			hHql.put("country", "select label" + lang_ + ", code from Country order by 1");
			hHql.put("state", "select label" + lang_ + ", code from State order by 1");
			List<Object[]> list = DatabaseHelper.execute(hHql.get(hParams.get("p")));
			for (Object[] t : list)
				html.append("<tr><td>" + t[0] + "</td><td>" + t[1] + "</td></tr>");
			ServletHelper.writeText(response, html.append("</table>").toString());
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
	}
	
	private static void loadOverview(HttpServletResponse response, Map hParams, String lang, Contributor user) throws Exception {
		StringBuffer html = new StringBuffer();
		ArrayList<Object> lFuncParams = new ArrayList<Object>();
		lFuncParams.add(hParams.get("entity"));
		lFuncParams.add(StringUtils.toInt(hParams.get("sport")));
		lFuncParams.add(StringUtils.toInt(hParams.get("count")));
		lFuncParams.add(hParams.get("pattern"));
		lFuncParams.add("_" + lang);
		String currentEntity = null;
		for (RefItem item : (List<RefItem>) DatabaseHelper.call("Overview", lFuncParams)) {
			if (currentEntity == null || !item.getEntity().equals(currentEntity)) {
				if (currentEntity != null);
					html.append("</table>");
				if (item.getEntity().equals(Result.alias))
					html.append("<table><tr><th colspan='11' style='text-align:center;'>" + ResourceUtils.getText("entity." + Result.alias, lang).toUpperCase() + "</th></tr><tr><th>Year</th><th>Event</th><th>Podium</th><th>Results</th><th>Final+Score</th><th>Complex</th><th>City</th><th>Date</th><th>Draw</th><th>Ext.links</th><th>Photo</th></tr>");
				else if (item.getEntity().equals(Athlete.alias))
					html.append("<table><tr><th colspan='5' style='text-align:center;'>" + ResourceUtils.getText("entity." + Athlete.alias, lang).toUpperCase() + "</th></tr><tr><th>Name</th><th>Ref.</th><th>Ext.links</th><th>Photo</th></tr>");
				currentEntity = item.getEntity();
			}
			boolean isPhoto = StringUtils.notEmpty(ImageUtils.getPhotoFile(item.getEntity(), item.getIdItem()));
			html.append("<tr>");
			//html.append("<td>" + item.getIdItem() + "</td>");
			if (item.getEntity().equals(Result.alias)) {
				int rkcount = (item.getTxt3() != null ? item.getTxt3().split("\\|").length : 0);
				int rscount = (item.getTxt4() != null ? item.getTxt4().split("\\|").length : 0);
				boolean isScore = (rkcount >= 2 && rscount == 1);
				String[] tplace = item.getTxt1().split("\\|", -1);
				int cxcount = (StringUtils.notEmpty(tplace[0]) && !tplace[0].equals("0") ? 1 : 0) + (StringUtils.notEmpty(tplace[1]) && !tplace[1].equals("0") ? 1 : 0);
				int ctcount = (StringUtils.notEmpty(tplace[2]) && !tplace[2].equals("0") ? 1 : 0) + (StringUtils.notEmpty(tplace[3]) && !tplace[3].equals("0") ? 1 : 0);
				String[] tdate = item.getTxt2().split("\\|", -1);
				int dtcount = (StringUtils.notEmpty(tdate[0]) && !tdate[0].equals("0") ? 1 : 0) + (StringUtils.notEmpty(tdate[1]) && !tdate[1].equals("0") ? 1 : 0);
				html.append("<td>" + item.getLabelRel1() + "</td>");
				html.append("<td><a href='/update/results/" + StringUtils.encode(Result.alias + "-" + item.getIdItem()) + "' target='_blank'>" + item.getLabelRel2() + " - " + item.getLabelRel3() + (StringUtils.notEmpty(item.getLabelRel4()) ? " - " + item.getLabelRel4() : "") + (StringUtils.notEmpty(item.getLabelRel5()) ? " - " + item.getLabelRel5() : "") + (StringUtils.notEmpty(item.getLabelRel6()) ? " - " + item.getLabelRel6() : "") + "</a></td>");
				html.append("<td" + (rkcount >= 3 ? " class='tick'>(" + rkcount + ")" : ">") + "</td>");
				html.append("<td" + (rscount >= 3 ? " class='tick'>(" + rscount + ")" : ">") + "</td>");
				html.append("<td" + (isScore ? " class='tick'" : "") + "></td>");
				html.append("<td" + (cxcount > 0 ? " class='tick'>(" + cxcount + ")" : ">") + "</td>");
				html.append("<td" + (ctcount > 0 ? " class='tick'>(" + ctcount + ")" : ">") + "</td>");
				html.append("<td" + (dtcount > 0 ? " class='tick'>(" + dtcount + ")" : " class='missing'>") + "</td>");
				html.append("<td" + (item.getIdRel4() != null ? " class='tick'" : "") + "></td>");
			}
			else if (item.getEntity().equals(Athlete.alias)) {
				html.append("<td style='font-weight:bold;'>" + item.getLabelRel1() + ",&nbsp;" + item.getLabelRel2() + "</td>");
				html.append("<td>" + item.getCount2() + "</td>");
			}
			html.append("<td" + (item.getCount1() > 0 ? " class='tick'>(" + item.getCount1() + ")" : " class='missing'>") + "</td>");
			html.append("<td" + (isPhoto ? " class='tick'" : " class='missing'") + "></td>");
			html.append("</tr>");
		}
		ServletHelper.writeText(response, html.append("</table>").toString());
	}
	
	private static void saveConfig(HttpServletResponse response, Map hParams, String lang, Contributor user) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			for (Object o : hParams.keySet()) {
				String p = String.valueOf(o);
				if (p.startsWith("p_")) {
					Config c = (Config) DatabaseHelper.loadEntity(Config.class, p.substring(2));
					if (c != null) {
						c.setValue(String.valueOf(hParams.get(p)));
						DatabaseHelper.saveEntity(c, null);
					}
				}
			}
			sbMsg.append(ResourceUtils.getText("update.ok", lang));
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void executeQuery(HttpServletResponse response, Map hParams, String lang, Contributor user) throws Exception {
		StringBuffer sb = new StringBuffer("<table>");
		ArrayList<String> queries = new ArrayList<String>();
		queries.add("SELECT DISTINCT LAST_NAME || ',' || FIRST_NAME || ',' || ID_SPORT AS N, COUNT(*) AS C\r\nFROM \"Athlete\"\r\nWHERE LINK IS NULL\r\nGROUP BY N\r\nORDER BY C DESC\r\nLIMIT 100");
		queries.add("SELECT 'EV', ID, LABEL FROM \"Event\"\r\nWHERE ID NOT IN (SELECT ID_EVENT FROM \"Result\" WHERE ID_EVENT IS NOT NULL) AND ID NOT IN (SELECT ID_SUBEVENT FROM \"Result\" WHERE ID_SUBEVENT IS NOT NULL) AND ID NOT IN (SELECT ID_SUBEVENT2 FROM \"Result\" WHERE ID_SUBEVENT2 IS NOT NULL)\r\nAND ID NOT IN (SELECT ID_EVENT FROM \"Record\" WHERE ID_EVENT IS NOT NULL) AND ID NOT IN (SELECT ID_SUBEVENT FROM \"Record\" WHERE ID_SUBEVENT IS NOT NULL)\r\nUNION SELECT 'CP', ID, LABEL FROM \"Championship\" WHERE ID NOT IN (SELECT ID_CHAMPIONSHIP FROM \"Result\" WHERE ID_CHAMPIONSHIP IS NOT NULL)\r\nAND ID NOT IN (SELECT ID_CHAMPIONSHIP FROM \"Record\" WHERE ID_CHAMPIONSHIP IS NOT NULL)\r\nORDER BY 1, 3");
		queries.add("SELECT SP.label AS SPORT, CP.label AS Championship, EV.label AS EVENT, SE.label AS SUBEVENT, SE2.label AS SUBEVENT2, YR.label AS YEAR\r\nFROM (SELECT DISTINCT id_sport, id_championship, id_event, id_subevent, id_subevent2 FROM \"Result\" EXCEPT SELECT DISTINCT id_sport, id_championship, id_event, id_subevent, id_subevent2 FROM \"Result\" WHERE id_year = (SELECT id FROM \"Year\" WHERE label = '#YEAR#')) T\r\nLEFT JOIN \"Sport\" SP ON T.id_sport = SP.id\r\nLEFT JOIN \"Championship\" CP ON T.id_championship = CP.id LEFT JOIN \"Event\" EV ON T.id_event = EV.id\r\nLEFT JOIN \"Event\" SE ON T.id_subevent = SE.id LEFT JOIN \"Event\" SE2 ON T.id_subevent2 = SE2.id LEFT JOIN \"Year\" YR ON YR.label = '#YEAR#'\r\nLEFT JOIN \"~InactiveItem\" II ON (T.id_sport=II.id_sport AND T.id_championship=II.id_championship AND T.id_event=II.id_event AND (T.id_subevent IS NULL OR T.id_subevent=II.id_subevent) AND (T.id_subevent2 IS NULL OR T.id_subevent2=II.id_subevent2))\r\nWHERE 1=1 AND #WHERE# AND II.id IS NULL\r\nORDER BY SP.label, CP.index, CP.label, EV.index, EV.label, SE.index, SE.label, SE2.index, SE2.label");
		queries.add("SELECT DISTINCT id_sport, id_championship, id_event, id_subevent, id_subevent2, SP.label AS label1, CP.label AS label2, EV.label AS label3, SE.label AS label4, SE2.label AS label5 FROM \"Result\" RS LEFT JOIN \"Sport\" SP ON RS.id_sport=SP.id LEFT JOIN \"Championship\" CP ON RS.id_championship=CP.id LEFT JOIN \"Event\" EV ON RS.id_event=EV.id LEFT JOIN \"Event\" SE ON RS.id_subevent=SE.id LEFT JOIN \"Event\" SE2 ON RS.id_subevent2=SE2.id ORDER BY SP.label, CP.label, EV.label, SE.label, SE2.label");
		queries.add("SELECT * FROM (SELECT 'CP', ID, LABEL FROM \"Championship\" WHERE LABEL=LABEL_FR UNION SELECT 'CT', ID, LABEL FROM \"City\" WHERE LABEL=LABEL_FR UNION SELECT 'CX', ID, LABEL FROM \"Complex\" WHERE LABEL=LABEL_FR UNION SELECT 'CN', ID, LABEL FROM \"Country\" WHERE LABEL=LABEL_FR UNION SELECT 'EV', ID, LABEL FROM \"Event\" WHERE LABEL=LABEL_FR UNION SELECT 'SP', ID, LABEL FROM \"Sport\" WHERE LABEL=LABEL_FR ) T ORDER BY 1,2");
		queries.add("SELECT DISTINCT SP.label || '-' || CP.label || '-' || EV.label || (CASE WHEN SE.id IS NOT NULL THEN '-' || SE.label ELSE '' END) || (CASE WHEN SE2.id IS NOT NULL THEN '-' || SE2.label ELSE '' END), COUNT(*) AS N FROM \"Result\" RS LEFT JOIN \"Sport\" SP ON RS.id_sport=SP.id LEFT JOIN \"Championship\" CP ON RS.id_championship=CP.id LEFT JOIN \"Event\" EV ON RS.id_event=EV.id LEFT JOIN \"Event\" SE ON RS.id_subevent=SE.id LEFT JOIN \"Event\" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN \"~InactiveItem\" II ON (RS.id_sport = II.id_sport AND RS.id_championship = II.id_championship AND RS.id_event = II.id_event AND (RS.id_subevent = II.id_subevent OR RS.id_subevent IS NULL) AND (RS.id_subevent2 = II.id_subevent2 OR RS.id_subevent2 IS NULL)) WHERE II.id IS NULL GROUP BY 1 HAVING COUNT(*)<5 ORDER BY 2, 1");
		queries.add("SELECT 'PR', id, last_name || ', ' || first_name AS label FROM \"Athlete\" WHERE id_country IS NULL UNION SELECT 'TM', id, label FROM \"Team\" WHERE id_country IS NULL ORDER BY 1, 3");
		queries.add("SELECT 'CT', id, label FROM \"City\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='CT') UNION SELECT 'CX', id, label FROM \"Complex\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='CX') UNION SELECT 'CN', id, label FROM \"Country\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='CN') UNION SELECT 'CP', id, label FROM \"Championship\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='CP') UNION SELECT 'EV', id, label FROM \"Event\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='EV') UNION SELECT 'PR', id, last_name || ', ' || first_name FROM \"Athlete\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='PR') UNION SELECT 'RS', RS.id, SP.label || '-' || CP.label || '-' || EV.label || '-' || YR.label AS label FROM \"Result\" RS LEFT JOIN \"Sport\" SP ON RS.id_sport=SP.id LEFT JOIN \"Championship\" CP ON RS.id_championship=CP.id LEFT JOIN \"Event\" EV ON RS.id_event=EV.id LEFT JOIN \"Year\" YR ON RS.id_year=YR.id WHERE RS.id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='RS') UNION SELECT 'SP', id, label FROM \"Sport\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='SP') UNION SELECT 'TM', id, label FROM \"Team\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='TM') ORDER BY 1, 3");
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String query = queries.get(new Integer(String.valueOf(hParams.get("index"))));
		query = query.replaceAll("#YEAR#", String.valueOf(year));
		query = query.replaceAll("#WHERE#", (year % 4 == 0 ? "(CP.id<>1 OR SP.type<>0)" : (year % 4 == 2 ? "(CP.id<>1 OR SP.type<>1)" : "CP.id<>1")));
		
		List<Object[]> list = (List<Object[]>) DatabaseHelper.executeNative(query);
		if (list != null && list.size() > 0) {
			boolean isFirstRow = true;
			for (Object[] t : list)  {
				sb.append("<tr>");
				if (isFirstRow) {
					for (int i = 1 ; i <= t.length ; i++)
						sb.append("<th>Col." + i + "</th>");
					sb.append("</tr><tr>");	
					isFirstRow = false;
				}
				for (Object o : t)
					sb.append("<td>").append(o != null ? String.valueOf(o) : "").append("</td>");
				sb.append("</tr>");
			}
		}
		ServletHelper.writeText(response, sb.append("</table>").toString());
	}
	
	private static void loadResult(HttpServletRequest request, HttpServletResponse response, Map hParams, String lang, Contributor user) throws Exception {
		Object tp = hParams.get("tp");
		request.setAttribute("title", StringUtils.getTitle(ResourceUtils.getText("update.results", lang)));
		String p = String.valueOf(hParams.get("p"));
		p = StringUtils.decode(p);
		Object[] t = p.split("\\-");
		Result rs = null;
		Year yr = null;

		if (tp != null) {
			String hql = "from Result where ";
			if (String.valueOf(tp).equalsIgnoreCase("direct"))
				hql += "id=" + hParams.get("id");
			else
				hql += "year.id " + (tp.equals("next") ? ">" : "<") + " " + hParams.get("yr") + " and sport.id=" + hParams.get("sp") + " and championship.id=" + hParams.get("cp") + " and event.id=" + hParams.get("ev") + (StringUtils.notEmpty(hParams.get("se")) ? " and subevent.id=" + hParams.get("se") : "") + (StringUtils.notEmpty(hParams.get("se2")) ? " and subevent2.id=" + hParams.get("se2") : "") + " order by year.id " + (tp.equals("next") ? "asc" : "desc");
			rs = (Result) DatabaseHelper.loadEntityFromQuery(hql);
			if (rs != null) {
				yr = rs.getYear();
				t = new Object[3 + (rs.getSubevent() != null ? 1 : 0) + (rs.getSubevent2() != null ? 1 : 0)];
				t[0] = rs.getSport().getId();
				t[1] = rs.getChampionship().getId();
				t[2] = rs.getEvent().getId();
				if (rs.getSubevent() != null)
					t[3] = rs.getSubevent().getId();
				if (rs.getSubevent2() != null)
					t[4] = rs.getSubevent2().getId();
			}
			else
				t = null;
		}

		if (t != null) {
			if (t[0].equals(Result.alias)) {
				rs = (Result)DatabaseHelper.loadEntity(Result.class, t[1]);
				String s = rs.getSport().getId() + "-" + rs.getChampionship().getId() + "-" + rs.getEvent().getId() + (rs.getSubevent() != null ? "-" + rs.getSubevent().getId() : "") + (rs.getSubevent2() != null ? "-" + rs.getSubevent2().getId() : "");
				yr = rs.getYear();
				t = s.split("\\-");
			}
			Sport sp = (Sport)DatabaseHelper.loadEntity(Sport.class, t[0]);
			Championship cp = (Championship)DatabaseHelper.loadEntity(Championship.class, t[1]); 
			Event ev = (Event)DatabaseHelper.loadEntity(Event.class, t[2]);
			Event se = (Event)DatabaseHelper.loadEntity(Event.class, t.length > 3 ? t[3] : 0);
			Event se2 = (Event)DatabaseHelper.loadEntity(Event.class, t.length > 4 ? t[4] : 0);
			if (yr == null)
				yr = (Year)DatabaseHelper.loadEntityFromQuery("from Year where label='" + Calendar.getInstance().get(Calendar.YEAR) + "'");

			StringBuffer sb = new StringBuffer();
			sb.append(sp.getId()).append("~").append(sp.getLabel(lang)).append("~");
			sb.append(cp.getId()).append("~").append(cp.getLabel(lang)).append("~");
			sb.append(ev.getId()).append("~").append(ev.getLabel(lang) + " (" + ev.getType().getLabel(lang) + ")").append("~").append(ev.getType().getNumber()).append("~");
			sb.append(se != null ? se.getId() : "").append("~").append(se != null ? se.getLabel(lang) + " (" + se.getType().getLabel(lang) + ")" : "").append("~").append(se != null ? se.getType().getNumber() : "").append("~");
			sb.append(se2 != null ? se2.getId() : "").append("~").append(se2 != null ? se2.getLabel(lang) + " (" + se2.getType().getLabel(lang) + ")" : "").append("~").append(se2 != null ? se2.getType().getNumber() : "").append("~");
			sb.append(yr.getId()).append("~").append(yr.getLabel()).append("~");
			if (rs != null) { // Existing result
				request.setAttribute("id", rs.getId());
				// Result Info
				sb.append(rs.getId()).append("~");
				sb.append(rs.getDate1()).append("~").append(rs.getDate2()).append("~");
				sb.append(rs.getComplex1() != null ? rs.getComplex1().getId() : (rs.getCity1() != null ? rs.getCity1().getId() : "")).append("~");
				sb.append(rs.getComplex1() != null ? rs.getComplex1().toString2(lang) : (rs.getCity1() != null ? rs.getCity1().toString2(lang) : "")).append("~");
				sb.append(rs.getComplex2() != null ? rs.getComplex2().getId() : (rs.getCity2() != null ? rs.getCity2().getId() : "")).append("~");
				sb.append(rs.getComplex2() != null ? rs.getComplex2().toString2(lang) : (rs.getCity2() != null ? rs.getCity2().toString2(lang) : "")).append("~");
				sb.append(rs.getExa()).append("~").append(rs.getComment()).append("~").append(ImageUtils.getPhotoFile(Result.alias, rs.getId())).append("~");
				// External links
				StringBuffer sbLinks = new StringBuffer();
				try {
					List<ExternalLink> list = DatabaseHelper.execute("from ExternalLink where entity='" + Result.alias + "' and idItem=" + rs.getId() + " order by id");
					for (ExternalLink link : list)
						sbLinks.append(link.getUrl()).append("|");
				}
				catch (Exception e_) {
					Logger.getLogger("sh").error(e_.getMessage(), e_);
				}
				sb.append(sbLinks.toString()).append("~");
				Integer n = ev.getType().getNumber();
				if (se != null)
					n = se.getType().getNumber();
				if (se2 != null)
					n = se2.getType().getNumber();
				// Rankings
				for (int i = 1 ; i <= MAX_RANKS ; i++) {
					Integer id = null;
					String rank = null;
					String result = null;
					Method m = Result.class.getMethod("getIdRank" + i);
					Object o = m.invoke(rs);
					if (o != null)
						id = (Integer) o;
					if (id != null && id > 0) {
						rank = getEntityLabel(n, id, lang);
						m = Result.class.getMethod("getResult" + i);
						o = m.invoke(rs);
						if (o != null)
							result = String.valueOf(o);
					}
					sb.append(id != null ? id : "").append("~");
					sb.append(rank != null ? rank : "").append("~");
					sb.append(result != null ? result : "").append("~");
				}
				// PersonList
				List lPList = DatabaseHelper.execute("from PersonList where idResult=" + rs.getId() + " order by id");
				if (lPList != null && lPList.size() > 0) {
					List<String> l = new ArrayList<String>();
					for (PersonList pl : (List<PersonList>) lPList) {
						int rk = pl.getRank();
						if (l.size() < rk)
							l.add("");
						Athlete a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, pl.getIdPerson());
						l.set(rk - 1, (StringUtils.notEmpty(l.get(rk - 1)) ? l.get(rk - 1) + "|" : "") + pl.getIdPerson() + ":" + a.toString2());
					}
					sb.append("rkl-" + StringUtils.implode(l, "#")).append("~");
				}
				// Draws
				List lDraw = DatabaseHelper.execute("from Draw where idResult=" + rs.getId());
				if (lDraw != null && lDraw.size() > 0) {
					Draw dr = (Draw) lDraw.get(0);
					sb.append(dr.getId()).append("~");
					for (String s : new String[]{"qf1", "qf2", "qf3", "qf4", "sf1", "sf2", "thd"}) {
						Integer id1 = null;
						Integer id2 = null;
						Method m = Draw.class.getMethod("getId1" + String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1));
						Object o = m.invoke(dr);
						if (o != null)
							id1 = (Integer) o;
						m = Draw.class.getMethod("getId2" + String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1));
						o = m.invoke(dr);
						if (o != null)
							id2 = (Integer) o;
						
						String label1 = null;
						String label2 = null;
						if (id1 != null && id1 > 0)
							label1 = getEntityLabel(n, id1, lang);
						if (id2 != null && id2 > 0)
							label2 = getEntityLabel(n, id2, lang);
						
						String result = null;
						m = Draw.class.getMethod("getResult_" + s);
						o = m.invoke(dr);
						if (o != null)
							result = String.valueOf(o);								
						
						sb.append(id1 != null ? id1 : "").append("~").append(label1 != null ? label1 : "").append("~");
						sb.append(id2 != null ? id2 : "").append("~").append(label2 != null ? label2 : "").append("~");
						sb.append(result != null ? result : "").append("~");
					}
				}
			}
			String result = sb.toString().replaceAll("~null~", "~~").replaceAll("~null~", "~~").replaceAll("\"", "\\\\\"");
			if (tp != null)
				ServletHelper.writeText(response, result);
			else {
				request.setAttribute("value", result);
				request.getRequestDispatcher("/jsp/update/results.jsp").forward(request, response);					
			}
		}
		else
			ServletHelper.writeText(response, "");
	}
	
	private static void loadEntity(HttpServletResponse response, Map hParams, String lang, Contributor user) throws Exception {
		HashMap<String, Short> hLocs = new HashMap<String, Short>();
		hLocs.put("first", DatabaseHelper.FIRST);
		hLocs.put("previous", DatabaseHelper.PREVIOUS);
		hLocs.put("next", DatabaseHelper.NEXT);
		hLocs.put("last", DatabaseHelper.LAST);
		String action = String.valueOf(hParams.get("action"));
		String id = String.valueOf(hParams.get("id"));
		String alias = String.valueOf(hParams.get("alias"));
		Class c = DatabaseHelper.getClassFromAlias(alias);
		Object o = (action.equals("find") ? DatabaseHelper.loadEntity(c, id) : DatabaseHelper.move(c, id, hLocs.get(action), null));
		StringBuffer sb = new StringBuffer();
		if (o != null) {
			id = String.valueOf(c.getMethod("getId").invoke(o, new Object[0]));
			sb.append(id).append("~");
			if (o instanceof Athlete) {
				Athlete at = (Athlete) o;
				sb.append(at.getLastName()).append("~");
				sb.append(StringUtils.notEmpty(at.getFirstName()) ? at.getFirstName() : "").append("~");
				sb.append(at.getSport() != null ? at.getSport().getId() : 0).append("~");
				sb.append(at.getSport() != null ? at.getSport().getLabel(lang) : "").append("~");
				sb.append(at.getTeam() != null ? at.getTeam().getId() : 0).append("~");
				sb.append(at.getTeam() != null ? at.getTeam().getLabel() : "").append("~");
				sb.append(at.getCountry() != null ? at.getCountry().getId() : 0).append("~");
				sb.append(at.getCountry() != null ? at.getCountry().getLabel(lang) : "").append("~");
				if (at.getLink() != null && at.getLink() > 0) {
					try {
						Athlete a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, at.getLink());
						sb.append(at.getLink() != null ? at.getLink() : 0).append("~");
						sb.append(a.toString2()).append("~");
					}
					catch (Exception e) {
						Logger.getLogger("sh").error(e.getMessage());
					}
				}
				else
					sb.append("~~");
				sb.append(ImageUtils.getPhotoFile(Athlete.alias, id)).append("~");
			}
			else if (o instanceof Championship) {
				Championship cp = (Championship) o;
				sb.append(cp.getLabel()).append("~");
				sb.append(cp.getLabelFr()).append("~");
				sb.append(cp.getIndex() != null ? String.valueOf(cp.getIndex()) : "").append("~");
			}
			else if (o instanceof City) {
				City ct = (City) o;
				sb.append(ct.getLabel()).append("~");
				sb.append(ct.getLabelFr()).append("~");
				sb.append(ct.getState() != null ? ct.getState().getId() : 0).append("~");
				sb.append(ct.getState() != null ? ct.getState().getLabel(lang) : "").append("~");
				sb.append(ct.getCountry() != null ? ct.getCountry().getId() : 0).append("~");
				sb.append(ct.getCountry() != null ? ct.getCountry().getLabel(lang) : "").append("~");
				if (ct.getLink() != null && ct.getLink() > 0) {
					try {
						City c_ = (City) DatabaseHelper.loadEntity(City.class, ct.getLink());
						sb.append(ct.getLink() != null ? ct.getLink() : 0).append("~");
						sb.append(c_.toString2(lang)).append("~");
					}
					catch (Exception e) {
						Logger.getLogger("sh").error(e.getMessage());
					}
				}
				else
					sb.append("~~");
				sb.append(ImageUtils.getPhotoFile(City.alias, id)).append("~");
			}
			else if (o instanceof Complex) {
				Complex cx = (Complex) o;
				sb.append(cx.getLabel()).append("~");
				sb.append(cx.getLabelFr()).append("~");
				sb.append(cx.getCity() != null ? cx.getCity().getId() : 0).append("~");
				sb.append(cx.getCity() != null ? cx.getCity().toString2(lang) : "").append("~");
				if (cx.getLink() != null && cx.getLink() > 0) {
					try {
						Complex c_ = (Complex) DatabaseHelper.loadEntity(Complex.class, cx.getLink());
						sb.append(cx.getLink() != null ? cx.getLink() : 0).append("~");
						sb.append(c_.toString2(lang)).append("~");
					}
					catch (Exception e) {
						Logger.getLogger("sh").error(e.getMessage());
					}
				}
				else
					sb.append("~~");
				sb.append(ImageUtils.getPhotoFile(Complex.alias, id)).append("~");
			}
			else if (o instanceof Contributor) {
				Contributor cb = (Contributor) o;
				sb.append(cb.getLogin()).append("~");
				sb.append(cb.getPublicName()).append("~");
				sb.append(cb.getEmail()).append("~");
				sb.append(cb.isActive() ? "1" : "0").append("~");
				sb.append(cb.isAdmin() ? "1" : "0").append("~");
				sb.append(cb.getSports()).append("~");
			}
			else if (o instanceof Country) {
				Country cn = (Country) o;
				sb.append(cn.getLabel()).append("~");
				sb.append(cn.getLabelFr()).append("~");
				sb.append(cn.getCode()).append("~");
			}
			else if (o instanceof Event) {
				Event ev = (Event) o;
				sb.append(ev.getLabel()).append("~");
				sb.append(ev.getLabelFr()).append("~");
				sb.append(ev.getType() != null ? ev.getType().getId() : 0).append("~");
				sb.append(ev.getType() != null ? ev.getType().getLabel(lang) : "").append("~");
				sb.append(ev.getIndex()).append("~");
			}
			else if (o instanceof Olympics) {
				Olympics ol = (Olympics) o;
				sb.append(ol.getYear() != null ? ol.getYear().getId() : 0).append("~");
				sb.append(ol.getYear() != null ? ol.getYear().getLabel(lang) : "").append("~");
				sb.append(ol.getCity() != null ? ol.getCity().getId() : 0).append("~");
				sb.append(ol.getCity() != null ? ol.getCity().getLabel(lang) : "").append("~");
				sb.append(ol.getType()).append("~");
				sb.append(ol.getDate1()).append("~");
				sb.append(ol.getDate2()).append("~");
				sb.append(ol.getCountSport()).append("~");
				sb.append(ol.getCountEvent()).append("~");
				sb.append(ol.getCountCountry()).append("~");
				sb.append(ol.getCountPerson()).append("~");
			}
			else if (o instanceof Sport) {
				Sport sp = (Sport) o;
				sb.append(sp.getLabel()).append("~");
				sb.append(sp.getLabelFr()).append("~");
				sb.append(sp.getType() != null ? sp.getType() : "").append("~");
				sb.append(sp.getIndex() != null ? sp.getIndex() : "").append("~");
			}
			else if (o instanceof State) {
				State st = (State) o;
				sb.append(st.getLabel()).append("~");
				sb.append(st.getLabelFr()).append("~");
				sb.append(st.getCode()).append("~");
				sb.append(st.getCapital()).append("~");
			}
			else if (o instanceof Team) {
				Team tm = (Team) o;
				sb.append(tm.getLabel()).append("~");
				sb.append(tm.getSport() != null ? tm.getSport().getId() : 0).append("~");
				sb.append(tm.getSport() != null ? tm.getSport().getLabel(lang) : "").append("~");
				sb.append(tm.getCountry() != null ? tm.getCountry().getId() : 0).append("~");
				sb.append(tm.getCountry() != null ? tm.getCountry().getLabel(lang) : "").append("~");
				sb.append(tm.getLeague() != null ? tm.getLeague().getId() : 0).append("~");
				sb.append(tm.getLeague() != null ? tm.getLeague().getLabel() : "").append("~");
				sb.append(StringUtils.notEmpty(tm.getConference()) ? tm.getConference() : "").append("~");
				sb.append(StringUtils.notEmpty(tm.getDivision()) ? tm.getDivision() : "").append("~");
				sb.append(StringUtils.notEmpty(tm.getComment()) ? tm.getComment() : "").append("~");
				sb.append(StringUtils.notEmpty(tm.getYear1()) ? tm.getYear1() : "").append("~");
				sb.append(StringUtils.notEmpty(tm.getYear2()) ? tm.getYear2() : "").append("~");
				if (tm.getLink() != null && tm.getLink() > 0) {
					try {
						Team t = (Team) DatabaseHelper.loadEntity(Team.class, tm.getLink());
						sb.append(tm.getLink() != null ? tm.getLink() : 0).append("~");
						sb.append(t.toString()).append("~");
					}
					catch (Exception e) {
						Logger.getLogger("sh").error(e.getMessage());
					}
				}
				else
					sb.append("~~");
			}
			else if (o instanceof Year) {
				Year yr = (Year) o;
				sb.append(yr.getLabel()).append("~");
			}
		}
		ServletHelper.writeText(response, sb.toString());
	}
	
	private static void saveEntity(HttpServletResponse response, Map hParams, String lang, Contributor user) throws Exception {
		String msg = null;
		try {
			String id = String.valueOf(hParams.get("id"));
			String alias = String.valueOf(hParams.get("alias"));
			Class c = DatabaseHelper.getClassFromAlias(alias);
			Object o = (StringUtils.notEmpty(id) ? DatabaseHelper.loadEntity(c, Integer.parseInt(id)) : c.newInstance());
			if (alias.equalsIgnoreCase(Athlete.alias)) {
				Athlete en = (Athlete) o;
				en.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, StringUtils.toInt(hParams.get("pr-sport"))));
				en.setTeam((Team)DatabaseHelper.loadEntity(Team.class, StringUtils.toInt(hParams.get("pr-team"))));
				en.setCountry((Country)DatabaseHelper.loadEntity(Country.class, StringUtils.toInt(hParams.get("pr-country"))));
				en.setLastName(String.valueOf(hParams.get("pr-lastname")));
				en.setFirstName(String.valueOf(hParams.get("pr-firstname")));
				en.setLink(StringUtils.notEmpty(hParams.get("pr-link")) ? new Integer(String.valueOf(hParams.get("pr-link"))) : null);
				if (en.getLink() != null && en.getLink() > 0) {
					try {
						Athlete a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, en.getLink());
						while (a.getLink() != null && a.getLink() > 0)
							a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, a.getLink());
						en.setLink(a.getId());
						DatabaseHelper.executeUpdate("UPDATE \"Athlete\" SET LINK=0 WHERE ID=" + en.getLink());
					}
					catch (Exception e) {
						Logger.getLogger("sh").error(e.getMessage());
					}
				}
			}
			else if (alias.equalsIgnoreCase(Championship.alias)) {
				Championship en = (Championship) o;
				en.setLabel(String.valueOf(hParams.get("cp-label")));
				en.setLabelFr(String.valueOf(hParams.get("cp-labelfr")));
				en.setIndex(StringUtils.notEmpty(hParams.get("cp-index")) ? StringUtils.toInt(hParams.get("cp-index")) : Integer.MAX_VALUE);
			}
			else if (alias.equalsIgnoreCase(City.alias)) {
				City en = (City) o;
				en.setLabel(String.valueOf(hParams.get("ct-label")));
				en.setLabelFr(String.valueOf(hParams.get("ct-labelfr")));
				en.setState((State)DatabaseHelper.loadEntity(State.class, StringUtils.toInt(hParams.get("ct-state"))));
				en.setCountry((Country)DatabaseHelper.loadEntity(Country.class, StringUtils.toInt(hParams.get("ct-country"))));
				en.setLink(StringUtils.notEmpty(hParams.get("ct-link")) ? new Integer(String.valueOf(hParams.get("ct-link"))) : null);
				if (en.getLink() != null && en.getLink() > 0) {
					try {
						City c_ = (City) DatabaseHelper.loadEntity(City.class, en.getLink());
						while (c_.getLink() != null && c_.getLink() > 0)
							c_ = (City) DatabaseHelper.loadEntity(City.class, c_.getLink());
						en.setLink(c_.getId());
						DatabaseHelper.executeUpdate("UPDATE \"City\" SET LINK=0 WHERE ID=" + en.getLink());
					}
					catch (Exception e) {
						Logger.getLogger("sh").error(e.getMessage());
					}
				}
			}
			else if (alias.equalsIgnoreCase(Complex.alias)) {
				Complex en = (Complex) o;
				en.setLabel(String.valueOf(hParams.get("cx-label")));
				en.setLabelFr(String.valueOf(hParams.get("cx-labelfr")));
				en.setCity((City)DatabaseHelper.loadEntity(City.class, StringUtils.toInt(hParams.get("cx-city"))));
				en.setLink(StringUtils.notEmpty(hParams.get("cx-link")) ? new Integer(String.valueOf(hParams.get("cx-link"))) : null);
				if (en.getLink() != null && en.getLink() > 0) {
					try {
						Complex c_ = (Complex) DatabaseHelper.loadEntity(Complex.class, en.getLink());
						while (c_.getLink() != null && c_.getLink() > 0)
							c_ = (Complex) DatabaseHelper.loadEntity(Complex.class, c_.getLink());
						en.setLink(c_.getId());
						DatabaseHelper.executeUpdate("UPDATE \"Complex\" SET LINK=0 WHERE ID=" + en.getLink());
					}
					catch (Exception e) {
						Logger.getLogger("sh").error(e.getMessage());
					}
				}
			}
			else if (alias.equalsIgnoreCase(Contributor.alias)) {
				Contributor en = (Contributor) o;
				en.setLogin(String.valueOf(hParams.get("cb-login")));
				en.setPublicName(String.valueOf(hParams.get("cb-name")));
				en.setEmail(String.valueOf(hParams.get("cb-email")));
				en.setActive(String.valueOf(hParams.get("cb-active")).equals("true"));
				en.setAdmin(String.valueOf(hParams.get("cb-admin")).equals("true"));
				en.setSports(StringUtils.notEmpty(hParams.get("cb-sports")) ? String.valueOf(hParams.get("cb-sports")) : null);
			}
			else if (alias.equalsIgnoreCase(Country.alias)) {
				Country en = (Country) o;
				en.setLabel(String.valueOf(hParams.get("cp-label")));
				en.setLabelFr(String.valueOf(hParams.get("cp-labelfr")));
				en.setCode(String.valueOf(hParams.get("cp-code")));
			}
			else if (alias.equalsIgnoreCase(Event.alias)) {
				Event en = (Event) o;
				en.setLabel(String.valueOf(hParams.get("ev-label")));
				en.setLabelFr(String.valueOf(hParams.get("ev-labelfr")));
				en.setType((Type)DatabaseHelper.loadEntity(Type.class, StringUtils.toInt(hParams.get("ev-type"))));
				en.setIndex(StringUtils.notEmpty(hParams.get("ev-index")) ? StringUtils.toInt(hParams.get("ev-index")) : Integer.MAX_VALUE);
			}
			else if (alias.equalsIgnoreCase(Olympics.alias)) {
				Olympics en = (Olympics) o;
				en.setYear((Year)DatabaseHelper.loadEntity(Year.class, StringUtils.toInt(hParams.get("ol-year"))));
				en.setCity((City)DatabaseHelper.loadEntity(City.class, StringUtils.toInt(hParams.get("ol-city"))));
				en.setType(StringUtils.notEmpty(hParams.get("ol-type")) ? StringUtils.toInt(hParams.get("ol-type")) : 0);
				en.setDate1(String.valueOf(hParams.get("ol-start")));
				en.setDate2(String.valueOf(hParams.get("ol-end")));
				en.setCountSport(StringUtils.notEmpty(hParams.get("ol-sports")) ? StringUtils.toInt(hParams.get("ol-sports")) : 0);
				en.setCountEvent(StringUtils.notEmpty(hParams.get("ol-events")) ? StringUtils.toInt(hParams.get("ol-events")) : 0);
				en.setCountCountry(StringUtils.notEmpty(hParams.get("ol-countries")) ? StringUtils.toInt(hParams.get("ol-countries")) : 0);
				en.setCountPerson(StringUtils.notEmpty(hParams.get("ol-persons")) ? StringUtils.toInt(hParams.get("ol-persons")) : 0);
			}
			else if (alias.equalsIgnoreCase(Sport.alias)) {
				Sport en = (Sport) o;
				en.setLabel(String.valueOf(hParams.get("sp-label")));
				en.setLabelFr(String.valueOf(hParams.get("sp-labelfr")));
				en.setType(StringUtils.notEmpty(hParams.get("sp-type")) ? StringUtils.toInt(hParams.get("sp-type")) : 0);
				en.setIndex(StringUtils.notEmpty(hParams.get("sp-index")) ? Float.valueOf(String.valueOf(hParams.get("sp-index"))) : Float.MAX_VALUE);
			}
			else if (alias.equalsIgnoreCase(State.alias)) {
				State en = (State) o;
				en.setLabel(String.valueOf(hParams.get("st-label")));
				en.setLabelFr(String.valueOf(hParams.get("st-labelfr")));
				en.setCode(String.valueOf(hParams.get("st-code")));
				en.setCapital(String.valueOf(hParams.get("st-capital")));
			}
			else if (alias.equalsIgnoreCase(Team.alias)) {
				Team en = (Team) o;
				en.setLabel(String.valueOf(hParams.get("tm-label")));
				en.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, StringUtils.toInt(hParams.get("tm-sport"))));
				en.setCountry((Country)DatabaseHelper.loadEntity(Country.class, StringUtils.toInt(hParams.get("tm-country"))));
				en.setLeague((League)DatabaseHelper.loadEntity(League.class, StringUtils.toInt(hParams.get("tm-league"))));
				en.setConference(String.valueOf(hParams.get("tm-conference")));
				en.setDivision(String.valueOf(hParams.get("tm-division")));
				en.setComment(String.valueOf(hParams.get("tm-comment")));
				en.setYear1(String.valueOf(hParams.get("tm-year1")));
				en.setYear2(String.valueOf(hParams.get("tm-year2")));
				en.setLink(StringUtils.notEmpty(hParams.get("cx-link")) ? new Integer(String.valueOf(hParams.get("cx-link"))) : null);
				if (en.getLink() != null && en.getLink() > 0) {
					try {
						Team t = (Team) DatabaseHelper.loadEntity(Team.class, en.getLink());
						while (t.getLink() != null && t.getLink() > 0)
							t = (Team) DatabaseHelper.loadEntity(Team.class, t.getLink());
						en.setLink(t.getId());
						DatabaseHelper.executeUpdate("UPDATE \"Team\" SET LINK=0 WHERE ID=" + en.getLink());
					}
					catch (Exception e) {
						Logger.getLogger("sh").error(e.getMessage());
					}
				}
			}
			else if (alias.equalsIgnoreCase(Year.alias)) {
				Year en = (Year) o;
				en.setLabel(String.valueOf(hParams.get("yr-label")));
			}
			o = DatabaseHelper.saveEntity(o, user);
			String id_ = String.valueOf(c.getMethod("getId").invoke(o, new Object[0]));
			msg = ResourceUtils.getText("update.ok", lang) + "&nbsp;–&nbsp;" + ResourceUtils.getText("entity." + alias + ".1", lang) + " #" + id_;
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
			msg = "ERR:" + e.getMessage();
		}
		finally {
			ServletHelper.writeText(response, msg);
		}
	}
	
	private static String getEntityLabel(int n, Integer id, String lang) throws Exception {
		String label = null;
		if (n < 10) {
			Athlete a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, id);
			label = a.toString2();
		}
		else if (n == 50) {
			Team t_ = (Team) DatabaseHelper.loadEntity(Team.class, id);
			label = t_.getLabel() + (t_.getCountry() != null ? " (" + t_.getCountry().getCode() + ")" : "");
		}
		else {
			Country c = (Country) DatabaseHelper.loadEntity(Country.class, id);
			label = c.getLabel(lang);
		}
		return label;
	}

}