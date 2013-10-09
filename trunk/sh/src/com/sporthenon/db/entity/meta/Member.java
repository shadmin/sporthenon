package com.sporthenon.db.entity.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "\"~MEMBER\"")
public class Member {

	@Id
	@SequenceGenerator(name = "sq_member", sequenceName = "\"~SQ_MEMBER\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_member")
	private Integer id;
	
	@Column(name = "\"login\"", length = 15, nullable = false)
	private String login;
	
	@Column(name = "last_name", length = 20)
	private String lastName;
	
	@Column(name = "first_name", length = 20)
	private String firstName;
	
	@Column(name = "password", length = 35)
	private String password;
	
	@Column(name = "email", length = 50)
	private String email;
	
	@Column(name = "active")
	private Boolean active;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
