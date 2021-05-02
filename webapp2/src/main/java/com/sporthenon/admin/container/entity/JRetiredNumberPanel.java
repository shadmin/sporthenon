package com.sporthenon.admin.container.entity;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.window.JMainFrame;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.League;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.Year;
import com.sporthenon.utils.SwingUtils;

public class JRetiredNumberPanel extends JAbstractEntityPanel implements ItemListener {

private static final long serialVersionUID = 1L;
	
	public JEntityPicklist jLeague;
	public JEntityPicklist jTeam;
	public JEntityPicklist jPerson;
	public JEntityPicklist jYear;
	public JTextField jNumber;

	public JRetiredNumberPanel() {
		super(6);
		initialize();
	}

	protected void initialize() {
        //League
        JLabel lLeague = new JLabel(" League:");
        lLeague.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLeague);
        jLeague = new JEntityPicklist(this, League.alias, false);
        jLeague.getAddButton().setVisible(false);
        jLeague.getCombobox().addItemListener(this);
        gridPanel.add(jLeague);
        
        //Team
        JLabel lTeam = new JLabel(" Team:");
        lTeam.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lTeam);
        jTeam = new JEntityPicklist(this, Team.alias, true);
        gridPanel.add(jTeam);

        //Person
        JLabel lPerson = new JLabel(" Person:");
        lPerson.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lPerson);
        jPerson = new JEntityPicklist(this, Athlete.alias, true);
        gridPanel.add(jPerson);
        
        //Year
        JLabel lYear = new JLabel(" Year:");
        lPerson.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lYear);
        jYear = new JEntityPicklist(this, Year.alias, false);
        gridPanel.add(jYear);
        
        //Number
        JLabel lNumber = new JLabel(" Number:");
        lNumber.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lNumber);
        jNumber = new JTextField();
        gridPanel.add(jNumber);
	}
	
	public JEntityPicklist getLeague() {
		return jLeague;
	}

	public JEntityPicklist getTeam() {
		return jTeam;
	}
	
	public JEntityPicklist getPerson() {
		return jPerson;
	}
	
	public JEntityPicklist getYear() {
		return jYear;
	}
	
	public JTextField getNumber() {
		return jNumber;
	}
	
	public void setLeague(Integer id) {
		SwingUtils.selectValue(jLeague, id);
	}
	
	public void setTeam(Integer id) {
		SwingUtils.selectValue(jTeam, id);
	}
	
	public void setPerson(Integer id) {
		SwingUtils.selectValue(jPerson, id);
	}
	
	public void setYear(Integer id) {
		SwingUtils.selectValue(jYear, id);
	}
	
	public void setNumber(String s) {
		jNumber.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLeague.clear();
		jTeam.clear();
		jYear.clear();
		jPerson.clear();
		jNumber.setText("");
	}
	
	public void focus() {
		jLeague.requestFocus();
	}
	
	public void itemStateChanged(ItemEvent e) {
		Integer leagueId = SwingUtils.getValue(jLeague);
		if (leagueId != null) {
			SwingUtils.fillPicklist(jTeam, JMainFrame.getPicklists().get(Team.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
			SwingUtils.fillPicklist(jPerson, JMainFrame.getPicklists().get(Athlete.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
		}
	}
	
}