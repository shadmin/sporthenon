package com.sporthenon.db.function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class USRecordsBean {

	@Id
	@Column(name = "rc_id")
	private Integer rcId;
	
	@Column(name = "rc_rank1")
	private Integer rcRank1;
	
	@Column(name = "rc_rank2")
	private Integer rcRank2;
	
	@Column(name = "rc_rank3")
	private Integer rcRank3;
	
	@Column(name = "rc_rank4")
	private Integer rcRank4;
	
	@Column(name = "rc_rank5")
	private Integer rcRank5;
	
	@Column(name = "rc_label")
	private String rcLabel;
	
	@Column(name = "rc_exa")
	private String rcExa;
	
	@Column(name = "rc_comment")
	private String rcComment;
	
	@Column(name = "ev_id")
	private Integer evId;

	@Column(name = "ev_label")
	private String evLabel;
	
	@Column(name = "se_id")
	private Integer seId;

	@Column(name = "se_label")
	private String seLabel;
	
	@Column(name = "rc_type1")
	private String rcType1;
	
	@Column(name = "rc_type2")
	private String rcType2;
	
	@Column(name = "rc_number1")
	private Integer rcNumber1;
	
	@Column(name = "rc_number2")
	private Integer rcNumber2;
	
	@Column(name = "rc_record1")
	private String rcRecord1;
	
	@Column(name = "rc_record2")
	private String rcRecord2;
	
	@Column(name = "rc_record3")
	private String rcRecord3;
	
	@Column(name = "rc_record4")
	private String rcRecord4;
	
	@Column(name = "rc_record5")
	private String rcRecord5;
	
	@Column(name = "rc_date1")
	private String rcDate1;
	
	@Column(name = "rc_date2")
	private String rcDate2;
	
	@Column(name = "rc_date3")
	private String rcDate3;
	
	@Column(name = "rc_date4")
	private String rcDate4;
	
	@Column(name = "rc_date5")
	private String rcDate5;
	
	@Column(name = "rc_person1")
	private String rcPerson1;
	
	@Column(name = "rc_team1")
	private String rcTeam1;
	
	@Column(name = "rc_idprteam1")
	private Integer rcIdPrTeam1;
	
	@Column(name = "rc_prteam1")
	private String rcPrTeam1;
	
	@Column(name = "rc_person2")
	private String rcPerson2;
	
	@Column(name = "rc_team2")
	private String rcTeam2;
	
	@Column(name = "rc_idprteam2")
	private Integer rcIdPrTeam2;
	
	@Column(name = "rc_prteam2")
	private String rcPrTeam2;
	
	@Column(name = "rc_person3")
	private String rcPerson3;
	
	@Column(name = "rc_team3")
	private String rcTeam3;
	
	@Column(name = "rc_idprteam3")
	private Integer rcIdPrTeam3;
	
	@Column(name = "rc_prteam3")
	private String rcPrTeam3;
	
	@Column(name = "rc_person4")
	private String rcPerson4;
	
	@Column(name = "rc_team4")
	private String rcTeam4;
	
	@Column(name = "rc_idprteam4")
	private Integer rcIdPrTeam4;
	
	@Column(name = "rc_prteam4")
	private String rcPrTeam4;
	
	@Column(name = "rc_person5")
	private String rcPerson5;
	
	@Column(name = "rc_team5")
	private String rcTeam5;
	
	@Column(name = "rc_idprteam5")
	private Integer rcIdPrTeam5;
	
	@Column(name = "rc_prteam5")
	private String rcPrTeam5;

	public Integer getRcId() {
		return rcId;
	}

	public void setRcId(Integer rcId) {
		this.rcId = rcId;
	}

	public Integer getRcRank1() {
		return rcRank1;
	}

	public void setRcRank1(Integer rcRank1) {
		this.rcRank1 = rcRank1;
	}

	public Integer getRcRank2() {
		return rcRank2;
	}

	public void setRcRank2(Integer rcRank2) {
		this.rcRank2 = rcRank2;
	}

	public Integer getRcRank3() {
		return rcRank3;
	}

	public void setRcRank3(Integer rcRank3) {
		this.rcRank3 = rcRank3;
	}

	public Integer getRcRank4() {
		return rcRank4;
	}

	public void setRcRank4(Integer rcRank4) {
		this.rcRank4 = rcRank4;
	}

	public Integer getRcRank5() {
		return rcRank5;
	}

	public void setRcRank5(Integer rcRank5) {
		this.rcRank5 = rcRank5;
	}

	public String getRcLabel() {
		return rcLabel;
	}

	public void setRcLabel(String rcLabel) {
		this.rcLabel = rcLabel;
	}

	public String getRcExa() {
		return rcExa;
	}

	public void setRcExa(String rcExa) {
		this.rcExa = rcExa;
	}

	public String getRcComment() {
		return rcComment;
	}

	public void setRcComment(String rcComment) {
		this.rcComment = rcComment;
	}

	public Integer getEvId() {
		return evId;
	}

	public void setEvId(Integer evId) {
		this.evId = evId;
	}

	public String getEvLabel() {
		return evLabel;
	}

	public void setEvLabel(String evLabel) {
		this.evLabel = evLabel;
	}

	public Integer getSeId() {
		return seId;
	}

	public void setSeId(Integer seId) {
		this.seId = seId;
	}

	public String getSeLabel() {
		return seLabel;
	}

	public void setSeLabel(String seLabel) {
		this.seLabel = seLabel;
	}

	public String getRcType1() {
		return rcType1;
	}

	public void setRcType1(String rcType1) {
		this.rcType1 = rcType1;
	}

	public String getRcType2() {
		return rcType2;
	}

	public void setRcType2(String rcType2) {
		this.rcType2 = rcType2;
	}

	public Integer getRcNumber1() {
		return rcNumber1;
	}

	public void setRcNumber1(Integer rcNumber1) {
		this.rcNumber1 = rcNumber1;
	}

	public Integer getRcNumber2() {
		return rcNumber2;
	}

	public void setRcNumber2(Integer rcNumber2) {
		this.rcNumber2 = rcNumber2;
	}

	public String getRcRecord1() {
		return rcRecord1;
	}

	public void setRcRecord1(String rcRecord1) {
		this.rcRecord1 = rcRecord1;
	}

	public String getRcRecord2() {
		return rcRecord2;
	}

	public void setRcRecord2(String rcRecord2) {
		this.rcRecord2 = rcRecord2;
	}

	public String getRcRecord3() {
		return rcRecord3;
	}

	public void setRcRecord3(String rcRecord3) {
		this.rcRecord3 = rcRecord3;
	}

	public String getRcRecord4() {
		return rcRecord4;
	}

	public void setRcRecord4(String rcRecord4) {
		this.rcRecord4 = rcRecord4;
	}

	public String getRcRecord5() {
		return rcRecord5;
	}

	public void setRcRecord5(String rcRecord5) {
		this.rcRecord5 = rcRecord5;
	}

	public String getRcDate1() {
		return rcDate1;
	}

	public void setRcDate1(String rcDate1) {
		this.rcDate1 = rcDate1;
	}

	public String getRcDate2() {
		return rcDate2;
	}

	public void setRcDate2(String rcDate2) {
		this.rcDate2 = rcDate2;
	}

	public String getRcDate3() {
		return rcDate3;
	}

	public void setRcDate3(String rcDate3) {
		this.rcDate3 = rcDate3;
	}

	public String getRcDate4() {
		return rcDate4;
	}

	public void setRcDate4(String rcDate4) {
		this.rcDate4 = rcDate4;
	}

	public String getRcDate5() {
		return rcDate5;
	}

	public void setRcDate5(String rcDate5) {
		this.rcDate5 = rcDate5;
	}

	public String getRcPerson1() {
		return rcPerson1;
	}

	public void setRcPerson1(String rcPerson1) {
		this.rcPerson1 = rcPerson1;
	}

	public String getRcTeam1() {
		return rcTeam1;
	}

	public void setRcTeam1(String rcTeam1) {
		this.rcTeam1 = rcTeam1;
	}

	public String getRcPrTeam1() {
		return rcPrTeam1;
	}

	public void setRcPrTeam1(String rcPrTeam1) {
		this.rcPrTeam1 = rcPrTeam1;
	}

	public String getRcPerson2() {
		return rcPerson2;
	}

	public void setRcPerson2(String rcPerson2) {
		this.rcPerson2 = rcPerson2;
	}

	public String getRcTeam2() {
		return rcTeam2;
	}

	public void setRcTeam2(String rcTeam2) {
		this.rcTeam2 = rcTeam2;
	}

	public String getRcPrTeam2() {
		return rcPrTeam2;
	}

	public void setRcPrTeam2(String rcPrTeam2) {
		this.rcPrTeam2 = rcPrTeam2;
	}

	public String getRcPerson3() {
		return rcPerson3;
	}

	public void setRcPerson3(String rcPerson3) {
		this.rcPerson3 = rcPerson3;
	}

	public String getRcTeam3() {
		return rcTeam3;
	}

	public void setRcTeam3(String rcTeam3) {
		this.rcTeam3 = rcTeam3;
	}

	public String getRcPrTeam3() {
		return rcPrTeam3;
	}

	public void setRcPrTeam3(String rcPrTeam3) {
		this.rcPrTeam3 = rcPrTeam3;
	}

	public String getRcPerson4() {
		return rcPerson4;
	}

	public void setRcPerson4(String rcPerson4) {
		this.rcPerson4 = rcPerson4;
	}

	public String getRcTeam4() {
		return rcTeam4;
	}

	public void setRcTeam4(String rcTeam4) {
		this.rcTeam4 = rcTeam4;
	}

	public String getRcPrTeam4() {
		return rcPrTeam4;
	}

	public void setRcPrTeam4(String rcPrTeam4) {
		this.rcPrTeam4 = rcPrTeam4;
	}

	public String getRcPerson5() {
		return rcPerson5;
	}

	public void setRcPerson5(String rcPerson5) {
		this.rcPerson5 = rcPerson5;
	}

	public String getRcTeam5() {
		return rcTeam5;
	}

	public void setRcTeam5(String rcTeam5) {
		this.rcTeam5 = rcTeam5;
	}

	public String getRcPrTeam5() {
		return rcPrTeam5;
	}

	public void setRcPrTeam5(String rcPrTeam5) {
		this.rcPrTeam5 = rcPrTeam5;
	}

	public Integer getRcIdPrTeam1() {
		return rcIdPrTeam1;
	}

	public Integer getRcIdPrTeam2() {
		return rcIdPrTeam2;
	}

	public Integer getRcIdPrTeam3() {
		return rcIdPrTeam3;
	}

	public Integer getRcIdPrTeam4() {
		return rcIdPrTeam4;
	}

	public Integer getRcIdPrTeam5() {
		return rcIdPrTeam5;
	}

	public void setRcIdPrTeam1(Integer rcIdPrTeam1) {
		this.rcIdPrTeam1 = rcIdPrTeam1;
	}

	public void setRcIdPrTeam2(Integer rcIdPrTeam2) {
		this.rcIdPrTeam2 = rcIdPrTeam2;
	}

	public void setRcIdPrTeam3(Integer rcIdPrTeam3) {
		this.rcIdPrTeam3 = rcIdPrTeam3;
	}

	public void setRcIdPrTeam4(Integer rcIdPrTeam4) {
		this.rcIdPrTeam4 = rcIdPrTeam4;
	}

	public void setRcIdPrTeam5(Integer rcIdPrTeam5) {
		this.rcIdPrTeam5 = rcIdPrTeam5;
	}

	@Override
	public String toString() {
		return "USRecordsBean [evId=" + evId + ", evLabel=" + evLabel
				+ ", rcComment=" + rcComment + ", rcDate1=" + rcDate1
				+ ", rcDate2=" + rcDate2 + ", rcDate3=" + rcDate3
				+ ", rcDate4=" + rcDate4 + ", rcDate5=" + rcDate5 + ", rcId="
				+ rcId + ", rcLabel=" + rcLabel + ", rcNumber1=" + rcNumber1
				+ ", rcNumber2=" + rcNumber2 + ", rcPerson1=" + rcPerson1
				+ ", rcPerson2=" + rcPerson2 + ", rcPerson3=" + rcPerson3
				+ ", rcPerson4=" + rcPerson4 + ", rcPerson5=" + rcPerson5
				+ ", rcPrTeam1=" + rcPrTeam1 + ", rcPrTeam2=" + rcPrTeam2
				+ ", rcPrTeam3=" + rcPrTeam3 + ", rcPrTeam4=" + rcPrTeam4
				+ ", rcPrTeam5=" + rcPrTeam5 + ", rcRank1=" + rcRank1
				+ ", rcRank2=" + rcRank2 + ", rcRank3=" + rcRank3
				+ ", rcRank4=" + rcRank4 + ", rcRank5=" + rcRank5
				+ ", rcRecord1=" + rcRecord1 + ", rcRecord2=" + rcRecord2
				+ ", rcRecord3=" + rcRecord3 + ", rcRecord4=" + rcRecord4
				+ ", rcRecord5=" + rcRecord5 + ", rcTeam1=" + rcTeam1
				+ ", rcTeam2=" + rcTeam2 + ", rcTeam3=" + rcTeam3
				+ ", rcTeam4=" + rcTeam4 + ", rcTeam5=" + rcTeam5
				+ ", rcType1=" + rcType1 + ", rcType2=" + rcType2 + ", seId="
				+ seId + ", seLabel=" + seLabel + "]";
	}
	
}