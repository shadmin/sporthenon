package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class Country extends AbstractEntity {
	
	private Integer id;
	private String code;
	private String label;
	private String labelFR;
	private Integer ref;
	private Boolean nopic;
	
	public static final transient String alias = "CN";
	public static final transient String table = "country";
	public static final transient String key = 	 "id";
	
	public Country() {
	}
	
	public Country(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setCode((String)mapValues.get("code"));
			setLabel((String)mapValues.get("label"));
			setLabelFr((String)mapValues.get("label_fr"));
			setRef((Integer)mapValues.get("ref"));
			setNopic((Boolean)mapValues.get("no_pic"));
			setMetadata(new Metadata((Integer)mapValues.get("id_contributor"), (Timestamp)mapValues.get("first_update"), (Timestamp)mapValues.get("last_update")));
		}
	}

	public Country(String code) {
		this.code = code;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getLabel(String lang) {
		return (lang != null && lang.equalsIgnoreCase("fr") ? labelFR : label);
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
	public boolean equals(Object obj) {
		return (obj instanceof Country && ((Country) obj).getCode().equals(code));
	}

	@Override
	public String toString() {
		return label + ", " + code + " [#" + id + "]";
	}
	
	public String toString2(String lang) {
		return getLabel(lang) + " (" + code + ")";
	}
	
}