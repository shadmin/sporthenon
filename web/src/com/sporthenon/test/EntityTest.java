package com.sporthenon.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import junit.framework.TestCase;

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

@SuppressWarnings("unchecked")
public class EntityTest extends TestCase {

	private EntityManagerFactory emf = null;
	private EntityManager em;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		emf = Persistence.createEntityManagerFactory("shunit-standalone");
		em = emf.createEntityManager();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		if (em != null)
			em.close();
		if (emf != null)
			emf.close();
	}
	
	public void testAthlete() {
		Query q = em.createQuery("from Athlete");
		q.setMaxResults(10);
		List<Athlete> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Athlete);
	}
	
	public void testCalendar() {
		Query q = em.createQuery("from Calendar");
		q.setMaxResults(10);
		List<Calendar> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Calendar);
	}
	
	public void testChampionship() {
		Query q = em.createQuery("from Championship");
		q.setMaxResults(10);
		List<Championship> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Championship);
	}
	
	public void testCity() {
		Query q = em.createQuery("from City");
		q.setMaxResults(10);
		List<City> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof City);
	}
	
	public void testComplex() {
		Query q = em.createQuery("from Complex");
		q.setMaxResults(10);
		List<Complex> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Complex);
	}
	
	public void testCountry() {
		Query q = em.createQuery("from Country");
		q.setMaxResults(10);
		List<Country> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Country);
	}
	
	public void testEvent() {
		Query q = em.createQuery("from Event");
		q.setMaxResults(10);
		List<Event> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Event);
	}
	
	public void testHallOfFame() {
		Query q = em.createQuery("from HallOfFame");
		q.setMaxResults(10);
		List<HallOfFame> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof HallOfFame);
	}
	
	public void testLeague() {
		Query q = em.createQuery("from League");
		q.setMaxResults(10);
		List<League> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof League);
	}
	
	public void testOlympicRanking() {
		Query q = em.createQuery("from OlympicRanking");
		q.setMaxResults(10);
		List<OlympicRanking> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof OlympicRanking);
	}
	
	public void testOlympics() {
		Query q = em.createQuery("from Olympics");
		q.setMaxResults(10);
		List<Olympics> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Olympics);
	}
	
	public void testRecord() {
		Query q = em.createQuery("from Record");
		q.setMaxResults(10);
		List<Record> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Record);
	}
	
	public void testResult() {
		Query q = em.createQuery("from Result");
		q.setMaxResults(10);
		List<Result> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Result);
	}
	
	public void testRetiredNumber() {
		Query q = em.createQuery("from RetiredNumber");
		q.setMaxResults(10);
		List<RetiredNumber> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof RetiredNumber);
	}
	
	public void testRound() {
		Query q = em.createQuery("from Round");
		q.setMaxResults(10);
		List<Round> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Round);
	}
	
	public void testRoundType() {
		Query q = em.createQuery("from RoundType");
		q.setMaxResults(10);
		List<RoundType> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof RoundType);
	}
	
	public void testSport() {
		Query q = em.createQuery("from Sport");
		q.setMaxResults(10);
		List<Sport> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Sport);
	}
	
	public void testState() {
		Query q = em.createQuery("from State");
		q.setMaxResults(10);
		List<State> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof State);
	}
	
	public void testTeam() {
		Query q = em.createQuery("from Team");
		q.setMaxResults(10);
		List<Team> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Team);
	}
	
	public void testTeamStadium() {
		Query q = em.createQuery("from TeamStadium");
		q.setMaxResults(10);
		List<TeamStadium> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof TeamStadium);
	}
	
	public void testType() {
		Query q = em.createQuery("from Type");
		q.setMaxResults(10);
		List<Type> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Type);
	}
	
	public void testWinLoss() {
		Query q = em.createQuery("from WinLoss");
		q.setMaxResults(10);
		List<WinLoss> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof WinLoss);
	}
	
	public void testYear() {
		Query q = em.createQuery("from Year");
		q.setMaxResults(10);
		List<Year> lst = q.getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Year);
	}

}
