package com.sporthenon.db.entity.meta;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "\"~CONTRIBUTION\"")
public class Contribution {

	public static final transient String alias = "CO";
	
	@Id
	@SequenceGenerator(name = "sq_contribution", sequenceName = "\"~SQ_CONTRIBUTION\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_contribution")
	private Integer id;
	
	@Column(name = "id_item", nullable = false)
	private Integer idItem;
	
	@Column(name = "id_member", nullable = false)
	private Integer idMember;
	
	@Column(name = "\"type\"")
	private Character type;
	
	@Column(name = "date", nullable = false)
	private Timestamp date;

	public Integer getId() {
		return id;
	}

	public Integer getIdItem() {
		return idItem;
	}

	public Integer getIdMember() {
		return idMember;
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

	public void setIdMember(Integer idMember) {
		this.idMember = idMember;
	}

	public void setType(Character type) {
		this.type = type;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	
}