package com.sporthenon.utils;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.entity.meta.ExternalLink;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.utils.res.ResourceUtils;

public class HtmlUtils {

	public static String writeNoResult(String lang) {
		return "<div class='noresult'>" + ResourceUtils.getText("no.result", lang) + "</div>";
	}
	
	public static String writeImage(short type, Object id, char size, String year, String title) {
		StringBuffer html = new StringBuffer();
		final String name = type + "-" + id + "-" + size;
		String name2 = "";
		if (type == ImageUtils.INDEX_SPORT_CHAMPIONSHIP || type == ImageUtils.INDEX_SPORT_EVENT)
			name2 = (type == ImageUtils.INDEX_SPORT_CHAMPIONSHIP ? ImageUtils.INDEX_CHAMPIONSHIP : ImageUtils.INDEX_EVENT) + "-" + String.valueOf(id).split("\\-")[1] + "-" + size;
		LinkedList<String> list = new LinkedList<String>();
		LinkedList<String> list2 = new LinkedList<String>();
		for (String s : ImageUtils.getImgFiles()) {
			if (s.startsWith(name)) {
				boolean isInclude = true;
				if (StringUtils.notEmpty(year)) {
					String[] t = s.replaceAll("^" + name + "(\\_|)|(gif|png)$|(\\_\\d+|)\\.", "").split("\\-");
					try {
						if (t.length > 1) {
							Integer y = Integer.parseInt(year.contains("-") || year.contains("/") ? year.substring(year.length() - 4) : year);
							Integer y1 = Integer.parseInt(t[0].equalsIgnoreCase("X") ? "0" : t[0]);
							Integer y2 = Integer.parseInt(t[1].equalsIgnoreCase("X") ? "5000" : t[1]);
							isInclude = (y >= y1 && y <= y2);
						}
					}
					catch (NumberFormatException e) {}
				}
				else
					isInclude = !s.matches(".*\\d{4}\\-\\d{4}\\.(gif|png)$");
				if (isInclude)
					list.add(s);
			}
			else if (StringUtils.notEmpty(name2) && s.startsWith(name2))
				list2.add(s);
		}
		if (list.isEmpty())
			list.addAll(list2);
		Collections.sort(list);
		if (!list.isEmpty())
			html.append("<img alt=''" + (StringUtils.notEmpty(title) ? " title=\"" + title + "\"" : "") + " src='" + ImageUtils.getUrl() + list.getLast() + "'/>");
		else if (size == ImageUtils.SIZE_LARGE && type != ImageUtils.INDEX_SPORT_CHAMPIONSHIP && type != ImageUtils.INDEX_SPORT_EVENT)
			html.append("<img alt='' src='/img/noimage.png?0'/>");
		return html.toString();
	}
	
	public static String writeURL(String main, String params, String text) {
		if (params != null)
			params = params.replaceAll("\\,\\s", "-").replaceAll("[\\[\\]]", "").replaceAll("\\-\\_(en|fr)$", "");
		return main + (StringUtils.notEmpty(text) ? "/" + StringUtils.urlEscape(text.replaceAll("\\&nbsp;\\-\\&nbsp\\;", "/")) : "") + (StringUtils.notEmpty(params) ? "/" + StringUtils.encode(params) : "");
	}

	public static String writeLink(String alias, int id, String text1, String text2) {
		StringBuffer html = new StringBuffer();
		StringBuffer url = new StringBuffer();
		text2 = (text2 != null ? text2 : text1);
		url.append("/" + ResourceUtils.getText("entity." + alias + ".1", ResourceUtils.LGDEFAULT).replaceAll("\\s", "").toLowerCase());
		url.append("/" + StringUtils.urlEscape(text2));
		url.append("/" + StringUtils.encode(alias + "-" + id + (alias.equals(Result.alias) ? "-1" : "")));
		if (text1 != null) {
			html.append("<a href='").append(url).append("'");
			if (alias.equals(Athlete.alias) && StringUtils.notEmpty(text2) && !text1.toLowerCase().equals(text2.toLowerCase()))
				html.append(" title=\"" + text2.replaceAll("\\\"", "'") + "\"");
			html.append(">" + (!text1.startsWith("<") ? text1.replaceAll("\\s", "&nbsp;")/*.replaceAll("\\-", "&#8209;")*/ : text1) + "</a>");
		}
		else
			html.append("/" + url.toString().substring(1));
		return html.toString();
	}
	
