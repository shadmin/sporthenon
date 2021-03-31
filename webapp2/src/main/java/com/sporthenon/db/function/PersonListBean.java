package com.sporthenon.db.function;

public class PersonListBean {

	private Integer plId;
	private Integer rsId;
	private Integer plRank;
	private String plIndex;
	private Integer prId;
	private String prLastName;
	private String prFirstName;
	private Integer prCountryId;
	private String prCountryCode;

	public Integer getPlId() {
		return plId;
	}

	public Integer getPlRank() {
		return plRank;
	}

	public Integer getPrId() {
		return prId;
	}

	public String getPrLastName() {
		return prLastName;
	}

	public String getPrFirstName() {
		return prFirstName;
	}

	public Integer getPrCountryId() {
		return prCountryId;
	}

	public String getPrCountryCode() {
		return prCountryCode;
	}

	public void setPlId(Integer plId) {
		this.plId = plId;
	}

	public void setPlRank(Integer plRank) {
		this.plRank = plRank;
	}

	public void setPrId(Integer prId) {
		this.prId = prId;
	}

	public void setPrLastName(String prLastName) {
		this.prLastName = prLastName;
	}

	public void setPrFirstName(String prFirstName) {
		this.prFirstName = prFirstName;
	}

	public void setPrCountryId(Integer prCountryId) {
		this.prCountryId = prCountryId;
	}

	public void setPrCountryCode(String prCountryCode) {
		this.prCountryCode = prCountryCode;
	}

	public Integer getRsId() {
		return rsId;
	}

	public void setRsId(Integer rsId) {
		this.rsId = rsId;
	}

	public String getPlIndex() {
		return plIndex;
	}

	public void setPlIndex(String plIndex) {
		this.plIndex = plIndex;
	}
	
}