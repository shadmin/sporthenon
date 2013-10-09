package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class USChampionshipsBean {

	@Id
	@Column(name = "rs_id")
	private Integer rsId;
	
	@Column(name = "rs_date1")
	private String rsDate1;
	
	@Column(name = "rs_date2")
	private String rsDate2;
	
	@Column(name = "rs_rank1")
	private Integer rsRank1;
	
	@Column(name = "rs_rank2")
	private Integer rsRank2;
	
	@Column(name = "rs_result")
	private String rsResult;
	
	@Column(name = "rs_comment")
	private String rsComment;
	
	@Column(name = "yr_id")
	private Integer yrId;

	@Column(name = "yr_label")
	private String yrLabel;
	
	@Column(name = "cx_id")
	private Integer cxId;

	@Column(name = "cx_label")
	private String cxLabel;
	
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
	
	@Column(name = "cn_id")
	private Integer cnId;
	
	@Column(name = "cn_code")
	private String cnCode;

	@Column(name = "cn_label")
	private String cnLabel;
	
	@Column(name = "rs_team1")
	private String rsTeam1;
	
	@Column(name = "rs_team2")
	private String rsTeam2;

	public Integer getRsId() {
		return rsId;
	}

	public void setRsId(Integer rsId) {
		this.rsId = rsId;
	}

	public String getRsDate1() {
		return rsDate1;
	}

	public void setRsDate1(String rsDate1) {
		this.rsDate1 = rsDate1;
	}

	public String getRsDate2() {
		return rsDate2;
	}

	public void setRsDate2(String rsDate2) {
		this.rsDate2 = rsDate2;
	}

	public Integer getRsRank1() {
		return rsRank1;
	}

	public void setRsRank1(Integer rsRank1) {
		this.rsRank1 = rsRank1;
	}

	public Integer getRsRank2() {
		return rsRank2;
	}

	public void setRsRank2(Integer rsRank2) {
		this.rsRank2 = rsRank2;
	}

	public String getRsResult() {
		return rsResult;
	}

	public void setRsResult(String rsResult) {
		this.rsResult = rsResult;
	}

	public String getRsComment() {
		return rsComment;
	}

	public void setRsComment(String rsComment) {
		this.rsComment = rsComment;
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

	public Integer getCxId() {
		return cxId;
	}

	public void setCxId(Integer cxId) {
		this.cxId = cxId;
	}

	public String getCxLabel() {
		return cxLabel;
	}

	public void setCxLabel(String cxLabel) {
		this.cxLabel = cxLabel;
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

	public Integer getCnId() {
		return cnId;
	}

	public void setCnId(Integer cnId) {
		this.cnId = cnId;
	}

	public String getCnCode() {
		return cnCode;
	}

	public void setCnCode(String cnCode) {
		this.cnCode = cnCode;
	}

	public String getCnLabel() {
		return cnLabel;
	}

	public void setCnLabel(String cnLabel) {
		this.cnLabel = cnLabel;
	}

	public String getRsTeam1() {
		return rsTeam1;
	}

	public void setRsTeam1(String rsTeam1) {
		this.rsTeam1 = rsTeam1;
	}

	public String getRsTeam2() {
		return rsTeam2;
	}

	public void setRsTeam2(String rsTeam2) {
		this.rsTeam2 = rsTeam2;
	}

	@Override
	public String toString() {
		return "USChampionshipsBean [cnCode=" + cnCode + ", cnId=" + cnId
				+ ", cnLabel=" + cnLabel + ", ctId=" + ctId + ", ctLabel="
				+ ctLabel + ", cxId=" + cxId + ", cxLabel=" + cxLabel
				+ ", rsComment=" + rsComment + ", rsDate1=" + rsDate1
				+ ", rsDate2=" + rsDate2 + ", rsId=" + rsId + ", rsRank1="
				+ rsRank1 + ", rsRank2=" + rsRank2 + ", rsResult=" + rsResult
				+ ", rsTeam1=" + rsTeam1 + ", rsTeam2=" + rsTeam2 + ", stCode="
				+ stCode + ", stId=" + stId + ", stLabel=" + stLabel
				+ ", yrId=" + yrId + ", yrLabel=" + yrLabel + "]";
	}
	
}
