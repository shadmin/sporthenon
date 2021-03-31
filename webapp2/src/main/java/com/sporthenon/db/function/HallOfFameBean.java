package com.sporthenon.db.function;

import java.sql.Timestamp;

public class HallOfFameBean {

	private Integer hfId;
	private Timestamp hfLastUpdate;
	private Integer lgId;
	private Integer yrId;
	private String yrLabel;
	private Integer prId;
	private String prLastName;
	private String prFirstName;
	private String hfPosition;

	public Integer getHfId() {
		return hfId;
	}

	public void setHfId(Integer hfId) {
		this.hfId = hfId;
	}

	public Integer getYrId() {
		return yrId;
	}

	public void setYrId(Integer yrId) {
		this.yrId = yrId;
	}

	public String getYrLabel() {
		return yrLabel;
	}

	public void setYrLabel(String yrLabel) {
		this.yrLabel = yrLabel;
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

	public String getHfPosition() {
		return hfPosition;
	}

	public void setHfPosition(String hfPosition) {
		this.hfPosition = hfPosition;
	}

	public Integer getLgId() {
		return lgId;
	}

	public void setLgId(Integer lgId) {
		this.lgId = lgId;
	}

	public Timestamp getHfLastUpdate() {
		return hfLastUpdate;
	}

	public void setHfLastUpdate(Timestamp hfLastUpdate) {
		this.hfLastUpdate = hfLastUpdate;
	}

	@Override
	public String toString() {
		return "HallOfFameBean [hfPosition=" + hfPosition + ", hfId=" + hfId
				+ ", prFirstName=" + prFirstName + ", prId=" + prId
				+ ", prLastName=" + prLastName + ", yrId=" + yrId
				+ ", yrLabel=" + yrLabel + "]";
	}
	
}