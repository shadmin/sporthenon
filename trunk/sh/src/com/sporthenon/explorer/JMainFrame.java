package com.sporthenon.explorer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class JMainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JBarPanel jMainPanel = null;
	private JPanel jStatusPanel = null;
	private JPanel jPanel = null;
	private JCustomTabbedPane jCustomTabbedPane = null;
	private JActionPanel jActionPanel = null;
	private JSelectPanel jSelectPanel = null;
	
	public JMainFrame() {
		super();
		initialize();
		this.setVisible(true);
	}

	private void initialize() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");	
		}
		catch (Exception e) {}
		this.setSize(728, 462);
		this.setMinimumSize(new Dimension(250, 150));
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJMainPanel(), BorderLayout.NORTH);
			jContentPane.add(getJStatusPanel(), BorderLayout.SOUTH);
			jContentPane.add(getJPanel(), BorderLayout.WEST);
			jContentPane.add(getJCustomTabbedPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	private JBarPanel getJMainPanel() {
		if (jMainPanel == null) {
			jMainPanel = new JBarPanel();
			for (int i = 0 ; i < jMainPanel.getComponentCount() ; i++) {
				if (jMainPanel.getComponent(i) instanceof JBarToggleButton) {
					JBarToggleButton btn = (JBarToggleButton) jMainPanel.getComponent(i);
					btn.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							jSelectPanel.changePanel(e.getActionCommand());
						}
					});
				}
			}
		}
		return jMainPanel;
	}

	private JPanel getJStatusPanel() {
		if (jStatusPanel == null) {
			jStatusPanel = new JStatusPanel();
		}
		return jStatusPanel;
	}

	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(0);
			borderLayout.setVgap(0);
			jPanel.setLayout(new BorderLayout());
			jPanel.setPreferredSize(new Dimension(180, 0));
			jPanel.add(getJActionPanel(), BorderLayout.SOUTH);
			jPanel.add(getJSelectPanel(), BorderLayout.CENTER);
		}
		return jPanel;
	}

	private JTabbedPane getJCustomTabbedPane() {
		if (jCustomTabbedPane == null) {
			jCustomTabbedPane = new JCustomTabbedPane();
			jCustomTabbedPane.addTab("Results > Athletics");
			jCustomTabbedPane.addTab("Loading...");
		}
		return jCustomTabbedPane;
	}

	private JActionPanel getJActionPanel() {
		if (jActionPanel == null) {
			jActionPanel = new JActionPanel();
		}
		return jActionPanel;
	}

	private JSelectPanel getJSelectPanel() {
		if (jSelectPanel == null) {
			jSelectPanel = new JSelectPanel();
			CardLayout cardLayout = new CardLayout();
			jSelectPanel.setLayout(cardLayout);
		}
		return jSelectPanel;
	}

}