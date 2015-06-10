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

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.PicklistBean;
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
	
	private JTextField jText = null;
	private JList jList = null;
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
		jList = new JList(new DefaultListModel());
		jList.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2)
					actionPerformed(new ActionEvent(jButtonBar.getOk(), 0, "ok"));
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
		((DefaultListModel)jList.getModel()).clear();
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel"))
			jList.clearSelection();
		if ((e.getActionCommand().equals("ok") && jList.getSelectedIndex() != -1) || e.getActionCommand().equals("cancel"))
			this.setVisible(false);
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		try {
			DefaultListModel model = (DefaultListModel)jList.getModel();
			if (e.getKeyCode() == KeyEvent.VK_ENTER && StringUtils.notEmpty(jText.getText())) {
				model.clear();
				if (jPicklist != null) {
					for (int i = 1 ; i < jPicklist.getPicklist().getItemCount() ; i++) {
						PicklistBean bean = (PicklistBean) jPicklist.getPicklist().getItemAt(i);
						if (bean.getText().toLowerCase().matches("^" + jText.getText().toLowerCase() + ".*"))
							model.addElement(bean);
					}
				}
				else {
					Class c = DatabaseHelper.getClassFromAlias(alias);
					String label = null;
					if (c.equals(City.class))
						label = "label || ', ' || country.code";
					else if (c.equals(Complex.class))
						label = "label || ' [' || city.label || ', ' || city.country.code || ']'";
					else if (c.equals(Country.class)) 
						label = "label || ', ' || code";
					else if (c.equals(Olympics.class)) 
						label = "year.label || ' - ' || city.label";
					else if (c.equals(Athlete.class))
						label = "lastName || ', ' || firstName";
					else if (c.equals(Team.class))
						label = "label || ', ' || sport.label";
					else if (c.equals(HallOfFame.class)) 
						label = "league.label || ' - ' || year.label";
					else if (c.equals(OlympicRanking.class)) 
						label = "olympics.year.label || ' - ' || olympics.city.label || ' - ' || country.label";
					else if (c.equals(Record.class)) 
						label = "championship.label || ' - ' || event.label || ' - ' || subevent.label || ' - ' || type1 || ' - ' || type2 || ' - ' || label";
					else if (c.equals(RetiredNumber.class)) 
						label = "league.label || ' - ' || team.label";
					else if (c.equals(TeamStadium.class)) 
						label = "league.label || ' - ' || team.label";
					else if (c.equals(WinLoss.class)) 
						label = "league.label || ' - ' || team.label";
					label = (label != null ? label : "label") + " || ' [#' || id || ']'";
					String pattern = jText.getText().replaceAll("\\*", "%").toLowerCase();
					String hql = "select id," + label + " from " + c.getSimpleName();
					hql += " where " + (pattern.matches("^\\#\\d+") ? "id=" + pattern.substring(1) : "lower(" + label + ") like '" + pattern + "%'");
					hql += " order by " + label;
					for (PicklistBean bean : DatabaseHelper.getPicklistFromQuery(hql, false))
						model.addElement(bean);
				}
			}
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
		}
	}

	public void keyTyped(KeyEvent e) {
	}
	
	public PicklistBean getSelectedItem() {
		return (jList.getSelectedIndex() != -1 ? (PicklistBean)((DefaultListModel)jList.getModel()).getElementAt(jList.getSelectedIndex()) : null);
	}
	
}