package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
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
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.commons.io.IOUtils;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.container.tab.JResultsPanel;
import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Round;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.entity.meta.PersonList;
import com.sporthenon.db.entity.meta.Picture;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class JEditResultDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JEditResultDialog.class.getName());
	private static final int RANK_COUNT = 20;
	
	private JDialogButtonBar jButtonBar = null;
	private JEntityPicklist jYear = null;
	private JEntityPicklist jCity1 = null;
	private JEntityPicklist jComplex1 = null;
	private JEntityPicklist jCity2 = null;
	private JEntityPicklist jComplex2 = null;
	private JTextField jDate1 = null;
	private JTextField jDate2 = null;
	private JCheckBox jDraft = null;
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
	private List<Round> rounds;
	private boolean roundsModified;
	private Set<Integer> roundsDeleted = new HashSet<>();
	private List<Picture> photos;
	private List<Picture> photosAdded = new ArrayList<>();
	private Set<Integer> photosDeleted = new HashSet<>();
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
		
		this.setSize(new Dimension(935, 490));
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
		jCity1.setPreferredSize(new Dimension(250, 21));
		jComplex1 = new JEntityPicklist(this, Complex.alias, true);
		jComplex1.setPreferredSize(new Dimension(350, 21));
		jCity2 = new JEntityPicklist(this, City.alias, true);
		jCity2.setPreferredSize(new Dimension(250, 21));
		jComplex2 = new JEntityPicklist(this, Complex.alias, true);
		jComplex2.setPreferredSize(new Dimension(350, 21));
		jDate1 = new JTextField();
		jDate1.setPreferredSize(new Dimension(72, 21));
		jDate2 = new JTextField();
		jDate2.setPreferredSize(new Dimension(72, 21));
		JCustomButton jToday = new JCustomButton(null, "today.png", null);
		jToday.setMargin(new Insets(0, 0, 0, 0));
		jToday.setToolTipText("Today");
		jToday.setActionCommand("today");
		jToday.addActionListener(this);
		jDraft = new JCheckBox();
		jDraft.setText("Not published (only rounds)");
		jExa = new JTextField();
		jExa.setPreferredSize(new Dimension(60, 21));
		
		JSeparator jSeparator1 = new JSeparator(JSeparator.HORIZONTAL);
		jSeparator1.setPreferredSize(new Dimension(350, 0));
		JSeparator jSeparator2 = new JSeparator(JSeparator.HORIZONTAL);
		jSeparator2.setPreferredSize(new Dimension(180, 0));
		JSeparator jSeparator3 = new JSeparator(JSeparator.HORIZONTAL);
		jSeparator3.setPreferredSize(new Dimension(180, 0));
		JPanel jEventPanel = new JPanel();
		jEventPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 4));
		jEventPanel.setPreferredSize(new Dimension(0, 130));
		jEventPanel.setBorder(BorderFactory.createTitledBorder(null, "Event Info", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jEventPanel.add(new JLabel("Year:"), null);
		jEventPanel.add(jYear, null);
		jEventPanel.add(new JLabel("From:"), null);
		jEventPanel.add(jDate1, null);
		jEventPanel.add(new JLabel("to:"), null);
		jEventPanel.add(jDate2, null);
		jEventPanel.add(jToday, null);
		jEventPanel.add(jDraft, null);
		jEventPanel.add(jSeparator1);
		jEventPanel.add(new JLabel("Complex #1:"), null);
		jEventPanel.add(jComplex1, null);
		jEventPanel.add(new JLabel("City #1:"), null);
		jEventPanel.add(jCity1, null);
		jEventPanel.add(jSeparator2);
		jEventPanel.add(new JLabel("Complex #2:"), null);
		jEventPanel.add(jComplex2, null);
		jEventPanel.add(new JLabel("City #2:"), null);
		jEventPanel.add(jCity2, null);
		jEventPanel.add(jSeparator3);
		jEventPanel.add(new JLabel("Tie:"), null);
		for (int i = 0 ; i < RANK_COUNT ; i++) {
			jExaCheckbox[i] = new JCheckBox(String.valueOf(i + 1));
			jEventPanel.add(jExaCheckbox[i], null);
			jExaCheckbox[i].setActionCommand("exacb-" + (i + 1));
			jExaCheckbox[i].addActionListener(this);
		}
		jEventPanel.add(jExa, null);
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
			labels[i].setPreferredSize(new Dimension(28, 21));
			jRes[i] = new JTextField();
			jRes[i].setPreferredSize(new Dimension(90, 21));
		}
		
		JPanel jStandingsPanel = new JPanel();
		jStandingsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 4));
		jStandingsPanel.setPreferredSize(new Dimension(0, 80));
		jStandingsPanel.setBorder(BorderFactory.createTitledBorder(null, "Final standings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		for (int i = 0 ; i < 10 ; i++) {
			jStandingsPanel.add(labels[i]);
			jStandingsPanel.add(jRanks[i]);
			jStandingsPanel.add(jRes[i]);
			jStandingsPanel.add(labels[i + 10]);
			jStandingsPanel.add(jRanks[i + 10]);
			jStandingsPanel.add(jRes[i + 10]);
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
			jEditPhotosDialog.setPictures(photos);
			jEditPhotosDialog.open();
		}
		else if (cmd.equals("today")) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			jDate2.setText(df.format(Calendar.getInstance().getTime()));
		}
		else if (cmd.equals("comment")) {
			jCommentDialog.setTitle("Edit Comment");
			jCommentDialog.getComment().setText(comment);
			jCommentDialog.setSize(new Dimension(300, 250));
			jCommentDialog.open();
		}
		else if (cmd.equals("extlinks")) {
			jCommentDialog.setTitle("External Links");
			jCommentDialog.getComment().setText(extlinks);
			jCommentDialog.setSize(new Dimension(600, 250));
			jCommentDialog.open();
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
				rs.setDate1(StringUtils.notEmpty(jDate1.getText()) ? jDate1.getText() : null);
				rs.setDate2(StringUtils.notEmpty(jDate2.getText()) ? jDate2.getText() : null);
				rs.setComment(StringUtils.notEmpty(comment) ? comment : null);
				rs.setExa(StringUtils.notEmpty(jExa.getText()) ? jExa.getText() : null);
				rs.setDraft(jDraft.isSelected());
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
						round.setIdResultType(type.getId());
						DatabaseManager.saveEntity(round, cb);
					}
				}
				if (extlinksModified) {
					DatabaseManager.saveExternalLinks(Result.alias, rs.getId(), extlinks);
				}
				if (!photosAdded.isEmpty()) {
					int n = photos.size();
					for (Picture pic : photosAdded) {
						if (pic.getId() != null) {
							continue;
						}
						pic.setEntity(Result.alias);
						pic.setIdItem(rs.getId());
						
						if (!pic.isEmbedded()) {
							try(FileInputStream inputStream = new FileInputStream(new File(pic.getValue()))) {
								String ext = "." + pic.getValue().substring(pic.getValue().lastIndexOf(".") + 1).toLowerCase();
								String fileName = "P" + StringUtils.encode(pic.getEntity() + "-" + id);
								if (n > 0) {
									fileName += n;
								}
								fileName += ext;
								byte[] content = IOUtils.toByteArray(inputStream);
								ImageUtils.uploadImage(null, fileName, content, JMainFrame.getOptionsDialog().getCredentialsFile().getText());
								pic.setValue(fileName);
							}
							n++;
						}
						
						DatabaseManager.saveEntity(pic, null);
					}
				}
				if (!photosDeleted.isEmpty()) {
					for (Picture pic : photosAdded) {
						if (!pic.isEmbedded()) {
							ImageUtils.removeImage(pic.getValue(), JMainFrame.getOptionsDialog().getCredentialsFile().getText());
						}
						DatabaseManager.removeEntity(pic);
					}
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
		this.setVisible(cmd.matches("rounds|photos|comment|extlinks|today|persons.+|exacb.+"));
	}
	
	private Vector<Object> getDataVector(Result rs) {
		Vector<Object> v = new Vector<Object>();
		v.add(rs.getId());
		v.add(SwingUtils.getText(jYear));
		for (int i = 0 ; i < jRanks.length ; i++) {
			v.add(SwingUtils.getText(jRanks[i]) + (StringUtils.notEmpty(jRes[i].getText()) ? " - " + jRes[i].getText() : ""));
		}
		v.add(StringUtils.notEmpty(jDate1.getText()) ? jDate1.getText() : "");
		v.add(StringUtils.notEmpty(jDate2.getText()) ? jDate2.getText() : "");
		v.add(StringUtils.notEmpty(SwingUtils.getText(jComplex1)) ? SwingUtils.getText(jComplex1) : SwingUtils.getText(jCity1));
		v.add(StringUtils.notEmpty(SwingUtils.getText(jComplex2)) ? SwingUtils.getText(jComplex2) : SwingUtils.getText(jCity2));
		return v;
	}
	
	public void clear() {
		jYear.clear();
		jComplex1.clear();
		jCity1.clear();
		jComplex2.clear();
		jCity2.clear();
		jDate1.setText("");
		jDate2.setText("");
		jDraft.setSelected(false);
		for (JEntityPicklist pl : jRanks) {
			pl.clear();
		}
		for (JTextField tf : jRes) {
			tf.setText("");
		}
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
		this.setVisible(true);
		this.extlinksModified = false;
		this.roundsModified = false;
		this.personListModified.clear();
		this.personListDeleted.clear();
		this.roundsDeleted.clear();
		this.photosAdded.clear();
		this.photosDeleted.clear();
		for (int i = 0 ; i < RANK_COUNT ; i++) {
			jExaCheckbox[i].setSelected(false);
		}
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

	public JEntityPicklist getCity2() {
		return jCity2;
	}

	public JEntityPicklist getComplex2() {
		return jComplex2;
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
	
	public JCheckBox getDraft() {
		return jDraft;
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

	public void setPhotos(List<Picture> photos) {
		this.photos = photos;
	}

	public List<Picture> getPhotosAdded() {
		return photosAdded;
	}

	public void setPhotosAdded(List<Picture> photosAdded) {
		this.photosAdded = photosAdded;
	}

	public Set<Integer> getPhotosDeleted() {
		return photosDeleted;
	}

	public void setPhotosDeleted(Set<Integer> photosDeleted) {
		this.photosDeleted = photosDeleted;
	}

}