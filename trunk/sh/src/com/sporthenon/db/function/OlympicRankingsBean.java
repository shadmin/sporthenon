package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OlympicRankingsBean {

	@Id
	@Column(name = "or_id")
	private Integer orId;
	
	@Column(name = "ol_id")
	private Integer olId;
	
	@Column(name = "ol_type")
	private Integer olType;
	
	@Column(name = "ol_date1")
	private String olDate1;
	
	@Column(name = "ol_date2")
	private String olDate2;

	@Column(name = "yr_id")
	private Integer yrId;

	@Column(name = "yr_label")
	private String yrLabel;
	
	@Column(name = "ct_id")
	private Integer ctId;

	@Column(name = "ct_label")
	private String ctLabel;
	
	@Column(name = "st_id")
	private Integer stId;
	
	@Column(name = "st_code")
	private String stCode;

	@Column(name = "st_label")
	private String stLabel;
	
	@Column(name = "cn1_id")
	private Integer cn1Id;
	
	@Column(name = "cn1_code")
	private String cn1Code;

	@Column(name = "cn1_label")
	private String cn1Label;
	
	@Column(name = "cn2_id")
	private Integer cn2Id;
	
	@Column(name = "cn2_code")
	private String cn2Code;

	@Column(name = "cn2_label")
	private String cn2Label;

	@Column(name = "or_count_gold")
	private Integer orCountGold;
	
	@Column(name = "or_count_silver")
	private Integer orCountSilver;
	
	@Column(name = "or_count_bronze")
	private Integer orCountBronze;

	public Integer getOrId() {
		return orId;
	}

	public void setOrId(Integer orId) {
		this.orId = orId;
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

	public Integer getCtId() {
		return ctId;
	}

	public void setCtId(Integer ctId) {
		this.ctId = ctId;
	}

	public String getCtLabel() {
		return ctLabel;
	}

	public void setCtLabel(String ctLabel) {
		this.ctLabel = ctLabel;
	}

	public Integer getStId() {
		return stId;
	}

	public void setStId(Integer stId) {
		this.stId = stId;
	}

	public String getStCode() {
		return stCode;
	}

	public void setStCode(String stCode) {
		this.stCode = stCode;
	}

	public String getStLabel() {
		return stLabel;
	}

	public void setStLabel(String stLabel) {
		this.stLabel = stLabel;
	}

	public Integer getCn1Id() {
		return cn1Id;
	}

	public void setCn1Id(Integer cn1Id) {
		this.cn1Id = cn1Id;
	}

	public String getCn1Code() {
		return cn1Code;
	}

	public void setCn1Code(String cn1Code) {
		this.cn1Code = cn1Code;
	}

	public String getCn1Label() {
		return cn1Label;
	}

	public void setCn1Label(String cn1Label) {
		this.cn1Label = cn1Label;
	}

	public Integer getCn2Id() {
		return cn2Id;
	}

	public void setCn2Id(Integer cn2Id) {
		this.cn2Id = cn2Id;
	}

	public String getCn2Code() {
		return cn2Code;
	}

	public void setCn2Code(String cn2Code) {
		this.cn2Code = cn2Code;
	}

	public String getCn2Label() {
		return cn2Label;
	}

	public void setCn2Label(String cn2Label) {
		this.cn2Label = cn2Label;
	}

	public Integer getOrCountGold() {
		return orCountGold;
	}

	public void setOrCountGold(Integer orCountGold) {
		this.orCountGold = orCountGold;
	}

	public Integer getOrCountSilver() {
		return orCountSilver;
	}

	public void setOrCountSilver(Integer orCountSilver) {
		this.orCountSilver = orCountSilver;
	}

	public Integer getOrCountBronze() {
		return orCountBronze;
	}

	public void setOrCountBronze(Integer orCountBronze) {
		this.orCountBronze = orCountBronze;
	}

	public Integer getOlId() {
		return olId;
	}

	public void setOlId(Integer olId) {
		this.olId = olId;
	}

	public Integer getOlType() {
		return olType;
	}

	public void setOlType(Integer olType) {
		this.olType = olType;
	}

	public String getOlDate1() {
		return olDate1;
	}

	public void setOlDate1(String olDate1) {
		this.olDate1 = olDate1;
	}

	public String getOlDate2() {
		return olDate2;
	}

	public void setOlDate2(String olDate2) {
		this.olDate2 = olDate2;
	}

	@Override
	public String toString() {
		return "OlympicRankingsBean [cn1Code=" + cn1Code + ", cn1Id=" + cn1Id
				+ ", cn1Label=" + cn1Label + ", cn2Code=" + cn2Code
				+ ", cn2Id=" + cn2Id + ", cn2Label=" + cn2Label + ", ctId="
				+ ctId + ", ctLabel=" + ctLabel + ", olDate1=" + olDate1
				+ ", olDate2=" + olDate2 + ", olId=" + olId + ", olType="
				+ olType + ", orCountBronze=" + orCountBronze
				+ ", orCountGold=" + orCountGold + ", orCountSilver="
				+ orCountSilver + ", orId=" + orId + ", stCode=" + stCode
				+ ", stId=" + stId + ", stLabel=" + stLabel + ", yrId=" + yrId
				+ ", yrLabel=" + yrLabel + "]";
	}
	
}
