package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class State extends AbstractEntity {
	
	private Integer id;
	private String 	code;
	private String 	label;
	private String 	labelFR;
	private String 	capital;
	private Integer ref;
	private Boolean nopic;
	
	public static final transient String alias 	= "ST";
	public static final transient String table 	= "state";
	public static final transient String key 	= "id";
	
	public State() {}
	
	public State(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setCode((String)mapValues.get("code"));
			setLabel((String)mapValues.get("label"));
			setLabelFr((String)mapValues.get("label_fr"));
			setCapital((String)mapValues.get("capital"));
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

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getLabel(String lang) {
		return (lang != null && lang.equalsIgnoreCase("fr") ? labelFR : label);
	}
	
	public Integer getRef() {
		return ref;
	}
	
	public Boolean getNopic() {
		return nopic;
	}

	public void setNopic(Boolean nopic) {
		this.nopic = nopic;
	}

	public void setRef(Integer ref) {
		this.ref = ref;
	}

	@Override
	public String toString() {
		return label + " [#" + id + "]";
	}
	
}