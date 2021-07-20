package com.sporthenon.admin.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.utils.SwingUtils;

public class JCalendarPanel extends JAbstractEntityPanel {

private static final long serialVersionUID = 1L;
	
	public JEntityPicklist jSport;
	public JEntityPicklist jChampionship;
	public JEntityPicklist jEvent;
	public JEntityPicklist jSubevent;
	public JEntityPicklist jSubevent2;
	public JEntityPicklist jComplex;
	public JEntityPicklist jCity;
	public JEntityPicklist jCountry;
	public JTextField jDate1;
	public JTextField jDate2;
	
	public JCalendarPanel() {
		super(11);
		initialize();
		//setPreferredSize(new Dimension(310, (LINE_HEIGHT * 50) - 50));
	}

	protected void initialize() {
        //Sport
        JLabel lSport = new JLabel(" Sport:");
        lSport.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lSport);
        jSport = new JEntityPicklist(this, Sport.alias, false);
        gridPanel.add(jSport);
        
        //Championship
        JLabel lChampionship = new JLabel(" Championship:");
        lChampionship.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lChampionship);
        jChampionship = new JEntityPicklist(this, Championship.alias, false);
        gridPanel.add(jChampionship);
        
        //Event
        JLabel lEvent = new JLabel(" Event:");
        lEvent.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lEvent);
        jEvent = new JEntityPicklist(this, Event.alias, false);
        gridPanel.add(jEvent);
        
        //Subevent
        JLabel lSubevent = new JLabel(" Subevent:");
        lSubevent.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lSubevent);
        jSubevent = new JEntityPicklist(this, Event.alias, false);
        gridPanel.add(jSubevent);
        
        //Subevent
        JLabel lSubevent2 = new JLabel(" Subevent #2:");
        lSubevent2.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lSubevent2);
        jSubevent2 = new JEntityPicklist(this, Event.alias, false);
        gridPanel.add(jSubevent2);
        
        //City
        JLabel lCity = new JLabel(" City:");
        lCity.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCity);
        jCity = new JEntityPicklist(this, City.alias, true);
        gridPanel.add(jCity);
        
        //Complex
        JLabel lComplex = new JLabel(" Complex:");
        lComplex.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lComplex);
        jComplex = new JEntityPicklist(this, Complex.alias, true);
        gridPanel.add(jComplex);
        
        //Country
        JLabel lCountry = new JLabel(" Country:");
        lCountry.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCountry);
        jCountry = new JEntityPicklist(this, Country.alias, false);
        gridPanel.add(jCountry);
        
        //Date1
        JLabel lDate1 = new JLabel(" Date1:");
        lDate1.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lDate1);
        jDate1 = new JTextField();
        jDate1.setPreferredSize(TEXT_SIZE);
        jDate1 = new JTextField();
        jDate1.addFocusListener(this);
        gridPanel.add(jDate1);
        
        //Date2
        JLabel lDate2 = new JLabel(" Date2:");
        lDate2.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lDate2);
        jDate2 = new JTextField();
        jDate2.setPreferredSize(TEXT_SIZE);
        jDate2.addFocusListener(this);
        gridPanel.add(jDate2);
	}
	
	public JEntityPicklist getSport() {
		return jSport;
	}
	
	public JEntityPicklist getChampionship() {
		return jChampionship;
	}
	
	public JEntityPicklist getEvent() {
		return jEvent;
	}
	
	public JEntityPicklist getSubevent() {
		return jSubevent;
	}
	
	public JEntityPicklist getSubevent2() {
		return jSubevent2;
	}
	
	public JEntityPicklist getComplex() {
		return jComplex;
	}
	
	public JEntityPicklist getCity() {
		return jCity;
	}
	
	public JEntityPicklist getCountry() {
		return jCountry;
	}
	
	public JTextField getDate1() {
		return jDate1;
	}
	
	public JTextField getDate2() {
		return jDate2;
	}
	
	public void setSport(Integer id) {
		SwingUtils.selectValue(jSport, id);
	}
	
	public void setChampionship(Integer id) {
		SwingUtils.selectValue(jChampionship, id);
	}
	
	public void setEvent(Integer id) {
		SwingUtils.selectValue(jEvent, id);
	}
	
	public void setSubevent(Integer id) {
		SwingUtils.selectValue(jSubevent, id);
	}
	
	public void setSubevent2(Integer id) {
		SwingUtils.selectValue(jSubevent2, id);
	}
	
	public void setComplex(Integer id) {
		SwingUtils.selectValue(jComplex, id);
	}
	
	public void setCity(Integer id) {
		SwingUtils.selectValue(jCity, id);
	}
	
	public void setCountry(Integer id) {
		SwingUtils.selectValue(jCountry, id);
	}
	
	public void setDate1(String s) {
		jDate1.setText(s);
	}
	
	public void setDate2(String s) {
		jDate2.setText(s);
	}
	
	public void clear() {
		jId.setText("");
		jSport.clear();
		jChampionship.clear();
		jEvent.clear();
		jSubevent.clear();
		jSubevent2.clear();
		jComplex.clear();
		jCity.clear();
		jCountry.clear();
		jDate1.setText("");
		jDate2.setText("");
	}
	
	public void focus() {
		jSport.requestFocus();
	}
	
}