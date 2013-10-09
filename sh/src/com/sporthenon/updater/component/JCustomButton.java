package com.sporthenon.updater.component;

import java.awt.Insets;

import javax.swing.JButton;

import com.sporthenon.utils.res.ResourceUtils;


public class JCustomButton extends JButton {

	private static final long serialVersionUID = 1L;

	public JCustomButton() {
		super();
		initialize();
	}
	
	public JCustomButton(String text, String icon) {
		super();
		initialize();
		this.setText(text);
		if (icon != null)
			this.setIcon(ResourceUtils.getIcon(icon));
		this.setMargin(new Insets(2, 5, 2, 5));
	}

	protected void initialize() {
	}

	@Override
	public boolean isFocusPainted() {
		return false;
	}
	
	public void setIcon(String icon) {
		this.setIcon(ResourceUtils.getIcon(icon));
	}

}