	public static String writeDateLink(Object value1, Object value2, String text) throws Exception {
		StringBuffer html = new StringBuffer();
		StringBuffer url = new StringBuffer("/calendar");
		// Date 1
		String s1 = "";
		String s2 = "";
		if (value1 != null) {
			if (value1 instanceof String) {
				if (String.valueOf(value1).length() > 4) {
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					value1 = new Timestamp(df.parse(String.valueOf(value1)).getTime());	
				}
				else {
					s1 = String.valueOf(value1);
					s2 = s1 + "-0-0";
				}
			}
			if (value1 instanceof Timestamp) {
				s1 = StringUtils.toTextDate((Timestamp) value1, ResourceUtils.LGDEFAULT, "yyyy-MM-dd");
				s2 = StringUtils.toTextDate((Timestamp) value1, ResourceUtils.LGDEFAULT, "yyyyMMdd");
			}
		}
		// Date 2
		String s1_ = "";
		String s2_ = "";
		if (value2 != null) {
			if (value2 instanceof String) {
				if (String.valueOf(value2).length() > 4) {
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					value2 = new Timestamp(df.parse(String.valueOf(value2)).getTime());	
				}
				else {
					s1_ = String.valueOf(value2);
					s2_ = s1_ + "-0-0";
				}
			}
			if (value2 instanceof Timestamp) {
				s1_ = StringUtils.toTextDate((Timestamp) value2, ResourceUtils.LGDEFAULT, "yyyy-MM-dd");
				s2_ = StringUtils.toTextDate((Timestamp) value2, ResourceUtils.LGDEFAULT, "yyyyMMdd");
			}
		}
		url.append(value1 != null ? "/" + s1 : "").append("/" + s1_);
		url.append("/" + StringUtils.encode((value1 != null ? s2 + "-" : "") + s2_));
		html.append("<a href='").append(url).append("'>").append(text).append("</a>");
		return html.toString();
	}

	public static String writeImgTable(String img, String text) {
		StringBuffer html = new StringBuffer();
		if (StringUtils.notEmpty(img)) {
			html.append("<table><tr><th" + (img.indexOf("expand") != -1 ? " class='nowrap'" : "") + ">" + img + "</th>");
			html.append("<td>" + text + "</td></tr></table>");
		}
		else
			html.append(text);
		return html.toString();
	}

	public static String writeToggleTitle(String s, boolean collapsed) {
		StringBuffer html = new StringBuffer();
		html.append("<img alt='' src='" + ImageUtils.getRenderUrl() + (collapsed ? "expand" : "collapse") + ".gif' class='toggleimg' onclick='toggleContent(this)'/>");
		html.append("<span class='toggletext' onclick='toggleContent(this)'>" + s + "</span>");
		return html.toString();
	}

