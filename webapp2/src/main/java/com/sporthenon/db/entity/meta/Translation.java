package com.sporthenon.db.entity.meta;

import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class Translation extends AbstractEntity {
	
	private Integer id;
	private String 	entity;
	private Integer idItem;
	private Boolean checked;
	
	public static final transient String table 	= "_translation";
	public static final transient String key 	= "id";
	public static final transient String cols 	= "entity,id_item,checked";
	
	public Translation() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setEntity((String)mapValues.get("entity"));
			setIdItem((Integer)mapValues.get("id_item"));
			setChecked((Boolean)mapValues.get("checked"));
		}
	}
	
	public Integer getId() {
		return id;
	}


	public String getEntity() {
		return entity;
	}


	public Integer getIdItem() {
		return idItem;
	}


	public Boolean getChecked() {
		return checked;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public void setEntity(String entity) {
		this.entity = entity;
	}


	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}


	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public boolean isChecked() {
		return (checked != null && checked);
	}

	@Override
	public String toString() {
		return "Translation [id=" + id + ", entity=" + entity + ", idItem=" + idItem + ", checked=" + checked + "]";
	}
	
}