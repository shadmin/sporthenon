package com.sporthenon.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.sporthenon.db.PicklistItem;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;

public class ServletHelper {
	
	public static HashMap<String, Object> getParams(HttpServletRequest req) throws Exception {
		HashMap<String, Object> hParams = new HashMap<String, Object>();
		for (String key : Collections.list(req.getParameterNames()))
			hParams.put(key, req.getParameter(key));
		return hParams;
	}
	
	public static void writePicklist(HttpServletResponse response, Collection<PicklistItem> picklist, String plId) throws Exception {
        Document doc = DocumentFactory.getInstance().createDocument();
        Element root = doc.addElement("picklist");
        root.addAttribute("id", plId);
        if (picklist != null && picklist.size() > 0)
        	for (PicklistItem plb : picklist) {
        		Element item = root.addElement("item");
        		item.addAttribute("value", String.valueOf(plb.getValue()));
        		item.addAttribute("text", plb.getText());
        		if (plb.getParam() != null)
        			item.addAttribute("param", String.valueOf(plb.getParam()));
        	}
        response.setContentType("text/xml");
        response.setCharacterEncoding("utf-8");
        XMLWriter writer = new XMLWriter(response.getOutputStream(), OutputFormat.createPrettyPrint());
        writer.write(doc);
        writer.flush();
        response.flushBuffer();
	}
	
	public static void writeTabHtml(HttpServletRequest request, HttpServletResponse response, StringBuffer sb, String lang) throws IOException, ParseException {
		boolean isMoreItems = sb.toString().startsWith("<tr");
		if (!isMoreItems)
			sb.append("<p id=\"errorlink\"><a href=\"javascript:displayErrorReport();\">" + StringUtils.text("report.error", request.getSession()) + "</p></span>");
		String s = sb.toString();
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
        if (s.contains("#INFO#")) {
        	StringBuffer sbInfo = new StringBuffer();
        	sbInfo.append(StringUtils.getSizeBytes(s));
        	sbInfo.append("|#DTIME#");
        	sbInfo.append("|" + StringUtils.countIn(s, "<img"));
        	sbInfo.append("|" + lang);
        	sbInfo.append("|" + (StringUtils.notEmpty(request.getAttribute("lastupdate")) ? request.getAttribute("lastupdate") : ""));
        	s = s.replaceAll("\\#INFO\\#", sbInfo.toString());
        }
        if (!isMoreItems) {
        	s = s.replaceAll("\\shref\\=", " target='_blank' href=");
        }
        PrintWriter writer = response.getWriter();
        writer.write(s);
        response.flushBuffer();
	}
	
	public static void writePageHtml(HttpServletRequest request, HttpServletResponse response, StringBuffer sb, String lang, boolean isPrint) throws ServletException, IOException, ParseException {
		String s = sb.append(!isPrint ? "<p id=\"errorlink\"><a href=\"javascript:displayErrorReport();\">" + StringUtils.text("report.error", request.getSession()) + "</a></p>" : "").toString();
		if (s.contains("#INFO#")) {
			StringBuffer sbInfo = new StringBuffer();
			sbInfo.append(StringUtils.getSizeBytes(s));
			sbInfo.append("|#DTIME#");
			sbInfo.append("|" + StringUtils.countIn(s, "<img"));
			sbInfo.append("|" + lang);
			sbInfo.append("|" + (StringUtils.notEmpty(request.getAttribute("lastupdate")) ? request.getAttribute("lastupdate") : ""));
			s = s.replaceAll("\\#INFO\\#", sbInfo.toString());
		}
		request.setAttribute("version", "v=" + ConfigUtils.getProperty("version"));
		request.setAttribute("html", s);
		request.setAttribute("t2", System.currentTimeMillis());
		request.getRequestDispatcher("/jsp/db/" + (isPrint ? "print" : "default") + ".jsp").forward(request, response);
	}
	
	public static void writeText(HttpServletResponse response, String s) throws IOException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(s);
        response.flushBuffer();
	}
	
	public static String getURL(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + (request.getServerPort() > 80 ? ":" + request.getServerPort() : "") + request.getRequestURI() + (StringUtils.notEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "");
	}
	
}