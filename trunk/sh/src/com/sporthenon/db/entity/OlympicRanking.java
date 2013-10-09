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
@Table(name = "\"OLYMPIC_RANKING\"")
public class OlympicRanking {
	
	public static final transient String alias = "OR";

	@Id
	@SequenceGenerator(name = "sq_olympic_ranking", sequenceName = "\"SQ_OLYMPIC_RANKING\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_olympic_ranking")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_olympics", nullable = false)
	private Olympics olympics;
	
	@ManyToOne
	@JoinColumn(name = "id_country", nullable = false)
	private Country country;
	
	@Column(name = "count_gold", nullable = false)
	private Integer countGold;
	
	@Column(name = "count_silver", nullable = false)
	private Integer countSilver;
	
	@Column(name = "count_bronze", nullable = false)
	private Integer countBronze;
	
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

	public Olympics getOlympics() {
		return olympics;
	}

	public void setOlympics(Olympics olympics) {
		this.olympics = olympics;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Integer getCountGold() {
		return countGold;
	}

	public void setCountGold(Integer countGold) {
		this.countGold = countGold;
	}

	public Integer getCountSilver() {
		return countSilver;
	}

	public void setCountSilver(Integer countSilver) {
		this.countSilver = countSilver;
	}

	public Integer getCountBronze() {
		return countBronze;
	}

	public void setCountBronze(Integer countBronze) {
		this.countBronze = countBronze;
	}

	@Override
	public String toString() {
		return "OlympicRanking [id=" + id + ", olympics=" + olympics
				+ ", country=" + country + ", countGold=" + countGold
				+ ", countSilver=" + countSilver + ", countBronze="
				+ countBronze + "]";
	}
	
}