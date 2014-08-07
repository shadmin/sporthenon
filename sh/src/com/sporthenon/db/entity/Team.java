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
@Table(name = "\"TEAM\"")
public class Team {

	public static final transient String alias = "TM";
	
	@Id
	@SequenceGenerator(name = "sq_team", sequenceName = "\"SQ_TEAM\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_team")
	private Integer id;
	
	@Column(name = "label", length = 60, nullable = false)
	private String label;
	
	@ManyToOne
	@JoinColumn(name = "id_sport", nullable = false)
	private Sport sport;
	
	@Column(name = "year1", length = 4)
	private String year1;
	
	@Column(name = "year2", length = 4)
	private String year2;
	
	@ManyToOne
	@JoinColumn(name = "id_country")
	private Country country;
	
	@Column(name = "conference", length = 10)
	private String conference;
	
	@Column(name = "division", length = 10)
	private String division;
	
	@Column(name = "\"comment\"", length = 500)
	private String comment;
	
	@Column(name = "link")
	private Integer link;
	
	@Column(name = "inactive")
	private Boolean inactive;
	
	@Column(name = "url_wiki")
	private String urlWiki;
	
	@Column(name = "url_bktref")
	private String urlBktref;
	
	@Column(name = "url_bbref")
	private String urlBbref;
	
	@Column(name = "url_ftref")
	private String urlFtref;
	
	@Column(name = "url_hkref")
	private String urlHkref;
	
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

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public String getYear1() {
		return year1;
	}

	public void setYear1(String year1) {
		this.year1 = year1;
	}

	public String getYear2() {
		return year2;
	}

	public void setYear2(String year2) {
		this.year2 = year2;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getConference() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference = conference;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getLink() {
		return link;
	}

	public Boolean getInactive() {
		return inactive;
	}

	public void setLink(Integer link) {
		this.link = link;
	}

	public void setInactive(Boolean inactive) {
		this.inactive = inactive;
	}

	public String getUrlWiki() {
		return urlWiki;
	}

	public void setUrlWiki(String urlWiki) {
		this.urlWiki = urlWiki;
	}

	public String getUrlBktref() {
		return urlBktref;
	}

	public String getUrlBbref() {
		return urlBbref;
	}

	public String getUrlFtref() {
		return urlFtref;
	}

	public String getUrlHkref() {
		return urlHkref;
	}

	public void setUrlBktref(String urlBktref) {
		this.urlBktref = urlBktref;
	}

	public void setUrlBbref(String urlBbref) {
		this.urlBbref = urlBbref;
	}

	public void setUrlFtref(String urlFtref) {
		this.urlFtref = urlFtref;
	}

	public void setUrlHkref(String urlHkref) {
		this.urlHkref = urlHkref;
	}

	@Override
	public String toString() {
		return label + (country != null ? ", " + country : "") + (sport != null ? ", " + sport : "") + " [#" + id + "]";
	}
	
}