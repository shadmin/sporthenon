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
@Table(name = "\"PERSON\"")
public class Athlete {
	
	public static final transient String alias = "PR";

	@Id
	@SequenceGenerator(name = "sq_person", sequenceName = "\"SQ_PERSON\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_person")
	private Integer id;
	
	@Column(name = "last_name", length = 30, nullable = false)
	private String lastName;
	
	@Column(name = "first_name", length = 30)
	private String firstName;
	
	@ManyToOne
	@JoinColumn(name = "id_country")
	private Country country;
	
	@ManyToOne
	@JoinColumn(name = "id_team")
	private Team team;
	
	@ManyToOne
	@JoinColumn(name = "id_sport", nullable = false)
	private Sport sport;
	
	@Column(name = "link")
	private Integer link;
	
	@Column(name = "url_wiki")
	private String urlWiki;
	
	@Column(name = "url_olyref")
	private String urlOlyref;
	
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public Integer getLink() {
		return link;
	}

	public void setLink(Integer link) {
		this.link = link;
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
		return lastName + ", " + firstName + (team != null ? ", " + team : "") + (country != null ? ", " + country : "") + (sport != null ? ", " + sport : "") + " [#" + id + "]";
	}
	
	public String toString2() {
		return lastName + ", " + firstName + (country != null ? " [" + country.getCode() + (team != null ? ", " + team.getLabel() : "") + "]" : "");
	}
	
}