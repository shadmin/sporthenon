package com.sporthenon.admin.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.sporthenon.utils.res.ResourceUtils;

public class JQueryStatus extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel jLabel = null;
	private JProgressBar jConnectProgress = null;

	public static final short SUCCESS = 0;
	public static final short FAILURE = 1;

	public JQueryStatus() {
		super();
		jLabel = new JLabel();
		jLabel.setFont(new Font("Verdana", Font.BOLD, 11));
		jConnectProgress = new JProgressBar();
		jConnectProgress.setBorder(null);
		jConnectProgress.setIndeterminate(false);
		jConnectProgress.setVisible(false);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEtchedBorder());
		this.add(jLabel, BorderLayout.CENTER);
		this.add(jConnectProgress, BorderLayout.CENTER);
	}

	public void clear() {
		jLabel.setText("");
		jLabel.setIcon(null);
	}

	public void set(short status, String msg) {
		if (status > -1) {
			this.add(jLabel, BorderLayout.CENTER);
			jLabel.setText(msg);
			jLabel.setForeground(status == SUCCESS ? new Color(0, 153, 51) : Color.red);
			jLabel.setIcon(ResourceUtils.getIcon(status == SUCCESS ? "success.png" : "failure.png"));
		}
		else {
			jLabel.setText("");
			jLabel.setIcon(null);
		}
	}

	public void showProgress() {
		jConnectProgress.setIndeterminate(true);
		jConnectProgress.setVisible(true);
	}

	public void hideProgress() {
		jConnectProgress.setIndeterminate(false);
		jConnectProgress.setVisible(false);
	}

}