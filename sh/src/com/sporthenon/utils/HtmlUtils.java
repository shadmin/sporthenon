package com.sporthenon.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.db.function.WinRecordsBean;
import com.sporthenon.utils.res.ResourceUtils;

public class HtmlUtils {

	public final static String SPACE = " ";

	public static String writeNoResult(String lang) {
		return "<div class='noresult'>" + ResourceUtils.getText("no.result", lang) + "</div>";
	}
	
	public static String writeImage(short type, int id, char size, String year, String title) {
		StringBuffer html = new StringBuffer();
		String folder = ConfigUtils.getProperty("img.folder");
		String name = type + "-" + id + "-" + size;
		LinkedList<String> list = new LinkedList<String>();
		for (File f : new File(folder).listFiles())
			if (f.getName().indexOf(name) == 0) {
				boolean isInclude = true;
				if (StringUtils.notEmpty(year)) {
					String[] t = f.getName().replaceAll("^" + name + "(\\_|)|(gif|png)$|(\\_\\d+|)\\.", "").split("\\-");
					if (t.length > 1) {
						Integer y = Integer.parseInt(year);
						Integer y1 = Integer.parseInt(t[0].equalsIgnoreCase("X") ? "0" : t[0]);
						Integer y2 = Integer.parseInt(t[1].equalsIgnoreCase("X") ? "5000" : t[1]);
						isInclude = (y >= y1 && y <= y2);
					}
				}
				else
					isInclude = !f.getName().matches(".*\\d{4}\\-\\d{4}\\.(gif|png)$");
				if (isInclude)
					list.add(f.getName());
			}
		Collections.sort(list);
		if (!list.isEmpty())
			html.append("<img alt=''" + (StringUtils.notEmpty(title) ? " title=\"" + title + "\"" : "") + " src='" + ImageUtils.getUrl() + list.getLast() + "'/>");
		else if (size == ImageUtils.SIZE_LARGE && type != ImageUtils.INDEX_CHAMPIONSHIP && type != ImageUtils.INDEX_EVENT)
			html.append("<img alt='' src='img/noimage.png'/>");
		return html.toString();
	}

	public static String writeLink(String alias, int id, String text, String title) {
		StringBuffer html = new StringBuffer();
		html.append("<a href='ref?p=" + StringUtils.encode(alias + "-" + id) + "'" + (StringUtils.notEmpty(title) ? " title=\"" + title + "\"" : "") + ">" + (text != null ? text.replaceAll("\\s", SPACE) : "") + "</a>");
		return html.toString();
	}

	public static String writeImgTable(String img, String text) {
		StringBuffer html = new StringBuffer();
		if (StringUtils.notEmpty(img)) {
			html.append("<table><tr><th>" + img + "</th>");
			html.append("<td>" + text + "</td></tr></table>");
		}
		else
			html.append(text);
		return html.toString();
	}

	public static String writeToggleTitle(String s) {
		StringBuffer html = new StringBuffer();
		html.append("<img alt='' src='" + ImageUtils.getRenderUrl() + "collapse.gif' class='toggleimg' onclick='toggleContent(this)'/>");
		html.append("<span class='toggletext' onclick='toggleContent(this)'>" + s + "</span>");
		return html.toString();
	}

	public static StringBuffer writeHeader(HashMap<String, String> h, String lang) {
		StringBuffer html = new StringBuffer();
		html.append("<span class='title'>" + h.get("title") + "</span>");
		if (h.containsKey("url"))
			html.append("<span class='url'>" + h.get("url") + "</span>");
		html.append("<span class='infostats'>" + h.get("info") + "</span>");
		html.append("<div class='header'><table><tr><td style='font-weight:bold;'>" + h.get("item0") + "</td>");
		html.append(h.containsKey("item1") ? "<td class='arrow'>&nbsp;</td><td>" + h.get("item1") + "</td>" : "");
		html.append(h.containsKey("item2") ? "<td class='arrow'>&nbsp;</td><td>" + h.get("item2") + "</td>" : "");
		html.append(h.containsKey("item3") ? "<td class='arrow'>&nbsp;</td><td>" + h.get("item3") + "</td>" : "");
		html.append(h.containsKey("item4") ? "<td class='arrow'>&nbsp;</td><td>" + h.get("item4") + "</td>" : "");
		html.append(h.containsKey("item5") ? "<td class='arrow'>&nbsp;</td><td>" + h.get("item5") + "</td>" : "");
		html.append("</tr></table></div>");
		html.append("<div class='toolbar'>");
		html.append("<table><tr><td><input id='export' type='button' class='button export' onclick='displayExport();' value='" + ResourceUtils.getText("button.export", lang) + "'/></td>");
		html.append("<td><input id='link' type='button' class='button link' onclick='displayLink();' value='" + ResourceUtils.getText("button.link", lang) + "'/></td>");
		html.append("<td><input id='print' type='button' class='button print' onclick='javascript:printCurrentTab();' value='" + ResourceUtils.getText("button.print", lang) + "'/></td>");
		html.append("<td><input id='info2' type='button' class='button info2' onclick='displayInfo();' value='" + ResourceUtils.getText("button.info", lang) + "'/></td>");
		html.append("</tr></table></div>");
		html.append("<div class='separatortop'></div>");
		return html;
	}
	
