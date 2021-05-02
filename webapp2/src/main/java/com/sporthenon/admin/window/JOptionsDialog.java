package com.sporthenon.admin.window;

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
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.container.JTopPanel;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;

public class JOptionsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JOptionsDialog.class.getName());
	
	private JTopPanel parent;
	private JComboBox<String> jEnvironment;
	private JTextField jHost;
	private JTextField jDatabase;
	private JTextField jLogin;
	private JCheckBox jAlwaysTop = null;
	private JTextField jCredentialsFile;
	
	private Properties props = null;
	private Map<String, String> hConfig = null;
	private String userHomeDir = null;
	private final String[] tHost = new String[] {"sporthenon.com", "localhost"};
	private final String[] tDatabase = new String[] {"shdb", "shlocal"};

	public JOptionsDialog(JFrame owner) {
		super(owner);
		initialize();
	}

	private void initialize() {
		try {
			try {
				userHomeDir = System.getProperty("user.home");
			}
			catch (Exception e) {}
			props = new Properties();
			File f = new File(userHomeDir + "\\shupdate.xml");
			InputStream is = (StringUtils.notEmpty(userHomeDir) && f.exists() ? new FileInputStream(f) : ConfigUtils.class.getResourceAsStream("/com/sporthenon/admin/options.xml"));
			props.loadFromXML(is);
			hConfig = new HashMap<String, String>();
			for (Object key : props.keySet()) {
				hConfig.put(String.valueOf(key), props.getProperty(String.valueOf(key)));
			}
		}
		catch (IOException e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(370, 320));
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
		p.add(getCredentialsPanel());
		jContentPane.add(p, BorderLayout.NORTH);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}

	private JPanel getDatabasePanel() {
		JPanel p = new JPanel(new GridLayout(4, 2, 5, 5));
		p.setBorder(BorderFactory.createTitledBorder(null, "Database", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jEnvironment = new JComboBox<String>();
		jEnvironment.addItem("Prod");
		jEnvironment.addItem("Local");
		jEnvironment.addItem("Custom");
		jEnvironment.setSelectedIndex(-1);
		jEnvironment.setPreferredSize(new Dimension(350, 22));
		for (int i = 0 ; i < 2 ; i++) {
			if (tHost[i].equals(hConfig.get("db.host")) && tDatabase[i].equals(hConfig.get("db.name"))) {
				jEnvironment.setSelectedIndex(i);
			}
		}
		if (jEnvironment.getSelectedIndex() == -1) {
			jEnvironment.setSelectedIndex(2);
		}
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
		jHost.setEnabled(jEnvironment.getSelectedIndex() == 2);
		jDatabase.setEnabled(jEnvironment.getSelectedIndex() == 2);
		return p;
	}

	private JPanel getWindowPanel() {
		JPanel p = new JPanel(new GridLayout(1, 1, 5, 5));
		p.setBorder(BorderFactory.createTitledBorder(null, "Window", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jAlwaysTop = new JCheckBox("Always on top");
		jAlwaysTop.setSelected(hConfig.get("alwaystop") != null && hConfig.get("alwaystop").equals("1"));
		p.add(jAlwaysTop);
		return p;
	}
	
	private JPanel getCredentialsPanel() {
		JPanel p = new JPanel(new GridLayout(2, 1, 2, 2));
		p.setBorder(BorderFactory.createTitledBorder(null, "Storage credentials", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jCredentialsFile = new JTextField(hConfig.get("cred.file"));
		p.add(new JLabel(" JSON credentials file:"));
		p.add(jCredentialsFile);
		return p;
	}
	
	public void open(JTopPanel parent) {
		this.parent = parent;
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			try {
				if (StringUtils.notEmpty(userHomeDir)) {
					props = new Properties();
					props.setProperty("db.host", jHost.getText());
					props.setProperty("db.name", jDatabase.getText());
					props.setProperty("db.user", jLogin.getText());
					props.setProperty("alwaystop", jAlwaysTop.isSelected() ? "1" : "0");
					props.setProperty("cred.file", jCredentialsFile.getText());
					props.storeToXML(new FileOutputStream(new File(userHomeDir + "\\shupdate.xml")), null);
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
				jHost.setEnabled(jEnvironment.getSelectedIndex() == 2);
				jDatabase.setEnabled(jEnvironment.getSelectedIndex() == 2);
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
	
	public JTextField getCredentialsFile() {
		return jCredentialsFile;
	}
	
}