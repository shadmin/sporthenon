package com.sporthenon.db.entity.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "\"~Translation\"")
public class Translation {
	
	@Id
	@SequenceGenerator(name = "seq_translation", sequenceName = "\"~SeqTranslation\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_translation")
	private Integer id;

	@Column(name = "entity", length = 2, nullable = false)
	private String entity;
	
	@Column(name = "id_item", nullable = false)
	private Integer idItem;
	
	@Column(name = "checked")
	private Boolean checked;

	
	public Integer getId() {
		return id;
	}


	public String getEntity() {
		return entity;
	}


	public Integer getIdItem() {
		return idItem;
	}


	public Boolean getChecked() {
		return checked;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public void setEntity(String entity) {
		this.entity = entity;
	}


	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}


	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public boolean isChecked() {
		return (checked != null && checked);
	}
	
}