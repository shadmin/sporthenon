package com.sporthenon.db.entity.meta;

import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class PersonList extends AbstractEntity {
	
	private Integer id;
	private Integer idResult;
	private Integer rank;
	private Integer idPerson;
	private String 	index;
	
	public static final transient String table 	= "_person_list";
	public static final transient String cols 	= "id_result,rank,id_person,index";
	
	public PersonList() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setIdResult((Integer)mapValues.get("id_result"));
			setRank((Integer)mapValues.get("rank"));
			setIdPerson((Integer)mapValues.get("id_person"));
			setIndex((String)mapValues.get("index"));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public Integer getIdResult() {
		return idResult;
	}

	public Integer getRank() {
		return rank;
	}

	public Integer getIdPerson() {
		return idPerson;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIdResult(Integer idResult) {
		this.idResult = idResult;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public void setIdPerson(Integer idPerson) {
		this.idPerson = idPerson;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "PersonList [id=" + id + ", idResult=" + idResult + ", rank=" + rank + ", idPerson=" + idPerson
				+ ", index=" + index + "]";
	}

}