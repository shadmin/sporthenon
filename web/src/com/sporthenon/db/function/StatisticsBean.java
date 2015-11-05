package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StatisticsBean {

	@Id
	@Column(name = "count_city")
	private Integer countCity;
	
	@Column(name = "count_complex")
	private Integer countComplex;
	
	@Column(name = "count_country")
	private Integer countCountry;
	
	@Column(name = "count_event")
	private Integer countEvent;
	
	@Column(name = "count_person")
	private Integer countPerson;
	
	@Column(name = "count_result")
	private Integer countResult;
	
	@Column(name = "count_sport")
	private Integer countSport;
	
	@Column(name = "count_team")
	private Integer countTeam;

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

	public Integer getCountCity() {
		return countCity;
	}

	public Integer getCountComplex() {
		return countComplex;
	}

	public Integer getCountCountry() {
		return countCountry;
	}

	public Integer getCountTeam() {
		return countTeam;
	}

	public void setCountCity(Integer countCity) {
		this.countCity = countCity;
	}

	public void setCountComplex(Integer countComplex) {
		this.countComplex = countComplex;
	}

	public void setCountCountry(Integer countCountry) {
		this.countCountry = countCountry;
	}

	public void setCountTeam(Integer countTeam) {
		this.countTeam = countTeam;
	}
	
}