package com.sporthenon.updater.container.entity;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.League;
import com.sporthenon.db.entity.Year;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.updater.window.JMainFrame;
import com.sporthenon.utils.SwingUtils;

public class JHallOfFamePanel extends JAbstractEntityPanel implements ItemListener {

	private static final long serialVersionUID = 1L;
	
	public JEntityPicklist jLeague;
	public JEntityPicklist jYear;
	public JEntityPicklist jPerson;
	public JCheckBox jDeceased;
	
	public JHallOfFamePanel() {
		super(5);
		initialize();
	}

	protected void initialize() {
        //League
        JLabel lLeague = new JLabel(" League:");
        lLeague.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLeague);
        jLeague = new JEntityPicklist(this, League.alias);
        jLeague.getAddButton().setVisible(false);
        jLeague.setPreferredSize(TEXT_SIZE);
        jLeague.getPicklist().addItemListener(this);
        gridPanel.add(jLeague);
        
        //Year
        JLabel lYear = new JLabel(" Year:");
        lYear.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lYear);
        jYear = new JEntityPicklist(this, Year.alias);
        gridPanel.add(jYear);

        //Person
        JLabel lPerson = new JLabel(" Person:");
        lPerson.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lPerson);
        jPerson = new JEntityPicklist(this, Athlete.alias);
        gridPanel.add(jPerson);
        
        //Deceased
        JLabel lDeceased = new JLabel(" Deceased:");
        lDeceased.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lDeceased);
        jDeceased = new JCheckBox();
        gridPanel.add(jDeceased);
	}
	
	public JEntityPicklist getLeague() {
		return jLeague;
	}

	public JEntityPicklist getYear() {
		return jYear;
	}
	
	public JEntityPicklist getPerson() {
		return jPerson;
	}
	
	public JCheckBox getDeceased() {
		return jDeceased;
	}
	
	public void setLeague(Integer id) {
		SwingUtils.selectValue(jLeague, id);
	}
	
	public void setYear(Integer id) {
		SwingUtils.selectValue(jYear, id);
	}
	
	public void setPerson(Integer id) {
		SwingUtils.selectValue(jPerson, id);
	}
	
	public void setDeceased(Boolean b) {
		jDeceased.setSelected(b);
	}

	public void clear() {
		jId.setText("");
		jLeague.clear();
		jYear.clear();
		jPerson.clear();
		jDeceased.setText("");
	}
	
	public void focus() {
		jLeague.requestFocus();
	}
	
	public void itemStateChanged(ItemEvent e) {
		int leagueId = SwingUtils.getValue(jLeague);
		SwingUtils.fillPicklist(jPerson, JMainFrame.getPicklists().get(Athlete.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
	}
	
}