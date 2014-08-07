package com.sporthenon.updater.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.sporthenon.db.PicklistBean;


public class JEntityPicklist extends JPanel implements ItemListener {

	private static final long serialVersionUID = 1L;
	
	private JComboBox jPicklist = null;
	private JCustomButton jAddButton = null;
	private JCustomButton jFindButton = null;
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
        
        jPicklist = new JComboBox();
        jPicklist.setMaximumRowCount(20);
        jPicklist.addItemListener(this);
        this.add(getJPanel(), BorderLayout.EAST);
        this.add(jPicklist, BorderLayout.CENTER);
	}

	private JPanel getJPanel() {
		JPanel jButtonPanel = new JPanel();
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
		jButtonPanel.add(jAddButton, null);
		jButtonPanel.add(jFindButton, null);
		
		return jButtonPanel;
	}

	public JComboBox getPicklist() {
		return jPicklist;
	}

	public JCustomButton getAddButton() {
		return jAddButton;
	}

	public JCustomButton getFindButton() {
		return jFindButton;
	}
	
	public void clear() {
		jPicklist.setSelectedIndex(0);
	}
	
	public void setEnabled(boolean enabled) {
		jPicklist.setEnabled(enabled);
		jAddButton.setEnabled(enabled);
		jFindButton.setEnabled(enabled);
	}

	public void itemStateChanged(ItemEvent e) {
		jPicklist.setToolTipText(jPicklist.getSelectedIndex() != -1 ? ((PicklistBean) jPicklist.getSelectedItem()).getText() : "");
	}

}