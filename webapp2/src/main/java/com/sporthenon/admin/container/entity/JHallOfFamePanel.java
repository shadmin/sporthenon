package com.sporthenon.admin.container.entity;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.window.JMainFrame;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.League;
import com.sporthenon.db.entity.Year;
import com.sporthenon.utils.SwingUtils;

public class JHallOfFamePanel extends JAbstractEntityPanel implements ItemListener {

	private static final long serialVersionUID = 1L;
	
	public JEntityPicklist jLeague;
	public JEntityPicklist jYear;
	public JEntityPicklist jPerson;
	public JTextField jPosition;
	
	public JHallOfFamePanel() {
		super(5);
		initialize();
	}

	protected void initialize() {
        //League
        JLabel lLeague = new JLabel(" League:");
        lLeague.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLeague);
        jLeague = new JEntityPicklist(this, League.alias, false);
        jLeague.getAddButton().setVisible(false);
        jLeague.setPreferredSize(TEXT_SIZE);
        jLeague.getCombobox().addItemListener(this);
        gridPanel.add(jLeague);
        
        //Year
        JLabel lYear = new JLabel(" Year:");
        lYear.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lYear);
        jYear = new JEntityPicklist(this, Year.alias, false);
        gridPanel.add(jYear);

        //Person
        JLabel lPerson = new JLabel(" Person:");
        lPerson.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lPerson);
        jPerson = new JEntityPicklist(this, Athlete.alias, true);
        gridPanel.add(jPerson);
        
        //Position
        JLabel lPosition = new JLabel(" Position:");
        lPosition.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lPosition);
        jPosition = new JTextField();
        gridPanel.add(jPosition);
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
	
	public JTextField getPosition() {
		return jPosition;
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
	
	public void setPosition(String s) {
		jPosition.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLeague.clear();
		jYear.clear();
		jPerson.clear();
		jPosition.setText("");
	}
	
	public void focus() {
		jLeague.requestFocus();
	}
	
	public void itemStateChanged(ItemEvent e) {
		Integer leagueId = SwingUtils.getValue(jLeague);
		if (leagueId != null) {
			SwingUtils.fillPicklist(jPerson, JMainFrame.getPicklists().get(Athlete.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
		}
	}
	
}