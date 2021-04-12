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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import com.sporthenon.admin.container.entity.JChampionshipPanel;
import com.sporthenon.admin.container.entity.JCityPanel;
import com.sporthenon.admin.container.entity.JComplexPanel;
import com.sporthenon.admin.container.entity.JCountryPanel;
import com.sporthenon.admin.container.entity.JEventPanel;
import com.sporthenon.admin.container.entity.JHallOfFamePanel;
import com.sporthenon.admin.container.entity.JOlympicRankingPanel;
import com.sporthenon.admin.container.entity.JOlympicsPanel;
import com.sporthenon.admin.container.entity.JRecordPanel;
import com.sporthenon.admin.container.entity.JRetiredNumberPanel;
import com.sporthenon.admin.container.entity.JSportPanel;
import com.sporthenon.admin.container.entity.JStatePanel;
import com.sporthenon.admin.container.entity.JTeamPanel;
import com.sporthenon.admin.container.entity.JTeamStadiumPanel;
import com.sporthenon.admin.container.entity.JWinLossPanel;
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
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.entity.WinLoss;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;
import com.sporthenon.utils.res.ResourceUtils;

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
	private static JEditFolderDialog jFolderDialog = null;
	private static JEditEntityDialog jEntityDialog = null;
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
	private static HashMap<String, JAbstractEntityPanel> jEntityPanels = null;
	private static HashMap<String, List<PicklistItem>> hPicklists = new HashMap<String, List<PicklistItem>>();

	public JMainFrame() {
		super();
		initialize();
		this.setVisible(true);
	}

	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			jEntityPanels = new HashMap<String, JAbstractEntityPanel>();
			jEntityPanels.put(Championship.alias, new JChampionshipPanel());
			jEntityPanels.put(City.alias, new JCityPanel());
			jEntityPanels.put(Complex.alias, new JComplexPanel());
			jEntityPanels.put(Country.alias, new JCountryPanel());
			jEntityPanels.put(Event.alias, new JEventPanel());
			jEntityPanels.put(Olympics.alias, new JOlympicsPanel());
			jEntityPanels.put(Athlete.alias, new JAthletePanel());
			jEntityPanels.put(Sport.alias, new JSportPanel());
			jEntityPanels.put(State.alias, new JStatePanel());
			jEntityPanels.put(Team.alias, new JTeamPanel());
			jEntityPanels.put(Year.alias, new JYearPanel());
			jEntityPanels.put(HallOfFame.alias, new JHallOfFamePanel());
			jEntityPanels.put(OlympicRanking.alias, new JOlympicRankingPanel());
			jEntityPanels.put(Record.alias, new JRecordPanel());
			jEntityPanels.put(RetiredNumber.alias, new JRetiredNumberPanel());
			jEntityPanels.put(TeamStadium.alias, new JTeamStadiumPanel());
			jEntityPanels.put(WinLoss.alias, new JWinLossPanel());

			jInfoDialog = new JInfoDialog(this);
			jImportDialog = new JImportDialog(this);
			jQueryDialog = new JQueryDialog(this);
			jOptionsDialog = new JOptionsDialog(this);
			jPasswordDialog = new JPasswordDialog(this);
			
			this.setFont(SwingUtils.getDefaultFont());
			this.setMinimumSize(new Dimension(640, 480));
			this.setSize(new Dimension(1050, 700));
			this.setTitle("Sporthenon Admin v" + ConfigUtils.getProperty("version"));
			this.setContentPane(getJContentPane());
			this.setLocationRelativeTo(null);
			List<Image> lIcons = new ArrayList<Image>();
			for (String size : new String[]{"16", "24", "32", "48", "64", "72", "96", "128", "256"})
				lIcons.add(Toolkit.getDefaultToolkit().getImage(JMainFrame.class.getResource("/com/sporthenon/utils/res/img/icon" + size + ".png")));
			this.setIconImages(lIcons);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
			System.exit(1);
		}
	}

	private void initAll(boolean quickload) throws Exception {
		if (!quickload)
			initPicklists();
		jResultsPanel.setTree();
		jResultDialog  = new JEditResultDialog(this);
		jFolderDialog  = new JEditFolderDialog(this);
		jEntityDialog  = new JEditEntityDialog(this);
		jFindDialog  = new JFindEntityDialog(this);
		jMergeDialog  = new JMergeEntityDialog(this);
		jAddMultipleDialog = new JAddMultipleDialog(this);
		jUrlUpdateDialog = new JUrlUpdateDialog(this);
		jCommentDialog = new JCommentDialog(this);
		jCharsDialog = new JCharsDialog(this);
		if (!quickload) {
			fillPicklists(null);
			jPicturesPanel.getSportList().removeAllItems();
			for (int i = 0 ; i < jFolderDialog.getSport().getPicklist().getItemCount() ; i++)
				jPicturesPanel.getSportList().addItem(jFolderDialog.getSport().getPicklist().getItemAt(i));	
		}
	}

	public static void fillPicklists(String alias) {
		if (alias == null || alias.equalsIgnoreCase(Athlete.alias)) {
			SwingUtils.fillPicklist(((JHallOfFamePanel)jEntityPanels.get(HallOfFame.alias)).getPerson(), hPicklists.get(Athlete.alias), null);
			SwingUtils.fillPicklist(((JRetiredNumberPanel)jEntityPanels.get(RetiredNumber.alias)).getPerson(), hPicklists.get(Athlete.alias), null);
			SwingUtils.fillPicklist(jAllAthletes, hPicklists.get(Athlete.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Olympics.alias)) {
			SwingUtils.fillPicklist(((JOlympicRankingPanel)jEntityPanels.get(OlympicRanking.alias)).getOlympics(), hPicklists.get(Olympics.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Year.alias)) {
			SwingUtils.fillPicklist(jResultDialog.getYear(), hPicklists.get(Year.alias), null);
			SwingUtils.fillPicklist(((JOlympicsPanel)jEntityPanels.get(Olympics.alias)).getYear(), hPicklists.get(Year.alias), null);
			SwingUtils.fillPicklist(((JHallOfFamePanel)jEntityPanels.get(HallOfFame.alias)).getYear(), hPicklists.get(Year.alias), null);
			SwingUtils.fillPicklist(((JRetiredNumberPanel)jEntityPanels.get(RetiredNumber.alias)).getYear(), hPicklists.get(Year.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Complex.alias)) {
			SwingUtils.fillPicklist(jResultDialog.getComplex1(), hPicklists.get(Complex.alias), null);
			SwingUtils.fillPicklist(jResultDialog.getComplex2(), hPicklists.get(Complex.alias), null);
			SwingUtils.fillPicklist(((JTeamStadiumPanel)jEntityPanels.get(TeamStadium.alias)).getComplex(), hPicklists.get(Complex.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(City.alias)) {
			SwingUtils.fillPicklist(jResultDialog.getCity1(), hPicklists.get(City.alias), null);
			SwingUtils.fillPicklist(jResultDialog.getCity2(), hPicklists.get(City.alias), null);
			SwingUtils.fillPicklist(((JComplexPanel)jEntityPanels.get(Complex.alias)).getCity(), hPicklists.get(City.alias), null);
			SwingUtils.fillPicklist(((JOlympicsPanel)jEntityPanels.get(Olympics.alias)).getCity(), hPicklists.get(City.alias), null);
			SwingUtils.fillPicklist(((JRecordPanel)jEntityPanels.get(Record.alias)).getCity(), hPicklists.get(City.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Sport.alias)) {
			SwingUtils.fillPicklist(jFolderDialog.getSport(), hPicklists.get(Sport.alias), null);
			SwingUtils.fillPicklist(((JAthletePanel)jEntityPanels.get(Athlete.alias)).getSport(), hPicklists.get(Sport.alias), null);
			SwingUtils.fillPicklist(((JTeamPanel)jEntityPanels.get(Team.alias)).getSport(), hPicklists.get(Sport.alias), null);
			SwingUtils.fillPicklist(((JRecordPanel)jEntityPanels.get(Record.alias)).getSport(), hPicklists.get(Sport.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Championship.alias)) {
			SwingUtils.fillPicklist(jFolderDialog.getCategory1(), hPicklists.get(Championship.alias), null);
			SwingUtils.fillPicklist(((JRecordPanel)jEntityPanels.get(Record.alias)).getChampionship(), hPicklists.get(Championship.alias), null);
			SwingUtils.fillPicklist(((JRecordPanel)jEntityPanels.get(Record.alias)).getChampionship(), hPicklists.get(Championship.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Event.alias)) {
			SwingUtils.fillPicklist(jFolderDialog.getCategory2(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(jFolderDialog.getCategory3(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(jFolderDialog.getCategory4(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(((JRecordPanel)jEntityPanels.get(Record.alias)).getEvent(), hPicklists.get(Event.alias), null);
			SwingUtils.fillPicklist(((JRecordPanel)jEntityPanels.get(Record.alias)).getSubevent(), hPicklists.get(Event.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Country.alias)) {
			SwingUtils.fillPicklist(((JCityPanel)jEntityPanels.get(City.alias)).getCountry(), hPicklists.get(Country.alias), null);
			SwingUtils.fillPicklist(((JAthletePanel)jEntityPanels.get(Athlete.alias)).getCountry(), hPicklists.get(Country.alias), null);
			SwingUtils.fillPicklist(((JTeamPanel)jEntityPanels.get(Team.alias)).getCountry(), hPicklists.get(Country.alias), null);
			SwingUtils.fillPicklist(((JOlympicRankingPanel)jEntityPanels.get(OlympicRanking.alias)).getCountry(), hPicklists.get(Country.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(State.alias)) {
			SwingUtils.fillPicklist(((JCityPanel)jEntityPanels.get(City.alias)).getState(), hPicklists.get(State.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(com.sporthenon.db.entity.Type.alias)) {
			SwingUtils.fillPicklist(((JEventPanel)jEntityPanels.get(Event.alias)).getType(), hPicklists.get(com.sporthenon.db.entity.Type.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Team.alias)) {
			SwingUtils.fillPicklist(((JAthletePanel)jEntityPanels.get(Athlete.alias)).getTeam(), hPicklists.get(Team.alias), null);
			SwingUtils.fillPicklist(((JRetiredNumberPanel)jEntityPanels.get(RetiredNumber.alias)).getTeam(), hPicklists.get(Team.alias), null);
			SwingUtils.fillPicklist(((JTeamStadiumPanel)jEntityPanels.get(TeamStadium.alias)).getTeam(), hPicklists.get(Team.alias), null);
			SwingUtils.fillPicklist(((JWinLossPanel)jEntityPanels.get(WinLoss.alias)).getTeam(), hPicklists.get(Team.alias), null);
			SwingUtils.fillPicklist(jAllTeams, hPicklists.get(Team.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(League.alias)) {
			SwingUtils.fillPicklist(((JTeamPanel)jEntityPanels.get(Team.alias)).getLeague(), hPicklists.get(League.alias), null);
			SwingUtils.fillPicklist(((JHallOfFamePanel)jEntityPanels.get(HallOfFame.alias)).getLeague(), hPicklists.get(League.alias), null);
			SwingUtils.fillPicklist(((JRetiredNumberPanel)jEntityPanels.get(RetiredNumber.alias)).getLeague(), hPicklists.get(League.alias), null);
			SwingUtils.fillPicklist(((JTeamStadiumPanel)jEntityPanels.get(TeamStadium.alias)).getLeague(), hPicklists.get(League.alias), null);
			SwingUtils.fillPicklist(((JWinLossPanel)jEntityPanels.get(WinLoss.alias)).getLeague(), hPicklists.get(League.alias), null);
		}
	}

	private void initPicklists() throws Exception {
		Class<?>[] t = {Championship.class, City.class, Complex.class, Country.class, Event.class, League.class, Olympics.class, Athlete.class, Sport.class, State.class, Team.class, com.sporthenon.db.entity.Type.class, Year.class};
		for (Class<?> c_ : t) {
			String sql = null;
			if (c_.equals(City.class)) {
				sql = "SELECT CT.id, CT.label || case when id_state is not null then ', ' || ST.code else '' end || ', ' || CN.code as text "
					+ " FROM city CT left join state ST on CT.id_state=ST.id left join country CN on CT.id_country=CN.id ORDER BY text";
			}
			else if (c_.equals(Complex.class)) {
				sql = "SELECT CX.id, CX.label || ' [' || CT.label || case when id_state is not null then ', ' || ST.code else '' end || ', ' || CN.code || ']' as text "
					+ " FROM complex CX left join city CT on CX.id_city=CT.id left join state ST on CT.id_state=ST.id left join country CN on CT.id_country=CN.id ORDER BY text";
			}
			else if (c_.equals(Athlete.class)) {
				sql = "SELECT PR.id, last_name || ', ' || first_name || case when PR.id_country is not null then ' [' || CN.code || ']' else '' end || case when PR.id_team is not null then ' [' || TM.label || ']' else '' end as text, PR.id_sport "
					+ " FROM athlete PR left join country CN on PR.id_country=CN.id left join team TM on PR.id_team=TM.id ORDER BY text";
			}
			else if (c_.equals(Team.class)) {
				sql = "SELECT TM.id, TM.label || case when id_country is not null then ' [' || CN.code || ']' else '' end || case when TM.year1 is not null and TM.year1 <> '' then ' [' || TM.year1 || ']' else '' end || case when TM.year2 is not null and TM.year2 <> '' then ' [' || TM.year2 || ']' else '' end as text, TM.id_sport "
					+ " FROM team TM left join country CN on TM.id_country=CN.id order by text";
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
				jAllAthletes = new JEntityPicklist(null, Athlete.alias);
				SwingUtils.fillPicklist(jAllAthletes, lst, null);
			}
			else if (c_.equals(Team.class)) {
				jAllTeams = new JEntityPicklist(null, Team.alias);
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
		Class<? extends AbstractEntity> c = DatabaseManager.getClassFromAlias(alias);
		Object o = (id != null ? DatabaseManager.loadEntity(c, id) : c.getConstructor().newInstance());
		PicklistItem plb = new PicklistItem();
		if (alias.equalsIgnoreCase(Athlete.alias)) {
			JAthletePanel p = (JAthletePanel) jEntityPanels.get(alias);
			Athlete en = (Athlete) o;
			en.setSport((Sport)DatabaseManager.loadEntity(Sport.class, SwingUtils.getValue(p.getSport())));
			en.setTeam((Team)DatabaseManager.loadEntity(Team.class, SwingUtils.getValue(p.getTeam())));
			en.setCountry((Country)DatabaseManager.loadEntity(Country.class, SwingUtils.getValue(p.getCountry())));
			en.setLink(StringUtils.toInt(p.getLink().getText()));
			en.setLastName(p.getLastName().getText());
			en.setFirstName(p.getFirstName().getText());
			plb.setParam(String.valueOf(en.getSport().getId())); plb.setText(en.getLastName() + ", " + en.getFirstName() + (en.getCountry() != null ? " [" + en.getCountry().getCode() + "]" : "") + (en.getTeam() != null ? " [" + en.getTeam().getLabel() + "]" : ""));
			if (en.getLink() != null && en.getLink() > 0) {
				try {
					Athlete a = (Athlete) DatabaseManager.loadEntity(Athlete.class, en.getLink());
					while (a.getLink() != null && a.getLink() > 0)
						a = (Athlete) DatabaseManager.loadEntity(Athlete.class, a.getLink());
					en.setLink(a.getId());
					p.setLinkLabel(" Linked to: [" + a.getLastName() + (StringUtils.notEmpty(a.getFirstName()) ? ", " + a.getFirstName() : "") + (a.getCountry() != null ? ", " + a.getCountry().getCode() : "") + (a.getTeam() != null ? ", " + a.getTeam().getLabel() : "") + "]");
					DatabaseManager.executeUpdate("UPDATE athlete SET LINK = 0 WHERE id = ?", Arrays.asList(en.getLink()));
				}
				catch (Exception e) {
					log.log(Level.WARNING, e.getMessage());
				}
			}
		}
		else if (alias.equalsIgnoreCase(Championship.alias)) {
			JChampionshipPanel p = (JChampionshipPanel) jEntityPanels.get(alias);
			Championship en = (Championship) o;
			en.setLabel(p.getLabel().getText());
			en.setLabelFr(p.getLabelFR().getText());
			en.setIndex(StringUtils.notEmpty(p.getIndex().getText()) ? Double.parseDouble(p.getIndex().getText()) : Double.MAX_VALUE);
			plb.setText(en.getLabel());
		}
		else if (alias.equalsIgnoreCase(City.alias)) {
			JCityPanel p = (JCityPanel) jEntityPanels.get(alias);
			City en = (City) o;
			en.setLabel(p.getLabel().getText());
			en.setLabelFr(p.getLabelFR().getText());
			en.setState((State)DatabaseManager.loadEntity(State.class, SwingUtils.getValue(p.getState())));
			en.setCountry((Country)DatabaseManager.loadEntity(Country.class, SwingUtils.getValue(p.getCountry())));
			en.setLink(StringUtils.toInt(p.getLink().getText()));
			plb.setText(en.getLabel() + ", " + en.getCountry().getCode());
			if (en.getLink() != null && en.getLink() > 0) {
				try {
					City c_ = (City) DatabaseManager.loadEntity(City.class, en.getLink());
					while (c_.getLink() != null && c_.getLink() > 0)
						c_ = (City) DatabaseManager.loadEntity(City.class, c_.getLink());
					en.setLink(c_.getId());
					p.setLinkLabel(" Linked to: [" + c_.toString2(ResourceUtils.LGDEFAULT) + "]");
					DatabaseManager.executeUpdate("UPDATE city SET LINK = 0 WHERE ID = ?", Arrays.asList(en.getLink()));
				}
				catch (Exception e) {
					log.log(Level.WARNING, e.getMessage());
				}
			}
		}
		else if (alias.equalsIgnoreCase(Complex.alias)) {
			JComplexPanel p = (JComplexPanel) jEntityPanels.get(alias);
			Complex en = (Complex) o;
			en.setLabel(p.getLabel().getText());
			en.setCity((City)DatabaseManager.loadEntity(City.class, SwingUtils.getValue(p.getCity())));
			en.setLink(StringUtils.toInt(p.getLink().getText()));
			plb.setText(en.getLabel() + " [" + en.getCity().getLabel() + ", " + en.getCity().getCountry().getCode() + "]");
			if (en.getLink() != null && en.getLink() > 0) {
				try {
					Complex c_ = (Complex) DatabaseManager.loadEntity(Complex.class, en.getLink());
					while (c_.getLink() != null && c_.getLink() > 0)
						c_ = (Complex) DatabaseManager.loadEntity(Complex.class, c_.getLink());
					en.setLink(c_.getId());
					p.setLinkLabel(" Linked to: [" + c_.toString2(ResourceUtils.LGDEFAULT) + "]");
					DatabaseManager.executeUpdate("UPDATE complex SET LINK = 0 WHERE ID = ?", Arrays.asList(en.getLink()));
				}
				catch (Exception e) {
					log.log(Level.WARNING, e.getMessage());
				}
			}
		}
		else if (alias.equalsIgnoreCase(Country.alias)) {
			JCountryPanel p = (JCountryPanel) jEntityPanels.get(alias);
			Country en = (Country) o;
			en.setLabel(p.getLabel().getText());
			en.setLabelFr(p.getLabelFR().getText());
			en.setCode(p.getCode().getText());
			plb.setText(en.getLabel() + " [" + en.getCode() + "]");
		}
		else if (alias.equalsIgnoreCase(Event.alias)) {
			JEventPanel p = (JEventPanel) jEntityPanels.get(alias);
			Event en = (Event) o;
			en.setLabel(p.getLabel().getText());
			en.setLabelFr(p.getLabelFR().getText());
			en.setType((com.sporthenon.db.entity.Type)DatabaseManager.loadEntity(com.sporthenon.db.entity.Type.class, SwingUtils.getValue(p.getType())));
			en.setIndex(StringUtils.notEmpty(p.getIndex().getText()) ? Double.parseDouble(p.getIndex().getText()) : Double.MAX_VALUE);
			plb.setText(en.getLabel());
		}
		else if (alias.equalsIgnoreCase(Olympics.alias)) {
			JOlympicsPanel p = (JOlympicsPanel) jEntityPanels.get(alias);
			Olympics en = (Olympics) o;
			en.setYear((Year)DatabaseManager.loadEntity(Year.class, SwingUtils.getValue(p.getYear())));
			en.setCity((City)DatabaseManager.loadEntity(City.class, SwingUtils.getValue(p.getCity())));
			en.setType(StringUtils.toInt(p.getType().getText()));
			en.setCountSport(StringUtils.toInt(p.getSports().getText()));
			en.setCountEvent(StringUtils.toInt(p.getEvents().getText()));
			en.setCountPerson(StringUtils.toInt(p.getPersons().getText()));
			en.setCountCountry(StringUtils.toInt(p.getCountries().getText()));
			en.setDate1(p.getStart().getText());
			en.setDate2(p.getEnd().getText());
			plb.setText(en.getYear().getLabel() + " - " + en.getCity().getLabel());
		}
		else if (alias.equalsIgnoreCase(Sport.alias)) {
			JSportPanel p = (JSportPanel) jEntityPanels.get(alias);
			Sport en = (Sport) o;
			en.setLabel(p.getLabel().getText());
			en.setLabelFr(p.getLabelFR().getText());
			en.setType(StringUtils.toInt(p.getType().getText()));
			en.setIndex(StringUtils.notEmpty(p.getIndex().getText()) ? Double.valueOf(p.getIndex().getText()) : null);
			plb.setText(en.getLabel());
		}
		else if (alias.equalsIgnoreCase(State.alias)) {
			JStatePanel p = (JStatePanel) jEntityPanels.get(alias);
			State en = (State) o;
			en.setLabel(p.getLabel().getText());
			en.setLabelFr(p.getLabelFR().getText());
			en.setCode(p.getCode().getText());
			en.setCapital(p.getCapital().getText());
			plb.setText(en.getLabel());
		}
		else if (alias.equalsIgnoreCase(Team.alias)) {
			JTeamPanel p = (JTeamPanel) jEntityPanels.get(alias);
			Team en = (Team) o;
			en.setLabel(p.getLabel().getText());
			en.setSport((Sport)DatabaseManager.loadEntity(Sport.class, SwingUtils.getValue(p.getSport())));
			en.setCountry((Country)DatabaseManager.loadEntity(Country.class, SwingUtils.getValue(p.getCountry())));
			en.setLeague((League)DatabaseManager.loadEntity(League.class, SwingUtils.getValue(p.getLeague())));
			en.setConference(p.getConference().getText());
			en.setDivision(p.getDivision().getText());
			en.setComment(p.getComment().getText());
			en.setYear1(p.getYear1().getText());
			en.setYear2(p.getYear2().getText());
			en.setLink(StringUtils.toInt(p.getLink().getText()));
			en.setInactive(p.getInactive().isSelected());
			plb.setParam(String.valueOf(en.getSport().getId())); plb.setText(en.getLabel() + (en.getCountry() != null ? " [" + en.getCountry().getCode() + "]" : ""));
			if (en.getLink() != null && en.getLink() > 0) {
				try {
					Team t = (Team) DatabaseManager.loadEntity(Team.class, en.getLink());
					while (t.getLink() != null && t.getLink() > 0)
						t = (Team) DatabaseManager.loadEntity(Team.class, t.getLink());
					en.setLink(t.getId());
					p.setLinkLabel(" Linked to: [" + t.getLabel() + "]");
					DatabaseManager.executeUpdate("UPDATE team SET LINK = 0 WHERE ID = ?", Arrays.asList(en.getLink()));
				}
				catch (Exception e) {
					log.log(Level.WARNING, e.getMessage());
				}
			}
		}
		else if (alias.equalsIgnoreCase(Year.alias)) {
			JYearPanel p = (JYearPanel) jEntityPanels.get(alias);
			Year en = (Year) o;
			en.setLabel(p.getLabel().getText());
			plb.setText(en.getLabel());
		}
		else if (alias.equalsIgnoreCase(HallOfFame.alias)) {
			JHallOfFamePanel p = (JHallOfFamePanel) jEntityPanels.get(alias);
			HallOfFame en = (HallOfFame) o;
			en.setLeague((League)DatabaseManager.loadEntity(League.class, SwingUtils.getValue(p.getLeague())));
			en.setYear((Year)DatabaseManager.loadEntity(Year.class, SwingUtils.getValue(p.getYear())));
			en.setPerson((Athlete)DatabaseManager.loadEntity(Athlete.class, SwingUtils.getValue(p.getPerson())));
			en.setPosition(p.getPosition().getText());
		}
		else if (alias.equalsIgnoreCase(OlympicRanking.alias)) {
			JOlympicRankingPanel p = (JOlympicRankingPanel) jEntityPanels.get(alias);
			OlympicRanking en = (OlympicRanking) o;
			en.setOlympics((Olympics)DatabaseManager.loadEntity(Olympics.class, SwingUtils.getValue(p.getOlympics())));
			en.setCountry((Country)DatabaseManager.loadEntity(Country.class, SwingUtils.getValue(p.getCountry())));
			en.setCountGold(StringUtils.toInt(p.getGold().getText()));
			en.setCountSilver(StringUtils.toInt(p.getSilver().getText()));
			en.setCountBronze(StringUtils.toInt(p.getBronze().getText()));
		}
		else if (alias.equalsIgnoreCase(Record.alias)) {
			JRecordPanel p = (JRecordPanel) jEntityPanels.get(alias);
			Record en = (Record) o;
			en.setSport((Sport)DatabaseManager.loadEntity(Sport.class, SwingUtils.getValue(p.getSport())));
			en.setChampionship((Championship)DatabaseManager.loadEntity(Championship.class, SwingUtils.getValue(p.getChampionship())));
			en.setEvent((Event)DatabaseManager.loadEntity(Event.class, SwingUtils.getValue(p.getEvent())));
			en.setSubevent((Event)DatabaseManager.loadEntity(Event.class, SwingUtils.getValue(p.getSubevent())));
			en.setType1(StringUtils.notEmpty(p.getType1().getText()) ? p.getType1().getText() : null);
			en.setType2(StringUtils.notEmpty(p.getType2().getText()) ? p.getType2().getText() : null);
			en.setCity((City)DatabaseManager.loadEntity(City.class, SwingUtils.getValue(p.getCity())));
			en.setLabel(StringUtils.notEmpty(p.getLabel().getText()) ? p.getLabel().getText() : null);
			en.setIdRank1(SwingUtils.getValue(p.getRank1()));
			en.setIdRank2(SwingUtils.getValue(p.getRank2()));
			en.setIdRank3(SwingUtils.getValue(p.getRank3()));
			en.setIdRank4(SwingUtils.getValue(p.getRank4()));
			en.setIdRank5(SwingUtils.getValue(p.getRank5()));
			en.setRecord1(StringUtils.notEmpty(p.getRecord1().getText()) ? p.getRecord1().getText() : null);
			en.setRecord2(StringUtils.notEmpty(p.getRecord2().getText()) ? p.getRecord2().getText() : null);
			en.setRecord3(StringUtils.notEmpty(p.getRecord3().getText()) ? p.getRecord3().getText() : null);
			en.setRecord4(StringUtils.notEmpty(p.getRecord4().getText()) ? p.getRecord4().getText() : null);
			en.setRecord5(StringUtils.notEmpty(p.getRecord5().getText()) ? p.getRecord5().getText() : null);
			en.setDate1(StringUtils.notEmpty(p.getDate1().getText()) ? p.getDate1().getText() : null);
			en.setDate2(StringUtils.notEmpty(p.getDate2().getText()) ? p.getDate2().getText() : null);
			en.setDate3(StringUtils.notEmpty(p.getDate3().getText()) ? p.getDate3().getText() : null);
			en.setDate4(StringUtils.notEmpty(p.getDate4().getText()) ? p.getDate4().getText() : null);
			en.setCounting(p.getCounting().isSelected());
			//en.setIndex(StringUtils.notEmpty(p.getIndex().getText()) ? BigDecimal.valueOf(p.getIndex().getText()) : null);
			en.setExa(p.getExa().getText());
			en.setComment(p.getComment().getText());
		}
		else if (alias.equalsIgnoreCase(RetiredNumber.alias)) {
			JRetiredNumberPanel p = (JRetiredNumberPanel) jEntityPanels.get(alias);
			RetiredNumber en = (RetiredNumber) o;
			en.setLeague((League)DatabaseManager.loadEntity(League.class, SwingUtils.getValue(p.getLeague())));
			en.setTeam((Team)DatabaseManager.loadEntity(Team.class, SwingUtils.getValue(p.getTeam())));
			en.setPerson((Athlete)DatabaseManager.loadEntity(Athlete.class, SwingUtils.getValue(p.getPerson())));
			en.setYear((Year)DatabaseManager.loadEntity(Year.class, SwingUtils.getValue(p.getYear())));
			en.setNumber(StringUtils.toInt(p.getNumber().getText()));
		}
		else if (alias.equalsIgnoreCase(TeamStadium.alias)) {
			JTeamStadiumPanel p = (JTeamStadiumPanel) jEntityPanels.get(alias);
			TeamStadium en = (TeamStadium) o;
			en.setLeague((League)DatabaseManager.loadEntity(League.class, SwingUtils.getValue(p.getLeague())));
			en.setTeam((Team)DatabaseManager.loadEntity(Team.class, SwingUtils.getValue(p.getTeam())));
			en.setComplex((Complex)DatabaseManager.loadEntity(Complex.class, SwingUtils.getValue(p.getComplex())));
			en.setDate1(StringUtils.toInt(p.getDate1().getText()));
			en.setDate2(StringUtils.toInt(p.getDate2().getText()));
			en.setRenamed(p.getRenamed().isSelected());
		}
		else if (alias.equalsIgnoreCase(WinLoss.alias)) {
			JWinLossPanel p = (JWinLossPanel) jEntityPanels.get(alias);
			WinLoss en = (WinLoss) o;
			en.setLeague((League)DatabaseManager.loadEntity(League.class, SwingUtils.getValue(p.getLeague())));
			en.setTeam((Team)DatabaseManager.loadEntity(Team.class, SwingUtils.getValue(p.getTeam())));
			en.setType(String.valueOf(p.getType().getSelectedItem()));
			en.setCountWin(StringUtils.toInt(p.getWin().getText()));
			en.setCountLoss(StringUtils.toInt(p.getLoss().getText()));
			en.setCountTie(StringUtils.toInt(p.getTie().getText()));
			en.setCountOtloss(StringUtils.toInt(p.getOtLoss().getText()));
		}
		o = DatabaseManager.saveEntity(o, contributor);
		String id_ = String.valueOf(c.getMethod("getId").invoke(o, new Object[0]));
		plb.setValue(StringUtils.toInt(id_));
		JMainFrame.refreshPicklist(alias, plb);
		return plb;
	}

	public static void mergeEntities(String alias, Integer id1, Integer id2) throws Exception {
		DatabaseManager.executeSelect("select _merge('" + alias + "', " + id1 + ", " + id2 + ")");
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
				HashMap<String, String> h = new HashMap<>();
				h.put("hibernate.connection.url", "jdbc:postgresql://" + jOptionsDialog.getHost().getText() + "/" + jOptionsDialog.getDatabase().getText());
				h.put("hibernate.connection.username", jOptionsDialog.getLogin().getText());
				h.put("hibernate.connection.password", new String(jPasswordDialog.getPassword().getPassword()));
				h.put("hibernate.show_sql", "false");
				h.put("hibernate.connection.driver_class", "org.postgresql.Driver");
				h.put("hibernate.connection.autocommit", "true");
				h.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
				String sql = "SELECT * FROM _contributor WHERE login = '" + jOptionsDialog.getLogin().getText() + "' AND active = TRUE AND admin = TRUE";
				List<Contributor> lst = (List<Contributor>) DatabaseManager.executeSelect(sql, Contributor.class);
				if (lst == null || lst.isEmpty()) {
					throw new Exception("Your account is not active or has not admin rights.");
				}
				contributor = lst.get(0);
				initAll(jPasswordDialog.getQuickLoading().isSelected());
				jDataPanel.getList().setSelectedIndex(0);
				jDataPanel.valueChanged(new ListSelectionEvent(this, 0, 0, true));
				jPicturesPanel.getList().setSelectedIndex(0);
				jPicturesPanel.valueChanged(new ListSelectionEvent(this, 0, 0, true));
				jExtLinksPanel.valueChanged(new ListSelectionEvent(this, 0, 0, true));
				jUsersPanel.initList();
				jUsersPanel.valueChanged(new ListSelectionEvent(jUsersPanel.getList(), 0, 0, true));
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
			log.log(Level.WARNING, e.getMessage(), e);
			JOptionPane.showMessageDialog(this, "Failed to connect to database. See message below:\r\n\r\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		finally {
			jBottomPanel.getConnectionStatus().set((short)(connected ? 2 : 0), jOptionsDialog.getDatabase().getText());
			if (!connected)
				jConnectInfoLabel.setText("Not connected to database. Click on \"Connect\".");
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

	public static JEditFolderDialog getFolderDialog() {
		return jFolderDialog;
	}

	public static JEditEntityDialog getEntityDialog() {
		return jEntityDialog;
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

	public static HashMap<String, List<PicklistItem>> getPicklists() {
		return hPicklists;
	}

	public static HashMap<String, JAbstractEntityPanel> getEntityPanels() {
		return jEntityPanels;
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