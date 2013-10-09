package com.sporthenon.updater.container.entity;

import javax.swing.JLabel;

import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.State;
import com.sporthenon.updater.component.JCustomTextField;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.utils.SwingUtils;


public class JCityPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JCustomTextField jLabel;
	public JEntityPicklist jState;
	public JEntityPicklist jCountry;

	public JCityPanel() {
		super(4);
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

        //State
        JLabel lState = new JLabel(" State:");
        lState.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lState);
        jState = new JEntityPicklist(this, State.alias);
        gridPanel.add(jState);
        
        //Country
        JLabel lCountry = new JLabel(" Country:");
        lCountry.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCountry);
        jCountry = new JEntityPicklist(this, Country.alias);
        gridPanel.add(jCountry);
	}

	public JCustomTextField getLabel() {
		return jLabel;
	}

	public JEntityPicklist getState() {
		return jState;
	}
	
	public JEntityPicklist getCountry() {
		return jCountry;
	}

	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void setState(Integer id) {
		SwingUtils.selectValue(jState, id);
	}

	public void setCountry(Integer id) {
		SwingUtils.selectValue(jCountry, id);
	}

	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jState.clear();
		jCountry.clear();
	}
	
	public void focus() {
		jLabel.focus();
	}
	
}