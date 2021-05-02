package com.sporthenon.admin.container.entity;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Year;
import com.sporthenon.utils.SwingUtils;


public class JOlympicsPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JEntityPicklist jYear;
	public JEntityPicklist jCity;
	public JTextField jType;
	public JTextField jStart;
	public JTextField jEnd;
	public JTextField jSports;
	public JTextField jEvents;
	public JTextField jCountries;
	public JTextField jPersons;
	public JCheckBox jNopic;
	
	public JOlympicsPanel() {
		super(11);
		initialize();
	}

	protected void initialize() {
        //Name
        JLabel lYear = new JLabel(" Year:");
        lYear.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lYear);
        jYear = new JEntityPicklist(this, Year.alias, false);
        gridPanel.add(jYear);
        
        //City
        JLabel lCity = new JLabel(" City:");
        lCity.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCity);
        jCity = new JEntityPicklist(this, City.alias, true);
        gridPanel.add(jCity);

        //Type
        JLabel lType = new JLabel(" Type (0=Winter / 1=Summer):");
        lType.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lType);
        jType = new JTextField();
        jType.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jType);
        
        //Start
        JLabel lStart = new JLabel(" Start:");
        lStart.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lStart);
        jStart = new JTextField();
        jStart.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jStart);
        
        //End
        JLabel lEnd = new JLabel(" End:");
        lEnd.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lEnd);
        jEnd = new JTextField();
        jEnd.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jEnd);
        
        //Sports
        JLabel lSports = new JLabel(" Sports:");
        lSports.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lSports);
        jSports = new JTextField();
        jSports.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jSports);
        
        //Events
        JLabel lEvents = new JLabel(" Events:");
        lEvents.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lEvents);
        jEvents = new JTextField();
        jEvents.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jEvents);

        //Countries
        JLabel lCountries = new JLabel(" Countries:");
        lCountries.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCountries);
        jCountries = new JTextField();
        jCountries.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jCountries);
        
        //Persons
        JLabel lPersons = new JLabel(" Persons:");
        lPersons.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lPersons);
        jPersons = new JTextField();
        jPersons.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jPersons);
        
        //Nopic
        JLabel lNopic = new JLabel(" No picture:");
        lNopic.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lNopic);
        jNopic = new JCheckBox();
        gridPanel.add(jNopic);
	}

	public JEntityPicklist getYear() {
		return jYear;
	}

	public JEntityPicklist getCity() {
		return jCity;
	}

	public JTextField getType() {
		return jType;
	}

	public JTextField getStart() {
		return jStart;
	}

	public JTextField getEnd() {
		return jEnd;
	}

	public JTextField getSports() {
		return jSports;
	}

	public JTextField getEvents() {
		return jEvents;
	}

	public JTextField getCountries() {
		return jCountries;
	}

	public JTextField getPersons() {
		return jPersons;
	}
	
	public JCheckBox getNopic() {
		return jNopic;
	}
	
	public void setYear(Integer id) {
		SwingUtils.selectValue(jYear, id);;
	}

	public void setCity(Integer id) {
		SwingUtils.selectValue(jCity, id);
	}

	public void setType(String s) {
		jType.setText(s);
	}

	public void setStart(String s) {
		jStart.setText(s);
	}

	public void setEnd(String s) {
		jEnd.setText(s);
	}

	public void setSports(String s) {
		jSports.setText(s);
	}

	public void setEvents(String s) {
		jEvents.setText(s);;
	}

	public void setCountries(String s) {
		jCountries.setText(s);
	}

	public void setPersons(String s) {
		jPersons.setText(s);
	}
	
	public void setNopic(Boolean b) {
		jNopic.setSelected(b != null && b);
	}
	
	public void clear() {
		jId.setText("");
		jType.setText("");
		jStart.setText("");
		jEnd.setText("");
		jSports.setText("");
		jEvents.setText("");
		jCountries.setText("");
		jPersons.setText("");
		jYear.clear();
		jCity.clear();
		jNopic.setSelected(false);
	}
	
	public void focus() {
		jYear.requestFocus();
	}
	
}
