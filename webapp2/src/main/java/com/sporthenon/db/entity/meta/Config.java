package com.sporthenon.db.entity.meta;

import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class Config extends AbstractEntity {
	
	private Integer	id;
	private String 	key;
	private String 	value;
	private String 	valueHtml;
	
	public static final transient String alias 	= "CG";
	public static final transient String table 	= "_config";
	public static final transient String cols 	= "key,value,value_html";

	public Config() {}
	
	public Config(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setKey((String)mapValues.get("key"));
			setValue((String)mapValues.get("value"));
			setValueHtml((String)mapValues.get("value_html"));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueHtml() {
		return valueHtml;
	}

	public void setValueHtml(String valueHtml) {
		this.valueHtml = valueHtml;
	}

	@Override
	public String toString() {
		return "Config [key_=" + key + ", value=" + value + ", valueHtml=" + valueHtml + "]";
	}
	
}