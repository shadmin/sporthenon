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
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.HallOfFame;
import com.sporthenon.db.entity.OlympicRanking;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.RetiredNumber;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.entity.WinLoss;
import com.sporthenon.db.entity.Year;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.web.RenderOptions;
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
	
	private static void appendElement(Document d, Element e, String tag, Object value) {
		Element item = d.createElement(tag);
		item.setAttribute("value", value != null ? String.valueOf(value) : "");
		e.appendChild(item);
	}
	
	public static void writeData(HttpServletResponse res, Object data) throws Exception {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("data");
        doc.appendChild(root);
        if (data instanceof Championship) {
        	Championship e = (Championship) data;
        	appendElement(doc, root, "cp-id", e.getId());
        	appendElement(doc, root, "cp-label", e.getLabel());
        	appendElement(doc, root, "cp-website", e.getWebsite());
        }
        else if (data instanceof City) {
        	City e = (City) data;
        	appendElement(doc, root, "ct-id", e.getId());
        	appendElement(doc, root, "ct-label", e.getLabel());
        	appendElement(doc, root, "ct-state", e.getState() != null ? e.getState().getId() : 0);
        	appendElement(doc, root, "ct-country", e.getCountry() != null ? e.getCountry().getId() : 0);
        }
        else if (data instanceof Complex) {
        	Complex e = (Complex) data;
        	appendElement(doc, root, "cx-id", e.getId());
        	appendElement(doc, root, "cx-label", e.getLabel());
        	appendElement(doc, root, "cx-city", e.getCity() != null ? e.getCity().getId() : 0);
        }
        else if (data instanceof Country) {
        	Country e = (Country) data;
        	appendElement(doc, root, "cn-id", e.getId());
        	appendElement(doc, root, "cn-label", e.getLabel());
        	appendElement(doc, root, "cn-code", e.getCode());
        }
        else if (data instanceof Event) {
        	Event e = (Event) data;
        	appendElement(doc, root, "ev-id", e.getId());
        	appendElement(doc, root, "ev-label", e.getLabel());
        	appendElement(doc, root, "ev-type", e.getType().getId());
        	appendElement(doc, root, "ev-comment", e.getComment());
        }
        else if (data instanceof HallOfFame) {
        	HallOfFame e = (HallOfFame) data;
        	appendElement(doc, root, "hf-id", e.getId());
        	appendElement(doc, root, "hf-league", e.getLeague().getId());
        	appendElement(doc, root, "hf-year", e.getYear().getId());
        	appendElement(doc, root, "hf-person", e.getPerson().getId());
        	appendElement(doc, root, "hf-deceased", e.getDeceased());
        }
        else if (data instanceof OlympicRanking) {
        	OlympicRanking e = (OlympicRanking) data;
        	appendElement(doc, root, "or-id", e.getId());
        	appendElement(doc, root, "or-olympics", e.getOlympics().getId());
        	appendElement(doc, root, "or-country", e.getCountry().getId());
        	appendElement(doc, root, "or-countgold", e.getCountGold());
        	appendElement(doc, root, "or-countsilver", e.getCountSilver());
        	appendElement(doc, root, "or-countbronze", e.getCountBronze());
        }
        else if (data instanceof Olympics) {
        	Olympics e = (Olympics) data;
        	appendElement(doc, root, "ol-id", e.getId());
        	appendElement(doc, root, "ol-year", e.getYear().getId());
        	appendElement(doc, root, "ol-city", e.getCity().getId());
        	appendElement(doc, root, "ol-type", e.getType());
        	appendElement(doc, root, "ol-date1", e.getDate1());
        	appendElement(doc, root, "ol-date2", e.getDate2());
        	appendElement(doc, root, "ol-countcountry", e.getCountCountry());
        	appendElement(doc, root, "ol-countevent", e.getCountEvent());
        	appendElement(doc, root, "ol-countperson", e.getCountPerson());
        	appendElement(doc, root, "ol-countsport", e.getCountSport());
        }
        else if (data instanceof Athlete) {
        	Athlete e = (Athlete) data;
        	appendElement(doc, root, "pr-id", e.getId());
        	appendElement(doc, root, "pr-lastname", e.getLastName());
        	appendElement(doc, root, "pr-firstname", e.getFirstName());
        	appendElement(doc, root, "pr-sport", e.getSport().getId());
        	appendElement(doc, root, "pr-team", e.getTeam() != null ? e.getTeam().getId() : 0);
        	appendElement(doc, root, "pr-country", e.getCountry() != null ? e.getCountry().getId() : 0);
        }
        else if (data instanceof Record) {
        	Record e = (Record) data;
        	appendElement(doc, root, "rc-sport", e.getSport().getId());
        	appendElement(doc, root, "rc-number", e.getSubevent() != null ? e.getSubevent().getType().getNumber() : (e.getEvent() != null ? e.getEvent().getType().getNumber() : 0));
        	appendElement(doc, root, "rc-id", e.getId());
        	appendElement(doc, root, "rc-championship", e.getChampionship() != null ? e.getChampionship().getId() : 0);
        	appendElement(doc, root, "rc-event", e.getEvent() != null ? e.getEvent().getId() : 0);
        	appendElement(doc, root, "rc-subevent", e.getSubevent() != null ? e.getSubevent().getId() : 0);
        	appendElement(doc, root, "rc-city", e.getCity() != null ? e.getCity().getId() : 0);
        	appendElement(doc, root, "rc-type1", e.getType1());
        	appendElement(doc, root, "rc-type2", e.getType2());
        	appendElement(doc, root, "rc-label", e.getLabel());
        	appendElement(doc, root, "rc-rank1", e.getIdRank1());
        	appendElement(doc, root, "rc-rank2", e.getIdRank2());
        	appendElement(doc, root, "rc-rank3", e.getIdRank3());
        	appendElement(doc, root, "rc-rank4", e.getIdRank4());
        	appendElement(doc, root, "rc-rank5", e.getIdRank5());
        	appendElement(doc, root, "rc-record1", e.getRecord1());
        	appendElement(doc, root, "rc-record2", e.getRecord2());
        	appendElement(doc, root, "rc-record3", e.getRecord3());
        	appendElement(doc, root, "rc-record4", e.getRecord4());
        	appendElement(doc, root, "rc-record5", e.getRecord5());
        	appendElement(doc, root, "rc-date1", e.getDate1());
        	appendElement(doc, root, "rc-date2", e.getDate2());
        	appendElement(doc, root, "rc-date3", e.getDate3());
        	appendElement(doc, root, "rc-date4", e.getDate4());
        	appendElement(doc, root, "rc-date5", e.getDate5());
        	appendElement(doc, root, "rc-comment", e.getComment());
        	appendElement(doc, root, "rc-counting", e.getCounting());
        }
        else if (data instanceof Result) {
        	Result e = (Result) data;
        	appendElement(doc, root, "rs-sport", e.getSport().getId());
        	appendElement(doc, root, "rs-number", e.getSubevent() != null ? e.getSubevent().getType().getNumber() : (e.getEvent() != null ? e.getEvent().getType().getNumber() : 0));
        	appendElement(doc, root, "rs-id", e.getId());
        	appendElement(doc, root, "rs-championship", e.getChampionship() != null ? e.getChampionship().getId() : 0);
        	appendElement(doc, root, "rs-event", e.getEvent() != null ? e.getEvent().getId() : 0);
        	appendElement(doc, root, "rs-subevent", e.getSubevent() != null ? e.getSubevent().getId() : 0);
        	appendElement(doc, root, "rs-year", e.getYear() != null ? e.getYear().getId() : 0);
        	appendElement(doc, root, "rs-complex", e.getComplex() != null ? e.getComplex().getId() : 0);
        	appendElement(doc, root, "rs-city", e.getCity() != null ? e.getCity().getId() : 0);
        	appendElement(doc, root, "rs-rank1", e.getIdRank1());
        	appendElement(doc, root, "rs-rank2", e.getIdRank2());
        	appendElement(doc, root, "rs-rank3", e.getIdRank3());
        	appendElement(doc, root, "rs-rank4", e.getIdRank4());
        	appendElement(doc, root, "rs-rank5", e.getIdRank5());
        	appendElement(doc, root, "rs-rank6", e.getIdRank6());
        	appendElement(doc, root, "rs-rank7", e.getIdRank7());
        	appendElement(doc, root, "rs-rank8", e.getIdRank8());
        	appendElement(doc, root, "rs-rank9", e.getIdRank9());
        	appendElement(doc, root, "rs-rank10", e.getIdRank10());
        	appendElement(doc, root, "rs-result1", e.getResult1());
        	appendElement(doc, root, "rs-result2", e.getResult2());
        	appendElement(doc, root, "rs-result3", e.getResult3());
        	appendElement(doc, root, "rs-result4", e.getResult4());
        	appendElement(doc, root, "rs-result5", e.getResult5());
        	appendElement(doc, root, "rs-date1", e.getDate1());
        	appendElement(doc, root, "rs-date2", e.getDate2());
        	appendElement(doc, root, "rs-comment", e.getComment());
        }
        else if (data instanceof RetiredNumber) {
        	RetiredNumber e = (RetiredNumber) data;
        	appendElement(doc, root, "rn-id", e.getId());
        	appendElement(doc, root, "rn-league", e.getLeague().getId());
        	appendElement(doc, root, "rn-team", e.getTeam().getId());
        	appendElement(doc, root, "rn-person", e.getPerson().getId());
        	appendElement(doc, root, "rn-number", e.getNumber());
        }
        else if (data instanceof Sport) {
        	Sport e = (Sport) data;
        	appendElement(doc, root, "sp-id", e.getId());
        	appendElement(doc, root, "sp-label", e.getLabel());
        	appendElement(doc, root, "sp-type", e.getType());
        	appendElement(doc, root, "sp-website", e.getWebsite());
        }
        else if (data instanceof State) {
        	State e = (State) data;
        	appendElement(doc, root, "st-id", e.getId());
        	appendElement(doc, root, "st-label", e.getLabel());
        	appendElement(doc, root, "st-code", e.getCode());
        	appendElement(doc, root, "st-capital", e.getCapital());
        }
        else if (data instanceof Team) {
        	Team e = (Team) data;
        	appendElement(doc, root, "tm-id", e.getId());
        	appendElement(doc, root, "tm-label", e.getLabel());
        	appendElement(doc, root, "tm-code", e.getCode());
        	appendElement(doc, root, "tm-country", e.getCountry() != null ? e.getCountry().getId() : 0);
        	appendElement(doc, root, "tm-conference", e.getConference());
        	appendElement(doc, root, "tm-division", e.getDivision());
        	appendElement(doc, root, "tm-comment", e.getComment());
        	appendElement(doc, root, "tm-deleted", e.getDeleted());
        	appendElement(doc, root, "tm-parent", e.getParent() != null ? e.getParent().getId() : 0);
        	appendElement(doc, root, "tm-sport", e.getSport() != null ? e.getSport().getId() : 0);
        	appendElement(doc, root, "tm-year1", e.getYear1());
        	appendElement(doc, root, "tm-year2", e.getYear2());
        }
        else if (data instanceof TeamStadium) {
        	TeamStadium e = (TeamStadium) data;
        	appendElement(doc, root, "ts-id", e.getId());
        	appendElement(doc, root, "ts-league", e.getLeague().getId());
        	appendElement(doc, root, "ts-team", e.getTeam().getId());
        	appendElement(doc, root, "ts-complex", e.getComplex().getId());
        	appendElement(doc, root, "ts-date1", e.getDate1());
        	appendElement(doc, root, "ts-date2", e.getDate2());
        	appendElement(doc, root, "ts-renamed", e.getRenamed());
        	appendElement(doc, root, "ts-comment", e.getComment());
        }
        else if (data instanceof WinLoss) {
        	WinLoss e = (WinLoss) data;
        	appendElement(doc, root, "wl-id", e.getId());
        	appendElement(doc, root, "wl-league", e.getLeague().getId());
        	appendElement(doc, root, "wl-team", e.getTeam().getId());
        	appendElement(doc, root, "wl-type", e.getType());
        	appendElement(doc, root, "wl-countwin", e.getCountWin());
        	appendElement(doc, root, "wl-countloss", e.getCountLoss());
        	appendElement(doc, root, "wl-counttie", e.getCountTie());
        	appendElement(doc, root, "wl-countotloss", e.getCountOtloss());
        	appendElement(doc, root, "wl-average", e.getAverage());
        }
        else if (data instanceof Year) {
        	Year e = (Year) data;
        	appendElement(doc, root, "yr-id", e.getId());
        	appendElement(doc, root, "yr-label", e.getLabel());
        }
        res.setContentType("text/xml");
        res.setCharacterEncoding("utf-8");
        XMLSerializer serializer = new XMLSerializer();
        serializer.setOutputCharStream(res.getWriter());
        serializer.serialize(doc);
	}
	
	public static void writeHtml(HttpServletResponse res, StringBuffer sb) throws IOException {
		res.setContentType("text/html");
        res.setCharacterEncoding("utf-8");
        PrintWriter writer = res.getWriter();
        writer.write(sb.toString());
	}
	
	public static void writeLinkHtml(HttpServletRequest req, HttpServletResponse res, StringBuffer sb) throws ServletException, IOException {
		req.setAttribute("version", "v=" + ConfigUtils.getProperty("version"));
		req.setAttribute("html", sb.toString());
		req.getRequestDispatcher("/jsp/db/link.jsp").forward(req, res);
	}
	
	public static void writeText(HttpServletResponse res, String s) throws IOException {
		res.setContentType("text/plain");
        res.setCharacterEncoding("utf-8");
        PrintWriter writer = res.getWriter();
        writer.write(s);
	}

	public static RenderOptions buildOptions(HashMap<String, Object> h) {
		RenderOptions opts = new RenderOptions();
		opts.setHeaderDisabled(h.containsKey("opt-header") && String.valueOf(h.get("opt-header")).equals("1"));
		opts.setPicturesDisabled(h.containsKey("opt-pic") && String.valueOf(h.get("opt-pic")).equals("1"));
		opts.setStyle(String.valueOf(h.get("opt-style")));
		return opts;
	}

}
