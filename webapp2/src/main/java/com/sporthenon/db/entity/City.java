package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class City extends AbstractEntity {

	private Integer id;
	private String 	label;
	private String 	labelFR;
	private Country country;
	private State 	state;
	private Integer link;
	private Integer ref;
	
	public static final transient String alias 	= "CT";
	public static final transient String table 	= "city";
	public static final transient String key 	= "id";
	public static final transient String cols 	= "label,label_fr,id_country,id_state,link,ref";
	public static final transient String query 	= "SELECT T.*, CN.code AS cn_code, CN.label AS cn_label, CN.label_fr AS cn_label_fr, "
			+ " ST.code AS st_code, ST.label AS st_label, ST.label_fr AS st_label_fr "
			+ " FROM city T LEFT JOIN country CN ON CN.id = T.id_country "
			+ " LEFT JOIN state ST ON ST.id = T.id_state";
	
	public City() {}
	
	public City(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setLabel((String)mapValues.get("label"));
			setLabelFr((String)mapValues.get("label_fr"));
			Integer idState = (Integer)mapValues.get("id_state");
			if (idState != null) {
				setState(new State());
				getState().setValuesFromMap(extractEntityColumns(State.alias, idState, mapValues));
			}
			Integer idCountry = (Integer)mapValues.get("id_country");
			if (idCountry != null) {
				setCountry(new Country());
				getCountry().setValuesFromMap(extractEntityColumns(Country.alias, idCountry, mapValues));
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabelFr() {
		return labelFR;
	}

	public void setLabelFr(String labelFr) {
		this.labelFR = labelFr;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getLabel(String lang) {
		return (lang != null && lang.equalsIgnoreCase("fr") ? labelFR : label);
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

	public Integer getIdCountry() {
		return (country != null ? country.getId() : null);
	}
	
	public Integer getIdState() {
		return (state != null ? state.getId() : null);
	}
	
	public String toString() {
		return label + (country != null ? ", " + country : "") + " [#" + id + "]";
	}
	
	public String toString2(String lang) {
		return getLabel(lang) + (country != null ? ", " + country.getCode() : "");
	}

}