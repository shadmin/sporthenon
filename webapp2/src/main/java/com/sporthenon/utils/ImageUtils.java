package com.sporthenon.utils;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.meta.Picture;
import com.sporthenon.utils.res.ResourceUtils;

@SuppressWarnings("unchecked")
public class ImageUtils {

	private static final Logger log = Logger.getLogger(ImageUtils.class.getName());
	
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
	
	private static Map<String, Short> mapIndex;
	private static List<String> imgFiles;
	
	static {
		mapIndex = new HashMap<String, Short>();
		mapIndex.put("SP", INDEX_SPORT);
		mapIndex.put("CP", INDEX_CHAMPIONSHIP);
		mapIndex.put("ST", INDEX_STATE);
		mapIndex.put("OL", INDEX_OLYMPICS);
		mapIndex.put("CN", INDEX_COUNTRY);
		mapIndex.put("TM", INDEX_TEAM);
		mapIndex.put("EV", INDEX_EVENT);
		mapIndex.put("SPCP", INDEX_SPORT_CHAMPIONSHIP);
		mapIndex.put("SPEV", INDEX_SPORT_EVENT);
	}

	public static short getIndex(String alias) {
		return (mapIndex.containsKey(alias) ? mapIndex.get(alias) : -1);
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
		if (imgFiles == null) {
			imgFiles = new LinkedList<String>();
			try {
				final String imgFolder = ConfigUtils.getProperty("img.folder");
				File[] files = new File(imgFolder).listFiles();
				for (File f : files)
					imgFiles.add(f.getName());
				Collections.sort(imgFiles);
			}
			catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return imgFiles;
	}
	
	public static Collection<String> getImageList(short type, Object id, char size) {
		LinkedList<String> list = new LinkedList<String>();
		String name = type + "-" + id + "-" + size;
		boolean found = false;
		for (String s : imgFiles) {
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
		for (Picture p : (List<Picture>) DatabaseManager.executeSelect("SELECT * FROM _picture WHERE entity='" + entity + "' AND id_item=" + id + " ORDER BY id", Picture.class)) {
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