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
@Table(name = "\"COMPLEX\"")
public class Complex {
	
	public static final transient String alias = "CX";

	@Id
	@SequenceGenerator(name = "sq_complex", sequenceName = "\"SQ_COMPLEX\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_complex")
	private Integer id;
	
	@Column(name = "label", length = 40, nullable = false)
	private String label;
	
	@Column(name = "label_fr", length = 40, nullable = false)
	private String labelFR;
	
	@ManyToOne
	@JoinColumn(name = "id_city", nullable = false)
	private City city;
	
	@Column(name = "url_wiki")
	private String urlWiki;
	
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

	public String getUrlWiki() {
		return urlWiki;
	}

	public void setUrlWiki(String urlWiki) {
		this.urlWiki = urlWiki;
	}

	public String getLabel(String lang) {
		return (lang != null && lang.equalsIgnoreCase("fr") ? labelFR : label);
	}
	
	@Override
	public String toString() {
		return label + (city != null ? ", " + city : "") + " [#" + id + "]";
	}
	
	public String toString2() {
		return label + (city != null ? ", " + city.toString2() : "");
	}
	
}