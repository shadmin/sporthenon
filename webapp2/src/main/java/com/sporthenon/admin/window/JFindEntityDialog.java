package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.PicklistItem;
import com.sporthenon.db.entity.AbstractEntity;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.HallOfFame;
import com.sporthenon.db.entity.OlympicRanking;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.RetiredNumber;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.entity.WinLoss;
import com.sporthenon.utils.StringUtils;

public class JFindEntityDialog extends JDialog implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JFindEntityDialog.class.getName());
	
	private JTextField jText = null;
	private JList<PicklistItem> jList = null;
	private String alias = null;
	private JEntityPicklist jPicklist = null;
	private JDialogButtonBar jButtonBar = null;
	
	public JFindEntityDialog(JFrame owner) {
		super(owner);
		initialize();
	}
	
	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(400, 320));
		this.setTitle("Find");
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setContentPane(jContentPane);
		
		BorderLayout layout = new BorderLayout();
		layout.setVgap(5);
		jButtonBar = new JDialogButtonBar(this);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(layout);
		
		jText = new JTextField();
		jText.setSize(new Dimension(0, 21));
		jText.addKeyListener(this);
		jList = new JList<>(new DefaultListModel<>());
		jList.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					actionPerformed(new ActionEvent(jButtonBar.getOk(), 0, "ok"));
				}
			}
		});
		jContentPane.add(jText, BorderLayout.NORTH);
		jContentPane.add(new JScrollPane(jList), BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}
	
	public void open(String alias, JEntityPicklist picklist) {
		this.alias = alias;
		this.jPicklist = picklist;
		jText.setText("");
		jText.requestFocus();
		jList.clearSelection();
		((DefaultListModel<PicklistItem>)jList.getModel()).clear();
		this.setVisible(true);
	}

	public void open(String alias, JEntityPicklist picklist, String value, List<PicklistItem> items) {
		this.alias = alias;
		this.jPicklist = picklist;
		jText.setText(value);
		DefaultListModel<PicklistItem> model = (DefaultListModel<PicklistItem>)jList.getModel();
		model.clear();
		model.addAll(items);
		jList.requestFocus();
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel")) {
			jList.clearSelection();
		}
		if ((e.getActionCommand().equals("ok") && jList.getSelectedIndex() != -1) || e.getActionCommand().equals("cancel")) {
			this.setVisible(false);
		}
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		try {
			DefaultListModel<PicklistItem> model = (DefaultListModel<PicklistItem>)jList.getModel();
			if (e.getKeyCode() == KeyEvent.VK_ENTER && StringUtils.notEmpty(jText.getText())) {
				model.clear();
				if (jPicklist != null) {
					model.addAll(jPicklist.getItemsFromText(jText.getText()));
				}
				else {
					Class<? extends AbstractEntity> c = DatabaseManager.getClassFromAlias(alias);
					String label = null;
					String joins = "";
					if (c.equals(City.class)) {
						label = "T.label || ', ' || CN.code";
						joins = "JOIN country CN ON CN.id = T.id_country";
					}
					else if (c.equals(Complex.class)) {
						label = "label || ' [' || CT.label || ', ' || CN.code || ']'";
						joins = "JOIN city CT ON CT.id = T.id_city JOIN country CN ON CN.id = CT.id_country";
					}
					else if (c.equals(Country.class)) {
						label = "T.label || ', ' || T.code";
					}
					else if (c.equals(Olympics.class)) {
						label = "YR.label || ' - ' || CT.label";
						joins = "JOIN year YR ON YR.id = T.id_year JOIN city CT ON CT.id = T.id_city";
					}
					else if (c.equals(Athlete.class)) {
						label = "T.last_name || ', ' || T.first_name || ' [' || CN.code || ']'";
						joins = "JOIN country CN ON CN.id = T.id_country";
					}
					else if (c.equals(Team.class)) {
						label = "T.label || ', ' || SP.label";
						joins = "JOIN sport SP ON SP.id = T.id_sport";
					}
					else if (c.equals(HallOfFame.class)) {
						label = "LG.label || ' - ' || YR.label";
						joins = "JOIN league LG ON LG.id = T.id_league JOIN year YR ON YR.id = T.id_year";
					}
					else if (c.equals(OlympicRanking.class)) {
						label = "YR.label || ' - ' || CT.label || ' - ' || CN.label";
						joins = "JOIN country CN ON CN.id = T.id_country JOIN olympics OL ON OL.id = T.id_olympics JOIN year YR ON YR.id = OL.id_year JOIN city CT ON CT.id = OL.id_city";
					}
					else if (c.equals(Record.class)) {
						label = "CP.label || ' - ' || EV.label || ' - ' || SE.label || ' - ' || T.type1 || ' - ' || T.type2 || ' - ' || T.label";
						joins = "JOIN championship CP ON CP.id = T.id_championship JOIN event EV ON EV.id = T.id_event JOIN event SE ON SE.id = T.id_subevent";
					}
					else if (c.equals(RetiredNumber.class)) {
						label = "LG.label || ' - ' || TM.label";
						joins = "JOIN league LG ON LG.id = T.id_league JOIN team TM ON TM.id = T.id_team";
					}
					else if (c.equals(TeamStadium.class)) {
						label = "LG.label || ' - ' || TM.label";
						joins = "JOIN league LG ON LG.id = T.id_league JOIN team TM ON TM.id = T.id_team";
					}
					else if (c.equals(WinLoss.class)) {
						label = "LG.label || ' - ' || TM.label";
						joins = "JOIN league LG ON LG.id = T.id_league JOIN team TM ON TM.id = T.id_team";
					}
					label = (label != null ? label : "label") + " || ' [#' || T.id || ']'";
					String pattern = jText.getText().replaceAll("\\*", "%").toLowerCase();
					String sql = "SELECT T.id," + label
						+ " FROM " + DatabaseManager.getTable(c) + " T " + joins
						+ " WHERE " + (pattern.matches("^\\#\\d+") ? "T.id = " + pattern.substring(1) : "LOWER(" + label + ") LIKE '" + pattern + "%'")
					 	+ " ORDER BY 2";
					for (PicklistItem bean : DatabaseManager.getPicklist(sql, null)) {
						model.addElement(bean);
					}
				}
			}
		}
		catch (Exception e_) {
			log.log(Level.WARNING, e_.getMessage(), e_);
		}
	}

	public void keyTyped(KeyEvent e) {
	}
	
	public PicklistItem getSelectedItem() {
		return (jList.getSelectedIndex() != -1 ? (PicklistItem)((DefaultListModel<PicklistItem>)jList.getModel()).getElementAt(jList.getSelectedIndex()) : null);
	}
	
}