package com.sporthenon.updater.container.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.meta.News;
import com.sporthenon.updater.component.JCustomButton;
import com.sporthenon.updater.component.JQueryStatus;
import com.sporthenon.updater.window.JMainFrame;

public class JNewsPanel extends JSplitPane implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;

	private JMainFrame main = null;
	private JList jNewsList = null;
	private JTextField jTitle = null;
	private JTextArea jText = null;
	private JTextField jTitleFR = null;
	private JTextArea jTextFR = null;
	private JLabel jDate = null;
	private List<Integer> lId = null;
	private Integer currentId = null;

	public JNewsPanel(JMainFrame parent) {
		this.main = parent;
		initialize();
	}

	private void initialize() {
		lId = new ArrayList<Integer>();
		jNewsList = new JList(new DefaultListModel());
		JScrollPane leftPanel = new JScrollPane(jNewsList);
		leftPanel.setPreferredSize(new Dimension(150, 0));
		leftPanel.setBorder(BorderFactory.createEmptyBorder());
		leftPanel.setMinimumSize(new Dimension(100, 0));
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 5));
		rightPanel.setLayout(new BorderLayout(10, 10));
		rightPanel.add(getButtonPanel(), BorderLayout.SOUTH);
		rightPanel.add(getNewsPanel(), BorderLayout.CENTER);
		this.setTopComponent(leftPanel);
		this.setBottomComponent(rightPanel);
	}
	
	public void initList(List l) {
		DefaultListModel model = (DefaultListModel) jNewsList.getModel();
		model.clear();
		lId.clear();
		for (News n : (List<News>) l) {
			lId.add(n.getId());
			model.addElement(n.getTitle());
		}
		jNewsList.setName("mainlist");
		jNewsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jNewsList.setLayoutOrientation(JList.VERTICAL);
		jNewsList.setSelectedIndex(0);
		jNewsList.addListSelectionListener(this);
	}
	
	private JPanel getNewsPanel() {
		JPanel p = new JPanel(new GridLayout(10, 1));
		p.add(new JLabel("Title: "));
		jTitle = new JTextField();
		jTitle.setPreferredSize(new Dimension(200, 22));
		p.add(jTitle);
		p.add(new JLabel("Text: "));
		jText = new JTextArea();
		JScrollPane commentPane = new JScrollPane(jText);
		commentPane.setPreferredSize(new Dimension(400, 200));
		p.add(commentPane);
		p.add(new JLabel("Title (FR): "));
		jTitleFR = new JTextField();
		jTitleFR.setPreferredSize(new Dimension(200, 22));
		p.add(jTitleFR);
		p.add(new JLabel("Text (FR): "));
		jTextFR = new JTextArea();
		commentPane = new JScrollPane(jTextFR);
		commentPane.setPreferredSize(new Dimension(400, 200));
		p.add(commentPane);
		p.add(new JLabel("Date: "));
		jDate = new JLabel();
		p.add(jDate);
		return p;
	}
	
	private JPanel getButtonPanel() {
		JPanel rightPanel = new JPanel();
		JCustomButton jAddButton = new JCustomButton(null, "add.png", "Add");
		jAddButton.addActionListener(this);
		jAddButton.setActionCommand("add");
		JCustomButton jSaveButton = new JCustomButton(null, "save.png", "Save");
		jSaveButton.addActionListener(this);
		jSaveButton.setActionCommand("save");
		JCustomButton jRemoveButton = new JCustomButton(null, "remove.png", "Remove");
		jRemoveButton.addActionListener(this);
		jRemoveButton.setActionCommand("remove");
		rightPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
		rightPanel.add(jAddButton, null);
		rightPanel.add(jSaveButton, null);
		rightPanel.add(jRemoveButton, null);

		JPanel p = new JPanel(new BorderLayout(0, 0));
		p.add(rightPanel, BorderLayout.EAST);

		return p;
	}
	

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().matches("add")) {
				News n = new News();				
				n.setTitle(jTitle.getText());
				n.setTextHtml(jText.getText());
				n.setTitleFR(jTitleFR.getText());
				n.setTextHtmlFR(jTextFR.getText());
				n.setDate(new Timestamp(System.currentTimeMillis()));
				n = (News) DatabaseHelper.saveEntity(n, null);
				main.getQueryStatus().set(JQueryStatus.SUCCESS, "News created.");
				lId.add(n.getId());
				((DefaultListModel) jNewsList.getModel()).addElement(n.getTitle());
			}
			else if (e.getActionCommand().matches("save")) {
				News n = (News) DatabaseHelper.loadEntity(News.class, currentId);
				n.setTitle(jTitle.getText());
				n.setTextHtml(jText.getText());
				n.setTitleFR(jTitleFR.getText());
				n.setTextHtmlFR(jTextFR.getText());
				DatabaseHelper.saveEntity(n, null);
				main.getQueryStatus().set(JQueryStatus.SUCCESS, "News modified.");
			}
			else if (e.getActionCommand().matches("remove")) {
				DatabaseHelper.removeEntity(DatabaseHelper.loadEntity(News.class, currentId));
				main.getQueryStatus().set(JQueryStatus.SUCCESS, "News removed.");
				lId.remove(currentId);
				((DefaultListModel) jNewsList.getModel()).removeElementAt(jNewsList.getSelectedIndex());
				jNewsList.setSelectedIndex(0);
				currentId = null;
			}
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		try {
			int index = ((JList)e.getSource()).getSelectedIndex();
			if (index > -1) {
				currentId = lId.get(index);
				News n = (News) DatabaseHelper.loadEntity(News.class, currentId);
				if (n != null) {
					jTitle.setText(n.getTitle());
					jText.setText(n.getTextHtml());
					jTitleFR.setText(n.getTitleFR());
					jTextFR.setText(n.getTextHtmlFR());
					jDate.setText(n.getDate().toString());
				}
			}
			main.getQueryStatus().clear();
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
		}
	}

}