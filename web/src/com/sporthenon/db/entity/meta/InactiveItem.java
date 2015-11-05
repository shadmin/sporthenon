package com.sporthenon.db.entity.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "\"~InactiveItem\"")
public class InactiveItem {

	@Id
	@SequenceGenerator(name = "seq_inactive_item", sequenceName = "\"~SeqInactiveItem\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_inactive_item")
	private Integer id;
	
	@Column(name = "id_sport", nullable = false)
	private Integer idSport;
	
	@Column(name = "id_championship", nullable = false)
	private Integer idChampionship;
	
	@Column(name = "id_event")
	private Integer idEvent;
	
	@Column(name = "id_subevent")
	private Integer idSubevent;
	
	@Column(name = "id_subevent2")
	private Integer idSubevent2;

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
	  
}