	public static StringBuffer writeHeader(Map<String, String> h, Integer sp, Contributor m, String lang) {
		StringBuffer html = new StringBuffer();
		html.append("<span class='title'>" + h.get("title") + "</span>");
		html.append("<span class='desc'>" + h.get("desc") + "</span>");
		html.append("<span class='infostats'>" + h.get("info") + "</span>");
		String url = null;
		if (h.containsKey("url") && StringUtils.notEmpty(h.get("url"))) {
			url = h.get("url").substring(1);
			html.append("<span class='url'>" + ConfigUtils.getProperty("url") + url + "</span>");
		}
		html.append("<div id='Header' class='header'><table><tr>");
		html.append(h.containsKey("item0") ? "<td style='font-weight:bold;'>" + h.get("item0") + "</td>" : "");
		html.append(h.containsKey("item1") ? "<td class='arrow'>&nbsp;</td><td>" + h.get("item1") + "</td>" : "");
		html.append(h.containsKey("item2") ? "<td class='arrow'>&nbsp;</td><td>" + h.get("item2") + "</td>" : "");
		html.append(h.containsKey("item3") ? "<td class='arrow'>&nbsp;</td><td>" + h.get("item3") + "</td>" : "");
		html.append(h.containsKey("item4") ? "<td class='arrow'>&nbsp;</td><td>" + h.get("item4") + "</td>" : "");
		html.append(h.containsKey("item5") ? "<td class='arrow'>&nbsp;</td><td>" + h.get("item5") + "</td>" : "");
		html.append("<td style='padding-left:10px;'><img id='favimg' alt='fav' src='/img/menu/favorites2.png' style='cursor:pointer;'/></td>");
		html.append("</tr></table>");
		html.append("</div>");
		html.append("<div class='toolbar'>");
		html.append("<table><tr>");
		final String SHARE_OPTIONS = "<div id='shareopt' class='baroptions' style='display:none;'><table><tr><td onclick='share(\"fb\");' class='fb'>Facebook</td></tr><tr><td onclick='share(\"tw\");' class='tw'>Twitter</td></tr><tr><td onclick='share(\"gp\");' class='gp'>Google+</td></tr><tr><td onclick='share(\"bg\");' class='bg'>Blogger</td></tr><tr><td onclick='share(\"tm\");' class='tm' style='border-bottom:none;'>Tumblr</td></tr></table></div>";
		final String EXPORT_OPTIONS = "<div id='exportopt' class='baroptions' style='display:none;'><table><tr><td onclick='exportPage(\"html\");' class='html'>" + ResourceUtils.getText("web.page", lang) + "</td></tr><tr><td onclick='exportPage(\"csv\");' class='csv'>" + ResourceUtils.getText("csv.file", lang) + "</td></tr><tr><td onclick='exportPage(\"xls\");' class='excel'>" + ResourceUtils.getText("excel.sheet", lang) + "</td></tr><tr><td onclick='exportPage(\"pdf\");' class='pdf'>" + ResourceUtils.getText("pdf.file", lang) + "</td></tr><tr><td onclick='exportPage(\"txt\");' class='text' style='border-bottom:none;'>" + ResourceUtils.getText("plain.text", lang) + "</td></tr></table></div>";
		if (h.containsKey("errors"))
			html.append("<td>" + h.get("errors") + "</td>");
		if (m != null && url != null && sp != null && m.isSport(sp)) {
			if (url.matches("^results.*"))
				html.append("<td><input id='add' type='button' class='button add' onclick='location.href=\"" + h.get("url").replaceAll("\\/results", "/update") + "\";' value='" + ResourceUtils.getText("button.add", lang) + "'/></td>");	
			else if (url.matches("^result.*"))
				html.append("<td><input id='modify' type='button' class='button modify' onclick='location.href=\"" + h.get("url").replaceAll("\\/result", "/update") + "\";' value='" + ResourceUtils.getText("button.modify", lang) + "'/></td>");
		}
		html.append("<td onmouseover=\"$('shareopt').show();\" onmouseout=\"$('shareopt').hide();\"><input id='share' type='button' class='button share' value='" + ResourceUtils.getText("share", lang) + "'/>" + SHARE_OPTIONS + "</td>");
		html.append("<td onmouseover=\"$('exportopt').show();\" onmouseout=\"$('exportopt').hide();\"><input id='export' type='button' class='button export' value='" + ResourceUtils.getText("button.export", lang) + "'/>" + EXPORT_OPTIONS + "</td>");
		html.append("<td><input id='link' type='button' class='button link' onclick='displayLink();' value='" + ResourceUtils.getText("button.link", lang) + "'/></td>");
		html.append("<td><input id='print' type='button' class='button print' onclick='javascript:printCurrentTab();' value='" + ResourceUtils.getText("button.print", lang) + "'/></td>");
		html.append("<td><input id='info2' type='button' class='button info2' onclick='displayInfo();' value='" + ResourceUtils.getText("button.info", lang) + "'/></td>");
		html.append("</tr></table></div>");
		return html;
	}
	
