package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class HallOfFame extends AbstractEntity {
	
	private Integer id;
	private League league;
	private Year year;
	private Athlete person;
	private String position;
	
	public static final transient String alias = "HF";
	public static final transient String table = "hall_of_fame";
	public static final transient String key = 	 "id";
	
	public HallOfFame() {}
	
	public HallOfFame(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			Integer idLeague = (Integer)mapValues.get("id_league");
			if (idLeague != null) {
				setLeague(new League(idLeague));	
			}
			Integer idYear = (Integer)mapValues.get("id_year");
			if (idYear != null) {
				setYear(new Year(idYear));	
			}
			Integer idPerson = (Integer)mapValues.get("id_person");
			if (idPerson != null) {
				setPerson(new Athlete(idPerson));	
			}
			setPosition((String)mapValues.get("position"));
			setMetadata(new Metadata((Integer)mapValues.get("id_contributor"), (Timestamp)mapValues.get("first_update"), (Timestamp)mapValues.get("last_update")));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public Athlete getPerson() {
		return person;
	}

	public void setPerson(Athlete person) {
		this.person = person;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "HallOfFame [id=" + id + ", league=" + league + ", year=" + year + ", person=" + person + ", position=" + position + "]";
	}
	
	public String toString2() {
		return league.getLabel() + " - " + year.getLabel();
	}
	
}