	public static StringBuffer writeInfoHeader(LinkedHashMap<String, String> h, String lang) {
		StringBuffer html = new StringBuffer();
		String tabTitle = h.get("tabtitle");
		Integer width = (h.containsKey("width") ? Integer.valueOf(h.get("width")) : 0);
		html.append("<span class='title'>" + tabTitle.replaceAll(".{6}\\[.+#.*\\]$", "") + "</span>");
		html.append("<span class='url'>" + h.get("url") + "</span>");
		html.append("<span class='infostats'>" + h.get("info") + "</span>");
		html.append("<table class='info'" + (width != null && width > 0 ? " style='width:" + width + "px;'" : "") + ">");
		if (h.containsKey("titlename"))
			html.append("<tr><th>" + h.get("titlename") + "</th></tr>");
		for (String key : h.keySet()) {
			if (!key.matches("(tab|^)title|url|info|\\_sport\\_|width|titlename") && StringUtils.notEmpty(h.get(key))) {
				html.append("<tr>" + (h.containsKey("_sport_") ? "" : "<th class='caption'>" + ResourceUtils.getText(key, lang) + "</th>"));
				html.append("<td" + (key.matches("logo|otherlogos|flag|otherflags|record|extlinks") ? " class='" + key + "'" : "") + (h.containsKey("_sport_") ? " style='text-align:center;'" : "") + ">" + h.get(key) + "</td></tr>");
			}
		}
		html.append("</table>");
		return html;
	}

	public static String writeTip(String t, Object o) {
		StringBuffer html = new StringBuffer();
		long time = System.currentTimeMillis();
		html.append("<a style='cursor:help;' href='#" + t + "-" + time + "'><img src='" + ImageUtils.getRenderUrl() + "note.png'/></a>" + (o instanceof Collection ? "&nbsp;" + ((Collection)o).size() : ""));
		html.append("<div id='" + t + "-" + time + "' class='rendertip'>" + (o instanceof String ? o : StringUtils.implode((Collection<String>) o, "<br/>")) + "</div>");
		return html.toString();
	}
	
	public static String writeComment(Integer id, String s) {
		StringBuffer html = new StringBuffer();
		if (StringUtils.notEmpty(s)) {
			s = s.replaceAll("\r|\n", "<br/>");
			html.append(s.matches("^\\#\\#.*") ? s.substring(2) : writeTip("cmt-" + id, s));
		}
		return html.toString();
	}

	public static StringBuffer writeWinRecTable(Collection<WinRecordsBean> c, String lang) {
		StringBuffer html = new StringBuffer();
		html.append("<table class='winrec'><tr><th colspan='3'>" + writeToggleTitle(ResourceUtils.getText("win.records", lang)) + "</th></tr>");
		int max = -1;
		int i = 0;
		for (WinRecordsBean bean : c) {
			max = (max == -1 ? bean.getCountWin() : max);
			html.append("<tr" + (++i > 5 ? " class='hidden'" : "") + "><td class='caption'>" + writeLink(bean.getEntityType() < 10 ? Athlete.alias : (bean.getEntityType() == 50 ? Team.alias : Country.alias), bean.getEntityId(), bean.getEntityStr(), null) + "</td>");
			html.append("<td><table><tr><td class='bar1'>&nbsp;</td>");
			html.append("<td class='bar2' style='width:" + (int)((bean.getCountWin() * 100) / max) + "px;'>&nbsp;</td>");
			html.append("<td class='bar3'>&nbsp;</td></tr></table></td>");
			html.append("<td class='count'>" + bean.getCountWin() + "</td></tr>");
		}
		if (i > 5)
			html.append("<tr class='refseefull' onclick='winrecSeeFull(this);'><td colspan='3'></td></tr>");
		return html.append("</table>");
	}

