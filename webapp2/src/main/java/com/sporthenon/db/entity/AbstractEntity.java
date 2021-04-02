package com.sporthenon.db.entity;

import java.util.HashMap;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public abstract class AbstractEntity {

	private Metadata metadata;
	
	@Override
	public abstract String toString();
	
	public abstract void setValuesFromMap(Map<String, Object> mapValues);
	
	protected Map<String, Object> extractEntityColumns(String alias, Integer id, Map<String, Object> mapValues) {
		Map<String, Object> mapResults = new HashMap<>();
		mapResults.put("id", id);
		String alias_ = alias.toLowerCase() + "_";
		for (String key : mapValues.keySet()) {
			if (key.toLowerCase().startsWith(alias_)) {
				mapResults.put(key.replace(alias_, ""), mapValues.get(key));
			}
		}
		return mapResults;
	}
	
	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
}