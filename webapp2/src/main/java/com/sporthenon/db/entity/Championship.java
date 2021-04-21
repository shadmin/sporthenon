package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class Championship extends AbstractEntity {
	
	private Integer id;
	private String 	label;
	private String 	labelFR;
	private Double 	index;
	private Integer ref;
	private Boolean nopic;
	
	public static final transient String alias 	= "CP";
	public static final transient String table 	= "championship";
	public static final transient String cols 	= "label,label_fr,index,ref,no_pic";
	
	public Championship() {}
	
	public Championship(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setLabel((String)mapValues.get("label"));
			setLabelFr((String)mapValues.get("label_fr"));
			setIndex((Double)mapValues.get("index"));
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		this.labelFR = label;
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

	public Double getIndex() {
		return index;
	}

	public void setIndex(Double index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return label + " [#" + id + "]";
	}

}