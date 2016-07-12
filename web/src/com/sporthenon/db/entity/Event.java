package com.sporthenon.db.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sporthenon.db.entity.meta.Metadata;

@Entity
@Table(name = "\"Event\"")
public class Event {
	
	public static final transient String alias = "EV";

	@Id
	@SequenceGenerator(name = "seq_event", sequenceName = "\"SeqEvent\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_event")
	private Integer id;
	
	@Column(name = "label", length = 50, nullable = false)
	private String label;
	
	@Column(name = "label_fr", length = 50, nullable = false)
	private String labelFR;
	
	@Column(name = "\"index\"")
	private Float index;
	
	@ManyToOne
	@JoinColumn(name = "id_type", nullable = false)
	private Type type;
	
	@Column(name = "ref")
	private Integer ref;
	
	@Column(name = "no_pic")
	private Boolean nopic;
	
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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
	
	public Boolean getNopic() {
		return nopic;
	}

	public void setNopic(Boolean nopic) {
		this.nopic = nopic;
	}

	public Float getIndex() {
		return index;
	}

	public void setIndex(Float index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return label + " [#" + id + "]";
	}
	
}