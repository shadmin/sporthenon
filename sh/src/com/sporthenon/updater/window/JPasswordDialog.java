package com.sporthenon.updater.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.sporthenon.updater.component.JDialogButtonBar;

public class JPasswordDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JLabel jLabel;
	private JPasswordField jPassword;
	
	public JPasswordDialog(JFrame owner) {
		super(owner);
		initialize();
	}
	
	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(280, 90));
		this.setSize(this.getPreferredSize());
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Password");
		this.setContentPane(jContentPane);
		
		BorderLayout layout = new BorderLayout();
		layout.setVgap(5);
		JDialogButtonBar jButtonBar = new JDialogButtonBar(this);
		jButtonBar.getCancel().setVisible(false);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 5));
		jContentPane.setLayout(layout);
		jLabel = new JLabel();
		jContentPane.add(jLabel, BorderLayout.WEST);
		jPassword = new JPasswordField();
		jPassword.addActionListener(this);
		jContentPane.add(jPassword, BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}
	
	public void open() {
		jLabel.setText("Password for " + JMainFrame.getOptionsDialog().getLogin().getText() + ": ");
		jPassword.setText("");
		this.setVisible(true);
		jPassword.requestFocus();
	}

	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	public JPasswordField getPassword() {
		return jPassword;
	}

}