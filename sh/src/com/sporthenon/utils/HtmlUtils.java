package com.sporthenon.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.db.function.WinRecordsBean;
import com.sporthenon.utils.res.ResourceUtils;

public class HtmlUtils {

	public final static String SPACE = " ";
	public final static String NO_RESULT = "<div class='noresult'>No result found matching the selected criteria.</div>";

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
		return html.toString();
	}

	public static String writeLink(String alias, int id, String text, String title) {
		StringBuffer html = new StringBuffer();
		html.append("<a href='javascript:info(\"" + alias + "-" + id + "\")'" + (StringUtils.notEmpty(title) ? " title=\"" + title + "\"" : "") + ">" + (text != null ? text.replaceAll("\\s", SPACE) : "") + "</a>");
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
		html.append("<img src='" + ImageUtils.getRenderUrl() + "collapse.gif' class='toggleimg' onclick='toggleContent(this)'/>");
		html.append("<span class='toggletext' onclick='toggleContent(this)'>" + s + "</span>");
		return html.toString();
	}

	public static StringBuffer writeHeader(HashMap<String, String> h) {
		StringBuffer html = new StringBuffer();
		html.append("<span class='shorttitle'>" + h.get("tabshorttitle") + "</span>");
		html.append("<span class='url'>" + h.get("url") + "</span>");
		html.append("<span class='infostats'>" + h.get("info") + "</span>");
		html.append("<table class='header'><tr><th colspan='2'>" + writeToggleTitle(h.get("title")) + "</th></tr>");
		html.append("<tr><td class='logos' rowspan=4>" + h.get("logos") + "</td>");
		html.append("<td>" + h.get("item1") + "</td></tr>");
		html.append("<tr><td>" + (h.containsKey("item2") && StringUtils.notEmpty(h.get("item2")) ? h.get("item2") : "-") + "</td></tr>");
		html.append("<tr><td>" + (h.containsKey("item3") &&StringUtils.notEmpty(h.get("item3")) ? h.get("item3") : "-") + "</td></tr>");
		html.append("<tr><td>" + (h.containsKey("item4") &&StringUtils.notEmpty(h.get("item4")) ? h.get("item4") : "-") + "</td></tr>");			
		html.append("</table>");
		return html;
	}
	
	public static StringBuffer writeInfoHeader(LinkedHashMap<String, String> h, String lang) {
		StringBuffer html = new StringBuffer();
		String tabTitle = h.get("tabtitle");
		html.append("<span class='shorttitle'>" + tabTitle.replaceAll(".{6}\\[.+#.*\\]$", "") + "</span>");
		html.append("<span class='url'>" + h.get("url") + "</span>");
		html.append("<span class='infostats'>" + h.get("info") + "</span>");
		html.append("<table class='info'><tr><th colspan=2>" + writeToggleTitle(h.get("title")) + "</th></tr>");
		if (h.containsKey("logo")) {
			String logo = h.get("logo");
			String bordered = (logo.matches("^.*" + ConfigUtils.getProperty("app.url") + "(" + ImageUtils.INDEX_COUNTRY + "|" + ImageUtils.INDEX_STATE + ").*") ? " bordered" : "");
			html.append("<tr><td colspan=2 class='logo" + bordered + "'>" + logo + "</td></tr>");
		}
		for (String key : h.keySet()) {
			if (!key.matches("(tab|^)title|logo|url|info") && StringUtils.notEmpty(h.get(key)))
				html.append("<tr><th class='caption'>" + ResourceUtils.getText(key, lang) + "</th><td" + (key.matches("record|extlinks") ? " class='" + key + "'" : "") + ">" + h.get(key) + "</td></tr>");
		}
		html.append("</table>");
		return html;
	}

	public static String writeTip(String t, Object o) {
		StringBuffer html = new StringBuffer();
		long time = System.currentTimeMillis();
		html.append((o instanceof Collection ? ((Collection)o).size() + "&nbsp;" : "") + "<a style='cursor:help;' href='#" + t + "-" + time + "'><img src='" + ImageUtils.getRenderUrl() + "note.png'/></a>");
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

	public static StringBuffer writeWinRecTable(Collection<WinRecordsBean> c) {
		StringBuffer html = new StringBuffer();
		html.append("<table class='winrec'><tr><th colspan=3>" + writeToggleTitle("WIN RECORDS") + "</th></tr>");
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
		return html;
	}

	public static String writeURL(String main, String params) {
		return ConfigUtils.getProperty("url") + main + "?" + params.replaceAll("\\,\\s", "-").replaceAll("[\\[\\]]", "");
	}
	
	public static String writeRecordItems(Collection<RefItem> cRecord, String lang) {
		StringBuffer sbRecord = new StringBuffer();
		for (RefItem item : cRecord) {
			sbRecord.append("<table" + (sbRecord.toString().length() > 0 ? "style='margin-top:5px;'" : "") + "><tr><td rowspan='2' style='text-align:right;width:125px;font-weight:normal;'><u>" + ResourceUtils.getText("rec." + item.getLabel(), lang).replaceAll("\\s", "&nbsp;") + "</u></td>");
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
			sbRecord.append("</tr>");
		}
		return sbRecord.append("</table>").toString();
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
					sbHtml.append("<tr><th>Wikipedia</th></tr><tr><td><table><tr><td style='width:16px;'><img src='img/render/link-wiki.png'/></td><td>&nbsp;<a href='" + url + "' target='_blank'>" + url + "</a></td></tr></table></td></tr>");				
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
						sbHtml.append("<tr><th>" + h.get(s) + "-reference</th></tr><tr><td><table><tr><td style='width:16px;'><img src='img/render/link-" + s.toLowerCase() + "ref.png'/></td><td>&nbsp;<a href='" + url + "' target='_blank'>" + url + "</a></td></tr></table></td></tr>");				
				}
			}
		}
		return (sbHtml.toString().length() > 0 ? "<table>" + sbHtml.append("</table>").toString() : "");
	}

}