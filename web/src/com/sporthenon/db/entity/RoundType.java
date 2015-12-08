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
@Table(name = "\"RoundType\"")
public class RoundType {

	public static final transient String alias = "RT";
	
	@Id
	@SequenceGenerator(name = "seq_round_type", sequenceName = "\"SeqRoundType\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_round_type")
	private Integer id;
	
	@Column(name = "label", length = 25, nullable = false)
	private String label;
	
	@Column(name = "label_fr", length = 25, nullable = false)
	private String labelFR;
	
	@Column(name = "\"index\"", nullable = false)
	private Integer index;
	
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

	public String getLabelFr() {
		return labelFR;
	}

	public void setLabelFr(String labelFr) {
		this.labelFR = labelFr;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getLabel(String lang) {
		return (lang != null && lang.equalsIgnoreCase("fr") ? labelFR : label);
	}

	@Override
	public String toString() {
		return label + " [#" + id + "]";
	}
	
}