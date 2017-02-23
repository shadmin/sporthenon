package com.sporthenon.db.entity.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "\"~Picture\"")
public class Picture {
	
	@Id
	@SequenceGenerator(name = "seq_picture", sequenceName = "\"~SeqPicture\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_picture")
	private Integer id;

	@Column(name = "entity", length = 2, nullable = false)
	private String entity;
	
	@Column(name = "id_item", nullable = false)
	private Integer idItem;
	
	@Column(name = "value")
	private String value;
	
	@Column(name = "source")
	private String source;
	
	@Column(name = "embedded")
	private Boolean embedded;

	public Integer getId() {
		return id;
	}

	public String getEntity() {
		return entity;
	}

	public Integer getIdItem() {
		return idItem;
	}

	public String getValue() {
		return value;
	}

	public String getSource() {
		return source;
	}

	public Boolean getEmbedded() {
		return embedded;
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

	public void setValue(String value) {
		this.value = value;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setEmbedded(Boolean embedded) {
		this.embedded = embedded;
	}
	
	public boolean isEmbedded() {
		return (embedded != null && embedded);
	}

	@Override
	public String toString() {
		return "Picture [id=" + id + ", entity=" + entity + ", idItem="
				+ idItem + ", value=" + value + ", source=" + source
				+ ", embedded=" + embedded + "]";
	}
	
}