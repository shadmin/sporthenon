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
@Table(name = "\"Record\"")
public class Record {
	
	public static final transient String alias = "RC";

	@Id
	@SequenceGenerator(name = "seq_record", sequenceName = "\"SeqRecord\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_record")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_sport", nullable = false)
	private Sport sport;
	
	@ManyToOne
	@JoinColumn(name = "id_championship")
	private Championship championship;
	
	@ManyToOne
	@JoinColumn(name = "id_event")
	private Event event;
	
	@ManyToOne
	@JoinColumn(name = "id_subevent")
	private Event subevent;
	
	@ManyToOne
	@JoinColumn(name = "id_city")
	private City city;
	
	@Column(name = "label", length = 70, nullable = false)
	private String label;
	
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
	
	@Column(name = "record1", length = 20, nullable = false)
	private String record1;
	
	@Column(name = "record2", length = 20)
	private String record2;
	
	@Column(name = "record3", length = 20)
	private String record3;
	
	@Column(name = "record4", length = 20)
	private String record4;
	
	@Column(name = "record5", length = 20)
	private String record5;
	
	@Column(name = "date1", length = 30)
	private String date1;
	
	@Column(name = "date2", length = 30)
	private String date2;
	
	@Column(name = "date3", length = 30)
	private String date3;
	
	@Column(name = "date4", length = 30)
	private String date4;
	
	@Column(name = "date5", length = 30)
	private String date5;
	
	@Column(name = "counting")
	private Boolean counting;
	
	@Column(name = "\"index\"")
	private Float index;
	
	@Column(name = "type1", length = 10)
	private String type1;
	
	@Column(name = "type2", length = 10)
	private String type2;
	
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

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public String getRecord1() {
		return record1;
	}

	public void setRecord1(String record1) {
		this.record1 = record1;
	}

	public String getRecord2() {
		return record2;
	}

	public void setRecord2(String record2) {
		this.record2 = record2;
	}

	public String getRecord3() {
		return record3;
	}

	public void setRecord3(String record3) {
		this.record3 = record3;
	}

	public String getRecord4() {
		return record4;
	}

	public void setRecord4(String record4) {
		this.record4 = record4;
	}

	public String getRecord5() {
		return record5;
	}

	public void setRecord5(String record5) {
		this.record5 = record5;
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

	public String getDate3() {
		return date3;
	}

	public void setDate3(String date3) {
		this.date3 = date3;
	}

	public String getDate4() {
		return date4;
	}

	public void setDate4(String date4) {
		this.date4 = date4;
	}

	public String getDate5() {
		return date5;
	}

	public void setDate5(String date5) {
		this.date5 = date5;
	}

	public Boolean getCounting() {
		return counting;
	}

	public void setCounting(Boolean counting) {
		this.counting = counting;
	}

	public Float getIndex() {
		return index;
	}

	public void setIndex(Float index) {
		this.index = index;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
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

	@Override
	public String toString() {
		return "Record [id=" + id + ", sport=" + sport + ", championship="
				+ championship + ", event=" + event + ", subevent=" + subevent
				+ ", city=" + city + ", label=" + label + ", idRank1="
				+ idRank1 + ", idRank2=" + idRank2 + ", idRank3=" + idRank3
				+ ", idRank4=" + idRank4 + ", idRank5=" + idRank5
				+ ", record1=" + record1 + ", record2=" + record2
				+ ", record3=" + record3 + ", record4=" + record4
				+ ", record5=" + record5 + ", date1=" + date1 + ", date2="
				+ date2 + ", date3=" + date3 + ", date4=" + date4 + ", date5="
				+ date5 + ", counting=" + counting + ", index=" + index
				+ ", type1=" + type1 + ", type2=" + type2 + ", comment="
				+ comment + ", exa=" + exa + ", metadata=" + metadata + "]";
	}
	
}