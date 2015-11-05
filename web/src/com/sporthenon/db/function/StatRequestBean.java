package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StatRequestBean {

	@Id
	@Column(name = "key")
	private String key;
	
	@Column(name = "value")
	private Integer value;

	public String getKey() {
		return key;
	}

	public Integer getValue() {
		return value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
}