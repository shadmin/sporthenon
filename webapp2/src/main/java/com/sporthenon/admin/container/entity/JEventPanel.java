package com.sporthenon.admin.container.entity;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.db.entity.Type;
import com.sporthenon.utils.SwingUtils;


public class JEventPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	public JTextField jLabelFR;
	public JEntityPicklist jType;
	public JTextField jIndex;
	public JCheckBox jNopic;
	
	public JEventPanel() {
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
        JLabel lType = new JLabel(" Type:");
        lType.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lType);
        jType = new JEntityPicklist(this, Type.alias, false);
        gridPanel.add(jType);
        
        //Index
        JLabel lIndex = new JLabel(" Index:");
        lIndex.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lIndex);
        jIndex = new JTextField();
        gridPanel.add(jIndex);
        
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

	public JEntityPicklist getType() {
		return jType;
	}
	
	public JTextField getIndex() {
		return jIndex;
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
	
	public void setType(Integer id) {
		SwingUtils.selectValue(jType, id);
	}
	
	public void setIndex(String s) {
		jIndex.setText(s);
	}
	
	public void setNopic(Boolean b) {
		jNopic.setSelected(b != null && b);
	}
	
	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jLabelFR.setText("");
		jType.clear();
		jIndex.setText("");
		jNopic.setSelected(false);
	}
	
	public void focus() {
		jLabel.requestFocus();
	}
	
}
