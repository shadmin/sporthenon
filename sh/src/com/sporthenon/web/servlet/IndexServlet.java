package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.function.StatisticsBean;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.web.HtmlConverter;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class IndexServlet extends AbstractServlet {

private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
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
		        lParams.add("_" + getLocale(request));
		        ServletHelper.writeTabHtml(response, HtmlConverter.convertLastUpdates(DatabaseHelper.call("LastUpdates", lParams), count, offset, getLocale(request)), getLocale(request));
			}
			else {
				DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		        Document doc = docBuilder.newDocument();
		        Element root = doc.createElement("home");
		        doc.appendChild(root);
		        
		        // Statistics
		        ArrayList<StatisticsBean> lStats = new ArrayList(DatabaseHelper.call("Statistics", null));
				StatisticsBean stb = lStats.get(0);
		        Element stats = doc.createElement("stats");
		        Element item = doc.createElement("stat");
		        item.setAttribute("id", "count-sport"); item.setAttribute("value", StringUtils.formatNumber(stb.getCountSport()));
		        stats.appendChild(item);
		        item = doc.createElement("stat");
		        item.setAttribute("id", "count-event"); item.setAttribute("value", StringUtils.formatNumber(stb.getCountEvent()));
		        stats.appendChild(item);
		        item = doc.createElement("stat");
		        item.setAttribute("id", "count-result"); item.setAttribute("value", StringUtils.formatNumber(stb.getCountResult()));
		        stats.appendChild(item);
		        item = doc.createElement("stat");
		        item.setAttribute("id", "count-person"); item.setAttribute("value", StringUtils.formatNumber(stb.getCountPerson()));
		        stats.appendChild(item);
		        root.appendChild(stats);
		        
		        response.setContentType("text/xml");
	        	response.setCharacterEncoding("utf-8");
		        XMLSerializer serializer = new XMLSerializer();
		        serializer.setOutputCharStream(response.getWriter());
		        serializer.serialize(doc);
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