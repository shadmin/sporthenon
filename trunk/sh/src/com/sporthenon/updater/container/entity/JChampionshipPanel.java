package com.sporthenon.updater.container.entity;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sporthenon.utils.SwingUtils;

public class JChampionshipPanel extends JAbstractEntityPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField jLabel;
	public JTextField jWebsite;
	public JTextArea jComment;
	public JTextField jIndex;
	public JCheckBox jInactive;
	public JTextField jUrlWiki;

	public JChampionshipPanel() {
		super(7);
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
        
        //Website
        JLabel lWebsite = new JLabel(" Website:");
        lWebsite.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lWebsite);
        jWebsite = new JTextField();
        jLabel.setPreferredSize(TEXT_SIZE);
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
        lIndex.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lInactive);
        jInactive = new JCheckBox();
        gridPanel.add(jInactive);
	}

	public JTextField getLabel() {
		return jLabel;
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
		jWebsite.setText("");
		jComment.setText("");
		jIndex.setText("");
		jUrlWiki.setText("");
		jInactive.setSelected(false);
	}
	
	public void focus() {
		jLabel.requestFocus();
	}

}