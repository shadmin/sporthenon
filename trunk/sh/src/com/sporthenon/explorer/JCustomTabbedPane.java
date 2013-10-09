package com.sporthenon.explorer;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

import com.sporthenon.utils.res.ResourceUtils;


public class JCustomTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	public JCustomTabbedPane() {
		Color transparentC = new Color(0, 0, 0, 0);
		this.setBorder(BorderFactory.createLineBorder(transparentC, 2));
	}
	
	public void addTab(String title) {
		super.addTab(title, ResourceUtils.getIcon("explorer/search.png"), new JCustomTab());
	}
	
}
