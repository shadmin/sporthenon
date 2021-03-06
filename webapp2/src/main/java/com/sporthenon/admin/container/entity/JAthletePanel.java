package com.sporthenon.admin.container.entity;

import javax.swing.JLabel;

import com.sporthenon.admin.component.JCustomTextField;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.component.JLinkTextField;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.Team;
import com.sporthenon.utils.SwingUtils;

public class JAthletePanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;

	public JCustomTextField jLastName;
	public JCustomTextField jFirstName;
	public JEntityPicklist jSport;
	public JEntityPicklist jTeam;
	public JEntityPicklist jCountry;
	public JLabel lLink;
	public JLinkTextField jLink;
	
	public JAthletePanel() {
		super(9);
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

		//First Name
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
		jSport = new JEntityPicklist(this, Sport.alias, false);
		gridPanel.add(jSport);

		//Team
		JLabel lTeam = new JLabel(" Team:");
		lTeam.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lTeam);
		jTeam = new JEntityPicklist(this, Team.alias, true);
		gridPanel.add(jTeam);

		//Country
		JLabel lCountry = new JLabel(" Country:");
		lCountry.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lCountry);
		jCountry = new JEntityPicklist(this, Country.alias, false);
		gridPanel.add(jCountry);
		
		//Link
		lLink = new JLabel(" Linked to:");
		lLink.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lLink);
		jLink = new JLinkTextField(this, Athlete.alias);
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
	
	public JLinkTextField getLink() {
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

	public void setLinkLabel(String s) {
		lLink.setText(s);
	}

	public void clear() {
		jId.setText("");
		jLastName.setText("");
		jFirstName.setText("");
		jSport.clear();
		jCountry.clear();
		jTeam.clear();
		jLink.setText("");
		setLinkLabel(" Linked to:");
	}

	public void focus() {
		jLastName.focus();
	}

}