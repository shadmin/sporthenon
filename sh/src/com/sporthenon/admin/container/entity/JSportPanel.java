package com.sporthenon.admin.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class JSportPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	public JTextField jLabelFR;
	public JTextField jType;
	public JTextField jIndex;
	public JTextField jImgURL;
	
	public JSportPanel() {
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
        
        //Type
        JLabel lType = new JLabel(" Type (If Olympic: 0=Winter / 1=Summer):");
        lType.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lType);
        jType = new JTextField();
        jType.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jType);
        
        //Index
        JLabel lIndex = new JLabel(" Index:");
        lIndex.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lIndex);
        jIndex = new JTextField();
        gridPanel.add(jIndex);
        
		//Image URL
		JLabel lImgURL = new JLabel(" Image URL:");
		lImgURL.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lImgURL);
		jImgURL = new JTextField();
		jImgURL.setPreferredSize(TEXT_SIZE);
		gridPanel.add(jImgURL);
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
	
	public JTextField getImgURL() {
		return jImgURL;
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
	
	public void setImgURL(String s) {
		jImgURL.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jLabelFR.setText("");
		jType.setText("");
		jIndex.setText("");
		jImgURL.setText("");
	}
	
	public void focus() {
		jLabel.requestFocus();
	}
	
}
