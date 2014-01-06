package com.sporthenon.updater.window;

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

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.PicklistBean;
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
import com.sporthenon.db.entity.Type;
import com.sporthenon.db.entity.WinLoss;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Member;
import com.sporthenon.updater.component.JConnectionStatus;
import com.sporthenon.updater.component.JQueryStatus;
import com.sporthenon.updater.container.JBottomPanel;
import com.sporthenon.updater.container.JTopPanel;
import com.sporthenon.updater.container.entity.JAbstractEntityPanel;
import com.sporthenon.updater.container.entity.JAthletePanel;
import com.sporthenon.updater.container.entity.JChampionshipPanel;
import com.sporthenon.updater.container.entity.JCityPanel;
import com.sporthenon.updater.container.entity.JComplexPanel;
import com.sporthenon.updater.container.entity.JCountryPanel;
import com.sporthenon.updater.container.entity.JEventPanel;
import com.sporthenon.updater.container.entity.JHallOfFamePanel;
import com.sporthenon.updater.container.entity.JOlympicRankingPanel;
import com.sporthenon.updater.container.entity.JOlympicsPanel;
import com.sporthenon.updater.container.entity.JRecordPanel;
import com.sporthenon.updater.container.entity.JRetiredNumberPanel;
import com.sporthenon.updater.container.entity.JSportPanel;
import com.sporthenon.updater.container.entity.JStatePanel;
import com.sporthenon.updater.container.entity.JTeamPanel;
import com.sporthenon.updater.container.entity.JTeamStadiumPanel;
import com.sporthenon.updater.container.entity.JWinLossPanel;
import com.sporthenon.updater.container.entity.JYearPanel;
import com.sporthenon.updater.container.tab.JDataPanel;
import com.sporthenon.updater.container.tab.JPicturesPanel;
import com.sporthenon.updater.container.tab.JResultsPanel;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;

