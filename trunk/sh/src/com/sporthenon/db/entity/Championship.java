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
@Table(name = "\"CHAMPIONSHIP\"")
public class Championship {
	
	public static final transient String alias = "CP";
	
	@Id
	@SequenceGenerator(name = "sq_championship", sequenceName = "\"SQ_CHAMPIONSHIP\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_championship")
	private Integer id;
	
	@Column(name = "label", length = 40, nullable = false)
	private String label;
	
	@Column(name = "label_fr", length = 40, nullable = false)
	private String labelFR;

	@Column(name = "website", length = 50)
	private String website;
	
	@Column(name = "index")
	private Integer index;
	
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
		this.labelFR = label;
	}

	public String getLabelFr() {
		return labelFR;
	}

	public void setLabelFr(String labelFr) {
		this.labelFR = labelFr;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
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
		return label + " [#" + id + "]";
	}

}