package com.sporthenon.updater.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.Year;
import com.sporthenon.updater.component.JCustomButton;
import com.sporthenon.updater.container.JTopPanel;
import com.sporthenon.utils.StringUtils;

public class JImportDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final String scPattern = "[^a-zA-Z0-9\\|\\,\\s\\(\\)_]";
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
		jContentPane.add(getSettingsPanel(), BorderLayout.NORTH);
		jContentPane.add(getTablePanel(), BorderLayout.CENTER);
	}

	private JPanel getSettingsPanel() {
		JPanel p = new JPanel(new FlowLayout(0, 2, 2));
		p.setBorder(BorderFactory.createTitledBorder(null, "Import File", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		JLabel label = new JLabel("CSV File:");
		p.add(label);
		jFile = new JTextField(35);
		p.add(jFile);
		JCustomButton btn = new JCustomButton(null, "common/folder.png");
		btn.setToolTipText("Browse");
		btn.setActionCommand("browse");
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.addActionListener(this);
		p.add(btn);
		jProcessButton = new JCustomButton("Process", "updater/ok.png");
		jProcessButton.setActionCommand("process");
		jProcessButton.setEnabled(false);
		jProcessButton.addActionListener(this);
		p.add(jProcessButton);
		jUpdateButton = new JCustomButton("Update", "updater/update.png");
		jUpdateButton.setActionCommand("update");
		jUpdateButton.setEnabled(false);
		jUpdateButton.addActionListener(this);
		p.add(jUpdateButton);
		btn = new JCustomButton("Close", "updater/cancel.png");
		btn.setActionCommand("close");
		btn.addActionListener(this);
		p.add(btn);
		jProgressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
		jProgressBar.setValue(0);
		jProgressBar.setStringPainted(true);
		p.add(jProgressBar);
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
		//this.parent = parent;
		jFile.setText(null);
		jProcessButton.setEnabled(false);
		jScrollPane.setViewportView(null);
		jProcessButton.setText("Process");
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
					//jUpdateButton.setEnabled(true);
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
		v.add(new Vector());
		for (int i = 0 ; i < jProcessTable.getRowCount() ; i++) {
			Vector<String> v_ = new Vector<String>();
			for (int j = 0 ; j < n ; j++)
				v_.add(String.valueOf(jProcessTable.getValueAt(i, j)));
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
			hTitle.put("msg", "Message");
			hTitle.put("sp", "Sport");
			hTitle.put("cp", "Championship");
			hTitle.put("ev", "Event");
			hTitle.put("se", "Subevent");
			hTitle.put("yr", "Year");
			hTitle.put("rk1", "Rank 1");
			hTitle.put("rk2", "Rank 2");
			hTitle.put("rk3", "Rank 3");
			hTitle.put("rk4", "Rank 4");
			hTitle.put("rk5", "Rank 5");
			hTitle.put("rs1", "Result 1");
			hTitle.put("rs2", "Result 2");
			hTitle.put("rs3", "Result 3");
			hTitle.put("rs4", "Result 4");
			hTitle.put("rs5", "Result 5");
			hTitle.put("dt1", "Date 1");
			hTitle.put("dt2", "Date 2");
			hTitle.put("pl", "Place");
			hTitle.put("cmt", "Comment");
			Vector<Vector<String>> vFile = (isReprocess ? getTableAsVector() : StringUtils.readCSV(jFile.getText()));
			Vector<String> vHeaderLabel = new Vector<String>();
			int i = 0;
			float pg = 0.0f;
			boolean isError = false;
			for (Vector<String> v : vFile) {
				if (i == 0) {
					if (vHeader == null) {
						vHeader = new Vector<String>();
						vHeader.add("msg");
						vHeader.addAll(v);
					}
					for (String s : vHeader)
						vHeaderLabel.add(hTitle.get(s));
				}
				else {
					if (!isReprocess)
						v.insertElementAt("", 0);
					else
						v.set(0, "");
					isError |= processLine(i, vHeader, v, isUpdate);
				}
				if (i * 100 / vFile.size() > pg) {
					incrementProgress();
					pg = i * 100 / vFile.size();
				}
				i++;
			}
			jUpdateButton.setEnabled(!isError);
			jProgressBar.setValue(100);
			vFile.remove(0);
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
				jProcessTable.getColumnModel().getColumn(j).setPreferredWidth(h.matches("rk\\d|pl") ? 200 : (h.matches("(rs|dt)\\d") ? 80 : 150));
			}
			jProcessTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jScrollPane.setViewportView(jProcessTable);
			jProcessButton.setText("Reprocess");
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
		}
	}

	private boolean processLine(int row, Vector<String> vHeader, Vector<String> vLine, boolean isUpdate) throws Exception {
		boolean isError = false;
		List<Integer> lId = null;
		HashMap<String, Integer> hId = new HashMap();
		Integer n = null;
		boolean isComplex = false;
		for (int i = 1 ; i < vLine.size() ; i++) {
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
				else if (h.equalsIgnoreCase(Year.alias))
					hql = "select id from Year where lower(label) like '" + s_ + "'";
				else if (h.equalsIgnoreCase("pl")) {
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
						h = "cx";
						hql = "select id from Complex where lower(city.country.code) like '" + cn + "' and lower(city.label) like '" + ct + "' and lower(label) like '" + cx + "'";
						isComplex = true;
					}
					else {
						h = "ct";
						hql = "select id from City where lower(country.code) like '" + cn + "' and lower(label) like '" + ct + "'";
					}
				}
				else if (h.matches("rk\\d")) {
					if (n == null) {
						List<Integer> lNumber = (List<Integer>) DatabaseHelper.execute("select type.number from Event ev where ev.id = " + hId.get(hId.containsKey("se") ? "se" : "ev"));
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
								hql = "select id from Team where lower(label) like '" + (s_.indexOf(" (") > -1 ? s_.substring(0, s_.indexOf(" (")) : s_) + "' and id_parent is null";
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
				//Logger.getLogger("sh").debug(hql);
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
					vLine.set(i, s);
				}
			}
			else {
				if (h.matches("dt\\d") && StringUtils.notEmpty(s) && !s.matches("\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d")) {
					isError = true;
					writeError(vLine, "ERROR: Invalid Date (Column " + h.toUpperCase() + ")");
				}
			}
		}
		if (isUpdate) {
			Integer idSp = hId.get("sp");
			Integer idCp = hId.get("cp");
			Integer idEv = hId.get("ev");
			Integer idSe = hId.get("se");
			Integer idYr = hId.get("yr");
			Integer idCx = hId.get("cx");
			Integer idCt = hId.get("ct");
			Integer idRk1 = hId.get("rk1");Integer idRk2 = hId.get("rk2");Integer idRk3 = hId.get("rk3");Integer idRk4 = hId.get("rk4");Integer idRk5 = hId.get("rk5");
			String rs1 = null;String rs2 = null;String rs3 = null;String rs4 = null;String rs5 = null;
			String dt1 = null;String dt2 = null;
			String cmt = null;
			String err = null;
			try {
				for (int i = 0 ; i < vLine.size() ; i++) {
					String h = vHeader.get(i).replaceAll(scPattern, "").toLowerCase();
					String s = vLine.get(i);
					if (StringUtils.notEmpty(s)) {
						if(idRk1 == null && h.equalsIgnoreCase("rk1"))
							idRk1 = insertEntity(row, n, idSp, s);
						else if(idRk2 == null && h.equalsIgnoreCase("rk2"))
							idRk2 = insertEntity(row, n, idSp, s);
						else if(idRk3 == null && h.equalsIgnoreCase("rk3"))
							idRk3 = insertEntity(row, n, idSp, s);
						else if(idRk4 == null && h.equalsIgnoreCase("rk4"))
							idRk4 = insertEntity(row, n, idSp, s);
						else if(idRk5 == null && h.equalsIgnoreCase("rk5"))
							idRk5 = insertEntity(row, n, idSp, s);
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
						else if (idCx == null && idCt == null && h.equalsIgnoreCase("pl")) {
							if (isComplex)
								idCx = insertPlace(row, s);
							else
								idCt = insertPlace(row, s);
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
				Result rs = new Result();
				rs.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, idSp));
				rs.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, idCp));
				rs.setEvent((Event)DatabaseHelper.loadEntity(Event.class, idEv != null ? idEv : 0));
				rs.setSubevent((Event)DatabaseHelper.loadEntity(Event.class, idSe != null ? idSe : 0));
				rs.setYear((Year)DatabaseHelper.loadEntity(Year.class, idYr));
				rs.setComplex((Complex)DatabaseHelper.loadEntity(Complex.class, idCx));
				rs.setCity((City)DatabaseHelper.loadEntity(City.class, idCt));
				rs.setDate1(StringUtils.notEmpty(dt1) ? dt1 : "");
				rs.setDate2(StringUtils.notEmpty(dt2) ? dt2 : "");
				rs.setComment(StringUtils.notEmpty(cmt) ? cmt : "");
				rs.setIdRank1(idRk1);
				rs.setIdRank2(idRk2);
				rs.setIdRank3(idRk3);
				rs.setIdRank4(idRk4);
				rs.setIdRank5(idRk5);
				rs.setResult1(rs1);
				rs.setResult2(rs2);
				rs.setResult3(rs3);
				rs.setResult4(rs4);
				rs.setResult5(rs5);
				rs = (Result) DatabaseHelper.saveEntity(rs, JMainFrame.getMember());
				processReport.append("Row " + row + ": New Result | " + rs).append("\r\n");
			}
		}
		return isError;
	}

	private Integer insertEntity(int row, int n, int idSp, String s) throws Exception {
		Integer id = null;
		Object o = null;
		String msg = null;
		try {
			if (n < 10) {
				int p = s.indexOf(", ");
				int p_ = (s.indexOf(" (") > -1  ? s.indexOf(" (") : s.length());
				Athlete a = new Athlete();
				a.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, idSp));
				a.setLastName(s.substring(0, p > -1 ? p : p_));
				a.setFirstName(p > -1 && s.charAt(p + 2) != '(' ? s.substring(p + 2, p_) : null);
				boolean isCountryTeam = s.toLowerCase().matches(".*\\([a-z]{3}\\,\\s.+\\)$");
				boolean isCountry = s.toLowerCase().matches(".*\\([a-z]{3}\\)$");
				boolean isTeam = (!isCountry && s.toLowerCase().matches(".*\\([^\\,\\(\\)]+\\)$")); 
				if (isCountry || isCountryTeam) { // Country set
					p = s.indexOf(" (") + 2;
					String countryCode = s.substring(p, p + 3).toLowerCase();
					Object o_ = DatabaseHelper.loadEntityFromQuery("from Country cn where lower(cn.code) = '" + countryCode + "'");
					if (o_ == null)
						throw new Exception("Invalid Country: " + countryCode.toUpperCase());
					a.setCountry((Country)o_);
				}
				if (isTeam || isCountryTeam) { // Team set
					p = s.lastIndexOf(isCountryTeam ? ", " : " (") + 2;
					String tm = s.substring(p, s.length() - 1);
					Object o_ = DatabaseHelper.loadEntityFromQuery("from Team tm where lower(tm.label) = '" + tm.toLowerCase() + "'");
					if (o_ == null) {
						Integer idTm = insertEntity(row, 50, idSp, tm);
						o_ = DatabaseHelper.loadEntity(Team.class, idTm);
					}
					a.setTeam((Team)o_);
				}
				o = a;
				o = DatabaseHelper.saveEntity(a, JMainFrame.getMember());
				msg = "New Athlete";
			}
			else if (n == 50) {
				Team t = new Team();
				t.setLabel(s);
				t.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, idSp));
				if (s.matches(".*\\([A-Z]{3}\\)$")) {
					int p = s.indexOf(" (") + 2;
					String countryCode = s.substring(p, p + 3).toLowerCase();
					Object o_ = DatabaseHelper.loadEntityFromQuery("from Country cn where lower(cn.code) = '" + countryCode + "'");
					if (o_ == null)
						throw new Exception("Invalid Country: " + countryCode.toUpperCase());
					t.setLabel(s.substring(0, p - 2));
					t.setCountry((Country)o_);
				}
				o = t;
				o = DatabaseHelper.saveEntity(t, JMainFrame.getMember());
				msg = "New Team";
			}
		}
		finally {
			if (o != null) {
				processReport.append("Row " + row + ": " + msg + " | " + o).append("\r\n");
				id = Integer.valueOf(String.valueOf(o.getClass().getMethod("getId").invoke(o)));
			}
		}
		return id;
	}

	private Integer insertPlace(int row, String s) throws Exception {
		Integer id = null;
		Object o = null;
		String msg = null;
		try {
			String[] t = s.split("\\,\\s");
			String cx = null;
			String ct = null;
			String st = null;
			String cn = t[t.length - 1];
			if (t.length > 2 && t[t.length - 2].length() == 2)
				st = t[t.length - 2];
			if (t.length > (st != null ? 3 : 2)) {
				cx = t[0];
				ct = t[1];
			}
			else
				ct = t[0];
			City ct_ = null;
			if (cx != null) { // Set City (for complex)
				Object o_ = DatabaseHelper.loadEntityFromQuery("from City ct where lower(ct.label) like '" + ct.toLowerCase() + "' and lower(country.code) = '" + cn.toLowerCase() + "'");
				if (o_ == null) {
					Integer idCt = insertPlace(row, ct + (st != null ? ", " + st : "") + ", " + cn);
					o_ = DatabaseHelper.loadEntity(City.class, idCt);
				}
				ct_ = (City)o_;
			}
			State st_ = null;
			if (st != null) { // Set State
				Object o_ = DatabaseHelper.loadEntityFromQuery("from State st where lower(st.code) = '" + st.toLowerCase() + "'");
				if (o_ == null)
					throw new Exception("Invalid State: " + st.toUpperCase());
				st_ = (State)o_;
			}
			Country cn_ = null;
			if (cn != null) { // Set Country
				Object o_ = DatabaseHelper.loadEntityFromQuery("from Country cn where lower(cn.code) = '" + cn.toLowerCase() + "'");
				if (o_ == null)
					throw new Exception("Invalid Country: " + cn.toUpperCase());
				cn_ = (Country)o_;
			}
			if (cx != null) {
				Complex c = new Complex();
				c.setLabel(cx);
				c.setCity(ct_);
				o = c;
				o = DatabaseHelper.saveEntity(c, JMainFrame.getMember());
				msg = "New Complex";
			}
			else if (ct != null) {
				City c = new City();
				c.setLabel(ct);
				c.setState(st_);
				c.setCountry(cn_);
				o = c;
				o = DatabaseHelper.saveEntity(c, JMainFrame.getMember());
				msg = "New City";
			}
		}
		finally {
			if (o != null) {
				processReport.append("Row " + row + ": " + msg + " | " + o).append("\r\n");
				id = Integer.valueOf(String.valueOf(o.getClass().getMethod("getId").invoke(o)));
			}
		}
		return id;
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
					o = DatabaseHelper.saveEntity(a, JMainFrame.getMember());
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
					o = DatabaseHelper.saveEntity(t, JMainFrame.getMember());
					msg = "Update Team";
				}
			}
		}
		finally {
			if (o != null)
				processReport.append("Row " + row + ": " + msg + " | " + o).append("\r\n");
		}
	}
	
	private void writeError(Vector<String> vLine, String msg) {
		if (vLine != null && !StringUtils.notEmpty(vLine.get(0)))
			vLine.set(0, msg);
	}

}