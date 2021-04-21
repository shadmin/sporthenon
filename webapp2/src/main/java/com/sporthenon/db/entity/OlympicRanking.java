package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class OlympicRanking extends AbstractEntity {
	
	private Integer  id;
	private Olympics olympics;
	private Country  country;
	private Integer  countGold;
	private Integer  countSilver;
	private Integer  countBronze;
	
	public static final transient String alias 	= "OR_";
	public static final transient String table 	= "olympic_ranking";
	public static final transient String cols 	= "id_olympics,id_country,count_gold,count_silver,count_bronze";
	public static final transient String query 	= "SELECT T.*, YR.id AS ol_id_year, YR.label AS ol_yr_label, "
			+ " CT.id AS ol_id_city, ct.label AS ol_ct_label, ct.label_fr AS ol_ct_label_fr, "
			+ " CN.code AS cn_code, CN.label AS cn_label, CN.label_fr AS cn_label_fr "
			+ " FROM olympic_ranking T LEFT JOIN olympics OL ON OL.id = T.id_olympics "
			+ " LEFT JOIN city CT ON CT.id = OL.id_city "
			+ " LEFT JOIN year YR ON YR.id = OL.id_year"
			+ " LEFT JOIN country CN ON CN.id = T.id_country";
	
	public OlympicRanking() {}
	
	public OlympicRanking(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			Integer idOlympics = (Integer)mapValues.get("id_olympics");
			if (idOlympics != null) {
				setOlympics(new Olympics());
				getOlympics().setValuesFromMap(extractEntityColumns(Olympics.alias, idOlympics, mapValues));
			}
			Integer idCountry = (Integer)mapValues.get("id_country");
			if (idCountry != null) {
				setCountry(new Country());
				getCountry().setValuesFromMap(extractEntityColumns(Country.alias, idCountry, mapValues));
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

	public Integer getIdOlympics() {
		return (olympics != null ? olympics.getId() : null);
	}
	
	public Integer getIdCountry() {
		return (country != null ? country.getId() : null);
	}

	public void setIdOlympics(Integer id) {
		olympics = (id != null && id > 0 ? new Olympics(id) : null);
	}
	
	public void setIdCountry(Integer id) {
		country = (id != null && id > 0 ? new Country(id) : null);
	}
	
	@Override
	public String toString() {
		return "OlympicRanking [id=" + id + ", olympics=" + olympics
				+ ", country=" + country + ", countGold=" + countGold
				+ ", countSilver=" + countSilver + ", countBronze="
				+ countBronze + "]";
	}
	
}