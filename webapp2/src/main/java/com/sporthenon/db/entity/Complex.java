package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class Complex extends AbstractEntity {
	
	private Integer id;
	private String 	label;
	private City 	city;
	private Integer link;
	private Integer ref;
	
	public static final transient String alias 	= "CX";
	public static final transient String table 	= "complex";
	public static final transient String key 	= "id";
	public static final transient String cols 	= "label,id_city,link,ref";
	public static final transient String query 	= "SELECT T.*, CT.label AS ct_label, CT.label_fr AS ct_label_fr, "
			+ " CN.id AS ct_id_country, CN.code AS ct_cn_code, CN.label AS ct_cn_label, CN.label_fr AS ct_cn_label_fr, "
			+ " ST.id AS ct_id_state, ST.code AS ct_st_code, ST.label AS ct_st_label, ST.label_fr AS ct_st_label_fr "
			+ " FROM complex T LEFT JOIN city CT ON CT.id = T.id_city "
			+ " LEFT JOIN country CN ON CN.id = CT.id_country "
			+ " LEFT JOIN state ST ON ST.id = CT.id_state";
	
	public Complex() {}
	
	public Complex(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setLabel((String)mapValues.get("label"));
			Integer idCity = (Integer)mapValues.get("id_city");
			if (idCity != null) {
				setCity(new City());
				getCity().setValuesFromMap(extractEntityColumns(City.alias, idCity, mapValues));
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

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
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

	public Integer getIdCity() {
		return (city != null ? city.getId() : null);
	}
	
	@Override
	public String toString() {
		return label + (city != null ? ", " + city : "") + " [#" + id + "]";
	}
	
	public String toString2(String lang) {
		return getLabel() + (city != null ? ", " + city.toString2(lang) : "");
	}
	
}