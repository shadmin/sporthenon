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
	@SequenceGenerator(name = "seq_contribution", sequenceName = "\"~SeqContribution\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_contribution")
	private Integer id;
	
	@Column(name = "id_item", nullable = false)
	private Integer idItem;
	
	@Column(name = "id_contributor", nullable = false)
	private Integer idContributor;
	
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
	
}