package com.sporthenon.db.function;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LastUpdateBean {

	@Id
	@Column(name = "rs_id")
	private Integer rsId;
	
	@Column(name = "rs_text1")
	private String rsText1;
	
	@Column(name = "rs_text2")
	private String rsText2;
	
	@Column(name = "rs_text3")
	private String rsText3;
	
	@Column(name = "rs_text4")
	private String rsText4;
	
	@Column(name = "rs_date")
	private Timestamp rsDate;
	
	@Column(name = "yr_id")
	private Integer yrId;
	
	@Column(name = "yr_label")
	private String yrLabel;
	
	@Column(name = "sp_id")
	private Integer spId;
	
	@Column(name = "cp_id")
	private Integer cpId;
	
	@Column(name = "ev_id")
	private Integer evId;
	
	@Column(name = "se_id")
	private Integer seId;

	@Column(name = "se2_id")
	private Integer se2Id;
	
	@Column(name = "sp_label")
	private String spLabel;
	
	@Column(name = "cp_label")
	private String cpLabel;
	
	@Column(name = "ev_label")
	private String evLabel;
	
	@Column(name = "se_label")
	private String seLabel;

	@Column(name = "se2_label")
	private String se2Label;

	@Column(name = "sp_label_en")
	private String spLabelEN;
	
	@Column(name = "cp_label_en")
	private String cpLabelEN;
	
	@Column(name = "ev_label_en")
	private String evLabelEN;
	
	@Column(name = "se_label_en")
	private String seLabelEN;

	@Column(name = "se2_label_en")
	private String se2LabelEN;
	
	@Column(name = "tp1_number")
	private Integer tp1Number;

	@Column(name = "tp2_number")
	private Integer tp2Number;
	
	@Column(name = "tp3_number")
	private Integer tp3Number;
	
	@Column(name = "pr1_id")
	private Integer pr1Id;
	
	@Column(name = "pr1_first_name")
	private String pr1FirstName;
	
	@Column(name = "pr1_last_name")
	private String pr1LastName;
	
	@Column(name = "pr1_country")
	private Integer pr1Country;
	
	@Column(name = "pr1_country_code")
	private String pr1CountryCode;
	
	@Column(name = "tm1_id")
	private Integer tm1Id;
	
	@Column(name = "tm1_label")
	private String tm1Label;
	
	@Column(name = "cn1_id")
	private Integer cn1Id;
	
	@Column(name = "cn1_code")
	private String cn1Code;
	
	@Column(name = "cn1_label")
	private String cn1Label;
	
	@Column(name = "cn1_label_en")
	private String cn1LabelEN;
	
	@Column(name = "pr2_id")
	private Integer pr2Id;
	
	@Column(name = "pr2_first_name")
	private String pr2FirstName;
	
	@Column(name = "pr2_last_name")
	private String pr2LastName;
	
	@Column(name = "pr2_country")
	private Integer pr2Country;
	
	@Column(name = "pr2_country_code")
	private String pr2CountryCode;
	
	@Column(name = "tm2_id")
	private Integer tm2Id;
	
	@Column(name = "tm2_label")
	private String tm2Label;

	@Column(name = "cn2_id")
	private Integer cn2Id;
	
	@Column(name = "cn2_code")
	private String cn2Code;
	
	@Column(name = "cn2_label")
	private String cn2Label;

	@Column(name = "cn2_label_en")
	private String cn2LabelEN;
	
	@Column(name = "pr3_id")
	private Integer pr3Id;
	
	@Column(name = "pr3_first_name")
	private String pr3FirstName;
	
	@Column(name = "pr3_last_name")
	private String pr3LastName;
	
	@Column(name = "pr3_country")
	private Integer pr3Country;
	
	@Column(name = "pr3_country_code")
	private String pr3CountryCode;
	
	@Column(name = "tm3_id")
	private Integer tm3Id;
	
	@Column(name = "tm3_label")
	private String tm3Label;

	@Column(name = "cn3_id")
	private Integer cn3Id;
	
	@Column(name = "cn3_code")
	private String cn3Code;
	
	@Column(name = "cn3_label")
	private String cn3Label;

	@Column(name = "cn3_label_en")
	private String cn3LabelEN;

	@Column(name = "pr4_id")
	private Integer pr4Id;
	
	@Column(name = "pr4_first_name")
	private String pr4FirstName;
	
	@Column(name = "pr4_last_name")
	private String pr4LastName;
	
	@Column(name = "pr4_country")
	private Integer pr4Country;
	
	@Column(name = "pr4_country_code")
	private String pr4CountryCode;
	
	@Column(name = "tm4_id")
	private Integer tm4Id;
	
	@Column(name = "tm4_label")
	private String tm4Label;

	@Column(name = "cn4_id")
	private Integer cn4Id;
	
	@Column(name = "cn4_code")
	private String cn4Code;
	
	@Column(name = "cn4_label")
	private String cn4Label;

	@Column(name = "cn4_label_en")
	private String cn4LabelEN;
	
	public Integer getRsId() {
		return rsId;
	}

	public String getRsText1() {
		return rsText1;
	}

	public String getRsText2() {
		return rsText2;
	}

	public String getRsText3() {
		return rsText3;
	}

	public void setRsText3(String rsText3) {
		this.rsText3 = rsText3;
	}

	public String getYrLabel() {
		return yrLabel;
	}

	public Integer getSpId() {
		return spId;
	}

	public Integer getCpId() {
		return cpId;
	}

	public Integer getEvId() {
		return evId;
	}

	public Integer getSeId() {
		return seId;
	}

	public Integer getSe2Id() {
		return se2Id;
	}

	public String getSpLabel() {
		return spLabel;
	}

	public String getCpLabel() {
		return cpLabel;
	}

	public String getEvLabel() {
		return evLabel;
	}

	public String getSeLabel() {
		return seLabel;
	}

	public String getSe2Label() {
		return se2Label;
	}

	public Integer getTp1Number() {
		return tp1Number;
	}

	public Integer getTp2Number() {
		return tp2Number;
	}

	public Integer getTp3Number() {
		return tp3Number;
	}

	public String getPr1FirstName() {
		return pr1FirstName;
	}

	public String getPr1LastName() {
		return pr1LastName;
	}

	public Integer getPr1Country() {
		return pr1Country;
	}

	public String getPr2FirstName() {
		return pr2FirstName;
	}

	public String getPr2LastName() {
		return pr2LastName;
	}

	public Integer getPr2Country() {
		return pr2Country;
	}

	public String getTm1Label() {
		return tm1Label;
	}

	public String getTm2Label() {
		return tm2Label;
	}

	public String getCn1Code() {
		return cn1Code;
	}

	public String getCn1Label() {
		return cn1Label;
	}

	public String getCn2Code() {
		return cn2Code;
	}

	public String getCn2Label() {
		return cn2Label;
	}

	public void setRsId(Integer rsId) {
		this.rsId = rsId;
	}

	public void setRsText1(String rsText1) {
		this.rsText1 = rsText1;
	}

	public void setRsText2(String rsText2) {
		this.rsText2 = rsText2;
	}

	public void setYrLabel(String yrLabel) {
		this.yrLabel = yrLabel;
	}

	public void setSpId(Integer spId) {
		this.spId = spId;
	}

	public void setCpId(Integer cpId) {
		this.cpId = cpId;
	}

	public void setEvId(Integer evId) {
		this.evId = evId;
	}

	public void setSeId(Integer seId) {
		this.seId = seId;
	}

	public void setSe2Id(Integer se2Id) {
		this.se2Id = se2Id;
	}

	public void setSpLabel(String spLabel) {
		this.spLabel = spLabel;
	}

	public void setCpLabel(String cpLabel) {
		this.cpLabel = cpLabel;
	}

	public void setEvLabel(String evLabel) {
		this.evLabel = evLabel;
	}

	public void setSeLabel(String seLabel) {
		this.seLabel = seLabel;
	}

	public void setSe2Label(String se2Label) {
		this.se2Label = se2Label;
	}

	public void setTp1Number(Integer tp1Number) {
		this.tp1Number = tp1Number;
	}

	public void setTp2Number(Integer tp2Number) {
		this.tp2Number = tp2Number;
	}

	public void setTp3Number(Integer tp3Number) {
		this.tp3Number = tp3Number;
	}

	public void setPr1FirstName(String pr1FirstName) {
		this.pr1FirstName = pr1FirstName;
	}

	public void setPr1LastName(String pr1LastName) {
		this.pr1LastName = pr1LastName;
	}

	public void setPr1Country(Integer pr1Country) {
		this.pr1Country = pr1Country;
	}

	public void setPr2FirstName(String pr2FirstName) {
		this.pr2FirstName = pr2FirstName;
	}

	public void setPr2LastName(String pr2LastName) {
		this.pr2LastName = pr2LastName;
	}

	public void setPr2Country(Integer pr2Country) {
		this.pr2Country = pr2Country;
	}

	public void setTm1Label(String tm1Label) {
		this.tm1Label = tm1Label;
	}

	public void setTm2Label(String tm2Label) {
		this.tm2Label = tm2Label;
	}

	public void setCn1Code(String cn1Code) {
		this.cn1Code = cn1Code;
	}

	public void setCn1Label(String cn1Label) {
		this.cn1Label = cn1Label;
	}

	public void setCn2Code(String cn2Code) {
		this.cn2Code = cn2Code;
	}

	public void setCn2Label(String cn2Label) {
		this.cn2Label = cn2Label;
	}

	public Integer getCn1Id() {
		return cn1Id;
	}

	public Integer getCn2Id() {
		return cn2Id;
	}

	public void setCn1Id(Integer cn1Id) {
		this.cn1Id = cn1Id;
	}

	public void setCn2Id(Integer cn2Id) {
		this.cn2Id = cn2Id;
	}

	public Timestamp getRsDate() {
		return rsDate;
	}

	public void setRsDate(Timestamp rsDate) {
		this.rsDate = rsDate;
	}

	public Integer getPr1Id() {
		return pr1Id;
	}

	public Integer getPr2Id() {
		return pr2Id;
	}

	public Integer getTm1Id() {
		return tm1Id;
	}

	public Integer getTm2Id() {
		return tm2Id;
	}

	public void setPr1Id(Integer pr1Id) {
		this.pr1Id = pr1Id;
	}

	public void setPr2Id(Integer pr2Id) {
		this.pr2Id = pr2Id;
	}

	public void setTm1Id(Integer tm1Id) {
		this.tm1Id = tm1Id;
	}

	public void setTm2Id(Integer tm2Id) {
		this.tm2Id = tm2Id;
	}

	public String getCn1LabelEN() {
		return cn1LabelEN;
	}

	public String getCn2LabelEN() {
		return cn2LabelEN;
	}

	public void setCn1LabelEN(String cn1LabelEN) {
		this.cn1LabelEN = cn1LabelEN;
	}

	public void setCn2LabelEN(String cn2LabelEN) {
		this.cn2LabelEN = cn2LabelEN;
	}

	public String getSpLabelEN() {
		return spLabelEN;
	}

	public String getCpLabelEN() {
		return cpLabelEN;
	}

	public String getEvLabelEN() {
		return evLabelEN;
	}

	public String getSeLabelEN() {
		return seLabelEN;
	}

	public String getSe2LabelEN() {
		return se2LabelEN;
	}

	public void setSpLabelEN(String spLabelEN) {
		this.spLabelEN = spLabelEN;
	}

	public void setCpLabelEN(String cpLabelEN) {
		this.cpLabelEN = cpLabelEN;
	}

	public void setEvLabelEN(String evLabelEN) {
		this.evLabelEN = evLabelEN;
	}

	public void setSeLabelEN(String seLabelEN) {
		this.seLabelEN = seLabelEN;
	}

	public void setSe2LabelEN(String se2LabelEN) {
		this.se2LabelEN = se2LabelEN;
	}

	public String getRsText4() {
		return rsText4;
	}

	public String getPr3FirstName() {
		return pr3FirstName;
	}

	public String getPr3LastName() {
		return pr3LastName;
	}

	public Integer getPr3Country() {
		return pr3Country;
	}

	public Integer getTm3Id() {
		return tm3Id;
	}

	public String getTm3Label() {
		return tm3Label;
	}

	public Integer getCn3Id() {
		return cn3Id;
	}

	public String getCn3Code() {
		return cn3Code;
	}

	public String getCn3Label() {
		return cn3Label;
	}

	public String getCn3LabelEN() {
		return cn3LabelEN;
	}

	public String getPr4FirstName() {
		return pr4FirstName;
	}

	public String getPr4LastName() {
		return pr4LastName;
	}

	public Integer getPr4Country() {
		return pr4Country;
	}

	public Integer getTm4Id() {
		return tm4Id;
	}

	public String getTm4Label() {
		return tm4Label;
	}

	public Integer getCn4Id() {
		return cn4Id;
	}

	public String getCn4Code() {
		return cn4Code;
	}

	public String getCn4Label() {
		return cn4Label;
	}

	public String getCn4LabelEN() {
		return cn4LabelEN;
	}

	public void setRsText4(String rsText4) {
		this.rsText4 = rsText4;
	}

	public void setPr3FirstName(String pr3FirstName) {
		this.pr3FirstName = pr3FirstName;
	}

	public void setPr3LastName(String pr3LastName) {
		this.pr3LastName = pr3LastName;
	}

	public void setPr3Country(Integer pr3Country) {
		this.pr3Country = pr3Country;
	}

	public void setTm3Id(Integer tm3Id) {
		this.tm3Id = tm3Id;
	}

	public void setTm3Label(String tm3Label) {
		this.tm3Label = tm3Label;
	}

	public void setCn3Id(Integer cn3Id) {
		this.cn3Id = cn3Id;
	}

	public void setCn3Code(String cn3Code) {
		this.cn3Code = cn3Code;
	}

	public void setCn3Label(String cn3Label) {
		this.cn3Label = cn3Label;
	}

	public void setCn3LabelEN(String cn3LabelEN) {
		this.cn3LabelEN = cn3LabelEN;
	}

	public void setPr4FirstName(String pr4FirstName) {
		this.pr4FirstName = pr4FirstName;
	}

	public void setPr4LastName(String pr4LastName) {
		this.pr4LastName = pr4LastName;
	}

	public void setPr4Country(Integer pr4Country) {
		this.pr4Country = pr4Country;
	}

	public void setTm4Id(Integer tm4Id) {
		this.tm4Id = tm4Id;
	}

	public void setTm4Label(String tm4Label) {
		this.tm4Label = tm4Label;
	}

	public void setCn4Id(Integer cn4Id) {
		this.cn4Id = cn4Id;
	}

	public void setCn4Code(String cn4Code) {
		this.cn4Code = cn4Code;
	}

	public void setCn4Label(String cn4Label) {
		this.cn4Label = cn4Label;
	}

	public void setCn4LabelEN(String cn4LabelEN) {
		this.cn4LabelEN = cn4LabelEN;
	}

	public Integer getPr3Id() {
		return pr3Id;
	}

	public Integer getPr4Id() {
		return pr4Id;
	}

	public void setPr3Id(Integer pr3Id) {
		this.pr3Id = pr3Id;
	}

	public void setPr4Id(Integer pr4Id) {
		this.pr4Id = pr4Id;
	}

	public String getPr1CountryCode() {
		return pr1CountryCode;
	}

	public String getPr2CountryCode() {
		return pr2CountryCode;
	}

	public String getPr3CountryCode() {
		return pr3CountryCode;
	}

	public String getPr4CountryCode() {
		return pr4CountryCode;
	}

	public void setPr1CountryCode(String pr1CountryCode) {
		this.pr1CountryCode = pr1CountryCode;
	}

	public void setPr2CountryCode(String pr2CountryCode) {
		this.pr2CountryCode = pr2CountryCode;
	}

	public void setPr3CountryCode(String pr3CountryCode) {
		this.pr3CountryCode = pr3CountryCode;
	}

	public void setPr4CountryCode(String pr4CountryCode) {
		this.pr4CountryCode = pr4CountryCode;
	}

	public Integer getYrId() {
		return yrId;
	}

	public void setYrId(Integer yrId) {
		this.yrId = yrId;
	}
	
}