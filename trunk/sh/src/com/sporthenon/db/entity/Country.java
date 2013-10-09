package com.sporthenon.db.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sporthenon.db.entity.meta.Metadata;

@Entity
@Table(name = "\"COUNTRY\"")
public class Country {
	
	public static final transient String alias = "CN";

	@Id
	@SequenceGenerator(name = "sq_country", sequenceName = "\"SQ_COUNTRY\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_country")
	private Integer id;
	
	@Column(name = "code", length = 3, nullable = false)
	private String code;
	
	@Column(name = "label", length = 35, nullable = false)
	private String label;
	
	@Column(name = "label_fr", length = 35, nullable = false)
	private String labelFr;
	
	@Embedded
	private Metadata metadata;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		this.labelFr = label;
	}

	public String getLabelFr() {
		return labelFr;
	}

	public void setLabelFr(String labelFr) {
		this.labelFr = labelFr;
	}

	@Override
	public String toString() {
		return label + ", " + code + " [#" + id + "]";
	}
	
}