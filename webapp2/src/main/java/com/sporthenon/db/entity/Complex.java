package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class Complex extends AbstractEntity {
	
	private Integer id;
	private String label;
	private City city;
	private Integer link;
	private Integer ref;
	
	public static final transient String alias = "CX";
	public static final transient String table = "complex";
	public static final transient String key = 	 "id";
	
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
				setCity(new City(idCity));	
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

	@Override
	public String toString() {
		return label + (city != null ? ", " + city : "") + " [#" + id + "]";
	}
	
	public String toString2(String lang) {
		return getLabel() + (city != null ? ", " + city.toString2(lang) : "");
	}
	
}