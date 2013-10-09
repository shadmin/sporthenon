package com.sporthenon.explorer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.sporthenon.utils.res.ResourceUtils;


public class JStatusPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public JStatusPanel() {
		super();
		initialize();
	}

	private void initialize() {
		this.setSize(new Dimension(401, 21));
        this.setPreferredSize(this.getSize());
        FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(2);
		flowLayout.setVgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		this.setLayout(flowLayout);
		this.setBorder(BorderFactory.createEtchedBorder());
		
		JLabel jConnectedLabel = new JLabel();
        jConnectedLabel.setText("Not connected");
        jConnectedLabel.setPreferredSize(new Dimension(100, 16));
		jConnectedLabel.setIcon(ResourceUtils.getIcon("common/bullet-red.png"));
        JLabel jTimeLabel = new JLabel();
        jTimeLabel.setText("3.216 seconds");
        jTimeLabel.setIcon(ResourceUtils.getIcon("explorer/time.png"));
        jTimeLabel.setPreferredSize(new Dimension(110, 14));
		this.add(jConnectedLabel, null);
		this.add(getSeparator(), null);
		this.add(jTimeLabel, null);
		this.add(getSeparator(), null);
	}
	
	private JSeparator getSeparator() {
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		separator.setPreferredSize(new Dimension(1, 16));
		return separator;
	}

}