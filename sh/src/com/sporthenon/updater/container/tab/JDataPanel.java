package com.sporthenon.updater.container.tab;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
import com.sporthenon.updater.component.JCustomButton;
import com.sporthenon.updater.component.JQueryStatus;
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
import com.sporthenon.updater.window.JFindEntityDialog;
import com.sporthenon.updater.window.JMainFrame;
import com.sporthenon.updater.window.JMergeEntityDialog;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class JDataPanel extends JSplitPane implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	private JList jList = null;
	private Container jContainer = null;
	private JScrollPane jScrollPane = null;
	private String alias = Championship.alias;
	private String currentId;
	private JQueryStatus jQueryStatus = null;
	
	public JDataPanel(JMainFrame parent) {
		this.jQueryStatus = parent.getQueryStatus();
		initialize();
	}
	
	public JList getList() {
		return jList;
	}
	
	private void initialize() {
		initList();
		JScrollPane leftPanel = new JScrollPane(jList);
		leftPanel.setPreferredSize(new Dimension(150, 0));
		leftPanel.setBorder(BorderFactory.createEmptyBorder());
		leftPanel.setMinimumSize(new Dimension(0, 0));
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(getButtonPanel(), BorderLayout.SOUTH);
		jScrollPane = new JScrollPane(getEntityPanel());
		jScrollPane.setBorder(BorderFactory.createEmptyBorder());
		rightPanel.add(jScrollPane, BorderLayout.CENTER);
		this.setTopComponent(leftPanel);
		this.setBottomComponent(rightPanel);
	}
	
	private void initList() {
		Vector<String> v = new Vector<String>();
		v.add("Athlete");
		v.add("Championship");
		v.add("City");
		v.add("Complex");
		v.add("Country");
		v.add("Event");
		v.add("Olympics");
		v.add("Sport");
		v.add("State");
		v.add("Team");
		v.add("Year");
		v.add("-----------------------------------");
		v.add("Hall of Fame");
		v.add("Olympic Medals");
		v.add("Records");
		v.add("Retired Numbers");
		v.add("Team Stadiums");
		v.add("Win/Loss");
		jList = new JList(v);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setSelectedIndex(0);
		jList.addListSelectionListener(this);
	}
	
	private JPanel getButtonPanel() {
		JPanel leftPanel = new JPanel();
		JCustomButton jFirstButton = new JCustomButton(null, "first.png", "First");
		jFirstButton.addActionListener(this);
		jFirstButton.setActionCommand("first");
		jFirstButton.setMnemonic(KeyEvent.VK_PAGE_UP);
		JCustomButton jPreviousButton = new JCustomButton(null, "previous.png", "Previous");
		jPreviousButton.addActionListener(this);
		jPreviousButton.setActionCommand("previous");
		jPreviousButton.setMnemonic(KeyEvent.VK_LEFT);
		JCustomButton jFindButton = new JCustomButton(null, "find.png", "Find");
		jFindButton.addActionListener(this);
		jFindButton.setActionCommand("find");
		jFindButton.setMnemonic(KeyEvent.VK_F);
		JCustomButton jNextButton = new JCustomButton(null, "next.png", "Next");
		jNextButton.addActionListener(this);
		jNextButton.setActionCommand("next");
		jNextButton.setMnemonic(KeyEvent.VK_RIGHT);
		JCustomButton jLastButton = new JCustomButton(null, "last.png", "Last");
		jLastButton.addActionListener(this);
		jLastButton.setActionCommand("last");
		jLastButton.setMnemonic(KeyEvent.VK_PAGE_DOWN);
		leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
		JCustomButton jUrlUpdateButton = new JCustomButton("URL Update", "url.png", null);
		jUrlUpdateButton.addActionListener(this);
		jUrlUpdateButton.setActionCommand("urlupdate");
		leftPanel.add(jFirstButton, null);
		leftPanel.add(jPreviousButton, null);
		leftPanel.add(jFindButton, null);
		leftPanel.add(jNextButton, null);
		leftPanel.add(jLastButton, null);
		leftPanel.add(jUrlUpdateButton, null);

		JPanel rightPanel = new JPanel();
		JCustomButton jAddButton = new JCustomButton(null, "add.png", "New");
		jAddButton.addActionListener(this);
		jAddButton.setActionCommand("new");
		jAddButton.setMnemonic(KeyEvent.VK_N);
		JCustomButton jSaveButton = new JCustomButton(null, "save.png", "Save");
		jSaveButton.addActionListener(this);
		jSaveButton.setActionCommand("save");
		jSaveButton.setMnemonic(KeyEvent.VK_S);
		JCustomButton jCopyButton = new JCustomButton(null, "copy.png", "Copy");
		jCopyButton.addActionListener(this);
		jCopyButton.setActionCommand("copy");
		jCopyButton.setMnemonic(KeyEvent.VK_C);
		JCustomButton jMergeButton = new JCustomButton(null, "merge.png", "Merge");
		jMergeButton.addActionListener(this);
		jMergeButton.setActionCommand("merge");
		jMergeButton.setMnemonic(KeyEvent.VK_M);
		JCustomButton jRemoveButton = new JCustomButton(null, "remove.png", "Remove");
		jRemoveButton.addActionListener(this);
		jRemoveButton.setActionCommand("remove");
		jRemoveButton.setMnemonic(KeyEvent.VK_R);
		rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 1));
		rightPanel.add(jAddButton, null);
		rightPanel.add(jSaveButton, null);
		rightPanel.add(jRemoveButton, null);
		rightPanel.add(jCopyButton, null);
		rightPanel.add(jMergeButton, null);
		
		JPanel p = new JPanel(new BorderLayout(0, 0));
		p.setPreferredSize(new Dimension(0, 26));
		p.add(leftPanel, BorderLayout.WEST);
		p.add(rightPanel, BorderLayout.EAST);
		
		return p;
	}

	private JPanel getEntityPanel() {
		HashMap<String, JAbstractEntityPanel> panels = JMainFrame.getEntityPanels();
		jContainer = new Container();
		jContainer.setLayout(new CardLayout());
		for (String key : panels.keySet())
			jContainer.add(panels.get(key), key);

		JPanel jMainPanel = new JPanel();
		jMainPanel.setBorder(BorderFactory.createEmptyBorder());
		jMainPanel.add(jContainer, null);
		return jMainPanel;
	}
	
	public void actionPerformed(ActionEvent e) {
		JAbstractEntityPanel panel = JMainFrame.getEntityPanels().get(alias);
		if (e.getActionCommand().matches("first|previous|next|last")) {
			try {
				HashMap<String, Short> hLocs = new HashMap<String, Short>();
				hLocs.put("first", DatabaseHelper.FIRST);
				hLocs.put("previous", DatabaseHelper.PREVIOUS);
				hLocs.put("next", DatabaseHelper.NEXT);
				hLocs.put("last", DatabaseHelper.LAST);
				Class c = DatabaseHelper.getClassFromAlias(alias);
				Object data = DatabaseHelper.move(c, currentId, hLocs.get(e.getActionCommand()));
				if (data != null) {
					currentId = String.valueOf(c.getMethod("getId").invoke(data, new Object[0]));
					load(data);
				}
			}
			catch (Exception e_) {
				Logger.getLogger("sh").error(e_.getMessage(), e_);
			}
		}
		else if (e.getActionCommand().equals("find")) {
			try {
				JFindEntityDialog dlg = JMainFrame.getFindDialog();
				dlg.open(alias, null);
				if (dlg.getSelectedItem() != null) {
					Class c = DatabaseHelper.getClassFromAlias(alias);
					Object data = DatabaseHelper.loadEntity(c, dlg.getSelectedItem().getValue());
					currentId = String.valueOf(c.getMethod("getId").invoke(data, new Object[0]));
					load(data);
				}
			}
			catch (Exception e_) {
				Logger.getLogger("sh").error(e_.getMessage(), e_);
			}
		}
		else if (e.getActionCommand().equals("urlupdate")) {
			JMainFrame.getUrlUpdateDialog().open();
		}
		else if (e.getActionCommand().equals("new")) {
			panel.clear();
			panel.focus();
		}
		else if (e.getActionCommand().equals("copy")) {
			panel.setId("");
			panel.focus();
		}
		else if (e.getActionCommand().equals("save")) {
			boolean err = false;
			String msg = null;
			try {
				String id = panel.getId().getText();
				PicklistBean plb = JMainFrame.saveEntity(alias, StringUtils.notEmpty(id) ? new Integer(id) : null);
				msg = ResourceUtils.getText("entity." + alias.toUpperCase() + ".1", "en") + " #" + plb.getValue() + " successfully " + (StringUtils.notEmpty(id) ? "updated" : "created") + ".";
				panel.setId(String.valueOf(plb.getValue()));
				currentId = panel.getId().getText();
			}
			catch (Exception e_) {
				err = true;
				msg = e_.getMessage();
				Logger.getLogger("sh").error(e_.getMessage(), e_);
			}
			finally {
				jQueryStatus.set(err ? JQueryStatus.FAILURE : JQueryStatus.SUCCESS, msg);
			}
		}
		else if (e.getActionCommand().equals("remove")) {
			String id = panel.getId().getText();
			Object[] options = {"Yes", "No"};
			int n = JOptionPane.showOptionDialog(this, "Remove object #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (n != 0)
				return;
			String msg = null;
			boolean err = false;
			try {
				DatabaseHelper.removeEntity(DatabaseHelper.loadEntity(DatabaseHelper.getClassFromAlias(alias), id));
				msg = "Object #" + id + " has been successfully removed.";
				actionPerformed(new ActionEvent(this, 0, "previous"));
			}
			catch (Exception e_) {
				err = true;
				msg = e_.getMessage();
				Logger.getLogger("sh").error(e_.getMessage(), e_);
			}
			finally {
				jQueryStatus.set(err ? JQueryStatus.FAILURE : JQueryStatus.SUCCESS, msg);
			}
		}
		else if (e.getActionCommand().equals("merge")) {
			String msg = null;
			boolean err = false;
			try {
				JFindEntityDialog dlg = JMainFrame.getFindDialog();
				dlg.open(alias, null);
				if (dlg.getSelectedItem() != null) {
					Class c = DatabaseHelper.getClassFromAlias(alias);
					Object data1 = DatabaseHelper.loadEntity(c, currentId);
					Object data2 = DatabaseHelper.loadEntity(c, dlg.getSelectedItem().getValue());
					String id1 = String.valueOf(c.getMethod("getId").invoke(data1, new Object[0]));
					String id2 = String.valueOf(c.getMethod("getId").invoke(data2, new Object[0]));
					JMergeEntityDialog dlg_ = JMainFrame.getMergeDialog();
					dlg_.getlEntity1().setText(data1.toString());
					dlg_.getlEntity2().setText(data2.toString());
					dlg_.open(alias, Integer.parseInt(id1), Integer.parseInt(id2));
					if (dlg_.getAlias() != null) {
						JMainFrame.mergeEntities(dlg_.getAlias(), dlg_.getIdEntity1(), dlg_.getIdEntity2());
						msg = "Entities #" + id1 + " and #" + id2 + " merged successfully.";
					}
				}
			}
			catch (Exception e_) {
				err = true;
				msg = "An error occured while merging - " + e_.getMessage();
				Logger.getLogger("sh").error(e_.getMessage(), e_);
			}
			finally {
				if (msg != null)
					jQueryStatus.set(err ? JQueryStatus.FAILURE : JQueryStatus.SUCCESS, msg);
			}
		}
	}
	
	private void load(Object o) {
		JAbstractEntityPanel panel = JMainFrame.getEntityPanels().get(alias);
		panel.setId(currentId);
		if (o instanceof Athlete) {
			Athlete pr = (Athlete) o;
			JAthletePanel p = (JAthletePanel) panel;
			p.setTeam(pr.getTeam() != null ? pr.getTeam().getId() : null);
			p.setCountry(pr.getCountry() != null ? pr.getCountry().getId() : null);
			p.setSport(pr.getSport() != null ? pr.getSport().getId() : null);
			p.setLink(pr.getLink() != null ? String.valueOf(pr.getLink()) : null);
			p.setLastName(pr.getLastName());
			p.setFirstName(StringUtils.notEmpty(pr.getFirstName()) ? pr.getFirstName() : "");
			p.setUrlWiki(pr.getUrlWiki());
			p.setUrlOlyref(pr.getUrlOlyref());
			p.setUrlBktref(pr.getUrlBktref());
			p.setUrlBbref(pr.getUrlBbref());
			p.setUrlFtref(pr.getUrlFtref());
			p.setUrlHkref(pr.getUrlHkref());
			p.setLinkLabel("Link:");
			if (pr.getLink() != null && pr.getLink() > 0) {
				try {
					Athlete a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, pr.getLink());
					p.setLinkLabel("Link: [" + a.getLastName() + (StringUtils.notEmpty(a.getFirstName()) ? ", " + a.getFirstName() : "") + (a.getCountry() != null ? ", " + a.getCountry().getCode() : "") + (a.getTeam() != null ? ", " + a.getTeam().getLabel() : "") + "]");
				}
				catch (Exception e) {
					Logger.getLogger("sh").error(e.getMessage());
				}
			}
		}
		else if (o instanceof Championship) {
			Championship cp = (Championship) o;
			JChampionshipPanel p = (JChampionshipPanel) panel;
			p.setLabel(cp.getLabel());
			p.setLabelFR(cp.getLabelFr());
			p.setWebsite(cp.getWebsite());
			p.setIndex(cp.getIndex() != null ? String.valueOf(cp.getIndex()) : null);
			p.setUrlWiki(cp.getUrlWiki());
		}
		else if (o instanceof City) {
			City ct = (City) o;
			JCityPanel p = (JCityPanel) panel;
			p.setLabel(ct.getLabel());
			p.setLabelFR(ct.getLabelFr());
			p.setState(ct.getState() != null ? ct.getState().getId() : null);
			p.setCountry(ct.getCountry() != null ? ct.getCountry().getId() : null);
			p.setUrlWiki(ct.getUrlWiki());
		}
		else if (o instanceof Complex) {
			Complex cx = (Complex) o;
			JComplexPanel p = (JComplexPanel) panel;
			p.setLabel(cx.getLabel());
			p.setLabelFR(cx.getLabelFr());
			p.setCity(cx.getCity() != null ? cx.getCity().getId() : null);
			p.setUrlWiki(cx.getUrlWiki());
		}
		else if (o instanceof Country) {
			Country cn = (Country) o;
			JCountryPanel p = (JCountryPanel) panel;
			p.setLabel(cn.getLabel());
			p.setLabelFR(cn.getLabelFr());
			p.setCode(cn.getCode());
			p.setUrlWiki(cn.getUrlWiki());
			p.setUrlOlyref(cn.getUrlOlyref());
		}
		else if (o instanceof Event) {
			Event ev = (Event) o;
			JEventPanel p = (JEventPanel) panel;
			p.setLabel(ev.getLabel());
			p.setLabelFR(ev.getLabelFr());
			p.setType(ev.getType() != null ? ev.getType().getId() : null);
			p.setWebsite(ev.getWebsite());
			p.setIndex(ev.getIndex() != null ? String.valueOf(ev.getIndex()) : null);
			p.setUrlWiki(ev.getUrlWiki());
		}
		else if (o instanceof Olympics) {
			Olympics ol = (Olympics) o;
			JOlympicsPanel p = (JOlympicsPanel) panel;
			p.setYear(ol.getYear() != null ? ol.getYear().getId() : null);
			p.setCity(ol.getCity() != null ? ol.getCity().getId() : null);
			p.setType(String.valueOf(ol.getType()));
			p.setStart(ol.getDate1());
			p.setEnd(ol.getDate2());
			p.setSports(String.valueOf(ol.getCountSport()));
			p.setEvents(String.valueOf(ol.getCountEvent()));
			p.setCountries(String.valueOf(ol.getCountCountry()));
			p.setPersons(String.valueOf(ol.getCountPerson()));
			p.setUrlWiki(ol.getUrlWiki());
			p.setUrlOlyref(ol.getUrlOlyref());
		}
		else if (o instanceof Sport) {
			Sport sp = (Sport) o;
			JSportPanel p = (JSportPanel) panel;
			p.setLabel(sp.getLabel());
			p.setLabelFR(sp.getLabelFr());
			p.setWebsite(sp.getWebsite());
			p.setType(String.valueOf(sp.getType()));
			p.setUrlWiki(sp.getUrlWiki());
			p.setUrlOlyref(sp.getUrlOlyref());
			p.setWikiPattern(sp.getWikiPattern());
		}
		else if (o instanceof State) {
			State st = (State) o;
			JStatePanel p = (JStatePanel) panel;
			p.setLabel(st.getLabel());
			p.setLabelFR(st.getLabelFr());
			p.setCode(st.getCode());
			p.setCapital(st.getCapital());
			p.setUrlWiki(st.getUrlWiki());
		}
		else if (o instanceof Team) {
			Team tm = (Team) o;
			JTeamPanel p = (JTeamPanel) panel;
			p.setCountry(tm.getCountry() != null ? tm.getCountry().getId() : null);
			p.setSport(tm.getSport() != null ? tm.getSport().getId() : null);
			p.setLabel(tm.getLabel());
			p.setConference(StringUtils.notEmpty(tm.getConference()) ? tm.getConference() : "");
			p.setDivision(StringUtils.notEmpty(tm.getDivision()) ? tm.getDivision() : "");
			p.setYear1(tm.getYear1());
			p.setYear2(tm.getYear2());
			p.setComment(StringUtils.notEmpty(tm.getComment()) ? tm.getComment() : "");
			p.setLink(tm.getLink() != null ? String.valueOf(tm.getLink()) : null);
			p.setUrlWiki(tm.getUrlWiki());
			p.setUrlBktref(tm.getUrlBktref());
			p.setUrlBbref(tm.getUrlBbref());
			p.setUrlFtref(tm.getUrlFtref());
			p.setUrlHkref(tm.getUrlHkref());
			p.setInactive(tm.getInactive());
			p.setLinkLabel("Link:");
			if (tm.getLink() != null && tm.getLink() > 0) {
				try {
					Team a = (Team) DatabaseHelper.loadEntity(Team.class, tm.getLink());
					p.setLinkLabel("Link: [" + a.getLabel() + "]");
				}
				catch (Exception e) {
					Logger.getLogger("sh").error(e.getMessage());
				}
			}
		}
		else if (o instanceof Year) {
			Year yr = (Year) o;
			JYearPanel p = (JYearPanel) panel;
			p.setLabel(yr.getLabel());
		}
		else if (o instanceof HallOfFame) {
			HallOfFame hf = (HallOfFame) o;
			JHallOfFamePanel p = (JHallOfFamePanel) panel;
			int leagueId = hf.getLeague().getId();
			SwingUtils.fillPicklist(p.getPerson(), JMainFrame.getPicklists().get(Athlete.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
			p.setLeague(hf.getLeague().getId());
			p.setYear(hf.getYear().getId());
			p.setPerson(hf.getPerson().getId());
			p.setPosition(hf.getPosition());
		}
		else if (o instanceof OlympicRanking) {
			OlympicRanking or = (OlympicRanking) o;
			JOlympicRankingPanel p = (JOlympicRankingPanel) panel;
			p.setOlympics(or.getOlympics().getId());
			p.setCountry(or.getCountry().getId());
			p.setGold(String.valueOf(or.getCountGold()));
			p.setSilver(String.valueOf(or.getCountSilver()));
			p.setBronze(String.valueOf(or.getCountBronze()));
		}
		else if (o instanceof Record) {
			Record rc = (Record) o;
			JRecordPanel p = (JRecordPanel) panel;
			int n = (rc.getSubevent() != null ? rc.getSubevent().getType().getNumber() : rc.getEvent().getType().getNumber());
			SwingUtils.fillPicklist(p.getRank1(), JMainFrame.getPicklists().get(n == 99 ? Country.alias : (n == 50 ? Team.alias : Athlete.alias)), rc.getSport().getId());
			SwingUtils.fillPicklist(p.getRank2(), JMainFrame.getPicklists().get(n == 99 ? Country.alias : (n == 50 ? Team.alias : Athlete.alias)), rc.getSport().getId());
			SwingUtils.fillPicklist(p.getRank3(), JMainFrame.getPicklists().get(n == 99 ? Country.alias : (n == 50 ? Team.alias : Athlete.alias)), rc.getSport().getId());
			SwingUtils.fillPicklist(p.getRank4(), JMainFrame.getPicklists().get(n == 99 ? Country.alias : (n == 50 ? Team.alias : Athlete.alias)), rc.getSport().getId());
			SwingUtils.fillPicklist(p.getRank5(), JMainFrame.getPicklists().get(n == 99 ? Country.alias : (n == 50 ? Team.alias : Athlete.alias)), rc.getSport().getId());			
			p.setSport(rc.getSport().getId());
			p.setChampionship(rc.getChampionship() != null ? rc.getChampionship().getId() : null);
			p.setEvent(rc.getEvent() != null ? rc.getEvent().getId() : null);
			p.setSubevent(rc.getSubevent() != null ? rc.getSubevent().getId() : null);
			p.setType1(rc.getType1());
			p.setType2(rc.getType2());
			p.setCity(rc.getCity() != null ? rc.getCity().getId() : null);
			p.setLabel(rc.getLabel());
			p.setRank1(rc.getIdRank1());
			p.setRank2(rc.getIdRank2());
			p.setRank3(rc.getIdRank3());
			p.setRank4(rc.getIdRank4());
			p.setRank5(rc.getIdRank5());
			p.setRecord1(rc.getRecord1());
			p.setRecord2(rc.getRecord2());
			p.setRecord3(rc.getRecord3());
			p.setRecord4(rc.getRecord4());
			p.setRecord5(rc.getRecord5());
			p.setDate1(rc.getDate1());
			p.setDate2(rc.getDate2());
			p.setDate3(rc.getDate3());
			p.setDate4(rc.getDate4());
			p.setDate5(rc.getDate5());
			p.setCounting(rc.getCounting());
			p.setIndex(String.valueOf(rc.getIndex()));
			p.setComment(rc.getComment());
		}
		else if (o instanceof RetiredNumber) {
			RetiredNumber rn = (RetiredNumber) o;
			JRetiredNumberPanel p = (JRetiredNumberPanel) panel;
			int leagueId = rn.getLeague().getId();
			SwingUtils.fillPicklist(p.getTeam(), JMainFrame.getPicklists().get(Team.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
			SwingUtils.fillPicklist(p.getPerson(), JMainFrame.getPicklists().get(Athlete.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
			p.setLeague(rn.getLeague().getId());
			p.setTeam(rn.getTeam().getId());
			p.setPerson(rn.getPerson().getId());
			p.setYear(rn.getYear() != null ? rn.getYear().getId() : null);
			p.setNumber(String.valueOf(rn.getNumber()));
		}
		else if (o instanceof TeamStadium) {
			TeamStadium ts = (TeamStadium) o;
			JTeamStadiumPanel p = (JTeamStadiumPanel) panel;
			int leagueId = ts.getLeague().getId();
			SwingUtils.fillPicklist(p.getTeam(), JMainFrame.getPicklists().get(Team.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
			p.setLeague(ts.getLeague().getId());
			p.setTeam(ts.getTeam().getId());
			p.setComplex(ts.getComplex().getId());
			p.setDate1(String.valueOf(ts.getDate1()));
			p.setDate2(String.valueOf(ts.getDate2()));
			p.setRenamed(ts.getRenamed() != null);
		}
		else if (o instanceof WinLoss) {
			WinLoss wl = (WinLoss) o;
			JWinLossPanel p = (JWinLossPanel) panel;
			int leagueId = wl.getLeague().getId();
			SwingUtils.fillPicklist(p.getTeam(), JMainFrame.getPicklists().get(Team.alias), leagueId == 1 ? 23 : (leagueId == 2 ? 24 : (leagueId == 3 ? 25 : 26)));
			p.setLeague(wl.getLeague().getId());
			p.setTeam(wl.getTeam().getId());
			p.setType(wl.getType());
			p.setWin(wl.getCountWin() != null ? String.valueOf(wl.getCountWin()) : "");
			p.setLoss(wl.getCountLoss() != null ? String.valueOf(wl.getCountLoss()) : "");
			p.setTie(wl.getCountTie() != null ? String.valueOf(wl.getCountTie()) : "");
			p.setOtLoss(wl.getCountOtloss() != null ? String.valueOf(wl.getCountOtloss()) : "");
			p.setAverage(wl.getAverage() != null ? wl.getAverage() : "");
		}
		jQueryStatus.clear();
	}

	public void valueChanged(ListSelectionEvent e) {
		int index = 0;
		if (e != null && e.getSource() instanceof JList)
			index = ((JList)e.getSource()).getSelectedIndex();
		switch (index) {
			case 0: alias = Athlete.alias; break;
			case 1: alias = Championship.alias; break;
			case 2: alias = City.alias; break;
			case 3: alias = Complex.alias; break;
			case 4: alias = Country.alias; break;
			case 5: alias = Event.alias; break;
			case 6: alias = Olympics.alias; break;
			case 7: alias = Sport.alias; break;
			case 8: alias = State.alias; break;
			case 9: alias = Team.alias; break;
			case 10: alias = Year.alias; break;
			case 12: alias = HallOfFame.alias; break;
			case 13: alias = OlympicRanking.alias; break;
			case 14: alias = Record.alias; break;
			case 15: alias = RetiredNumber.alias; break;
			case 16: alias = TeamStadium.alias; break;
			case 17: alias = WinLoss.alias; break;
		}
		jContainer.add(JMainFrame.getEntityPanels().get(alias), alias);
		((CardLayout) jContainer.getLayout()).show(jContainer, alias);
		jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMinimum());
		actionPerformed(new ActionEvent(this, 0, "last"));
	}
	
}