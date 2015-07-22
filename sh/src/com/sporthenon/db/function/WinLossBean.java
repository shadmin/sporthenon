package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WinLossBean {

	@Id
	@Column(name = "wl_id")
	private Integer wlId;
	
	@Column(name = "tm_id")
	private Integer tmId;
	
	@Column(name = "tm_label")
	private String tmLabel;
	
	@Column(name = "wl_type")
	private String wlType;

	@Column(name = "wl_count_win")
	private Integer wlCountWin;
	
	@Column(name = "wl_count_loss")
	private Integer wlCountLoss;
	
	@Column(name = "wl_count_tie")
	private Integer wlCountTie;
	
	@Column(name = "wl_count_otloss")
	private Integer wlCountOtloss;
	
	public Integer getWlId() {
		return wlId;
	}

	public void setWlId(Integer wlId) {
		this.wlId = wlId;
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

	public String getWlType() {
		return wlType;
	}

	public void setWlType(String wlType) {
		this.wlType = wlType;
	}

	public Integer getWlCountWin() {
		return wlCountWin;
	}

	public void setWlCountWin(Integer wlCountWin) {
		this.wlCountWin = wlCountWin;
	}

	public Integer getWlCountLoss() {
		return wlCountLoss;
	}

	public void setWlCountLoss(Integer wlCountLoss) {
		this.wlCountLoss = wlCountLoss;
	}

	public Integer getWlCountTie() {
		return wlCountTie;
	}

	public void setWlCountTie(Integer wlCountTie) {
		this.wlCountTie = wlCountTie;
	}

	public Integer getWlCountOtloss() {
		return wlCountOtloss;
	}

	public void setWlCountOtloss(Integer wlCountOtloss) {
		this.wlCountOtloss = wlCountOtloss;
	}

	@Override
	public String toString() {
		return "WinLossBean [tmId=" + tmId
				+ ", tmLabel=" + tmLabel + ", wlCountLoss=" + wlCountLoss + ", wlCountOtloss="
				+ wlCountOtloss + ", wlCountTie=" + wlCountTie
				+ ", wlCountWin=" + wlCountWin + ", wlId=" + wlId + ", wlType="
				+ wlType + "]";
	}
	
}