	public static String writeURL(String main, String params) {
		params = params.replaceAll("\\,\\s", "-").replaceAll("[\\[\\]]", "").replaceAll("\\-\\_(en|fr)$", "");
		return ConfigUtils.getProperty("url") + main + "?p=" + StringUtils.encode(params);
	}
	
	public static String writeRecordItems(Collection<RefItem> cRecord, String lang) {
		StringBuffer sbRecord = new StringBuffer();
		for (RefItem item : cRecord) {
			sbRecord.append("<table" + (sbRecord.toString().length() > 0 ? " style='margin-top:5px;'" : "") + "><tr><td rowspan='2' style='text-align:right;width:125px;font-weight:normal;text-decoration:underline;'>" + ResourceUtils.getText("rec." + item.getLabel(), lang).replaceAll("\\s", "&nbsp;") + "</td>");
			if (StringUtils.notEmpty(item.getTxt1()))
				sbRecord.append("<th>" + (item.getTxt1().equalsIgnoreCase("#GOLD#") ? ImageUtils.getGoldMedImg() : item.getTxt1()) + "</th>");
			if (StringUtils.notEmpty(item.getTxt2()))
				sbRecord.append("<th>" + (item.getTxt2().equalsIgnoreCase("#SILVER#") ? ImageUtils.getSilverMedImg() : item.getTxt2()) + "</th>");
			if (StringUtils.notEmpty(item.getTxt3()))
				sbRecord.append("<th>" + (item.getTxt3().equalsIgnoreCase("#BRONZE#") ? ImageUtils.getBronzeMedImg() : item.getTxt3()) + "</th>");
			if (StringUtils.notEmpty(item.getTxt4()))
				sbRecord.append("<th>" + item.getTxt4() + "</th>");
			sbRecord.append("</tr>");
			sbRecord.append("<tr>");
			if (StringUtils.notEmpty(item.getTxt1()))
				sbRecord.append("<td>" + item.getCount1() + "</td>");
			if (StringUtils.notEmpty(item.getTxt2()))
				sbRecord.append("<td>" + item.getCount2() + "</td>");
			if (StringUtils.notEmpty(item.getTxt3()))	
				sbRecord.append("<td>" + item.getCount3() + "</td>");
			if (StringUtils.notEmpty(item.getTxt4()))
				sbRecord.append("<td>" + item.getCount4() + "</td>");
			sbRecord.append("</tr></table>");
		}
		return sbRecord.toString();
	}
	
	public static String writeExternalLinks(Object o) throws Exception {
		StringBuffer sbHtml = new StringBuffer();
		Method m = null;
		try {
			m = o.getClass().getMethod("getUrlWiki");
		}
		catch (NoSuchMethodException e) {
			m = null;
		}
		if (m != null) {
			if (m.invoke(o) != null) {
				String url = (String) m.invoke(o);
				if (StringUtils.notEmpty(url))
					sbHtml.append("<tr><th>Wikipedia</th></tr><tr><td><table><tr><td style='width:16px;'><img alt='Wiki' src='img/render/link-wiki.png'/></td><td>&nbsp;<a href='" + url + "' target='_blank'>" + url + "</a></td></tr></table></td></tr>");				
			}
		}
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("Oly", "Olympics");
		h.put("Bkt", "Basketball");
		h.put("Bb", "Baseball");
		h.put("Ft", "Pro-football");
		h.put("Hk", "Hockey");
		for (String s : new String[]{ "Oly", "Bkt", "Bb", "Ft", "Hk" }) {
			m = null;
			try {
				m = o.getClass().getMethod("getUrl" + s + "ref");
			}
			catch (NoSuchMethodException e) {
				m = null;
			}
			if (m != null) {
				if (m.invoke(o) != null) {
					String url = (String) m.invoke(o);
					if (StringUtils.notEmpty(url))
						sbHtml.append("<tr><th>" + h.get(s) + "-reference</th></tr><tr><td><table><tr><td style='width:16px;'><img alt='spref' src='img/render/link-" + s.toLowerCase() + "ref.png'/></td><td>&nbsp;<a href='" + url + "' target='_blank'>" + url + "</a></td></tr></table></td></tr>");				
				}
			}
		}
		return (sbHtml.toString().length() > 0 ? "<table>" + sbHtml.append("</table>").toString() : "");
	}
	
	public static void setTitle(HttpServletRequest req, String header) throws Exception {
		req.setAttribute("title", header.replaceAll("\\</span\\>.*", "").replaceAll(".*'title'\\>", "") + " | SPORTHENON");
	}

}