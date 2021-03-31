package com.sporthenon.db.entity.meta;

import java.sql.Timestamp;

public class Metadata {

	private Contributor contributor;
	private Timestamp firstUpdate;
	private Timestamp lastUpdate;

	public Metadata() {
	}

	public Metadata(Integer idContributor, Timestamp firstUpdate, Timestamp lastUpdate) {
		this.contributor = new Contributor(idContributor);
		this.firstUpdate = firstUpdate;
		this.lastUpdate = lastUpdate;
	}
	
	public Contributor getContributor() {
		return contributor;
	}

	public void setContributor(Contributor contributor) {
		this.contributor = contributor;
	}

	public Timestamp getFirstUpdate() {
		return firstUpdate;
	}

	public void setFirstUpdate(Timestamp firstUpdate) {
		this.firstUpdate = firstUpdate;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "Metadata [contributor=" + contributor + ", firstUpdate=" + firstUpdate + ", lastUpdate=" + lastUpdate + "]";
	}
	
}