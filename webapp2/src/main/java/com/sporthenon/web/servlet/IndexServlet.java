package com.sporthenon.web.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.meta.ErrorReport;
import com.sporthenon.db.function.LastUpdateBean;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.HtmlConverter;
import com.sporthenon.web.ServletHelper;

@WebServlet(
    name = "IndexServlet",
    urlPatterns = {"/IndexServlet"}
)
public class IndexServlet extends AbstractServlet {

private static final long serialVersionUID = 1L;

	public static final String REPORT_QUERY1 = "SELECT SP.label#LANG#, COUNT(*) FROM result RS LEFT JOIN sport SP ON RS.id_sport=SP.id GROUP BY SP.label#LANG# ORDER BY 2 DESC LIMIT 10";
	public static final String REPORT_QUERY2 = "SELECT CN.label#LANG#, COUNT(*) FROM country CN LEFT JOIN athlete PR ON PR.id_country=CN.id GROUP BY CN.label#LANG# ORDER BY 2 DESC LIMIT 10";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			String lang = getLocale(request);
			HashMap<String, Object> mapParams = ServletHelper.getParams(request);
			if (mapParams.containsKey("p")) {
				String p = String.valueOf(mapParams.get("p"));
				p = StringUtils.decode(p);
				String[] t = p.split("\\-");
				mapParams.put("sport", t[0]);
				mapParams.put("count", t[1]);
				mapParams.put("offset", t[2]);
			}
			if (mapParams.containsKey("lang")) { // Language
		        request.getSession().setAttribute("locale", String.valueOf(mapParams.get("value")));
			}
			else if (mapParams.containsKey("error")) { // Error Report
				ErrorReport er = new ErrorReport();
				er.setUrl(String.valueOf(mapParams.get("url")));
				er.setText(String.valueOf(mapParams.get("text")));
				er.setDate(new Timestamp(System.currentTimeMillis()));
				DatabaseManager.saveEntity(er, null);
			}
			else if (mapParams.containsKey("lastupdates")) { // Last Updates
				final int ITEM_LIMIT = Integer.parseInt(ConfigUtils.getValue("default_lastupdates_limit"));
				boolean isFull = !mapParams.containsKey("p");
				Integer sport = StringUtils.toInt(mapParams.get("sport"));
				Integer count = (isFull ? ITEM_LIMIT : StringUtils.toInt(mapParams.get("count")));
		        Integer offset = (isFull ? 0 : StringUtils.toInt(mapParams.get("offset")));
		    	List<Object> params = new ArrayList<Object>();
		    	params.add(sport);
		    	params.add(count);
		    	params.add(offset);
		    	params.add(ResourceUtils.getLocaleParam(lang));
		    	StringBuffer html = HtmlConverter.convertLastUpdates(DatabaseManager.callFunction("_last_updates", params, LastUpdateBean.class), sport, count, offset, getLocale(request));
		    	ServletHelper.writeHtmlResponse(request, response, html, getLocale(request));
			}
			else if (mapParams.containsKey("randomevent")) { // Random Event
				ServletHelper.writeText(response, getRandomEvent(lang));
			}
			else if (mapParams.containsKey("report")) { // Report
				List<String> lReport = new ArrayList<String>();
				lReport.add(REPORT_QUERY1);
				lReport.add(REPORT_QUERY2);
				int index = Integer.parseInt(String.valueOf(mapParams.get("report")));
				String langParam = ResourceUtils.getLocaleParam(lang);				
				List<Object[]> list = (List<Object[]>) DatabaseManager.executeSelect(lReport.get(index).replaceAll("#LANG#", langParam));
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
		for (Object obj : DatabaseManager.executeSelect("SELECT * FROM sport ORDER BY index", Sport.class)) {
			Sport sp = (Sport) obj;
			String img = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, sp.getId(), ImageUtils.SIZE_LARGE, null, null);
			String text = sp.getLabel(lang);
			img = img.replaceAll(".*\\ssrc\\='|'/\\>", "");
			hSports.put(sp.getId(), "<div id='sport-#INDEX#' class='sport' style=\"background-image:url('" + img + "');" + "\">" + HtmlUtils.writeLink(Sport.alias, sp.getId(), text, sp.getLabel()) + "</div>");
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
	
	@SuppressWarnings("unchecked")
	public static String getRandomEvent(String lang) {
		String s = "";
		try {
			Integer max = ((List<Integer>)DatabaseManager.executeSelect("SELECT MAX(id) FROM result", Integer.class)).get(0);
			int id = new Random().nextInt(max);
			Result rs = (Result) DatabaseManager.loadEntity(Result.class, id);
			if (rs != null) {
				s = HtmlUtils.writeURL("/results", rs.getSport().getId() + "-" + rs.getChampionship().getId() + "-" + rs.getEvent().getId() + (rs.getSubevent() != null ? "-" + rs.getSubevent().getId() : "") + (rs.getSubevent2() != null ? "-" + rs.getSubevent2().getId() : ""), rs.getSport().getLabel() + "/" + rs.getChampionship().getLabel() + (rs.getEvent() != null ? "/" + rs.getEvent().getLabel() + (rs.getSubevent() != null ? "/" + rs.getSubevent().getLabel() : "") + (rs.getSubevent2() != null ? "/" + rs.getSubevent2().getLabel() : "") : ""));
				s = "<a href='" + s + "'>" + rs.getSport().getLabel(lang) + " " + StringUtils.SEP1 + " " + rs.getChampionship().getLabel(lang) + " " + StringUtils.SEP1 + " " + rs.getEvent().getLabel(lang) + (rs.getSubevent() != null ? " " + StringUtils.SEP1 + " " + rs.getSubevent().getLabel(lang) : "") + (rs.getSubevent2() != null ? " " + StringUtils.SEP1 + " " + rs.getSubevent2().getLabel(lang) : "") + "</a>";
			}
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
		return s;
	}
	
}