package com.sporthenon.admin.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.utils.SwingUtils;

public class JOlympicRankingPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JEntityPicklist jOlympics;
	public JEntityPicklist jCountry;
	public JTextField jGold;
	public JTextField jSilver;
	public JTextField jBronze;
	
	public JOlympicRankingPanel() {
		super(6);
		initialize();
	}

	protected void initialize() {
        //Olympics
        JLabel lOlympics = new JLabel(" Olympics:");
        lOlympics.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lOlympics);
        jOlympics = new JEntityPicklist(this, Olympics.alias);
        gridPanel.add(jOlympics);

        //Country
        JLabel lCountry = new JLabel(" Country:");
        lCountry.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCountry);
        jCountry = new JEntityPicklist(this, Country.alias);
        gridPanel.add(jCountry);
        
        //Gold
        JLabel lGold = new JLabel(" Gold:");
        lGold.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lGold);
        jGold = new JTextField();
        gridPanel.add(jGold);
        
        //Silver
        JLabel lSilver = new JLabel(" Silver:");
        lSilver.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lSilver);
        jSilver = new JTextField();
        gridPanel.add(jSilver);
        
        //Bronze
        JLabel lBronze = new JLabel(" Bronze:");
        lBronze.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lBronze);
        jBronze = new JTextField();
        gridPanel.add(jBronze);
	}
	
	public JEntityPicklist getOlympics() {
		return jOlympics;
	}
	
	public JEntityPicklist getCountry() {
		return jCountry;
	}
	
	public JTextField getGold() {
		return jGold;
	}
	
	public JTextField getSilver() {
		return jSilver;
	}
	
	public JTextField getBronze() {
		return jBronze;
	}
	
	public void setOlympics(Integer id) {
		SwingUtils.selectValue(jOlympics, id);
	}
	
	public void setCountry(Integer id) {
		SwingUtils.selectValue(jCountry, id);
	}
	
	public void setGold(String s) {
		jGold.setText(s);
	}

	public void setSilver(String s) {
		jSilver.setText(s);
	}
	
	public void setBronze(String s) {
		jBronze.setText(s);
	}
	
	public void clear() {
		jId.setText("");
		jOlympics.clear();
		jCountry.clear();
		jGold.setText("");
		jSilver.setText("");
		jBronze.setText("");
	}
	
	public void focus() {
		jOlympics.requestFocus();
	}
	
}