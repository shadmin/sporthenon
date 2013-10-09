package com.sporthenon.updater.component;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sporthenon.utils.res.ResourceUtils;

public class JConnectionStatus extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel jLabel = null;

	public JConnectionStatus() {
		super();
		jLabel = new JLabel("Not connected");
		jLabel.setIconTextGap(2);
		jLabel.setHorizontalAlignment(JLabel.CENTER);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setPreferredSize(new Dimension(160, 16));
		this.add(jLabel, BorderLayout.CENTER);
	}

	public void set(short index, String db) {
		jLabel.setText(index == 2 ? "Connected (" + db + ")" : (index == 1 ? "Connection in progress..." : "Not connected"));
		jLabel.setIcon(ResourceUtils.getIcon(index == 2 ? "common/bullet-green.png" : (index == 1 ? "common/bullet-orange.png" : "common/bullet-red.png")));
	}
	
}