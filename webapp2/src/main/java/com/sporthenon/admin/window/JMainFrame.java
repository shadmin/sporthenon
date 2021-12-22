package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;

import com.sporthenon.admin.component.JConnectionStatus;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.component.JQueryStatus;
import com.sporthenon.admin.container.JBottomPanel;
import com.sporthenon.admin.container.JTopPanel;
import com.sporthenon.admin.container.entity.JAbstractEntityPanel;
import com.sporthenon.admin.container.entity.JAthletePanel;
import com.sporthenon.admin.container.entity.JCalendarPanel;
import com.sporthenon.admin.container.entity.JChampionshipPanel;
import com.sporthenon.admin.container.entity.JCityPanel;
import com.sporthenon.admin.container.entity.JComplexPanel;
import com.sporthenon.admin.container.entity.JConfigPanel;
import com.sporthenon.admin.container.entity.JCountryPanel;
import com.sporthenon.admin.container.entity.JEventPanel;
import com.sporthenon.admin.container.entity.JHallOfFamePanel;
import com.sporthenon.admin.container.entity.JOlympicRankingPanel;
import com.sporthenon.admin.container.entity.JOlympicsPanel;
import com.sporthenon.admin.container.entity.JRecordPanel;
import com.sporthenon.admin.container.entity.JRetiredNumberPanel;
import com.sporthenon.admin.container.entity.JRoundTypePanel;
import com.sporthenon.admin.container.entity.JSportPanel;
import com.sporthenon.admin.container.entity.JStatePanel;
import com.sporthenon.admin.container.entity.JTeamPanel;
import com.sporthenon.admin.container.entity.JTeamStadiumPanel;
import com.sporthenon.admin.container.entity.JYearPanel;
import com.sporthenon.admin.container.tab.JDataPanel;
import com.sporthenon.admin.container.tab.JExtLinksPanel;
import com.sporthenon.admin.container.tab.JPicturesPanel;
import com.sporthenon.admin.container.tab.JResultsPanel;
import com.sporthenon.admin.container.tab.JUsersPanel;
import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.PicklistItem;
import com.sporthenon.db.entity.AbstractEntity;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Calendar;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.HallOfFame;
import com.sporthenon.db.entity.League;
import com.sporthenon.db.entity.OlympicRanking;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.RetiredNumber;
import com.sporthenon.db.entity.RoundType;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Config;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;
import com.sporthenon.utils.UpdateUtils;

