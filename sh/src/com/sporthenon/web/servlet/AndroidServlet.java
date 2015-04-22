package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
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
import com.sporthenon.db.function.ResultsBean;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
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
	        else if (p2.equalsIgnoreCase("SE")) {
	        	String[] t = p.split("\\-");
	        	String filter = "sport.id=" + t[0] + " and championship.id=" + t[1] + " and event.id=" + t[2];
	        	addItems(doc, root, ImageUtils.INDEX_EVENT, DatabaseHelper.getPicklist(Result.class, "subevent", filter, null, (short) 1, lang));
	        }
	        else if (p2.equalsIgnoreCase("SE2")) {
	        	String[] t = p.split("\\-");
	        	String filter = "sport.id=" + t[0] + " and championship.id=" + t[1] + " and event.id=" + t[2] + " and subevent.id=" + t[3];
	        	addItems(doc, root, ImageUtils.INDEX_EVENT, DatabaseHelper.getPicklist(Result.class, "subevent2", filter, null, (short) 1, lang));
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