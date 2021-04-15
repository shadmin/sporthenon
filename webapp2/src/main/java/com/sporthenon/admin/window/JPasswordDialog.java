package com.sporthenon.admin.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.sporthenon.admin.component.JCustomButton;

public class JPasswordDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JLabel jLabel;
	private JPasswordField jPassword;
	private JCheckBox jQuickLoading;
	
	public JPasswordDialog(JFrame owner) {
		super(owner);
		initialize();
	}
	
	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(280, 125));
		this.setSize(this.getPreferredSize());
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Password");
		this.setContentPane(jContentPane);
		
		FlowLayout layout = new FlowLayout();
		layout.setVgap(0);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 5));
		jContentPane.setLayout(layout);
		jLabel = new JLabel();
		jContentPane.add(jLabel);
		jLabel.setPreferredSize(new Dimension(240, 20));
		jPassword = new JPasswordField();
		jPassword.addActionListener(this);
		jPassword.setPreferredSize(new Dimension(240, 20));
		jQuickLoading = new JCheckBox("Quick connect");
		jQuickLoading.setPreferredSize(new Dimension(265, 20));
		JCustomButton jOk = new JCustomButton("OK", "ok.png", null);
		jOk.setActionCommand("ok");
		jOk.addActionListener(this);
		jContentPane.add(jPassword);
		//jContentPane.add(jQuickLoading);
		jContentPane.add(jOk);
	}
	
	public void open() {
		jLabel.setText("Password for " + JMainFrame.getOptionsDialog().getLogin().getText() + ": ");
		jPassword.setText("");
		jQuickLoading.setSelected(false);
		this.setVisible(true);
		jPassword.requestFocus();
	}

	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	public JPasswordField getPassword() {
		return jPassword;
	}
	
	public JCheckBox getQuickLoading() {
		return jQuickLoading;
	}

}