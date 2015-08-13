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
@Table(name = "\"WinLoss\"")
public class WinLoss {

	public static final transient String alias = "WL";
	
	@Id
	@SequenceGenerator(name = "seq_win_loss", sequenceName = "\"SeqWinLoss\"")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_win_loss")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_league", nullable = false)
	private League league;
	
	@ManyToOne
	@JoinColumn(name = "id_team", nullable = false)
	private Team team;
	
	@Column(name = "\"type\"", length = 35, nullable = false)
	private String type;
	
	@Column(name = "count_win", nullable = false)
	private Integer countWin;
	
	@Column(name = "count_loss", nullable = false)
	private Integer countLoss;
	
	@Column(name = "count_tie")
	private Integer countTie;
	
	@Column(name = "count_otloss")
	private Integer countOtloss;
	
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

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getCountWin() {
		return countWin;
	}

	public void setCountWin(Integer countWin) {
		this.countWin = countWin;
	}

	public Integer getCountLoss() {
		return countLoss;
	}

	public void setCountLoss(Integer countLoss) {
		this.countLoss = countLoss;
	}

	public Integer getCountTie() {
		return countTie;
	}

	public void setCountTie(Integer countTie) {
		this.countTie = countTie;
	}

	public Integer getCountOtloss() {
		return countOtloss;
	}

	public void setCountOtloss(Integer countOtloss) {
		this.countOtloss = countOtloss;
	}

	@Override
	public String toString() {
		return "WinLoss [id=" + id + ", league=" + league + ", team=" + team
				+ ", type=" + type + ", countWin=" + countWin + ", countLoss="
				+ countLoss + ", countTie=" + countTie + ", countOtloss="
				+ countOtloss + "]";
	}
	
}