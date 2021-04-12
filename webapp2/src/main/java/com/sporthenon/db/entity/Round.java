package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class Round extends AbstractEntity {

	private Integer   id;
	private Integer   idResult;
	private Integer   idResultType;
	private RoundType roundType;
	private Integer   idRank1;
	private String 	  result1;
	private Integer   idRank2;
	private String 	  result2;
	private Integer   idRank3;
	private String    result3;
	private Integer   idRank4;
	private String    result4;
	private Integer   idRank5;
	private String    result5;
	private City  	  city1;
	private Complex   complex1;
	private City 	  city2;
	private Complex   complex2;
	private String    date1;
	private String    date2;
	private String 	  exa;
	private String 	  comment;

	public static final transient String alias 	= "RD";
	public static final transient String table 	= "round";
	public static final transient String key 	= "id";
	public static final transient String cols 	= "id_result,id_result_type,id_round_type,id_rank1,result1,id_rank2,result2,"
			+ "id_rank3,result3,id_rank4,result4,id_rank5,result5,id_city,id_city1,id_city,id_complex1,date1,date2,exa,comment";
	public static final transient String query 	= "SELECT T.*, "
			+ " CX1.label AS cx1_label, CT1.label AS ct1_label, CT1.label_fr AS ct1_label_fr, "
			+ " CX2.label AS cx2_label, CT2.label AS ct2_label, CT2.label_fr AS ct2_label_fr "
			+ " FROM round T LEFT JOIN complex CX1 ON CX1.id = T.id_complex"
			+ " LEFT JOIN city CT1 ON CT1.id = T.id_city"
			+ " LEFT JOIN complex CX2 ON CX2.id = T.id_complex1"
			+ " LEFT JOIN city CT2 ON CT2.id = T.id_city1";
	
	public Round() {}
	
	public Round(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			setIdResult((Integer)mapValues.get("id_result"));
			setIdResultType((Integer)mapValues.get("id_result_type"));
			Integer idRoundType = (Integer)mapValues.get("id_round_type");
			if (idRoundType != null) {
				setRoundType(new RoundType());
				getRoundType().setValuesFromMap(extractEntityColumns(RoundType.alias, idRoundType, mapValues));
			}
			setIdRank1((Integer)mapValues.get("id_rank1"));
			setIdRank2((Integer)mapValues.get("id_rank2"));
			setIdRank3((Integer)mapValues.get("id_rank3"));
			setIdRank4((Integer)mapValues.get("id_rank4"));
			setIdRank5((Integer)mapValues.get("id_rank5"));
			setResult1((String)mapValues.get("result1"));
			setResult2((String)mapValues.get("result2"));
			setResult3((String)mapValues.get("result3"));
			setResult4((String)mapValues.get("result4"));
			setResult5((String)mapValues.get("result5"));
			Integer idCity1 = (Integer)mapValues.get("id_city");
			if (idCity1 != null) {
				setCity1(new City());
				getCity1().setValuesFromMap(extractEntityColumns("CT1", idCity1, mapValues));
			}
			Integer idCity2 = (Integer)mapValues.get("id_city1");
			if (idCity2 != null) {
				setCity2(new City());
				getCity2().setValuesFromMap(extractEntityColumns("CT2", idCity2, mapValues));
			}
			Integer idComplex1 = (Integer)mapValues.get("id_complex");
			if (idComplex1 != null) {
				setComplex1(new Complex());
				getComplex1().setValuesFromMap(extractEntityColumns("CX1", idComplex1, mapValues));
			}
			Integer idComplex2 = (Integer)mapValues.get("id_complex1");
			if (idComplex2 != null) {
				setComplex2(new Complex());
				getComplex2().setValuesFromMap(extractEntityColumns("CX2", idComplex2, mapValues));
			}
			setDate1((String)mapValues.get("date1"));
			setDate2((String)mapValues.get("date2"));
			setExa((String)mapValues.get("exa"));
			setComment((String)mapValues.get("comment"));
			setMetadata(new Metadata((Integer)mapValues.get("id_contributor"), (Timestamp)mapValues.get("first_update"), (Timestamp)mapValues.get("last_update")));
		}
	}
	
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

	public Integer getIdRank4() {
		return idRank4;
	}

	public String getResult4() {
		return result4;
	}

	public Integer getIdRank5() {
		return idRank5;
	}

	public String getResult5() {
		return result5;
	}

	public void setIdRank4(Integer idRank4) {
		this.idRank4 = idRank4;
	}

	public void setResult4(String result4) {
		this.result4 = result4;
	}

	public void setIdRank5(Integer idRank5) {
		this.idRank5 = idRank5;
	}

	public void setResult5(String result5) {
		this.result5 = result5;
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
				+ ", comment=" + comment + "]";
	}

}