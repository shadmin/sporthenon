package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TeamStadiumBean {

	@Id
	@Column(name = "ts_id")
	private Integer tsId;
	
	@Column(name = "tm_id")
	private Integer tmId;
	
	@Column(name = "tm_label")
	private String tmLabel;
	
	@Column(name = "ts_renamed")
	private Boolean tsRenamed;
	
	@Column(name = "ts_comment")
	private String tsComment;
	
	@Column(name = "cx_id")
	private Integer cxId;

	@Column(name = "cx_label")
	private String cxLabel;
	
	@Column(name = "cx_label_en")
	private String cxLabelEN;
	
	@Column(name = "ct_id")
	private Integer ctId;

	@Column(name = "ct_label")
	private String ctLabel;
	
	@Column(name = "ct_label_en")
	private String ctLabelEN;
	
	@Column(name = "st_id")
	private Integer stId;

	@Column(name = "st_code")
	private String stCode;
	
	@Column(name = "st_label")
	private String stLabel;
	
	@Column(name = "st_label_en")
	private String stLabelEN;
	
	@Column(name = "cn_id")
	private Integer cnId;

	@Column(name = "cn_code")
	private String cnCode;
	
	@Column(name = "cn_label")
	private String cnLabel;
	
	@Column(name = "cn_label_en")
	private String cnLabelEN;
	
	@Column(name = "ts_date1")
	private String tsDate1;
	
	@Column(name = "ts_date2")
	private String tsDate2;

	public Integer getTsId() {
		return tsId;
	}

	public void setTsId(Integer tsId) {
		this.tsId = tsId;
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

	public Boolean getTsRenamed() {
		return tsRenamed;
	}

	public void setTsRenamed(Boolean tsRenamed) {
		this.tsRenamed = tsRenamed;
	}

	public String getTsComment() {
		return tsComment;
	}

	public void setTsComment(String tsComment) {
		this.tsComment = tsComment;
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

	public String getTsDate1() {
		return tsDate1;
	}

	public void setTsDate1(String tsDate1) {
		this.tsDate1 = tsDate1;
	}

	public String getTsDate2() {
		return tsDate2;
	}

	public void setTsDate2(String tsDate2) {
		this.tsDate2 = tsDate2;
	}

	public String getCxLabelEN() {
		return cxLabelEN;
	}

	public String getCtLabelEN() {
		return ctLabelEN;
	}

	public String getStLabelEN() {
		return stLabelEN;
	}

	public String getCnLabelEN() {
		return cnLabelEN;
	}

	public void setCxLabelEN(String cxLabelEN) {
		this.cxLabelEN = cxLabelEN;
	}

	public void setCtLabelEN(String ctLabelEN) {
		this.ctLabelEN = ctLabelEN;
	}

	public void setStLabelEN(String stLabelEN) {
		this.stLabelEN = stLabelEN;
	}

	public void setCnLabelEN(String cnLabelEN) {
		this.cnLabelEN = cnLabelEN;
	}

	@Override
	public String toString() {
		return "TeamStadiumBean [cnCode=" + cnCode + ", cnId=" + cnId
				+ ", cnLabel=" + cnLabel + ", ctId=" + ctId + ", ctLabel="
				+ ctLabel + ", cxId=" + cxId + ", cxLabel=" + cxLabel
				+ ", stCode=" + stCode + ", stId=" + stId + ", stLabel="
				+ stLabel + ", tmId=" + tmId
				+ ", tmLabel=" + tmLabel + ", tsComment=" + tsComment
				+ ", tsDate1=" + tsDate1 + ", tsDate2=" + tsDate2 + ", tsId="
				+ tsId + ", tsRenamed=" + tsRenamed + "]";
	}
	
}
