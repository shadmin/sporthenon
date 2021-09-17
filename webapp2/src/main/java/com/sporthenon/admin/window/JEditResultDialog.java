package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.container.tab.JResultsPanel;
import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Round;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.entity.meta.PersonList;
import com.sporthenon.db.entity.meta.Picture;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class JEditResultDialog extends JDialog implements ActionListener, FocusListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JEditResultDialog.class.getName());
	private static final int RANK_COUNT = 20;
	
	private JDialogButtonBar jButtonBar = null;
	private JEntityPicklist jYear = null;
	private JEntityPicklist jCity1 = null;
	private JEntityPicklist jComplex1 = null;
	private JEntityPicklist jCountry1 = null;
	private JEntityPicklist jCity2 = null;
	private JEntityPicklist jComplex2 = null;
	private JEntityPicklist jCountry2 = null;
	private JTextField jDate1 = null;
	private JTextField jDate2 = null;
	private JCheckBox jInProgress = null;
	private JTextField jExa = null;
	private JCheckBox[] jExaCheckbox = new JCheckBox[RANK_COUNT];
	private JEntityPicklist[] jRanks = new JEntityPicklist[RANK_COUNT];
	private JTextField[] jRes = new JTextField[RANK_COUNT];
	private JCommentDialog jCommentDialog = null;
	private JEditPersonListDialog jEditPersonListDialog = null;
	private JEditRoundsDialog jEditRoundsDialog = null;
	private JEditPhotosDialog jEditPhotosDialog = null;
	
	private JResultsPanel parent;
	private Integer id;
	private short mode;
	private com.sporthenon.db.entity.Type type;
	private String comment;
	private Map<Integer, List<PersonList>> mapPersonList;
	private Set<Integer> personListModified = new HashSet<>();
	private Set<Integer> personListDeleted = new HashSet<>();
	private String extlinks;
	private boolean extlinksModified;
	private List<Round> rounds = new ArrayList<>();
	private boolean roundsModified;
	private Set<Integer> roundsDeleted = new HashSet<>();
	private List<Picture> photos = new ArrayList<>();
	private String alias;
	private Object param;
	private Integer idSport;
	public static final short NEW = 1;
	public static final short EDIT = 2;
	public static final short COPY = 3;
	
	public JEditResultDialog(JFrame owner) {
		super(owner);
		initialize();
	}

	private void initialize() {
		JPanel jContentPane = new JPanel();
		BorderLayout layout = new BorderLayout();
		layout.setVgap(5);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(layout);
		jButtonBar = new JDialogButtonBar(this);
		jButtonBar.getOptional().setIcon("rounds.png");
		jButtonBar.getOptional().setVisible(true);
		jButtonBar.getOptional().setActionCommand("rounds");
		jButtonBar.getOptional().addActionListener(this);
		jButtonBar.getOptional2().setIcon("comment.png");
		jButtonBar.getOptional2().setVisible(true);
		jButtonBar.getOptional2().setActionCommand("comment");
		jButtonBar.getOptional2().addActionListener(this);
		jButtonBar.getOptional3().setIcon("weblinks.png");
		jButtonBar.getOptional3().setVisible(true);
		jButtonBar.getOptional3().setActionCommand("extlinks");
		jButtonBar.getOptional3().addActionListener(this);
		jButtonBar.getOptional4().setText("Photos");
		jButtonBar.getOptional4().setIcon("photos.png");
		jButtonBar.getOptional4().setVisible(true);
		jButtonBar.getOptional4().setActionCommand("photos");
		jButtonBar.getOptional4().addActionListener(this);
		jContentPane.add(getEventPanel(), BorderLayout.NORTH);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
		jContentPane.add(getStandingsPanel(), BorderLayout.CENTER);
		
		jCommentDialog = new JCommentDialog(this);
		jEditPersonListDialog = new JEditPersonListDialog(this);
		jEditRoundsDialog = new JEditRoundsDialog(this);
		jEditPhotosDialog = new JEditPhotosDialog(this);
		
		this.setSize(new Dimension(935, 510));
        this.setContentPane(jContentPane);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModal(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
	}

	private JPanel getEventPanel() {
		jYear = new JEntityPicklist(this, Year.alias, false);
		jYear.setPreferredSize(new Dimension(130, 21));
		jCity1 = new JEntityPicklist(this, City.alias, true);
		jCity1.setPreferredSize(new Dimension(220, 21));
		jComplex1 = new JEntityPicklist(this, Complex.alias, true);
		jComplex1.setPreferredSize(new Dimension(300, 21));
		jCountry1 = new JEntityPicklist(this, Country.alias, true);
		jCountry1.setPreferredSize(new Dimension(190, 21));
		jCity2 = new JEntityPicklist(this, City.alias, true);
		jCity2.setPreferredSize(new Dimension(220, 21));
		jComplex2 = new JEntityPicklist(this, Complex.alias, true);
		jComplex2.setPreferredSize(new Dimension(300, 21));
		jCountry2 = new JEntityPicklist(this, Country.alias, true);
		jCountry2.setPreferredSize(new Dimension(190, 21));
		jDate1 = new JTextField();
		jDate1.setPreferredSize(new Dimension(72, 21));
		jDate1.addFocusListener(this);
		jDate2 = new JTextField();
		jDate2.setPreferredSize(new Dimension(72, 21));
		jDate2.addFocusListener(this);
		JCustomButton jDateBtn1 = new JCustomButton("Today", "date.png", null);
		jDateBtn1.setActionCommand("today");
		jDateBtn1.addActionListener(this);
		JCustomButton jDateBtn2 = new JCustomButton("Yesterday", "date.png", null);
		jDateBtn2.setActionCommand("yesterday");
		jDateBtn2.addActionListener(this);
		JCustomButton jDateBtn3 = new JCustomButton("2 days ago", "date.png", null);
		jDateBtn3.setActionCommand("2dago");
		jDateBtn3.addActionListener(this);
		JCustomButton jDateBtn4 = new JCustomButton("3 days ago", "date.png", null);
		jDateBtn4.setActionCommand("3dago");
		jDateBtn4.addActionListener(this);
		jInProgress = new JCheckBox();
		jInProgress.setText("Event in progress");
		jExa = new JTextField();
		jExa.setPreferredSize(new Dimension(60, 21));
		jExa.addFocusListener(this);
		
		JPanel jEventPanel = new JPanel();
		jEventPanel.setLayout(new BoxLayout(jEventPanel, BoxLayout.Y_AXIS));
		jEventPanel.setBorder(BorderFactory.createTitledBorder(null, "Event Info", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 4));
		panel.add(new JLabel("Year:"), null);
		panel.add(jYear, null);
		panel.add(new JLabel("From:"), null);
		panel.add(jDate1, null);
		panel.add(new JLabel("to:"), null);
		panel.add(jDate2, null);
		panel.add(jDateBtn1, null);
		panel.add(jDateBtn2, null);
		panel.add(jDateBtn3, null);
		panel.add(jDateBtn4, null);
		panel.add(jInProgress, null);
		jEventPanel.add(panel);
		
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 4));
		panel.add(new JLabel("Complex #1:"), null);
		panel.add(jComplex1, null);
		panel.add(new JLabel("City #1:"), null);
		panel.add(jCity1, null);
		panel.add(new JLabel("Country #1:"), null);
		panel.add(jCountry1, null);
		jEventPanel.add(panel);
		
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 4));
		panel.add(new JLabel("Complex #2:"), null);
		panel.add(jComplex2, null);
		panel.add(new JLabel("City #2:"), null);
		panel.add(jCity2, null);
		panel.add(new JLabel("Country #2:"), null);
		panel.add(jCountry2, null);
		jEventPanel.add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 4));
		panel.add(new JLabel("Tie:"), null);
		for (int i = 0 ; i < RANK_COUNT ; i++) {
			jExaCheckbox[i] = new JCheckBox(String.valueOf(i + 1));
			panel.add(jExaCheckbox[i], null);
			jExaCheckbox[i].setActionCommand("exacb-" + (i + 1));
			jExaCheckbox[i].addActionListener(this);
		}
		panel.add(jExa, null);
		jEventPanel.add(panel);
		
		return jEventPanel;
	}

	private JPanel getStandingsPanel() {
		JLabel[] labels = new JLabel[RANK_COUNT];
		for (int i = 0 ; i < jRanks.length ; i++) {
			jRanks[i] = new JEntityPicklist(this, "EN", true);
			jRanks[i].setPreferredSize(new Dimension(320, 21));
			JCustomButton optionalBtn = jRanks[i].getOptionalButton();
			optionalBtn.setText(null);
			optionalBtn.setIcon("persons.png");
			optionalBtn.setVisible(true);
			optionalBtn.setActionCommand("persons-" + (i + 1));
			optionalBtn.addActionListener(this);
			jRanks[i].getButtonPanel2().setPreferredSize(new Dimension(69, 0));
			labels[i] = new JLabel(ResourceUtils.getText("rank." + (i + 1), ResourceUtils.LGDEFAULT) + ":");
			labels[i].setPreferredSize(new Dimension(30, 21));
			jRes[i] = new JTextField();
			jRes[i].setPreferredSize(new Dimension(90, 21));
			jRes[i].addFocusListener(this);
		}

		JPanel jStandingsPanel = new JPanel();
		jStandingsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
		jStandingsPanel.setBorder(BorderFactory.createTitledBorder(null, "Final standings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		for (int col = 1 ; col <= 6 ; col++) {
			JPanel columnPanel = new JPanel();
			columnPanel.setLayout(new BoxLayout(columnPanel, BoxLayout.Y_AXIS));
			int index1 = (col <= 3 ? 0 : 10);
			int index2 = (col <= 3 ? 10 : 20);
			for (int i = index1 ; i < index2 ; i++) {
				if (col == 1 || col == 4) {
					columnPanel.add(labels[i]);	
				}
				if (col == 2 || col == 5) {
					columnPanel.add(jRanks[i]);	
				}
				if (col == 3 || col == 6) {
					columnPanel.add(jRes[i]);	
				}
				JPanel sep = new JPanel();
				sep.setPreferredSize(new Dimension(0, 5));
				columnPanel.add(sep);
			}
			jStandingsPanel.add(columnPanel);
		}

		return jStandingsPanel;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.matches("\\D\\D\\-(add|find)")) {
			SwingUtils.openAddFindDialog(e, type.getNumber());
			return;
		}
		else if (cmd.startsWith("persons")) {
			int rank = Integer.valueOf(cmd.replace("persons-", ""));
			jEditPersonListDialog.open(rank, idSport, mapPersonList.get(rank));
		}
		else if (cmd.equals("rounds")) {
			jEditRoundsDialog.open(alias, param, rounds);
			jButtonBar.getOptional().setText("Rounds" + (!rounds.isEmpty() ? " (" + rounds.size() + ")" : ""));
		}
		else if (cmd.equals("photos")) {
			jEditPhotosDialog.setAlias(Result.alias);
			jEditPhotosDialog.setId(this.id);
			jEditPhotosDialog.getPhotos().clear();
			for (Picture pic: photos) {
				jEditPhotosDialog.getPhotos().put(pic.getId(), pic);
			}
			jEditPhotosDialog.open();
			jButtonBar.getOptional4().setText("Photos" + (!photos.isEmpty() ? " (" + photos.size() + ")" : ""));
		}
		else if (cmd.matches("today|yesterday|2dago|3dago")) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			if (cmd.equals("yesterday")) {
				cal.add(Calendar.DAY_OF_YEAR, -1);
			}
			if (cmd.equals("2dago")) {
				cal.add(Calendar.DAY_OF_YEAR, -2);
			}
			if (cmd.equals("3dago")) {
				cal.add(Calendar.DAY_OF_YEAR, -3);
			}
			jDate2.setText(df.format(cal.getTime()));
		}
		else if (cmd.equals("comment")) {
			jCommentDialog.setTitle("Edit Comment");
			jCommentDialog.getComment().setText(comment);
			jCommentDialog.setSize(new Dimension(300, 250));
			jCommentDialog.open();
			jButtonBar.getOptional2().setText("Comment" + (StringUtils.notEmpty(comment) ? " (1)" : ""));
		}
		else if (cmd.equals("extlinks")) {
			jCommentDialog.setTitle("External Links");
			jCommentDialog.getComment().setText(extlinks);
			jCommentDialog.setSize(new Dimension(600, 250));
			jCommentDialog.open();
			jButtonBar.getOptional3().setText("Ext. Links" + (StringUtils.notEmpty(extlinks) ? " (" + extlinks.split("\n").length + ")" : ""));
		}
		else if (cmd.matches("exacb.*")) {
			int min = 100;
			int max = -1;
			for (int i = 0 ; i < RANK_COUNT ; i++) {
				if (jExaCheckbox[i].isSelected() && i < min) {
					min = i;
				}
				if (jExaCheckbox[i].isSelected() && i > max) {
					max = i;
				}
			}
			if (max > -1) {
				for (int i = min ; i <= max ; i++) {
					jExaCheckbox[i].setSelected(true);
				}
				jExa.setText(String.valueOf(min != max ? (min + 1) + "-" + (max + 1) : (min + 1)));
			}
			else {
				jExa.setText(null);
			}
		}
		else if (cmd.equals("ok")) {
			if (!checkErrors()) {
				return;
			}
			Contributor cb = JMainFrame.getContributor();
			Result rs = null;
			String msg = null;
			boolean err = false;
			try {
				rs = (Result)(mode == EDIT ? DatabaseManager.loadEntity(Result.class, id) : new Result());
				rs.setIdSport(JResultsPanel.getIdSport());
				rs.setIdChampionship(JResultsPanel.getIdChampionship());
				rs.setIdEvent(JResultsPanel.getIdEvent());
				rs.setIdSubevent(JResultsPanel.getIdSubevent());
				rs.setIdSubevent2(JResultsPanel.getIdSubevent2());
				rs.setIdYear(SwingUtils.getValue(jYear));
				rs.setIdCity1(SwingUtils.getValue(jCity1));
				rs.setIdCity2(SwingUtils.getValue(jCity2));
				rs.setIdComplex1(SwingUtils.getValue(jComplex1));
				rs.setIdComplex2(SwingUtils.getValue(jComplex2));
				rs.setIdCountry1(SwingUtils.getValue(jCountry1));
				rs.setIdCountry2(SwingUtils.getValue(jCountry2));
				rs.setDate1(StringUtils.notEmpty(jDate1.getText()) ? jDate1.getText() : null);
				rs.setDate2(StringUtils.notEmpty(jDate2.getText()) ? jDate2.getText() : null);
				rs.setComment(StringUtils.notEmpty(comment) ? comment : null);
				rs.setExa(StringUtils.notEmpty(jExa.getText()) ? jExa.getText() : null);
				rs.setInProgress(jInProgress.isSelected());
				for (int i = 0 ; i < jRanks.length ; i++) {
					Integer id = SwingUtils.getValue(jRanks[i]);
					Result.class.getMethod("setIdRank" + (i + 1), Integer.class).invoke(rs, id);
					Result.class.getMethod("setResult" + (i + 1), String.class).invoke(rs, StringUtils.notEmpty(jRes[i].getText()) ? jRes[i].getText() : null);
				}
				rs = (Result) DatabaseManager.saveEntity(rs, cb);
				if (!personListDeleted.isEmpty()) {
					String sql = "DELETE FROM _person_list WHERE id in (" + StringUtils.repeat("?", personListDeleted.size(), ",") + ")";
					DatabaseManager.executeUpdate(sql, personListDeleted);
				}
				if (!personListModified.isEmpty()) {
					for (int j = 1 ; j <= jRanks.length ; j++) {
						if (personListModified.contains(j)) {
							List<PersonList> personLists = mapPersonList.get(j);
							for (PersonList pl : personLists) {
								pl.setIdResult(rs.getId());
								DatabaseManager.saveEntity(pl, null);
							}
						}
					}
				}
				if (!roundsDeleted.isEmpty()) {
					String sql = "DELETE FROM round WHERE id in (" + StringUtils.repeat("?", roundsDeleted.size(), ",") + ")";
					DatabaseManager.executeUpdate(sql, roundsDeleted);
				}
				if (roundsModified) {
					for (Round round : rounds) {
						round.setIdResult(rs.getId());
						round.setIdResultType(type.getNumber());
						DatabaseManager.saveEntity(round, cb);
					}
				}
				if (extlinksModified) {
					DatabaseManager.saveExternalLinks(Result.alias, rs.getId(), extlinks);
				}
				msg = "Result #" + rs.getId() + " has been successfully " + (mode == EDIT ? "updated" : "created") + ".";
			}
			catch (Exception e_) {
				err = true;
				msg = e_.getMessage();
				log.log(Level.WARNING, e_.getMessage(), e_);
			}
			finally {
				parent.resultCallback(mode, getDataVector(rs), msg, err);
			}
		}
		this.setVisible(cmd.matches("rounds|photos|comment|extlinks|today|yesterday|2dago|3dago|persons.+|exacb.+"));
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		((JTextField)e.getSource()).selectAll();
	}

	@Override
	public void focusLost(FocusEvent e) {
	}
	
	private boolean checkErrors() {
		boolean err = false;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		try {
			jDate1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			if (StringUtils.notEmpty(jDate1.getText())) {
				if (!jDate1.getText().matches(StringUtils.PATTERN_DATE)) {
					throw new ParseException(null, 0);
				}
				df.parse(jDate1.getText());	
			}
		}
		catch (ParseException e) {
			jDate1.setBorder(BorderFactory.createLineBorder(Color.RED));
			JOptionPane.showMessageDialog(this, "The value of field 'From' is invalid (should be DD/MM/YYYY).", "Error", JOptionPane.ERROR_MESSAGE);
			err = true;
		}
		try {
			jDate2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			if (StringUtils.notEmpty(jDate2.getText())) {
				if (!jDate2.getText().matches(StringUtils.PATTERN_DATE)) {
					throw new ParseException(null, 0);
				}
				df.parse(jDate2.getText());	
			}
		}
		catch (ParseException e) {
			jDate2.setBorder(BorderFactory.createLineBorder(Color.RED));
			JOptionPane.showMessageDialog(this, "The value of field 'To' is invalid (should be DD/MM/YYYY).", "Error", JOptionPane.ERROR_MESSAGE);
			err = true;
		}
		return !err;
	}
	
	private Vector<Object> getDataVector(Result rs) {
		Vector<Object> v = new Vector<Object>();
		v.add(rs.getId());
		v.add(SwingUtils.getText(jYear));
		for (int i = 0 ; i < 3 ; i++) {
			v.add(SwingUtils.getText(jRanks[i]) + (StringUtils.notEmpty(jRes[i].getText()) ? " - " + jRes[i].getText() : ""));
		}
		v.add(StringUtils.notEmpty(jDate1.getText()) ? jDate1.getText() : "");
		v.add(StringUtils.notEmpty(jDate2.getText()) ? jDate2.getText() : "");
		v.add(StringUtils.notEmpty(SwingUtils.getText(jComplex1)) ? SwingUtils.getText(jComplex1) : (StringUtils.notEmpty(SwingUtils.getText(jCity1)) ? SwingUtils.getText(jCity1) : SwingUtils.getText(jCountry1)));
		v.add(StringUtils.notEmpty(SwingUtils.getText(jComplex2)) ? SwingUtils.getText(jComplex2) : (StringUtils.notEmpty(SwingUtils.getText(jCity2)) ? SwingUtils.getText(jCity2) : SwingUtils.getText(jCountry2)));
		v.add(jInProgress.isSelected() ? "X" : "");
		v.add(StringUtils.notEmpty(comment) ? "X" : "");
		v.add(StringUtils.notEmpty(jExa.getText()) ? "X" : "");
		v.add("Now");
		return v;
	}
	
	public void clear() {
		jYear.clear();
		jComplex1.clear();
		jCity1.clear();
		jCountry1.clear();
		jComplex2.clear();
		jCity2.clear();
		jCountry2.clear();
		jDate1.setText("");
		jDate2.setText("");
		jInProgress.setSelected(false);
		rounds.clear();
		photos.clear();
		comment = "";
		extlinks = "";
		for (JEntityPicklist pl : jRanks) {
			pl.clear();
		}
		for (JTextField tf : jRes) {
			tf.setText("");
		}
		for (int i = 0 ; i < RANK_COUNT ; i++) {
			jExaCheckbox[i].setSelected(false);
		}
		jExa.setText("");
	}
	
	public void open(JResultsPanel parent, Integer id, Integer drawId, short mode, com.sporthenon.db.entity.Type type) {
		this.parent = parent;
		this.id = id;
		this.mode = mode;
		this.type = type;
		this.setTitle(mode == NEW || mode == COPY ? "New Result" : "Edit Result #" + id);
		jButtonBar.getOptional().setText("Rounds" + (!rounds.isEmpty() ? " (" + rounds.size() + ")" : ""));
		jButtonBar.getOptional2().setText("Comment" + (StringUtils.notEmpty(comment) ? " (1)" : ""));
		jButtonBar.getOptional3().setText("Ext. Links" + (StringUtils.notEmpty(extlinks) ? " (" + extlinks.split("\r\n").length + ")" : ""));
		jButtonBar.getOptional4().setText("Photos" + (!photos.isEmpty() ? " (" + photos.size() + ")" : ""));
		jButtonBar.getOptional4().setEnabled(this.id != null && this.id > 0 && mode != JEditResultDialog.COPY);
		jDate1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		jDate2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		this.extlinksModified = false;
		this.roundsModified = false;
		this.personListModified.clear();
		this.personListDeleted.clear();
		this.roundsDeleted.clear();
		if (mode == JEditResultDialog.COPY) {
			if (StringUtils.notEmpty(this.extlinks)) {
				this.extlinksModified = true;
			}
			if (!this.rounds.isEmpty()) {
				for (Round round : this.rounds) {
					round.setId(null);
					round.setIdResult(null);
				}
				this.roundsModified = true;
			}
		}
		this.setVisible(true);
	}
	
	public Integer getId() {
		return id;
	}
	
	public JEntityPicklist getYear() {
		return jYear;
	}

	public JEntityPicklist getCity1() {
		return jCity1;
	}

	public JEntityPicklist getComplex1() {
		return jComplex1;
	}

	public JEntityPicklist getCountry1() {
		return jCountry1;
	}
	
	public JEntityPicklist getCity2() {
		return jCity2;
	}

	public JEntityPicklist getComplex2() {
		return jComplex2;
	}
	
	public JEntityPicklist getCountry2() {
		return jCountry2;
	}
	
	public JEntityPicklist[] getRanks() {
		return jRanks;
	}

	public JTextField getDate1() {
		return jDate1;
	}

	public JTextField getDate2() {
		return jDate2;
	}

	public JTextField getExa() {
		return jExa;
	}
	
	public JCheckBox[] getExaCheckbox() {
		return jExaCheckbox;
	}

	public JTextField[] getRes() {
		return jRes;
	}
	
	public JCheckBox getInProgress() {
		return jInProgress;
	}
	
	public com.sporthenon.db.entity.Type getResultType() {
		return type;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public void setExtLinks(String extlinks) {
		this.extlinks = extlinks;
	}
	
	public void setExtLinksModified(boolean extlinksModified) {
		this.extlinksModified = extlinksModified;
	}

	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}

	public void setRoundsModified(boolean roundsModified) {
		this.roundsModified = roundsModified;
	}
	
	public Set<Integer> getRoundsDeleted() {
		return roundsDeleted;
	}

	public void setRoundsDeleted(Set<Integer> roundsDeleted) {
		this.roundsDeleted = roundsDeleted;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setParam(Object param) {
		this.param = param;
	}
	
	public void setIdSport(Integer idSport) {
		this.idSport = idSport;
	}

	public Set<Integer> getPersonListModified() {
		return personListModified;
	}
	
	public Set<Integer> getPersonListDeleted() {
		return personListDeleted;
	}

	public void setPersonListDeleted(Set<Integer> personListDeleted) {
		this.personListDeleted = personListDeleted;
	}

	public Map<Integer, List<PersonList>> getMapPersonList() {
		return mapPersonList;
	}

	public void setMapPersonList(Map<Integer, List<PersonList>> mapPersonList) {
		this.mapPersonList = mapPersonList;
	}

	public List<Picture> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Picture> photos) {
		this.photos = photos;
	}

}