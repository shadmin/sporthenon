package com.sporthenon.admin.container.entity;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.window.JMainFrame;
import com.sporthenon.db.entity.League;
import com.sporthenon.db.entity.Team;
import com.sporthenon.utils.SwingUtils;

public class JWinLossPanel extends JAbstractEntityPanel implements ItemListener {

private static final long serialVersionUID = 1L;
	
	public JEntityPicklist jLeague;
	public JEntityPicklist jTeam;
	public JComboBox<String> jType = null;
	public JTextField jWin;
	public JTextField jLoss;
	public JTextField jTie;
	public JTextField jOtLoss;
	
	public JWinLossPanel() {
		super(9);
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

        //Type
        JLabel lType = new JLabel(" Type:");
        lType.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lType);
        jType = new JComboBox<>();
        jType.setPreferredSize(new Dimension(0, 21));
        jType.addItem("Regular Season");
        jType.addItem("Playoffs");
        jType.addItem("Playoffs (including Super Bowl)");
        jType.addItem("Super Bowl");
        gridPanel.add(jType);
        
        //Win
        JLabel lWin = new JLabel(" Win:");
        lWin.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lWin);
        jWin = new JTextField();
        gridPanel.add(jWin);
        
        //Loss
        JLabel lLoss = new JLabel(" Loss:");
        lLoss.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLoss);
        jLoss = new JTextField();
        gridPanel.add(jLoss);
        
        //Tie
        JLabel lTie = new JLabel(" Tie:");
        lTie.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lTie);
        jTie = new JTextField();
        gridPanel.add(jTie);
        
        //OT Loss
        JLabel lOtLoss = new JLabel(" Ot Loss:");
        lOtLoss.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lOtLoss);
        jOtLoss = new JTextField();
        gridPanel.add(jOtLoss);
	}
	
	public JEntityPicklist getLeague() {
		return jLeague;
	}

	public JEntityPicklist getTeam() {
		return jTeam;
	}
	
	public JComboBox<String> getType() {
		return jType;
	}
	
	public JTextField getWin() {
		return jWin;
	}
	
	public JTextField getLoss() {
		return jLoss;
	}
	
	public JTextField getTie() {
		return jTie;
	}
	
	public JTextField getOtLoss() {
		return jOtLoss;
	}
	
	public void setLeague(Integer id) {
		SwingUtils.selectValue(jLeague, id);
	}
	
	public void setTeam(Integer id) {
		SwingUtils.selectValue(jTeam, id);
	}
	
	public void setType(String s) {
		jType.setSelectedItem(s);
	}
	
	public void setWin(String s) {
		jWin.setText(s);
	}
	
	public void setLoss(String s) {
		jLoss.setText(s);
	}
	
	public void setTie(String s) {
		jTie.setText(s);
	}
	
	public void setOtLoss(String s) {
		jOtLoss.setText(s);
	}
	
	public void clear() {
		jId.setText("");
		jLeague.clear();
		jTeam.clear();
		jType.setSelectedIndex(0);
		jWin.setText("");
		jLoss.setText("");
		jTie.setText("");
		jOtLoss.setText("");
	}
	
	public void focus() {
		jLeague.requestFocus();
	}
	
	public void itemStateChanged(ItemEvent e) {
		int leagueId = SwingUtils.getValue(jLeague);
		SwingUtils.fillPicklist(jTeam, JMainFrame.getPicklists().get(Team.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
	}
	
}