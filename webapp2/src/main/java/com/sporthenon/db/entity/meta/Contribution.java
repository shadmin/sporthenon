package com.sporthenon.db.entity.meta;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class Contribution extends AbstractEntity {

	private Integer   id;
	private Integer	  idItem;
	private Integer   idContributor;
	private Character type;
	private Timestamp date;

	public static final transient String alias 	= "CO";
	public static final transient String table 	= "_contribution";
	public static final transient String cols 	= "id_item,id_contributor,type,date";
	
	public Contribution() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setIdItem((Integer)mapValues.get("id_item"));
			setIdContributor((Integer)mapValues.get("id_contributor"));
			setType((Character)mapValues.get("type"));
			setDate((Timestamp)mapValues.get("date"));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public Integer getIdItem() {
		return idItem;
	}

	public Integer getIdContributor() {
		return idContributor;
	}

	public Character getType() {
		return type;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}

	public void setIdContributor(Integer idContributor) {
		this.idContributor = idContributor;
	}

	public void setType(Character type) {
		this.type = type;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Contribution [id=" + id + ", idItem=" + idItem + ", idContributor=" + idContributor + ", type=" + type
				+ ", date=" + date + "]";
	}
	
}