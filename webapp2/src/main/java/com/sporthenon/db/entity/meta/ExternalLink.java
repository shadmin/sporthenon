package com.sporthenon.db.entity.meta;

import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class ExternalLink extends AbstractEntity {
	
	private Integer   id;
	private String 	  entity;
	private Integer   idItem;
	private String 	  url;
	private Boolean   checked;
	private Character flag;
	
	public static final transient String table 	= "_external_link";
	public static final transient String key 	= "id";
	public static final transient String cols 	= "entity,id_item,url,checked,flag";
	
	public ExternalLink() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setEntity((String)mapValues.get("entity"));
			setIdItem((Integer)mapValues.get("id_item"));
			setUrl((String)mapValues.get("url"));
			setChecked((Boolean)mapValues.get("checked"));
			setFlag(String.valueOf(mapValues.get("flag")).charAt(0));
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

	public String getUrl() {
		return url;
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

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
	public Character getFlag() {
		return flag;
	}

	public void setFlag(Character flag) {
		this.flag = flag;
	}

	public boolean isChecked() {
		return (checked != null && checked);
	}

	@Override
	public String toString() {
		return "ExternalLink [id=" + id + ", entity=" + entity + ", idItem="
				+ idItem + ", url=" + url + ", checked=" + checked + ", flag="
				+ flag + "]";
	}
	
}