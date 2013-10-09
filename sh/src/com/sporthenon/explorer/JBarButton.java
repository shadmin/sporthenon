package com.sporthenon.explorer;

import javax.swing.JButton;

import com.sporthenon.utils.res.ResourceUtils;


public class JBarButton extends JButton {

	private static final long serialVersionUID = 1L;

	public JBarButton() {
		super();
		initialize();
	}
	
	public JBarButton(String text, String icon) {
		super();
		initialize();
		this.setText(text);
		this.setIcon(ResourceUtils.getIcon(icon));
	}

	protected void initialize() {
	}

	@Override
	public boolean isFocusPainted() {
		return false;
	}

}