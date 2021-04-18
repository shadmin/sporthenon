package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.db.DatabaseManager;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.UpdateUtils;

public class JQueryDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JQueryDialog.class.getName());
	
	private JTextArea jQuery = null;
	private JScrollPane jResultPane = null;
	private JTable jResult = null;
	
	public JQueryDialog(JFrame owner) {
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
			String query = UpdateUtils.queries.get(StringUtils.toInt(cmd.substring(5)));
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
				Collection<Object[]> coll = DatabaseManager.executeSelect(UpdateUtils.queries.get(3));
				for (Object[] t : coll) {
					sbSitemap.append("<url><loc>http://sporthenon.com/results/");
					sbSitemap.append(StringUtils.urlEscape(t[5] + "/" + t[6] + "/" + t[7] + (t[8] != null ? "/" + t[8] : "") + (t[9] != null ? "/" + t[9] : "")));
					sbSitemap.append("/" + StringUtils.encode(t[0] + "-" + t[1] + "-" + t[2] + (t[3] != null ? "-" + t[3] : "") + (t[4] != null ? "-" + t[4] : "") + "-0"));
					sbSitemap.append("</loc><changefreq>monthly</changefreq><priority>0.2</priority>");
					sbSitemap.append("\r\n");
				}
				sbSitemap.append("</urlset>");
				jQuery.setText(sbSitemap.toString());
			}
			catch (Exception e_) {
				log.log(Level.WARNING, e_.getMessage(), e_);
				JOptionPane.showMessageDialog(this, e_.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (cmd.equalsIgnoreCase("close"))
			this.setVisible(false);
	}
	
	private void executeQuery() {
		try {
			List<Object[]> l = (List<Object[]>) DatabaseManager.executeSelect(jQuery.getText());
			Vector<String> cols = new Vector<String>();
			Vector<Vector<Object>> v = new Vector<>();
			if (l != null && l.size() > 0) {
				for (Object[] t : l)  {
					Vector<Object> v_ = new Vector<>();
					for (Object o : t) {
						v_.add(o != null ? String.valueOf(o) : "");
					}
					v.add(v_);
				}
				for (int i = 0 ; i < l.get(0).length ; i++) {
					cols.add(" ");
				}
			}
			else {
				Vector<Object> v_ = new Vector<>();
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
			for (int i = 0 ; i < jResult.getColumnCount() ; i++) {
				jResult.getColumnModel().getColumn(i).setPreferredWidth(150);
			}
			jResultPane.setViewportView(jResult);
		}
		catch (Exception e_) {
			log.log(Level.WARNING, e_.getMessage(), e_);
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
//			log.log(Level.WARNING, e_.getMessage(), e_);
//			JOptionPane.showMessageDialog(this, e_.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//		}
	}
	
}