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
@Table(name = "\"CITY\"")
public class City {

	public static final transient String alias = "CT";
	
	@Id
	@SequenceGenerator(name = "sq_city", sequenceName = "\"SQ_CITY\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_city")
	private Integer id;
	
	@Column(name = "label", length = 25, nullable = false)
	private String label;
	
	@Column(name = "label_fr", length = 25, nullable = false)
	private String labelFR;
	
	@ManyToOne
	@JoinColumn(name = "id_country", nullable = false)
	private Country country;
	
	@ManyToOne
	@JoinColumn(name = "id_state")
	private State state;
	
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
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
		return label + (country != null ? ", " + country : "") + " [#" + id + "]";
	}
	
	public String toString2() {
		return label + (state != null ? ", " + state.getCode() : "") + (country != null ? ", " + country.getCode() : "");
	}

}