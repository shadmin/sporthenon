package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MedalCountBean {

	@Id
	@Column(name = "count_gold")
	private Integer countGold;
	
	@Column(name = "count_silver")
	private Integer countSilver;
	
	@Column(name = "count_bronze")
	private Integer countBronze;

	public Integer getCountGold() {
		return countGold;
	}

	public Integer getCountSilver() {
		return countSilver;
	}

	public Integer getCountBronze() {
		return countBronze;
	}

	public void setCountGold(Integer countGold) {
		this.countGold = countGold;
	}

	public void setCountSilver(Integer countSilver) {
		this.countSilver = countSilver;
	}

	public void setCountBronze(Integer countBronze) {
		this.countBronze = countBronze;
	}
	
}