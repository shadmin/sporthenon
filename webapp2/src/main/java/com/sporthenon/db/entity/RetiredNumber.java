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
	private Integer number;
	
	public static final transient String alias 	= "RN";
	public static final transient String table 	= "retired_number";
	public static final transient String key 	= "id";
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
			setNumber((Integer)mapValues.get("number"));
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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "RetiredNumber [id=" + id + ", league=" + league + ", team=" + team + ", person=" + person + ", number=" + number + "]";
	}

	public String toString2() {
		return league.getLabel() + " - " + team.getLabel();
	}
	
}