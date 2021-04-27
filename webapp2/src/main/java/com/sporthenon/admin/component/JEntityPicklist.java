package com.sporthenon.admin.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.sporthenon.db.PicklistItem;


public class JEntityPicklist extends JPanel implements ItemListener {

	private static final long serialVersionUID = 1L;
	
	private JComboBox<PicklistItem> jPicklist = null;
	private JPanel jButtonPanel = null;
	private JCustomButton jAddButton = null;
	private JCustomButton jFindButton = null;
	private JCustomButton jOptionalButton = null;
	private ActionListener listener = null;
	private String alias = null;

	public JEntityPicklist(ActionListener listener, String alias) {
		super();
		this.listener = listener;
		this.alias = alias;
		initialize();
	}

	private void initialize() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(0, 21));
        
        jPicklist = new JComboBox<>();
        jPicklist.setMaximumRowCount(20);
        jPicklist.addItemListener(this);
        this.add(getButtonPanel(), BorderLayout.EAST);
        this.add(jPicklist, BorderLayout.CENTER);
	}

	private JPanel getButtonPanel() {
		jButtonPanel = new JPanel();
		jButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		jButtonPanel.setPreferredSize(new Dimension(46, 0));
		
		jAddButton = new JCustomButton(null, "add.png", null);
		jAddButton.setMargin(new Insets(0, 0, 0, 0));
		jAddButton.setToolTipText("Add");
		jAddButton.setFocusable(false);
		jAddButton.addActionListener(listener);
		jAddButton.setActionCommand(alias + "-add");
		
		jFindButton = new JCustomButton(null, "find.png", null);
		jFindButton.setMargin(new Insets(0, 0, 0, 0));
		jFindButton.setToolTipText("Find");
		jFindButton.setFocusable(false);
		jFindButton.addActionListener(listener);
		jFindButton.setActionCommand(alias + "-find");
		
		jOptionalButton = new JCustomButton(null, null, null);
		jOptionalButton.setMargin(new Insets(0, 0, 0, 0));
		jOptionalButton.setFocusable(false);
		jOptionalButton.setVisible(false);
		
		jButtonPanel.add(jAddButton, null);
		jButtonPanel.add(jFindButton, null);
		jButtonPanel.add(jOptionalButton, null);
		
		return jButtonPanel;
	}

	public JComboBox<PicklistItem> getPicklist() {
		return jPicklist;
	}

	public JPanel getButtonPanel2() {
		return jButtonPanel;
	}

	public JCustomButton getAddButton() {
		return jAddButton;
	}

	public JCustomButton getFindButton() {
		return jFindButton;
	}

	public JCustomButton getOptionalButton() {
		return jOptionalButton;
	}
	
	public void clear() {
		jPicklist.setSelectedIndex(0);
	}
	
	public void setEnabled(boolean enabled) {
		jPicklist.setEnabled(enabled);
		jAddButton.setEnabled(enabled);
		jFindButton.setEnabled(enabled);
		jOptionalButton.setEnabled(enabled);
	}

	public void itemStateChanged(ItemEvent e) {
		jPicklist.setToolTipText(jPicklist.getSelectedIndex() != -1 ? ((PicklistItem) jPicklist.getSelectedItem()).getText() : "");
	}

}