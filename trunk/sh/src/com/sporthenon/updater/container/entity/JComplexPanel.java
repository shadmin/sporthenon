package com.sporthenon.updater.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.db.entity.City;
import com.sporthenon.updater.component.JCustomTextField;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.utils.SwingUtils;


public class JComplexPanel extends JAbstractEntityPanel {
	
	private static final long serialVersionUID = 1L;
	
	public JCustomTextField jLabel;
	public JEntityPicklist jCity;
	public JTextField jUrlWiki;

	public JComplexPanel() {
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

        //City
        JLabel lCity = new JLabel(" City:");
        lCity.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCity);
        jCity = new JEntityPicklist(this, City.alias);
        gridPanel.add(jCity);
        
		//URL (WIKI)
		JLabel lUrlWiki = new JLabel(" URL (Wikipedia):");
		lUrlWiki.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lUrlWiki);
		jUrlWiki = new JTextField();
		jUrlWiki.setPreferredSize(TEXT_SIZE);
		gridPanel.add(jUrlWiki);
	}

	public JCustomTextField getLabel() {
		return jLabel;
	}

	public JEntityPicklist getCity() {
		return jCity;
	}
	
	public JTextField getUrlWiki() {
		return jUrlWiki;
	}

	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void setCity(Integer id) {
		SwingUtils.selectValue(jCity, id);
	}
	
	public void setUrlWiki(String s) {
		jUrlWiki.setText(s);
	}
	
	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jCity.clear();
		jUrlWiki.setText("");
	}
	
	public void focus() {
		jLabel.focus();
	}

}
