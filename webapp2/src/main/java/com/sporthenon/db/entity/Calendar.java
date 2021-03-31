package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class Calendar extends AbstractEntity {

	private Integer id;
	private Sport sport;
	private Championship championship;
	private Event event;
	private Event subevent;
	private Event subevent2;
	private Complex complex;
	private City city;
	private Country country;
	private String date1;
	private String date2;
	
	public static final transient String alias = "CL";
	public static final transient String table = "calendar";
	public static final transient String key = 	 "id";
	
	public Calendar() {}
	
	public Calendar(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			Integer idSport = (Integer)mapValues.get("id_sport");
			if (idSport != null) {
				setSport(new Sport(idSport));	
			}
			Integer idChampionship = (Integer)mapValues.get("id_championship");
			if (idChampionship != null) {
				setChampionship(new Championship(idChampionship));	
			}
			Integer idEvent = (Integer)mapValues.get("id_event");
			if (idEvent != null) {
				setEvent(new Event(idEvent));	
			}
			Integer idSubevent = (Integer)mapValues.get("id_subevent");
			if (idSubevent != null) {
				setSubevent(new Event(idSubevent));	
			}
			Integer idSubevent2 = (Integer)mapValues.get("id_subevent2");
			if (idSubevent2 != null) {
				setSubevent2(new Event(idSubevent2));	
			}
			Integer idComplex = (Integer)mapValues.get("id_complex");
			if (idComplex != null) {
				setComplex(new Complex(idComplex));	
			}
			Integer idCity = (Integer)mapValues.get("id_city");
			if (idCity != null) {
				setCity(new City(idCity));	
			}
			Integer idCountry = (Integer)mapValues.get("id_country");
			if (idCountry != null) {
				setCountry(new Country(idCountry));	
			}
			setDate1((String)mapValues.get("date1"));
			setDate2((String)mapValues.get("date2"));
			setMetadata(new Metadata((Integer)mapValues.get("id_contributor"), (Timestamp)mapValues.get("first_update"), (Timestamp)mapValues.get("last_update")));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Calendar [id=" + id + ", sport=" + sport + ", championship=" + championship + ", event=" + event
				+ ", subevent=" + subevent + ", subevent2=" + subevent2 + ", complex=" + complex + ", city=" + city
				+ ", country=" + country + ", date1=" + date1 + ", date2=" + date2 + "]";
	}

	public String toString2(String lang) {
		return sport.getLabel(lang) + " - " + championship.getLabel(lang) + (event != null ? " - " + event.getLabel(lang) : "") + (subevent != null ? " - " + subevent.getLabel(lang) : "") + (subevent2 != null ? " - " + subevent2.getLabel(lang) : "") + " - " + date1 + "/" + date2;
	}
	
}