package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class Calendar extends AbstractEntity {

	private Integer id;
	private Sport 	sport;
	private Championship championship;
	private Event 	event;
	private Event 	subevent;
	private Event 	subevent2;
	private Complex complex;
	private City 	city;
	private Country country;
	private String 	date1;
	private String 	date2;
	
	public static final transient String alias 	= "CL";
	public static final transient String table 	= "calendar";
	public static final transient String key 	= "id";
	public static final transient String query 	= "SELECT T.*, SP.label AS sp_label, SP.label_fr AS sp_label_fr, CP.label AS cp_label, CP.label_fr AS cp_label_fr, "
			+ " EV.label AS ev_label, EV.label_fr AS ev_label_fr, EV.id_type AS ev_id_type, TP1.label AS ev_tp_label, TP1.number AS ev_tp_number, "
			+ " SE.label AS se_label, SE.label_fr AS se_label_fr, SE.id_type AS se_id_type, TP2.label AS se_tp_label, TP2.number AS se_tp_number, "
			+ " SE2.label AS se2_label, SE2.label_fr AS se2_label_fr, SE2.id_type AS se2_id_type, TP3.label AS se2_tp_label, TP3.number AS se2_tp_number, "
			+ " CX.label AS cx_label, CT.label AS ct_label, CT.label_fr AS ct_label_fr, "
			+ " CN.label AS cn_label, CN.label_fr AS cn_label_fr "
			+ " FROM calendar T LEFT JOIN sport SP ON SP.id = T.id_sport "
			+ " LEFT JOIN championship CP ON CP.id = T.id_championship"
			+ " LEFT JOIN event EV ON EV.id = T.id_event"
			+ " LEFT JOIN event SE ON SE.id = T.id_subevent"
			+ " LEFT JOIN event SE2 ON SE2.id = T.id_subevent2"
			+ " LEFT JOIN type TP1 ON TP1.id = EV.id_type"
			+ " LEFT JOIN type TP2 ON TP2.id = SE.id_type"
			+ " LEFT JOIN type TP3 ON TP3.id = SE2.id_type"
			+ " LEFT JOIN complex CX ON CX.id = T.id_complex"
			+ " LEFT JOIN city CT ON CT.id = T.id_city"
			+ " LEFT JOIN country CN ON CN.id = T.id_country";
	
	public Calendar() {}
	
	public Calendar(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			Integer idSport = (Integer)mapValues.get("id_sport");
			if (idSport != null) {
				setSport(new Sport());
				getSport().setValuesFromMap(extractEntityColumns(Sport.alias, idSport, mapValues));
			}
			Integer idChampionship = (Integer)mapValues.get("id_championship");
			if (idChampionship != null) {
				setChampionship(new Championship());
				getChampionship().setValuesFromMap(extractEntityColumns(Championship.alias, idChampionship, mapValues));
			}
			Integer idEvent = (Integer)mapValues.get("id_event");
			if (idEvent != null) {
				setEvent(new Event());
				getEvent().setValuesFromMap(extractEntityColumns(Event.alias, idEvent, mapValues));
			}
			Integer idSubevent = (Integer)mapValues.get("id_subevent");
			if (idSubevent != null) {
				setSubevent(new Event());	
				getSubevent().setValuesFromMap(extractEntityColumns("SE", idSubevent, mapValues));
			}
			Integer idSubevent2 = (Integer)mapValues.get("id_subevent2");
			if (idSubevent2 != null) {
				setSubevent2(new Event());
				getSubevent2().setValuesFromMap(extractEntityColumns("SE2", idSubevent2, mapValues));
			}
			Integer idComplex = (Integer)mapValues.get("id_complex");
			if (idComplex != null) {
				setComplex(new Complex());
				getComplex().setValuesFromMap(extractEntityColumns(Complex.alias, idComplex, mapValues));
			}
			Integer idCity = (Integer)mapValues.get("id_city");
			if (idCity != null) {
				setCity(new City());
				getCity().setValuesFromMap(extractEntityColumns(City.alias, idCity, mapValues));
			}
			Integer idCountry = (Integer)mapValues.get("id_country");
			if (idCountry != null) {
				setCountry(new Country());
				getCountry().setValuesFromMap(extractEntityColumns(Country.alias, idCountry, mapValues));
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