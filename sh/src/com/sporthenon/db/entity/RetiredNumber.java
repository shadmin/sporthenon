package com.sporthenon.db.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sporthenon.db.entity.meta.Metadata;

@Entity
@Table(name = "\"RETIRED_NUMBER\"")
public class RetiredNumber {
	
	public static final transient String alias = "RN";

	@Id
	@SequenceGenerator(name = "sq_retired_number", sequenceName = "\"SQ_RETIRED_NUMBER\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_retired_number")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_league", nullable = false)
	private League league;
	
	@ManyToOne
	@JoinColumn(name = "id_team", nullable = false)
	private Team team;
	
	@ManyToOne
	@JoinColumn(name = "id_person", nullable = false)
	private Athlete person;
	
	@ManyToOne
	@JoinColumn(name = "id_year", nullable = false)
	private Year year;
	
	@Column(name = "\"number\"", nullable = false)
	private Integer number;
	
	@Embedded
	private Metadata metadata;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
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
		return "RetiredNumber [id=" + id + ", league=" + league + ", team="
				+ team + ", person=" + person + ", number=" + number + "]";
	}
	
}