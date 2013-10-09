package com.sporthenon.explorer;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JSearchPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public JSearchPanel() {
		super();
		initialize();
	}

	private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(new Dimension(121, 154));
        this.add(new JLabel("search"));
	}

}