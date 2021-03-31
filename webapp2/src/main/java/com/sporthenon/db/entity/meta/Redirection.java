package com.sporthenon.db.entity.meta;

import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class Redirection extends AbstractEntity {

	private Integer id;
	private String previousPath;
	private String currentPath;

	public static final transient String alias = "RE";
	public static final transient String table = "_redirection";
	public static final transient String key = 	 "id";
	
	public Redirection() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setPreviousPath((String)mapValues.get("previous_path"));
			setCurrentPath((String)mapValues.get("current_path"));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public String getPreviousPath() {
		return previousPath;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPreviousPath(String previousPath) {
		this.previousPath = previousPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	@Override
	public String toString() {
		return "Redirection [id=" + id + ", previousPath=" + previousPath + ", currentPath=" + currentPath + "]";
	}

}