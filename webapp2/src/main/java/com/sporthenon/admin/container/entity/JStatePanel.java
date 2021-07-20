package com.sporthenon.admin.container.entity;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class JStatePanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	public JTextField jLabelFR;
	public JTextField jCode;
	public JTextField jCapital;
	public JCheckBox jNopic;
	
	public JStatePanel() {
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
        jLabel.addFocusListener(this);
        gridPanel.add(jLabel);
        
        //Name (FR)
        JLabel lLabelFR = new JLabel(" Name (FR):");
        lLabelFR.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLabelFR);
        jLabelFR = new JTextField();
        jLabelFR.setPreferredSize(TEXT_SIZE);
        jLabelFR.addFocusListener(this);
        gridPanel.add(jLabelFR);
        
        //Code
        JLabel lCode = new JLabel(" Code:");
        lCode.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCode);
        jCode = new JTextField();
        jCode.setPreferredSize(TEXT_SIZE);
        jCode.addFocusListener(this);
        gridPanel.add(jCode);
        
        //Capital
        JLabel lCapital = new JLabel(" Capital:");
        lCapital.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCapital);
        jCapital = new JTextField();
        jCapital.setPreferredSize(TEXT_SIZE);
        jCapital.addFocusListener(this);
        gridPanel.add(jCapital);
        
        //Nopic
        JLabel lNopic = new JLabel(" No picture:");
        lNopic.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lNopic);
        jNopic = new JCheckBox();
        gridPanel.add(jNopic);
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

	public JTextField getCapital() {
		return jCapital;
	}
	
	public JCheckBox getNopic() {
		return jNopic;
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

	public void setCapital(String s) {
		jCapital.setText(s);
	}
	
	public void setNopic(Boolean b) {
		jNopic.setSelected(b != null && b);
	}
	
	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jLabelFR.setText("");
		jCode.setText("");
		jCapital.setText("");
		jNopic.setSelected(false);
	}
	
	public void focus() {
		jLabel.requestFocus();
	}
	
}
