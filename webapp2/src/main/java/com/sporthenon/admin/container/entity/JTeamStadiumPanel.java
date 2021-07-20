package com.sporthenon.admin.container.entity;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.window.JMainFrame;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.League;
import com.sporthenon.db.entity.Team;
import com.sporthenon.utils.SwingUtils;

public class JTeamStadiumPanel extends JAbstractEntityPanel implements ItemListener {

	private static final long serialVersionUID = 1L;
	
	public JEntityPicklist jLeague;
	public JEntityPicklist jTeam;
	public JEntityPicklist jComplex;
	public JTextField jDate1;
	public JTextField jDate2;
	public JCheckBox jRenamed;
	
	public JTeamStadiumPanel() {
		super(7);
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
        
        //Team
        JLabel lTeam = new JLabel(" Team:");
        lTeam.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lTeam);
        jTeam = new JEntityPicklist(this, Team.alias, true);
        gridPanel.add(jTeam);

        //Complex
        JLabel lComplex = new JLabel(" Complex:");
        lComplex.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lComplex);
        jComplex = new JEntityPicklist(this, Complex.alias, true);
        gridPanel.add(jComplex);
        
        //Date1
        JLabel lDate1 = new JLabel(" Start Date:");
        lDate1.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lDate1);
        jDate1 = new JTextField();
        jDate1.addFocusListener(this);
        gridPanel.add(jDate1);
        
        //Date2
        JLabel lDate2 = new JLabel(" End Date:");
        lDate2.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lDate2);
        jDate2 = new JTextField();
        jDate2.addFocusListener(this);
        gridPanel.add(jDate2);
        
        //Renamed
        JLabel lRenamed = new JLabel(" Renamed:");
        lRenamed.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lRenamed);
        jRenamed = new JCheckBox();
        gridPanel.add(jRenamed);
	}
	
	public JEntityPicklist getLeague() {
		return jLeague;
	}

	public JEntityPicklist getTeam() {
		return jTeam;
	}
	
	public JEntityPicklist getComplex() {
		return jComplex;
	}
	
	public JTextField getDate1() {
		return jDate1;
	}
	
	public JTextField getDate2() {
		return jDate2;
	}
	
	public JCheckBox getRenamed() {
		return jRenamed;
	}
	
	public void setLeague(Integer id) {
		SwingUtils.selectValue(jLeague, id);
	}
	
	public void setTeam(Integer id) {
		SwingUtils.selectValue(jTeam, id);
	}
	
	public void setComplex(Integer id) {
		SwingUtils.selectValue(jComplex, id);
	}
	
	public void setDate1(String s) {
		jDate1.setText(s);
	}

	public void setDate2(String s) {
		jDate2.setText(s);
	}
	
	public void setRenamed(Boolean b) {
		jRenamed.setSelected(b);
	}
	
	public void clear() {
		jId.setText("");
		jLeague.clear();
		jTeam.clear();
		jComplex.clear();
		jDate1.setText("");
		jDate2.setText("");
		jRenamed.setSelected(false);
	}
	
	public void focus() {
		jLeague.requestFocus();
	}
	
	public void itemStateChanged(ItemEvent e) {
		Integer leagueId = SwingUtils.getValue(jLeague);
		if (leagueId != null) {
			SwingUtils.fillPicklist(jTeam, JMainFrame.getPicklists().get(Team.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
		}
	}
	
}
