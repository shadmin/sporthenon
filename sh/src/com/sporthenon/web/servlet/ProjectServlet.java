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
import com.sporthenon.db.function.StatRequestBean;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ProjectServlet extends AbstractServlet {

private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("stats");
			doc.appendChild(root);
			
			String index = String.valueOf(hParams.get("index"));
			ArrayList<Object> lFuncParams = new ArrayList<Object>();
			lFuncParams.add(Short.valueOf(index));
			int i = 0;
			for (Object o : DatabaseHelper.call("StatRequest", lFuncParams)) {
				StatRequestBean srb = (StatRequestBean) o;
				Element item = doc.createElement("stat");
				String key = srb.getKey();
				if (index.equals("0"))
					key = key.replaceAll("RS", "Results").replaceAll("OL", "Olympics").replaceAll("US", "US Leagues").replaceAll("SC", "Search").replaceAll("IF", "Info");
				item.setAttribute("key", key); item.setAttribute("value", String.valueOf(srb.getValue()));
				root.appendChild(item);
				if (++i == 12)
					break;
			}

			response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
			XMLSerializer serializer = new XMLSerializer();
			serializer.setOutputCharStream(response.getWriter());
			serializer.serialize(doc);
		}
		catch (Exception e) {
			handleException(e);
		}
	}
	
}