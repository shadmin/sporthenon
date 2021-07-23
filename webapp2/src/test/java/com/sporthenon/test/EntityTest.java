package com.sporthenon.test;

import java.util.List;
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
import com.sporthenon.db.entity.Year;

import junit.framework.TestCase;

@SuppressWarnings("unchecked")
public class EntityTest extends TestCase {

	private static final Logger log = Logger.getLogger(EntityTest.class.getName());
	
	@Override
	protected void setUp() throws Exception {
		final String dbHost = 	System.getenv("SHDB_HOST");
		final String dbPort = 	System.getenv("SHDB_PORT");
		final String dbName = 	System.getenv("SHDB_NAME");
		final String dbUser = 	System.getenv("SHDB_USER");
		final String dbPwd = 	System.getenv("SHDB_PWD");
		try {
			DatabaseManager.createConnectionPool(dbHost, dbPort, dbName, dbUser, dbPwd);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		super.setUp();
	}

	public void testAthlete() {
		List<Athlete> lst = (List<Athlete>) DatabaseManager.executeSelect(Athlete.query + " LIMIT 10", Athlete.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testCalendar() {
		List<Calendar> lst = (List<Calendar>) DatabaseManager.executeSelect(Calendar.query + " LIMIT 10", Calendar.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testChampionship() {
		List<Championship> lst = (List<Championship>) DatabaseManager.executeSelect("SELECT * FROM championship LIMIT 10", Championship.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testCity() {
		List<City> lst = (List<City>) DatabaseManager.executeSelect(City.query + " LIMIT 10", City.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testComplex() {
		List<Complex> lst = (List<Complex>) DatabaseManager.executeSelect(Complex.query + " LIMIT 10", Complex.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testCountry() {
		List<Country> lst = (List<Country>) DatabaseManager.executeSelect("SELECT * FROM country LIMIT 10", Country.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testEvent() {
		List<Event> lst = (List<Event>) DatabaseManager.executeSelect(Event.query + " LIMIT 10", Event.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testHallOfFame() {
		List<HallOfFame> lst = (List<HallOfFame>) DatabaseManager.executeSelect(HallOfFame.query + " LIMIT 10", HallOfFame.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testLeague() {
		List<League> lst = (List<League>) DatabaseManager.executeSelect("SELECT * FROM league LIMIT 10", League.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testOlympicRanking() {
		List<OlympicRanking> lst = (List<OlympicRanking>) DatabaseManager.executeSelect(OlympicRanking.query + " LIMIT 10", OlympicRanking.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testOlympics() {
		List<Olympics> lst = (List<Olympics>) DatabaseManager.executeSelect(Olympics.query + " LIMIT 10", Olympics.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testRecord() {
		List<Record> lst = (List<Record>) DatabaseManager.executeSelect(Record.query + " LIMIT 10", Record.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testResult() {
		List<Result> lst = (List<Result>) DatabaseManager.executeSelect(Result.query + " LIMIT 10", Result.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testRetiredNumber() {
		List<RetiredNumber> lst = (List<RetiredNumber>) DatabaseManager.executeSelect(RetiredNumber.query + " LIMIT 10", RetiredNumber.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testRound() {
		List<Round> lst = (List<Round>) DatabaseManager.executeSelect(Round.query + " LIMIT 10", Round.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testRoundType() {
		List<RoundType> lst = (List<RoundType>) DatabaseManager.executeSelect("SELECT * FROM round_type LIMIT 10", RoundType.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testSport() {
		List<Sport> lst = (List<Sport>) DatabaseManager.executeSelect("SELECT * FROM sport LIMIT 10", Sport.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testState() {
		List<State> lst = (List<State>) DatabaseManager.executeSelect("SELECT * FROM state LIMIT 10", State.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testTeam() {
		List<Team> lst = (List<Team>) DatabaseManager.executeSelect(Team.query + " LIMIT 10", Team.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testTeamStadium() {
		List<TeamStadium> lst = (List<TeamStadium>) DatabaseManager.executeSelect(TeamStadium.query + " LIMIT 10", TeamStadium.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testType() {
		List<Type> lst = (List<Type>) DatabaseManager.executeSelect("SELECT * FROM type LIMIT 10", Type.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
	public void testYear() {
		List<Year> lst = (List<Year>) DatabaseManager.executeSelect("SELECT * FROM year LIMIT 10", Year.class);
		assertNotNull(lst);
		assertFalse(lst.isEmpty());
		log.log(Level.FINEST, lst.toString());
	}
	
}
