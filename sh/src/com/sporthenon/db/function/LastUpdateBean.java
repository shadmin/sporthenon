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
	
	@Column(name = "sp_label")
	private String spLabel;
	
	@Column(name = "cp_label")
	private String cpLabel;
	
	@Column(name = "ev_label")
	private String evLabel;
	
	@Column(name = "se_label")
	private String seLabel;

	@Column(name = "rs_update")
	private Timestamp rsUpdate;
	
	@Column(name = "tp1_number")
	private Integer tp1Number;

	@Column(name = "tp2_number")
	private Integer tp2Number;
	
	@Column(name = "pr_first_name")
	private String prFirstName;
	
	@Column(name = "pr_last_name")
	private String prLastName;
	
	@Column(name = "tm_label")
	private String tmLabel;
	
	@Column(name = "cn_code")
	private String cnCode;
	
	public Integer getRsId() {
		return rsId;
	}

	public String getYrLabel() {
		return yrLabel;
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

	public void setRsId(Integer rsId) {
		this.rsId = rsId;
	}

	public void setYrLabel(String yrLabel) {
		this.yrLabel = yrLabel;
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

	public Timestamp getRsUpdate() {
		return rsUpdate;
	}

	public Integer getTp1Number() {
		return tp1Number;
	}

	public Integer getTp2Number() {
		return tp2Number;
	}

	public String getPrFirstName() {
		return prFirstName;
	}

	public String getPrLastName() {
		return prLastName;
	}

	public String getTmLabel() {
		return tmLabel;
	}

	public String getCnCode() {
		return cnCode;
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

	public void setPrFirstName(String prFirstName) {
		this.prFirstName = prFirstName;
	}

	public void setPrLastName(String prLastName) {
		this.prLastName = prLastName;
	}

	public void setTmLabel(String tmLabel) {
		this.tmLabel = tmLabel;
	}

	public void setCnCode(String cnCode) {
		this.cnCode = cnCode;
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
	
}