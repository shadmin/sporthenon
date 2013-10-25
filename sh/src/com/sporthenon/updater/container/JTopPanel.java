package com.sporthenon.updater.container;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.sporthenon.updater.component.JCustomButton;
import com.sporthenon.updater.component.JCustomToggleButton;
import com.sporthenon.updater.window.JMainFrame;
import com.sporthenon.updater.window.JOptionsDialog;

public class JTopPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JMainFrame parent = null;
	private JCustomButton jConnectButton = null;
	private JCustomToggleButton jResultsButton = null;
	private JCustomToggleButton jDataButton = null;
	private JCustomToggleButton jPicturesButton = null;
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

		jConnectButton = new JCustomButton("Connect", "common/connect.png");
		jConnectButton.setActionCommand("connect");
		setButtonLayout(jConnectButton);
		jResultsButton = new JCustomToggleButton("Results", "updater/results.png", "results");
		jResultsButton.setActionCommand("results");
		setButtonLayout(jResultsButton);
		jDataButton = new JCustomToggleButton("Data", "updater/data.png", "data");
		jDataButton.setActionCommand("data");
		setButtonLayout(jDataButton);
		jPicturesButton = new JCustomToggleButton("Pictures", "updater/image.png", "pictures");
		jPicturesButton.setActionCommand("pictures");
		setButtonLayout(jPicturesButton);
		jImportButton = new JCustomButton("Import", "updater/import.png");
		jImportButton.setActionCommand("import");
		jImportButton.setEnabled(false);
		setButtonLayout(jImportButton);
		jQueryButton = new JCustomButton("Query", "updater/query.png");
		jQueryButton.setActionCommand("query");
		jQueryButton.setEnabled(false);
		setButtonLayout(jQueryButton);
		jOptionsButton = new JCustomButton("Options", "common/options.png");
		jOptionsButton.setActionCommand("options");
		setButtonLayout(jOptionsButton);
		jInfoButton = new JCustomButton("Info", "common/info.png");
		jInfoButton.setActionCommand("info");
		setButtonLayout(jInfoButton);
		jCloseButton = new JCustomButton("Close", "updater/close.png");
		jCloseButton.setActionCommand("close");
		setButtonLayout(jCloseButton);

		this.add(jConnectButton, null);
		this.add(jResultsButton, null);
		this.add(jDataButton, null);
		this.add(jPicturesButton, null);
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
						if (connected) {
							jResultsButton.setEnabled(true);
							jDataButton.setEnabled(true);
							jImportButton.setEnabled(true);
							jQueryButton.setEnabled(true);
							jPicturesButton.setEnabled(true);
							jResultsButton.setSelected(true);
							parent.changeTabPanel("results");
						}
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