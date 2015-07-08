package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WinRecordsBean {

	@Id
	@Column(name = "entity_id")
	private Integer entityId;
	
	@Column(name = "entity_type")
	private Integer entityType;
	
	@Column(name = "entity_str")
	private String entityStr;
	
	@Column(name = "entity_str_en")
	private String entityStrEN;
	
	@Column(name = "entity_rel")
	private String entityRel;
	
	@Column(name = "count_win")
	private Integer countWin;

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public Integer getEntityType() {
		return entityType;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}

	public String getEntityStr() {
		return entityStr;
	}

	public void setEntityStr(String entityStr) {
		this.entityStr = entityStr;
	}

	public Integer getCountWin() {
		return countWin;
	}

	public void setCountWin(Integer countWin) {
		this.countWin = countWin;
	}

	public String getEntityStrEN() {
		return entityStrEN;
	}

	public void setEntityStrEN(String entityStrEN) {
		this.entityStrEN = entityStrEN;
	}

	public String getEntityRel() {
		return entityRel;
	}

	public void setEntityRel(String entityRel) {
		this.entityRel = entityRel;
	}

	@Override
	public String toString() {
		return "WinRecordsBean [countWin=" + countWin + ", entityId="
				+ entityId + ", entityStr=" + entityStr + ", entityType="
				+ entityType + "]";
	}
	
}
