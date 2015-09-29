package com.sporthenon.utils;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.sporthenon.utils.res.ResourceUtils;

public class ImageUtils {

	public static final short INDEX_SPORT = 0;
	public static final short INDEX_CHAMPIONSHIP = 1;
	public static final short INDEX_STATE = 2;
	public static final short INDEX_OLYMPICS = 3;
	public static final short INDEX_COUNTRY = 4;
	public static final short INDEX_TEAM = 5;
	public static final short INDEX_EVENT = 6;
	public static final short INDEX_SPORT_CHAMPIONSHIP = 7;
	public static final short INDEX_SPORT_EVENT = 8;
	
	public static final char SIZE_LARGE = 'L';
	public static final char SIZE_SMALL = 'S';
	
	private static HashMap<String, Short> hIndex;
	private static List<String> lImgFiles;
	
	static {
		hIndex = new HashMap<String, Short>();
		hIndex.put("SP", INDEX_SPORT);
		hIndex.put("CP", INDEX_CHAMPIONSHIP);
		hIndex.put("ST", INDEX_STATE);
		hIndex.put("OL", INDEX_OLYMPICS);
		hIndex.put("CN", INDEX_COUNTRY);
		hIndex.put("TM", INDEX_TEAM);
		hIndex.put("EV", INDEX_EVENT);
		hIndex.put("SPCP", INDEX_SPORT_CHAMPIONSHIP);
		hIndex.put("SPEV", INDEX_SPORT_EVENT);
		lImgFiles = new LinkedList<String>();
		try {
			for (File f : new File(ConfigUtils.getProperty("img.folder")).listFiles())
				lImgFiles.add(f.getName());
		}
		catch (Exception e) {}
	}

	public static short getIndex(String alias) {
		return (hIndex.containsKey(alias) ? hIndex.get(alias) : -1);
	}
	
	public static String getUrl() {
		return ConfigUtils.getProperty("img.url");
	}
	
	public static String getRenderUrl() {
		return "/img/render/";
	}
	
	public static String getGoldMedImg(String lang) {
		return "<img alt='Gold' title='" + ResourceUtils.getText("gold", lang) + "' src='" + getRenderUrl() + "gold.png?4'/>";
	}
	
	public static String getSilverMedImg(String lang) {
		return "<img alt='Silver' title='" + ResourceUtils.getText("silver", lang) + "' src='" + getRenderUrl() + "silver.png?4'/>";
	}
	
	public static String getBronzeMedImg(String lang) {
		return "<img alt='Bronze' title='" + ResourceUtils.getText("bronze", lang) + "' src='" + getRenderUrl() + "bronze.png?4'/>";
	}
	
	public static String getGoldHeader(String lang) {
		return "<table><tr><td><img alt='" + ResourceUtils.getText("gold", lang) + "' src='" + getRenderUrl() + "gold.png'/></td><td class='bold'>" + ResourceUtils.getText("gold", lang) + "</td></tr></table>";
	}
	
	public static String getSilverHeader(String lang) {
		return "<table><tr><td><img alt='" + ResourceUtils.getText("silver", lang) + "' src='" + getRenderUrl() + "silver.png'/></td><td class='bold'>" + ResourceUtils.getText("silver", lang) + "</td></tr></table>";
	}
	
	public static String getBronzeHeader(String lang) {
		return "<table><tr><td><img alt='" + ResourceUtils.getText("bronze", lang) + "' src='" + getRenderUrl() + "bronze.png'/></td><td class='bold'>" + ResourceUtils.getText("bronze", lang) + "</td></tr></table>";
	}
	
	public static Collection<String> getImgFiles() {
		return lImgFiles;
	}
	
	public static Collection<String> getImageList(short type, Object id, char size) {
		LinkedList<String> list = new LinkedList<String>();
		String name = type + "-" + id + "-" + size;
		for (String s : lImgFiles)
			if (s.indexOf(name) == 0)
				list.add(s);
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}

	public static String getPhotoFile(String entity, Object id) {
		String p = "photo" + StringUtils.encode(entity + "-" + id);
		LinkedList<String> list = new LinkedList<String>();
		for (String s : ImageUtils.getImgFiles())
			if (s.startsWith(p))
				list.add(s);
		Collections.sort(list);
		return (!list.isEmpty() ? list.getLast() : "");
	}
	
	public static StringBuffer getPhotoFieldset(String url, String source, String lang) {
		StringBuffer html = new StringBuffer();
		url = ConfigUtils.getProperty("img.url") + url;
		html.append("<li style='text-align:right;'><fieldset class='photo'>");
		html.append("<a href='" + url + "' target='_blank' title=\"" + ResourceUtils.getText("enlarge", lang) + "\"><img alt='Photo' height='230px' src='" + url + "'/></a></fieldset>");
		if (StringUtils.notEmpty(source))
			html.append("<span class='source'>" + ResourceUtils.getText("source", lang) + ":&nbsp;" + source + "</span>");
		html.append("</li>");
		return html;
	}

}