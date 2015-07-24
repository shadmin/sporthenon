package com.sporthenon.admin.container.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.BatchUpdateException;
import java.util.Collection;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JQueryStatus;
import com.sporthenon.admin.window.JMainFrame;
import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.PicklistBean;
import com.sporthenon.db.entity.meta.Contributor;

public class JUsersPanel extends JSplitPane implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;

	private JScrollPane leftPanel = null;
	private JList jList = null;
	private JTextField jId = null;
	private JTextField jLogin = null;
	private JTextField jName = null;
	private JTextField jEmail = null;
	private JCheckBox jActive = null;
	private JCheckBox jAdmin = null;
	private JTextField jSports = null;
	private JQueryStatus jQueryStatus = null;

	public JUsersPanel(JMainFrame parent) {
		this.jQueryStatus = parent.getQueryStatus();
		initialize();
	}

	public JList getList() {
		return jList;
	}

	private void initialize() {
		leftPanel = new JScrollPane();
		leftPanel.setPreferredSize(new Dimension(150, 0));
		leftPanel.setBorder(BorderFactory.createEmptyBorder());
		leftPanel.setMinimumSize(new Dimension(100, 0));
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 5));
		rightPanel.setLayout(new BorderLayout(10, 10));
		rightPanel.add(getBottomPanel(), BorderLayout.SOUTH);
		rightPanel.add(getMainPanel(), BorderLayout.NORTH);
		this.setTopComponent(leftPanel);
		this.setBottomComponent(rightPanel);
	}

	public void initList() throws Exception {
		Vector<PicklistBean> v = new Vector<PicklistBean>();
		for (Contributor cb : (Collection<Contributor>) DatabaseHelper.execute("from Contributor order by login"))
			v.add(new PicklistBean(cb.getId(), cb.getLogin()));
		jList = new JList(v);
		jList.setName("mainlist");
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setSelectedIndex(0);
		jList.addListSelectionListener(this);
		leftPanel.setViewportView(jList);
	}

	private JPanel getBottomPanel() {
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 1));
		JCustomButton jSaveButton = new JCustomButton("Save", "save.png", "Save");
		jSaveButton.addActionListener(this);
		jSaveButton.setActionCommand("save");
		rightPanel.add(jSaveButton);
		
		JPanel p = new JPanel(new BorderLayout(0, 0));
		p.setPreferredSize(new Dimension(0, 26));
		p.add(rightPanel, BorderLayout.EAST);

		return p;
	}

	private JPanel getMainPanel() {
		JPanel p = new JPanel(new GridLayout(14, 1));
		p.setBorder(BorderFactory.createEmptyBorder());
		p.setPreferredSize(new Dimension(310, 280));
		
		JLabel l = new JLabel(" ID:");
		l.setHorizontalAlignment(JLabel.LEFT);
		p.add(l);
		jId = new JTextField();
		jId.setPreferredSize(new Dimension(0, 21));
		jId.setEnabled(false);
		p.add(jId);
		
		l = new JLabel(" Login:");
		l.setHorizontalAlignment(JLabel.LEFT);
		p.add(l);
		jLogin = new JTextField();
		jLogin.setPreferredSize(new Dimension(0, 21));
		p.add(jLogin);

		l = new JLabel(" Name:");
		l.setHorizontalAlignment(JLabel.LEFT);
		p.add(l);
		jName = new JTextField();
		jName.setPreferredSize(new Dimension(0, 21));
		p.add(jName);
		
		l = new JLabel(" E-mail:");
		l.setHorizontalAlignment(JLabel.LEFT);
		p.add(l);
		jEmail = new JTextField();
		jEmail.setPreferredSize(new Dimension(0, 21));
		p.add(jEmail);
		
		l = new JLabel(" Active:");
		l.setHorizontalAlignment(JLabel.LEFT);
		p.add(l);
		jActive = new JCheckBox();
		p.add(jActive);
		
		l = new JLabel(" Admin:");
		l.setHorizontalAlignment(JLabel.LEFT);
		p.add(l);
		jAdmin = new JCheckBox();
		p.add(jAdmin);
		
		l = new JLabel(" Sports:");
		l.setHorizontalAlignment(JLabel.LEFT);
		p.add(l);
		jSports = new JTextField();
		jSports.setPreferredSize(new Dimension(0, 21));
		p.add(jSports);
		
		return p;
	}

	public void actionPerformed(ActionEvent e) {
		boolean err = false;
		String msg = null;
		try {
			if (e.getActionCommand().equals("save")) {
				Contributor cb = (Contributor) DatabaseHelper.loadEntity(Contributor.class, new Integer(jId.getText()));
				cb.setLogin(jLogin.getText());
				cb.setPublicName(jName.getText());
				cb.setEmail(jEmail.getText());
				cb.setActive(jActive.isSelected());
				cb.setAdmin(jAdmin.isSelected());
				cb.setSports(jSports.getText());
				DatabaseHelper.saveEntity(cb, null);
//				if (jAdmin.isSelected())
//					DatabaseHelper.executeUpdate("CREATE ROLE " + cb.getLogin() + " LOGIN ENCRYPTED PASSWORD 'md5" + StringUtils.toMD5(cb.getLogin() + cb.getPassword()) + "' SUPERUSER VALID UNTIL 'infinity'");
//				else
//					DatabaseHelper.executeUpdate("DROP ROLE " + cb.getLogin());
				msg = "User '" + cb.getLogin() + "' saved successfully.";
			}
		}
		catch (Exception e_) {
			err = true;
			Logger.getLogger("sh").error(e_.getMessage(), e_);
			if (e_ instanceof BatchUpdateException)
				((BatchUpdateException)e_).getNextException().printStackTrace();
		}
		finally {
			if (msg != null)
				jQueryStatus.set(err ? JQueryStatus.FAILURE : JQueryStatus.SUCCESS, msg);
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		try {
			if (e != null && e.getSource() instanceof JList) {
				PicklistBean plb = (PicklistBean) ((JList)e.getSource()).getSelectedValue();
				Contributor cb = (Contributor) DatabaseHelper.loadEntity(Contributor.class, plb.getValue());
				jId.setText(String.valueOf(cb.getId()));
				jLogin.setText(cb.getLogin());
				jName.setText(cb.getPublicName());
				jEmail.setText(cb.getEmail());
				jActive.setSelected(cb.getActive() != null && cb.getActive());
				jAdmin.setSelected(cb.getAdmin() != null && cb.getAdmin());
				jSports.setText(cb.getSports());
			}
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
		}
	}

}