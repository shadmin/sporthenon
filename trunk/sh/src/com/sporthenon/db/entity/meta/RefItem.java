package com.sporthenon.db.entity.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"~REF_ITEM\"")
public class RefItem  {
	
	@Id
	private Integer id;
	
	@Column(name = "id_item", nullable = false)
	private Integer idItem;
	
	@Column(name = "label", length = 70, nullable = false)
	private String label;
	
	@Column(name = "entity", length = 2, nullable = false)
	private String entity;
	
	@Column(name = "count_ref")
	private String countRef;
	
	@Column(name = "id_rel1")
	private Integer idRel1;
	
	@Column(name = "id_rel2")
	private Integer idRel2;
	
	@Column(name = "id_rel3")
	private Integer idRel3;
	
	@Column(name = "id_rel4")
	private Integer idRel4;
	
	@Column(name = "id_rel5")
	private Integer idRel5;
	
	@Column(name = "id_rel6")
	private Integer idRel6;
	
	@Column(name = "id_rel7")
	private Integer idRel7;
	
	@Column(name = "id_rel8")
	private Integer idRel8;
	
	@Column(name = "id_rel9")
	private Integer idRel9;
	
	@Column(name = "id_rel10")
	private Integer idRel10;
	
	@Column(name = "id_rel11")
	private Integer idRel11;
	
	@Column(name = "link")
	private Integer link;
	
	@Column(name = "label_rel1", length = 50)
	private String labelRel1;

	@Column(name = "label_rel2", length = 50)
	private String labelRel2;
	
	@Column(name = "label_rel3", length = 50)
	private String labelRel3;
	
	@Column(name = "label_rel4", length = 50)
	private String labelRel4;
	
	@Column(name = "label_rel5", length = 50)
	private String labelRel5;
	
	@Column(name = "label_rel6", length = 50)
	private String labelRel6;
	
	@Column(name = "label_rel7", length = 50)
	private String labelRel7;
	
	@Column(name = "label_rel8", length = 50)
	private String labelRel8;
	
	@Column(name = "label_rel9", length = 50)
	private String labelRel9;
	
	@Column(name = "label_rel10", length = 50)
	private String labelRel10;
	
	@Column(name = "label_rel11", length = 50)
	private String labelRel11;
	
	@Column(name = "txt1", length = 40)
	private String txt1;
	
	@Column(name = "txt2", length = 40)
	private String txt2;
	
	@Column(name = "txt3", length = 500)
	private String txt3;
	
	@Column(name = "txt4", length = 40)
	private String txt4;
	
	@Column(name = "comment", length = 20)
	private String comment;
	
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

	public String getCountRef() {
		return countRef;
	}

	public void setCountRef(String countRef) {
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

	@Override
	public String toString() {
		return "RefItem [comment=" + comment + ", countRef=" + countRef
				+ ", entity=" + entity + ", id=" + id + ", idItem=" + idItem
				+ ", idRel1=" + idRel1 + ", idRel2=" + idRel2 + ", idRel3="
				+ idRel3 + ", idRel4=" + idRel4 + ", idRel5=" + idRel5
				+ ", label=" + label + ", labelRel1=" + labelRel1
				+ ", labelRel2=" + labelRel2 + ", labelRel3=" + labelRel3
				+ ", labelRel4=" + labelRel4 + ", labelRel5=" + labelRel5
				+ ", txt1=" + txt1 + ", txt2=" + txt2 + "]";
	}

}