public class JMainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JTopPanel jTopPanel = null;
	private JBottomPanel jBottomPanel = null;
	private JPanel jTabsPanel = null;
	private JResultsPanel jResultsPanel = null;
	private JDataPanel jDataPanel = null;
	private JPicturesPanel jPicturesPanel = null;
	private JLabel jConnectInfoLabel = null;
	private static JPasswordDialog jPasswordDialog = null;
	private static JEditResultDialog jResultDialog = null;
	private static JEditFolderDialog jFolderDialog = null;
	private static JEditEntityDialog jEntityDialog = null;
	private static JFindEntityDialog jFindDialog = null;
	private static JAddMultipleDialog jAddMultipleDialog = null;
	private static JCharsDialog jCharsDialog = null;
	private static JImportDialog jImportDialog = null;
	private static JQueryDialog jQueryDialog = null;
	private static JOptionsDialog jOptionsDialog = null;
	private static JInfoDialog jInfoDialog = null;

	private static Member member;
	private static HashMap<String, JAbstractEntityPanel> jEntityPanels = null;
	private static HashMap<String, ArrayList<PicklistBean>> hPicklists = new HashMap<String, ArrayList<PicklistBean>>();

	public JMainFrame() {
		super();
		initialize();
		this.setVisible(true);
	}

	private void initialize() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");

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
			this.setSize(new Dimension(900, 650));
			this.setTitle("Sporthenon Update " + ConfigUtils.getProperty("version"));
			this.setContentPane(getJContentPane());
			this.setLocationRelativeTo(null);
			List<Image> lIcons = new ArrayList<Image>();
			for (String size : new String[]{"16", "32", "48", "64", "72", "128", "256"})
				lIcons.add(Toolkit.getDefaultToolkit().getImage(JMainFrame.class.getResource("/com/sporthenon/utils/res/img/updater/icon" + size + ".png")));
			this.setIconImages(lIcons);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
			System.exit(1);
		}
	}

	private void initAll() throws Exception {
		initPicklists();
		jResultsPanel.setTree();
		jResultDialog  = new JEditResultDialog(this);
		jFolderDialog  = new JEditFolderDialog(this);
		jEntityDialog  = new JEditEntityDialog(this);
		jFindDialog  = new JFindEntityDialog(this);
		jAddMultipleDialog = new JAddMultipleDialog(this);
		jCharsDialog = new JCharsDialog(this);
		fillPicklists(null);
	}

	public static void fillPicklists(String alias) {
		if (alias == null || alias.equalsIgnoreCase(Athlete.alias)) {
			SwingUtils.fillPicklist(((JHallOfFamePanel)jEntityPanels.get(HallOfFame.alias)).getPerson(), hPicklists.get(Athlete.alias), null);
			SwingUtils.fillPicklist(((JRetiredNumberPanel)jEntityPanels.get(RetiredNumber.alias)).getPerson(), hPicklists.get(Athlete.alias), null);
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
			SwingUtils.fillPicklist(jResultDialog.getComplex(), hPicklists.get(Complex.alias), null);
			SwingUtils.fillPicklist(((JTeamStadiumPanel)jEntityPanels.get(TeamStadium.alias)).getComplex(), hPicklists.get(Complex.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(City.alias)) {
			SwingUtils.fillPicklist(jResultDialog.getCity(), hPicklists.get(City.alias), null);
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
		if (alias == null || alias.equalsIgnoreCase(Type.alias)) {
			SwingUtils.fillPicklist(((JEventPanel)jEntityPanels.get(Event.alias)).getType(), hPicklists.get(Type.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(Team.alias)) {
			SwingUtils.fillPicklist(((JAthletePanel)jEntityPanels.get(Athlete.alias)).getTeam(), hPicklists.get(Team.alias), null);
			SwingUtils.fillPicklist(((JRetiredNumberPanel)jEntityPanels.get(RetiredNumber.alias)).getTeam(), hPicklists.get(Team.alias), null);
			SwingUtils.fillPicklist(((JTeamStadiumPanel)jEntityPanels.get(TeamStadium.alias)).getTeam(), hPicklists.get(Team.alias), null);
			SwingUtils.fillPicklist(((JWinLossPanel)jEntityPanels.get(WinLoss.alias)).getTeam(), hPicklists.get(Team.alias), null);
		}
		if (alias == null || alias.equalsIgnoreCase(League.alias)) {
			SwingUtils.fillPicklist(((JHallOfFamePanel)jEntityPanels.get(HallOfFame.alias)).getLeague(), hPicklists.get(League.alias), null);
			SwingUtils.fillPicklist(((JRetiredNumberPanel)jEntityPanels.get(RetiredNumber.alias)).getLeague(), hPicklists.get(League.alias), null);
			SwingUtils.fillPicklist(((JTeamStadiumPanel)jEntityPanels.get(TeamStadium.alias)).getLeague(), hPicklists.get(League.alias), null);
			SwingUtils.fillPicklist(((JWinLossPanel)jEntityPanels.get(WinLoss.alias)).getLeague(), hPicklists.get(League.alias), null);
		}
	}

	private void initPicklists() throws Exception {
		Class[] t = {Championship.class, City.class, Complex.class, Country.class, Event.class, League.class, Olympics.class, Athlete.class, Sport.class, State.class, Team.class, Type.class, Year.class};
		for (Class c_ : t) {
			String label = null;
			if (c_.equals(Country.class)) 
				label = "concat(concat(label, ' ['), concat(code, ']'))";
			else if (c_.equals(Event.class)) 
				label = "concat(concat(label, ' ['), concat(type.label, ']'))";
			else if (c_.equals(Olympics.class)) 
				label = "concat(concat(year.label, ' - '), city.label)";

			String sql = null;
			if (c_.equals(City.class)) {
				sql = "select CT.id, CT.label || case when id_state is not null then ', ' || ST.code else '' end || ', ' || CN.code as text ";
				sql += "from \"CITY\" CT left join \"STATE\" ST on CT.id_state=ST.id left join \"COUNTRY\" CN on CT.id_country=CN.id order by text";
			}
			else if (c_.equals(Complex.class)) {
				sql = "select CX.id, CX.label || ' [' || CT.label || case when id_state is not null then ', ' || ST.code else '' end || ', ' || CN.code || ']' as text ";
				sql += "from \"COMPLEX\" CX left join \"CITY\" CT on CX.id_city=CT.id left join \"STATE\" ST on CT.id_state=ST.id left join \"COUNTRY\" CN on CT.id_country=CN.id order by text";
			}
			else if (c_.equals(Athlete.class)) {
				sql = "select PR.id, last_name || ', ' || first_name || case when PR.id_country is not null then ' [' || CN.code || ']' else '' end || case when PR.id_team is not null then ' [' || TM.label || ']' else '' end as text, PR.id_sport ";
				sql += "from \"PERSON\" PR left join \"COUNTRY\" CN on PR.id_country=CN.id left join \"TEAM\" TM on PR.id_team=TM.id order by text";
			}
			else if (c_.equals(Team.class)) {
				sql = "select TM.id, TM.label || case when id_country is not null then ' [' || CN.code || ']' else '' end || case when TM.year1 is not null and TM.year1 <> '' then ' [' || TM.year1 || ']' else '' end || case when TM.year2 is not null and TM.year2 <> '' then ' [' || TM.year2 || ']' else '' end as text, TM.id_sport ";
				sql += "from \"TEAM\" TM left join \"COUNTRY\" CN on TM.id_country=CN.id order by text";
			}
			ArrayList<PicklistBean> lst = new ArrayList<PicklistBean>(sql != null ? DatabaseHelper.getPicklistFromQuery(sql, true) : DatabaseHelper.getEntityPicklist(c_, label, null));
			Field alias = c_.getDeclaredField("alias");
			alias.setAccessible(true);
			hPicklists.put(String.valueOf(alias.get(null)), lst);
		}
	}

	public static void refreshPicklist(String alias, PicklistBean value) {
		if (hPicklists.containsKey(alias)) {
			SwingUtils.insertValue(hPicklists.get(alias), value);
			fillPicklists(alias);			
		}
	}

	public static PicklistBean saveEntity(String alias, Integer id) throws Exception {
		Class c = DatabaseHelper.getClassFromAlias(alias);
		Object o = (id != null ? DatabaseHelper.loadEntity(c, id) : c.newInstance());
		PicklistBean plb = new PicklistBean();
		if (alias.equalsIgnoreCase(Athlete.alias)) {
			JAthletePanel p = (JAthletePanel) jEntityPanels.get(alias);
			Athlete en = (Athlete) o;
			en.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, SwingUtils.getValue(p.getSport())));
			en.setTeam((Team)DatabaseHelper.loadEntity(Team.class, SwingUtils.getValue(p.getTeam())));
			en.setCountry((Country)DatabaseHelper.loadEntity(Country.class, SwingUtils.getValue(p.getCountry())));
			en.setLink(StringUtils.notEmpty(p.getLink().getText()) ? new Integer(p.getLink().getText()) : null);
			en.setLastName(p.getLastName().getText());
			en.setFirstName(p.getFirstName().getText());
			plb.setParam(String.valueOf(en.getSport().getId())); plb.setText(en.getLastName() + ", " + en.getFirstName() + (en.getCountry() != null ? " [" + en.getCountry().getCode() + "]" : "") + (en.getTeam() != null ? " [" + en.getTeam().getLabel() + "]" : ""));
			if (en.getLink() != null && en.getLink() > 0) {
				try {
					Athlete a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, en.getLink());
					p.setLinkLabel("Link: [" + a.getLastName() + (StringUtils.notEmpty(a.getFirstName()) ? ", " + a.getFirstName() : "") + (a.getCountry() != null ? ", " + a.getCountry().getCode() : "") + (a.getTeam() != null ? ", " + a.getTeam().getLabel() : "") + "]");
					DatabaseHelper.executeUpdate("UPDATE \"PERSON\" SET LINK=0 WHERE ID=" + en.getLink());
				}
				catch (Exception e) {
					Logger.getLogger("sh").error(e.getMessage());
				}
			}
		}
		else if (alias.equalsIgnoreCase(Championship.alias)) {
			JChampionshipPanel p = (JChampionshipPanel) jEntityPanels.get(alias);
			Championship en = (Championship) o;
			en.setLabel(p.getLabel().getText());
			en.setWebsite(p.getWebsite().getText());
			en.setComment(p.getComment().getText());
			en.setIndex(StringUtils.notEmpty(p.getIndex().getText()) ? Integer.parseInt(p.getIndex().getText()) : Integer.MAX_VALUE);
			en.setInactive(p.getInactive().isSelected());
			plb.setText(en.getLabel());
		}
		else if (alias.equalsIgnoreCase(City.alias)) {
			JCityPanel p = (JCityPanel) jEntityPanels.get(alias);
			City en = (City) o;
			en.setLabel(p.getLabel().getText());
			en.setState((State)DatabaseHelper.loadEntity(State.class, SwingUtils.getValue(p.getState())));
			en.setCountry((Country)DatabaseHelper.loadEntity(Country.class, SwingUtils.getValue(p.getCountry())));
			plb.setText(en.getLabel() + ", " + en.getCountry().getCode());
		}
		else if (alias.equalsIgnoreCase(Complex.alias)) {
			JComplexPanel p = (JComplexPanel) jEntityPanels.get(alias);
			Complex en = (Complex) o;
			en.setLabel(p.getLabel().getText());
			en.setCity((City)DatabaseHelper.loadEntity(City.class, SwingUtils.getValue(p.getCity())));
			plb.setText(en.getLabel() + " [" + en.getCity().getLabel() + ", " + en.getCity().getCountry().getCode() + "]");
		}
		else if (alias.equalsIgnoreCase(Country.alias)) {
			JCountryPanel p = (JCountryPanel) jEntityPanels.get(alias);
			Country en = (Country) o;
			en.setLabel(p.getLabel().getText());
			en.setCode(p.getCode().getText());
			plb.setText(en.getLabel() + " [" + en.getCode() + "]");
		}
		else if (alias.equalsIgnoreCase(Event.alias)) {
			JEventPanel p = (JEventPanel) jEntityPanels.get(alias);
			Event en = (Event) o;
			en.setLabel(p.getLabel().getText());
			en.setType((Type)DatabaseHelper.loadEntity(Type.class, SwingUtils.getValue(p.getType())));
			en.setWebsite(p.getWebsite().getText());
			en.setComment(p.getComment().getText());
			en.setIndex(StringUtils.notEmpty(p.getIndex().getText()) ? Integer.parseInt(p.getIndex().getText()) : Integer.MAX_VALUE);
			en.setInactive(p.getInactive().isSelected());
			plb.setText(en.getLabel());
		}
		else if (alias.equalsIgnoreCase(Olympics.alias)) {
			JOlympicsPanel p = (JOlympicsPanel) jEntityPanels.get(alias);
			Olympics en = (Olympics) o;
			en.setYear((Year)DatabaseHelper.loadEntity(Year.class, SwingUtils.getValue(p.getYear())));
			en.setCity((City)DatabaseHelper.loadEntity(City.class, SwingUtils.getValue(p.getCity())));
			en.setType(new Integer(p.getType().getText()));
			en.setCountSport(new Integer(p.getSports().getText()));
			en.setCountEvent(new Integer(p.getEvents().getText()));
			en.setCountPerson(new Integer(p.getPersons().getText()));
			en.setCountCountry(new Integer(p.getCountries().getText()));
			en.setDate1(p.getStart().getText());
			en.setDate2(p.getEnd().getText());
			plb.setText(en.getYear().getLabel() + " - " + en.getCity().getLabel());
		}
		else if (alias.equalsIgnoreCase(Sport.alias)) {
			JSportPanel p = (JSportPanel) jEntityPanels.get(alias);
			Sport en = (Sport) o;
			en.setLabel(p.getLabel().getText());
			en.setType(new Integer(p.getType().getText()));
			en.setWebsite(p.getWebsite().getText());
			plb.setText(en.getLabel());
		}
		else if (alias.equalsIgnoreCase(State.alias)) {
			JStatePanel p = (JStatePanel) jEntityPanels.get(alias);
			State en = (State) o;
			en.setLabel(p.getLabel().getText());
			en.setCode(p.getCode().getText());
			en.setCapital(p.getCapital().getText());
			plb.setText(en.getLabel());
		}
		else if (alias.equalsIgnoreCase(Team.alias)) {
			JTeamPanel p = (JTeamPanel) jEntityPanels.get(alias);
			Team en = (Team) o;
			en.setLabel(p.getLabel().getText());
			en.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, SwingUtils.getValue(p.getSport())));
			en.setCountry((Country)DatabaseHelper.loadEntity(Country.class, SwingUtils.getValue(p.getCountry())));
			en.setConference(p.getConference().getText());
			en.setDivision(p.getDivision().getText());
			en.setComment(p.getComment().getText());
			en.setYear1(p.getYear1().getText());
			en.setYear2(p.getYear2().getText());
			en.setLink(StringUtils.notEmpty(p.getLink().getText()) ? new Integer(p.getLink().getText()) : null);
			en.setInactive(p.getInactive().isSelected());
			plb.setParam(String.valueOf(en.getSport().getId())); plb.setText(en.getLabel() + (en.getCountry() != null ? " [" + en.getCountry().getCode() + "]" : ""));
			if (en.getLink() != null && en.getLink() > 0) {
				try {
					Team a = (Team) DatabaseHelper.loadEntity(Team.class, en.getLink());
					p.setLinkLabel("Link: [" + a.getLabel() + "]");
					DatabaseHelper.executeUpdate("UPDATE \"TEAM\" SET LINK=0 WHERE ID=" + en.getLink());
				}
				catch (Exception e) {
					Logger.getLogger("sh").error(e.getMessage());
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
			en.setLeague((League)DatabaseHelper.loadEntity(League.class, SwingUtils.getValue(p.getLeague())));
			en.setYear((Year)DatabaseHelper.loadEntity(Year.class, SwingUtils.getValue(p.getYear())));
			en.setPerson((Athlete)DatabaseHelper.loadEntity(Athlete.class, SwingUtils.getValue(p.getPerson())));
			en.setPosition(p.getPosition().getText());
		}
		else if (alias.equalsIgnoreCase(OlympicRanking.alias)) {
			JOlympicRankingPanel p = (JOlympicRankingPanel) jEntityPanels.get(alias);
			OlympicRanking en = (OlympicRanking) o;
			en.setOlympics((Olympics)DatabaseHelper.loadEntity(Olympics.class, SwingUtils.getValue(p.getOlympics())));
			en.setCountry((Country)DatabaseHelper.loadEntity(Country.class, SwingUtils.getValue(p.getCountry())));
			en.setCountGold(new Integer(p.getGold().getText()));
			en.setCountSilver(new Integer(p.getSilver().getText()));
			en.setCountBronze(new Integer(p.getBronze().getText()));
		}
		else if (alias.equalsIgnoreCase(Record.alias)) {
			JRecordPanel p = (JRecordPanel) jEntityPanels.get(alias);
			Record en = (Record) o;
			en.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, SwingUtils.getValue(p.getSport())));
			en.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, SwingUtils.getValue(p.getChampionship())));
			en.setEvent((Event)DatabaseHelper.loadEntity(Event.class, SwingUtils.getValue(p.getEvent())));
			en.setSubevent((Event)DatabaseHelper.loadEntity(Event.class, SwingUtils.getValue(p.getSubevent())));
			en.setType1(StringUtils.notEmpty(p.getType1().getText()) ? p.getType1().getText() : null);
			en.setType2(StringUtils.notEmpty(p.getType2().getText()) ? p.getType2().getText() : null);
			en.setCity((City)DatabaseHelper.loadEntity(City.class, SwingUtils.getValue(p.getCity())));
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
			en.setIndex(StringUtils.notEmpty(p.getIndex().getText()) ? new Float(p.getIndex().getText()) : null);
			en.setExa(p.getExa().getText());
			en.setComment(p.getComment().getText());
		}
		else if (alias.equalsIgnoreCase(RetiredNumber.alias)) {
			JRetiredNumberPanel p = (JRetiredNumberPanel) jEntityPanels.get(alias);
			RetiredNumber en = (RetiredNumber) o;
			en.setLeague((League)DatabaseHelper.loadEntity(League.class, SwingUtils.getValue(p.getLeague())));
			en.setTeam((Team)DatabaseHelper.loadEntity(Team.class, SwingUtils.getValue(p.getTeam())));
			en.setPerson((Athlete)DatabaseHelper.loadEntity(Athlete.class, SwingUtils.getValue(p.getPerson())));
			en.setYear((Year)DatabaseHelper.loadEntity(Year.class, SwingUtils.getValue(p.getYear())));
			en.setNumber(new Integer(p.getNumber().getText()));
		}
		else if (alias.equalsIgnoreCase(TeamStadium.alias)) {
			JTeamStadiumPanel p = (JTeamStadiumPanel) jEntityPanels.get(alias);
			TeamStadium en = (TeamStadium) o;
			en.setLeague((League)DatabaseHelper.loadEntity(League.class, SwingUtils.getValue(p.getLeague())));
			en.setTeam((Team)DatabaseHelper.loadEntity(Team.class, SwingUtils.getValue(p.getTeam())));
			en.setComplex((Complex)DatabaseHelper.loadEntity(Complex.class, SwingUtils.getValue(p.getComplex())));
			en.setDate1(new Integer(p.getDate1().getText()));
			en.setDate2(StringUtils.notEmpty(p.getDate2().getText()) ? new Integer(p.getDate2().getText()) : 0);
			en.setRenamed(p.getRenamed().isSelected());
		}
		else if (alias.equalsIgnoreCase(WinLoss.alias)) {
			JWinLossPanel p = (JWinLossPanel) jEntityPanels.get(alias);
			WinLoss en = (WinLoss) o;
			en.setLeague((League)DatabaseHelper.loadEntity(League.class, SwingUtils.getValue(p.getLeague())));
			en.setTeam((Team)DatabaseHelper.loadEntity(Team.class, SwingUtils.getValue(p.getTeam())));
			en.setType(String.valueOf(p.getType().getSelectedItem()));
			en.setCountWin(StringUtils.notEmpty(p.getWin().getText()) ? new Integer(p.getWin().getText()) : null);
			en.setCountLoss(StringUtils.notEmpty(p.getLoss().getText()) ? new Integer(p.getLoss().getText()) : null);
			en.setCountTie(StringUtils.notEmpty(p.getTie().getText()) ? new Integer(p.getTie().getText()) : null);
			en.setCountOtloss(StringUtils.notEmpty(p.getOtLoss().getText()) ? new Integer(p.getOtLoss().getText()) : null);
			en.setAverage(p.getAverage().getText());
		}
		o = DatabaseHelper.saveEntity(o, member);
		String id_ = String.valueOf(c.getMethod("getId").invoke(o, new Object[0]));
		plb.setValue(new Integer(id_));
		JMainFrame.refreshPicklist(alias, plb);
		return plb;
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
		JPanel p = new JPanel();
		CardLayout cardLayout = new CardLayout();
		p.setLayout(cardLayout);
		p.add(emptyPanel, "empty");
		p.add(jResultsPanel, "results");
		p.add(jDataPanel, "data");
		p.add(jPicturesPanel, "pictures");
		return p;
	}
	
	public void changeTabPanel(String name) {
		((CardLayout) jTabsPanel.getLayout()).show(jTabsPanel, name);
	}

	public boolean connectCallback(boolean connected) {
		boolean err = false;
		try {
			if (connected) {
				jPasswordDialog.open();
				jConnectInfoLabel.setText("Connecting to database...");
				jBottomPanel.getQueryStatus().showProgress();
				jBottomPanel.getConnectionStatus().set((short)1, null);
				HashMap<String, String> h = new HashMap();
				h.put("hibernate.connection.url", "jdbc:postgresql://" + jOptionsDialog.getHost().getText() + "/" + jOptionsDialog.getDatabase().getText());
				h.put("hibernate.connection.username", jOptionsDialog.getLogin().getText());
				h.put("hibernate.connection.password", new String(jPasswordDialog.getPassword().getPassword()));
				h.put("hibernate.show_sql", "false");
				h.put("hibernate.connection.driver_class", "org.postgresql.Driver");
				h.put("hibernate.connection.autocommit", "false");
				h.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
				DatabaseHelper.setFactory(h, "standalone");
				String hql = "from Member where login='" + jOptionsDialog.getLogin().getText() + "' and active=true";
				ArrayList<Member> lst = (ArrayList<Member>) DatabaseHelper.execute(hql);
				member = lst.get(0);
				initAll();
				jDataPanel.getList().setSelectedIndex(0);
				jDataPanel.valueChanged(new ListSelectionEvent(this, 0, 0, true));
				jPicturesPanel.getList().setSelectedIndex(0);
				jPicturesPanel.valueChanged(new ListSelectionEvent(this, 0, 0, true));
			}
			else {
				DatabaseHelper.unsetFactory();
				member = null;
				changeTabPanel("empty");
				jTopPanel.getResultsButton().setSelected(false);
				jTopPanel.getDataButton().setSelected(false);
				jTopPanel.getPicturesButton().setSelected(false);
				jTopPanel.getResultsButton().setEnabled(false);
				jTopPanel.getDataButton().setEnabled(false);
				jTopPanel.getPicturesButton().setEnabled(false);
				jTopPanel.getImportButton().setEnabled(false);
				jTopPanel.getQueryButton().setEnabled(false);
				jBottomPanel.getQueryStatus().set((short)-1, null);
			}
		}
		catch (Exception e) {
			err = true;
			connected = false;
			Logger.getLogger("sh").error(e.getMessage(), e);
			JOptionPane.showMessageDialog(this, "Failed to connect to database. See message below:\r\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

	public static JAddMultipleDialog getAddMultipleDialog() {
		return jAddMultipleDialog;
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

	public static HashMap<String, ArrayList<PicklistBean>> getPicklists() {
		return hPicklists;
	}

	public static HashMap<String, JAbstractEntityPanel> getEntityPanels() {
		return jEntityPanels;
	}

	public static Member getMember() {
		return member;
	}

}