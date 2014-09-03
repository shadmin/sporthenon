package com.sporthenon.updater.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.db.entity.Type;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.utils.SwingUtils;


public class JEventPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	public JTextField jLabelFR;
	public JEntityPicklist jType;
	public JTextField jWebsite;
	public JTextField jIndex;
	public JTextField jUrlWiki;
	
	public JEventPanel() {
		super(8);
		initialize();
	}

	protected void initialize() {
        //Name
        JLabel lLabel = new JLabel(" Name:");
        lLabel.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLabel);
        jLabel = new JTextField();
        jLabel.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jLabel);
        
        //Name (FR)
        JLabel lLabelFR = new JLabel(" Name (FR):");
        lLabelFR.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLabelFR);
        jLabelFR = new JTextField();
        jLabelFR.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jLabelFR);
        
        //Type
        JLabel lType = new JLabel(" Type:");
        lType.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lType);
        jType = new JEntityPicklist(this, Type.alias);
        gridPanel.add(jType);
        
        //Website
        JLabel lWebsite = new JLabel(" Website:");
        lWebsite.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lWebsite);
        jWebsite = new JTextField();
        gridPanel.add(jWebsite);
        
        //Index
        JLabel lIndex = new JLabel(" Index:");
        lIndex.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lIndex);
        jIndex = new JTextField();
        gridPanel.add(jIndex);
        
		//URL (WIKI)
		JLabel lUrlWiki = new JLabel(" URL (Wikipedia):");
		lUrlWiki.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lUrlWiki);
		jUrlWiki = new JTextField();
		jUrlWiki.setPreferredSize(TEXT_SIZE);
		gridPanel.add(jUrlWiki);
	}

	public JTextField getLabel() {
		return jLabel;
	}
	
	public JTextField getLabelFR() {
		return jLabelFR;
	}

	public JEntityPicklist getType() {
		return jType;
	}
	
	public JTextField getWebsite() {
		return jWebsite;
	}
	
	public JTextField getIndex() {
		return jIndex;
	}
	
	public JTextField getUrlWiki() {
		return jUrlWiki;
	}
	
	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void setLabelFR(String s) {
		jLabelFR.setText(s);
	}
	
	public void setType(Integer id) {
		SwingUtils.selectValue(jType, id);
	}
	
	public void setWebsite(String s) {
		jWebsite.setText(s);
	}

	public void setIndex(String s) {
		jIndex.setText(s);
	}
	
	public void setUrlWiki(String s) {
		jUrlWiki.setText(s);
	}
	
	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jLabelFR.setText("");
		jType.clear();
		jIndex.setText("");
		jUrlWiki.setText("");
	}
	
	public void focus() {
		jLabel.requestFocus();
	}
	
}
