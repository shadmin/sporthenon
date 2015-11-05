package com.sporthenon.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Draw;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.utils.res.ResourceUtils;

public class ImportUtils {
	
	private static final String scPattern = "[^a-zA-Z0-9\\|\\,\\s\\(\\)_]";

	public static String processAll(HttpSession session, Vector<Vector<String>> vFile, boolean isUpdate, boolean isRS, boolean isDR, boolean isRC, Contributor cb, String lang) {
		StringBuffer html = new StringBuffer("<table>");
		StringBuffer report = new StringBuffer();
		try {
			Vector<String> vHeader = null;
			final HashMap<String, String> hTitle = new HashMap<String, String>();
			if (isRS) {
				vHeader = new Vector(Arrays.asList(new String[] {"msg", "sp", "cp", "ev", "se", "se2", "yr", "rk1", "rs1", "rk2", "rs2", "rk3", "rs3", "rk4", "rk5", "rk6", "rk7", "rk8", "rk9", "dt1", "dt2", "pl1", "pl2", "exa", "cmt", "exl"}));
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
				hTitle.put("exl", ResourceUtils.getText("ext.links", lang));
			}
			else if (isDR) {
				vHeader = new Vector(Arrays.asList(new String[] {"msg", "sp", "cp", "ev", "se", "se2", "yr", "qf1w", "qf1r", "qf1s", "qf2w", "qf2r", "qf2s", "qf3w", "qf3r", "qf3s", "qf4w", "qf4r", "qf4s", "sf1w", "sf1r", "sf1s", "sf2w", "sf2r", "sf2s", "thdw", "thdr", "thds"}));
				hTitle.put("msg", ResourceUtils.getText("message", lang));
				hTitle.put("sp", ResourceUtils.getText("entity.SP.1", lang));
				hTitle.put("cp", ResourceUtils.getText("entity.CP.1", lang));
				hTitle.put("ev", ResourceUtils.getText("entity.EV.1", lang) + " #1");
				hTitle.put("se", ResourceUtils.getText("entity.EV.1", lang) + " #2");
				hTitle.put("se2", ResourceUtils.getText("entity.EV.1", lang) + " #3");
				hTitle.put("yr", ResourceUtils.getText("entity.YR.1", lang));
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
			}
			else if (isRC) {
				vHeader = new Vector(Arrays.asList(new String[] {"msg", "sp", "cp", "ev", "se", "tp1", "tp2", "label", "rk1", "rc1", "dt1", "rk2", "rc2", "dt2", "rk3", "rc3", "dt3", "rk4", "rc4", "dt4", "rk5", "rc5", "dt5", "idx", "exa", "cmt"}));
				hTitle.put("msg", ResourceUtils.getText("message", lang));
				hTitle.put("sp", ResourceUtils.getText("entity.SP.1", lang));
				hTitle.put("cp", ResourceUtils.getText("entity.CP.1", lang));
				hTitle.put("ev", ResourceUtils.getText("entity.EV.1", lang));
				hTitle.put("se", ResourceUtils.getText("entity.SE.1", lang));
				hTitle.put("tp1", ResourceUtils.getText("type", lang) + " #1");
				hTitle.put("tp2", ResourceUtils.getText("type", lang) + " #2");
				hTitle.put("label", ResourceUtils.getText("name", lang));
				hTitle.put("rk1", ResourceUtils.getText("record.holder", lang));
				hTitle.put("rc1", ResourceUtils.getText("record2", lang));
				hTitle.put("dt1", ResourceUtils.getText("date", lang));
				hTitle.put("rk2", ResourceUtils.getText("rank.2", lang));
				hTitle.put("rc2", ResourceUtils.getText("record2", lang));
				hTitle.put("dt2", ResourceUtils.getText("date", lang));
				hTitle.put("rk3", ResourceUtils.getText("rank.3", lang));
				hTitle.put("rc3", ResourceUtils.getText("record2", lang));
				hTitle.put("dt3", ResourceUtils.getText("date", lang));
				hTitle.put("rk4", ResourceUtils.getText("rank.4", lang));
				hTitle.put("rc4", ResourceUtils.getText("record2", lang));
				hTitle.put("dt4", ResourceUtils.getText("date", lang));
				hTitle.put("rk5", ResourceUtils.getText("rank.5", lang));
				hTitle.put("rc5", ResourceUtils.getText("record2", lang));
				hTitle.put("dt5", ResourceUtils.getText("date", lang));
				hTitle.put("idx", "Index");
				hTitle.put("exa", ResourceUtils.getText("tie", lang));
				hTitle.put("cmt", ResourceUtils.getText("comment", lang));
			}
			html.append("<tr>");
			for (String s : vHeader)
				html.append("<th>").append(hTitle.get(s)).append("</th>");
			html.append("</tr>");
			int i = 0;
			int pg = 0;
			for (Vector<String> v : vFile) {
				v.insertElementAt("-", 0);
				if (isRS)
					processLineRS(i, vHeader, v, isUpdate, report, cb, lang);
				else if (isDR)
					processLineDR(i, vHeader, v, isUpdate, report, cb, lang);
				else if (isRC)
					processLineRC(i, vHeader, v, isUpdate, report, cb, lang);
				html.append("<tr><td>").append(StringUtils.implode(v, "</td><td>")).append("</td></tr>");
				if (i * 100 / vFile.size() > pg) {
					pg = i * 100 / vFile.size();
					session.setAttribute("progress", pg);
				}
				i++;
			}
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
		}
		html.append("</table>");
		if (isUpdate)
			html.append("<div id='ureport'>").append(report.toString()).append("</div>");
		return html.toString();
	}
	
