package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PersonListBean {

	@Id
	@Column(name = "pl_id")
	private Integer plId;
	
	@Column(name = "rs_id")
	private Integer rsId;
	
	@Column(name = "pl_rank")
	private Integer plRank;
	
	@Column(name = "pr_id")
	private Integer prId;
	
	@Column(name = "pr_last_name")
	private String prLastName;
	
	@Column(name = "pr_first_name")
	private String prFirstName;
	
	@Column(name = "pr_country_id")
	private Integer prCountryId;
	
	@Column(name = "pr_country_code")
	private String prCountryCode;

	public Integer getPlId() {
		return plId;
	}

	public Integer getPlRank() {
		return plRank;
	}

	public Integer getPrId() {
		return prId;
	}

	public String getPrLastName() {
		return prLastName;
	}

	public String getPrFirstName() {
		return prFirstName;
	}

	public Integer getPrCountryId() {
		return prCountryId;
	}

	public String getPrCountryCode() {
		return prCountryCode;
	}

	public void setPlId(Integer plId) {
		this.plId = plId;
	}

	public void setPlRank(Integer plRank) {
		this.plRank = plRank;
	}

	public void setPrId(Integer prId) {
		this.prId = prId;
	}

	public void setPrLastName(String prLastName) {
		this.prLastName = prLastName;
	}

	public void setPrFirstName(String prFirstName) {
		this.prFirstName = prFirstName;
	}

	public void setPrCountryId(Integer prCountryId) {
		this.prCountryId = prCountryId;
	}

	public void setPrCountryCode(String prCountryCode) {
		this.prCountryCode = prCountryCode;
	}

	public Integer getRsId() {
		return rsId;
	}

	public void setRsId(Integer rsId) {
		this.rsId = rsId;
	}
	
}