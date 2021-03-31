package com.sporthenon.db.function;

import java.sql.Timestamp;

public class RetiredNumberBean {

	private Integer rnId;
	private Timestamp rnLastUpdate;
	private Integer tmId;
	private String tmLabel;
	private Integer prId;
	private String prLastName;
	private String prFirstName;
	private Integer yrId;
	private String yrLabel;
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

	public Integer getYrId() {
		return yrId;
	}

	public String getYrLabel() {
		return yrLabel;
	}

	public void setYrId(Integer yrId) {
		this.yrId = yrId;
	}

	public void setYrLabel(String yrLabel) {
		this.yrLabel = yrLabel;
	}

	public Timestamp getRnLastUpdate() {
		return rnLastUpdate;
	}

	public void setRnLastUpdate(Timestamp rnLastUpdate) {
		this.rnLastUpdate = rnLastUpdate;
	}

	@Override
	public String toString() {
		return "RetiredNumberBean [prFirstName=" + prFirstName + ", prId="
				+ prId + ", prLastName=" + prLastName + ", rnId=" + rnId
				+ ", rnNumber=" + rnNumber + ", tmId="
				+ tmId + ", tmLabel=" + tmLabel + "]";
	}
	
}