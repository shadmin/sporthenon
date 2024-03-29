package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.container.entity.JAbstractEntityPanel;
import com.sporthenon.admin.container.entity.JAthletePanel;
import com.sporthenon.admin.container.entity.JTeamPanel;
import com.sporthenon.admin.container.tab.JResultsPanel;
import com.sporthenon.db.PicklistItem;
import com.sporthenon.utils.SwingUtils;
import com.sporthenon.utils.res.ResourceUtils;


public class JEditEntityDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Container jContainer = null;
	private JDialogButtonBar jButtonBar = null;
	private String alias = null;
	private JEntityPicklist picklist = null;
	private boolean fromDialog = false;
	
	public JEditEntityDialog(JFrame owner) {
		super(owner);
		initialize();
	}
	
	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(400, 400));
		this.setModal(true);
		this.setResizable(true);
		this.setContentPane(jContentPane);
		
		BorderLayout layout = new BorderLayout();
		layout.setVgap(5);
		jButtonBar = new JDialogButtonBar(this);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(layout);
		JScrollPane sp = new JScrollPane(getMainPanel());
		sp.setBorder(BorderFactory.createEmptyBorder());
		jContentPane.add(sp, BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}
	
	private JPanel getMainPanel() {
		jContainer = new Container();
		jContainer.setLayout(new CardLayout());

		JPanel jMainPanel = new JPanel(new BorderLayout());
		jMainPanel.setBorder(BorderFactory.createTitledBorder(null, "Entity Info", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jMainPanel.add(jContainer, BorderLayout.CENTER);
		return jMainPanel;
	}
	
	public void open(String alias, JEntityPicklist picklist, boolean fromDialog) {
		Map<String, JAbstractEntityPanel> panels = JMainFrame.getEntityPanels();
		JAbstractEntityPanel p = panels.get(alias);
		for (String key : panels.keySet()) {
			jContainer.remove(panels.get(key));
		}
		jContainer.add(p, alias);
		this.alias = alias;
		this.picklist = picklist;
		this.fromDialog = fromDialog;
		this.setTitle("Add " + ResourceUtils.getText("entity." + alias + ".1", ResourceUtils.LGDEFAULT));
		((CardLayout) jContainer.getLayout()).show(jContainer, alias);
		this.pack();
		p.clear();
		p.focus();
		if (p instanceof JAthletePanel) {
			SwingUtils.selectValue(((JAthletePanel)p).getSport(), JResultsPanel.getIdSport());
		}
		else if (p instanceof JTeamPanel) {
			SwingUtils.selectValue(((JTeamPanel)p).getSport(), JResultsPanel.getIdSport());
		}
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			try {
				PicklistItem plb = JMainFrame.saveEntity(alias, null);
				if (fromDialog && plb.getParam() != null) {
					SwingUtils.insertValue(picklist, plb);
					picklist.addItem(plb);	
				}
				picklist.setValue(plb.getValue());
			}
			catch (Exception e_) {
				JOptionPane.showMessageDialog(this, e_.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		this.setVisible(false);
	}

}