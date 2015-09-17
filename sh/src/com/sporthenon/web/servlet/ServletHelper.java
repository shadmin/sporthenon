package com.sporthenon.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.sporthenon.db.PicklistBean;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;

public class ServletHelper {
	
	public static HashMap<String, Object> getParams(HttpServletRequest req) throws Exception {
		HashMap<String, Object> hParams = new HashMap<String, Object>();
		for (String key : Collections.list(req.getParameterNames()))
			hParams.put(key, req.getParameter(key));
		return hParams;
	}
	
	public static void writePicklist(HttpServletResponse res, Collection<PicklistBean> picklist, String plId) throws Exception {
        Document doc = DocumentFactory.getInstance().createDocument();
        Element root = doc.addElement("picklist");
        root.addAttribute("id", plId);
        if (picklist != null && picklist.size() > 0)
        	for (PicklistBean plb : picklist) {
        		Element item = root.addElement("item");
        		item.addAttribute("value", String.valueOf(plb.getValue()));
        		item.addAttribute("text", plb.getText());
        		if (plb.getParam() != null)
        			item.addAttribute("param", String.valueOf(plb.getParam()));
        	}
        res.setContentType("text/xml");
        res.setCharacterEncoding("utf-8");
        XMLWriter writer = new XMLWriter(res.getOutputStream(), OutputFormat.createPrettyPrint());
        writer.write(doc);
        writer.flush();
        res.flushBuffer();
	}
	
	public static void writeTabHtml(HttpServletRequest req, HttpServletResponse res, StringBuffer sb, String lang) throws IOException {
		if (!sb.toString().startsWith("<tr"))
			sb.append("<p id=\"errorlink\"><a href=\"javascript:displayErrorReport();\">" + StringUtils.text("report.error", req.getSession()) + "</p></span>");
		String s = sb.toString();
		res.setContentType("text/html");
        res.setCharacterEncoding("utf-8");
        if (s.matches(".*\\#INFO\\#.*")) {
        	StringBuffer sbInfo = new StringBuffer();
        	sbInfo.append(StringUtils.getSizeBytes(s));
        	sbInfo.append("|#DTIME#");
        	sbInfo.append("|" + StringUtils.countIn(s, "<img"));
        	s = s.replaceAll("\\#INFO\\#", sbInfo.toString());
        }
        s = s.replaceAll("\\shref\\=", " target='_blank' href=");
        PrintWriter writer = res.getWriter();
        writer.write(s);
        res.flushBuffer();
	}
	
	public static void writePageHtml(HttpServletRequest req, HttpServletResponse res, StringBuffer sb, boolean isPrint) throws ServletException, IOException {
		String s = sb.append("<p id=\"errorlink\"><a href=\"javascript:displayErrorReport();\">" + StringUtils.text("report.error", req.getSession()) + "</a></p>").toString();
		if (s.matches(".*\\#INFO\\#.*")) {
			StringBuffer sbInfo = new StringBuffer();
			sbInfo.append(StringUtils.getSizeBytes(s));
			sbInfo.append("|#DTIME#");
			sbInfo.append("|" + StringUtils.countIn(s, "<img"));
			s = s.replaceAll("\\#INFO\\#", sbInfo.toString());
		}
		req.setAttribute("version", "v=" + ConfigUtils.getProperty("version"));
		req.setAttribute("html", s);
		req.setAttribute("t2", System.currentTimeMillis());
		req.getRequestDispatcher("/jsp/db/" + (isPrint ? "print" : "default") + ".jsp").forward(req, res);
	}
	
	public static void writeText(HttpServletResponse res, String s) throws IOException {
		res.setContentType("text/plain");
        res.setCharacterEncoding("utf-8");
        PrintWriter writer = res.getWriter();
        writer.write(s);
        res.flushBuffer();
	}
	
}