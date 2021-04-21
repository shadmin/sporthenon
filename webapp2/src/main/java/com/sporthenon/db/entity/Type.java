package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class Type extends AbstractEntity {

	private Integer id;
	private String 	label;
	private String 	labelFR;
	private Integer number;
	
	public static final transient String alias 	= "TP";
	public static final transient String table 	= "type";
	public static final transient String cols 	= "label,label_fr,number";
	
	public Type() {}
	
	public Type(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setLabel((String)mapValues.get("label"));
			setLabelFr((String)mapValues.get("label_fr"));
			setNumber((Integer)mapValues.get("number"));
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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public String getLabel(String lang) {
		return (lang != null && lang.equalsIgnoreCase("fr") ? labelFR : label);
	}

	@Override
	public String toString() {
		return label + " [#" + id + "]";
	}
	
}