package com.sporthenon.updater.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class JChampionshipPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	public JTextField jLabelFR;
	public JTextField jWebsite;
	public JTextField jIndex;
	public JTextField jUrlWiki;

	public JChampionshipPanel() {
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
        
        //Website
        JLabel lWebsite = new JLabel(" Website:");
        lWebsite.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lWebsite);
        jWebsite = new JTextField();
        jLabel.setPreferredSize(TEXT_SIZE);
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
		jWebsite.setText("");
		jIndex.setText("");
		jUrlWiki.setText("");
	}
	
	public void focus() {
		jLabel.requestFocus();
	}

}