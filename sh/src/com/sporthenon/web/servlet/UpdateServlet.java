package com.sporthenon.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Draw;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.Type;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.entity.meta.ExternalLink;
import com.sporthenon.db.entity.meta.PersonList;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class UpdateServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String lang = getLocale(request);
			Contributor user = getUser(request);
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			if (hParams.containsKey("p2") && hParams.get("p2").equals("ajax")) { // Ajax autocompletion
				String field = String.valueOf(hParams.get("p"));
				String value = (hParams.get("value") + "%").replaceAll("\\*", "%").replaceAll("'", "''");
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
				String labelHQL = "lower(label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) && !field.matches("tm|yr") ? lang.toUpperCase() : "") + ")";
				String whereHQL = "";
				if( field.equalsIgnoreCase(Athlete.alias)) {
					labelHQL = "lower(last_name) || ', ' || lower(first_name)";
					whereHQL = (sport != null ? " and sport.id=" + sport : "");
				}
				else if( field.equalsIgnoreCase(Team.alias))
					whereHQL = (sport != null ? " and sport.id=" + sport : "");
				List<Object> l = DatabaseHelper.execute("from " + hTable.get(field) + " where " + labelHQL + " like '" + value.toLowerCase() + "'" + whereHQL + " order by " + labelHQL);
				if (field.matches("pl\\d"))
					l.addAll(DatabaseHelper.execute("from City where " + labelHQL + " like '" + value.toLowerCase() + "' order by " + labelHQL));
				StringBuffer html = new StringBuffer("<ul>");
				for (Object o : l) {
					Method m1 = o.getClass().getMethod("getId");
					String id = String.valueOf(m1.invoke(o));
					String text = null;
					if (!(o instanceof Athlete) && !(o instanceof Team)) {
						Method m2 = o.getClass().getMethod("getLabel", String.class);
						text = String.valueOf(m2.invoke(o, lang));	
					}
					if (o instanceof Event) {
						Event e = (Event) o;
						text += " [" + e.getType().getLabel(lang) + "]";
					}
					else if (o instanceof Complex) {
						Complex c = (Complex) o;
						text += ", " + c.getCity().getLabel(lang) + (c.getCity().getState() != null ? ", " + c.getCity().getState().getCode() : "") + ", " + c.getCity().getCountry().getCode();
					}
					else if (o instanceof City) {
						City c = (City) o;
						text += (c.getState() != null ? ", " + c.getState().getCode() : "") + ", " + c.getCountry().getCode();
					}
					else if (o instanceof Athlete) {
						Athlete a = (Athlete) o;
						text = a.toString2();
					}
					else if (o instanceof Team) {
						Team t = (Team) o;
						text = t.getLabel() + (t.getCountry() != null ? " [" + t.getCountry().getCode() + "]" : "");
					}
					html.append("<li id='" + field + "-" + id + (o instanceof Event ? "-" + ((Event)o).getType().getNumber() : "") + "'>" + text + "</li>");
				}
				ServletHelper.writeText(response, html.append("</ul>").toString());
			}
			else if (hParams.containsKey("p") && hParams.get("p").equals("save")) { // Save result
				HashMap<Object, Integer> hInserted = new HashMap<Object, Integer>();
				StringBuffer sbMsg = new StringBuffer();
				try {
					int tp = 0;
					HashMap<String, Type> hType = new HashMap<String, Type>();
					for (Type type : (List<Type>) DatabaseHelper.execute("from Type"))
						hType.put(type.getLabel(lang), type);
					Integer idRS = (StringUtils.notEmpty(hParams.get("id")) ? Integer.valueOf(String.valueOf(hParams.get("id"))) : null);
					Result result = (idRS != null ? (Result)DatabaseHelper.loadEntity(Result.class, idRS) : new Result());
					// Sport
					result.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, hParams.get("sp")));
					if (result.getSport() == null) {
						Sport s = new Sport();
						s.setLabel(String.valueOf(hParams.get("sp-l")));
						s.setLabelFr(s.getLabel());
						s.setType(1);
						s.setIndex(Float.MAX_VALUE);
						s = (Sport) DatabaseHelper.saveEntity(s, user);
						result.setSport(s);
					}
					// Championship
					result.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, hParams.get("cp")));
					if (result.getChampionship() == null) {
						Championship c = new Championship();
						c.setLabel(String.valueOf(hParams.get("cp-l")));
						c.setLabelFr(c.getLabel());
						c.setIndex(Integer.MAX_VALUE);
						c = (Championship) DatabaseHelper.saveEntity(c, user);
						result.setChampionship(c);
					}
					// Event #1
					result.setEvent((Event)DatabaseHelper.loadEntity(Event.class, hParams.get("ev")));
					if (result.getEvent() == null) {
						String label_ = String.valueOf(hParams.get("ev-l"));
						Type type_ = (Type)DatabaseHelper.loadEntity(Type.class, 1);
						if (label_.contains(" (")) {
							String[] t_ = label_.replaceAll("\\)", "").split("\\s\\(");
							label_ = t_[0];
							type_ = (hType.containsKey(t_[1]) ? hType.get(t_[1]) : type_);
						}
						Event e = new Event();
						e.setLabel(label_);
						e.setLabelFr(label_);
						e.setType(type_);
						e.setIndex(Integer.MAX_VALUE);
						e = (Event) DatabaseHelper.saveEntity(e, user);
						result.setEvent(e);
					}
					tp = result.getEvent().getType().getNumber();
					// Event #2
					if (StringUtils.notEmpty(hParams.get("se")) || StringUtils.notEmpty(hParams.get("se-l"))) {
						result.setSubevent((Event)DatabaseHelper.loadEntity(Event.class, hParams.get("se")));
						if (result.getSubevent() == null) {
							String label_ = String.valueOf(hParams.get("se-l"));
							Type type_ = (Type)DatabaseHelper.loadEntity(Type.class, 1);
							if (label_.contains(" (")) {
								String[] t_ = label_.replaceAll("\\)", "").split("\\s\\(");
								label_ = t_[0];
								type_ = (hType.containsKey(t_[1]) ? hType.get(t_[1]) : type_);
							}
							Event e = new Event();
							e.setLabel(label_);
							e.setLabelFr(label_);
							e.setType(type_);
							e.setIndex(Integer.MAX_VALUE);
							e = (Event) DatabaseHelper.saveEntity(e, user);
							result.setSubevent(e);
						}
						tp = result.getSubevent().getType().getNumber();
					}
					// Event #3
					if (StringUtils.notEmpty(hParams.get("se2")) || StringUtils.notEmpty(hParams.get("se2-l"))) {
						result.setSubevent2((Event)DatabaseHelper.loadEntity(Event.class, hParams.get("se2")));
						if (result.getSubevent2() == null) {
							String label_ = String.valueOf(hParams.get("se2-l"));
							Type type_ = (Type)DatabaseHelper.loadEntity(Type.class, 1);
							if (label_.contains(" (")) {
								String[] t_ = label_.replaceAll("\\)", "").split("\\s\\(");
								label_ = t_[0];
								type_ = (hType.containsKey(t_[1]) ? hType.get(t_[1]) : type_);
							}
							Event e = new Event();
							e.setLabel(label_);
							e.setLabelFr(label_);
							e.setType(type_);
							e.setIndex(Integer.MAX_VALUE);
							e = (Event) DatabaseHelper.saveEntity(e, user);
							result.setSubevent2(e);
						}
						tp = result.getSubevent2().getType().getNumber();
					}
					// Year
					result.setYear((Year)DatabaseHelper.loadEntity(Year.class, hParams.get("yr")));
					if (result.getYear() == null) {
						Year y = new Year();
						y.setLabel(String.valueOf(hParams.get("yr-l")));
						y = (Year) DatabaseHelper.saveEntity(y, user);
						result.setYear(y);
					}
					// Places
					for(int i : new int[]{1, 2}) {
						if (StringUtils.notEmpty(hParams.get("pl" + i + "-l"))) {
							String[] t = String.valueOf(hParams.get("pl" + i + "-l")).toLowerCase().split("\\,\\s");
							boolean isComplex = false;
							String st = null;
							if (t.length > 2 && t[t.length - 2].length() == 2)
								st = t[t.length - 2];
							if (t.length > (st != null ? 3 : 2))
								isComplex = true;
							int id = 0;
							if (StringUtils.notEmpty(hParams.get("pl" + i)))
								id = new Integer(String.valueOf(hParams.get("pl" + i)));
							else
								id = DatabaseHelper.insertPlace(0, String.valueOf(hParams.get("pl" + i + "-l")), user, null, lang);
							if (isComplex) {
								if (i == 1)
									result.setComplex1((Complex)DatabaseHelper.loadEntity(Complex.class, id));
								else
									result.setComplex2((Complex)DatabaseHelper.loadEntity(Complex.class, id));
							}
							else {
								if (i == 1)
									result.setCity1((City)DatabaseHelper.loadEntity(City.class, id));
								else
									result.setCity2((City)DatabaseHelper.loadEntity(City.class, id));
							}
						}
					}
					result.setDate1(StringUtils.notEmpty(hParams.get("dt1-l")) ? String.valueOf(hParams.get("dt1-l")) : null);
					result.setDate2(StringUtils.notEmpty(hParams.get("dt2-l")) ? String.valueOf(hParams.get("dt2-l")) : null);
					result.setComment(StringUtils.notEmpty(hParams.get("cmt-l")) ? String.valueOf(hParams.get("cmt-l")) : null);
					result.setExa(StringUtils.notEmpty(hParams.get("exa-l")) ? String.valueOf(hParams.get("exa-l")) : null);
					// Rankings
					for (int i = 1 ; i <= 10 ; i++) {
						Integer id = (StringUtils.notEmpty(hParams.get("rk" + i)) ? new Integer(String.valueOf(hParams.get("rk" + i))) : 0);
						Object o = hParams.get("rk" + i + "-l");
						if (id == 0 && StringUtils.notEmpty(o)) {
							if (hInserted.keySet().contains(o))
								id = hInserted.get(o);
							else {
								id = DatabaseHelper.insertEntity(0, tp, result.getSport() != null ? result.getSport().getId() : 0, String.valueOf(o), null, user, null, lang);
								hInserted.put(o, id);
							}
						}
						Result.class.getMethod("setIdRank" + i, Integer.class).invoke(result, id > 0 ? id : null);
						if (i <= 5)
							Result.class.getMethod("setResult" + i, String.class).invoke(result, StringUtils.notEmpty(hParams.get("rs" + i + "-l")) ? hParams.get("rs" + i + "-l") : null);
					}
					result = (Result) DatabaseHelper.saveEntity(result, user);
					// External links
					if (StringUtils.notEmpty(hParams.get("exl-l")))
						DatabaseHelper.saveExternalLinks(Result.alias, result.getId(), String.valueOf(hParams.get("exl-l")));
					// Person List
					if (hParams.containsKey("rk1list")) {
						int i = 1;
						DatabaseHelper.executeUpdate("DELETE FROM \"~PERSON_LIST\" WHERE ID_RESULT=" + result.getId());
						while (hParams.containsKey("rk" + i + "list")) {
							String[] t = String.valueOf(hParams.get("rk" + i + "list")).split("\\|", 0);
							for (String value : t) {
								if (StringUtils.notEmpty(value) && !value.equals("null") && !value.startsWith("Name #")) {
									PersonList plist = new PersonList();
									plist.setIdResult(result.getId());
									plist.setRank(i);
									if (value.matches("\\d+"))
										plist.setIdPerson(Integer.parseInt(value));
									else
										plist.setIdPerson(DatabaseHelper.insertEntity(0, tp, result.getSport() != null ? result.getSport().getId() : 0, value, null, user, null, lang));
									DatabaseHelper.saveEntity(plist, user);
								}
							}
							i++;
						}
					}
					// Draws
					if (StringUtils.notEmpty(hParams.get("qf1w-l"))) {
						Integer idDR = (StringUtils.notEmpty(hParams.get("drid")) ? Integer.valueOf(String.valueOf(hParams.get("drid"))) : null);
						Draw draw = (idDR != null ? (Draw)DatabaseHelper.loadEntity(Draw.class, idDR) : new Draw());
						for (String s : new String[]{"qf1", "qf2", "qf3", "qf4", "sf1", "sf2", "thd"}) {
							Integer id1 = (StringUtils.notEmpty(hParams.get(s + "w")) ? new Integer(String.valueOf(hParams.get(s + "w"))) : 0);
							Integer id2 = (StringUtils.notEmpty(hParams.get(s + "l")) ? new Integer(String.valueOf(hParams.get(s + "l"))) : 0);
							Object ow = hParams.get(s + "w-l");
							Object ol = hParams.get(s + "l-l");
							if (id1 == 0 && StringUtils.notEmpty(ow)) {
								if (hInserted.keySet().contains(ow))
									id1 = hInserted.get(ow);
								else {
									id1 = DatabaseHelper.insertEntity(0, tp, result.getSport() != null ? result.getSport().getId() : 0, String.valueOf(ow), null, user, null, lang);
									hInserted.put(ow, id1);
								}
							}
							if (id2 == 0 && StringUtils.notEmpty(ol)) {
								if (hInserted.keySet().contains(ol))
									id2 = hInserted.get(ol);
								else {
									id2 = DatabaseHelper.insertEntity(0, tp, result.getSport() != null ? result.getSport().getId() : 0, String.valueOf(ol), null, user, null, lang);
									hInserted.put(ol, id2);
								}
							}
							Draw.class.getMethod("setId1" + String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1), Integer.class).invoke(draw, id1 > 0 ? id1 : null);
							Draw.class.getMethod("setId2" + String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1), Integer.class).invoke(draw, id2 > 0 ? id2 : null);
							Draw.class.getMethod("setResult_" + s, String.class).invoke(draw, StringUtils.notEmpty(hParams.get(s + "rs-l")) ? hParams.get(s + "rs-l") : null);
						}
						draw.setIdResult(result.getId());
						draw = (Draw) DatabaseHelper.saveEntity(draw, user);
					}
					sbMsg.append(ResourceUtils.getText("result." + (idRS != null ? "modified" : "created"), getLocale(request)));
				}
				catch (Exception e) {
					Logger.getLogger("sh").error(e.getMessage(), e);
					sbMsg.append("ERR:" + e.getMessage());
				}
				finally {
					ServletHelper.writeText(response, sbMsg.toString());
				}
			}
			else if (hParams.containsKey("p2") && hParams.get("p2").equals("data")) { // Data tips
				try {
					String lang_ = (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? lang.toUpperCase() : "");
					StringBuffer html = new StringBuffer("<table>");
					List<Object[]> list = DatabaseHelper.execute(hParams.get("p").equals("team") ? "select label, sport.label" + lang_ + " from Team order by 1" : "select label" + lang_ + ", code from Country order by 1");
					for (Object[] t : list)
						html.append("<tr><td>" + t[0] + "</td><td>" + t[1] + "</td></tr>");
					ServletHelper.writeText(response, html.append("</table>").toString());
				}
				catch (Exception e) {
					Logger.getLogger("sh").error(e.getMessage(), e);
				}
			}
			else { // Load result
				Object tp = hParams.get("tp");
				request.setAttribute("title", ResourceUtils.getText("menu.update", getLocale(request)) + " | Sporthenon");
				String p = String.valueOf(hParams.get("p"));
				p = StringUtils.decode(p);
				Object[] t = p.split("\\-");
				Result rs = null;
				Year yr = null;
				
				if (tp != null) {
					rs = (Result) DatabaseHelper.loadEntityFromQuery("from Result where year.id " + (tp.equals("next") ? ">" : "<") + " " + hParams.get("yr") + " and sport.id=" + hParams.get("sp") + " and championship.id=" + hParams.get("cp") + " and event.id=" + hParams.get("ev") + (StringUtils.notEmpty(hParams.get("se")) ? " and subevent.id=" + hParams.get("se") : "") + (StringUtils.notEmpty(hParams.get("se2")) ? " and subevent2.id=" + hParams.get("se2") : "") + " order by year.id " + (tp.equals("next") ? "asc" : "desc"));
					if (rs != null) {
						yr = rs.getYear();
						t = new Object[3 + (rs.getSubevent() != null ? 1 : 0) + (rs.getSubevent2() != null ? 1 : 0)];
						t[0] = rs.getSport().getId();
						t[1] = rs.getChampionship().getId();
						t[2] = rs.getEvent().getId();
						if (rs.getSubevent() != null)
							t[3] = rs.getSubevent().getId();
						if (rs.getSubevent2() != null)
							t[4] = rs.getSubevent2().getId();
					}
					else
						t = null;
				}

				if (t != null) {
					if (t[0].equals(Result.alias)) {
						rs = (Result)DatabaseHelper.loadEntity(Result.class, t[1]);
						String s = rs.getSport().getId() + "-" + rs.getChampionship().getId() + "-" + rs.getEvent().getId() + (rs.getSubevent() != null ? "-" + rs.getSubevent().getId() : "") + (rs.getSubevent2() != null ? "-" + rs.getSubevent2().getId() : "");
						yr = rs.getYear();
						t = s.split("\\-");
					}
					Sport sp = (Sport)DatabaseHelper.loadEntity(Sport.class, t[0]);
					Championship cp = (Championship)DatabaseHelper.loadEntity(Championship.class, t[1]); 
					Event ev = (Event)DatabaseHelper.loadEntity(Event.class, t[2]);
					Event se = (Event)DatabaseHelper.loadEntity(Event.class, t.length > 3 ? t[3] : 0);
					Event se2 = (Event)DatabaseHelper.loadEntity(Event.class, t.length > 4 ? t[4] : 0);
					if (yr == null)
						yr = (Year)DatabaseHelper.loadEntityFromQuery("from Year where label='" + Calendar.getInstance().get(Calendar.YEAR) + "'");

					StringBuffer sb = new StringBuffer();
					sb.append(sp.getId()).append("~").append(sp.getLabel(lang)).append("~");
					sb.append(cp.getId()).append("~").append(cp.getLabel(lang)).append("~");
					sb.append(ev.getId()).append("~").append(ev.getLabel(lang) + " [" + ev.getType().getLabel(lang) + "]").append("~").append(ev.getType().getNumber()).append("~");
					sb.append(se != null ? se.getId() : "").append("~").append(se != null ? se.getLabel(lang) + " [" + se.getType().getLabel(lang) + "]" : "").append("~").append(se != null ? se.getType().getNumber() : "").append("~");
					sb.append(se2 != null ? se2.getId() : "").append("~").append(se2 != null ? se2.getLabel(lang) + " [" + se2.getType().getLabel(lang) + "]" : "").append("~").append(se2 != null ? se2.getType().getNumber() : "").append("~");
					sb.append(yr.getId()).append("~").append(yr.getLabel()).append("~");
					if (rs != null) { // Existing result
						request.setAttribute("id", rs.getId());
						// Result Info
						sb.append(rs.getId()).append("~");
						sb.append(rs.getResult1()).append("~").append(rs.getResult2()).append("~").append(rs.getResult3()).append("~").append(rs.getResult4()).append("~").append(rs.getResult5()).append("~");
						sb.append(rs.getDate1()).append("~").append(rs.getDate2()).append("~");
						sb.append(rs.getComplex1() != null ? rs.getComplex1().getId() : (rs.getCity1() != null ? rs.getCity1().getId() : "")).append("~");
						sb.append(rs.getComplex1() != null ? rs.getComplex1().toString2() : (rs.getCity1() != null ? rs.getCity1().toString2() : "")).append("~");
						sb.append(rs.getComplex2() != null ? rs.getComplex2().getId() : (rs.getCity2() != null ? rs.getCity2().getId() : "")).append("~");
						sb.append(rs.getComplex2() != null ? rs.getComplex2().toString2() : (rs.getCity2() != null ? rs.getCity2().toString2() : "")).append("~");
						sb.append(rs.getExa()).append("~").append(rs.getComment()).append("~");
						// External links
						StringBuffer sbLinks = new StringBuffer();
						try {
							List<ExternalLink> list = DatabaseHelper.execute("from ExternalLink where entity='" + Result.alias + "' and idItem=" + rs.getId() + " order by id");
							for (ExternalLink link : list)
								sbLinks.append(link.getUrl()).append("|");
						}
						catch (Exception e_) {
							Logger.getLogger("sh").error(e_.getMessage(), e_);
						}
						sb.append(sbLinks.toString()).append("~");
						Integer n = ev.getType().getNumber();
						if (se != null)
							n = se.getType().getNumber();
						if (se2 != null)
							n = se2.getType().getNumber();
						// Rankings
						for (int i = 1 ; i <= 10 ; i++) {
							Method m = Result.class.getMethod("getIdRank" + i);
							Integer id = null;
							Object o = m.invoke(rs);
							if (o != null)
								id = (Integer) o;
							String label = null;
							if (id != null && id > 0)
								label = getEntityLabel(n, id, lang);
							sb.append(id != null ? id : "").append("~").append(label != null ? label : "").append("~");
						}
						// PersonList
						List lPList = DatabaseHelper.execute("from PersonList where idResult=" + rs.getId() + " order by id");
						if (lPList != null && lPList.size() > 0) {
							List<String> l = new ArrayList<String>();
							for (PersonList pl : (List<PersonList>) lPList) {
								int rk = pl.getRank();
								if (l.size() < rk)
									l.add("");
								Athlete a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, pl.getIdPerson());
								l.set(rk - 1, (StringUtils.notEmpty(l.get(rk - 1)) ? l.get(rk - 1) + "|" : "") + pl.getIdPerson() + ":" + a.toString2());
							}
							sb.append("rkl-" + StringUtils.implode(l, "#")).append("~");
						}
						// Draws
						List lDraw = DatabaseHelper.execute("from Draw where idResult=" + rs.getId());
						if (lDraw != null && lDraw.size() > 0) {
							Draw dr = (Draw) lDraw.get(0);
							sb.append(dr.getId()).append("~");
							for (String s : new String[]{"qf1", "qf2", "qf3", "qf4", "sf1", "sf2", "thd"}) {
								Integer id1 = null;
								Integer id2 = null;
								Method m = Draw.class.getMethod("getId1" + String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1));
								Object o = m.invoke(dr);
								if (o != null)
									id1 = (Integer) o;
								m = Draw.class.getMethod("getId2" + String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1));
								o = m.invoke(dr);
								if (o != null)
									id2 = (Integer) o;
								
								String label1 = null;
								String label2 = null;
								if (id1 != null && id1 > 0)
									label1 = getEntityLabel(n, id1, lang);
								if (id2 != null && id2 > 0)
									label2 = getEntityLabel(n, id2, lang);
								
								String result = null;
								m = Draw.class.getMethod("getResult_" + s);
								o = m.invoke(dr);
								if (o != null)
									result = String.valueOf(o);								
								
								sb.append(id1 != null ? id1 : "").append("~").append(label1 != null ? label1 : "").append("~");
								sb.append(id2 != null ? id2 : "").append("~").append(label2 != null ? label2 : "").append("~");
								sb.append(result != null ? result : "").append("~");
							}
						}
					}
					String result = sb.toString().replaceAll("~null~", "~~").replaceAll("~null~", "~~").replaceAll("\"", "\\\\\"");
					if (tp != null)
						ServletHelper.writeText(response, result);
					else {
						request.setAttribute("value", result);
						request.getRequestDispatcher("/jsp/update.jsp").forward(request, response);					
					}
				}
				else
					ServletHelper.writeText(response, "");
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
	private static String getEntityLabel(int n, Integer id, String lang) throws Exception {
		String label = null;
		if (n < 10) {
			Athlete a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, id);
			label = a.toString2();
		}
		else if (n == 50) {
			Team t_ = (Team) DatabaseHelper.loadEntity(Team.class, id);
			label = t_.getLabel() + (t_.getCountry() != null ? " [" + t_.getCountry().getCode() + "]" : "");
		}
		else {
			Country c = (Country) DatabaseHelper.loadEntity(Country.class, id);
			label = c.getLabel(lang);
		}
		return label;
	}

}