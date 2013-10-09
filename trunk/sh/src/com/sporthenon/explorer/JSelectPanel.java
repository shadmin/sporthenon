package com.sporthenon.explorer;

import java.awt.CardLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class JSelectPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public final static String RESULTS_PANEL = "results";
	public final static String RECORDS_PANEL = "records"; 
	public final static String OLYMPICS_PANEL = "olympics";
	public final static String USLEAGUES_PANEL = "usleagues";
	public final static String SEARCH_PANEL = "search";
	
	private Container parent = null;
	private JResultsPanel jResultsPanel = null;
	private JRecordsPanel jRecordsPanel = null;
	private JOlympicsPanel jOlympicsPanel = null;
	private JUSLeaguesPanel jUSLeaguesPanel = null;
	private JSearchPanel jSearchPanel = null;
	
	public JSelectPanel() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 0, 0));
		parent = new Container();
		parent.setLayout(new CardLayout());
        jResultsPanel = new JResultsPanel();
        jRecordsPanel = new JRecordsPanel();
        jOlympicsPanel = new JOlympicsPanel();
        jUSLeaguesPanel = new JUSLeaguesPanel();
        jSearchPanel = new JSearchPanel();
        parent.add(jResultsPanel, RESULTS_PANEL);
        parent.add(jRecordsPanel, RECORDS_PANEL);
        parent.add(jOlympicsPanel, OLYMPICS_PANEL);
        parent.add(jUSLeaguesPanel, USLEAGUES_PANEL);
        parent.add(jSearchPanel, SEARCH_PANEL);
        this.add(parent, null);
	}
	
	public void changePanel(String name) {
		((CardLayout) parent.getLayout()).show(parent, name);
	}

}