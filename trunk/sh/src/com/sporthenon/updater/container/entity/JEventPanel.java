package com.sporthenon.updater.container.entity;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sporthenon.db.entity.Type;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.utils.SwingUtils;


public class JEventPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	public JEntityPicklist jType;
	public JTextField jWebsite;
	public JTextArea jComment;
	public JTextField jIndex;
	public JTextField jUrlWiki;
	public JCheckBox jInactive;
	
	public JEventPanel() {
		super(8);
		initialize();
	}

	protected void initialize() {
        //Name
        JLabel lLabel = new JLabel(" Name:");
        lLabel.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lLabel);
        jLabel = new JTextField();
        jLabel.setPreferredSize(TEXT_SIZE);
        gridPanel.add(jLabel);
        
        //Type
        JLabel lType = new JLabel(" Type:");
        lType.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lType);
        jType = new JEntityPicklist(this, Type.alias);
        gridPanel.add(jType);
        
        //Website
        JLabel lWebsite = new JLabel(" Website:");
        lWebsite.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lWebsite);
        jWebsite = new JTextField();
        gridPanel.add(jWebsite);
        
        //Comment
        JLabel lComment = new JLabel(" Comment:");
        lComment.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lComment);
        jComment = new JTextArea();
		jComment.setFont(SwingUtils.getDefaultFont());
		JScrollPane jCommentPane = new JScrollPane(jComment);
		jCommentPane.setPreferredSize(new Dimension(0, 21));
        gridPanel.add(jCommentPane);
        
        //Index
        JLabel lIndex = new JLabel(" Index:");
        lIndex.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lIndex);
        jIndex = new JTextField();
        gridPanel.add(jIndex);
        
		//URL (WIKI)
		JLabel lUrlWiki = new JLabel(" URL (Wikipedia):");
		lUrlWiki.setHorizontalAlignment(LABEL_ALIGNMENT);
		gridPanel.add(lUrlWiki);
		jUrlWiki = new JTextField();
		jUrlWiki.setPreferredSize(TEXT_SIZE);
		gridPanel.add(jUrlWiki);
        
        //Inactive
        JLabel lInactive = new JLabel(" Inactive:");
        lInactive.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lInactive);
        jInactive = new JCheckBox();
        gridPanel.add(jInactive);
	}

	public JTextField getLabel() {
		return jLabel;
	}

	public JEntityPicklist getType() {
		return jType;
	}
	
	public JTextField getWebsite() {
		return jWebsite;
	}
	
	public JTextArea getComment() {
		return jComment;
	}
	
	public JTextField getIndex() {
		return jIndex;
	}
	
	public JTextField getUrlWiki() {
		return jUrlWiki;
	}
	
	public JCheckBox getInactive() {
		return jInactive;
	}
	
	public void setLabel(String s) {
		jLabel.setText(s);
	}

	public void setType(Integer id) {
		SwingUtils.selectValue(jType, id);
	}
	
	public void setWebsite(String s) {
		jWebsite.setText(s);
	}

	public void setComment(String s) {
		jComment.setText(s);
	}
	
	public void setIndex(String s) {
		jIndex.setText(s);
	}
	
	public void setUrlWiki(String s) {
		jUrlWiki.setText(s);
	}
	
	public void setInactive(Boolean b) {
		jInactive.setSelected(b);
	}
	
	public void clear() {
		jId.setText("");
		jLabel.setText("");
		jComment.setText("");
		jType.clear();
		jIndex.setText("");
		jUrlWiki.setText("");
		jInactive.setSelected(false);
	}
	
	public void focus() {
		jLabel.requestFocus();
	}
	
}
