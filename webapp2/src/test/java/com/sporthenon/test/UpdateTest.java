package com.sporthenon.test;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Calendar;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.HallOfFame;
import com.sporthenon.db.entity.League;
import com.sporthenon.db.entity.OlympicRanking;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.RetiredNumber;
import com.sporthenon.db.entity.Round;
import com.sporthenon.db.entity.RoundType;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.entity.Type;
import com.sporthenon.db.entity.WinLoss;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Contributor;

import junit.framework.TestCase;

public class UpdateTest extends TestCase {

	private static final Logger log = Logger.getLogger(UpdateTest.class.getName());
	
	/*
Result.java
	 */
	
	public void testAthlete() {
		// Create
		Athlete at = new Athlete();
		at.setLastName("last Name");
		at.setFirstName("first Name");
		at.setCountry(new Country(1));
		at.setTeam(new Team(1));
		at.setSport(new Sport(1));
		at.setLink(1);
		at.setRef(0);
		boolean err = false;
		try {
			at = (Athlete) DatabaseManager.saveEntity(at, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(at.getId());
		assertFalse(err);

		// Update
		at.setLastName("last Name2");
		at.setFirstName("first Name2");
		at.setCountry(new Country(2));
		at.setTeam(new Team(2));
		at.setSport(new Sport(2));
		at.setLink(2);
		at.setRef(1);
		err = false;
		try {
			at = (Athlete) DatabaseManager.saveEntity(at, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(at.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(at);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testCalendar() {
		// Create
		Calendar cl = new Calendar();
		cl.setSport(new Sport(1));
		cl.setChampionship(new Championship(1));
		cl.setEvent(new Event(1));
		cl.setSubevent(new Event(1));
		cl.setSubevent2(new Event(1));
		cl.setComplex(new Complex(4));
		cl.setCity(new City(1));
		cl.setCountry(new Country(1));
		cl.setDate1("01/01/2000");
		cl.setDate2("02/01/2000");
		boolean err = false;
		try {
			cl = (Calendar) DatabaseManager.saveEntity(cl, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(cl.getId());
		assertFalse(err);

		// Update
		cl.setSport(new Sport(2));
		cl.setChampionship(new Championship(2));
		cl.setEvent(new Event(3));
		cl.setSubevent(new Event(3));
		cl.setSubevent2(new Event(3));
		cl.setComplex(new Complex(5));
		cl.setCity(new City(2));
		cl.setCountry(new Country(2));
		cl.setDate1("03/01/2000");
		cl.setDate2("04/01/2000");
		err = false;
		try {
			cl = (Calendar) DatabaseManager.saveEntity(cl, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(cl.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(cl);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testChampionship() {
		// Create
		Championship cp = new Championship();
		cp.setLabel("label");
		cp.setLabelFr("label FR");
		cp.setIndex(1.0d);
		cp.setRef(0);
		cp.setNopic(false);
		boolean err = false;
		try {
			cp = (Championship) DatabaseManager.saveEntity(cp, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(cp.getId());
		assertFalse(err);

		// Update
		cp.setLabel("label2");
		cp.setLabelFr("label FR2");
		cp.setIndex(2.0d);
		cp.setRef(1);
		cp.setNopic(true);
		err = false;
		try {
			cp = (Championship) DatabaseManager.saveEntity(cp, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(cp.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(cp);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testCity() {
		// Create
		City ct = new City();
		ct.setLabel("label");
		ct.setLabelFr("label FR");
		ct.setCountry(new Country(1));
		ct.setState(new State(1));
		ct.setLink(0);
		ct.setRef(0);
		boolean err = false;
		try {
			ct = (City) DatabaseManager.saveEntity(ct, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(ct.getId());
		assertFalse(err);

		// Update
		ct.setLabel("label2");
		ct.setLabelFr("label FR2");
		ct.setCountry(new Country(2));
		ct.setState(new State(2));
		ct.setLink(1);
		ct.setRef(1);
		err = false;
		try {
			ct = (City) DatabaseManager.saveEntity(ct, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(ct.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(ct);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testComplex() {
		// Create
		Complex cx = new Complex();
		cx.setLabel("label");
		cx.setCity(new City(1));
		cx.setLink(0);
		cx.setRef(0);
		boolean err = false;
		try {
			cx = (Complex) DatabaseManager.saveEntity(cx, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(cx.getId());
		assertFalse(err);

		// Update
		cx.setLabel("label2");
		cx.setCity(new City(2));
		cx.setLink(1);
		cx.setRef(1);
		err = false;
		try {
			cx = (Complex) DatabaseManager.saveEntity(cx, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(cx.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(cx);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testCountry() {
		// Create
		Country cn = new Country();
		cn.setCode("c");
		cn.setLabel("label");
		cn.setLabelFr("label FR");
		cn.setRef(0);
		cn.setNopic(false);
		boolean err = false;
		try {
			cn = (Country) DatabaseManager.saveEntity(cn, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(cn.getId());
		assertFalse(err);

		// Update
		cn.setCode("c2");
		cn.setLabel("label2");
		cn.setLabelFr("label FR2");
		cn.setRef(1);
		cn.setNopic(true);
		err = false;
		try {
			cn = (Country) DatabaseManager.saveEntity(cn, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(cn.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(cn);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testEvent() {
		// Create
		Event ev = new Event();
		ev.setLabel("label");
		ev.setLabelFr("label FR");
		ev.setIndex(1.0d);
		ev.setType(new Type(3));
		ev.setRef(0);
		ev.setNopic(false);
		boolean err = false;
		try {
			ev = (Event) DatabaseManager.saveEntity(ev, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(ev.getId());
		assertFalse(err);

		// Update
		ev.setLabel("label2");
		ev.setLabelFr("label FR2");
		ev.setIndex(2.0d);
		ev.setType(new Type(6));
		ev.setRef(1);
		ev.setNopic(true);
		err = false;
		try {
			ev = (Event) DatabaseManager.saveEntity(ev, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(ev.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(ev);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testHallOfFame() {
		// Create
		HallOfFame hf = new HallOfFame();
		hf.setLeague(new League(1));
		hf.setYear(new Year(1));
		hf.setPerson(new Athlete(1));
		hf.setPosition("1");
		boolean err = false;
		try {
			hf = (HallOfFame) DatabaseManager.saveEntity(hf, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(hf.getId());
		assertFalse(err);
		
		// Update
		hf.setLeague(new League(2));
		hf.setYear(new Year(2));
		hf.setPerson(new Athlete(2));
		hf.setPosition("2");
		err = false;
		try {
			hf = (HallOfFame) DatabaseManager.saveEntity(hf, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(hf.getId());
		assertFalse(err);
		
		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(hf);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testLeague() {
		// Create
		League lg = new League();
		lg.setLabel("lab");
		boolean err = false;
		try {
			lg = (League) DatabaseManager.saveEntity(lg, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(lg.getId());
		assertFalse(err);

		// Update
		lg.setLabel("lab2");
		err = false;
		try {
			lg = (League) DatabaseManager.saveEntity(lg, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(lg.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(lg);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testOlympicRanking() {
		// Create
		OlympicRanking or = new OlympicRanking();
		or.setOlympics(new Olympics(1));
		or.setCountry(new Country(1));
		or.setCountGold(1);
		or.setCountSilver(1);
		or.setCountBronze(1);
		boolean err = false;
		try {
			or = (OlympicRanking) DatabaseManager.saveEntity(or, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(or.getId());
		assertFalse(err);

		// Update
		or.setOlympics(new Olympics(2));
		or.setCountry(new Country(2));
		or.setCountGold(2);
		or.setCountSilver(2);
		or.setCountBronze(2);
		err = false;
		try {
			or = (OlympicRanking) DatabaseManager.saveEntity(or, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(or.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(or);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testOlympics() {
		// Create
		Olympics ol = new Olympics();
		ol.setYear(new Year(1));
		ol.setCity(new City(1));
		ol.setCountCountry(1);
		ol.setCountPerson(1);
		ol.setCountSport(1);
		ol.setCountEvent(1);
		ol.setDate1("date1");
		ol.setDate2("date2");
		ol.setType(0);
		ol.setRef(0);
		ol.setNopic(false);
		boolean err = false;
		try {
			ol = (Olympics) DatabaseManager.saveEntity(ol, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(ol.getId());
		assertFalse(err);

		// Update
		ol.setYear(new Year(2));
		ol.setCity(new City(2));
		ol.setCountCountry(2);
		ol.setCountPerson(2);
		ol.setCountSport(2);
		ol.setCountEvent(2);
		ol.setDate1("date3");
		ol.setDate2("date1");
		ol.setType(1);
		ol.setRef(1);
		ol.setNopic(true);
		err = false;
		try {
			ol = (Olympics) DatabaseManager.saveEntity(ol, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(ol.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(ol);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testRecord() {
		// Create
		Record rc = new Record();
		rc.setSport(new Sport(1));
		rc.setChampionship(new Championship(1));
		rc.setEvent(new Event(1));
		rc.setSubevent(new Event(1));
		rc.setCity(new City(1));
		rc.setLabel("label");
		rc.setIdRank1(1);
		rc.setIdRank2(1);
		rc.setIdRank3(1);
		rc.setIdRank4(1);
		rc.setIdRank5(1);
		rc.setRecord1("1");
		rc.setRecord2("1");
		rc.setRecord3("1");
		rc.setRecord4("1");
		rc.setRecord5("1");
		rc.setDate1("1");
		rc.setDate2("1");
		rc.setDate3("1");
		rc.setDate4("1");
		rc.setDate5("1");
		rc.setCounting(false);
		rc.setIndex(BigDecimal.ZERO);
		rc.setType1("1");
		rc.setType2("1");
		rc.setComment("comment");
		rc.setExa("exa");
		boolean err = false;
		try {
			rc = (Record) DatabaseManager.saveEntity(rc, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(rc.getId());
		assertFalse(err);

		// Update
		rc.setSport(new Sport(2));
		rc.setChampionship(new Championship(2));
		rc.setEvent(new Event(3));
		rc.setSubevent(new Event(3));
		rc.setCity(new City(2));
		rc.setLabel("label2");
		rc.setIdRank1(2);
		rc.setIdRank2(2);
		rc.setIdRank3(2);
		rc.setIdRank4(2);
		rc.setIdRank5(2);
		rc.setRecord1("2");
		rc.setRecord2("2");
		rc.setRecord3("2");
		rc.setRecord4("2");
		rc.setRecord5("2");
		rc.setDate1("2");
		rc.setDate2("2");
		rc.setDate3("2");
		rc.setDate4("2");
		rc.setDate5("2");
		rc.setCounting(false);
		rc.setIndex(BigDecimal.ZERO);
		rc.setType1("2");
		rc.setType2("2");
		rc.setComment("comment2");
		rc.setExa("exa2");
		err = false;
		try {
			rc = (Record) DatabaseManager.saveEntity(rc, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(rc.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(rc);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testResult() {
		// Create
		Result rs = new Result();
		rs.setSport(new Sport(1));
		rs.setChampionship(new Championship(1));
		rs.setEvent(new Event(1));
		rs.setSubevent(new Event(1));
		rs.setSubevent2(new Event(1));
		rs.setCity1(new City(1));
		rs.setCity2(new City(1));
		rs.setComplex1(new Complex(4));
		rs.setComplex2(new Complex(4));
		rs.setCountry1(new Country(1));
		rs.setCountry2(new Country(1));
		rs.setYear(new Year(1));
		rs.setIdRank1(1);
		rs.setIdRank2(1);
		rs.setIdRank3(1);
		rs.setIdRank4(1);
		rs.setIdRank5(1);
		rs.setIdRank6(1);
		rs.setIdRank7(1);
		rs.setIdRank8(1);
		rs.setIdRank9(1);
		rs.setIdRank10(1);
		rs.setIdRank11(1);
		rs.setIdRank12(1);
		rs.setIdRank13(1);
		rs.setIdRank14(1);
		rs.setIdRank15(1);
		rs.setIdRank16(1);
		rs.setIdRank17(1);
		rs.setIdRank18(1);
		rs.setIdRank19(1);
		rs.setIdRank20(1);
		rs.setResult1("1");
		rs.setResult2("1");
		rs.setResult3("1");
		rs.setResult4("1");
		rs.setResult5("1");
		rs.setResult6("1");
		rs.setResult7("1");
		rs.setResult8("1");
		rs.setResult9("1");
		rs.setResult10("1");
		rs.setResult11("1");
		rs.setResult12("1");
		rs.setResult13("1");
		rs.setResult14("1");
		rs.setResult15("1");
		rs.setResult16("1");
		rs.setResult17("1");
		rs.setResult18("1");
		rs.setResult19("1");
		rs.setResult20("1");
		rs.setDate1("01/01/2000");
		rs.setDate2("02/01/2000");
		rs.setComment("comment");
		rs.setExa("exa");
		rs.setDraft(false);
		rs.setNoDate(false);
		rs.setNoPlace(false);
		boolean err = false;
		try {
			rs = (Result) DatabaseManager.saveEntity(rs, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(rs.getId());
		assertFalse(err);

		// Update
		rs.setSport(new Sport(2));
		rs.setChampionship(new Championship(2));
		rs.setEvent(new Event(3));
		rs.setSubevent(new Event(3));
		rs.setSubevent2(new Event(3));
		rs.setCity1(new City(2));
		rs.setCity2(new City(2));
		rs.setComplex1(new Complex(5));
		rs.setComplex2(new Complex(5));
		rs.setCountry1(new Country(2));
		rs.setCountry2(new Country(2));
		rs.setYear(new Year(2));
		rs.setIdRank1(2);
		rs.setIdRank2(2);
		rs.setIdRank3(2);
		rs.setIdRank4(2);
		rs.setIdRank5(2);
		rs.setIdRank6(2);
		rs.setIdRank7(2);
		rs.setIdRank8(2);
		rs.setIdRank9(2);
		rs.setIdRank10(2);
		rs.setIdRank11(2);
		rs.setIdRank12(2);
		rs.setIdRank13(2);
		rs.setIdRank14(2);
		rs.setIdRank15(2);
		rs.setIdRank16(2);
		rs.setIdRank17(2);
		rs.setIdRank18(2);
		rs.setIdRank19(2);
		rs.setIdRank20(2);
		rs.setResult1("2");
		rs.setResult2("2");
		rs.setResult3("2");
		rs.setResult4("2");
		rs.setResult5("2");
		rs.setResult6("2");
		rs.setResult7("2");
		rs.setResult8("2");
		rs.setResult9("2");
		rs.setResult10("2");
		rs.setResult11("2");
		rs.setResult12("2");
		rs.setResult13("2");
		rs.setResult14("2");
		rs.setResult15("2");
		rs.setResult16("2");
		rs.setResult17("2");
		rs.setResult18("2");
		rs.setResult19("2");
		rs.setResult20("2");
		rs.setDate1("03/01/2000");
		rs.setDate2("04/01/2000");
		rs.setComment("comment2");
		rs.setExa("exa2");
		rs.setDraft(true);
		rs.setNoDate(true);
		rs.setNoPlace(true);
		err = false;
		try {
			rs = (Result) DatabaseManager.saveEntity(rs, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(rs.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(rs);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testRetiredNumber() {
		// Create
		RetiredNumber rn = new RetiredNumber();
		rn.setLeague(new League(1));
		rn.setTeam(new Team(1));
		rn.setPerson(new Athlete(1));
		rn.setYear(new Year(1));
		rn.setNumber(1);
		boolean err = false;
		try {
			rn = (RetiredNumber) DatabaseManager.saveEntity(rn, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(rn.getId());
		assertFalse(err);
		
		// Update
		rn.setLeague(new League(2));
		rn.setTeam(new Team(2));
		rn.setPerson(new Athlete(2));
		rn.setYear(new Year(2));
		rn.setNumber(2);
		err = false;
		try {
			rn = (RetiredNumber) DatabaseManager.saveEntity(rn, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(rn.getId());
		assertFalse(err);
		
		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(rn);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testRound() {
		// Create
		Round rd = new Round();
		rd.setIdResult(1);
		rd.setIdResultType(1);
		rd.setRoundType(new RoundType(1));
		rd.setIdRank1(1);
		rd.setIdRank2(1);
		rd.setIdRank3(1);
		rd.setIdRank1(1);
		rd.setIdRank5(1);
		rd.setResult1("1");
		rd.setResult2("1");
		rd.setResult3("1");
		rd.setResult4("1");
		rd.setResult5("1");
		rd.setCity1(new City(1));
		rd.setComplex1(new Complex(4));
		rd.setCity2(new City(1));
		rd.setComplex2(new Complex(4));
		rd.setDate("2000");
		rd.setDate1("2001");
		rd.setExa("exa");
		rd.setComment("comment");
		boolean err = false;
		try {
			rd = (Round) DatabaseManager.saveEntity(rd, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(rd.getId());
		assertFalse(err);
		
		// Update
		rd.setIdResult(2);
		rd.setIdResultType(2);
		rd.setRoundType(new RoundType(2));
		rd.setIdRank1(2);
		rd.setIdRank2(2);
		rd.setIdRank3(2);
		rd.setIdRank2(2);
		rd.setIdRank5(2);
		rd.setResult1("2");
		rd.setResult2("2");
		rd.setResult3("2");
		rd.setResult4("2");
		rd.setResult5("2");
		rd.setCity1(new City(2));
		rd.setComplex1(new Complex(5));
		rd.setCity2(new City(2));
		rd.setComplex2(new Complex(5));
		rd.setDate("2002");
		rd.setDate1("2003");
		rd.setExa("exa2");
		rd.setComment("comment2");
		err = false;
		try {
			rd = (Round) DatabaseManager.saveEntity(rd, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(rd.getId());
		assertFalse(err);
		
		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(rd);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testRoundType() {
		// Create
		RoundType rt = new RoundType();
		rt.setLabel("label");
		rt.setLabelFr("label FR");
		rt.setIndex(1.0d);
		boolean err = false;
		try {
			rt = (RoundType) DatabaseManager.saveEntity(rt, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(rt.getId());
		assertFalse(err);

		// Update
		rt.setLabel("label2");
		rt.setLabelFr("label FR2");
		rt.setIndex(2.0d);
		err = false;
		try {
			rt = (RoundType) DatabaseManager.saveEntity(rt, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(rt.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(rt);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testState() {
		// Create
		State st = new State();
		st.setCode("c");
		st.setLabel("label");
		st.setLabelFr("label FR");
		st.setCapital("capital");
		st.setRef(0);
		st.setNopic(false);
		boolean err = false;
		try {
			st = (State) DatabaseManager.saveEntity(st, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(st.getId());
		assertFalse(err);

		// Update
		st.setCode("c2");
		st.setLabel("label2");
		st.setLabelFr("label FR2");
		st.setCapital("capital2");
		st.setRef(1);
		st.setNopic(true);
		err = false;
		try {
			st = (State) DatabaseManager.saveEntity(st, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(st.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(st);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testType() {
		// Create
		Type tp = new Type();
		tp.setLabel("label");
		tp.setLabelFr("label FR");
		tp.setNumber(0);
		boolean err = false;
		try {
			tp = (Type) DatabaseManager.saveEntity(tp, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(tp.getId());
		assertFalse(err);

		// Update
		tp.setLabel("label2");
		tp.setLabelFr("label FR2");
		tp.setNumber(1);
		err = false;
		try {
			tp = (Type) DatabaseManager.saveEntity(tp, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(tp.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(tp);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testSport() {
		// Create
		Sport sp = new Sport();
		sp.setLabel("label");
		sp.setLabelFr("label FR");
		sp.setIndex(1.0d);
		sp.setType(1);
		sp.setRef(0);
		sp.setNopic(false);
		boolean err = false;
		try {
			sp = (Sport) DatabaseManager.saveEntity(sp, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(sp.getId());
		assertFalse(err);
		
		// Update
		sp.setLabel("label2");
		sp.setLabelFr("label FR2");
		sp.setIndex(2.0d);
		sp.setType(0);
		sp.setRef(1);
		sp.setNopic(true);
		err = false;
		try {
			sp = (Sport) DatabaseManager.saveEntity(sp, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(sp.getId());
		assertFalse(err);
		
		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(sp);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}

	public void testTeam() {
		// Create
		Team tm = new Team();
		tm.setLabel("label");
		tm.setSport(new Sport(1));
		tm.setYear1("1900");
		tm.setYear2("1901");
		tm.setCountry(new Country(1));
		tm.setLeague(new League(1));
		tm.setConference("conf");
		tm.setConference("div");
		tm.setComment("comment");
		tm.setLink(0);
		tm.setInactive(false);
		tm.setRef(0);
		tm.setNopic(false);
		boolean err = false;
		try {
			tm = (Team) DatabaseManager.saveEntity(tm, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(tm.getId());
		assertFalse(err);
		
		// Update
		tm.setLabel("label2");
		tm.setSport(new Sport(2));
		tm.setYear1("1902");
		tm.setYear2("1903");
		tm.setCountry(new Country(2));
		tm.setLeague(new League(2));
		tm.setConference("conf2");
		tm.setConference("div2");
		tm.setComment("comment2");
		tm.setLink(1);
		tm.setInactive(true);
		tm.setRef(1);
		tm.setNopic(true);
		err = false;
		try {
			tm = (Team) DatabaseManager.saveEntity(tm, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(tm.getId());
		assertFalse(err);
		
		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(tm);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testTeamStadium() {
		// Create
		TeamStadium ts = new TeamStadium();
		ts.setLeague(new League(1));
		ts.setTeam(new Team(1));
		ts.setComplex(new Complex(4));
		ts.setDate1(1900);
		ts.setDate2(1901);
		ts.setRenamed(false);
		ts.setComment("comment");
		boolean err = false;
		try {
			ts = (TeamStadium) DatabaseManager.saveEntity(ts, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(ts.getId());
		assertFalse(err);
		
		// Update
		ts.setLeague(new League(2));
		ts.setTeam(new Team(2));
		ts.setComplex(new Complex(5));
		ts.setDate1(1902);
		ts.setDate2(1903);
		ts.setRenamed(true);
		ts.setComment("comment2");
		err = false;
		try {
			ts = (TeamStadium) DatabaseManager.saveEntity(ts, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(ts.getId());
		assertFalse(err);
		
		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(ts);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testWinLoss() {
		// Create
		WinLoss wl = new WinLoss();
		wl.setLeague(new League(1));
		wl.setTeam(new Team(1));
		wl.setType("1");
		wl.setCountWin(1);
		wl.setCountLoss(1);
		wl.setCountTie(1);
		wl.setCountOtloss(1);
		boolean err = false;
		try {
			wl = (WinLoss) DatabaseManager.saveEntity(wl, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(wl.getId());
		assertFalse(err);
		
		// Update
		wl.setLeague(new League(2));
		wl.setTeam(new Team(2));
		wl.setType("2");
		wl.setCountWin(2);
		wl.setCountLoss(2);
		wl.setCountTie(2);
		wl.setCountOtloss(2);
		err = false;
		try {
			wl = (WinLoss) DatabaseManager.saveEntity(wl, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(wl.getId());
		assertFalse(err);
		
		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(wl);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
	public void testYear() {
		// Create
		Year yr = new Year();
		yr.setLabel("3000");
		yr.setRef(0);
		boolean err = false;
		try {
			yr = (Year) DatabaseManager.saveEntity(yr, new Contributor(1));	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(yr.getId());
		assertFalse(err);

		// Update
		yr.setLabel("3001");
		yr.setRef(1);
		err = false;
		try {
			yr = (Year) DatabaseManager.saveEntity(yr, new Contributor(1));
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertNotNull(yr.getId());
		assertFalse(err);

		// Delete
		err = false;
		try {
			DatabaseManager.removeEntity(yr);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			err = true;
		}
		assertFalse(err);
	}
	
}
