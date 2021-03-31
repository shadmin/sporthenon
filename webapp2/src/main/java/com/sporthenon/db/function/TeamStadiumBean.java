package com.sporthenon.db.function;

import java.sql.Timestamp;

public class TeamStadiumBean {

	private Integer tsId;
	private Timestamp tsLastUpdate;
	private Integer tmId;
	private String tmLabel;
	private Boolean tsRenamed;
	private String tsComment;
	private Integer cxId;
	private String cxLabel;
	private Integer ctId;
	private String ctLabel;
	private String ctLabelEN;
	private Integer stId;
	private String stCode;
	private String stLabel;
	private String stLabelEN;
	private Integer cnId;
	private String cnCode;
	private String cnLabel;
	private String cnLabelEN;
	private Integer tsDate1;
	private Integer tsDate2;

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

	public String getCtLabelEN() {
		return ctLabelEN;
	}

	public String getStLabelEN() {
		return stLabelEN;
	}

	public String getCnLabelEN() {
		return cnLabelEN;
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

	public Timestamp getTsLastUpdate() {
		return tsLastUpdate;
	}

	public void setTsLastUpdate(Timestamp tsLastUpdate) {
		this.tsLastUpdate = tsLastUpdate;
	}

	public Integer getTsDate1() {
		return tsDate1;
	}

	public void setTsDate1(Integer tsDate1) {
		this.tsDate1 = tsDate1;
	}

	public Integer getTsDate2() {
		return tsDate2;
	}

	public void setTsDate2(Integer tsDate2) {
		this.tsDate2 = tsDate2;
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
