package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class Olympics extends AbstractEntity {
	
	private Integer id;
	private Year 	year;
	private City 	city;
	private Integer countCountry;
	private Integer countPerson;
	private Integer countSport;
	private Integer countEvent;
	private String 	date1;
	private String 	date2;
	private Integer type;
	private Integer ref;
	private Boolean nopic;
	
	public static final transient String alias 	= "OL";
	public static final transient String table 	= "olympics";
	public static final transient String key 	= "id";
	public static final transient String query 	= "SELECT T.*, YR.label AS yr_label, ct.label AS ct_label, ct.label_fr AS ct_label_fr "
			+ " FROM olympics T LEFT JOIN city CT ON CT.id = T.id_city "
			+ " LEFT JOIN year YR ON YR.id = T.id_year";
	
	public Olympics() {}
	
	public Olympics(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			Integer idYear = (Integer)mapValues.get("id_year");
			if (idYear != null) {
				setYear(new Year());
				getYear().setValuesFromMap(extractEntityColumns(Year.alias, idYear, mapValues));
			}
			Integer idCity = (Integer)mapValues.get("id_city");
			if (idCity != null) {
				setCity(new City());
				getCity().setValuesFromMap(extractEntityColumns(City.alias, idCity, mapValues));
			}
			setCountCountry((Integer)mapValues.get("count_country"));
			setCountPerson((Integer)mapValues.get("count_person"));
			setCountSport((Integer)mapValues.get("count_sport"));
			setCountEvent((Integer)mapValues.get("count_event"));
			setDate1((String)mapValues.get("date1"));
			setDate2((String)mapValues.get("date2"));
			setType((Integer)mapValues.get("type"));
			setRef((Integer)mapValues.get("ref"));
			setNopic((Boolean)mapValues.get("no_pic"));
			setMetadata(new Metadata((Integer)mapValues.get("id_contributor"), (Timestamp)mapValues.get("first_update"), (Timestamp)mapValues.get("last_update")));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Integer getCountCountry() {
		return countCountry;
	}

	public void setCountCountry(Integer countCountry) {
		this.countCountry = countCountry;
	}

	public Integer getCountPerson() {
		return countPerson;
	}

	public void setCountPerson(Integer countPerson) {
		this.countPerson = countPerson;
	}

	public Integer getCountSport() {
		return countSport;
	}

	public void setCountSport(Integer countSport) {
		this.countSport = countSport;
	}

	public Integer getCountEvent() {
		return countEvent;
	}

	public void setCountEvent(Integer countEvent) {
		this.countEvent = countEvent;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRef() {
		return ref;
	}

	public void setRef(Integer ref) {
		this.ref = ref;
	}

	public Boolean getNopic() {
		return nopic;
	}

	public void setNopic(Boolean nopic) {
		this.nopic = nopic;
	}
	
	@Override
	public String toString() {
		return "Olympics [id=" + id + ", year=" + year + ", city=" + city
				+ ", countCountry=" + countCountry + ", countPerson="
				+ countPerson + ", countSport=" + countSport + ", countEvent="
				+ countEvent + ", date1=" + date1 + ", date2=" + date2
				+ ", type=" + type + "]";
	}
	
	public String toString2(String lang) {
		return getYear().getLabel() + " " + getCity().getLabel(lang);
	}
	
}