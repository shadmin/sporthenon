package com.sporthenon.admin.container;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.sporthenon.admin.component.JConnectionStatus;
import com.sporthenon.admin.component.JQueryStatus;


public class JBottomPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JConnectionStatus jConnectionStatus = null;
	private JQueryStatus jQueryStatus = null;
	
	public JBottomPanel() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(2, 2));
		this.setPreferredSize(new Dimension(0, 23));
		jConnectionStatus = new JConnectionStatus();
		jQueryStatus = new JQueryStatus();
		jConnectionStatus.set((short)0, null);
		this.add(jConnectionStatus, BorderLayout.WEST);
		this.add(jQueryStatus, BorderLayout.CENTER);
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(80, 0));
		p.setBorder(BorderFactory.createEtchedBorder());
		this.add(p, BorderLayout.EAST);
	}

	public JConnectionStatus getConnectionStatus() {
		return jConnectionStatus;
	}

	public JQueryStatus getQueryStatus() {
		return jQueryStatus;
	}
	
}
