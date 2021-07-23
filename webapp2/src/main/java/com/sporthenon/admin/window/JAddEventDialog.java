package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.container.tab.JResultsPanel;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.utils.SwingUtils;

public class JAddEventDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JEntityPicklist jSport = null;
	private JEntityPicklist jCategory1 = null;
	private JEntityPicklist jCategory2 = null;
	private JEntityPicklist jCategory3 = null;
	private JEntityPicklist jCategory4 = null;
	private JResultsPanel parent;
	
	public JAddEventDialog(JFrame owner) {
		super(owner);
		initialize();
	}
	
	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(480, 250));
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setContentPane(jContentPane);
		this.setTitle("Add Event");
		
		JDialogButtonBar jButtonBar = new JDialogButtonBar(this);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(new BorderLayout());
		jContentPane.add(getEditPanel(), BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}
	
	private JPanel getEditPanel() {
		JPanel jEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jEditPanel.setBorder(BorderFactory.createTitledBorder(null, "Event Info", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		
		JLabel lSport = new JLabel(" Sport:");
		lSport.setPreferredSize(new Dimension(80, 21));
		jSport = new JEntityPicklist(this, Sport.alias, false);
		jSport.setPreferredSize(new Dimension(350, 21));
		jEditPanel.add(lSport, null);
		jEditPanel.add(jSport, null);
		
		JLabel lCategory1 = new JLabel(" Championship:");
		lCategory1.setPreferredSize(new Dimension(80, 21));
		jCategory1 = new JEntityPicklist(this, Championship.alias, false);
		jCategory1.setPreferredSize(new Dimension(350, 21));
		jEditPanel.add(lCategory1, null);
		jEditPanel.add(jCategory1, null);
		
		JLabel lCategory2 = new JLabel(" Event #1:");
		lCategory2.setPreferredSize(new Dimension(80, 21));
		jCategory2 = new JEntityPicklist(this, Event.alias, false);
		jCategory2.setPreferredSize(new Dimension(350, 21));
		jEditPanel.add(lCategory2, null);
		jEditPanel.add(jCategory2, null);
		
		JLabel lCategory3 = new JLabel(" Event #2:");
		lCategory3.setPreferredSize(new Dimension(80, 21));
		jCategory3 = new JEntityPicklist(this, Event.alias, false);
		jCategory3.setPreferredSize(new Dimension(350, 21));
		jEditPanel.add(lCategory3, null);
		jEditPanel.add(jCategory3, null);
		
		JLabel lCategory4 = new JLabel(" Event #3:");
		lCategory4.setPreferredSize(new Dimension(80, 21));
		jCategory4 = new JEntityPicklist(this, Event.alias, false);
		jCategory4.setPreferredSize(new Dimension(350, 21));
		jEditPanel.add(lCategory4, null);
		jEditPanel.add(jCategory4, null);
		
		return jEditPanel;
	}

	public void open(JResultsPanel parent) {
		this.parent = parent;
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.matches("\\D\\D\\-(add|find)")) {
			SwingUtils.openAddFindDialog(e, null);
			return;
		}
		else if (cmd.equals("ok")) {
			if (SwingUtils.getValue(jSport) == null) {
				JOptionPane.showMessageDialog(this, "Field 'Sport' can not be empty.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if (SwingUtils.getValue(jCategory1) == null) {
				JOptionPane.showMessageDialog(this, "Field 'Championship' can not be empty.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if (SwingUtils.getValue(jCategory2) == null) {
				JOptionPane.showMessageDialog(this, "Field 'Event  #1' can not be empty.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if (SwingUtils.getValue(jCategory3) == null && SwingUtils.getValue(jCategory4) != null) {
				JOptionPane.showMessageDialog(this, "Field 'Event  #2' can not be empty if 'Event  #3' is defined.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			parent.addEventCallback();
		}
		this.setVisible(false);
	}

	public JEntityPicklist getSport() {
		return jSport;
	}

	public JEntityPicklist getCategory1() {
		return jCategory1;
	}

	public JEntityPicklist getCategory2() {
		return jCategory2;
	}

	public JEntityPicklist getCategory3() {
		return jCategory3;
	}
	
	public JEntityPicklist getCategory4() {
		return jCategory4;
	}
	
}