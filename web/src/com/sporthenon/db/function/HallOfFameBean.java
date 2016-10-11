package com.sporthenon.db.function;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class HallOfFameBean {

	@Id
	@Column(name = "hf_id")
	private Integer hfId;
	
	@Column(name = "hf_last_update")
	private Timestamp hfLastUpdate;
	
	@Column(name = "lg_id")
	private Integer lgId;
	
	@Column(name = "yr_id")
	private Integer yrId;

	@Column(name = "yr_label")
	private String yrLabel;
	
	@Column(name = "pr_id")
	private Integer prId;

	@Column(name = "pr_last_name")
	private String prLastName;
	
	@Column(name = "pr_first_name")
	private String prFirstName;
	
	@Column(name = "hf_position")
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