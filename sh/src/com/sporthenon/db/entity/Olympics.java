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
@Table(name = "\"Olympics\"")
public class Olympics {
	
	public static final transient String alias = "OL";

	@Id
	@SequenceGenerator(name = "seq_olympics", sequenceName = "\"SeqOlympics\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_olympics")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_year", nullable = false)
	private Year year;
	
	@ManyToOne
	@JoinColumn(name = "id_city", nullable = false)
	private City city;
	
	@Column(name = "count_country", nullable = false)
	private Integer countCountry;
	
	@Column(name = "count_person", nullable = false)
	private Integer countPerson;
	
	@Column(name = "count_sport", nullable = false)
	private Integer countSport;
	
	@Column(name = "count_event", nullable = false)
	private Integer countEvent;
	
	@Column(name = "date1", length = 10, nullable = false)
	private String date1;
	
	@Column(name = "date2", length = 10, nullable = false)
	private String date2;
	
	@Column(name = "type", nullable = false)
	private Integer type;
	
	@Column(name = "ref")
	private Integer ref;
	
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

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Integer getCountCountry() {
		return countCountry;
	}

	public void setCountCountry(Integer countCountry) {
		this.countCountry = countCountry;
	}

	public Integer getCountPerson() {
		return countPerson;
	}

	public void setCountPerson(Integer countPerson) {
		this.countPerson = countPerson;
	}

	public Integer getCountSport() {
		return countSport;
	}

	public void setCountSport(Integer countSport) {
		this.countSport = countSport;
	}

	public Integer getCountEvent() {
		return countEvent;
	}

	public void setCountEvent(Integer countEvent) {
		this.countEvent = countEvent;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRef() {
		return ref;
	}

	public void setRef(Integer ref) {
		this.ref = ref;
	}

	@Override
	public String toString() {
		return "Olympics [id=" + id + ", year=" + year + ", city=" + city
				+ ", countCountry=" + countCountry + ", countPerson="
				+ countPerson + ", countSport=" + countSport + ", countEvent="
				+ countEvent + ", date1=" + date1 + ", date2=" + date2
				+ ", type=" + type + "]";
	}
	
}