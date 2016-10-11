package com.sporthenon.db.function;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OlympicRankingsBean {

	@Id
	@Column(name = "cn1_id")
	private Integer cn1Id;
	
	@Column(name = "cn1_code")
	private String cn1Code;

	@Column(name = "cn1_label")
	private String cn1Label;
	
	@Column(name = "cn1_label_en")
	private String cn1LabelEN;
	
	@Column(name = "or_count_gold")
	private Integer orCountGold;
	
	@Column(name = "or_count_silver")
	private Integer orCountSilver;
	
	@Column(name = "or_count_bronze")
	private Integer orCountBronze;
	
	@Column(name = "or_last_update")
	private Timestamp orLastUpdate;

	public Integer getCn1Id() {
		return cn1Id;
	}

	public String getCn1Code() {
		return cn1Code;
	}

	public String getCn1Label() {
		return cn1Label;
	}

	public String getCn1LabelEN() {
		return cn1LabelEN;
	}

	public Integer getOrCountGold() {
		return orCountGold;
	}

	public Integer getOrCountSilver() {
		return orCountSilver;
	}

	public Integer getOrCountBronze() {
		return orCountBronze;
	}

	public void setCn1Id(Integer cn1Id) {
		this.cn1Id = cn1Id;
	}

	public void setCn1Code(String cn1Code) {
		this.cn1Code = cn1Code;
	}

	public void setCn1Label(String cn1Label) {
		this.cn1Label = cn1Label;
	}

	public void setCn1LabelEN(String cn1LabelEN) {
		this.cn1LabelEN = cn1LabelEN;
	}

	public void setOrCountGold(Integer orCountGold) {
		this.orCountGold = orCountGold;
	}

	public void setOrCountSilver(Integer orCountSilver) {
		this.orCountSilver = orCountSilver;
	}

	public void setOrCountBronze(Integer orCountBronze) {
		this.orCountBronze = orCountBronze;
	}

	public Timestamp getOrLastUpdate() {
		return orLastUpdate;
	}

	public void setOrLastUpdate(Timestamp orLastUpdate) {
		this.orLastUpdate = orLastUpdate;
	}

}