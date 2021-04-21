package com.sporthenon.db.entity.meta;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class FolderHistory extends AbstractEntity {

	private Integer   id;
	private String 	  previousParams;
	private String 	  currentParams;
	private String 	  currentPath;
	private Timestamp date;

	public static final transient String alias 	= "FH";
	public static final transient String table 	= "_folder_history";
	public static final transient String cols 	= "previous_params,current_params,current_path,date";
	
	public FolderHistory() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setPreviousParams((String)mapValues.get("previous_params"));
			setCurrentParams((String)mapValues.get("current_params"));
			setCurrentPath((String)mapValues.get("current_path"));
			setDate((Timestamp)mapValues.get("date"));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public String getPreviousParams() {
		return previousParams;
	}

	public String getCurrentParams() {
		return currentParams;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPreviousParams(String previousParams) {
		this.previousParams = previousParams;
	}

	public void setCurrentParams(String currentParams) {
		this.currentParams = currentParams;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "FolderHistory [id=" + id + ", previousParams=" + previousParams + ", currentParams=" + currentParams
				+ ", currentPath=" + currentPath + ", date=" + date + "]";
	}

}