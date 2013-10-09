package com.sporthenon.explorer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class JBarPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public JBarPanel() {
		super();
		initialize();
	}

	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setHgap(2);
		flowLayout.setVgap(2);
		this.setLayout(flowLayout);
		this.setSize(626, 46);
		this.setPreferredSize(this.getSize());
		
		JBarButton jConnectBtn = new JBarButton("Connect", "common/connect.png");
		JBarToggleButton jResultsButton = new JBarToggleButton("Results", "explorer/results.png", JSelectPanel.RESULTS_PANEL);
		//JBarToggleButton jRecordsButton = new JBarToggleButton("Records", "/records.png", JSelectPanel.RECORDS_PANEL);
		JBarToggleButton jOlympicsButton = new JBarToggleButton("Olympics", "explorer/olympics.png", JSelectPanel.OLYMPICS_PANEL);
		JBarToggleButton jUSLeaguesButton = new JBarToggleButton("US Leagues", "explorer/usflag.png", JSelectPanel.USLEAGUES_PANEL);
		JBarToggleButton jSearchButton = new JBarToggleButton("Search", "explorer/search.png", JSelectPanel.SEARCH_PANEL);
		JBarButton jStatButton = new JBarButton("Statistics", "explorer/chart_pie.png");
		JBarButton jOptionsBtn = new JBarButton("Options", "common/options.png");
		JBarButton jAboutBtn = new JBarButton("About", "explorer/about.png");
		jStatButton.setEnabled(false);
		
		this.add(jConnectBtn, null);
		this.add(getSeparator(), null);
		this.add(jResultsButton, null);
		//this.add(jRecordsButton, null);
		this.add(jOlympicsButton, null);
		this.add(jUSLeaguesButton, null);
		this.add(jSearchButton, null);
		this.add(getSeparator(), null);
		this.add(jStatButton, null);
		this.add(getSeparator(), null);
		this.add(jOptionsBtn, null);
		this.add(getSeparator(), null);
		this.add(jAboutBtn, null);
		setButtonLayout();
	}
	
	private JSeparator getSeparator() {
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		separator.setPreferredSize(new Dimension(1, this.getSize().height));
		return separator;
	}
	
	private void setButtonLayout() {
		ButtonGroup btnGroup = new ButtonGroup();
		for (int i = 0 ; i < this.getComponentCount() ; i++) {
			if (this.getComponent(i) instanceof AbstractButton) {
				AbstractButton btn = (AbstractButton) this.getComponent(i);
				btn.setSize(new Dimension(62, 43));
				btn.setPreferredSize(btn.getSize());
				btn.setHorizontalAlignment(SwingConstants.CENTER);
				btn.setHorizontalTextPosition(SwingConstants.CENTER);
				btn.setVerticalTextPosition(SwingConstants.BOTTOM);
				btn.setMargin(new Insets(2, 0, 0, 0));
				btn.setIconTextGap(3);
				btnGroup.add(btn);
			}
		}
	}

}