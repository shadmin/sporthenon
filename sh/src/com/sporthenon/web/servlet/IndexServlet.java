package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.HtmlConverter;

public class IndexServlet extends AbstractServlet {

private static final long serialVersionUID = 1L;

	public static final String REPORT_QUERY1 = "SELECT SP.label#LANG#, COUNT(*) FROM \"RESULT\" RS LEFT JOIN \"SPORT\" SP ON RS.id_sport=SP.id GROUP BY SP.label#LANG# ORDER BY 2 DESC LIMIT 15";
	public static final String REPORT_QUERY2 = "SELECT CN.label#LANG#, COUNT(*) FROM \"COUNTRY\" CN LEFT JOIN \"PERSON\" PR ON PR.id_country=CN.id GROUP BY CN.label#LANG# ORDER BY 2 DESC LIMIT 15";
	
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
			else if (hParams.containsKey("lastupdates")) { // Last Updates
		        ArrayList<Object> lParams = new ArrayList<Object>();
		        Integer count = new Integer(String.valueOf(hParams.get("count")));
		        Integer offset = new Integer(String.valueOf(hParams.get("offset")));
		        lParams.add(count);
		        lParams.add(offset);
		        lParams.add("_" + lang);
		        ServletHelper.writeTabHtml(response, HtmlConverter.convertLastUpdates(DatabaseHelper.call("LastUpdates", lParams), count, offset, getLocale(request)), getLocale(request));
			}
			else if (hParams.containsKey("report")) { // Report
				ArrayList<String> lReport = new ArrayList<String>();
				lReport.add(REPORT_QUERY1);
				lReport.add(REPORT_QUERY2);
				int index = Integer.parseInt(String.valueOf(hParams.get("report")));
				String lang_ = (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? "_" + lang : "");				
				List<Object[]> list = DatabaseHelper.executeNative(lReport.get(index).replaceAll("#LANG#", lang_));
				StringBuffer sb1 = new StringBuffer();
				StringBuffer sb2 = new StringBuffer();
				for (Object[] t : list) {
					sb1.append(sb1.toString().length() > 0 ? "|" : "").append(t[0]);
					sb2.append(sb2.toString().length() > 0 ? "|" : "").append(t[1]);
				}
				ServletHelper.writeText(response, sb1.toString() + "~" + sb2.toString());
			}
		}
		catch (Exception e) {
			handleException(e);
		}
	}
	
	public static String getSportDivs(String lang) throws Exception {
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
		int count = 1;
		for (Integer i : lId) {
			if (index > 0 && index % 7 == 0) {
				sports.append("</div><div class='slide'>");
				count++;
			}
			sports.append(hSports.get(i).replaceAll("#INDEX#", String.valueOf(index)));
			index++;
			if (index == 7)
				slide1 = sports.toString();
			if (count > 5 && index % 7 == 0)
				break;
		}
		sports.append("</div>");
		
		return sports.append(slide1).toString().replaceAll("\"", "\\\\\"");
	}
	
}