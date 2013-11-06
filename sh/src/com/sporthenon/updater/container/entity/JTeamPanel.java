package com.sporthenon.updater.container.entity;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.Team;
import com.sporthenon.updater.component.JCustomTextField;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.utils.SwingUtils;


public class JTeamPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JCustomTextField jLabel;
	public JEntityPicklist jSport;
	public JEntityPicklist jCountry;
	public JEntityPicklist jParent;
	public JTextField jConference;
	public JTextField jDivision;
	public JCheckBox jDeleted;
	public JTextField jComment;
	public JTextField jYear1;
	public JTextField jYear2;
	
	public JTeamPanel() {
		super(11);
		initialize();
	}

	protected void initialize() {
        //Name
        JLabel lLabel = new JLabel(" Name:");
        lLabel.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLabel);
        jLabel = new JCustomTextField();
        jLabel.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jLabel);
        
        //Sport
        JLabel lSport = new JLabel(" Sport:");
        lSport.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lSport);
        jSport = new JEntityPicklist(null, Sport.alias);
        gridPanel.add(jSport);
        
        //Country
        JLabel lCountry = new JLabel(" Country:");
        lCountry.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCountry);
        jCountry = new JEntityPicklist(null, Country.alias);
        gridPanel.add(jCountry);
        
        //Parent
        JLabel lParent = new JLabel(" Parent:");
        lParent.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lParent);
        jParent = new JEntityPicklist(null, Team.alias);
        gridPanel.add(jParent);
        
        //Conference
        JLabel lConference = new JLabel(" Conference:");
        lConference.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lConference);
        jConference = new JTextField();
        jConference.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jConference);
        
        //Division
        JLabel lDivision = new JLabel(" Division:");
        lDivision.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lDivision);
        jDivision = new JTextField();
        jDivision.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jDivision);
        
        //Deleted
        JLabel lDeleted = new JLabel(" Deleted (0=Yes, 1=No):");
        lDeleted.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lDeleted);
        jDeleted = new JCheckBox();
        jDeleted.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jDeleted);
        
        //Comment
        JLabel lComment = new JLabel(" Comment:");
        lComment.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lComment);
        jComment = new JTextField();
        jComment.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jComment);
        
        //Year1
        JLabel lYear1 = new JLabel(" Year1:");
        lYear1.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lYear1);
        jYear1 = new JTextField();
        jYear1.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jYear1);
        
        //Year2
        JLabel lYear2 = new JLabel(" Year2:");
        lYear2.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lYear2);
        jYear2 = new JTextField();
        jYear2.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jYear2);
	}

	public JCustomTextField getLabel() {
		return jLabel;
	}

	public JEntityPicklist getSport() {
		return jSport;
	}

	public JEntityPicklist getCountry() {
		return jCountry;
	}

	public JEntityPicklist getParentTeam() {
		return jParent;
	}

	public JTextField getConference() {
		return jConference;
	}

	public JTextField getDivision() {
		return jDivision;
	}

	public JCheckBox getDeleted() {
		return jDeleted;
	}

	public JTextField getComment() {
		return jComment;
	}

	public JTextField getYear1() {
		return jYear1;
	}

	public JTextField getYear2() {
		return jYear2;
	}

	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void setSport(Integer id) {
		SwingUtils.selectValue(jSport, id);
	}

	public void setCountry(Integer id) {
		SwingUtils.selectValue(jCountry, id);
	}

	public void setParentTeam(Integer id) {
		SwingUtils.selectValue(jParent, id);
	}

	public void setConference(String s) {
		jConference.setText(s);
	}

	public void setDivision(String s) {
		jDivision.setText(s);
	}

	public void setDeleted(boolean b) {
		jDeleted.setSelected(b);
	}

	public void setComment(String s) {
		jComment.setText(s);
	}

	public void setYear1(String s) {
		jYear1.setText(s);;
	}

	public void setYear2(String s) {
		jYear2.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jSport.clear();
		jCountry.clear();
		jConference.setText("");
		jDeleted.setSelected(false);
		jDivision.setText("");
		jComment.setText("");
		jYear1.setText("");
		jYear2.setText("");
	}
	
	public void focus() {
		jLabel.focus();
	}
	
}