package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class OlympicRanking extends AbstractEntity {
	
	private Integer id;
	private Olympics olympics;
	private Country country;
	private Integer countGold;
	private Integer countSilver;
	private Integer countBronze;
	
	public static final transient String alias = "OR";
	public static final transient String table = "olympic_ranking";
	public static final transient String key = 	 "id";
	
	public OlympicRanking() {}
	
	public OlympicRanking(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			Integer idOlympics = (Integer)mapValues.get("id_olympics");
			if (idOlympics != null) {
				setOlympics(new Olympics(idOlympics));
			}
			Integer idCountry = (Integer)mapValues.get("id_country");
			if (idCountry != null) {
				setCountry(new Country(idCountry));	
			}
			setCountGold((Integer)mapValues.get("count_gold"));
			setCountSilver((Integer)mapValues.get("count_silver"));
			setCountBronze((Integer)mapValues.get("count_bronze"));
			setMetadata(new Metadata((Integer)mapValues.get("id_contributor"), (Timestamp)mapValues.get("first_update"), (Timestamp)mapValues.get("last_update")));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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