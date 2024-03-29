package com.sporthenon.admin.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sporthenon.admin.window.JMainFrame;


public class JCustomTextField extends JPanel implements ActionListener, FocusListener {

	private static final long serialVersionUID = 1L;
	
	private JTextField jField = null;
	private JCustomButton jCharsButton = null;

	public JCustomTextField() {
		super();
		initialize();
	}

	private void initialize() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(0, 21));
        
        jField = new JTextField();
        jField.addFocusListener(this);
        this.add(getJPanel(), BorderLayout.EAST);
        this.add(jField, BorderLayout.CENTER);
	}

	private JPanel getJPanel() {
		JPanel jButtonPanel = new JPanel();
		jButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		jButtonPanel.setPreferredSize(new Dimension(23, 0));
		
		jCharsButton = new JCustomButton(null, "chars.png", null);
		jCharsButton.setMargin(new Insets(0, 0, 0, 0));
		jCharsButton.setToolTipText("Select Character");
		jCharsButton.setFocusable(false);
		jCharsButton.addActionListener(this);
		jButtonPanel.add(jCharsButton, null);
		
		return jButtonPanel;
	}

	public void actionPerformed(ActionEvent e) {
		JMainFrame.getCharsDialog().open(this);
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		((JTextField)e.getSource()).selectAll();
	}

	@Override
	public void focusLost(FocusEvent e) {
	}
	
	public void setText(String s) {
		this.jField.setText(s);
	}
	
	public String getText() {
		return this.jField.getText();
	}
	
	public void focus() {
		this.jField.requestFocus();
	}

}
