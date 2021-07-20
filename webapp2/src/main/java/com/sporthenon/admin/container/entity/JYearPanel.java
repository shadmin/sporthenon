package com.sporthenon.admin.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class JYearPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	
	public JYearPanel() {
		super(2);
		initialize();
	}

	protected void initialize() {
        //Name
        JLabel lLabel = new JLabel(" Name:");
        lLabel.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLabel);
        jLabel = new JTextField();
        jLabel.setPreferredSize(TEXT_SIZE);
        jLabel.addFocusListener(this);
        gridPanel.add(jLabel);
	}

	public JTextField getLabel() {
		return jLabel;
	}
	
	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLabel.setText("");
	}
	
	public void focus() {
		jLabel.requestFocus();
	}
	
}
