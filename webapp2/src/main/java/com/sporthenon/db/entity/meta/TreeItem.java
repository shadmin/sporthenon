package com.sporthenon.db.entity.meta;

import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class TreeItem extends AbstractEntity {
	
	private Integer id;
	private Integer idItem;
	private String 	label;
	private String 	labelEN;
	private Integer level;
	
	public static final transient String table 	= "_tree_item";
	public static final transient String key 	= "id";
	public static final transient String cols 	= "id_item,label,label_en,level";

	public TreeItem() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setIdItem((Integer)mapValues.get("id_item"));
			setLabel((String)mapValues.get("label"));
			setLabelEN((String)mapValues.get("label_en"));
			setLevel((Integer)mapValues.get("level"));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdItem() {
		return idItem;
	}

	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}

	public String getStdLabel() {
		return this.label;
	}
	
	public String getLabel() {
		return (label != null && label.contains("'") ? label.replaceAll("'", "\\\\'") : label);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getLabelEN() {
		return labelEN;
	}

	public void setLabelEN(String labelEN) {
		this.labelEN = labelEN;
	}

	public String toString() {
		return "TreeItem [id=" + id + ", idItem=" + idItem + ", label=" + label + ", level=" + level + "]";
	}

}
