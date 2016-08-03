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
@Table(name = "\"Round\"")
public class Round {

	public static final transient String alias = "RD";
	
	@Id
	@SequenceGenerator(name = "seq_round", sequenceName = "\"SeqRound\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_round")
	private Integer id;
	
	@Column(name = "id_result")
	private Integer idResult;
	
	@Column(name = "id_result_type")
	private Integer idResultType;
	
	@ManyToOne
	@JoinColumn(name = "id_round_type")
	private RoundType roundType;
	
	@Column(name = "id_rank1")
	private Integer idRank1;
	
	@Column(name = "result1", length = 40)
	private String result1;
	
	@Column(name = "id_rank2")
	private Integer idRank2;
	
	@Column(name = "result2", length = 20)
	private String result2;
	
	@Column(name = "id_rank3")
	private Integer idRank3;
	
	@Column(name = "result3", length = 20)
	private String result3;
	
	@ManyToOne
	@JoinColumn(name = "id_city1")
	private City city1;
	
	@ManyToOne
	@JoinColumn(name = "id_complex1")
	private Complex complex1;
	
	@ManyToOne
	@JoinColumn(name = "id_city")
	private City city2;
	
	@ManyToOne
	@JoinColumn(name = "id_complex")
	private Complex complex2;
	
	@Column(name = "date1", length = 10)
	private String date1;
	
	@Column(name = "date", length = 10)
	private String date2;
	
	@Column(name = "exa", length = 15)
	private String exa;
	
	@Column(name = "\"comment\"", length = 500)
	private String comment;
	
	@Embedded
	private Metadata metadata;

	public Integer getId() {
		return id;
	}

	public Integer getIdResult() {
		return idResult;
	}

	public Integer getIdResultType() {
		return idResultType;
	}

	public Integer getIdRank1() {
		return idRank1;
	}

	public String getResult1() {
		return result1;
	}

	public Integer getIdRank2() {
		return idRank2;
	}

	public String getResult2() {
		return result2;
	}

	public Integer getIdRank3() {
		return idRank3;
	}

	public String getResult3() {
		return result3;
	}

	public String getExa() {
		return exa;
	}

	public String getComment() {
		return comment;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIdResult(Integer idResult) {
		this.idResult = idResult;
	}

	public void setIdResultType(Integer idResultType) {
		this.idResultType = idResultType;
	}

	public void setIdRank1(Integer idRank1) {
		this.idRank1 = idRank1;
	}

	public void setResult1(String result1) {
		this.result1 = result1;
	}

	public void setIdRank2(Integer idRank2) {
		this.idRank2 = idRank2;
	}

	public void setResult2(String result2) {
		this.result2 = result2;
	}

	public void setIdRank3(Integer idRank3) {
		this.idRank3 = idRank3;
	}

	public void setResult3(String result3) {
		this.result3 = result3;
	}

	public void setExa(String exa) {
		this.exa = exa;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public RoundType getRoundType() {
		return roundType;
	}

	public void setRoundType(RoundType roundType) {
		this.roundType = roundType;
	}

	public City getCity1() {
		return city1;
	}

	public Complex getComplex1() {
		return complex1;
	}

	public City getCity2() {
		return city2;
	}

	public Complex getComplex2() {
		return complex2;
	}

	public void setCity1(City city1) {
		this.city1 = city1;
	}

	public void setComplex1(Complex complex1) {
		this.complex1 = complex1;
	}

	public void setCity2(City city2) {
		this.city2 = city2;
	}

	public void setComplex2(Complex complex2) {
		this.complex2 = complex2;
	}

	public String getDate1() {
		return date1;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	@Override
	public String toString() {
		return "Round [id=" + id + ", idResult=" + idResult + ", idResultType="
				+ idResultType + ", roundType=" + roundType + ", idRank1="
				+ idRank1 + ", result1=" + result1 + ", idRank2=" + idRank2
				+ ", result2=" + result2 + ", idRank3=" + idRank3
				+ ", result3=" + result3 + ", city1=" + city1 + ", complex1="
				+ complex1 + ", city2=" + city2 + ", complex2=" + complex2
				+ ", date1=" + date1 + ", date2=" + date2 + ", exa=" + exa
				+ ", comment=" + comment + ", metadata=" + metadata + "]";
	}

}