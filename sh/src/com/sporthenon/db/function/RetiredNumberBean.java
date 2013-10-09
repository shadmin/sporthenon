package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RetiredNumberBean {

	@Id
	@Column(name = "rn_id")
	private Integer rnId;
	
	@Column(name = "tm_id")
	private Integer tmId;

	@Column(name = "tm_code")
	private String tmCode;
	
	@Column(name = "tm_label")
	private String tmLabel;
	
	@Column(name = "pr_id")
	private Integer prId;

	@Column(name = "pr_last_name")
	private String prLastName;
	
	@Column(name = "pr_first_name")
	private String prFirstName;
	
	@Column(name = "rn_number")
	private Integer rnNumber;

	public Integer getRnId() {
		return rnId;
	}

	public void setRnId(Integer rnId) {
		this.rnId = rnId;
	}

	public Integer getTmId() {
		return tmId;
	}

	public void setTmId(Integer tmId) {
		this.tmId = tmId;
	}

	public String getTmCode() {
		return tmCode;
	}

	public void setTmCode(String tmCode) {
		this.tmCode = tmCode;
	}

	public String getTmLabel() {
		return tmLabel;
	}

	public void setTmLabel(String tmLabel) {
		this.tmLabel = tmLabel;
	}

	public Integer getPrId() {
		return prId;
	}

	public void setPrId(Integer prId) {
		this.prId = prId;
	}

	public String getPrLastName() {
		return prLastName;
	}

	public void setPrLastName(String prLastName) {
		this.prLastName = prLastName;
	}

	public String getPrFirstName() {
		return prFirstName;
	}

	public void setPrFirstName(String prFirstName) {
		this.prFirstName = prFirstName;
	}

	public Integer getRnNumber() {
		return rnNumber;
	}

	public void setRnNumber(Integer rnNumber) {
		this.rnNumber = rnNumber;
	}

	@Override
	public String toString() {
		return "RetiredNumberBean [prFirstName=" + prFirstName + ", prId="
				+ prId + ", prLastName=" + prLastName + ", rnId=" + rnId
				+ ", rnNumber=" + rnNumber + ", tmCode=" + tmCode + ", tmId="
				+ tmId + ", tmLabel=" + tmLabel + "]";
	}
	
}
