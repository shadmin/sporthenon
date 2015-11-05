package com.sporthenon.db.entity.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sporthenon.utils.StringUtils;

@Entity
@Table(name = "\"~Contributor\"")
public class Contributor {

	public static final transient String alias = "CB";
	
	@Id
	@SequenceGenerator(name = "seq_contributor", sequenceName = "\"~SeqContributor\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_contributor")
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
	
	@Column(name = "admin")
	private Boolean admin;
	
	@Column(name = "sports")
	private String sports;
	
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

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public String getSports() {
		return sports;
	}

	public void setSports(String sports) {
		this.sports = sports;
	}

	@Override
	public String toString() {
		return login;
	}
	
	public boolean isActive() {
		return (active != null && active);
	}
	
	public boolean isAdmin() {
		return (admin != null && admin);
	}

	public boolean isSport(Integer id) {
		return ((admin != null && admin) || (StringUtils.notEmpty(sports) && sports.matches("(^|.*\\,)" + id + "(\\,.*|$)")));
	}
	
}