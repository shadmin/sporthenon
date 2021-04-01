package com.sporthenon.db.entity.meta;

import java.sql.Timestamp;
import java.util.Map;

import com.sporthenon.db.entity.AbstractEntity;

public class RefItem extends AbstractEntity {
	
	private Integer id;
	private Integer idItem;
	private String label;
	private String labelEN;
	private String entity;
	private Integer countRef;
	private Integer idRel1;
	private Integer idRel2;
	private Integer idRel3;
	private Integer idRel4;
	private Integer idRel5;
	private Integer idRel6;
	private Integer idRel7;
	private Integer idRel8;
	private Integer idRel9;
	private Integer idRel10;
	private Integer idRel11;
	private Integer idRel12;
	private Integer idRel13;
	private Integer idRel14;
	private Integer idRel15;
	private Integer idRel16;
	private Integer idRel17;
	private Integer idRel18;
	private Integer link;
	private String labelRel1;
	private String labelRel2;
	private String labelRel3;
	private String labelRel4;
	private String labelRel5;
	private String labelRel6;
	private String labelRel7;
	private String labelRel8;
	private String labelRel9;
	private String labelRel10;
	private String labelRel11;
	private String labelRel12;
	private String labelRel13;
	private String labelRel14;
	private String labelRel15;
	private String labelRel16;
	private String labelRel17;
	private String labelRel18;
	private String labelRel19;
	private String labelRel20;
	private String labelRel21;
	private String labelRel22;
	private String labelRel23;
	private String labelRel24;
	private String labelRel25;
	private String txt1;
	private String txt2;
	private String txt3;
	private String txt4;
	private String txt5;
	private String txt6;
	private Integer count1;
	private Integer count2;
	private Integer count3;
	private Integer count4;
	private Integer count5;
	private Timestamp date1;
	private Timestamp date2;
	private Timestamp date3;
	private String comment;
	
	public static final transient String table = 	"_ref_item";
	public static final transient String key = 	"id";
	
	public RefItem() {}
	
