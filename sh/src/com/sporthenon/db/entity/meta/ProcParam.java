package com.sporthenon.db.entity.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"~PROC_PARAM\"")
public class ProcParam {
	
	@Id
	private int id;
	
	@Column(name = "\"name\"", length = 20, nullable = false)
	private String name;
	
	@Column(name = "int_value", nullable = false)
	private int intValue;
	
	@Column(name = "char_value", length = 100, nullable = false)
	private String charValue;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public String getCharValue() {
		return charValue;
	}

	public void setCharValue(String charValue) {
		this.charValue = charValue;
	}

}
