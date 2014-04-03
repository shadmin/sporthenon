package com.sporthenon.db.entity.meta;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"~NEWS\"")
public class News {
	
	@Column(name = "date_text", length = 20, nullable = false)
	private String dateText;
	
	@Column(name = "text_html", length = 300)
	private String textHtml;

	@Id
	@Column(name = "date")
	private Timestamp date;

	public String getDateText() {
		return dateText;
	}

	public String getTextHtml() {
		return textHtml;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDateText(String dateText) {
		this.dateText = dateText;
	}

	public void setTextHtml(String textHtml) {
		this.textHtml = textHtml;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	
}