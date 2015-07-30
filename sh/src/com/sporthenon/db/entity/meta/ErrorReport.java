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
@Table(name = "\"~ERROR_REPORT\"")
public class ErrorReport {

	public static final transient String alias = "ER";
	
	@Id
	@SequenceGenerator(name = "seq_error_report", sequenceName = "\"~SeqErrorReport\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_error_report")
	private Integer id;
	
	@Column(name = "url", length = 255)
	private String url;
	
	@Column(name = "text_")
	private String text;
	
	@Column(name = "date", nullable = false)
	private Timestamp date;

	public Integer getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getText() {
		return text;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}