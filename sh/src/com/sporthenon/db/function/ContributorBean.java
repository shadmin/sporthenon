package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ContributorBean {

	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "login")
	private String login;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "count_a")
	private Integer countA;

	@Column(name = "count_u")
	private Integer countU;
	
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
	
}