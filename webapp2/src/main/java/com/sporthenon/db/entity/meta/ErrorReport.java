package com.sporthenon.db.entity.meta;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class ErrorReport extends AbstractEntity {

	private Integer   id;
	private String 	  url;
	private String 	  text;
	private Timestamp date;

	public static final transient String alias 	= "ER";
	public static final transient String table 	= "_error_report";
	public static final transient String key 	= "id";
	
	public ErrorReport() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setUrl((String)mapValues.get("url"));
			setText((String)mapValues.get("text"));
			setDate((Timestamp)mapValues.get("date"));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getText() {
		return text;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ErrorReport [id=" + id + ", url=" + url + ", text=" + text + ", date=" + date + "]";
	}

}