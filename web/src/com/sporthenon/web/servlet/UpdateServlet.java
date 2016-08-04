package com.sporthenon.web.servlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.HallOfFame;
import com.sporthenon.db.entity.League;
import com.sporthenon.db.entity.OlympicRanking;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.RetiredNumber;
import com.sporthenon.db.entity.Round;
import com.sporthenon.db.entity.RoundType;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.entity.Type;
import com.sporthenon.db.entity.WinLoss;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Config;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.entity.meta.ErrorReport;
import com.sporthenon.db.entity.meta.ExternalLink;
import com.sporthenon.db.entity.meta.FolderHistory;
import com.sporthenon.db.entity.meta.InactiveItem;
import com.sporthenon.db.entity.meta.PersonList;
import com.sporthenon.db.entity.meta.Redirection;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.db.entity.meta.Translation;
import com.sporthenon.db.entity.meta.TreeItem;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.ImportUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.ServletHelper;

public class UpdateServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	private static final int MAX_RANKS = 20;
	private static final int MAX_AUTOCOMPLETE_RESULTS = 50;
	private static ImportUtils.Progress IMPORT_PROGESS;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String lang = getLocale(request);
			Contributor cb = getUser(request);
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			if (hParams.containsKey("p2") && hParams.get("p2").equals("ajax"))
				ajaxAutocomplete(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("save"))
				saveResult(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("delete"))
				deleteResult(response, hParams, lang, cb);
			else if (hParams.containsKey("p2") && hParams.get("p2").equals("data"))
				dataTips(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("load-entity"))
				loadEntity(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("save-entity"))
				saveEntity(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("delete-entity"))
				deleteEntity(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("load-overview"))
				loadOverview(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("save-config"))
				saveConfig(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("execute-query"))
				executeQuery(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("merge"))
				mergeEntity(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("execute-import"))
				executeImport(request, response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("check-progress-import")) {
				if (IMPORT_PROGESS != null)
					ServletHelper.writeText(response, String.valueOf(IMPORT_PROGESS.value));
			}
			else if (hParams.containsKey("p") && hParams.get("p").equals("load-template"))
				loadTemplate(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("load-extlinks"))
				loadExternalLinks(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("save-extlinks"))
				saveExternalLinks(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("updateauto-extlinks"))
				updateAutoExternalLinks(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("load-translations"))
				loadTranslations(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("save-translations"))
				saveTranslations(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("load-folders"))
				loadFolders(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("save-folders"))
				saveFolders(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("load-errors"))
				loadErrors(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("load-redirections"))
				loadRedirections(response, hParams, lang, cb);
			else if (hParams.containsKey("p") && hParams.get("p").equals("save-redirections"))
				saveRedirections(response, hParams, lang, cb);
			else
				loadResult(request, response, hParams, lang, cb);
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
	private static void ajaxAutocomplete(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		String p = String.valueOf(hParams.get("p"));
		String field = p.split("\\~")[0];
		String currentId = null;
		String field_ = field;
		boolean isId = String.valueOf(hParams.get("value")).matches("^\\#\\d+");
		String value = "^" + (hParams.get("value") + "%").replaceAll("\\*", "%");
		String sport = null;
		boolean isData = field.matches(".*\\-l$");
		if (isData) {
			if (field.contains("rc-rank")) {
				field_ = field.replaceAll("^..\\-|\\-l$", "");
				field = field.substring(0, 2);
			}
			else {
				field = field.substring(3).replaceAll("\\-l$", "");
				field_ = field_.replaceFirst("\\-l$", "");
				if (field.equals("link")) {
					field = (field_.startsWith("pr") ? "pr" : (field_.startsWith("cx") ? "cx" : (field_.startsWith("ct") ? "ct" : (field_.startsWith("tm") ? "tm" : ""))));
					currentId = (p.contains("~") ? p.split("\\~")[1] : null);
				}	
			}
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
		hTable.put("ev", "Event"); hTable.put("event", "Event"); hTable.put("subevent", "Event"); hTable.put("subevent2", "Event");
		hTable.put("se", "Event");
		hTable.put("se2", "Event");
		hTable.put("tp", "Type"); hTable.put("type", "Type");
		hTable.put("yr", "Year"); hTable.put("year", "Year");
		hTable.put("cx", "Complex"); hTable.put("complex", "Complex");
		hTable.put("ct", "City"); hTable.put("city", "City");
		hTable.put("st", "State"); hTable.put("state", "State");
		hTable.put("pl1", "Complex"); hTable.put("complex", "Complex");
		hTable.put("pl2", "Complex"); hTable.put("complex", "Complex");
		hTable.put("pr", "Athlete"); hTable.put("athlete", "Athlete"); hTable.put("person", "Athlete");
		hTable.put("tm", "Team"); hTable.put("team", "Team");
		hTable.put("cn", "Country"); hTable.put("country", "Country");
		hTable.put("cb", "Contributor"); hTable.put("contributor", "Contributor");
		hTable.put("lg", "League"); hTable.put("league", "League");
		hTable.put("rs", "Result"); hTable.put("result", "Result");
		hTable.put("ol", "Olympics"); hTable.put("olympics", "Olympics");
		hTable.put("rt", "RoundType");
		hTable.put("hf", "HallOfFame");
		hTable.put("rc", "Record");
		hTable.put("rn", "RetiredNumber");
		hTable.put("ts", "TeamStadium");
		hTable.put("wl", "WinLoss");
		String alias = (String) Class.forName("com.sporthenon.db.entity." + hTable.get(field)).getField("alias").get(null);
		String labelHQL = "T.label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) && !field.matches("cx|pl1|pl2|lg|tm|yr|complex|league|team|year") ? "_" + lang : "");
		String l_ = "label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? "_" + lang : "");
		String whereHQL = "";
		String joins = "";
		if (field.matches(Athlete.alias.toLowerCase() + "|athlete|person")) {
			labelHQL = "last_name || ', ' || first_name || ' (' || CN.code || ')'";
			whereHQL += (sport != null ? " and T.id_sport=" + sport : "");
			whereHQL += (cb != null && !cb.isAdmin() ? " and T.id_sport in (" + cb.getSports() + ")" : "");
			joins += " LEFT JOIN \"Country\" CN ON T.id_country=CN.id";
		}
		else if (field.matches(Team.alias.toLowerCase() + "|team")) {
			whereHQL += (sport != null ? " and T.id_sport=" + sport : "");
			whereHQL += (cb != null && !cb.isAdmin() ? " and T.id_sport in (" + cb.getSports() + ")" : "");
		}
		else if (field.equalsIgnoreCase(Sport.alias) && cb != null && StringUtils.notEmpty(cb.getSports()))
			whereHQL += (" and T.id in (" + cb.getSports() + ")");
		else if (field.equalsIgnoreCase(Contributor.alias))
			labelHQL = "login";
		else if (field.matches("pl\\d|complex")) {
			labelHQL = "T.label || ', ' || CT." + l_ + " || ', ' || CN.code";
			joins += " LEFT JOIN \"City\" CT ON T.id_city=CT.id";
			joins += " LEFT JOIN \"Country\" CN ON CT.id_country=CN.id";
		}
		else if (field.matches(Olympics.alias.toLowerCase() + "|olympics")) {
			labelHQL = "YR.label || ' ' || CT." + l_;
			joins += " LEFT JOIN \"City\" CT ON T.id_city=CT.id";
			joins += " LEFT JOIN \"Year\" YR ON T.id_year=YR.id";
		}
		else if (field.equalsIgnoreCase(Result.alias)) {
			value = "%" + value;
			// TODO coalesce(subevent." + l + ", ''), coalesce(subevent2." + l + ", '')
			labelHQL = "SP." + l_ + " || ' - ' || CP." + l_ + " || ' - ' || EV." + l_ + " || ' - ' || YR.label";
			joins += " LEFT JOIN \"Sport\" SP ON T.id_sport=SP.id";
			joins += " LEFT JOIN \"Championship\" CP ON T.id_championship=CP.id";
			joins += " LEFT JOIN \"Event\" EV ON T.id_event=EV.id";
			joins += " LEFT JOIN \"Year\" YR ON T.id_year=YR.id";
		}
		else if (field.equalsIgnoreCase(HallOfFame.alias)) {
			labelHQL = "LG.label || ' - ' || YR.label";
			joins += " LEFT JOIN \"League\" LG ON T.id_league=LG.id";
			joins += " LEFT JOIN \"Year\" YR ON T.id_year=YR.id";
		}
		else if (field.equalsIgnoreCase(Record.alias)) {
			value = "%" + value;
			labelHQL = "SP." + l_ + " || ' - ' || CP." + l_ + " || ' - ' || EV." + l_ + " || ' - ' || type1 || ' - ' || type2 || ' - ' || T.label";
			joins += " LEFT JOIN \"Sport\" SP ON T.id_sport=SP.id";
			joins += " LEFT JOIN \"Championship\" CP ON T.id_championship=CP.id";
			joins += " LEFT JOIN \"Event\" EV ON T.id_event=EV.id";
		}
		else if (field.toUpperCase().matches(RetiredNumber.alias + "|" + TeamStadium.alias + "|" + WinLoss.alias)) {
			labelHQL = "LG.label || ' - ' || TM.label";
			joins += " LEFT JOIN \"League\" LG ON T.id_league=LG.id";
			joins += " LEFT JOIN \"Team\" TM ON T.id_team=TM.id";
		}
		if (StringUtils.notEmpty(currentId))
			whereHQL += " and T.id <> " + currentId;
		// Execute query
		String regexp = StringUtils.toPatternString(value);
		List<Object[]> l = DatabaseHelper.executeNative("SELECT T.id, " + labelHQL + ", CAST('" + alias + "' AS VARCHAR) FROM \"" + hTable.get(field) + "\" T" + joins + " WHERE (" + (isId ? "T.id=" + value.substring(1).replaceFirst("\\%", "") : "lower(" + labelHQL + ") ~ E'" + regexp) + "')" + whereHQL + " ORDER BY " + (field.equalsIgnoreCase(Result.alias) ? "id_year desc" : labelHQL) + " LIMIT " + MAX_AUTOCOMPLETE_RESULTS);
		if (field.matches(Athlete.alias.toLowerCase() + "|athlete|person")) {
			String labelHQL_ = "last_name || ', ' || first_name || ' (' || CN.code || ', ' || TM.label || ')'";
			joins += " LEFT JOIN \"Team\" TM ON T.id_team=TM.id";
			l.addAll(DatabaseHelper.executeNative("SELECT T.id, " + labelHQL_ + ", CAST('" + Athlete.alias + "' AS VARCHAR) FROM \"Athlete\" T" + joins + " WHERE lower(" + labelHQL_ + ") ~ E'" + regexp + "'" + whereHQL + " ORDER BY " + (field.equalsIgnoreCase(Result.alias) ? "id_year desc" : labelHQL_) + " LIMIT " + MAX_AUTOCOMPLETE_RESULTS));
		}
		else if (field.matches("pl\\d|complex")) {
			l.addAll(DatabaseHelper.executeNative("SELECT T.id, T." + l_ + ", CAST('" + City.alias + "' AS VARCHAR) FROM \"City\" T LEFT JOIN \"Country\" CN ON T.id_country=CN.id WHERE lower(T." + l_ + ") || ', ' || lower(CN.code) ~ E'" + regexp + "' ORDER BY T." + l_ + " LIMIT " + MAX_AUTOCOMPLETE_RESULTS));
			l.addAll(DatabaseHelper.executeNative("SELECT T.id, T." + l_ + ", CAST('" + Country.alias + "' AS VARCHAR) FROM \"Country\" T WHERE lower(T." + l_ + ") ~ E'" + regexp + "' ORDER BY T." + l_ + " LIMIT " + MAX_AUTOCOMPLETE_RESULTS));
		}
		StringBuffer html = new StringBuffer("<ul>");
		int n = 0;
		ArrayList<String> list = new ArrayList<String>();
		for (Object[] t : l) {
			if (n++ == MAX_AUTOCOMPLETE_RESULTS)
				break;
			String id = String.valueOf(t[0]);
			String text = String.valueOf(t[1]);
			String alias_ = String.valueOf(t[2]);
			if (list.contains(id))
				continue;
			Object o = DatabaseHelper.loadEntity(DatabaseHelper.getClassFromAlias(alias_), id);
			if (!(o instanceof Athlete) && !(o instanceof Complex) && !(o instanceof League) && !(o instanceof Team) && !(o instanceof Result) && !(o instanceof Contributor) && !(o instanceof HallOfFame) && !(o instanceof Olympics) && !(o instanceof Record) && !(o instanceof RetiredNumber) && !(o instanceof TeamStadium) && !(o instanceof WinLoss)) {
				Method m2 = o.getClass().getMethod("getLabel", String.class);
				text = String.valueOf(m2.invoke(o, lang));
			}
			if (o instanceof Event) {
				Event e = (Event) o;
				text += " (" + e.getType().getLabel(lang) + ")";
			}
			else if (o instanceof Complex) {
				Complex c = (Complex) o;
				text = c.getLabel() + ", " + c.getCity().getLabel(lang) + ", " + c.getCity().getCountry().getCode();
			}
			else if (o instanceof City) {
				City c = (City) o;
				text += ", " + c.getCountry().getCode();
			}
			else if (o instanceof Country) {
				Country c = (Country) o;
				text = c.toString2(lang);
			}
			else if (o instanceof Contributor) {
				Contributor c = (Contributor) o;
				text = c.getLogin();
			}
			else if (o instanceof Athlete) {
				Athlete a = (Athlete) o;
				text = a.toString2();
			}
			else if (o instanceof Team) {
				Team t_ = (Team) o;
				text = t_.toString2() + (isData ? ", " + t_.getSport().getLabel(lang) : "") + (StringUtils.notEmpty(t_.getYear1()) ? " [" + t_.getYear1() + "-" + (StringUtils.notEmpty(t_.getYear2()) ? t_.getYear2() : "") + "]" : "");
			}
			else if (o instanceof League) {
				League l__ = (League) o;
				text = l__.getLabel();
			}
			else if (o instanceof Olympics) {
				Olympics o_ = (Olympics) o;
				text = o_.toString2(lang);
			}
			else if (o instanceof Result) {
				Result r = (Result) o;
				text = r.toString2(lang);
			}
			else if (o instanceof HallOfFame) {
				HallOfFame h = (HallOfFame) o;
				text = h.toString2();
			}
			else if (o instanceof Record) {
				Record r = (Record) o;
				text = r.toString2(lang);
			}
			else if (o instanceof RetiredNumber) {
				RetiredNumber r = (RetiredNumber) o;
				text = r.toString2();
			}
			else if (o instanceof TeamStadium) {
				TeamStadium t_ = (TeamStadium) o;
				text = t_.toString2();
			}
			else if (o instanceof WinLoss) {
				WinLoss w = (WinLoss) o;
				text = w.toString2();
			}
			text += "<div class='ajaxid'>&nbsp;[#" + id + "]</div>";
			html.append("<li id='" + field_ + "|" + id + (o instanceof Event ? "|" + ((Event)o).getType().getNumber() : "") + "'>" + text + "</li>");
			list.add(id);
		}
		ServletHelper.writeText(response, html.append("</ul>").toString());
	}
	
	private static void saveResult(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		HashMap<Object, Integer> hInserted = new HashMap<Object, Integer>();
		StringBuffer sbMsg = new StringBuffer();
		StringBuffer sbMsgW = new StringBuffer();
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
				s = (Sport) DatabaseHelper.saveEntity(s, cb);
				result.setSport(s);
			}
			// Championship
			result.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, hParams.get("cp")));
			if (result.getChampionship() == null) {
				Championship c = new Championship();
				c.setLabel(String.valueOf(hParams.get("cp-l")));
				c.setLabelFr(c.getLabel());
				c.setIndex(Float.MAX_VALUE);
				c = (Championship) DatabaseHelper.saveEntity(c, cb);
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
				e.setIndex(Float.MAX_VALUE);
				e = (Event) DatabaseHelper.saveEntity(e, cb);
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
					e.setIndex(Float.MAX_VALUE);
					e = (Event) DatabaseHelper.saveEntity(e, cb);
					result.setSubevent(e);
				}
				tp = result.getSubevent().getType().getNumber();
			}
			else
				result.setSubevent(null);
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
					e.setIndex(Float.MAX_VALUE);
					e = (Event) DatabaseHelper.saveEntity(e, cb);
					result.setSubevent2(e);
				}
				tp = result.getSubevent2().getType().getNumber();
			}
			else
				result.setSubevent2(null);
			// Year
			result.setYear((Year)DatabaseHelper.loadEntity(Year.class, hParams.get("yr")));
			if (result.getYear() == null) {
				Year y = new Year();
				y.setLabel(String.valueOf(hParams.get("yr-l")));
				y = (Year) DatabaseHelper.saveEntity(y, cb);
				result.setYear(y);
			}
			// Places
			for(int i : new int[]{1, 2}) {
				if (StringUtils.notEmpty(hParams.get("pl" + i + "-l"))) {
					String[] t = String.valueOf(hParams.get("pl" + i + "-l")).toLowerCase().split("\\,\\s");
					boolean isComplex = false;
					boolean isCity = false;
					if (t.length > 2)
						isComplex = true;
					else if (t.length > 1)
						isCity = true;
					int id = 0;
					if (StringUtils.notEmpty(hParams.get("pl" + i)))
						id = new Integer(String.valueOf(hParams.get("pl" + i)));
					else
						id = DatabaseHelper.insertPlace(0, String.valueOf(hParams.get("pl" + i + "-l")), cb, null, lang);
					if (isComplex) {
						if (i == 1) {
							result.setComplex1((Complex)DatabaseHelper.loadEntity(Complex.class, id));
							result.setCity1(null);
							result.setCountry1(null);
						}
						else {
							result.setComplex2((Complex)DatabaseHelper.loadEntity(Complex.class, id));
							result.setCity2(null);
							result.setCountry2(null);
						}
					}
					else if (isCity){
						if (i == 1) {
							result.setCity1((City)DatabaseHelper.loadEntity(City.class, id));
							result.setComplex1(null);
							result.setCountry1(null);
						}
						else {
							result.setCity2((City)DatabaseHelper.loadEntity(City.class, id));
							result.setComplex2(null);
							result.setCountry2(null);
						}
					}
					else {
						if (i == 1) {
							result.setCountry1((Country)DatabaseHelper.loadEntity(Country.class, id));
							result.setCity1(null);
							result.setComplex1(null);
						}
						else {
							result.setCountry2((Country)DatabaseHelper.loadEntity(Country.class, id));
							result.setCity2(null);
							result.setComplex2(null);
						}
					}
				}
				else {
					if (i == 1) {
						result.setCity1(null);
						result.setComplex1(null);
					}
					else {
						result.setCity2(null);
						result.setComplex2(null);
					}
				}
			}
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			df.setLenient(false);
			if (StringUtils.notEmpty(hParams.get("dt1-l"))) {
				String dt = String.valueOf(hParams.get("dt1-l"));
				try {
					df.parse(dt);
				}
				catch (ParseException e) {
					throw new Exception(ResourceUtils.getText("err.invalid.date", lang).replaceAll("#S#", dt));
				}
				result.setDate1(dt);
			}
			else
				result.setDate1(null);
			if (StringUtils.notEmpty(hParams.get("dt2-l"))) {
				String dt = String.valueOf(hParams.get("dt2-l"));
				try {
					df.parse(dt);
				}
				catch (ParseException e) {
					throw new Exception(ResourceUtils.getText("err.invalid.date", lang).replaceAll("#S#", dt));
				}
				result.setDate2(dt);
			}
			else
				result.setDate2(null);
			result.setComment(StringUtils.notEmpty(hParams.get("cmt-l")) ? String.valueOf(hParams.get("cmt-l")) : null);
			result.setExa(StringUtils.notEmpty(hParams.get("exa-l")) ? String.valueOf(hParams.get("exa-l")) : null);
			result.setPhotoSource(StringUtils.notEmpty(hParams.get("source-l")) ? String.valueOf(hParams.get("source-l")) : null);
			result.setDraft(String.valueOf(hParams.get("draft")).equals("1"));
			// Inactive item
			String hql = "from InactiveItem where id_sport=" + result.getSport().getId() + " and id_championship=" + result.getChampionship().getId() + " and id_event=" + result.getEvent().getId();
			hql += (result.getSubevent() != null ? " and id_subevent=" + result.getSubevent().getId() : "");
			hql += (result.getSubevent2() != null ? " and id_subevent2=" + result.getSubevent2().getId() : "");
			Object o = DatabaseHelper.loadEntityFromQuery(hql);
			String inact = (o != null ? "1" : "0");
			if (!inact.equals(String.valueOf(hParams.get("inact")))) {
				if (o != null)
					DatabaseHelper.removeEntity(o);
				else {
					InactiveItem item = new InactiveItem();
					item.setIdSport(result.getSport().getId());
					item.setIdChampionship(result.getChampionship().getId());
					item.setIdEvent(result.getEvent().getId());
					item.setIdSubevent(result.getSubevent() != null ? result.getSubevent().getId() : null);
					item.setIdSubevent2(result.getSubevent2() != null ? result.getSubevent2().getId() : null);
					DatabaseHelper.saveEntity(item, null);
				}
			}
			// Rankings
			for (int i = 1 ; i <= MAX_RANKS ; i++) {
				Integer id = (StringUtils.notEmpty(hParams.get("rk" + i)) ? new Integer(String.valueOf(hParams.get("rk" + i))) : 0);
				o = hParams.get("rk" + i + "-l");
				if (id == 0 && StringUtils.notEmpty(o)) {
					if (hInserted.keySet().contains(o))
						id = hInserted.get(o);
					else {
						StringBuffer sb = new StringBuffer();
						id = DatabaseHelper.insertEntity(0, tp, result.getSport() != null ? result.getSport().getId() : 0, String.valueOf(o), result.getYear().getLabel(), cb, sb, lang);
						hInserted.put(o, id);
						if (sb != null && sb.toString().contains("New Team"))
							sbMsgW.append("<span style='color:orange;'>(" + ResourceUtils.getText("warning.team.created", lang).replaceAll("\\{1\\}", "\"" + sb.toString().replaceAll("\\,.*", "").split("\\|")[1].trim() + "\"") + ")</span>");
					}
				}
				Result.class.getMethod("setIdRank" + i, Integer.class).invoke(result, id > 0 ? id : null);
				Result.class.getMethod("setResult" + i, String.class).invoke(result, StringUtils.notEmpty(hParams.get("rs" + i + "-l")) ? hParams.get("rs" + i + "-l") : null);
			}
			result = (Result) DatabaseHelper.saveEntity(result, cb);
			// External links
			DatabaseHelper.saveExternalLinks(Result.alias, result.getId(), String.valueOf(hParams.get("exl-l")));
			// Person List
			if (hParams.containsKey("rk1list")) {
				int i = 1;
				DatabaseHelper.executeUpdate("DELETE FROM \"~PersonList\" WHERE ID_RESULT=" + result.getId());
				while (hParams.containsKey("rk" + i + "list")) {
					String[] t = String.valueOf(hParams.get("rk" + i + "list")).split("\\|", 0);
					for (String value : t) {
						String[] t_ = value.split("\\:", -1);
						String idp = t_[0];
						if (StringUtils.notEmpty(idp) && !idp.equals("null") && !idp.startsWith("Name #")) {
							String index = (t_.length > 1 ? t_[1] : null);
							PersonList plist = new PersonList();
							plist.setIdResult(result.getId());
							plist.setRank(i);
							if (idp.matches("\\d+"))
								plist.setIdPerson(Integer.parseInt(idp));
							else
								plist.setIdPerson(DatabaseHelper.insertEntity(0, 1, result.getSport() != null ? result.getSport().getId() : 0, idp, result.getYear().getLabel(), cb, null, lang));
							plist.setIndex(StringUtils.notEmpty(index) && !index.equals("null") ? index : null);
							DatabaseHelper.saveEntity(plist, cb);
						}
					}
					i++;
				}
			}
			// Rounds
			if (hParams.containsKey("rdlist")) {
				String[] t = String.valueOf(hParams.get("rdlist")).split("\\|", 0);
				for (String value : t) {
					String[] t_ = value.split("\\~", -1);
					if (t_.length > 1) {
						String idr = (idRS != null ? t_[0] : null);
						RoundType rdRt = null;
						if (StringUtils.notEmpty(t_[1]))
							rdRt = (RoundType) DatabaseHelper.loadEntity(RoundType.class, t_[1]);
						else {
							RoundType rdt = new RoundType();
							rdt.setLabel(t_[2]);
							rdt.setLabelFr(t_[2]);
							rdt.setIndex(0.0f);
							rdRt = (RoundType) DatabaseHelper.saveEntity(rdt, cb);
						}
						Integer rdRk1 = (StringUtils.notEmpty(t_[3]) ? Integer.parseInt(t_[3]) : (StringUtils.notEmpty(t_[4]) ? DatabaseHelper.insertEntity(0, tp, (result.getSport() != null ? result.getSport().getId() : 0), t_[4], result.getYear().getLabel(), cb, null, lang) : 0));
						String rdRs1 = (StringUtils.notEmpty(t_[5]) ? t_[5] : null);
						Integer rdRk2 = (StringUtils.notEmpty(t_[6]) ? Integer.parseInt(t_[6]) : (StringUtils.notEmpty(t_[7]) ? DatabaseHelper.insertEntity(0, tp, (result.getSport() != null ? result.getSport().getId() : 0), t_[7], result.getYear().getLabel(), cb, null, lang) : 0));
						String rdRs2 = (StringUtils.notEmpty(t_[8]) ? t_[8] : null);
						Integer rdRk3 = (StringUtils.notEmpty(t_[9]) ? Integer.parseInt(t_[9]) : (StringUtils.notEmpty(t_[10]) ? DatabaseHelper.insertEntity(0, tp, (result.getSport() != null ? result.getSport().getId() : 0), t_[10], result.getYear().getLabel(), cb, null, lang) : 0));
						String rdRs3 = (StringUtils.notEmpty(t_[11]) ? t_[11] : null);
						String rdDt1 = (StringUtils.notEmpty(t_[12]) ? t_[12] : null);
						String rdDt2 = (StringUtils.notEmpty(t_[13]) ? t_[13] : null);
						Complex rdCx1 = null; Complex rdCx2 = null;
						City rdCt1 = null; City rdCt2 = null;
						if (StringUtils.notEmpty(t_[14]) || StringUtils.notEmpty(t_[15])) {
							String[] tpl = t_[15].toLowerCase().split("\\,\\s");
							if (tpl.length > 1) {
								int id = 0;
								if (StringUtils.notEmpty(t_[14]))
									id = new Integer(String.valueOf(t_[14]));
								else
									id = DatabaseHelper.insertPlace(0, String.valueOf(t_[15]), cb, null, lang);
								if (tpl.length > 2)
									rdCx1 = (Complex) DatabaseHelper.loadEntity(Complex.class, id);
								else
									rdCt1 = (City) DatabaseHelper.loadEntity(City.class, id);
							}
						}
						if (StringUtils.notEmpty(t_[16]) || StringUtils.notEmpty(t_[17])) {
							String[] tpl = t_[17].toLowerCase().split("\\,\\s");
							if (tpl.length > 1) {
								int id = 0;
								if (StringUtils.notEmpty(t_[16]))
									id = new Integer(String.valueOf(t_[16]));
								else
									id = DatabaseHelper.insertPlace(0, String.valueOf(t_[17]), cb, null, lang);
								if (tpl.length > 2)
									rdCx2 = (Complex) DatabaseHelper.loadEntity(Complex.class, id);
								else
									rdCt2 = (City) DatabaseHelper.loadEntity(City.class, id);
							}
						}
						String rdExa = (StringUtils.notEmpty(t_[18]) ? t_[18] : null);
						String rdCmt = (StringUtils.notEmpty(t_[19]) ? t_[19] : null);
						Round rd = (StringUtils.notEmpty(idr) ? (Round) DatabaseHelper.loadEntity(Round.class, idr) : new Round());
						rd.setIdResult(result.getId());
						rd.setIdResultType(tp);
						rd.setRoundType(rdRt);
						rd.setIdRank1(rdRk1 > 0 ? rdRk1 : null);
						rd.setResult1(rdRs1);
						rd.setIdRank2(rdRk2 > 0 ? rdRk2 : null);
						rd.setResult2(rdRs2);
						rd.setIdRank3(rdRk3 > 0 ? rdRk3 : null);
						rd.setResult3(rdRs3);
						rd.setComplex1(rdCx1);
						rd.setCity1(rdCt1);
						rd.setComplex2(rdCx2);
						rd.setCity2(rdCt2);
						rd.setDate1(rdDt1);
						rd.setDate2(rdDt2);
						rd.setExa(rdExa);
						rd.setComment(rdCmt);
						DatabaseHelper.saveEntity(rd, cb);
					}
				}
			}
			if (hParams.containsKey("rddel") && idRS != null) {
				String[] t = String.valueOf(hParams.get("rddel")).split("\\|", 0);
				for (String value : t)
					if (StringUtils.notEmpty(value))
						DatabaseHelper.removeEntity(DatabaseHelper.loadEntity(Round.class, Integer.parseInt(value)));
			}
			sbMsg.append(result.getId() + "#" + ResourceUtils.getText("result." + (idRS != null ? "modified" : "created"), lang));
			if (sbMsgW.length() > 0)
				sbMsg.append(" ").append(sbMsgW);
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void deleteResult(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			Object id = hParams.get("id");
			DatabaseHelper.removeEntity(DatabaseHelper.loadEntity(Result.class, StringUtils.toInt(id)));
			sbMsg.append(ResourceUtils.getText("result.deleted", lang));
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void dataTips(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
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
	
	private static void loadOverview(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		StringBuffer html = new StringBuffer();
		ArrayList<Object> lFuncParams = new ArrayList<Object>();
		Object pattern = hParams.get("pattern");
		lFuncParams.add(hParams.get("entity"));
		lFuncParams.add(StringUtils.toInt(hParams.get("sport")));
		lFuncParams.add(StringUtils.toInt(hParams.get("count")));
		lFuncParams.add(pattern);
		if (pattern != null && String.valueOf(pattern).matches("\\d+\\-\\d+")) {
			String[] t = String.valueOf(pattern).split("\\-");
			lFuncParams.add(Integer.parseInt(t[0]));
			lFuncParams.add(Integer.parseInt(t[1]));
		}
		else {
			lFuncParams.add(0);
			lFuncParams.add(0);
		}
		lFuncParams.add("_" + lang);
		String currentEntity = null;
		for (RefItem item : (List<RefItem>) DatabaseHelper.call("Overview", lFuncParams)) {
			if (currentEntity == null || !item.getEntity().equals(currentEntity)) {
				if (currentEntity != null)
					html.append("</tbody></table>");
				html.append("<table><thead><tr>");
				if (item.getEntity().equals(Result.alias))
					html.append("<th colspan='11' style='text-align:center;'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity." + Result.alias, lang).toUpperCase(), false) + "</th></tr><tr><th>" + ResourceUtils.getText("entity.YR.1", lang) + "</th><th>" + ResourceUtils.getText("entity.EV.1", lang) + "</th><th>" + ResourceUtils.getText("podium", lang) + "</th><th>" + ResourceUtils.getText("entity.RS", lang) + "</th><th>" + ResourceUtils.getText("final", lang) + "+" + ResourceUtils.getText("score", lang) + "</th><th>" + ResourceUtils.getText("entity.CX.1", lang) + "</th><th>" + ResourceUtils.getText("entity.CT.1", lang) + "</th><th>" + ResourceUtils.getText("date", lang) + "</th><th>" + ResourceUtils.getText("entity.RD", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("photo", lang) + "</th>");
				else if (item.getEntity().equals(Athlete.alias))
					html.append("<th colspan='8' style='text-align:center;'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity." + Athlete.alias, lang).toUpperCase(), false) + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("entity.SP.1", lang) + "</th><th>" + ResourceUtils.getText("entity.CN.1", lang) + "</th><th>" + ResourceUtils.getText("entity.TM.1", lang) + "</th><th>" + ResourceUtils.getText("linked.to", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("photo", lang) + "</th>");
				else if (item.getEntity().equals(Team.alias))
					html.append("<th colspan='8' style='text-align:center;'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity." + Team.alias, lang).toUpperCase(), false) + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("entity.SP.1", lang) + "</th><th>" + ResourceUtils.getText("entity.CN.1", lang) + "</th><th>" + ResourceUtils.getText("league", lang) + "</th><th>" + ResourceUtils.getText("linked.to", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("logo", lang) + "</th>");
				else if (item.getEntity().equals(Sport.alias))
					html.append("<th colspan='4' style='text-align:center;'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity." + Sport.alias, lang).toUpperCase(), false) + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("picture", lang) + "</th>");
				else if (item.getEntity().equals(Championship.alias))
					html.append("<th colspan='4' style='text-align:center;'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity." + Championship.alias, lang).toUpperCase(), false) + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("picture", lang) + "</th>");
				else if (item.getEntity().equals(Event.alias))
					html.append("<th colspan='4' style='text-align:center;'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity." + Event.alias, lang).toUpperCase(), false) + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("picture", lang) + "</th>");
				else if (item.getEntity().equals(City.alias))
					html.append("<th colspan='6' style='text-align:center;'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity." + City.alias, lang).toUpperCase(), false) + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("entity.CN.1", lang) + "</th><th>" + ResourceUtils.getText("linked.to", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("picture", lang) + "</th>");
				else if (item.getEntity().equals(Complex.alias))
					html.append("<th colspan='6' style='text-align:center;'>" + HtmlUtils.writeToggleTitle(ResourceUtils.getText("entity." + Complex.alias, lang).toUpperCase(), false) + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("entity.CT.1", lang) + "</th><th>" + ResourceUtils.getText("linked.to", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("picture", lang) + "</th>");
				html.append("</tr></thead><tbody class='tby'>");
				currentEntity = item.getEntity();
			}
			boolean isPhoto = StringUtils.notEmpty(ImageUtils.getPhotoFile(item.getEntity(), item.getIdItem()));
			boolean isNopic = (item.getCount3() != null && item.getCount3() == 1);
			int picsL = ImageUtils.getImageList(ImageUtils.getIndex(item.getEntity()), item.getIdItem(), ImageUtils.SIZE_LARGE).size();
			int picsS = ImageUtils.getImageList(ImageUtils.getIndex(item.getEntity()), item.getIdItem(), ImageUtils.SIZE_SMALL).size();
			String href = "#";
			html.append("<tr>");
			//html.append("<td>" + item.getIdItem() + "</td>");
			if (item.getEntity().equals(Result.alias)) {
				int rkcount = (item.getTxt3() != null ? item.getTxt3().split("\\,").length : 0);
				int rscount = (item.getTxt4() != null ? item.getTxt4().split("\\,").length : 0);
				boolean isScore = (rkcount >= 2 && rscount == 1);
				String[] tplace = item.getTxt1().split("\\,", -1);
				int cxcount = (StringUtils.notEmpty(tplace[0]) && !tplace[0].equals("0") ? 1 : 0) + (StringUtils.notEmpty(tplace[1]) && !tplace[1].equals("0") ? 1 : 0);
				int ctcount = (StringUtils.notEmpty(tplace[2]) && !tplace[2].equals("0") ? 1 : 0) + (StringUtils.notEmpty(tplace[3]) && !tplace[3].equals("0") ? 1 : 0);
				String[] tdate = item.getTxt2().split("\\,", -1);
				int dtcount = (StringUtils.notEmpty(tdate[0]) && !tdate[0].equals("0") ? 1 : 0) + (StringUtils.notEmpty(tdate[1]) && !tdate[1].equals("0") ? 1 : 0);
				html.append("<td>" + item.getLabelRel1() + "</td>");
				html.append("<td><a href='/update/results/" + StringUtils.encode(Result.alias + "-" + item.getIdItem()) + "' target='_blank'>" + item.getLabelRel2() + " - " + item.getLabelRel3() + (StringUtils.notEmpty(item.getLabelRel4()) ? " - " + item.getLabelRel4() : "") + (StringUtils.notEmpty(item.getLabelRel5()) ? " - " + item.getLabelRel5() : "") + (StringUtils.notEmpty(item.getLabelRel6()) ? " - " + item.getLabelRel6() : "") + "</a></td>");
				html.append("<td" + (rkcount >= 3 ? " class='tick'>(" + rkcount + ")" : ">") + "</td>");
				html.append("<td" + (rscount >= 3 ? " class='tick'>(" + rscount + ")" : ">") + "</td>");
				html.append("<td" + (isScore ? " class='tick'" : "") + "></td>");
				html.append("<td" + (cxcount > 0 ? " class='tick'>(" + cxcount + ")" : ">") + "</td>");
				html.append("<td" + (ctcount > 0 ? " class='tick'>(" + ctcount + ")" : ">") + "</td>");
				html.append("<td" + (dtcount > 0 ? " class='tick'>(" + dtcount + ")" : " class='missing'>") + "</td>");
				html.append(StringUtils.notEmpty(item.getTxt6()) ? "<td class='tick'>" + item.getTxt6().split("\\,").length + "</td>" : "<td></td>");
			}
			else if (item.getEntity().equals(Athlete.alias)) {
				html.append("<td><a href='" + href + "'>" + item.getLabelRel1() + ",&nbsp;" + item.getLabelRel2() + "</a></td>");
				html.append("<td" + (StringUtils.notEmpty(item.getLabelRel5()) ? ">" + item.getLabelRel5() : " class='missing'>") + "</td>");
				html.append("<td" + (StringUtils.notEmpty(item.getLabelRel3()) ? ">" + item.getLabelRel3() : " class='missing'>") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(item.getLabelRel4()) ? item.getLabelRel4() : "-") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(item.getLabelEN()) ? item.getLabelEN() : "-") + "</td>");
				html.append("<td>" + item.getCount2() + "</td>");
			}
			else if (item.getEntity().equals(Team.alias)) {
				html.append("<td><a href='" + href + "'>" + item.getLabelRel1() + "</a></td>");
				html.append("<td" + (StringUtils.notEmpty(item.getLabelRel2()) ? ">" + item.getLabelRel2() : " class='missing'>") + "</td>");
				html.append("<td" + (StringUtils.notEmpty(item.getLabelRel3()) ? ">" + item.getLabelRel3() : " class='missing'>") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(item.getLabelRel4()) ? item.getLabelRel4() : "-") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(item.getLabelEN()) ? item.getLabelEN() : "-") + "</td>");
				html.append("<td>" + item.getCount2() + "</td>");
			}
			else if (item.getEntity().equals(Sport.alias)) {
				html.append("<td><a href='" + href + "'>" + item.getLabelRel1() + "</a></td>");
				html.append("<td>" + item.getCount2() + "</td>");
			}
			else if (item.getEntity().equals(Championship.alias)) {
				html.append("<td><a href='" + href + "'>" + item.getLabelRel1() + "</a></td>");
				html.append("<td>" + item.getCount2() + "</td>");
			}
			else if (item.getEntity().equals(Event.alias)) {
				html.append("<td><a href='" + href + "'>" + item.getLabelRel1() + "</a></td>");
				html.append("<td>" + item.getCount2() + "</td>");
			}
			else if (item.getEntity().equals(City.alias)) {
				html.append("<td><a href='" + href + "'>" + item.getLabelRel1() + "</a></td>");
				html.append("<td" + (StringUtils.notEmpty(item.getLabelRel2()) ? ">" + item.getLabelRel2() : " class='missing'>") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(item.getLabelEN()) ? item.getLabelEN() : "-") + "</td>");
				html.append("<td>" + item.getCount2() + "</td>");
			}
			else if (item.getEntity().equals(Complex.alias)) {
				html.append("<td><a href='" + href + "'>" + item.getLabelRel1() + "</a></td>");
				html.append("<td" + (StringUtils.notEmpty(item.getLabelRel2()) ? ">" + item.getLabelRel2() + ", " + item.getLabelRel3() : " class='missing'>") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(item.getLabelEN()) ? item.getLabelEN() : "-") + "</td>");
				html.append("<td>" + item.getCount2() + "</td>");
			}
			html.append(StringUtils.notEmpty(item.getLabel()) ? "<td class='tick'>" + item.getLabel().split("\\,").length + "</td>" : "<td></td>");
			if (item.getEntity().matches(Athlete.alias + "|" + Result.alias))
				html.append("<td" + (isPhoto ? " class='tick'" : " class='missing'") + "></td>");
			else
				html.append("<td" + (picsL > 0 && picsS > 0 ? " class='tick'>(" + picsL + "L+" + picsS + "S)" : (isNopic ? ">-" : " class='missing'>")) + "</td>");
			if (hParams.get("showimg").equals("1")) {
				short index = ImageUtils.getIndex(item.getEntity());
				if (index != -1) {
					html.append("<td>");
					for (String img : ImageUtils.getImageList(index, item.getIdItem(), ImageUtils.SIZE_SMALL))
						html.append("<a href='" + ImageUtils.getUrl() + img.replaceFirst("\\-S", "\\-L") + "' target='_blank'><img title='" + img + "' src='" + ImageUtils.getUrl() + img + "'/></a>");
					html.append("</td>");
				}
			}
			html.append("</tr>");
		}
		ServletHelper.writeText(response, html.append("</tbody></table>").toString());
	}
	
	private static void saveConfig(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			for (Object o : hParams.keySet()) {
				String p = String.valueOf(o);
				if (p.startsWith("p_")) {
					Config c = (Config) DatabaseHelper.loadEntity(Config.class, p.substring(2));
					if (c != null) {
						if (c.getKey().startsWith("html")) {
							c.setValue(null);
							c.setValueHtml(String.valueOf(hParams.get(p)));
						}
						else {
							c.setValue(String.valueOf(hParams.get(p)));
							c.setValueHtml(null);
						}
						DatabaseHelper.saveEntity(c, null);
					}
				}
				else if (p.equals("new") && StringUtils.notEmpty(hParams.get(p))) {
					for (String s : String.valueOf(hParams.get(p)).split("\r\n")) {
						String[] t = s.split("\\=", -1);
						Config c = new Config();
						c.setKey(t[0]);
						c.setValue(t[1]);
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
	
	private static void executeQuery(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		boolean isCSV = hParams.containsKey("csv");
		StringBuffer sb = new StringBuffer(!isCSV ? "<table>" : "");
		ArrayList<String> queries = new ArrayList<String>();
		queries.add("SELECT DISTINCT LAST_NAME || ',' || FIRST_NAME || ',' || ID_SPORT AS N, COUNT(*) AS C\r\nFROM \"Athlete\"\r\nWHERE LINK IS NULL\r\nGROUP BY N\r\nORDER BY C DESC\r\nLIMIT 100");
		queries.add("SELECT DISTINCT LABEL AS N, COUNT(*) AS C\r\nFROM \"City\"\r\nWHERE LINK IS NULL\r\nGROUP BY N\r\nORDER BY C DESC\r\nLIMIT 100");
		queries.add("SELECT 'EV', ID, LABEL FROM \"Event\"\r\nWHERE ID NOT IN (SELECT ID_EVENT FROM \"Result\" WHERE ID_EVENT IS NOT NULL) AND ID NOT IN (SELECT ID_SUBEVENT FROM \"Result\" WHERE ID_SUBEVENT IS NOT NULL) AND ID NOT IN (SELECT ID_SUBEVENT2 FROM \"Result\" WHERE ID_SUBEVENT2 IS NOT NULL)\r\nAND ID NOT IN (SELECT ID_EVENT FROM \"Record\" WHERE ID_EVENT IS NOT NULL) AND ID NOT IN (SELECT ID_SUBEVENT FROM \"Record\" WHERE ID_SUBEVENT IS NOT NULL)\r\nUNION SELECT 'CP', ID, LABEL FROM \"Championship\" WHERE ID NOT IN (SELECT ID_CHAMPIONSHIP FROM \"Result\" WHERE ID_CHAMPIONSHIP IS NOT NULL)\r\nAND ID NOT IN (SELECT ID_CHAMPIONSHIP FROM \"Record\" WHERE ID_CHAMPIONSHIP IS NOT NULL)\r\nORDER BY 1, 3");
		queries.add("SELECT SP.label AS SPORT, CP.label AS Championship, EV.label AS EVENT, SE.label AS SUBEVENT, SE2.label AS SUBEVENT2, YR.label AS YEAR\r\nFROM (SELECT DISTINCT id_sport, id_championship, id_event, id_subevent, id_subevent2 FROM \"Result\" EXCEPT SELECT DISTINCT id_sport, id_championship, id_event, id_subevent, id_subevent2 FROM \"Result\" WHERE id_year = (SELECT id FROM \"Year\" WHERE label = '#YEAR#')) T\r\nLEFT JOIN \"Sport\" SP ON T.id_sport = SP.id\r\nLEFT JOIN \"Championship\" CP ON T.id_championship = CP.id LEFT JOIN \"Event\" EV ON T.id_event = EV.id\r\nLEFT JOIN \"Event\" SE ON T.id_subevent = SE.id LEFT JOIN \"Event\" SE2 ON T.id_subevent2 = SE2.id LEFT JOIN \"Year\" YR ON YR.label = '#YEAR#'\r\nLEFT JOIN \"~InactiveItem\" II ON (T.id_sport=II.id_sport AND T.id_championship=II.id_championship AND T.id_event=II.id_event AND (T.id_subevent IS NULL OR T.id_subevent=II.id_subevent) AND (T.id_subevent2 IS NULL OR T.id_subevent2=II.id_subevent2))\r\nWHERE 1=1 AND #WHERE# AND II.id IS NULL\r\nORDER BY SP.label, CP.index, CP.label, EV.index, EV.label, SE.index, SE.label, SE2.index, SE2.label");
		queries.add("SELECT DISTINCT id_sport, id_championship, id_event, id_subevent, id_subevent2, SP.label AS label1, CP.label AS label2, EV.label AS label3, SE.label AS label4, SE2.label AS label5 FROM \"Result\" RS LEFT JOIN \"Sport\" SP ON RS.id_sport=SP.id LEFT JOIN \"Championship\" CP ON RS.id_championship=CP.id LEFT JOIN \"Event\" EV ON RS.id_event=EV.id LEFT JOIN \"Event\" SE ON RS.id_subevent=SE.id LEFT JOIN \"Event\" SE2 ON RS.id_subevent2=SE2.id ORDER BY SP.label, CP.label, EV.label, SE.label, SE2.label");
		queries.add("SELECT * FROM (SELECT 'CP', ID, LABEL FROM \"Championship\" WHERE LABEL=LABEL_FR UNION SELECT 'CT', ID, LABEL FROM \"City\" WHERE LABEL=LABEL_FR UNION SELECT 'CX', ID, LABEL FROM \"Complex\" WHERE LABEL=LABEL_FR UNION SELECT 'CN', ID, LABEL FROM \"Country\" WHERE LABEL=LABEL_FR UNION SELECT 'EV', ID, LABEL FROM \"Event\" WHERE LABEL=LABEL_FR UNION SELECT 'SP', ID, LABEL FROM \"Sport\" WHERE LABEL=LABEL_FR ) T ORDER BY 1,2");
		queries.add("SELECT DISTINCT SP.label || '-' || CP.label || '-' || EV.label || (CASE WHEN SE.id IS NOT NULL THEN '-' || SE.label ELSE '' END) || (CASE WHEN SE2.id IS NOT NULL THEN '-' || SE2.label ELSE '' END), COUNT(*) AS N FROM \"Result\" RS LEFT JOIN \"Sport\" SP ON RS.id_sport=SP.id LEFT JOIN \"Championship\" CP ON RS.id_championship=CP.id LEFT JOIN \"Event\" EV ON RS.id_event=EV.id LEFT JOIN \"Event\" SE ON RS.id_subevent=SE.id LEFT JOIN \"Event\" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN \"~InactiveItem\" II ON (RS.id_sport = II.id_sport AND RS.id_championship = II.id_championship AND RS.id_event = II.id_event AND (RS.id_subevent = II.id_subevent OR RS.id_subevent IS NULL) AND (RS.id_subevent2 = II.id_subevent2 OR RS.id_subevent2 IS NULL)) WHERE II.id IS NULL GROUP BY 1 HAVING COUNT(*)<5 ORDER BY 2, 1");
		queries.add("SELECT 'PR', id, last_name || ', ' || first_name AS label FROM \"Athlete\" WHERE id_country IS NULL UNION SELECT 'TM', id, label FROM \"Team\" WHERE id_country IS NULL ORDER BY 1, 3");
		queries.add("SELECT 'CT', id, label FROM \"City\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='CT') UNION SELECT 'CX', id, label FROM \"Complex\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='CX') UNION SELECT 'CN', id, label FROM \"Country\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='CN') UNION SELECT 'CP', id, label FROM \"Championship\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='CP') UNION SELECT 'EV', id, label FROM \"Event\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='EV') UNION SELECT 'PR', id, last_name || ', ' || first_name FROM \"Athlete\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='PR') UNION SELECT 'RS', RS.id, SP.label || '-' || CP.label || '-' || EV.label || '-' || YR.label AS label FROM \"Result\" RS LEFT JOIN \"Sport\" SP ON RS.id_sport=SP.id LEFT JOIN \"Championship\" CP ON RS.id_championship=CP.id LEFT JOIN \"Event\" EV ON RS.id_event=EV.id LEFT JOIN \"Year\" YR ON RS.id_year=YR.id WHERE RS.id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='RS') UNION SELECT 'SP', id, label FROM \"Sport\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='SP') UNION SELECT 'TM', id, label FROM \"Team\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='TM') ORDER BY 1, 3");
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String query = null;
		Integer index = new Integer(String.valueOf(hParams.get("index")));
		if (index != -1) {
			query = queries.get(index);
			query = query.replaceAll("#YEAR#", String.valueOf(year));
			query = query.replaceAll("#WHERE#", (year % 4 == 0 ? "(CP.id<>1 OR SP.type<>0)" : (year % 4 == 2 ? "(CP.id<>1 OR SP.type<>1)" : "CP.id<>1")) + (year % 4 != 1 ? " AND CP.id<>78" : ""));			
		}
		else
			query = String.valueOf(hParams.get("query"));
		
		if (!isCSV)
			sb.append("<tr style='display:none;'><td>" + query + "</td></tr>");
		
		List<Object[]> list = (List<Object[]>) DatabaseHelper.executeNative(query);
		if (list != null && list.size() > 0) {
			int i = 0;
			boolean isFirstRow = true;
			for (Object[] t : list)  {
				if (!isCSV)
					sb.append("<tr>");
				if (isFirstRow) {
					for (i = 1 ; i <= t.length ; i++)
						sb.append(!isCSV ? "<th>" : (i > 1 ? "," : "")).append("Col." + i).append(!isCSV ? "</th>" : "");
					sb.append(isCSV ? "\r\n" : "</tr><tr>");
					isFirstRow = false;
				}
				i = 0;
				for (Object o : t)
					sb.append(!isCSV ? "<td>" : (i++ > 0 ? "," : "")).append(o != null ? String.valueOf(o) : "").append(!isCSV ? "</td>" : "");
				sb.append(isCSV ? "\r\n" : "</tr>");
			}
		}

		if (!isCSV)
			sb.append("</table>");

		if (isCSV) {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=query" + (hParams.get("index"))+ ".csv");
			response.setContentType("text/csv");
			response.getWriter().write(sb.toString());
		}
		else
			ServletHelper.writeText(response, sb.toString());
	}
	
	private static void mergeEntity(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		String msg = "";
		try {
			String alias = String.valueOf(hParams.get("alias"));
			Integer id1 = StringUtils.toInt(hParams.get("id1"));
			Integer id2 = StringUtils.toInt(hParams.get("id2"));
			Object o1 = DatabaseHelper.loadEntity(DatabaseHelper.getClassFromAlias(alias), id1);
			Object o2 = DatabaseHelper.loadEntity(DatabaseHelper.getClassFromAlias(alias), id2);
			if (hParams.containsKey("confirm"))
				msg = ResourceUtils.getText("confirm.merge", lang).replaceFirst("\\{1\\}", "<br/><b>" + o1.toString() + "</b><br/><br/>").replaceFirst("\\{2\\}", "<br/><b>" + o2.toString() + "</b>");
			else {
				DatabaseHelper.executeNative("select \"~Merge\"('" + alias + "', " + id1 + ", " + id2 + ")");
				msg = ResourceUtils.getText("merge.success", lang).replaceFirst("\\{1\\}", String.valueOf(id1)).replaceFirst("\\{2\\}", String.valueOf(id2));
			}
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
			msg = "ERR:" + e.getMessage();
		}
		finally {
			ServletHelper.writeText(response, msg);
		}
	}
	
	private static void loadResult(HttpServletRequest request, HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		Object tp = hParams.get("tp");
		request.setAttribute("title", StringUtils.getTitle(ResourceUtils.getText("update.results", lang)));
		String p = String.valueOf(hParams.get("p"));
		p = StringUtils.decode(p);
		Object[] t = p.split("\\-");
		Result rs = null;
		Year yr = null;
		List<Result> lResult = null;

		if (tp != null) {
			String where = null;
			if (String.valueOf(tp).equalsIgnoreCase("direct"))
				where = "id=" + hParams.get("id");
			else if (String.valueOf(tp).equalsIgnoreCase("year") && StringUtils.notEmpty(hParams.get("yrfind")))
				where = "year.label='" + hParams.get("yrfind") + "' and sport.id=" + hParams.get("sp") + " and championship.id=" + hParams.get("cp") + " and event.id=" + hParams.get("ev") + (StringUtils.notEmpty(hParams.get("se")) ? " and subevent.id=" + hParams.get("se") : "") + (StringUtils.notEmpty(hParams.get("se2")) ? " and subevent2.id=" + hParams.get("se2") : "");
			else if (String.valueOf(tp).matches("first|last"))
				where = "sport.id=" + hParams.get("sp") + " and championship.id=" + hParams.get("cp") + " and event.id=" + hParams.get("ev") + (StringUtils.notEmpty(hParams.get("se")) ? " and subevent.id=" + hParams.get("se") : "") + (StringUtils.notEmpty(hParams.get("se2")) ? " and subevent2.id=" + hParams.get("se2") : "") + " order by year.id " + (tp.equals("first") ? "asc" : "desc");			
			else if (StringUtils.notEmpty(hParams.get("yr")))
				where = "year.id " + (tp.equals("next") ? ">" : "<") + " " + hParams.get("yr") + " and sport.id=" + hParams.get("sp") + " and championship.id=" + hParams.get("cp") + " and event.id=" + hParams.get("ev") + (StringUtils.notEmpty(hParams.get("se")) ? " and subevent.id=" + hParams.get("se") : "") + (StringUtils.notEmpty(hParams.get("se2")) ? " and subevent2.id=" + hParams.get("se2") : "") + " order by year.id " + (tp.equals("next") ? "asc" : "desc");
			if (where != null) {
				lResult = DatabaseHelper.execute("from Result where " + where);
				if (lResult != null && !lResult.isEmpty())
					rs = lResult.get(0);
			}
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
				if (rs != null) {
					String s = rs.getSport().getId() + "-" + rs.getChampionship().getId() + "-" + rs.getEvent().getId() + (rs.getSubevent() != null ? "-" + rs.getSubevent().getId() : "") + (rs.getSubevent2() != null ? "-" + rs.getSubevent2().getId() : "");
					yr = rs.getYear();
					t = s.split("\\-");
				}
			}
			StringBuffer sb = new StringBuffer();
			Event ev = null;
			Event se = null;
			Event se2 = null;
			String path = null;
			if (t.length > 0 && String.valueOf(t[0]).matches("\\d+")) {
				Sport sp = (Sport)DatabaseHelper.loadEntity(Sport.class, t[0]);
				Championship cp = (Championship)DatabaseHelper.loadEntity(Championship.class, t[1]); 
				ev = (Event)DatabaseHelper.loadEntity(Event.class, t[2]);
				se = (Event)DatabaseHelper.loadEntity(Event.class, t.length > 3 ? t[3] : 0);
				se2 = (Event)DatabaseHelper.loadEntity(Event.class, t.length > 4 ? t[4] : 0);
				if (yr == null)
					yr = (Year)DatabaseHelper.loadEntityFromQuery("from Year where label='" + Calendar.getInstance().get(Calendar.YEAR) + "'");
				path = sp.getId() + "-" + cp.getId() + (ev != null ? "-" + ev.getId() : "") + (se != null ? "-" + se.getId() : "") + (se2 != null ? "-" + se2.getId() : "");
						
				sb.append(sp.getId()).append("~").append(sp.getLabel(lang)).append("~");
				sb.append(cp.getId()).append("~").append(cp.getLabel(lang)).append("~");
				sb.append(ev.getId()).append("~").append(ev.getLabel(lang) + " (" + ev.getType().getLabel(lang) + ")").append("~").append(ev.getType().getNumber()).append("~");
				sb.append(se != null ? se.getId() : "").append("~").append(se != null ? se.getLabel(lang) + " (" + se.getType().getLabel(lang) + ")" : "").append("~").append(se != null ? se.getType().getNumber() : "").append("~");
				sb.append(se2 != null ? se2.getId() : "").append("~").append(se2 != null ? se2.getLabel(lang) + " (" + se2.getType().getLabel(lang) + ")" : "").append("~").append(se2 != null ? se2.getType().getNumber() : "").append("~");
				sb.append(yr.getId()).append("~").append(yr.getLabel()).append("~");
			}
			if (rs != null) { // Existing result
				request.setAttribute("id", rs.getId());
				// Result Info
				sb.append(rs.getId()).append("~");
				sb.append(rs.getDate1()).append("~").append(rs.getDate2()).append("~");
				sb.append(rs.getComplex1() != null ? rs.getComplex1().getId() : (rs.getCity1() != null ? rs.getCity1().getId() : (rs.getCountry1() != null ? rs.getCountry1().getId() : ""))).append("~");
				sb.append(rs.getComplex1() != null ? rs.getComplex1().toString2(lang) : (rs.getCity1() != null ? rs.getCity1().toString2(lang) : (rs.getCountry1() != null ? rs.getCountry1().toString2(lang) : ""))).append("~");
				sb.append(rs.getComplex2() != null ? rs.getComplex2().getId() : (rs.getCity2() != null ? rs.getCity2().getId() : (rs.getCountry2() != null ? rs.getCountry2().getId() : ""))).append("~");
				sb.append(rs.getComplex2() != null ? rs.getComplex2().toString2(lang) : (rs.getCity2() != null ? rs.getCity2().toString2(lang) : (rs.getCountry2() != null ? rs.getCountry2().toString2(lang) : ""))).append("~");
				sb.append(rs.getExa()).append("~").append(rs.getComment()).append("~").append(ImageUtils.getPhotoFile(Result.alias, rs.getId())).append("~").append(rs.getPhotoSource()).append("~");
				sb.append(ResourceUtils.getText("metadata", lang).replaceFirst("\\{1\\}", StringUtils.toTextDate(rs.getMetadata().getFirstUpdate(), lang, "dd/MM/yyyy HH:mm")).replaceFirst("\\{2\\}", StringUtils.toTextDate(rs.getMetadata().getLastUpdate(), lang, "dd/MM/yyyy HH:mm")).replaceFirst("\\{3\\}", "<a target='_blank' href='" + HtmlUtils.writeLink(Contributor.alias, rs.getMetadata().getContributor().getId(), null, rs.getMetadata().getContributor().getLogin()) + "'>" + rs.getMetadata().getContributor().getLogin() + "</a>")).append("~");
				// Inactive item?
				String hql = "from InactiveItem where id_sport=" + rs.getSport().getId() + " and id_championship=" + rs.getChampionship().getId() + " and id_event=" + rs.getEvent().getId();
				hql += (rs.getSubevent() != null ? " and id_subevent=" + rs.getSubevent().getId() : "");
				hql += (rs.getSubevent2() != null ? " and id_subevent2=" + rs.getSubevent2().getId() : "");
				Object inact = DatabaseHelper.loadEntityFromQuery(hql);
				sb.append(inact != null ? "1" : "0").append("~");
				// Draft
				sb.append(rs.getDraft() != null && rs.getDraft() ? "1" : "0").append("~");
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
				// Person List
				List lPList = DatabaseHelper.execute("from PersonList where idResult=" + rs.getId() + " order by id");
				if (lPList != null && lPList.size() > 0) {
					List<String> l = new ArrayList<String>();
					for (PersonList pl : (List<PersonList>) lPList) {
						int rk = pl.getRank();
						if (l.size() < rk)
							l.add("");
						Athlete a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, pl.getIdPerson());
						l.set(rk - 1, (StringUtils.notEmpty(l.get(rk - 1)) ? l.get(rk - 1) + "|" : "") + pl.getIdPerson() + ":" + a.toString2() + ":" + (StringUtils.notEmpty(pl.getIndex()) ? pl.getIndex() : ""));
					}
					sb.append("rkl-" + StringUtils.join(l, "#")).append("~");
				}
				// Rounds
				List lRounds = DatabaseHelper.execute("from Round round where idResult=" + rs.getId() + " order by roundType.index, roundType.label, round.id");
				if (lRounds != null && lRounds.size() > 0) {
					for (Round rd : (List<Round>) lRounds) {
						List<String> l = new ArrayList<String>();
						l.add(String.valueOf(rd.getId()));
						l.add(String.valueOf(rd.getRoundType().getId()));
						l.add(rd.getRoundType().getLabel(lang));
						if (rd.getIdRank1() != null) {
							l.add(String.valueOf(rd.getIdRank1()));
							l.add(getEntityLabel(rd.getIdResultType(), rd.getIdRank1(), lang));
							l.add(StringUtils.notEmpty(rd.getResult1()) ? rd.getResult1() : "");
						}
						else {
							l.add("");
							l.add("");
							l.add("");
						}
						if (rd.getIdRank2() != null) {
							l.add(String.valueOf(rd.getIdRank2()));
							l.add(getEntityLabel(rd.getIdResultType(), rd.getIdRank2(), lang));
							l.add(StringUtils.notEmpty(rd.getResult2()) ? rd.getResult2() : "");
						}
						else {
							l.add("");
							l.add("");
							l.add("");
						}
						if (rd.getIdRank3() != null) {
							l.add(String.valueOf(rd.getIdRank3()));
							l.add(getEntityLabel(rd.getIdResultType(), rd.getIdRank3(), lang));
							l.add(StringUtils.notEmpty(rd.getResult3()) ? rd.getResult3() : "");
						}
						else {
							l.add("");
							l.add("");
							l.add("");
						}
						l.add(StringUtils.notEmpty(rd.getDate1()) ? rd.getDate1() : "");
						l.add(StringUtils.notEmpty(rd.getDate2()) ? rd.getDate2() : "");
						if (rd.getComplex1() != null) {
							l.add(String.valueOf(rd.getComplex1().getId()));
							l.add(rd.getComplex1().toString2(lang));
						}
						else if (rd.getCity1() != null) {
							l.add(String.valueOf(rd.getCity1().getId()));
							l.add(rd.getCity1().toString2(lang));
						}
						else {
							l.add("");
							l.add("");
						}
						if (rd.getComplex2() != null) {
							l.add(String.valueOf(rd.getComplex2().getId()));
							l.add(rd.getComplex2().toString2(lang));
						}
						else if (rd.getCity2() != null) {
							l.add(String.valueOf(rd.getCity2().getId()));
							l.add(rd.getCity2().toString2(lang));
						}
						else {
							l.add("");
							l.add("");
						}
						l.add(StringUtils.notEmpty(rd.getExa()) ? rd.getExa() : "");
						l.add(StringUtils.notEmpty(rd.getComment()) ? rd.getComment() : "");
						sb.append("rd-" + StringUtils.join(l, "|")).append("~");
					}
				}
				sb.append(path != null ? StringUtils.encode(path) : "").append("~"); // Link to results
				sb.append(StringUtils.encode("RS-" + rs.getId() + "-1")).append("~"); // Link to single result
				if (lResult != null && lResult.size() > 1) {
					StringBuffer sb_ = new StringBuffer();
					for (Result rs_ : lResult)
						if (rs != null && rs.getYear().getId().equals(rs_.getYear().getId()))
							sb_.append(sb_.length() > 0 ?  "," : "").append(rs_.getId());
					sb.append(sb_).append("~");
				}
				else
					sb.append("~");
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
	
	private static void loadEntity(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		HashMap<String, Short> hLocs = new HashMap<String, Short>();
		hLocs.put("first", DatabaseHelper.FIRST);
		hLocs.put("previous", DatabaseHelper.PREVIOUS);
		hLocs.put("next", DatabaseHelper.NEXT);
		hLocs.put("last", DatabaseHelper.LAST);
		String action = String.valueOf(hParams.get("action"));
		String id = String.valueOf(hParams.get("id"));
		String alias = String.valueOf(hParams.get("alias"));
		String filter = (alias.matches(Athlete.alias + "|" + Team.alias + "|" + Sport.alias) ? (cb != null && !cb.isAdmin() ? (alias.equalsIgnoreCase(Sport.alias) ? "" : "sport.") + "id in (" + cb.getSports() + ")" : null) : null);
		if (StringUtils.notEmpty(hParams.get("sp")))
			filter = (filter != null ? filter + " and " : "") + "sport.id=" + hParams.get("sp");
		Class c = DatabaseHelper.getClassFromAlias(alias);
		Object o = (action.equals("direct") ? DatabaseHelper.loadEntity(c, id) : DatabaseHelper.move(c, id, hLocs.get(action), filter));
		StringBuffer sb = new StringBuffer();
		if (o != null) {
			id = String.valueOf(c.getMethod("getId").invoke(o, new Object[0]));
			String exl = null;
			if (alias.matches(Athlete.alias + "|" + Championship.alias + "|" + City.alias + "|" + Complex.alias + "|" + Country.alias + "|" + Event.alias + "|" + Olympics.alias + "|" + Sport.alias + "|" + State.alias + "|" + Team.alias)) {
				StringBuffer sbexl = new StringBuffer();
				for (ExternalLink exl_ : (Collection<ExternalLink>) DatabaseHelper.execute("from ExternalLink where entity='" + alias + "' and idItem=" + id + " order by id"))
					sbexl.append(exl_.getUrl()).append("\r\n");
				exl = sbexl.toString();
			}
			sb.append(id).append("~");
			if (o instanceof Athlete) {
				Athlete at = (Athlete) o;
				sb.append(at.getLastName()).append("~");
				sb.append(StringUtils.notEmpty(at.getFirstName()) ? at.getFirstName() : "").append("~");
				sb.append(at.getSport() != null ? at.getSport().getId() : 0).append("~");
				sb.append(at.getSport() != null ? at.getSport().getLabel(lang) : "").append("~");
				sb.append(at.getTeam() != null ? at.getTeam().getId() : 0).append("~");
				sb.append(at.getTeam() != null ? at.getTeam().getLabel() + ", " + at.getTeam().getSport().getLabel(lang) : "").append("~");
				sb.append(at.getCountry() != null ? at.getCountry().getId() : 0).append("~");
				sb.append(at.getCountry() != null ? at.getCountry().getLabel(lang) : "").append("~");
				sb.append(StringUtils.notEmpty(at.getPhotoSource()) ? at.getPhotoSource() : "").append("~");
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
				else if (at.getLink() != null && at.getLink() == 0)
					sb.append("0~[root]~");
				else
					sb.append("~~");
				sb.append(exl).append("~");
				sb.append(ImageUtils.getPhotoFile(Athlete.alias, id)).append("~");
			}
			else if (o instanceof com.sporthenon.db.entity.Calendar) {
				com.sporthenon.db.entity.Calendar cl = (com.sporthenon.db.entity.Calendar) o;
				sb.append(cl.getSport() != null ? cl.getSport().getId() : 0).append("~");
				sb.append(cl.getSport() != null ? cl.getSport().getLabel(lang) : "").append("~");
				sb.append(cl.getChampionship() != null ? cl.getChampionship().getId() : 0).append("~");
				sb.append(cl.getChampionship() != null ? cl.getChampionship().getLabel(lang) : "").append("~");
				sb.append(cl.getEvent() != null ? cl.getEvent().getId() : 0).append("~");
				sb.append(cl.getEvent() != null ? cl.getEvent().getLabel(lang) : "").append("~");
				sb.append(cl.getSubevent() != null ? cl.getSubevent().getId() : 0).append("~");
				sb.append(cl.getSubevent() != null ? cl.getSubevent().getLabel(lang) : "").append("~");
				sb.append(cl.getSubevent2() != null ? cl.getSubevent2().getId() : 0).append("~");
				sb.append(cl.getSubevent2() != null ? cl.getSubevent2().getLabel(lang) : "").append("~");
				sb.append(cl.getComplex() != null ? cl.getComplex().getId() : 0).append("~");
				sb.append(cl.getComplex() != null ? cl.getComplex().getLabel() : "").append("~");
				sb.append(cl.getCity() != null ? cl.getCity().getId() : 0).append("~");
				sb.append(cl.getCity() != null ? cl.getCity().getLabel(lang) : "").append("~");
				sb.append(cl.getCountry() != null ? cl.getCountry().getId() : 0).append("~");
				sb.append(cl.getCountry() != null ? cl.getCountry().getLabel(lang) : "").append("~");
				sb.append(cl.getDate1() != null ? cl.getDate1() : "").append("~");
				sb.append(cl.getDate2() != null ? cl.getDate2() : "").append("~");
			}	
			else if (o instanceof Championship) {
				Championship cp = (Championship) o;
				sb.append(cp.getLabel()).append("~");
				sb.append(cp.getLabelFr()).append("~");
				sb.append(cp.getIndex() != null ? cp.getIndex() : "").append("~");
				sb.append(exl).append("~");
				sb.append(cp.getNopic() != null && cp.getNopic() ? "1" : "0");
			}
			else if (o instanceof City) {
				City ct = (City) o;
				sb.append(ct.getLabel()).append("~");
				sb.append(ct.getLabelFr()).append("~");
				sb.append(ct.getState() != null ? ct.getState().getId() : 0).append("~");
				sb.append(ct.getState() != null ? ct.getState().getLabel(lang) : "").append("~");
				sb.append(ct.getCountry() != null ? ct.getCountry().getId() : 0).append("~");
				sb.append(ct.getCountry() != null ? ct.getCountry().getLabel(lang) : "").append("~");
				sb.append(StringUtils.notEmpty(ct.getPhotoSource()) ? ct.getPhotoSource() : "").append("~");
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
				else if (ct.getLink() != null && ct.getLink() == 0)
					sb.append("0~[root]~");
				else
					sb.append("~~");
				sb.append(exl).append("~");
				sb.append(ImageUtils.getPhotoFile(City.alias, id)).append("~");
			}
			else if (o instanceof Complex) {
				Complex cx = (Complex) o;
				sb.append(cx.getLabel()).append("~");
				sb.append(cx.getCity() != null ? cx.getCity().getId() : 0).append("~");
				sb.append(cx.getCity() != null ? cx.getCity().toString2(lang) : "").append("~");
				sb.append(StringUtils.notEmpty(cx.getPhotoSource()) ? cx.getPhotoSource() : "").append("~");
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
				else if (cx.getLink() != null && cx.getLink() == 0)
					sb.append("0~[root]~");
				else
					sb.append("~~");
				sb.append(exl).append("~");
				sb.append(ImageUtils.getPhotoFile(Complex.alias, id)).append("~");
			}
			else if (o instanceof Contributor) {
				Contributor cb_ = (Contributor) o;
				sb.append(cb_.getLogin()).append("~");
				sb.append(cb_.getPublicName()).append("~");
				sb.append(cb_.getEmail()).append("~");
				sb.append(cb_.isActive() ? "1" : "0").append("~");
				sb.append(cb_.isAdmin() ? "1" : "0").append("~");
				sb.append(cb_.getSports()).append("~");
			}
			else if (o instanceof Country) {
				Country cn = (Country) o;
				sb.append(cn.getLabel()).append("~");
				sb.append(cn.getLabelFr()).append("~");
				sb.append(cn.getCode()).append("~");
				sb.append(exl).append("~");
				sb.append(cn.getNopic() != null && cn.getNopic() ? "1" : "0");
			}
			else if (o instanceof Event) {
				Event ev = (Event) o;
				sb.append(ev.getLabel()).append("~");
				sb.append(ev.getLabelFr()).append("~");
				sb.append(ev.getType() != null ? ev.getType().getId() : 0).append("~");
				sb.append(ev.getType() != null ? ev.getType().getLabel(lang) : "").append("~");
				sb.append(ev.getIndex() != null ? ev.getIndex() : "").append("~");
				sb.append(exl).append("~");
				sb.append(ev.getNopic() != null && ev.getNopic() ? "1" : "0");
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
				sb.append(exl).append("~");
				sb.append(ol.getNopic() != null && ol.getNopic() ? "1" : "0");
			}
			else if (o instanceof OlympicRanking) {
				OlympicRanking or = (OlympicRanking) o;
				sb.append(or.getOlympics() != null ? or.getOlympics().getId() : 0).append("~");
				sb.append(or.getOlympics() != null ? or.getOlympics().toString2(lang) : "").append("~");
				sb.append(or.getCountry() != null ? or.getCountry().getId() : 0).append("~");
				sb.append(or.getCountry() != null ? or.getCountry().getLabel(lang) : "").append("~");
				sb.append(or.getCountGold()).append("~");
				sb.append(or.getCountSilver()).append("~");
				sb.append(or.getCountBronze()).append("~");
			}
			else if (o instanceof RoundType) {
				RoundType rt = (RoundType) o;
				sb.append(rt.getLabel()).append("~");
				sb.append(rt.getLabelFr()).append("~");
				sb.append(rt.getIndex() != null ? rt.getIndex() : "").append("~");
			}
			else if (o instanceof Sport) {
				Sport sp = (Sport) o;
				sb.append(sp.getLabel()).append("~");
				sb.append(sp.getLabelFr()).append("~");
				sb.append(sp.getType() != null ? sp.getType() : "").append("~");
				sb.append(sp.getIndex() != null ? sp.getIndex() : "").append("~");
				sb.append(exl).append("~");
				sb.append(sp.getNopic() != null && sp.getNopic() ? "1" : "0");
			}
			else if (o instanceof State) {
				State st = (State) o;
				sb.append(st.getLabel()).append("~");
				sb.append(st.getLabelFr()).append("~");
				sb.append(st.getCode()).append("~");
				sb.append(st.getCapital()).append("~");
				sb.append(exl).append("~");
				sb.append(st.getNopic() != null && st.getNopic() ? "1" : "0");
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
				else if (tm.getLink() != null && tm.getLink() == 0)
					sb.append("0~[root]~");
				else
					sb.append("~~");
				sb.append(exl).append("~");
				sb.append(tm.getNopic() != null && tm.getNopic() ? "1" : "0");
			}
			else if (o instanceof Year) {
				Year yr = (Year) o;
				sb.append(yr.getLabel()).append("~");
			}
			else if (o instanceof HallOfFame) {
				HallOfFame hf = (HallOfFame) o;
				sb.append(hf.getLeague() != null ? hf.getLeague().getId() : 0).append("~");
				sb.append(hf.getLeague() != null ? hf.getLeague().getLabel() : "").append("~");
				sb.append(hf.getYear() != null ? hf.getYear().getId() : 0).append("~");
				sb.append(hf.getYear() != null ? hf.getYear().getLabel() : "").append("~");
				sb.append(hf.getPerson() != null ? hf.getPerson().getId() : 0).append("~");
				sb.append(hf.getPerson() != null ? hf.getPerson().toString2() : "").append("~");
				sb.append(StringUtils.notEmpty(hf.getPosition()) ? hf.getPosition() : "").append("~");
			}
			else if (o instanceof Record) {
				Record rc = (Record) o;
				Integer n = rc.getEvent().getType().getNumber();
				if (StringUtils.notEmpty(rc.getType1()))
					n = (rc.getType1().equalsIgnoreCase("individual") ? 1 : 50);
				else if (rc.getSubevent() != null)
					n = rc.getSubevent().getType().getNumber();
				sb.append(rc.getSport() != null ? rc.getSport().getId() : 0).append("~");
				sb.append(rc.getSport() != null ? rc.getSport().getLabel(lang) : "").append("~");
				sb.append(rc.getChampionship() != null ? rc.getChampionship().getId() : 0).append("~");
				sb.append(rc.getChampionship() != null ? rc.getChampionship().getLabel(lang) : "").append("~");
				sb.append(rc.getEvent() != null ? rc.getEvent().getId() : 0).append("~");
				sb.append(rc.getEvent() != null ? rc.getEvent().getLabel(lang) : "").append("~");
				sb.append(rc.getSubevent() != null ? rc.getSubevent().getId() : 0).append("~");
				sb.append(rc.getSubevent() != null ? rc.getSubevent().getLabel(lang) : "").append("~");
				sb.append(rc.getType1() != null ? rc.getType1() : "").append("~");
				sb.append(rc.getType2() != null ? rc.getType2() : "").append("~");
				sb.append(rc.getCity() != null ? rc.getCity().getId() : 0).append("~");
				sb.append(rc.getCity() != null ? rc.getCity().toString2(lang) : "").append("~");
				sb.append(rc.getLabel() != null ? rc.getLabel() : "").append("~");
				sb.append(rc.getIdRank1() != null ? rc.getIdRank1() :"").append("~");
				sb.append(rc.getIdRank1() != null ? getEntityLabel(n, rc.getIdRank1(), lang) : "").append("~");
				sb.append(StringUtils.notEmpty(rc.getRecord1()) ? rc.getRecord1() : "").append("~");
				sb.append(StringUtils.notEmpty(rc.getDate1()) ? rc.getDate1() : "").append("~");
				sb.append(rc.getIdRank2() != null ? rc.getIdRank2() : "").append("~");
				sb.append(rc.getIdRank2() != null ? getEntityLabel(n, rc.getIdRank2(), lang) : "").append("~");
				sb.append(StringUtils.notEmpty(rc.getRecord2()) ? rc.getRecord2() : "").append("~");
				sb.append(StringUtils.notEmpty(rc.getDate2()) ? rc.getDate2() : "").append("~");
				sb.append(rc.getIdRank3() != null ? rc.getIdRank3() : "").append("~");
				sb.append(rc.getIdRank3() != null ? getEntityLabel(n, rc.getIdRank3(), lang) : "").append("~");
				sb.append(StringUtils.notEmpty(rc.getRecord3()) ? rc.getRecord3() : "").append("~");
				sb.append(StringUtils.notEmpty(rc.getDate3()) ? rc.getDate3() : "").append("~");
				sb.append(rc.getIdRank4() != null ? rc.getIdRank4() : "").append("~");
				sb.append(rc.getIdRank4() != null ? getEntityLabel(n, rc.getIdRank4(), lang) : "").append("~");
				sb.append(StringUtils.notEmpty(rc.getRecord4()) ? rc.getRecord4() : "").append("~");
				sb.append(StringUtils.notEmpty(rc.getDate4()) ? rc.getDate4() : "").append("~");
				sb.append(rc.getIdRank5() != null ? rc.getIdRank5() : "").append("~");
				sb.append(rc.getIdRank5() != null ? getEntityLabel(n, rc.getIdRank5(), lang) : "").append("~");
				sb.append(StringUtils.notEmpty(rc.getRecord5()) ? rc.getRecord5() : "").append("~");
				sb.append(StringUtils.notEmpty(rc.getDate5()) ? rc.getDate5() : "").append("~");
				sb.append(rc.getCounting() != null ? rc.getCounting() : "").append("~");
				sb.append(rc.getIndex() != null ? rc.getIndex() : "").append("~");
				sb.append(StringUtils.notEmpty(rc.getExa()) ? rc.getExa() : "").append("~");
				sb.append(StringUtils.notEmpty(rc.getComment()) ? rc.getComment() : "").append("~");
			}
			else if (o instanceof RetiredNumber) {
				RetiredNumber rn = (RetiredNumber) o;
				sb.append(rn.getLeague() != null ? rn.getLeague().getId() : 0).append("~");
				sb.append(rn.getLeague() != null ? rn.getLeague().getLabel() : "").append("~");
				sb.append(rn.getTeam() != null ? rn.getTeam().getId() : 0).append("~");
				sb.append(rn.getTeam() != null ? rn.getTeam().getLabel() : "").append("~");
				sb.append(rn.getPerson() != null ? rn.getPerson().getId() : 0).append("~");
				sb.append(rn.getPerson() != null ? rn.getPerson().toString2() : "").append("~");
				sb.append(rn.getYear() != null ? rn.getYear().getId() : 0).append("~");
				sb.append(rn.getYear() != null ? rn.getYear().getLabel() : "").append("~");
				sb.append(StringUtils.notEmpty(rn.getNumber()) ? rn.getNumber() : "").append("~");
			}
			else if (o instanceof TeamStadium) {
				TeamStadium ts = (TeamStadium) o;
				sb.append(ts.getLeague() != null ? ts.getLeague().getId() : 0).append("~");
				sb.append(ts.getLeague() != null ? ts.getLeague().getLabel() : "").append("~");
				sb.append(ts.getTeam() != null ? ts.getTeam().getId() : 0).append("~");
				sb.append(ts.getTeam() != null ? ts.getTeam().getLabel() : "").append("~");
				sb.append(ts.getComplex() != null ? ts.getComplex().getId() : 0).append("~");
				sb.append(ts.getComplex() != null ? ts.getComplex().toString2(lang) : "").append("~");
				sb.append(StringUtils.notEmpty(ts.getDate1()) ? ts.getDate1() : "").append("~");
				sb.append(StringUtils.notEmpty(ts.getDate2()) ? ts.getDate2() : "").append("~");
				sb.append(ts.getRenamed() != null && ts.getRenamed() ? "1" : "0").append("~");
			}
			else if (o instanceof WinLoss) {
				WinLoss wl = (WinLoss) o;
				sb.append(wl.getLeague() != null ? wl.getLeague().getId() : 0).append("~");
				sb.append(wl.getLeague() != null ? wl.getLeague().getLabel() : "").append("~");
				sb.append(wl.getTeam() != null ? wl.getTeam().getId() : 0).append("~");
				sb.append(wl.getTeam() != null ? wl.getTeam().getLabel() : "").append("~");
				sb.append(StringUtils.notEmpty(wl.getType()) ? wl.getType() : "").append("~");
				sb.append(StringUtils.notEmpty(wl.getCountWin()) ? wl.getCountWin() : "").append("~");
				sb.append(StringUtils.notEmpty(wl.getCountLoss()) ? wl.getCountLoss() : "").append("~");
				sb.append(StringUtils.notEmpty(wl.getCountTie()) ? wl.getCountTie() : "").append("~");
				sb.append(StringUtils.notEmpty(wl.getCountLoss()) ? wl.getCountLoss() : "").append("~");
			}
		}
		ServletHelper.writeText(response, sb.toString());
	}
	
	private static void saveEntity(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
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
				en.setLastName(String.valueOf(hParams.get("pr-lastname")).trim());
				en.setFirstName(String.valueOf(hParams.get("pr-firstname")).trim());
				en.setLink(StringUtils.notEmpty(hParams.get("pr-link")) ? new Integer(String.valueOf(hParams.get("pr-link"))) : null);
				en.setPhotoSource(String.valueOf(hParams.get("pr-source")));
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
			else if (alias.equalsIgnoreCase(com.sporthenon.db.entity.Calendar.alias)) {
				com.sporthenon.db.entity.Calendar en = (com.sporthenon.db.entity.Calendar) o;
				en.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, StringUtils.toInt(hParams.get("cl-sport"))));
				en.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, StringUtils.toInt(hParams.get("cl-championship"))));
				en.setEvent((Event)DatabaseHelper.loadEntity(Event.class, StringUtils.toInt(hParams.get("cl-event"))));
				en.setSubevent((Event)DatabaseHelper.loadEntity(Event.class, StringUtils.toInt(hParams.get("cl-subevent"))));
				en.setSubevent2((Event)DatabaseHelper.loadEntity(Event.class, StringUtils.toInt(hParams.get("cl-subevent2"))));
				en.setComplex((Complex)DatabaseHelper.loadEntity(Complex.class, StringUtils.toInt(hParams.get("cl-complex"))));
				en.setCity((City)DatabaseHelper.loadEntity(City.class, StringUtils.toInt(hParams.get("cl-city"))));
				en.setCountry((Country)DatabaseHelper.loadEntity(Country.class, StringUtils.toInt(hParams.get("cl-country"))));
				en.setDate1(StringUtils.notEmpty(hParams.get("cl-date1")) ? String.valueOf(hParams.get("cl-date1")) : null);
				en.setDate2(StringUtils.notEmpty(hParams.get("cl-date2")) ? String.valueOf(hParams.get("cl-date2")) : null);
			}
			else if (alias.equalsIgnoreCase(Championship.alias)) {
				Championship en = (Championship) o;
				en.setLabel(String.valueOf(hParams.get("cp-label")));
				en.setLabelFr(String.valueOf(hParams.get("cp-labelfr")));
				en.setIndex(StringUtils.notEmpty(hParams.get("cp-index")) ? Float.valueOf(String.valueOf(hParams.get("cp-index"))) : Float.MAX_VALUE);
			}
			else if (alias.equalsIgnoreCase(City.alias)) {
				City en = (City) o;
				en.setLabel(String.valueOf(hParams.get("ct-label")));
				en.setLabelFr(String.valueOf(hParams.get("ct-labelfr")));
				en.setState((State)DatabaseHelper.loadEntity(State.class, StringUtils.toInt(hParams.get("ct-state"))));
				en.setCountry((Country)DatabaseHelper.loadEntity(Country.class, StringUtils.toInt(hParams.get("ct-country"))));
				en.setPhotoSource(String.valueOf(hParams.get("ct-source")));
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
				en.setCity((City)DatabaseHelper.loadEntity(City.class, StringUtils.toInt(hParams.get("cx-city"))));
				en.setPhotoSource(String.valueOf(hParams.get("cx-source")));
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
				en.setLabel(String.valueOf(hParams.get("cn-label")));
				en.setLabelFr(String.valueOf(hParams.get("cn-labelfr")));
				en.setCode(String.valueOf(hParams.get("cn-code")));
			}
			else if (alias.equalsIgnoreCase(Event.alias)) {
				Event en = (Event) o;
				en.setLabel(String.valueOf(hParams.get("ev-label")));
				en.setLabelFr(String.valueOf(hParams.get("ev-labelfr")));
				en.setType((Type)DatabaseHelper.loadEntity(Type.class, StringUtils.toInt(hParams.get("ev-type"))));
				en.setIndex(StringUtils.notEmpty(hParams.get("ev-index")) ? Float.valueOf(String.valueOf(hParams.get("ev-index"))) : Float.MAX_VALUE);
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
			else if (alias.equalsIgnoreCase(OlympicRanking.alias)) {
				OlympicRanking en = (OlympicRanking) o;
				en.setOlympics((Olympics)DatabaseHelper.loadEntity(Olympics.class, StringUtils.toInt(hParams.get("or-olympics"))));
				en.setCountry((Country)DatabaseHelper.loadEntity(Country.class, StringUtils.toInt(hParams.get("or-country"))));
				en.setCountGold(StringUtils.notEmpty(hParams.get("or-gold")) ? StringUtils.toInt(hParams.get("or-gold")) : 0);
				en.setCountSilver(StringUtils.notEmpty(hParams.get("or-silver")) ? StringUtils.toInt(hParams.get("or-silver")) : 0);
				en.setCountBronze(StringUtils.notEmpty(hParams.get("or-bronze")) ? StringUtils.toInt(hParams.get("or-bronze")) : 0);
			}
			else if (alias.equalsIgnoreCase(RoundType.alias)) {
				RoundType en = (RoundType) o;
				en.setLabel(String.valueOf(hParams.get("rt-label")));
				en.setLabelFr(String.valueOf(hParams.get("rt-labelfr")));
				en.setIndex(StringUtils.notEmpty(hParams.get("rt-index")) ? Float.valueOf(String.valueOf(hParams.get("rt-index"))) : Float.MAX_VALUE);
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
				en.setLabel(String.valueOf(hParams.get("tm-label")).trim());
				en.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, StringUtils.toInt(hParams.get("tm-sport"))));
				en.setCountry((Country)DatabaseHelper.loadEntity(Country.class, StringUtils.toInt(hParams.get("tm-country"))));
				en.setLeague((League)DatabaseHelper.loadEntity(League.class, StringUtils.toInt(hParams.get("tm-league"))));
				en.setConference(String.valueOf(hParams.get("tm-conference")));
				en.setDivision(String.valueOf(hParams.get("tm-division")));
				en.setComment(String.valueOf(hParams.get("tm-comment")));
				en.setYear1(String.valueOf(hParams.get("tm-year1")));
				en.setYear2(String.valueOf(hParams.get("tm-year2")));
				en.setLink(StringUtils.notEmpty(hParams.get("tm-link")) ? new Integer(String.valueOf(hParams.get("tm-link"))) : null);
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
			else if (alias.equalsIgnoreCase(HallOfFame.alias)) {
				HallOfFame en = (HallOfFame) o;
				en.setLeague((League)DatabaseHelper.loadEntity(League.class, StringUtils.toInt(hParams.get("hf-league"))));
				en.setYear((Year)DatabaseHelper.loadEntity(Year.class, StringUtils.toInt(hParams.get("hf-year"))));
				en.setPerson((Athlete)DatabaseHelper.loadEntity(Athlete.class, StringUtils.toInt(hParams.get("hf-person"))));
				en.setPosition(StringUtils.notEmpty(hParams.get("hf-position")) ? String.valueOf(hParams.get("hf-position")) : null);
			}
			else if (alias.equalsIgnoreCase(Record.alias)) {
				Record en = (Record) o;
				en.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, StringUtils.toInt(hParams.get("rc-sport"))));
				en.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, StringUtils.toInt(hParams.get("rc-championship"))));
				en.setEvent((Event)DatabaseHelper.loadEntity(Event.class, StringUtils.toInt(hParams.get("rc-event"))));
				en.setSubevent((Event)DatabaseHelper.loadEntity(Event.class, StringUtils.toInt(hParams.get("rc-subevent"))));
				en.setType1(StringUtils.notEmpty(hParams.get("rc-type1")) ? String.valueOf(hParams.get("rc-type1")) : null);
				en.setType2(StringUtils.notEmpty(hParams.get("rc-type2")) ? String.valueOf(hParams.get("rc-type2")) : null);
				en.setCity((City)DatabaseHelper.loadEntity(City.class, StringUtils.toInt(hParams.get("rc-city"))));
				en.setLabel(StringUtils.notEmpty(hParams.get("rc-label")) ? String.valueOf(hParams.get("rc-label")) : null);
				en.setIdRank1(StringUtils.notEmpty(hParams.get("rc-rank1")) ? new Integer(String.valueOf(hParams.get("rc-rank1"))) : null);
				en.setRecord1(StringUtils.notEmpty(hParams.get("rc-record1")) ? String.valueOf(hParams.get("rc-record1")) : null);
				en.setDate1(StringUtils.notEmpty(hParams.get("rc-date1")) ? String.valueOf(hParams.get("rc-date1")) : null);
				en.setIdRank2(StringUtils.notEmpty(hParams.get("rc-rank2")) ? new Integer(String.valueOf(hParams.get("rc-rank2"))) : null);
				en.setRecord2(StringUtils.notEmpty(hParams.get("rc-record2")) ? String.valueOf(hParams.get("rc-record2")) : null);
				en.setDate2(StringUtils.notEmpty(hParams.get("rc-date2")) ? String.valueOf(hParams.get("rc-date2")) : null);
				en.setIdRank3(StringUtils.notEmpty(hParams.get("rc-rank3")) ? new Integer(String.valueOf(hParams.get("rc-rank3"))) : null);
				en.setRecord3(StringUtils.notEmpty(hParams.get("rc-record3")) ? String.valueOf(hParams.get("rc-record3")) : null);
				en.setDate3(StringUtils.notEmpty(hParams.get("rc-date3")) ? String.valueOf(hParams.get("rc-date3")) : null);
				en.setIdRank4(StringUtils.notEmpty(hParams.get("rc-rank4")) ? new Integer(String.valueOf(hParams.get("rc-rank4"))) : null);
				en.setRecord4(StringUtils.notEmpty(hParams.get("rc-record4")) ? String.valueOf(hParams.get("rc-record4")) : null);
				en.setDate4(StringUtils.notEmpty(hParams.get("rc-date4")) ? String.valueOf(hParams.get("rc-date4")) : null);
				en.setIdRank5(StringUtils.notEmpty(hParams.get("rc-rank5")) ? new Integer(String.valueOf(hParams.get("rc-rank5"))) : null);
				en.setRecord5(StringUtils.notEmpty(hParams.get("rc-record5")) ? String.valueOf(hParams.get("rc-record5")) : null);
				en.setDate5(StringUtils.notEmpty(hParams.get("rc-date5")) ? String.valueOf(hParams.get("rc-date5")) : null);
				en.setCounting(StringUtils.notEmpty(hParams.get("rc-counting")) ? String.valueOf(hParams.get("rc-counting")).equals("1") : null);
				en.setIndex(StringUtils.notEmpty(hParams.get("rc-index")) ? new Float(String.valueOf(hParams.get("rc-index"))) : null);
				en.setExa(StringUtils.notEmpty(hParams.get("rc-tie")) ? String.valueOf(hParams.get("rc-tie")) : null);
				en.setComment(StringUtils.notEmpty(hParams.get("rc-comment")) ? String.valueOf(hParams.get("rc-comment")) : null);
			}
			else if (alias.equalsIgnoreCase(RetiredNumber.alias)) {
				RetiredNumber en = (RetiredNumber) o;
				en.setLeague((League)DatabaseHelper.loadEntity(League.class, StringUtils.toInt(hParams.get("rn-league"))));
				en.setTeam((Team)DatabaseHelper.loadEntity(Team.class, StringUtils.toInt(hParams.get("rn-team"))));
				en.setPerson((Athlete)DatabaseHelper.loadEntity(Athlete.class, StringUtils.toInt(hParams.get("rn-person"))));
				en.setYear((Year)DatabaseHelper.loadEntity(Year.class, StringUtils.toInt(hParams.get("rn-year"))));
				en.setNumber(StringUtils.notEmpty(hParams.get("rn-number")) ? new Integer(String.valueOf(hParams.get("rn-number"))) : null);
			}
			else if (alias.equalsIgnoreCase(TeamStadium.alias)) {
				TeamStadium en = (TeamStadium) o;
				en.setLeague((League)DatabaseHelper.loadEntity(League.class, StringUtils.toInt(hParams.get("ts-league"))));
				en.setTeam((Team)DatabaseHelper.loadEntity(Team.class, StringUtils.toInt(hParams.get("ts-team"))));
				en.setComplex((Complex)DatabaseHelper.loadEntity(Complex.class, StringUtils.toInt(hParams.get("ts-complex"))));
				en.setDate1(StringUtils.notEmpty(hParams.get("ts-date1")) ? new Integer(String.valueOf(hParams.get("ts-date1"))) : null);
				en.setDate2(StringUtils.notEmpty(hParams.get("ts-date2")) ? new Integer(String.valueOf(hParams.get("ts-date2"))) : null);
				en.setRenamed(StringUtils.notEmpty(hParams.get("ts-renamed")) ? String.valueOf(hParams.get("ts-renamed")).equals("1") : null);
			}
			else if (alias.equalsIgnoreCase(WinLoss.alias)) {
				WinLoss en = (WinLoss) o;
				en.setLeague((League)DatabaseHelper.loadEntity(League.class, StringUtils.toInt(hParams.get("wl-league"))));
				en.setTeam((Team)DatabaseHelper.loadEntity(Team.class, StringUtils.toInt(hParams.get("wl-team"))));
				en.setType(StringUtils.notEmpty(hParams.get("wl-type")) ? String.valueOf(hParams.get("wl-type")) : null);
				en.setCountWin(StringUtils.notEmpty(hParams.get("wl-win")) ? new Integer(String.valueOf(hParams.get("wl-win"))) : null);
				en.setCountLoss(StringUtils.notEmpty(hParams.get("wl-loss")) ? new Integer(String.valueOf(hParams.get("wl-loss"))) : null);
				en.setCountTie(StringUtils.notEmpty(hParams.get("wl-tie")) ? new Integer(String.valueOf(hParams.get("wl-tie"))) : null);
				en.setCountOtloss(StringUtils.notEmpty(hParams.get("wl-otloss")) ? new Integer(String.valueOf(hParams.get("wl-otloss"))) : null);
			}
			o = DatabaseHelper.saveEntity(o, cb);
			String id_ = String.valueOf(c.getMethod("getId").invoke(o, new Object[0]));
			if (alias.matches(Athlete.alias + "|" + Championship.alias + "|" + City.alias + "|" + Complex.alias + "|" + Country.alias + "|" + Event.alias + "|" + Olympics.alias + "|" + Sport.alias + "|" + State.alias + "|" + Team.alias))
				DatabaseHelper.saveExternalLinks(alias, Integer.parseInt(id_), String.valueOf(hParams.get("exl")));
			msg = ResourceUtils.getText("update.ok", lang) + "&nbsp;" + StringUtils.SEP1 + "&nbsp;" + ResourceUtils.getText("entity." + alias + ".1", lang) + " #" + id_;
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
			msg = "ERR:" + e.getMessage();
		}
		finally {
			ServletHelper.writeText(response, msg);
		}
	}
	
	private static void deleteEntity(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			String id = String.valueOf(hParams.get("id"));
			String alias = String.valueOf(hParams.get("alias"));
			Class c = DatabaseHelper.getClassFromAlias(alias);
			DatabaseHelper.removeEntity(DatabaseHelper.loadEntity(c, StringUtils.toInt(id)));
			sbMsg.append(ResourceUtils.getText("delete.ok", lang) + "&nbsp;" + StringUtils.SEP1 + "&nbsp;" + ResourceUtils.getText("entity." + alias + ".1", lang) + " #" + id);
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void executeImport(HttpServletRequest request, HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		Object file = hParams.get("file");
		if (StringUtils.notEmpty(file)) {
			FileInputStream fis = new FileInputStream(ConfigUtils.getProperty("temp.folder") + file);
			byte[] b = IOUtils.toByteArray(fis);
			response.setHeader("Content-Disposition", "attachment;filename=" + file);
			response.setContentType("text/html");
			response.getWriter().write(new String(b));
			response.flushBuffer();
			fis.close();
		}
		else {
			IMPORT_PROGESS = new ImportUtils.Progress();
			InputStream input = null;
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			Collection<FileItem> items = upload.parseRequest(request);
			for (FileItem fitem : items)
				input = fitem.getInputStream();
			Vector v = new Vector<Vector<String>>();
			BufferedReader bf = new BufferedReader(new InputStreamReader(input, "UTF8"));
			String s = null;
			while ((s = bf.readLine()) != null) {
				if (StringUtils.notEmpty(s)) {
					Vector v_ = new Vector<String>();
					for (String s_ : s.split(";", -1))
						v_.add(s_.trim());
					v.add(v_);
				}
			}
			String type = String.valueOf(hParams.get("type"));
			String update = String.valueOf(hParams.get("update"));
			String result = ImportUtils.processAll(IMPORT_PROGESS,  v, update.equals("1"), type.equalsIgnoreCase(Result.alias), type.equalsIgnoreCase(Round.alias), type.equalsIgnoreCase(Record.alias), cb, lang);
			IMPORT_PROGESS.value = 100;
			if (update.equals("1")) {
				String f = "import" + System.currentTimeMillis() + ".html";
				String css = "<style>table{border-collapse: collapse;}th,td{white-space: nowrap;border: 1px solid gray;padding: 2px;}div{margin-top: 10px;font-style: italic;}</style>";
				FileOutputStream fos = new FileOutputStream(ConfigUtils.getProperty("temp.folder") + f);
				fos.write((css + result).getBytes());
				fos.close();
				result = f;
			}
			ServletHelper.writeText(response, result);
		}
	}
	
	private static void loadTemplate(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		try {
			String type = String.valueOf(hParams.get("type"));
			String ext = String.valueOf(hParams.get("ext"));
			List<ArrayList<String>> list = ImportUtils.getTemplate(type, lang);
			List lTh = new ArrayList<ArrayList<String>>();
			List lTd = new ArrayList<ArrayList<String>>();
			StringBuffer sb = new StringBuffer();
			for (ArrayList<String> list_ : list) {
				int i = 0;
				ArrayList<String> lTh_ = new ArrayList<String>();
				ArrayList<String> lTd_ = new ArrayList<String>();
				for (String s : list_) {
					lTh_.add(s);
					lTd_.add(s);
					sb.append(i++ > 0 ? ";" : "").append(s.replaceAll("^\\#.*\\#", ""));
				}
				if (lTh.isEmpty()) {
					lTd_ = new ArrayList<String>();
					lTd_.add("--NEW--");
					lTh.add(lTh_);
				}
				lTd.add(lTd_);
				sb.append("\r\n");
			}
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + ResourceUtils.getText("entity." + type, ResourceUtils.LGDEFAULT) + "." + ext);
			if (ext.equalsIgnoreCase("xls")) {
				response.setContentType("application/vnd.ms-excel");
				ExportUtils.buildXLS(response.getOutputStream(), null, lTh, lTd, null, new boolean[]{false});
			}
			else {
				response.setContentType("text/plain");
				response.getWriter().write(sb.toString());
				response.flushBuffer();
			}
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
	}
	
	private static void loadExternalLinks(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		try {
			StringBuffer html = new StringBuffer("<table>");
			String sport = String.valueOf(hParams.get("sport"));
			String range = String.valueOf(hParams.get("range"));
			String pattern = String.valueOf(hParams.get("pattern"));
			String entity = String.valueOf(hParams.get("entity"));
			String includechecked = String.valueOf(hParams.get("includechecked"));
			
			String[] tIds = range.split("\\-");
			StringBuffer hql = new StringBuffer("from ExternalLink where entity='" + entity + "'");
			if (tIds.length > 1)
				hql.append(" and idItem between " + tIds[0] + " and " + tIds[1]);
			hql.append(" order by idItem, type");

			StringBuffer where = new StringBuffer(" where 1=1");
			where.append(tIds.length > 1 ? " and id between " + tIds[0] + " and " + tIds[1] : "");
			where.append(entity.matches(Athlete.alias + "|" + Team.alias + "|" + Sport.alias) ? (cb != null && !cb.isAdmin() ? " and " + (entity.equalsIgnoreCase(Sport.alias) ? "" : "sport.") + "id in (" + cb.getSports() + ")" : "") : "");
			String label_ = (entity.equals(Athlete.alias) ? "lastName || ', ' || firstName || ' - ' || sport.label" : (entity.equals(Team.alias) ? "label || ' - ' || sport.label" : (entity.equals(Olympics.alias) ? "city.label || ' ' || year.label" : "label")));
			if (entity.equalsIgnoreCase(City.alias))
				label_ = "label || '<i> - ' || country.code || '</i>', labelFR";
			else if (entity.equalsIgnoreCase(Complex.alias))
				label_ = "label || '<i> - ' || city.label || ', ' || city.country.code || '</i>'";
			if (StringUtils.notEmpty(pattern))
				where.append(" and lower(" + label_ + ") like '%" + pattern.toLowerCase() + "%'");
			if (entity.matches(Athlete.alias + "|" + Team.alias) && !sport.equals("0"))
				where.append(" and sport.id=" + sport);
			List<Object[]> items = DatabaseHelper.execute("select id, " + label_ + " from " + DatabaseHelper.getClassFromAlias(entity).getName() + where.toString() + " order by id");
			List<ExternalLink> links = DatabaseHelper.execute(hql.toString());
			html.append("<thead><th>ID</th><th>" + ResourceUtils.getText("label", lang) + "</th><th>" + ResourceUtils.getText("type", lang) + "</th><th>URL</th><th>" + ResourceUtils.getText("checked", lang) + "&nbsp;<input type='checkbox' onclick='checkAllLinks();'/></th></thead><tbody>");
			for (Object[] t : items) {
				Integer id = StringUtils.toInt(t[0]);
				String label = String.valueOf(t[1]);
				boolean isLink = false;
				boolean isExclude = false;
				for (ExternalLink el : links) {
					if (el.getIdItem().equals(id)) {
						if (includechecked.equals("0") && el.isChecked()) {
							isExclude = true;
							continue;
						}
						isLink = true;
						html.append("<tr id='el-" + el.getId() + "'><td style='display:none;'>" + el.getId() + "</td>");
						html.append("<td>" + el.getIdItem() + "</td>");
						html.append("<td>" + label + "</td>");
						html.append("<td><a href='" + el.getUrl() + "' target='_blank'>" + el.getUrl() + "</a></td>");
						html.append("<td><input type='checkbox'" + (el.isChecked() ? " checked='checked'" : "") + "/></td>");
						html.append("<td><a href='javascript:addExtLink(" + el.getId() + ");'><img alt='' src='/img/component/button/add.png'/></a></td>");
						html.append("<td><a href='javascript:modifyExtLink(" + el.getId() + ");'><img alt='' src='/img/component/button/modify.png'/></a></td></tr>");
					}
					else if (isLink)
						break;
				}
				if (!isLink && !isExclude) {
					html.append("<tr><td style='display:none;'>0</td>");
					html.append("<td>" + id + "</td>");
					html.append("<td>" + label + "</td>");
					html.append("<td><input type='text' style='width:60px;'/></td>");
					html.append("<td><input type='text' style='width:500px;'/></td>");
					html.append("<td><input type='checkbox'/></td><td/></tr>");
				}
			}
			ServletHelper.writeText(response, html.append("</tbody></table>").toString());
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
	}
	
	private static void saveExternalLinks(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			String entity = String.valueOf(hParams.get("entity"));
			String value = String.valueOf(hParams.get("value"));
			for (String s : value.split("\\|")) {
				String[] t = s.split("\\~");
				String id = t[0];
				ExternalLink el = (id.equals("0") ? new ExternalLink() : (ExternalLink) DatabaseHelper.loadEntity(ExternalLink.class, id));
				if (t.length > 2) {
					el.setEntity(entity);
					el.setIdItem(Integer.parseInt(t[1]));
					el.setUrl(t[2]);
					el.setChecked(t[3].equals("1"));
					DatabaseHelper.saveEntity(el, cb);
				}
				else if (t.length == 2 && el.getId() != null) {
					el.setChecked(t[1].equals("1"));
					DatabaseHelper.saveEntity(el, cb);
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
	
	private static void updateAutoExternalLinks(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
//			System.getProperties().setProperty("http.proxyHost", "globalproxy-emea.pharma.aventis.com");
//			System.getProperties().setProperty("http.proxyPort", "3129");

			String range = String.valueOf(hParams.get("range"));
			String entity = String.valueOf(hParams.get("entity"));
			StringBuffer sbUpdateSql = new StringBuffer();
			List<String> lHql = new LinkedList<String>();
			String filter = " and id between " + range.replaceAll("\\-", " and ");
			if (entity.equalsIgnoreCase(Athlete.alias)) {
				lHql.add("from Athlete where id not in (select idItem from ExternalLink where entity='" + Athlete.alias + "' and type='wiki')" + filter + " order by id");
				lHql.add("from Athlete where id not in (select idItem from ExternalLink where entity='" + Athlete.alias + "' and type='oly-ref')" + filter + " order by id");
				lHql.add("from Athlete where id not in (select idItem from ExternalLink where entity='" + Athlete.alias + "' and type='bkt-ref')" + filter + " order by id");
				lHql.add("from Athlete where id not in (select idItem from ExternalLink where entity='" + Athlete.alias + "' and type='bb-ref')" + filter + " order by id");
				lHql.add("from Athlete where id not in (select idItem from ExternalLink where entity='" + Athlete.alias + "' and type='ft-ref')" + filter + " order by id");
				lHql.add("from Athlete where id not in (select idItem from ExternalLink where entity='" + Athlete.alias + "' and type='hk-ref')" + filter + " order by id");
			}
			if (entity.equalsIgnoreCase(Championship.alias))
				lHql.add("from Championship where id not in (select idItem from ExternalLink where entity='" + Championship.alias + "' and type='wiki')" + filter + " order by id");
			if (entity.equalsIgnoreCase(City.alias))
				lHql.add("from City where id not in (select idItem from ExternalLink where entity='" + City.alias + "' and type='wiki')" + filter + " order by id");
			if (entity.equalsIgnoreCase(Complex.alias))
				lHql.add("from Complex where id not in (select idItem from ExternalLink where entity='" + Complex.alias + "' and type='wiki')" + filter + " order by id");
			if (entity.equalsIgnoreCase(Country.alias)) {
				lHql.add("from Country where id not in (select idItem from ExternalLink where entity='" + Country.alias + "' and type='wiki')" + filter + " order by id");
				lHql.add("from Country where id not in (select idItem from ExternalLink where entity='" + Country.alias + "' and type='oly-ref')" + filter + " order by id");	
			}
			if (entity.equalsIgnoreCase(Event.alias))
				lHql.add("from Event where id not in (select idItem from ExternalLink where entity='" + Event.alias + "' and type='wiki')" + filter + " order by id");
			if (entity.equalsIgnoreCase(Olympics.alias)) {
				lHql.add("from Olympics where id not in (select idItem from ExternalLink where entity='" + Olympics.alias + "' and type='wiki')" + filter + " order by id");
				lHql.add("from Olympics where id not in (select idItem from ExternalLink where entity='" + Olympics.alias + "' and type='oly-ref')" + filter + " order by id");	
			}
			if (entity.equalsIgnoreCase(Sport.alias)) {
				lHql.add("from Sport where id not in (select idItem from ExternalLink where entity='" + Sport.alias + "' and type='wiki')" + filter + " order by id");
				lHql.add("from Sport where id not in (select idItem from ExternalLink where entity='" + Sport.alias + "' and type='oly-ref')" + filter + " order by id");	
			}
			if (entity.equalsIgnoreCase(State.alias))
				lHql.add("from State where id not in (select idItem from ExternalLink where entity='" + State.alias + "' and type='wiki')" + filter + " order by id");
			if (entity.equalsIgnoreCase(Team.alias)) {
				lHql.add("from Team where id not in (select idItem from ExternalLink where entity='" + Team.alias + "' and type='wiki')" + filter + " order by id");
				lHql.add("from Team where id not in (select idItem from ExternalLink where entity='" + Team.alias + "' and type='bkt-ref')" + filter + " order by id");
				lHql.add("from Team where id not in (select idItem from ExternalLink where entity='" + Team.alias + "' and type='bb-ref')" + filter + " order by id");
				lHql.add("from Team where id not in (select idItem from ExternalLink where entity='" + Team.alias + "' and type='ft-ref')" + filter + " order by id");
				lHql.add("from Team where id not in (select idItem from ExternalLink where entity='" + Team.alias + "' and type='hk-ref')" + filter + " order by id");	
			}
			for (String hql : lHql) {
				List<Object> l = DatabaseHelper.execute(hql);
				for (Object o : l)
					sbUpdateSql.append(getExternalLink(o, hql));
			}
			DatabaseHelper.executeUpdate(sbUpdateSql.toString());
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
	
	private static void loadTranslations(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		try {
			StringBuffer html = new StringBuffer("<table>");
			String range = String.valueOf(hParams.get("range"));
			String pattern = String.valueOf(hParams.get("pattern"));
			String entity = String.valueOf(hParams.get("entity"));
			String includechecked = String.valueOf(hParams.get("includechecked"));
			
			String[] tIds = range.split("\\-");
			StringBuffer hql = new StringBuffer("from Translation where entity='" + entity + "'");
			if (tIds.length > 1 && pattern.length() == 0)
				hql.append(" and idItem between " + tIds[0] + " and " + tIds[1]);		
			hql.append(" order by idItem");

			String labels = "label, labelFR";
			if (entity.equalsIgnoreCase(City.alias))
				labels = "label || '<i> - ' || country.code || '</i>', labelFR";
			else if (entity.equalsIgnoreCase(Complex.alias))
				labels = "label || '<i> - ' || city.label || ', ' || city.country.code || '</i>'";
			List<Object[]> items = DatabaseHelper.execute("select id, " + labels + " from " + DatabaseHelper.getClassFromAlias(entity).getName() + " where 1=1" + (tIds.length > 1 ? " and id between " + tIds[0] + " and " + tIds[1] : "") + (StringUtils.notEmpty(pattern) ? " and (lower(label) like '" + pattern + "%' or lower(labelFR) like '" + pattern + "%')" : "") + " order by id");
			List<Translation> translations = DatabaseHelper.execute(hql.toString());
			html.append("<thead><th>ID</th><th>" + ResourceUtils.getText("label", lang) + " (EN)</th><th>" + ResourceUtils.getText("label", lang) + " (FR)</th><th>" + ResourceUtils.getText("checked", lang) + "&nbsp;<input type='checkbox' onclick='checkAllTranslations();'/></th></thead><tbody>");
			for (Object[] t : items) {
				Integer id = StringUtils.toInt(t[0]);
				String labelEN = String.valueOf(t[1]);
				String labelFR = String.valueOf(t[2]);
				boolean isTranslation = false;
				boolean isExclude = false;
				for (Translation tr : translations) {
					if (tr.getIdItem().equals(id)) {
						if (includechecked.equals("0") && tr.isChecked()) {
							isExclude = true;
							continue;
						}
						isTranslation = true;
						html.append("<tr id='tr-" + tr.getId() + "'><td style='display:none;'>" + tr.getId() + "</td>");
						html.append("<td>" + tr.getIdItem() + "</td>");
						html.append("<td>" + labelEN + "</td>");
						html.append("<td>" + labelFR + "</td>");
						html.append("<td><input type='checkbox'" + (tr.isChecked() ? " checked='checked'" : "") + "/></td></tr>");
					}
					else if (isTranslation)
						break;
				}
				if (!isTranslation && !isExclude) {
					html.append("<tr><td style='display:none;'>0</td>");
					html.append("<td>" + id + "</td>");
					html.append("<td>" + labelEN + "</td>");
					html.append("<td>" + labelFR + "</td>");
					html.append("<td><input type='checkbox'/></td></tr>");
				}
			}
			ServletHelper.writeText(response, html.append("</tbody></table>").toString());
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
	}
	
	private static void saveTranslations(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			String entity = String.valueOf(hParams.get("entity"));
			String value = String.valueOf(hParams.get("value"));
			for (String s : value.split("\\|")) {
				String[] t = s.split("\\~");
				String id = t[0];
				Translation tr = (id.equals("0") ? new Translation() : (Translation) DatabaseHelper.loadEntity(Translation.class, id));
				if (tr.getId() == null) {
					tr.setIdItem(Integer.parseInt(t[1]));
					tr.setEntity(entity);
				}
				tr.setChecked(t[2].equals("1"));
				DatabaseHelper.saveEntity(tr, cb);
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
	
	private static void loadFolders(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		try {
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(cb != null && !cb.isAdmin() ? " where SP.id in (" + cb.getSports() + ")" : "");
			params.add("_" + lang.toLowerCase());
			Collection<Object> coll = DatabaseHelper.call("TreeResults", params);
			StringBuffer sb = new StringBuffer();
			ArrayList<Object> lst = new ArrayList<Object>(coll);
			int i, j, k, l, m;
			for (i = 0 ; i < lst.size() ; i++) {
				TreeItem item = (TreeItem) lst.get(i);
				sb.append("<option value='" + item.getIdItem() + "'>" + item.getStdLabel() + "</option>");
				for (j = i + 1 ; j < lst.size() ; j++) {
					TreeItem item2 = (TreeItem) lst.get(j);
					if (item2.getLevel() < 2) {j--; break;}
					sb.append("<option value='" + item.getIdItem() + "," + item2.getIdItem() + "'>" + item.getStdLabel() + "-" + item2.getStdLabel() + "</option>");	
					for (k = j + 1 ; k < lst.size() ; k++) {
						TreeItem item3 = (TreeItem) lst.get(k);
						if (item3.getLevel() < 3) {k--; break;}
						sb.append("<option value='" + item.getIdItem() + "," + item2.getIdItem() + "," + item3.getIdItem() + "'>" + item.getStdLabel() + "-" + item2.getStdLabel() + "-" + item3.getStdLabel() + "</option>");
						for (l = k + 1 ; l < lst.size() ; l++) {
							TreeItem item4 = (TreeItem) lst.get(l);
							if (item4.getLevel() < 4) {l--; break;}
							sb.append("<option value='" + item.getIdItem() + "," + item2.getIdItem() + "," + item3.getIdItem() + "," + item4.getIdItem() + "'>" + item.getStdLabel() + "-" + item2.getStdLabel() + "-" + item3.getStdLabel() + "-" + item4.getStdLabel() + "</option>");
							for (m = l + 1 ; m < lst.size() ; m++) {
								TreeItem item5 = (TreeItem) lst.get(m);
								if (item5.getLevel() < 5) {m--; break;}
								sb.append("<option value='" + item.getIdItem() + "," + item2.getIdItem() + "," + item3.getIdItem() + "," + item4.getIdItem() + "," + item5.getIdItem() + "'>" + item.getStdLabel() + "-" + item2.getStdLabel() + "-" + item3.getStdLabel() + "-" + item4.getStdLabel() + "-" + item5.getStdLabel() + "</option>");
							}
							l = m;
						}
						k = l;
					}
					j = k;
				}
				i = j;
			}
			ServletHelper.writeText(response, sb.toString());
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
	}
	
	private static void saveFolders(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			DatabaseHelper.executeUpdate("ALTER TABLE \"Result\" DISABLE TRIGGER \"TriggerRS\";");
			Integer sp = StringUtils.toInt(hParams.get("sp"));
			Integer c1 = StringUtils.toInt(hParams.get("cp"));
			Integer c2 = StringUtils.toInt(hParams.get("ev1"));
			Integer c3 = StringUtils.toInt(hParams.get("ev2"));
			Integer c4 = StringUtils.toInt(hParams.get("ev3"));
			Integer autose = StringUtils.toInt(hParams.get("cb1"));
			Integer clearse1 = StringUtils.toInt(hParams.get("cb2"));
			Integer clearse2 = StringUtils.toInt(hParams.get("cb3"));
			String splabel = ((Sport) DatabaseHelper.loadEntity(Sport.class, sp)).getLabel();
			String cplabel = null;
			String ev1label = null;
			String ev2label = null;
			String ev3label = null;
			StringBuffer sql_ = new StringBuffer("UPDATE \"Result\" SET id_sport=" + sp);
			if (c1 != null && c1 > 0) {
				sql_.append(", id_championship=" + c1);
				cplabel = ((Championship) DatabaseHelper.loadEntity(Championship.class, c1)).getLabel();
			}
			if (c2 != null && c2 > 0) {
				sql_.append(", id_event=" + c2);
				ev1label = ((Event) DatabaseHelper.loadEntity(Event.class, c2)).getLabel();
			}
			if (c3 != null && c3 > 0) {
				sql_.append(", id_subevent=" + c3);
				ev2label = ((Event) DatabaseHelper.loadEntity(Event.class, c3)).getLabel();
			}
			if (c4 != null && c4 > 0) {
				sql_.append(", id_subevent2=" + c4);
				ev3label = ((Event) DatabaseHelper.loadEntity(Event.class, c4)).getLabel();
			}
			if (clearse1 == 1)
				sql_.append(", id_subevent=NULL");
			if (clearse2 == 1)
				sql_.append(", id_subevent2=NULL");
			for (String s : String.valueOf(hParams.get("list")).split("\\~")) {
				String[] t = s.split("\\,");
				StringBuffer sql = new StringBuffer(sql_);
				if (autose == 1) {
					if (t.length == 3 && (c3 == null || c3 == 0))
						sql.append(", id_subevent=" + t[2]);
					else if (t.length == 4 && (c4 == null || c4 == 0))
						sql.append(", id_subevent2=" + t[3]);
				}
				sql.append(" WHERE id_sport=" + t[0]);
				if (t.length > 1)
					sql.append(" AND id_championship=" + t[1]);
				if (t.length > 2)
					sql.append(" AND id_event=" + t[2]);
				if (t.length > 3)
					sql.append(" AND id_subevent=" + t[3]);
				if (t.length > 4)
					sql.append(" AND id_subevent2=" + t[4]);
				DatabaseHelper.executeUpdate(sql.toString());
				DatabaseHelper.executeUpdate(sql.toString().replaceAll("Result", "~InactiveItem"));
				// Keep previous path in folders history (for redirection)
				String currentParams = sp + (c1 != null && c1 > 0 ? "-" + c1 : "") + (c2 != null && c2 > 0 ? "-" + c2 : "") + (c3 != null && c3 > 0 ? "-" + c3 : "") + (c4 != null && c4 > 0 ? "-" + c4 : "");
				String currentPath = splabel + (c1 != null && c1 > 0 ? "/" + cplabel : "") + (c2 != null && c2 > 0 ? "/" + ev1label : "") + (c3 != null && c3 > 0 ? "/" + ev2label : "") + (c4 != null && c4 > 0 ? "/" + ev3label : "");
				if (autose == 1) {
					String s_ = ((Sport) DatabaseHelper.loadEntity(Sport.class, t[0])).getLabel() + (t.length > 1 ? " | " + ((Championship) DatabaseHelper.loadEntity(Championship.class, t[1])).getLabel() : "") + (t.length > 2 ? " | " + ((Event) DatabaseHelper.loadEntity(Event.class, t[2])).getLabel() : "") + (t.length > 3 ? " | " + ((Event) DatabaseHelper.loadEntity(Event.class, t[3])).getLabel() : "") + (t.length > 4 ? " | " + ((Event) DatabaseHelper.loadEntity(Event.class, t[4])).getLabel() : "");
					String[] t_ = s_.split("\\s\\|\\s");
					if (t.length == 3 && (c3 == null || c3 == 0)) {
						currentParams += "-" + t[2];
						currentPath += "/" + t_[2];
					}
					else if (t.length == 4 && (c4 == null || c4 == 0)) {
						currentParams += "-" + t[3];
						currentPath += "/" + t_[3];
					}
				}
				FolderHistory fh = new FolderHistory();
				fh.setPreviousParams(StringUtils.join(Arrays.asList(t),"-"));
				fh.setCurrentParams(currentParams);
				fh.setCurrentPath(currentPath);
				fh.setDate(new Timestamp(System.currentTimeMillis()));
				DatabaseHelper.saveEntity(fh, null);
			}
			DatabaseHelper.executeUpdate("ALTER TABLE \"Result\" ENABLE TRIGGER \"TriggerRS\";");
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
	
	private static void loadErrors(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		try {
			StringBuffer html = new StringBuffer("<table>");
			html.append("<thead><th>URL</th><th>Text</th><th>" + ResourceUtils.getText("date", lang) + "</th></thead><tbody>");
			for (ErrorReport er : (List<ErrorReport>) DatabaseHelper.execute("from ErrorReport order by date desc")) {
				String url = er.getUrl().replaceFirst("http\\:\\/\\/", "");
				html.append("<tr><td style='display:none;'>0</td>");
				html.append("<td><a href='http://" + url + "' target='_blank'>" + url.substring(url.indexOf("/")) + "</a></td>");
				html.append("<td>" + er.getText().replaceAll("\\||\r\n|\n", "<br/>") + "</td>");
				html.append("<td>" + StringUtils.toTextDate(er.getDate(), lang, null) + "</td></tr>");
			}
			ServletHelper.writeText(response, html.append("</tbody></table>").toString());
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
	}
	
	private static void loadRedirections(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		try {
			StringBuffer html = new StringBuffer("<table>");
			html.append("<thead><th>Previous path</th><th>Current path</th></thead><tbody>");
			for (Redirection re : (List<Redirection>) DatabaseHelper.execute("from Redirection order by id")) {
				html.append("<tr id='re-" + re.getId() + "'><td style='display:none;'>" + re.getId() + "</td>");
				html.append("<td><input type='text' value='" + re.getPreviousPath() + "' style='width:450px;'/></td>");
				html.append("<td><input type='text' value='" + re.getCurrentPath() + "' style='width:450px;'/></td>");
				html.append("<td><a href='javascript:addRedirection(" + re.getId() + ");'><img alt='' src='/img/component/button/add.png'/></a></td></tr>");
			}
			ServletHelper.writeText(response, html.append("</tbody></table>").toString());
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
	}
	
	private static void saveRedirections(HttpServletResponse response, Map hParams, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			String value = String.valueOf(hParams.get("value"));
			for (String s : value.split("\\|")) {
				String[] t = s.split("\\~");
				String id = t[0];
				Redirection re = (id.equals("0") ? new Redirection() : (Redirection) DatabaseHelper.loadEntity(Redirection.class, id));
				re.setPreviousPath(t[1]);
				re.setCurrentPath(t[2]);
				DatabaseHelper.saveEntity(re, cb);
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
	
	private static String getEntityLabel(int n, Integer id, String lang) throws Exception {
		String label = null;
		if (n < 10) {
			Athlete a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, id);
			if (a != null)
				label = a.toString2();
		}
		else if (n == 50) {
			Team t_ = (Team) DatabaseHelper.loadEntity(Team.class, id);
			if (t_ != null)
				label = t_.getLabel() + (t_.getCountry() != null ? " (" + t_.getCountry().getCode() + ")" : "");
		}
		else {
			Country c = (Country) DatabaseHelper.loadEntity(Country.class, id);
			if (c != null)
				label = c.getLabel(lang);
		}
		return label;
	}
	
	private static String getExternalLink(Object o, String hql) throws Exception {
		StringBuffer sql = new StringBuffer();
		String str1 = "", str2 = "", alias = "";
		if (o instanceof Athlete) {
			Athlete a = (Athlete) o;
			str1 = a.getFirstName() + " " + a.getLastName();
			str2 = a.getLastName();
			alias = Athlete.alias;
		}
		else if (o instanceof Championship) {
			Championship c = (Championship) o;
			str1 = c.getLabel();
			alias = Championship.alias;
		}
		else if (o instanceof City) {
			City c = (City) o;
			str1 = c.getLabel();
			str2 = c.getCountry().getLabel();
			alias = City.alias;
		}
		else if (o instanceof Complex) {
			Complex c = (Complex) o;
			str1 = c.getLabel();
			alias = Complex.alias;
		}
		else if (o instanceof Country) {
			Country c = (Country) o;
			str1 = c.getLabel();
			str2 = c.getCode();
			alias = Country.alias;
		}
		else if (o instanceof Event) {
			Event e = (Event) o;
			str1 = e.getLabel();
			alias = Event.alias;
		}
		else if (o instanceof Olympics) {
			Olympics o_ = (Olympics) o;
			str1 = o_.getYear().getLabel() + " " + (o_.getType() == 0 ? "Winter" : "Summer") + " Olympics";
			str2 = (o_.getType() == 0 ? "Winter" : "Summer") + "/" + o_.getYear().getLabel() + "/";
			alias = Olympics.alias;
		}
		else if (o instanceof Sport) {
			Sport s = (Sport) o;
			str1 = s.getLabel();
			alias = Sport.alias;
		}
		else if (o instanceof State) {
			State s = (State) o;
			str1 = s.getLabel();
			alias = State.alias;
		}
		else if (o instanceof Team) {
			Team t = (Team) o;
			str1 = t.getLabel();
			alias = Team.alias;
		}
		Integer id = (Integer) o.getClass().getMethod("getId").invoke(o);
		String url = null;
		URL url_ = null;
		HttpURLConnection conn = null;
		// WIKIPEDIA
		if (hql.matches(".*\\'wiki\\'.*")) {
			url = "https://en.wikipedia.org/wiki/" + URLEncoder.encode(str1.replaceAll("\\s", "_"), "utf-8");
			sql.append("insert into \"~ExternalLink\" (select nextval('\"~SeqExternalLink\"'), '" + alias + "', " + id + ", 'wiki', '" + url + "');\r\n");
		}
		// OLYMPICS-REFERENCE
		if (hql.matches(".*\\'oly-ref\\'.*")) {
			url = null;
			if (o instanceof Athlete) {
				url = "http://www.sports-reference.com/olympics/athletes/" + str2.substring(0, 2).toLowerCase() + "/" + str1.replaceAll("\\s", "-").toLowerCase() + "-1.html";	
			}
			else if (o instanceof Country) {
				url = "http://www.sports-reference.com/olympics/countries/" + str2;
			}
			else if (o instanceof Olympics) {
				url = "http://www.sports-reference.com/olympics/" + str2;
			}
			if (url != null) {
				url_ = new URL(StringUtils.normalize(url));
				conn = (HttpURLConnection) url_.openConnection();
				conn.setDoOutput(true);
				if (conn.getResponseCode() == 200) {
					StringWriter writer = new StringWriter();
					IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
					if (writer.toString().indexOf("File Not Found") == -1 && writer.toString().indexOf("0 hits") == -1)
						sql.append("insert into \"~ExternalLink\" (select nextval('\"~SeqExternalLink\"'), '" + alias + "', " + id + ", 'oly-ref', '" + StringUtils.normalize(url) + "');\r\n");
				}
			}
		}
		// BASKETBALL-REFERENCE
		if (hql.matches(".*\\'bkt-ref\\'.*")) {
			url = null;
			if (o instanceof Athlete && ((Athlete)o).getSport().getId() == 24) {
				url = "http://www.basketball-reference.com/players/" + str2.substring(0, 1).toLowerCase() + "/" + str2.substring(0, str2.length() > 5 ? 5 : str2.length()).toLowerCase() + str1.substring(0, 2).toLowerCase() + "01.html";	
			}
			if (url != null) {
				url_ = new URL(StringUtils.normalize(url));
				conn = (HttpURLConnection) url_.openConnection();
				conn.setDoOutput(true);
				if (conn.getResponseCode() == 200) {
					StringWriter writer = new StringWriter();
					IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
					if (writer.toString().indexOf("File Not Found") == -1)
						sql.append("insert into \"~ExternalLink\" (select nextval('\"~SeqExternalLink\"'), '" + alias + "', " + id + ", 'bkt-ref', '" + StringUtils.normalize(url) + "');\r\n");
				}
			}
		}
		// BASEBALL-REFERENCE
		if (hql.matches(".*\\'bb-ref\\'.*")) {
			url = null;
			if (o instanceof Athlete && ((Athlete)o).getSport().getId() == 26) {
				url = "http://www.baseball-reference.com/players/" + str2.substring(0, 1).toLowerCase() + "/" + str2.substring(0, str2.length() > 5 ? 5 : str2.length()).toLowerCase() + str1.substring(0, 2).toLowerCase() + "01.shtml";	
			}
			if (url != null) {
				url_ = new URL(StringUtils.normalize(url));
				conn = (HttpURLConnection) url_.openConnection();
				conn.setDoOutput(true);
				if (conn.getResponseCode() == 200) {
					StringWriter writer = new StringWriter();
					IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
					if (writer.toString().indexOf("File Not Found") == -1)
						sql.append("insert into \"~ExternalLink\" (select nextval('\"~SeqExternalLink\"'), '" + alias + "', " + id + ", 'bb-ref', '" + StringUtils.normalize(url) + "');\r\n");
				}
			}
		}
		// PRO-FOOTBALL-REFERENCE
		if (hql.matches(".*\\'ft-ref\\'.*")) {
			url = null;
			if (o instanceof Athlete && ((Athlete)o).getSport().getId() == 23) {
				url = "http://www.pro-football-reference.com/players/" + str2.substring(0, 1) + "/" + str2.substring(0, str2.length() > 4 ? 4 : str2.length()) + str1.substring(0, 2) + "00.htm";	
			}
			if (url != null) {
				url_ = new URL(StringUtils.normalize(url));
				conn = (HttpURLConnection) url_.openConnection();
				conn.setDoOutput(true);
				if (conn.getResponseCode() == 200) {
					StringWriter writer = new StringWriter();
					IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
					if (writer.toString().indexOf("File Not Found") == -1)
						sql.append("insert into \"~ExternalLink\" (select nextval('\"~SeqExternalLink\"'), '" + alias + "', " + id + ", 'ft-ref', '" + StringUtils.normalize(url) + "');\r\n");
				}
			}
		}
		// HOCKEY-REFERENCE
		if (hql.matches(".*\\'hk-ref\\'.*")) {
			url = null;
			if (o instanceof Athlete && ((Athlete)o).getSport().getId() == 25) {
				url = "http://www.hockey-reference.com/players/" + str2.substring(0, 1).toLowerCase() + "/" + str2.substring(0, str2.length() > 5 ? 5 : str2.length()).toLowerCase() + str1.substring(0, 2).toLowerCase() + "01.html";	
			}
			if (url != null) {
				url_ = new URL(StringUtils.normalize(url));
				conn = (HttpURLConnection) url_.openConnection();
				conn.setDoOutput(true);
				if (conn.getResponseCode() == 200) {
					StringWriter writer = new StringWriter();
					IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
					if (writer.toString().indexOf("File Not Found") == -1)
						sql.append("insert into \"~ExternalLink\" (select nextval('\"~SeqExternalLink\"'), '" + alias + "', " + id + ", 'hk-ref', '" + StringUtils.normalize(url) + "');\r\n");
				}
			}
		}
		return sql.toString();
	}

}