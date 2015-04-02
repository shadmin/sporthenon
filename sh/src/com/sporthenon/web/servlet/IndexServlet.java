package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.PicklistBean;
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
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			if (hParams.containsKey("lang")) { // Language
		        request.getSession().setAttribute("locale", String.valueOf(hParams.get("value")));
			}
			else if (hParams.containsKey("lastupdates")) { // Last Updates
		        ArrayList<Object> lParams = new ArrayList<Object>();
		        lParams.add(new Integer(String.valueOf(hParams.get("count"))));
		        lParams.add("_" + getLocale(request));
		        ServletHelper.writeTabHtml(response, HtmlConverter.convertLastUpdates(DatabaseHelper.call("LastUpdates", lParams), getLocale(request)), getLocale(request));
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
		Collection<PicklistBean> cPicklist = DatabaseHelper.getEntityPicklist(Sport.class, "label", null, lang);
		for (PicklistBean plb : cPicklist) {
			String img = HtmlUtils.writeImage(ImageUtils.INDEX_SPORT, plb.getValue(), ImageUtils.SIZE_LARGE, null, null);
			img = img.replaceAll(".*\\ssrc\\='|'/\\>", "");
			hSports.put(plb.getValue(), "<div id='sport-#INDEX#' class='sport' style=\"background-image:url('" + img + "');\">" + HtmlUtils.writeLink(Sport.alias, plb.getValue(), plb.getText().replaceAll("\\s", "&nbsp;")) + "</div>");
		}
		StringBuffer sports = new StringBuffer();
		int index = 0;
		
		// Slide #1
		sports.append("<div class='slide'>");
		for (Integer i : new Integer[]{21, 7, 5, 22, 1, 24, 19}) // Football, Rugby, Auto Racing, Tennis, Athletics, Basketball, Cycling
			sports.append(hSports.get(i).replaceAll("#INDEX#", String.valueOf(++index)));
		sports.append("</div>");
		String slide1 = sports.toString();

		// Slide #2
		sports.append("<div class='slide'>");
		for (Integer i : new Integer[]{30, 13, 20, 2, 8, 15, 14}) // Boxing, , Volleyball, Golf, Swimming, Alpine Skiing, Bobsleigh, Fencing
			sports.append(hSports.get(i).replaceAll("#INDEX#", String.valueOf(++index)));
		sports.append("</div>");
		
		// Slide #3
		sports.append("<div class='slide'>");
		for (Integer i : new Integer[]{18, 25, 42, 48, 45, 50, 41}) // Motorcycling, Ice Hockey, Judo, Cricket, Squash, Wrestling, Diving
			sports.append(hSports.get(i).replaceAll("#INDEX#", String.valueOf(++index)));
		sports.append("</div>");
		
		// Slide #4
		sports.append("<div class='slide'>");
		for (Integer i : new Integer[]{34, 31, 27, 47, 32, 26, 38}) // Weightlifting, Archery, Badminton, Gymnastics, Canoeing, Baseball, Curling
			sports.append(hSports.get(i).replaceAll("#INDEX#", String.valueOf(++index)));
		sports.append("</div>");

		// Slide #5
		sports.append("<div class='slide'>");
		for (Integer i : new Integer[]{4, 35, 36, 3, 52, 40, 16}) // Handball, Ice Skating, Surfing, Table Tennis, Mountainbiking, Waterpolo, Luge
			sports.append(hSports.get(i).replaceAll("#INDEX#", String.valueOf(++index)));
		sports.append("</div>");
		
		return sports.append(slide1).toString().replaceAll("\"", "\\\\\"");
	}
	
}