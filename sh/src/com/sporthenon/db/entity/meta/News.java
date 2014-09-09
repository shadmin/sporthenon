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
@Table(name = "\"~NEWS\"")
public class News {
	
	@Id
	@SequenceGenerator(name = "sq_news", sequenceName = "\"~SQ_NEWS\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_news")
	private Integer id;
	
	@Column(name = "title", length = 100, nullable = false)
	private String title;
	
	@Column(name = "text_html", length = 300)
	private String textHtml;
	
	@Column(name = "title_fr", length = 100)
	private String titleFR;
	
	@Column(name = "text_html_fr", length = 300)
	private String textHtmlFR;

	@Column(name = "date")
	private Timestamp date;

	public String getTextHtml() {
		return textHtml;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setTextHtml(String textHtml) {
		this.textHtml = textHtml;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleFR() {
		return titleFR;
	}

	public String getTextHtmlFR() {
		return textHtmlFR;
	}

	public void setTitleFR(String titleFR) {
		this.titleFR = titleFR;
	}

	public void setTextHtmlFR(String textHtmlFR) {
		this.textHtmlFR = textHtmlFR;
	}

	@Override
	public String toString() {
		return title;
	}
	
}