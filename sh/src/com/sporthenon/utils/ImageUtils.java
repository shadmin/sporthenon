package com.sporthenon.utils;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import com.sporthenon.utils.res.ResourceUtils;

public class ImageUtils {

	public static final short INDEX_SPORT = 0;
	public static final short INDEX_CHAMPIONSHIP = 1;
	public static final short INDEX_STATE = 2;
	public static final short INDEX_OLYMPICS = 3;
	public static final short INDEX_COUNTRY = 4;
	public static final short INDEX_TEAM = 5;
	public static final short INDEX_EVENT = 6;
	
	public static final char SIZE_LARGE = 'L';
	public static final char SIZE_SMALL = 'S';
	
	private static HashMap<String, Short> hIndex;
	
	static {
		hIndex = new HashMap<String, Short>();
		hIndex.put("SP", INDEX_SPORT);
		hIndex.put("CP", INDEX_CHAMPIONSHIP);
		hIndex.put("ST", INDEX_STATE);
		hIndex.put("OL", INDEX_OLYMPICS);
		hIndex.put("CN", INDEX_COUNTRY);
		hIndex.put("TM", INDEX_TEAM);
		hIndex.put("EV", INDEX_EVENT);
	}
	
	public static short getIndex(String alias) {
		return (hIndex.containsKey(alias) ? hIndex.get(alias) : -1);
	}
	
	public static String getUrl() {
		return ConfigUtils.getProperty("img.url");
	}
	
	public static String getRenderUrl() {
		return "img/render/";
	}
	
	public static String getGoldMedImg() {
		return "<img title='Gold' src='" + getRenderUrl() + "gold-mini.png?2'/>";
	}
	
	public static String getSilverMedImg() {
		return "<img title='Silver' src='" + getRenderUrl() + "silver-mini.png?2'/>";
	}
	
	public static String getBronzeMedImg() {
		return "<img title='Bronze' src='" + getRenderUrl() + "bronze-mini.png?2'/>";
	}
	
	public static String getGoldHeader(String lang) {
		return "<table><tr><td><img src='" + getRenderUrl() + "gold.png'/></td><td class='bold'>" + ResourceUtils.getText("gold", lang) + "</td></tr></table>";
	}
	
	public static String getSilverHeader(String lang) {
		return "<table><tr><td><img src='" + getRenderUrl() + "silver.png'/></td><td class='bold'>" + ResourceUtils.getText("silver", lang) + "</td></tr></table>";
	}
	
	public static String getBronzeHeader(String lang) {
		return "<table><tr><td><img src='" + getRenderUrl() + "bronze.png'/></td><td class='bold'>" + ResourceUtils.getText("bronze", lang) + "</td></tr></table>";
	}
	
	public static Collection<String> getImageList(short type, int id, char size) {
		LinkedList<String> list = new LinkedList<String>();
		String folder = ConfigUtils.getProperty("img.folder");
		String name = type + "-" + id + "-" + size;
		for (File f : new File(folder).listFiles())
			if (f.getName().indexOf(name) == 0)
				list.add(f.getName());
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}
	
}