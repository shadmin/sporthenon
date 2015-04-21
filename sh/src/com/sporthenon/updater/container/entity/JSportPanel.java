package com.sporthenon.updater.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class JSportPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	public JTextField jLabelFR;
	public JTextField jType;
	public JTextField jWebsite;
	public JTextField jIndex;
	public JTextField jUrlWiki;
	public JTextField jUrlOlyref;
	public JTextField jWikiPattern;
	
	public JSportPanel() {
		super(9);
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
        JLabel lType = new JLabel(" Type (If Olympic: 0=Winter / 1=Summer):");
        lType.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lType);
        jType = new JTextField();
        jType.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jType);
        
        //Website
        JLabel lWebsite = new JLabel(" Website:");
        lWebsite.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lWebsite);
        jWebsite = new JTextField();
        jWebsite.setPreferredSize(TEXT_SIZE);
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
		
		//URL (OLYMPICS-REFERENCE)
		JLabel lUrlOlyref = new JLabel(" URL (Olympics-reference):");
		lUrlOlyref.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lUrlOlyref);
		jUrlOlyref = new JTextField();
		jUrlOlyref.setPreferredSize(TEXT_SIZE);
		gridPanel.add(jUrlOlyref);
		
		//WIKI PATTERN
		JLabel lWikiPattern = new JLabel(" Wiki Pattern:");
		lWikiPattern.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lWikiPattern);
		jWikiPattern = new JTextField();
		jWikiPattern.setPreferredSize(TEXT_SIZE);
		gridPanel.add(jWikiPattern);
	}

	public JTextField getLabel() {
		return jLabel;
	}

	public JTextField getLabelFR() {
		return jLabelFR;
	}

	public JTextField getType() {
		return jType;
	}
	
	public JTextField getIndex() {
		return jIndex;
	}

	public JTextField getWebsite() {
		return jWebsite;
	}
	
	public JTextField getUrlWiki() {
		return jUrlWiki;
	}
	
	public JTextField getUrlOlyref() {
		return jUrlOlyref;
	}
	
	public JTextField getWikiPattern() {
		return jWikiPattern;
	}

	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void setLabelFR(String s) {
		jLabelFR.setText(s);
	}

	public void setType(String s) {
		jType.setText(s);
	}

	public void setIndex(String s) {
		jIndex.setText(s);
	}
	
	public void setWebsite(String s) {
		jWebsite.setText(s);
	}
	
	public void setUrlWiki(String s) {
		jUrlWiki.setText(s);
	}
	
	public void setUrlOlyref(String s) {
		jUrlOlyref.setText(s);
	}
	
	public void setWikiPattern(String s) {
		jWikiPattern.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jLabelFR.setText("");
		jType.setText("");
		jIndex.setText("");
		jWebsite.setText("");
		jUrlWiki.setText("");
		jUrlOlyref.setText("");
		jWikiPattern.setText("");
	}
	
	public void focus() {
		jLabel.requestFocus();
	}
	
}
