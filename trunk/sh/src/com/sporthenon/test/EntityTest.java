package com.sporthenon.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.TestCase;

import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.HallOfFame;
import com.sporthenon.db.entity.League;
import com.sporthenon.db.entity.OlympicRanking;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.RetiredNumber;
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
	
	public void testChampionship() {
		List<Championship> lst = em.createQuery("from Championship").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Championship);
	}
	
	public void testCity() {
		List<City> lst = em.createQuery("from City").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof City);
	}
	
	public void testComplex() {
		List<Complex> lst = em.createQuery("from Complex").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Complex);
	}
	
	public void testCountry() {
		List<Country> lst = em.createQuery("from Country").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Country);
	}
	
	public void testEvent() {
		List<Event> lst = em.createQuery("from Event").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Event);
	}
	
	public void testHallOfFame() {
		List<HallOfFame> lst = em.createQuery("from HallOfFame").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof HallOfFame);
	}
	
	public void testLeague() {
		List<League> lst = em.createQuery("from League").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof League);
	}
	
	public void testOlympicRanking() {
		List<OlympicRanking> lst = em.createQuery("from OlympicRanking").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof OlympicRanking);
	}
	
	public void testOlympics() {
		List<Olympics> lst = em.createQuery("from Olympics").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Olympics);
	}
	
	public void testAthlete() {
		List<Athlete> lst = em.createQuery("from Athlete").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Athlete);
	}
	
	public void testRecord() {
		List<Record> lst = em.createQuery("from Record").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Record);
	}
	
	public void testResult() {
		List<Result> lst = em.createQuery("from Result").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Result);
	}
	
	public void testRetiredNumber() {
		List<RetiredNumber> lst = em.createQuery("from RetiredNumber").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof RetiredNumber);
	}
	
	public void testSport() {
		List<Sport> lst = em.createQuery("from Sport").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Sport);
	}
	
	public void testState() {
		List<State> lst = em.createQuery("from State").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof State);
	}
	
	public void testTeam() {
		List<Team> lst = em.createQuery("from Team").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Team);
	}
	
	public void testTeamStadium() {
		List<TeamStadium> lst = em.createQuery("from TeamStadium").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof TeamStadium);
	}
	
	public void testType() {
		List<Type> lst = em.createQuery("from Type").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Type);
	}
	
	public void testWinLoss() {
		List<WinLoss> lst = em.createQuery("from WinLoss").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof WinLoss);
	}
	
	public void testYear() {
		List<Year> lst = em.createQuery("from Year").getResultList();
		assertNotNull(lst);
		assertTrue(lst.size() > 0 && lst.get(0) instanceof Year);
	}

}
