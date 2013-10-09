package com.sporthenon.updater.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.sporthenon.updater.component.JDialogButtonBar;
import com.sporthenon.utils.SwingUtils;


public class JImportReportDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JImportDialog parent;
	private JScrollPane jScrollPane;
	private JTextArea jReport;

	public JImportReportDialog(JDialog owner) {
		super(owner);
		initialize();
	}

	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(640, 480));
		this.setSize(this.getPreferredSize());
		this.setModal(false);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setTitle("Report");
		this.setContentPane(jContentPane);

		jScrollPane = new JScrollPane();
		jScrollPane.setBorder(BorderFactory.createEtchedBorder());
		jReport = new JTextArea();
		jReport.setFont(SwingUtils.getDefaultFont());
		
		JDialogButtonBar jButtonBar = new JDialogButtonBar(this);
		jButtonBar.getCancel().setVisible(false);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2));
		jContentPane.setLayout(new BorderLayout());
		jContentPane.add(jScrollPane, BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}

	public void open() {
		jScrollPane.setViewportView(jReport);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok"))
			this.setVisible(false);
		this.setVisible(false);
	}

	public JImportDialog getParent() {
		return parent;
	}

	public JTextArea getReport() {
		return jReport;
	}

}