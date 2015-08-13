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
@Table(name = "\"Result\"")
public class Result {

	public static final transient String alias = "RS";
	
	@Id
	@SequenceGenerator(name = "seq_result", sequenceName = "\"SeqResult\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_result")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_sport", nullable = false)
	private Sport sport;
	
	@ManyToOne
	@JoinColumn(name = "id_championship", nullable = false)
	private Championship championship;
	
	@ManyToOne
	@JoinColumn(name = "id_event")
	private Event event;
	
	@ManyToOne
	@JoinColumn(name = "id_subevent")
	private Event subevent;
	
	@ManyToOne
	@JoinColumn(name = "id_subevent2")
	private Event subevent2;
	
	@ManyToOne
	@JoinColumn(name = "id_city1")
	private City city1;
	
	@ManyToOne
	@JoinColumn(name = "id_city2")
	private City city2;
	
	@ManyToOne
	@JoinColumn(name = "id_complex1")
	private Complex complex1;
	
	@ManyToOne
	@JoinColumn(name = "id_complex2")
	private Complex complex2;
	
	@ManyToOne
	@JoinColumn(name = "id_year", nullable = false)
	private Year year;
	
	@Column(name = "date1", length = 10)
	private String date1;
	
	@Column(name = "date2", length = 10)
	private String date2;
	
	@Column(name = "id_rank1")
	private Integer idRank1;
	
	@Column(name = "id_rank2")
	private Integer idRank2;
	
	@Column(name = "id_rank3")
	private Integer idRank3;
	
	@Column(name = "id_rank4")
	private Integer idRank4;
	
	@Column(name = "id_rank5")
	private Integer idRank5;
	
	@Column(name = "id_rank6")
	private Integer idRank6;
	
	@Column(name = "id_rank7")
	private Integer idRank7;
	
	@Column(name = "id_rank8")
	private Integer idRank8;
	
	@Column(name = "id_rank9")
	private Integer idRank9;
	
	@Column(name = "id_rank10")
	private Integer idRank10;
	
	@Column(name = "result1", length = 40)
	private String result1;
	
	@Column(name = "result2", length = 20)
	private String result2;
	
	@Column(name = "result3", length = 20)
	private String result3;
	
	@Column(name = "result4", length = 20)
	private String result4;
	
	@Column(name = "result5", length = 20)
	private String result5;
	
	private transient String result6;
	private transient String result7;
	private transient String result8;
	private transient String result9;
	private transient String result10;
	
	@Column(name = "\"comment\"", length = 500)
	private String comment;
	
	@Column(name = "exa", length = 15)
	private String exa;
	
	@Embedded
	private Metadata metadata;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public Championship getChampionship() {
		return championship;
	}

	public void setChampionship(Championship championship) {
		this.championship = championship;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Event getSubevent() {
		return subevent;
	}

	public void setSubevent(Event subevent) {
		this.subevent = subevent;
	}

	public City getCity1() {
		return city1;
	}

	public City getCity2() {
		return city2;
	}

	public Complex getComplex1() {
		return complex1;
	}

	public Complex getComplex2() {
		return complex2;
	}

	public void setCity1(City city1) {
		this.city1 = city1;
	}

	public void setCity2(City city2) {
		this.city2 = city2;
	}

	public void setComplex1(Complex complex1) {
		this.complex1 = complex1;
	}

	public void setComplex2(Complex complex2) {
		this.complex2 = complex2;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public Integer getIdRank1() {
		return idRank1;
	}

	public void setIdRank1(Integer idRank1) {
		this.idRank1 = idRank1;
	}

	public Integer getIdRank2() {
		return idRank2;
	}

	public void setIdRank2(Integer idRank2) {
		this.idRank2 = idRank2;
	}

	public Integer getIdRank3() {
		return idRank3;
	}

	public void setIdRank3(Integer idRank3) {
		this.idRank3 = idRank3;
	}

	public Integer getIdRank4() {
		return idRank4;
	}

	public void setIdRank4(Integer idRank4) {
		this.idRank4 = idRank4;
	}

	public Integer getIdRank5() {
		return idRank5;
	}

	public void setIdRank5(Integer idRank5) {
		this.idRank5 = idRank5;
	}

	public Integer getIdRank6() {
		return idRank6;
	}

	public void setIdRank6(Integer idRank6) {
		this.idRank6 = idRank6;
	}

	public Integer getIdRank7() {
		return idRank7;
	}

	public void setIdRank7(Integer idRank7) {
		this.idRank7 = idRank7;
	}

	public Integer getIdRank8() {
		return idRank8;
	}

	public void setIdRank8(Integer idRank8) {
		this.idRank8 = idRank8;
	}

	public Integer getIdRank9() {
		return idRank9;
	}

	public void setIdRank9(Integer idRank9) {
		this.idRank9 = idRank9;
	}

	public Integer getIdRank10() {
		return idRank10;
	}

	public void setIdRank10(Integer idRank10) {
		this.idRank10 = idRank10;
	}

	public String getResult1() {
		return result1;
	}

	public void setResult1(String result1) {
		this.result1 = result1;
	}

	public String getResult2() {
		return result2;
	}

	public void setResult2(String result2) {
		this.result2 = result2;
	}

	public String getResult3() {
		return result3;
	}

	public void setResult3(String result3) {
		this.result3 = result3;
	}

	public String getResult4() {
		return result4;
	}

	public void setResult4(String result4) {
		this.result4 = result4;
	}

	public String getResult5() {
		return result5;
	}

	public void setResult5(String result5) {
		this.result5 = result5;
	}

	public String getResult6() {
		return result6;
	}

	public String getResult7() {
		return result7;
	}

	public String getResult8() {
		return result8;
	}

	public String getResult9() {
		return result9;
	}

	public String getResult10() {
		return result10;
	}

	public void setResult6(String result6) {
		this.result6 = result6;
	}

	public void setResult7(String result7) {
		this.result7 = result7;
	}

	public void setResult8(String result8) {
		this.result8 = result8;
	}

	public void setResult9(String result9) {
		this.result9 = result9;
	}

	public void setResult10(String result10) {
		this.result10 = result10;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getExa() {
		return exa;
	}

	public void setExa(String exa) {
		this.exa = exa;
	}

	public Event getSubevent2() {
		return subevent2;
	}

	public void setSubevent2(Event subevent2) {
		this.subevent2 = subevent2;
	}

	@Override
	public String toString() {
		return sport + ", " + championship + (event != null ? ", " + event : "") + (subevent != null ? ", " + subevent : "") + ", " + year + (date1 != null ? ", " + date1 : "") + (date2 != null ? ", " + date2 : "") + ", 1. #" + idRank1 + ", 2. #" + idRank2 + ", 3. #" + idRank3 + " [#" + id + "]";
	}
	
}