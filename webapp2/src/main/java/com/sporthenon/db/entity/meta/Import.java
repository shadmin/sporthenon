package com.sporthenon.db.entity.meta;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class Import extends AbstractEntity {
	
	private Integer   id;
	private Timestamp date;
	private String 	  csvContent;
	
	public static final transient String table 	= "_import";
	public static final transient String cols = "date,csv_content";

	public Import() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setDate((Timestamp)mapValues.get("date"));
			setCsvContent((String)mapValues.get("csv_content"));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public Timestamp getDate() {
		return date;
	}

	public String getCsvContent() {
		return csvContent;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public void setCsvContent(String csvContent) {
		this.csvContent = csvContent;
	}

	@Override
	public String toString() {
		return "Import [id=" + id + ", date=" + date + ", csvContent=" + csvContent + "]";
	}

}