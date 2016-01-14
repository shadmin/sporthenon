package com.sporthenon.db.entity.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "\"~Redirection\"")
public class Redirection {

	public static final transient String alias = "RE";
	
	@Id
	@SequenceGenerator(name = "seq_redirection", sequenceName = "\"~SeqRedirection\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_redirection")
	private Integer id;
	
	@Column(name = "previous_path", length = 255)
	private String previousPath;
	
	@Column(name = "current_path", length = 255)
	private String currentPath;
	
	public Integer getId() {
		return id;
	}

	public String getPreviousPath() {
		return previousPath;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPreviousPath(String previousPath) {
		this.previousPath = previousPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

}