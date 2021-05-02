package com.sporthenon.admin.container.entity;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.utils.SwingUtils;

public class JRecordPanel extends JAbstractEntityPanel {

private static final long serialVersionUID = 1L;
	
	public JEntityPicklist jSport;
	public JEntityPicklist jChampionship;
	public JEntityPicklist jEvent;
	public JEntityPicklist jSubevent;
	public JTextField jType1;
	public JTextField jType2;
	public JEntityPicklist jCity;
	public JTextField jLabel;
	public JEntityPicklist jRank1;
	public JEntityPicklist jRank2;
	public JEntityPicklist jRank3;
	public JEntityPicklist jRank4;
	public JEntityPicklist jRank5;
	public JTextField jRecord1;
	public JTextField jRecord2;
	public JTextField jRecord3;
	public JTextField jRecord4;
	public JTextField jRecord5;
	public JTextField jDate1;
	public JTextField jDate2;
	public JTextField jDate3;
	public JTextField jDate4;
	public JTextField jDate5;
	public JCheckBox jCounting;
	public JTextField jIndex;
	public JTextField jExa;
	public JTextArea jComment;
	
	public JRecordPanel() {
		super(27);
		initialize();
		setPreferredSize(new Dimension(310, (LINE_HEIGHT * 50) - 50));
	}

	protected void initialize() {
        //Sport
        JLabel lSport = new JLabel(" Sport:");
        lSport.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lSport);
        jSport = new JEntityPicklist(this, Sport.alias, false);
        gridPanel.add(jSport);
        
