package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.component.JRoundPanel;

public class JEditRoundsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	JEditResultDialog parent = null;
	private JDialogButtonBar jButtonBar = null;
	private JScrollPane jScrollPane = null;
	private List<JRoundPanel> rounds = new ArrayList<>();

	public JEditRoundsDialog(JEditResultDialog owner) {
		super(owner);
		parent = (JEditResultDialog) this.getOwner();
		initialize();
	}

	private void initialize() {
		JPanel jContentPane = new JPanel();
		BorderLayout layout = new BorderLayout();
		layout.setVgap(5);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(layout);
		jButtonBar = new JDialogButtonBar(this);
		jContentPane.add(getScrollPane(), BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);

		this.setSize(new Dimension(850, 400));
		this.setContentPane(jContentPane);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}

	private JScrollPane getScrollPane() {
		jScrollPane = new JScrollPane();
		jScrollPane.setBorder(BorderFactory.createEmptyBorder());
		jScrollPane.setViewportView(getRoundsPanel());
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		return jScrollPane;
	}
	
	private JPanel getRoundsPanel() {
		JPanel roundsPanel = new JPanel();
		roundsPanel.setLayout(new GridLayout(20, 18, 0, 0));
		for (int i = 0 ; i < 20 ; i++) {
			rounds.add(new JRoundPanel(roundsPanel));
			//roundsPanel.add(rounds.get(i));
		}
		return roundsPanel;
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("ok")) {
		}
		this.setVisible(false);
	}
	
	public void clear() {
	}

	public void open() {
		this.setTitle("Edit Rounds");
		this.setVisible(true);
	}

	public List<JRoundPanel> getRounds() {
		return rounds;
	}

}