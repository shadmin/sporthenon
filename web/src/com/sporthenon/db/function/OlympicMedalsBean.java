package com.sporthenon.db.function;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OlympicMedalsBean {

	@Id
	@Column(name = "rs_id")
	private Integer rsId;
	
	@Column(name = "rs_last_update")
	private Timestamp rsLastUpdate;
	
	@Column(name = "rs_comment")
	private String rsComment;
	
	@Column(name = "rs_exa")
	private String rsExa;
	
	@Column(name = "ol_id")
	private Integer olId;
	
	@Column(name = "ol_type")
	private Integer olType;
	
	@Column(name = "ol_date1")
	private String olDate1;
	
	@Column(name = "ol_date2")
	private String olDate2;
	
	@Column(name = "ol_city")
	private String olCity;
	
	@Column(name = "ol_city_en")
	private String olCityEN;
	
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
	
	@Column(name = "rs_result1")
	private String rsResult1;
	
	@Column(name = "rs_result2")
	private String rsResult2;
	
	@Column(name = "rs_result3")
	private String rsResult3;
	
	@Column(name = "yr_id")
	private Integer yrId;

	@Column(name = "yr_label")
	private String yrLabel;
	
	@Column(name = "ev_id")
	private Integer evId;

	@Column(name = "ev_label")
	private String evLabel;
	
	@Column(name = "se_id")
	private Integer seId;

	@Column(name = "se_label")
	private String seLabel;

	@Column(name = "se2_id")
	private Integer se2Id;

	@Column(name = "se2_label")
	private String se2Label;
	
	@Column(name = "cx_id")
	private Integer cxId;

	@Column(name = "cx_label")
	private String cxLabel;
	
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

	@Column(name = "st1_label")
	private String st1Label;
	
	@Column(name = "st1_label_en")
	private String st1LabelEN;
	
	@Column(name = "cn1_id")
	private Integer cn1Id;
	
	@Column(name = "cn1_code_")
	private String cn1Code_;

	@Column(name = "cn1_label_")
	private String cn1Label_;
	
	@Column(name = "cn1_label_en_")
	private String cn1LabelEN_;
	
	@Column(name = "ct2_id")
	private Integer ct2Id;

	@Column(name = "ct2_label")
	private String ct2Label;
	
	@Column(name = "st2_id")
	private Integer st2Id;
	
	@Column(name = "st2_code")
	private String st2Code;

	@Column(name = "st2_label")
	private String st2Label;
	
	@Column(name = "cn2_id")
	private Integer cn2Id;
	
	@Column(name = "cn2_code_")
	private String cn2Code_;

	@Column(name = "cn2_label_")
	private String cn2Label_;
	
	@Column(name = "pr1_last_name")
	private String pr1LastName;
	
	@Column(name = "pr1_first_name")
	private String pr1FirstName;
	
	@Column(name = "pr1_cn_id")
	private Integer pr1CnId;
	
	@Column(name = "pr1_cn_code")
	private String pr1CnCode;
	
	@Column(name = "pr1_cn_label")
	private String pr1CnLabel;
	
	@Column(name = "pr1_cn_label_en")
	private String pr1CnLabelEN;
	
	@Column(name = "cn1_code")
	private String cn1Code;
	
	@Column(name = "cn1_label")
	private String cn1Label;
	
	@Column(name = "cn1_label_en")
	private String cn1LabelEN;
	
	@Column(name = "pr2_last_name")
	private String pr2LastName;
	
	@Column(name = "pr2_first_name")
	private String pr2FirstName;
	
	@Column(name = "pr2_cn_id")
	private Integer pr2CnId;
	
	@Column(name = "pr2_cn_code")
	private String pr2CnCode;
	
	@Column(name = "pr2_cn_label")
	private String pr2CnLabel;
	
	@Column(name = "pr2_cn_label_en")
	private String pr2CnLabelEN;
	
	@Column(name = "cn2_code")
	private String cn2Code;
	
	@Column(name = "cn2_label")
	private String cn2Label;
	
	@Column(name = "cn2_label_en")
	private String cn2LabelEN;
	
	@Column(name = "pr3_last_name")
	private String pr3LastName;
	
	@Column(name = "pr3_first_name")
	private String pr3FirstName;
	
	@Column(name = "pr3_cn_id")
	private Integer pr3CnId;
	
	@Column(name = "pr3_cn_code")
	private String pr3CnCode;
	
	@Column(name = "pr3_cn_label")
	private String pr3CnLabel;
	
	@Column(name = "pr3_cn_label_en")
	private String pr3CnLabelEN;
	
	@Column(name = "cn3_code")
	private String cn3Code;
	
	@Column(name = "cn3_label")
	private String cn3Label;
	
	@Column(name = "cn3_label_en")
	private String cn3LabelEN;
	
	@Column(name = "pr4_last_name")
	private String pr4LastName;
	
	@Column(name = "pr4_first_name")
	private String pr4FirstName;
	
	@Column(name = "pr4_cn_id")
	private Integer pr4CnId;
	
	@Column(name = "pr4_cn_code")
	private String pr4CnCode;
	
	@Column(name = "pr4_cn_label")
	private String pr4CnLabel;
	
	@Column(name = "pr4_cn_label_en")
	private String pr4CnLabelEN;
	
	@Column(name = "cn4_code")
	private String cn4Code;
	
	@Column(name = "cn4_label")
	private String cn4Label;
	
	@Column(name = "cn4_label_en")
	private String cn4LabelEN;
	
	@Column(name = "pr5_last_name")
	private String pr5LastName;
	
	@Column(name = "pr5_first_name")
	private String pr5FirstName;
	
	@Column(name = "pr5_cn_id")
	private Integer pr5CnId;
	
	@Column(name = "pr5_cn_code")
	private String pr5CnCode;
	
	@Column(name = "pr5_cn_label")
	private String pr5CnLabel;
	
	@Column(name = "pr5_cn_label_en")
	private String pr5CnLabelEN;
	
	@Column(name = "cn5_code")
	private String cn5Code;
	
	@Column(name = "cn5_label")
	private String cn5Label;
	
	@Column(name = "cn5_label_en")
	private String cn5LabelEN;
	
	@Column(name = "tp1_number")
	private Integer tp1Number;

	@Column(name = "tp2_number")
	private Integer tp2Number;

	@Column(name = "tp3_number")
	private Integer tp3Number;
	
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

	public Integer getEvId() {
		return evId;
	}

	public void setEvId(Integer evId) {
		this.evId = evId;
	}

	public String getEvLabel() {
		return evLabel;
	}

	public void setEvLabel(String evLabel) {
		this.evLabel = evLabel;
	}

	public String getPr1LastName() {
		return pr1LastName;
	}

	public void setPr1LastName(String pr1LastName) {
		this.pr1LastName = pr1LastName;
	}

	public String getPr1FirstName() {
		return pr1FirstName;
	}

	public void setPr1FirstName(String pr1FirstName) {
		this.pr1FirstName = pr1FirstName;
	}

	public Integer getPr1CnId() {
		return pr1CnId;
	}

	public void setPr1CnId(Integer pr1CnId) {
		this.pr1CnId = pr1CnId;
	}

	public String getPr1CnCode() {
		return pr1CnCode;
	}

	public void setPr1CnCode(String pr1CnCode) {
		this.pr1CnCode = pr1CnCode;
	}

	public String getPr1CnLabel() {
		return pr1CnLabel;
	}

	public void setPr1CnLabel(String pr1CnLabel) {
		this.pr1CnLabel = pr1CnLabel;
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

	public String getPr2LastName() {
		return pr2LastName;
	}

	public void setPr2LastName(String pr2LastName) {
		this.pr2LastName = pr2LastName;
	}

	public String getPr2FirstName() {
		return pr2FirstName;
	}

	public void setPr2FirstName(String pr2FirstName) {
		this.pr2FirstName = pr2FirstName;
	}

	public Integer getPr2CnId() {
		return pr2CnId;
	}

	public void setPr2CnId(Integer pr2CnId) {
		this.pr2CnId = pr2CnId;
	}

	public String getPr2CnCode() {
		return pr2CnCode;
	}

	public void setPr2CnCode(String pr2CnCode) {
		this.pr2CnCode = pr2CnCode;
	}

	public String getPr2CnLabel() {
		return pr2CnLabel;
	}

	public void setPr2CnLabel(String pr2CnLabel) {
		this.pr2CnLabel = pr2CnLabel;
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

	public String getPr3LastName() {
		return pr3LastName;
	}

	public void setPr3LastName(String pr3LastName) {
		this.pr3LastName = pr3LastName;
	}

	public String getPr3FirstName() {
		return pr3FirstName;
	}

	public void setPr3FirstName(String pr3FirstName) {
		this.pr3FirstName = pr3FirstName;
	}

	public Integer getPr3CnId() {
		return pr3CnId;
	}

	public void setPr3CnId(Integer pr3CnId) {
		this.pr3CnId = pr3CnId;
	}

	public String getPr3CnCode() {
		return pr3CnCode;
	}

	public void setPr3CnCode(String pr3CnCode) {
		this.pr3CnCode = pr3CnCode;
	}

	public String getPr3CnLabel() {
		return pr3CnLabel;
	}

	public void setPr3CnLabel(String pr3CnLabel) {
		this.pr3CnLabel = pr3CnLabel;
	}

	public String getCn3Code() {
		return cn3Code;
	}

	public void setCn3Code(String cn3Code) {
		this.cn3Code = cn3Code;
	}

	public String getCn3Label() {
		return cn3Label;
	}

	public void setCn3Label(String cn3Label) {
		this.cn3Label = cn3Label;
	}

	public String getPr4LastName() {
		return pr4LastName;
	}

	public void setPr4LastName(String pr4LastName) {
		this.pr4LastName = pr4LastName;
	}

	public String getPr4FirstName() {
		return pr4FirstName;
	}

	public void setPr4FirstName(String pr4FirstName) {
		this.pr4FirstName = pr4FirstName;
	}

	public Integer getPr4CnId() {
		return pr4CnId;
	}

	public void setPr4CnId(Integer pr4CnId) {
		this.pr4CnId = pr4CnId;
	}

	public String getPr4CnCode() {
		return pr4CnCode;
	}

	public void setPr4CnCode(String pr4CnCode) {
		this.pr4CnCode = pr4CnCode;
	}

	public String getPr4CnLabel() {
		return pr4CnLabel;
	}

	public void setPr4CnLabel(String pr4CnLabel) {
		this.pr4CnLabel = pr4CnLabel;
	}

	public String getCn4Code() {
		return cn4Code;
	}

	public void setCn4Code(String cn4Code) {
		this.cn4Code = cn4Code;
	}

	public String getCn4Label() {
		return cn4Label;
	}

	public void setCn4Label(String cn4Label) {
		this.cn4Label = cn4Label;
	}

	public String getPr5LastName() {
		return pr5LastName;
	}

	public void setPr5LastName(String pr5LastName) {
		this.pr5LastName = pr5LastName;
	}

	public String getPr5FirstName() {
		return pr5FirstName;
	}

	public void setPr5FirstName(String pr5FirstName) {
		this.pr5FirstName = pr5FirstName;
	}

	public Integer getPr5CnId() {
		return pr5CnId;
	}

	public void setPr5CnId(Integer pr5CnId) {
		this.pr5CnId = pr5CnId;
	}

	public String getPr5CnCode() {
		return pr5CnCode;
	}

	public void setPr5CnCode(String pr5CnCode) {
		this.pr5CnCode = pr5CnCode;
	}

	public String getPr5CnLabel() {
		return pr5CnLabel;
	}

	public void setPr5CnLabel(String pr5CnLabel) {
		this.pr5CnLabel = pr5CnLabel;
	}

	public String getCn5Code() {
		return cn5Code;
	}

	public void setCn5Code(String cn5Code) {
		this.cn5Code = cn5Code;
	}

	public String getCn5Label() {
		return cn5Label;
	}

	public void setCn5Label(String cn5Label) {
		this.cn5Label = cn5Label;
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

	public Integer getCxId() {
		return cxId;
	}

	public String getCxLabel() {
		return cxLabel;
	}

	public Integer getCt1Id() {
		return ct1Id;
	}

	public String getCt1Label() {
		return ct1Label;
	}

	public Integer getSt1Id() {
		return st1Id;
	}

	public String getSt1Code() {
		return st1Code;
	}

	public String getSt1Label() {
		return st1Label;
	}

	public Integer getCn1Id() {
		return cn1Id;
	}

	public String getCn1Code_() {
		return cn1Code_;
	}

	public String getCn1Label_() {
		return cn1Label_;
	}

	public Integer getCt2Id() {
		return ct2Id;
	}

	public String getCt2Label() {
		return ct2Label;
	}

	public Integer getSt2Id() {
		return st2Id;
	}

	public String getSt2Code() {
		return st2Code;
	}

	public String getSt2Label() {
		return st2Label;
	}

	public Integer getCn2Id() {
		return cn2Id;
	}

	public String getCn2Code_() {
		return cn2Code_;
	}

	public String getCn2Label_() {
		return cn2Label_;
	}

	public void setCxId(Integer cxId) {
		this.cxId = cxId;
	}

	public void setCxLabel(String cxLabel) {
		this.cxLabel = cxLabel;
	}

	public void setCt1Id(Integer ct1Id) {
		this.ct1Id = ct1Id;
	}

	public void setCt1Label(String ct1Label) {
		this.ct1Label = ct1Label;
	}

	public void setSt1Id(Integer st1Id) {
		this.st1Id = st1Id;
	}

	public void setSt1Code(String st1Code) {
		this.st1Code = st1Code;
	}

	public void setSt1Label(String st1Label) {
		this.st1Label = st1Label;
	}

	public void setCn1Id(Integer cn1Id) {
		this.cn1Id = cn1Id;
	}

	public void setCn1Code_(String cn1Code_) {
		this.cn1Code_ = cn1Code_;
	}

	public void setCn1Label_(String cn1Label_) {
		this.cn1Label_ = cn1Label_;
	}

	public void setCt2Id(Integer ct2Id) {
		this.ct2Id = ct2Id;
	}

	public void setCt2Label(String ct2Label) {
		this.ct2Label = ct2Label;
	}

	public void setSt2Id(Integer st2Id) {
		this.st2Id = st2Id;
	}

	public void setSt2Code(String st2Code) {
		this.st2Code = st2Code;
	}

	public void setSt2Label(String st2Label) {
		this.st2Label = st2Label;
	}

	public void setCn2Id(Integer cn2Id) {
		this.cn2Id = cn2Id;
	}

	public void setCn2Code_(String cn2Code_) {
		this.cn2Code_ = cn2Code_;
	}

	public void setCn2Label_(String cn2Label_) {
		this.cn2Label_ = cn2Label_;
	}

	public String getRsComment() {
		return rsComment;
	}

	public String getOlCity() {
		return olCity;
	}

	public void setRsComment(String rsComment) {
		this.rsComment = rsComment;
	}

	public String getRsExa() {
		return rsExa;
	}

	public void setRsExa(String rsExa) {
		this.rsExa = rsExa;
	}

	public void setOlCity(String olCity) {
		this.olCity = olCity;
	}

	public Integer getSeId() {
		return seId;
	}

	public String getSeLabel() {
		return seLabel;
	}

	public void setSeId(Integer seId) {
		this.seId = seId;
	}

	public void setSeLabel(String seLabel) {
		this.seLabel = seLabel;
	}

	public Integer getTp1Number() {
		return tp1Number;
	}

	public Integer getTp2Number() {
		return tp2Number;
	}

	public void setTp1Number(Integer tp1Number) {
		this.tp1Number = tp1Number;
	}

	public void setTp2Number(Integer tp2Number) {
		this.tp2Number = tp2Number;
	}

	public Integer getSe2Id() {
		return se2Id;
	}

	public String getSe2Label() {
		return se2Label;
	}

	public Integer getTp3Number() {
		return tp3Number;
	}

	public void setSe2Id(Integer se2Id) {
		this.se2Id = se2Id;
	}

	public void setSe2Label(String se2Label) {
		this.se2Label = se2Label;
	}

	public void setTp3Number(Integer tp3Number) {
		this.tp3Number = tp3Number;
	}

	public String getOlCityEN() {
		return olCityEN;
	}

	public String getCn1LabelEN() {
		return cn1LabelEN;
	}

	public String getCn2LabelEN() {
		return cn2LabelEN;
	}

	public String getCn3LabelEN() {
		return cn3LabelEN;
	}

	public String getCn4LabelEN() {
		return cn4LabelEN;
	}

	public String getCn5LabelEN() {
		return cn5LabelEN;
	}

	public void setOlCityEN(String olCityEN) {
		this.olCityEN = olCityEN;
	}

	public void setCn1LabelEN(String cn1LabelEN) {
		this.cn1LabelEN = cn1LabelEN;
	}

	public void setCn2LabelEN(String cn2LabelEN) {
		this.cn2LabelEN = cn2LabelEN;
	}

	public void setCn3LabelEN(String cn3LabelEN) {
		this.cn3LabelEN = cn3LabelEN;
	}

	public void setCn4LabelEN(String cn4LabelEN) {
		this.cn4LabelEN = cn4LabelEN;
	}

	public void setCn5LabelEN(String cn5LabelEN) {
		this.cn5LabelEN = cn5LabelEN;
	}

	public String getCt1LabelEN() {
		return ct1LabelEN;
	}

	public String getSt1LabelEN() {
		return st1LabelEN;
	}

	public String getCn1LabelEN_() {
		return cn1LabelEN_;
	}

	public void setCt1LabelEN(String ct1LabelEN) {
		this.ct1LabelEN = ct1LabelEN;
	}

	public void setSt1LabelEN(String st1LabelEN) {
		this.st1LabelEN = st1LabelEN;
	}

	public void setCn1LabelEN_(String cn1LabelEN_) {
		this.cn1LabelEN_ = cn1LabelEN_;
	}

	public String getPr1CnLabelEN() {
		return pr1CnLabelEN;
	}

	public String getPr2CnLabelEN() {
		return pr2CnLabelEN;
	}

	public String getPr3CnLabelEN() {
		return pr3CnLabelEN;
	}

	public String getPr4CnLabelEN() {
		return pr4CnLabelEN;
	}

	public String getPr5CnLabelEN() {
		return pr5CnLabelEN;
	}

	public void setPr1CnLabelEN(String pr1CnLabelEN) {
		this.pr1CnLabelEN = pr1CnLabelEN;
	}

	public void setPr2CnLabelEN(String pr2CnLabelEN) {
		this.pr2CnLabelEN = pr2CnLabelEN;
	}

	public void setPr3CnLabelEN(String pr3CnLabelEN) {
		this.pr3CnLabelEN = pr3CnLabelEN;
	}

	public void setPr4CnLabelEN(String pr4CnLabelEN) {
		this.pr4CnLabelEN = pr4CnLabelEN;
	}

	public void setPr5CnLabelEN(String pr5CnLabelEN) {
		this.pr5CnLabelEN = pr5CnLabelEN;
	}

	public Timestamp getRsLastUpdate() {
		return rsLastUpdate;
	}

	public void setRsLastUpdate(Timestamp rsLastUpdate) {
		this.rsLastUpdate = rsLastUpdate;
	}
	
}