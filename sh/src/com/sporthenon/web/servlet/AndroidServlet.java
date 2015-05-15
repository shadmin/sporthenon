package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.function.ResultsBean;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.HtmlConverter;
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
			String label = "label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? lang.toUpperCase() : "");
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
	        	addItems(doc, root, ImageUtils.INDEX_CHAMPIONSHIP, DatabaseHelper.getPicklist(Result.class, "championship", filter, null, "x.championship.index, x.championship." + label, lang));
	        }
	        else if (p2.equalsIgnoreCase(Event.alias)) {
	        	String[] t = p.split("\\-");
	        	String filter = "sport.id=" + t[0] + " and championship.id=" + t[1];
	        	addItems(doc, root, ImageUtils.INDEX_EVENT, DatabaseHelper.getPicklist(Result.class, "event", filter, null, "x.event.index, x.event." + label, lang));
	        }
	        else if (p2.equalsIgnoreCase("SE")) {
	        	String[] t = p.split("\\-");
	        	String filter = "sport.id=" + t[0] + " and championship.id=" + t[1] + " and event.id=" + t[2];
	        	addItems(doc, root, ImageUtils.INDEX_EVENT, DatabaseHelper.getPicklist(Result.class, "subevent", filter, null, "x.subevent.index, x.subevent." + label, lang));
	        }
	        else if (p2.equalsIgnoreCase("SE2")) {
	        	String[] t = p.split("\\-");
	        	String filter = "sport.id=" + t[0] + " and championship.id=" + t[1] + " and event.id=" + t[2] + " and subevent.id=" + t[3];
	        	addItems(doc, root, ImageUtils.INDEX_EVENT, DatabaseHelper.getPicklist(Result.class, "subevent2", filter, null, "x.subevent2.index, x.subevent2." + label, lang));
	        }
	        else if (p2.equalsIgnoreCase(Result.alias)) {
	        	String[] t = p.split("\\-");
	        	Integer sp = new Integer(t[0]);
	        	Integer cp = new Integer(t[1]);
	        	Integer ev = new Integer(t[2]);
	        	Integer se = new Integer(t.length > 3 ? t[3] : "0");
	        	Integer se2 = new Integer(t.length > 4 ? t[4] : "0");
	        	
	        	ArrayList<Object> lFuncParams = new ArrayList<Object>();
				lFuncParams.add(sp);
				lFuncParams.add(cp);
				lFuncParams.add(ev);
				lFuncParams.add(se);
				lFuncParams.add(se2);
				lFuncParams.add("0");
				lFuncParams.add("_" + lang);
				Event ev_ = (Event) DatabaseHelper.loadEntity(Event.class, (se2 > 0 ? se2 : (se > 0 ? se : ev)));
				addResultItems(doc, root, ev_, DatabaseHelper.call("GetResults", lFuncParams));
	        }
	        else if (p2.equalsIgnoreCase("R1")) {
	        	Result r = (Result) DatabaseHelper.loadEntity(Result.class, p);
	        	Element sp = doc.createElement("sport");
	        	sp.setAttribute("img", getImage(ImageUtils.INDEX_SPORT, r.getSport().getId(), ImageUtils.SIZE_SMALL, null, null));
	        	sp.setTextContent(r.getSport().getLabel(lang));
	        	root.appendChild(sp);
	        	Element cp = doc.createElement("championship");
	        	cp.setAttribute("img", getImage(ImageUtils.INDEX_CHAMPIONSHIP, r.getChampionship().getId(), ImageUtils.SIZE_SMALL, null, null));
	        	cp.setTextContent(r.getChampionship().getLabel(lang));
	        	root.appendChild(cp);
	        	Element ev = doc.createElement("event");
	        	ev.setAttribute("img", getImage(ImageUtils.INDEX_EVENT, r.getEvent().getId(), ImageUtils.SIZE_SMALL, null, null));
	        	ev.setTextContent(r.getEvent().getLabel(lang));
	        	root.appendChild(ev);
	        	if (r.getSubevent() != null) {
	        		Element se = doc.createElement("subevent");
		        	se.setAttribute("img", getImage(ImageUtils.INDEX_EVENT, r.getSubevent().getId(), ImageUtils.SIZE_SMALL, null, null));
		        	se.setTextContent(r.getSubevent().getLabel(lang));
		        	root.appendChild(se);	
	        	}
	        	if (r.getSubevent2() != null) {
	        		Element se2 = doc.createElement("subevent");
		        	se2.setAttribute("img", getImage(ImageUtils.INDEX_EVENT, r.getSubevent2().getId(), ImageUtils.SIZE_SMALL, null, null));
		        	se2.setTextContent(r.getSubevent2().getLabel(lang));
		        	root.appendChild(se2);	
	        	}
	        	if (StringUtils.notEmpty(r.getDate2())) {
	        		Element dates = doc.createElement("dates");
	        		if (StringUtils.notEmpty(r.getDate1()))
	        			dates.setAttribute("date1", StringUtils.toTextDate(r.getDate1(), lang, "d MMMM yyyy"));
	        		dates.setAttribute("date2", StringUtils.toTextDate(r.getDate2(), lang, "d MMMM yyyy"));
		        	root.appendChild(dates);
	        	}
				if (StringUtils.notEmpty(r.getComplex2()) || StringUtils.notEmpty(r.getCity2())) {
					String pl1 = null;
					String pl2 = null;
					if (r.getComplex1() != null) {
						Complex cx = r.getComplex1();
						pl1 = HtmlConverter.getPlace(cx.getId(), cx.getCity().getId(), cx.getCity().getState() != null ? cx.getCity().getState().getId() : null, cx.getCity().getCountry().getId(), cx.getLabel(lang), cx.getCity().getLabel(lang), cx.getCity().getState() != null ? cx.getCity().getState().getLabel(lang) : null, cx.getCity().getCountry().getLabel(lang), cx.getLabel(), cx.getCity().getLabel(), cx.getCity().getState() != null ? cx.getCity().getState().getLabel() : null, cx.getCity().getCountry().getLabel(), r.getYear().getLabel());
					}
					else if (r.getCity1() != null) {
						City ct = r.getCity1();
						pl1 = HtmlConverter.getPlace(null, ct.getId(), ct.getState() != null ? ct.getState().getId() : null, ct.getCountry().getId(), null, ct.getLabel(lang), ct.getState() != null ? ct.getState().getLabel(lang) : null, ct.getCountry().getLabel(lang), null, ct.getLabel(), ct.getState() != null ? ct.getState().getLabel() : null, ct.getCountry().getLabel(), r.getYear().getLabel());
					}
					if (r.getComplex2() != null) {
						Complex cx = r.getComplex2();
						pl2 = HtmlConverter.getPlace(cx.getId(), cx.getCity().getId(), cx.getCity().getState() != null ? cx.getCity().getState().getId() : null, cx.getCity().getCountry().getId(), cx.getLabel(lang), cx.getCity().getLabel(lang), cx.getCity().getState() != null ? cx.getCity().getState().getLabel(lang) : null, cx.getCity().getCountry().getLabel(lang), cx.getLabel(), cx.getCity().getLabel(), cx.getCity().getState() != null ? cx.getCity().getState().getLabel() : null, cx.getCity().getCountry().getLabel(), r.getYear().getLabel());
					}
					else if (r.getCity2() != null) {
						City ct = r.getCity2();
						pl2 = HtmlConverter.getPlace(null, ct.getId(), ct.getState() != null ? ct.getState().getId() : null, ct.getCountry().getId(), null, ct.getLabel(lang), ct.getState() != null ? ct.getState().getLabel(lang) : null, ct.getCountry().getLabel(lang), null, ct.getLabel(), ct.getState() != null ? ct.getState().getLabel() : null, ct.getCountry().getLabel(), r.getYear().getLabel());
					}
					Element places = doc.createElement("places");
	        		if (StringUtils.notEmpty(pl1))
	        			places.setAttribute("place1", pl1);
	        		places.setAttribute("place2", pl2);
		        	root.appendChild(places);
				}
			
				// Result
				ArrayList<Object> lFuncParams = new ArrayList<Object>();
				lFuncParams.add(r.getSport().getId());
				lFuncParams.add(r.getChampionship().getId());
				lFuncParams.add(r.getEvent().getId());
				lFuncParams.add(r.getSubevent() != null ? r.getSubevent().getId() : 0);
				lFuncParams.add(r.getSubevent2() != null ? r.getSubevent2().getId() : 0);
				lFuncParams.add(String.valueOf(r.getYear().getId()));
				lFuncParams.add("_" + lang);
				List<ResultsBean> list = (List<ResultsBean>) DatabaseHelper.call("GetResults", lFuncParams);
				if (list != null && !list.isEmpty()) {
					ResultsBean bean = list.get(0);
					String rk1 = null;
					String rk2 = null;
					String rk3 = null;
					String rs1 = null;
					String rs2 = null;
					String rs3 = null;
					Event ev_ = (Event) DatabaseHelper.loadEntity(Event.class, (r.getSubevent2() != null ? r.getSubevent2().getId() : (r.getSubevent() != null ? r.getSubevent().getId() : r.getEvent().getId())));
					int type_ = ev_.getType().getNumber();
					boolean isScore = (bean.getRsRank1() != null && bean.getRsRank2() != null && StringUtils.notEmpty(bean.getRsResult1()) && !StringUtils.notEmpty(bean.getRsResult2()) && !StringUtils.notEmpty(bean.getRsResult3()));
					if (bean.getRsRank1() != null) {
						rk1 = HtmlConverter.getResultsEntity(type_, bean.getRsRank1(), bean.getEn1Str1(), bean.getEn1Str2(), bean.getEn1Str3(), bean.getYrLabel());
						rs1 = bean.getRsResult1();
						String img = null;
						String txt = null;
						if (bean.getEn1Rel2Id() != null) {
							img = getImage(ImageUtils.INDEX_COUNTRY, bean.getEn1Rel2Id(), ImageUtils.SIZE_SMALL, r.getYear().getLabel(), null);
							txt = bean.getEn1Rel2Label();
						}
						else if (bean.getEn1Rel1Id() != null) {
							img = getImage(ImageUtils.INDEX_TEAM, bean.getEn1Rel1Id(), ImageUtils.SIZE_SMALL, r.getYear().getLabel(), null);
							txt = bean.getEn1Rel1Label();
						}
						if (StringUtils.notEmpty(txt))
							rk1 = "<table><tr>" + (StringUtils.notEmpty(img) ? "<th>" + img + "</th>" : "") + "<td><b>" + rk1 + "</b></td><td>&nbsp;(" + txt + ")</td>" + (StringUtils.notEmpty(rs1) && !isScore ? "<td>&nbsp;&ndash;&nbsp;" + rs1 + "</td>" : "") + "</tr></table>";
					}
					if (bean.getRsRank2() != null) {
						rk2 = HtmlConverter.getResultsEntity(type_, bean.getRsRank2(), bean.getEn2Str1(), bean.getEn2Str2(), bean.getEn2Str3(), bean.getYrLabel());
						rs2 = bean.getRsResult2();
						String img = null;
						String txt = null;
						if (bean.getEn2Rel2Id() != null) {
							img = getImage(ImageUtils.INDEX_COUNTRY, bean.getEn2Rel2Id(), ImageUtils.SIZE_SMALL, r.getYear().getLabel(), null);
							txt = bean.getEn2Rel2Label();
						}
						else if (bean.getEn2Rel1Id() != null) {
							img = getImage(ImageUtils.INDEX_TEAM, bean.getEn2Rel1Id(), ImageUtils.SIZE_SMALL, r.getYear().getLabel(), null);
							txt = bean.getEn2Rel1Label();
						}
						if (StringUtils.notEmpty(txt))
							rk2 = "<table><tr>" + (StringUtils.notEmpty(img) ? "<th>" + img + "</th>" : "") + "<td>" + rk2 + "</td><td>&nbsp;(" + txt + ")</td>" + (StringUtils.notEmpty(rs2) ? "<td>&nbsp;&ndash;&nbsp;" + rs2 + "</td>" : "") + "</tr></table>";
					}
					if (bean.getRsRank3() != null) {
						rk3 = HtmlConverter.getResultsEntity(type_, bean.getRsRank3(), bean.getEn3Str1(), bean.getEn3Str2(), bean.getEn3Str3(), bean.getYrLabel());
						rs3 = bean.getRsResult3();
						String img = null;
						String txt = null;
						if (bean.getEn3Rel2Id() != null) {
							img = getImage(ImageUtils.INDEX_COUNTRY, bean.getEn3Rel2Id(), ImageUtils.SIZE_SMALL, r.getYear().getLabel(), null);
							txt = bean.getEn3Rel2Label();
						}
						else if (bean.getEn3Rel1Id() != null) {
							img = getImage(ImageUtils.INDEX_TEAM, bean.getEn3Rel1Id(), ImageUtils.SIZE_SMALL, r.getYear().getLabel(), null);
							txt = bean.getEn3Rel1Label();
						}
						if (StringUtils.notEmpty(txt))
							rk3 = "<table><tr>" + (StringUtils.notEmpty(img) ? "<th>" + img + "</th>" : "") + "<td>" + rk3 + "</td><td>&nbsp;(" + txt + ")</td>" + (StringUtils.notEmpty(rs3) ? "<td>&nbsp;&ndash;&nbsp;" + rs3 + "</td>" : "") + "</tr></table>";
					}
					Element rank1 = doc.createElement("rank1");
					rank1.appendChild(doc.createCDATASection(rk1));
		        	root.appendChild(rank1);
		        	Element rank2 = doc.createElement("rank2");
					rank2.appendChild(doc.createCDATASection(rk2));
		        	root.appendChild(rank2);
		        	Element rank3 = doc.createElement("rank3");
					rank3.appendChild(doc.createCDATASection(rk3));
		        	root.appendChild(rank3);
				}
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
	
	public static String getImage(short type, int id, char size, String year, String title) {
		String html = HtmlUtils.writeImage(type, id, size, year, title);
		return "http:" + html.replaceAll(".*\\ssrc\\=\\'|\\'\\/\\>", "");
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
	
	private void addResultItems(Document doc, Element root, Event ev, Collection<ResultsBean> list) {
		if (list != null && list.size() > 0) {
			Integer tp = ev.getType().getNumber();
			for (ResultsBean bean : list) {
				Element item = doc.createElement("item");
				item.setAttribute("id", String.valueOf(bean.getRsId()));
				item.setAttribute("year", bean.getYrLabel());
				item.setAttribute("type", String.valueOf(tp));
				item.setAttribute("rk1", String.valueOf(bean.getRsRank1()));
				item.setAttribute("str1", bean.getEn1Str1());
				item.setAttribute("str2", bean.getEn1Str2());
				item.setAttribute("str3", bean.getEn1Str3());
				item.setAttribute("rs1", bean.getRsResult1());
				item.setAttribute("score", bean.getRsRank1() != null && bean.getRsRank2() != null && StringUtils.notEmpty(bean.getRsResult1()) && !StringUtils.notEmpty(bean.getRsResult2()) && !StringUtils.notEmpty(bean.getRsResult3()) && !StringUtils.notEmpty(bean.getRsResult4()) && !StringUtils.notEmpty(bean.getRsResult5()) ? "1" : "0");
				if (bean.getRsRank1() != null) {
					String img = null;
					if (tp < 10) {
						Integer tm = bean.getEn1Rel1Id();
						Integer cn = bean.getEn1Rel2Id();
						if (tm != null && tm > 0)
							img = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, tm, ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
						else {
							img = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, cn, ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
							item.setAttribute("code", bean.getEn1Rel2Code());
						}
					}
					else if (tp == 50)
						img = HtmlUtils.writeImage(ImageUtils.INDEX_TEAM, bean.getRsRank1(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
					else if (tp == 99)
						img = HtmlUtils.writeImage(ImageUtils.INDEX_COUNTRY, bean.getRsRank1(), ImageUtils.SIZE_SMALL, bean.getYrLabel(), null);
					item.setAttribute("img", img);
				}
				root.appendChild(item);
			}
		}
	}

}