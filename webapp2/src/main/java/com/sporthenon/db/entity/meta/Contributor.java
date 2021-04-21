package com.sporthenon.db.entity.meta;

import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;
import com.sporthenon.utils.StringUtils;

public class Contributor extends AbstractEntity {

	
	private Integer id;
	private String 	login;
	private String 	publicName;
	private String 	password;
	private String 	email;
	private Boolean active;
	private Boolean admin;
	private String 	sports;
	
	public static final transient String alias 	= "CB";
	public static final transient String table 	= "_contributor";
	public static final transient String cols 	= "login,public_name,password,email,active,admin,sports";
	
	public Contributor() {}
	
	public Contributor(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setLogin((String)mapValues.get("login"));
			setPublicName((String)mapValues.get("public_name"));
			setPassword((String)mapValues.get("password"));
			setEmail((String)mapValues.get("email"));
			setActive((Boolean)mapValues.get("active"));
			setAdmin((Boolean)mapValues.get("admin"));
			setSports((String)mapValues.get("sports"));
		}
	}
	
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

	public boolean isActive() {
		return (active != null && active);
	}
	
	public boolean isAdmin() {
		return (admin != null && admin);
	}

	public boolean isSport(Integer id) {
		return ((admin != null && admin) || (StringUtils.notEmpty(sports) && sports.matches("(^|.*\\,)" + id + "(\\,.*|$)")));
	}
	
	@Override
	public String toString() {
		return "Contributor [id=" + id + ", login=" + login + ", publicName=" + publicName + ", password=" + password
				+ ", email=" + email + ", active=" + active + ", admin=" + admin + ", sports=" + sports + "]";
	}
	
}