package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import org.apache.log4j.Logger;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.utils.StringUtils;

public class JQueryDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JTextArea jQuery = null;
	private JScrollPane jResultPane = null;
	private JTable jResult = null;
	private static ArrayList<String> QUERIES;
	
	static {
		QUERIES = new ArrayList<String>();
		QUERIES.add("SELECT DISTINCT LAST_NAME || ',' || FIRST_NAME || ',' || ID_SPORT AS N, COUNT(*) AS C\r\nFROM \"Athlete\"\r\nWHERE LINK IS NULL\r\nGROUP BY N\r\nORDER BY C DESC\r\nLIMIT 100");
		QUERIES.add("SELECT 'EV', ID, LABEL FROM \"Event\"\r\nWHERE ID NOT IN (SELECT ID_EVENT FROM \"Result\" WHERE ID_EVENT IS NOT NULL) AND ID NOT IN (SELECT ID_SUBEVENT FROM \"Result\" WHERE ID_SUBEVENT IS NOT NULL) AND ID NOT IN (SELECT ID_SUBEVENT2 FROM \"Result\" WHERE ID_SUBEVENT2 IS NOT NULL)\r\nAND ID NOT IN (SELECT ID_EVENT FROM \"Record\" WHERE ID_EVENT IS NOT NULL) AND ID NOT IN (SELECT ID_SUBEVENT FROM \"Record\" WHERE ID_SUBEVENT IS NOT NULL)\r\nUNION SELECT 'CP', ID, LABEL FROM \"Championship\" WHERE ID NOT IN (SELECT ID_CHAMPIONSHIP FROM \"Result\" WHERE ID_CHAMPIONSHIP IS NOT NULL)\r\nAND ID NOT IN (SELECT ID_CHAMPIONSHIP FROM \"Record\" WHERE ID_CHAMPIONSHIP IS NOT NULL)\r\nORDER BY 1, 3");
		QUERIES.add("SELECT SP.label AS SPORT, CP.label AS Championship, EV.label AS EVENT, SE.label AS SUBEVENT, SE2.label AS SUBEVENT2, YR.label AS YEAR\r\nFROM (SELECT DISTINCT id_sport, id_championship, id_event, id_subevent, id_subevent2 FROM \"Result\" EXCEPT SELECT DISTINCT id_sport, id_championship, id_event, id_subevent, id_subevent2 FROM \"Result\" WHERE id_year = (SELECT id FROM \"Year\" WHERE label = '#YEAR#')) T\r\nLEFT JOIN \"Sport\" SP ON T.id_sport = SP.id\r\nLEFT JOIN \"Championship\" CP ON T.id_championship = CP.id LEFT JOIN \"Event\" EV ON T.id_event = EV.id\r\nLEFT JOIN \"Event\" SE ON T.id_subevent = SE.id LEFT JOIN \"Event\" SE2 ON T.id_subevent2 = SE2.id LEFT JOIN \"Year\" YR ON YR.label = '#YEAR#'\r\nLEFT JOIN \"~InactiveItem\" II ON (T.id_sport=II.id_sport AND T.id_championship=II.id_championship AND T.id_event=II.id_event AND (T.id_subevent IS NULL OR T.id_subevent=II.id_subevent) AND (T.id_subevent2 IS NULL OR T.id_subevent2=II.id_subevent2))\r\nWHERE 1=1 AND #WHERE# AND II.id IS NULL\r\nORDER BY SP.label, CP.index, CP.label, EV.index, EV.label, SE.index, SE.label, SE2.index, SE2.label");
		QUERIES.add("SELECT DISTINCT id_sport, id_championship, id_event, id_subevent, id_subevent2, SP.label AS label1, CP.label AS label2, EV.label AS label3, SE.label AS label4, SE2.label AS label5 FROM \"Result\" RS LEFT JOIN \"Sport\" SP ON RS.id_sport=SP.id LEFT JOIN \"Championship\" CP ON RS.id_championship=CP.id LEFT JOIN \"Event\" EV ON RS.id_event=EV.id LEFT JOIN \"Event\" SE ON RS.id_subevent=SE.id LEFT JOIN \"Event\" SE2 ON RS.id_subevent2=SE2.id ORDER BY SP.label, CP.label, EV.label, SE.label, SE2.label");
		QUERIES.add("SELECT * FROM (SELECT 'CP', ID, LABEL FROM \"Championship\" WHERE LABEL=LABEL_FR UNION SELECT 'CT', ID, LABEL FROM \"City\" WHERE LABEL=LABEL_FR UNION SELECT 'CX', ID, LABEL FROM \"Complex\" WHERE LABEL=LABEL_FR UNION SELECT 'CN', ID, LABEL FROM \"Country\" WHERE LABEL=LABEL_FR UNION SELECT 'EV', ID, LABEL FROM \"Event\" WHERE LABEL=LABEL_FR UNION SELECT 'SP', ID, LABEL FROM \"Sport\" WHERE LABEL=LABEL_FR ) T ORDER BY 1,2");
		QUERIES.add("SELECT DISTINCT SP.label || '-' || CP.label || '-' || EV.label || (CASE WHEN SE.id IS NOT NULL THEN '-' || SE.label ELSE '' END) || (CASE WHEN SE2.id IS NOT NULL THEN '-' || SE2.label ELSE '' END), COUNT(*) AS N FROM \"Result\" RS LEFT JOIN \"Sport\" SP ON RS.id_sport=SP.id LEFT JOIN \"Championship\" CP ON RS.id_championship=CP.id LEFT JOIN \"Event\" EV ON RS.id_event=EV.id LEFT JOIN \"Event\" SE ON RS.id_subevent=SE.id LEFT JOIN \"Event\" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN \"~InactiveItem\" II ON (RS.id_sport = II.id_sport AND RS.id_championship = II.id_championship AND RS.id_event = II.id_event AND (RS.id_subevent = II.id_subevent OR RS.id_subevent IS NULL) AND (RS.id_subevent2 = II.id_subevent2 OR RS.id_subevent2 IS NULL)) WHERE II.id IS NULL GROUP BY 1 HAVING COUNT(*)<5 ORDER BY 2, 1");
		QUERIES.add("SELECT 'PR', id, last_name || ', ' || first_name AS label FROM \"Athlete\" WHERE id_country IS NULL UNION SELECT 'TM', id, label FROM \"Team\" WHERE id_country IS NULL ORDER BY 1, 3");
		QUERIES.add("SELECT 'CT', id, label FROM \"City\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='CT') UNION SELECT 'CX', id, label FROM \"Complex\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='CX') UNION SELECT 'CN', id, label FROM \"Country\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='CN') UNION SELECT 'CP', id, label FROM \"Championship\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='CP') UNION SELECT 'EV', id, label FROM \"Event\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='EV') UNION SELECT 'PR', id, last_name || ', ' || first_name FROM \"Athlete\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='PR') UNION SELECT 'RS', RS.id, SP.label || '-' || CP.label || '-' || EV.label || '-' || YR.label AS label FROM \"Result\" RS LEFT JOIN \"Sport\" SP ON RS.id_sport=SP.id LEFT JOIN \"Championship\" CP ON RS.id_championship=CP.id LEFT JOIN \"Event\" EV ON RS.id_event=EV.id LEFT JOIN \"Year\" YR ON RS.id_year=YR.id WHERE RS.id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='RS') UNION SELECT 'SP', id, label FROM \"Sport\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='SP') UNION SELECT 'TM', id, label FROM \"Team\" WHERE id NOT IN (SELECT id_item FROM \"~ExternalLink\" WHERE entity='TM') ORDER BY 1, 3");
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
		JPanel p = new JPanel(new GridLayout(5, 2));
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
		
		jButton = new JButton("Generate site map");
		jButton.setActionCommand("sitemap");
		jButton.addActionListener(this);
		p.add(jButton);
		
		jButton = new JButton("Untranslated items");
		jButton.setActionCommand("query4");
		jButton.addActionListener(this);
		p.add(jButton);
		
		jButton = new JButton("Incomplete event results");
		jButton.setActionCommand("query5");
		jButton.addActionListener(this);
		p.add(jButton);
		
		jButton = new JButton("Athletes/teams without country");
		jButton.setActionCommand("query6");
		jButton.addActionListener(this);
		p.add(jButton);

		jButton = new JButton("Entities without external link");
		jButton.setActionCommand("query7");
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
			int year = Calendar.getInstance().get(Calendar.YEAR);
			String query = QUERIES.get(new Integer(cmd.substring(5)));
			query = query.replaceAll("#YEAR#", String.valueOf(year));
			query = query.replaceAll("#WHERE#", (year % 4 == 0 ? "(CP.id<>1 OR SP.type<>0)" : (year % 4 == 2 ? "(CP.id<>1 OR SP.type<>1)" : "CP.id<>1")));
			jQuery.setText(query);
			executeQuery();
		}
		else if (cmd.equalsIgnoreCase("missing"))
			missingPictures();
		else if (cmd.equalsIgnoreCase("sitemap")) {
			try {
				StringBuffer sbSitemap = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\r\n");
				sbSitemap.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">").append("\r\n");
				List<Object[]> l = DatabaseHelper.executeNative(QUERIES.get(3));
				for (Object[] t : l) {
					sbSitemap.append("<url><loc>http://www.sporthenon.com/results/");
					sbSitemap.append(StringUtils.urlEscape(t[5] + "/" + t[6] + "/" + t[7] + (t[8] != null ? "/" + t[8] : "") + (t[9] != null ? "/" + t[9] : "")));
					sbSitemap.append("/" + StringUtils.encode(t[0] + "-" + t[1] + "-" + t[2] + (t[3] != null ? "-" + t[3] : "") + (t[4] != null ? "-" + t[4] : "") + "-0"));
					sbSitemap.append("</loc><changefreq>monthly</changefreq><priority>0.2</priority>");
					sbSitemap.append("\r\n");
				}
				sbSitemap.append("</urlset>");
				jQuery.setText(sbSitemap.toString());
			}
			catch (Exception e_) {
				Logger.getLogger("sh").error(e_.getMessage(), e_);
				JOptionPane.showMessageDialog(this, e_.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
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

	private void missingPictures() {
//		try {
//			Vector cols = new Vector();
//			cols.add("TYPE");
//			cols.add("INDEX");
//			cols.add("ID");
//			cols.add("NAME");
//			Vector v = new Vector();
//			URL url = new URL(ConfigUtils.getProperty("url") + "ImageServlet?missing=1");
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			if (conn.getResponseCode() == 200) {
//				PlainTextInputStream pis = (PlainTextInputStream) conn.getContent();
//				DataInputStream dis = new DataInputStream(pis);
//				String s = dis.readLine();
//				for (String s_ : s.split("\\|")) {
//					String[] t = s_.split(";");
//					Vector v_ = new Vector();
//					v_.add(t[0]);
//					v_.add(t[1]);
//					v_.add(t[2]);
//					v_.add(t[3]);
//					v.add(v_);
//				}
//			}
//			jResult = new JTable(v, cols) {
//				private static final long serialVersionUID = 1L;
//				public boolean isCellEditable(int row, int column) {
//					return true;
//				}
//			};
//			jResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//			TableColumnModel tcm = jResult.getColumnModel();
//			tcm.getColumn(0).setPreferredWidth(50);
//			tcm.getColumn(1).setPreferredWidth(50);
//			tcm.getColumn(2).setPreferredWidth(50);
//			tcm.getColumn(3).setPreferredWidth(250);
//			jResultPane.setViewportView(jResult);
//		}
//		catch (Exception e_) {
//			Logger.getLogger("sh").error(e_.getMessage(), e_);
//			JOptionPane.showMessageDialog(this, e_.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//		}
	}
	
}