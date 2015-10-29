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
@Table(name = "\"Calendar\"")
public class Calendar {

	public static final transient String alias = "CL";
	
	@Id
	@SequenceGenerator(name = "seq_calendar", sequenceName = "\"SeqCalendar\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_calendar")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_sport", nullable = false)
	private Sport sport;
	
	@ManyToOne
	@JoinColumn(name = "id_championship", nullable = false)
	private Championship championship;
	
	@ManyToOne
	@JoinColumn(name = "id_event")
	private Event event;
	
	@ManyToOne
	@JoinColumn(name = "id_subevent")
	private Event subevent;
	
	@ManyToOne
	@JoinColumn(name = "id_subevent2")
	private Event subevent2;
	
	@ManyToOne
	@JoinColumn(name = "id_complex")
	private Complex complex;
	
	@ManyToOne
	@JoinColumn(name = "id_city")
	private City city;
	
	@Column(name = "date1", length = 10)
	private String date1;
	
	@Column(name = "date2", length = 10)
	private String date2;
	
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

	public Sport getSport() {
		return sport;
	}

	public Championship getChampionship() {
		return championship;
	}

	public Event getEvent() {
		return event;
	}

	public Event getSubevent() {
		return subevent;
	}

	public Event getSubevent2() {
		return subevent2;
	}

	public Complex getComplex() {
		return complex;
	}

	public City getCity() {
		return city;
	}

	public String getDate1() {
		return date1;
	}

	public String getDate2() {
		return date2;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public void setChampionship(Championship championship) {
		this.championship = championship;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public void setSubevent(Event subevent) {
		this.subevent = subevent;
	}

	public void setSubevent2(Event subevent2) {
		this.subevent2 = subevent2;
	}

	public void setComplex(Complex complex) {
		this.complex = complex;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public String toString2(String lang) {
		return sport.getLabel(lang) + " - " + championship.getLabel(lang) + (event != null ? " - " + event.getLabel(lang) : "") + (subevent != null ? " - " + subevent.getLabel(lang) : "") + (subevent2 != null ? " - " + subevent2.getLabel(lang) : "") + " - " + date1 + "/" + date2;
	}
	
}