package com.sporthenon.admin.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;


public class JLinkTextField extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextField jField = null;
	private JCustomButton jFindButton = null;
	private ActionListener listener = null;
	private String alias = null;

	public JLinkTextField(ActionListener listener, String alias) {
		super();
		this.listener = listener;
		this.alias = alias;
		initialize();
	}
	private void initialize() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(0, 21));
        
        jField = new JTextField();
        this.add(getJPanel(), BorderLayout.EAST);
        this.add(jField, BorderLayout.CENTER);
	}

	private JPanel getJPanel() {
		JPanel jButtonPanel = new JPanel();
		jButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		jButtonPanel.setPreferredSize(new Dimension(23, 0));
		
		jFindButton = new JCustomButton(null, "find.png", null);
		jFindButton.setMargin(new Insets(0, 0, 0, 0));
		jFindButton.setToolTipText("Find");
		jFindButton.setFocusable(false);
		jFindButton.addActionListener(listener);
		jFindButton.setActionCommand(alias + "-find");
		jButtonPanel.add(jFindButton, null);
		
		return jButtonPanel;
	}

	public String getAlias() {
		return alias;
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