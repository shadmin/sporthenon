package com.sporthenon.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
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

import org.apache.log4j.Logger;

import com.sporthenon.utils.res.ResourceUtils;

public class StringUtils {

	public static final String EMPTY = "-";
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
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat dftxt = new SimpleDateFormat(notEmpty(format) ? format : "dd MMMM", l);
		return dftxt.format(df.parse(dt));
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
		return s.replaceAll("^\\+", "&dagger;&nbsp;");
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
		if (s.matches("^Clay.*"))
			color = "#ffc24c";
		else if (s.matches("^Decoturf.*"))
			color = "#8cb9ff";
		else if (s.matches("^Grass.*"))
			color = "#0F0";
		else if (s.matches("^Gravel\\/T.*"))
			color = "#b4a676";
		else if (s.matches("^Gravel.*"))
			color = "#dcc989";
		else if (s.matches("^Hard.*"))
			color = "#8cb9ff";
		else if (s.matches("^Rebound.*"))
			color = "#9dd4fc";
		else if (s.matches("^Snow.*"))
			color = "#a3fffe";
		else if (s.matches("^Tarmac.*"))
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

	public static final String getShortName(String name) {
		if (notEmpty(name) && name.matches(PATTERN_ATHLETE)) {
			String[] t = name.replaceAll("\\s\\(.*", "").split("\\,\\s", -1);
			String suffix = (name.matches(".*\\s\\(.*") ? name.substring(name.indexOf(" (")) : "");
			if (name.matches(".*\\((" + PATTERN_REVERT_NAME + ")\\)"))
				name = t[0] + (t.length > 1 && StringUtils.notEmpty(t[1]) ? "&nbsp;" + t[1].charAt(0) + "." : "") + suffix;
			else
				name = (t.length > 1 && StringUtils.notEmpty(t[1]) ? t[1].charAt(0) + ".&nbsp;" : "") + t[0] + suffix;
			//name = t[0] + ",&nbsp;" + t[1].charAt(0) + "." + (name.matches(".*\\s\\(.*") ? name.substring(name.indexOf(" (")) : "");
		}
		return name;
	}
	
}