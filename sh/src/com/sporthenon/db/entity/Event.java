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
@Table(name = "\"EVENT\"")
public class Event {
	
	public static final transient String alias = "EV";

	@Id
	@SequenceGenerator(name = "sq_event", sequenceName = "\"SQ_EVENT\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_event")
	private Integer id;
	
	@Column(name = "label", length = 45, nullable = false)
	private String label;
	
	@Column(name = "label_fr", length = 45, nullable = false)
	private String labelFr;
	
	@Column(name = "website", length = 50)
	private String website;
	
	@Column(name = "\"comment\"", length = 500)
	private String comment;
	
	@Column(name = "index")
	private Integer index;
	
	@Column(name = "inactive")
	private Boolean inactive;
	
	@ManyToOne
	@JoinColumn(name = "id_type", nullable = false)
	private Type type;
	
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
		this.labelFr = label;
	}

	public String getLabelFr() {
		return labelFr;
	}

	public void setLabelFr(String labelFr) {
		this.labelFr = labelFr;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Boolean getInactive() {
		return inactive;
	}

	public void setInactive(Boolean inactive) {
		this.inactive = inactive;
	}

	@Override
	public String toString() {
		return label + " [#" + id + "]";
	}
	
}