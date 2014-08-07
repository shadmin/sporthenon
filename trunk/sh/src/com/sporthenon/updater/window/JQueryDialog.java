package com.sporthenon.updater.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import sun.net.www.content.text.PlainTextInputStream;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.updater.component.JCustomButton;
import com.sporthenon.updater.component.JDialogButtonBar;
import com.sporthenon.utils.ConfigUtils;

public class JQueryDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JTextArea jQuery = null;
	private JScrollPane jResultPane = null;
	private JTable jResult = null;
	private static ArrayList<String> QUERIES;
	
	static {
		QUERIES = new ArrayList<String>();
		QUERIES.add("SELECT DISTINCT LAST_NAME || ',' || FIRST_NAME || ',' || ID_SPORT AS N, COUNT(*) AS C\r\nFROM \"PERSON\"\r\nWHERE LINK IS NULL\r\nGROUP BY N\r\nORDER BY C DESC\r\nLIMIT 100");
		QUERIES.add("SELECT 'EV', ID, LABEL FROM \"EVENT\"\r\nWHERE ID NOT IN (SELECT ID_EVENT FROM \"RESULT\" WHERE ID_EVENT IS NOT NULL) AND ID NOT IN (SELECT ID_SUBEVENT FROM \"RESULT\" WHERE ID_SUBEVENT IS NOT NULL)\r\nAND ID NOT IN (SELECT ID_EVENT FROM \"RECORD\" WHERE ID_EVENT IS NOT NULL) AND ID NOT IN (SELECT ID_SUBEVENT FROM \"RECORD\" WHERE ID_SUBEVENT IS NOT NULL)\r\nUNION SELECT 'CP', ID, LABEL FROM \"CHAMPIONSHIP\" WHERE ID NOT IN (SELECT ID_CHAMPIONSHIP FROM \"RESULT\" WHERE ID_CHAMPIONSHIP IS NOT NULL)\r\nAND ID NOT IN (SELECT ID_CHAMPIONSHIP FROM \"RECORD\" WHERE ID_CHAMPIONSHIP IS NOT NULL)\r\nORDER BY 1, 3");
		QUERIES.add("SELECT SP.label AS SPORT, CP.label AS CHAMPIONSHIP, EV.label AS EVENT, SE.label AS SUBEVENT, YR.label AS YEAR\r\nFROM (SELECT DISTINCT id_sport, id_championship, id_event, id_subevent FROM \"RESULT\" EXCEPT SELECT DISTINCT id_sport, id_championship, id_event, id_subevent FROM \"RESULT\" WHERE id_year = (SELECT id FROM \"YEAR\" WHERE label = '#YEAR#')) T\r\nLEFT JOIN \"SPORT\" SP ON T.id_sport = SP.id\r\nLEFT JOIN \"CHAMPIONSHIP\" CP ON T.id_championship = CP.id\r\nLEFT JOIN \"EVENT\" EV ON T.id_event = EV.id\r\nLEFT JOIN \"EVENT\" SE ON T.id_subevent = SE.id\r\nLEFT JOIN \"YEAR\" YR ON YR.label = '#YEAR#'\r\nWHERE EV.inactive = FALSE AND (SE.id IS NULL OR SE.inactive = FALSE)\r\nORDER BY SP.label, CP.index, CP.label, EV.index, EV.label, SE.index, SE.label");
	}
	
	public JQueryDialog(JFrame owner) {
		super(owner);
		initialize();
	}
	
	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(700, 550));
		this.setSize(this.getPreferredSize());
		this.setModal(false);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setTitle("Query");
		this.setContentPane(jContentPane);
		
		BorderLayout layout = new BorderLayout();
		layout.setVgap(5);
		JDialogButtonBar jButtonBar = new JDialogButtonBar(this);
		jButtonBar.getCancel().setVisible(false);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2));
		jContentPane.setLayout(layout);
		jContentPane.add(getQueryPanel(), BorderLayout.NORTH);
		jContentPane.add(getResultPanel(), BorderLayout.CENTER);
		jContentPane.add(getButtonPanel(), BorderLayout.SOUTH);
	}
	
	private JPanel getQueryPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEtchedBorder());

		jQuery = new JTextArea();
		JScrollPane jScrollPane = new JScrollPane(jQuery);
		jScrollPane.setPreferredSize(new Dimension(0, 150));
		p.add(jScrollPane, BorderLayout.CENTER);
		
		JPanel p_ = new JPanel(new FlowLayout());
		JCustomButton jExecuteButton = new JCustomButton("Execute", "ok.png", null);
		jExecuteButton.setActionCommand("execute");
		jExecuteButton.addActionListener(this);
		p_.add(jExecuteButton);
		JCustomButton jCloseButton = new JCustomButton("Close", "cancel.png", null);
		jCloseButton.setActionCommand("close");
		jCloseButton.addActionListener(this);
		p_.add(jCloseButton);
		p.add(p_, BorderLayout.SOUTH);
		
		return p;
	}
	
	private JPanel getResultPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEmptyBorder());

		jResult = new JTable();
		jResultPane = new JScrollPane();
		jResultPane.setBorder(BorderFactory.createEmptyBorder());
		jResultPane.setViewportView(jResult);
		p.add(jResultPane, BorderLayout.CENTER);
		
		return p;
	}
	
	private JPanel getButtonPanel() {
		JPanel p = new JPanel(new GridLayout(2, 2));
		p.setBorder(BorderFactory.createTitledBorder(null, "Useful Queries", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));

		JButton jButton = new JButton("Duplicate Athletes by Sport");
		jButton.setActionCommand("query0");
		jButton.addActionListener(this);
		p.add(jButton);
		
		jButton = new JButton("Missing Pictures");
		jButton.setActionCommand("missing");
		jButton.addActionListener(this);
		p.add(jButton);
		
		jButton = new JButton("Events/Championships not used");
		jButton.setActionCommand("query1");
		jButton.addActionListener(this);
		p.add(jButton);
		
		jButton = new JButton("Events not completed for current year");
		jButton.setActionCommand("query2");
		jButton.addActionListener(this);
		p.add(jButton);
		
		return p;
	}

	public void open() {
		jQuery.setText("");
		jResultPane.setViewportView(null);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equalsIgnoreCase("execute")) {
			executeQuery();
		}
		else if (cmd.matches("query\\d")) {
			String query = QUERIES.get(new Integer(cmd.substring(5)));
			query = query.replaceAll("#YEAR#", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			jQuery.setText(query);
			executeQuery();
		}
		else if (cmd.equalsIgnoreCase("missing"))
			missingPictures();
		else if (cmd.equalsIgnoreCase("close"))
			this.setVisible(false);
	}
	
	private void executeQuery() {
		try {
			List<Object[]> l = (List<Object[]>) DatabaseHelper.executeNative(jQuery.getText());
			Vector<String> cols = new Vector<String>();
			Vector<Vector> v = new Vector<Vector>();
			if (l != null && l.size() > 0) {
				for (Object[] t : l)  {
					Vector v_ = new Vector();
					for (Object o : t)
						v_.add(o != null ? String.valueOf(o) : "");
					v.add(v_);
				}
				for (int i = 0 ; i < l.get(0).length ; i++)
					cols.add(" ");
			}
			else {
				Vector v_ = new Vector();
				v_.add("Empty");
				v.add(v_);
				cols.add("");
			}
			jResult = new JTable(v, cols) {
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) {
					return true;
				}
			};
			jResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			for (int i = 0 ; i < jResult.getColumnCount() ; i++)
				jResult.getColumnModel().getColumn(i).setPreferredWidth(150);
			jResultPane.setViewportView(jResult);
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
			JOptionPane.showMessageDialog(this, e_.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@SuppressWarnings("deprecation")
	private void missingPictures() {
		try {
			Vector cols = new Vector();
			cols.add("TYPE");
			cols.add("INDEX");
			cols.add("ID");
			cols.add("NAME");
			Vector v = new Vector();
			URL url = new URL(ConfigUtils.getProperty("url") + "ImageServlet?missing=1");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (conn.getResponseCode() == 200) {
				PlainTextInputStream pis = (PlainTextInputStream) conn.getContent();
				DataInputStream dis = new DataInputStream(pis);
				String s = dis.readLine();
				for (String s_ : s.split("\\|")) {
					String[] t = s_.split(";");
					Vector v_ = new Vector();
					v_.add(t[0]);
					v_.add(t[1]);
					v_.add(t[2]);
					v_.add(t[3]);
					v.add(v_);
				}
			}
			jResult = new JTable(v, cols) {
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) {
					return true;
				}
			};
			jResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableColumnModel tcm = jResult.getColumnModel();
			tcm.getColumn(0).setPreferredWidth(50);
			tcm.getColumn(1).setPreferredWidth(50);
			tcm.getColumn(2).setPreferredWidth(50);
			tcm.getColumn(3).setPreferredWidth(250);
			jResultPane.setViewportView(jResult);
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
			JOptionPane.showMessageDialog(this, e_.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}