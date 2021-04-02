package com.sporthenon.db.entity.meta;

import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class InactiveItem extends AbstractEntity {

	private Integer id;
	private Integer idSport;
	private Integer idChampionship;
	private Integer idEvent;
	private Integer idSubevent;
	private Integer idSubevent2;
	
	public static final transient String table	= "_inactive_item";
	public static final transient String key 	= "id";

	public InactiveItem() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setIdSport((Integer)mapValues.get("id_sport"));
			setIdChampionship((Integer)mapValues.get("id_championship"));
			setIdEvent((Integer)mapValues.get("id_event"));
			setIdSubevent((Integer)mapValues.get("id_subevent"));
			setIdSubevent2((Integer)mapValues.get("id_subevent2"));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdSport() {
		return idSport;
	}

	public Integer getIdChampionship() {
		return idChampionship;
	}

	public Integer getIdEvent() {
		return idEvent;
	}

	public Integer getIdSubevent() {
		return idSubevent;
	}

	public Integer getIdSubevent2() {
		return idSubevent2;
	}

	public void setIdSport(Integer idSport) {
		this.idSport = idSport;
	}

	public void setIdChampionship(Integer idChampionship) {
		this.idChampionship = idChampionship;
	}

	public void setIdEvent(Integer idEvent) {
		this.idEvent = idEvent;
	}

	public void setIdSubevent(Integer idSubevent) {
		this.idSubevent = idSubevent;
	}

	public void setIdSubevent2(Integer idSubevent2) {
		this.idSubevent2 = idSubevent2;
	}

	@Override
	public String toString() {
		return "InactiveItem [id=" + id + ", idSport=" + idSport + ", idChampionship=" + idChampionship + ", idEvent="
				+ idEvent + ", idSubevent=" + idSubevent + ", idSubevent2=" + idSubevent2 + "]";
	}
	  
}