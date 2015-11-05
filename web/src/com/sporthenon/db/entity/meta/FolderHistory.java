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
@Table(name = "\"~FolderHistory\"")
public class FolderHistory {

	public static final transient String alias = "FH";
	
	@Id
	@SequenceGenerator(name = "seq_folder_history", sequenceName = "\"~SeqFolderHistory\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_folder_history")
	private Integer id;
	
	@Column(name = "previous_params", length = 30)
	private String previousParams;
	
	@Column(name = "current_params", length = 30)
	private String currentParams;
	
	@Column(name = "current_path", length = 255)
	private String currentPath;
	
	@Column(name = "date")
	private Timestamp date;

	public Integer getId() {
		return id;
	}

	public String getPreviousParams() {
		return previousParams;
	}

	public String getCurrentParams() {
		return currentParams;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPreviousParams(String previousParams) {
		this.previousParams = previousParams;
	}

	public void setCurrentParams(String currentParams) {
		this.currentParams = currentParams;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}