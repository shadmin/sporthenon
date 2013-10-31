package com.sporthenon.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.log4j.Logger;

public class StringUtils {

	public static final String EMPTY = "-";
	public static final String PATTERN_ATHLETE = "^[^\\,\\(\\)]+\\,\\s[^\\,\\(\\)]*([^\\s]|\\(([^\\,\\(\\)]+|[a-z]{3}\\,\\s[^\\,\\(\\)]+)\\))$";
	public static final String PATTERN_TEAM = "^[^\\,\\(\\)]+([^\\s]|\\s\\([a-z]{3}\\))$";
	public static final String PATTERN_COUNTRY = "^[a-z]{3}$";

	public static boolean notEmpty(Object obj) throws ClassCastException {
		return (obj != null && String.valueOf(obj).trim().length() > 0);
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
		if (s != null)
			for (String s_ : s.split(";")) {
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

	public static String toTextDate(String dt, Locale l) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		//SimpleDateFormat dftxt = new SimpleDateFormat("MMMM d, yyyy", l);
		SimpleDateFormat dftxt = new SimpleDateFormat("MMMM d", l);
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

}