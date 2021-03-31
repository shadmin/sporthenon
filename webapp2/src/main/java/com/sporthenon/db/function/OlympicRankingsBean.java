package com.sporthenon.db.function;

import java.sql.Timestamp;

public class OlympicRankingsBean {

	private Integer cn1Id;
	private String cn1Code;
	private String cn1Label;
	private String cn1LabelEN;
	private Long orCountGold;
	private Long orCountSilver;
	private Long orCountBronze;
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

	public Long getOrCountGold() {
		return orCountGold;
	}

	public Long getOrCountSilver() {
		return orCountSilver;
	}

	public Long getOrCountBronze() {
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

	public void setOrCountGold(Long orCountGold) {
		this.orCountGold = orCountGold;
	}

	public void setOrCountSilver(Long orCountSilver) {
		this.orCountSilver = orCountSilver;
	}

	public void setOrCountBronze(Long orCountBronze) {
		this.orCountBronze = orCountBronze;
	}

	public Timestamp getOrLastUpdate() {
		return orLastUpdate;
	}

	public void setOrLastUpdate(Timestamp orLastUpdate) {
		this.orLastUpdate = orLastUpdate;
	}

}