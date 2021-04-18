package com.sporthenon.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.Type;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.utils.res.ResourceUtils;

@SuppressWarnings("unchecked")
public class ImportUtils {
	
	private static final Logger log = Logger.getLogger(ImportUtils.class.getName());
	
	private static final String scPattern = "[^a-zA-Z0-9\\|\\,\\s\\(\\)_]";

	public static class Progress {
		public int percent;
		public int current;
		public int total;
		
		public Progress() {
			percent = 0;
			current = 0;
			total = 0;
		}

		@Override
		public String toString() {
			return current + "|" + total + "|" + percent;
		}
	};
	
	public static String processAll(Progress progress, Vector<Vector<String>> vFile, boolean isUpdate, boolean isRS, boolean isDR, boolean isRC, Contributor cb, String lang) {
		StringBuffer html = new StringBuffer("<table>");
		StringBuffer report = new StringBuffer();
		try {
			Vector<String> vHeader = null;
			if (isRS)
				vHeader = new Vector<>(Arrays.asList(new String[] {"msg", "sp", "cp", "ev", "se", "se2", "yr", "rk1", "rs1", "rk2", "rs2", "rk3", "rs3", "rk4", "rk5", "rk6", "rk7", "rk8", "rk9", "dt1", "dt2", "pl1", "pl2", "exa", "cmt", "exl1", "exl2", "exl3"}));
			else if (isDR)
				vHeader = new Vector<>(Arrays.asList(new String[] {"msg", "sp", "cp", "ev", "se", "se2", "yr", "qf1w", "qf1r", "qf1s", "qf2w", "qf2r", "qf2s", "qf3w", "qf3r", "qf3s", "qf4w", "qf4r", "qf4s", "sf1w", "sf1r", "sf1s", "sf2w", "sf2r", "sf2s", "thdw", "thdr", "thds"}));
			else if (isRC)
				vHeader = new Vector<>(Arrays.asList(new String[] {"msg", "sp", "cp", "ev", "se", "tp1", "tp2", "label", "rk1", "rc1", "dt1", "rk2", "rc2", "dt2", "rk3", "rc3", "dt3", "rk4", "rc4", "dt4", "rk5", "rc5", "dt5", "idx", "exa", "cmt"}));
			html.append("<tr>");
			for (String s : vHeader)
				html.append("<th>").append(getColumnTitle(s, lang)).append("</th>");
			html.append("</tr>");
			int i = 0;
			int pg = 0;
			progress.total = vFile.size();
			for (Vector<String> v : vFile) {
				v.insertElementAt("-", 0);
				if (isRS)
					processLineRS(i, vHeader, v, isUpdate, report, cb, lang);
//				else if (isDR)
//					processLineDR(i, vHeader, v, isUpdate, report, cb, lang);
				else if (isRC)
					processLineRC(i, vHeader, v, isUpdate, report, cb, lang);
				html.append("<tr><td>").append(StringUtils.join(v, "</td><td>")).append("</td></tr>");
				if (i * 100 / vFile.size() > pg) {
					pg = i * 100 / vFile.size();
					progress.percent = pg;
				}
				progress.current = i;	
				i++;
			}
		}
		catch (Exception e_) {
			log.log(Level.WARNING, e_.getMessage(), e_);
		}
		html.append("</table>");
		if (isUpdate)
			html.append("<div>").append(report.toString()).append("</div>");
		return html.toString();
	}
	
