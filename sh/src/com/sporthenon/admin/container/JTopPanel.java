package com.sporthenon.admin.container;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JCustomToggleButton;
import com.sporthenon.admin.window.JMainFrame;
import com.sporthenon.admin.window.JOptionsDialog;

public class JTopPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JMainFrame parent = null;
	private JCustomButton jConnectButton = null;
	private JCustomToggleButton jResultsButton = null;
	private JCustomToggleButton jDataButton = null;
	private JCustomToggleButton jPicturesButton = null;
	private JCustomToggleButton jExtLinksButton = null;
	private JCustomToggleButton jUsersButton = null;
	
	private JCustomButton jImportButton = null;
	private JCustomButton jQueryButton = null;
	private JCustomButton jOptionsButton = null;
	private JCustomButton jInfoButton = null;
	private JCustomButton jCloseButton = null;
	private boolean connected = false;

	public JTopPanel(JMainFrame parent) {
		super();
		this.parent = parent;
		initialize();
	}

	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setHgap(1);
		flowLayout.setVgap(1);
		this.setLayout(flowLayout);
		//this.setBorder(BorderFactory.createEtchedBorder());
		this.setPreferredSize(new Dimension(0, 50));

		jConnectButton = new JCustomButton("Connect", "common/connect.png", null);
		jConnectButton.setActionCommand("connect");
		setButtonLayout(jConnectButton);
		jResultsButton = new JCustomToggleButton("Results", "results.png", "results");
		jResultsButton.setActionCommand("results");
		setButtonLayout(jResultsButton);
		jDataButton = new JCustomToggleButton("Data", "data.png", "data");
		jDataButton.setActionCommand("data");
		setButtonLayout(jDataButton);
		jPicturesButton = new JCustomToggleButton("Pictures", "image.png", "pictures");
		jPicturesButton.setActionCommand("pictures");
		setButtonLayout(jPicturesButton);
		jExtLinksButton = new JCustomToggleButton("Ext. Links", "weblinks.png", "extlinks");
		jExtLinksButton.setActionCommand("extlinks");
		setButtonLayout(jExtLinksButton);
		jUsersButton = new JCustomToggleButton("Users", "users.png", "users");
		jUsersButton.setActionCommand("users");
		setButtonLayout(jUsersButton);
		jImportButton = new JCustomButton("Import", "import.png", null);
		jImportButton.setActionCommand("import");
		jImportButton.setEnabled(false);
		setButtonLayout(jImportButton);
		jQueryButton = new JCustomButton("Query", "query.png", null);
		jQueryButton.setActionCommand("query");
		jQueryButton.setEnabled(false);
		setButtonLayout(jQueryButton);
		jOptionsButton = new JCustomButton("Options", "common/options.png", null);
		jOptionsButton.setActionCommand("options");
		setButtonLayout(jOptionsButton);
		jInfoButton = new JCustomButton("Info", "common/info.png", null);
		jInfoButton.setActionCommand("info");
		setButtonLayout(jInfoButton);
		jCloseButton = new JCustomButton("Close", "close.png", null);
		jCloseButton.setActionCommand("close");
		setButtonLayout(jCloseButton);

		this.add(jConnectButton, null);
		this.add(jResultsButton, null);
		this.add(jDataButton, null);
		this.add(jPicturesButton, null);
		this.add(jExtLinksButton, null);
		this.add(jUsersButton, null);
		this.add(jImportButton, null);
		this.add(jQueryButton, null);
		this.add(jOptionsButton, null);
		this.add(jInfoButton, null);
		this.add(jCloseButton, null);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("connect")) {
			new Thread (new Runnable() {
				public void run() {
					if (parent.connectCallback(!connected)) {
						connected = !connected;
						jConnectButton.setText(connected ? "Disconnect" : "Connect");
						jConnectButton.setIcon("common/" + (connected ? "disconnect" : "connect") + ".png");
						jResultsButton.setEnabled(connected);
						jDataButton.setEnabled(connected);
						jImportButton.setEnabled(connected);
						jQueryButton.setEnabled(connected);
						jPicturesButton.setEnabled(connected);
						jExtLinksButton.setEnabled(connected);
						jUsersButton.setEnabled(connected);
						jResultsButton.setSelected(connected);
						if (connected)
							parent.changeTabPanel("results");
					}
				}
			}).start();
		}
		else if (e.getActionCommand().equals("import")) {
			JMainFrame.getImportDialog().open(this);
		}
		else if (e.getActionCommand().equals("query")) {
			JMainFrame.getQueryDialog().open();
		}
		else if (e.getActionCommand().equals("options")) {
			JMainFrame.getOptionsDialog().open(this);
		}
		else if (e.getActionCommand().equals("info")) {
			JMainFrame.getInfoDialog().open();
		}
		else if (e.getActionCommand().equals("close")) {
			parent.dispose();
		}
	}

	public void optionsCallback() {
		JOptionsDialog dlg = JMainFrame.getOptionsDialog();
		parent.setAlwaysOnTop(dlg.getAlwaysTop().isSelected());
	}

	private void setButtonLayout(AbstractButton btn) {
		btn.setSize(new Dimension(62, 43));
		btn.setPreferredSize(jConnectButton.getSize());
		btn.setHorizontalAlignment(SwingConstants.CENTER);
		btn.setHorizontalTextPosition(SwingConstants.CENTER);
		btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setIconTextGap(3);
		btn.addActionListener(this);
		if (btn instanceof JCustomToggleButton) {
			btn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					parent.changeTabPanel(e.getActionCommand());
					jResultsButton.setSelected(e.getActionCommand().equalsIgnoreCase("results"));
					jDataButton.setSelected(e.getActionCommand().equalsIgnoreCase("data"));
					jPicturesButton.setSelected(e.getActionCommand().equalsIgnoreCase("pictures"));
					jExtLinksButton.setSelected(e.getActionCommand().equalsIgnoreCase("extlinks"));
					jUsersButton.setSelected(e.getActionCommand().equalsIgnoreCase("users"));
				}
			});
		}
	}

	public JCustomToggleButton getResultsButton() {
		return jResultsButton;
	}

	public JCustomToggleButton getDataButton() {
		return jDataButton;
	}

	public JCustomToggleButton getPicturesButton() {
		return jPicturesButton;
	}
	
	public JCustomToggleButton getExtLinksButton() {
		return jExtLinksButton;
	}
	
	public JCustomToggleButton getUsersButton() {
		return jUsersButton;
	}
	
	public JCustomButton getImportButton() {
		return jImportButton;
	}
	
	public JCustomButton getQueryButton() {
		return jQueryButton;
	}
	
	//	private JSeparator getSeparator() {
	//		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
	//		separator.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	//		separator.setPreferredSize(new Dimension(10, this.getSize().height));
	//		return separator;
	//	}

}