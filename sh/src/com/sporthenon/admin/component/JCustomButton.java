package com.sporthenon.admin.component;

import java.awt.Insets;

import javax.swing.JButton;

import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;


public class JCustomButton extends JButton {

	private static final long serialVersionUID = 1L;

	public JCustomButton() {
		super();
		initialize();
	}
	
	public JCustomButton(String text, String icon, String tip) {
		super();
		initialize();
		this.setText(text);
		this.setToolTipText(tip);
		if (icon != null)
			this.setIcon(ResourceUtils.getIcon(icon));
		this.setMargin(StringUtils.notEmpty(text) ? new Insets(2, 5, 2, 5) : new Insets(2, 2, 2, 2));
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