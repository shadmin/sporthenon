package com.sporthenon.explorer;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JRecordsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public JRecordsPanel() {
		super();
		initialize();
	}

	private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(new Dimension(121, 154));
        this.add(new JLabel("records"));
	}

}