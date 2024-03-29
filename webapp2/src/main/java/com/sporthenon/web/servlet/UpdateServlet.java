package com.sporthenon.web.servlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
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
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.util.IOUtils;

import com.sporthenon.db.DatabaseManager;
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
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Config;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.entity.meta.ErrorReport;
import com.sporthenon.db.entity.meta.ExternalLink;
import com.sporthenon.db.entity.meta.Import;
import com.sporthenon.db.entity.meta.InactiveItem;
import com.sporthenon.db.entity.meta.PersonList;
import com.sporthenon.db.entity.meta.Redirection;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.db.entity.meta.Translation;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.ImportUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.UpdateUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.ServletHelper;

@WebServlet(
    name = "UpdateServlet",
    urlPatterns = {"/UpdateServlet"}
)
@SuppressWarnings("unchecked")
public class UpdateServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	private static final int MAX_RANKS = 20;
	private static final int MAX_AUTOCOMPLETE_RESULTS = 50;
	private static ImportUtils.Progress IMPORT_PROGRESS;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String lang = getLocale(request);
			Contributor cb = getUser(request);
			HashMap<String, Object> params = ServletHelper.getParams(request);
			if (params.containsKey("p2") && params.get("p2").equals("ajax")) {
				ajaxAutocomplete(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("save")) {
				saveResult(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("delete")) {
				deleteResult(response, params, lang, cb);
			}
			else if (params.containsKey("p2") && params.get("p2").equals("data")) {
				dataTips(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("load-entity")) {
				loadEntity(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("save-entity")) {
				saveEntity(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("delete-entity")) {
				deleteEntity(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("load-overview")) {
				loadOverview(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("set-overview-flag")) {
				setOverviewFlag(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("save-config")) {
				saveConfig(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("execute-query")) {
				executeQuery(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("merge")) {
				mergeEntity(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("execute-import")) {
				executeImport(request, response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("check-progress-import")) {
				if (IMPORT_PROGRESS != null) {
					ServletHelper.writeText(response, String.valueOf(IMPORT_PROGRESS.toString()));
				}
			}
			else if (params.containsKey("p") && params.get("p").equals("load-template")) {
				loadTemplate(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("load-extlinks")) {
				loadExternalLinks(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("save-extlinks")) {
				saveExternalLinks(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("delete-extlink")) {
				deleteExternalLink(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("updateauto-extlinks")) {
				updateAutoExternalLinks(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("load-translations")) {
				loadTranslations(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("save-translations")) {
				saveTranslations(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("load-errors")) {
				loadErrors(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("remove-error")) {
				removeError(response, params);
			}
			else if (params.containsKey("p") && params.get("p").equals("load-redirections")) {
				loadRedirections(response, params, lang, cb);
			}
			else if (params.containsKey("p") && params.get("p").equals("save-redirections")) {
				saveRedirections(response, params, lang, cb);
			}
			else {
				loadResult(request, response, params, lang, cb);
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
	private static void ajaxAutocomplete(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		String p = String.valueOf(params.get("p"));
		String field = p.split("\\~")[0];
		String currentId = null;
		String field_ = field;
		boolean isId = String.valueOf(params.get("value")).matches("^\\#\\d+");
		String value = "^" + (params.get("value") + "%").replaceAll("\\*", "%");
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
		hTable.put("sp", "sport"); hTable.put("sport", "Sport");
		hTable.put("cp", "championship"); hTable.put("championship", "Championship");
		hTable.put("ev", "event"); hTable.put("event", "Event"); hTable.put("subevent", "Event"); hTable.put("subevent2", "Event");
		hTable.put("se", "event");
		hTable.put("se2", "event");
		hTable.put("tp", "type"); hTable.put("type", "type");
		hTable.put("yr", "year"); hTable.put("year", "year");
		hTable.put("cx", "complex"); hTable.put("complex", "complex");
		hTable.put("ct", "city"); hTable.put("city", "city");
		hTable.put("st", "state"); hTable.put("state", "state");
		hTable.put("pl1", "complex"); hTable.put("complex", "complex");
		hTable.put("pl2", "complex"); hTable.put("complex", "complex");
		hTable.put("pr", "athlete"); hTable.put("athlete", "athlete"); hTable.put("person", "athlete");
		hTable.put("tm", "team"); hTable.put("team", "team");
		hTable.put("cn", "country"); hTable.put("country", "country");
		hTable.put("cb", "contributor"); hTable.put("contributor", "contributor");
		hTable.put("lg", "league"); hTable.put("league", "league");
		hTable.put("rs", "result"); hTable.put("result", "result");
		hTable.put("ol", "olympics"); hTable.put("olympics", "olympics");
		hTable.put("rt", "round_type");
		hTable.put("hf", "hall_of_fame");
		hTable.put("rc", "record");
		hTable.put("rn", "retired_number");
		hTable.put("ts", "team_stadium");
		String alias = field.toUpperCase();
		String labelHQL = "T.label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) && !field.matches("cx|pl1|pl2|lg|tm|yr|complex|league|team|year") ? "_" + lang : "");
		String l_ = "label" + ResourceUtils.getLocaleParam(lang);
		String whereHQL = "";
		String joins = "";
		if (field.matches(Athlete.alias.toLowerCase() + "|athlete|person")) {
			labelHQL = "last_name || ', ' || first_name || ' (' || CN.code || ')'";
			whereHQL += (sport != null ? " and T.id_sport=" + sport : "");
			whereHQL += (cb != null && !cb.isAdmin() ? " and T.id_sport in (" + cb.getSports() + ")" : "");
			joins += " LEFT JOIN country CN ON T.id_country=CN.id";
			alias = Athlete.alias;
		}
		else if (field.matches(Team.alias.toLowerCase() + "|team")) {
			whereHQL += (sport != null ? " and T.id_sport=" + sport : "");
			whereHQL += (cb != null && !cb.isAdmin() ? " and T.id_sport in (" + cb.getSports() + ")" : "");
			alias = Team.alias;
		}
		else if (field.equalsIgnoreCase(Sport.alias) && cb != null && StringUtils.notEmpty(cb.getSports())) {
			whereHQL += (" and T.id in (" + cb.getSports() + ")");
			alias = Sport.alias;
		}
		else if (field.equalsIgnoreCase(Contributor.alias)) {
			labelHQL = "login";
		}
		else if (field.matches("pl\\d|complex")) {
			labelHQL = "T.label || ', ' || CT." + l_ + " || ', ' || CN.code";
			joins += " LEFT JOIN city CT ON T.id_city=CT.id";
			joins += " LEFT JOIN country CN ON CT.id_country=CN.id";
			alias = Complex.alias;
		}
		else if (field.matches(Olympics.alias.toLowerCase() + "|olympics")) {
			labelHQL = "YR.label || ' ' || CT." + l_;
			joins += " LEFT JOIN city CT ON T.id_city=CT.id";
			joins += " LEFT JOIN year YR ON T.id_year=YR.id";
			alias = Olympics.alias;
		}
		else if (field.equalsIgnoreCase(Result.alias)) {
			value = "%" + value;
			// TODO coalesce(subevent." + l + ", ''), coalesce(subevent2." + l + ", '')
			labelHQL = "SP." + l_ + " || ' - ' || CP." + l_ + " || ' - ' || EV." + l_ + " || ' - ' || YR.label";
			joins += " LEFT JOIN sport SP ON T.id_sport=SP.id";
			joins += " LEFT JOIN championship CP ON T.id_championship=CP.id";
			joins += " LEFT JOIN event EV ON T.id_event=EV.id";
			joins += " LEFT JOIN year YR ON T.id_year=YR.id";
		}
		else if (field.equalsIgnoreCase(HallOfFame.alias)) {
			labelHQL = "LG.label || ' - ' || YR.label";
			joins += " LEFT JOIN league LG ON T.id_league=LG.id";
			joins += " LEFT JOIN year YR ON T.id_year=YR.id";
		}
		else if (field.equalsIgnoreCase(Record.alias)) {
			value = "%" + value;
			labelHQL = "SP." + l_ + " || ' - ' || CP." + l_ + " || ' - ' || EV." + l_ + " || ' - ' || type1 || ' - ' || type2 || ' - ' || T.label";
			joins += " LEFT JOIN sport SP ON T.id_sport=SP.id";
			joins += " LEFT JOIN championship CP ON T.id_championship=CP.id";
			joins += " LEFT JOIN event EV ON T.id_event=EV.id";
		}
		else if (field.toUpperCase().matches(RetiredNumber.alias + "|" + TeamStadium.alias)) {
			labelHQL = "LG.label || ' - ' || TM.label";
			joins += " LEFT JOIN league LG ON T.id_league=LG.id";
			joins += " LEFT JOIN team TM ON T.id_team=TM.id";
			alias = RetiredNumber.alias;
		}
		if (StringUtils.notEmpty(currentId)) {
			whereHQL += " and T.id <> " + currentId;
		}
		// Execute query
		String regexp = StringUtils.toPatternString(value);
		String sql = "SELECT T.id, " + labelHQL + ", CAST('" + alias + "' AS VARCHAR) "
				+ " FROM " + hTable.get(field) + " T" + joins
				+ " WHERE (" + (isId ? "T.id=" + value.substring(1).replaceFirst("\\%", "") : "lower(" + labelHQL + ") ~ E'" + regexp) + "')" + whereHQL
				+ " ORDER BY " + (field.equalsIgnoreCase(Result.alias) ? "id_year desc" : labelHQL)
				+ " LIMIT " + MAX_AUTOCOMPLETE_RESULTS;
		List<Object[]> l = (List<Object[]>) DatabaseManager.executeSelect(sql);
		if (field.matches(Athlete.alias.toLowerCase() + "|athlete|person")) {
			String labelHQL_ = "last_name || ', ' || first_name || ' (' || CN.code || ', ' || TM.label || ')'";
			joins += " LEFT JOIN Team TM ON T.id_team=TM.id";
			sql = "SELECT T.id, " + labelHQL_ + ", CAST('" + Athlete.alias + "' AS VARCHAR) "
				+ " FROM Athlete T" + joins
				+ " WHERE lower(" + labelHQL_ + ") ~ E'" + regexp + "'" + whereHQL
				+ " ORDER BY " + (field.equalsIgnoreCase(Result.alias) ? "id_year desc" : labelHQL_)
				+ " LIMIT " + MAX_AUTOCOMPLETE_RESULTS;
			l.addAll(DatabaseManager.executeSelect(sql));
		}
		else if (field.matches("pl\\d|complex")) {
			sql = "SELECT T.id, T." + l_ + ", CAST('" + City.alias + "' AS VARCHAR)"
				+ "FROM City T "
				+ " LEFT JOIN Country CN ON T.id_country=CN.id "
				+ " WHERE lower(T." + l_ + ") || ', ' || lower(CN.code) ~ E'" + regexp + "'"
				+ " ORDER BY T." + l_
				+ " LIMIT " + MAX_AUTOCOMPLETE_RESULTS;
			l.addAll(DatabaseManager.executeSelect(sql));
			sql = "SELECT T.id, T." + l_ + ", CAST('" + Country.alias + "' AS VARCHAR) "
				+ " FROM Country T "
				+ " WHERE lower(T." + l_ + ") ~ E'" + regexp + "'"
				+ " ORDER BY T." + l_
				+ " LIMIT " + MAX_AUTOCOMPLETE_RESULTS;
			l.addAll(DatabaseManager.executeSelect(sql));
		}
		StringBuffer html = new StringBuffer("<ul>");
		int n = 0;
		List<String> list = new ArrayList<String>();
		for (Object[] t : l) {
			if (n++ == MAX_AUTOCOMPLETE_RESULTS) {
				break;
			}
			String id = String.valueOf(t[0]);
			String text = String.valueOf(t[1]);
			String alias_ = String.valueOf(t[2]);
			if (list.contains(id)) {
				continue;
			}
			Object o = DatabaseManager.loadEntity(DatabaseManager.getClassFromAlias(alias_), id);
			if (!(o instanceof Athlete) && !(o instanceof Complex) && !(o instanceof League) && !(o instanceof Team) && !(o instanceof Result) && !(o instanceof Contributor) && !(o instanceof HallOfFame) && !(o instanceof Olympics) && !(o instanceof Record) && !(o instanceof RetiredNumber) && !(o instanceof TeamStadium)) {
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
				if (a.getLink() != null && a.getLink() == 0) {
					text = "<b>" + text + "</b>";
				}
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

			text += "<div class='ajaxid'> [#" + id + "]</div>";
			html.append("<li id='" + field_ + "|" + id + (o instanceof Event ? "|" + ((Event)o).getType().getNumber() : "") + "'>" + text + "</li>");
			list.add(id);
		}
		ServletHelper.writeText(response, html.append("</ul>").toString());
	}
	
	private static void saveResult(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		HashMap<Object, Integer> hInserted = new HashMap<Object, Integer>();
		StringBuffer sbMsg = new StringBuffer();
		StringBuffer sbMsgW = new StringBuffer();
		try {
			int tp = 0;
			HashMap<String, Type> hType = new HashMap<String, Type>();
			for (Type type : (List<Type>) DatabaseManager.executeSelect("SELECT * FROM type", Type.class)) {
				hType.put(type.getLabel(lang), type);
			}
			Integer idRS = (StringUtils.notEmpty(params.get("id")) ? StringUtils.toInt(params.get("id")) : null);
			Result result = (idRS != null ? (Result)DatabaseManager.loadEntity(Result.class, idRS) : new Result());
			// Sport
			result.setSport((Sport)DatabaseManager.loadEntity(Sport.class, params.get("sp")));
			if (result.getSport() == null) {
				Sport s = new Sport();
				s.setLabel(String.valueOf(params.get("sp-l")));
				s.setLabelFr(s.getLabel());
				s.setType(1);
				s.setIndex(Double.MAX_VALUE);
				s = (Sport) DatabaseManager.saveEntity(s, cb);
				result.setSport(s);
			}
			// Championship
			result.setChampionship((Championship)DatabaseManager.loadEntity(Championship.class, params.get("cp")));
			if (result.getChampionship() == null) {
				Championship c = new Championship();
				c.setLabel(String.valueOf(params.get("cp-l")));
				c.setLabelFr(c.getLabel());
				c.setIndex(Double.MAX_VALUE);
				c = (Championship) DatabaseManager.saveEntity(c, cb);
				result.setChampionship(c);
			}
			// Event #1
			result.setEvent((Event)DatabaseManager.loadEntity(Event.class, params.get("ev")));
			if (result.getEvent() == null) {
				String label_ = String.valueOf(params.get("ev-l"));
				Type type_ = (Type)DatabaseManager.loadEntity(Type.class, 1);
				if (label_.contains(" (")) {
					String[] t_ = label_.replaceAll("\\)", "").split("\\s\\(");
					label_ = t_[0];
					type_ = (hType.containsKey(t_[1]) ? hType.get(t_[1]) : type_);
				}
				Event e = new Event();
				e.setLabel(label_);
				e.setLabelFr(label_);
				e.setType(type_);
				e.setIndex(Double.MAX_VALUE);
				e = (Event) DatabaseManager.saveEntity(e, cb);
				result.setEvent(e);
			}
			tp = result.getEvent().getType().getNumber();
			// Event #2
			if (StringUtils.notEmpty(params.get("se")) || StringUtils.notEmpty(params.get("se-l"))) {
				result.setSubevent((Event)DatabaseManager.loadEntity(Event.class, params.get("se")));
				if (result.getSubevent() == null) {
					String label_ = String.valueOf(params.get("se-l"));
					Type type_ = (Type)DatabaseManager.loadEntity(Type.class, 1);
					if (label_.contains(" (")) {
						String[] t_ = label_.replaceAll("\\)", "").split("\\s\\(");
						label_ = t_[0];
						type_ = (hType.containsKey(t_[1]) ? hType.get(t_[1]) : type_);
					}
					Event e = new Event();
					e.setLabel(label_);
					e.setLabelFr(label_);
					e.setType(type_);
					e.setIndex(Double.MAX_VALUE);
					e = (Event) DatabaseManager.saveEntity(e, cb);
					result.setSubevent(e);
				}
				tp = result.getSubevent().getType().getNumber();
			}
			else
				result.setSubevent(null);
			// Event #3
			if (StringUtils.notEmpty(params.get("se2")) || StringUtils.notEmpty(params.get("se2-l"))) {
				result.setSubevent2((Event)DatabaseManager.loadEntity(Event.class, params.get("se2")));
				if (result.getSubevent2() == null) {
					String label_ = String.valueOf(params.get("se2-l"));
					Type type_ = (Type)DatabaseManager.loadEntity(Type.class, 1);
					if (label_.contains(" (")) {
						String[] t_ = label_.replaceAll("\\)", "").split("\\s\\(");
						label_ = t_[0];
						type_ = (hType.containsKey(t_[1]) ? hType.get(t_[1]) : type_);
					}
					Event e = new Event();
					e.setLabel(label_);
					e.setLabelFr(label_);
					e.setType(type_);
					e.setIndex(Double.MAX_VALUE);
					e = (Event) DatabaseManager.saveEntity(e, cb);
					result.setSubevent2(e);
				}
				tp = result.getSubevent2().getType().getNumber();
			}
			else {
				result.setSubevent2(null);
			}
			// Year
			result.setYear((Year)DatabaseManager.loadEntity(Year.class, params.get("yr")));
			if (result.getYear() == null) {
				Year y = new Year();
				y.setLabel(String.valueOf(params.get("yr-l")));
				y = (Year) DatabaseManager.saveEntity(y, cb);
				result.setYear(y);
			}
			// Places
			for(int i : new int[]{1, 2}) {
				if (StringUtils.notEmpty(params.get("pl" + i + "-l"))) {
					String[] t = String.valueOf(params.get("pl" + i + "-l")).toLowerCase().split("\\,\\s");
					boolean isComplex = false;
					boolean isCity = false;
					if (t.length > 2) {
						isComplex = true;
					}
					else if (t.length > 1) {
						isCity = true;
					}
					int id = 0;
					if (StringUtils.notEmpty(params.get("pl" + i))) {
						id = StringUtils.toInt(params.get("pl" + i));
					}
					else {
						id = ImportUtils.insertPlace(0, String.valueOf(params.get("pl" + i + "-l")), cb, null, lang);
					}
					if (isComplex) {
						if (i == 1) {
							result.setComplex1((Complex)DatabaseManager.loadEntity(Complex.class, id));
							result.setCity1(null);
							result.setCountry1(null);
						}
						else {
							result.setComplex2((Complex)DatabaseManager.loadEntity(Complex.class, id));
							result.setCity2(null);
							result.setCountry2(null);
						}
					}
					else if (isCity){
						if (i == 1) {
							result.setCity1((City)DatabaseManager.loadEntity(City.class, id));
							result.setComplex1(null);
							result.setCountry1(null);
						}
						else {
							result.setCity2((City)DatabaseManager.loadEntity(City.class, id));
							result.setComplex2(null);
							result.setCountry2(null);
						}
					}
					else {
						if (i == 1) {
							result.setCountry1((Country)DatabaseManager.loadEntity(Country.class, id));
							result.setCity1(null);
							result.setComplex1(null);
						}
						else {
							result.setCountry2((Country)DatabaseManager.loadEntity(Country.class, id));
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
			if (StringUtils.notEmpty(params.get("dt1-l"))) {
				String dt = String.valueOf(params.get("dt1-l"));
				try {
					df.parse(dt);
				}
				catch (ParseException e) {
					throw new Exception(ResourceUtils.getText("err.invalid.date", lang).replaceAll("#S#", dt));
				}
				result.setDate1(dt);
			}
			else {
				result.setDate1(null);
			}
			if (StringUtils.notEmpty(params.get("dt2-l"))) {
				String dt = String.valueOf(params.get("dt2-l"));
				try {
					df.parse(dt);
				}
				catch (ParseException e) {
					throw new Exception(ResourceUtils.getText("err.invalid.date", lang).replaceAll("#S#", dt));
				}
				result.setDate2(dt);
			}
			else {
				result.setDate2(null);
			}
			result.setComment(StringUtils.notEmpty(params.get("cmt-l")) ? String.valueOf(params.get("cmt-l")) : null);
			result.setExa(StringUtils.notEmpty(params.get("exa-l")) ? String.valueOf(params.get("exa-l")) : null);
			result.setInProgress(String.valueOf(params.get("inprogress")).equals("1"));
			result.setNoDate(String.valueOf(params.get("nodate")).equals("1"));
			result.setNoPlace(String.valueOf(params.get("noplace")).equals("1"));
			// Inactive item
			String hql = "SELECT * FROM _inactive_item WHERE id_sport = ? and id_championship = ? and id_event = ?"
					+ (result.getSubevent() != null ? " and id_subevent = " + result.getSubevent().getId() : "")
					+ (result.getSubevent2() != null ? " and id_subevent2 = " + result.getSubevent2().getId() : "");
			Object o = DatabaseManager.loadEntity(hql, Arrays.asList(result.getSport().getId(), result.getChampionship().getId(), result.getEvent().getId()), InactiveItem.class);
			String inact = (o != null ? "1" : "0");
			if (!inact.equals(String.valueOf(params.get("inact")))) {
				if (o != null) {
					DatabaseManager.removeEntity(o);
				}
				else {
					InactiveItem item = new InactiveItem();
					item.setIdSport(result.getSport().getId());
					item.setIdChampionship(result.getChampionship().getId());
					item.setIdEvent(result.getEvent().getId());
					item.setIdSubevent(result.getSubevent() != null ? result.getSubevent().getId() : null);
					item.setIdSubevent2(result.getSubevent2() != null ? result.getSubevent2().getId() : null);
					DatabaseManager.saveEntity(item, null);
				}
			}
			// Rankings
			for (int i = 1 ; i <= MAX_RANKS ; i++) {
				Integer id = (StringUtils.notEmpty(params.get("rk" + i)) ? StringUtils.toInt(params.get("rk" + i)) : 0);
				o = params.get("rk" + i + "-l");
				if (id == 0 && StringUtils.notEmpty(o)) {
					if (hInserted.keySet().contains(o)) {
						id = hInserted.get(o);
					}
					else {
						StringBuffer sb = new StringBuffer();
						id = ImportUtils.insertEntity(0, tp, result.getSport() != null ? result.getSport().getId() : 0, String.valueOf(o), result.getYear().getLabel(), cb, sb, lang);
						hInserted.put(o, id);
						if (sb != null && sb.toString().contains("New Team")) {
							sbMsgW.append("<span style='color:orange;'>(" + ResourceUtils.getText("warning.team.created", lang).replaceAll("\\{1\\}", "\"" + sb.toString().replaceAll("\\,.*", "").split("\\|")[1].trim() + "\"") + ")</span>");
						}
					}
				}
				Result.class.getMethod("setIdRank" + i, Integer.class).invoke(result, id > 0 ? id : null);
				Result.class.getMethod("setResult" + i, String.class).invoke(result, StringUtils.notEmpty(params.get("rs" + i + "-l")) ? params.get("rs" + i + "-l") : null);
			}
			result = (Result) DatabaseManager.saveEntity(result, cb);
			// External links
			DatabaseManager.saveExternalLinks(Result.alias, result.getId(), String.valueOf(params.get("exl-l")));
			// Person List
			if (params.containsKey("rk1list")) {
				int i = 1;
				while (params.containsKey("rk" + i + "list")) {
					String[] t = String.valueOf(params.get("rk" + i + "list")).split("\\|", 0);
					for (String value : t) {
						if (StringUtils.notEmpty(value)) {
							String[] t_ = value.split("\\:", -1);
							String id = t_[0];
							String idp = t_[1];
							if (StringUtils.notEmpty(idp) && !idp.equals("null") && !idp.startsWith("Name #")) {
								String index = (t_.length > 1 ? t_[2] : null);
								PersonList plist = (StringUtils.notEmpty(id) && !id.equals("0") ? (PersonList) DatabaseManager.loadEntity(PersonList.class, id) : new PersonList());
								plist.setIdResult(result.getId());
								plist.setRank(i);
								if (idp.matches("\\d+")) {
									plist.setIdPerson(Integer.parseInt(idp));
								}
								else {
									plist.setIdPerson(ImportUtils.insertEntity(0, 1, result.getSport() != null ? result.getSport().getId() : 0, idp, result.getYear().getLabel(), cb, null, lang));
								}
								plist.setIndex(StringUtils.notEmpty(index) && !index.equals("null") ? index : null);
								DatabaseManager.saveEntity(plist, cb);
							}
						}
					}
					i++;
				}
			}
			if (params.containsKey("pldel") && idRS != null) {
				String[] t = String.valueOf(params.get("pldel")).split("\\|", 0);
				for (String value : t) {
					if (StringUtils.notEmpty(value)) {
						DatabaseManager.removeEntity(DatabaseManager.loadEntity(PersonList.class, value));
					}
				}
			}
			// Rounds
			if (params.containsKey("rdlist")) {
				String[] t = String.valueOf(params.get("rdlist")).split("\\|", 0);
				for (String value : t) {
					String[] t_ = value.split("\\~", -1);
					if (t_.length > 1) {
						String idr = (idRS != null ? t_[0] : null);
						RoundType rdRt = null;
						if (StringUtils.notEmpty(t_[1])) {
							rdRt = (RoundType) DatabaseManager.loadEntity(RoundType.class, t_[1]);
						}
						else {
							RoundType rdt = new RoundType();
							rdt.setLabel(t_[2]);
							rdt.setLabelFr(t_[2]);
							rdt.setIndex(0.0d);
							rdRt = (RoundType) DatabaseManager.saveEntity(rdt, cb);
						}
						Integer rdRk1 = (StringUtils.notEmpty(t_[3]) ? Integer.parseInt(t_[3]) : (StringUtils.notEmpty(t_[4]) ? ImportUtils.insertEntity(0, tp, (result.getSport() != null ? result.getSport().getId() : 0), t_[4], result.getYear().getLabel(), cb, null, lang) : 0));
						String rdRs1 = (StringUtils.notEmpty(t_[5]) ? t_[5] : null);
						Integer rdRk2 = (StringUtils.notEmpty(t_[6]) ? Integer.parseInt(t_[6]) : (StringUtils.notEmpty(t_[7]) ? ImportUtils.insertEntity(0, tp, (result.getSport() != null ? result.getSport().getId() : 0), t_[7], result.getYear().getLabel(), cb, null, lang) : 0));
						String rdRs2 = (StringUtils.notEmpty(t_[8]) ? t_[8] : null);
						Integer rdRk3 = (StringUtils.notEmpty(t_[9]) ? Integer.parseInt(t_[9]) : (StringUtils.notEmpty(t_[10]) ? ImportUtils.insertEntity(0, tp, (result.getSport() != null ? result.getSport().getId() : 0), t_[10], result.getYear().getLabel(), cb, null, lang) : 0));
						String rdRs3 = (StringUtils.notEmpty(t_[11]) ? t_[11] : null);
						Integer rdRk4 = (StringUtils.notEmpty(t_[12]) ? Integer.parseInt(t_[12]) : (StringUtils.notEmpty(t_[13]) ? ImportUtils.insertEntity(0, tp, (result.getSport() != null ? result.getSport().getId() : 0), t_[13], result.getYear().getLabel(), cb, null, lang) : 0));
						String rdRs4 = (StringUtils.notEmpty(t_[14]) ? t_[14] : null);
						Integer rdRk5 = (StringUtils.notEmpty(t_[15]) ? Integer.parseInt(t_[15]) : (StringUtils.notEmpty(t_[16]) ? ImportUtils.insertEntity(0, tp, (result.getSport() != null ? result.getSport().getId() : 0), t_[16], result.getYear().getLabel(), cb, null, lang) : 0));
						String rdRs5 = (StringUtils.notEmpty(t_[17]) ? t_[17] : null);
						String rdDt1 = (StringUtils.notEmpty(t_[18]) ? t_[18] : null);
						String rdDt2 = (StringUtils.notEmpty(t_[19]) ? t_[19] : null);
						Complex rdCx1 = null; Complex rdCx2 = null;
						City rdCt1 = null; City rdCt2 = null;
						if (StringUtils.notEmpty(t_[20]) || StringUtils.notEmpty(t_[21])) {
							String[] tpl = t_[21].toLowerCase().split("\\,\\s");
							if (tpl.length > 1) {
								int id = 0;
								if (StringUtils.notEmpty(t_[20])) {
									id = StringUtils.toInt(t_[20]);
								}
								else {
									id = ImportUtils.insertPlace(0, String.valueOf(t_[21]), cb, null, lang);
								}
								if (tpl.length > 2) {
									rdCx1 = (Complex) DatabaseManager.loadEntity(Complex.class, id);
								}
								else {
									rdCt1 = (City) DatabaseManager.loadEntity(City.class, id);
								}
							}
						}
						if (StringUtils.notEmpty(t_[22]) || StringUtils.notEmpty(t_[23])) {
							String[] tpl = t_[23].toLowerCase().split("\\,\\s");
							if (tpl.length > 1) {
								int id = 0;
								if (StringUtils.notEmpty(t_[22])) {
									id = StringUtils.toInt(t_[22]);
								}
								else {
									id = ImportUtils.insertPlace(0, String.valueOf(t_[23]), cb, null, lang);
								}
								if (tpl.length > 2) {
									rdCx2 = (Complex) DatabaseManager.loadEntity(Complex.class, id);
								}
								else {
									rdCt2 = (City) DatabaseManager.loadEntity(City.class, id);
								}
							}
						}
						String rdExa = (StringUtils.notEmpty(t_[24]) ? t_[24] : null);
						String rdCmt = (StringUtils.notEmpty(t_[25]) ? t_[25] : null);
						Round rd = (StringUtils.notEmpty(idr) ? (Round) DatabaseManager.loadEntity(Round.class, idr) : new Round());
						rd.setIdResult(result.getId());
						rd.setIdResultType(tp);
						rd.setRoundType(rdRt);
						rd.setIdRank1(rdRk1 > 0 ? rdRk1 : null);
						rd.setResult1(rdRs1);
						rd.setIdRank2(rdRk2 > 0 ? rdRk2 : null);
						rd.setResult2(rdRs2);
						rd.setIdRank3(rdRk3 > 0 ? rdRk3 : null);
						rd.setResult3(rdRs3);
						rd.setIdRank4(rdRk4 > 0 ? rdRk4 : null);
						rd.setResult4(rdRs4);
						rd.setIdRank5(rdRk5 > 0 ? rdRk5 : null);
						rd.setResult5(rdRs5);
						rd.setComplex1(rdCx1);
						rd.setCity1(rdCt1);
						rd.setComplex2(rdCx2);
						rd.setCity2(rdCt2);
						rd.setDate(rdDt1);
						rd.setDate1(rdDt2);
						rd.setExa(rdExa);
						rd.setComment(rdCmt);
						DatabaseManager.saveEntity(rd, cb);
					}
				}
			}
			if (params.containsKey("rddel") && idRS != null) {
				String[] t = String.valueOf(params.get("rddel")).split("\\|", 0);
				for (String value : t) {
					if (StringUtils.notEmpty(value)) {
						DatabaseManager.removeEntity(DatabaseManager.loadEntity(Round.class, value));
					}
				}
			}
			sbMsg.append(result.getId() + "#" + ResourceUtils.getText("result." + (idRS != null ? "modified" : "created"), lang));
			if (sbMsgW.length() > 0) {
				sbMsg.append(" ").append(sbMsgW);
			}
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void deleteResult(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			Object id = params.get("id");
			DatabaseManager.removeEntity(DatabaseManager.loadEntity(Result.class, id));
			sbMsg.append(ResourceUtils.getText("result.deleted", lang));
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void dataTips(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		try {
			String lang_ = ResourceUtils.getLocaleParam(lang);
			StringBuffer html = new StringBuffer("<table>");
			HashMap<String, String> sql = new HashMap<String, String>();
			sql.put("team", "SELECT TM.label, SP.label" + lang_ + " FROM team TM JOIN sport SP ON SP.id = TM.id_sport ORDER BY 1");
			sql.put("country", "SELECT label" + lang_ + ", code FROM country ORDER BY 1");
			sql.put("state", "SELECT label" + lang_ + ", code FROM state ORDER BY 1");
			List<Object[]> list = (List<Object[]>) DatabaseManager.executeSelect(sql.get(params.get("p")));
			for (Object[] t : list) {
				html.append("<tr><td>" + t[0] + "</td><td>" + t[1] + "</td></tr>");
			}
			ServletHelper.writeText(response, html.append("</table>").toString());
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	private static void loadOverview(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		StringBuffer html = new StringBuffer();
		List<Object> params_ = new ArrayList<Object>();
		Object pattern = params.get("pattern");
		params_.add(params.get("entity"));
		params_.add(StringUtils.toInt(params.get("sport")));
		params_.add(StringUtils.toInt(params.get("count")));
		params_.add(pattern);
		if (pattern != null && String.valueOf(pattern).matches("\\d+\\-\\d+")) {
			String[] t = String.valueOf(pattern).split("\\-");
			params_.add(Integer.parseInt(t[0]));
			params_.add(Integer.parseInt(t[1]));
		}
		else {
			params_.add(0);
			params_.add(0);
		}
		params_.add(ResourceUtils.getLocaleParam(lang));
		String currentEntity = null;
		for (RefItem item : (List<RefItem>) DatabaseManager.callFunctionSelect("_overview", params_, RefItem.class, "id")) {
			if (currentEntity == null || !item.getEntity().equals(currentEntity)) {
				if (currentEntity != null) {
					html.append("</tbody></table>");
				}
				html.append("<table><thead><tr>");
				if (item.getEntity().equals(Result.alias)) {
					html.append("<th colspan='9' style='text-align:center;'>" + ResourceUtils.getText("entity." + Result.alias, lang).toUpperCase() + "</th></tr><tr><th>" + ResourceUtils.getText("entity.YR.1", lang) + "</th><th>" + ResourceUtils.getText("entity.EV.1", lang) + "</th><th>" + ResourceUtils.getText("entity.RS.1", lang) + "</th><th>" + ResourceUtils.getText("place", lang) + "</th><th>" + ResourceUtils.getText("dates", lang) + "</th><th>" + ResourceUtils.getText("entity.RD", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("photos", lang) + "</th>");
				}
				else if (item.getEntity().equals(Athlete.alias)) {
					html.append("<th colspan='9' style='text-align:center;'>" + ResourceUtils.getText("entity." + Athlete.alias, lang).toUpperCase() + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("entity.SP.1", lang) + "</th><th>" + ResourceUtils.getText("entity.CN.1", lang) + "</th><th>" + ResourceUtils.getText("entity.TM.1", lang) + "</th><th>" + ResourceUtils.getText("linked.to", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("photos", lang) + "</th>");
				}
				else if (item.getEntity().equals(Team.alias)) {
					html.append("<th colspan='9' style='text-align:center;'>" + ResourceUtils.getText("entity." + Team.alias, lang).toUpperCase() + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("entity.SP.1", lang) + "</th><th>" + ResourceUtils.getText("entity.CN.1", lang) + "</th><th>" + ResourceUtils.getText("league", lang) + "</th><th>" + ResourceUtils.getText("linked.to", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("logo", lang) + "</th>");
				}
				else if (item.getEntity().equals(Sport.alias)) {
					html.append("<th colspan='5' style='text-align:center;'>" + ResourceUtils.getText("entity." + Sport.alias, lang).toUpperCase() + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("pictures", lang) + "</th>");
				}
				else if (item.getEntity().equals(Championship.alias)) {
					html.append("<th colspan='5' style='text-align:center;'>" + ResourceUtils.getText("entity." + Championship.alias, lang).toUpperCase() + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("pictures", lang) + "</th>");
				}
				else if (item.getEntity().equals(Event.alias)) {
					html.append("<th colspan='5' style='text-align:center;'>" + ResourceUtils.getText("entity." + Event.alias, lang).toUpperCase() + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("pictures", lang) + "</th>");
				}
				else if (item.getEntity().equals(City.alias)) {
					html.append("<th colspan='7' style='text-align:center;'>" + ResourceUtils.getText("entity." + City.alias, lang).toUpperCase() + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("entity.CN.1", lang) + "</th><th>" + ResourceUtils.getText("linked.to", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("pictures", lang) + "</th>");
				}
				else if (item.getEntity().equals(Complex.alias)) {
					html.append("<th colspan='7' style='text-align:center;'>" + ResourceUtils.getText("entity." + Complex.alias, lang).toUpperCase() + "</th></tr><tr><th>" + ResourceUtils.getText("name", lang) + "</th><th>" + ResourceUtils.getText("entity.CT.1", lang) + "</th><th>" + ResourceUtils.getText("linked.to", lang) + "</th><th>" + ResourceUtils.getText("ref", lang) + "</th><th>" + ResourceUtils.getText("ext.links", lang) + "</th><th>" + ResourceUtils.getText("pictures", lang) + "</th>");
				}
				html.append("<th>" + ResourceUtils.getText("completion.pct", lang) + "</th>");
				html.append("</tr></thead><tbody class='tby'>");
				currentEntity = item.getEntity();
			}
			boolean isPhoto = (ImageUtils.getPhotos(item.getEntity(), item.getIdItem(), lang) != null);
			boolean isNopic = (item.getCount3() != null && item.getCount3() == 1);
			int picsL = ImageUtils.getImages(ImageUtils.getIndex(item.getEntity()), item.getIdItem(), ImageUtils.SIZE_LARGE).size();
			int picsS = ImageUtils.getImages(ImageUtils.getIndex(item.getEntity()), item.getIdItem(), ImageUtils.SIZE_SMALL).size();
			int comp = 0, max = 1;
			String url = "/" + ResourceUtils.getText("entity." + item.getEntity() + ".1", ResourceUtils.LGDEFAULT).replaceAll("\\s", "").toLowerCase() + "/" + StringUtils.encode(item.getEntity() + "-" + item.getIdItem());
			html.append("<tr>");
//			html.append("<td>" + item.getIdItem() + "</td>");
			if (item.getEntity().equals(Result.alias)) {
				int rkcount = (item.getTxt3() != null ? item.getTxt3().split("\\,").length : 0);
				int rscount = (item.getTxt4() != null ? item.getTxt4().split("\\,").length : 0);
				boolean isScore = (rkcount >= 2 && rscount == 1);
				boolean isNoDate = (item.getCount1() != null && item.getCount1() == 1);
				boolean isNoPlace = (item.getCount2() != null && item.getCount2() == 1);
				String[] tplace = item.getTxt1().split("\\,", -1);
				int cxcount = (StringUtils.notEmpty(tplace[0]) && !tplace[0].equals("0") ? 1 : 0) + (StringUtils.notEmpty(tplace[1]) && !tplace[1].equals("0") ? 1 : 0);
				int ctcount = (StringUtils.notEmpty(tplace[2]) && !tplace[2].equals("0") ? 1 : 0) + (StringUtils.notEmpty(tplace[3]) && !tplace[3].equals("0") ? 1 : 0);
				String[] tdate = item.getTxt2().split("\\,", -1);
				int dtcount = (StringUtils.notEmpty(tdate[0]) && !tdate[0].equals("0") ? 1 : 0) + (StringUtils.notEmpty(tdate[1]) && !tdate[1].equals("0") ? 1 : 0);
				html.append("<td>" + item.getLabelRel1() + "</td>");
				html.append("<td><a href='/update/results/" + StringUtils.encode(Result.alias + "-" + item.getIdItem()) + "' target='_blank'>" + item.getLabelRel2() + " - " + item.getLabelRel3() + (StringUtils.notEmpty(item.getLabelRel4()) ? " - " + item.getLabelRel4() : "") + (StringUtils.notEmpty(item.getLabelRel5()) ? " - " + item.getLabelRel5() : "") + (StringUtils.notEmpty(item.getLabelRel6()) ? " - " + item.getLabelRel6() : "") + "</a></td>");
				if (rkcount >= 3) {
					html.append("<td class='tick'>" + ResourceUtils.getText("podium", lang) + " (" + rkcount + ")</td>");
				}
				else if (isScore) {
					html.append("<td class='tick'>" + ResourceUtils.getText("final", lang) + "+" + ResourceUtils.getText("score", lang) + "</td>");
				}
				else if (rkcount > 0) {
					html.append("<td class='warning_'>" + rkcount + "</td>");
				}
				else {
					html.append("<td class='missing'></td>");
				}
				if (cxcount > 0) {
					html.append("<td class='tick'>" + ResourceUtils.getText("entity.CX.1", lang) + " (" + cxcount + ")</td>");
				}
				else if (ctcount > 0) {
					html.append("<td class='tick'>" + ResourceUtils.getText("entity.CT.1", lang) + " (" + ctcount + ")</td>");
				}
				else if (isNoPlace) {
					html.append("<td class='tick'></td>");
				}
				else {
					html.append("<td class='warning_'><input type='checkbox' title='" + ResourceUtils.getText("no.place", lang) + "' onclick=\"setOverviewFlag('no_place', " + item.getIdItem() + ");\"/></td>");
				}
				if (dtcount > 0 || isNoDate) {
					html.append("<td class='tick'></td>");
				}
				else {
					html.append("<td class='warning_'><input type='checkbox' title='" + ResourceUtils.getText("no.date", lang) + "' onclick=\"setOverviewFlag('no_date', " + item.getIdItem() + ");\"/></td>");
				}
				html.append(StringUtils.notEmpty(item.getTxt6()) ? "<td class='tick'>" + item.getTxt6().split("\\,").length + "</td>" : "<td></td>");
				comp = (rkcount > 0 ? 1 : 0) + (cxcount > 0 || ctcount > 0 || isNoPlace ? 1 : 0) + (dtcount > 0 || isNoDate ? 1 : 0) + (StringUtils.notEmpty(item.getTxt6()) ? 1 : 0);
				comp += (rkcount >= 3 || isScore ? 1 : 0);
				max = 7;
			}
			else if (item.getEntity().equals(Athlete.alias)) {
				html.append("<td><a href='" + url + "' target='_blank'>" + item.getLabelRel1() + ", " + item.getLabelRel2() + "</a></td>");
				html.append("<td" + (StringUtils.notEmpty(item.getLabelRel5()) ? ">" + item.getLabelRel5() : " class='missing'>") + "</td>");
				html.append("<td" + (StringUtils.notEmpty(item.getLabelRel3()) ? ">" + item.getLabelRel3() : " class='missing'>") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(item.getLabelRel4()) ? item.getLabelRel4() : "-") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(item.getLabelEN()) ? item.getLabelEN() : "-") + "</td>");
				html.append("<td>" + item.getCount2() + "</td>");
				comp = (StringUtils.notEmpty(item.getLabelRel5()) ? 1 : 0);
				max = 3;
			}
			else if (item.getEntity().equals(Team.alias)) {
				html.append("<td><a href='" + url + "' target='_blank'>" + item.getLabelRel1() + "</a></td>");
				html.append("<td" + (StringUtils.notEmpty(item.getLabelRel2()) ? ">" + item.getLabelRel2() : " class='missing'>") + "</td>");
				html.append("<td" + (StringUtils.notEmpty(item.getLabelRel3()) ? ">" + item.getLabelRel3() : " class='missing'>") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(item.getLabelRel4()) ? item.getLabelRel4() : "-") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(item.getLabelEN()) ? item.getLabelEN() : "-") + "</td>");
				html.append("<td>" + item.getCount2() + "</td>");
				comp = (StringUtils.notEmpty(item.getLabelRel2()) ? 1 : 0) + (StringUtils.notEmpty(item.getLabelRel3()) ? 1 : 0);
				max = 4;
			}
			else if (item.getEntity().equals(Sport.alias)) {
				html.append("<td><a href='" + url + "' target='_blank'>" + item.getLabelRel1() + "</a></td>");
				html.append("<td>" + item.getCount2() + "</td>");
				max = 2;
			}
			else if (item.getEntity().equals(Championship.alias)) {
				html.append("<td><a href='" + url + "' target='_blank'>" + item.getLabelRel1() + "</a></td>");
				html.append("<td>" + item.getCount2() + "</td>");
				max = 2;
			}
			else if (item.getEntity().equals(Event.alias)) {
				html.append("<td><a href='" + url + "' target='_blank'>" + item.getLabelRel1() + "</a></td>");
				html.append("<td>" + item.getCount2() + "</td>");
				max = 2;
			}
			else if (item.getEntity().equals(City.alias)) {
				html.append("<td><a href='" + url + "' target='_blank'>" + item.getLabelRel1() + "</a></td>");
				html.append("<td" + (StringUtils.notEmpty(item.getLabelRel2()) ? ">" + item.getLabelRel2() : " class='missing'>") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(item.getLabelEN()) ? item.getLabelEN() : "-") + "</td>");
				html.append("<td>" + item.getCount2() + "</td>");
				comp = (StringUtils.notEmpty(item.getLabelRel2()) ? 1 : 0);
				max = 3;
			}
			else if (item.getEntity().equals(Complex.alias)) {
				html.append("<td><a href='" + url + "' target='_blank'>" + item.getLabelRel1() + "</a></td>");
				html.append("<td" + (StringUtils.notEmpty(item.getLabelRel2()) ? ">" + item.getLabelRel2() + ", " + item.getLabelRel3() : " class='missing'>") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(item.getLabelEN()) ? item.getLabelEN() : "-") + "</td>");
				html.append("<td>" + item.getCount2() + "</td>");
				comp = (StringUtils.notEmpty(item.getLabelRel2()) ? 1 : 0);
				max = 3;
			}
			html.append(StringUtils.notEmpty(item.getLabel()) ? "<td class='tick'>" + item.getLabel().split("\\,").length + "</td>" : "<td class='warning_'></td>");
			comp += (StringUtils.notEmpty(item.getLabel()) ? 1 : 0);
			if (item.getEntity().matches(Athlete.alias + "|" + Result.alias)) {
				html.append("<td" + (isPhoto ? " class='tick'" : " class='missing'") + "></td>");
				comp += (isPhoto ? 1 : 0);
			}
			else {
				html.append("<td" + (picsL > 0 && picsS > 0 ? " class='tick'>(" + picsL + "L+" + picsS + "S)" : (isNopic ? ">-" : " class='missing'>")) + "</td>");
				comp += (picsL > 0 && picsS > 0 ? 1 : 0);
			}
			if (params.get("showimg").equals("1")) {
				short index = ImageUtils.getIndex(item.getEntity());
				if (index != -1) {
					html.append("<td>");
					for (String img : ImageUtils.getImages(index, item.getIdItem(), ImageUtils.SIZE_SMALL)) {
						html.append("<a href='" + ImageUtils.getUrl() + img.replaceFirst("\\-S", "\\-L") + "' target='_blank'><img title='" + img + "' src='" + ImageUtils.getUrl() + img + "'/></a>");
					}
					html.append("</td>");
				}
			}
			int pct = Math.round((100 * comp) / max);
			html.append("<td style='font-size:9px;font-weight:bold;color:" + (pct > 90 ? "green" : (pct > 50 ? "orange" : "red")) + ";'>" + pct + "%</td>");
			html.append("</tr>");
		}
		ServletHelper.writeText(response, html.append("</tbody></table>").toString());
	}
	
	private static void saveEntity(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		String msg = null;
		try {
			String alias = String.valueOf(params.get("alias"));
			String id = String.valueOf(params.get("id"));
			Class<?> c = DatabaseManager.getClassFromAlias(alias);
			Object o = UpdateUtils.saveEntity(alias, id, params, cb);
			String id_ = String.valueOf(c.getMethod("getId").invoke(o, new Object[0]));
			msg = ResourceUtils.getText("update.ok", lang) + " " + StringUtils.SEP1 + " " + ResourceUtils.getText("entity." + alias + ".1", lang) + " #" + id_;
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			msg = "ERR:" + e.getMessage();
		}
		finally {
			ServletHelper.writeText(response, msg);
		}
	}
	
	private static void saveConfig(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			for (Object o : params.keySet()) {
				String p = String.valueOf(o);
				if (p.startsWith("p_")) {
					Config c = (Config) DatabaseManager.loadEntity(Config.class, p.substring(2));
					if (c != null) {
						if (c.getKey().startsWith("html")) {
							c.setValue(null);
							c.setValueHtml(String.valueOf(params.get(p)));
						}
						else {
							c.setValue(String.valueOf(params.get(p)));
							c.setValueHtml(null);
						}
						DatabaseManager.saveEntity(c, null);
					}
				}
				else if (p.equals("new") && StringUtils.notEmpty(params.get(p))) {
					for (String s : String.valueOf(params.get(p)).split("\r\n")) {
						String[] t = s.split("\\=", -1);
						Config c = new Config();
						c.setKey(t[0]);
						c.setValue(t[1]);
						DatabaseManager.saveEntity(c, null);
					}
				}
			}
			sbMsg.append(ResourceUtils.getText("update.ok", lang));
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void executeQuery(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		boolean isCSV = params.containsKey("csv");
		StringBuffer sb = new StringBuffer(!isCSV ? "<table>" : "");
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String query = null;
		Integer index = StringUtils.toInt(params.get("index"));
		if (index != -1) {
			query = UpdateUtils.queries.get(index);
			query = query.replaceAll("#YEAR#", String.valueOf(year));
			query = query.replaceAll("#YEARP#", String.valueOf(params.get("year")));
			query = query.replaceAll("#SPORT#", String.valueOf(params.get("sport")));
			query = query.replaceAll("#WHERE#", (year % 4 == 0 ? "(CP.id<>1 OR SP.type<>0)" : (year % 4 == 2 ? "(CP.id<>1 OR SP.type<>1)" : "CP.id<>1")) + (year % 4 != 1 ? " AND CP.id<>78" : ""));			
		}
		else {
			query = String.valueOf(params.get("query"));
		}
		
		if (!isCSV) {
			sb.append("<tr style='display:none;'><td>" + query + "</td></tr>");
		}
		
		List<Object[]> list = (List<Object[]>) DatabaseManager.executeSelect(query);
		if (list != null && list.size() > 0) {
			int i = 0;
			boolean isFirstRow = true;
			for (Object[] t : list)  {
				if (!isCSV) {
					sb.append("<tr>");
				}
				if (isFirstRow && index != 9) {
					for (i = 1 ; i <= t.length ; i++) {
						sb.append(!isCSV ? "<th>" : (i > 1 ? "," : "")).append("Col." + i).append(!isCSV ? "</th>" : "");
					}
					sb.append(isCSV ? "\r\n" : "</tr><tr>");
					isFirstRow = false;
				}
				i = 0;
				for (Object o : t) {
					if (index != 9 || (i < 5 || i > 8)) {
						sb.append(!isCSV ? "<td>" : (i > 0 ? "," : "")).append(o != null ? String.valueOf(o) : "").append(!isCSV ? "</td>" : "");
					}
					i++;
				}
				sb.append(isCSV ? "\r\n" : "</tr>");
			}
		}

		if (!isCSV) {
			sb.append("</table>");
		}

		if (isCSV) {
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=query" + (params.get("index"))+ ".csv");
			response.setContentType("text/csv");
			response.getWriter().write(sb.toString());
		}
		else {
			ServletHelper.writeText(response, sb.toString());
		}
	}
	
	private static void mergeEntity(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		String msg = "";
		try {
			String alias = String.valueOf(params.get("alias"));
			Integer id1 = StringUtils.toInt(params.get("id1"));
			Integer id2 = StringUtils.toInt(params.get("id2"));
			Object o1 = DatabaseManager.loadEntity(DatabaseManager.getClassFromAlias(alias), id1);
			Object o2 = DatabaseManager.loadEntity(DatabaseManager.getClassFromAlias(alias), id2);
			if (params.containsKey("confirm")) {
				msg = ResourceUtils.getText("confirm.merge", lang).replaceFirst("\\{1\\}", "<br/><b>" + o1.toString() + "</b><br/><br/>").replaceFirst("\\{2\\}", "<br/><b>" + o2.toString() + "</b>");
			}
			else {
				DatabaseManager.executeSelect("select _merge('" + alias + "', " + id1 + ", " + id2 + ")");
				msg = ResourceUtils.getText("merge.success", lang).replaceFirst("\\{1\\}", String.valueOf(id1)).replaceFirst("\\{2\\}", String.valueOf(id2));
			}
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			msg = "ERR:" + e.getMessage();
		}
		finally {
			ServletHelper.writeText(response, msg);
		}
	}
	
	private static void loadResult(HttpServletRequest request, HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		Object tp = params.get("tp");
		request.setAttribute("title", StringUtils.getTitle(ResourceUtils.getText("update.results", lang)));
		String p = String.valueOf(params.get("p"));
		p = StringUtils.decode(p);
		Object[] t = p.split("\\-");
		Result rs = null;
		Year yr = null;
		List<Result> lResult = null;

		if (tp != null) {
			String where = null;
			if (String.valueOf(tp).equalsIgnoreCase("direct")) {
				where = "T.id = " + params.get("id");
			}
			else if (String.valueOf(tp).equalsIgnoreCase("year") && StringUtils.notEmpty(params.get("yrfind"))) {
				where = "YR.label='" + params.get("yrfind") + "' AND id_sport = " + params.get("sp") + " AND id_championship = " + params.get("cp") + " AND id_event = " + params.get("ev") + (StringUtils.notEmpty(params.get("se")) ? " AND id_subevent = " + params.get("se") : "") + (StringUtils.notEmpty(params.get("se2")) ? " AND id_subevent2 = " + params.get("se2") : "");
			}
			else if (String.valueOf(tp).matches("first|last")) {
				where = "id_sport = " + params.get("sp") + " AND id_championship = " + params.get("cp") + " AND id_event = " + params.get("ev") + (StringUtils.notEmpty(params.get("se")) ? " AND id_subevent = " + params.get("se") : "") + (StringUtils.notEmpty(params.get("se2")) ? " AND id_subevent2 = " + params.get("se2") : "") + " ORDER BY id_year " + (tp.equals("first") ? "ASC" : "DESC");
			}
			else if (StringUtils.notEmpty(params.get("yr"))) {
				where = "id_year " + (tp.equals("next") ? ">" : "<") + " " + params.get("yr") + " AND id_sport = " + params.get("sp") + " AND id_championship = " + params.get("cp") + " AND id_event = " + params.get("ev") + (StringUtils.notEmpty(params.get("se")) ? " AND id_subevent = " + params.get("se") : "") + (StringUtils.notEmpty(params.get("se2")) ? " AND id_subevent2 = " + params.get("se2") : "") + " ORDER BY id_year " + (tp.equals("next") ? "ASC" : "DESC");
			}
			if (where != null) {
				lResult = (List<Result>) DatabaseManager.executeSelect(Result.query + " WHERE " + where, Result.class);
				if (lResult != null && !lResult.isEmpty()) {
					rs = lResult.get(0);
				}
			}
			if (rs != null) {
				yr = rs.getYear();
				t = new Object[3 + (rs.getSubevent() != null ? 1 : 0) + (rs.getSubevent2() != null ? 1 : 0)];
				t[0] = rs.getSport().getId();
				t[1] = rs.getChampionship().getId();
				t[2] = rs.getEvent().getId();
				if (rs.getSubevent() != null) {
					t[3] = rs.getSubevent().getId();
				}
				if (rs.getSubevent2() != null) {
					t[4] = rs.getSubevent2().getId();
				}
			}
			else {
				t = null;
			}
		}

		if (t != null) {
			if (t[0].equals(Result.alias)) {
				rs = (Result)DatabaseManager.loadEntity(Result.class, t[1]);
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
				Sport sp = (Sport)DatabaseManager.loadEntity(Sport.class, t[0]);
				Championship cp = (Championship)DatabaseManager.loadEntity(Championship.class, t[1]); 
				ev = (Event)DatabaseManager.loadEntity(Event.class, t[2]);
				se = (Event)DatabaseManager.loadEntity(Event.class, t.length > 3 ? t[3] : 0);
				se2 = (Event)DatabaseManager.loadEntity(Event.class, t.length > 4 ? t[4] : 0);
				if (yr == null) {
					yr = (Year)DatabaseManager.loadEntity("SELECT * FROM year where label = ?", Arrays.asList(Calendar.getInstance().get(Calendar.YEAR)), Year.class);
				}
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
				sb.append(rs.getExa()).append("~").append(rs.getComment()).append("~");
				sb.append(ResourceUtils.getText("metadata", lang).replaceFirst("\\{1\\}", StringUtils.toTextDate(rs.getMetadata().getFirstUpdate(), lang, "dd/MM/yyyy HH:mm")).replaceFirst("\\{2\\}", StringUtils.toTextDate(rs.getMetadata().getLastUpdate(), lang, "dd/MM/yyyy HH:mm")).replaceFirst("\\{3\\}", "<a target='_blank' href='" + HtmlUtils.writeLink(Contributor.alias, rs.getMetadata().getContributor().getId(), null, rs.getMetadata().getContributor().getLogin()) + "'>" + rs.getMetadata().getContributor().getLogin() + "</a>")).append("~");
				// Inactive item?
				String sql = "SELECT * FROM _inactive_item WHERE id_sport = ? and id_championship = ? and id_event = ?"
						+ (rs.getSubevent() != null ? " and id_subevent = " + rs.getSubevent().getId() : "")
						+ (rs.getSubevent2() != null ? " and id_subevent2 = " + rs.getSubevent2().getId() : "");
				Object inact = DatabaseManager.loadEntity(sql, Arrays.asList(rs.getSport().getId(), rs.getChampionship().getId(), rs.getEvent().getId()), InactiveItem.class);
				sb.append(inact != null ? "1" : "0").append("~");
				// In progress
				sb.append(rs.getInProgress() != null && rs.getInProgress() ? "1" : "0").append("~");
				// No_date / No_place
				sb.append(rs.getNoDate() != null && rs.getNoDate() ? "1" : "0").append("~");
				sb.append(rs.getNoPlace() != null && rs.getNoPlace() ? "1" : "0").append("~");
				// External links
				StringBuffer sbLinks = new StringBuffer();
				try {
					List<ExternalLink> list = (List<ExternalLink>) DatabaseManager.executeSelect("SELECT * FROM _external_link WHERE entity = ? and id_item = ? ORDER BY id", Arrays.asList(Result.alias, rs.getId()), ExternalLink.class);
					for (ExternalLink link : list) {
						sbLinks.append(link.getUrl()).append("|");
					}
				}
				catch (Exception e_) {
					log.log(Level.WARNING, e_.getMessage(), e_);
				}
				sb.append(sbLinks.toString()).append("~");
				Integer n = ev.getType().getNumber();
				if (se != null) {
					n = se.getType().getNumber();
				}
				if (se2 != null) {
					n = se2.getType().getNumber();
				}
				// Rankings
				for (int i = 1 ; i <= MAX_RANKS ; i++) {
					Integer id = null;
					String rank = null;
					String result = null;
					Method m = Result.class.getMethod("getIdRank" + i);
					Object o = m.invoke(rs);
					if (o != null) {
						id = (Integer) o;
					}
					if (id != null && id > 0) {
						rank = getEntityLabel(n, id, lang);
						m = Result.class.getMethod("getResult" + i);
						o = m.invoke(rs);
						if (o != null) {
							result = String.valueOf(o);
						}
					}
					sb.append(id != null ? id : "").append("~");
					sb.append(rank != null ? rank : "").append("~");
					sb.append(result != null ? result : "").append("~");
				}
				// Person List
				List<PersonList> lPList = (List<PersonList>) DatabaseManager.executeSelect("SELECT * FROM _person_list WHERE id_result = ? ORDER BY id", Arrays.asList(rs.getId()), PersonList.class);
				if (lPList != null && lPList.size() > 0) {
					List<String> l = new ArrayList<String>();
					for (PersonList pl : (List<PersonList>) lPList) {
						int rk = pl.getRank();
						if (l.size() < rk) {
							l.add("");
						}
						Athlete a = (Athlete) DatabaseManager.loadEntity(Athlete.class, pl.getIdPerson());
						l.set(rk - 1, (StringUtils.notEmpty(l.get(rk - 1)) ? l.get(rk - 1) + "|" : "") + pl.getId() + ":" + pl.getIdPerson() + ":" + a.toString2() + ":" + (StringUtils.notEmpty(pl.getIndex()) ? pl.getIndex() : ""));
					}
					sb.append("rkl-" + StringUtils.join(l, "#")).append("~");
				}
				// Rounds
				List<Round> lRounds = (List<Round>) DatabaseManager.executeSelect(Round.query + " WHERE id_result = ? ORDER BY RT.index, RT.label, T.id", Arrays.asList(rs.getId()), Round.class);
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
						if (rd.getIdRank4() != null) {
							l.add(String.valueOf(rd.getIdRank4()));
							l.add(getEntityLabel(rd.getIdResultType(), rd.getIdRank4(), lang));
							l.add(StringUtils.notEmpty(rd.getResult4()) ? rd.getResult4() : "");
						}
						else {
							l.add("");
							l.add("");
							l.add("");
						}
						if (rd.getIdRank5() != null) {
							l.add(String.valueOf(rd.getIdRank5()));
							l.add(getEntityLabel(rd.getIdResultType(), rd.getIdRank5(), lang));
							l.add(StringUtils.notEmpty(rd.getResult5()) ? rd.getResult5() : "");
						}
						else {
							l.add("");
							l.add("");
							l.add("");
						}
						l.add(StringUtils.notEmpty(rd.getDate()) ? rd.getDate() : "");
						l.add(StringUtils.notEmpty(rd.getDate1()) ? rd.getDate1() : "");
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
					for (Result rs_ : lResult) {
						if (rs != null && rs.getYear().getId().equals(rs_.getYear().getId())) {
							sb_.append(sb_.length() > 0 ?  "," : "").append(rs_.getId());
						}
					}
					sb.append(sb_).append("~");
				}
				else {
					sb.append("~");
				}
			}
			String result = sb.toString().replaceAll("~null~", "~~").replaceAll("~null~", "~~").replaceAll("\"", "\\\\\"");
			if (tp != null) {
				ServletHelper.writeText(response, result);
			}
			else {
				request.setAttribute("value", result);
				request.getRequestDispatcher("/jsp/update/results.jsp").forward(request, response);			
			}
		}
		else {
			ServletHelper.writeText(response, "");
		}
	}
	
	private static void loadEntity(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		HashMap<String, Short> hLocs = new HashMap<String, Short>();
		hLocs.put("first", DatabaseManager.FIRST);
		hLocs.put("previous", DatabaseManager.PREVIOUS);
		hLocs.put("next", DatabaseManager.NEXT);
		hLocs.put("last", DatabaseManager.LAST);
		String action = String.valueOf(params.get("action"));
		String id = String.valueOf(params.get("id"));
		String alias = String.valueOf(params.get("alias"));
		String filter = (alias.matches(Athlete.alias + "|" + Team.alias) ? (cb != null && !cb.isAdmin() ? "id_sport in (" + cb.getSports() + ")" : null) : null);
		if (StringUtils.notEmpty(params.get("sp"))) {
			filter = (filter != null ? filter + " and " : "") + "id_sport=" + params.get("sp");
		}
		Class<?> c = DatabaseManager.getClassFromAlias(alias);
		Object o = (action.equals("direct") ? DatabaseManager.loadEntity(c, id) : DatabaseManager.move(c, id, hLocs.get(action), filter));
		StringBuffer sb = new StringBuffer();
		if (o != null) {
			id = String.valueOf(c.getMethod("getId").invoke(o, new Object[0]));
			String exl = null;
			if (alias.matches(Athlete.alias + "|" + Championship.alias + "|" + City.alias + "|" + Complex.alias + "|" + Country.alias + "|" + Event.alias + "|" + Olympics.alias + "|" + Sport.alias + "|" + State.alias + "|" + Team.alias)) {
				StringBuffer sbexl = new StringBuffer();
				for (ExternalLink exl_ : (Collection<ExternalLink>) DatabaseManager.executeSelect("SELECT * FROM _external_link WHERE entity = ? AND id_item = ? ORDER BY id", Arrays.asList(alias, Integer.valueOf(id)), ExternalLink.class)) {
					sbexl.append(exl_.getUrl()).append("\r\n");
				}
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
				if (at.getLink() != null && at.getLink() > 0) {
					try {
						Athlete a = (Athlete) DatabaseManager.loadEntity(Athlete.class, at.getLink());
						sb.append(at.getLink() != null ? at.getLink() : 0).append("~");
						sb.append(a.toString2()).append("~");
					}
					catch (Exception e) {
						log.log(Level.WARNING, e.getMessage());
					}
				}
				else if (at.getLink() != null && at.getLink() == 0) {
					sb.append("0~[root]~");
				}
				else {
					sb.append("~~");
				}
				sb.append(exl).append("~");
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
				if (ct.getLink() != null && ct.getLink() > 0) {
					try {
						City c_ = (City) DatabaseManager.loadEntity(City.class, ct.getLink());
						sb.append(ct.getLink() != null ? ct.getLink() : 0).append("~");
						sb.append(c_.toString2(lang)).append("~");
					}
					catch (Exception e) {
						log.log(Level.WARNING, e.getMessage());
					}
				}
				else if (ct.getLink() != null && ct.getLink() == 0) {
					sb.append("0~[root]~");
				}
				else {
					sb.append("~~");
				}
				sb.append(exl).append("~");
			}
			else if (o instanceof Complex) {
				Complex cx = (Complex) o;
				sb.append(cx.getLabel()).append("~");
				sb.append(cx.getCity() != null ? cx.getCity().getId() : 0).append("~");
				sb.append(cx.getCity() != null ? cx.getCity().toString2(lang) : "").append("~");
				if (cx.getLink() != null && cx.getLink() > 0) {
					try {
						Complex c_ = (Complex) DatabaseManager.loadEntity(Complex.class, cx.getLink());
						sb.append(cx.getLink() != null ? cx.getLink() : 0).append("~");
						sb.append(c_.toString2(lang)).append("~");
					}
					catch (Exception e) {
						log.log(Level.WARNING, e.getMessage());
					}
				}
				else if (cx.getLink() != null && cx.getLink() == 0) {
					sb.append("0~[root]~");
				}
				else {
					sb.append("~~");
				}
				sb.append(exl).append("~");
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
						Team t = (Team) DatabaseManager.loadEntity(Team.class, tm.getLink());
						sb.append(tm.getLink() != null ? tm.getLink() : 0).append("~");
						sb.append(t.toString()).append("~");
					}
					catch (Exception e) {
						log.log(Level.WARNING, e.getMessage());
					}
				}
				else if (tm.getLink() != null && tm.getLink() == 0) {
					sb.append("0~[root]~");
				}
				else {
					sb.append("~~");
				}
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
				if (StringUtils.notEmpty(rc.getType1())) {
					n = (rc.getType1().equalsIgnoreCase("individual") ? 1 : 50);
				}
				else if (rc.getSubevent() != null) {
					n = rc.getSubevent().getType().getNumber();
				}
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
		}
		ServletHelper.writeText(response, sb.toString());
	}
	
	private static void setOverviewFlag(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		try {
			String flag = String.valueOf(params.get("flag"));
			String id = String.valueOf(params.get("id"));
			Result r = (Result) DatabaseManager.loadEntity(Result.class, id);
			if (r != null) {
				if (flag.equals("no_date")) {
					r.setNoDate(true);
				}
				else {
					r.setNoPlace(true);
				}
				DatabaseManager.saveEntity(r, cb);
			}
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	private static void deleteEntity(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			String id = String.valueOf(params.get("id"));
			String alias = String.valueOf(params.get("alias"));
			Class<?> c = DatabaseManager.getClassFromAlias(alias);
			DatabaseManager.removeEntity(DatabaseManager.loadEntity(c, StringUtils.toInt(id)));
			sbMsg.append(ResourceUtils.getText("delete.ok", lang) + " " + StringUtils.SEP1 + " " + ResourceUtils.getText("entity." + alias + ".1", lang) + " #" + id);
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void executeImport(HttpServletRequest request, HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		Object file = params.get("file");
		if (StringUtils.notEmpty(file)) {
			FileInputStream fis = new FileInputStream(ConfigUtils.getProperty("temp.folder") + file);
			byte[] b = IOUtils.toByteArray(fis);
			response.setHeader("Content-Disposition", "attachment;filename=" + file);
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(new String(b));
			response.flushBuffer();
			fis.close();
		}
		else {
			IMPORT_PROGRESS = new ImportUtils.Progress();
			InputStream input = null;
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			Collection<FileItem> items = upload.parseRequest(request);
			for (FileItem fitem : items) {
				input = fitem.getInputStream();
			}
			Vector<Vector<String>> v = new Vector<>();
			BufferedReader bf = new BufferedReader(new InputStreamReader(input, "UTF8"));
			StringBuffer sb = new StringBuffer();
			String s = null;
			while ((s = bf.readLine()) != null) {
				if (StringUtils.notEmpty(s)) {
					Vector<String> v_ = new Vector<>();
					for (String s_ : s.split(";", -1)) {
						v_.add(s_.trim());
					}
					v.add(v_);
					sb.append(s).append("\r\n");
				}
			}
			String type = String.valueOf(params.get("type"));
			String update = String.valueOf(params.get("update"));
			String result = ImportUtils.processAll(IMPORT_PROGRESS,  v, update.equals("1"), type.equalsIgnoreCase(Result.alias), type.equalsIgnoreCase(Round.alias), type.equalsIgnoreCase(Record.alias), cb, lang);
			IMPORT_PROGRESS.percent = 100;
			if (update.equals("1")) {
				try {
					String f = "import" + System.currentTimeMillis() + ".html";
					String css = "<style>table{border-collapse: collapse;}th,td{white-space: nowrap;border: 1px solid gray;padding: 2px;}div{margin-top: 10px;font-style: italic;}</style>";
					FileOutputStream fos = new FileOutputStream(ConfigUtils.getProperty("temp.folder") + f);
					fos.write((css + result).getBytes());
					fos.close();
					result = f;
					Import i = new Import();
					i.setDate(new Timestamp(System.currentTimeMillis()));
					i.setCsvContent(sb.toString());
					DatabaseManager.saveEntity(i, null);	
				}
				catch (Exception e) {
					log.log(Level.WARNING, e.getMessage(), e);
				}
			}
			ServletHelper.writeText(response, result);
		}
	}
	
	private static void loadTemplate(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		try {
			String type = String.valueOf(params.get("type"));
			String ext = String.valueOf(params.get("ext"));
			List<List<String>> list = ImportUtils.getTemplate(type, lang);
			List<List<String>> lTh = new ArrayList<>();
			List<List<String>> lTd = new ArrayList<>();
			StringBuffer sb = new StringBuffer();
			for (List<String> list_ : list) {
				int i = 0;
				List<String> lTh_ = new ArrayList<String>();
				List<String> lTd_ = new ArrayList<String>();
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
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	private static void loadExternalLinks(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		try {
			StringBuffer html = new StringBuffer("<table>");
			String sport = String.valueOf(params.get("sport"));
			String count = String.valueOf(params.get("count") != null ? params.get("count") : 100);
			String idmax = String.valueOf(params.get("idmax") != null ? params.get("idmax") : 100000);
			String pattern = String.valueOf(params.get("pattern"));
			String entity = String.valueOf(params.get("entity"));
			String includechecked = String.valueOf(params.get("includechecked"));
			
			StringBuffer where = new StringBuffer(" WHERE T.id <= " + idmax);
			String joins = "";
			where.append(entity.matches(Athlete.alias + "|" + Team.alias + "|" + Sport.alias) ? (cb != null && !cb.isAdmin() ? " AND " + (entity.equalsIgnoreCase(Sport.alias) ? "T.id" : "T.id_sport") + " in (" + cb.getSports() + ")" : "") : "");
			String label_ = "T.label";
			if (entity.equalsIgnoreCase(Athlete.alias)) {
				label_ = "last_name || ', ' || first_name || ' - ' || SP.label";
				joins = " JOIN sport SP ON SP.id = T.id_sport ";
			}
			else if (entity.equalsIgnoreCase(City.alias)) {
				label_ = "T.label || '<i> - ' || CN.code || '</i>', T.label_fr";
				joins = " JOIN country CN ON CN.id = T.id_country ";
			}
			else if (entity.equalsIgnoreCase(Complex.alias)) {
				label_ = "T.label || '<i> - ' || CT.label || ', ' || CN.code || '</i>'";
				joins = " JOIN city CT ON CT.id = T.id_city JOIN country CN ON CN.id = CT.id_country ";
			}
			else if (entity.equalsIgnoreCase(Olympics.alias)) {
				label_ = "CT.label || ' ' || YR.label";
				joins = " JOIN city CT ON CT.id = T.id_city JOIN year YR ON YR.id = T.id_year ";
			}
			else if (entity.equalsIgnoreCase(Team.alias)) {
				label_ = "T.label || ' - ' || SP.label";
				joins = " JOIN sport SP ON SP.id = T.id_sport ";
			}
			if (StringUtils.notEmpty(pattern)) {
				where.append(" AND LOWER(" + label_ + ") LIKE '%" + pattern.toLowerCase() + "%'");
			}
			if (entity.matches(Athlete.alias + "|" + Team.alias) && !sport.equals("0")) {
				where.append(" AND T.id_sport = " + sport);
			}
			Class<?> c = DatabaseManager.getClassFromAlias(entity);
			Collection<Object[]> items = DatabaseManager.executeSelect("SELECT T.id, " + label_ + " FROM " + DatabaseManager.getTable(c) + " T " + joins + where.toString() + " ORDER BY T.id desc LIMIT " + Integer.parseInt(count));
			
			List<ExternalLink> links = (List<ExternalLink>) DatabaseManager.executeSelect("SELECT * FROM _external_link WHERE entity = ? ORDER BY entity, id_item", Arrays.asList(entity), ExternalLink.class);
			html.append("<thead><th>ID</th><th>" + ResourceUtils.getText("label", lang) + "</th><th>" + ResourceUtils.getText("type", lang) + "</th><th>URL</th><th>" + ResourceUtils.getText("checked", lang) + " <input type='checkbox' onclick='checkAllLinks();'/></th></thead><tbody>");
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
						html.append("<td>" + (StringUtils.notEmpty(el.getFlag()) ? el.getFlag() : "") + "</td>");
						html.append("<td><a href='" + el.getUrl() + "' target='_blank'>" + el.getUrl() + "</a></td>");
						html.append("<td><input type='checkbox'" + (el.isChecked() ? " checked='checked'" : "") + "/></td>");
						html.append("<td><a href='javascript:addExtLink(" + el.getId() + ");'><img alt='' src='/img/component/button/add.png'/></a></td>");
						html.append("<td><a href='javascript:modifyExtLink(" + el.getId() + ");'><img alt='' src='/img/component/button/modify.png'/></a></td>");
						html.append("<td><a href='javascript:removeExtLink(" + el.getId() + ");'><img alt='' src='/img/component/button/delete.png'/></a></td></tr>");
					}
					else if (isLink) {
						break;
					}
				}
				if (!isLink && !isExclude) {
					html.append("<tr><td style='display:none;'>0</td>");
					html.append("<td>" + id + "</td>");
					html.append("<td>" + label + "</td>");
					html.append("<td><input type='text' style='width:60px;'/></td>");
					html.append("<td><input type='text' style='width:500px;'/></td>");
					html.append("<td><input type='checkbox'/></td><td></td></tr>");
				}
			}
			ServletHelper.writeText(response, html.append("</tbody></table>").toString());
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	private static void saveExternalLinks(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			String entity = String.valueOf(params.get("entity"));
			String value = String.valueOf(params.get("value"));
			for (String s : value.split("\\|")) {
				String[] t = s.split("\\~");
				String id = t[0];
				ExternalLink el = (id.equals("0") ? new ExternalLink() : (ExternalLink) DatabaseManager.loadEntity(ExternalLink.class, id));
				if (t.length > 2) {
					el.setEntity(entity);
					el.setIdItem(Integer.parseInt(t[1]));
					el.setUrl(t[2]);
					el.setChecked(t[3].equals("1"));
					DatabaseManager.saveEntity(el, null);
				}
				else if (t.length == 2 && el.getId() != null) {
					el.setChecked(t[1].equals("1"));
					DatabaseManager.saveEntity(el, null);
				}
			}
			sbMsg.append(ResourceUtils.getText("update.ok", lang));
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void deleteExternalLink(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		try {
			String id = String.valueOf(params.get("id"));
			ExternalLink el = (ExternalLink) DatabaseManager.loadEntity(ExternalLink.class, id);
			if (el != null) {
				DatabaseManager.removeEntity(el);
			}
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	private static void updateAutoExternalLinks(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
//			System.getProperties().setProperty("http.proxyHost", "XXXX");
//			System.getProperties().setProperty("http.proxyPort", "XXXX");
			Integer count = Integer.parseInt(String.valueOf(params.get("count") != null ? params.get("count") : 100));
			Integer idmax = Integer.parseInt(String.valueOf(params.get("idmax") != null ? params.get("idmax") : 100000));
			String sport = String.valueOf(params.get("sport"));
			String entity = String.valueOf(params.get("entity"));
			StringBuffer sbUpdateSql = new StringBuffer();
			List<String> lSql = new LinkedList<String>();
			if (entity.equalsIgnoreCase(Athlete.alias)) {
				lSql.add("SELECT * FROM athlete where" + (!sport.equals("0") ? " sport.id=" + sport + " and" : "") + " id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Athlete.alias + "' and url like '%wikipedia%') ORDER BY id desc");
				lSql.add("SELECT * FROM athlete WHERE" + (!sport.equals("0") ? " sport.id=" + sport + " and" : "") + " id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Athlete.alias + "' and url like '%basketball-reference%') ORDER BY id desc");
				lSql.add("SELECT * FROM athlete WHERE" + (!sport.equals("0") ? " sport.id=" + sport + " and" : "") + " id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Athlete.alias + "' and url like '%pro-football-reference%') ORDER BY id desc");
				lSql.add("SELECT * FROM athlete WHERE" + (!sport.equals("0") ? " sport.id=" + sport + " and" : "") + " id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Athlete.alias + "' and url like '%hockey-reference%') ORDER BY id desc");
				lSql.add("SELECT * FROM athlete WHERE" + (!sport.equals("0") ? " sport.id=" + sport + " and" : "") + " id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Athlete.alias + "' and url like '%baseball-reference%') ORDER BY id desc");
			}
			if (entity.equalsIgnoreCase(Championship.alias)) {
				lSql.add("SELECT * FROM championship WHERE id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Championship.alias + "' and url like '%wikipedia%') ORDER BY id desc");
			}
			if (entity.equalsIgnoreCase(City.alias)) {
				lSql.add("SELECT * FROM city WHERE id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + City.alias + "' and url like '%wikipedia%') ORDER BY id desc");
			}
			if (entity.equalsIgnoreCase(Complex.alias)) {
				lSql.add("SELECT * FROM complex WHERE id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Complex.alias + "' and url like '%wikipedia%') ORDER BY id desc");
			}
			if (entity.equalsIgnoreCase(Country.alias)) {
				lSql.add("SELECT * FROM country WHERE id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Country.alias + "' and url like '%wikipedia%') ORDER BY id desc");
			}
			if (entity.equalsIgnoreCase(Event.alias)) {
				lSql.add("SELECT * FROM event WHERE id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Event.alias + "' and url like '%wikipedia%') ORDER BY id desc");
			}
			if (entity.equalsIgnoreCase(Olympics.alias)) {
				lSql.add("SELECT * FROM olympics WHERE id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Olympics.alias + "' and url like '%wikipedia%') ORDER BY id desc");
			}
			if (entity.equalsIgnoreCase(Sport.alias)) {
				lSql.add("SELECT * FROM sport WHERE id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Sport.alias + "' and url like '%wikipedia%') ORDER BY id desc");
			}
			if (entity.equalsIgnoreCase(State.alias)) {
				lSql.add("SELECT * FROM state WHERE id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + State.alias + "' and url like '%wikipedia%') ORDER BY id desc");
			}
			if (entity.equalsIgnoreCase(Team.alias)) {
				lSql.add("SELECT * FROM team WHERE" + (!sport.equals("0") ? " sport.id=" + sport + " and" : "") + " id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Team.alias + "' and url like '%wikipedia%') ORDER BY id desc");
				lSql.add("SELECT * FROM team WHERE" + (!sport.equals("0") ? " sport.id=" + sport + " and" : "") + " id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Team.alias + "' and url like '%basketball-reference%') ORDER BY id desc");
				lSql.add("SELECT * FROM team WHERE" + (!sport.equals("0") ? " sport.id=" + sport + " and" : "") + " id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Team.alias + "' and url like '%pro-football-reference%') ORDER BY id desc");
				lSql.add("SELECT * FROM team WHERE" + (!sport.equals("0") ? " sport.id=" + sport + " and" : "") + " id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Team.alias + "' and url like '%hockey-reference%') ORDER BY id desc");
				lSql.add("SELECT * FROM team WHERE" + (!sport.equals("0") ? " sport.id=" + sport + " and" : "") + " id <= " + idmax + " AND id NOT IN (SELECT id_item FROM _external_link WHERE entity = '" + Team.alias + "' and url like '%baseball-reference%') ORDER BY id desc");	
			}
			for (String sql : lSql) {
				List<Object> l = (List<Object>) DatabaseManager.executeSelect(sql + " LIMIT " + count, DatabaseManager.getClassFromAlias(entity));
				for (Object o : l) {
					sbUpdateSql.append(getExternalLink(o, sql));
				}
			}
			DatabaseManager.executeUpdate(sbUpdateSql.toString(), null);
			sbMsg.append(ResourceUtils.getText("update.ok", lang));
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void loadTranslations(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		try {
			StringBuffer html = new StringBuffer("<table>");
			String range = String.valueOf(params.get("range"));
			String pattern = String.valueOf(params.get("pattern"));
			String entity = String.valueOf(params.get("entity"));
			String includechecked = String.valueOf(params.get("includechecked"));
			
			String[] tIds = range.split("\\-");
			StringBuffer sql = new StringBuffer("SELECT * FROM _translation WHERE entity = ?");
			if (tIds.length > 1 && pattern.length() == 0) {
				sql.append(" and id_item BETWEEN " + tIds[0] + " AND " + tIds[1]);
			}
			sql.append(" ORDER BY id_item");

			String sql_ = null;
			if (entity.equalsIgnoreCase(City.alias)) {
				sql_ = "SELECT T.id, T.label || '<i> - ' || CN.code || '</i>', T.label_fr FROM city T JOIN country CN ON CN.id = T.id_country ";
			}
			else if (entity.equalsIgnoreCase(Complex.alias)) {
				sql_ = "SELECT T.id, T.label || '<i> - ' || CT.label || ', ' || CN.code || '</i>' FROM complex T JOIN city CT ON CT.id = T.id_city JOIN country CN ON CN.id = CT.id_country ";
			}
			else  {
				String table = (String) DatabaseManager.getClassFromAlias(entity).getField("table").get(null);
				sql_ = "SELECT T.id, T.label, T.label_fr FROM " + table + " T";
			}
			sql_ += " WHERE 1 = 1" + (tIds.length > 1 ? " AND T.id BETWEEN " + tIds[0] + " AND " + tIds[1] : "") + 
					(StringUtils.notEmpty(pattern) ? " and (LOWER(T.label) LIKE '" + pattern + "%' OR LOWER(T.label_fr) LIKE '" + pattern + "%')" : "") + " ORDER BY T.id";
			List<Object[]> items = (List<Object[]>) DatabaseManager.executeSelect(sql_);
			List<Translation> translations = (List<Translation>) DatabaseManager.executeSelect(sql.toString(), Arrays.asList(entity), Translation.class);
			html.append("<thead><th>ID</th><th>" + ResourceUtils.getText("label", lang) + " (EN)</th><th>" + ResourceUtils.getText("label", lang) + " (FR)</th><th>" + ResourceUtils.getText("checked", lang) + " <input type='checkbox' onclick='checkAllTranslations();'/></th></thead><tbody>");
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
					else if (isTranslation) {
						break;
					}
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
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	private static void saveTranslations(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			String entity = String.valueOf(params.get("entity"));
			String value = String.valueOf(params.get("value"));
			for (String s : value.split("\\|")) {
				String[] t = s.split("\\~");
				String id = t[0];
				Translation tr = (id.equals("0") ? new Translation() : (Translation) DatabaseManager.loadEntity(Translation.class, id));
				if (tr.getId() == null) {
					tr.setIdItem(Integer.parseInt(t[1]));
					tr.setEntity(entity);
				}
				tr.setChecked(t[2].equals("1"));
				DatabaseManager.saveEntity(tr, null);
			}
			sbMsg.append(ResourceUtils.getText("update.ok", lang));
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void loadErrors(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		try {
			StringBuffer html = new StringBuffer("<table>");
			html.append("<thead><th>URL</th><th>Text</th><th>" + ResourceUtils.getText("date", lang) + "</th></thead><tbody>");
			for (ErrorReport er : (List<ErrorReport>) DatabaseManager.executeSelect("SELECT * FROM _error_report ORDER BY date DESC", ErrorReport.class)) {
				String url = er.getUrl().replaceFirst("http\\:\\/\\/", "");
				html.append("<tr><td style='display:none;'>0</td>");
				html.append("<td><a href='http://" + url + "' target='_blank'>" + url.substring(url.indexOf("/")) + "</a></td>");
				html.append("<td>" + er.getText().replaceAll("\\||\r\n|\n", "<br/>") + "</td>");
				html.append("<td>" + StringUtils.toTextDate(er.getDate(), lang, null) + "</td>");
				html.append("<td><a href='javascript:removeError(" + er.getId() + ");'><img alt='' src='/img/component/button/delete.png'/></a></td></tr>");
			}
			ServletHelper.writeText(response, html.append("</tbody></table>").toString());
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	private static void removeError(HttpServletResponse response, Map<?, ?> params) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			Object value = params.get("value");
			if (StringUtils.notEmpty(value)) {
				DatabaseManager.removeEntity(DatabaseManager.loadEntity(ErrorReport.class, Integer.parseInt(String.valueOf(value))));
			}
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static void loadRedirections(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		try {
			StringBuffer html = new StringBuffer("<table>");
			html.append("<thead><th>Previous path</th><th>Current path</th></thead><tbody>");
			for (Redirection re : (List<Redirection>) DatabaseManager.executeSelect("SELECT * FROM _redirection ORDER BY id", Redirection.class)) {
				html.append("<tr id='re-" + re.getId() + "'><td style='display:none;'>" + re.getId() + "</td>");
				html.append("<td><input type='text' value='" + re.getPreviousPath() + "' style='width:450px;'/></td>");
				html.append("<td><input type='text' value='" + re.getCurrentPath() + "' style='width:450px;'/></td>");
				html.append("<td><a href='javascript:addRedirection(" + re.getId() + ");'><img alt='' src='/img/component/button/add.png'/></a></td></tr>");
			}
			ServletHelper.writeText(response, html.append("</tbody></table>").toString());
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	private static void saveRedirections(HttpServletResponse response, Map<?, ?> params, String lang, Contributor cb) throws Exception {
		StringBuffer sbMsg = new StringBuffer();
		try {
			String value = String.valueOf(params.get("value"));
			for (String s : value.split("\\|")) {
				String[] t = s.split("\\~");
				String id = t[0];
				Redirection re = (id.equals("0") ? new Redirection() : (Redirection) DatabaseManager.loadEntity(Redirection.class, id));
				re.setPreviousPath(t[1]);
				re.setCurrentPath(t[2]);
				DatabaseManager.saveEntity(re, null);
			}
			sbMsg.append(ResourceUtils.getText("update.ok", lang));
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			sbMsg.append("ERR:" + e.getMessage());
		}
		finally {
			ServletHelper.writeText(response, sbMsg.toString());
		}
	}
	
	private static String getEntityLabel(int n, Integer id, String lang) throws Exception {
		String label = null;
		if (n < 10) {
			Athlete a = (Athlete) DatabaseManager.loadEntity(Athlete.class, id);
			if (a != null) {
				label = a.toString2();	
			}
		}
		else if (n == 50) {
			Team t_ = (Team) DatabaseManager.loadEntity(Team.class, id);
			if (t_ != null) {
				label = t_.getLabel() + (t_.getCountry() != null ? " (" + t_.getCountry().getCode() + ")" : "");
			}
		}
		else {
			Country c = (Country) DatabaseManager.loadEntity(Country.class, id);
			if (c != null) {
				label = c.getLabel(lang);
			}
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
		// WIKIPEDIA
		if (hql.matches(".*wikipedia.*")) {
			url = "https://en.wikipedia.org/wiki/" + URLEncoder.encode(str1.replaceAll("\\s", "_"), "utf-8");
			sql.append("insert into _external_link (select nextval('_s_external_link'), '" + alias + "', " + id + ", '" + url + "', FALSE, NULL);\r\n");
		}
		// BASKETBALL-REFERENCE
		if (hql.matches(".*basketball\\-reference.*")) {
			url = null;
			if (o instanceof Athlete && ((Athlete)o).getSport().getId() == 24) {
				url = "http://www.basketball-reference.com/players/" + str2.substring(0, 1).toLowerCase() + "/" + str2.substring(0, str2.length() > 5 ? 5 : str2.length()).toLowerCase() + str1.substring(0, 2).toLowerCase() + "01.html";	
			}
			if (url != null) {
				sql.append("insert into _external_link (select nextval('_s_external_link'), '" + alias + "', " + id + ", '" + StringUtils.normalize(url) + "', FALSE, NULL);\r\n");
			}
		}
		// BASEBALL-REFERENCE
		if (hql.matches(".*baseball\\-reference.*")) {
			url = null;
			if (o instanceof Athlete && ((Athlete)o).getSport().getId() == 26) {
				url = "http://www.baseball-reference.com/players/" + str2.substring(0, 1).toLowerCase() + "/" + str2.substring(0, str2.length() > 5 ? 5 : str2.length()).toLowerCase() + str1.substring(0, 2).toLowerCase() + "01.shtml";	
			}
			if (url != null) {
				sql.append("insert into _external_link (select nextval('_s_external_link'), '" + alias + "', " + id + ", '" + StringUtils.normalize(url) + "', FALSE, NULL);\r\n");
			}
		}
		// PRO-FOOTBALL-REFERENCE
		if (hql.matches(".*pro\\-football\\-reference.*")) {
			url = null;
			if (o instanceof Athlete && ((Athlete)o).getSport().getId() == 23) {
				url = "http://www.pro-football-reference.com/players/" + str2.substring(0, 1) + "/" + str2.substring(0, str2.length() > 4 ? 4 : str2.length()) + str1.substring(0, 2) + "00.htm";	
			}
			if (url != null) {
				sql.append("insert into _external_link (select nextval('_s_external_link'), '" + alias + "', " + id + ", '" + StringUtils.normalize(url) + "', FALSE, NULL);\r\n");
			}
		}
		// HOCKEY-REFERENCE
		if (hql.matches(".*hockey\\-reference.*")) {
			url = null;
			if (o instanceof Athlete && ((Athlete)o).getSport().getId() == 25) {
				url = "http://www.hockey-reference.com/players/" + str2.substring(0, 1).toLowerCase() + "/" + str2.substring(0, str2.length() > 5 ? 5 : str2.length()).toLowerCase() + str1.substring(0, 2).toLowerCase() + "01.html";	
			}
			if (url != null) {
				sql.append("insert into _external_link (select nextval('_s_external_link'), '" + alias + "', " + id + ", '" + StringUtils.normalize(url) + "', FALSE, NULL);\r\n");
			}
		}
		return sql.toString();
	}

}