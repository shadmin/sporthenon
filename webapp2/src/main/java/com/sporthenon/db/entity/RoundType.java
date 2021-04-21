package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class RoundType extends AbstractEntity {

	private Integer id;
	private String 	label;
	private String 	labelFR;
	private Double 	index;
	
	public static final transient String alias 	= "RT";
	public static final transient String table 	= "round_type";
	public static final transient String cols 	= "label,label_fr,index";
	
	public RoundType() {}
	
	public RoundType(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setLabel((String)mapValues.get("label"));
			setLabelFr((String)mapValues.get("label_fr"));
			setIndex((Double)mapValues.get("index"));
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

	public Double getIndex() {
		return index;
	}

	public void setIndex(Double index) {
		this.index = index;
	}

	public String getLabel(String lang) {
		return (lang != null && lang.equalsIgnoreCase("fr") ? labelFR : label);
	}

	@Override
	public String toString() {
		return label + " [#" + id + "]";
	}
	
}