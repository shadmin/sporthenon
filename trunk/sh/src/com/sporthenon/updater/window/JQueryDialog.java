package com.sporthenon.updater.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.PicklistBean;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Team;
import com.sporthenon.updater.component.JCustomButton;
import com.sporthenon.updater.component.JDialogButtonBar;
import com.sporthenon.updater.container.tab.JDataPanel;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.ImageUtils;

public class JQueryDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JTextArea jQuery = null;
	private JScrollPane jResultPane = null;
	private JTable jResult = null;
	private static ArrayList<String> QUERIES;
	
	static {
		QUERIES = new ArrayList<String>();
		QUERIES.add("SELECT DISTINCT LAST_NAME || ',' || FIRST_NAME || ',' || ID_SPORT AS N, COUNT(*) AS C\r\nFROM \"PERSON\"\r\nWHERE LINK IS NULL\r\nGROUP BY N\r\nORDER BY C DESC\r\nLIMIT 100");
		QUERIES.add("");
		QUERIES.add("");
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
		JCustomButton jExecuteButton = new JCustomButton("Execute", "updater/ok.png");
		jExecuteButton.setActionCommand("execute");
		jExecuteButton.addActionListener(this);
		p_.add(jExecuteButton);
		JCustomButton jCloseButton = new JCustomButton("Close", "updater/cancel.png");
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
		
		jButton = new JButton("-");
		jButton.setActionCommand("query1");
		jButton.addActionListener(this);
		p.add(jButton);
		
		jButton = new JButton("-");
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
			jQuery.setText(QUERIES.get(new Integer(cmd.substring(5))));
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
						v_.add(String.valueOf(o));
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
				jResult.getColumnModel().getColumn(i).setPreferredWidth(200);
			jResultPane.setViewportView(jResult);
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
			JOptionPane.showMessageDialog(this, e_.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void missingPictures() {
		try {
			Vector cols = new Vector();
			cols.add("TYPE");
			cols.add("INDEX");
			cols.add("ID");
			cols.add("NAME");
			Vector v = new Vector();
			for (String entity : new String[]{"CP", "EV", "SP", "CN", "OL"}) {
				String label = null;
				if (entity.equalsIgnoreCase(Olympics.alias))
					label = "concat(concat(year.label, ' - '), city.label)";
				else if (entity.equalsIgnoreCase(Team.alias))
					label = "concat(concat(label, ' - '), sport.label)";
				Collection<PicklistBean> lst = DatabaseHelper.getEntityPicklist(JDataPanel.getClassFromAlias(entity), label, null);
				int n = 0;
				for (PicklistBean o : lst) {
					String ext = (entity.toUpperCase().matches("CP|EV|OL|SP|TM") ? ".png" : ".gif");
					String fileName = ImageUtils.getIndex(entity.toUpperCase()) + "-" + o.getValue() + "-L" + ext;
					File f = new File(ConfigUtils.getProperty("img.folder") + fileName);
					if (!f.exists()) {
						Vector v_ = new Vector();
						v_.add(entity);
						v_.add(++n);
						v_.add(o.getValue());
						v_.add(o.getText());
						v.add(v_);
					}
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