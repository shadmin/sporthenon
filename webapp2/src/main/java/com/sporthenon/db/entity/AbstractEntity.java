package com.sporthenon.db.entity;

import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public abstract class AbstractEntity {

	private Metadata metadata;
	
	@Override
	public abstract String toString();
	
	public abstract void setValuesFromMap(Map<String, Object> mapValues);
	
	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
}