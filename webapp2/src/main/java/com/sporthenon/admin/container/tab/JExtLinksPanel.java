package com.sporthenon.admin.container.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JQueryStatus;
import com.sporthenon.admin.window.JMainFrame;
import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.meta.ExternalLink;
import com.sporthenon.utils.StringUtils;

public class JExtLinksPanel extends JSplitPane implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JExtLinksPanel.class.getName());
	
	private JList<String> jList = null;
	private JTextField jIdRange = null;
	private JTextField jSearch = null;
	private JScrollPane jScrollPane;
	private JTable jLinkTable = null;
	private JCheckBox jChecked = null;
	private JQueryStatus jQueryStatus = null;
	private String alias = Championship.alias;

	public JExtLinksPanel(JMainFrame parent) {
		this.jQueryStatus = parent.getQueryStatus();
		initialize();
	}

	public JList<String> getList() {
		return jList;
	}

	private void initialize() {
		initList();
		JScrollPane leftPanel = new JScrollPane(jList);
		leftPanel.setPreferredSize(new Dimension(150, 0));
		leftPanel.setBorder(BorderFactory.createEmptyBorder());
		leftPanel.setMinimumSize(new Dimension(100, 0));
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 5));
		rightPanel.setLayout(new BorderLayout(10, 10));
		rightPanel.add(getBottomPanel(), BorderLayout.SOUTH);
		rightPanel.add(getTablePanel(), BorderLayout.CENTER);
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
		v.add("Result");
		v.add("Sport");
		v.add("State");
		v.add("Team");
		jList = new JList<>(v);
		jList.setName("mainlist");
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setSelectedIndex(0);
		jList.addListSelectionListener(this);
	}

	private JPanel getBottomPanel() {
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 1));
		leftPanel.add(new JLabel("ID Range : "));
		jIdRange = new JTextField("1-500");
		jIdRange.setPreferredSize(new Dimension(70, 20));
		leftPanel.add(jIdRange);
		leftPanel.add(new JLabel("Search : "));
		jSearch = new JTextField();
		jSearch.setPreferredSize(new Dimension(120, 20));
		leftPanel.add(jSearch);
		JCustomButton jOkButton = new JCustomButton("OK", "ok.png", "ok");
		jOkButton.addActionListener(this);
		jOkButton.setActionCommand("ok");
		leftPanel.add(jOkButton);
		jChecked = new JCheckBox("Show checked links");
		jChecked.addActionListener(this);
		jChecked.setActionCommand("ok");
		leftPanel.add(jChecked);
		
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 1));
		JCustomButton jAutoUpdateButton = new JCustomButton("Auto Update", "url.png", null);
		jAutoUpdateButton.addActionListener(this);
		jAutoUpdateButton.setActionCommand("autoupdate");
		JCustomButton jCheckButton = new JCustomButton("Check Selected Lines", "checked.png", "check");
		jCheckButton.addActionListener(this);
		jCheckButton.setActionCommand("check");
		JCustomButton jSaveButton = new JCustomButton("Save Selected Lines", "save.png", "Save");
		jSaveButton.addActionListener(this);
		jSaveButton.setActionCommand("save");
		rightPanel.add(jAutoUpdateButton, null);
		rightPanel.add(jCheckButton, null);
		rightPanel.add(jSaveButton, null);

		JPanel p = new JPanel(new BorderLayout(0, 0));
		p.setPreferredSize(new Dimension(0, 26));
		p.add(leftPanel, BorderLayout.WEST);
		p.add(rightPanel, BorderLayout.EAST);

		return p;
	}

	private JPanel getTablePanel() {
		JPanel p = new JPanel(new BorderLayout());
		jLinkTable = new JTable();
		jScrollPane = new JScrollPane();
		jScrollPane.setBorder(BorderFactory.createEtchedBorder());
		jScrollPane.setViewportView(jLinkTable);
		p.add(jScrollPane);
		return p;
	}

	public void actionPerformed(ActionEvent e) {
		boolean err = false;
		String msg = null;
		try {
			if (e.getActionCommand().equals("ok"))
				loadLinks();
			else if (e.getActionCommand().equals("check")) {
				for (int i : jLinkTable.getSelectedRows()) {
					String id = String.valueOf(jLinkTable.getValueAt(i, 0));
					DatabaseManager.executeUpdate("UPDATE _external_link SET checked = TRUE WHERE id = ?", Arrays.asList(Integer.valueOf(id)));
					jLinkTable.setValueAt("X", i, 6);
				}
				msg = "Links checked successfully (" + jLinkTable.getSelectedRowCount() + ")";
			}
			else if (e.getActionCommand().equals("save")) {
				for (int i : jLinkTable.getSelectedRows()) {
					String id = String.valueOf(jLinkTable.getValueAt(i, 0));
					DatabaseManager.executeUpdate("UPDATE _external_link SET type = ?, url = ? WHERE id = ?", Arrays.asList(jLinkTable.getValueAt(i, 4), jLinkTable.getValueAt(i, 5), id));
				}
				msg = "Links updated successfully (" + jLinkTable.getSelectedRowCount() + ")";
			}
			else if (e.getActionCommand().equals("autoupdate"))
				JMainFrame.getUrlUpdateDialog().open();
		}
		catch (Exception e_) {
			err = true;
			log.log(Level.WARNING, e_.getMessage(), e_);
		}
		finally {
			if (msg != null)
				jQueryStatus.set(err ? JQueryStatus.FAILURE : JQueryStatus.SUCCESS, msg);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadLinks() {
		try {
			String[] tIds = jIdRange.getText().split("\\-");
			String text = jSearch.getText();
			StringBuilder sql = new StringBuilder("SELECT * FROM _external_link WHERE entity = '" + alias + "'");
			if (tIds.length > 1)
				sql.append(" AND id_item BETWEEN " + tIds[0] + " AND " + tIds[1]);				
			if (StringUtils.notEmpty(text)) {
				sql.append(" AND (0=1" + (text.matches("\\d+") ? " OR idItem=" + text : ""));
				sql.append(" OR LOWER(url) LIKE '%" + text.toLowerCase() + "%'");
				sql.append(")");
			}
			if (!jChecked.isSelected())
				sql.append(" AND (checked = FALSE OR checked IS NULL)");
			sql.append(" ORDER BY id_item");
			HashMap<Integer, String> hLabel = new HashMap<Integer, String>();
			String table = (String) DatabaseManager.getClassFromAlias(alias).getField("table").get(null);
			String sql_ = "SELECT id, " + (alias.equals(Athlete.alias) ? "last_name || ', ' || first_name || ' - ' || id_sport" : "label") + " FROM " + table + (tIds.length > 1 ? " WHERE id BETWEEN " + tIds[0] + " AND " + tIds[1] : "");
			for (Object[] t_ : (List<Object[]>) DatabaseManager.executeSelect(sql_)) {
				hLabel.put(Integer.parseInt(String.valueOf(t_[0])), String.valueOf(t_[1]));
			}
			Collection<ExternalLink> lLinks = (Collection<ExternalLink>) DatabaseManager.executeSelect(sql.toString(), ExternalLink.class);
			Vector<Vector<String>> vLinks = new Vector<Vector<String>>();
			for (ExternalLink l : lLinks) {
				Vector<String> v = new Vector<String>();
				v.add(String.valueOf(l.getId()));
				v.add(String.valueOf(l.getIdItem()));
				v.add(l.getEntity());
				v.add(hLabel.get(l.getIdItem()));
				v.add(l.getUrl());
				v.add(l.getChecked() != null && l.getChecked() ? "X" : "");
				vLinks.add(v);
			}
			Vector<String> vHeader = new Vector<String>();
			vHeader.add("Link ID");
			vHeader.add("Entity ID");
			vHeader.add("Entity");
			vHeader.add("Label");
			vHeader.add("URL type");
			vHeader.add("URL");
			vHeader.add("Checked");
			jLinkTable = new JTable(vLinks, vHeader) {
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) {
					return true;
				}
			};
			jLinkTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableColumnModel model = jLinkTable.getColumnModel();
			model.getColumn(0).setPreferredWidth(50);
			model.getColumn(1).setPreferredWidth(50);
			model.getColumn(2).setPreferredWidth(50);
			model.getColumn(3).setPreferredWidth(100);
			model.getColumn(4).setPreferredWidth(70);
			model.getColumn(5).setPreferredWidth(450);
			model.getColumn(6).setPreferredWidth(50);
			jLinkTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jScrollPane.setViewportView(jLinkTable);
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		int index = 0;
		if (e != null && e.getSource() instanceof JList)
			index = ((JList<?>)e.getSource()).getSelectedIndex();
		switch (index) {
			case 0: alias = Athlete.alias; break;
			case 1: alias = Championship.alias; break;
			case 2: alias = City.alias; break;
			case 3: alias = Complex.alias; break;
			case 4: alias = Country.alias; break;
			case 5: alias = Event.alias; break;
			case 6: alias = Olympics.alias; break;
			case 7: alias = Result.alias; break;
			case 8: alias = Sport.alias; break;
			case 9: alias = State.alias; break;
			case 10: alias = Team.alias; break;
		}
		loadLinks();
	}

}