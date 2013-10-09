package com.sporthenon.updater.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class JSportPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	public JTextField jType;
	public JTextField jWebsite;
	
	public JSportPanel() {
		super(4);
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
        
        //Type
        JLabel lType = new JLabel(" Type (If Olympic: 0=Winter / 1=Summer):");
        lType.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lType);
        jType = new JTextField();
        jType.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jType);
        
        //Type
        JLabel lWebsite = new JLabel(" Website:");
        lWebsite.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lWebsite);
        jWebsite = new JTextField();
        jWebsite.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jWebsite);
	}

	public JTextField getLabel() {
		return jLabel;
	}

	public JTextField getType() {
		return jType;
	}

	public JTextField getWebsite() {
		return jWebsite;
	}

	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void setType(String s) {
		jType.setText(s);
	}

	public void setWebsite(String s) {
		jWebsite.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jType.setText("");
		jWebsite.setText("");
	}
	
	public void focus() {
		jLabel.requestFocus();
	}
	
}
