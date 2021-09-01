package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class HallOfFame extends AbstractEntity {
	
	private Integer id;
	private League 	league;
	private Year 	year;
	private Athlete person;
	private String text;
	private String 	position;
	
	public static final transient String alias 	= "HF";
	public static final transient String table 	= "hall_of_fame";
	public static final transient String cols 	= "id_league,id_year,id_person,text,position";
	public static final transient String query 	= "SELECT T.*, LG.label AS lg_label, "
			+ " PR.last_name AS pr_last__name, PR.first_name AS pr_first_name, YR.label AS yr_label "
			+ " FROM hall_of_fame T LEFT JOIN league LG ON LG.id = T.id_league "
			+ " LEFT JOIN athlete PR ON PR.id = T.id_person"
			+ " LEFT JOIN year YR ON YR.id = T.id_year";
	
	public HallOfFame() {}
	
	public HallOfFame(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			Integer idLeague = (Integer)mapValues.get("id_league");
			if (idLeague != null) {
				setLeague(new League());
				getLeague().setValuesFromMap(extractEntityColumns(League.alias, idLeague, mapValues));
			}
			Integer idYear = (Integer)mapValues.get("id_year");
			if (idYear != null) {
				setYear(new Year());	
				getYear().setValuesFromMap(extractEntityColumns(Year.alias, idYear, mapValues));
			}
			Integer idPerson = (Integer)mapValues.get("id_person");
			if (idPerson != null) {
				setPerson(new Athlete());
				getPerson().setValuesFromMap(extractEntityColumns(Athlete.alias, idPerson, mapValues));
			}
			setPosition((String)mapValues.get("position"));
			setText(mapValues.get("text") != null ? (String)mapValues.get("text") : null);
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getIdLeague() {
		return (league != null ? league.getId() : null);
	}

	public Integer getIdYear() {
		return (year != null ? year.getId() : null);
	}

	public Integer getIdPerson() {
		return (person != null ? person.getId() : null);
	}
	
	public void setIdLeague(Integer id) {
		league = (id != null && id > 0 ? new League(id) : null);
	}
	
	public void setIdYear(Integer id) {
		year = (id != null && id > 0 ? new Year(id) : null);
	}
	
	public void setIdPerson(Integer id) {
		person = (id != null && id > 0 ? new Athlete(id) : null);
	}
	
	@Override
	public String toString() {
		return "HallOfFame [id=" + id + ", league=" + league + ", year=" + year + ", person=" + person + ", text="
				+ text + ", position=" + position + "]";
	}

	public String toString2() {
		return league.getLabel() + " - " + year.getLabel();
	}
	
}