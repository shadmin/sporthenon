package com.sporthenon.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.meta.Contributor;

import junit.framework.TestCase;

public class UpdateTest extends TestCase {

	private static final Logger log = Logger.getLogger(UpdateTest.class.getName());
	
	/*
Calendar.java
Championship.java
City.java
Complex.java
Country.java
Event.java
HallOfFame.java
League.java
OlympicRanking.java
Olympics.java
Record.java
Result.java
RetiredNumber.java
Round.java
RoundType.java
State.java
Team.java
TeamStadium.java
Type.java
WinLoss.java
Year.java
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
	
}
