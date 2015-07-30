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
@Table(name = "\"LEAGUE\"")
public class League {
	
	public static final transient String alias = "LG";

	@Id
	@SequenceGenerator(name = "seq_league", sequenceName = "\"SeqLeague\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_league")
	private Integer id;
	
	@Column(name = "label", length = 5, nullable = false)
	private String label;
	
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

	@Override
	public String toString() {
		return label + " [#" + id + "]";
	}
	
}