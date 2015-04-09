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
public class Contributor {

	public static final transient String alias = "CB";
	
	@Id
	@SequenceGenerator(name = "sq_member", sequenceName = "\"~SQ_MEMBER\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_member")
	private Integer id;
	
	@Column(name = "\"login\"", length = 15, nullable = false)
	private String login;
	
	@Column(name = "public_name", length = 100)
	private String publicName;
	
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

	public String getPublicName() {
		return publicName;
	}

	public void setPublicName(String publicName) {
		this.publicName = publicName;
	}
	
}