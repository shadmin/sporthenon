package com.sporthenon.updater.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.updater.component.JCustomButton;
import com.sporthenon.updater.component.JDialogButtonBar;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.updater.container.tab.JResultsPanel;
import com.sporthenon.utils.SwingUtils;


public class JEditFolderDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JEntityPicklist jSport = null;
	private JEntityPicklist jCategory1 = null;
	private JEntityPicklist jCategory2 = null;
	private JEntityPicklist jCategory3 = null;
	private JEntityPicklist jCategory4 = null;
	private Integer currentSport;
	private Integer currentCategory1;
	private Integer currentCategory2;
	private Integer currentCategory3;
	private Integer currentCategory4;
	private short mode;
	private JResultsPanel parent;
	
	public static final short NEW = 1;
	public static final short EDIT = 2;
	
	public JEditFolderDialog(JFrame owner) {
		super(owner);
		initialize();
	}
	
	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(350, 300));
		this.setSize(this.getPreferredSize());
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
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
		JPanel jMainPanel = new JPanel(new GridLayout(10, 1, 0, 0));
		jMainPanel.setBorder(BorderFactory.createTitledBorder(null, "Folder Info", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		
		JLabel lSport = new JLabel(" Sport:");
		jSport = new JEntityPicklist(this, Sport.alias);
		jMainPanel.add(lSport, null);
		jMainPanel.add(jSport, null);
		
		JLabel lCategory1 = new JLabel(" Category #1:");
		jCategory1 = new JEntityPicklist(this, Championship.alias);
		jMainPanel.add(lCategory1, null);
		jMainPanel.add(jCategory1, null);
		
		JLabel lCategory2 = new JLabel(" Category #2:");
		jCategory2 = new JEntityPicklist(this, Event.alias);
		jMainPanel.add(lCategory2, null);
		jMainPanel.add(jCategory2, null);
		
		JLabel lCategory3 = new JLabel(" Category #3:");
		jCategory3 = new JEntityPicklist(this, Event.alias);
		jMainPanel.add(lCategory3, null);
		jMainPanel.add(jCategory3, null);
		
		JLabel lCategory4 = new JLabel(" Category #4:");
		jCategory4 = new JEntityPicklist(this, Event.alias);
		jMainPanel.add(lCategory4, null);
		jMainPanel.add(jCategory4, null);
		
		return jMainPanel;
	}

	public void open(JResultsPanel parent, short mode) {
		this.parent = parent;
		this.mode = mode;
		this.currentSport = SwingUtils.getValue(jSport);
		this.currentCategory1 = SwingUtils.getValue(jCategory1);
		this.currentCategory2 = SwingUtils.getValue(jCategory2);
		this.currentCategory3 = SwingUtils.getValue(jCategory3);
		this.currentCategory4 = SwingUtils.getValue(jCategory4);
		this.setTitle((mode == NEW ? "New" : "Edit") + " Folder");
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.matches("\\D\\D\\-(add|find)")) {
			String alias = cmd.substring(0, 2);
			JEntityPicklist srcPicklist = (JEntityPicklist)((JCustomButton)e.getSource()).getParent().getParent();
			if (cmd.matches("\\D\\D\\-add"))
				JMainFrame.getEntityDialog().open(alias, srcPicklist);
			else {
				JFindEntityDialog dlg = JMainFrame.getFindDialog();
				dlg.open(alias, srcPicklist);
				if (dlg.getSelectedItem() != null)
					SwingUtils.selectValue(srcPicklist, dlg.getSelectedItem().getValue());
			}
			return;
		}
		else if (cmd.equals("ok")) {
			if (SwingUtils.getValue(jSport) == 0) {
				JOptionPane.showMessageDialog(this, "Field 'Sport' can not be empty.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if (SwingUtils.getValue(jCategory1) == 0) {
				JOptionPane.showMessageDialog(this, "Field 'Category #1' can not be empty.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if (SwingUtils.getValue(jCategory2) == 0 && SwingUtils.getValue(jCategory3) == 0) {
				JOptionPane.showMessageDialog(this, "Fields 'Category #2' and 'Category #3' can not be both empty.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String msg = null;
			boolean err = false;
			try {
				Integer sp = SwingUtils.getValue(jSport);
				Integer c1 = SwingUtils.getValue(jCategory1);
				Integer c2 = SwingUtils.getValue(jCategory2);
				Integer c3 = SwingUtils.getValue(jCategory3);
				Integer c4 = SwingUtils.getValue(jCategory4);
				if (mode == NEW) {
					String sql = "INSERT INTO \"RESULT\"(id, id_sport, id_championship, id_event, id_subevent, id_subevent2, id_year, id_member, last_update) ";
					sql += "VALUES (nextval('\"SQ_RESULT\"')," + sp + "," + c1 + "," + (c2 > 0 ? c2 : "NULL") + "," + (c3 > 0 ? c3 : "NULL") + "," + (c4 > 0 ? c4 : "NULL") + ",1," + JMainFrame.getMember().getId() + ",now())";
					DatabaseHelper.executeUpdate(sql);
				}
				else if (mode == EDIT) {
					String sql = "UPDATE \"RESULT\" SET id_sport=" + sp + ", id_championship=" + c1 + ", id_event=" + (c2 > 0 ? c2 : "NULL") + ", id_subevent=" + (c3 > 0 ? c3 : "NULL") + ", id_subevent2=" + (c4 > 0 ? c4 : "NULL") + " ";
					sql += "WHERE id_sport=" + currentSport + " AND id_championship=" + currentCategory1 + (currentCategory2 > 0 ? " AND id_event=" + currentCategory2 : "") + (currentCategory3 > 0 ? " AND id_subevent=" + currentCategory3 : "") + (currentCategory4 > 0 ? " AND id_subevent2=" + currentCategory4 : "");
					DatabaseHelper.executeUpdate(sql);
				}
				msg = "Folder has been " + (mode == NEW ? "created" : "updated") + " successfully.";
			}
			catch (Exception e_) {
				err = true;
				msg = e_.getMessage();
				Logger.getLogger("sh").error(e_.getMessage(), e_);
			}
			finally {
				parent.folderCallback(mode, msg, err);
			}
		}
		this.setVisible(false);
	}

	public JEntityPicklist getSport() {
		return jSport;
	}

	public JEntityPicklist getCategory1() {
		return jCategory1;
	}

	public JEntityPicklist getCategory2() {
		return jCategory2;
	}

	public JEntityPicklist getCategory3() {
		return jCategory3;
	}
	
	public JEntityPicklist getCategory4() {
		return jCategory4;
	}
	
}
