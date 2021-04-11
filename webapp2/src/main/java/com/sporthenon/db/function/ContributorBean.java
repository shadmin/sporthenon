package com.sporthenon.db.function;

public class ContributorBean {

	private Integer id;
	private String login;
	private String name;
	private Long countA;
	private Long countU;
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

	public Long getCountA() {
		return countA;
	}

	public Long getCountU() {
		return countU;
	}

	public void setCountA(Long countA) {
		this.countA = countA;
	}

	public void setCountU(Long countU) {
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