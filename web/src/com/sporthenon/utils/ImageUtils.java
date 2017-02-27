package com.sporthenon.utils;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.meta.Picture;
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
			Collections.sort(lImgFiles);
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
	
	public static List<String> getImgFiles() {
		return lImgFiles;
	}
	
	public static Collection<String> getImageList(short type, Object id, char size) {
		LinkedList<String> list = new LinkedList<String>();
		String name = type + "-" + id + "-" + size;
		boolean found = false;
		for (String s : lImgFiles) {
			if (s.indexOf(name) == 0) {
				list.add(s);
				found = true;
			}
			else if (found)
				break;
		}
		Collections.reverse(list);
		return list;
	}

	public static StringBuffer getPhotos(String entity, Object id, String lang) throws Exception {
		final int MAX_WIDTH = Integer.parseInt(ConfigUtils.getValue("max_photo_width"));
		final int MAX_HEIGHT = Integer.parseInt(ConfigUtils.getValue("max_photo_height"));
		StringBuffer sb = new StringBuffer();
		for (Picture p : (List<Picture>) DatabaseHelper.execute("from Picture where entity='" + entity + "' and idItem=" + id + "order by id")) {
			sb.append("<li id='ph-" + p.getId() + "'>");
			if (p.isEmbedded()) {
				String value = p.getValue().replaceAll("%", "px");
				value = value.replaceAll("height\\:100px\\;", "");
				value = value.replaceAll("width\\:100px\\;", "width:" + MAX_WIDTH + "px;height:" + MAX_HEIGHT + "px;");
				value = value.replaceFirst("padding:[\\d\\.]+px", "padding:0");
				sb.append(value);
			}
			else
				sb.append("<img alt='" + (StringUtils.notEmpty(p.getSource()) ? p.getSource() : "") + "' src='" + ImageUtils.getUrl() + p.getValue() + "'/>");
			sb.append("<div class='enlarge'><a href='javascript:enlargePhoto(" + p.getId() + ");'><img src='" + getRenderUrl() + "enlarge.png' title='" + ResourceUtils.getText("enlarge", lang) + "'/></a></div></li>");
		}
		return (sb.length() > 0 ? sb : null);
	}
	
}