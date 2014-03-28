package com.sporthenon.updater.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

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
	public JTextField jUrlWiki;

	public JCityPanel() {
		super(5);
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

	public JEntityPicklist getState() {
		return jState;
	}
	
	public JEntityPicklist getCountry() {
		return jCountry;
	}
	
	public JTextField getUrlWiki() {
		return jUrlWiki;
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
	
	public void setUrlWiki(String s) {
		jUrlWiki.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jState.clear();
		jCountry.clear();
		jUrlWiki.setText("");
	}
	
	public void focus() {
		jLabel.focus();
	}
	
}