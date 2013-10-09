package com.sporthenon.explorer;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.sporthenon.utils.SwingUtils;

public class JActionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public JActionPanel() {
		super();
		initialize();
	}

	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		flowLayout.setHgap(1);
		flowLayout.setVgap(2);
        this.setLayout(flowLayout);
        this.setSize(new Dimension(318, 80));
        
        JBarButton jRunBtn = new JBarButton("Run", "explorer/action_go.gif");
        JBarButton jClearBtn = new JBarButton("Clear", "explorer/action_stop.gif");
        JSeparator jSeparator = new JSeparator(JSeparator.VERTICAL);
        jSeparator.setPreferredSize(new Dimension(2, 0));
        jSeparator.setBorder(BorderFactory.createEmptyBorder());
        jRunBtn.setMargin(new Insets(3, 3, 3, 3));
        jRunBtn.setEnabled(false);
        jRunBtn.setPreferredSize(new Dimension(70, 25));
        jRunBtn.setFont(SwingUtils.getDefaultFont());
        jClearBtn.setMargin(new Insets(3, 3, 3, 3));
        jClearBtn.setEnabled(false);
        jClearBtn.setPreferredSize(new Dimension(70, 25));
        this.add(jRunBtn, null);
        this.add(jSeparator, null);
        this.add(jClearBtn, null);
	}

}