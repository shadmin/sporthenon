package com.sporthenon.web.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.meta.ErrorReport;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.HtmlConverter;
import com.sporthenon.web.ServletHelper;

public class IndexServlet extends AbstractServlet {

private static final long serialVersionUID = 1L;

	public static final String REPORT_QUERY1 = "SELECT SP.label#LANG#, COUNT(*) FROM \"Result\" RS LEFT JOIN \"Sport\" SP ON RS.id_sport=SP.id GROUP BY SP.label#LANG# ORDER BY 2 DESC LIMIT 10";
	public static final String REPORT_QUERY2 = "SELECT CN.label#LANG#, COUNT(*) FROM \"Country\" CN LEFT JOIN \"Athlete\" PR ON PR.id_country=CN.id GROUP BY CN.label#LANG# ORDER BY 2 DESC LIMIT 10";
	public static final String REPORT_QUERY3 = "SELECT SP.label#LANG#, COUNT(*) FROM \"~Request\" RQ LEFT JOIN \"Sport\" SP ON (SP.ID || '')=regexp_replace(replace(params || '', 'SP-', ''), '-.*', '') WHERE RQ.path IN ('/results', '/sport') AND LABEL IS NOT NULL GROUP BY SP.id, SP.label#LANG# ORDER BY 2 DESC LIMIT 8";
	public static final String REPORT_QUERY4 = "SELECT to_char(R.date, 'YYYY-MM') AS M, COUNT(id) FROM \"~Request\" R GROUP BY to_char(R.date, 'YYYY-MM') ORDER BY M DESC LIMIT 12";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			String lang = getLocale(request);
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			if (hParams.containsKey("p")) {
				String p = String.valueOf(hParams.get("p"));
				p = StringUtils.decode(p);
				String[] t = p.split("\\-");
				hParams.put("count", t[0]);
				hParams.put("offset", t[1]);
			}
			if (hParams.containsKey("lang")) { // Language
		        request.getSession().setAttribute("locale", String.valueOf(hParams.get("value")));
			}
			else if (hParams.containsKey("error")) { // Error Report
				ErrorReport er = new ErrorReport();
				er.setUrl(String.valueOf(hParams.get("url")));
				er.setText(String.valueOf(hParams.get("text")));
				er.setDate(new Timestamp(System.currentTimeMillis()));
				DatabaseHelper.saveEntity(er, null);
			}
			else if (hParams.containsKey("lastupdates")) { // Last Updates
		        ArrayList<Object> lParams = new ArrayList<Object>();
		        Integer count = new Integer(String.valueOf(hParams.get("count")));
		        Integer offset = new Integer(String.valueOf(hParams.get("offset")));
		        lParams.add(count);
		        lParams.add(offset);
		        lParams.add("_" + lang);
		        ServletHelper.writeTabHtml(request, response, HtmlConverter.convertLastUpdates(DatabaseHelper.call("LastUpdates", lParams), count, offset, getLocale(request)), getLocale(request));
			}
			else if (hParams.containsKey("randomevent")) { // Random Event
				ServletHelper.writeText(response, getRandomEvent(lang));
			}
			else if (hParams.containsKey("report")) { // Report
				ArrayList<String> lReport = new ArrayList<String>();
				lReport.add(REPORT_QUERY1);
				lReport.add(REPORT_QUERY2);
				lReport.add(REPORT_QUERY3);
				lReport.add(REPORT_QUERY4);
				int index = Integer.parseInt(String.valueOf(hParams.get("report")));
				String lang_ = (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? "_" + lang : "");				
				List<Object[]> list = DatabaseHelper.executeNative(lReport.get(index).replaceAll("#LANG#", lang_));
				StringBuffer sb1 = new StringBuffer();
				StringBuffer sb2 = new StringBuffer();
				for (Object[] t : list) {
					if (index == 3) {
						String s = String.valueOf(t[0]);
						s = ResourceUtils.getText("month." + Integer.parseInt(s.substring(5)), lang).substring(0, 3) + " " + s.substring(2, 4);
						t[0] = s;
					}
					sb1.append(sb1.toString().length() > 0 ? "|" : "").append(t[0]);
					sb2.append(sb2.toString().length() > 0 ? "|" : "").append(t[1]);
				}
				ServletHelper.writeText(response, sb1.toString() + "~" + sb2.toString());
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
	public static String getSportDivs(String lang) throws Exception {
		final int N = 8;
		HashMap<Integer, String> hSports = new HashMap<Integer, String>();
		ArrayList<Integer> lId = new ArrayList<Integer>();
		for (Object obj : DatabaseHelper.execute("from Sport order by index")) {
			Sport sp = (Sport) obj;
			String img = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, sp.getId(), ImageUtils.SIZE_LARGE, null, null);
			String text = sp.getLabel(lang);
			img = img.replaceAll(".*\\ssrc\\='|'/\\>", "");
			hSports.put(sp.getId(), "<div id='sport-#INDEX#' class='sport' style=\"background-image:url('" + img + "');" + "\">" + HtmlUtils.writeLink(Sport.alias, sp.getId(), text.replaceAll("\\s", "&nbsp;"), sp.getLabel()) + "</div>");
			lId.add(sp.getId());
		}
		
		StringBuffer sports = new StringBuffer("<div class='slide'>");
		String slide1 = null;
		int index = 0;
		for (Integer i : lId) {
			if (index > 0 && index % N == 0)
				sports.append("</div><div class='slide'>");
			sports.append(hSports.get(i).replaceAll("#INDEX#", String.valueOf(index)));
			index++;
			if (index == N)
				slide1 = sports.toString();
		}
		sports.append("</div>");
		
		return sports.append(slide1).toString().replaceAll("\"", "\\\\\"");
	}
	
	public static String getRandomEvent(String lang) {
		String s = "";
		try {
			Integer max = (Integer) DatabaseHelper.executeNative("SELECT max(id) FROM \"Result\"").get(0);
			int id = new Random().nextInt(max);
			Result rs = (Result) DatabaseHelper.loadEntity(Result.class, id);
			if (rs != null) {
				s = HtmlUtils.writeURL("/results", rs.getSport().getId() + "-" + rs.getChampionship().getId() + "-" + rs.getEvent().getId() + (rs.getSubevent() != null ? "-" + rs.getSubevent().getId() : "") + (rs.getSubevent2() != null ? "-" + rs.getSubevent2().getId() : ""), rs.getSport().getLabel() + "/" + rs.getChampionship().getLabel() + (rs.getEvent() != null ? "/" + rs.getEvent().getLabel() + (rs.getSubevent() != null ? "/" + rs.getSubevent().getLabel() : "") + (rs.getSubevent2() != null ? "/" + rs.getSubevent2().getLabel() : "") : ""));
				s = "<a href='" + s + "'>" + rs.getSport().getLabel(lang) + "&nbsp;" + StringUtils.SEP1 + "&nbsp;" + rs.getChampionship().getLabel(lang) + "&nbsp;" + StringUtils.SEP1 + "&nbsp;" + rs.getEvent().getLabel(lang) + (rs.getSubevent() != null ? "&nbsp;" + StringUtils.SEP1 + "&nbsp;" + rs.getSubevent().getLabel(lang) : "") + (rs.getSubevent2() != null ? "&nbsp;" + StringUtils.SEP1 + "&nbsp;" + rs.getSubevent2().getLabel(lang) : "") + "</a>";
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return s;
	}
	
}