package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.container.tab.JResultsPanel;
import com.sporthenon.db.DatabaseManager;
import com.sporthenon.utils.SwingUtils;


public class JAddMultipleDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JAddMultipleDialog.class.getName());
	
	private JTextArea jYears = null;
	private JResultsPanel parent;
	
	public JAddMultipleDialog(JFrame owner) {
		super(owner);
		initialize();
	}
	
	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(280, 240));
		this.setSize(this.getPreferredSize());
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Add Multiple");
		this.setContentPane(jContentPane);
		
		BorderLayout layout = new BorderLayout();
		layout.setVgap(5);
		JDialogButtonBar jButtonBar = new JDialogButtonBar(this);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(layout);
		jContentPane.add(getMainPanel(), BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}
	
	private JPanel getMainPanel() {
		JPanel jMainPanel = new JPanel(new BorderLayout());
		jMainPanel.setBorder(BorderFactory.createTitledBorder(null, "Years", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		
		jYears = new JTextArea();
		jYears.setFont(SwingUtils.getDefaultFont());
		jMainPanel.add(new JScrollPane(jYears), BorderLayout.CENTER);
		
		return jMainPanel;
	}
	
	public void open(JResultsPanel parent) {
		this.parent = parent;
		this.jYears.setText("");
		this.jYears.requestFocus();
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			String msg = null;
			boolean err = false;
			try {
				Integer sp = JResultsPanel.getIdSport();
				Integer cp = JResultsPanel.getIdChampionship();
				Integer ev = JResultsPanel.getIdEvent();
				Integer se = JResultsPanel.getIdSubevent();
				Integer se2 = JResultsPanel.getIdSubevent2();
				for (String s : jYears.getText().split("\n")) {
					String sql_ = "SELECT ID FROM year WHERE LABEL='" + s + "'";
					String sql = "INSERT INTO result(id, id_sport, id_championship, id_event, id_subevent, id_subevent2, id_year, id_contributor, last_update) ";
					sql += "VALUES (nextval('s_result')," + sp + "," + cp + "," + (ev != null && ev > 0 ? ev : "NULL") + "," + (se != null && se > 0 ? se : "NULL") + "," + (se2 != null && se > 0 ? se2 : "NULL") + ",(" + sql_ + ")," + JMainFrame.getContributor().getId() + ",now())";
					DatabaseManager.executeUpdate(sql);
				}
				msg = "Years have been added successfully.";
			}
			catch (Exception e_) {
				err = true;
				msg = e_.getMessage();
				log.log(Level.WARNING, e_.getMessage(), e_);
			}
			finally {
				parent.addMultipleCallback(msg, err);
			}
		}
		this.setVisible(false);
	}

	public JTextArea getYears() {
		return jYears;
	}

}
