package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class Result extends AbstractEntity {

	private Integer id;
	private Sport 	sport;
	private Championship championship;
	private Event 	event;
	private Event 	subevent;
	private Event 	subevent2;
	private City 	city1;
	private City 	city2;
	private Complex complex1;
	private Complex complex2;
	private Country country1;
	private Country country2;
	private Year 	year;
	private String 	date1;
	private String 	date2;
	private Integer idRank1;
	private Integer idRank2;
	private Integer idRank3;
	private Integer idRank4;
	private Integer idRank5;
	private Integer idRank6;
	private Integer idRank7;
	private Integer idRank8;
	private Integer idRank9;
	private Integer idRank10;
	private Integer idRank11;
	private Integer idRank12;
	private Integer idRank13;
	private Integer idRank14;
	private Integer idRank15;
	private Integer idRank16;
	private Integer idRank17;
	private Integer idRank18;
	private Integer idRank19;
	private Integer idRank20;
	private String 	result1;
	private String 	result2;
	private String 	result3;
	private String 	result4;
	private String 	result5;
	private String 	result6;
	private String 	result7;
	private String 	result8;
	private String 	result9;
	private String 	result10;
	private String 	result11;
	private String 	result12;
	private String 	result13;
	private String 	result14;
	private String 	result15;
	private String 	result16;
	private String 	result17;
	private String 	result18;
	private String 	result19;
	private String 	result20;
	private String 	comment;
	private String 	exa;
	private Boolean draft;
	private Boolean noPlace;
	private Boolean noDate;
	
	public static final transient String alias 	= "RS";
	public static final transient String table 	= "result";
	public static final transient String cols 	= "id_sport,id_championship,id_event,id_subevent,id_subevent2,id_city1,id_city2,id_complex1,id_complex2,"
			+ "id_country1,id_country2,id_year,date1,date2,id_rank1,id_rank2,id_rank3,id_rank4,id_rank5,id_rank6,id_rank7,id_rank8,id_rank9,id_rank10,"
			+ "id_rank11,id_rank12,id_rank13,id_rank14,id_rank15,id_rank16,id_rank17,id_rank18,id_rank19,id_rank20,"
			+ "result1,result2,result3,result4,result5,result6,result7,result8,result9,result10,"
			+ "result11,result12,result13,result14,result15,result16,result17,result18,result19,result20,"
			+ "comment,exa,draft,no_place,no_date";
	public static final transient String query 	= "SELECT T.*, SP.label AS sp_label, SP.label_fr AS sp_label_fr, CP.label AS cp_label, CP.label_fr AS cp_label_fr, "
			+ " EV.label AS ev_label, EV.label_fr AS ev_label_fr, EV.id_type AS ev_id_type, TP1.label AS ev_tp_label, TP1.number AS ev_tp_number, "
			+ " SE.label AS se_label, SE.label_fr AS se_label_fr, SE.id_type AS se_id_type, TP2.label AS se_tp_label, TP2.number AS se_tp_number, "
			+ " SE2.label AS se2_label, SE2.label_fr AS se2_label_fr, SE2.id_type AS se2_id_type, TP3.label AS se2_tp_label, TP3.number AS se2_tp_number, "
			+ " CX1.label AS cx1_label, CT1.id AS cx1_id_city, CT1.label AS cx1_ct_label, CT1.label_fr AS cx1_ct_label_fr, CN1.id AS cx1_ct_id_country, CN1.label AS cx1_ct_cn_label, CN1.label_fr AS cx1_ct_cn_label_fr, CN1.code AS cx1_ct_cn_code, "
			+ " CX2.label AS cx2_label, CT2.id AS cx2_id_city, CT2.label AS cx2_ct_label, CT2.label_fr AS cx2_ct_label_fr, CN2.id AS cx2_ct_id_country, CN2.label AS cx2_ct_cn_label, CN2.label_fr AS cx2_ct_cn_label_fr, CN2.code AS cx2_ct_cn_code, "
			+ " CT3.label AS ct1_label, CT3.label_fr AS ct1_label_fr, CN3.id AS ct1_id_country, CN3.label AS ct1_cn_label, CN3.label_fr AS ct1_cn_label_fr, CN3.code AS ct1_cn_code, "
			+ " CT4.label AS ct2_label, CT4.label_fr AS ct2_label_fr, CN4.id AS ct2_id_country, CN4.label AS ct2_cn_label, CN4.label_fr AS ct2_cn_label_fr, CN4.code AS ct2_cn_code, "
			+ " CN5.label AS cn1_label, CN5.label_fr AS cn1_label_fr, CN5.code AS cn1_code, "
			+ " CN6.label AS cn2_label, CN6.label_fr AS cn2_label_fr, CN6.code AS cn2_code, "
			+ " YR.label AS yr_label "
			+ " FROM result T LEFT JOIN sport SP ON SP.id = T.id_sport "
			+ " LEFT JOIN championship CP ON CP.id = T.id_championship"
			+ " LEFT JOIN event EV ON EV.id = T.id_event"
			+ " LEFT JOIN event SE ON SE.id = T.id_subevent"
			+ " LEFT JOIN event SE2 ON SE2.id = T.id_subevent2"
			+ " LEFT JOIN type TP1 ON TP1.id = EV.id_type"
			+ " LEFT JOIN type TP2 ON TP2.id = SE.id_type"
			+ " LEFT JOIN type TP3 ON TP3.id = SE2.id_type"
			+ " LEFT JOIN complex CX1 ON CX1.id = T.id_complex1"
			+ " LEFT JOIN city CT1 ON CT1.id = CX1.id_city"
			+ " LEFT JOIN country CN1 ON CN1.id = CT1.id_country"
			+ " LEFT JOIN complex CX2 ON CX2.id = T.id_complex2"
			+ " LEFT JOIN city CT2 ON CT2.id = CX2.id_city"
			+ " LEFT JOIN country CN2 ON CN2.id = CT2.id_country"
			+ " LEFT JOIN city CT3 ON CT3.id = T.id_city1"
			+ " LEFT JOIN country CN3 ON CN3.id = CT3.id_country"
			+ " LEFT JOIN city CT4 ON CT4.id = T.id_city2"
			+ " LEFT JOIN country CN4 ON CN4.id = CT4.id_country"
			+ " LEFT JOIN country CN5 ON CN5.id = T.id_country1"
			+ " LEFT JOIN country CN6 ON CN6.id = T.id_country2"
			+ " LEFT JOIN year YR ON YR.id = T.id_year";
	
	public Result() {}
	
	public Result(Integer id) {
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
				setSubevent2(new Event(idSubevent2));
				getSubevent2().setValuesFromMap(extractEntityColumns("SE2", idSubevent2, mapValues));
			}
			Integer idCity1 = (Integer)mapValues.get("id_city1");
			if (idCity1 != null) {
				setCity1(new City());
				getCity1().setValuesFromMap(extractEntityColumns("CT1", idCity1, mapValues));
			}
			Integer idCity2 = (Integer)mapValues.get("id_city2");
			if (idCity2 != null) {
				setCity2(new City());
				getCity2().setValuesFromMap(extractEntityColumns("CT2", idCity2, mapValues));
			}
			Integer idComplex1 = (Integer)mapValues.get("id_complex1");
			if (idComplex1 != null) {
				setComplex1(new Complex());
				getComplex1().setValuesFromMap(extractEntityColumns("CX1", idComplex1, mapValues));
			}
			Integer idComplex2 = (Integer)mapValues.get("id_complex2");
			if (idComplex2 != null) {
				setComplex2(new Complex());
				getComplex2().setValuesFromMap(extractEntityColumns("CX2", idComplex2, mapValues));
			}
			Integer idCountry1 = (Integer)mapValues.get("id_country1");
			if (idCountry1 != null) {
				setCountry1(new Country());
				getCountry1().setValuesFromMap(extractEntityColumns("CN1", idCountry1, mapValues));
			}
			Integer idCountry2 = (Integer)mapValues.get("id_country2");
			if (idCountry2 != null) {
				setCountry2(new Country());
				getCountry2().setValuesFromMap(extractEntityColumns("CN2", idCountry2, mapValues));
			}
			Integer idYear = (Integer)mapValues.get("id_year");
			if (idYear != null) {
				setYear(new Year());
				getYear().setValuesFromMap(extractEntityColumns(Year.alias, idYear, mapValues));
			}
			setDate1((String)mapValues.get("date1"));
			setDate2((String)mapValues.get("date2"));
			setIdRank1((Integer)mapValues.get("id_rank1"));
			setIdRank2((Integer)mapValues.get("id_rank2"));
			setIdRank3((Integer)mapValues.get("id_rank3"));
			setIdRank4((Integer)mapValues.get("id_rank4"));
			setIdRank5((Integer)mapValues.get("id_rank5"));
			setIdRank6((Integer)mapValues.get("id_rank6"));
			setIdRank7((Integer)mapValues.get("id_rank7"));
			setIdRank8((Integer)mapValues.get("id_rank8"));
			setIdRank9((Integer)mapValues.get("id_rank9"));
			setIdRank10((Integer)mapValues.get("id_rank10"));
			setIdRank11((Integer)mapValues.get("id_rank11"));
			setIdRank12((Integer)mapValues.get("id_rank12"));
			setIdRank13((Integer)mapValues.get("id_rank13"));
			setIdRank14((Integer)mapValues.get("id_rank14"));
			setIdRank15((Integer)mapValues.get("id_rank15"));
			setIdRank16((Integer)mapValues.get("id_rank16"));
			setIdRank17((Integer)mapValues.get("id_rank17"));
			setIdRank18((Integer)mapValues.get("id_rank18"));
			setIdRank19((Integer)mapValues.get("id_rank19"));
			setIdRank20((Integer)mapValues.get("id_rank20"));
			setResult1((String)mapValues.get("result1"));
			setResult2((String)mapValues.get("result2"));
			setResult3((String)mapValues.get("result3"));
			setResult4((String)mapValues.get("result4"));
			setResult5((String)mapValues.get("result5"));
			setResult6((String)mapValues.get("result6"));
			setResult7((String)mapValues.get("result7"));
			setResult8((String)mapValues.get("result8"));
			setResult9((String)mapValues.get("result9"));
			setResult10((String)mapValues.get("result10"));
			setResult11((String)mapValues.get("result11"));
			setResult12((String)mapValues.get("result12"));
			setResult13((String)mapValues.get("result13"));
			setResult14((String)mapValues.get("result14"));
			setResult15((String)mapValues.get("result15"));
			setResult16((String)mapValues.get("result16"));
			setResult17((String)mapValues.get("result17"));
			setResult18((String)mapValues.get("result18"));
			setResult19((String)mapValues.get("result19"));
			setResult20((String)mapValues.get("result20"));
			setComment((String)mapValues.get("comment"));
			setExa((String)mapValues.get("exa"));
			setDraft((Boolean)mapValues.get("draft"));
			setNoPlace((Boolean)mapValues.get("no_place"));
			setNoDate((Boolean)mapValues.get("no_date"));
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

	public City getCity1() {
		return city1;
	}

	public City getCity2() {
		return city2;
	}

	public Complex getComplex1() {
		return complex1;
	}

	public Complex getComplex2() {
		return complex2;
	}

	public void setCity1(City city1) {
		this.city1 = city1;
	}

	public void setCity2(City city2) {
		this.city2 = city2;
	}

	public void setComplex1(Complex complex1) {
		this.complex1 = complex1;
	}

	public void setComplex2(Complex complex2) {
		this.complex2 = complex2;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
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

	public Integer getIdRank6() {
		return idRank6;
	}

	public void setIdRank6(Integer idRank6) {
		this.idRank6 = idRank6;
	}

	public Integer getIdRank7() {
		return idRank7;
	}

	public void setIdRank7(Integer idRank7) {
		this.idRank7 = idRank7;
	}

	public Integer getIdRank8() {
		return idRank8;
	}

	public void setIdRank8(Integer idRank8) {
		this.idRank8 = idRank8;
	}

	public Integer getIdRank9() {
		return idRank9;
	}

	public void setIdRank9(Integer idRank9) {
		this.idRank9 = idRank9;
	}

	public Integer getIdRank10() {
		return idRank10;
	}

	public void setIdRank10(Integer idRank10) {
		this.idRank10 = idRank10;
	}

	public String getResult1() {
		return result1;
	}

	public void setResult1(String result1) {
		this.result1 = result1;
	}

	public String getResult2() {
		return result2;
	}

	public void setResult2(String result2) {
		this.result2 = result2;
	}

	public String getResult3() {
		return result3;
	}

	public void setResult3(String result3) {
		this.result3 = result3;
	}

	public String getResult4() {
		return result4;
	}

	public void setResult4(String result4) {
		this.result4 = result4;
	}

	public String getResult5() {
		return result5;
	}

	public void setResult5(String result5) {
		this.result5 = result5;
	}

	public String getResult6() {
		return result6;
	}

	public String getResult7() {
		return result7;
	}

	public String getResult8() {
		return result8;
	}

	public String getResult9() {
		return result9;
	}

	public String getResult10() {
		return result10;
	}

	public void setResult6(String result6) {
		this.result6 = result6;
	}

	public void setResult7(String result7) {
		this.result7 = result7;
	}

	public void setResult8(String result8) {
		this.result8 = result8;
	}

	public void setResult9(String result9) {
		this.result9 = result9;
	}

	public void setResult10(String result10) {
		this.result10 = result10;
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

	public Event getSubevent2() {
		return subevent2;
	}

	public void setSubevent2(Event subevent2) {
		this.subevent2 = subevent2;
	}

	public Integer getIdRank11() {
		return idRank11;
	}

	public Integer getIdRank12() {
		return idRank12;
	}

	public Integer getIdRank13() {
		return idRank13;
	}

	public Integer getIdRank14() {
		return idRank14;
	}

	public Integer getIdRank15() {
		return idRank15;
	}

	public Integer getIdRank16() {
		return idRank16;
	}

	public Integer getIdRank17() {
		return idRank17;
	}

	public Integer getIdRank18() {
		return idRank18;
	}

	public Integer getIdRank19() {
		return idRank19;
	}

	public Integer getIdRank20() {
		return idRank20;
	}

	public String getResult11() {
		return result11;
	}

	public String getResult12() {
		return result12;
	}

	public String getResult13() {
		return result13;
	}

	public String getResult14() {
		return result14;
	}

	public String getResult15() {
		return result15;
	}

	public String getResult16() {
		return result16;
	}

	public String getResult17() {
		return result17;
	}

	public String getResult18() {
		return result18;
	}

	public String getResult19() {
		return result19;
	}

	public String getResult20() {
		return result20;
	}

	public void setIdRank11(Integer idRank11) {
		this.idRank11 = idRank11;
	}

	public void setIdRank12(Integer idRank12) {
		this.idRank12 = idRank12;
	}

	public void setIdRank13(Integer idRank13) {
		this.idRank13 = idRank13;
	}

	public void setIdRank14(Integer idRank14) {
		this.idRank14 = idRank14;
	}

	public void setIdRank15(Integer idRank15) {
		this.idRank15 = idRank15;
	}

	public void setIdRank16(Integer idRank16) {
		this.idRank16 = idRank16;
	}

	public void setIdRank17(Integer idRank17) {
		this.idRank17 = idRank17;
	}

	public void setIdRank18(Integer idRank18) {
		this.idRank18 = idRank18;
	}

	public void setIdRank19(Integer idRank19) {
		this.idRank19 = idRank19;
	}

	public void setIdRank20(Integer idRank20) {
		this.idRank20 = idRank20;
	}

	public void setResult11(String result11) {
		this.result11 = result11;
	}

	public void setResult12(String result12) {
		this.result12 = result12;
	}

	public void setResult13(String result13) {
		this.result13 = result13;
	}

	public void setResult14(String result14) {
		this.result14 = result14;
	}

	public void setResult15(String result15) {
		this.result15 = result15;
	}

	public void setResult16(String result16) {
		this.result16 = result16;
	}

	public void setResult17(String result17) {
		this.result17 = result17;
	}

	public void setResult18(String result18) {
		this.result18 = result18;
	}

	public void setResult19(String result19) {
		this.result19 = result19;
	}

	public void setResult20(String result20) {
		this.result20 = result20;
	}

	public Country getCountry1() {
		return country1;
	}

	public Country getCountry2() {
		return country2;
	}

	public void setCountry1(Country country1) {
		this.country1 = country1;
	}

	public void setCountry2(Country country2) {
		this.country2 = country2;
	}

	public Boolean getDraft() {
		return draft;
	}

	public void setDraft(Boolean draft) {
		if (draft == null) {
			draft = false;
		}
		this.draft = draft;
	}

	public Boolean getNoPlace() {
		return noPlace;
	}

	public Boolean getNoDate() {
		return noDate;
	}

	public void setNoPlace(Boolean noPlace) {
		this.noPlace = noPlace;
	}

	public void setNoDate(Boolean noDate) {
		this.noDate = noDate;
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
	
	public Integer getIdSubevent2() {
		return (subevent2 != null ? subevent2.getId() : null);
	}
	
	public Integer getIdComplex1() {
		return (complex1 != null ? complex1.getId() : null);
	}
	
	public Integer getIdCity1() {
		return (city1 != null ? city1.getId() : null);
	}
	
	public Integer getIdCountry1() {
		return (country1 != null ? country1.getId() : null);
	}
	
	public Integer getIdComplex2() {
		return (complex2 != null ? complex2.getId() : null);
	}
	
	public Integer getIdCity2() {
		return (city2 != null ? city2.getId() : null);
	}
	
	public Integer getIdCountry2() {
		return (country2 != null ? country2.getId() : null);
	}
	
	public Integer getIdYear() {
		return (year != null ? year.getId() : null);
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
	
	public void setIdSubevent2(Integer id) {
		subevent2 = (id != null && id > 0 ? new Event(id) : null);
	}
	
	public void setIdComplex1(Integer id) {
		complex1 = (id != null && id > 0 ? new Complex(id) : null);
	}
	
	public void setIdComplex2(Integer id) {
		complex2 = (id != null && id > 0 ? new Complex(id) : null);
	}
	
	public void setIdCity1(Integer id) {
		city1 = (id != null && id > 0 ? new City(id) : null);
	}
	
	public void setIdCity2(Integer id) {
		city2 = (id != null && id > 0 ? new City(id) : null);
	}
	
	public void setIdCountry1(Integer id) {
		country1 = (id != null && id > 0 ? new Country(id) : null);
	}
	
	public void setIdCountry2(Integer id) {
		country2 = (id != null && id > 0 ? new Country(id) : null);
	}
	
	public void setIdYear(Integer id) {
		year = (id != null && id > 0 ? new Year(id) : null);
	}
	
	@Override
	public String toString() {
		return sport + ", " + championship + (event != null ? ", " + event : "") + (subevent != null ? ", " + subevent : "") + ", " + year + (date1 != null ? ", " + date1 : "") + (date2 != null ? ", " + date2 : "") + ", 1. #" + idRank1 + ", 2. #" + idRank2 + ", 3. #" + idRank3 + " [#" + id + "]";
	}
	
	public String toString2(String lang) {
		return sport.getLabel(lang) + " - " + championship.getLabel(lang) + (event != null ? " - " + event.getLabel(lang) : "") + (subevent != null ? " - " + subevent.getLabel(lang) : "") + (subevent2 != null ? " - " + subevent2.getLabel(lang) : "") + " - " + year.getLabel();
	}
	
}