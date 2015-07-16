package com.sporthenon.admin.container.entity;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JCustomTextField;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.component.JLinkTextField;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.utils.SwingUtils;


public class JComplexPanel extends JAbstractEntityPanel {
	
	private static final long serialVersionUID = 1L;
	
	public JCustomTextField jLabel;
	public JTextField jLabelFR;
	public JEntityPicklist jCity;
	public JLabel lLink;
	public JLinkTextField jLink;
	public JTextField jImgURL;
	
	public JComplexPanel() {
		super(6);
		initialize();
	}

	protected void initialize() {
        //Name
        JLabel lLabel = new JLabel(" Name:");
        lLabel.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLabel);
        jLabel = new JCustomTextField();
        jLabel.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jLabel);
        
        //Name (FR)
        JLabel lLabelFR = new JLabel(" Name (FR):");
        lLabelFR.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLabelFR);
        jLabelFR = new JTextField();
        jLabelFR.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jLabelFR);

        //City
        JLabel lCity = new JLabel(" City:");
        lCity.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lCity);
        jCity = new JEntityPicklist(this, City.alias);
        gridPanel.add(jCity);
        
		//Image URL
		JLabel lImgURL = new JLabel(" Image URL:");
		lImgURL.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lImgURL);
		jImgURL = new JTextField();
		jImgURL.setPreferredSize(TEXT_SIZE);
		gridPanel.add(jImgURL);
        
		//Link
		lLink = new JLabel(" Linked to:");
		lLink.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lLink);
		jLink = new JLinkTextField(this, Complex.alias);
		jLink.setPreferredSize(TEXT_SIZE);
		gridPanel.add(jLink);
	}

	public JCustomTextField getLabel() {
		return jLabel;
	}
	
	public JTextField getLabelFR() {
		return jLabelFR;
	}

	public JEntityPicklist getCity() {
		return jCity;
	}
	
	public JTextField getImgURL() {
		return jImgURL;
	}
	
	public JLinkTextField getLink() {
		return jLink;
	}
	
	public void setLabel(String s) {
		jLabel.setText(s);
	}
	
	public void setLabelFR(String s) {
		jLabelFR.setText(s);
	}

	public void setCity(Integer id) {
		SwingUtils.selectValue(jCity, id);
	}
	
	public void setImgURL(String s) {
		jImgURL.setText(s);
	}
	
	public void setLink(String s) {
		jLink.setText(s);
	}
	
	public void setLinkLabel(String s) {
		lLink.setText(s);
	}
	
	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jLabelFR.setText("");
		jCity.clear();
		jImgURL.setText("");
		jLink.setText("");
	}
	
	public void focus() {
		jLabel.focus();
	}

}
