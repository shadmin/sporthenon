package com.sporthenon.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base32;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.sporthenon.utils.res.ResourceUtils;

public class StringUtils {

	public static final String EMPTY = "-";
	public static final String PATTERN_PLACE = "^([^\\,\\(\\)]+\\,\\s|)[^\\,\\(\\)]+(\\,\\s[a-z]{2}|)\\,\\s[a-z]{3}$";
	public static final String PATTERN_ATHLETE = "^[^\\,\\(\\)]+\\,\\s[^\\,\\(\\)]*([^\\s]|\\(([^\\,\\(\\)]+|[a-z]{3}\\,\\s[^\\,\\(\\)]+)\\))$";
	public static final String PATTERN_TEAM = "^[^\\,\\(\\)]+([^\\s]|\\s\\([a-z]{3}\\))$";
	public static final String PATTERN_COUNTRY = "^[a-z]{3}$";
	public static final String PATTERN_REVERT_NAME = "CHN|KOR|PRK|TPE|MAS|VIE";
	
	public static boolean notEmpty(Object obj) throws ClassCastException {
		return (obj != null && String.valueOf(obj).trim().length() > 0);
	}

	public static String text(String key, HttpSession session) {
		return ResourceUtils.getText(key, String.valueOf(session.getAttribute("locale")));
	}
	
	public static String encode(String s) {
		Base32 b32 = new Base32();
		return b32.encodeAsString(s.replaceAll("(\\-0)+$", "").getBytes()).replaceAll("\\=+$", "");
	}

	public static String decode(String s) {
		if (s != null)
			while (s.length() % 4 > 0)
				s += "=";
		Base32 b32 = new Base32();
		return new String(b32.decode(s));
	}

	public static String implode(Iterable<String> tValues, String sSeparator) {
		StringBuffer sb = new StringBuffer();
		for (String s : tValues)
			sb.append((sb.toString().length() > 0 ? sSeparator : "") + s);
		return sb.toString();
	}

	public static int countIn(String s, String pattern) {
		return (s != null ? s.split(pattern).length - 1 : 0);
	}

	public static Integer toInt(Object o) {
		return (o instanceof Integer ? (Integer) o : (notEmpty(o) ? new Integer(String.valueOf(o)) : 0));
	}

