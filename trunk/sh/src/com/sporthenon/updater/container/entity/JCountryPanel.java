package com.sporthenon.updater.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class JCountryPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	public JTextField jLabelFR;
	public JTextField jCode;
	public JTextField jUrlWiki;
	public JTextField jUrlOlyref;

	public JCountryPanel() {
		super(6);
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
        
        //Code
        JLabel lCode = new JLabel(" Code:");
        lLabel.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCode);
        jCode = new JTextField();
        jCode.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jCode);
        
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
	}

	public JTextField getLabel() {
		return jLabel;
	}

	public JTextField getLabelFR() {
		return jLabelFR;
	}
	
	public JTextField getCode() {
		return jCode;
	}
	
	public JTextField getUrlWiki() {
		return jUrlWiki;
	}
	
	public JTextField getUrlOlyref() {
		return jUrlOlyref;
	}

	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void setLabelFR(String s) {
		jLabelFR.setText(s);
	}

	
	public void setCode(String s) {
		jCode.setText(s);
	}
	
	public void setUrlWiki(String s) {
		jUrlWiki.setText(s);
	}
	
	public void setUrlOlyref(String s) {
		jUrlOlyref.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jLabelFR.setText("");
		jCode.setText("");
		jUrlWiki.setText("");
		jUrlOlyref.setText("");
	}
	
	public void focus() {
		jLabel.requestFocus();
	}
	
}
