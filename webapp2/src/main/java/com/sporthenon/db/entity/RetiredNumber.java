package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class RetiredNumber extends AbstractEntity {
	
	private Integer id;
	private League 	league;
	private Team 	team;
	private Athlete person;
	private Year 	year;
	private String 	number;
	
	public static final transient String alias 	= "RN";
	public static final transient String table 	= "retired_number";
	public static final transient String cols 	= "id_league,id_team,id_person,id_year,number";
	public static final transient String query 	= "SELECT T.*, LG.label AS lg_label, TM.label AS tm_label, "
			+ " PR.last_name AS pr_last_name, PR.first_name AS pr_first_name, YR.label AS yr_label "
			+ " FROM retired_number T LEFT JOIN league LG ON LG.id = T.id_league "
			+ " LEFT JOIN team TM ON TM.id = T.id_team"
			+ " LEFT JOIN athlete PR ON PR.id = T.id_person"
			+ " LEFT JOIN year YR ON YR.id = T.id_year";
	
	public RetiredNumber() {}
	
	public RetiredNumber(Integer id) {
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
			Integer idTeam = (Integer)mapValues.get("id_team");
			if (idTeam != null) {
				setTeam(new Team());
				getTeam().setValuesFromMap(extractEntityColumns(Team.alias, idTeam, mapValues));
			}
			Integer idPerson = (Integer)mapValues.get("id_person");
			if (idPerson != null) {
				setPerson(new Athlete());
				getPerson().setValuesFromMap(extractEntityColumns(Athlete.alias, idPerson, mapValues));
			}
			Integer idYear = (Integer)mapValues.get("id_year");
			if (idYear != null) {
				setYear(new Year());
				getYear().setValuesFromMap(extractEntityColumns(Year.alias, idYear, mapValues));
			}
			setNumber(mapValues.get("number") != null ? (String)mapValues.get("number") : null);
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

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Athlete getPerson() {
		return person;
	}

	public void setPerson(Athlete person) {
		this.person = person;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public Integer getIdLeague() {
		return (league != null ? league.getId() : null);
	}
	
	public Integer getIdTeam() {
		return (team != null ? team.getId() : null);
	}
	
	public Integer getIdPerson() {
		return (person != null ? person.getId() : null);
	}
	
	public Integer getIdYear() {
		return (year != null ? year.getId() : null);
	}
	
	public void setIdLeague(Integer id) {
		league = (id != null && id > 0 ? new League(id) : null);
	}
	
	public void setIdTeam(Integer id) {
		team = (id != null && id > 0 ? new Team(id) : null);
	}
	
	public void setIdPerson(Integer id) {
		person = (id != null && id > 0 ? new Athlete(id) : null);
	}
	
	public void setIdYear(Integer id) {
		year = (id != null && id > 0 ? new Year(id) : null);
	}
	
	@Override
	public String toString() {
		return "RetiredNumber [id=" + id + ", league=" + league + ", team=" + team + ", person=" + person + ", number=" + number + "]";
	}

	public String toString2() {
		return league.getLabel() + " - " + team.getLabel();
	}
	
}