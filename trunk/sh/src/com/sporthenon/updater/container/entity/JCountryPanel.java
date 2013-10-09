package com.sporthenon.updater.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class JCountryPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	public JTextField jCode;

	public JCountryPanel() {
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
        
        //Code
        JLabel lCode = new JLabel(" Code:");
        lLabel.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCode);
        jCode = new JTextField();
        jCode.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jCode);
	}

	public JTextField getLabel() {
		return jLabel;
	}

	public JTextField getCode() {
		return jCode;
	}

	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void setCode(String s) {
		jCode.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jCode.setText("");
	}
	
	public void focus() {
		jLabel.requestFocus();
	}
	
}
