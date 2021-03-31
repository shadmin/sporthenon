package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class TeamStadium extends AbstractEntity {

	private Integer id;
	private League league;
	private Team team;
	private Complex complex;
	private Integer date1;
	private Integer date2;
	private Boolean renamed;
	private String comment;
	
	public static final transient String alias = "TS";
	public static final transient String table = "team_stadium";
	public static final transient String key = 	 "id";
	
	public TeamStadium() {}
	
	public TeamStadium(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			Integer idLeague = (Integer)mapValues.get("id_league");
			if (idLeague != null) {
				setLeague(new League(idLeague));	
			}
			Integer idTeam = (Integer)mapValues.get("id_team");
			if (idTeam != null) {
				setTeam(new Team(idTeam));	
			}
			Integer idComplex = (Integer)mapValues.get("id_complex");
			if (idComplex != null) {
				setComplex(new Complex(idComplex));	
			}
			setDate1((Integer)mapValues.get("date1"));
			setDate2((Integer)mapValues.get("date2"));
			setRenamed((Boolean)mapValues.get("renamed"));
			setComment((String)mapValues.get("comment"));
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

	public Complex getComplex() {
		return complex;
	}

	public void setComplex(Complex complex) {
		this.complex = complex;
	}

	public Integer getDate1() {
		return date1;
	}

	public void setDate1(Integer date1) {
		this.date1 = date1;
	}

	public Integer getDate2() {
		return date2;
	}

	public void setDate2(Integer date2) {
		this.date2 = date2;
	}

	public Boolean getRenamed() {
		return renamed;
	}

	public void setRenamed(Boolean renamed) {
		this.renamed = renamed;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "TeamStadium [id=" + id + ", league=" + league + ", team=" + team + ", complex=" + complex + ", date1=" + date1 + ", date2=" + date2 + ", renamed=" + renamed + ", comment=" + comment + "]";
	}

	public String toString2() {
		return league.getLabel() + " - " + team.getLabel();
	}
	
}