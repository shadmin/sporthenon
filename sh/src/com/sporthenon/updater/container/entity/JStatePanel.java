package com.sporthenon.updater.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class JStatePanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	public JTextField jCode;
	public JTextField jCapital;
	public JTextField jUrlWiki;
	
	public JStatePanel() {
		super(5);
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
        
        //Code
        JLabel lCode = new JLabel(" Code:");
        lCode.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCode);
        jCode = new JTextField();
        jCode.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jCode);
        
        //Code
        JLabel lCapital = new JLabel(" Code:");
        lCapital.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCapital);
        jCapital = new JTextField();
        jCapital.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jCapital);
		
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

	public JTextField getCode() {
		return jCode;
	}

	public JTextField getCapital() {
		return jCapital;
	}
	
	public JTextField getUrlWiki() {
		return jUrlWiki;
	}

	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void setCode(String s) {
		jCode.setText(s);
	}

	public void setCapital(String s) {
		jCapital.setText(s);
	}
	
	public void setUrlWiki(String s) {
		jUrlWiki.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jCode.setText("");
		jCapital.setText("");
		jUrlWiki.setText("");
	}
	
	public void focus() {
		jLabel.requestFocus();
	}
	
}
