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
	
	@Column(name = "url", length = 200, nullable = false)
	private String url;
	
	@Column(name = "checked")
	private Boolean checked;
	
	@Column(name = "flag")
	private Character flag;

	public Integer getId() {
		return id;
	}

	public String getEntity() {
		return entity;
	}

	public Integer getIdItem() {
		return idItem;
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

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
	public Character getFlag() {
		return flag;
	}

	public void setFlag(Character flag) {
		this.flag = flag;
	}

	public boolean isChecked() {
		return (checked != null && checked);
	}

	@Override
	public String toString() {
		return "ExternalLink [id=" + id + ", entity=" + entity + ", idItem="
				+ idItem + ", url=" + url + ", checked=" + checked + ", flag="
				+ flag + "]";
	}
	
}