package com.sporthenon.explorer;

import java.awt.Color;

import javax.swing.JToggleButton;

import com.sporthenon.utils.res.ResourceUtils;


public class JBarToggleButton extends JToggleButton {

	private static final long serialVersionUID = 1L;

	public JBarToggleButton() {
		super();
		initialize();
	}
	
	public JBarToggleButton(String text, String icon, String command) {
		super();
		initialize();
		this.setText(text);
		this.setIcon(ResourceUtils.getIcon(icon));
		this.setActionCommand(command);
		//this.setEnabled(false);
	}

	private void initialize() {
        this.addItemListener(new java.awt.event.ItemListener() {
        	public void itemStateChanged(java.awt.event.ItemEvent e) {
        		int st = e.getStateChange();
        		((JBarToggleButton) e.getItem()).setForeground(st == 1 ? new Color(110, 110, 110) : Color.BLACK);
        		((JBarToggleButton) e.getItem()).setBackground(st == 1 ? new Color(240, 240, 240) : new Color(236, 233, 216));
        	}
        });
	}
	
	@Override
	public boolean isFocusPainted() {
		return false;
	}

}