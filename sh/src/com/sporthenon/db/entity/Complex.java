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
@Table(name = "\"Complex\"")
public class Complex {
	
	public static final transient String alias = "CX";

	@Id
	@SequenceGenerator(name = "seq_complex", sequenceName = "\"SeqComplex\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_complex")
	private Integer id;
	
	@Column(name = "label", length = 40, nullable = false)
	private String label;
	
	@Column(name = "label_fr", length = 40, nullable = false)
	private String labelFR;
	
	@ManyToOne
	@JoinColumn(name = "id_city", nullable = false)
	private City city;
	
	@Column(name = "photo_source")
	private String photoSource;
	
	@Column(name = "link")
	private Integer link;
	
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

	public String getLabelFr() {
		return labelFR;
	}

	public void setLabelFr(String labelFr) {
		this.labelFR = labelFr;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getLabel(String lang) {
		return (lang != null && lang.equalsIgnoreCase("fr") ? labelFR : label);
	}
	
	public Integer getLink() {
		return link;
	}

	public void setLink(Integer link) {
		this.link = link;
	}

	public Integer getRef() {
		return ref;
	}

	public void setRef(Integer ref) {
		this.ref = ref;
	}
	
	public String getPhotoSource() {
		return photoSource;
	}

	public void setPhotoSource(String photoSource) {
		this.photoSource = photoSource;
	}

	@Override
	public String toString() {
		return label + (city != null ? ", " + city : "") + " [#" + id + "]";
	}
	
	public String toString2(String lang) {
		return getLabel(lang) + (city != null ? ", " + city.toString2(lang) : "");
	}
	
}