	public static StringBuffer writeInfoHeader(LinkedHashMap<String, String> h, String lang) {
		StringBuffer html = new StringBuffer();
		String title = h.get("title");
		Integer width = (h.containsKey("width") ? Integer.valueOf(h.get("width")) : 0);
		html.append("<span class='title'>" + title.replaceAll(".{6}\\[.+#.*\\]$", "") + "</span>");
		html.append("<span class='infostats'>" + h.get("info") + "</span>");
		html.append("<span class='url'>" + h.get("url") + "</span>");
		// Info
		html.append("<ul class='uinfo'><li>");
		html.append("<table class='info'" + (width != null && width > 0 ? " style='width:" + width + "px;'" : "") + ">");
		if (h.containsKey("titlename")) {
			html.append("<tr><th id='titlename'" + (!h.containsKey("_sport_") && !h.containsKey("_year_") ? " colspan='2'" : "") + ">" + h.get("titlename") + "</th></tr>");
			// Photo
			if (h.containsKey("imgurl"))
				html.append("<tr><td colspan='2' class='photo'>" + ImageUtils.getPhotoImg(h.get("imgurl"), h.get("source"), lang) + "</td></tr>");
		}
		for (String key : h.keySet()) {
			if (!key.matches("(tab|^)title|titleEN|imgurl|source|url|info|\\_sport\\_|\\_year\\_|width|titlename|titlename2") && StringUtils.notEmpty(h.get(key))) {
				html.append("<tr>" + (h.containsKey("_sport_") || h.containsKey("_team_") || h.containsKey("_year_") || key.matches("flag|logo") ? "" : "<th class='caption'>" + ResourceUtils.getText(key, lang) + "</th>"));
				html.append("<td" + (key.matches("logo|logosport|otherlogos|flag|otherflags|record|extlinks") ? " class='" + key + "'" : "") + (key.matches("flag|logo") ? " colspan='2'" : "") + ">" + h.get(key) + "</td></tr>");
			}
			else if (key.equals("titlename2"))
				html.append("<tr><td id='titlename2' colspan='2'>" + h.get("titlename2") + "</th></tr>");
		}
		html.append("</table></li>");
		return html.append("</ul>");
	}

	public static String writeTip(String t, Object o) {
		StringBuffer html = new StringBuffer();
		long time = System.currentTimeMillis();
		html.append("<a style='cursor:help;' href='#" + t + "-" + time + "'><img alt='note' src='" + ImageUtils.getRenderUrl() + "note.png'/></a>" + (o instanceof Collection ? "&nbsp;" + ((Collection)o).size() : ""));
		html.append("<div id='" + t + "-" + time + "' class='rendertip' style='display:none;'>" + (o instanceof String ? o : StringUtils.join((Collection<String>) o, "<br/>")) + "</div>");
		return html.toString();
	}
	
	public static String writeComment(Integer id, String s) {
		StringBuffer html = new StringBuffer();
		if (StringUtils.notEmpty(s)) {
			s = s.replaceAll("\r|\n", "<br/>");
			// Normal display
			if (s.matches("^\\#\\#.*")) {
				s = s.substring(2).replaceAll("\\s", "&nbsp;");
				html.append(s.replaceAll("\\{\\{.*", ""));
				// Forced tooltip
				if (s.contains("{{"))
					html.append("&nbsp;").append(writeTip("cmt-" + id, s.replaceAll(".*\\{\\{|\\}\\}", "")));
			}
			// Tooltip
			else
				html.append(writeTip("cmt-" + id, s));
		}
		return html.toString();
	}

