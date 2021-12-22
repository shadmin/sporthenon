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
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.container.tab.JResultsPanel;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.utils.SwingUtils;

public class JEditEventDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JTextField jCurrentSport = null;
	private JTextField jCurrentCategory1 = null;
	private JTextField jCurrentCategory2 = null;
	private JTextField jCurrentCategory3 = null;
	private JTextField jCurrentCategory4 = null;
	private JEntityPicklist jSport = null;
	private JEntityPicklist jCategory1 = null;
	private JEntityPicklist jCategory2 = null;
	private JEntityPicklist jCategory3 = null;
	private JEntityPicklist jCategory4 = null;
	private JPanel jCurrentPanel;
	private TitledBorder panelTitle;
	private JResultsPanel parent;
	private Integer idSport;
	private Integer idChampionship;
	private Integer idEvent;
	private Integer idSubevent;
	private Integer idSubevent2;
	public static final short ADD = 1;
	public static final short MOVE = 2;
	private short type;
	
	public JEditEventDialog(JFrame owner) {
		super(owner);
		initialize();
	}
	
	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setContentPane(jContentPane);
		
		JDialogButtonBar jButtonBar = new JDialogButtonBar(this);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(new BorderLayout());
		jContentPane.add(getCurrentPanel(), BorderLayout.NORTH);
		jContentPane.add(getEditPanel(), BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}
	
	private JPanel getCurrentPanel() {
		jCurrentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jCurrentPanel.setBorder(BorderFactory.createTitledBorder(null, "From", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jCurrentPanel.setPreferredSize(new Dimension(0, 160));
		
		JLabel lSport = new JLabel(" Sport:");
		lSport.setPreferredSize(new Dimension(80, 21));
		jCurrentSport = new JTextField();
		jCurrentSport.setPreferredSize(new Dimension(350, 21));
		jCurrentSport.setEnabled(false);
		jCurrentPanel.add(lSport, null);
		jCurrentPanel.add(jCurrentSport, null);
		
		JLabel lCategory1 = new JLabel(" Championship:");
		lCategory1.setPreferredSize(new Dimension(80, 21));
		jCurrentCategory1 = new JTextField();
		jCurrentCategory1.setPreferredSize(new Dimension(350, 21));
		jCurrentCategory1.setEnabled(false);
		jCurrentPanel.add(lCategory1, null);
		jCurrentPanel.add(jCurrentCategory1, null);
		
		JLabel lCategory2 = new JLabel(" Event #1:");
		lCategory2.setPreferredSize(new Dimension(80, 21));
		jCurrentCategory2 = new JTextField();
		jCurrentCategory2.setPreferredSize(new Dimension(350, 21));
		jCurrentCategory2.setEnabled(false);
		jCurrentPanel.add(lCategory2, null);
		jCurrentPanel.add(jCurrentCategory2, null);
		
		JLabel lCategory3 = new JLabel(" Event #2:");
		lCategory3.setPreferredSize(new Dimension(80, 21));
		jCurrentCategory3 = new JTextField();
		jCurrentCategory3.setPreferredSize(new Dimension(350, 21));
		jCurrentCategory3.setEnabled(false);
		jCurrentPanel.add(lCategory3, null);
		jCurrentPanel.add(jCurrentCategory3, null);
		
		JLabel lCategory4 = new JLabel(" Event #3:");
		lCategory4.setPreferredSize(new Dimension(80, 21));
		jCurrentCategory4 = new JTextField();
		jCurrentCategory4.setPreferredSize(new Dimension(350, 21));
		jCurrentCategory4.setEnabled(false);
		jCurrentPanel.add(lCategory4, null);
		jCurrentPanel.add(jCurrentCategory4, null);
		
		return jCurrentPanel;
	}
	
	private JPanel getEditPanel() {
		JPanel jEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelTitle = BorderFactory.createTitledBorder(null, "Event Info", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black);
		jEditPanel.setBorder(panelTitle);
		
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
		if (type == ADD) {
			jCurrentPanel.setVisible(false);
			this.setSize(new Dimension(480, 240));
			panelTitle.setTitle("Event Info");
			this.setTitle("Add Event");
		}
		else {
			jCurrentPanel.setVisible(true);
			this.setSize(new Dimension(480, 390));
			panelTitle.setTitle("To");
			this.setTitle("Move Event");
			
			idSport = SwingUtils.getValue(jSport);
			idChampionship = SwingUtils.getValue(jCategory1);
			idEvent = SwingUtils.getValue(jCategory2);
			idSubevent = SwingUtils.getValue(jCategory3);
			idSubevent2 = SwingUtils.getValue(jCategory4);
			
			jCurrentSport.setText(SwingUtils.getText(jSport));
			jCurrentCategory1.setText(SwingUtils.getText(jCategory1));
			jCurrentCategory2.setText(SwingUtils.getText(jCategory2));
			jCurrentCategory3.setText(SwingUtils.getText(jCategory3));
			jCurrentCategory4.setText(SwingUtils.getText(jCategory4));
		}
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.matches("\\D\\D\\-(add|find)")) {
			SwingUtils.openAddFindDialog(e, null, true);
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
			if (type == MOVE) {
				if (idSport.equals(SwingUtils.getValue(jSport))
					&& idChampionship.equals(SwingUtils.getValue(jCategory1))
					&& idEvent.equals(SwingUtils.getValue(jCategory2))
					&& ((idSubevent != null && idSubevent.equals(SwingUtils.getValue(jCategory3))) || (idSubevent == null && SwingUtils.getValue(jCategory3) == null))
					&& ((idSubevent2 != null && idSubevent2.equals(SwingUtils.getValue(jCategory4))) || (idSubevent2 == null && SwingUtils.getValue(jCategory4) == null))) {
					JOptionPane.showMessageDialog(this, "The two paths can not be identical.", "Error", JOptionPane.ERROR_MESSAGE);
					return;	
				}
				String type1 = (idSubevent2 != null ? jCurrentCategory4.getText() : (idSubevent != null ? jCurrentCategory3.getText() : jCurrentCategory2.getText())).replaceAll(".*\\[|\\]", "");
				String type2 = (SwingUtils.getValue(jCategory4) != null ? SwingUtils.getText(jCategory4) : (SwingUtils.getValue(jCategory3) != null ? SwingUtils.getText(jCategory3) : SwingUtils.getText(jCategory2))).replaceAll(".*\\[|\\]", "");
				if (!type1.equals(type2)) {
					JOptionPane.showMessageDialog(this, "The two event types must be the same (currently: " + type1 + " / " + type2 + ")", "Error", JOptionPane.ERROR_MESSAGE);
					return;	
				}
			}
			if (type == ADD) {
				parent.addEventCallback();	
			}
			else {
				parent.moveEventCallback();
			}
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

	public void setType(short type) {
		this.type = type;
	}
	
}