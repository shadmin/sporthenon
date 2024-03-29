package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class Event extends AbstractEntity {
	
	private Integer id;
	private String 	label;
	private String 	labelFR;
	private Double 	index;
	private Type 	type;
	private Integer ref;
	private Boolean nopic;
	
	public static final transient String alias 	= "EV";
	public static final transient String table 	= "event";
	public static final transient String cols 	= "label,label_fr,index,id_type,ref,no_pic";
	public static final transient String query 	= "SELECT T.*, TP.label AS tp_label, TP.label_fr AS tp_label_fr, TP.number AS tp_number "
			+ " FROM event T LEFT JOIN type TP ON TP.id = T.id_type";
	
	public Event() {}
	
	public Event(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setLabel((String)mapValues.get("label"));
			setLabelFr((String)mapValues.get("label_fr"));
			setIndex((Double)mapValues.get("index"));
			Integer idType = (Integer)mapValues.get("id_type");
			if (idType != null) {
				setType(new Type(idType));
				getType().setLabel((String)mapValues.get("tp_label"));
				getType().setLabelFr((String)mapValues.get("tp_label_fr"));
				getType().setNumber((Integer)mapValues.get("tp_number"));
			}
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
	}

	public String getLabelFr() {
		return labelFR;
	}

	public void setLabelFr(String labelFr) {
		this.labelFR = labelFr;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	public Integer getIdType() {
		return (type != null ? type.getId() : null);
	}
	
	public void setIdType(Integer id) {
		type = (id != null && id > 0 ? new Type(id) : null);
	}
	
	@Override
	public String toString() {
		return label + " [#" + id + "]";
	}
	
}