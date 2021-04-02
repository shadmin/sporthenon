package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;
import com.sporthenon.utils.StringUtils;

public class Athlete extends AbstractEntity {
	
	private Integer id;
	private String 	lastName;
	private String 	firstName;
	private Country country;
	private Team 	team;
	private Sport 	sport;
	private Integer link;
	private Integer ref;
	
	public static final transient String alias 	= "PR";
	public static final transient String table 	= "athlete";
	public static final transient String key 	= "id";
	public static final transient String query 	= "SELECT T.*, CN.code AS cn_code, CN.label AS cn_label, CN.label_fr AS cn_label_fr, "
			+ " SP.label AS sp_label, SP.label_fr AS sp_label_fr, SP.type AS sp_type, TM.label AS tm_label "
			+ " FROM athlete T LEFT JOIN country CN ON CN.id = T.id_country "
			+ " LEFT JOIN team TM ON TM.id = T.id_team"
			+ " LEFT JOIN sport SP ON SP.id = T.id_sport";
	
	public Athlete() {}
	
	public Athlete(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setLastName((String)mapValues.get("last_name"));
			setFirstName((String)mapValues.get("first_name"));
			Integer idCountry = (Integer)mapValues.get("id_country");
			if (idCountry != null) {
				setCountry(new Country());
				getCountry().setValuesFromMap(extractEntityColumns(Country.alias, idCountry, mapValues));
			}
			Integer idTeam = (Integer)mapValues.get("id_team");
			if (idTeam != null) {
				setTeam(new Team());
				getTeam().setValuesFromMap(extractEntityColumns(Team.alias, idTeam, mapValues));
			}
			Integer idSport = (Integer)mapValues.get("id_sport");
			if (idSport != null) {
				setSport(new Sport());
				getSport().setValuesFromMap(extractEntityColumns(Sport.alias, idSport, mapValues));
			}
			setLink((Integer)mapValues.get("link"));
			setRef((Integer)mapValues.get("ref"));
			setMetadata(new Metadata((Integer)mapValues.get("id_contributor"), (Timestamp)mapValues.get("first_update"), (Timestamp)mapValues.get("last_update")));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public Integer getLink() {
		return link;
	}

	public void setLink(Integer link) {
		this.link = link;
	}

	public Integer getRef() {
		return ref;
	}

	public void setRef(Integer ref) {
		this.ref = ref;
	}

	@Override
	public String toString() {
		return lastName + ", " + firstName + (team != null ? ", " + team : "") + (country != null ? ", " + country : "") + (sport != null ? ", " + sport : "") + " [#" + id + "]";
	}
	
	public String toString2() {
		return lastName + (StringUtils.notEmpty(firstName) ? ", " + firstName : "") + (country != null ? " (" + country.getCode() + (team != null ? ", " + team.getLabel() : "") + ")" : "");
	}
	
}