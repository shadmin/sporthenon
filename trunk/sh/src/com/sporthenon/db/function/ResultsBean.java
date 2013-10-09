package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ResultsBean {

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
	
	@Column(name = "rs_rank3")
	private Integer rsRank3;
	
	@Column(name = "rs_rank4")
	private Integer rsRank4;
	
	@Column(name = "rs_rank5")
	private Integer rsRank5;
	
	@Column(name = "rs_rank6")
	private Integer rsRank6;
	
	@Column(name = "rs_rank7")
	private Integer rsRank7;
	
	@Column(name = "rs_rank8")
	private Integer rsRank8;
	
	@Column(name = "rs_rank9")
	private Integer rsRank9;
	
	@Column(name = "rs_rank10")
	private Integer rsRank10;
	
	@Column(name = "rs_result1")
	private String rsResult1;
	
	@Column(name = "rs_result2")
	private String rsResult2;
	
	@Column(name = "rs_result3")
	private String rsResult3;
	
	@Column(name = "rs_result4")
	private String rsResult4;
	
	@Column(name = "rs_result5")
	private String rsResult5;
	
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
	
	@Column(name = "ct1_id")
	private Integer ct1Id;

	@Column(name = "ct1_label")
	private String ct1Label;
	
	@Column(name = "ct2_id")
	private Integer ct2Id;

	@Column(name = "ct2_label")
	private String ct2Label;
	
	@Column(name = "st1_id")
	private Integer st1Id;
	
	@Column(name = "st1_code")
	private String st1Code;

	@Column(name = "st1_label")
	private String st1Label;
	
	@Column(name = "st2_id")
	private Integer st2Id;
	
	@Column(name = "st2_code")
	private String st2Code;

	@Column(name = "st2_label")
	private String st2Label;
	
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
	
	@Column(name = "en1_str1")
	private String en1Str1;
	
	@Column(name = "en1_str2")
	private String en1Str2;
	
	@Column(name = "en1_rel1_id")
	private Integer en1Rel1Id;
	
	@Column(name = "en1_rel1_code")
	private String en1Rel1Code;
	
	@Column(name = "en1_rel1_label")
	private String en1Rel1Label;
	
	@Column(name = "en1_rel2_id")
	private Integer en1Rel2Id;
	
	@Column(name = "en1_rel2_code")
	private String en1Rel2Code;
	
	@Column(name = "en1_rel2_label")
	private String en1Rel2Label;
	
	@Column(name = "en2_str1")
	private String en2Str1;
	
	@Column(name = "en2_str2")
	private String en2Str2;
	
	@Column(name = "en2_rel1_id")
	private Integer en2Rel1Id;
	
	@Column(name = "en2_rel1_code")
	private String en2Rel1Code;
	
	@Column(name = "en2_rel1_label")
	private String en2Rel1Label;
	
	@Column(name = "en2_rel2_id")
	private Integer en2Rel2Id;
	
	@Column(name = "en2_rel2_code")
	private String en2Rel2Code;
	
	@Column(name = "en2_rel2_label")
	private String en2Rel2Label;
	
	@Column(name = "en3_str1")
	private String en3Str1;
	
	@Column(name = "en3_str2")
	private String en3Str2;
	
	@Column(name = "en3_rel1_id")
	private Integer en3Rel1Id;
	
	@Column(name = "en3_rel1_code")
	private String en3Rel1Code;
	
	@Column(name = "en3_rel1_label")
	private String en3Rel1Label;
	
	@Column(name = "en3_rel2_id")
	private Integer en3Rel2Id;
	
	@Column(name = "en3_rel2_code")
	private String en3Rel2Code;
	
	@Column(name = "en3_rel2_label")
	private String en3Rel2Label;
	
	@Column(name = "en4_str1")
	private String en4Str1;
	
	@Column(name = "en4_str2")
	private String en4Str2;
	
	@Column(name = "en4_rel1_id")
	private Integer en4Rel1Id;
	
	@Column(name = "en4_rel1_code")
	private String en4Rel1Code;
	
	@Column(name = "en4_rel1_label")
	private String en4Rel1Label;
	
	@Column(name = "en4_rel2_id")
	private Integer en4Rel2Id;
	
	@Column(name = "en4_rel2_code")
	private String en4Rel2Code;
	
	@Column(name = "en4_rel2_label")
	private String en4Rel2Label;
	
	@Column(name = "en5_str1")
	private String en5Str1;
	
	@Column(name = "en5_str2")
	private String en5Str2;
	
	@Column(name = "en5_rel1_id")
	private Integer en5Rel1Id;
	
	@Column(name = "en5_rel1_code")
	private String en5Rel1Code;
	
	@Column(name = "en5_rel1_label")
	private String en5Rel1Label;
	
	@Column(name = "en5_rel2_id")
	private Integer en5Rel2Id;
	
	@Column(name = "en5_rel2_code")
	private String en5Rel2Code;
	
	@Column(name = "en5_rel2_label")
	private String en5Rel2Label;
	
	@Column(name = "en6_str1")
	private String en6Str1;
	
	@Column(name = "en6_str2")
	private String en6Str2;
	
	@Column(name = "en6_rel1_id")
	private Integer en6Rel1Id;
	
	@Column(name = "en6_rel1_code")
	private String en6Rel1Code;
	
	@Column(name = "en6_rel1_label")
	private String en6Rel1Label;
	
	@Column(name = "en6_rel2_id")
	private Integer en6Rel2Id;
	
	@Column(name = "en6_rel2_code")
	private String en6Rel2Code;
	
	@Column(name = "en6_rel2_label")
	private String en6Rel2Label;
	
	@Column(name = "en7_str1")
	private String en7Str1;
	
	@Column(name = "en7_str2")
	private String en7Str2;
	
	@Column(name = "en7_rel1_id")
	private Integer en7Rel1Id;
	
	@Column(name = "en7_rel1_code")
	private String en7Rel1Code;
	
	@Column(name = "en7_rel1_label")
	private String en7Rel1Label;
	
	@Column(name = "en7_rel2_id")
	private Integer en7Rel2Id;
	
	@Column(name = "en7_rel2_code")
	private String en7Rel2Code;
	
	@Column(name = "en7_rel2_label")
	private String en7Rel2Label;
	
	@Column(name = "en8_str1")
	private String en8Str1;
	
	@Column(name = "en8_str2")
	private String en8Str2;
	
	@Column(name = "en8_rel1_id")
	private Integer en8Rel1Id;
	
	@Column(name = "en8_rel1_code")
	private String en8Rel1Code;
	
	@Column(name = "en8_rel1_label")
	private String en8Rel1Label;
	
	@Column(name = "en8_rel2_id")
	private Integer en8Rel2Id;
	
	@Column(name = "en8_rel2_code")
	private String en8Rel2Code;
	
	@Column(name = "en8_rel2_label")
	private String en8Rel2Label;
	
	@Column(name = "en9_str1")
	private String en9Str1;
	
	@Column(name = "en9_str2")
	private String en9Str2;
	
	@Column(name = "en9_rel1_id")
	private Integer en9Rel1Id;
	
	@Column(name = "en9_rel1_code")
	private String en9Rel1Code;
	
	@Column(name = "en9_rel1_label")
	private String en9Rel1Label;
	
	@Column(name = "en9_rel2_id")
	private Integer en9Rel2Id;
	
	@Column(name = "en9_rel2_code")
	private String en9Rel2Code;
	
	@Column(name = "en9_rel2_label")
	private String en9Rel2Label;
	
	@Column(name = "en10_str1")
	private String en10Str1;
	
	@Column(name = "en10_str2")
	private String en10Str2;
	
	@Column(name = "en10_rel1_id")
	private Integer en10Rel1Id;
	
	@Column(name = "en10_rel1_code")
	private String en10Rel1Code;
	
	@Column(name = "en10_rel1_label")
	private String en10Rel1Label;
	
	@Column(name = "en10_rel2_id")
	private Integer en10Rel2Id;
	
	@Column(name = "en10_rel2_code")
	private String en10Rel2Code;
	
	@Column(name = "en10_rel2_label")
	private String en10Rel2Label;
	
	@Column(name = "dr_id")
	private Integer drId;
	
	public int getRsId() {
		return rsId;
	}

	public void setRsId(int rsId) {
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

	public Integer getRsRank3() {
		return rsRank3;
	}

	public void setRsRank3(Integer rsRank3) {
		this.rsRank3 = rsRank3;
	}

	public Integer getRsRank4() {
		return rsRank4;
	}

	public void setRsRank4(Integer rsRank4) {
		this.rsRank4 = rsRank4;
	}

	public Integer getRsRank5() {
		return rsRank5;
	}

	public void setRsRank5(Integer rsRank5) {
		this.rsRank5 = rsRank5;
	}

	public Integer getRsRank6() {
		return rsRank6;
	}

	public void setRsRank6(Integer rsRank6) {
		this.rsRank6 = rsRank6;
	}

	public Integer getRsRank7() {
		return rsRank7;
	}

	public void setRsRank7(Integer rsRank7) {
		this.rsRank7 = rsRank7;
	}

	public Integer getRsRank8() {
		return rsRank8;
	}

	public void setRsRank8(Integer rsRank8) {
		this.rsRank8 = rsRank8;
	}

	public Integer getRsRank9() {
		return rsRank9;
	}

	public void setRsRank9(Integer rsRank9) {
		this.rsRank9 = rsRank9;
	}

	public Integer getRsRank10() {
		return rsRank10;
	}

	public void setRsRank10(Integer rsRank10) {
		this.rsRank10 = rsRank10;
	}

	public String getRsResult1() {
		return rsResult1;
	}

	public void setRsResult1(String rsResult1) {
		this.rsResult1 = rsResult1;
	}

	public String getRsResult2() {
		return rsResult2;
	}

	public void setRsResult2(String rsResult2) {
		this.rsResult2 = rsResult2;
	}

	public String getRsResult3() {
		return rsResult3;
	}

	public void setRsResult3(String rsResult3) {
		this.rsResult3 = rsResult3;
	}

	public String getRsResult4() {
		return rsResult4;
	}

	public void setRsResult4(String rsResult4) {
		this.rsResult4 = rsResult4;
	}

	public String getRsResult5() {
		return rsResult5;
	}

	public void setRsResult5(String rsResult5) {
		this.rsResult5 = rsResult5;
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

	public Integer getCt1Id() {
		return ct1Id;
	}

	public void setCt1Id(Integer ct1Id) {
		this.ct1Id = ct1Id;
	}

	public String getCt1Label() {
		return ct1Label;
	}

	public void setCt1Label(String ct1Label) {
		this.ct1Label = ct1Label;
	}

	public Integer getCt2Id() {
		return ct2Id;
	}

	public void setCt2Id(Integer ct2Id) {
		this.ct2Id = ct2Id;
	}

	public String getCt2Label() {
		return ct2Label;
	}

	public void setCt2Label(String ct2Label) {
		this.ct2Label = ct2Label;
	}

	public Integer getSt1Id() {
		return st1Id;
	}

	public void setSt1Id(Integer st1Id) {
		this.st1Id = st1Id;
	}

	public String getSt1Code() {
		return st1Code;
	}

	public void setSt1Code(String st1Code) {
		this.st1Code = st1Code;
	}

	public String getSt1Label() {
		return st1Label;
	}

	public void setSt1Label(String st1Label) {
		this.st1Label = st1Label;
	}

	public Integer getSt2Id() {
		return st2Id;
	}

	public void setSt2Id(Integer st2Id) {
		this.st2Id = st2Id;
	}

	public String getSt2Code() {
		return st2Code;
	}

	public void setSt2Code(String st2Code) {
		this.st2Code = st2Code;
	}

	public String getSt2Label() {
		return st2Label;
	}

	public void setSt2Label(String st2Label) {
		this.st2Label = st2Label;
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

	public String getEn1Str1() {
		return en1Str1;
	}

	public void setEn1Str1(String en1Str1) {
		this.en1Str1 = en1Str1;
	}

	public String getEn1Str2() {
		return en1Str2;
	}

	public void setEn1Str2(String en1Str2) {
		this.en1Str2 = en1Str2;
	}

	public String getEn2Str1() {
		return en2Str1;
	}

	public void setEn2Str1(String en2Str1) {
		this.en2Str1 = en2Str1;
	}

	public String getEn2Str2() {
		return en2Str2;
	}

	public void setEn2Str2(String en2Str2) {
		this.en2Str2 = en2Str2;
	}

	public String getEn3Str1() {
		return en3Str1;
	}

	public void setEn3Str1(String en3Str1) {
		this.en3Str1 = en3Str1;
	}

	public String getEn3Str2() {
		return en3Str2;
	}

	public void setEn3Str2(String en3Str2) {
		this.en3Str2 = en3Str2;
	}

	public String getEn4Str1() {
		return en4Str1;
	}

	public void setEn4Str1(String en4Str1) {
		this.en4Str1 = en4Str1;
	}

	public String getEn4Str2() {
		return en4Str2;
	}

	public void setEn4Str2(String en4Str2) {
		this.en4Str2 = en4Str2;
	}

	public String getEn5Str1() {
		return en5Str1;
	}

	public void setEn5Str1(String en5Str1) {
		this.en5Str1 = en5Str1;
	}

	public String getEn5Str2() {
		return en5Str2;
	}

	public void setEn5Str2(String en5Str2) {
		this.en5Str2 = en5Str2;
	}

	public String getEn6Str1() {
		return en6Str1;
	}

	public void setEn6Str1(String en6Str1) {
		this.en6Str1 = en6Str1;
	}

	public String getEn6Str2() {
		return en6Str2;
	}

	public void setEn6Str2(String en6Str2) {
		this.en6Str2 = en6Str2;
	}

	public String getEn7Str1() {
		return en7Str1;
	}

	public void setEn7Str1(String en7Str1) {
		this.en7Str1 = en7Str1;
	}

	public String getEn7Str2() {
		return en7Str2;
	}

	public void setEn7Str2(String en7Str2) {
		this.en7Str2 = en7Str2;
	}

	public String getEn8Str1() {
		return en8Str1;
	}

	public void setEn8Str1(String en8Str1) {
		this.en8Str1 = en8Str1;
	}

	public String getEn8Str2() {
		return en8Str2;
	}

	public void setEn8Str2(String en8Str2) {
		this.en8Str2 = en8Str2;
	}

	public String getEn9Str1() {
		return en9Str1;
	}

	public void setEn9Str1(String en9Str1) {
		this.en9Str1 = en9Str1;
	}

	public String getEn9Str2() {
		return en9Str2;
	}

	public void setEn9Str2(String en9Str2) {
		this.en9Str2 = en9Str2;
	}

	public String getEn10Str1() {
		return en10Str1;
	}

	public void setEn10Str1(String en10Str1) {
		this.en10Str1 = en10Str1;
	}

	public String getEn10Str2() {
		return en10Str2;
	}

	public void setEn10Str2(String en10Str2) {
		this.en10Str2 = en10Str2;
	}

	public void setRsId(Integer rsId) {
		this.rsId = rsId;
	}

	public Integer getEn1Rel1Id() {
		return en1Rel1Id;
	}

	public void setEn1Rel1Id(Integer en1Rel1Id) {
		this.en1Rel1Id = en1Rel1Id;
	}

	public String getEn1Rel1Code() {
		return en1Rel1Code;
	}

	public void setEn1Rel1Code(String en1Rel1Code) {
		this.en1Rel1Code = en1Rel1Code;
	}

	public String getEn1Rel1Label() {
		return en1Rel1Label;
	}

	public void setEn1Rel1Label(String en1Rel1Label) {
		this.en1Rel1Label = en1Rel1Label;
	}

	public Integer getEn1Rel2Id() {
		return en1Rel2Id;
	}

	public void setEn1Rel2Id(Integer en1Rel2Id) {
		this.en1Rel2Id = en1Rel2Id;
	}

	public String getEn1Rel2Code() {
		return en1Rel2Code;
	}

	public void setEn1Rel2Code(String en1Rel2Code) {
		this.en1Rel2Code = en1Rel2Code;
	}

	public String getEn1Rel2Label() {
		return en1Rel2Label;
	}

	public void setEn1Rel2Label(String en1Rel2Label) {
		this.en1Rel2Label = en1Rel2Label;
	}

	public Integer getEn2Rel1Id() {
		return en2Rel1Id;
	}

	public void setEn2Rel1Id(Integer en2Rel1Id) {
		this.en2Rel1Id = en2Rel1Id;
	}

	public String getEn2Rel1Code() {
		return en2Rel1Code;
	}

	public void setEn2Rel1Code(String en2Rel1Code) {
		this.en2Rel1Code = en2Rel1Code;
	}

	public String getEn2Rel1Label() {
		return en2Rel1Label;
	}

	public void setEn2Rel1Label(String en2Rel1Label) {
		this.en2Rel1Label = en2Rel1Label;
	}

	public Integer getEn2Rel2Id() {
		return en2Rel2Id;
	}

	public void setEn2Rel2Id(Integer en2Rel2Id) {
		this.en2Rel2Id = en2Rel2Id;
	}

	public String getEn2Rel2Code() {
		return en2Rel2Code;
	}

	public void setEn2Rel2Code(String en2Rel2Code) {
		this.en2Rel2Code = en2Rel2Code;
	}

	public String getEn2Rel2Label() {
		return en2Rel2Label;
	}

	public void setEn2Rel2Label(String en2Rel2Label) {
		this.en2Rel2Label = en2Rel2Label;
	}

	public Integer getEn3Rel1Id() {
		return en3Rel1Id;
	}

	public void setEn3Rel1Id(Integer en3Rel1Id) {
		this.en3Rel1Id = en3Rel1Id;
	}

	public String getEn3Rel1Code() {
		return en3Rel1Code;
	}

	public void setEn3Rel1Code(String en3Rel1Code) {
		this.en3Rel1Code = en3Rel1Code;
	}

	public String getEn3Rel1Label() {
		return en3Rel1Label;
	}

	public void setEn3Rel1Label(String en3Rel1Label) {
		this.en3Rel1Label = en3Rel1Label;
	}

	public Integer getEn3Rel2Id() {
		return en3Rel2Id;
	}

	public void setEn3Rel2Id(Integer en3Rel2Id) {
		this.en3Rel2Id = en3Rel2Id;
	}

	public String getEn3Rel2Code() {
		return en3Rel2Code;
	}

	public void setEn3Rel2Code(String en3Rel2Code) {
		this.en3Rel2Code = en3Rel2Code;
	}

	public String getEn3Rel2Label() {
		return en3Rel2Label;
	}

	public void setEn3Rel2Label(String en3Rel2Label) {
		this.en3Rel2Label = en3Rel2Label;
	}

	public Integer getEn4Rel1Id() {
		return en4Rel1Id;
	}

	public void setEn4Rel1Id(Integer en4Rel1Id) {
		this.en4Rel1Id = en4Rel1Id;
	}

	public String getEn4Rel1Code() {
		return en4Rel1Code;
	}

	public void setEn4Rel1Code(String en4Rel1Code) {
		this.en4Rel1Code = en4Rel1Code;
	}

	public String getEn4Rel1Label() {
		return en4Rel1Label;
	}

	public void setEn4Rel1Label(String en4Rel1Label) {
		this.en4Rel1Label = en4Rel1Label;
	}

	public Integer getEn4Rel2Id() {
		return en4Rel2Id;
	}

	public void setEn4Rel2Id(Integer en4Rel2Id) {
		this.en4Rel2Id = en4Rel2Id;
	}

	public String getEn4Rel2Code() {
		return en4Rel2Code;
	}

	public void setEn4Rel2Code(String en4Rel2Code) {
		this.en4Rel2Code = en4Rel2Code;
	}

	public String getEn4Rel2Label() {
		return en4Rel2Label;
	}

	public void setEn4Rel2Label(String en4Rel2Label) {
		this.en4Rel2Label = en4Rel2Label;
	}

	public Integer getEn5Rel1Id() {
		return en5Rel1Id;
	}

	public void setEn5Rel1Id(Integer en5Rel1Id) {
		this.en5Rel1Id = en5Rel1Id;
	}

	public String getEn5Rel1Code() {
		return en5Rel1Code;
	}

	public void setEn5Rel1Code(String en5Rel1Code) {
		this.en5Rel1Code = en5Rel1Code;
	}

	public String getEn5Rel1Label() {
		return en5Rel1Label;
	}

	public void setEn5Rel1Label(String en5Rel1Label) {
		this.en5Rel1Label = en5Rel1Label;
	}

	public Integer getEn5Rel2Id() {
		return en5Rel2Id;
	}

	public void setEn5Rel2Id(Integer en5Rel2Id) {
		this.en5Rel2Id = en5Rel2Id;
	}

	public String getEn5Rel2Code() {
		return en5Rel2Code;
	}

	public void setEn5Rel2Code(String en5Rel2Code) {
		this.en5Rel2Code = en5Rel2Code;
	}

	public String getEn5Rel2Label() {
		return en5Rel2Label;
	}

	public void setEn5Rel2Label(String en5Rel2Label) {
		this.en5Rel2Label = en5Rel2Label;
	}

	public Integer getEn6Rel1Id() {
		return en6Rel1Id;
	}

	public void setEn6Rel1Id(Integer en6Rel1Id) {
		this.en6Rel1Id = en6Rel1Id;
	}

	public String getEn6Rel1Code() {
		return en6Rel1Code;
	}

	public void setEn6Rel1Code(String en6Rel1Code) {
		this.en6Rel1Code = en6Rel1Code;
	}

	public String getEn6Rel1Label() {
		return en6Rel1Label;
	}

	public void setEn6Rel1Label(String en6Rel1Label) {
		this.en6Rel1Label = en6Rel1Label;
	}

	public Integer getEn6Rel2Id() {
		return en6Rel2Id;
	}

	public void setEn6Rel2Id(Integer en6Rel2Id) {
		this.en6Rel2Id = en6Rel2Id;
	}

	public String getEn6Rel2Code() {
		return en6Rel2Code;
	}

	public void setEn6Rel2Code(String en6Rel2Code) {
		this.en6Rel2Code = en6Rel2Code;
	}

	public String getEn6Rel2Label() {
		return en6Rel2Label;
	}

	public void setEn6Rel2Label(String en6Rel2Label) {
		this.en6Rel2Label = en6Rel2Label;
	}

	public Integer getEn7Rel1Id() {
		return en7Rel1Id;
	}

	public void setEn7Rel1Id(Integer en7Rel1Id) {
		this.en7Rel1Id = en7Rel1Id;
	}

	public String getEn7Rel1Code() {
		return en7Rel1Code;
	}

	public void setEn7Rel1Code(String en7Rel1Code) {
		this.en7Rel1Code = en7Rel1Code;
	}

	public String getEn7Rel1Label() {
		return en7Rel1Label;
	}

	public void setEn7Rel1Label(String en7Rel1Label) {
		this.en7Rel1Label = en7Rel1Label;
	}

	public Integer getEn7Rel2Id() {
		return en7Rel2Id;
	}

	public void setEn7Rel2Id(Integer en7Rel2Id) {
		this.en7Rel2Id = en7Rel2Id;
	}

	public String getEn7Rel2Code() {
		return en7Rel2Code;
	}

	public void setEn7Rel2Code(String en7Rel2Code) {
		this.en7Rel2Code = en7Rel2Code;
	}

	public String getEn7Rel2Label() {
		return en7Rel2Label;
	}

	public void setEn7Rel2Label(String en7Rel2Label) {
		this.en7Rel2Label = en7Rel2Label;
	}

	public Integer getEn8Rel1Id() {
		return en8Rel1Id;
	}

	public void setEn8Rel1Id(Integer en8Rel1Id) {
		this.en8Rel1Id = en8Rel1Id;
	}

	public String getEn8Rel1Code() {
		return en8Rel1Code;
	}

	public void setEn8Rel1Code(String en8Rel1Code) {
		this.en8Rel1Code = en8Rel1Code;
	}

	public String getEn8Rel1Label() {
		return en8Rel1Label;
	}

	public void setEn8Rel1Label(String en8Rel1Label) {
		this.en8Rel1Label = en8Rel1Label;
	}

	public Integer getEn8Rel2Id() {
		return en8Rel2Id;
	}

	public void setEn8Rel2Id(Integer en8Rel2Id) {
		this.en8Rel2Id = en8Rel2Id;
	}

	public String getEn8Rel2Code() {
		return en8Rel2Code;
	}

	public void setEn8Rel2Code(String en8Rel2Code) {
		this.en8Rel2Code = en8Rel2Code;
	}

	public String getEn8Rel2Label() {
		return en8Rel2Label;
	}

	public void setEn8Rel2Label(String en8Rel2Label) {
		this.en8Rel2Label = en8Rel2Label;
	}

	public Integer getEn9Rel1Id() {
		return en9Rel1Id;
	}

	public void setEn9Rel1Id(Integer en9Rel1Id) {
		this.en9Rel1Id = en9Rel1Id;
	}

	public String getEn9Rel1Code() {
		return en9Rel1Code;
	}

	public void setEn9Rel1Code(String en9Rel1Code) {
		this.en9Rel1Code = en9Rel1Code;
	}

	public String getEn9Rel1Label() {
		return en9Rel1Label;
	}

	public void setEn9Rel1Label(String en9Rel1Label) {
		this.en9Rel1Label = en9Rel1Label;
	}

	public Integer getEn9Rel2Id() {
		return en9Rel2Id;
	}

	public void setEn9Rel2Id(Integer en9Rel2Id) {
		this.en9Rel2Id = en9Rel2Id;
	}

	public String getEn9Rel2Code() {
		return en9Rel2Code;
	}

	public void setEn9Rel2Code(String en9Rel2Code) {
		this.en9Rel2Code = en9Rel2Code;
	}

	public String getEn9Rel2Label() {
		return en9Rel2Label;
	}

	public void setEn9Rel2Label(String en9Rel2Label) {
		this.en9Rel2Label = en9Rel2Label;
	}

	public Integer getEn10Rel1Id() {
		return en10Rel1Id;
	}

	public void setEn10Rel1Id(Integer en10Rel1Id) {
		this.en10Rel1Id = en10Rel1Id;
	}

	public String getEn10Rel1Code() {
		return en10Rel1Code;
	}

	public void setEn10Rel1Code(String en10Rel1Code) {
		this.en10Rel1Code = en10Rel1Code;
	}

	public String getEn10Rel1Label() {
		return en10Rel1Label;
	}

	public void setEn10Rel1Label(String en10Rel1Label) {
		this.en10Rel1Label = en10Rel1Label;
	}

	public Integer getEn10Rel2Id() {
		return en10Rel2Id;
	}

	public void setEn10Rel2Id(Integer en10Rel2Id) {
		this.en10Rel2Id = en10Rel2Id;
	}

	public String getEn10Rel2Code() {
		return en10Rel2Code;
	}

	public void setEn10Rel2Code(String en10Rel2Code) {
		this.en10Rel2Code = en10Rel2Code;
	}

	public String getEn10Rel2Label() {
		return en10Rel2Label;
	}

	public void setEn10Rel2Label(String en10Rel2Label) {
		this.en10Rel2Label = en10Rel2Label;
	}

	public Integer getDrId() {
		return drId;
	}

	public void setDrId(Integer drId) {
		this.drId = drId;
	}

	@Override
	public String toString() {
		return "GetResultsBean [cn1Code=" + cn1Code + ", cn1Id=" + cn1Id
				+ ", cn1Label=" + cn1Label + ", cn2Code=" + cn2Code
				+ ", cn2Id=" + cn2Id + ", cn2Label=" + cn2Label + ", ct1Id="
				+ ct1Id + ", ct1Label=" + ct1Label + ", ct2Id=" + ct2Id
				+ ", ct2Label=" + ct2Label + ", cxId=" + cxId + ", cxLabel="
				+ cxLabel + ", en10Rel1Code=" + en10Rel1Code + ", en10Rel1Id="
				+ en10Rel1Id + ", en10Rel1Label=" + en10Rel1Label
				+ ", en10Rel2Code=" + en10Rel2Code + ", en10Rel2Id="
				+ en10Rel2Id + ", en10Rel2Label=" + en10Rel2Label
				+ ", en10Str1=" + en10Str1 + ", en10Str2=" + en10Str2
				+ ", en1Rel1Code=" + en1Rel1Code + ", en1Rel1Id=" + en1Rel1Id
				+ ", en1Rel1Label=" + en1Rel1Label + ", en1Rel2Code="
				+ en1Rel2Code + ", en1Rel2Id=" + en1Rel2Id + ", en1Rel2Label="
				+ en1Rel2Label + ", en1Str1=" + en1Str1 + ", en1Str2="
				+ en1Str2 + ", en2Rel1Code=" + en2Rel1Code + ", en2Rel1Id="
				+ en2Rel1Id + ", en2Rel1Label=" + en2Rel1Label
				+ ", en2Rel2Code=" + en2Rel2Code + ", en2Rel2Id=" + en2Rel2Id
				+ ", en2Rel2Label=" + en2Rel2Label + ", en2Str1=" + en2Str1
				+ ", en2Str2=" + en2Str2 + ", en3Rel1Code=" + en3Rel1Code
				+ ", en3Rel1Id=" + en3Rel1Id + ", en3Rel1Label=" + en3Rel1Label
				+ ", en3Rel2Code=" + en3Rel2Code + ", en3Rel2Id=" + en3Rel2Id
				+ ", en3Rel2Label=" + en3Rel2Label + ", en3Str1=" + en3Str1
				+ ", en3Str2=" + en3Str2 + ", en4Rel1Code=" + en4Rel1Code
				+ ", en4Rel1Id=" + en4Rel1Id + ", en4Rel1Label=" + en4Rel1Label
				+ ", en4Rel2Code=" + en4Rel2Code + ", en4Rel2Id=" + en4Rel2Id
				+ ", en4Rel2Label=" + en4Rel2Label + ", en4Str1=" + en4Str1
				+ ", en4Str2=" + en4Str2 + ", en5Rel1Code=" + en5Rel1Code
				+ ", en5Rel1Id=" + en5Rel1Id + ", en5Rel1Label=" + en5Rel1Label
				+ ", en5Rel2Code=" + en5Rel2Code + ", en5Rel2Id=" + en5Rel2Id
				+ ", en5Rel2Label=" + en5Rel2Label + ", en5Str1=" + en5Str1
				+ ", en5Str2=" + en5Str2 + ", en6Rel1Code=" + en6Rel1Code
				+ ", en6Rel1Id=" + en6Rel1Id + ", en6Rel1Label=" + en6Rel1Label
				+ ", en6Rel2Code=" + en6Rel2Code + ", en6Rel2Id=" + en6Rel2Id
				+ ", en6Rel2Label=" + en6Rel2Label + ", en6Str1=" + en6Str1
				+ ", en6Str2=" + en6Str2 + ", en7Rel1Code=" + en7Rel1Code
				+ ", en7Rel1Id=" + en7Rel1Id + ", en7Rel1Label=" + en7Rel1Label
				+ ", en7Rel2Code=" + en7Rel2Code + ", en7Rel2Id=" + en7Rel2Id
				+ ", en7Rel2Label=" + en7Rel2Label + ", en7Str1=" + en7Str1
				+ ", en7Str2=" + en7Str2 + ", en8Rel1Code=" + en8Rel1Code
				+ ", en8Rel1Id=" + en8Rel1Id + ", en8Rel1Label=" + en8Rel1Label
				+ ", en8Rel2Code=" + en8Rel2Code + ", en8Rel2Id=" + en8Rel2Id
				+ ", en8Rel2Label=" + en8Rel2Label + ", en8Str1=" + en8Str1
				+ ", en8Str2=" + en8Str2 + ", en9Rel1Code=" + en9Rel1Code
				+ ", en9Rel1Id=" + en9Rel1Id + ", en9Rel1Label=" + en9Rel1Label
				+ ", en9Rel2Code=" + en9Rel2Code + ", en9Rel2Id=" + en9Rel2Id
				+ ", en9Rel2Label=" + en9Rel2Label + ", en9Str1=" + en9Str1
				+ ", en9Str2=" + en9Str2 + ", rsComment=" + rsComment
				+ ", rsDate1=" + rsDate1 + ", rsDate2=" + rsDate2 + ", rsId="
				+ rsId + ", rsRank1=" + rsRank1 + ", rsRank10=" + rsRank10
				+ ", rsRank2=" + rsRank2 + ", rsRank3=" + rsRank3
				+ ", rsRank4=" + rsRank4 + ", rsRank5=" + rsRank5
				+ ", rsRank6=" + rsRank6 + ", rsRank7=" + rsRank7
				+ ", rsRank8=" + rsRank8 + ", rsRank9=" + rsRank9
				+ ", rsResult1=" + rsResult1 + ", rsResult2=" + rsResult2
				+ ", rsResult3=" + rsResult3 + ", rsResult4=" + rsResult4
				+ ", rsResult5=" + rsResult5 + ", st1Code=" + st1Code
				+ ", st1Id=" + st1Id + ", st1Label=" + st1Label + ", st2Code="
				+ st2Code + ", st2Id=" + st2Id + ", st2Label=" + st2Label
				+ ", yrId=" + yrId + ", yrLabel=" + yrLabel + "]";
	}
	
}