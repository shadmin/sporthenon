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
@Table(name = "\"SPORT\"")
public class Sport {
	
	public static final transient String alias = "SP";

	@Id
	@SequenceGenerator(name = "sq_sport", sequenceName = "\"SQ_SPORT\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_sport")
	private Integer id;
	
	@Column(name = "label", length = 25, nullable = false)
	private String label;
	
	@Column(name = "label_fr", length = 25, nullable = false)
	private String labelFr;
	
	@Column(name = "website", length = 50)
	private String website;
	
	@Column(name = "\"type\"")
	private Integer type;
	
	@Column(name = "url_wiki")
	private String urlWiki;
	
	@Column(name = "url_olyref")
	private String urlOlyref;
	
	@Column(name = "wiki_pattern")
	private String wikiPattern;
	
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

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrlWiki() {
		return urlWiki;
	}

	public String getUrlOlyref() {
		return urlOlyref;
	}

	public void setUrlWiki(String urlWiki) {
		this.urlWiki = urlWiki;
	}

	public void setUrlOlyref(String urlOlyref) {
		this.urlOlyref = urlOlyref;
	}

	public String getWikiPattern() {
		return wikiPattern;
	}

	public void setWikiPattern(String wikiPattern) {
		this.wikiPattern = wikiPattern;
	}

	@Override
	public String toString() {
		return label + " [#" + id + "]";
	}
	
}