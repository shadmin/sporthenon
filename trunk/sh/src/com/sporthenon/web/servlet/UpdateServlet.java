package com.sporthenon.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Event;

public class UpdateServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			if (hParams.containsKey("p") && hParams.get("p").equals("ajax")) {
				String lang = getLocale(request);
				String field = String.valueOf(hParams.get("p2"));
				String value = hParams.get("value") + "%";
				HashMap<String, String> hTable = new HashMap<String, String>();
				hTable.put("sp", "Sport");
				hTable.put("cp", "Championship");
				hTable.put("ev", "Event");
				hTable.put("se", "Event");
				hTable.put("se2", "Event");
				hTable.put("yr", "Year");
				hTable.put("yr", "Year");
				hTable.put("pl1", "Complex");
				hTable.put("pl2", "Complex");
				String labelHQL = "lower(label" + (lang != null && !lang.equalsIgnoreCase("en") && !field.equals("yr") ? lang.toUpperCase() : "") + ")";
				List<Object> l = DatabaseHelper.execute("from " + hTable.get(field) + " where " + labelHQL + " like '" + value.toLowerCase() + "' order by " + labelHQL);
				if (field.matches("pl\\d"))
					l.addAll(DatabaseHelper.execute("from City where " + labelHQL + " like '" + value.toLowerCase() + "' order by " + labelHQL));
				StringBuffer html = new StringBuffer("<ul>");
				for (Object o : l) {
					Method m1 = o.getClass().getMethod("getId");
					Method m2 = o.getClass().getMethod("getLabel", String.class);
					String id = String.valueOf(m1.invoke(o));
					String text = String.valueOf(m2.invoke(o, lang));
					if (o instanceof Event) {
						Event e = (Event) o;
						text += " [" + e.getType().getLabel(lang) + "]";
					}
					else if (o instanceof Complex) {
						Complex c = (Complex) o;
						text += ", " + c.getCity().getLabel(lang) + ", " + c.getCity().getCountry().getLabel(lang);
					}
					else if (o instanceof City) {
						City c = (City) o;
						text += ", " + c.getCountry().getLabel(lang);
					}
					html.append("<li id='" + field + "-" + id + "'>" + text + "</li>");
				}
				ServletHelper.writeText(response, html.append("</ul>").toString());
			}
		}
		catch (Exception e) {
			handleException(e);
		}
	}

}