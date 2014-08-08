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
import com.sporthenon.db.converter.HtmlConverter;
import com.sporthenon.db.function.LastUpdateBean;
import com.sporthenon.db.function.StatisticsBean;
import com.sporthenon.utils.StringUtils;
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
			else if (hParams.containsKey("lastupdates")) { // Last Updatess
		        ArrayList<Object> lParams = new ArrayList<Object>();
		        lParams.add(new Integer(String.valueOf(hParams.get("count"))));
		        lParams.add("_" + getLocale(request));
		        ServletHelper.writeHtml(response, HtmlConverter.convertLastUpdates(DatabaseHelper.call("LastUpdates", lParams), getLocale(request)), getLocale(request));
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
		        
		        // Last Updates
		        ArrayList<Object> lParams = new ArrayList<Object>();
		        lParams.add(new Integer(10));
		        lParams.add("_" + getLocale(request));
		        ArrayList<LastUpdateBean> lUpdates = new ArrayList(DatabaseHelper.call("LastUpdates", lParams));
		        Element updates = doc.createElement("updates");
		        for (LastUpdateBean bean : lUpdates) {
		        	item = doc.createElement("update");
			        item.setAttribute("yr", bean.getYrLabel());
			        item.setAttribute("sp", bean.getSpLabel());
			        item.setAttribute("cp", bean.getCpLabel());
			        item.setAttribute("ev", bean.getEvLabel());
			        item.setAttribute("se", bean.getSeLabel());
			        item.setAttribute("link", "results?" + bean.getSpId() + "-" + bean.getCpId() + "-" + bean.getEvId() + "-" + (bean.getSeId() != null ? bean.getSeId() : 0) + "-0");
			        updates.appendChild(item);
		        }
		        root.appendChild(updates);
		        
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
	
}