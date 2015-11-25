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
@Table(name = "\"HallOfFame\"")
public class HallOfFame {
	
	public static final transient String alias = "HF";

	@Id
	@SequenceGenerator(name = "seq_hall_of_fame", sequenceName = "\"SeqHallOfFame\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_hall_of_fame")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_league", nullable = false)
	private League league;
	
	@ManyToOne
	@JoinColumn(name = "id_year", nullable = false)
	private Year year;
	
	@ManyToOne
	@JoinColumn(name = "id_person", nullable = false)
	private Athlete person;
	
	@Column(name = "\"position\"")
	private String position;
	
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