	public void setValuesFromMap(Map<String, Object> mapValues) {
		setId((Integer)mapValues.get("id"));
		setIdItem((Integer)mapValues.get("id_item"));
		setLabel((String)mapValues.get("label"));
		setLabelEN((String)mapValues.get("label_en"));
		setEntity((String)mapValues.get("entity"));
		setCountRef((Integer)mapValues.get("count_ref"));
		setIdRel1((Integer)mapValues.get("id_rel1"));
		setIdRel2((Integer)mapValues.get("id_rel2"));
		setIdRel3((Integer)mapValues.get("id_rel3"));
		setIdRel4((Integer)mapValues.get("id_rel4"));
		setIdRel5((Integer)mapValues.get("id_rel5"));
		setIdRel6((Integer)mapValues.get("id_rel6"));
		setIdRel7((Integer)mapValues.get("id_rel7"));
		setIdRel8((Integer)mapValues.get("id_rel8"));
		setIdRel9((Integer)mapValues.get("id_rel9"));
		setIdRel10((Integer)mapValues.get("id_rel10"));
		setIdRel11((Integer)mapValues.get("id_rel11"));
		setIdRel12((Integer)mapValues.get("id_rel12"));
		setIdRel13((Integer)mapValues.get("id_rel13"));
		setIdRel14((Integer)mapValues.get("id_rel14"));
		setIdRel15((Integer)mapValues.get("id_rel15"));
		setIdRel16((Integer)mapValues.get("id_rel16"));
		setIdRel17((Integer)mapValues.get("id_rel17"));
		setIdRel18((Integer)mapValues.get("id_rel18"));
		setLink((Integer)mapValues.get("link"));
		setLabelRel1((String)mapValues.get("label_rel1"));
		setLabelRel2((String)mapValues.get("label_rel2"));
		setLabelRel3((String)mapValues.get("label_rel3"));
		setLabelRel4((String)mapValues.get("label_rel4"));
		setLabelRel5((String)mapValues.get("label_rel5"));
		setLabelRel6((String)mapValues.get("label_rel6"));
		setLabelRel7((String)mapValues.get("label_rel7"));
		setLabelRel8((String)mapValues.get("label_rel8"));
		setLabelRel9((String)mapValues.get("label_rel9"));
		setLabelRel10((String)mapValues.get("label_rel10"));
		setLabelRel11((String)mapValues.get("label_rel11"));
		setLabelRel12((String)mapValues.get("label_rel12"));
		setLabelRel13((String)mapValues.get("label_rel13"));
		setLabelRel14((String)mapValues.get("label_rel14"));
		setLabelRel15((String)mapValues.get("label_rel15"));
		setLabelRel16((String)mapValues.get("label_rel16"));
		setLabelRel17((String)mapValues.get("label_rel17"));
		setLabelRel18((String)mapValues.get("label_rel18"));
		setLabelRel19((String)mapValues.get("label_rel19"));
		setLabelRel20((String)mapValues.get("label_rel20"));
		setLabelRel21((String)mapValues.get("label_rel21"));
		setLabelRel22((String)mapValues.get("label_rel22"));
		setLabelRel23((String)mapValues.get("label_rel23"));
		setLabelRel24((String)mapValues.get("label_rel24"));
		setLabelRel25((String)mapValues.get("label_rel25"));
		setTxt1((String)mapValues.get("txt1"));
		setTxt2((String)mapValues.get("txt2"));
		setTxt3((String)mapValues.get("txt3"));
		setTxt4((String)mapValues.get("txt4"));
		setTxt5((String)mapValues.get("txt5"));
		setTxt6((String)mapValues.get("txt6"));
		setCount1((Integer)mapValues.get("count1"));
		setCount2((Integer)mapValues.get("count2"));
		setCount3((Integer)mapValues.get("count3"));
		setCount4((Integer)mapValues.get("count4"));
		setCount5((Integer)mapValues.get("count5"));
		setDate1((Timestamp)mapValues.get("date1"));
		setDate2((Timestamp)mapValues.get("date2"));
		setDate3((Timestamp)mapValues.get("date3"));
		setComment((String)mapValues.get("comment"));
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdItem() {
		return idItem;
	}

	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Integer getCountRef() {
		return countRef;
	}

	public void setCountRef(Integer countRef) {
		this.countRef = countRef;
	}

	public Integer getIdRel1() {
		return idRel1;
	}

	public void setIdRel1(Integer idRel1) {
		this.idRel1 = idRel1;
	}

	public Integer getIdRel2() {
		return idRel2;
	}

	public void setIdRel2(Integer idRel2) {
		this.idRel2 = idRel2;
	}

	public Integer getIdRel3() {
		return idRel3;
	}

	public void setIdRel3(Integer idRel3) {
		this.idRel3 = idRel3;
	}

	public String getLabelRel1() {
		return labelRel1;
	}

	public void setLabelRel1(String labelRel1) {
		this.labelRel1 = labelRel1;
	}

	public String getLabelRel2() {
		return labelRel2;
	}

	public void setLabelRel2(String labelRel2) {
		this.labelRel2 = labelRel2;
	}

	public String getLabelRel3() {
		return labelRel3;
	}

	public void setLabelRel3(String labelRel3) {
		this.labelRel3 = labelRel3;
	}

	public Integer getIdRel4() {
		return idRel4;
	}

	public void setIdRel4(Integer idRel4) {
		this.idRel4 = idRel4;
	}

	public Integer getIdRel5() {
		return idRel5;
	}

	public void setIdRel5(Integer idRel5) {
		this.idRel5 = idRel5;
	}

	public String getLabelRel4() {
		return labelRel4;
	}

	public void setLabelRel4(String labelRel4) {
		this.labelRel4 = labelRel4;
	}

	public String getLabelRel5() {
		return labelRel5;
	}

	public void setLabelRel5(String labelRel5) {
		this.labelRel5 = labelRel5;
	}

	public String getTxt1() {
		return txt1;
	}

	public void setTxt1(String txt1) {
		this.txt1 = txt1;
	}

	public String getTxt2() {
		return txt2;
	}

	public void setTxt2(String txt2) {
		this.txt2 = txt2;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getLink() {
		return link;
	}

	public void setLink(Integer link) {
		this.link = link;
	}

	public Integer getIdRel6() {
		return idRel6;
	}

	public Integer getIdRel7() {
		return idRel7;
	}

	public Integer getIdRel8() {
		return idRel8;
	}

	public Integer getIdRel9() {
		return idRel9;
	}

	public Integer getIdRel10() {
		return idRel10;
	}

	public String getLabelRel6() {
		return labelRel6;
	}

	public String getLabelRel7() {
		return labelRel7;
	}

	public String getLabelRel8() {
		return labelRel8;
	}

	public String getLabelRel9() {
		return labelRel9;
	}

	public String getLabelRel10() {
		return labelRel10;
	}

	public String getTxt3() {
		return txt3;
	}

	public void setIdRel6(Integer idRel6) {
		this.idRel6 = idRel6;
	}

	public void setIdRel7(Integer idRel7) {
		this.idRel7 = idRel7;
	}

	public void setIdRel8(Integer idRel8) {
		this.idRel8 = idRel8;
	}

	public void setIdRel9(Integer idRel9) {
		this.idRel9 = idRel9;
	}

	public void setIdRel10(Integer idRel10) {
		this.idRel10 = idRel10;
	}

	public void setLabelRel6(String labelRel6) {
		this.labelRel6 = labelRel6;
	}

	public void setLabelRel7(String labelRel7) {
		this.labelRel7 = labelRel7;
	}

	public void setLabelRel8(String labelRel8) {
		this.labelRel8 = labelRel8;
	}

	public void setLabelRel9(String labelRel9) {
		this.labelRel9 = labelRel9;
	}

	public void setLabelRel10(String labelRel10) {
		this.labelRel10 = labelRel10;
	}

	public void setTxt3(String txt3) {
		this.txt3 = txt3;
	}

	public String getTxt4() {
		return txt4;
	}

	public void setTxt4(String txt4) {
		this.txt4 = txt4;
	}

	public Integer getIdRel11() {
		return idRel11;
	}

	public String getLabelRel11() {
		return labelRel11;
	}

	public void setIdRel11(Integer idRel11) {
		this.idRel11 = idRel11;
	}

	public void setLabelRel11(String labelRel11) {
		this.labelRel11 = labelRel11;
	}

	public Integer getIdRel12() {
		return idRel12;
	}

	public Integer getIdRel13() {
		return idRel13;
	}

	public Integer getIdRel14() {
		return idRel14;
	}

	public Integer getIdRel15() {
		return idRel15;
	}

	public Integer getIdRel16() {
		return idRel16;
	}

	public Integer getIdRel17() {
		return idRel17;
	}

	public void setIdRel12(Integer idRel12) {
		this.idRel12 = idRel12;
	}

	public void setIdRel13(Integer idRel13) {
		this.idRel13 = idRel13;
	}

	public void setIdRel14(Integer idRel14) {
		this.idRel14 = idRel14;
	}

	public void setIdRel15(Integer idRel15) {
		this.idRel15 = idRel15;
	}

	public void setIdRel16(Integer idRel16) {
		this.idRel16 = idRel16;
	}

	public void setIdRel17(Integer idRel17) {
		this.idRel17 = idRel17;
	}

	public Integer getCount1() {
		return count1;
	}

	public Integer getCount2() {
		return count2;
	}

	public Integer getCount3() {
		return count3;
	}

	public Integer getCount4() {
		return count4;
	}

	public Integer getCount5() {
		return count5;
	}

	public void setCount1(Integer count1) {
		this.count1 = count1;
	}

	public void setCount2(Integer count2) {
		this.count2 = count2;
	}

	public void setCount3(Integer count3) {
		this.count3 = count3;
	}

	public void setCount4(Integer count4) {
		this.count4 = count4;
	}

	public void setCount5(Integer count5) {
		this.count5 = count5;
	}

	public String getLabelRel12() {
		return labelRel12;
	}

	public void setLabelRel12(String labelRel12) {
		this.labelRel12 = labelRel12;
	}

	public Integer getIdRel18() {
		return idRel18;
	}

	public String getLabelRel18() {
		return labelRel18;
	}

	public void setIdRel18(Integer idRel18) {
		this.idRel18 = idRel18;
	}

	public void setLabelRel18(String labelRel18) {
		this.labelRel18 = labelRel18;
	}

	public Timestamp getDate1() {
		return date1;
	}

	public void setDate1(Timestamp date1) {
		this.date1 = date1;
	}

	public String getLabelEN() {
		return labelEN;
	}

	public void setLabelEN(String labelEN) {
		this.labelEN = labelEN;
	}

	public String getLabelRel13() {
		return labelRel13;
	}

	public String getLabelRel14() {
		return labelRel14;
	}

	public String getLabelRel15() {
		return labelRel15;
	}

	public String getLabelRel16() {
		return labelRel16;
	}

	public String getLabelRel17() {
		return labelRel17;
	}

	public void setLabelRel13(String labelRel13) {
		this.labelRel13 = labelRel13;
	}

	public void setLabelRel14(String labelRel14) {
		this.labelRel14 = labelRel14;
	}

	public void setLabelRel15(String labelRel15) {
		this.labelRel15 = labelRel15;
	}

	public void setLabelRel16(String labelRel16) {
		this.labelRel16 = labelRel16;
	}

	public void setLabelRel17(String labelRel17) {
		this.labelRel17 = labelRel17;
	}

	public String getLabelRel19() {
		return labelRel19;
	}

	public String getLabelRel20() {
		return labelRel20;
	}

	public String getLabelRel21() {
		return labelRel21;
	}

	public String getLabelRel22() {
		return labelRel22;
	}

	public String getLabelRel23() {
		return labelRel23;
	}

	public String getLabelRel24() {
		return labelRel24;
	}

	public String getLabelRel25() {
		return labelRel25;
	}

	public void setLabelRel19(String labelRel19) {
		this.labelRel19 = labelRel19;
	}

	public void setLabelRel20(String labelRel20) {
		this.labelRel20 = labelRel20;
	}

	public void setLabelRel21(String labelRel21) {
		this.labelRel21 = labelRel21;
	}

	public void setLabelRel22(String labelRel22) {
		this.labelRel22 = labelRel22;
	}

	public void setLabelRel23(String labelRel23) {
		this.labelRel23 = labelRel23;
	}

	public void setLabelRel24(String labelRel24) {
		this.labelRel24 = labelRel24;
	}

	public void setLabelRel25(String labelRel25) {
		this.labelRel25 = labelRel25;
	}

	public Timestamp getDate2() {
		return date2;
	}

	public void setDate2(Timestamp date2) {
		this.date2 = date2;
	}

	public String getTxt5() {
		return txt5;
	}

	public void setTxt5(String txt5) {
		this.txt5 = txt5;
	}

	public String getTxt6() {
		return txt6;
	}

	public void setTxt6(String txt6) {
		this.txt6 = txt6;
	}

	public Timestamp getDate3() {
		return date3;
	}

	public void setDate3(Timestamp date3) {
		this.date3 = date3;
	}

	@Override
	public String toString() {
		return "RefItem [id=" + id + ", idItem=" + idItem + ", label=" + label
				+ ", labelEN=" + labelEN + ", entity=" + entity + ", countRef="
				+ countRef + ", idRel1=" + idRel1 + ", idRel2=" + idRel2
				+ ", idRel3=" + idRel3 + ", idRel4=" + idRel4 + ", idRel5="
				+ idRel5 + ", idRel6=" + idRel6 + ", idRel7=" + idRel7
				+ ", idRel8=" + idRel8 + ", idRel9=" + idRel9 + ", idRel10="
				+ idRel10 + ", idRel11=" + idRel11 + ", idRel12=" + idRel12
				+ ", idRel13=" + idRel13 + ", idRel14=" + idRel14
				+ ", idRel15=" + idRel15 + ", idRel16=" + idRel16
				+ ", idRel17=" + idRel17 + ", idRel18=" + idRel18 + ", link="
				+ link + ", labelRel1=" + labelRel1 + ", labelRel2="
				+ labelRel2 + ", labelRel3=" + labelRel3 + ", labelRel4="
				+ labelRel4 + ", labelRel5=" + labelRel5 + ", labelRel6="
				+ labelRel6 + ", labelRel7=" + labelRel7 + ", labelRel8="
				+ labelRel8 + ", labelRel9=" + labelRel9 + ", labelRel10="
				+ labelRel10 + ", labelRel11=" + labelRel11 + ", labelRel12="
				+ labelRel12 + ", labelRel13=" + labelRel13 + ", labelRel14="
				+ labelRel14 + ", labelRel15=" + labelRel15 + ", labelRel16="
				+ labelRel16 + ", labelRel17=" + labelRel17 + ", labelRel18="
				+ labelRel18 + ", labelRel19=" + labelRel19 + ", labelRel20="
				+ labelRel20 + ", labelRel21=" + labelRel21 + ", labelRel22="
				+ labelRel22 + ", labelRel23=" + labelRel23 + ", labelRel24="
				+ labelRel24 + ", labelRel25=" + labelRel25 + ", txt1=" + txt1
				+ ", txt2=" + txt2 + ", txt3=" + txt3 + ", txt4=" + txt4
				+ ", txt5=" + txt5 + ", count1=" + count1 + ", count2="
				+ count2 + ", count3=" + count3 + ", count4=" + count4
				+ ", count5=" + count5 + ", date1=" + date1 + ", date2="
				+ date2 + ", comment=" + comment + "]";
	}

}