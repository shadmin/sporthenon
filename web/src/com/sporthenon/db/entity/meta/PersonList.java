package com.sporthenon.db.entity.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "\"~PersonList\"")
public class PersonList {
	
	@Id
	@SequenceGenerator(name = "seq_person_list", sequenceName = "\"~SeqPersonList\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_person_list")
	private Integer id;

	
	@Column(name = "id_result", nullable = false)
	private Integer idResult;
	
	@Column(name = "rank", nullable = false)
	private Integer rank;
	
	@Column(name = "id_person", nullable = false)
	private Integer idPerson;

	@Column(name = "index")
	private String index;
	
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

}