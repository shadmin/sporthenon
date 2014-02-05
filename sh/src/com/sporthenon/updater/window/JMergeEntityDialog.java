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
import javax.swing.JPanel;

import com.sporthenon.updater.component.JDialogButtonBar;
import com.sporthenon.utils.SwingUtils;


public class JMergeEntityDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private String alias;
	private Integer idEntity1;
	private Integer idEntity2;
	private JLabel lEntity1 = null;
	private JLabel lEntity2 = null;
	
	public JMergeEntityDialog(JFrame owner) {
		super(owner);
		initialize();
	}
	
	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(500, 150));
		this.setSize(this.getPreferredSize());
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
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
		JPanel jMainPanel = new JPanel(new GridLayout(4, 1, 3, 3));
		
		JLabel lFrom = new JLabel(" From:");
		jMainPanel.add(lFrom, null);
		
		lEntity1 = new JLabel();
		lEntity1.setFont(SwingUtils.getBoldFont());

		JLabel lTo = new JLabel(" To:");
		jMainPanel.add(lTo, null);
		
		lEntity2 = new JLabel();
		lEntity2.setFont(SwingUtils.getBoldFont());
		
		jMainPanel.add(lFrom, null);
		jMainPanel.add(lEntity1, null);
		jMainPanel.add(lTo, null);
		jMainPanel.add(lEntity2, null);
		
		return jMainPanel;
	}

	public void open(String alias, Integer id1, Integer id2) {
		this.alias = alias;
		this.idEntity1 = id1;
		this.idEntity2 = id2;
		this.setTitle("Merge");
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (!cmd.equals("ok")) {
			setAlias(null);
			setIdEntity1(null);
			setIdEntity2(null);
		}
		this.setVisible(false);
	}

	public String getAlias() {
		return alias;
	}

	public Integer getIdEntity1() {
		return idEntity1;
	}

	public Integer getIdEntity2() {
		return idEntity2;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setIdEntity1(Integer idEntity1) {
		this.idEntity1 = idEntity1;
	}

	public void setIdEntity2(Integer idEntity2) {
		this.idEntity2 = idEntity2;
	}

	public JLabel getlEntity1() {
		return lEntity1;
	}

	public JLabel getlEntity2() {
		return lEntity2;
	}

	public void setlEntity1(JLabel lEntity1) {
		this.lEntity1 = lEntity1;
	}

	public void setlEntity2(JLabel lEntity2) {
		this.lEntity2 = lEntity2;
	}
	
}