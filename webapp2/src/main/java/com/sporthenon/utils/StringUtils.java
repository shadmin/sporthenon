package com.sporthenon.utils;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base32;
import org.jsoup.Jsoup;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.utils.res.ResourceUtils;

public class StringUtils {

	private static final Logger log = Logger.getLogger(StringUtils.class.getName());
	
	public static final String EMPTY = "-";
	public static final String SEP1 = "&ndash;";
	public static final String RARROW = "&#8209;";
	public static final String PATTERN_DATE = "^\\d{2}\\/\\d{2}\\/\\d{4}$";
	public static final String PATTERN_PLACE = "^([^\\,\\(\\)]+\\,\\s|)[^\\,\\(\\)]+(\\,\\s[a-z]{2}|)\\,\\s[a-z]{3}$|^[a-z]{3}$";
	public static final String PATTERN_ATHLETE = "^[^\\,\\(\\)]+(\\,\\s{1}[^\\s][^\\,\\(\\)]*)?\\s{1}\\([a-z]{3}(|\\,\\s{1}[^\\s][^\\,\\(\\)]+)\\)$";
	public static final String PATTERN_TEAM = "^[^\\,\\(\\)]+([^\\s]|\\s\\([a-z]{3}\\))$";
	public static final String PATTERN_COUNTRY = "^[a-z]{3}$";
	public static final String PATTERN_REVERT_NAME = "CHN|KOR|PRK|TPE|VIE";
	
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

	public static String join(Iterable<String> tValues, String sSeparator) {
		StringBuilder sb = new StringBuilder();
		for (String s : tValues)
			sb.append(sb.length() > 0 ? sSeparator : "").append(s);
		return sb.toString();
	}
	
	public static String repeat(String s, int count, String separator) {
		List<String> list = new ArrayList<String>();
		for (int i = 0 ; i < count ; i++) {
			list.add(s);
		}
		return join(list, separator);
	}

	public static int countIn(String s, String pattern) {
		return (s != null ? s.split(pattern).length - 1 : 0);
	}

	public static Integer toInt(Object o) {
		return (o != null && o instanceof Integer ? (Integer)o : (notEmpty(o) ? Integer.valueOf(String.valueOf(o)) : 0));
	}
	
