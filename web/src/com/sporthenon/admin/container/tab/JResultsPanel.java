package com.sporthenon.admin.container.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.component.JQueryStatus;
import com.sporthenon.admin.window.JEditFolderDialog;
import com.sporthenon.admin.window.JEditResultDialog;
import com.sporthenon.admin.window.JMainFrame;
import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.PicklistBean;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.meta.InactiveItem;
import com.sporthenon.db.entity.meta.TreeItem;
import com.sporthenon.db.function.ResultsBean;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;
import com.sporthenon.utils.res.ResourceUtils;


public class JResultsPanel extends JSplitPane implements TreeSelectionListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JScrollPane jScrollPane = null;
	private JTree jTree = null;
	private JTable jResultTable = null;
	private static Integer idSport = null;
	private static Integer idChampionship = null;
	private static Integer idEvent = null;
	private static Integer idSubevent = null;
	private static Integer idSubevent2 = null;
	private JCustomButton jEditButton = null;
	private JCustomButton jCopyButton = null;
	private JCustomButton jRemoveButton = null;
	private JCheckBox jInactive = null;
	private JQueryStatus jQueryStatus = null;
	private List<PicklistBean> treeItems;

	public JResultsPanel(JMainFrame parent) {
		this.jQueryStatus = parent.getQueryStatus();
		initialize();
	}

	private void initialize() {
		JScrollPane leftPanel = new JScrollPane(jTree);
		leftPanel.setPreferredSize(new Dimension(230, 0));
		leftPanel.setBorder(BorderFactory.createEmptyBorder());
		leftPanel.setMinimumSize(new Dimension(0, 0));
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		jScrollPane = new JScrollPane();
		jScrollPane.setBorder(BorderFactory.createEmptyBorder());
		jScrollPane.setViewportView(jResultTable);
		rightPanel.add(getButtonPanel(), BorderLayout.SOUTH);
		rightPanel.add(jScrollPane, BorderLayout.CENTER);
		rightPanel.setMinimumSize(new Dimension(0, 0));
		this.setTopComponent(leftPanel);
		this.setBottomComponent(rightPanel);
	}

	public void setTree() {
		try {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode();
			DefaultMutableTreeNode level1Node = null;
			DefaultMutableTreeNode level2Node = null;
			DefaultMutableTreeNode level3Node = null;
			DefaultMutableTreeNode level4Node = null;
			DefaultMutableTreeNode level5Node = null;
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(new String(""));
			params.add(new String(""));
			Collection<Object> coll = DatabaseHelper.call("TreeResults", params);
			treeItems = new ArrayList<PicklistBean>();
			PicklistBean plb = null;
			ArrayList<Object> lst = new ArrayList<Object>(coll);
			int i, j, k, l, m;
			for (i = 0 ; i < lst.size() ; i++) {
				TreeItem item = (TreeItem) lst.get(i);
				plb = new PicklistBean(item.getIdItem(), item.getStdLabel(), item.getIdItem());
				level1Node = new DefaultMutableTreeNode(plb);
				root.add(level1Node);
				treeItems.add(plb);
				for (j = i + 1 ; j < lst.size() ; j++) {
					TreeItem item2 = (TreeItem) lst.get(j);
					if (item2.getLevel() < 2) {j--; break;}
					plb = new PicklistBean(item2.getIdItem(), item2.getStdLabel(), item.getIdItem() + "," + item2.getIdItem());
					level2Node = new DefaultMutableTreeNode(plb);
					level1Node.add(level2Node);
					treeItems.add(new PicklistBean(plb.getValue(), item.getStdLabel() + " | " + item2.getStdLabel(), plb.getParam()));
					for (k = j + 1 ; k < lst.size() ; k++) {
						TreeItem item3 = (TreeItem) lst.get(k);
						if (item3.getLevel() < 3) {k--; break;}
						plb = new PicklistBean(item3.getIdItem(), item3.getStdLabel(), item.getIdItem() + "," + item2.getIdItem() + "," + item3.getIdItem());
						level3Node = new DefaultMutableTreeNode(plb);
						level2Node.add(level3Node);
						treeItems.add(new PicklistBean(plb.getValue(), item.getStdLabel() + " | " + item2.getStdLabel() + " | " + item3.getStdLabel(), plb.getParam()));
						for (l = k + 1 ; l < lst.size() ; l++) {
							TreeItem item4 = (TreeItem) lst.get(l);
							if (item4.getLevel() < 4) {l--; break;}
							plb = new PicklistBean(item4.getIdItem(), item4.getStdLabel(), item.getIdItem() + "," + item2.getIdItem() + "," + item3.getIdItem() + "," + item4.getIdItem());
							level4Node = new DefaultMutableTreeNode(plb);
							level3Node.add(level4Node);
							treeItems.add(new PicklistBean(plb.getValue(), item.getStdLabel() + " | " + item2.getStdLabel() + " | " + item3.getStdLabel() + " | " + item4.getStdLabel(), plb.getParam()));
							for (m = l + 1 ; m < lst.size() ; m++) {
								TreeItem item5 = (TreeItem) lst.get(m);
								if (item5.getLevel() < 5) {m--; break;}
								plb = new PicklistBean(item5.getIdItem(), item5.getStdLabel(), item.getIdItem() + "," + item2.getIdItem() + "," + item3.getIdItem() + "," + item4.getIdItem() + "," + item5.getIdItem());
								level5Node = new DefaultMutableTreeNode(plb);
								level4Node.add(level5Node);
								treeItems.add(new PicklistBean(plb.getValue(), item.getStdLabel() + " | " + item2.getStdLabel() + " | " + item3.getStdLabel() + " | " + item4.getStdLabel() + " | " + item5.getStdLabel(), plb.getParam()));
							}
							l = m;
						}
						k = l;
					}
					j = k;
				}
				i = j;
			}
			DefaultTreeModel treeModel = new DefaultTreeModel(root);
			jTree = new JTree(treeModel);
			jTree.setRootVisible(false);
			jTree.setShowsRootHandles(true);
			jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
			jTree.addTreeSelectionListener(this);

			ImageIcon folderIcon = ResourceUtils.getIcon("common/treefolder.png");
			ImageIcon leafIcon = ResourceUtils.getIcon("common/treeitem.png");
			if (leafIcon != null) {
				DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
				renderer.setOpenIcon(folderIcon);
				renderer.setClosedIcon(folderIcon);
				renderer.setLeafIcon(leafIcon);
				jTree.setCellRenderer(renderer);
			}

			((JScrollPane)this.getTopComponent()).setViewportView(jTree);
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
	}

	private JPanel getButtonPanel() {
		JPanel leftPanel = new JPanel();
		JCustomButton jRefreshTreeButton = new JCustomButton("Refresh", "common/refresh.png", "Refresh Tree");
		jRefreshTreeButton.addActionListener(this);
		jRefreshTreeButton.setActionCommand("refresh-tree");
		JCustomButton jNewFolderButton = new JCustomButton("New Folder", "newfolder.png", "New Folder");
		jNewFolderButton.addActionListener(this);
		jNewFolderButton.setActionCommand("add-folder");
		JCustomButton jEditFolderButton = new JCustomButton("Edit Folders", "editfolder.png", "Edit Folder");
		jEditFolderButton.addActionListener(this);
		jEditFolderButton.setActionCommand("edit-folder");
		leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
		jInactive = new JCheckBox("Inactive event");
		jInactive.addActionListener(this);
		jInactive.setActionCommand("set-inactive");
		leftPanel.add(jRefreshTreeButton, null);
		leftPanel.add(jNewFolderButton, null);
		leftPanel.add(jEditFolderButton, null);
		leftPanel.add(jInactive, null);

		JPanel rightPanel = new JPanel();
		JCustomButton jAddButton = new JCustomButton("Add", "add.png", "Add");
		jAddButton.addActionListener(this);
		jAddButton.setActionCommand("add-result");
		jEditButton = new JCustomButton("Edit", "edit.png", "Edit");
		jEditButton.addActionListener(this);
		jEditButton.setActionCommand("edit-result");
		jEditButton.setEnabled(false);
		jCopyButton = new JCustomButton("Copy", "copy.png", "Copy");
		jCopyButton.addActionListener(this);
		jCopyButton.setActionCommand("copy-result");
		jCopyButton.setEnabled(false);
		JCustomButton jAddMultipleButton = new JCustomButton("Add+", "addmultiple.png", "Add Multiple");
		jAddMultipleButton.addActionListener(this);
		jAddMultipleButton.setActionCommand("addmultiple-result");
		jRemoveButton = new JCustomButton("Remove", "remove.png", "Remove");
		jRemoveButton.addActionListener(this);
		jRemoveButton.setActionCommand("remove-result");
		jRemoveButton.setEnabled(false);
		rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 1));
		rightPanel.add(jAddButton, null);
		rightPanel.add(jAddMultipleButton, null);
		rightPanel.add(jEditButton, null);
		rightPanel.add(jCopyButton, null);
		rightPanel.add(jRemoveButton, null);

		JPanel p = new JPanel(new BorderLayout(0, 0));
		p.setPreferredSize(new Dimension(0, 26));
		p.add(leftPanel, BorderLayout.WEST);
		p.add(rightPanel, BorderLayout.EAST);

		return p;
	}

	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
		if (node != null) {
			Object info = node.getUserObject();
			String param = String.valueOf(((PicklistBean)info).getParam());
			String[] t = param.split(",");
			idSport = new Integer(t[0]);
			idChampionship = (t.length > 1 ? new Integer(t[1]) : null);
			idEvent = (t.length > 2 ? new Integer(t[2]) : null);
			idSubevent = (t.length > 3 ? new Integer(t[3]) : null);
			idSubevent2 = (t.length > 4 ? new Integer(t[4]) : null);
			if (node.isLeaf())
				loadData(param);
		}
	}

	private String getEntityTxt(int type, ResultsBean rb, int index) {
		StringBuffer sb = new StringBuffer();
		try {
			Object id = ResultsBean.class.getMethod("getRsRank" + index).invoke(rb);
			Object rel1Id = ResultsBean.class.getMethod("getEn" + index + "Rel1Id").invoke(rb);
			Object rel2Id = ResultsBean.class.getMethod("getEn" + index + "Rel2Id").invoke(rb);
			Object res = (index <= 5 ? ResultsBean.class.getMethod("getRsResult" + index).invoke(rb) : null);
			if (id != null) {
				String str1 = String.valueOf(ResultsBean.class.getMethod("getEn" + index + "Str1").invoke(rb));
				String str2 = String.valueOf(ResultsBean.class.getMethod("getEn" + index + "Str2").invoke(rb));
				sb.append(type < 10 ? str1 + (StringUtils.notEmpty(str2) ? ", " + str2 : "") : (str2 + (type == 99 ? " [" + str1 + "]" : "")));
			}
			if (rel2Id != null) {
				String str = String.valueOf(ResultsBean.class.getMethod("getEn" + index + "Rel2Code").invoke(rb));
				sb.append(" [" + str + "]");
			}
			if (rel1Id != null) {
				String str = String.valueOf(ResultsBean.class.getMethod("getEn" + index + "Rel1Label").invoke(rb));
				sb.append(" [" + str + "]");
			}
			if (StringUtils.notEmpty(res))
				sb.append(" - " + res);
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
		return sb.toString();
	}

	private void loadData(String s) {
		try {
			String[] t = s.split(",");
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(idSport);
			params.add(idChampionship);
			params.add(idEvent);
			params.add(idSubevent != null ? idSubevent : 0);
			params.add(idSubevent2 != null ? idSubevent2 : 0);
			params.add("0");
			params.add("");
			Event ev = (Event) DatabaseHelper.loadEntity(Event.class, new Integer(String.valueOf(t.length > 4 ? params.get(4) : (t.length > 3 ? params.get(3) : params.get(2)))));
			int type = ev.getType().getNumber();
			Collection<ResultsBean> list = (Collection<ResultsBean>) DatabaseHelper.call("GetResults", params);
			Vector<Vector> v = new Vector<Vector>();
			for (ResultsBean rb : list) {
				Vector v_ = new Vector();				
				v_.add(rb.getRsId());
				v_.add(rb.getYrLabel());
				for (int i = 1 ; i <= 10 ; i++)
					v_.add(getEntityTxt(type, rb, i));
				v_.add(StringUtils.notEmpty(rb.getRsDate1()) ? rb.getRsDate1() : "");
				v_.add(StringUtils.notEmpty(rb.getRsDate2()) ? rb.getRsDate2() : "");
				v_.add((rb.getCx1Id() != null ? rb.getCx1Label() + " [" : "") + (rb.getCt1Id() != null ? rb.getCt1Label() + (rb.getSt1Id() != null ? ", " + rb.getSt1Code() : "") + ", " + rb.getCn1Code() : (rb.getCt2Id() != null ? rb.getCt2Label() + (rb.getSt2Id() != null ? ", " + rb.getSt2Code() : "") + ", " + rb.getCn2Code() : "")) + (rb.getCx1Id() != null ? "]" : ""));
				v_.add((rb.getCx2Id() != null ? rb.getCx2Label() + " [" : "") + (rb.getCt3Id() != null ? rb.getCt3Label() + (rb.getSt3Id() != null ? ", " + rb.getSt3Code() : "") + ", " + rb.getCn3Code() : (rb.getCt4Id() != null ? rb.getCt4Label() + (rb.getSt4Id() != null ? ", " + rb.getSt4Code() : "") + ", " + rb.getCn4Code() : "")) + (rb.getCx2Id() != null ? "]" : ""));
				v.add(v_);
			}
			Vector<String> cols = new Vector<String>();
			cols.add("ID");cols.add("Year");
			cols.add("1st");cols.add("2nd");cols.add("3rd");cols.add("4th");cols.add("5th");
			cols.add("6th");cols.add("7th");cols.add("8th");cols.add("9th");cols.add("10th");
			cols.add("Date #1");cols.add("Date #2");cols.add("Place #1");cols.add("Place #2");
			jResultTable = new JTable(v, cols) {
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			jResultTable.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (e.getClickCount() == 2)
						actionPerformed(new ActionEvent(jEditButton, 0, "edit-result"));
					jEditButton.setEnabled(true);
					jCopyButton.setEnabled(true);
					jRemoveButton.setEnabled(true);
				}
			});
			jResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			for (int i = 0 ; i < jResultTable.getColumnCount() ; i++)
				jResultTable.getColumnModel().getColumn(i).setPreferredWidth(i < 2 ? 50 : (i == 12 || i == 13 ? 80 : 200));
			jScrollPane.setViewportView(jResultTable);
			jEditButton.setEnabled(false);
			jCopyButton.setEnabled(false);
			jRemoveButton.setEnabled(false);
			String hql = "from InactiveItem where id_sport=" + idSport + " and id_championship=" + idChampionship + " and id_event=" + idEvent;
			hql += (idSubevent != null ? " and id_subevent=" + idSubevent : "");
			hql += (idSubevent2 != null ? " and id_subevent2=" + idSubevent2 : "");
			Object o = DatabaseHelper.loadEntityFromQuery(hql);
			jInactive.setSelected(o != null);
			jQueryStatus.clear();
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
	}

	public void resultCallback(short mode, Vector v, String msg, boolean err) {
		if (!err) {
			if (mode == JEditResultDialog.NEW || mode == JEditResultDialog.COPY)
				((DefaultTableModel)jResultTable.getModel()).addRow(v);
			else
				for (int i = 0 ; i < jResultTable.getColumnCount() ; i++)
					jResultTable.setValueAt(v.get(i), jResultTable.getSelectedRow(), i);
		}
		jQueryStatus.set(err ? JQueryStatus.FAILURE : JQueryStatus.SUCCESS, msg);
	}

	public void folderCallback(String msg, boolean err) {
		if (!err) {
			if (jResultTable != null)
				jResultTable.setVisible(false);
			setTree();
		}
		jQueryStatus.set(err ? JQueryStatus.FAILURE : JQueryStatus.SUCCESS, msg);
	}

	public void addMultipleCallback(String msg, boolean err) {
		if (!err)
			loadData(idSport + "," + idChampionship + "," + idEvent + (idSubevent != null && idSubevent > 0 ? "," + idSubevent : "") + (idSubevent2 != null && idSubevent2 > 0 ? "," + idSubevent2 : ""));
		jQueryStatus.set(err ? JQueryStatus.FAILURE : JQueryStatus.SUCCESS, msg);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			jQueryStatus.clear();
			if (e.getActionCommand().matches("refresh-tree")) {
				if (jResultTable != null)
					jResultTable.setVisible(false);
				setTree();
			}
			else if (e.getActionCommand().matches("(add|edit)-folder")) {
				JEditFolderDialog dlg = JMainFrame.getFolderDialog();
				SwingUtils.selectValue(dlg.getSport(), idSport);
				SwingUtils.selectValue(dlg.getCategory1(), idChampionship);
				SwingUtils.selectValue(dlg.getCategory2(), idEvent);
				SwingUtils.selectValue(dlg.getCategory3(), idSubevent);
				SwingUtils.selectValue(dlg.getCategory4(), idSubevent2);
				List<PicklistBean> list = new ArrayList<PicklistBean>();
				for (TreePath path : jTree.getSelectionPaths()) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
					Object info = node.getUserObject();
					list.add((PicklistBean)info);
				}
				dlg.open(this, list);
			}
			else if (e.getActionCommand().matches("set-inactive")) {
				String msg = null;
				boolean err = false;
				try {
					String hql = "from InactiveItem where id_sport=" + idSport + " and id_championship=" + idChampionship + " and id_event=" + idEvent;
					hql += (idSubevent != null ? " and id_subevent=" + idSubevent : "");
					hql += (idSubevent2 != null ? " and id_subevent2=" + idSubevent2 : "");
					Object o = DatabaseHelper.loadEntityFromQuery(hql);
					if (o != null)
						DatabaseHelper.removeEntity(o);
					if (jInactive.isSelected()) {
						InactiveItem item = new InactiveItem();
						item.setIdSport(idSport);
						item.setIdChampionship(idChampionship);
						item.setIdEvent(idEvent);
						item.setIdSubevent(idSubevent);
						item.setIdSubevent2(idSubevent2);
						DatabaseHelper.saveEntity(item, null);
					}
					msg = "Inactive item has been successfully updated.";
				}
				catch (Exception e_) {
					err = true;
					msg = e_.getMessage();
					Logger.getLogger("sh").error(e_.getMessage(), e_);
				}
				finally {
					jQueryStatus.set(err ? JQueryStatus.FAILURE : JQueryStatus.SUCCESS, msg);
				}
			}
			else if (e.getActionCommand().matches("(add|copy|edit)-result")) {
				boolean isAdd = e.getActionCommand().matches("add.*");
				boolean isEdit = e.getActionCommand().matches("edit.*");
				boolean isCopy = e.getActionCommand().matches("copy.*");
				Result rs = null;
				String resultId = null;
				int type = 0;
				HashMap<String, ArrayList<PicklistBean>> pl = JMainFrame.getPicklists();
				JEditResultDialog rd = JMainFrame.getResultDialog();
				rd.clear();
				if (isAdd) {
					Event ev = (Event)DatabaseHelper.loadEntity(Event.class, new Integer(idEvent));
					Event se = (Event)(idSubevent != null ? DatabaseHelper.loadEntity(Event.class, new Integer(idSubevent)) : null);
					Event se2 = (Event)(idSubevent2 != null ? DatabaseHelper.loadEntity(Event.class, new Integer(idSubevent2)) : null);
					type = (se2 != null ? se2.getType().getNumber() : (se != null ? se.getType().getNumber() : ev.getType().getNumber()));
				}
				else {
					resultId = String.valueOf(jResultTable.getValueAt(jResultTable.getSelectedRow(), 0));
					rs = (Result) DatabaseHelper.loadEntity(Result.class, new Integer(resultId));
					SwingUtils.selectValue(rd.getYear(), rs.getYear().getId());
					if (rs.getComplex1() != null)
						SwingUtils.selectValue(rd.getComplex1(), rs.getComplex1().getId());
					if (rs.getCity1() != null)
						SwingUtils.selectValue(rd.getCity1(), rs.getCity1().getId());
					if (rs.getComplex2() != null)
						SwingUtils.selectValue(rd.getComplex2(), rs.getComplex2().getId());
					if (rs.getCity2() != null)
						SwingUtils.selectValue(rd.getCity2(), rs.getCity2().getId());
					rd.getDate1().setText(StringUtils.notEmpty(rs.getDate1()) ? rs.getDate1() : null);
					rd.getDate2().setText(StringUtils.notEmpty(rs.getDate2()) ? rs.getDate2() : null);
					rd.getComment().setText(StringUtils.notEmpty(rs.getComment()) ? rs.getComment() : null);
					rd.getExa().setText(StringUtils.notEmpty(rs.getExa()) ? rs.getExa() : null);
					List<Integer> lTie = StringUtils.tieList(rs.getExa());
					for (int i = 0 ; i < 10 ; i++)
						rd.getExaCheckbox()[i].setSelected(lTie.contains(i + 1));
					type = (rs.getSubevent2() != null ? rs.getSubevent2().getType().getNumber() : (rs.getSubevent() != null ? rs.getSubevent().getType().getNumber() : rs.getEvent().getType().getNumber()));
				}
				JEntityPicklist[] ranks = rd.getRanks();
				JTextField[] res = rd.getRes();
				Object param = (type != 99 ? idSport : null);
				String alias = (type < 10 ? Athlete.alias : (type == 50 ? Team.alias : Country.alias));
				for (int i = 0 ; i < ranks.length ; i++) {
					SwingUtils.fillPicklist(ranks[i], pl.get(alias), param);
					if (isCopy || isEdit) {
						Object id = Result.class.getMethod("getIdRank" + (i + 1)).invoke(rs);
						Object res_ = Result.class.getMethod("getResult" + (i + 1)).invoke(rs);
						SwingUtils.selectValue(ranks[i], id != null ? (Integer) id : null);
						res[i].setText(StringUtils.notEmpty(res_) ? String.valueOf(res_) : null);
					}
				}
				rd.open(this, resultId != null ? new Integer(resultId) : null, null, isAdd ? JEditResultDialog.NEW : (isCopy ? JEditResultDialog.COPY : JEditResultDialog.EDIT), type);
			}
			else if (e.getActionCommand().equals("addmultiple-result"))
				JMainFrame.getAddMultipleDialog().open(this);
			else if (e.getActionCommand().equals("remove-result")) {
				String resultId = String.valueOf(jResultTable.getValueAt(jResultTable.getSelectedRow(), 0));
				Object[] options = {"Yes", "No"};
				int n = JOptionPane.showOptionDialog(this, "Remove result #" + resultId + "?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (n != 0)
					return;
				String msg = null;
				boolean err = false;
				try {
					DatabaseHelper.removeEntity(DatabaseHelper.loadEntity(Result.class, resultId));
					msg = "Result #" + resultId + " has been successfully removed.";
					((DefaultTableModel)jResultTable.getModel()).removeRow(jResultTable.getSelectedRow());
				}
				catch (Exception e_) {
					err = true;
					msg = e_.getMessage();
					Logger.getLogger("sh").error(e_.getMessage(), e_);
				}
				finally {
					jQueryStatus.set(err ? JQueryStatus.FAILURE : JQueryStatus.SUCCESS, msg);
				}
			}
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
		}
	}

	public static Integer getIdSport() {
		return idSport;
	}

	public static Integer getIdChampionship() {
		return idChampionship;
	}

	public static Integer getIdEvent() {
		return idEvent;
	}

	public static Integer getIdSubevent() {
		return idSubevent;
	}
	
	public static Integer getIdSubevent2() {
		return idSubevent2;
	}

	public List<PicklistBean> getTreeItems() {
		return treeItems;
	}

}