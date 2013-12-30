package com.sporthenon.db.entity.meta;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class Metadata {

	@ManyToOne
	@JoinColumn(name = "id_member", nullable = false)
	private Member member;
	
	@Column(name = "first_update", nullable = false)
	private Timestamp firstUpdate;

	@Column(name = "last_update", nullable = false)
	private Timestamp lastUpdate;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
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
		return "Metadata [member=" + member + ", firstUpdate=" + firstUpdate + ", lastUpdate=" + lastUpdate + "]";
	}
	
}