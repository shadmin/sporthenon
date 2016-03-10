package com.sporthenon.db.entity.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"~Config\"")
public class Config {
	
	@Id
	private String key;

	@Column(name = "value")
	private String value;
	
	@Column(name = "value_html")
	private String valueHtml;

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
	
}