	public static boolean processLineRS(int row, Vector<String> vHeader, Vector<String> vLine, boolean isUpdate, StringBuffer sb, Contributor cb, String lang) throws Exception {
		boolean isError = false;
		List<Integer> lId = null;
		List<Country> lCountries = (List<Country>) DatabaseManager.executeSelect("SELECT * FROM country", Country.class);
		HashMap<String, Integer> hId = new HashMap<>();
		Integer n = null;
		boolean isComplex1 = false;
		boolean isComplex2 = false;
		String tp = null;
		String yr = null;
		for (int i = 0 ; i < vLine.size() ; i++) {
			try {
				String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
				String s = vLine.get(i);
				String sql = null;
				if (StringUtils.notEmpty(s)) {
					if (s.matches(".*\\s\\[#\\d+]$"))
						s = s.substring(0, s.indexOf("[#") - 1);
					String s_ = s.replaceAll(scPattern, "_").toLowerCase();
					String regexp = StringUtils.toPatternString(s_);
					if (h.equalsIgnoreCase(Sport.alias))
						sql = "SELECT T.id FROM sport T WHERE lower(T.label) ~ E'^" + regexp + "$'";
					else if (h.equalsIgnoreCase(Championship.alias))
						sql = "SELECT T.id FROM championship T WHERE lower(T.label) ~ E'^" + regexp + "$'";
					else if (h.matches("ev|se|se2")) {
						String[] tEv = s_.split("\\|");
						sql = "SELECT T.id from event T LEFT JOIN type TP ON T.id_type=TP.id WHERE lower(T.label) ~ E'^" + StringUtils.toPatternString(tEv[0]) + "$'" + (tEv.length > 1 ? " AND lower(TP.label) ~ E'^" + tEv[1] + "$'" : "") + " ORDER BY T.id";
						if (tEv.length > 1)
							tp = tEv[1];
					}
					else if (h.equalsIgnoreCase(Year.alias)) {
						sql = "SELECT T.id FROM year T WHERE lower(T.label) ~ E'^" + regexp + "$'";
						yr = s;
					}
					else if (h.matches("pl\\d")) {
						String[] t = s.toLowerCase().split("\\,\\s");
						if (t.length < 1 || t.length > 4 || !s.toLowerCase().matches(StringUtils.PATTERN_PLACE)) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang)+ ")");
						}
						else {
							String cx = null;
							String ct = null;
							String cn = t[t.length - 1];
							if (t.length > 2) {
								cx = StringUtils.toPatternString(t[0]);
								ct = StringUtils.toPatternString(t[1]);
							}
							else if (t.length > 1)
								ct = StringUtils.toPatternString(t[0]);
							if (cx != null) {
								h = "cx" + h.replaceAll("pl", "");
								sql = "SELECT T.id from complex T LEFT JOIN city CT ON T.id_city=CT.id LEFT JOIN country CN ON CT.id_country=CN.id WHERE lower(CN.code) = '" + cn + "' AND lower(CT.label) ~ E'^" + ct + "$' AND lower(T.label) ~ E'^" + cx + "$'";
								if (h.equals("cx1"))
									isComplex1 = true;
								else
									isComplex2 = true;
							}
							else if (ct != null) {
								h = "ct" + h.replaceAll("pl", "");
								sql = "SELECT T.id from city T LEFT JOIN country CN ON T.id_country=CN.id WHERE lower(CN.code) = '" + cn + "' AND lower(T.label) ~ E'^" + ct + "$'";
							}
							else if (cn != null) {
								h = "cn" + h.replaceAll("pl", "");
								sql = "SELECT T.id from country T WHERE lower(T.code) = '" + cn + "'";
							}
						}
					}
					else if (h.matches("rk\\d")) {
						if (n == null) {
							if (tp != null)
								n = (tp.equalsIgnoreCase("country") ? 99 : (tp.equalsIgnoreCase("team") ? 50 : 1));
							else {
								String sql_ = "select type.number from Event ev where ev.id = ?";
								List<Integer> lNumber = (List<Integer>) DatabaseManager.executeSelect(sql_, Arrays.asList(hId.get(hId.containsKey("se2") ? "se2" : (hId.containsKey("se") ? "se" : "ev"))), Integer.class);
								if (lNumber != null && lNumber.size() > 0)
									n = lNumber.get(0);
							}
						}
						if (n != null) {
							if (n < 10) { // Athlete
								if (!s_.matches(StringUtils.PATTERN_ATHLETE)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
								}
								else {
									boolean isCountryTeam = s_.matches(".*\\([a-z]{3}\\,\\s.+\\)$");
									boolean isCountry = s_.matches(".*\\([a-z]{3}\\)$");
									boolean isTeam = (!isCountry && s_.matches(".*\\([^\\,\\(\\)]+\\)$")); 
									sql = "SELECT T.id FROM athlete T LEFT JOIN country CN ON T.id_country=CN.id LEFT JOIN team TM ON T.id_team=TM.id WHERE T.id_sport=" + hId.get("sp") + " AND lower(last_name) || ', ' || lower(first_name) " + (isCountryTeam ? " || ' (' || lower(CN.code) " + " || ', ' || lower(TM.label) || ')'" : (isCountry ? " || ' (' || lower(CN.code) || ')'" : (isTeam ? " || ' (' || lower(TM.label) || ')'" : ""))) + " ~ E'^" + regexp + "$'";
								}
							}
							else if (n == 50) { // Team
								if (!s_.matches(StringUtils.PATTERN_TEAM)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
								}
								else {
									String whereCond = (yr != null ? " and '" + yr + "' between year1 and (case year2 when null then '9999' when '' then '9999' else year2 end)" : "");
									if (s_.matches(".*\\([a-z]{3}\\)$")) {
										int p = s.indexOf(" (") + 2;
										String tmLabel = s.substring(0, p - 2).toLowerCase();
										String cnCode = s.substring(p, p + 3).toLowerCase();
										sql = "SELECT T.id from team T LEFT JOIN country CN ON T.id_country=CN.id where T.id_sport=" + hId.get("sp") + " AND lower(T.label) ~ E'^" + StringUtils.toPatternString(tmLabel) + "$' AND lower(CN.code) = '" + cnCode + "'" + whereCond;
									}
									else
										sql = "SELECT T.id from team T where T.id_sport=" + hId.get("sp") + " AND lower(T.label) ~ E'^" + regexp + "$'" + whereCond;
								}
							}
							else if (n == 99) { // Country
								if (!s_.matches(StringUtils.PATTERN_COUNTRY)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
								}
								else
									sql = "SELECT T.id FROM country T WHERE lower(code) ~ E'^" + s_ + "$'";
							}
						}
					}
				}
				if (sql != null || h.matches("sp|cp|ev")) {
//					Logger.getLogger("sh").fatal(sql);
					lId = (sql != null ? (List<Integer>) DatabaseManager.executeSelect(sql, Integer.class) : null);
					if (lId != null && lId.size() > 0) {
						hId.put(h, Integer.parseInt(String.valueOf(lId.get(0))));
						vLine.set(i, (!isUpdate ? "<span class='green'>" : "") + s + " [#" + lId.get(0) + "]" + (!isUpdate ? "</span>" : ""));
					}
					else {
						if (h.equalsIgnoreCase(Sport.alias)) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.invalid.sport", lang));
						}
						else if (h.equalsIgnoreCase(Championship.alias)) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.invalid.championship", lang));
						}
						else if (h.equalsIgnoreCase(Event.alias) && !StringUtils.notEmpty(s)) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.invalid.event", lang));
						}
						else if (h.matches("ev|se|se2"))
							writeError(vLine, ResourceUtils.getText("warning.event.notexist", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
						else if (h.equalsIgnoreCase(Year.alias)) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.invalid.year", lang));
						}
						else if (h.matches("(cx|ct|cn)\\d") && StringUtils.notEmpty(s)) {
							Pattern pattern = Pattern.compile("[A-Z]{3}$");
							Matcher matcher = pattern.matcher(s);
							if (!matcher.find() || !lCountries.contains(new Country(matcher.group(0)))) {
								isError = true;
								writeError(vLine, ResourceUtils.getText("err.invalid.country", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
							}
						}
						else if (h.matches("rk\\d")) {
							if (n == 99) {
								isError = true;
								writeError(vLine, ResourceUtils.getText("err.invalid.country", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
							}
							else if (n == 50)
								writeError(vLine, ResourceUtils.getText("warning.team.notexist", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
							else {
								Pattern pattern = Pattern.compile("[A-Z]{3}");
								Matcher matcher = pattern.matcher(s);
								if (!matcher.find() || !lCountries.contains(new Country(matcher.group(0)))) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.country", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
								}
								boolean isCountryTeam = s.toLowerCase().matches(".*\\([a-z]{3}\\,\\s.+\\)$");
								if (isCountryTeam) {
									String tm = StringUtils.toPatternString(s.substring(s.lastIndexOf(", ") + 2).toLowerCase().replaceAll("\\)$", ""));
									List<Integer> l = (List<Integer>) DatabaseManager.executeSelect("SELECT T.id FROM team T WHERE lower(T.label) ~ E'^" + tm + "$'", Integer.class);
									if (l == null || l.isEmpty())
										writeError(vLine, ResourceUtils.getText("warning.team.notexist", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
								}
							}
						}
						vLine.set(i, s);
					}
				}
				else {
					if (h.matches("dt\\d") && StringUtils.notEmpty(s)) {
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						df.setLenient(false);
						try {
							df.parse(s);
						}
						catch (ParseException e) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.invalid.date", lang).replaceAll("#S#", s) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
						}
					}
					else if (h.matches("rs\\d") && StringUtils.notEmpty(s) && s.length() > (h.equalsIgnoreCase("rs1") ? 40 : 20)) {
						isError = true;
						writeError(vLine, ResourceUtils.getText("err.value.toolong", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
					}
				}
			}
			catch (Exception e) {
				isError = true;
				writeError(vLine, ResourceUtils.getText("error", lang).toUpperCase() + ": " + e.getMessage());
			}
			if (isError)
				break;
		}
		if (isUpdate) {
			Integer idSp = hId.get("sp");
			Integer idCp = hId.get("cp");
			Integer idEv = hId.get("ev");
			Integer idSe = hId.get("se");
			Integer idSe2 = hId.get("se2");
			Integer idYr = hId.get("yr");
			Integer idCx1 = hId.get("cx1");
			Integer idCt1 = hId.get("ct1");
			Integer idCn1 = hId.get("cn1");
			Integer idCx2 = hId.get("cx2");
			Integer idCt2 = hId.get("ct2");
			Integer idCn2 = hId.get("cn2");
			Integer idRk1 = hId.get("rk1");
			Integer idRk2 = hId.get("rk2");
			Integer idRk3 = hId.get("rk3");
			Integer idRk4 = hId.get("rk4");
			Integer idRk5 = hId.get("rk5");
			Integer idRk6 = hId.get("rk6");
			Integer idRk7 = hId.get("rk7");
			Integer idRk8 = hId.get("rk8");
			Integer idRk9 = hId.get("rk9");
			String rs1 = null; String rs2 = null; String rs3 = null; String rs4 = null; String rs5 = null;
			String dt1 = null; String dt2 = null;
			String cmt = null;
			String exl1 = null; String exl2 = null; String exl3 = null;
			String exa = null;
			String err = null;
			try {
				for (int i = 0 ; i < vLine.size() ; i++) {
					String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
					String s = vLine.get(i);
					if (StringUtils.notEmpty(s)) {
						if(idEv == null && h.equalsIgnoreCase("ev"))
							idEv = insertEvent(row, s, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idSe == null && h.equalsIgnoreCase("se"))
							idSe = insertEvent(row, s, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idSe2 == null && h.equalsIgnoreCase("se2"))
							idSe2 = insertEvent(row, s, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk1 == null && h.equalsIgnoreCase("rk1"))
							idRk1 = insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk2 == null && h.equalsIgnoreCase("rk2"))
							idRk2 = insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk3 == null && h.equalsIgnoreCase("rk3"))
							idRk3 = insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk4 == null && h.equalsIgnoreCase("rk4"))
							idRk4 = insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk5 == null && h.equalsIgnoreCase("rk5"))
							idRk5 = insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk6 == null && h.equalsIgnoreCase("rk6"))
							idRk6 = insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk7 == null && h.equalsIgnoreCase("rk7"))
							idRk7 = insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk8 == null && h.equalsIgnoreCase("rk8"))
							idRk8 = insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk9 == null && h.equalsIgnoreCase("rk9"))
							idRk9 = insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk1"))
							updateEntity(row, n, idRk1, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk2"))
							updateEntity(row, n, idRk2, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk3"))
							updateEntity(row, n, idRk3, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk4"))
							updateEntity(row, n, idRk4, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk5"))
							updateEntity(row, n, idRk5, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk6"))
							updateEntity(row, n, idRk6, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk7"))
							updateEntity(row, n, idRk7, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk8"))
							updateEntity(row, n, idRk8, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk9"))
							updateEntity(row, n, idRk9, s, sb, cb);
						else if (idCx1 == null && idCt1 == null && idCn1 == null && h.matches("pl1")) {
							if (isComplex1)
								idCx1 = insertPlace(row, s, cb, sb, ResourceUtils.LGDEFAULT);
							else
								idCt1 = insertPlace(row, s, cb, sb, ResourceUtils.LGDEFAULT);
						}
						else if (idCx2 == null && idCt2 == null && idCn2 == null && h.matches("pl2")) {
							if (isComplex2)
								idCx2 = insertPlace(row, s, cb, sb, ResourceUtils.LGDEFAULT);
							else
								idCt2 = insertPlace(row, s, cb, sb, ResourceUtils.LGDEFAULT);
						}
						else if (h.equalsIgnoreCase("rs1"))
							rs1 = s;
						else if (h.equalsIgnoreCase("rs2"))
							rs2 = s;
						else if (h.equalsIgnoreCase("rs3"))
							rs3 = s;
						else if (h.equalsIgnoreCase("rs4"))
							rs4 = s;
						else if (h.equalsIgnoreCase("rs5"))
							rs5 = s;
						else if (h.equalsIgnoreCase("dt1"))
							dt1 = s;
						else if (h.equalsIgnoreCase("dt2"))
							dt2 = s;
						else if (h.equalsIgnoreCase("exa"))
							exa = s;
						else if (h.equalsIgnoreCase("cmt"))
							cmt = s;
						else if (h.equalsIgnoreCase("exl1"))
							exl1 = s;
						else if (h.equalsIgnoreCase("exl2"))
							exl2 = s;
						else if (h.equalsIgnoreCase("exl3"))
							exl3 = s;
					}
				}
			}
			catch (Exception e) {
				err = e.getMessage();
				log.log(Level.WARNING, e.getMessage(), e);
			}
			if (err != null)
				writeError(vLine, ResourceUtils.getText("error", lang).toUpperCase() + ": " + err);
			else {
				Result rs = new Result();
				rs.setSport((Sport)DatabaseManager.loadEntity(Sport.class, idSp));
				rs.setChampionship((Championship)DatabaseManager.loadEntity(Championship.class, idCp));
				rs.setEvent((Event)DatabaseManager.loadEntity(Event.class, idEv != null ? idEv : 0));
				rs.setSubevent((Event)DatabaseManager.loadEntity(Event.class, idSe != null ? idSe : 0));
				rs.setSubevent2((Event)DatabaseManager.loadEntity(Event.class, idSe2 != null ? idSe2 : 0));
				rs.setYear((Year)DatabaseManager.loadEntity(Year.class, idYr));
				rs.setComplex1((Complex)DatabaseManager.loadEntity(Complex.class, idCx1));
				rs.setComplex2((Complex)DatabaseManager.loadEntity(Complex.class, idCx2));
				rs.setCity1((City)DatabaseManager.loadEntity(City.class, idCt1));
				rs.setCity2((City)DatabaseManager.loadEntity(City.class, idCt2));
				rs.setCountry1((Country)DatabaseManager.loadEntity(Country.class, idCn1));
				rs.setCountry2((Country)DatabaseManager.loadEntity(Country.class, idCn2));
				rs.setDate1(StringUtils.notEmpty(dt1) ? dt1 : null);
				rs.setDate2(StringUtils.notEmpty(dt2) ? dt2 : null);
				rs.setExa(StringUtils.notEmpty(exa) ? exa : null);
				rs.setComment(StringUtils.notEmpty(cmt) ? cmt : null);
				rs.setDraft(false);
				rs.setIdRank1(idRk1);
				rs.setIdRank2(idRk2);
				rs.setIdRank3(idRk3);
				rs.setIdRank4(idRk4);
				rs.setIdRank5(idRk5);
				rs.setIdRank6(idRk6);
				rs.setIdRank7(idRk7);
				rs.setIdRank8(idRk8);
				rs.setIdRank9(idRk9);
				rs.setResult1(rs1);
				rs.setResult2(rs2);
				rs.setResult3(rs3);
				rs.setResult4(rs4);
				rs.setResult5(rs5);
				rs = (Result) DatabaseManager.saveEntity(rs, cb);
				if (StringUtils.notEmpty(exl1))
					DatabaseManager.saveExternalLinks(Result.alias, rs.getId(), exl1 + (StringUtils.notEmpty(exl2) ? " " + exl2 : "") + (StringUtils.notEmpty(exl3) ? " " + exl3 : ""));
				sb.append("Row " + (row + 1) + ": New Result | " + rs).append("<br/>");
			}
		}
		return isError;
	}

	public static boolean processLineRC(int row, Vector<String> vHeader, Vector<String> vLine, boolean isUpdate, StringBuffer sb, Contributor cb, String lang) throws Exception {
		boolean isError = false;
		List<Integer> lId = null;
		HashMap<String, Integer> hId = new HashMap<>();
		Integer n = null;
		Integer rcId = null;
		String label = null;
		String type1 = null, type2 = null;
		for (int i = 0 ; i < vLine.size() ; i++) {
			try {
				String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
				String s = vLine.get(i);
				String sql = null;
				if (StringUtils.notEmpty(s)) {
					if (s.matches(".*\\s\\[#\\d+]$"))
						s = s.substring(0, s.indexOf("[#") - 1);
					String s_ = s.replaceAll(scPattern, "_").toLowerCase();
					if (h.equalsIgnoreCase(Sport.alias))
						sql = "SELECT id FROM sport WHERE LOWER(label) LIKE '" + s_ + "'";
					else if (h.equalsIgnoreCase(Championship.alias))
						sql = "SELECT id FROM championship WHERE LOWER(label) LIKE '" + s_ + "'";
					else if (h.matches("ev|se")) {
						String[] tEv = s_.split("\\|");
						sql = "SELECT id FROM event WHERE LOWER(label) LIKE '" + tEv[0] + "'" + (tEv.length > 1 ? " and LOWER(type.label) = '" + tEv[1] + "'" : "") + " ORDER BY id";
					}
					else if (h.equalsIgnoreCase("tp1"))
						type1 = s;
					else if (h.equalsIgnoreCase("tp2"))
						type2 = s;
					else if (h.matches("label")) {
						label = s;
						sql = "SELECT id FROM record WHERE LOWER(label) LIKE '" + s_.replaceAll("'", "''") + "'" + (type1 != null ? " and LOWER(type1)='" + type1.toLowerCase() + "'" : "") + (type2 != null ? " and LOWER(type2)='" + type2.toLowerCase() + "'" : "") + " and sport.id=" + hId.get("sp") + " and championship.id=" + hId.get("cp") + " and event.id=" + hId.get("ev") + (hId.containsKey("se") ? " and subevent.id=" + hId.get("se") : "");
					}
					else if (h.matches("rk\\d")) {
						if (n == null) {
							String sql_ = "SELECT type.number FROM event ev WHERE ev.id = ?";
							List<Integer> lNumber = (List<Integer>) DatabaseManager.executeSelect(sql_, Arrays.asList(hId.get(hId.containsKey("se") ? "se" : "ev")), Integer.class);
							if (lNumber != null && lNumber.size() > 0)
								n = lNumber.get(0);
						}
						if (n != null) {
							if (type1.equalsIgnoreCase("individual")) { // Athlete
								n = 1;
								if (!s_.matches(StringUtils.PATTERN_ATHLETE)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
								}
								else {
									boolean isCountryTeam = s_.matches(".*\\([a-z]{3}\\,\\s.+\\)$");
									boolean isCountry = s_.matches(".*\\([a-z]{3}\\)$");
									boolean isTeam = (!isCountry && s_.matches(".*\\([^\\,\\(\\)]+\\)$")); 
									sql = "SELECT id FROM athlete WHERE sport.id=" + hId.get("sp") + " and LOWER(last_name) || ', ' || LOWER(first_name) " + (isCountryTeam ? " || ' (' || LOWER(country.code) " + " || ', ' || LOWER(team.label) || ')'" : (isCountry ? " || ' (' || LOWER(country.code) || ')'" : (isTeam ? " || ' (' || LOWER(team.label) || ')'" : ""))) + " LIKE '" + s_ + "'";
								}
							}
							else if (type1.equalsIgnoreCase("team")) { // Team
								n = 50;
								if (!s_.matches(StringUtils.PATTERN_TEAM)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
								}
								else {
									// Get record year
									String rcy = null;
									if (vLine.size() > i + 2) {
										rcy = vLine.get(i + 2);
										rcy = (StringUtils.notEmpty(rcy) && rcy.matches(".*\\d{4}$") ? rcy.substring(rcy.length() - 4) : null);
									}
									sql = "SELECT id FROM team WHERE sport.id=" + hId.get("sp") + " and LOWER(label) LIKE '" + (s_.indexOf(" (") > -1 ? s_.substring(0, s_.indexOf(" (")) : s_) + "'" + (rcy != null ? " and '" + rcy + "' between (case when year1 is null or year1='' then '0000' else year1 end) and (case when year2 is null or year2='' then '9999' else year2 end)" : "");
								}
							}
							else if (n == 99) { // Country
								if (!s_.matches(StringUtils.PATTERN_COUNTRY)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
								}
								else
									sql = "SELECT id FROM country WHERE LOWER(code) LIKE '" + s_ + "'";
							}
						}
					}
				}
				if (sql != null) {
//					Logger.getLogger("sh").info(hql);
					lId = (List<Integer>) DatabaseManager.executeSelect(sql, Integer.class);
					if (lId != null && lId.size() > 0) {
						hId.put(h, lId.get(0));
						vLine.set(i, (!isUpdate ? "<span class='green'>" : "") + s + " [#" + lId.get(0) + "]" + (!isUpdate ? "</span>" : ""));
					}
					else {
						if (h.equalsIgnoreCase(Sport.alias)) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.invalid.sport", lang));
						}
						else if (h.equalsIgnoreCase(Championship.alias)) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.invalid.championship", lang));
						}
						else if (h.matches("ev|se")) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.invalid.event", lang));
						}
						else if (h.matches("rk\\d")) {
							if (n == 50)
								writeError(vLine, ResourceUtils.getText("warning.team.notexist", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
						}
						vLine.set(i, s);
					}
				}
				else {
					if (h.matches("rc\\d") && StringUtils.notEmpty(s) && s.length() > 15) {
						isError = true;
						writeError(vLine, ResourceUtils.getText("error.value.toolong", lang) + " (" + ResourceUtils.getText("column", lang) + " <b>" + getColumnTitle(h, lang) + "</b>)");
					}
				}
			}
			catch (Exception e) {
				isError = true;
				writeError(vLine, ResourceUtils.getText("error", lang).toUpperCase() + ": " + e.getMessage());
			}
			if (isError)
				break;
		}
		if (isUpdate) {
			rcId = hId.get("label");
			Integer idSp = hId.get("sp");
			Integer idCp = hId.get("cp");
			Integer idEv = hId.get("ev");
			Integer idSe = hId.get("se");
			Integer idRk1 = hId.get("rk1");
			Integer idRk2 = hId.get("rk2");
			Integer idRk3 = hId.get("rk3");
			Integer idRk4 = hId.get("rk4");
			Integer idRk5 = hId.get("rk5");
			String rc1 = null, rc2 = null, rc3 = null, rc4 = null, rc5 = null;
			String dt1 = null, dt2 = null, dt3 = null, dt4 = null, dt5 = null;
			String exa = null, cmt = null;
			BigDecimal idx = null;
			String err = null;
			try {
				for (int i = 0 ; i < vLine.size() ; i++) {
					String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
					String s = vLine.get(i);
					if (StringUtils.notEmpty(s)) {
						if(idRk1 == null && h.equalsIgnoreCase("rk1"))
							idRk1 = insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk2 == null && h.equalsIgnoreCase("rk2"))
							idRk2 = insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk3 == null && h.equalsIgnoreCase("rk3"))
							idRk3 = insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk4 == null && h.equalsIgnoreCase("rk4"))
							idRk4 = insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk5 == null && h.equalsIgnoreCase("rk5"))
							idRk5 = insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, cb, sb, ResourceUtils.LGDEFAULT);
						else if (h.equalsIgnoreCase("rc1"))
							rc1 = s;
						else if (h.equalsIgnoreCase("rc2"))
							rc2 = s;
						else if (h.equalsIgnoreCase("rc3"))
							rc3 = s;
						else if (h.equalsIgnoreCase("rc4"))
							rc4 = s;
						else if (h.equalsIgnoreCase("rc5"))
							rc5 = s;
						else if (h.equalsIgnoreCase("dt1"))
							dt1 = s;
						else if (h.equalsIgnoreCase("dt2"))
							dt2 = s;
						else if (h.equalsIgnoreCase("dt3"))
							dt3 = s;
						else if (h.equalsIgnoreCase("dt4"))
							dt4 = s;
						else if (h.equalsIgnoreCase("dt5"))
							dt5 = s;
						else if (h.equalsIgnoreCase("idx"))
							idx = new BigDecimal(s);
						else if (h.equalsIgnoreCase("exa"))
							exa = s;
						else if (h.equalsIgnoreCase("cmt"))
							cmt = s;
					}
				}
			}
			catch (Exception e) {
				err = e.getMessage();
				log.log(Level.WARNING, e.getMessage(), e);
			}
			if (err != null)
				writeError(vLine, ResourceUtils.getText("error", lang).toUpperCase() + ": " + err);
			else {
				Record rc = null;
				if (rcId != null)
					rc = (Record) DatabaseManager.loadEntity(Record.class, rcId);
				else {
					rc = new Record();
					rc.setLabel(label);
					rc.setSport((Sport)DatabaseManager.loadEntity(Sport.class, idSp));
					rc.setChampionship((Championship)DatabaseManager.loadEntity(Championship.class, idCp));
					rc.setEvent((Event)DatabaseManager.loadEntity(Event.class, idEv != null ? idEv : 0));
					rc.setSubevent((Event)DatabaseManager.loadEntity(Event.class, idSe != null ? idSe : 0));
					rc.setType1(type1);
					rc.setType2(type2);
				}
				rc.setIdRank1(idRk1);
				rc.setIdRank2(idRk2);
				rc.setIdRank3(idRk3);
				rc.setIdRank4(idRk4);
				rc.setIdRank5(idRk5);
				rc.setRecord1(rc1);
				rc.setRecord2(rc2);
				rc.setRecord3(rc3);
				rc.setRecord4(rc4);
				rc.setRecord5(rc5);
				rc.setDate1(dt1);
				rc.setDate2(dt2);
				rc.setDate3(dt3);
				rc.setDate4(dt4);
				rc.setDate5(dt5);
				rc.setIndex(idx);
				rc.setExa(StringUtils.notEmpty(exa) ? exa : null);
				rc.setComment(StringUtils.notEmpty(cmt) ? cmt : null);
				rc = (Record) DatabaseManager.saveEntity(rc, cb);
				sb.append("Row " + (row + 1) + ": Record " + (rcId != null ? "Updated" : "Created") + " | " + rc).append("<br/>");
			}
		}
		return isError;
	}
	
	public static void updateEntity(int row, int n, int id, String s, StringBuffer sb, Contributor cb) throws Exception {
		Object o = null;
		String msg = null;
		try {
			if (s.matches(".*\\s\\[#\\d+]$"))
				s = s.substring(0, s.indexOf("[#") - 1);
			if (n < 10) {
				int p = s.indexOf(", ");
				Athlete a = (Athlete) DatabaseManager.loadEntity(Athlete.class, id);
				String lastName = (s.substring(0, p > -1 ? p : s.indexOf(" (")));
				String firstName = (p > -1 ? s.substring(p + 2, s.indexOf(" (")) : null);
				if ((lastName + firstName).matches(".*" + scPattern + ".*") && (!lastName.equals(a.getLastName()) || !firstName.equals(a.getFirstName()))) {
					a.setLastName(lastName);
					a.setFirstName(firstName);
					o = DatabaseManager.saveEntity(a, cb);
					msg = "Update Athlete";
				}
			}
			else if (n == 50) {
				Team t = (Team) DatabaseManager.loadEntity(Team.class, id);
				String label = s;
				if (s.matches(".*\\([A-Z]{3}\\).*")) {
					int p = s.indexOf(" (") + 2;
					label = s.substring(0, p - 2);
				}
				if (label.matches(".*" + scPattern + ".*") && !label.equals(t.getLabel())) {
					t.setLabel(label);
					o = DatabaseManager.saveEntity(t, cb);
					msg = "Update Team";
				}
			}
		}
		finally {
			if (o != null)
				sb.append("Row " + (row + 1) + ": " + msg + " | " + o).append("<br/>");
		}
	}
	
	private static void writeError(Vector<String> vLine, String msg) {
		if (vLine != null && !vLine.get(0).contains("'red'") && (!StringUtils.notEmpty(vLine.get(0)) || vLine.get(0).equals("-") || vLine.get(0).contains("'orange'")))
			vLine.set(0, "<span class='" + (msg.startsWith("ERR") ? "red" : "orange") + "'>" + msg + "</span>");
	}
	
	public static List<List<String>> getTemplate(String type, String lang) {
		String COLOR1 = "#color-blue#";
		String COLOR2 = "#color-green#";
		String COLOR3 = "#color-orange#";
		List<List<String>> list = new ArrayList<List<String>>();
		if (type.equalsIgnoreCase(Result.alias)) {
			List<String> list_ = new ArrayList<String>();
			for (String s : new String[] {COLOR1 + ResourceUtils.getText("sport", lang), COLOR1 + ResourceUtils.getText("event", lang) + " 1", COLOR1 + ResourceUtils.getText("event", lang) + " 2", COLOR1 + ResourceUtils.getText("event", lang) + " 3", COLOR1 + ResourceUtils.getText("event", lang) + " 4", COLOR1 + ResourceUtils.getText("year", lang), COLOR2 + ResourceUtils.getText("template.rs.winner", lang), COLOR2 + ResourceUtils.getText("template.rs.score", lang), COLOR2 + ResourceUtils.getText("template.rs.second", lang), COLOR2 + ResourceUtils.getText("template.rs.result", lang) + " (" + ResourceUtils.getText("rank.2", lang) + ")", COLOR2 + ResourceUtils.getText("template.rs.third", lang), COLOR2 + ResourceUtils.getText("template.rs.result", lang) + " (" + ResourceUtils.getText("rank.3", lang) + ")", COLOR2 + ResourceUtils.getText("rank.4", lang), COLOR2 + ResourceUtils.getText("rank.5", lang), COLOR2 + ResourceUtils.getText("rank.6", lang), COLOR2 + ResourceUtils.getText("rank.7", lang), COLOR2 + ResourceUtils.getText("rank.8", lang), COLOR2 + ResourceUtils.getText("rank.9", lang), COLOR3 + ResourceUtils.getText("template.date1", lang), COLOR3 + ResourceUtils.getText("template.date2", lang), COLOR3 + ResourceUtils.getText("template.place1", lang), COLOR3 + ResourceUtils.getText("template.place2", lang), COLOR3 + ResourceUtils.getText("tie", lang), COLOR3 + ResourceUtils.getText("comment", lang), COLOR3 + ResourceUtils.getText("extlinks", lang) + " (1)", COLOR3 + ResourceUtils.getText("extlinks", lang) + " (2)", COLOR3 + ResourceUtils.getText("extlinks", lang) + " (3)"})
				list_.add(s);
			list.add(list_);
			list_ = new ArrayList<String>();
			for (String s : new String[] {COLOR1 + "Athletics", COLOR1 + "Olympic Games", COLOR1 + "Men", COLOR1 + "100 M", COLOR1 + "", COLOR1 + "1936", COLOR2 + "Owens, Jessie (USA)", COLOR2 + "10.30", COLOR2 + "Metcalfe, Ralf (USA)", COLOR2 + "10.40", COLOR2 + "Osendarp, Tinus (NED)", COLOR2 + "10.50", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR3 + "", COLOR3 + "08/03/1936", COLOR3 + "", COLOR3 + "Olympiastadion, Berlin, GER", COLOR3 + "", COLOR3 + "", COLOR3 + "https://en.wikipedia.org/wiki/Athletics_at_the_1936_Summer_Olympics_%E2%80%93_Men%27s_100_metres", COLOR3 + "http://www.olympic.org/summer/1936/ATH/mens-100-metres.html", COLOR3 + ""})
				list_.add(s);
			list.add(list_);
			list_ = new ArrayList<String>();
			for (String s : new String[] {COLOR1 + "Tennis", COLOR1 + "Grand Slam", COLOR1 + "Men", COLOR1 + "French Open", COLOR1 + "", COLOR1 + "2005", COLOR2 + "Nadal, Rafael (ESP)", COLOR2 + "6/7 6/3 6/1 7/5", COLOR2 + "Puerta, Mariano (ARG)", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR3 + "07/06/2005", COLOR3 + "07/06/2005", COLOR3 + "", COLOR3 + "Roland Garros, Paris, FRA", COLOR3 + "", COLOR3 + "", COLOR3 + "", COLOR3 + "", COLOR3 + ""})
				list_.add(s);
			list.add(list_);
			list_ = new ArrayList<String>();
			for (String s : new String[] {COLOR1 + "Road Cycling", COLOR1 + "UCI World Tour", COLOR1 + "Milan-San Remo", COLOR1 + "", COLOR1 + "", COLOR1 + "2004", COLOR2 + "Cancellara, Fabian (SUI)", COLOR2 + "", COLOR2 + "Contador, Albero (ESP)", COLOR2 + "Rodriguez, Joaquin (ESP)", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR2 + "", COLOR3 + "", COLOR3 + "06/03/2004", COLOR3 + "Milan, ITA", COLOR3 + "San Remo, ITA", COLOR3 + "", COLOR3 + "", COLOR3 + "", COLOR3 + "", COLOR3 + ""})
				list_.add(s);
			list.add(list_);
		}
		else if (type.equalsIgnoreCase(Record.alias)) {
			List<String> list_ = new ArrayList<String>();
			for (String s : new String[] {ResourceUtils.getText("sport", lang), ResourceUtils.getText("event", lang) + " 1", ResourceUtils.getText("event", lang) + " 2", ResourceUtils.getText("event", lang) + " 3", ResourceUtils.getText("type", lang) + " #1", ResourceUtils.getText("type", lang) + " #2", ResourceUtils.getText("record.label", lang), ResourceUtils.getText("record.holder", lang), ResourceUtils.getText("record2", lang) + " (" + ResourceUtils.getText("record.holder", lang) + ")", ResourceUtils.getText("date", lang) + " (" + ResourceUtils.getText("record.holder", lang) + ")", ResourceUtils.getText("record.holder", lang), ResourceUtils.getText("record2", lang) + " (" + ResourceUtils.getText("rank.2", lang) + ")", ResourceUtils.getText("date", lang) + " (" + ResourceUtils.getText("rank.2", lang) + ")", ResourceUtils.getText("rank.3", lang), ResourceUtils.getText("record2", lang) + " (" + ResourceUtils.getText("rank.3", lang) + ")", ResourceUtils.getText("date", lang) + " (" + ResourceUtils.getText("rank.3", lang) + ")", ResourceUtils.getText("rank.4", lang), ResourceUtils.getText("record2", lang) + " (" + ResourceUtils.getText("rank.4", lang) + ")", ResourceUtils.getText("date", lang) + " (" + ResourceUtils.getText("rank.4", lang) + ")", ResourceUtils.getText("rank.5", lang), ResourceUtils.getText("record2", lang) + " (" + ResourceUtils.getText("rank.5", lang) + ")", ResourceUtils.getText("date", lang) + " (" + ResourceUtils.getText("rank.5", lang) + ")", ResourceUtils.getText("index", lang), ResourceUtils.getText("tie", lang), ResourceUtils.getText("comment", lang) })
				list_.add(s);
			list.add(list_);
			list.add(new ArrayList<String>());
			list_ = new ArrayList<String>();
			for (String s : new String[] {COLOR1 + "Basketball", COLOR1 + "NBA", COLOR1 + "Rebounds", COLOR1 + "", COLOR1 + "Individual", COLOR1 + "Game", COLOR1 + "Most rebounds", COLOR2 + "Chamberlain, Wilt (Philadelphia Warriors)", COLOR2 + "55", COLOR2 + "10/23/1962", COLOR2 + "Russell, Bill (Boston Celtics)", COLOR2 + "52", COLOR2 + "04/02/1965", COLOR2 + "Rodman, Dennis (Chicago Bulls)", COLOR2 + "41", COLOR2 + "10/10/1998", COLOR2 + "", COLOR2 + "", "", COLOR2 + "", COLOR2 + "", COLOR3 + "", COLOR3 + "1.0", COLOR3 + "", COLOR3 + ""})
				list_.add(s);
			list.add(list_);
		}
		return list;
	}
	
	private static final String getColumnTitle(String s, String lang) {
		HashMap<String, String> hTitle = new HashMap<String, String>();
		hTitle.put("msg", ResourceUtils.getText("message", lang));
		hTitle.put("sp", ResourceUtils.getText("entity.SP.1", lang));
		hTitle.put("cp", ResourceUtils.getText("entity.CP.1", lang));
		hTitle.put("ev", ResourceUtils.getText("entity.EV.1", lang) + " #1");
		hTitle.put("se", ResourceUtils.getText("entity.EV.1", lang) + " #2");
		hTitle.put("se2", ResourceUtils.getText("entity.EV.1", lang) + " #3");
		hTitle.put("yr", ResourceUtils.getText("entity.YR.1", lang));
		hTitle.put("rk1", ResourceUtils.getText("rank.1", lang)); hTitle.put("rk2", ResourceUtils.getText("rank.2", lang)); hTitle.put("rk3", ResourceUtils.getText("rank.3", lang)); hTitle.put("rk4", ResourceUtils.getText("rank.4", lang)); hTitle.put("rk5", ResourceUtils.getText("rank.5", lang)); hTitle.put("rk6", ResourceUtils.getText("rank.6", lang)); hTitle.put("rk7", ResourceUtils.getText("rank.7", lang)); hTitle.put("rk8", ResourceUtils.getText("rank.8", lang)); hTitle.put("rk9", ResourceUtils.getText("rank.9", lang));
		hTitle.put("rs1", ResourceUtils.getText("entity.RS.1", lang) + " #1");
		hTitle.put("rs2", ResourceUtils.getText("entity.RS.1", lang) + " #2");
		hTitle.put("rs3", ResourceUtils.getText("entity.RS.1", lang) + " #3");
		hTitle.put("rs4", ResourceUtils.getText("entity.RS.1", lang) + " #4");
		hTitle.put("rs5", ResourceUtils.getText("entity.RS.1", lang) + " #5");
		hTitle.put("dt1", ResourceUtils.getText("date", lang) + " #1");
		hTitle.put("dt2", ResourceUtils.getText("date", lang) + " #2");
		hTitle.put("pl1", ResourceUtils.getText("place", lang) + " #1");
		hTitle.put("pl2", ResourceUtils.getText("place", lang) + " #2");
		hTitle.put("exa", ResourceUtils.getText("tie", lang));
		hTitle.put("cmt", ResourceUtils.getText("comment", lang));
		hTitle.put("exl1", ResourceUtils.getText("ext.links", lang) + " #1");
		hTitle.put("exl2", ResourceUtils.getText("ext.links", lang) + " #2");
		hTitle.put("exl3", ResourceUtils.getText("ext.links", lang) + " #3");
		hTitle.put("qf1w", ResourceUtils.getText("quarterfinal", lang) + " #1 - W");
		hTitle.put("qf1r", ResourceUtils.getText("quarterfinal", lang) + " #1 - L");
		hTitle.put("qf1s", ResourceUtils.getText("quarterfinal", lang) + " #1 - Score");
		hTitle.put("qf2w", ResourceUtils.getText("quarterfinal", lang) + " #2 - W");
		hTitle.put("qf2r", ResourceUtils.getText("quarterfinal", lang) + " #2 - L");
		hTitle.put("qf2s", ResourceUtils.getText("quarterfinal", lang) + " #2 - Score");
		hTitle.put("qf3w", ResourceUtils.getText("quarterfinal", lang) + " #3 - W");
		hTitle.put("qf3r", ResourceUtils.getText("quarterfinal", lang) + " #3 - L");
		hTitle.put("qf3s", ResourceUtils.getText("quarterfinal", lang) + " #3 - Score");
		hTitle.put("qf4w", ResourceUtils.getText("quarterfinal", lang) + " #4 - W");
		hTitle.put("qf4r", ResourceUtils.getText("quarterfinal", lang) + " #4 - L");
		hTitle.put("qf4s", ResourceUtils.getText("quarterfinal", lang) + " #4 - Score");
		hTitle.put("sf1w", ResourceUtils.getText("semifinal", lang) + " #1 - W");
		hTitle.put("sf1r", ResourceUtils.getText("semifinal", lang) + " #1 - L");
		hTitle.put("sf1s", ResourceUtils.getText("semifinal", lang) + " #1 - Score");
		hTitle.put("sf2w", ResourceUtils.getText("semifinal", lang) + " #2 - W");
		hTitle.put("sf2r", ResourceUtils.getText("semifinal", lang) + " #2 - L");
		hTitle.put("sf2s", ResourceUtils.getText("semifinal", lang) + " #2 - Score");
		hTitle.put("thdw", ResourceUtils.getText("third.place", lang) + " - W");
		hTitle.put("thdr", ResourceUtils.getText("third.place", lang) + " - L");
		hTitle.put("thds", ResourceUtils.getText("third.place", lang) + " - Score");
		hTitle.put("tp1", ResourceUtils.getText("type", lang) + " #1");
		hTitle.put("tp2", ResourceUtils.getText("type", lang) + " #2");
		hTitle.put("label", ResourceUtils.getText("name", lang));
		hTitle.put("rc1", ResourceUtils.getText("record2", lang));
		hTitle.put("rc2", ResourceUtils.getText("record2", lang));
		hTitle.put("rc3", ResourceUtils.getText("record2", lang));
		hTitle.put("rc4", ResourceUtils.getText("record2", lang));
		hTitle.put("rc5", ResourceUtils.getText("record2", lang));
		hTitle.put("idx", "Index");
		return (hTitle.containsKey(s) ? hTitle.get(s) : s);
	}
	
	public static Integer insertEvent(int row, String s, Contributor cb, StringBuffer sb, String lang) throws Exception {
		Integer id = null;
		Object o = null;
		String msg = null;
		try {
			String[] t = s.split("\\|");
			Object o_ = null;
			if (t.length > 1)
				o_ = DatabaseManager.loadEntity("SELECT * FROM type tp WHERE LOWER(tp.label) = ?", Arrays.asList(t[1].toLowerCase()), Type.class);
			if (o_ == null)
				o_ = DatabaseManager.loadEntity("SELECT * FROM type tp WHERE LOWER(tp.label) = 'single'", null, Type.class);
			Event ev = new Event();
			ev.setLabel(t[0]);
			ev.setLabelFr(t[0]);
			ev.setIndex(Double.MAX_VALUE);
			ev.setType((Type)o_);
			o = DatabaseManager.saveEntity(ev, cb);
			msg = "New Event";
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			throw e;
		}
		finally {
			if (o != null) {
				if (sb != null)
					sb.append("Row " + (row + 1) + ": " + msg + " | " + o).append("<br/>");
				id = StringUtils.toInt(o.getClass().getMethod("getId").invoke(o));
			}
		}
		return id;
	}

	public static Integer insertEntity(int row, int n, int spid, String s, String date, Contributor cb, StringBuffer sb, String lang) throws Exception {
		Integer id = null;
		Object o = null;
		String msg = null;
		try {
			if (n < 10) {
				s = s.replaceAll("\\[", "(").replaceAll("\\]", ")");
				if (!s.toLowerCase().matches(StringUtils.PATTERN_ATHLETE))
					throw new Exception(ResourceUtils.getText("err.invalid.athlete", lang).replaceAll("#S#", s));
				int p = s.indexOf(", ");
				int p_ = (s.indexOf(" (") > -1  ? s.indexOf(" (") : s.length());
				if (p > s.indexOf(" ("))
					p = -1;
				Athlete a = new Athlete();
				a.setSport((Sport)DatabaseManager.loadEntity(Sport.class, spid));
				a.setLastName(s.substring(0, p > -1 ? p : p_));
				a.setFirstName(p > -1 && s.charAt(p + 2) != '(' ? s.substring(p + 2, p_) : "");
				boolean isCountryTeam = s.toLowerCase().matches(".*\\([a-z]{3}\\,\\s.+\\)$");
				boolean isCountry = s.toLowerCase().matches(".*\\([a-z]{3}\\)$");
				boolean isTeam = (!isCountry && s.toLowerCase().matches(".*\\([^\\,\\(\\)]+\\)$")); 
				// Check if exists
				String regexp = StringUtils.toPatternString(s);
				String sql = "SELECT AT.id FROM athlete AT "
						+ " LEFT JOIN country CN ON AT.id_country = CN.id"
						+ " LEFT JOIN team TM ON AT.id_team = TM.id"
						+ " WHERE AT.id_sport = ? and LOWER(last_name) || ', ' || LOWER(first_name) " + 
							(isCountryTeam ? " || ' (' || LOWER(CN.code) " + " || ', ' || LOWER(TM.label) || ')'" : (isCountry ? " || ' (' || LOWER(CN.code) || ')'" : (isTeam ? " || ' (' || LOWER(TM.label) || ')'" : ""))) + " ~ E'^" + regexp + "$'";
				List<Integer> lId = (List<Integer>) DatabaseManager.executeSelect(sql, Arrays.asList(spid), Integer.class);
				if (lId != null && lId.size() > 0)
					return lId.get(0);
				// Automatic link
				regexp = StringUtils.toPatternString(a.getLastName() + ", " + (StringUtils.notEmpty(a.getFirstName()) ? a.getFirstName() : ""));
				sql = "SELECT T.id FROM athlete T WHERE T.id_sport = ? and lower(last_name) || ', ' || lower(first_name) ~ E'^" + regexp + "$' ORDER BY T.id";
				lId = (List<Integer>) DatabaseManager.executeSelect(sql, Arrays.asList(spid), Integer.class);
				if (lId != null && lId.size() > 0) {
					a.setLink(lId.get(0));
					DatabaseManager.executeUpdate("UPDATE athlete SET link = 0 WHERE id = ?",  Arrays.asList(lId.get(0)));
				}
				if (isCountry || isCountryTeam) { // Country set
					p = s.indexOf(" (") + 2;
					String countryCode = s.substring(p, p + 3).toLowerCase();
					Object o_ = DatabaseManager.loadEntity("SELECT * FROM country cn WHERE LOWER(cn.code) = ?", Arrays.asList(countryCode), Country.class);
					if (o_ == null)
						throw new Exception("Invalid Country: " + countryCode.toUpperCase());
					a.setCountry((Country)o_);
				}
				if (isTeam || isCountryTeam) { // Team set
					p = s.lastIndexOf(isCountryTeam ? ", " : " (") + 2;
					String tm = s.substring(p, s.length() - 1);
					if (StringUtils.notEmpty(date))
						date = (StringUtils.notEmpty(date) && date.matches(".*\\d{4}$") ? date.substring(date.length() - 4) : null);
					 sql = "SELECT * FROM team tm WHERE id_sport = ? and lower(label) = ?" + (date != null ? " and '" + date + "' BETWEEN year1 AND (CASE year2 WHEN NULL THEN '9999' WHEN '' THEN '9999' ELSE year2 END)" : "");
					Object o_ = DatabaseManager.loadEntity(sql, Arrays.asList(spid, tm.toLowerCase().replaceAll("'", "''")), Team.class);
					if (o_ == null) {
						Integer idTm = insertEntity(row, 50, spid, tm, null, cb, sb, lang);
						o_ = DatabaseManager.loadEntity(Team.class, idTm);
					}
					a.setTeam((Team)o_);
				}
				o = a;
				o = DatabaseManager.saveEntity(a, cb);
				msg = "New Athlete";
			}
			else if (n == 50) {
				s = s.replaceAll("\\[", "(").replaceAll("\\]", ")");
				if (!s.toLowerCase().matches(StringUtils.PATTERN_TEAM))
					throw new Exception(ResourceUtils.getText("err.invalid.team", lang).replaceAll("#S#", s));
				// Check if exists
				String regexp = StringUtils.toPatternString(s.indexOf(" (") > -1 ? s.substring(0, s.indexOf(" (")).toLowerCase() : s.toLowerCase());
				String sql = "SELECT T.id from team T WHERE T.id_sport = ? AND lower(label) ~ E'^" + regexp + "$' AND (link IS NULL OR link = 0)";
				List<Integer> lId = (List<Integer>) DatabaseManager.executeSelect(sql, Arrays.asList(spid), Integer.class);
				if (lId != null && lId.size() > 0)
					return lId.get(0);
				Team t = new Team();
				t.setLabel(s);
				t.setSport((Sport)DatabaseManager.loadEntity(Sport.class, spid));
				if (s.matches(".*\\([A-Z]{3}\\)$")) {
					int p = s.indexOf(" (") + 2;
					String countryCode = s.substring(p, p + 3).toLowerCase();
					Object o_ = DatabaseManager.loadEntity("SELECT * FROM country cn WHERE LOWER(cn.code) = ?", Arrays.asList(countryCode), Country.class);
					if (o_ == null)
						throw new Exception("Invalid Country: " + countryCode.toUpperCase());
					t.setLabel(s.substring(0, p - 2));
					t.setCountry((Country)o_);
				}
				o = t;
				o = DatabaseManager.saveEntity(t, cb);
				msg = "New Team";
			}
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			throw e;
		}
		finally {
			if (o != null) {
				if (sb != null)
					sb.append("Row " + (row + 1) + ": " + msg + " | " + o).append("<br/>");
				id = StringUtils.toInt(o.getClass().getMethod("getId").invoke(o));
			}
		}
		return id;
	}
	
	public static Integer insertPlace(int row, String s, Contributor cb, StringBuffer sb, String lang) throws Exception {
		Integer id = null;
		Object o = null;
		String msg = null;
		try {
			if (!s.toLowerCase().matches(StringUtils.PATTERN_PLACE))
				throw new Exception(ResourceUtils.getText("err.invalid.place", lang).replaceAll("#S#", s));
			String[] t = s.split("\\,\\s");
			String cx = null;
			String ct = null;
			String cn = t[t.length - 1];
			if (t.length > 2) {
				cx = t[0];
				ct = t[1];
			}
			else
				ct = t[0];
			City ct_ = null;
			if (cx != null) { // Set City (for complex)
				String sql = "SELECT * FROM city CT WHERE LOWER(CT.label" + ResourceUtils.getLocaleParam(lang) + ") LIKE ? and lower(country.code) = ?";
				Object o_ = DatabaseManager.loadEntity(sql, Arrays.asList(ct.toLowerCase().replaceAll("'", "''"), cn.toLowerCase()), City.class);
				if (o_ == null) {
					Integer idCt = insertPlace(row, ct + ", " + cn, cb, sb, lang);
					o_ = DatabaseManager.loadEntity(City.class, idCt);
				}
				ct_ = (City)o_;
			}
			Country cn_ = null;
			if (cn != null) { // Set Country
				Object o_ = DatabaseManager.loadEntity("SELECT * FROM country cn WHERE LOWER(cn.code) = ?", Arrays.asList(cn.toLowerCase()), Country.class);
				if (o_ == null)
					throw new Exception("Invalid Country: " + cn.toUpperCase());
				cn_ = (Country)o_;
			}
			if (cx != null) {
				// Check if exists
				String regexp1 = StringUtils.toPatternString(ct);
				String regexp2 = StringUtils.toPatternString(cx);
				String sql = "SELECT T.id FROM complex T "
						+ " LEFT JOIN city CT ON T.id_city = CT.id "
						+ " LEFT JOIN country CN ON CT.id_country = CN.id "
						+ " WHERE LOWER(CN.code) LIKE ? "
						+ " AND LOWER(CT.label) ~ E'^" + regexp1.replaceAll("'", "''") + "$' AND LOWER(T.label) ~ E'^" + regexp2.replaceAll("'", "''") + "$'";
				List<Integer> lId = (List<Integer>) DatabaseManager.executeSelect(sql, Arrays.asList(cn.toLowerCase()), Integer.class);
				if (lId != null && lId.size() > 0)
					return lId.get(0);
				
				Complex c = new Complex();
				c.setLabel(cx);
				c.setCity(ct_);
				o = c;
				o = DatabaseManager.saveEntity(c, cb);
				msg = "New Complex";
			}
			else if (ct != null) {
				// Check if exists
				String regexp = StringUtils.toPatternString(ct);
				String sql = "SELECT T.id FROM city T "
						+ " LEFT JOIN country CN ON T.id_country = CN.id "
						+ " WHERE LOWER(CN.code) LIKE ? "
						+ " AND LOWER(T.label) ~ E'^" + regexp.replaceAll("'", "''") + "$'";
				List<Integer> lId = (List<Integer>) DatabaseManager.executeSelect(sql, Arrays.asList(cn.toLowerCase()), Integer.class);
				if (lId != null && lId.size() > 0)
					return lId.get(0);
				
				City c = new City();
				c.setLabel(ct);
				c.setLabelFr(ct);
				c.setCountry(cn_);
				o = c;
				o = DatabaseManager.saveEntity(c, cb);
				msg = "New City";
			}
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			throw e;
		}
		finally {
			if (o != null) {
				if (sb != null)
					sb.append("Row " + (row + 1) + ": " + msg + " | " + o).append("<br/>");
				id = StringUtils.toInt(o.getClass().getMethod("getId").invoke(o));
			}
		}
		return id;
	}
	
}