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
@Table(name = "\"Country\"")
public class Country {
	
	public static final transient String alias = "CN";

	@Id
	@SequenceGenerator(name = "seq_country", sequenceName = "\"SeqCountry\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_country")
	private Integer id;
	
	@Column(name = "code", length = 3, nullable = false)
	private String code;
	
	@Column(name = "label", length = 35, nullable = false)
	private String label;
	
	@Column(name = "label_fr", length = 35, nullable = false)
	private String labelFR;
	
	@Column(name = "ref")
	private Integer ref;
	
	@Embedded
	private Metadata metadata;
	
	public Country() {}
	
	public Country(String code) {
		this.code = code;
	}
	
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
	}

	public String getLabelFr() {
		return labelFR;
	}

	public void setLabelFr(String labelFr) {
		this.labelFR = labelFr;
	}

	public String getLabel(String lang) {
		return (lang != null && lang.equalsIgnoreCase("fr") ? labelFR : label);
	}
	
	public Integer getRef() {
		return ref;
	}

	public void setRef(Integer ref) {
		this.ref = ref;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Country && ((Country) obj).getCode().equals(code));
	}

	@Override
	public String toString() {
		return label + ", " + code + " [#" + id + "]";
	}
	
}