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
@Table(name = "\"~Import\"")
public class Import {
	
	@Id
	@SequenceGenerator(name = "seq_import", sequenceName = "\"~SeqImport\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_import")
	private Integer id;

	@Column(name = "date")
	private Timestamp date;
	
	@Column(name = "csv_content")
	private String csvContent;

	public Integer getId() {
		return id;
	}

	public Timestamp getDate() {
		return date;
	}

	public String getCsvContent() {
		return csvContent;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public void setCsvContent(String csvContent) {
		this.csvContent = csvContent;
	}

}