	public static String writeRecordItems(Collection<RefItem> cRecord, String lang) {
		StringBuffer sbRecord = new StringBuffer();
		String currentHeader = null;
		for (RefItem item : cRecord) {
			if (currentHeader == null || !currentHeader.equals(item.getTxt1() + item.getTxt2() + item.getTxt3())) {
				if (currentHeader != null)
					sbRecord.append("</table>");
				sbRecord.append("<table" + (sbRecord.toString().length() > 0 ? " style='margin-top:5px;'" : "") + "><tr><td style='border:none;'/>");
				if (StringUtils.notEmpty(item.getTxt1()))
					sbRecord.append("<th>" + (item.getTxt1().equalsIgnoreCase("#GOLD#") ? ImageUtils.getGoldMedImg(lang) : ResourceUtils.getText(item.getTxt1(), lang)) + "</th>");
				if (StringUtils.notEmpty(item.getTxt2()))
					sbRecord.append("<th>" + (item.getTxt2().equalsIgnoreCase("#SILVER#") ? ImageUtils.getSilverMedImg(lang) : ResourceUtils.getText(item.getTxt2(), lang)) + "</th>");
				if (StringUtils.notEmpty(item.getTxt3()))
					sbRecord.append("<th>" + (item.getTxt3().equalsIgnoreCase("#BRONZE#") ? ImageUtils.getBronzeMedImg(lang) : ResourceUtils.getText(item.getTxt3(), lang)) + "</th>");
				if (StringUtils.notEmpty(item.getTxt4()))
					sbRecord.append("<th>" + item.getTxt4() + "</th>");
				sbRecord.append("</tr>");
				currentHeader = item.getTxt1() + item.getTxt2() + item.getTxt3();
			}
			sbRecord.append("<tr><td style='text-align:right;padding-left:10px;font-weight:normal;text-decoration:underline;'>" + ResourceUtils.getText("rec." + item.getLabel(), lang).replaceAll("\\s", "&nbsp;") + "</td>");
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
		if (sbRecord.toString().length() > 0)
			sbRecord.append("</table>");
		return sbRecord.toString();
	}
	
	public static String writeExternalLinks(String alias, Object id, String lang) throws Exception {
		StringBuffer sbHtml = new StringBuffer();
		String currentType = null;
		List<ExternalLink> list = DatabaseHelper.execute("from ExternalLink where entity='" + alias + "' and idItem=" + id + " order by id");
		for (ExternalLink link : list) {
			if (link.getType().equals("lequipe") && !lang.equalsIgnoreCase("fr"))
				continue;
			// Title
			if (currentType == null || !currentType.equalsIgnoreCase(link.getType())) {
				if (link.getType().equals("wiki"))
					sbHtml.append("<tr><th>" + ResourceUtils.getText("wikipedia", lang) + "</th></tr>");
				else if (link.getType().equals("lequipe"))
					sbHtml.append("<tr><th>L&#039;ÉQUIPE.fr</th></tr>");
				else if (link.getType().matches(".*\\-ref$")) {
					HashMap<String, String> h = new HashMap<String, String>();
					h.put("oly-ref", "Olympics");
					h.put("bkt-ref", "Basketball");
					h.put("bb-ref", "Baseball");
					h.put("ft-ref", "Pro-football");
					h.put("hk-ref", "Hockey");
					sbHtml.append("<tr><th>" + h.get(link.getType()) + "-reference</th></tr><tr>");
				}
				else
					sbHtml.append("<tr><th>" + ResourceUtils.getText("extlink." + link.getType(), lang).replaceAll("\\s", "&nbsp;") + "</th></tr>");
			}
			currentType = link.getType();
			// Link
			String formattedURL = URLDecoder.decode(link.getUrl(), "UTF-8");
			String title = "";
			if (formattedURL.length() > 100) {
				title = " title=\"" + formattedURL + "\"";
				formattedURL = "..." + formattedURL.substring(formattedURL.lastIndexOf("/"));
			}
			if (link.getType().equals("wiki"))
				sbHtml.append("<tr><td><table><tr><td style='width:16px;'><img alt='Wiki' src='/img/render/link-wiki.png'/></td><td>&nbsp;<a href='" + link.getUrl() + "'" + title + " target='_blank'>" + formattedURL + "</a></td></tr></table></td></tr>");
			else if (link.getType().equals("lequipe"))
				sbHtml.append("<tr><td><table><tr><td style='width:16px;'><img alt='Lequipe' src='/img/render/link-lequipe.png'/></td><td>&nbsp;<a href='" + link.getUrl() + "'" + title + " target='_blank'>" + formattedURL + "</a></td></tr></table></td></tr>");
			else if (link.getType().matches(".*\\-ref$"))
				sbHtml.append("<td><table><tr><td style='width:16px;'><img alt='spref' src='/img/render/link-" + link.getType().replaceAll("\\-ref", "") + "ref.png'/></td><td>&nbsp;<a href='" + link.getUrl() + "'" + title + " target='_blank'>" + formattedURL + "</a></td></tr></table></td></tr>");
			else
				sbHtml.append("<tr><td><table><tr><td style='width:16px;'><img alt='spref' src='/img/render/website.png'/></td><td>&nbsp;<a href='" + link.getUrl() + "'" + title + " target='_blank'>" + formattedURL + "</a></td></tr></table></td></tr>");
		}
		return (sbHtml.toString().length() > 0 ? "<table>" + sbHtml.append("</table>").toString() : "");
	}
	
	public static void setHeadInfo(HttpServletRequest request, String header) throws Exception {
		Document d = Jsoup.parse(header);
		Elements e = d.getElementsByTag("span");
		String title = e.get(0).text();
		String desc = e.get(1).text();
		request.setAttribute("title", StringUtils.getTitle(title));
		request.setAttribute("desc", desc);
	}
	
}