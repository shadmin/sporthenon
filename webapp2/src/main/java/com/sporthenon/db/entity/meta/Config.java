package com.sporthenon.db.entity.meta;

import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class Config extends AbstractEntity {
	
	private String key_;
	private String value;
	private String valueHtml;
	
	public static final transient String alias 	= "CG";
	public static final transient String table 	= "_config";
	public static final transient String key 	= "key";
	public static final transient String cols 	= "key,value,value_html";

	public Config() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setKey((String)mapValues.get("key"));
			setValue((String)mapValues.get("value"));
			setValueHtml((String)mapValues.get("value_html"));
		}
	}
	
	public String getKey() {
		return key_;
	}

	public String getValue() {
		return value;
	}

	public void setKey(String key_) {
		this.key_ = key_;
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
		return "Config [key_=" + key_ + ", value=" + value + ", valueHtml=" + valueHtml + "]";
	}
	
}