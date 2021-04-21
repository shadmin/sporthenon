package com.sporthenon.db.entity.meta;

import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class Picture extends AbstractEntity {
	
	private Integer id;
	private String 	entity;
	private Integer idItem;
	private String 	value;
	private String 	source;
	private Boolean embedded;
	
	public static final transient String table 	= "_picture";
	public static final transient String cols 	= "entity,id_item,value,source,embedded";
	
	public Picture() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setEntity((String)mapValues.get("entity"));
			setIdItem((Integer)mapValues.get("id_item"));
			setValue((String)mapValues.get("value"));
			setSource((String)mapValues.get("source"));
			setEmbedded((Boolean)mapValues.get("embedded"));
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

	public String getValue() {
		return value;
	}

	public String getSource() {
		return source;
	}

	public Boolean getEmbedded() {
		return embedded;
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

	public void setValue(String value) {
		this.value = value;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setEmbedded(Boolean embedded) {
		this.embedded = embedded;
	}
	
	public boolean isEmbedded() {
		return (embedded != null && embedded);
	}

	@Override
	public String toString() {
		return "Picture [id=" + id + ", entity=" + entity + ", idItem="
				+ idItem + ", value=" + value + ", source=" + source
				+ ", embedded=" + embedded + "]";
	}
	
}