        //Championship
        JLabel lChampionship = new JLabel(" Championship:");
        lChampionship.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lChampionship);
        jChampionship = new JEntityPicklist(this, Championship.alias, false);
        gridPanel.add(jChampionship);
        
        //Event
        JLabel lEvent = new JLabel(" Event:");
        lEvent.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lEvent);
        jEvent = new JEntityPicklist(this, Event.alias, false);
        gridPanel.add(jEvent);
        
        //Subevent
        JLabel lSubevent = new JLabel(" Subevent:");
        lSubevent.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lSubevent);
        jSubevent = new JEntityPicklist(this, Event.alias, false);
        gridPanel.add(jSubevent);
        
        //Type
        JLabel lType = new JLabel(" Type:");
        lType.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lType);
        jType1 = new JTextField();
        gridPanel.add(jType1);
        jType2 = new JTextField();
        gridPanel.add(jType2);
        
        //City
        JLabel lCity = new JLabel(" City:");
        lCity.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCity);
        jCity = new JEntityPicklist(this, City.alias, true);
        gridPanel.add(jCity);
        
        //Deceased
        JLabel lLabel = new JLabel(" Label:");
        lLabel.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLabel);
        jLabel = new JTextField();
        gridPanel.add(jLabel);
        
        // Rank1
        JLabel lRank1 = new JLabel(" Rank #1 (Name / Record / Date):");
        lRank1.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lRank1);
        jRank1 = new JEntityPicklist(this, null, true);
        gridPanel.add(jRank1);
        jRecord1 = new JTextField();
        gridPanel.add(jRecord1);
        jDate1 = new JTextField();
        gridPanel.add(jDate1);
        
        // Rank2
        JLabel lRank2 = new JLabel(" Rank #2 (Name / Record / Date):");
        lRank2.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lRank2);
        jRank2 = new JEntityPicklist(this, null, true);
        gridPanel.add(jRank2);
        jRecord2 = new JTextField();
        gridPanel.add(jRecord2);
        jDate2 = new JTextField();
        gridPanel.add(jDate2);
        
        // Rank3
        JLabel lRank3 = new JLabel(" Rank #3 (Name / Record / Date):");
        lRank3.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lRank3);
        jRank3 = new JEntityPicklist(this, null, true);
        gridPanel.add(jRank3);
        jRecord3 = new JTextField();
        gridPanel.add(jRecord3);
        jDate3 = new JTextField();
        gridPanel.add(jDate3);
        
        // Rank4
        JLabel lRank4 = new JLabel(" Rank #4 (Name / Record / Date):");
        lRank4.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lRank4);
        jRank4 = new JEntityPicklist(this, null, true);
        gridPanel.add(jRank4);
        jRecord4 = new JTextField();
        gridPanel.add(jRecord4);
        jDate4 = new JTextField();
        gridPanel.add(jDate4);
        
        // Rank5
        JLabel lRank5 = new JLabel(" Rank #5 (Name / Record / Date):");
        lRank5.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lRank5);
        jRank5 = new JEntityPicklist(this, null, true);
        gridPanel.add(jRank5);
        jRecord5 = new JTextField();
        gridPanel.add(jRecord5);
        jDate5 = new JTextField();
        gridPanel.add(jDate5);
        
        //Counting
        JLabel lCounting = new JLabel(" Counting:");
        lCounting.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCounting);
        jCounting = new JCheckBox();
        gridPanel.add(jCounting);
        
        //Index
        JLabel lIndex = new JLabel(" Index:");
        lIndex.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lIndex);
        jIndex = new JTextField();
        gridPanel.add(jIndex);
        
        //Tie
        JLabel lExa = new JLabel(" Tie:");
        lIndex.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lExa);
        jExa = new JTextField();
        gridPanel.add(jExa);
        
        //Comment
        JLabel lComment = new JLabel(" Comment:");
        lComment.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lComment);
        jComment = new JTextArea();
		jComment.setFont(SwingUtils.getDefaultFont());
		JScrollPane jCommentPane = new JScrollPane(jComment);
		jCommentPane.setPreferredSize(new Dimension(0, 21));
        gridPanel.add(jCommentPane);
	}
	
	public JEntityPicklist getSport() {
		return jSport;
	}
	
	public JEntityPicklist getChampionship() {
		return jChampionship;
	}
	
	public JEntityPicklist getEvent() {
		return jEvent;
	}
	
	public JEntityPicklist getSubevent() {
		return jSubevent;
	}
	
	public JTextField getType1() {
		return jType1;
	}
	
	public JTextField getType2() {
		return jType2;
	}
	
	public JEntityPicklist getCity() {
		return jCity;
	}
	
	public JTextField getLabel() {
		return jLabel;
	}
	
	public JEntityPicklist getRank1() {
		return jRank1;
	}
	
	public JEntityPicklist getRank2() {
		return jRank2;
	}
	
	public JEntityPicklist getRank3() {
		return jRank3;
	}
	
	public JEntityPicklist getRank4() {
		return jRank4;
	}
	
	public JEntityPicklist getRank5() {
		return jRank5;
	}
	
	public JTextField getRecord1() {
		return jRecord1;
	}
	
	public JTextField getRecord2() {
		return jRecord2;
	}
	
	public JTextField getRecord3() {
		return jRecord3;
	}
	
	public JTextField getRecord4() {
		return jRecord4;
	}
	
	public JTextField getRecord5() {
		return jRecord5;
	}
	
	public JTextField getDate1() {
		return jDate1;
	}
	
	public JTextField getDate2() {
		return jDate2;
	}
	
	public JTextField getDate3() {
		return jDate3;
	}
	
	public JTextField getDate4() {
		return jDate4;
	}
	
	public JTextField getDate5() {
		return jDate5;
	}
	
	public JCheckBox getCounting() {
		return jCounting;
	}
	
	public JTextField getIndex() {
		return jIndex;
	}
	
	public JTextField getExa() {
		return jExa;
	}
	
	public JTextArea getComment() {
		return jComment;
	}
	
	public void setSport(Integer id) {
		SwingUtils.selectValue(jSport, id);
	}
	
	public void setChampionship(Integer id) {
		SwingUtils.selectValue(jChampionship, id);
	}
	
	public void setEvent(Integer id) {
		SwingUtils.selectValue(jEvent, id);
	}
	
	public void setSubevent(Integer id) {
		SwingUtils.selectValue(jSubevent, id);
	}
	
	public void setType1(String s) {
		jType1.setText(s);
	}
	
	public void setType2(String s) {
		jType2.setText(s);
	}
	
	public void setCity(Integer id) {
		SwingUtils.selectValue(jCity, id);
	}
	
	public void setLabel(String s) {
		jLabel.setText(s);
	}
	
	public void setRank1(Integer id) {
		SwingUtils.selectValue(jRank1, id);
	}
	
	public void setRank2(Integer id) {
		SwingUtils.selectValue(jRank2, id);
	}
	
	public void setRank3(Integer id) {
		SwingUtils.selectValue(jRank3, id);
	}
	
	public void setRank4(Integer id) {
		SwingUtils.selectValue(jRank4, id);
	}
	
	public void setRank5(Integer id) {
		SwingUtils.selectValue(jRank5, id);
	}
	
	public void setRecord1(String s) {
		jRecord1.setText(s);
	}
	
	public void setRecord2(String s) {
		jRecord2.setText(s);
	}
	
	public void setRecord3(String s) {
		jRecord3.setText(s);
	}
	
	public void setRecord4(String s) {
		jRecord4.setText(s);
	}
	
	public void setRecord5(String s) {
		jRecord5.setText(s);
	}
	
	public void setDate1(String s) {
		jDate1.setText(s);
	}
	
	public void setDate2(String s) {
		jDate2.setText(s);
	}
	
	public void setDate3(String s) {
		jDate3.setText(s);
	}
	
	public void setDate4(String s) {
		jDate4.setText(s);
	}
	
	public void setDate5(String s) {
		jDate5.setText(s);
	}
	
	public void setCounting(Boolean b) {
		jCounting.setSelected(b != null && b);
	}
	
	public void setIndex(String s) {
		jIndex.setText(s);
	}
	
	public void setExa(String s) {
		jExa.setText(s);
	}
	
	public void setComment(String s) {
		jComment.setText(s);
	}
	
	public void clear() {
		jId.setText("");
		jSport.clear();
		jChampionship.clear();
		jEvent.clear();
		jSubevent.clear();
		jType1.setText("");
		jType2.setText("");
		jCity.clear();
		jLabel.setText("");
		jRank1.clear();jRecord1.setText("");jDate1.setText("");
		jRank2.clear();jRecord2.setText("");jDate2.setText("");
		jRank3.clear();jRecord3.setText("");jDate3.setText("");
		jRank4.clear();jRecord4.setText("");jDate4.setText("");
		jRank5.clear();jRecord5.setText("");jDate5.setText("");
		jCounting.setSelected(false);
		jIndex.setText("");
		jExa.setText("");
		jComment.setText("");
	}
	
	public void focus() {
		jSport.requestFocus();
	}
	
}