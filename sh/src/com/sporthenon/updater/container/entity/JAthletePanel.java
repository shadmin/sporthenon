package com.sporthenon.updater.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.Team;
import com.sporthenon.updater.component.JCustomTextField;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.utils.SwingUtils;

public class JAthletePanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;

	public JCustomTextField jLastName;
	public JCustomTextField jFirstName;
	public JEntityPicklist jSport;
	public JEntityPicklist jTeam;
	public JEntityPicklist jCountry;
	public JTextField jLink;

	public JAthletePanel() {
		super(8);
		initialize();
	}

	protected void initialize() {
		//Last Name
		JLabel lLastName = new JLabel(" Last Name:");
		lLastName.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lLastName);
		jLastName = new JCustomTextField();
		jLastName.setPreferredSize(TEXT_SIZE);
		gridPanel.add(jLastName);

		//Last Name
		JLabel lFirstName = new JLabel(" First Name:");
		lFirstName.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lFirstName);
		jFirstName = new JCustomTextField();
		jFirstName.setPreferredSize(TEXT_SIZE);
		gridPanel.add(jFirstName);

		//Sport
		JLabel lSport = new JLabel(" Sport:");
		lSport.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lSport);
		jSport = new JEntityPicklist(this, Sport.alias);
		gridPanel.add(jSport);

		//Team
		JLabel lTeam = new JLabel(" Team:");
		lTeam.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lTeam);
		jTeam = new JEntityPicklist(this, Team.alias);
		gridPanel.add(jTeam);

		//Country
		JLabel lCountry = new JLabel(" Country:");
		lCountry.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lCountry);
		jCountry = new JEntityPicklist(this, Country.alias);
		gridPanel.add(jCountry);

		//Link
		JLabel lLink = new JLabel(" Link:");
		lLink.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lLink);
		jLink = new JTextField();
		jLink.setPreferredSize(TEXT_SIZE);
		gridPanel.add(jLink);
	}

	public JCustomTextField getLastName() {
		return jLastName;
	}

	public JCustomTextField getFirstName() {
		return jFirstName;
	}

	public JEntityPicklist getSport() {
		return jSport;
	}

	public JEntityPicklist getTeam() {
		return jTeam;
	}

	public JEntityPicklist getCountry() {
		return jCountry;
	}

	public JTextField getLink() {
		return jLink;
	}

	public void setLastName(String s) {
		jLastName.setText(s);
	}

	public void setFirstName(String s) {
		jFirstName.setText(s);
	}

	public void setSport(Integer id) {
		SwingUtils.selectValue(jSport, id);
	}

	public void setTeam(Integer id) {
		SwingUtils.selectValue(jTeam, id);
	}

	public void setCountry(Integer id) {
		SwingUtils.selectValue(jCountry, id);
	}

	public void setLink(String s) {
		jLink.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLastName.setText("");
		jFirstName.setText("");
		jLink.setText("");
		jSport.clear();
		jCountry.clear();
		jTeam.clear();
	}

	public void focus() {
		jLastName.focus();
	}

}