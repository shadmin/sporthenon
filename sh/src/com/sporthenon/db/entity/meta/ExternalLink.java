package com.sporthenon.db.entity.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "\"~ExternalLink\"")
public class ExternalLink {
	
	@Id
	@SequenceGenerator(name = "seq_external_link", sequenceName = "\"~SeqExternalLink\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_external_link")
	private Integer id;

	@Column(name = "entity", length = 2, nullable = false)
	private String entity;
	
	@Column(name = "id_item", nullable = false)
	private Integer idItem;
	
	@Column(name = "type", length = 10, nullable = false)
	private String type;
	
	@Column(name = "url", length = 200, nullable = false)
	private String url;
	
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

	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
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

	public void setType(String type) {
		this.type = type;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
}