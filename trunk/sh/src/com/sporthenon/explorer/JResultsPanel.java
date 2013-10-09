package com.sporthenon.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTree;

public class JResultsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel jViewPanel = null;
	private JPanel jPicklistPanel = null;
	private JPanel jTreePanel = null;
	
	public JResultsPanel() {
		super();
		initialize();
	}

	private void initialize() {
		this.setLayout(new BorderLayout());
        this.setSize(new Dimension(209, 263));
        this.setBorder(BorderFactory.createEtchedBorder(1));
        this.add(getJViewPanel(), BorderLayout.NORTH);
        this.add(getJPicklistPanel(), BorderLayout.CENTER);
        this.add(getJTreePanel(), BorderLayout.CENTER);
	}

	private JPanel getJViewPanel() {
		if (jViewPanel == null) {
			JLabel jLabel = new JLabel();
			jLabel.setText("View:");
			jViewPanel = new JPanel();
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			flowLayout.setHgap(0);
			flowLayout.setVgap(2);
			jViewPanel.setLayout(flowLayout);
			jViewPanel.setPreferredSize(new Dimension(0, 24));
			jViewPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
			jViewPanel.add(jLabel, null);

			JRadioButton jPicklistBtn = new JRadioButton("Picklist");
			JRadioButton jTreeBtn = new JRadioButton("Tree");
			jPicklistBtn.setFocusable(false);
			jTreeBtn.setFocusable(false);
			ButtonGroup group = new ButtonGroup();
			group.add(jPicklistBtn);
			group.add(jTreeBtn);
			jPicklistBtn.setSelected(true);
			jViewPanel.add(jPicklistBtn, null);
			jViewPanel.add(jTreeBtn, null);
		}
		return jViewPanel;
	}

	private JPanel getJTreePanel() {
		if (jTreePanel == null) {
			jTreePanel = new JPanel();
			JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
			separator.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			separator.setPreferredSize(new Dimension(150, 1));
			jTreePanel.setLayout(new BorderLayout());
			jTreePanel.add(separator, BorderLayout.NORTH);
			jTreePanel.add(new JTree(), BorderLayout.CENTER);
			jTreePanel.setVisible(false);
		}
		return jTreePanel;
	}
	
	private JPanel getJPicklistPanel() {
		if (jPicklistPanel == null) {
			jPicklistPanel = new JPanel();
			JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
			separator.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			separator.setPreferredSize(new Dimension(150, 1));
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEADING);
			flowLayout.setHgap(2);
			flowLayout.setVgap(2);
			jPicklistPanel.setLayout(flowLayout);
			jPicklistPanel.add(separator, BorderLayout.NORTH);
		}
		return jPicklistPanel;
	}

}