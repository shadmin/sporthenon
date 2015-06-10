package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.log4j.Logger;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.container.JTopPanel;
import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Draw;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.Year;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class JImportDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final String scPattern = "[^a-zA-Z0-9\\|\\,\\s\\(\\)_]";
	private JRadioButton jTypeRS;
	private JRadioButton jTypeDR;
	private JRadioButton jTypeRC;
	private JTextField jFile;
	private JFileChooser jFileChooser;
	private JCustomButton jProcessButton;
	private JCustomButton jUpdateButton;
	private JScrollPane jScrollPane;
	private JTable jProcessTable;
	private JProgressBar jProgressBar;
	private JImportReportDialog jImportReportDialog;
	private Vector<String> vHeader = null;
	private StringBuffer processReport = null;

	public JImportDialog(JFrame owner) {
		super(owner);
		initialize();
	}

	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(800, 600));
		this.setSize(this.getPreferredSize());
		this.setModal(false);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setTitle("Import");
		this.setContentPane(jContentPane);
		jImportReportDialog = new JImportReportDialog(this);

		BorderLayout layout = new BorderLayout();
		//layout.setVgap(5);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 3));
		jContentPane.setLayout(layout);
		jContentPane.add(getTopPanel(), BorderLayout.NORTH);
		jContentPane.add(getTablePanel(), BorderLayout.CENTER);
	}
	
	private JPanel getTopPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setPreferredSize(new Dimension(0, 105));
		p.setBorder(BorderFactory.createTitledBorder(null, "Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		p.add(getPanel1(), BorderLayout.NORTH);
		p.add(getPanel2(), BorderLayout.CENTER);
		p.add(getPanel3(), BorderLayout.SOUTH);
		return p;
	}

	private JPanel getPanel1() {
		JPanel p = new JPanel(new FlowLayout(0, 2, 0));
		p.add(new JLabel("CSV File:"));
		jFile = new JTextField(50);
		p.add(jFile);
		JCustomButton btn = new JCustomButton(null, "common/folder.png", null);
		btn.setToolTipText("Browse");
		btn.setActionCommand("browse");
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.addActionListener(this);
		p.add(btn);
		jTypeRS = new JRadioButton("Results");
		jTypeDR = new JRadioButton("Draws");
		jTypeRC = new JRadioButton("Records");
		jTypeRS.setSelected(true);
		ButtonGroup grp = new ButtonGroup();
		grp.add(jTypeRS);
		grp.add(jTypeDR);
		grp.add(jTypeRC);
		p.add(new JLabel(" Import Type:"));
		p.add(jTypeRS);
		p.add(jTypeDR);
		p.add(jTypeRC);
		return p;
	}
	
	private JPanel getPanel2() {
		JPanel p = new JPanel(new FlowLayout(0, 2, 2));
		jProgressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
		jProgressBar.setPreferredSize(new Dimension(350, 20));
		jProgressBar.setValue(0);
		jProgressBar.setStringPainted(true);
		p.add(jProgressBar);
		jProcessButton = new JCustomButton("Process", "ok.png", null);
		jProcessButton.setActionCommand("process");
		jProcessButton.setEnabled(false);
		jProcessButton.addActionListener(this);
		p.add(jProcessButton);
		jUpdateButton = new JCustomButton("Update", "update.png", null);
		jUpdateButton.setActionCommand("update");
		jUpdateButton.setEnabled(false);
		jUpdateButton.addActionListener(this);
		p.add(jUpdateButton);
		JCustomButton btn = new JCustomButton("Close", "cancel.png", null);
		btn.setActionCommand("close");
		btn.addActionListener(this);
		p.add(btn);
		return p;
	}
	
	private JPanel getPanel3() {
		JPanel p = new JPanel(new FlowLayout(0, 2, 2));
		p.add(new JLabel("Templates:"));
		JCustomButton btn = new JCustomButton("Results (XLS)", null, null);
		btn.setActionCommand("template-RS-xls");
		btn.addActionListener(this);
		p.add(btn);
		btn = new JCustomButton("Results (CSV)", null, null);
		btn.setActionCommand("template-RS-csv");
		btn.addActionListener(this);
		p.add(btn);
		btn = new JCustomButton("Draws (XLS)", null, null);
		btn.setActionCommand("template-DR-xls");
		btn.addActionListener(this);
		p.add(btn);
		btn = new JCustomButton("Draws (CSV)", null, null);
		btn.setActionCommand("template-DR-csv");
		btn.addActionListener(this);
		p.add(btn);
		btn = new JCustomButton("Records (XLS)", null, null);
		btn.setActionCommand("template-RC-xls");
		btn.addActionListener(this);
		p.add(btn);
		btn = new JCustomButton("Records (CSV)", null, null);
		btn.setActionCommand("template-RC-csv");
		btn.addActionListener(this);
		p.add(btn);
		return p;
	}

	private JPanel getTablePanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder(null, "File Analysis", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jProcessTable = new JTable();
		jScrollPane = new JScrollPane();
		jScrollPane.setBorder(BorderFactory.createEtchedBorder());
		jScrollPane.setViewportView(jProcessTable);
		p.add(jScrollPane);
		return p;
	}

	public void open(JTopPanel parent) {
		jFile.setText(null);
		jProcessButton.setEnabled(false);
		jUpdateButton.setEnabled(false);
		jScrollPane.setViewportView(null);
		jProcessButton.setText("Process");
		jTypeRS.setSelected(true);
		jProgressBar.setValue(0);
		vHeader = null;
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("close")) {
			jImportReportDialog.setVisible(false);
			setVisible(false);
		}
		else if (e.getActionCommand().equals("browse")) {
			jFileChooser = new JFileChooser();
			jFileChooser.setDialogTitle("Select File for Import");
			if (jFileChooser.showOpenDialog(this) == 0) {
				File f = jFileChooser.getSelectedFile();
				if (f != null) {
					jFile.setText(f.getPath());
					jProcessButton.setEnabled(true);
				}
			}
		}
		else if (e.getActionCommand().equals("process")) {
			new Thread (new Runnable() {
				public void run() {
					processAll(jProcessButton.getText().equalsIgnoreCase("reprocess"), false);
				}
			}).start();
		}
		else if (e.getActionCommand().equals("update")) {
			new Thread (new Runnable() {
				public void run() {
					processReport = new StringBuffer();
					processAll(true, true);
					jImportReportDialog.getReport().setText(processReport.toString());
					jImportReportDialog.open();
				}
			}).start ();
		}
		else if (e.getActionCommand().matches("template.*")) {
			jFileChooser = new JFileChooser();
			jFileChooser.setDialogTitle("Select Folder for Template");
			jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (jFileChooser.showOpenDialog(this) == 0) {
				int x = e.getActionCommand().lastIndexOf("-");
				String type = e.getActionCommand().substring(x - 2, x);
				String ext = e.getActionCommand().substring(x + 1);
				ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
				if (type.equalsIgnoreCase(Result.alias)) {
					ArrayList<String> list_ = new ArrayList<String>();
					for (String s : new String[] {"Sport", "Event 1", "Event 2", "Event 3", "Event 4", "Year", "Winner / Gold Medal / 1st Place", "Winner Result (or Game Score)", "Runner-up / Silver Medal / 2nd Place", "2nd Result", "Bronze Medal / 3rd Place", "3rd Result", "4th Place", "5th Place", "6th Place", "7th Place", "8th Place", "9th Place", "Start Date (DD/MM/YYYY)", "End Date (DD/MM/YYYY)", "Place #1 (Complex Name or City)", "Place #2 (Complex Name or City)", "Tie", "Comment", "External Link"})
						list_.add(s);
					list.add(list_);
					list.add(new ArrayList<String>());
					list_ = new ArrayList<String>();
					for (String s : new String[] {"Athletics", "Olympic Games", "Men", "100 M", "", "1936", "Owens, Jessie (USA)", "10.30", "Metcalfe, Ralf (USA)", "10.40", "Osendarp, Tinus (NED)", "10.50", "", "", "", "", "", "", "", "08/03/1936", "", "Olympiastadion, Berlin, GER", "", "", ""})
						list_.add(s);
					list.add(list_);
					list_ = new ArrayList<String>();
					for (String s : new String[] {"Tennis", "Grand Slam", "Men", "French Open", "", "2005", "Nadal, Rafael (ESP)", "6/7 6/3 6/1 7/5", "Puerta, Mariano (ARG)", "", "", "", "", "", "", "", "", "", "", "06/05/2005", "", "Roland Garros, Paris, FRA", "", "", ""})
						list_.add(s);
					list.add(list_);
					list_ = new ArrayList<String>();
					for (String s : new String[] {"Road Cycling", "UCI World Tour", "Milan-San Remo", "", "", "2004", "Cancellara, Fabian (SUI)", "", "Contador, Albero (ESP)", "Rodriguez, Joaquin (ESP)", "", "", "", "", "", "", "", "", "", "06/03/2004", "Milan, ITA", "San Remo, ITA", "", "", ""})
						list_.add(s);
					list.add(list_);
				}
				else if (type.equalsIgnoreCase(Draw.alias)) {
					ArrayList<String> list_ = new ArrayList<String>();
					for (String s : new String[] {"Sport", "Event 1", "Event 2", "Event 3", "Event 4", "Year", "QF #1 - Winner", "QF #1 - Runner-up", "QF #1 - Score", "QF #2 - Winner", "QF #2 - Runner-up", "QF #2 - Score", "QF #3 - Winner", "QF #3 - Runner-up", "QF #3 - Score", "QF #4 - Winner", "QF #4 - Runner-up", "QF #4 - Score", "SF #1 - Winner", "SF #1 - Runner-up", "SF #1 - Score", "SF #2 - Winner", "SF #2 - Runner-up", "SF #2 - Score", "3rd pl. - Winner", "3rd pl. - Runner-up", "3rd pl. - Score"})
						list_.add(s);
					list.add(list_);
					list.add(new ArrayList<String>());
					list_ = new ArrayList<String>();
					for (String s : new String[] {"Football", "Word Cup", "Men", "Results", "", "2006", "GER", "ARG", "1-1 (4-2 ap.)", "ITA", "UKR", "3-0", "POR", "ENG", "0-0 (3-1 ap.)", "FRA", "BRA", "1-0", "ITA", "GER", "2-0", "FRA", "POR", "1-0", "GER", "POR", "3-1"})
						list_.add(s);
					list.add(list_);
					list_ = new ArrayList<String>();
					for (String s : new String[] {"American Football", "NFL", "Championships", "Super Bowl", "", "2000", "Tampa Bay Buccaneers", "Washington Redskins", "14-13", "Saint Louis Rams", "Minnesota Vikings", "49-37", "Tennessee Titans", "Indianapolis Colts", "19-16", "Jacksonville Jaguars", "Miami Dolphins", "62-7", "Saint Louis Rams", "Tampa Bay Buccaneers", "11-6", "Tennessee Titans", "Jacksonville Jaguars", "33-14", "", "", ""})
						list_.add(s);
					list.add(list_);
				}
				else if (type.equalsIgnoreCase(Record.alias)) {
					ArrayList<String> list_ = new ArrayList<String>();
					for (String s : new String[] {"Sport", "Event 1", "Event 2", "Event 3", "Type #1", "Type #2", "Record Label", "Holder", "Record (Holder)", "Date (Holder)", "2nd", "Record (2nd)", "Date (2nd)", "3rd", "Record (3rd)", "Date (3rd)", "4th", "Record (4th)", "Date (4th)", "5th", "Record (5th)", "Date (5th)", "Index", "Tie", "Comment"})
						list_.add(s);
					list.add(list_);
					list.add(new ArrayList<String>());
					list_ = new ArrayList<String>();
					for (String s : new String[] {"Basketball", "NBA", "Rebounds", "", "Individual", "Game", "Most rebounds", "Chamberlain, Wilt (Philadelphia Warriors)", "55", "10/23/1962", "Russell, Bill (Boston Celtics)", "52", "04/02/1965", "Rodman, Dennis (Chicago Bulls)", "41", "10/10/1998", "", "", "", "", "", "", "1.0", "", ""})
						list_.add(s);
					list.add(list_);
				}
				FileOutputStream fos = null;
				try {
					File f = jFileChooser.getSelectedFile();
					fos = new FileOutputStream(new File(f.getAbsolutePath() + "\\SH-Template-" + type + "." + ext));
					if (ext.equalsIgnoreCase("xls")) {
						ExportUtils.buildExcel(fos, null, null, list, null, new boolean[]{false});
					}
					else if (ext.equalsIgnoreCase("csv")) {
						StringBuffer sb = new StringBuffer();
						for (ArrayList<String> list_ : list) {
							int i = 0;
							for (String s : list_)
								sb.append(i++ > 0 ? ";" : "").append(s);
							sb.append("\r\n");
						}
						fos.write(sb.toString().getBytes());
					}
				}
				catch (Exception e_) {
					Logger.getLogger("sh").error(e_.getMessage(), e_);
				}
				finally {
					try {
						if (fos != null)
							fos.close();
					}
					catch (Exception e_) {
						Logger.getLogger("sh").error(e_.getMessage(), e_);
					}
				}
			}
		}
	}

	private void incrementProgress() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jProgressBar.setValue(jProgressBar.getValue() + 1);
			}
		});
		try  {
			//java.lang.Thread.sleep(100);
		}
		catch (Exception e) {}
	}

	private Vector<Vector<String>> getTableAsVector() {
		final int n = jProcessTable.getColumnCount();
		Vector v = new Vector<Vector<String>>();
		for (int i = 0 ; i < jProcessTable.getRowCount() ; i++) {
			Vector<String> v_ = new Vector<String>();
			for (int j = 0 ; j < n ; j++) {
				Object o = jProcessTable.getValueAt(i, j);
				v_.add(o != null ? String.valueOf(o) : "");
			}
			v.add(v_);
		}
		return v;
	}

	private void processAll(boolean isReprocess, boolean isUpdate) {
		try {
			jUpdateButton.setEnabled(false);
			jProgressBar.setValue(0);
			jScrollPane.setViewportView(null);
			final HashMap<String, String> hTitle = new HashMap<String, String>();
			Vector<String> vHeaderLabel = new Vector<String>();
			if (jTypeRS.isSelected()) {
				vHeader = new Vector(Arrays.asList(new String[] {"msg", "sp", "cp", "ev", "se", "se2", "yr", "rk1", "rs1", "rk2", "rs2", "rk3", "rs3", "rk4", "rk5", "rk6", "rk7", "rk8", "rk9", "dt1", "dt2", "pl1", "pl2", "exa", "cmt", "exl"}));
				hTitle.put("msg", "Message");
				hTitle.put("sp", "Sport");
				hTitle.put("cp", "Championship");
				hTitle.put("ev", "Event #1");
				hTitle.put("se", "Event #2");
				hTitle.put("se2", "Event #3");
				hTitle.put("yr", "Year");
				hTitle.put("rk1", "Rank #1"); hTitle.put("rk2", "Rank #2"); hTitle.put("rk3", "Rank #3"); hTitle.put("rk4", "Rank #4"); hTitle.put("rk5", "Rank #5"); hTitle.put("rk6", "Rank #6"); hTitle.put("rk7", "Rank #7"); hTitle.put("rk8", "Rank #8"); hTitle.put("rk9", "Rank #9");
				hTitle.put("rs1", "Result #1");
				hTitle.put("rs2", "Result #2");
				hTitle.put("rs3", "Result #3");
				hTitle.put("rs4", "Result #4");
				hTitle.put("rs5", "Result #5");
				hTitle.put("dt1", "Date #1");
				hTitle.put("dt2", "Date #2");
				hTitle.put("pl1", "Place #1");
				hTitle.put("pl2", "Place #2");
				hTitle.put("exa", "Tie");
				hTitle.put("cmt", "Comment");
				hTitle.put("exl", "External Link");
			}
			else if (jTypeDR.isSelected()) {
				vHeader = new Vector(Arrays.asList(new String[] {"msg", "sp", "cp", "ev", "se", "se2", "yr", "qf1w", "qf1r", "qf1s", "qf2w", "qf2r", "qf2s", "qf3w", "qf3r", "qf3s", "qf4w", "qf4r", "qf4s", "sf1w", "sf1r", "sf1s", "sf2w", "sf2r", "sf2s", "thdw", "thdr", "thds"}));
				hTitle.put("msg", "Message");
				hTitle.put("sp", "Sport");
				hTitle.put("cp", "Championship");
				hTitle.put("ev", "Event #1");
				hTitle.put("se", "Event #2");
				hTitle.put("se2", "Event #3");
				hTitle.put("yr", "Year");
				hTitle.put("qf1w", "Quarterfinal #1 - W");
				hTitle.put("qf1r", "Quarterfinal #1 - L");
				hTitle.put("qf1s", "Quarterfinal #1 - Score");
				hTitle.put("qf2w", "Quarterfinal #2 - W");
				hTitle.put("qf2r", "Quarterfinal #2 - L");
				hTitle.put("qf2s", "Quarterfinal #2 - Score");
				hTitle.put("qf3w", "Quarterfinal #3 - W");
				hTitle.put("qf3r", "Quarterfinal #3 - L");
				hTitle.put("qf3s", "Quarterfinal #3 - Score");
				hTitle.put("qf4w", "Quarterfinal #4 - W");
				hTitle.put("qf4r", "Quarterfinal #4 - L");
				hTitle.put("qf4s", "Quarterfinal #4 - Score");
				hTitle.put("sf1w", "Semifinal #1 - W");
				hTitle.put("sf1r", "Semifinal #1 - L");
				hTitle.put("sf1s", "Semifinal #1 - Score");
				hTitle.put("sf2w", "Semifinal #2 - W");
				hTitle.put("sf2r", "Semifinal #2 - L");
				hTitle.put("sf2s", "Semifinal #2 - Score");
				hTitle.put("thdw", "3rd place - W");
				hTitle.put("thdr", "3rd place - L");
				hTitle.put("thds", "3rd place - Score");
			}
			else if (jTypeRC.isSelected()) {
				vHeader = new Vector(Arrays.asList(new String[] {"msg", "sp", "cp", "ev", "se", "tp1", "tp2", "label", "rk1", "rc1", "dt1", "rk2", "rc2", "dt2", "rk3", "rc3", "dt3", "rk4", "rc4", "dt4", "rk5", "rc5", "dt5", "idx", "exa", "cmt"}));
				hTitle.put("msg", "Message");
				hTitle.put("sp", "Sport");
				hTitle.put("cp", "Championship");
				hTitle.put("ev", "Event");
				hTitle.put("se", "Subevent");
				hTitle.put("tp1", "Type #1");
				hTitle.put("tp2", "Type #2");
				hTitle.put("label", "Record Label");
				hTitle.put("rk1", "Holder");
				hTitle.put("rc1", "Record - Holder");
				hTitle.put("dt1", "Date - Holder");
				hTitle.put("rk2", "2nd");
				hTitle.put("rc2", "Record - 2nd");
				hTitle.put("dt2", "Date - 2nd");
				hTitle.put("rk3", "3rd");
				hTitle.put("rc3", "Record - 3rd");
				hTitle.put("dt3", "Date - 3rd");
				hTitle.put("rk4", "4th");
				hTitle.put("rc4", "Record - 4th");
				hTitle.put("dt4", "Date - 4th");
				hTitle.put("rk5", "5th");
				hTitle.put("rc5", "Record - 5th");
				hTitle.put("dt5", "Date - 5th");
				hTitle.put("idx", "Index");
				hTitle.put("exa", "Tie");
				hTitle.put("cmt", "Comment");
			}
			for (String s : vHeader)
				vHeaderLabel.add(hTitle.get(s));
			Vector<Vector<String>> vFile = (isReprocess ? getTableAsVector() : StringUtils.readCSV(jFile.getText()));
			int i = 0;
			float pg = 0.0f;
			boolean isError = false;
			for (Vector<String> v : vFile) {
				if (!isReprocess)
					v.insertElementAt("", 0);
				else
					v.set(0, "");
				if (jTypeRS.isSelected())
					isError |= processLineRS(i, vHeader, v, isUpdate);
				else if (jTypeDR.isSelected())
					isError |= processLineDR(i, vHeader, v, isUpdate);
				else if (jTypeRC.isSelected())
					isError |= processLineRC(i, vHeader, v, isUpdate);
				if (i * 100 / vFile.size() > pg) {
					incrementProgress();
					pg = i * 100 / vFile.size();
				}
				i++;
			}
			jUpdateButton.setEnabled(!isError);
			jProgressBar.setValue(100);
			jProcessTable = new JTable(vFile, vHeaderLabel) {
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) {
					return true;
				}
			};
			jProcessTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					String s = String.valueOf(value);
					Color color = (s.indexOf("[#") > -1 ? new Color(0, 120, 0) : new Color(0, 0, 0));
					component.setForeground(color);
					if (String.valueOf(table.getValueAt(row, 0)).matches("^ERROR\\:.*")) {
						component.setForeground(Color.white);
						component.setBackground(Color.red);
					}
					else if (String.valueOf(table.getValueAt(row, 0)).matches("^WARNING\\:.*")) {
						component.setForeground(Color.white);
						component.setBackground(Color.orange);
					}
					else
						component.setBackground(Color.white);
					return component;
				}
			});
			jProcessTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			for (int j = 0 ; j < jProcessTable.getColumnCount() ; j++) {
				String h = vHeader.get(j);
				jProcessTable.getColumnModel().getColumn(j).setPreferredWidth(h.matches("rk\\d|pl\\d") ? 200 : (h.matches("(rs|dt)\\d") ? 80 : 150));
			}
			jProcessTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jScrollPane.setViewportView(jProcessTable);
			jProcessButton.setText("Reprocess");
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
		}
	}

	private boolean processLineRS(int row, Vector<String> vHeader, Vector<String> vLine, boolean isUpdate) throws Exception {
		boolean isError = false;
		List<Integer> lId = null;
		HashMap<String, Integer> hId = new HashMap();
		Integer n = null;
		boolean isComplex1 = false;
		boolean isComplex2 = false;
		for (int i = 0 ; i < vLine.size() ; i++) {
			try {
				String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
				String s = vLine.get(i);
				String hql = null;
				if (StringUtils.notEmpty(s)) {
					if (s.matches(".*\\s\\[#\\d+]$"))
						s = s.substring(0, s.indexOf("[#") - 1);
					String s_ = s.replaceAll(scPattern, "_").toLowerCase();
					if (h.equalsIgnoreCase(Sport.alias))
						hql = "select id from Sport where lower(label) like '" + s_ + "'";
					else if (h.equalsIgnoreCase(Championship.alias))
						hql = "select id from Championship where lower(label) like '" + s_ + "'";
					else if (h.matches("ev|se|se2")) {
						String[] tEv = s_.split("\\|");
						hql = "select id from Event where lower(label) like '" + tEv[0] + "'" + (tEv.length > 1 ? " and lower(type.label)='" + tEv[1] + "'" : "") + " order by id";
					}
					else if (h.equalsIgnoreCase(Year.alias))
						hql = "select id from Year where lower(label) like '" + s_ + "'";
					else if (h.matches("pl\\d")) {
						String[] t = s.toLowerCase().split("\\,\\s");
						String cx = null;
						String ct = null;
						String st = null;
						String cn = t[t.length - 1];
						if (t.length > 2 && t[t.length - 2].length() == 2)
							st = t[t.length - 2];
						if (t.length > (st != null ? 3 : 2)) {
							cx = t[0].replaceAll(scPattern, "_");
							ct = t[1].replaceAll(scPattern, "_");
						}
						else
							ct = t[0].replaceAll(scPattern, "_");
						if (cx != null) {
							h = "cx" + h.replaceAll("pl", "");
							hql = "select id from Complex where lower(city.country.code) like '" + cn + "' and lower(city.label) like '" + ct.replaceAll("'", "''") + "' and lower(label) like '" + cx.replaceAll("'", "''") + "'";
							if (h.equals("cx1"))
								isComplex1 = true;
							else
								isComplex2 = true;
						}
						else {
							h = "ct" + h.replaceAll("pl", "");
							hql = "select id from City where lower(country.code) like '" + cn + "' and lower(label) like '" + ct.replaceAll("'", "''") + "'";
						}
					}
					else if (h.matches("rk\\d")) {
						if (n == null) {
							List<Integer> lNumber = (List<Integer>) DatabaseHelper.execute("select type.number from Event ev where ev.id = " + hId.get(hId.containsKey("se2") ? "se2" : (hId.containsKey("se") ? "se" : "ev")));
							if (lNumber != null && lNumber.size() > 0)
								n = lNumber.get(0);
						}
						if (n != null) {
							if (n < 10) { // Athlete
								if (!s_.matches(StringUtils.PATTERN_ATHLETE)) {
									isError = true;
									writeError(vLine, "ERROR: Invalid Format (Column " + h.toUpperCase() + ")");
								}
								else {
									boolean isCountryTeam = s_.matches(".*\\([a-z]{3}\\,\\s.+\\)$");
									boolean isCountry = s_.matches(".*\\([a-z]{3}\\)$");
									boolean isTeam = (!isCountry && s_.matches(".*\\([^\\,\\(\\)]+\\)$")); 
									hql = "select id from Athlete where sport.id=" + hId.get("sp") + " and lower(last_name) || ', ' || lower(first_name) " + (isCountryTeam ? " || ' (' || lower(country.code) " + " || ', ' || lower(team.label) || ')'" : (isCountry ? " || ' (' || lower(country.code) || ')'" : (isTeam ? " || ' (' || lower(team.label) || ')'" : ""))) + " like '" + s_ + "'";
								}
							}
							else if (n == 50) { // Team
								if (!s_.matches(StringUtils.PATTERN_TEAM)) {
									isError = true;
									writeError(vLine, "ERROR: Invalid Format (Column " + h.toUpperCase() + ")");
								}
								else
									hql = "select id from Team where sport.id=" + hId.get("sp") + " and lower(label) like '" + (s_.indexOf(" (") > -1 ? s_.substring(0, s_.indexOf(" (")) : s_) + "' and (link is null or link = 0)";
							}
							else if (n == 99) { // Country
								if (!s_.matches(StringUtils.PATTERN_COUNTRY)) {
									isError = true;
									writeError(vLine, "ERROR: Invalid Format (Column " + h.toUpperCase() + ")");
								}
								else
									hql = "select id from Country where lower(code) like '" + s_ + "'";
							}
						}
					}
				}
				if (hql != null) {
//					Logger.getLogger("sh").info(hql);
					lId = (List<Integer>) DatabaseHelper.execute(hql);
					if (lId != null && lId.size() > 0) {
						hId.put(h, lId.get(0));
						vLine.set(i, s + " [#" + lId.get(0) + "]");
					}
					else {
						if (h.equalsIgnoreCase(Sport.alias)) {
							isError = true;
							writeError(vLine, "ERROR: Invalid Sport");
						}
						else if (h.equalsIgnoreCase(Championship.alias)) {
							isError = true;
							writeError(vLine, "ERROR: Invalid Championship");
						}
						else if (h.matches("ev|se")) {
							isError = true;
							writeError(vLine, "ERROR: Invalid Event");
						}
						else if (h.equalsIgnoreCase(Year.alias)) {
							isError = true;
							writeError(vLine, "ERROR: Invalid Year");
						}
						else if (n == 99 && h.matches("rk\\d")) {
							isError = true;
							writeError(vLine, "ERROR: Invalid Country (column " + h.toUpperCase() + ")");
						}
						vLine.set(i, s);
					}
				}
				else {
					if (h.matches("dt\\d") && StringUtils.notEmpty(s) && !s.matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d")) {
						isError = true;
						writeError(vLine, "ERROR: Invalid Date (Column " + h.toUpperCase() + ")");
					}
					else if (h.matches("rs\\d") && StringUtils.notEmpty(s) && s.length() > (h.equalsIgnoreCase("rs1") ? 40 : 20)) {
						isError = true;
						writeError(vLine, "ERROR: Invalid Result - Too long (Column " + h.toUpperCase() + ")");
					}
				}
			}
			catch (Exception e) {
				isError = true;
				writeError(vLine, "ERROR: " + e.getMessage());
			}
			if (isError)
				break;
		}
		if (isUpdate) {
			Integer idSp = hId.get("sp");
			Integer idCp = hId.get("cp");
			Integer idEv = hId.get("ev");
			Integer idSe = hId.get("se");
			Integer idSe2 = hId.get("se2");
			Integer idYr = hId.get("yr");
			Integer idCx1 = hId.get("cx1");
			Integer idCt1 = hId.get("ct1");
			Integer idCx2 = hId.get("cx2");
			Integer idCt2 = hId.get("ct2");
			Integer idRk1 = hId.get("rk1");
			Integer idRk2 = hId.get("rk2");
			Integer idRk3 = hId.get("rk3");
			Integer idRk4 = hId.get("rk4");
			Integer idRk5 = hId.get("rk5");
			Integer idRk6 = hId.get("rk6");
			Integer idRk7 = hId.get("rk7");
			Integer idRk8 = hId.get("rk8");
			Integer idRk9 = hId.get("rk9");
			String rs1 = null;String rs2 = null;String rs3 = null;String rs4 = null;String rs5 = null;
			String dt1 = null;String dt2 = null;
			String cmt = null;
			String exl = null;
			String exa = null;
			String err = null;
			try {
				for (int i = 0 ; i < vLine.size() ; i++) {
					String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
					String s = vLine.get(i);
					if (StringUtils.notEmpty(s)) {
						if(idRk1 == null && h.equalsIgnoreCase("rk1"))
							idRk1 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if(idRk2 == null && h.equalsIgnoreCase("rk2"))
							idRk2 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if(idRk3 == null && h.equalsIgnoreCase("rk3"))
							idRk3 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if(idRk4 == null && h.equalsIgnoreCase("rk4"))
							idRk4 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if(idRk5 == null && h.equalsIgnoreCase("rk5"))
							idRk5 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if(idRk6 == null && h.equalsIgnoreCase("rk6"))
							idRk6 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if(idRk7 == null && h.equalsIgnoreCase("rk7"))
							idRk7 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if(idRk8 == null && h.equalsIgnoreCase("rk8"))
							idRk8 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if(idRk9 == null && h.equalsIgnoreCase("rk9"))
							idRk9 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk1"))
							updateEntity(row, n, idRk1, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk2"))
							updateEntity(row, n, idRk2, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk3"))
							updateEntity(row, n, idRk3, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk4"))
							updateEntity(row, n, idRk4, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk5"))
							updateEntity(row, n, idRk5, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk6"))
							updateEntity(row, n, idRk6, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk7"))
							updateEntity(row, n, idRk7, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk8"))
							updateEntity(row, n, idRk8, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("rk9"))
							updateEntity(row, n, idRk9, s);
						else if (idCx1 == null && idCt1 == null && h.matches("pl1")) {
							if (isComplex1)
								idCx1 = DatabaseHelper.insertPlace(row, s, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
							else
								idCt1 = DatabaseHelper.insertPlace(row, s, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						}
						else if (idCx2 == null && idCt2 == null && h.matches("pl2")) {
							if (isComplex2)
								idCx2 = DatabaseHelper.insertPlace(row, s, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
							else
								idCt2 = DatabaseHelper.insertPlace(row, s, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						}
						else if (h.equalsIgnoreCase("rs1"))
							rs1 = s;
						else if (h.equalsIgnoreCase("rs2"))
							rs2 = s;
						else if (h.equalsIgnoreCase("rs3"))
							rs3 = s;
						else if (h.equalsIgnoreCase("rs4"))
							rs4 = s;
						else if (h.equalsIgnoreCase("rs5"))
							rs5 = s;
						else if (h.equalsIgnoreCase("dt1"))
							dt1 = s;
						else if (h.equalsIgnoreCase("dt2"))
							dt2 = s;
						else if (h.equalsIgnoreCase("exa"))
							exa = s;
						else if (h.equalsIgnoreCase("cmt"))
							cmt = s;
						else if (h.equalsIgnoreCase("exl"))
							exl = s;
					}
				}
			}
			catch (Exception e) {
				err = e.getMessage();
				Logger.getLogger("sh").error(e.getMessage(), e);
			}
			if (err != null)
				writeError(vLine, "ERROR: " + err);
			else {
				Result rs = new Result();
				rs.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, idSp));
				rs.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, idCp));
				rs.setEvent((Event)DatabaseHelper.loadEntity(Event.class, idEv != null ? idEv : 0));
				rs.setSubevent((Event)DatabaseHelper.loadEntity(Event.class, idSe != null ? idSe : 0));
				rs.setSubevent2((Event)DatabaseHelper.loadEntity(Event.class, idSe2 != null ? idSe2 : 0));
				rs.setYear((Year)DatabaseHelper.loadEntity(Year.class, idYr));
				rs.setComplex1((Complex)DatabaseHelper.loadEntity(Complex.class, idCx1));
				rs.setComplex2((Complex)DatabaseHelper.loadEntity(Complex.class, idCx2));
				rs.setCity1((City)DatabaseHelper.loadEntity(City.class, idCt1));
				rs.setCity2((City)DatabaseHelper.loadEntity(City.class, idCt2));
				rs.setDate1(StringUtils.notEmpty(dt1) ? dt1 : null);
				rs.setDate2(StringUtils.notEmpty(dt2) ? dt2 : null);
				rs.setExa(StringUtils.notEmpty(exa) ? exa : null);
				rs.setComment(StringUtils.notEmpty(cmt) ? cmt : null);
				rs.setIdRank1(idRk1);
				rs.setIdRank2(idRk2);
				rs.setIdRank3(idRk3);
				rs.setIdRank4(idRk4);
				rs.setIdRank5(idRk5);
				rs.setIdRank5(idRk6);
				rs.setIdRank5(idRk7);
				rs.setIdRank5(idRk8);
				rs.setIdRank5(idRk9);
				rs.setResult1(rs1);
				rs.setResult2(rs2);
				rs.setResult3(rs3);
				rs.setResult4(rs4);
				rs.setResult5(rs5);
				rs = (Result) DatabaseHelper.saveEntity(rs, JMainFrame.getContributor());
				if (StringUtils.notEmpty(exl))
					DatabaseHelper.saveExternalLinks(Result.alias, rs.getId(), exl);
				processReport.append("Row " + (row + 1) + ": New Result | " + rs).append("\r\n");
			}
		}
		return isError;
	}

	private boolean processLineDR(int row, Vector<String> vHeader, Vector<String> vLine, boolean isUpdate) throws Exception {
		boolean isError = false;
		List<Integer> lId = null;
		HashMap<String, Integer> hId = new HashMap();
		Integer n = null;
		Integer rsId = null;
		for (int i = 0 ; i < vLine.size() ; i++) {
			try {
				String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
				String s = vLine.get(i);
				String hql = null;
				if (StringUtils.notEmpty(s)) {
					if (s.matches(".*\\s\\[#\\d+]$"))
						s = s.substring(0, s.indexOf("[#") - 1);
					String s_ = s.replaceAll(scPattern, "_").toLowerCase();
					if (h.equalsIgnoreCase(Sport.alias))
						hql = "select id from Sport where lower(label) like '" + s_ + "'";
					else if (h.equalsIgnoreCase(Championship.alias))
						hql = "select id from Championship where lower(label) like '" + s_ + "'";
					else if (h.matches("ev|se|se2")) {
						String[] tEv = s_.split("\\|");
						hql = "select id from Event where lower(label) like '" + tEv[0] + "'" + (tEv.length > 1 ? " and lower(type.label)='" + tEv[1] + "'" : "") + " order by id";
					}
					else if (h.equalsIgnoreCase(Year.alias))
						hql = "select id from Result where year.label='" + s_ + "' and sport.id=" + hId.get("sp") + " and championship.id=" + hId.get("cp") + " and event.id=" + hId.get("ev") + (hId.containsKey("se") ? " and subevent.id=" + hId.get("se") : "") + (hId.containsKey("se2") ? " and subevent2.id=" + hId.get("se2") : "");
					else if (h.matches("(qf|sf|thd)\\d*(w|r)")) {
						if (n == null) {
							List<Integer> lNumber = (List<Integer>) DatabaseHelper.execute("select type.number from Event ev where ev.id = " + hId.get(hId.containsKey("se2") ? "se2" : (hId.containsKey("se") ? "se" : "ev")));
							if (lNumber != null && lNumber.size() > 0)
								n = lNumber.get(0);
						}
						if (n != null) {
							if (n < 10) { // Athlete
								if (!s_.matches(StringUtils.PATTERN_ATHLETE)) {
									isError = true;
									writeError(vLine, "ERROR: Invalid Format (Column " + h.toUpperCase() + ")");
								}
								else {
									boolean isCountryTeam = s_.matches(".*\\([a-z]{3}\\,\\s.+\\)$");
									boolean isCountry = s_.matches(".*\\([a-z]{3}\\)$");
									boolean isTeam = (!isCountry && s_.matches(".*\\([^\\,\\(\\)]+\\)$")); 
									hql = "select id from Athlete where sport.id=" + hId.get("sp") + " and lower(last_name) || ', ' || lower(first_name) " + (isCountryTeam ? " || ' (' || lower(country.code) " + " || ', ' || lower(team.label) || ')'" : (isCountry ? " || ' (' || lower(country.code) || ')'" : (isTeam ? " || ' (' || lower(team.label) || ')'" : ""))) + " like '" + s_ + "'";
								}
							}
							else if (n == 50) { // Team
								if (!s_.matches(StringUtils.PATTERN_TEAM)) {
									isError = true;
									writeError(vLine, "ERROR: Invalid Format (Column " + h.toUpperCase() + ")");
								}
								else
									hql = "select id from Team where sport.id=" + hId.get("sp") + " and lower(label) like '" + (s_.indexOf(" (") > -1 ? s_.substring(0, s_.indexOf(" (")) : s_) + "' and (link is null or link = 0)";
							}
							else if (n == 99) { // Country
								if (!s_.matches(StringUtils.PATTERN_COUNTRY)) {
									isError = true;
									writeError(vLine, "ERROR: Invalid Format (Column " + h.toUpperCase() + ")");
								}
								else
									hql = "select id from Country where lower(code) like '" + s_ + "'";
							}
						}
					}
				}
				if (hql != null) {
//					Logger.getLogger("sh").info(hql);
					lId = (List<Integer>) DatabaseHelper.execute(hql);
					if (lId != null && lId.size() > 0) {
						hId.put(h, lId.get(0));
						vLine.set(i, s + " [#" + lId.get(0) + "]");
					}
					else {
						if (h.equalsIgnoreCase(Sport.alias)) {
							isError = true;
							writeError(vLine, "ERROR: Invalid Sport");
						}
						else if (h.equalsIgnoreCase(Championship.alias)) {
							isError = true;
							writeError(vLine, "ERROR: Invalid Championship");
						}
						else if (h.matches("ev|se")) {
							isError = true;
							writeError(vLine, "ERROR: Invalid Event");
						}
						else if (h.equalsIgnoreCase(Year.alias)) {
							isError = true;
							writeError(vLine, "ERROR: Result does not exist");
						}
						else if (n == 99 && h.matches("(qf|sf|thd)\\d*(w|r)")) {
							isError = true;
							writeError(vLine, "ERROR: Invalid Country (column " + h.toUpperCase() + ")");
						}
						vLine.set(i, s);
					}
				}
				else {
					if (h.matches(".*s$") && StringUtils.notEmpty(s) && s.length() > 40) {
						isError = true;
						writeError(vLine, "ERROR: Invalid Score - Too long (Column " + h.toUpperCase() + ")");
					}
				}
			}
			catch (Exception e) {
				isError = true;
				writeError(vLine, "ERROR: " + e.getMessage());
			}
			if (isError)
				break;
		}
		if (isUpdate) {
			rsId = hId.get("yr");
			Integer idSp = hId.get("sp");
			Integer id1Qf1 = hId.get("qf1w");Integer id2Qf1 = hId.get("qf1r");String sQf1 = (hId.get("qf1s") != null ? String.valueOf(hId.get("qf1s")) : null);
			Integer id1Qf2 = hId.get("qf2w");Integer id2Qf2 = hId.get("qf2r");String sQf2 = (hId.get("qf2s") != null ? String.valueOf(hId.get("qf2s")) : null);
			Integer id1Qf3 = hId.get("qf3w");Integer id2Qf3 = hId.get("qf3r");String sQf3 = (hId.get("qf3s") != null ? String.valueOf(hId.get("qf3s")) : null);
			Integer id1Qf4 = hId.get("qf4w");Integer id2Qf4 = hId.get("qf4r");String sQf4 = (hId.get("qf4s") != null ? String.valueOf(hId.get("qf4s")) : null);
			Integer id1Sf1 = hId.get("sf1w");Integer id2Sf1 = hId.get("sf1r");String sSf1 = (hId.get("sf1s") != null ? String.valueOf(hId.get("sf1s")) : null);
			Integer id1Sf2 = hId.get("sf2w");Integer id2Sf2 = hId.get("sf2r");String sSf2 = (hId.get("sf2s") != null ? String.valueOf(hId.get("sf2s")) : null);
			Integer id1Thd = hId.get("thdw");Integer id2Thd = hId.get("thdr");String sThd = (hId.get("thds") != null ? String.valueOf(hId.get("thds")) : null);
			String err = null;
			try {
				HashMap<String, Integer> hAlreadyInserted = new HashMap<String, Integer>();
				for (int i = 0 ; i < vLine.size() ; i++) {
					String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
					String s = vLine.get(i);
					if (StringUtils.notEmpty(s)) {
						if(id1Qf1 == null && h.equalsIgnoreCase("qf1w")) {
							if (hAlreadyInserted.containsKey(s))
								id1Qf1 = hAlreadyInserted.get(s);
							else {
								id1Qf1 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Qf1);	
							}
						}
						else if(id2Qf1 == null && h.equalsIgnoreCase("qf1r")) {
							if (hAlreadyInserted.containsKey(s))
								id2Qf1 = hAlreadyInserted.get(s);
							else {
								id2Qf1 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id2Qf1);
							}
						}
						else if(id1Qf2 == null && h.equalsIgnoreCase("qf2w")) {
							if (hAlreadyInserted.containsKey(s))
								id1Qf2 = hAlreadyInserted.get(s);
							else {
								id1Qf2 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Qf2);
							}
						}
						else if(id2Qf2 == null && h.equalsIgnoreCase("qf2r")) {
							if (hAlreadyInserted.containsKey(s))
								id2Qf2 = hAlreadyInserted.get(s);
							else {
								id2Qf2 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id2Qf2);
							}
						}
						else if(id1Qf3 == null && h.equalsIgnoreCase("qf3w")) {
							if (hAlreadyInserted.containsKey(s))
								id1Qf3 = hAlreadyInserted.get(s);
							else {
								id1Qf3 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Qf3);
							}
						}
						else if(id2Qf3 == null && h.equalsIgnoreCase("qf3r")) {
							if (hAlreadyInserted.containsKey(s))
								id2Qf3 = hAlreadyInserted.get(s);
							else {
								id2Qf3 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id2Qf3);
							}
						}
						else if(id1Qf4 == null && h.equalsIgnoreCase("qf4w")) {
							if (hAlreadyInserted.containsKey(s))
								id1Qf4 = hAlreadyInserted.get(s);
							else {
								id1Qf4 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Qf4);
							}
						}
						else if(id1Qf4 == null && h.equalsIgnoreCase("qf4r")) {
							if (hAlreadyInserted.containsKey(s))
								id1Qf4 = hAlreadyInserted.get(s);
							else {
								id1Qf4 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Qf4);
							}
						}
						else if(id1Sf1 == null && h.equalsIgnoreCase("sf1w")) {
							if (hAlreadyInserted.containsKey(s))
								id1Sf1 = hAlreadyInserted.get(s);
							else {
								id1Sf1 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Sf1);
							}
						}
						else if(id2Sf1 == null && h.equalsIgnoreCase("sf1r")) {
							if (hAlreadyInserted.containsKey(s))
								id2Sf1 = hAlreadyInserted.get(s);
							else {
								id2Sf1 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id2Sf1);
							}
						}
						else if(id1Sf2 == null && h.equalsIgnoreCase("sf2w")) {
							if (hAlreadyInserted.containsKey(s))
								id1Sf2 = hAlreadyInserted.get(s);
							else {
								id1Sf2 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Sf2);
							}
						}
						else if(id2Sf2 == null && h.equalsIgnoreCase("sf2r")) {
							if (hAlreadyInserted.containsKey(s))
								id2Sf2 = hAlreadyInserted.get(s);
							else {
								id2Sf2 = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id2Sf2);
							}
						}
						else if(id1Thd == null && h.equalsIgnoreCase("thdw")) {
							if (hAlreadyInserted.containsKey(s))
								id1Thd = hAlreadyInserted.get(s);
							else {
								id1Thd = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id1Thd);
							}
						}
						else if(id2Thd == null && h.equalsIgnoreCase("thdr")) {
							if (hAlreadyInserted.containsKey(s))
								id2Thd = hAlreadyInserted.get(s);
							else {
								id2Thd = DatabaseHelper.insertEntity(row, n, idSp, s, null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
								hAlreadyInserted.put(s, id2Thd);
							}
						}
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf1w"))
							updateEntity(row, n, id1Qf1, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf1r"))
							updateEntity(row, n, id2Qf1, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf2w"))
							updateEntity(row, n, id1Qf2, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf2r"))
							updateEntity(row, n, id2Qf2, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf3w"))
							updateEntity(row, n, id1Qf3, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf3r"))
							updateEntity(row, n, id2Qf3, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf4w"))
							updateEntity(row, n, id1Qf4, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("qf4r"))
							updateEntity(row, n, id2Qf4, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("sf1w"))
							updateEntity(row, n, id1Sf1, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("sf1r"))
							updateEntity(row, n, id2Sf1, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("sf2w"))
							updateEntity(row, n, id1Sf2, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("sf2r"))
							updateEntity(row, n, id2Sf2, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("thdw"))
							updateEntity(row, n, id1Thd, s);
						else if (s.matches(".*" + scPattern + ".*") && h.equalsIgnoreCase("thdr"))
							updateEntity(row, n, id2Thd, s);
						else if (h.equalsIgnoreCase("qf1s"))
							sQf1 = s;
						else if (h.equalsIgnoreCase("qf2s"))
							sQf2 = s;
						else if (h.equalsIgnoreCase("qf3s"))
							sQf3 = s;
						else if (h.equalsIgnoreCase("qf4s"))
							sQf4 = s;
						else if (h.equalsIgnoreCase("sf1s"))
							sSf1 = s;
						else if (h.equalsIgnoreCase("sf2s"))
							sSf2 = s;
						else if (h.equalsIgnoreCase("thds"))
							sThd = s;
					}
				}
			}
			catch (Exception e) {
				err = e.getMessage();
				Logger.getLogger("sh").error(e.getMessage(), e);
			}
			if (err != null)
				writeError(vLine, "ERROR: " + err);
			else {
				Draw dr = new Draw();
				dr.setIdResult(rsId);
				dr.setId1Qf1(id1Qf1); dr.setId2Qf1(id2Qf1); dr.setResult_qf1(sQf1);
				dr.setId1Qf2(id1Qf2); dr.setId2Qf2(id2Qf2); dr.setResult_qf2(sQf2);
				dr.setId1Qf3(id1Qf3); dr.setId2Qf3(id2Qf3); dr.setResult_qf3(sQf3);
				dr.setId1Qf4(id1Qf4); dr.setId2Qf4(id2Qf4); dr.setResult_qf4(sQf4);
				dr.setId1Sf1(id1Sf1); dr.setId2Sf1(id2Sf1); dr.setResult_sf1(sSf1);
				dr.setId1Sf2(id1Sf2); dr.setId2Sf2(id2Sf2); dr.setResult_sf2(sSf2);
				dr.setId1Thd(id1Thd); dr.setId2Thd(id2Thd); dr.setResult_thd(sThd);
				dr = (Draw) DatabaseHelper.saveEntity(dr, JMainFrame.getContributor());
				processReport.append("Row " + (row + 1) + ": New Draw | " + dr).append("\r\n");
			}
		}
		return isError;
	}
	
	private boolean processLineRC(int row, Vector<String> vHeader, Vector<String> vLine, boolean isUpdate) throws Exception {
		boolean isError = false;
		List<Integer> lId = null;
		HashMap<String, Integer> hId = new HashMap();
		Integer n = null;
		Integer rcId = null;
		String label = null;
		String type1 = null, type2 = null;
		for (int i = 0 ; i < vLine.size() ; i++) {
			try {
				String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
				String s = vLine.get(i);
				String hql = null;
				if (StringUtils.notEmpty(s)) {
					if (s.matches(".*\\s\\[#\\d+]$"))
						s = s.substring(0, s.indexOf("[#") - 1);
					String s_ = s.replaceAll(scPattern, "_").toLowerCase();
					if (h.equalsIgnoreCase(Sport.alias))
						hql = "select id from Sport where lower(label) like '" + s_ + "'";
					else if (h.equalsIgnoreCase(Championship.alias))
						hql = "select id from Championship where lower(label) like '" + s_ + "'";
					else if (h.matches("ev|se")) {
						String[] tEv = s_.split("\\|");
						hql = "select id from Event where lower(label) like '" + tEv[0] + "'" + (tEv.length > 1 ? " and lower(type.label)='" + tEv[1] + "'" : "") + " order by id";
					}
					else if (h.equalsIgnoreCase("tp1"))
						type1 = s;
					else if (h.equalsIgnoreCase("tp2"))
						type2 = s;
					else if (h.matches("label")) {
						label = s;
						hql = "select id from Record where lower(label)='" + s_.replaceAll("'", "''") + "'" + (type1 != null ? " and lower(type1)='" + type1.toLowerCase() + "'" : "") + (type2 != null ? " and lower(type2)='" + type2.toLowerCase() + "'" : "") + " and sport.id=" + hId.get("sp") + " and championship.id=" + hId.get("cp") + " and event.id=" + hId.get("ev") + (hId.containsKey("se") ? " and subevent.id=" + hId.get("se") : "");
					}
					else if (h.matches("rk\\d")) {
						if (n == null) {
							List<Integer> lNumber = (List<Integer>) DatabaseHelper.execute("select type.number from Event ev where ev.id = " + hId.get(hId.containsKey("se") ? "se" : "ev"));
							if (lNumber != null && lNumber.size() > 0)
								n = lNumber.get(0);
						}
						if (n != null) {
							if (type1.equalsIgnoreCase("individual")) { // Athlete
								n = 1;
								if (!s_.matches(StringUtils.PATTERN_ATHLETE)) {
									isError = true;
									writeError(vLine, "ERROR: Invalid Format (Column " + h.toUpperCase() + ")");
								}
								else {
									boolean isCountryTeam = s_.matches(".*\\([a-z]{3}\\,\\s.+\\)$");
									boolean isCountry = s_.matches(".*\\([a-z]{3}\\)$");
									boolean isTeam = (!isCountry && s_.matches(".*\\([^\\,\\(\\)]+\\)$")); 
									hql = "select id from Athlete where sport.id=" + hId.get("sp") + " and lower(last_name) || ', ' || lower(first_name) " + (isCountryTeam ? " || ' (' || lower(country.code) " + " || ', ' || lower(team.label) || ')'" : (isCountry ? " || ' (' || lower(country.code) || ')'" : (isTeam ? " || ' (' || lower(team.label) || ')'" : ""))) + " like '" + s_ + "'";
								}
							}
							else if (type1.equalsIgnoreCase("team")) { // Team
								n = 50;
								if (!s_.matches(StringUtils.PATTERN_TEAM)) {
									isError = true;
									writeError(vLine, "ERROR: Invalid Format (Column " + h.toUpperCase() + ")");
								}
								else {
									// Get record year
									String rcy = null;
									if (vLine.size() > i + 2) {
										rcy = vLine.get(i + 2);
										rcy = (StringUtils.notEmpty(rcy) && rcy.matches(".*\\d{4}$") ? rcy.substring(rcy.length() - 4) : null);
									}
									hql = "select id from Team where sport.id=" + hId.get("sp") + " and lower(label) like '" + (s_.indexOf(" (") > -1 ? s_.substring(0, s_.indexOf(" (")) : s_) + "'" + (rcy != null ? " and '" + rcy + "' between year1 and (case year2 when null then '9999' when '' then '9999' else year2 end)" : "");
								}
							}
							else if (n == 99) { // Country
								if (!s_.matches(StringUtils.PATTERN_COUNTRY)) {
									isError = true;
									writeError(vLine, "ERROR: Invalid Format (Column " + h.toUpperCase() + ")");
								}
								else
									hql = "select id from Country where lower(code) like '" + s_ + "'";
							}
						}
					}
				}
				if (hql != null) {
//					Logger.getLogger("sh").info(hql);
					lId = (List<Integer>) DatabaseHelper.execute(hql);
					if (lId != null && lId.size() > 0) {
						hId.put(h, lId.get(0));
						vLine.set(i, s + " [#" + lId.get(0) + "]");
					}
					else {
						if (h.equalsIgnoreCase(Sport.alias)) {
							isError = true;
							writeError(vLine, "ERROR: Invalid Sport");
						}
						else if (h.equalsIgnoreCase(Championship.alias)) {
							isError = true;
							writeError(vLine, "ERROR: Invalid Championship");
						}
						else if (h.matches("ev|se")) {
							isError = true;
							writeError(vLine, "ERROR: Invalid Event");
						}
						vLine.set(i, s);
					}
				}
				else {
					if (h.matches("rc\\d") && StringUtils.notEmpty(s) && s.length() > 15) {
						isError = true;
						writeError(vLine, "ERROR: Invalid Record - Too long (Column " + h.toUpperCase() + ")");
					}
				}
			}
			catch (Exception e) {
				isError = true;
				writeError(vLine, "ERROR: " + e.getMessage());
			}
			if (isError)
				break;
		}
		if (isUpdate) {
			rcId = hId.get("label");
			Integer idSp = hId.get("sp");
			Integer idCp = hId.get("cp");
			Integer idEv = hId.get("ev");
			Integer idSe = hId.get("se");
			Integer idRk1 = hId.get("rk1");
			Integer idRk2 = hId.get("rk2");
			Integer idRk3 = hId.get("rk3");
			Integer idRk4 = hId.get("rk4");
			Integer idRk5 = hId.get("rk5");
			String rc1 = null, rc2 = null, rc3 = null, rc4 = null, rc5 = null;
			String dt1 = null, dt2 = null, dt3 = null, dt4 = null, dt5 = null;
			String exa = null, cmt = null;
			Float idx = null;
			String err = null;
			try {
				for (int i = 0 ; i < vLine.size() ; i++) {
					String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
					String s = vLine.get(i);
					if (StringUtils.notEmpty(s)) {
						if(idRk1 == null && h.equalsIgnoreCase("rk1"))
							idRk1 = DatabaseHelper.insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if(idRk2 == null && h.equalsIgnoreCase("rk2"))
							idRk2 = DatabaseHelper.insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if(idRk3 == null && h.equalsIgnoreCase("rk3"))
							idRk3 = DatabaseHelper.insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if(idRk4 == null && h.equalsIgnoreCase("rk4"))
							idRk4 = DatabaseHelper.insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if(idRk5 == null && h.equalsIgnoreCase("rk5"))
							idRk5 = DatabaseHelper.insertEntity(row, n, idSp, s, vLine.size() > i + 2 ? vLine.get(i + 2) : null, JMainFrame.getContributor(), processReport, ResourceUtils.LGDEFAULT);
						else if (h.equalsIgnoreCase("rc1"))
							rc1 = s;
						else if (h.equalsIgnoreCase("rc2"))
							rc2 = s;
						else if (h.equalsIgnoreCase("rc3"))
							rc3 = s;
						else if (h.equalsIgnoreCase("rc4"))
							rc4 = s;
						else if (h.equalsIgnoreCase("rc5"))
							rc5 = s;
						else if (h.equalsIgnoreCase("dt1"))
							dt1 = s;
						else if (h.equalsIgnoreCase("dt2"))
							dt2 = s;
						else if (h.equalsIgnoreCase("dt3"))
							dt3 = s;
						else if (h.equalsIgnoreCase("dt4"))
							dt4 = s;
						else if (h.equalsIgnoreCase("dt5"))
							dt5 = s;
						else if (h.equalsIgnoreCase("idx"))
							idx = Float.valueOf(s);
						else if (h.equalsIgnoreCase("exa"))
							exa = s;
						else if (h.equalsIgnoreCase("cmt"))
							cmt = s;
					}
				}
			}
			catch (Exception e) {
				err = e.getMessage();
				Logger.getLogger("sh").error(e.getMessage(), e);
			}
			if (err != null)
				writeError(vLine, "ERROR: " + err);
			else {
				Record rc = null;
				if (rcId != null)
					rc = (Record) DatabaseHelper.loadEntity(Record.class, rcId);
				else {
					rc = new Record();
					rc.setLabel(label);
					rc.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, idSp));
					rc.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, idCp));
					rc.setEvent((Event)DatabaseHelper.loadEntity(Event.class, idEv != null ? idEv : 0));
					rc.setSubevent((Event)DatabaseHelper.loadEntity(Event.class, idSe != null ? idSe : 0));
					rc.setType1(type1);
					rc.setType2(type2);
				}
				rc.setIdRank1(idRk1);
				rc.setIdRank2(idRk2);
				rc.setIdRank3(idRk3);
				rc.setIdRank4(idRk4);
				rc.setIdRank5(idRk5);
				rc.setRecord1(rc1);
				rc.setRecord2(rc2);
				rc.setRecord3(rc3);
				rc.setRecord4(rc4);
				rc.setRecord5(rc5);
				rc.setDate1(dt1);
				rc.setDate2(dt2);
				rc.setDate3(dt3);
				rc.setDate4(dt4);
				rc.setDate5(dt5);
				rc.setIndex(idx);
				rc.setExa(StringUtils.notEmpty(exa) ? exa : null);
				rc.setComment(StringUtils.notEmpty(cmt) ? cmt : null);
				rc = (Record) DatabaseHelper.saveEntity(rc, JMainFrame.getContributor());
				processReport.append("Row " + (row + 1) + ": Record " + (rcId != null ? "Updated" : "Created") + " | " + rc).append("\r\n");
			}
		}
		return isError;
	}
	
	private void updateEntity(int row, int n, int id, String s) throws Exception {
		Object o = null;
		String msg = null;
		try {
			if (s.matches(".*\\s\\[#\\d+]$"))
				s = s.substring(0, s.indexOf("[#") - 1);
			if (n < 10) {
				int p = s.indexOf(", ");
				Athlete a = (Athlete) DatabaseHelper.loadEntity(Athlete.class, id);
				String lastName = (s.substring(0, p > -1 ? p : s.indexOf(" (")));
				String firstName = (p > -1 ? s.substring(p + 2, s.indexOf(" (")) : null);
				if ((lastName + firstName).matches(".*" + scPattern + ".*") && (!lastName.equals(a.getLastName()) || !firstName.equals(a.getFirstName()))) {
					a.setLastName(lastName);
					a.setFirstName(firstName);
					o = DatabaseHelper.saveEntity(a, JMainFrame.getContributor());
					msg = "Update Athlete";
				}
			}
			else if (n == 50) {
				Team t = (Team) DatabaseHelper.loadEntity(Team.class, id);
				String label = s;
				if (s.matches(".*\\([A-Z]{3}\\).*")) {
					int p = s.indexOf(" (") + 2;
					label = s.substring(0, p - 2);
				}
				if (label.matches(".*" + scPattern + ".*") && !label.equals(t.getLabel())) {
					t.setLabel(label);
					o = DatabaseHelper.saveEntity(t, JMainFrame.getContributor());
					msg = "Update Team";
				}
			}
		}
		finally {
			if (o != null)
				processReport.append("Row " + (row + 1) + ": " + msg + " | " + o).append("\r\n");
		}
	}
	
	private void writeError(Vector<String> vLine, String msg) {
		if (vLine != null && !StringUtils.notEmpty(vLine.get(0)))
			vLine.set(0, msg);
	}

}