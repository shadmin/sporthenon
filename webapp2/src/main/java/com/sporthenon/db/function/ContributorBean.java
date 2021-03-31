package com.sporthenon.db.function;

public class ContributorBean {

	private Integer id;
	private String login;
	private String name;
	private Integer countA;
	private Integer countU;
	private String sports;
	private String sportsFR;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public String getName() {
		return name;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCountA() {
		return countA;
	}

	public Integer getCountU() {
		return countU;
	}

	public void setCountA(Integer countA) {
		this.countA = countA;
	}

	public void setCountU(Integer countU) {
		this.countU = countU;
	}

	public String getSports() {
		return sports;
	}

	public String getSportsFR() {
		return sportsFR;
	}

	public void setSports(String sports) {
		this.sports = sports;
	}

	public void setSportsFR(String sportsFR) {
		this.sportsFR = sportsFR;
	}
	
}