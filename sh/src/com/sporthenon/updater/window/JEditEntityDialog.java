package com.sporthenon.updater.window;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import com.sporthenon.db.PicklistBean;
import com.sporthenon.updater.component.JDialogButtonBar;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.updater.container.entity.JAbstractEntityPanel;
import com.sporthenon.updater.container.entity.JAthletePanel;
import com.sporthenon.updater.container.entity.JTeamPanel;
import com.sporthenon.updater.container.tab.JResultsPanel;
import com.sporthenon.utils.SwingUtils;
import com.sporthenon.utils.res.ResourceUtils;


public class JEditEntityDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Container jContainer = null;
	private JDialogButtonBar jButtonBar = null;
	private String alias = null;
	private JEntityPicklist picklist = null;
	
	public JEditEntityDialog(JFrame owner) {
		super(owner);
		initialize();
	}
	
	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(450, 350));
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

		JPanel jMainPanel = new JPanel();
		jMainPanel.setBorder(BorderFactory.createTitledBorder(null, "Entity Info", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jMainPanel.add(jContainer, null);
		return jMainPanel;
	}
	
	public void open(String alias, JEntityPicklist picklist) {
		HashMap<String, JAbstractEntityPanel> panels = JMainFrame.getEntityPanels();
		JAbstractEntityPanel p = panels.get(alias);
		for (String key : panels.keySet())
			jContainer.remove(panels.get(key));
		jContainer.add(p, alias);
		this.alias = alias;
		this.picklist = picklist;
		this.setTitle("Add " + ResourceUtils.getText("entity." + alias + ".1", "en"));
		((CardLayout) jContainer.getLayout()).show(jContainer, alias);
		this.pack();
		p.clear();
		p.focus();
		if (p instanceof JAthletePanel)
			SwingUtils.selectValue(((JAthletePanel)p).getSport(), JResultsPanel.getIdSport());
		else if (p instanceof JTeamPanel)
			SwingUtils.selectValue(((JTeamPanel)p).getSport(), JResultsPanel.getIdSport());
//((JTeamPanel)p).setInactive(false);
//		int width = p.getPreferredSize().width + 30;
//		int height = p.getPreferredSize().height + 110;
//		this.setSize(new Dimension(width, height));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			try {
				PicklistBean plb = JMainFrame.saveEntity(alias, null);
				SwingUtils.insertValue(picklist, plb);
			}
			catch (Exception e_) {
				JOptionPane.showMessageDialog(this, e_.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		this.setVisible(false);
	}

}