package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StatisticsBean {

	@Id
	@Column(name = "count_sport")
	private Integer countSport;
	
	@Column(name = "count_event")
	private Integer countEvent;
	
	@Column(name = "count_result")
	private Integer countResult;
	
	@Column(name = "count_person")
	private Integer countPerson;

	public Integer getCountSport() {
		return countSport;
	}

	public Integer getCountEvent() {
		return countEvent;
	}

	public Integer getCountResult() {
		return countResult;
	}

	public Integer getCountPerson() {
		return countPerson;
	}

	public void setCountSport(Integer countSport) {
		this.countSport = countSport;
	}

	public void setCountEvent(Integer countEvent) {
		this.countEvent = countEvent;
	}

	public void setCountResult(Integer countResult) {
		this.countResult = countResult;
	}

	public void setCountPerson(Integer countPerson) {
		this.countPerson = countPerson;
	}
	
}
