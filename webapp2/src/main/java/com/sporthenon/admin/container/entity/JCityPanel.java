package com.sporthenon.admin.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JCustomTextField;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.component.JLinkTextField;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.State;
import com.sporthenon.utils.SwingUtils;


public class JCityPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JCustomTextField jLabel;
	public JTextField jLabelFR;
	public JEntityPicklist jState;
	public JEntityPicklist jCountry;
	public JLabel lLink;
	public JLinkTextField jLink;
	
	public JCityPanel() {
		super(7);
		initialize();
	}

	protected void initialize() {
        //Name
        JLabel lLabel = new JLabel(" Name:");
        lLabel.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLabel);
        jLabel = new JCustomTextField();
        jLabel.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jLabel);
        
        //Name (FR)
        JLabel lLabelFR = new JLabel(" Name (FR):");
        lLabelFR.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLabelFR);
        jLabelFR = new JTextField();
        jLabelFR.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jLabelFR);

        //State
        JLabel lState = new JLabel(" State:");
        lState.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lState);
        jState = new JEntityPicklist(this, State.alias, false);
        gridPanel.add(jState);
        
        //Country
        JLabel lCountry = new JLabel(" Country:");
        lCountry.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCountry);
        jCountry = new JEntityPicklist(this, Country.alias, false);
        gridPanel.add(jCountry);
        
		//Link
		lLink = new JLabel(" Linked to:");
		lLink.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lLink);
		jLink = new JLinkTextField(this, City.alias);
		jLink.setPreferredSize(TEXT_SIZE);
		gridPanel.add(jLink);
	}

	public JCustomTextField getLabel() {
		return jLabel;
	}

	public JTextField getLabelFR() {
		return jLabelFR;
	}
	
	public JEntityPicklist getState() {
		return jState;
	}
	
	public JEntityPicklist getCountry() {
		return jCountry;
	}
	
	public JLinkTextField getLink() {
		return jLink;
	}
	
	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void setLabelFR(String s) {
		jLabelFR.setText(s);
	}
	
	public void setState(Integer id) {
		SwingUtils.selectValue(jState, id);
	}

	public void setCountry(Integer id) {
		SwingUtils.selectValue(jCountry, id);
	}
	
	public void setLink(String s) {
		jLink.setText(s);
	}
	
	public void setLinkLabel(String s) {
		lLink.setText(s);
	}
	
	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jLabelFR.setText("");
		jState.clear();
		jCountry.clear();
		jLink.setText("");
		setLinkLabel(" Linked to:");
	}
	
	public void focus() {
		jLabel.focus();
	}
	
}