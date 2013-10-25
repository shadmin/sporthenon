package com.sporthenon.utils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.function.WinRecordsBean;
import com.sporthenon.utils.res.ResourceUtils;


public class HtmlUtils {

	public final static String SPACE = " ";

	public static String writeImage(short type, int id, char size, boolean disabled) {
		StringBuffer html = new StringBuffer();
		if (!disabled) {
			String ext = (type != ImageUtils.INDEX_COUNTRY && type != ImageUtils.INDEX_STATE ? ".png" : ".gif");
			String name = type + "-" + id + "-" + size + ext;
			if (new File(ConfigUtils.getProperty("img.folder") + name).exists())
				html.append("<img alt='' src='" + ImageUtils.getUrl() + name + "?" + System.currentTimeMillis() + "'/>");
		}
		return html.toString();
	}

	public static String writeLink(String alias, int id, String text) {
		StringBuffer html = new StringBuffer();
		html.append("<a href='javascript:info(\"" + alias + "-" + id + "\")'>" + (text != null ? text.replaceAll("\\s", SPACE) : "") + "</a>");
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

	public static StringBuffer writeHeader(HashMap<String, String> h, boolean b1, boolean b2) {
		StringBuffer html = new StringBuffer();
		html.append("<span class='shorttitle'>" + h.get("tabshorttitle") + "</span>");
		html.append("<span class='url'>" + h.get("url") + "</span>");
		html.append("<span class='infostats'>" + h.get("info") + "</span>");
		if (!b1) {
			html.append("<table class='header'><tr><th colspan=" + (!b2 ? "2" : "1") + ">" + writeToggleTitle(h.get("title")) + "</th></tr>");
			html.append("<tr>" + (!b2 ? "<td class='logos' rowspan=4>" + h.get("logos") + "</td>" : ""));
			html.append("<td>" + h.get("item1") + "</td></tr>");
			html.append("<tr><td>" + (h.containsKey("item2") && StringUtils.notEmpty(h.get("item2")) ? h.get("item2") : "-") + "</td></tr>");
			html.append("<tr><td>" + (h.containsKey("item3") &&StringUtils.notEmpty(h.get("item3")) ? h.get("item3") : "-") + "</td></tr>");
			html.append("<tr><td>" + (h.containsKey("item4") &&StringUtils.notEmpty(h.get("item4")) ? h.get("item4") : "-") + "</td></tr>");			
			html.append("</table>");
		}
		return html;
	}
	
	public static StringBuffer writeInfoHeader(LinkedHashMap<String, String> h, boolean b) {
		StringBuffer html = new StringBuffer();
		String tabTitle = h.get("tabtitle");
		html.append("<span class='shorttitle'>" + tabTitle.replaceAll(".{6}\\[.+#.*\\]$", "") + "</span>");
		html.append("<span class='url'>" + h.get("url") + "</span>");
		html.append("<span class='infostats'>" + h.get("info") + "</span>");
		html.append("<table class='info'><tr><th colspan=2>" + writeToggleTitle(h.get("title")) + "</th></tr>");
		if (h.containsKey("logo") && !b) {
			String logo = h.get("logo");
			String bordered = (logo.matches("^(" + ImageUtils.INDEX_COUNTRY + "|" + ImageUtils.INDEX_STATE + ").*") ? " bordered" : "");
			html.append("<tr><td colspan=2 class='logo" + bordered + "'><img src='" + ImageUtils.getUrl() + logo + "?" + System.currentTimeMillis() + "'/></td></tr>");
		}
		for (String key : h.keySet()) {
			if (!key.matches("(tab|^)title|logo|url|info"))
				html.append("<tr><th class='caption'>" + ResourceUtils.get(key) + "</th><td>" + h.get(key) + "</td></tr>");
		}
		html.append("</table>");
		return html;
	}

	public static String writeTip(String t, Object o) {
		StringBuffer html = new StringBuffer();
		long time = System.currentTimeMillis();
		html.append((o instanceof Collection ? ((Collection)o).size() + "&nbsp;" : "") + "<a href='#" + t + "-" + time + "'><img src='" + ImageUtils.getRenderUrl() + "note.png'/></a>");
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
			html.append("<tr" + (++i > 5 ? " class='hidden'" : "") + "><td class='caption'>" + writeLink(bean.getEntityType() < 10 ? Athlete.alias : (bean.getEntityType() == 50 ? Team.alias : Country.alias), bean.getEntityId(), bean.getEntityStr()) + "</td>");
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

}