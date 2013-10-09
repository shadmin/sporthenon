package com.sporthenon.updater.container.entity;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.League;
import com.sporthenon.db.entity.Team;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.updater.window.JMainFrame;
import com.sporthenon.utils.SwingUtils;

public class JRetiredNumberPanel extends JAbstractEntityPanel implements ItemListener {

private static final long serialVersionUID = 1L;
	
	public JEntityPicklist jLeague;
	public JEntityPicklist jTeam;
	public JEntityPicklist jPerson;
	public JTextField jNumber;
	
	public JRetiredNumberPanel() {
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
        
        //Team
        JLabel lTeam = new JLabel(" Team:");
        lTeam.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lTeam);
        jTeam = new JEntityPicklist(this, Team.alias);
        gridPanel.add(jTeam);

        //Person
        JLabel lPerson = new JLabel(" Person:");
        lPerson.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lPerson);
        jPerson = new JEntityPicklist(this, Athlete.alias);
        gridPanel.add(jPerson);
        
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
	
	public void setNumber(String s) {
		jNumber.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLeague.clear();
		jTeam.clear();
		jPerson.clear();
		jNumber.setText("");
	}
	
	public void focus() {
		jLeague.requestFocus();
	}
	
	public void itemStateChanged(ItemEvent e) {
		int leagueId = SwingUtils.getValue(jLeague);
		SwingUtils.fillPicklist(jTeam, JMainFrame.getPicklists().get(Team.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
		SwingUtils.fillPicklist(jPerson, JMainFrame.getPicklists().get(Athlete.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
	}
	
}