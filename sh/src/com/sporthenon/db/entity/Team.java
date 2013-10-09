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
	
	@Column(name = "label", length = 35, nullable = false)
	private String label;
	
	@Column(name = "code", length = 3)
	private String code;
	
	@ManyToOne
	@JoinColumn(name = "id_sport", nullable = false)
	private Sport sport;
	
	@Column(name = "year1", length = 4)
	private String year1;
	
	@Column(name = "year2", length = 4)
	private String year2;
	
	@ManyToOne
	@JoinColumn(name = "id_parent")
	private Team parent;
	
	@Column(name = "deleted")
	private Boolean deleted;
	
	@ManyToOne
	@JoinColumn(name = "id_country")
	private Country country;
	
	@Column(name = "conference", length = 10)
	private String conference;
	
	@Column(name = "division", length = 10)
	private String division;
	
	@Column(name = "\"comment\"", length = 500)
	private String comment;
	
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Team getParent() {
		return parent;
	}

	public void setParent(Team parent) {
		this.parent = parent;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
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

	@Override
	public String toString() {
		return label + (country != null ? ", " + country : "") + (sport != null ? ", " + sport : "") + " [#" + id + "]";
	}
	
}