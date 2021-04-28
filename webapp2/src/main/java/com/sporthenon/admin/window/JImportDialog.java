package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.swing.table.DefaultTableModel;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.container.JTopPanel;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.ImportUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class JImportDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JImportDialog.class.getName());
	
	private JRadioButton jTypeRS;
	private JRadioButton jTypeRD;
	private JTextField jFile;
	private JFileChooser jFileChooser;
	private JCustomButton jProcessButton;
	private JCustomButton jUpdateButton;
	private JScrollPane jScrollPane;
	private JTable jProcessTable;
	private JProgressBar jProgressBar;
	private JImportReportDialog jImportReportDialog;
	private Vector<String> vHeader = null;

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
		JCustomButton btn = new JCustomButton(null, "foldertable.png", null);
		btn.setToolTipText("Browse");
		btn.setActionCommand("browse");
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.addActionListener(this);
		p.add(btn);
		jTypeRS = new JRadioButton("Results");
		jTypeRD = new JRadioButton("Rounds");
		jTypeRS.setSelected(true);
		ButtonGroup grp = new ButtonGroup();
		grp.add(jTypeRS);
		grp.add(jTypeRD);
		p.add(new JLabel(" Import type:"));
		p.add(jTypeRS);
		p.add(jTypeRD);
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
		jUpdateButton = new JCustomButton("Update", "save.png", null);
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
		btn = new JCustomButton("Rounds (XLS)", null, null);
		btn.setActionCommand("template-RD-xls");
		btn.addActionListener(this);
		p.add(btn);
		btn = new JCustomButton("Rounds (CSV)", null, null);
		btn.setActionCommand("template-RD-csv");
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
		((DefaultTableModel) jProcessTable.getModel()).setRowCount(0);
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
					jImportReportDialog.disableOk();
					jImportReportDialog.open();
					processAll(true, true);
					jImportReportDialog.enableOk();
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
				List<List<String>> list = ImportUtils.getTemplate(type, "en");
				File f = jFileChooser.getSelectedFile();
				try (FileOutputStream fos = new FileOutputStream(new File(f.getAbsolutePath() + "\\SH-Template-" + type + "." + ext));) {
					if (ext.equalsIgnoreCase("xls")) {
						ExportUtils.buildXLS(fos, null, null, list, null, new boolean[]{false});
					}
					else if (ext.equalsIgnoreCase("csv")) {
						StringBuffer sb = new StringBuffer();
						for (List<String> list_ : list) {
							int i = 0;
							for (String s : list_) {
								sb.append(i++ > 0 ? ";" : "").append(s.replaceFirst("^\\#color.+\\#", ""));
							}
							sb.append("\r\n");
						}
						fos.write(sb.toString().getBytes());
					}
				}
				catch (Exception e_) {
					log.log(Level.WARNING, e_.getMessage(), e_);
				}
			}
		}
	}

	private void incrementProgress(float pg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jProgressBar.setValue(Math.round(pg));
			}
		});
	}

	private Vector<Vector<String>> getTableAsVector() throws Exception {
		final int n = jProcessTable.getColumnCount();
		Vector<Vector<String>> v = new Vector<Vector<String>>();
		for (int i = 0 ; i < jProcessTable.getRowCount() ; i++) {
			Vector<String> v_ = new Vector<String>();
			for (int j = 0 ; j < n ; j++) {
				Object o = jProcessTable.getValueAt(i, j);
				v_.add(o != null ? String.valueOf(o) : "");
			}
			v.add(v_);
		}
		if (v.isEmpty() && StringUtils.notEmpty(jFile.getText())) {
			try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(jFile.getText()), "UTF8"));) {
				String s = null;
				while ((s = bf.readLine()) != null) {
					if (StringUtils.notEmpty(s)) {
						Vector<String> v_ = new Vector<>();
						for (String s_ : s.split(";", -1)) {
							v_.add(s_.trim());
						}
						v.add(v_);
					}
				}
			}
		}
		return v;
	}

	private void processAll(boolean isReprocess, boolean isUpdate) {
		try {
			jUpdateButton.setEnabled(false);
			jProgressBar.setValue(0);
			jScrollPane.setViewportView(null);
			final Map<String, String> hTitle = new HashMap<String, String>();
			Vector<String> vHeaderLabel = new Vector<String>();
			if (jTypeRS.isSelected()) {
				vHeader = new Vector<String>(Arrays.asList(new String[] {"msg", "sp", "cp", "ev", "se", "se2", "yr", "rk1", "rs1", "rk2", "rs2", "rk3", "rs3", "rk4", "rs4", "rk5", "rs5", "rk6", "rs6", "rk7", "rs7", "rk8", "rs8", "rk9", "rs9", "rk10", "rs10", "rk11", "rs11", "rk12", "rs12", "rk13", "rs13", "rk14", "rs14", "rk15", "rs15", "rk16", "rs16", "rk17", "rs17", "rk18", "rs18", "rk19", "rs19", "rk20", "rs20", "dt1", "dt2", "pl1", "pl2", "exa", "cmt", "exl"}));
				hTitle.put("msg", "Message");
				hTitle.put("sp", "Sport");
				hTitle.put("cp", "Championship");
				hTitle.put("ev", "Event #1");
				hTitle.put("se", "Event #2");
				hTitle.put("se2", "Event #3");
				hTitle.put("yr", "Year");
				for (int i = 1 ; i <= 20 ; i++) {
					hTitle.put("rk" + i, "Rank #" + i);
					hTitle.put("rs" + i, "Result #" + i);
				}
				hTitle.put("dt1", "Date #1");
				hTitle.put("dt2", "Date #2");
				hTitle.put("pl1", "Place #1");
				hTitle.put("pl2", "Place #2");
				hTitle.put("exa", "Tie");
				hTitle.put("cmt", "Comment");
				hTitle.put("exl", "External Link");
			}
			else if (jTypeRD.isSelected()) {
				vHeader = new Vector<String>(Arrays.asList(new String[] {"idr", "rt", "rk1", "rs1", "rk2", "rs2", "rk3", "rs3", "rk4", "rs4", "rk5", "rs5", "pl1", "pl2", "dt1", "dt2", "exa", "cmt"}));
				hTitle.put("idr", "ID Result");
				hTitle.put("rt", "Round Type");
				for (int i = 1 ; i <= 5 ; i++) {
					hTitle.put("rk" + i, "Rank #" + i);
					hTitle.put("rs" + i, "Result #" + i);	
				}
				hTitle.put("pl1", "Place #1");
				hTitle.put("pl2", "Place #2");
				hTitle.put("dt1", "Date #1");
				hTitle.put("dt2", "Date #2");
				hTitle.put("exa", "Tie");
				hTitle.put("cmt", "Comment");
			}
			for (String s : vHeader) {
				vHeaderLabel.add(hTitle.get(s));
			}
			Vector<Vector<String>> vFile = getTableAsVector();
			int i = 0;
			float pg = 0.0f;
			boolean isError = false;
			for (Vector<String> v : vFile) {
				StringBuffer processReport = new StringBuffer();
				if (!isReprocess) {
					v.insertElementAt("", 0);
				}
				else {
					v.set(0, "");
				}
				if (jTypeRS.isSelected()) {
					isError |= ImportUtils.processLineRS(i, vHeader, v, isUpdate, processReport, JMainFrame.getContributor(), ResourceUtils.LGDEFAULT);
				}
				else if (jTypeRD.isSelected()) {
					isError |= ImportUtils.processLineRC(i, vHeader, v, isUpdate, processReport, JMainFrame.getContributor(), ResourceUtils.LGDEFAULT);
				}
				if (i * 100 / vFile.size() > pg) {
					pg = i * 100 / vFile.size();
					incrementProgress(pg);
				}
				jImportReportDialog.getReport().append(processReport.toString());
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
					else {
						component.setBackground(Color.white);
					}
					return component;
				}
			});
			jProcessTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			for (int j = 0 ; j < jProcessTable.getColumnCount() ; j++) {
				String h = vHeader.get(j);
				jProcessTable.getColumnModel().getColumn(j).setPreferredWidth(h.matches("rk\\d|pl\\d") ? 200 : (h.matches("(rs|dt)\\d") ? 80 : 150));
			}
			jProcessTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jProcessTable.getTableHeader().setReorderingAllowed(false);
			jScrollPane.setViewportView(jProcessTable);
			jProcessButton.setText("Reprocess");
		}
		catch (Exception e_) {
			log.log(Level.WARNING, e_.getMessage(), e_);
		}
	}

}