	public static String toMD5(String s) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(), 0, s.length());
			return new BigInteger(1, m.digest()).toString(16);
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public static String toPatternString(String s) throws Exception {
		String regexp = s.replaceAll("\\_", ".").replaceAll("'", "''");
		regexp = regexp.replaceAll("\\,", "\\\\\\\\,");
		regexp = regexp.replaceAll("\\(", "\\\\\\\\\\\\\\\\(");
		regexp = regexp.replaceAll("\\)", "\\\\\\\\\\\\\\\\)");
		String sql = "SELECT _pattern_string(E'" + regexp + "')";
		List<String> l = (List<String>) DatabaseManager.executeSelect(sql, String.class);
		return l.get(0).replaceAll("'", "''");
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
		for (String s : t) {
			v.add(s);
		}
		for (int i = v.size() - 1 ; i >= 0 ; i--) {
			if (v.get(i) == null) {
				v.remove(i);
			}
		}
		String[] t_ = new String[t.length];
		for (int i = 0 ; i < t_.length ; i++) {
			t_[i] = (v.size() > i ? v.get(i) : null);
		}
		return t_;
	}

	public static String toTextDate(String dt, String lang, String format) throws ParseException {
		if (dt != null) {
			Locale l = (lang != null && lang.equalsIgnoreCase("fr") ? Locale.FRENCH : Locale.ENGLISH);
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			SimpleDateFormat dftxt = new SimpleDateFormat(notEmpty(format) ? format : "d MMM", l);
			if (dt != null) {
				if (!dt.matches(".*\\d\\d\\:\\d\\d$")) {
					dt += " 00:00";
				}
				dt = dftxt.format(df.parse(dt));
				dt = dt.substring(0, 1).toUpperCase() + dt.substring(1);
			}
		}
		return dt;
	}
	
	public static String toTextDate(Timestamp dt, String lang, String format) throws ParseException {
		if (dt != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return toTextDate(df.format(dt), lang, format);
		}
		return null;
	}

	public static String formatNumber(Object n, String lang) {
		Integer n_ = null;
		if (n instanceof Integer) {
			n_ = (Integer) n;
		}
		else {
			String s = String.valueOf(n);
			if (s.matches("\\d+")) {
				n_ = Integer.parseInt(s);
			}
		}
		if (n_ != null) {
			return ((DecimalFormat) NumberFormat.getNumberInstance(lang != null && lang.equalsIgnoreCase("fr") ? Locale.FRENCH : Locale.ENGLISH)).format(n_);
		}
		else {
			return (n != null ? String.valueOf(n) : "");
		}
	}
	
	public static String formatResult(Object s, String lang) {
		return formatNumber(s, lang).replaceAll("\\-", StringUtils.SEP1);
	}

	public static Integer extractId(Object o) {
		return (notEmpty(o) ? StringUtils.toInt(String.valueOf(o).replaceAll(".*\\s\\[#|\\]$", "")) : 0);
	}

	public static final String toTree(String s) {
		return (s.matches("^\\+{2}.*") ? "<span style=\"color:#666;\">&dagger; <i>" + s.substring(2) + "</i></span>" : s);
	}
	
	public static final String getSizeBytes(String s) {
		DecimalFormat df = new DecimalFormat("###,##0.00");
		return df.format(s != null ? Double.valueOf(s.getBytes().length / 1024.0d) : 0);
	}
	
	public static String normalize(String s) {
		final Pattern P = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");
	    s = Normalizer.normalize(s, Normalizer.Form.NFD);
	    s = P.matcher(s).replaceAll("");
	    return s;
	}
	
	public static final String getComment(String s, String lang) {
		String result = null;
		if (StringUtils.notEmpty(s) && !s.matches("\\#(SINGLE|DOUBLE|TRIPLE)\\#")) {
			String[] t = s.split("�", -1);
			result = t[t.length > 1 && lang.equalsIgnoreCase("fr") ? 1 : 0];
			result = result.replaceAll("\r\n|\\|", "<br/>");
			result = result.replaceFirst("^(FR|EN)\\:", "");
		}
		return result;
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
	
	public static final String getShortName(String name) {
		if (notEmpty(name)) {
			if (name.toLowerCase().matches(".+\\,\\s.+\\|.+")) {
				String[] t = name.split("\\,\\s", -1);
				if (name.matches(".*\\((" + PATTERN_REVERT_NAME + ")\\)")) {
					name = t[0] + (t.length > 1 && StringUtils.notEmpty(t[1]) ? " " + t[1].charAt(0) + "." : "");
				}
				else {
					name = (t.length > 1 && StringUtils.notEmpty(t[1]) ? t[1].charAt(0) + ". " : "") + t[0];
				}
			}
			name = name.replaceAll("\\|.*", "");
		}
		return name;
	}
	
	public static final String toFullName(String ln, String fn, String country, boolean uc) {
		String result = "";
		if (isRevertName(country, ln + " " + fn)) {
			result = (ln != null ? (uc ? ln.toUpperCase()  : ln) : "") + (StringUtils.notEmpty(fn) ? " " + fn : "");
		}
		else {
			result = (StringUtils.notEmpty(fn) ? fn + " " : "") + (ln != null ? (uc ? ln.toUpperCase()  : ln) : "");
		}
		return result;
	}
	
	public static boolean isRevertName(String country, String name) {
		return (notEmpty(country) && (country.matches(PATTERN_REVERT_NAME) || (country.equalsIgnoreCase("MAS") && name.split("\\s").length > 2)));
	}
	
	public static final String urlEscape(String s) {
		try {
			return (StringUtils.notEmpty(s) ? URLEncoder.encode(s, "UTF-8").replaceAll("%2F", "/").replaceAll("\\+", "-") : "");	
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
		return "";
	}
	
	public static String removeTags(String s) {
		return (notEmpty(s) ? Jsoup.parse(s).text() : s);
	}
	
	public static String getTitle(String s) {
		return s.replaceAll("\\-", StringUtils.SEP1) + (s.toLowerCase().startsWith("sporthenon") ? "" : " | Sporthenon");
	}
	
	public static String getRoundTypeLabel(String s) {
		return (s != null ? s.replaceFirst("\\s\\#.+", "") : null);
	}
	
}