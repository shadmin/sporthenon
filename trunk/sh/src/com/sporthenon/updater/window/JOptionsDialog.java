package com.sporthenon.updater.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.sporthenon.updater.component.JDialogButtonBar;
import com.sporthenon.updater.container.JTopPanel;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.RegUtils;
import com.sporthenon.utils.StringUtils;

public class JOptionsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTopPanel parent;
	private JComboBox jEnvironment;
	private JTextField jHost;
	private JTextField jDatabase;
	private JTextField jLogin;
	private JCheckBox jAlwaysTop = null;
	private JTextField jProxyAddr;
	private JTextField jProxyPort;
	
	private Properties props = null;
	private HashMap<String, String> hConfig = null;
	private String configDir = null;
	private final String[] tHost = new String[] {"92.243.3.85", "92.243.3.85", "localhost"};
	private final String[] tDatabase = new String[] {"shprod", "shtest", "shlocal"};

	public JOptionsDialog(JFrame owner) {
		super(owner);
		initialize();
	}

	private void initialize() {
		try {
			try {
				configDir = RegUtils.readRegistry("HKEY_CURRENT_USER\\Software\\Sporthenon", "configDir");
			}
			catch (Exception e) {}
			props = new Properties();
			File f = new File(configDir + "\\options.xml");
			InputStream is = (StringUtils.notEmpty(configDir) && f.exists() ? new FileInputStream(f) : ConfigUtils.class.getResourceAsStream("/com/sporthenon/updater/options.xml"));
			props.loadFromXML(is);
			hConfig = new HashMap<String, String>();
			for (Object key : props.keySet())
				hConfig.put(String.valueOf(key), props.getProperty(String.valueOf(key)));
		}
		catch (IOException e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(350, 320));
		this.setSize(this.getPreferredSize());
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Options");
		this.setContentPane(jContentPane);

		JDialogButtonBar jButtonBar = new JDialogButtonBar(this);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(new BorderLayout());
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(getDatabasePanel());
		p.add(getWindowPanel());
		p.add(getProxyPanel());
		jContentPane.add(p, BorderLayout.NORTH);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}

	private JPanel getDatabasePanel() {
		JPanel p = new JPanel(new GridLayout(4, 2, 5, 5));
		p.setBorder(BorderFactory.createTitledBorder(null, "Database", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jEnvironment = new JComboBox();
		jEnvironment.addItem("Production");
		jEnvironment.addItem("Test");
		jEnvironment.addItem("Local");
		jEnvironment.addItem("Custom");
		jEnvironment.setSelectedIndex(-1);
		jEnvironment.setPreferredSize(new Dimension(350, 22));
		for (int i = 0 ; i < 3 ; i++)
			if (tHost[i].equals(hConfig.get("db.host")) && tDatabase[i].equals(hConfig.get("db.name")))
				jEnvironment.setSelectedIndex(i);
		if (jEnvironment.getSelectedIndex() == -1)
			jEnvironment.setSelectedIndex(3);
		jEnvironment.setActionCommand("env");
		jEnvironment.addActionListener(this);
		p.add(new JLabel(" Environment:"));
		p.add(jEnvironment);
		jHost = new JTextField(hConfig.get("db.host"));
		p.add(new JLabel(" Host:"));
		p.add(jHost);
		jDatabase = new JTextField(hConfig.get("db.name"));
		p.add(new JLabel(" Database:"));
		p.add(jDatabase);
		jLogin = new JTextField(hConfig.get("db.user"));
		p.add(new JLabel(" User ID:"));
		p.add(jLogin);
		jHost.setEnabled(jEnvironment.getSelectedIndex() == 3);
		jDatabase.setEnabled(jEnvironment.getSelectedIndex() == 3);
		return p;
	}

	private JPanel getWindowPanel() {
		JPanel p = new JPanel(new GridLayout(1, 1, 0, 0));
		p.setBorder(BorderFactory.createTitledBorder(null, "Window", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jAlwaysTop = new JCheckBox("Always on top");
		jAlwaysTop.setSelected(hConfig.get("alwaystop") != null && hConfig.get("alwaystop").equals("1"));
		p.add(jAlwaysTop);
		return p;
	}
	
	private JPanel getProxyPanel() {
		JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
		p.setBorder(BorderFactory.createTitledBorder(null, "Proxy", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jProxyAddr = new JTextField(hConfig.get("proxy.addr"));
		jProxyAddr.setPreferredSize(new Dimension(350, 22));
		p.add(new JLabel(" Address:"));
		p.add(jProxyAddr);
		jProxyPort = new JTextField(hConfig.get("proxy.port"));
		p.add(new JLabel(" Port:"));
		p.add(jProxyPort);
		return p;
	}

	public void open(JTopPanel parent) {
		this.parent = parent;
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			try {
				if (StringUtils.notEmpty(configDir)) {
					props = new Properties();
					props.setProperty("db.host", jHost.getText());
					props.setProperty("db.name", jDatabase.getText());
					props.setProperty("db.user", jLogin.getText());
					props.setProperty("alwaystop", jAlwaysTop.isSelected() ? "1" : "0");
					props.setProperty("proxy.addr", jProxyAddr.getText());
					props.setProperty("proxy.port", jProxyPort.getText());
					props.storeToXML(new FileOutputStream(new File(configDir + "\\options.xml")), null);
				}
			}
			catch (Exception e_) {}
			parent.optionsCallback();
		}
		else if (e.getActionCommand().equals("env")) {
			if (jEnvironment.getSelectedIndex() > -1) {
				if (jEnvironment.getSelectedIndex() < tHost.length) {
					jHost.setText(tHost[jEnvironment.getSelectedIndex()]);
					jDatabase.setText(tDatabase[jEnvironment.getSelectedIndex()]);
				}
				jHost.setEnabled(jEnvironment.getSelectedIndex() == 3);
				jDatabase.setEnabled(jEnvironment.getSelectedIndex() == 3);
			}
		}
		this.setVisible(!e.getActionCommand().matches("ok|cancel"));
	}

	public JTextField getHost() {
		return jHost;
	}

	public JTextField getDatabase() {
		return jDatabase;
	}

	public JTextField getLogin() {
		return jLogin;
	}

	public JCheckBox getAlwaysTop() {
		return jAlwaysTop;
	}
	
	public JTextField getProxyAddr() {
		return jProxyAddr;
	}
	
	public JTextField getProxyPort() {
		return jProxyPort;
	}

}