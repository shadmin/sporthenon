package com.sporthenon.db.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sporthenon.db.entity.meta.Metadata;

@Entity
@Table(name = "\"DRAW\"")
public class Draw {

	public static final transient String alias = "DR";
	
	@Id
	@SequenceGenerator(name = "sq_draw", sequenceName = "\"SQ_DRAW\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_draw")
	private Integer id;
	
	@Column(name = "id_result")
	private Integer idResult;
	
	@Column(name = "id1_qf1")
	private Integer id1Qf1;
	
	@Column(name = "id2_qf1")
	private Integer id2Qf1;
	
	@Column(name = "id1_qf2")
	private Integer id1Qf2;
	
	@Column(name = "id2_qf2")
	private Integer id2Qf2;
	
	@Column(name = "id1_qf3")
	private Integer id1Qf3;
	
	@Column(name = "id2_qf3")
	private Integer id2Qf3;
	
	@Column(name = "id1_qf4")
	private Integer id1Qf4;
	
	@Column(name = "id2_qf4")
	private Integer id2Qf4;
	
	@Column(name = "id1_sf1")
	private Integer id1Sf1;
	
	@Column(name = "id2_sf1")
	private Integer id2Sf1;
	
	@Column(name = "id1_sf2")
	private Integer id1Sf2;
	
	@Column(name = "id2_sf2")
	private Integer id2Sf2;
	
	@Column(name = "id1_thd")
	private Integer id1Thd;
	
	@Column(name = "id2_thd")
	private Integer id2Thd;
	
	@Column(name = "result_qf1")
	private String result_qf1;
	
	@Column(name = "result_qf2")
	private String result_qf2;

	@Column(name = "result_qf3")
	private String result_qf3;
	
	@Column(name = "result_qf4")
	private String result_qf4;
	
	@Column(name = "result_sf1")
	private String result_sf1;
	
	@Column(name = "result_sf2")
	private String result_sf2;
	
	@Column(name = "result_thd")
	private String result_thd;
	
	@Embedded
	private Metadata metadata;

	public Integer getId() {
		return id;
	}

	public Integer getIdResult() {
		return idResult;
	}

	public void setIdResult(Integer idResult) {
		this.idResult = idResult;
	}

	public Integer getId1Qf1() {
		return id1Qf1;
	}

	public Integer getId2Qf1() {
		return id2Qf1;
	}

	public Integer getId1Qf2() {
		return id1Qf2;
	}

	public Integer getId2Qf2() {
		return id2Qf2;
	}

	public Integer getId1Qf3() {
		return id1Qf3;
	}

	public Integer getId2Qf3() {
		return id2Qf3;
	}

	public Integer getId1Qf4() {
		return id1Qf4;
	}

	public Integer getId2Qf4() {
		return id2Qf4;
	}

	public Integer getId1Sf1() {
		return id1Sf1;
	}

	public Integer getId2Sf1() {
		return id2Sf1;
	}

	public Integer getId1Sf2() {
		return id1Sf2;
	}

	public Integer getId2Sf2() {
		return id2Sf2;
	}

	public String getResult_qf1() {
		return result_qf1;
	}

	public String getResult_qf2() {
		return result_qf2;
	}

	public String getResult_qf3() {
		return result_qf3;
	}

	public String getResult_qf4() {
		return result_qf4;
	}

	public String getResult_sf1() {
		return result_sf1;
	}

	public String getResult_sf2() {
		return result_sf2;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setId1Qf1(Integer id1Qf1) {
		this.id1Qf1 = id1Qf1;
	}

	public void setId2Qf1(Integer id2Qf1) {
		this.id2Qf1 = id2Qf1;
	}

	public void setId1Qf2(Integer id1Qf2) {
		this.id1Qf2 = id1Qf2;
	}

	public void setId2Qf2(Integer id2Qf2) {
		this.id2Qf2 = id2Qf2;
	}

	public void setId1Qf3(Integer id1Qf3) {
		this.id1Qf3 = id1Qf3;
	}

	public void setId2Qf3(Integer id2Qf3) {
		this.id2Qf3 = id2Qf3;
	}

	public void setId1Qf4(Integer id1Qf4) {
		this.id1Qf4 = id1Qf4;
	}

	public void setId2Qf4(Integer id2Qf4) {
		this.id2Qf4 = id2Qf4;
	}

	public void setId1Sf1(Integer id1Sf1) {
		this.id1Sf1 = id1Sf1;
	}

	public void setId2Sf1(Integer id2Sf1) {
		this.id2Sf1 = id2Sf1;
	}

	public void setId1Sf2(Integer id1Sf2) {
		this.id1Sf2 = id1Sf2;
	}

	public void setId2Sf2(Integer id2Sf2) {
		this.id2Sf2 = id2Sf2;
	}

	public void setResult_qf1(String result_qf1) {
		this.result_qf1 = result_qf1;
	}

	public void setResult_qf2(String result_qf2) {
		this.result_qf2 = result_qf2;
	}

	public void setResult_qf3(String result_qf3) {
		this.result_qf3 = result_qf3;
	}

	public void setResult_qf4(String result_qf4) {
		this.result_qf4 = result_qf4;
	}

	public void setResult_sf1(String result_sf1) {
		this.result_sf1 = result_sf1;
	}

	public void setResult_sf2(String result_sf2) {
		this.result_sf2 = result_sf2;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public Integer getId1Thd() {
		return id1Thd;
	}

	public Integer getId2Thd() {
		return id2Thd;
	}

	public String getResult_thd() {
		return result_thd;
	}

	public void setId1Thd(Integer id1Thd) {
		this.id1Thd = id1Thd;
	}

	public void setId2Thd(Integer id2Thd) {
		this.id2Thd = id2Thd;
	}

	public void setResult_thd(String result_thd) {
		this.result_thd = result_thd;
	}

	@Override
	public String toString() {
		return "Draw [id=" + id + ", idResult=" + idResult + ", id1Qf1="
				+ id1Qf1 + ", id2Qf1=" + id2Qf1 + ", id1Qf2=" + id1Qf2
				+ ", id2Qf2=" + id2Qf2 + ", id1Qf3=" + id1Qf3 + ", id2Qf3="
				+ id2Qf3 + ", id1Qf4=" + id1Qf4 + ", id2Qf4=" + id2Qf4
				+ ", id1Sf1=" + id1Sf1 + ", id2Sf1=" + id2Sf1 + ", id1Sf2="
				+ id1Sf2 + ", id2Sf2=" + id2Sf2 + ", id1Thd=" + id1Thd
				+ ", id2Thd=" + id2Thd + ", result_qf1=" + result_qf1
				+ ", result_qf2=" + result_qf2 + ", result_qf3=" + result_qf3
				+ ", result_qf4=" + result_qf4 + ", result_sf1=" + result_sf1
				+ ", result_sf2=" + result_sf2 + ", result_thd=" + result_thd
				+ ", metadata=" + metadata + "]";
	}
	
}