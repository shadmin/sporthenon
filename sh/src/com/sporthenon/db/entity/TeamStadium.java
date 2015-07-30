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
@Table(name = "\"TEAM_STADIUM\"")
public class TeamStadium {

	public static final transient String alias = "TS";
	
	@Id
	@SequenceGenerator(name = "seq_team_stadium", sequenceName = "\"SeqTeamStadium\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_team_stadium")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_league", nullable = false)
	private League league;
	
	@ManyToOne
	@JoinColumn(name = "id_team", nullable = false)
	private Team team;
	
	@ManyToOne
	@JoinColumn(name = "id_complex", nullable = false)
	private Complex complex;
	
	@Column(name = "date1", nullable = false)
	private Integer date1;
	
	@Column(name = "date2", nullable = false)
	private Integer date2;
	
	@Column(name = "renamed")
	private Boolean renamed;
	
	@Column(name = "\"comment\"", length = 500)
	private String comment;
	
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
		return "TeamStadium [id=" + id + ", league=" + league + ", team="
				+ team + ", complex=" + complex + ", date1=" + date1
				+ ", date2=" + date2 + ", renamed=" + renamed + ", comment="
				+ comment + "]";
	}
	
}