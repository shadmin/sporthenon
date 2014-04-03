package com.sporthenon.updater.container.tab;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.meta.News;
import com.sporthenon.updater.component.JCustomButton;
import com.sporthenon.updater.component.JQueryStatus;
import com.sporthenon.updater.window.JMainFrame;

public class JNewsPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JMainFrame main = null;
	private JTextField jDate = null;
	private JTextArea jText = null;

	public JNewsPanel(JMainFrame parent) {
		this.main = parent;
		initialize();
	}

	public JTextField getjDate() {
		return jDate;
	}

	public JTextArea getjText() {
		return jText;
	}

	public void setjDate(JTextField jDate) {
		this.jDate = jDate;
	}

	public void setjText(JTextArea jText) {
		this.jText = jText;
	}

	private void initialize() {
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		this.add(new JLabel("Date: "));
		jDate = new JTextField();
		jDate.setPreferredSize(new Dimension(200, 22));
		this.add(jDate);
		this.add(new JLabel(" Text: "));
		jText = new JTextArea();
		JScrollPane commentPane = new JScrollPane(jText);
		commentPane.setPreferredSize(new Dimension(400, 200));
		this.add(commentPane);
		JCustomButton saveButton = new JCustomButton("Save", "updater/save.png", null);
		saveButton.addActionListener(this);
		saveButton.setActionCommand("save");
		this.add(saveButton);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().matches("save")) {
				News n = new News();
				n.setDateText(jDate.getText());
				n.setTextHtml(jText.getText());
				n.setDate(new Timestamp(System.currentTimeMillis()));
				DatabaseHelper.saveEntity(n, null);
				main.getQueryStatus().set(JQueryStatus.SUCCESS, "News created.");
			}
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
		}
	}

}