package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DrawBean {

	@Id
	@Column(name = "dr_id")
	private Integer drId;
	
	@Column(name = "dr_type")
	private Integer drType;
	
	@Column(name = "yr_label")
	private String yrLabel;
	
	@Column(name = "dr_result_qf1")
	private String drResultQf1;
	
	@Column(name = "dr_result_qf2")
	private String drResultQf2;
	
	@Column(name = "dr_result_qf3")
	private String drResultQf3;
	
	@Column(name = "dr_result_qf4")
	private String drResultQf4;
	
	@Column(name = "dr_result_sf1")
	private String drResultSf1;
	
	@Column(name = "dr_result_sf2")
	private String drResultSf2;
	
	@Column(name = "rs_result_f")
	private String rsResultF;
	
	@Column(name = "en1_qf1_id")
	private Integer en1Qf1Id;
	
	@Column(name = "en1_qf1_str1")
	private String en1Qf1Str1;
	
	@Column(name = "en1_qf1_str2")
	private String en1Qf1Str2;
	
	@Column(name = "en1_qf1_rel1_id")
	private Integer en1Qf1Rel1Id;
	
	@Column(name = "en1_qf1_rel1_code")
	private String en1Qf1Rel1Code;
	
	@Column(name = "en1_qf1_rel1_label")
	private String en1Qf1Rel1Label;
	
	@Column(name = "en1_qf1_rel2_id")
	private Integer en1Qf1Rel2Id;
	
	@Column(name = "en1_qf1_rel2_code")
	private String en1Qf1Rel2Code;
	
	@Column(name = "en1_qf1_rel2_label")
	private String en1Qf1Rel2Label;
	
	@Column(name = "en2_qf1_id")
	private Integer en2Qf1Id;
	
	@Column(name = "en2_qf1_str1")
	private String en2Qf1Str1;
	
	@Column(name = "en2_qf1_str2")
	private String en2Qf1Str2;
	
	@Column(name = "en2_qf1_rel1_id")
	private Integer en2Qf1Rel1Id;
	
	@Column(name = "en2_qf1_rel1_code")
	private String en2Qf1Rel1Code;
	
	@Column(name = "en2_qf1_rel1_label")
	private String en2Qf1Rel1Label;
	
	@Column(name = "en2_qf1_rel2_id")
	private Integer en2Qf1Rel2Id;
	
	@Column(name = "en2_qf1_rel2_code")
	private String en2Qf1Rel2Code;
	
	@Column(name = "en2_qf1_rel2_label")
	private String en2Qf1Rel2Label;
	
	@Column(name = "en1_qf2_id")
	private Integer en1Qf2Id;
	
	@Column(name = "en1_qf2_str1")
	private String en1Qf2Str1;
	
	@Column(name = "en1_qf2_str2")
	private String en1Qf2Str2;
	
	@Column(name = "en1_qf2_rel1_id")
	private Integer en1Qf2Rel1Id;
	
	@Column(name = "en1_qf2_rel1_code")
	private String en1Qf2Rel1Code;
	
	@Column(name = "en1_qf2_rel1_label")
	private String en1Qf2Rel1Label;
	
	@Column(name = "en1_qf2_rel2_id")
	private Integer en1Qf2Rel2Id;
	
	@Column(name = "en1_qf2_rel2_code")
	private String en1Qf2Rel2Code;
	
	@Column(name = "en1_qf2_rel2_label")
	private String en1Qf2Rel2Label;
	
	@Column(name = "en2_qf2_id")
	private Integer en2Qf2Id;
	
	@Column(name = "en2_qf2_str1")
	private String en2Qf2Str1;
	
	@Column(name = "en2_qf2_str2")
	private String en2Qf2Str2;
	
	@Column(name = "en2_qf2_rel1_id")
	private Integer en2Qf2Rel1Id;
	
	@Column(name = "en2_qf2_rel1_code")
	private String en2Qf2Rel1Code;
	
	@Column(name = "en2_qf2_rel1_label")
	private String en2Qf2Rel1Label;
	
	@Column(name = "en2_qf2_rel2_id")
	private Integer en2Qf2Rel2Id;
	
	@Column(name = "en2_qf2_rel2_code")
	private String en2Qf2Rel2Code;
	
	@Column(name = "en2_qf2_rel2_label")
	private String en2Qf2Rel2Label;
	
	@Column(name = "en1_qf3_id")
	private Integer en1Qf3Id;
	
	@Column(name = "en1_qf3_str1")
	private String en1Qf3Str1;
	
	@Column(name = "en1_qf3_str2")
	private String en1Qf3Str2;
	
	@Column(name = "en1_qf3_rel1_id")
	private Integer en1Qf3Rel1Id;
	
	@Column(name = "en1_qf3_rel1_code")
	private String en1Qf3Rel1Code;
	
	@Column(name = "en1_qf3_rel1_label")
	private String en1Qf3Rel1Label;
	
	@Column(name = "en1_qf3_rel2_id")
	private Integer en1Qf3Rel2Id;
	
	@Column(name = "en1_qf3_rel2_code")
	private String en1Qf3Rel2Code;
	
	@Column(name = "en1_qf3_rel2_label")
	private String en1Qf3Rel2Label;

	@Column(name = "en2_qf3_id")
	private Integer en2Qf3Id;
	
	@Column(name = "en2_qf3_str1")
	private String en2Qf3Str1;
	
	@Column(name = "en2_qf3_str2")
	private String en2Qf3Str2;
	
	@Column(name = "en2_qf3_rel1_id")
	private Integer en2Qf3Rel1Id;
	
	@Column(name = "en2_qf3_rel1_code")
	private String en2Qf3Rel1Code;
	
	@Column(name = "en2_qf3_rel1_label")
	private String en2Qf3Rel1Label;
	
	@Column(name = "en2_qf3_rel2_id")
	private Integer en2Qf3Rel2Id;
	
	@Column(name = "en2_qf3_rel2_code")
	private String en2Qf3Rel2Code;
	
	@Column(name = "en2_qf3_rel2_label")
	private String en2Qf3Rel2Label;
	
	@Column(name = "en1_qf4_id")
	private Integer en1Qf4Id;
	
	@Column(name = "en1_qf4_str1")
	private String en1Qf4Str1;
	
	@Column(name = "en1_qf4_str2")
	private String en1Qf4Str2;
	
	@Column(name = "en1_qf4_rel1_id")
	private Integer en1Qf4Rel1Id;
	
	@Column(name = "en1_qf4_rel1_code")
	private String en1Qf4Rel1Code;
	
	@Column(name = "en1_qf4_rel1_label")
	private String en1Qf4Rel1Label;
	
	@Column(name = "en1_qf4_rel2_id")
	private Integer en1Qf4Rel2Id;
	
	@Column(name = "en1_qf4_rel2_code")
	private String en1Qf4Rel2Code;
	
	@Column(name = "en1_qf4_rel2_label")
	private String en1Qf4Rel2Label;
	
	@Column(name = "en2_qf4_id")
	private Integer en2Qf4Id;
	
	@Column(name = "en2_qf4_str1")
	private String en2Qf4Str1;
	
	@Column(name = "en2_qf4_str2")
	private String en2Qf4Str2;
	
	@Column(name = "en2_qf4_rel1_id")
	private Integer en2Qf4Rel1Id;
	
	@Column(name = "en2_qf4_rel1_code")
	private String en2Qf4Rel1Code;
	
	@Column(name = "en2_qf4_rel1_label")
	private String en2Qf4Rel1Label;
	
	@Column(name = "en2_qf4_rel2_id")
	private Integer en2Qf4Rel2Id;
	
	@Column(name = "en2_qf4_rel2_code")
	private String en2Qf4Rel2Code;
	
	@Column(name = "en2_qf4_rel2_label")
	private String en2Qf4Rel2Label;
	
	@Column(name = "en1_sf1_id")
	private Integer en1Sf1Id;
	
	@Column(name = "en1_sf1_str1")
	private String en1Sf1Str1;
	
	@Column(name = "en1_sf1_str2")
	private String en1Sf1Str2;
	
	@Column(name = "en1_sf1_rel1_id")
	private Integer en1Sf1Rel1Id;
	
	@Column(name = "en1_sf1_rel1_code")
	private String en1Sf1Rel1Code;
	
	@Column(name = "en1_sf1_rel1_label")
	private String en1Sf1Rel1Label;
	
	@Column(name = "en1_sf1_rel2_id")
	private Integer en1Sf1Rel2Id;
	
	@Column(name = "en1_sf1_rel2_code")
	private String en1Sf1Rel2Code;
	
	@Column(name = "en1_sf1_rel2_label")
	private String en1Sf1Rel2Label;
	
	@Column(name = "en2_sf1_id")
	private Integer en2Sf1Id;
	
	@Column(name = "en2_sf1_str1")
	private String en2Sf1Str1;
	
	@Column(name = "en2_sf1_str2")
	private String en2Sf1Str2;
	
	@Column(name = "en2_sf1_rel1_id")
	private Integer en2Sf1Rel1Id;
	
	@Column(name = "en2_sf1_rel1_code")
	private String en2Sf1Rel1Code;
	
	@Column(name = "en2_sf1_rel1_label")
	private String en2Sf1Rel1Label;
	
	@Column(name = "en2_sf1_rel2_id")
	private Integer en2Sf1Rel2Id;
	
	@Column(name = "en2_sf1_rel2_code")
	private String en2Sf1Rel2Code;
	
	@Column(name = "en2_sf1_rel2_label")
	private String en2Sf1Rel2Label;
	
	@Column(name = "en1_sf2_id")
	private Integer en1Sf2Id;
	
	@Column(name = "en1_sf2_str1")
	private String en1Sf2Str1;
	
	@Column(name = "en1_sf2_str2")
	private String en1Sf2Str2;
	
	@Column(name = "en1_sf2_rel1_id")
	private Integer en1Sf2Rel1Id;
	
	@Column(name = "en1_sf2_rel1_code")
	private String en1Sf2Rel1Code;
	
	@Column(name = "en1_sf2_rel1_label")
	private String en1Sf2Rel1Label;
	
	@Column(name = "en1_sf2_rel2_id")
	private Integer en1Sf2Rel2Id;
	
	@Column(name = "en1_sf2_rel2_code")
	private String en1Sf2Rel2Code;
	
	@Column(name = "en1_sf2_rel2_label")
	private String en1Sf2Rel2Label;
	
	@Column(name = "en2_sf2_id")
	private Integer en2Sf2Id;
	
	@Column(name = "en2_sf2_str1")
	private String en2Sf2Str1;
	
	@Column(name = "en2_sf2_str2")
	private String en2Sf2Str2;
	
	@Column(name = "en2_sf2_rel1_id")
	private Integer en2Sf2Rel1Id;
	
	@Column(name = "en2_sf2_rel1_code")
	private String en2Sf2Rel1Code;
	
	@Column(name = "en2_sf2_rel1_label")
	private String en2Sf2Rel1Label;
	
	@Column(name = "en2_sf2_rel2_id")
	private Integer en2Sf2Rel2Id;
	
	@Column(name = "en2_sf2_rel2_code")
	private String en2Sf2Rel2Code;
	
	@Column(name = "en2_sf2_rel2_label")
	private String en2Sf2Rel2Label;

	@Column(name = "en1_f_id")
	private Integer en1FId;
	
	@Column(name = "en1_f_str1")
	private String en1FStr1;
	
	@Column(name = "en1_f_str2")
	private String en1FStr2;
	
	@Column(name = "en1_f_rel1_id")
	private Integer en1FRel1Id;
	
	@Column(name = "en1_f_rel1_code")
	private String en1FRel1Code;
	
	@Column(name = "en1_f_rel1_label")
	private String en1FRel1Label;
	
	@Column(name = "en1_f_rel2_id")
	private Integer en1FRel2Id;
	
	@Column(name = "en1_f_rel2_code")
	private String en1FRel2Code;
	
	@Column(name = "en1_f_rel2_label")
	private String en1FRel2Label;
	
	@Column(name = "en2_f_id")
	private Integer en2FId;
	
	@Column(name = "en2_f_str1")
	private String en2FStr1;
	
	@Column(name = "en2_f_str2")
	private String en2FStr2;
	
	@Column(name = "en2_f_rel1_id")
	private Integer en2FRel1Id;
	
	@Column(name = "en2_f_rel1_code")
	private String en2FRel1Code;
	
	@Column(name = "en2_f_rel1_label")
	private String en2FRel1Label;
	
	@Column(name = "en2_f_rel2_id")
	private Integer en2FRel2Id;
	
	@Column(name = "en2_f_rel2_code")
	private String en2FRel2Code;
	
	@Column(name = "en2_f_rel2_label")
	private String en2FRel2Label;

	public Integer getDrId() {
		return drId;
	}

	public Integer getDrType() {
		return drType;
	}

	public String getDrResultQf1() {
		return drResultQf1;
	}

	public String getDrResultQf2() {
		return drResultQf2;
	}

	public String getDrResultQf3() {
		return drResultQf3;
	}

	public String getDrResultQf4() {
		return drResultQf4;
	}

	public String getDrResultSf1() {
		return drResultSf1;
	}

	public String getDrResultSf2() {
		return drResultSf2;
	}

	public String getEn1Qf1Str1() {
		return en1Qf1Str1;
	}

	public String getEn1Qf1Str2() {
		return en1Qf1Str2;
	}

	public Integer getEn1Qf1Rel1Id() {
		return en1Qf1Rel1Id;
	}

	public String getEn1Qf1Rel1Code() {
		return en1Qf1Rel1Code;
	}

	public String getEn1Qf1Rel1Label() {
		return en1Qf1Rel1Label;
	}

	public Integer getEn1Qf1Rel2Id() {
		return en1Qf1Rel2Id;
	}

	public String getEn1Qf1Rel2Code() {
		return en1Qf1Rel2Code;
	}

	public String getEn1Qf1Rel2Label() {
		return en1Qf1Rel2Label;
	}

	public String getEn2Qf1Str1() {
		return en2Qf1Str1;
	}

	public String getEn2Qf1Str2() {
		return en2Qf1Str2;
	}

	public Integer getEn2Qf1Rel1Id() {
		return en2Qf1Rel1Id;
	}

	public String getEn2Qf1Rel1Code() {
		return en2Qf1Rel1Code;
	}

	public String getEn2Qf1Rel1Label() {
		return en2Qf1Rel1Label;
	}

	public Integer getEn2Qf1Rel2Id() {
		return en2Qf1Rel2Id;
	}

	public String getEn2Qf1Rel2Code() {
		return en2Qf1Rel2Code;
	}

	public String getEn2Qf1Rel2Label() {
		return en2Qf1Rel2Label;
	}

	public String getEn1Qf2Str1() {
		return en1Qf2Str1;
	}

	public String getEn1Qf2Str2() {
		return en1Qf2Str2;
	}

	public Integer getEn1Qf2Rel1Id() {
		return en1Qf2Rel1Id;
	}

	public String getEn1Qf2Rel1Code() {
		return en1Qf2Rel1Code;
	}

	public String getEn1Qf2Rel1Label() {
		return en1Qf2Rel1Label;
	}

	public Integer getEn1Qf2Rel2Id() {
		return en1Qf2Rel2Id;
	}

	public String getEn1Qf2Rel2Code() {
		return en1Qf2Rel2Code;
	}

	public String getEn1Qf2Rel2Label() {
		return en1Qf2Rel2Label;
	}

	public String getEn2Qf2Str1() {
		return en2Qf2Str1;
	}

	public String getEn2Qf2Str2() {
		return en2Qf2Str2;
	}

	public Integer getEn2Qf2Rel1Id() {
		return en2Qf2Rel1Id;
	}

	public String getEn2Qf2Rel1Code() {
		return en2Qf2Rel1Code;
	}

	public String getEn2Qf2Rel1Label() {
		return en2Qf2Rel1Label;
	}

	public Integer getEn2Qf2Rel2Id() {
		return en2Qf2Rel2Id;
	}

	public String getEn2Qf2Rel2Code() {
		return en2Qf2Rel2Code;
	}

	public String getEn2Qf2Rel2Label() {
		return en2Qf2Rel2Label;
	}

	public String getEn1Qf3Str1() {
		return en1Qf3Str1;
	}

	public String getEn1Qf3Str2() {
		return en1Qf3Str2;
	}

	public Integer getEn1Qf3Rel1Id() {
		return en1Qf3Rel1Id;
	}

	public String getEn1Qf3Rel1Code() {
		return en1Qf3Rel1Code;
	}

	public String getEn1Qf3Rel1Label() {
		return en1Qf3Rel1Label;
	}

	public Integer getEn1Qf3Rel2Id() {
		return en1Qf3Rel2Id;
	}

	public String getEn1Qf3Rel2Code() {
		return en1Qf3Rel2Code;
	}

	public String getEn1Qf3Rel2Label() {
		return en1Qf3Rel2Label;
	}

	public String getEn2Qf3Str1() {
		return en2Qf3Str1;
	}

	public String getEn2Qf3Str2() {
		return en2Qf3Str2;
	}

	public Integer getEn2Qf3Rel1Id() {
		return en2Qf3Rel1Id;
	}

	public String getEn2Qf3Rel1Code() {
		return en2Qf3Rel1Code;
	}

	public String getEn2Qf3Rel1Label() {
		return en2Qf3Rel1Label;
	}

	public Integer getEn2Qf3Rel2Id() {
		return en2Qf3Rel2Id;
	}

	public String getEn2Qf3Rel2Code() {
		return en2Qf3Rel2Code;
	}

	public String getEn2Qf3Rel2Label() {
		return en2Qf3Rel2Label;
	}

	public String getEn1Qf4Str1() {
		return en1Qf4Str1;
	}

	public String getEn1Qf4Str2() {
		return en1Qf4Str2;
	}

	public Integer getEn1Qf4Rel1Id() {
		return en1Qf4Rel1Id;
	}

	public String getEn1Qf4Rel1Code() {
		return en1Qf4Rel1Code;
	}

	public String getEn1Qf4Rel1Label() {
		return en1Qf4Rel1Label;
	}

	public Integer getEn1Qf4Rel2Id() {
		return en1Qf4Rel2Id;
	}

	public String getEn1Qf4Rel2Code() {
		return en1Qf4Rel2Code;
	}

	public String getEn1Qf4Rel2Label() {
		return en1Qf4Rel2Label;
	}

	public String getEn2Qf4Str1() {
		return en2Qf4Str1;
	}

	public String getEn2Qf4Str2() {
		return en2Qf4Str2;
	}

	public Integer getEn2Qf4Rel1Id() {
		return en2Qf4Rel1Id;
	}

	public String getEn2Qf4Rel1Code() {
		return en2Qf4Rel1Code;
	}

	public String getEn2Qf4Rel1Label() {
		return en2Qf4Rel1Label;
	}

	public Integer getEn2Qf4Rel2Id() {
		return en2Qf4Rel2Id;
	}

	public String getEn2Qf4Rel2Code() {
		return en2Qf4Rel2Code;
	}

	public String getEn2Qf4Rel2Label() {
		return en2Qf4Rel2Label;
	}

	public String getEn1Sf1Str1() {
		return en1Sf1Str1;
	}

	public String getEn1Sf1Str2() {
		return en1Sf1Str2;
	}

	public Integer getEn1Sf1Rel1Id() {
		return en1Sf1Rel1Id;
	}

	public String getEn1Sf1Rel1Code() {
		return en1Sf1Rel1Code;
	}

	public String getEn1Sf1Rel1Label() {
		return en1Sf1Rel1Label;
	}

	public Integer getEn1Sf1Rel2Id() {
		return en1Sf1Rel2Id;
	}

	public String getEn1Sf1Rel2Code() {
		return en1Sf1Rel2Code;
	}

	public String getEn1Sf1Rel2Label() {
		return en1Sf1Rel2Label;
	}

	public String getEn2Sf1Str1() {
		return en2Sf1Str1;
	}

	public String getEn2Sf1Str2() {
		return en2Sf1Str2;
	}

	public Integer getEn2Sf1Rel1Id() {
		return en2Sf1Rel1Id;
	}

	public String getEn2Sf1Rel1Code() {
		return en2Sf1Rel1Code;
	}

	public String getEn2Sf1Rel1Label() {
		return en2Sf1Rel1Label;
	}

	public Integer getEn2Sf1Rel2Id() {
		return en2Sf1Rel2Id;
	}

	public String getEn2Sf1Rel2Code() {
		return en2Sf1Rel2Code;
	}

	public String getEn2Sf1Rel2Label() {
		return en2Sf1Rel2Label;
	}

	public String getEn1Sf2Str1() {
		return en1Sf2Str1;
	}

	public String getEn1Sf2Str2() {
		return en1Sf2Str2;
	}

	public Integer getEn1Sf2Rel1Id() {
		return en1Sf2Rel1Id;
	}

	public String getEn1Sf2Rel1Code() {
		return en1Sf2Rel1Code;
	}

	public String getEn1Sf2Rel1Label() {
		return en1Sf2Rel1Label;
	}

	public Integer getEn1Sf2Rel2Id() {
		return en1Sf2Rel2Id;
	}

	public String getEn1Sf2Rel2Code() {
		return en1Sf2Rel2Code;
	}

	public String getEn1Sf2Rel2Label() {
		return en1Sf2Rel2Label;
	}

	public String getEn2Sf2Str1() {
		return en2Sf2Str1;
	}

	public String getEn2Sf2Str2() {
		return en2Sf2Str2;
	}

	public Integer getEn2Sf2Rel1Id() {
		return en2Sf2Rel1Id;
	}

	public String getEn2Sf2Rel1Code() {
		return en2Sf2Rel1Code;
	}

	public String getEn2Sf2Rel1Label() {
		return en2Sf2Rel1Label;
	}

	public Integer getEn2Sf2Rel2Id() {
		return en2Sf2Rel2Id;
	}

	public String getEn2Sf2Rel2Code() {
		return en2Sf2Rel2Code;
	}

	public String getEn2Sf2Rel2Label() {
		return en2Sf2Rel2Label;
	}

	public String getEn1FStr1() {
		return en1FStr1;
	}

	public String getEn1FStr2() {
		return en1FStr2;
	}

	public Integer getEn1FRel1Id() {
		return en1FRel1Id;
	}

	public String getEn1FRel1Code() {
		return en1FRel1Code;
	}

	public String getEn1FRel1Label() {
		return en1FRel1Label;
	}

	public Integer getEn1FRel2Id() {
		return en1FRel2Id;
	}

	public String getEn1FRel2Code() {
		return en1FRel2Code;
	}

	public String getEn1FRel2Label() {
		return en1FRel2Label;
	}

	public String getEn2FStr1() {
		return en2FStr1;
	}

	public String getEn2FStr2() {
		return en2FStr2;
	}

	public Integer getEn2FRel1Id() {
		return en2FRel1Id;
	}

	public String getEn2FRel1Code() {
		return en2FRel1Code;
	}

	public String getEn2FRel1Label() {
		return en2FRel1Label;
	}

	public Integer getEn2FRel2Id() {
		return en2FRel2Id;
	}

	public String getEn2FRel2Code() {
		return en2FRel2Code;
	}

	public String getEn2FRel2Label() {
		return en2FRel2Label;
	}

	public void setDrId(Integer drId) {
		this.drId = drId;
	}

	public void setDrType(Integer drType) {
		this.drType = drType;
	}

	public void setDrResultQf1(String drResultQf1) {
		this.drResultQf1 = drResultQf1;
	}

	public void setDrResultQf2(String drResultQf2) {
		this.drResultQf2 = drResultQf2;
	}

	public void setDrResultQf3(String drResultQf3) {
		this.drResultQf3 = drResultQf3;
	}

	public void setDrResultQf4(String drResultQf4) {
		this.drResultQf4 = drResultQf4;
	}

	public void setDrResultSf1(String drResultSf1) {
		this.drResultSf1 = drResultSf1;
	}

	public void setDrResultSf2(String drResultSf2) {
		this.drResultSf2 = drResultSf2;
	}

	public void setEn1Qf1Str1(String en1Qf1Str1) {
		this.en1Qf1Str1 = en1Qf1Str1;
	}

	public void setEn1Qf1Str2(String en1Qf1Str2) {
		this.en1Qf1Str2 = en1Qf1Str2;
	}

	public void setEn1Qf1Rel1Id(Integer en1Qf1Rel1Id) {
		this.en1Qf1Rel1Id = en1Qf1Rel1Id;
	}

	public void setEn1Qf1Rel1Code(String en1Qf1Rel1Code) {
		this.en1Qf1Rel1Code = en1Qf1Rel1Code;
	}

	public void setEn1Qf1Rel1Label(String en1Qf1Rel1Label) {
		this.en1Qf1Rel1Label = en1Qf1Rel1Label;
	}

	public void setEn1Qf1Rel2Id(Integer en1Qf1Rel2Id) {
		this.en1Qf1Rel2Id = en1Qf1Rel2Id;
	}

	public void setEn1Qf1Rel2Code(String en1Qf1Rel2Code) {
		this.en1Qf1Rel2Code = en1Qf1Rel2Code;
	}

	public void setEn1Qf1Rel2Label(String en1Qf1Rel2Label) {
		this.en1Qf1Rel2Label = en1Qf1Rel2Label;
	}

	public void setEn2Qf1Str1(String en2Qf1Str1) {
		this.en2Qf1Str1 = en2Qf1Str1;
	}

	public void setEn2Qf1Str2(String en2Qf1Str2) {
		this.en2Qf1Str2 = en2Qf1Str2;
	}

	public void setEn2Qf1Rel1Id(Integer en2Qf1Rel1Id) {
		this.en2Qf1Rel1Id = en2Qf1Rel1Id;
	}

	public void setEn2Qf1Rel1Code(String en2Qf1Rel1Code) {
		this.en2Qf1Rel1Code = en2Qf1Rel1Code;
	}

	public void setEn2Qf1Rel1Label(String en2Qf1Rel1Label) {
		this.en2Qf1Rel1Label = en2Qf1Rel1Label;
	}

	public void setEn2Qf1Rel2Id(Integer en2Qf1Rel2Id) {
		this.en2Qf1Rel2Id = en2Qf1Rel2Id;
	}

	public void setEn2Qf1Rel2Code(String en2Qf1Rel2Code) {
		this.en2Qf1Rel2Code = en2Qf1Rel2Code;
	}

	public void setEn2Qf1Rel2Label(String en2Qf1Rel2Label) {
		this.en2Qf1Rel2Label = en2Qf1Rel2Label;
	}

	public void setEn1Qf2Str1(String en1Qf2Str1) {
		this.en1Qf2Str1 = en1Qf2Str1;
	}

	public void setEn1Qf2Str2(String en1Qf2Str2) {
		this.en1Qf2Str2 = en1Qf2Str2;
	}

	public void setEn1Qf2Rel1Id(Integer en1Qf2Rel1Id) {
		this.en1Qf2Rel1Id = en1Qf2Rel1Id;
	}

	public void setEn1Qf2Rel1Code(String en1Qf2Rel1Code) {
		this.en1Qf2Rel1Code = en1Qf2Rel1Code;
	}

	public void setEn1Qf2Rel1Label(String en1Qf2Rel1Label) {
		this.en1Qf2Rel1Label = en1Qf2Rel1Label;
	}

	public void setEn1Qf2Rel2Id(Integer en1Qf2Rel2Id) {
		this.en1Qf2Rel2Id = en1Qf2Rel2Id;
	}

	public void setEn1Qf2Rel2Code(String en1Qf2Rel2Code) {
		this.en1Qf2Rel2Code = en1Qf2Rel2Code;
	}

	public void setEn1Qf2Rel2Label(String en1Qf2Rel2Label) {
		this.en1Qf2Rel2Label = en1Qf2Rel2Label;
	}

	public void setEn2Qf2Str1(String en2Qf2Str1) {
		this.en2Qf2Str1 = en2Qf2Str1;
	}

	public void setEn2Qf2Str2(String en2Qf2Str2) {
		this.en2Qf2Str2 = en2Qf2Str2;
	}

	public void setEn2Qf2Rel1Id(Integer en2Qf2Rel1Id) {
		this.en2Qf2Rel1Id = en2Qf2Rel1Id;
	}

	public void setEn2Qf2Rel1Code(String en2Qf2Rel1Code) {
		this.en2Qf2Rel1Code = en2Qf2Rel1Code;
	}

	public void setEn2Qf2Rel1Label(String en2Qf2Rel1Label) {
		this.en2Qf2Rel1Label = en2Qf2Rel1Label;
	}

	public void setEn2Qf2Rel2Id(Integer en2Qf2Rel2Id) {
		this.en2Qf2Rel2Id = en2Qf2Rel2Id;
	}

	public void setEn2Qf2Rel2Code(String en2Qf2Rel2Code) {
		this.en2Qf2Rel2Code = en2Qf2Rel2Code;
	}

	public void setEn2Qf2Rel2Label(String en2Qf2Rel2Label) {
		this.en2Qf2Rel2Label = en2Qf2Rel2Label;
	}

	public void setEn1Qf3Str1(String en1Qf3Str1) {
		this.en1Qf3Str1 = en1Qf3Str1;
	}

	public void setEn1Qf3Str2(String en1Qf3Str2) {
		this.en1Qf3Str2 = en1Qf3Str2;
	}

	public void setEn1Qf3Rel1Id(Integer en1Qf3Rel1Id) {
		this.en1Qf3Rel1Id = en1Qf3Rel1Id;
	}

	public void setEn1Qf3Rel1Code(String en1Qf3Rel1Code) {
		this.en1Qf3Rel1Code = en1Qf3Rel1Code;
	}

	public void setEn1Qf3Rel1Label(String en1Qf3Rel1Label) {
		this.en1Qf3Rel1Label = en1Qf3Rel1Label;
	}

	public void setEn1Qf3Rel2Id(Integer en1Qf3Rel2Id) {
		this.en1Qf3Rel2Id = en1Qf3Rel2Id;
	}

	public void setEn1Qf3Rel2Code(String en1Qf3Rel2Code) {
		this.en1Qf3Rel2Code = en1Qf3Rel2Code;
	}

	public void setEn1Qf3Rel2Label(String en1Qf3Rel2Label) {
		this.en1Qf3Rel2Label = en1Qf3Rel2Label;
	}

	public void setEn2Qf3Str1(String en2Qf3Str1) {
		this.en2Qf3Str1 = en2Qf3Str1;
	}

	public void setEn2Qf3Str2(String en2Qf3Str2) {
		this.en2Qf3Str2 = en2Qf3Str2;
	}

	public void setEn2Qf3Rel1Id(Integer en2Qf3Rel1Id) {
		this.en2Qf3Rel1Id = en2Qf3Rel1Id;
	}

	public void setEn2Qf3Rel1Code(String en2Qf3Rel1Code) {
		this.en2Qf3Rel1Code = en2Qf3Rel1Code;
	}

	public void setEn2Qf3Rel1Label(String en2Qf3Rel1Label) {
		this.en2Qf3Rel1Label = en2Qf3Rel1Label;
	}

	public void setEn2Qf3Rel2Id(Integer en2Qf3Rel2Id) {
		this.en2Qf3Rel2Id = en2Qf3Rel2Id;
	}

	public void setEn2Qf3Rel2Code(String en2Qf3Rel2Code) {
		this.en2Qf3Rel2Code = en2Qf3Rel2Code;
	}

	public void setEn2Qf3Rel2Label(String en2Qf3Rel2Label) {
		this.en2Qf3Rel2Label = en2Qf3Rel2Label;
	}

	public void setEn1Qf4Str1(String en1Qf4Str1) {
		this.en1Qf4Str1 = en1Qf4Str1;
	}

	public void setEn1Qf4Str2(String en1Qf4Str2) {
		this.en1Qf4Str2 = en1Qf4Str2;
	}

	public void setEn1Qf4Rel1Id(Integer en1Qf4Rel1Id) {
		this.en1Qf4Rel1Id = en1Qf4Rel1Id;
	}

	public void setEn1Qf4Rel1Code(String en1Qf4Rel1Code) {
		this.en1Qf4Rel1Code = en1Qf4Rel1Code;
	}

	public void setEn1Qf4Rel1Label(String en1Qf4Rel1Label) {
		this.en1Qf4Rel1Label = en1Qf4Rel1Label;
	}

	public void setEn1Qf4Rel2Id(Integer en1Qf4Rel2Id) {
		this.en1Qf4Rel2Id = en1Qf4Rel2Id;
	}

	public void setEn1Qf4Rel2Code(String en1Qf4Rel2Code) {
		this.en1Qf4Rel2Code = en1Qf4Rel2Code;
	}

	public void setEn1Qf4Rel2Label(String en1Qf4Rel2Label) {
		this.en1Qf4Rel2Label = en1Qf4Rel2Label;
	}

	public void setEn2Qf4Str1(String en2Qf4Str1) {
		this.en2Qf4Str1 = en2Qf4Str1;
	}

	public void setEn2Qf4Str2(String en2Qf4Str2) {
		this.en2Qf4Str2 = en2Qf4Str2;
	}

	public void setEn2Qf4Rel1Id(Integer en2Qf4Rel1Id) {
		this.en2Qf4Rel1Id = en2Qf4Rel1Id;
	}

	public void setEn2Qf4Rel1Code(String en2Qf4Rel1Code) {
		this.en2Qf4Rel1Code = en2Qf4Rel1Code;
	}

	public void setEn2Qf4Rel1Label(String en2Qf4Rel1Label) {
		this.en2Qf4Rel1Label = en2Qf4Rel1Label;
	}

	public void setEn2Qf4Rel2Id(Integer en2Qf4Rel2Id) {
		this.en2Qf4Rel2Id = en2Qf4Rel2Id;
	}

	public void setEn2Qf4Rel2Code(String en2Qf4Rel2Code) {
		this.en2Qf4Rel2Code = en2Qf4Rel2Code;
	}

	public void setEn2Qf4Rel2Label(String en2Qf4Rel2Label) {
		this.en2Qf4Rel2Label = en2Qf4Rel2Label;
	}

	public void setEn1Sf1Str1(String en1Sf1Str1) {
		this.en1Sf1Str1 = en1Sf1Str1;
	}

	public void setEn1Sf1Str2(String en1Sf1Str2) {
		this.en1Sf1Str2 = en1Sf1Str2;
	}

	public void setEn1Sf1Rel1Id(Integer en1Sf1Rel1Id) {
		this.en1Sf1Rel1Id = en1Sf1Rel1Id;
	}

	public void setEn1Sf1Rel1Code(String en1Sf1Rel1Code) {
		this.en1Sf1Rel1Code = en1Sf1Rel1Code;
	}

	public void setEn1Sf1Rel1Label(String en1Sf1Rel1Label) {
		this.en1Sf1Rel1Label = en1Sf1Rel1Label;
	}

	public void setEn1Sf1Rel2Id(Integer en1Sf1Rel2Id) {
		this.en1Sf1Rel2Id = en1Sf1Rel2Id;
	}

	public void setEn1Sf1Rel2Code(String en1Sf1Rel2Code) {
		this.en1Sf1Rel2Code = en1Sf1Rel2Code;
	}

	public void setEn1Sf1Rel2Label(String en1Sf1Rel2Label) {
		this.en1Sf1Rel2Label = en1Sf1Rel2Label;
	}

	public void setEn2Sf1Str1(String en2Sf1Str1) {
		this.en2Sf1Str1 = en2Sf1Str1;
	}

	public void setEn2Sf1Str2(String en2Sf1Str2) {
		this.en2Sf1Str2 = en2Sf1Str2;
	}

	public void setEn2Sf1Rel1Id(Integer en2Sf1Rel1Id) {
		this.en2Sf1Rel1Id = en2Sf1Rel1Id;
	}

	public void setEn2Sf1Rel1Code(String en2Sf1Rel1Code) {
		this.en2Sf1Rel1Code = en2Sf1Rel1Code;
	}

	public void setEn2Sf1Rel1Label(String en2Sf1Rel1Label) {
		this.en2Sf1Rel1Label = en2Sf1Rel1Label;
	}

	public void setEn2Sf1Rel2Id(Integer en2Sf1Rel2Id) {
		this.en2Sf1Rel2Id = en2Sf1Rel2Id;
	}

	public void setEn2Sf1Rel2Code(String en2Sf1Rel2Code) {
		this.en2Sf1Rel2Code = en2Sf1Rel2Code;
	}

	public void setEn2Sf1Rel2Label(String en2Sf1Rel2Label) {
		this.en2Sf1Rel2Label = en2Sf1Rel2Label;
	}

	public void setEn1Sf2Str1(String en1Sf2Str1) {
		this.en1Sf2Str1 = en1Sf2Str1;
	}

	public void setEn1Sf2Str2(String en1Sf2Str2) {
		this.en1Sf2Str2 = en1Sf2Str2;
	}

	public void setEn1Sf2Rel1Id(Integer en1Sf2Rel1Id) {
		this.en1Sf2Rel1Id = en1Sf2Rel1Id;
	}

	public void setEn1Sf2Rel1Code(String en1Sf2Rel1Code) {
		this.en1Sf2Rel1Code = en1Sf2Rel1Code;
	}

	public void setEn1Sf2Rel1Label(String en1Sf2Rel1Label) {
		this.en1Sf2Rel1Label = en1Sf2Rel1Label;
	}

	public void setEn1Sf2Rel2Id(Integer en1Sf2Rel2Id) {
		this.en1Sf2Rel2Id = en1Sf2Rel2Id;
	}

	public void setEn1Sf2Rel2Code(String en1Sf2Rel2Code) {
		this.en1Sf2Rel2Code = en1Sf2Rel2Code;
	}

	public void setEn1Sf2Rel2Label(String en1Sf2Rel2Label) {
		this.en1Sf2Rel2Label = en1Sf2Rel2Label;
	}

	public void setEn2Sf2Str1(String en2Sf2Str1) {
		this.en2Sf2Str1 = en2Sf2Str1;
	}

	public void setEn2Sf2Str2(String en2Sf2Str2) {
		this.en2Sf2Str2 = en2Sf2Str2;
	}

	public void setEn2Sf2Rel1Id(Integer en2Sf2Rel1Id) {
		this.en2Sf2Rel1Id = en2Sf2Rel1Id;
	}

	public void setEn2Sf2Rel1Code(String en2Sf2Rel1Code) {
		this.en2Sf2Rel1Code = en2Sf2Rel1Code;
	}

	public void setEn2Sf2Rel1Label(String en2Sf2Rel1Label) {
		this.en2Sf2Rel1Label = en2Sf2Rel1Label;
	}

	public void setEn2Sf2Rel2Id(Integer en2Sf2Rel2Id) {
		this.en2Sf2Rel2Id = en2Sf2Rel2Id;
	}

	public void setEn2Sf2Rel2Code(String en2Sf2Rel2Code) {
		this.en2Sf2Rel2Code = en2Sf2Rel2Code;
	}

	public void setEn2Sf2Rel2Label(String en2Sf2Rel2Label) {
		this.en2Sf2Rel2Label = en2Sf2Rel2Label;
	}

	public void setEn1FStr1(String en1fStr1) {
		en1FStr1 = en1fStr1;
	}

	public void setEn1FStr2(String en1fStr2) {
		en1FStr2 = en1fStr2;
	}

	public void setEn1FRel1Id(Integer en1fRel1Id) {
		en1FRel1Id = en1fRel1Id;
	}

	public void setEn1FRel1Code(String en1fRel1Code) {
		en1FRel1Code = en1fRel1Code;
	}

	public void setEn1FRel1Label(String en1fRel1Label) {
		en1FRel1Label = en1fRel1Label;
	}

	public void setEn1FRel2Id(Integer en1fRel2Id) {
		en1FRel2Id = en1fRel2Id;
	}

	public void setEn1FRel2Code(String en1fRel2Code) {
		en1FRel2Code = en1fRel2Code;
	}

	public void setEn1FRel2Label(String en1fRel2Label) {
		en1FRel2Label = en1fRel2Label;
	}

	public void setEn2FStr1(String en2fStr1) {
		en2FStr1 = en2fStr1;
	}

	public void setEn2FStr2(String en2fStr2) {
		en2FStr2 = en2fStr2;
	}

	public void setEn2FRel1Id(Integer en2fRel1Id) {
		en2FRel1Id = en2fRel1Id;
	}

	public void setEn2FRel1Code(String en2fRel1Code) {
		en2FRel1Code = en2fRel1Code;
	}

	public void setEn2FRel1Label(String en2fRel1Label) {
		en2FRel1Label = en2fRel1Label;
	}

	public void setEn2FRel2Id(Integer en2fRel2Id) {
		en2FRel2Id = en2fRel2Id;
	}

	public void setEn2FRel2Code(String en2fRel2Code) {
		en2FRel2Code = en2fRel2Code;
	}

	public void setEn2FRel2Label(String en2fRel2Label) {
		en2FRel2Label = en2fRel2Label;
	}

	public String getRsResultF() {
		return rsResultF;
	}

	public void setRsResultF(String rsResultF) {
		this.rsResultF = rsResultF;
	}

	public Integer getEn1Qf1Id() {
		return en1Qf1Id;
	}

	public Integer getEn2Qf1Id() {
		return en2Qf1Id;
	}

	public Integer getEn1Qf2Id() {
		return en1Qf2Id;
	}

	public Integer getEn2Qf2Id() {
		return en2Qf2Id;
	}

	public Integer getEn1Qf3Id() {
		return en1Qf3Id;
	}

	public Integer getEn2Qf3Id() {
		return en2Qf3Id;
	}

	public Integer getEn1Qf4Id() {
		return en1Qf4Id;
	}

	public Integer getEn2Qf4Id() {
		return en2Qf4Id;
	}

	public Integer getEn1Sf1Id() {
		return en1Sf1Id;
	}

	public Integer getEn2Sf1Id() {
		return en2Sf1Id;
	}

	public Integer getEn1Sf2Id() {
		return en1Sf2Id;
	}

	public Integer getEn2Sf2Id() {
		return en2Sf2Id;
	}

	public Integer getEn1FId() {
		return en1FId;
	}

	public Integer getEn2FId() {
		return en2FId;
	}

	public void setEn1Qf1Id(Integer en1Qf1Id) {
		this.en1Qf1Id = en1Qf1Id;
	}

	public void setEn2Qf1Id(Integer en2Qf1Id) {
		this.en2Qf1Id = en2Qf1Id;
	}

	public void setEn1Qf2Id(Integer en1Qf2Id) {
		this.en1Qf2Id = en1Qf2Id;
	}

	public void setEn2Qf2Id(Integer en2Qf2Id) {
		this.en2Qf2Id = en2Qf2Id;
	}

	public void setEn1Qf3Id(Integer en1Qf3Id) {
		this.en1Qf3Id = en1Qf3Id;
	}

	public void setEn2Qf3Id(Integer en2Qf3Id) {
		this.en2Qf3Id = en2Qf3Id;
	}

	public void setEn1Qf4Id(Integer en1Qf4Id) {
		this.en1Qf4Id = en1Qf4Id;
	}

	public void setEn2Qf4Id(Integer en2Qf4Id) {
		this.en2Qf4Id = en2Qf4Id;
	}

	public void setEn1Sf1Id(Integer en1Sf1Id) {
		this.en1Sf1Id = en1Sf1Id;
	}

	public void setEn2Sf1Id(Integer en2Sf1Id) {
		this.en2Sf1Id = en2Sf1Id;
	}

	public void setEn1Sf2Id(Integer en1Sf2Id) {
		this.en1Sf2Id = en1Sf2Id;
	}

	public void setEn2Sf2Id(Integer en2Sf2Id) {
		this.en2Sf2Id = en2Sf2Id;
	}

	public void setEn1FId(Integer en1fId) {
		en1FId = en1fId;
	}

	public void setEn2FId(Integer en2fId) {
		en2FId = en2fId;
	}

	public String getYrLabel() {
		return yrLabel;
	}

	public void setYrLabel(String yrLabel) {
		this.yrLabel = yrLabel;
	}
	
}