	public static String toMD5(String s) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(), 0, s.length());
			return new BigInteger(1, m.digest()).toString(16);
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
		return s;
	}

	public static List<Integer> tieList(String s) {
		ArrayList<Integer> lst = new ArrayList<Integer>();
		if (StringUtils.notEmpty(s))
			for (String s_ : s.split("/")) {
				s_ = s_.replaceAll("\\D", "");
				int n1 = Integer.parseInt(String.valueOf(s_.charAt(0)));
				int n2 = (s_.length() > 1 ? Integer.parseInt(String.valueOf(s_.charAt(1))) : n1);
				if (lst.size() > 0)
					lst.add(-1);
				for (int i = n1 ; i <= n2 ; i++)
					lst.add(i);
			}
		return lst;
	}

	public static String[] removeNulls(String[] t) {
		Vector<String> v = new Vector<String>();
		for (String s : t)
			v.add(s);
		for (int i = v.size() - 1 ; i >= 0 ; i--)
			if (v.get(i) == null)
				v.remove(i);
		String[] t_ = new String[t.length];
		for (int i = 0 ; i < t_.length ; i++)
			t_[i] = (v.size() > i ? v.get(i) : null);
		return t_;
	}

	public static String toTextDate(String dt, String lang, String format) throws ParseException {
		Locale l = (lang != null && lang.equalsIgnoreCase("fr") ? Locale.FRENCH : Locale.ENGLISH);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		SimpleDateFormat dftxt = new SimpleDateFormat(notEmpty(format) ? format : "dd MMM", l);
		if (!dt.matches(".*\\d\\d\\:\\d\\d$"))
			dt += " 00:00";
		return dftxt.format(df.parse(dt)).replaceAll("\\s", "&nbsp;");
	}
	
	public static String toTextDate(Timestamp dt, String lang, String format) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return toTextDate(df.format(dt), lang, format);
	}

	public static String formatNumber(Integer n) {
		DecimalFormat df = new DecimalFormat("###,###.##");
		return df.format(n);
	}

	public static Integer extractId(Object o) {
		return (notEmpty(o) ? new Integer(String.valueOf(o).replaceAll(".*\\s\\[#|\\]$", "")) : 0);
	}

	public static Vector<Vector<String>> readCSV(String file) throws IOException {
		Vector v = new Vector<Vector<String>>();
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			String s = null;
			while ((s = bf.readLine()) != null) {
				if (StringUtils.notEmpty(s)) {
					Vector v_ = new Vector<String>();
					for (String s_ : s.split(";", -1))
						v_.add(s_.trim());
					v.add(v_);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (bf != null)
				bf.close();
		}
		return v;
	}
	
	public static final String toTree(String s) {
		return (s.matches("^\\+.*") ? "<font color=\"#666\">&dagger;&nbsp;<i>" + s.substring(1) + "</i></font>" : s);
	}
	
	public static final String getSizeBytes(String s) {
		DecimalFormat df = new DecimalFormat("###,##0.00");
		return df.format(s != null ? new Double(s.getBytes().length / 1024.0d) : 0);
	}
	
	public static String normalize(String s) {
		final Pattern P = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");
	    s = Normalizer.normalize(s, Normalizer.Form.NFD);
	    s = P.matcher(s).replaceAll("");
	    return s;
	}
	
	public static final String getCommentColor(String s) {
		String color = "#FFF";
		if (s.matches("^clay.*"))
			color = "#ffc24c";
		else if (s.matches("^cecoturf.*"))
			color = "#8cb9ff";
		else if (s.matches("^grass.*"))
			color = "#0F0";
		else if (s.matches("^gravel\\/T.*"))
			color = "#b4a676";
		else if (s.matches("^gravel.*"))
			color = "#dcc989";
		else if (s.matches("^hard.*"))
			color = "#8cb9ff";
		else if (s.matches("^rebound.*"))
			color = "#9dd4fc";
		else if (s.matches("^snow.*"))
			color = "#a3fffe";
		else if (s.matches("^tarmac.*"))
			color = "#c1c7c1";
		return color;
	}
	
	public static final String getUSPosition(Integer league, String pos) {
		HashMap<String, String> hPos1 = new HashMap<String, String>();
		HashMap<String, String> hPos2 = new HashMap<String, String>();
		HashMap<String, String> hPos3 = new HashMap<String, String>();
		HashMap<String, String> hPos4 = new HashMap<String, String>();
		hPos1.put("AD", "Administrator");
		hPos1.put("C", "Center");
		hPos1.put("CB", "Cornerback");
		hPos1.put("CH", "Coach");
		hPos1.put("CM", "Commissioner");
		hPos1.put("CT", "Contributor");
		hPos1.put("DE", "Defensive End");
		hPos1.put("DG", "Defensive Guard");
		hPos1.put("DT", "Defensive Tackle");
		hPos1.put("E", "End");
		hPos1.put("FB", "Fullback");
		hPos1.put("FK", "Flanker");
		hPos1.put("G", "Guard");
		hPos1.put("HB", "Halfback");
		hPos1.put("K", "Kicker");
		hPos1.put("KR", "Kick Returner");
		hPos1.put("LB", "Linebacker");
		hPos1.put("O", "Franchise Owner");
		hPos1.put("OE", "Offensive End");
		hPos1.put("OF", "Official");
		hPos1.put("OG", "Offensive Guard");
		hPos1.put("OT", "Offensive Tackle");
		hPos1.put("QB", "Quarterback");
		hPos1.put("RB", "Runningback");
		hPos1.put("S", "Safety");
		hPos1.put("T", "Tackle");
		hPos1.put("TE", "Tight End");
		hPos1.put("WR", "Wide Receiver");
		hPos2.put("C", "Center");
		hPos2.put("CH", "Coach");
		hPos2.put("F", "Forward");
		hPos2.put("G", "Guard");
		hPos2.put("PF", "Power Forward");
		hPos2.put("PG", "Point Guard");
		hPos2.put("SF", "Small Forward");
		hPos2.put("SG", "Shooting Guard");
		hPos3.put("C", "Center");
		hPos3.put("D", "Defenseman");
		hPos3.put("F", "Forward");
		hPos3.put("G", "Goaltender");
		hPos3.put("LW", "Left Wing");
		hPos3.put("OC", "Official");
		hPos3.put("RW", "Right Wing");
		hPos3.put("R", "Rover");
		hPos4.put("P", "Pitcher");
		hPos4.put("C", "Catcher");
		hPos4.put("1B", "1st Baseman");
		hPos4.put("2B", "2nd Baseman");
		hPos4.put("3B", "3rd Baseman");
		hPos4.put("SS", "Shortstop");
		hPos4.put("LF", "Left Fielder");
		hPos4.put("CF", "Center Fielder");
		hPos4.put("RF", "Right Fielder");
		hPos4.put("MGR", "Manager");
		hPos4.put("EX", "Executive");
		hPos4.put("PIO", "Pioneer Contributor");
		hPos3.put("OF", "Official");
		HashMap<String, String> hm = (league == 1 ? hPos1 : (league == 2 ? hPos2 : (league == 3 ? hPos3 : hPos4)));
		return (hm.containsKey(pos) ? hm.get(pos) : pos);
	}
	
	public static HashMap<String, String> getDrawLabels(int sp, int cp, int ev, String lang) {
		HashMap<String, String> h = new HashMap<String, String>();
		if (sp == 23 && cp == 51 && ev == 455) { // NFL
			h.put("Qf1", "NFC Divisional");
			h.put("Qf2", "NFC Divisional");
			h.put("Qf3", "AFC Divisional");
			h.put("Qf4", "AFC Divisional");
			h.put("Sf1", "NFC Championship");
			h.put("Sf2", "AFC Championship");
			h.put("F", "Super Bowl");
		}
		else if ((sp == 24 || sp == 25) && (cp == 54 || cp == 55) &&  ev == 455) { // NBA,NHL
			h.put("Qf1", ResourceUtils.getText("eastern.conf", lang) + " – " + ResourceUtils.getText("semifinal", lang));
			h.put("Qf2", ResourceUtils.getText("eastern.conf", lang) + " – " + ResourceUtils.getText("semifinal", lang));
			h.put("Qf3", ResourceUtils.getText("western.conf", lang) + " – " + ResourceUtils.getText("semifinal", lang));
			h.put("Qf4", ResourceUtils.getText("western.conf", lang) + " – " + ResourceUtils.getText("semifinal", lang));
			h.put("Sf1", ResourceUtils.getText("eastern.conf", lang) + " – " + ResourceUtils.getText("final", lang));
			h.put("Sf2", ResourceUtils.getText("western.conf", lang) + " – " + ResourceUtils.getText("final", lang));
			h.put("F", sp == 24 ? ResourceUtils.getText("nba.finals", lang) : ResourceUtils.getText("nhl.finals", lang));
		}
		else if (sp == 26 && cp == 56 &&  ev == 455) { // MLB
			h.put("Qf1", "NLDS");
			h.put("Qf2", "NLDS");
			h.put("Qf3", "ALDS");
			h.put("Qf4", "ALDS");
			h.put("Sf1", "NLCS");
			h.put("Sf2", "ALCS");
			h.put("F", "World Series");
		}
		else {
			h.put("Qf1", ResourceUtils.getText("quarterfinal", lang) + " #1");
			h.put("Qf2", ResourceUtils.getText("quarterfinal", lang) + " #2");
			h.put("Qf3", ResourceUtils.getText("quarterfinal", lang) + " #3");
			h.put("Qf4", ResourceUtils.getText("quarterfinal", lang) + " #4");
			h.put("Sf1", ResourceUtils.getText("semifinal", lang) + " #1");
			h.put("Sf2", ResourceUtils.getText("semifinal", lang) + " #2");
			h.put("F", ResourceUtils.getText("final", lang).toUpperCase());
			h.put("Thd", ResourceUtils.getText("third.place", lang));
		}
		return h;
	}

	public static final String getShortName(String name) {
		if (notEmpty(name) && name.matches(PATTERN_ATHLETE)) {
			String[] t = name.replaceAll("\\s\\(.*", "").split("\\,\\s", -1);
			String suffix = (name.matches(".*\\s\\(.*") ? name.substring(name.indexOf(" (")) : "");
			if (name.matches(".*\\((" + PATTERN_REVERT_NAME + ")\\)"))
				name = t[0] + (t.length > 1 && StringUtils.notEmpty(t[1]) ? "&nbsp;" + t[1].charAt(0) + "." : "") + suffix;
			else
				name = (t.length > 1 && StringUtils.notEmpty(t[1]) ? t[1].charAt(0) + ".&nbsp;" : "") + t[0] + suffix;
		}
		return name;
	}
	
	public static final String toFullName(String ln, String fn, String country, boolean uc) {
		String result = "";
		if (notEmpty(country) && country.matches(PATTERN_REVERT_NAME))
			result = (ln != null ? (uc ? ln.toUpperCase()  : ln) : "") + (StringUtils.notEmpty(fn) ? HtmlUtils.SPACE + fn : "");
		else
			result = (StringUtils.notEmpty(fn) ? fn + HtmlUtils.SPACE : "") + (ln != null ? (uc ? ln.toUpperCase()  : ln) : "");
		return result;
	}
	
	public static final String urlEscape(String s) {
		return (StringUtils.notEmpty(s) ? s.replaceAll("\\s\\-\\s|\\&nbsp\\;\\-\\&nbsp\\;", "/").replaceAll("\\s", "-").replaceAll("%", "%25") .replaceAll("'", "%27"): "");
	}
	
	public static String removeTags(String s) {
		return (notEmpty(s) ? Jsoup.parse(s).text() : s);
	}
	
	public static String getTitle(String s) {
		return s.replaceAll("\\-", "–") + " | Sporthenon";
	}
	
}