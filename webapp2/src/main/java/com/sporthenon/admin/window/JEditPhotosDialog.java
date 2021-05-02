package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.container.JTopPanel;

public class JEditPhotosDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	JEditResultDialog parent = null;
	private JTextField jFile;
	
	public JEditPhotosDialog(JEditResultDialog owner) {
		super(owner);
		parent = (JEditResultDialog) this.getOwner();
		initialize();
	}

	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(650, 400));
		this.setSize(this.getPreferredSize());
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setContentPane(jContentPane);

		JDialogButtonBar jButtonBar = new JDialogButtonBar(this);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(new BorderLayout());
		
		jContentPane.add(getUploadPanel(), BorderLayout.NORTH);
		jContentPane.add(getPhotosPanel(), BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}

	private JPanel getUploadPanel() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.setBorder(BorderFactory.createTitledBorder(null, "Upload Photo", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		p.add(new JLabel("File:"));
		jFile = new JTextField(50);
		p.add(jFile);
		JCustomButton btn = new JCustomButton(null, "folderimg.png", null);
		btn.setToolTipText("Browse");
		btn.setActionCommand("browse");
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.addActionListener(this);
		p.add(btn);
		return p;
	}

	private JPanel getPhotosPanel() {
		JPanel p = new JPanel(new GridLayout(1, 1, 5, 5));
		p.setBorder(BorderFactory.createTitledBorder(null, "Current Photos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		return p;
	}
	
	public void open() {
		this.setTitle("Edit Photos");
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
		}
		this.setVisible(!e.getActionCommand().matches("ok|cancel"));
	}

}