package com.sporthenon.utils;

import java.util.HashMap;

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
	
	public static String getGoldHeader() {
		return "<table><tr><td><img src='" + getRenderUrl() + "gold.png'/></td><td class='bold'>Gold</td></tr></table>";
	}
	
	public static String getSilverHeader() {
		return "<table><tr><td><img src='" + getRenderUrl() + "silver.png'/></td><td class='bold'>Silver</td></tr></table>";
	}
	
	public static String getBronzeHeader() {
		return "<table><tr><td><img src='" + getRenderUrl() + "bronze.png'/></td><td class='bold'>Bronze</td></tr></table>";
	}
	
}
