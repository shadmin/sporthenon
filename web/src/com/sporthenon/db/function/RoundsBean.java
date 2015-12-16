package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RoundsBean {

	@Id
	@Column(name = "rd_id")
	private Integer rdId;
	
	@Column(name = "rt_id")
	private Integer rtId;
	
	@Column(name = "rd_result_type")
	private Integer rdResultType;
	
	@Column(name = "rt_label")
	private String rtLabel;
	
	@Column(name = "rt_index")
	private Integer rtIndex;
	
	@Column(name = "rd_result1")
	private String rdResult1;
	
	@Column(name = "rd_result2")
	private String rdResult2;
	
	@Column(name = "rd_result3")
	private String rdResult3;
	
	@Column(name = "rd_date")
	private String rdDate;
	
	@Column(name = "rd_exa")
	private String rdExa;
	
	@Column(name = "rd_comment")
	private String rdComment;
	
	@Column(name = "cx_id")
	private Integer cxId;

	@Column(name = "cx_label")
	private String cxLabel;
	
	@Column(name = "cx_label_en")
	private String cxLabelEN;
	
	@Column(name = "ct1_id")
	private Integer ct1Id;

	@Column(name = "ct1_label")
	private String ct1Label;
	
	@Column(name = "ct1_label_en")
	private String ct1LabelEN;
	
	@Column(name = "st1_id")
	private Integer st1Id;
	
	@Column(name = "st1_code")
	private String st1Code;

	@Column(name = "st1_label_en")
	private String st1LabelEN;
	
	@Column(name = "cn1_id")
	private Integer cn1Id;
	
	@Column(name = "cn1_code")
	private String cn1Code;

	@Column(name = "cn1_label_en")
	private String cn1LabelEN;
	
	@Column(name = "ct2_id")
	private Integer ct2Id;

	@Column(name = "ct2_label")
	private String ct2Label;

	@Column(name = "ct2_label_en")
	private String ct2LabelEN;
	
	@Column(name = "st2_id")
	private Integer st2Id;
	
	@Column(name = "st2_code")
	private String st2Code;

	@Column(name = "st2_label_en")
	private String st2LabelEN;
	
	@Column(name = "cn2_id")
	private Integer cn2Id;
	
	@Column(name = "cn2_code")
	private String cn2Code;

	@Column(name = "cn2_label_en")
	private String cn2LabelEN;
	
	@Column(name = "rk1_id")
	private Integer rk1Id;
	
	@Column(name = "rk1_str1")
	private String rk1Str1;
	
	@Column(name = "rk1_str2")
	private String rk1Str2;
	
	@Column(name = "rk1_str3")
	private String rk1Str3;
	
	@Column(name = "rk1_rel1_id")
	private Integer rk1Rel1Id;
	
	@Column(name = "rk1_rel1_code")
	private String rk1Rel1Code;
	
	@Column(name = "rk1_rel1_label")
	private String rk1Rel1Label;
	
	@Column(name = "rk1_rel1_label_en")
	private String rk1Rel1LabelEN;
	
	@Column(name = "rk1_rel2_id")
	private Integer rk1Rel2Id;
	
	@Column(name = "rk1_rel2_code")
	private String rk1Rel2Code;
	
	@Column(name = "rk1_rel2_label")
	private String rk1Rel2Label;
	
	@Column(name = "rk1_rel2_label_en")
	private String rk1Rel2LabelEN;
	
	@Column(name = "rk2_id")
	private Integer rk2Id;
	
	@Column(name = "rk2_str1")
	private String rk2Str1;
	
	@Column(name = "rk2_str2")
	private String rk2Str2;
	
	@Column(name = "rk2_str3")
	private String rk2Str3;
	
	@Column(name = "rk2_rel1_id")
	private Integer rk2Rel1Id;
	
	@Column(name = "rk2_rel1_code")
	private String rk2Rel1Code;
	
	@Column(name = "rk2_rel1_label")
	private String rk2Rel1Label;
	
	@Column(name = "rk2_rel1_label_en")
	private String rk2Rel1LabelEN;
	
	@Column(name = "rk2_rel2_id")
	private Integer rk2Rel2Id;
	
	@Column(name = "rk2_rel2_code")
	private String rk2Rel2Code;
	
	@Column(name = "rk2_rel2_label")
	private String rk2Rel2Label;
	
	@Column(name = "rk2_rel2_label_en")
	private String rk2Rel2LabelEN;
	
	@Column(name = "rk3_id")
	private Integer rk3Id;
	
	@Column(name = "rk3_str1")
	private String rk3Str1;
	
	@Column(name = "rk3_str2")
	private String rk3Str2;
	
	@Column(name = "rk3_str3")
	private String rk3Str3;
	
	@Column(name = "rk3_rel1_id")
	private Integer rk3Rel1Id;
	
	@Column(name = "rk3_rel1_code")
	private String rk3Rel1Code;
	
	@Column(name = "rk3_rel1_label")
	private String rk3Rel1Label;
	
	@Column(name = "rk3_rel1_label_en")
	private String rk3Rel1LabelEN;
	
	@Column(name = "rk3_rel2_id")
	private Integer rk3Rel2Id;
	
	@Column(name = "rk3_rel2_code")
	private String rk3Rel2Code;
	
	@Column(name = "rk3_rel2_label")
	private String rk3Rel2Label;
	
	@Column(name = "rk3_rel2_label_en")
	private String rk3Rel2LabelEN;

	public Integer getRdId() {
		return rdId;
	}

	public Integer getRtId() {
		return rtId;
	}

	public String getRtLabel() {
		return rtLabel;
	}

	public String getRdResult1() {
		return rdResult1;
	}

	public String getRdResult2() {
		return rdResult2;
	}

	public String getRdResult3() {
		return rdResult3;
	}

	public String getRdDate() {
		return rdDate;
	}

	public String getRdExa() {
		return rdExa;
	}

	public String getRdComment() {
		return rdComment;
	}

	public Integer getCxId() {
		return cxId;
	}

	public String getCxLabel() {
		return cxLabel;
	}

	public String getCxLabelEN() {
		return cxLabelEN;
	}

	public Integer getCt1Id() {
		return ct1Id;
	}

	public String getCt1Label() {
		return ct1Label;
	}

	public String getCt1LabelEN() {
		return ct1LabelEN;
	}

	public Integer getSt1Id() {
		return st1Id;
	}

	public String getSt1Code() {
		return st1Code;
	}

	public String getSt1LabelEN() {
		return st1LabelEN;
	}

	public Integer getCn1Id() {
		return cn1Id;
	}

	public String getCn1Code() {
		return cn1Code;
	}

	public String getCn1LabelEN() {
		return cn1LabelEN;
	}

	public Integer getCt2Id() {
		return ct2Id;
	}

	public String getCt2Label() {
		return ct2Label;
	}

	public String getCt2LabelEN() {
		return ct2LabelEN;
	}

	public Integer getSt2Id() {
		return st2Id;
	}

	public String getSt2Code() {
		return st2Code;
	}

	public String getSt2LabelEN() {
		return st2LabelEN;
	}

	public Integer getCn2Id() {
		return cn2Id;
	}

	public String getCn2Code() {
		return cn2Code;
	}

	public String getCn2LabelEN() {
		return cn2LabelEN;
	}

	public Integer getRk1Id() {
		return rk1Id;
	}

	public String getRk1Str1() {
		return rk1Str1;
	}

	public String getRk1Str2() {
		return rk1Str2;
	}

	public String getRk1Str3() {
		return rk1Str3;
	}

	public Integer getRk1Rel1Id() {
		return rk1Rel1Id;
	}

	public String getRk1Rel1Code() {
		return rk1Rel1Code;
	}

	public String getRk1Rel1Label() {
		return rk1Rel1Label;
	}

	public String getRk1Rel1LabelEN() {
		return rk1Rel1LabelEN;
	}

	public Integer getRk1Rel2Id() {
		return rk1Rel2Id;
	}

	public String getRk1Rel2Code() {
		return rk1Rel2Code;
	}

	public String getRk1Rel2Label() {
		return rk1Rel2Label;
	}

	public String getRk1Rel2LabelEN() {
		return rk1Rel2LabelEN;
	}

	public Integer getRk2Id() {
		return rk2Id;
	}

	public String getRk2Str1() {
		return rk2Str1;
	}

	public String getRk2Str2() {
		return rk2Str2;
	}

	public String getRk2Str3() {
		return rk2Str3;
	}

	public Integer getRk2Rel1Id() {
		return rk2Rel1Id;
	}

	public String getRk2Rel1Code() {
		return rk2Rel1Code;
	}

	public String getRk2Rel1Label() {
		return rk2Rel1Label;
	}

	public String getRk2Rel1LabelEN() {
		return rk2Rel1LabelEN;
	}

	public Integer getRk2Rel2Id() {
		return rk2Rel2Id;
	}

	public String getRk2Rel2Code() {
		return rk2Rel2Code;
	}

	public String getRk2Rel2Label() {
		return rk2Rel2Label;
	}

	public String getRk2Rel2LabelEN() {
		return rk2Rel2LabelEN;
	}

	public Integer getRk3Id() {
		return rk3Id;
	}

	public String getRk3Str1() {
		return rk3Str1;
	}

	public String getRk3Str2() {
		return rk3Str2;
	}

	public String getRk3Str3() {
		return rk3Str3;
	}

	public Integer getRk3Rel1Id() {
		return rk3Rel1Id;
	}

	public String getRk3Rel1Code() {
		return rk3Rel1Code;
	}

	public String getRk3Rel1Label() {
		return rk3Rel1Label;
	}

	public String getRk3Rel1LabelEN() {
		return rk3Rel1LabelEN;
	}

	public Integer getRk3Rel2Id() {
		return rk3Rel2Id;
	}

	public String getRk3Rel2Code() {
		return rk3Rel2Code;
	}

	public String getRk3Rel2Label() {
		return rk3Rel2Label;
	}

	public String getRk3Rel2LabelEN() {
		return rk3Rel2LabelEN;
	}

	public void setRdId(Integer rdId) {
		this.rdId = rdId;
	}

	public void setRtId(Integer rtId) {
		this.rtId = rtId;
	}

	public void setRtLabel(String rtLabel) {
		this.rtLabel = rtLabel;
	}

	public void setRdResult1(String rdResult1) {
		this.rdResult1 = rdResult1;
	}

	public void setRdResult2(String rdResult2) {
		this.rdResult2 = rdResult2;
	}

	public void setRdResult3(String rdResult3) {
		this.rdResult3 = rdResult3;
	}

	public void setRdDate(String rdDate) {
		this.rdDate = rdDate;
	}

	public void setRdExa(String rdExa) {
		this.rdExa = rdExa;
	}

	public void setRdComment(String rdComment) {
		this.rdComment = rdComment;
	}

	public void setCxId(Integer cxId) {
		this.cxId = cxId;
	}

	public void setCxLabel(String cxLabel) {
		this.cxLabel = cxLabel;
	}

	public void setCxLabelEN(String cxLabelEN) {
		this.cxLabelEN = cxLabelEN;
	}

	public void setCt1Id(Integer ct1Id) {
		this.ct1Id = ct1Id;
	}

	public void setCt1Label(String ct1Label) {
		this.ct1Label = ct1Label;
	}

	public void setCt1LabelEN(String ct1LabelEN) {
		this.ct1LabelEN = ct1LabelEN;
	}

	public void setSt1Id(Integer st1Id) {
		this.st1Id = st1Id;
	}

	public void setSt1Code(String st1Code) {
		this.st1Code = st1Code;
	}

	public void setSt1LabelEN(String st1LabelEN) {
		this.st1LabelEN = st1LabelEN;
	}

	public void setCn1Id(Integer cn1Id) {
		this.cn1Id = cn1Id;
	}

	public void setCn1Code(String cn1Code) {
		this.cn1Code = cn1Code;
	}

	public void setCn1LabelEN(String cn1LabelEN) {
		this.cn1LabelEN = cn1LabelEN;
	}

	public void setCt2Id(Integer ct2Id) {
		this.ct2Id = ct2Id;
	}

	public void setCt2Label(String ct2Label) {
		this.ct2Label = ct2Label;
	}

	public void setCt2LabelEN(String ct2LabelEN) {
		this.ct2LabelEN = ct2LabelEN;
	}

	public void setSt2Id(Integer st2Id) {
		this.st2Id = st2Id;
	}

	public void setSt2Code(String st2Code) {
		this.st2Code = st2Code;
	}

	public void setSt2LabelEN(String st2LabelEN) {
		this.st2LabelEN = st2LabelEN;
	}

	public void setCn2Id(Integer cn2Id) {
		this.cn2Id = cn2Id;
	}

	public void setCn2Code(String cn2Code) {
		this.cn2Code = cn2Code;
	}

	public void setCn2LabelEN(String cn2LabelEN) {
		this.cn2LabelEN = cn2LabelEN;
	}

	public void setRk1Id(Integer rk1Id) {
		this.rk1Id = rk1Id;
	}

	public void setRk1Str1(String rk1Str1) {
		this.rk1Str1 = rk1Str1;
	}

	public void setRk1Str2(String rk1Str2) {
		this.rk1Str2 = rk1Str2;
	}

	public void setRk1Str3(String rk1Str3) {
		this.rk1Str3 = rk1Str3;
	}

	public void setRk1Rel1Id(Integer rk1Rel1Id) {
		this.rk1Rel1Id = rk1Rel1Id;
	}

	public void setRk1Rel1Code(String rk1Rel1Code) {
		this.rk1Rel1Code = rk1Rel1Code;
	}

	public void setRk1Rel1Label(String rk1Rel1Label) {
		this.rk1Rel1Label = rk1Rel1Label;
	}

	public void setRk1Rel1LabelEN(String rk1Rel1LabelEN) {
		this.rk1Rel1LabelEN = rk1Rel1LabelEN;
	}

	public void setRk1Rel2Id(Integer rk1Rel2Id) {
		this.rk1Rel2Id = rk1Rel2Id;
	}

	public void setRk1Rel2Code(String rk1Rel2Code) {
		this.rk1Rel2Code = rk1Rel2Code;
	}

	public void setRk1Rel2Label(String rk1Rel2Label) {
		this.rk1Rel2Label = rk1Rel2Label;
	}

	public void setRk1Rel2LabelEN(String rk1Rel2LabelEN) {
		this.rk1Rel2LabelEN = rk1Rel2LabelEN;
	}

	public void setRk2Id(Integer rk2Id) {
		this.rk2Id = rk2Id;
	}

	public void setRk2Str1(String rk2Str1) {
		this.rk2Str1 = rk2Str1;
	}

	public void setRk2Str2(String rk2Str2) {
		this.rk2Str2 = rk2Str2;
	}

	public void setRk2Str3(String rk2Str3) {
		this.rk2Str3 = rk2Str3;
	}

	public void setRk2Rel1Id(Integer rk2Rel1Id) {
		this.rk2Rel1Id = rk2Rel1Id;
	}

	public void setRk2Rel1Code(String rk2Rel1Code) {
		this.rk2Rel1Code = rk2Rel1Code;
	}

	public void setRk2Rel1Label(String rk2Rel1Label) {
		this.rk2Rel1Label = rk2Rel1Label;
	}

	public void setRk2Rel1LabelEN(String rk2Rel1LabelEN) {
		this.rk2Rel1LabelEN = rk2Rel1LabelEN;
	}

	public void setRk2Rel2Id(Integer rk2Rel2Id) {
		this.rk2Rel2Id = rk2Rel2Id;
	}

	public void setRk2Rel2Code(String rk2Rel2Code) {
		this.rk2Rel2Code = rk2Rel2Code;
	}

	public void setRk2Rel2Label(String rk2Rel2Label) {
		this.rk2Rel2Label = rk2Rel2Label;
	}

	public void setRk2Rel2LabelEN(String rk2Rel2LabelEN) {
		this.rk2Rel2LabelEN = rk2Rel2LabelEN;
	}

	public void setRk3Id(Integer rk3Id) {
		this.rk3Id = rk3Id;
	}

	public void setRk3Str1(String rk3Str1) {
		this.rk3Str1 = rk3Str1;
	}

	public void setRk3Str2(String rk3Str2) {
		this.rk3Str2 = rk3Str2;
	}

	public void setRk3Str3(String rk3Str3) {
		this.rk3Str3 = rk3Str3;
	}

	public void setRk3Rel1Id(Integer rk3Rel1Id) {
		this.rk3Rel1Id = rk3Rel1Id;
	}

	public void setRk3Rel1Code(String rk3Rel1Code) {
		this.rk3Rel1Code = rk3Rel1Code;
	}

	public void setRk3Rel1Label(String rk3Rel1Label) {
		this.rk3Rel1Label = rk3Rel1Label;
	}

	public void setRk3Rel1LabelEN(String rk3Rel1LabelEN) {
		this.rk3Rel1LabelEN = rk3Rel1LabelEN;
	}

	public void setRk3Rel2Id(Integer rk3Rel2Id) {
		this.rk3Rel2Id = rk3Rel2Id;
	}

	public void setRk3Rel2Code(String rk3Rel2Code) {
		this.rk3Rel2Code = rk3Rel2Code;
	}

	public void setRk3Rel2Label(String rk3Rel2Label) {
		this.rk3Rel2Label = rk3Rel2Label;
	}

	public void setRk3Rel2LabelEN(String rk3Rel2LabelEN) {
		this.rk3Rel2LabelEN = rk3Rel2LabelEN;
	}

	public Integer getRdResultType() {
		return rdResultType;
	}

	public void setRdResultType(Integer rdResultType) {
		this.rdResultType = rdResultType;
	}

	public Integer getRtIndex() {
		return rtIndex;
	}

	public void setRtIndex(Integer rtIndex) {
		this.rtIndex = rtIndex;
	}
	
}