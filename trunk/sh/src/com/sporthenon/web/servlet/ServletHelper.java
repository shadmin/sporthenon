package com.sporthenon.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sporthenon.db.PicklistBean;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ServletHelper {
	
	public static HashMap<String, Object> getParams(HttpServletRequest req) throws Exception {
		HashMap<String, Object> hParams = new HashMap<String, Object>();
		for (String key : Collections.list(req.getParameterNames()))
			hParams.put(key, req.getParameter(key));
		return hParams;
	}
	
	public static void writePicklist(HttpServletResponse res, Collection<PicklistBean> picklist, String plId) throws Exception {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("picklist");
        root.setAttribute("id", plId);
        doc.appendChild(root);
        if (picklist != null && picklist.size() > 0)
        	for (PicklistBean plb : picklist) {
        		Element item = doc.createElement("item");
        		item.setAttribute("value", String.valueOf(plb.getValue()));
        		item.setAttribute("text", plb.getText());
        		if (plb.getParam() != null)
        			item.setAttribute("param", String.valueOf(plb.getParam()));
        		root.appendChild(item);
        	}
        res.setContentType("text/xml");
        res.setCharacterEncoding("utf-8");
        XMLSerializer serializer = new XMLSerializer();
        serializer.setOutputCharStream(res.getWriter());
        serializer.serialize(doc);
	}
	
	public static void writeTabHtml(HttpServletResponse res, StringBuffer sb, String lang) throws IOException {
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
        String sBacktop = "<div class='backtop###' onclick='backTop();'><img alt='top' title='" + ResourceUtils.getText("back.top", lang) + "' src='img/render/back-top.gif'/></div>";
        PrintWriter writer = res.getWriter();
        writer.write(s + (s.matches(".*'tsort'.*") ? sBacktop.replaceAll("\\#\\#\\#", "left") + sBacktop.replaceAll("\\#\\#\\#", "right") : ""));
	}
	
	public static void writePageHtml(HttpServletRequest req, HttpServletResponse res, StringBuffer sb, boolean isPrint) throws ServletException, IOException {
		String s = sb.toString();
		if (s.matches(".*\\#INFO\\#.*")) {
			StringBuffer sbInfo = new StringBuffer();
			sbInfo.append(StringUtils.getSizeBytes(s));
			sbInfo.append("|#DTIME#");
			sbInfo.append("|" + StringUtils.countIn(s, "<img"));
			s = s.replaceAll("\\#INFO\\#", sbInfo.toString());
		}
		req.setAttribute("version", "v=" + ConfigUtils.getProperty("version"));
		req.setAttribute("html", s);
		req.getRequestDispatcher("/jsp/db/" + (isPrint ? "print" : "default") + ".jsp").forward(req, res);
	}
	
	public static void writeText(HttpServletResponse res, String s) throws IOException {
		res.setContentType("text/plain");
        res.setCharacterEncoding("utf-8");
        PrintWriter writer = res.getWriter();
        writer.write(s);
	}

}