public class JMainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JMainFrame.class.getName());
	
	private JPanel jContentPane = null;
	private JTopPanel jTopPanel = null;
	private JBottomPanel jBottomPanel = null;
	private JPanel jTabsPanel = null;
	private JResultsPanel jResultsPanel = null;
	private JDataPanel jDataPanel = null;
	private JPicturesPanel jPicturesPanel = null;
	private JExtLinksPanel jExtLinksPanel = null;
	private JUsersPanel jUsersPanel = null;
	private JLabel jConnectInfoLabel = null;
	private static JPasswordDialog jPasswordDialog = null;
	private static JEditResultDialog jResultDialog = null;
	private static JEditEventDialog jEditEventDialog = null;
	private static JFindEntityDialog jFindDialog = null;
	private static JMergeEntityDialog jMergeDialog = null;
	private static JAddMultipleDialog jAddMultipleDialog = null;
	private static JUrlUpdateDialog jUrlUpdateDialog = null;
	private static JCommentDialog jCommentDialog = null;
	private static JCharsDialog jCharsDialog = null;
	private static JImportDialog jImportDialog = null;
	private static JQueryDialog jQueryDialog = null;
	private static JOptionsDialog jOptionsDialog = null;
	private static JInfoDialog jInfoDialog = null;
	private static JEntityPicklist jAllAthletes = null;
	private static JEntityPicklist jAllTeams = null;

	private static Contributor contributor;
	private static Map<String, JAbstractEntityPanel> jEntityPanels = null;
	private static Map<String, List<PicklistItem>> hPicklists = new HashMap<String, List<PicklistItem>>();

	public JMainFrame() {
		super();
		initialize();
		this.setVisible(true);
	}

	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			jEntityPanels = new HashMap<String, JAbstractEntityPanel>();
			jEntityPanels.put(Athlete.alias, new JAthletePanel());
			jEntityPanels.put(Calendar.alias, new JCalendarPanel());
			jEntityPanels.put(Championship.alias, new JChampionshipPanel());
			jEntityPanels.put(City.alias, new JCityPanel());
			jEntityPanels.put(Complex.alias, new JComplexPanel());
			jEntityPanels.put(Country.alias, new JCountryPanel());
			jEntityPanels.put(Event.alias, new JEventPanel());
			jEntityPanels.put(HallOfFame.alias, new JHallOfFamePanel());
			jEntityPanels.put(Olympics.alias, new JOlympicsPanel());
			jEntityPanels.put(OlympicRanking.alias, new JOlympicRankingPanel());
			jEntityPanels.put(Record.alias, new JRecordPanel());
			jEntityPanels.put(RetiredNumber.alias, new JRetiredNumberPanel());
			jEntityPanels.put(RoundType.alias, new JRoundTypePanel());
			jEntityPanels.put(Sport.alias, new JSportPanel());
			jEntityPanels.put(State.alias, new JStatePanel());
			jEntityPanels.put(Team.alias, new JTeamPanel());
			jEntityPanels.put(TeamStadium.alias, new JTeamStadiumPanel());
			jEntityPanels.put(Year.alias, new JYearPanel());
			jEntityPanels.put(Config.alias, new JConfigPanel());
			
			jInfoDialog = new JInfoDialog(this);
			jImportDialog = new JImportDialog(this);
			jQueryDialog = new JQueryDialog(this);
			jOptionsDialog = new JOptionsDialog(this);
			jPasswordDialog = new JPasswordDialog(this);
			
			this.setFont(SwingUtils.getDefaultFont());
			this.setMinimumSize(new Dimension(640, 480));
			this.setSize(new Dimension(1050, 720));
			this.setTitle("Sporthenon Update Tool");
			this.setContentPane(getJContentPane());
			this.setLocationRelativeTo(null);
			List<Image> lIcons = new ArrayList<Image>();
			for (String size : new String[]{"16"}) {
				lIcons.add(Toolkit.getDefaultToolkit().getImage(JMainFrame.class.getResource("/com/sporthenon/utils/res/img/icon" + size + ".png")));
			}
			this.setIconImages(lIcons);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			System.exit(1);
		}
	}

	private void initAll(boolean quickload) throws Exception {
		if (!quickload) {
			initPicklists();
		}
		jResultsPanel.setTree();
		jResultDialog  = new JEditResultDialog(this);
		jEditEventDialog = new JEditEventDialog(this);
		jFindDialog  = new JFindEntityDialog(this);
		jMergeDialog  = new JMergeEntityDialog(this);
		jAddMultipleDialog = new JAddMultipleDialog(this);
		jUrlUpdateDialog = new JUrlUpdateDialog(this);
		jCommentDialog = new JCommentDialog(this);
		jCharsDialog = new JCharsDialog(this);
		if (!quickload) {
			fillPicklists(null);
			jPicturesPanel.getSportList().removeAllItems();
			for (int i = 0 ; i < jEditEventDialog.getSport().getCombobox().getItemCount() ; i++) {
				jPicturesPanel.getSportList().addItem(jEditEventDialog.getSport().getCombobox().getItemAt(i));
			}
		}
	}

	public static void fillPicklists(String alias) {
		if (alias == null || alias.equalsIgnoreCase(Athlete.alias)) {
			SwingUtils.fillPicklist(((JHallOfFamePanel)jEntityPanels.get(HallOfFame.alias)).getPerson(), hPicklists.get(Athlete.alias), null);
			SwingUtils.fillPicklist(((JRetiredNumberPanel)jEntityPanels.get(RetiredNumber.alias)).getPerson(), hPicklists.get(Athlete.alias), null);
			SwingUtils.fillPicklist(jAllAthletes, hPicklists.get(Athlete.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Championship.alias)) {
			SwingUtils.fillPicklist(jEditEventDialog.getCategory1(), hPicklists.get(Championship.alias), null);
			SwingUtils.fillPicklist(jEditEventDialog.getCategory1(), hPicklists.get(Championship.alias), null);
			SwingUtils.fillPicklist(((JRecordPanel)jEntityPanels.get(Record.alias)).getChampionship(), hPicklists.get(Championship.alias), null);
			SwingUtils.fillPicklist(((JCalendarPanel)jEntityPanels.get(Calendar.alias)).getChampionship(), hPicklists.get(Championship.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(City.alias)) {
			SwingUtils.fillPicklist(jResultDialog.getCity1(), hPicklists.get(City.alias), null);
			SwingUtils.fillPicklist(jResultDialog.getCity2(), hPicklists.get(City.alias), null);
			SwingUtils.fillPicklist(((JComplexPanel)jEntityPanels.get(Complex.alias)).getCity(), hPicklists.get(City.alias), null);
			SwingUtils.fillPicklist(((JOlympicsPanel)jEntityPanels.get(Olympics.alias)).getCity(), hPicklists.get(City.alias), null);
			SwingUtils.fillPicklist(((JRecordPanel)jEntityPanels.get(Record.alias)).getCity(), hPicklists.get(City.alias), null);
			SwingUtils.fillPicklist(((JCalendarPanel)jEntityPanels.get(Calendar.alias)).getCity(), hPicklists.get(City.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Complex.alias)) {
			SwingUtils.fillPicklist(jResultDialog.getComplex1(), hPicklists.get(Complex.alias), null);
			SwingUtils.fillPicklist(jResultDialog.getComplex2(), hPicklists.get(Complex.alias), null);
			SwingUtils.fillPicklist(((JTeamStadiumPanel)jEntityPanels.get(TeamStadium.alias)).getComplex(), hPicklists.get(Complex.alias), null);
			SwingUtils.fillPicklist(((JCalendarPanel)jEntityPanels.get(Calendar.alias)).getComplex(), hPicklists.get(Complex.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Country.alias)) {
			SwingUtils.fillPicklist(jResultDialog.getCountry1(), hPicklists.get(Country.alias), null);
			SwingUtils.fillPicklist(jResultDialog.getCountry2(), hPicklists.get(Country.alias), null);
			SwingUtils.fillPicklist(((JCityPanel)jEntityPanels.get(City.alias)).getCountry(), hPicklists.get(Country.alias), null);
			SwingUtils.fillPicklist(((JAthletePanel)jEntityPanels.get(Athlete.alias)).getCountry(), hPicklists.get(Country.alias), null);
			SwingUtils.fillPicklist(((JTeamPanel)jEntityPanels.get(Team.alias)).getCountry(), hPicklists.get(Country.alias), null);
			SwingUtils.fillPicklist(((JOlympicRankingPanel)jEntityPanels.get(OlympicRanking.alias)).getCountry(), hPicklists.get(Country.alias), null);
			SwingUtils.fillPicklist(((JCalendarPanel)jEntityPanels.get(Calendar.alias)).getCountry(), hPicklists.get(Country.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Event.alias)) {
			SwingUtils.fillPicklist(jEditEventDialog.getCategory2(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(jEditEventDialog.getCategory3(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(jEditEventDialog.getCategory4(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(jEditEventDialog.getCategory2(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(jEditEventDialog.getCategory3(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(jEditEventDialog.getCategory4(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(((JRecordPanel)jEntityPanels.get(Record.alias)).getEvent(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(((JRecordPanel)jEntityPanels.get(Record.alias)).getSubevent(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(((JCalendarPanel)jEntityPanels.get(Calendar.alias)).getEvent(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(((JCalendarPanel)jEntityPanels.get(Calendar.alias)).getSubevent(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(((JCalendarPanel)jEntityPanels.get(Calendar.alias)).getSubevent2(), hPicklists.get(Event.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(League.alias)) {
			SwingUtils.fillPicklist(((JTeamPanel)jEntityPanels.get(Team.alias)).getLeague(), hPicklists.get(League.alias), null);
			SwingUtils.fillPicklist(((JHallOfFamePanel)jEntityPanels.get(HallOfFame.alias)).getLeague(), hPicklists.get(League.alias), null);
			SwingUtils.fillPicklist(((JRetiredNumberPanel)jEntityPanels.get(RetiredNumber.alias)).getLeague(), hPicklists.get(League.alias), null);
			SwingUtils.fillPicklist(((JTeamStadiumPanel)jEntityPanels.get(TeamStadium.alias)).getLeague(), hPicklists.get(League.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Olympics.alias)) {
			SwingUtils.fillPicklist(((JOlympicRankingPanel)jEntityPanels.get(OlympicRanking.alias)).getOlympics(), hPicklists.get(Olympics.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Sport.alias)) {
			SwingUtils.fillPicklist(jEditEventDialog.getSport(), hPicklists.get(Sport.alias), null);
			SwingUtils.fillPicklist(jEditEventDialog.getSport(), hPicklists.get(Sport.alias), null);
			SwingUtils.fillPicklist(((JAthletePanel)jEntityPanels.get(Athlete.alias)).getSport(), hPicklists.get(Sport.alias), null);
			SwingUtils.fillPicklist(((JTeamPanel)jEntityPanels.get(Team.alias)).getSport(), hPicklists.get(Sport.alias), null);
			SwingUtils.fillPicklist(((JRecordPanel)jEntityPanels.get(Record.alias)).getSport(), hPicklists.get(Sport.alias), null);
			SwingUtils.fillPicklist(((JCalendarPanel)jEntityPanels.get(Calendar.alias)).getSport(), hPicklists.get(Sport.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(State.alias)) {
			SwingUtils.fillPicklist(((JCityPanel)jEntityPanels.get(City.alias)).getState(), hPicklists.get(State.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Team.alias)) {
			SwingUtils.fillPicklist(((JAthletePanel)jEntityPanels.get(Athlete.alias)).getTeam(), hPicklists.get(Team.alias), null);
			SwingUtils.fillPicklist(((JRetiredNumberPanel)jEntityPanels.get(RetiredNumber.alias)).getTeam(), hPicklists.get(Team.alias), null);
			SwingUtils.fillPicklist(((JTeamStadiumPanel)jEntityPanels.get(TeamStadium.alias)).getTeam(), hPicklists.get(Team.alias), null);
			SwingUtils.fillPicklist(jAllTeams, hPicklists.get(Team.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(com.sporthenon.db.entity.Type.alias)) {
			SwingUtils.fillPicklist(((JEventPanel)jEntityPanels.get(Event.alias)).getType(), hPicklists.get(com.sporthenon.db.entity.Type.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Year.alias)) {
			SwingUtils.fillPicklist(jResultDialog.getYear(), hPicklists.get(Year.alias), null);
			SwingUtils.fillPicklist(((JOlympicsPanel)jEntityPanels.get(Olympics.alias)).getYear(), hPicklists.get(Year.alias), null);
			SwingUtils.fillPicklist(((JHallOfFamePanel)jEntityPanels.get(HallOfFame.alias)).getYear(), hPicklists.get(Year.alias), null);
			SwingUtils.fillPicklist(((JRetiredNumberPanel)jEntityPanels.get(RetiredNumber.alias)).getYear(), hPicklists.get(Year.alias), null);
		}
	}

	private void initPicklists() throws Exception {
		Class<?>[] t = {Championship.class, City.class, Complex.class, Country.class, Event.class, League.class, Olympics.class, Athlete.class, RoundType.class, Sport.class, State.class, Team.class, com.sporthenon.db.entity.Type.class, Year.class};
		for (Class<?> c_ : t) {
			String sql = null;
			if (c_.equals(City.class)) {
				sql = "SELECT CT.id, CT.label || CASE WHEN id_state IS NOT NULL THEN ', ' || ST.code ELSE '' END || ', ' || CN.code AS text "
					+ " FROM city CT LEFT JOIN state ST ON CT.id_state=ST.id LEFT JOIN country CN ON CT.id_country = CN.id ORDER BY text";
			}
			else if (c_.equals(Complex.class)) {
				sql = "SELECT CX.id, CX.label || ' [' || CT.label || CASE WHEN id_state IS NOT NULL THEN ', ' || ST.code ELSE '' END || ', ' || CN.code || ']' AS text "
					+ " FROM complex CX LEFT JOIN city CT ON CX.id_city = CT.id LEFT JOIN state ST ON CT.id_state = ST.id LEFT JOIN country CN ON CT.id_country = CN.id ORDER BY text";
			}
			else if (c_.equals(Athlete.class)) {
				sql = "SELECT PR.id, last_name || ', ' || first_name || CASE WHEN PR.id_country IS NOT NULL THEN ' [' || CN.code || ']' ELSE '' END || CASE WHEN PR.id_team IS NOT NULL THEN ' [' || TM.label || ']' ELSE '' END AS text, PR.id_sport "
					+ " FROM athlete PR LEFT JOIN country CN ON PR.id_country = CN.id LEFT JOIN team TM ON PR.id_team = TM.id ORDER BY text";
			}
			else if (c_.equals(Team.class)) {
				sql = "SELECT TM.id, TM.label || CASE WHEN id_country IS NOT NULL THEN ' [' || CN.code || ']' ELSE '' END || ' [' || SP.label || ']' || CASE WHEN TM.year1 IS NOT NULL AND TM.year1 <> '' THEN ' [' || TM.year1 || ']' ELSE '' END || CASE WHEN TM.year2 IS NOT NULL AND TM.year2 <> '' THEN ' [' || TM.year2 || ']' ELSE '' END AS text, TM.id_sport "
					+ " FROM team TM LEFT JOIN country CN ON TM.id_country = CN.id LEFT JOIN sport SP ON TM.id_sport = SP.id ORDER BY text";
			}
			else if (c_.equals(Country.class)) {
				sql = "SELECT id, label || ' [' || code || ']' FROM country ORDER BY label";
			}
			else if (c_.equals(Event.class)) {
				sql = "SELECT EV.id, EV.label || ' [' || TP.label || ']' FROM event EV JOIN type TP ON TP.id = EV.id_type ORDER BY EV.label";
			}
			else if (c_.equals(Olympics.class)) {
				sql = "SELECT OL.id, YR.label || ' - ' || CT.label FROM olympics OL JOIN year YR ON YR.id = OL.id_year JOIN city CT ON CT.id = OL.id_city ORDER BY YR.label";
			}
			else {
				String table = (String) c_.getField("table").get(null);
				sql = "SELECT id, label FROM " + table + " ORDER BY label";
			}
			List<PicklistItem> lst = (List<PicklistItem>) DatabaseManager.getPicklist(sql, null);
			Field alias = c_.getDeclaredField("alias");
			alias.setAccessible(true);
			hPicklists.put(String.valueOf(alias.get(null)), lst);
			if (c_.equals(Athlete.class)) {
				jAllAthletes = new JEntityPicklist(null, Athlete.alias, true);
				SwingUtils.fillPicklist(jAllAthletes, lst, null);
			}
			else if (c_.equals(Team.class)) {
				jAllTeams = new JEntityPicklist(null, Team.alias, true);
				SwingUtils.fillPicklist(jAllTeams, lst, null);
			}
		}
	}

	public static void refreshPicklist(String alias, PicklistItem value) {
		if (hPicklists.containsKey(alias)) {
			SwingUtils.insertValue(hPicklists.get(alias), value);
			fillPicklists(alias);
		}
	}

	public static PicklistItem saveEntity(String alias, Integer id) throws Exception {
		PicklistItem pi = new PicklistItem();
		Map<String, Object> params = new HashMap<>();
		if (alias.equalsIgnoreCase(Athlete.alias)) {
			JAthletePanel p = (JAthletePanel) jEntityPanels.get(alias);
			params.put("pr-sport", 	SwingUtils.getValue(p.getSport()));
			params.put("pr-team", SwingUtils.getValue(p.getTeam()));
			params.put("pr-country", SwingUtils.getValue(p.getCountry()));
			params.put("pr-lastname", p.getLastName().getText());
			params.put("pr-firstname", p.getFirstName().getText());
			params.put("pr-link", p.getLink().getText());
			String piText = String.valueOf(params.get("pr-lastname"));
			if (StringUtils.notEmpty(params.get("pr-firstname"))) {
				piText += ", " + params.get("pr-firstname");
			}
			if (SwingUtils.getValue(p.getCountry()) != null) {
				piText += " [" + SwingUtils.getText(p.getCountry()).replaceAll(".+\\[|\\]$", "") + "]";
			}
			if (SwingUtils.getValue(p.getTeam()) != null) {
				piText += " [" + SwingUtils.getText(p.getTeam()).replaceAll("\\s\\[.+$", "") + "]";
			}
			pi.setText(piText);
			pi.setParam(StringUtils.toInt(params.get("pr-sport"))); 
		}
		else if (alias.equalsIgnoreCase(Calendar.alias)) {
			JCalendarPanel p = (JCalendarPanel) jEntityPanels.get(alias);
			params.put("cl-sport", SwingUtils.getValue(p.getSport()));
			params.put("cl-championship", SwingUtils.getValue(p.getChampionship()));
			params.put("cl-event", SwingUtils.getValue(p.getEvent()));
			params.put("cl-subevent", SwingUtils.getValue(p.getSubevent()));
			params.put("cl-subevent2", SwingUtils.getValue(p.getSubevent2()));
			params.put("cl-complex", SwingUtils.getValue(p.getComplex()));
			params.put("cl-city", SwingUtils.getValue(p.getCity()));
			params.put("cl-country", SwingUtils.getValue(p.getCountry()));
			params.put("cl-date1", p.getDate1().getText());
			params.put("cl-date2", p.getDate2().getText());
		}
		else if (alias.equalsIgnoreCase(Championship.alias)) {
			JChampionshipPanel p = (JChampionshipPanel) jEntityPanels.get(alias);
			params.put("cp-label", p.getLabel().getText());
			params.put("cp-labelfr", p.getLabelFR().getText());
			params.put("cp-index", p.getIndex().getText());
			params.put("cp-nopic", p.getNopic().isSelected() ? "1" : "0");
			pi.setText(String.valueOf(params.get("cp-label")));
		}
		else if (alias.equalsIgnoreCase(City.alias)) {
			JCityPanel p = (JCityPanel) jEntityPanels.get(alias);
			params.put("ct-label", p.getLabel().getText());
			params.put("ct-labelfr", p.getLabelFR().getText());
			params.put("ct-state", SwingUtils.getValue(p.getState()));
			params.put("ct-country", SwingUtils.getValue(p.getCountry()));
			params.put("ct-link", p.getLink().getText());
			pi.setText(params.get("ct-label") + ", " + SwingUtils.getText(p.getCountry()).replaceAll(".+\\[|\\]$", ""));
		}
		else if (alias.equalsIgnoreCase(Complex.alias)) {
			JComplexPanel p = (JComplexPanel) jEntityPanels.get(alias);
			params.put("cx-label", p.getLabel().getText());
			params.put("cx-city", SwingUtils.getValue(p.getCity()));
			params.put("cx-link", p.getLink().getText());
			pi.setText(params.get("cx-label") + " [" + SwingUtils.getText(p.getCity()) + "]");
		}
		else if (alias.equalsIgnoreCase(Country.alias)) {
			JCountryPanel p = (JCountryPanel) jEntityPanels.get(alias);
			params.put("cn-label", p.getLabel().getText());
			params.put("cn-labelfr", p.getLabelFR().getText());
			params.put("cn-code", p.getCode().getText());
			params.put("cn-nopic", p.getNopic().isSelected() ? "1" : "0");
			pi.setText(params.get("cn-label") + " [" + params.get("cn-code") + "]");
		}
		else if (alias.equalsIgnoreCase(Event.alias)) {
			JEventPanel p = (JEventPanel) jEntityPanels.get(alias);
			params.put("ev-label", p.getLabel().getText());
			params.put("ev-labelfr", p.getLabelFR().getText());
			params.put("ev-type", SwingUtils.getValue(p.getType()));
			params.put("ev-index", p.getIndex().getText());
			params.put("ev-nopic", p.getNopic().isSelected() ? "1" : "0");
			pi.setText(String.valueOf(params.get("ev-label")) + " [" + SwingUtils.getText(p.getType()) + "]");
		}
		else if (alias.equalsIgnoreCase(HallOfFame.alias)) {
			JHallOfFamePanel p = (JHallOfFamePanel) jEntityPanels.get(alias);
			params.put("hf-league", SwingUtils.getValue(p.getLeague()));
			params.put("hf-year", SwingUtils.getValue(p.getYear()));
			params.put("hf-person", SwingUtils.getValue(p.getPerson()));
			params.put("hf-text", p.getText().getText());
			params.put("hf-position", p.getPosition().getText());		
		}
		else if (alias.equalsIgnoreCase(Olympics.alias)) {
			JOlympicsPanel p = (JOlympicsPanel) jEntityPanels.get(alias);
			params.put("ol-year", SwingUtils.getValue(p.getYear()));
			params.put("ol-city", SwingUtils.getValue(p.getCity()));
			params.put("ol-type", p.getType().getText());
			params.put("ol-start", p.getStart().getText());
			params.put("ol-end", p.getEnd().getText());
			params.put("ol-sports", p.getSports().getText());
			params.put("ol-events", p.getEvents().getText());
			params.put("ol-countries", p.getCountries().getText());
			params.put("ol-persons", p.getPersons().getText());
			params.put("ol-nopic", p.getNopic().isSelected() ? "1" : "0");
			pi.setText(SwingUtils.getText(p.getYear()) + " " + SwingUtils.getText(p.getCity()));
		}
		else if (alias.equalsIgnoreCase(OlympicRanking.alias)) {
			JOlympicRankingPanel p = (JOlympicRankingPanel) jEntityPanels.get(alias);
			params.put("or-olympics", SwingUtils.getValue(p.getOlympics()));
			params.put("or-country", SwingUtils.getValue(p.getCountry()));
			params.put("or-gold", p.getGold().getText());
			params.put("or-silver", p.getSilver().getText());
			params.put("or-bronze", p.getBronze().getText());
		}
		else if (alias.equalsIgnoreCase(Record.alias)) {
			JRecordPanel p = (JRecordPanel) jEntityPanels.get(alias);
			params.put("rc-sport", SwingUtils.getValue(p.getSport()));
			params.put("rc-championship", SwingUtils.getValue(p.getChampionship()));
			params.put("rc-event", SwingUtils.getValue(p.getEvent()));
			params.put("rc-subevent", SwingUtils.getValue(p.getSubevent()));
			params.put("rc-city", SwingUtils.getValue(p.getCity()));
			params.put("rc-rank1", SwingUtils.getValue(p.getRank1()));
			params.put("rc-rank2", SwingUtils.getValue(p.getRank2()));
			params.put("rc-rank3", SwingUtils.getValue(p.getRank3()));
			params.put("rc-rank4", SwingUtils.getValue(p.getRank4()));
			params.put("rc-rank5", SwingUtils.getValue(p.getRank1()));
			params.put("rc-record1", p.getRecord1().getText());
			params.put("rc-record2", p.getRecord2().getText());
			params.put("rc-record3", p.getRecord3().getText());
			params.put("rc-record4", p.getRecord4().getText());
			params.put("rc-record5", p.getRecord5().getText());
			params.put("rc-date1", p.getDate1().getText());
			params.put("rc-date2", p.getDate2().getText());
			params.put("rc-date3", p.getDate3().getText());
			params.put("rc-date4", p.getDate4().getText());
			params.put("rc-date5", p.getDate1().getText());
			params.put("rc-type1", p.getType1().getText());
			params.put("rc-type2", p.getType2().getText());
			params.put("rc-label", p.getLabel().getText());
			params.put("rc-counting", p.getCounting().isSelected() ? "1" : "0");
			params.put("rc-index", p.getIndex().getText());
			params.put("rc-tie", p.getExa().getText());
			params.put("rc-comment", p.getComment().getText());
		}
		else if (alias.equalsIgnoreCase(RetiredNumber.alias)) {
			JRetiredNumberPanel p = (JRetiredNumberPanel) jEntityPanels.get(alias);
			params.put("rn-league", SwingUtils.getValue(p.getLeague()));
			params.put("rn-team", SwingUtils.getValue(p.getTeam()));
			params.put("rn-person", SwingUtils.getValue(p.getPerson()));
			params.put("rn-year", SwingUtils.getValue(p.getYear()));
			params.put("rn-number", p.getNumber().getText());
		}
		else if (alias.equalsIgnoreCase(RoundType.alias)) {
			JRoundTypePanel p = (JRoundTypePanel) jEntityPanels.get(alias);
			params.put("rt-label", p.getLabel().getText());
			params.put("rt-labelfr", p.getLabelFR().getText());
			params.put("rt-index", p.getIndex().getText());
			pi.setText(String.valueOf(params.get("rt-label")));
		}
		else if (alias.equalsIgnoreCase(Sport.alias)) {
			JSportPanel p = (JSportPanel) jEntityPanels.get(alias);
			params.put("sp-label", p.getLabel().getText());
			params.put("sp-labelfr", p.getLabelFR().getText());
			params.put("sp-type", p.getType().getText());
			params.put("sp-index", p.getIndex().getText());
			params.put("sp-nopic", p.getNopic().isSelected() ? "1" : "0");
			pi.setText(String.valueOf(params.get("sp-label")));
		}
		else if (alias.equalsIgnoreCase(State.alias)) {
			JStatePanel p = (JStatePanel) jEntityPanels.get(alias);
			params.put("st-label", p.getLabel().getText());
			params.put("st-labelfr", p.getLabelFR().getText());
			params.put("st-code", p.getCode().getText());
			params.put("st-capital", p.getCapital().getText());
			params.put("st-nopic", p.getNopic().isSelected() ? "1" : "0");
			pi.setText(String.valueOf(params.get("st-label")));
		}
		else if (alias.equalsIgnoreCase(Team.alias)) {
			JTeamPanel p = (JTeamPanel) jEntityPanels.get(alias);
			params.put("tm-label", p.getLabel().getText());
			params.put("tm-sport", SwingUtils.getValue(p.getSport()));
			params.put("tm-country", SwingUtils.getValue(p.getCountry()));
			params.put("tm-league", SwingUtils.getValue(p.getLeague()));
			params.put("tm-conference", p.getConference().getText());
			params.put("tm-division", p.getDivision().getText());
			params.put("tm-comment", p.getComment().getText());
			params.put("tm-year1", p.getYear1().getText());
			params.put("tm-year2", p.getYear2().getText());
			params.put("tm-link", p.getLink().getText());
			params.put("tm-inactive", p.getInactive().isSelected() ? "1" : "0");
			params.put("tm-nopic", p.getNopic().isSelected() ? "1" : "0");
			pi.setText(params.get("tm-label") + (SwingUtils.getValue(p.getCountry()) > 0 ? " [" + SwingUtils.getText(p.getCountry()) + "]" : "") + " [" + SwingUtils.getText(p.getSport()) + "]");
			pi.setParam(StringUtils.toInt(params.get("tm-sport")));
		}
		else if (alias.equalsIgnoreCase(TeamStadium.alias)) {
			JTeamStadiumPanel p = (JTeamStadiumPanel) jEntityPanels.get(alias);
			params.put("ts-league", SwingUtils.getValue(p.getLeague()));
			params.put("ts-team", SwingUtils.getValue(p.getTeam()));
			params.put("ts-complex", SwingUtils.getValue(p.getComplex()));
			params.put("ts-date1", p.getDate1().getText());
			params.put("ts-date2", p.getDate2().getText());
			params.put("ts-renamed", p.getRenamed().isSelected() ? "1" : "0");
		}
		else if (alias.equalsIgnoreCase(Year.alias)) {
			JYearPanel p = (JYearPanel) jEntityPanels.get(alias);
			params.put("yr-label", p.getLabel().getText());
			pi.setText(String.valueOf(params.get("yr-label")));
		}
		else if (alias.equalsIgnoreCase(Config.alias)) {
			JConfigPanel p = (JConfigPanel) jEntityPanels.get(alias);
			params.put("cg-key", p.getKey().getText());
			params.put("cg-value", p.getValue().getText());
			params.put("cg-valuehtml", p.getValueHtml().getText());
			pi.setText(String.valueOf(params.get("cg-key")));
		}
		Object o = UpdateUtils.saveEntity(alias, id, params, getContributor());
		Class<? extends AbstractEntity> c = DatabaseManager.getClassFromAlias(alias);
		String id_ = String.valueOf(c.getMethod("getId").invoke(o, new Object[0]));
		pi.setValue(StringUtils.toInt(id_));
		JMainFrame.refreshPicklist(alias, pi);
		return pi;
	}

	private JPanel getJContentPane() {
		jContentPane = new JPanel();
		jBottomPanel = new JBottomPanel();
		jTopPanel = new JTopPanel(this);
		jTabsPanel = getTabsPanel();
		jContentPane.setLayout(new BorderLayout(0, 3));
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 3));
		jContentPane.add(jTopPanel, BorderLayout.NORTH);
		jContentPane.add(jBottomPanel, BorderLayout.SOUTH);
		jContentPane.add(jTabsPanel, BorderLayout.CENTER);
		return jContentPane;
	}

	private JPanel getTabsPanel() {
		JPanel emptyPanel = new JPanel();
		emptyPanel.setLayout(new GridBagLayout());
		emptyPanel.setBorder(BorderFactory.createEtchedBorder());
		jConnectInfoLabel = new JLabel("Not connected to database. Click on \"Connect\".");
		emptyPanel.add(jConnectInfoLabel);
		jResultsPanel = new JResultsPanel(this);
		jDataPanel = new JDataPanel(this);
		jPicturesPanel = new JPicturesPanel(this);
		jExtLinksPanel = new JExtLinksPanel(this);
		jUsersPanel = new JUsersPanel(this);
		JPanel p = new JPanel();
		CardLayout cardLayout = new CardLayout();
		p.setLayout(cardLayout);
		p.add(emptyPanel, "empty");
		p.add(jResultsPanel, "results");
		p.add(jDataPanel, "data");
		p.add(jPicturesPanel, "pictures");
		p.add(jExtLinksPanel, "extlinks");
		p.add(jUsersPanel, "users");
		return p;
	}
	
	public void changeTabPanel(String name) {
		((CardLayout) jTabsPanel.getLayout()).show(jTabsPanel, name);
	}

	@SuppressWarnings("unchecked")
	public boolean connectCallback(boolean connected) {
		boolean err = false;
		try {
			if (connected) {
				jPasswordDialog.open();
				jConnectInfoLabel.setText("Connecting to database...");
				jBottomPanel.getQueryStatus().showProgress();
				jBottomPanel.getConnectionStatus().set((short)1, null);
				
				final String dbHost = jOptionsDialog.getHost().getText();
				final String dbPort = jOptionsDialog.getPort().getText();
				final String dbName = jOptionsDialog.getDatabase().getText();
				final String dbUser = jOptionsDialog.getLogin().getText();
				final String dbPwd = new String(jPasswordDialog.getPassword().getPassword());
				DatabaseManager.createConnectionPool(dbHost, dbPort, dbName, dbUser, dbPwd);
			
				String sql = "SELECT * FROM _contributor WHERE login = '" + jOptionsDialog.getLogin().getText() + "' AND active = TRUE AND (admin = TRUE OR contrib = TRUE)";
				List<Contributor> lst = (List<Contributor>) DatabaseManager.executeSelect(sql, Contributor.class);
				if (lst == null || lst.isEmpty()) {
					throw new Exception("Your account is not active or has not admin rights.");
				}
				contributor = lst.get(0);
				initAll(jPasswordDialog.getQuickLoading().isSelected());
				jDataPanel.getList().setSelectedIndex(0);
				jDataPanel.valueChanged(new ListSelectionEvent(this, 0, 0, true));
				jPicturesPanel.getList().setSelectedIndex(0);
				jPicturesPanel.changeEntity(0);
				jExtLinksPanel.valueChanged(new ListSelectionEvent(this, 0, 0, true));
				jUsersPanel.initList();
				jUsersPanel.valueChanged(new ListSelectionEvent(jUsersPanel.getList(), 0, 0, true));
			//	jUsersPanel.getSaveButton().setEnabled(contributor.isAdmin());
			}
			else {
				contributor = null;
				changeTabPanel("empty");
				jTopPanel.getResultsButton().setSelected(false);
				jTopPanel.getDataButton().setSelected(false);
				jTopPanel.getPicturesButton().setSelected(false);
				jTopPanel.getExtLinksButton().setSelected(false);
				jTopPanel.getUsersButton().setSelected(false);
				jTopPanel.getImportButton().setEnabled(false);
				jTopPanel.getQueryButton().setEnabled(false);
				jBottomPanel.getQueryStatus().set((short)-1, null);
			}
		}
		catch (Exception e) {
			err = true;
			connected = false;
			log.log(Level.SEVERE, e.getMessage(), e);
			JOptionPane.showMessageDialog(this, "Failed to connect to database. See message below:\r\n\r\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		finally {
			jBottomPanel.getConnectionStatus().set((short)(connected ? 2 : 0), jOptionsDialog.getDatabase().getText());
			if (!connected) {
				jConnectInfoLabel.setText("Not connected to database. Click on \"Connect\".");
			}
			jBottomPanel.getQueryStatus().hideProgress();
		}
		return !err;
	}

	public JConnectionStatus getConnectionStatus() {
		return jBottomPanel.getConnectionStatus();
	}

	public JQueryStatus getQueryStatus() {
		return jBottomPanel.getQueryStatus();
	}

	public static JEditResultDialog getResultDialog() {
		return jResultDialog;
	}

	public static JEditEventDialog getEditEventDialog() {
		return jEditEventDialog;
	}

	public static JFindEntityDialog getFindDialog() {
		return jFindDialog;
	}
	
	public static JMergeEntityDialog getMergeDialog() {
		return jMergeDialog;
	}

	public static JAddMultipleDialog getAddMultipleDialog() {
		return jAddMultipleDialog;
	}
	
	public static JUrlUpdateDialog getUrlUpdateDialog() {
		return jUrlUpdateDialog;
	}
	
	public static JCommentDialog getCommentDialog() {
		return jCommentDialog;
	}

	public static JCharsDialog getCharsDialog() {
		return jCharsDialog;
	}
	
	public static JImportDialog getImportDialog() {
		return jImportDialog;
	}

	public static JQueryDialog getQueryDialog() {
		return jQueryDialog;
	}

	public static JOptionsDialog getOptionsDialog() {
		return jOptionsDialog;
	}

	public static JInfoDialog getInfoDialog() {
		return jInfoDialog;
	}

	public static Map<String, List<PicklistItem>> getPicklists() {
		return hPicklists;
	}

	public static Map<String, JAbstractEntityPanel> getEntityPanels() {
		return jEntityPanels;
	}
	
	public JDataPanel getDataPanel() {
		return jDataPanel;
	}

	public static Contributor getContributor() {
		return contributor;
	}

	public static JEntityPicklist getAllAthletes() {
		return jAllAthletes;
	}

	public static JEntityPicklist getAllTeams() {
		return jAllTeams;
	}

}