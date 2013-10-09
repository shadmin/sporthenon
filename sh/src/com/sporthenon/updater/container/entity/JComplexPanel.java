package com.sporthenon.updater.container.entity;

import javax.swing.JLabel;

import com.sporthenon.db.entity.City;
import com.sporthenon.updater.component.JCustomTextField;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.utils.SwingUtils;


public class JComplexPanel extends JAbstractEntityPanel {
	
	private static final long serialVersionUID = 1L;
	
	public JCustomTextField jLabel;
	public JEntityPicklist jCity;

	public JComplexPanel() {
		super(3);
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

        //City
        JLabel lCity = new JLabel(" City:");
        lCity.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCity);
        jCity = new JEntityPicklist(this, City.alias);
        gridPanel.add(jCity);
	}

	public JCustomTextField getLabel() {
		return jLabel;
	}

	public JEntityPicklist getCity() {
		return jCity;
	}

	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void setCity(Integer id) {
		SwingUtils.selectValue(jCity, id);
	}

	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jCity.clear();
	}
	
	public void focus() {
		jLabel.focus();
	}

}
