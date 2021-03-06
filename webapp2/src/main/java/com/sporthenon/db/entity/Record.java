package com.sporthenon.db.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class Record extends AbstractEntity {
	
	private Integer id;
	private Sport 	sport;
	private Championship championship;
	private Event 	event;
	private Event 	subevent;
	private City 	city;
	private String 	label;
	private Integer idRank1;
	private Integer idRank2;
	private Integer idRank3;
	private Integer idRank4;
	private Integer idRank5;
	private String 	record1;
	private String 	record2;
	private String 	record3;
	private String 	record4;
	private String 	record5;
	private String 	date1;
	private String 	date2;
	private String 	date3;
	private String 	date4;
	private String 	date5;
	private Boolean counting;
	private BigDecimal index;
	private String 	type1;
	private String 	type2;
	private String 	comment;
	private String	exa;

	public static final transient String alias 	= "RC";
	public static final transient String table 	= "record";
	public static final transient String cols 	= "id_sport,id_championship,id_event,id_subevent,id_city,label,id_rank1,id_rank2,id_rank3,id_rank4,id_rank5,"
			+ "record1,record2,record3,record4,record5,date1,date2,date3,date4,date5,counting,index,type1,type2,comment,exa";
	public static final transient String query 	= "SELECT T.*, SP.label AS sp_label, SP.label_fr AS sp_label_fr, CP.label AS cp_label, CP.label_fr AS cp_label_fr, "
			+ " EV.label AS ev_label, EV.label_fr AS ev_label_fr, EV.id_type AS ev_id_type, TP1.label AS ev_tp_label, TP1.number AS ev_tp_number, "
			+ " SE.label AS se_label, SE.label_fr AS se_label_fr, SE.id_type AS se_id_type, TP2.label AS se_tp_label, TP2.number AS se_tp_number, "
			+ " CT.label AS ct_label, CT.label_fr AS ct_label_fr, CN.code AS ct_cn_code"
			+ " FROM record T LEFT JOIN sport SP ON SP.id = T.id_sport "
			+ " LEFT JOIN championship CP ON CP.id = T.id_championship"
			+ " LEFT JOIN event EV ON EV.id = T.id_event"
			+ " LEFT JOIN event SE ON SE.id = T.id_subevent"
			+ " LEFT JOIN type TP1 ON TP1.id = EV.id_type"
			+ " LEFT JOIN type TP2 ON TP2.id = SE.id_type"
			+ " LEFT JOIN city CT ON CT.id = T.id_city"
			+ " LEFT JOIN country CN ON CN.id = CT.id_country";
	
	public Record() {}
	
	public Record(Integer id) {
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
			Integer idCity = (Integer)mapValues.get("id_city");
			if (idCity != null) {
				setCity(new City());
				getCity().setValuesFromMap(extractEntityColumns(City.alias, idCity, mapValues));
			}
			setLabel((String)mapValues.get("label"));
			setIdRank1((Integer)mapValues.get("id_rank1"));
			setIdRank2((Integer)mapValues.get("id_rank2"));
			setIdRank3((Integer)mapValues.get("id_rank3"));
			setIdRank4((Integer)mapValues.get("id_rank4"));
			setIdRank5((Integer)mapValues.get("id_rank5"));
			setRecord1((String)mapValues.get("record1"));
			setRecord2((String)mapValues.get("record2"));
			setRecord3((String)mapValues.get("record3"));
			setRecord4((String)mapValues.get("record4"));
			setRecord5((String)mapValues.get("record5"));
			setDate1((String)mapValues.get("date1"));
			setDate2((String)mapValues.get("date2"));
			setDate3((String)mapValues.get("date3"));
			setDate4((String)mapValues.get("date4"));
			setDate5((String)mapValues.get("date5"));
			setCounting((Boolean)mapValues.get("counting"));
			setIndex((BigDecimal)mapValues.get("index"));
			setType1((String)mapValues.get("type1"));
			setType2((String)mapValues.get("type2"));
			setComment((String)mapValues.get("comment"));
			setExa((String)mapValues.get("exa"));
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

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public Championship getChampionship() {
		return championship;
	}

	public void setChampionship(Championship championship) {
		this.championship = championship;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Event getSubevent() {
		return subevent;
	}

	public void setSubevent(Event subevent) {
		this.subevent = subevent;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getIdRank1() {
		return idRank1;
	}

	public void setIdRank1(Integer idRank1) {
		this.idRank1 = idRank1;
	}

	public Integer getIdRank2() {
		return idRank2;
	}

	public void setIdRank2(Integer idRank2) {
		this.idRank2 = idRank2;
	}

	public Integer getIdRank3() {
		return idRank3;
	}

	public void setIdRank3(Integer idRank3) {
		this.idRank3 = idRank3;
	}

	public Integer getIdRank4() {
		return idRank4;
	}

	public void setIdRank4(Integer idRank4) {
		this.idRank4 = idRank4;
	}

	public Integer getIdRank5() {
		return idRank5;
	}

	public void setIdRank5(Integer idRank5) {
		this.idRank5 = idRank5;
	}

	public String getRecord1() {
		return record1;
	}

	public void setRecord1(String record1) {
		this.record1 = record1;
	}

	public String getRecord2() {
		return record2;
	}

	public void setRecord2(String record2) {
		this.record2 = record2;
	}

	public String getRecord3() {
		return record3;
	}

	public void setRecord3(String record3) {
		this.record3 = record3;
	}

	public String getRecord4() {
		return record4;
	}

	public void setRecord4(String record4) {
		this.record4 = record4;
	}

	public String getRecord5() {
		return record5;
	}

	public void setRecord5(String record5) {
		this.record5 = record5;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public String getDate3() {
		return date3;
	}

	public void setDate3(String date3) {
		this.date3 = date3;
	}

	public String getDate4() {
		return date4;
	}

	public void setDate4(String date4) {
		this.date4 = date4;
	}

	public String getDate5() {
		return date5;
	}

	public void setDate5(String date5) {
		this.date5 = date5;
	}

	public Boolean getCounting() {
		return counting;
	}

	public void setCounting(Boolean counting) {
		this.counting = counting;
	}

	public BigDecimal getIndex() {
		return index;
	}

	public void setIndex(BigDecimal index) {
		this.index = index;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getExa() {
		return exa;
	}

	public void setExa(String exa) {
		this.exa = exa;
	}

	public Integer getIdSport() {
		return (sport != null ? sport.getId() : null);
	}
	
	public Integer getIdChampionship() {
		return (championship != null ? championship.getId() : null);
	}
	
	public Integer getIdEvent() {
		return (event != null ? event.getId() : null);
	}
	
	public Integer getIdSubevent() {
		return (subevent != null ? subevent.getId() : null);
	}
	
	public Integer getIdCity() {
		return (city != null ? city.getId() : null);
	}
	
	public void setIdSport(Integer id) {
		sport = (id != null && id > 0 ? new Sport(id) : null);
	}
	
	public void setIdChampionship(Integer id) {
		championship = (id != null && id > 0 ? new Championship(id) : null);
	}
	
	public void setIdEvent(Integer id) {
		event = (id != null && id > 0 ? new Event(id) : null);
	}
	
	public void setIdSubevent(Integer id) {
		subevent = (id != null && id > 0 ? new Event(id) : null);
	}
	
	public void setIdCity(Integer id) {
		city = (id != null && id > 0 ? new City(id) : null);
	}
	
	@Override
	public String toString() {
		return "Record [id=" + id + ", sport=" + sport + ", championship=" + championship + ", event=" + event + ", subevent=" + subevent + ", city=" + city + ", label=" + label + ", idRank1=" + idRank1 + ", idRank2=" + idRank2 + ", idRank3=" + idRank3 + ", idRank4=" + idRank4 + ", idRank5=" + idRank5 + ", record1=" + record1 + ", record2=" + record2 + ", record3=" + record3 + ", record4=" + record4 + ", record5=" + record5 + ", date1=" + date1 + ", date2=" + date2 + ", date3=" + date3 + ", date4=" + date4 + ", date5=" + date5 + ", counting=" + counting + ", index=" + index + ", type1=" + type1 + ", type2=" + type2 + ", comment=" + comment + ", exa=" + exa + "]";
	}
	
	public String toString2(String lang) {
		return sport.getLabel(lang) + " - " + championship.getLabel(lang) + (event != null ? " - " + event.getLabel(lang) : "") + (subevent != null ? " - " + subevent.getLabel(lang) : "") + " - " + type1 + " - " + type2 + " - " + label;
	}
	
}