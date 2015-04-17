package com.sporthenon.web.servlet;

import java.io.IOException;
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
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class AndroidServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;

	public AndroidServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			String lang = (hParams.containsKey("lang") ? String.valueOf(hParams.get("lang")) : ResourceUtils.LGDEFAULT);
			String p = String.valueOf(hParams.get("p"));
			String p2 = String.valueOf(hParams.get("p2"));
			
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	        Document doc = docBuilder.newDocument();
	        Element root = doc.createElement("picklist");
	        root.setAttribute("id", p2);
	        doc.appendChild(root);
	        if (p2.equalsIgnoreCase(Sport.alias))
	        	addItems(doc, root, ImageUtils.INDEX_SPORT, DatabaseHelper.getEntityPicklist(Sport.class, "label", null, lang));
	        else if (p2.equalsIgnoreCase(Championship.alias)) {
	        	String filter = "sport.id=" + p;
	        	addItems(doc, root, ImageUtils.INDEX_CHAMPIONSHIP, DatabaseHelper.getPicklist(Result.class, "championship", filter, null, (short) 1, lang));
	        }
	        else if (p2.equalsIgnoreCase(Event.alias)) {
	        	String[] t = p.split("\\-");
	        	String filter = "sport.id=" + t[0] + " and championship.id=" + t[1];
	        	addItems(doc, root, ImageUtils.INDEX_EVENT, DatabaseHelper.getPicklist(Result.class, "event", filter, null, (short) 1, lang));
	        }
	        
	        response.setContentType("text/xml");
	        response.setCharacterEncoding("utf-8");
	        XMLSerializer serializer = new XMLSerializer();
	        serializer.setOutputCharStream(response.getWriter());
	        serializer.serialize(doc);
	        response.flushBuffer();
		}
		catch (Exception e) {
			handleException(e);
		}
	}
	
	private void addItems(Document doc, Element root, short index, Collection<PicklistBean> picklist) {
		if (picklist != null && picklist.size() > 0) {
			for (PicklistBean plb : picklist) {
				Element item = doc.createElement("item");
				String img = HtmlUtils.writeImage(index, plb.getValue(), ImageUtils.SIZE_LARGE, null, null);
				item.setAttribute("value", String.valueOf(plb.getValue()));
				item.setAttribute("text", plb.getText());
				item.setAttribute("img", img.replaceAll(".*src\\='|'\\/\\>", ""));
				root.appendChild(item);
			}
		}
	}

}