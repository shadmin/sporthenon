package com.sporthenon.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.Year;
import com.sporthenon.utils.StringUtils;

public class UpdateServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			if (hParams.containsKey("p") && hParams.get("p").equals("ajax")) { // Ajax autocompletion
				String lang = getLocale(request);
				String field = String.valueOf(hParams.get("p2"));
				String value = hParams.get("value") + "%";
				String sport = null;
				if (field.matches("(pr|tm|cn)\\-.*")) {
					String[] t = field.split("\\-", -1);
					field = t[0];
					sport = t[1];
				}
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
				hTable.put("pr", "Athlete");
				hTable.put("tm", "Team");
				hTable.put("cn", "Country");
				String labelHQL = "lower(label" + (lang != null && !lang.equalsIgnoreCase("en") && !field.equals("yr") ? lang.toUpperCase() : "") + ")";
				String whereHQL = "";
				if( field.matches("pr")) {
					labelHQL = "lower(last_name) || ', ' || lower(first_name)";
					whereHQL = (sport != null ? " and sport.id=" + sport : "");
				}
				List<Object> l = DatabaseHelper.execute("from " + hTable.get(field) + " where " + labelHQL + " like '" + value.toLowerCase() + "'" + whereHQL + " order by " + labelHQL);
				if (field.matches("pl\\d"))
					l.addAll(DatabaseHelper.execute("from City where " + labelHQL + " like '" + value.toLowerCase() + "' order by " + labelHQL));
				StringBuffer html = new StringBuffer("<ul>");
				for (Object o : l) {
					Method m1 = o.getClass().getMethod("getId");
					String id = String.valueOf(m1.invoke(o));
					String text = null;
					if (!(o instanceof Athlete)) {
						Method m2 = o.getClass().getMethod("getLabel", String.class);
						text = String.valueOf(m2.invoke(o, lang));	
					}
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
					else if (o instanceof Athlete) {
						Athlete a = (Athlete) o;
						text = a.getLastName() + ", " + a.getFirstName() + (a.getCountry() != null ? " [" + a.getCountry().getCode() + "]" : "") + (a.getTeam() != null ? " [" + a.getTeam().getLabel() + "]" : "");
					}
					html.append("<li id='" + field + "-" + id + (o instanceof Event ? "-" + ((Event)o).getType().getNumber() : "") + "'>" + text + "</li>");
				}
				ServletHelper.writeText(response, html.append("</ul>").toString());
			}
			else if (hParams.containsKey("p") && hParams.get("p").equals("save")) { // Save result
				Result rs = new Result();
				rs.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, hParams.get("sp")));
				rs.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, hParams.get("cp")));
				rs.setEvent((Event)DatabaseHelper.loadEntity(Event.class, hParams.get("ev")));
				rs.setSubevent((Event)DatabaseHelper.loadEntity(Event.class, StringUtils.notEmpty(hParams.get("se")) ? hParams.get("se") : 0));
				rs.setSubevent2((Event)DatabaseHelper.loadEntity(Event.class, StringUtils.notEmpty(hParams.get("se2")) ? hParams.get("se2") : 0));
				rs.setYear((Year)DatabaseHelper.loadEntity(Year.class, hParams.get("yr")));
				rs.setCity1((City)DatabaseHelper.loadEntity(City.class, StringUtils.notEmpty(hParams.get("pl1")) ? hParams.get("pl1") : 0));
				rs.setComplex1((Complex)DatabaseHelper.loadEntity(Complex.class, StringUtils.notEmpty(hParams.get("pl1")) ? hParams.get("pl1") : 0));
				rs.setCity2((City)DatabaseHelper.loadEntity(City.class, StringUtils.notEmpty(hParams.get("pl2")) ? hParams.get("pl2") : 0));
				rs.setComplex2((Complex)DatabaseHelper.loadEntity(Complex.class, StringUtils.notEmpty(hParams.get("pl2")) ? hParams.get("pl2") : 0));
				rs.setDate1(StringUtils.notEmpty(hParams.get("dt1")) ? String.valueOf(hParams.get("dt1")) : null);
				rs.setDate2(StringUtils.notEmpty(hParams.get("dt2")) ? String.valueOf(hParams.get("dt2")) : null);
				rs.setComment(StringUtils.notEmpty(hParams.get("cmt")) ? String.valueOf(hParams.get("cmt")) : null);
				rs.setExa(StringUtils.notEmpty(hParams.get("exa")) ? String.valueOf(hParams.get("exa")) : null);
				for (int i = 1 ; i <= 10 ; i++) {
					Integer id = (StringUtils.notEmpty(hParams.get("rk" + i)) ? new Integer(String.valueOf(hParams.get("rk" + i))) : 0);
					Result.class.getMethod("setIdRank" + i, Integer.class).invoke(rs, id > 0 ? id : null);
					if (i <= 5)
						Result.class.getMethod("setResult" + i, String.class).invoke(rs, StringUtils.notEmpty(hParams.get("rs" + i)) ? hParams.get("rs" + i) : null);
				}
				rs = (Result) DatabaseHelper.saveEntity(rs, getUser(request));
				ServletHelper.writeText(response, "update OK");
			}
		}
		catch (Exception e) {
			handleException(e);
		}
	}

}