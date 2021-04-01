package com.sporthenon.db.entity;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.meta.Metadata;

public class WinLoss extends AbstractEntity {

	private Integer id;
	private League league;
	private Team team;
	private String type;
	private Integer countWin;
	private Integer countLoss;
	private Integer countTie;
	private Integer countOtloss;
	
	public static final transient String alias 	= "WL";
	public static final transient String table 	= "win_loss";
	public static final transient String key 	= "id";
	public static final transient String query 	= "SELECT T.*, LG.label AS lg_label, TM.label AS tm_label "
			+ " FROM win_loss T JOIN league LG ON LG.id = T.id_league "
			+ " JOIN team TM ON TM.id = T.id_team";
	
	public WinLoss() {}
	
	public WinLoss(Integer id) {
		this.id = id;
	}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		if (mapValues != null) {
			setId((Integer)mapValues.get("id"));
			Integer idLeague = (Integer)mapValues.get("id_league");
			if (idLeague != null) {
				setLeague(new League(idLeague));
				getLeague().setLabel((String)mapValues.get("lg_label"));
			}
			Integer idTeam = (Integer)mapValues.get("id_team");
			if (idTeam != null) {
				setTeam(new Team(idTeam));
				getTeam().setLabel((String)mapValues.get("tm_label"));
			}
			setType((String)mapValues.get("type"));
			setCountWin((Integer)mapValues.get("count_win"));
			setCountLoss((Integer)mapValues.get("count_loss"));
			setCountTie((Integer)mapValues.get("count_tie"));
			setCountOtloss((Integer)mapValues.get("count_otloss"));
			setMetadata(new Metadata((Integer)mapValues.get("id_contributor"), (Timestamp)mapValues.get("first_update"), (Timestamp)mapValues.get("last_update")));
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		return "WinLoss [id=" + id + ", league=" + league + ", team=" + team + ", type=" + type + ", countWin=" + countWin + ", countLoss=" + countLoss + ", countTie=" + countTie + ", countOtloss=" + countOtloss + "]";
	}
	
	public String toString2() {
		return league.getLabel() + " - " + team.getLabel();
	}
	
}