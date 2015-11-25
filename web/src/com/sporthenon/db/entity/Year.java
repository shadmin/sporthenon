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
@Table(name = "\"Year\"")
public class Year {
	
	public static final transient String alias = "YR";

	@Id
	@SequenceGenerator(name = "seq_year", sequenceName = "\"SeqYear\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_year")
	private Integer id;
	
	@Column(name = "label", length = 4, nullable = false)
	private String label;
	
	@Column(name = "ref")
	private Integer ref;
	
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel(String lang) {
		return label;
	}

	public Integer getRef() {
		return ref;
	}

	public void setRef(Integer ref) {
		this.ref = ref;
	}

	@Override
	public String toString() {
		return label + " [#" + id + "]";
	}
	
}