	public static boolean processLineRS(int row, Vector<String> vHeader, Vector<String> vLine, boolean isUpdate, StringBuffer sb, Contributor cb, String lang) throws Exception {
		boolean isError = false;
		List<Integer> lId = null;
		List<Country> lCountries = DatabaseHelper.execute("from Country");
		HashMap<String, Integer> hId = new HashMap();
		Integer n = null;
		boolean isComplex1 = false;
		boolean isComplex2 = false;
		String tp = null;
		for (int i = 0 ; i < vLine.size() ; i++) {
			try {
				String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
				String s = vLine.get(i);
				String hql = null;
				if (StringUtils.notEmpty(s)) {
					if (s.matches(".*\\s\\[#\\d+]$"))
						s = s.substring(0, s.indexOf("[#") - 1);
					String s_ = s.replaceAll(scPattern, "_").toLowerCase();
					if (h.equalsIgnoreCase(Sport.alias))
						hql = "select id from Sport where lower(label) like '" + s_ + "'";
					else if (h.equalsIgnoreCase(Championship.alias))
						hql = "select id from Championship where lower(label) like '" + s_ + "'";
					else if (h.matches("ev|se|se2")) {
						String[] tEv = s_.split("\\|");
						hql = "select id from Event where lower(label) like '" + tEv[0] + "'" + (tEv.length > 1 ? " and lower(type.label)='" + tEv[1] + "'" : "") + " order by id";
						tp = (tEv.length > 1 ? tEv[1] : null);
					}
					else if (h.equalsIgnoreCase(Year.alias))
						hql = "select id from Year where lower(label) like '" + s_ + "'";
					else if (h.matches("pl\\d")) {
						String[] t = s.toLowerCase().split("\\,\\s");
						if (t.length < 2 || t.length > 4) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
						}
						else {
							String cx = null;
							String ct = null;
							String st = null;
							String cn = t[t.length - 1];
							if (t.length > 2 && t[t.length - 2].length() == 2)
								st = t[t.length - 2];
							if (t.length > (st != null ? 3 : 2)) {
								cx = t[0].replaceAll(scPattern, "_");
								ct = t[1].replaceAll(scPattern, "_");
							}
							else
								ct = t[0].replaceAll(scPattern, "_");
							if (cx != null) {
								h = "cx" + h.replaceAll("pl", "");
								hql = "select id from Complex where lower(city.country.code) like '" + cn + "' and lower(city.label) like '" + ct.replaceAll("'", "''") + "' and lower(label) like '" + cx.replaceAll("'", "''") + "'";
								if (h.equals("cx1"))
									isComplex1 = true;
								else
									isComplex2 = true;
							}
							else {
								h = "ct" + h.replaceAll("pl", "");
								hql = "select id from City where lower(country.code) like '" + cn + "' and lower(label) like '" + ct.replaceAll("'", "''") + "'";
							}
						}
					}
					else if (h.matches("rk\\d")) {
						if (n == null) {
							List<Integer> lNumber = (List<Integer>) DatabaseHelper.execute("select type.number from Event ev where ev.id = " + hId.get(hId.containsKey("se2") ? "se2" : (hId.containsKey("se") ? "se" : "ev")));
							if (lNumber != null && lNumber.size() > 0)
								n = lNumber.get(0);
							else if (tp != null)
								n = (tp.equalsIgnoreCase("country") ? 99 : (tp.equalsIgnoreCase("team") ? 50 : 1));
						}
						if (n != null) {
							if (n < 10) { // Athlete
								if (!s_.matches(StringUtils.PATTERN_ATHLETE)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
								}
								else {
									boolean isCountryTeam = s_.matches(".*\\([a-z]{3}\\,\\s.+\\)$");
									boolean isCountry = s_.matches(".*\\([a-z]{3}\\)$");
									boolean isTeam = (!isCountry && s_.matches(".*\\([^\\,\\(\\)]+\\)$")); 
									hql = "select id from Athlete where sport.id=" + hId.get("sp") + " and lower(last_name) || ', ' || lower(first_name) " + (isCountryTeam ? " || ' (' || lower(country.code) " + " || ', ' || lower(team.label) || ')'" : (isCountry ? " || ' (' || lower(country.code) || ')'" : (isTeam ? " || ' (' || lower(team.label) || ')'" : ""))) + " like '" + s_ + "'";
								}
							}
							else if (n == 50) { // Team
								if (!s_.matches(StringUtils.PATTERN_TEAM)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
								}
								else
									hql = "select id from Team where sport.id=" + hId.get("sp") + " and lower(label) like '" + (s_.indexOf(" (") > -1 ? s_.substring(0, s_.indexOf(" (")) : s_) + "' and (link is null or link = 0)";
							}
							else if (n == 99) { // Country
								if (!s_.matches(StringUtils.PATTERN_COUNTRY)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
								}
								else
									hql = "select id from Country where lower(code) like '" + s_ + "'";
							}
						}
					}
				}
				if (hql != null) {
//					Logger.getLogger("sh").info(hql);
					lId = (List<Integer>) DatabaseHelper.execute(hql);
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
						else if (h.matches("ev|se|se2"))
							writeError(vLine, ResourceUtils.getText("warning.event.notexist", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
						else if (h.equalsIgnoreCase(Year.alias)) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.invalid.year", lang));
						}
						else if (h.matches("(cx|ct)\\d") && StringUtils.notEmpty(s)) {
							Pattern pattern = Pattern.compile("[A-Z]{3}$");
							Matcher matcher = pattern.matcher(s);
							if (!matcher.find() || !lCountries.contains(new Country(matcher.group(0)))) {
								isError = true;
								writeError(vLine, ResourceUtils.getText("err.invalid.country", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
							}
						}
						else if (h.matches("rk\\d")) {
							if (n == 99) {
								isError = true;
								writeError(vLine, ResourceUtils.getText("err.invalid.country", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
							}
							else if (n == 50)
								writeError(vLine, ResourceUtils.getText("warning.team.notexist", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
							else {
								Pattern pattern = Pattern.compile("[A-Z]{3}");
								Matcher matcher = pattern.matcher(s);
								if (!matcher.find() || !lCountries.contains(new Country(matcher.group(0)))) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.country", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
								}
								boolean isCountryTeam = s.toLowerCase().matches(".*\\([a-z]{3}\\,\\s.+\\)$");
								if (isCountryTeam) {
									String tm = s.substring(s.lastIndexOf(", ") + 2);
									List l = DatabaseHelper.execute("from Team where lower(label)='" + tm.toLowerCase().replaceAll("\\)$", "") + "'");
									if (l == null || l.isEmpty())
										writeError(vLine, ResourceUtils.getText("warning.team.notexist", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
								}
							}
						}
						vLine.set(i, s);
					}
				}
				else {
					if (h.matches("dt\\d") && StringUtils.notEmpty(s) && !s.matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d")) {
						isError = true;
						writeError(vLine, ResourceUtils.getText("err.invalid.date", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
					}
					else if (h.matches("rs\\d") && StringUtils.notEmpty(s) && s.length() > (h.equalsIgnoreCase("rs1") ? 40 : 20)) {
						isError = true;
						writeError(vLine, ResourceUtils.getText("err.value.toolong", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
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
			Integer idCx2 = hId.get("cx2");
			Integer idCt2 = hId.get("ct2");
			Integer idRk1 = hId.get("rk1");
			Integer idRk2 = hId.get("rk2");
			Integer idRk3 = hId.get("rk3");
			Integer idRk4 = hId.get("rk4");
			Integer idRk5 = hId.get("rk5");
			Integer idRk6 = hId.get("rk6");
			Integer idRk7 = hId.get("rk7");
			Integer idRk8 = hId.get("rk8");
			Integer idRk9 = hId.get("rk9");
			String rs1 = null;String rs2 = null;String rs3 = null;String rs4 = null;String rs5 = null;
			String dt1 = null;String dt2 = null;
			String cmt = null;
			String exl = null;
			String exa = null;
			String err = null;
			try {
				for (int i = 0 ; i < vLine.size() ; i++) {
					String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
					String s = vLine.get(i);
					if (StringUtils.notEmpty(s)) {
						if(idEv == null && h.equalsIgnoreCase("ev"))
							idEv = DatabaseHelper.insertEvent(row, s, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idSe == null && h.equalsIgnoreCase("se"))
							idSe = DatabaseHelper.insertEvent(row, s, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idSe2 == null && h.equalsIgnoreCase("se2"))
							idSe2 = DatabaseHelper.insertEvent(row, s, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk1 == null && h.equalsIgnoreCase("rk1"))
							idRk1 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk2 == null && h.equalsIgnoreCase("rk2"))
							idRk2 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk3 == null && h.equalsIgnoreCase("rk3"))
							idRk3 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk4 == null && h.equalsIgnoreCase("rk4"))
							idRk4 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk5 == null && h.equalsIgnoreCase("rk5"))
							idRk5 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk6 == null && h.equalsIgnoreCase("rk6"))
							idRk6 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk7 == null && h.equalsIgnoreCase("rk7"))
							idRk7 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk8 == null && h.equalsIgnoreCase("rk8"))
							idRk8 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk9 == null && h.equalsIgnoreCase("rk9"))
							idRk9 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
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
						else if (idCx1 == null && idCt1 == null && h.matches("pl1")) {
							if (isComplex1)
								idCx1 = DatabaseHelper.insertPlace(row, s, cb, sb, ResourceUtils.LGDEFAULT);
							else
								idCt1 = DatabaseHelper.insertPlace(row, s, cb, sb, ResourceUtils.LGDEFAULT);
						}
						else if (idCx2 == null && idCt2 == null && h.matches("pl2")) {
							if (isComplex2)
								idCx2 = DatabaseHelper.insertPlace(row, s, cb, sb, ResourceUtils.LGDEFAULT);
							else
								idCt2 = DatabaseHelper.insertPlace(row, s, cb, sb, ResourceUtils.LGDEFAULT);
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
						else if (h.equalsIgnoreCase("exl"))
							exl = s;
					}
				}
			}
			catch (Exception e) {
				err = e.getMessage();
				Logger.getLogger("sh").error(e.getMessage(), e);
			}
			if (err != null)
				writeError(vLine, ResourceUtils.getText("error", lang).toUpperCase() + ": " + err);
			else {
				Result rs = new Result();
				rs.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, idSp));
				rs.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, idCp));
				rs.setEvent((Event)DatabaseHelper.loadEntity(Event.class, idEv != null ? idEv : 0));
				rs.setSubevent((Event)DatabaseHelper.loadEntity(Event.class, idSe != null ? idSe : 0));
				rs.setSubevent2((Event)DatabaseHelper.loadEntity(Event.class, idSe2 != null ? idSe2 : 0));
				rs.setYear((Year)DatabaseHelper.loadEntity(Year.class, idYr));
				rs.setComplex1((Complex)DatabaseHelper.loadEntity(Complex.class, idCx1));
				rs.setComplex2((Complex)DatabaseHelper.loadEntity(Complex.class, idCx2));
				rs.setCity1((City)DatabaseHelper.loadEntity(City.class, idCt1));
				rs.setCity2((City)DatabaseHelper.loadEntity(City.class, idCt2));
				rs.setDate1(StringUtils.notEmpty(dt1) ? dt1 : null);
				rs.setDate2(StringUtils.notEmpty(dt2) ? dt2 : null);
				rs.setExa(StringUtils.notEmpty(exa) ? exa : null);
				rs.setComment(StringUtils.notEmpty(cmt) ? cmt : null);
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
				rs = (Result) DatabaseHelper.saveEntity(rs, cb);
				if (StringUtils.notEmpty(exl))
					DatabaseHelper.saveExternalLinks(Result.alias, rs.getId(), exl);
				sb.append("Row " + (row + 1) + ": New Result | " + rs).append("<br/>");
			}
		}
		return isError;
	}

	public static boolean processLineDR(int row, Vector<String> vHeader, Vector<String> vLine, boolean isUpdate, StringBuffer sb, Contributor cb, String lang) throws Exception {
		boolean isError = false;
		List<Integer> lId = null;
		HashMap<String, Integer> hId = new HashMap();
		Integer n = null;
		Integer rsId = null;
		for (int i = 0 ; i < vLine.size() ; i++) {
			try {
				String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
				String s = vLine.get(i);
				String hql = null;
				if (StringUtils.notEmpty(s)) {
					if (s.matches(".*\\s\\[#\\d+]$"))
						s = s.substring(0, s.indexOf("[#") - 1);
					String s_ = s.replaceAll(scPattern, "_").toLowerCase();
					if (h.equalsIgnoreCase(Sport.alias))
						hql = "select id from Sport where lower(label) like '" + s_ + "'";
					else if (h.equalsIgnoreCase(Championship.alias))
						hql = "select id from Championship where lower(label) like '" + s_ + "'";
					else if (h.matches("ev|se|se2")) {
						String[] tEv = s_.split("\\|");
						hql = "select id from Event where lower(label) like '" + tEv[0] + "'" + (tEv.length > 1 ? " and lower(type.label)='" + tEv[1] + "'" : "") + " order by id";
					}
					else if (h.equalsIgnoreCase(Year.alias))
						hql = "select id from Result where year.label='" + s_ + "' and sport.id=" + hId.get("sp") + " and championship.id=" + hId.get("cp") + " and event.id=" + hId.get("ev") + (hId.containsKey("se") ? " and subevent.id=" + hId.get("se") : "") + (hId.containsKey("se2") ? " and subevent2.id=" + hId.get("se2") : "");
					else if (h.matches("(qf|sf|thd)\\d*(w|r)")) {
						if (n == null) {
							List<Integer> lNumber = (List<Integer>) DatabaseHelper.execute("select type.number from Event ev where ev.id = " + hId.get(hId.containsKey("se2") ? "se2" : (hId.containsKey("se") ? "se" : "ev")));
							if (lNumber != null && lNumber.size() > 0)
								n = lNumber.get(0);
						}
						if (n != null) {
							if (n < 10) { // Athlete
								if (!s_.matches(StringUtils.PATTERN_ATHLETE)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
								}
								else {
									boolean isCountryTeam = s_.matches(".*\\([a-z]{3}\\,\\s.+\\)$");
									boolean isCountry = s_.matches(".*\\([a-z]{3}\\)$");
									boolean isTeam = (!isCountry && s_.matches(".*\\([^\\,\\(\\)]+\\)$")); 
									hql = "select id from Athlete where sport.id=" + hId.get("sp") + " and lower(last_name) || ', ' || lower(first_name) " + (isCountryTeam ? " || ' (' || lower(country.code) " + " || ', ' || lower(team.label) || ')'" : (isCountry ? " || ' (' || lower(country.code) || ')'" : (isTeam ? " || ' (' || lower(team.label) || ')'" : ""))) + " like '" + s_ + "'";
								}
							}
							else if (n == 50) { // Team
								if (!s_.matches(StringUtils.PATTERN_TEAM)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
								}
								else
									hql = "select id from Team where sport.id=" + hId.get("sp") + " and lower(label) like '" + (s_.indexOf(" (") > -1 ? s_.substring(0, s_.indexOf(" (")) : s_) + "' and (link is null or link = 0)";
							}
							else if (n == 99) { // Country
								if (!s_.matches(StringUtils.PATTERN_COUNTRY)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
								}
								else
									hql = "select id from Country where lower(code) like '" + s_ + "'";
							}
						}
					}
				}
				if (hql != null) {
//					Logger.getLogger("sh").info(hql);
					lId = (List<Integer>) DatabaseHelper.execute(hql);
					if (lId != null && lId.size() > 0) {
						hId.put(h, lId.get(0));
						vLine.set(i, s + " [#" + lId.get(0) + "]");
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
						else if (h.equalsIgnoreCase(Year.alias)) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.result.notexist", lang));
						}
						else if (n == 99 && h.matches("(qf|sf|thd)\\d*(w|r)")) {
							isError = true;
							writeError(vLine, ResourceUtils.getText("err.invalid.country", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
						}
						vLine.set(i, s);
					}
				}
				else {
					if (h.matches(".*s$") && StringUtils.notEmpty(s) && s.length() > 40) {
						isError = true;
						writeError(vLine, ResourceUtils.getText("err.value.toolong", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
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
			rsId = hId.get("yr");
			Integer idSp = hId.get("sp");
			Integer id1Qf1 = hId.get("qf1w");Integer id2Qf1 = hId.get("qf1r");String sQf1 = (hId.get("qf1s") != null ? String.valueOf(hId.get("qf1s")) : null);
			Integer id1Qf2 = hId.get("qf2w");Integer id2Qf2 = hId.get("qf2r");String sQf2 = (hId.get("qf2s") != null ? String.valueOf(hId.get("qf2s")) : null);
			Integer id1Qf3 = hId.get("qf3w");Integer id2Qf3 = hId.get("qf3r");String sQf3 = (hId.get("qf3s") != null ? String.valueOf(hId.get("qf3s")) : null);
			Integer id1Qf4 = hId.get("qf4w");Integer id2Qf4 = hId.get("qf4r");String sQf4 = (hId.get("qf4s") != null ? String.valueOf(hId.get("qf4s")) : null);
			Integer id1Sf1 = hId.get("sf1w");Integer id2Sf1 = hId.get("sf1r");String sSf1 = (hId.get("sf1s") != null ? String.valueOf(hId.get("sf1s")) : null);
			Integer id1Sf2 = hId.get("sf2w");Integer id2Sf2 = hId.get("sf2r");String sSf2 = (hId.get("sf2s") != null ? String.valueOf(hId.get("sf2s")) : null);
			Integer id1Thd = hId.get("thdw");Integer id2Thd = hId.get("thdr");String sThd = (hId.get("thds") != null ? String.valueOf(hId.get("thds")) : null);
			String err = null;
			try {
				HashMap<String, Integer> hAlreadyInserted = new HashMap<String, Integer>();
				for (int i = 0 ; i < vLine.size() ; i++) {
					String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
					String s = vLine.get(i);
					if (StringUtils.notEmpty(s)) {
						if(id1Qf1 == null && h.equalsIgnoreCase("qf1w")) {
							if (hAlreadyInserted.containsKey(s))
								id1Qf1 = hAlreadyInserted.get(s);
							else {
								id1Qf1 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Qf1);	
							}
						}
						else if(id2Qf1 == null && h.equalsIgnoreCase("qf1r")) {
							if (hAlreadyInserted.containsKey(s))
								id2Qf1 = hAlreadyInserted.get(s);
							else {
								id2Qf1 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id2Qf1);
							}
						}
						else if(id1Qf2 == null && h.equalsIgnoreCase("qf2w")) {
							if (hAlreadyInserted.containsKey(s))
								id1Qf2 = hAlreadyInserted.get(s);
							else {
								id1Qf2 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Qf2);
							}
						}
						else if(id2Qf2 == null && h.equalsIgnoreCase("qf2r")) {
							if (hAlreadyInserted.containsKey(s))
								id2Qf2 = hAlreadyInserted.get(s);
							else {
								id2Qf2 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id2Qf2);
							}
						}
						else if(id1Qf3 == null && h.equalsIgnoreCase("qf3w")) {
							if (hAlreadyInserted.containsKey(s))
								id1Qf3 = hAlreadyInserted.get(s);
							else {
								id1Qf3 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Qf3);
							}
						}
						else if(id2Qf3 == null && h.equalsIgnoreCase("qf3r")) {
							if (hAlreadyInserted.containsKey(s))
								id2Qf3 = hAlreadyInserted.get(s);
							else {
								id2Qf3 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id2Qf3);
							}
						}
						else if(id1Qf4 == null && h.equalsIgnoreCase("qf4w")) {
							if (hAlreadyInserted.containsKey(s))
								id1Qf4 = hAlreadyInserted.get(s);
							else {
								id1Qf4 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Qf4);
							}
						}
						else if(id1Qf4 == null && h.equalsIgnoreCase("qf4r")) {
							if (hAlreadyInserted.containsKey(s))
								id1Qf4 = hAlreadyInserted.get(s);
							else {
								id1Qf4 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Qf4);
							}
						}
						else if(id1Sf1 == null && h.equalsIgnoreCase("sf1w")) {
							if (hAlreadyInserted.containsKey(s))
								id1Sf1 = hAlreadyInserted.get(s);
							else {
								id1Sf1 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Sf1);
							}
						}
						else if(id2Sf1 == null && h.equalsIgnoreCase("sf1r")) {
							if (hAlreadyInserted.containsKey(s))
								id2Sf1 = hAlreadyInserted.get(s);
							else {
								id2Sf1 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id2Sf1);
							}
						}
						else if(id1Sf2 == null && h.equalsIgnoreCase("sf2w")) {
							if (hAlreadyInserted.containsKey(s))
								id1Sf2 = hAlreadyInserted.get(s);
							else {
								id1Sf2 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Sf2);
							}
						}
						else if(id2Sf2 == null && h.equalsIgnoreCase("sf2r")) {
							if (hAlreadyInserted.containsKey(s))
								id2Sf2 = hAlreadyInserted.get(s);
							else {
								id2Sf2 = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id2Sf2);
							}
						}
						else if(id1Thd == null && h.equalsIgnoreCase("thdw")) {
							if (hAlreadyInserted.containsKey(s))
								id1Thd = hAlreadyInserted.get(s);
							else {
								id1Thd = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Thd);
							}
						}
						else if(id2Thd == null && h.equalsIgnoreCase("thdr")) {
							if (hAlreadyInserted.containsKey(s))
								id2Thd = hAlreadyInserted.get(s);
							else {
								id2Thd = DatabaseHelper.insertEntity(row, n, idSp, s, null, cb, sb, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id2Thd);
							}
						}
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf1w"))
							updateEntity(row, n, id1Qf1, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf1r"))
							updateEntity(row, n, id2Qf1, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf2w"))
							updateEntity(row, n, id1Qf2, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf2r"))
							updateEntity(row, n, id2Qf2, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf3w"))
							updateEntity(row, n, id1Qf3, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf3r"))
							updateEntity(row, n, id2Qf3, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf4w"))
							updateEntity(row, n, id1Qf4, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf4r"))
							updateEntity(row, n, id2Qf4, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("sf1w"))
							updateEntity(row, n, id1Sf1, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("sf1r"))
							updateEntity(row, n, id2Sf1, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("sf2w"))
							updateEntity(row, n, id1Sf2, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("sf2r"))
							updateEntity(row, n, id2Sf2, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("thdw"))
							updateEntity(row, n, id1Thd, s, sb, cb);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("thdr"))
							updateEntity(row, n, id2Thd, s, sb, cb);
						else if (h.equalsIgnoreCase("qf1s"))
							sQf1 = s;
						else if (h.equalsIgnoreCase("qf2s"))
							sQf2 = s;
						else if (h.equalsIgnoreCase("qf3s"))
							sQf3 = s;
						else if (h.equalsIgnoreCase("qf4s"))
							sQf4 = s;
						else if (h.equalsIgnoreCase("sf1s"))
							sSf1 = s;
						else if (h.equalsIgnoreCase("sf2s"))
							sSf2 = s;
						else if (h.equalsIgnoreCase("thds"))
							sThd = s;
					}
				}
			}
			catch (Exception e) {
				err = e.getMessage();
				Logger.getLogger("sh").error(e.getMessage(), e);
			}
			if (err != null)
				writeError(vLine, ResourceUtils.getText("error", lang).toUpperCase() + ": " + err);
			else {
				Draw dr = new Draw();
				dr.setIdResult(rsId);
				dr.setId1Qf1(id1Qf1); dr.setId2Qf1(id2Qf1); dr.setResult_qf1(sQf1);
				dr.setId1Qf2(id1Qf2); dr.setId2Qf2(id2Qf2); dr.setResult_qf2(sQf2);
				dr.setId1Qf3(id1Qf3); dr.setId2Qf3(id2Qf3); dr.setResult_qf3(sQf3);
				dr.setId1Qf4(id1Qf4); dr.setId2Qf4(id2Qf4); dr.setResult_qf4(sQf4);
				dr.setId1Sf1(id1Sf1); dr.setId2Sf1(id2Sf1); dr.setResult_sf1(sSf1);
				dr.setId1Sf2(id1Sf2); dr.setId2Sf2(id2Sf2); dr.setResult_sf2(sSf2);
				dr.setId1Thd(id1Thd); dr.setId2Thd(id2Thd); dr.setResult_thd(sThd);
				dr = (Draw) DatabaseHelper.saveEntity(dr, cb);
				sb.append("Row " + (row + 1) + ": New Draw | " + dr).append("<br/>");
			}
		}
		return isError;
	}
	
	public static boolean processLineRC(int row, Vector<String> vHeader, Vector<String> vLine, boolean isUpdate, StringBuffer sb, Contributor cb, String lang) throws Exception {
		boolean isError = false;
		List<Integer> lId = null;
		HashMap<String, Integer> hId = new HashMap();
		Integer n = null;
		Integer rcId = null;
		String label = null;
		String type1 = null, type2 = null;
		for (int i = 0 ; i < vLine.size() ; i++) {
			try {
				String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
				String s = vLine.get(i);
				String hql = null;
				if (StringUtils.notEmpty(s)) {
					if (s.matches(".*\\s\\[#\\d+]$"))
						s = s.substring(0, s.indexOf("[#") - 1);
					String s_ = s.replaceAll(scPattern, "_").toLowerCase();
					if (h.equalsIgnoreCase(Sport.alias))
						hql = "select id from Sport where lower(label) like '" + s_ + "'";
					else if (h.equalsIgnoreCase(Championship.alias))
						hql = "select id from Championship where lower(label) like '" + s_ + "'";
					else if (h.matches("ev|se")) {
						String[] tEv = s_.split("\\|");
						hql = "select id from Event where lower(label) like '" + tEv[0] + "'" + (tEv.length > 1 ? " and lower(type.label)='" + tEv[1] + "'" : "") + " order by id";
					}
					else if (h.equalsIgnoreCase("tp1"))
						type1 = s;
					else if (h.equalsIgnoreCase("tp2"))
						type2 = s;
					else if (h.matches("label")) {
						label = s;
						hql = "select id from Record where lower(label)='" + s_.replaceAll("'", "''") + "'" + (type1 != null ? " and lower(type1)='" + type1.toLowerCase() + "'" : "") + (type2 != null ? " and lower(type2)='" + type2.toLowerCase() + "'" : "") + " and sport.id=" + hId.get("sp") + " and championship.id=" + hId.get("cp") + " and event.id=" + hId.get("ev") + (hId.containsKey("se") ? " and subevent.id=" + hId.get("se") : "");
					}
					else if (h.matches("rk\\d")) {
						if (n == null) {
							List<Integer> lNumber = (List<Integer>) DatabaseHelper.execute("select type.number from Event ev where ev.id = " + hId.get(hId.containsKey("se") ? "se" : "ev"));
							if (lNumber != null && lNumber.size() > 0)
								n = lNumber.get(0);
						}
						if (n != null) {
							if (type1.equalsIgnoreCase("individual")) { // Athlete
								n = 1;
								if (!s_.matches(StringUtils.PATTERN_ATHLETE)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
								}
								else {
									boolean isCountryTeam = s_.matches(".*\\([a-z]{3}\\,\\s.+\\)$");
									boolean isCountry = s_.matches(".*\\([a-z]{3}\\)$");
									boolean isTeam = (!isCountry && s_.matches(".*\\([^\\,\\(\\)]+\\)$")); 
									hql = "select id from Athlete where sport.id=" + hId.get("sp") + " and lower(last_name) || ', ' || lower(first_name) " + (isCountryTeam ? " || ' (' || lower(country.code) " + " || ', ' || lower(team.label) || ')'" : (isCountry ? " || ' (' || lower(country.code) || ')'" : (isTeam ? " || ' (' || lower(team.label) || ')'" : ""))) + " like '" + s_ + "'";
								}
							}
							else if (type1.equalsIgnoreCase("team")) { // Team
								n = 50;
								if (!s_.matches(StringUtils.PATTERN_TEAM)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
								}
								else {
									// Get record year
									String rcy = null;
									if (vLine.size() > i + 2) {
										rcy = vLine.get(i + 2);
										rcy = (StringUtils.notEmpty(rcy) && rcy.matches(".*\\d{4}$") ? rcy.substring(rcy.length() - 4) : null);
									}
									hql = "select id from Team where sport.id=" + hId.get("sp") + " and lower(label) like '" + (s_.indexOf(" (") > -1 ? s_.substring(0, s_.indexOf(" (")) : s_) + "'" + (rcy != null ? " and '" + rcy + "' between year1 and (case year2 when null then '9999' when '' then '9999' else year2 end)" : "");
								}
							}
							else if (n == 99) { // Country
								if (!s_.matches(StringUtils.PATTERN_COUNTRY)) {
									isError = true;
									writeError(vLine, ResourceUtils.getText("err.invalid.format", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
								}
								else
									hql = "select id from Country where lower(code) like '" + s_ + "'";
							}
						}
					}
				}
				if (hql != null) {
//					Logger.getLogger("sh").info(hql);
					lId = (List<Integer>) DatabaseHelper.execute(hql);
					if (lId != null && lId.size() > 0) {
						hId.put(h, lId.get(0));
						vLine.set(i, s + " [#" + lId.get(0) + "]");
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
						vLine.set(i, s);
					}
				}
				else {
					if (h.matches("rc\\d") && StringUtils.notEmpty(s) && s.length() > 15) {
						isError = true;
						writeError(vLine, ResourceUtils.getText("error.value.toolong", lang) + " (" + ResourceUtils.getText("column", lang) + " " + h.toUpperCase() + ")");
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
			Float idx = null;
			String err = null;
			try {
				for (int i = 0 ; i < vLine.size() ; i++) {
					String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
					String s = vLine.get(i);
					if (StringUtils.notEmpty(s)) {
						if(idRk1 == null && h.equalsIgnoreCase("rk1"))
							idRk1 = DatabaseHelper.insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk2 == null && h.equalsIgnoreCase("rk2"))
							idRk2 = DatabaseHelper.insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk3 == null && h.equalsIgnoreCase("rk3"))
							idRk3 = DatabaseHelper.insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk4 == null && h.equalsIgnoreCase("rk4"))
							idRk4 = DatabaseHelper.insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, cb, sb, ResourceUtils.LGDEFAULT);
						else if(idRk5 == null && h.equalsIgnoreCase("rk5"))
							idRk5 = DatabaseHelper.insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, cb, sb, ResourceUtils.LGDEFAULT);
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
							idx = Float.valueOf(s);
						else if (h.equalsIgnoreCase("exa"))
							exa = s;
						else if (h.equalsIgnoreCase("cmt"))
							cmt = s;
					}
				}
			}
			catch (Exception e) {
				err = e.getMessage();
				Logger.getLogger("sh").error(e.getMessage(), e);
			}
			if (err != null)
				writeError(vLine, ResourceUtils.getText("error", lang).toUpperCase() + ": " + err);
			else {
				Record rc = null;
				if (rcId != null)
					rc = (Record) DatabaseHelper.loadEntity(Record.class, rcId);
				else {
					rc = new Record();
					rc.setLabel(label);
					rc.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, idSp));
					rc.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, idCp));
					rc.setEvent((Event)DatabaseHelper.loadEntity(Event.class, idEv != null ? idEv : 0));
					rc.setSubevent((Event)DatabaseHelper.loadEntity(Event.class, idSe != null ? idSe : 0));
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
				rc = (Record) DatabaseHelper.saveEntity(rc, cb);
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
				Athlete a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, id);
				String lastName = (s.substring(0, p > -1 ? p : s.indexOf(" (")));
				String firstName = (p > -1 ? s.substring(p + 2, s.indexOf(" (")) : null);
				if ((lastName + firstName).matches(".*" + scPattern + ".*") && (!lastName.equals(a.getLastName()) || !firstName.equals(a.getFirstName()))) {
					a.setLastName(lastName);
					a.setFirstName(firstName);
					o = DatabaseHelper.saveEntity(a, cb);
					msg = "Update Athlete";
				}
			}
			else if (n == 50) {
				Team t = (Team) DatabaseHelper.loadEntity(Team.class, id);
				String label = s;
				if (s.matches(".*\\([A-Z]{3}\\).*")) {
					int p = s.indexOf(" (") + 2;
					label = s.substring(0, p - 2);
				}
				if (label.matches(".*" + scPattern + ".*") && !label.equals(t.getLabel())) {
					t.setLabel(label);
					o = DatabaseHelper.saveEntity(t, cb);
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
		if (vLine != null && (!StringUtils.notEmpty(vLine.get(0)) || vLine.get(0).equals("-")))
			vLine.set(0, "<span class='" + (msg.startsWith("ERR") ? "red" : "orange") + "'>" + msg + "</span>");
	}
	
	public static List getTemplate(String type, String lang) {
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		if (type.equalsIgnoreCase(Result.alias)) {
			ArrayList<String> list_ = new ArrayList<String>();
			for (String s : new String[] {ResourceUtils.getText("sport", lang), ResourceUtils.getText("event", lang) + " 1", ResourceUtils.getText("event", lang) + " 2", ResourceUtils.getText("event", lang) + " 3", ResourceUtils.getText("event", lang) + " 4", ResourceUtils.getText("year", lang), ResourceUtils.getText("template.rs.winner", lang), ResourceUtils.getText("template.rs.score", lang), ResourceUtils.getText("template.rs.second", lang), ResourceUtils.getText("template.rs.result", lang) + " (" + ResourceUtils.getText("rank.2", lang) + ")", ResourceUtils.getText("template.rs.third", lang), ResourceUtils.getText("template.rs.result", lang) + " (" + ResourceUtils.getText("rank.3", lang) + ")", ResourceUtils.getText("rank.4", lang), ResourceUtils.getText("rank.5", lang), ResourceUtils.getText("rank.6", lang), ResourceUtils.getText("rank.7", lang), ResourceUtils.getText("rank.8", lang), ResourceUtils.getText("rank.9", lang), ResourceUtils.getText("template.date1", lang), ResourceUtils.getText("template.date2", lang), ResourceUtils.getText("template.place1", lang), ResourceUtils.getText("template.place2", lang), ResourceUtils.getText("tie", lang), ResourceUtils.getText("comment", lang), ResourceUtils.getText("extlinks", lang)})
				list_.add(s);
			list.add(list_);
			list.add(new ArrayList<String>());
			list_ = new ArrayList<String>();
			for (String s : new String[] {"Athletics", "Olympic Games", "Men", "100 M", "", "1936", "Owens, Jessie (USA)", "10.30", "Metcalfe, Ralf (USA)", "10.40", "Osendarp, Tinus (NED)", "10.50", "", "", "", "", "", "", "", "08/03/1936", "", "Olympiastadion, Berlin, GER", "", "", ""})
				list_.add(s);
			list.add(list_);
			list_ = new ArrayList<String>();
			for (String s : new String[] {"Tennis", "Grand Slam", "Men", "French Open", "", "2005", "Nadal, Rafael (ESP)", "6/7 6/3 6/1 7/5", "Puerta, Mariano (ARG)", "", "", "", "", "", "", "", "", "", "", "06/05/2005", "", "Roland Garros, Paris, FRA", "", "", ""})
				list_.add(s);
			list.add(list_);
			list_ = new ArrayList<String>();
			for (String s : new String[] {"Road Cycling", "UCI World Tour", "Milan-San Remo", "", "", "2004", "Cancellara, Fabian (SUI)", "", "Contador, Albero (ESP)", "Rodriguez, Joaquin (ESP)", "", "", "", "", "", "", "", "", "", "06/03/2004", "Milan, ITA", "San Remo, ITA", "", "", ""})
				list_.add(s);
			list.add(list_);
		}
		else if (type.equalsIgnoreCase(Draw.alias)) {
			ArrayList<String> list_ = new ArrayList<String>();
			for (String s : new String[] {ResourceUtils.getText("sport", lang), ResourceUtils.getText("event", lang) + " 1", ResourceUtils.getText("event", lang) + " 2", ResourceUtils.getText("event", lang) + " 3", ResourceUtils.getText("event", lang) + " 4", ResourceUtils.getText("year", lang), ResourceUtils.getText("quarterfinal", lang) + " #1 - " + ResourceUtils.getText("winner", lang), ResourceUtils.getText("quarterfinal", lang) + " #1 - " + ResourceUtils.getText("loser", lang), ResourceUtils.getText("quarterfinal", lang) + " #1 - " + ResourceUtils.getText("score", lang), ResourceUtils.getText("quarterfinal", lang) + " #2 - " + ResourceUtils.getText("winner", lang), ResourceUtils.getText("quarterfinal", lang) + " #2 - " + ResourceUtils.getText("loser", lang), ResourceUtils.getText("quarterfinal", lang) + " #2 - " + ResourceUtils.getText("score", lang), ResourceUtils.getText("quarterfinal", lang) + " #3 - " + ResourceUtils.getText("winner", lang), ResourceUtils.getText("quarterfinal", lang) + " #3 - " + ResourceUtils.getText("loser", lang), ResourceUtils.getText("quarterfinal", lang) + " #3 - " + ResourceUtils.getText("score", lang), ResourceUtils.getText("quarterfinal", lang) + " #4 - " + ResourceUtils.getText("winner", lang), ResourceUtils.getText("quarterfinal", lang) + " #4 - " + ResourceUtils.getText("loser", lang), ResourceUtils.getText("quarterfinal", lang) + " #4 - " + ResourceUtils.getText("score", lang), ResourceUtils.getText("semifinal", lang) + " #1 - " + ResourceUtils.getText("winner", lang), ResourceUtils.getText("semifinal", lang) + " #1 - " + ResourceUtils.getText("loser", lang), ResourceUtils.getText("semifinal", lang) + " #1 - " + ResourceUtils.getText("score", lang), ResourceUtils.getText("semifinal", lang) + " #2 - " + ResourceUtils.getText("winner", lang), ResourceUtils.getText("semifinal", lang) + " #2 - " + ResourceUtils.getText("loser", lang), ResourceUtils.getText("semifinal", lang) + " #2 - " + ResourceUtils.getText("score", lang), ResourceUtils.getText("third.place", lang) + " - " + ResourceUtils.getText("winner", lang), ResourceUtils.getText("third.place", lang) + " - Runner-up", ResourceUtils.getText("third.place", lang) + " - " + ResourceUtils.getText("score", lang)})
				list_.add(s);
			list.add(list_);
			list.add(new ArrayList<String>());
			list_ = new ArrayList<String>();
			for (String s : new String[] {"Football", "Word Cup", "Men", "Results", "", "2006", "GER", "ARG", "1-1 (4-2 ap.)", "ITA", "UKR", "3-0", "POR", "ENG", "0-0 (3-1 ap.)", "FRA", "BRA", "1-0", "ITA", "GER", "2-0", "FRA", "POR", "1-0", "GER", "POR", "3-1"})
				list_.add(s);
			list.add(list_);
			list_ = new ArrayList<String>();
			for (String s : new String[] {"American Football", "NFL", "Championships", "Super Bowl", "", "2000", "Tampa Bay Buccaneers", "Washington Redskins", "14-13", "Saint Louis Rams", "Minnesota Vikings", "49-37", "Tennessee Titans", "Indianapolis Colts", "19-16", "Jacksonville Jaguars", "Miami Dolphins", "62-7", "Saint Louis Rams", "Tampa Bay Buccaneers", "11-6", "Tennessee Titans", "Jacksonville Jaguars", "33-14", "", "", ""})
				list_.add(s);
			list.add(list_);
		}
		else if (type.equalsIgnoreCase(Record.alias)) {
			ArrayList<String> list_ = new ArrayList<String>();
			for (String s : new String[] {ResourceUtils.getText("sport", lang), ResourceUtils.getText("event", lang) + " 1", ResourceUtils.getText("event", lang) + " 2", ResourceUtils.getText("event", lang) + " 3", ResourceUtils.getText("type", lang) + " #1", ResourceUtils.getText("type", lang) + " #2", ResourceUtils.getText("record.label", lang), ResourceUtils.getText("record.holder", lang), ResourceUtils.getText("record2", lang) + " (" + ResourceUtils.getText("record.holder", lang) + ")", ResourceUtils.getText("date", lang) + " (" + ResourceUtils.getText("record.holder", lang) + ")", ResourceUtils.getText("record.holder", lang), ResourceUtils.getText("record2", lang) + " (" + ResourceUtils.getText("rank.2", lang) + ")", ResourceUtils.getText("date", lang) + " (" + ResourceUtils.getText("rank.2", lang) + ")", ResourceUtils.getText("rank.3", lang), ResourceUtils.getText("record2", lang) + " (" + ResourceUtils.getText("rank.3", lang) + ")", ResourceUtils.getText("date", lang) + " (" + ResourceUtils.getText("rank.3", lang) + ")", ResourceUtils.getText("rank.4", lang), ResourceUtils.getText("record2", lang) + " (" + ResourceUtils.getText("rank.4", lang) + ")", ResourceUtils.getText("date", lang) + " (" + ResourceUtils.getText("rank.4", lang) + ")", ResourceUtils.getText("rank.5", lang), ResourceUtils.getText("record2", lang) + " (" + ResourceUtils.getText("rank.5", lang) + ")", ResourceUtils.getText("date", lang) + " (" + ResourceUtils.getText("rank.5", lang) + ")", ResourceUtils.getText("index", lang), ResourceUtils.getText("type", lang), ResourceUtils.getText("comment", lang) })
				list_.add(s);
			list.add(list_);
			list.add(new ArrayList<String>());
			list_ = new ArrayList<String>();
			for (String s : new String[] {"Basketball", "NBA", "Rebounds", "", "Individual", "Game", "Most rebounds", "Chamberlain, Wilt (Philadelphia Warriors)", "55", "10/23/1962", "Russell, Bill (Boston Celtics)", "52", "04/02/1965", "Rodman, Dennis (Chicago Bulls)", "41", "10/10/1998", "", "", "", "", "", "", "1.0", "", ""})
				list_.add(s);
			list.add(list_);
		}
		return list;
	}

}