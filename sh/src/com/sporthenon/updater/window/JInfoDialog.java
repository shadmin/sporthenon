package com.sporthenon.updater.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sporthenon.updater.component.JDialogButtonBar;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.res.ResourceUtils;


public class JInfoDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	public JInfoDialog(JFrame owner) {
		super(owner);
		initialize();
	}
	
	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(310, 210));
		this.setSize(this.getPreferredSize());
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Info");
		this.setContentPane(jContentPane);
		
		BorderLayout layout = new BorderLayout();
		layout.setVgap(5);
		JDialogButtonBar jButtonBar = new JDialogButtonBar(this);
		jButtonBar.getCancel().setVisible(false);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2));
		jContentPane.setLayout(layout);
		jContentPane.add(getPanel(), BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}
	
	private JPanel getPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEtchedBorder());
		p.add(new JLabel(ResourceUtils.getIcon("updater/logo.png")), BorderLayout.WEST);
		
		JPanel p_ = new JPanel(new GridLayout(0, 1));
		p_.add(new JLabel());
		JLabel l1 = new JLabel("Sporthenon Update");
		l1.setHorizontalAlignment(JLabel.CENTER);
		l1.setFont(new Font("Verdana", Font.BOLD, 13));
		p_.add(l1);
		JLabel l2 = new JLabel("Version " + ConfigUtils.getProperty("version"));
		l2.setHorizontalAlignment(JLabel.CENTER);
		p_.add(l2);
		JLabel l3 = new JLabel("©2011-13 sporthenon.com");
		l3.setHorizontalAlignment(JLabel.CENTER);
		p_.add(l3);
		p_.add(new JLabel());
		p.add(p_, BorderLayout.CENTER);
		
		return p;
	}
	
	public void open() {
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

}
