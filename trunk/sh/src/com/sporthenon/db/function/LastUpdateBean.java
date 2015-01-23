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
	
	@Column(name = "rs_update")
	private Timestamp rsUpdate;
	
	@Column(name = "tp1_number")
	private Integer tp1Number;

	@Column(name = "tp2_number")
	private Integer tp2Number;
	
	@Column(name = "tp3_number")
	private Integer tp3Number;
	
	@Column(name = "pr1_first_name")
	private String pr1FirstName;
	
	@Column(name = "pr1_last_name")
	private String pr1LastName;
	
	@Column(name = "pr1_country")
	private Integer pr1Country;
	
	@Column(name = "pr2_first_name")
	private String pr2FirstName;
	
	@Column(name = "pr2_last_name")
	private String pr2LastName;
	
	@Column(name = "pr2_country")
	private Integer pr2Country;
	
	@Column(name = "tm1_label")
	private String tm1Label;
	
	@Column(name = "tm2_label")
	private String tm2Label;
	
	@Column(name = "cn1_code")
	private String cn1Code;
	
	@Column(name = "cn1_label")
	private String cn1Label;
	
	@Column(name = "cn2_code")
	private String cn2Code;
	
	@Column(name = "cn2_label")
	private String cn2Label;

	public Integer getRsId() {
		return rsId;
	}

	public String getRsText1() {
		return rsText1;
	}

	public String getRsText2() {
		return rsText2;
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

	public Timestamp getRsUpdate() {
		return rsUpdate;
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

	public void setRsUpdate(Timestamp rsUpdate) {
		this.rsUpdate = rsUpdate;
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
	
}