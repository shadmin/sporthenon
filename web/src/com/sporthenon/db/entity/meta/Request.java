package com.sporthenon.db.entity.meta;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "\"~Request\"")
public class Request {

	@Id
	@SequenceGenerator(name = "seq_request", sequenceName = "\"~SeqRequest\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_request")
	private Integer id;
	
	@Column(name = "path", length = 20)
	private String path;
	
	@Column(name = "params", length = 30)
	private String params;
	
	@Column(name = "date")
	private Timestamp date;

	@Column(name = "user_agent", length = 200)
	private String userAgent;
	
	public Integer getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

	public String getParams() {
		return params;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
}