package com.sporthenon.db.entity.meta;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class Metadata {

	@ManyToOne
	@JoinColumn(name = "id_contributor", nullable = false)
	private Contributor contributor;
	
	@Column(name = "first_update", nullable = false)
	private Timestamp firstUpdate;

	@Column(name = "last_update", nullable = false)
	private Timestamp lastUpdate;

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