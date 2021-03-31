package com.sporthenon.admin.container.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JImagePanel;
import com.sporthenon.admin.window.JFindEntityDialog;
import com.sporthenon.admin.window.JMainFrame;
import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.PicklistItem;
import com.sporthenon.db.entity.AbstractEntity;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;

public class JPicturesPanel extends JSplitPane implements ActionListener, ListSelectionListener, ItemListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JPicturesPanel.class.getName());
	
	private JList<String> jList = null;
	private String alias = Championship.alias;
	private String currentId;
	private JRadioButton largeRadioBtn = null;
	private JRadioButton smallRadioBtn = null;
	private JTextField jYear1 = null;
	private JTextField jYear2 = null;
	private JImagePanel jLocalPanel = null;
	private JImagePanel jRemotePanel = null;
	private JTextField jLocalFile = null;
	private JTextField jRemoteFile = null;
	private JList<String> jRemoteList = null;
	private JFileChooser jFileChooser = null;
	private JLabel jDescription = null;
	private JComboBox<PicklistItem> jSportList = null;

	public JPicturesPanel(JMainFrame parent) {
		initialize();
	}

	public JList<String> getList() {
		return jList;
	}

	public JComboBox<PicklistItem> getSportList() {
		return jSportList;
	}
	
	private void initialize() {
		initList();
		jFileChooser = new JFileChooser();
		JScrollPane leftPanel = new JScrollPane(jList);
		leftPanel.setPreferredSize(new Dimension(150, 0));
		leftPanel.setBorder(BorderFactory.createEmptyBorder());
		leftPanel.setMinimumSize(new Dimension(100, 0));
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 5));
		rightPanel.setLayout(new BorderLayout(10, 10));
		rightPanel.add(getButtonPanel(), BorderLayout.SOUTH);
		rightPanel.add(getImagePanel(), BorderLayout.CENTER);
		this.setTopComponent(leftPanel);
		this.setBottomComponent(rightPanel);
	}

	private void initList() {
		Vector<String> v = new Vector<String>();
		v.add("Championship");
		v.add("Country");
		v.add("Event");
		v.add("Olympics");
		v.add("Sport");
		v.add("State");
		v.add("Team");
		jList = new JList<>(v);
		jList.setName("mainlist");
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setSelectedIndex(0);
		jList.addListSelectionListener(this);
	}

	private JPanel getButtonPanel() {
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 8));
		jYear1 = new JTextField();
		jYear1.setPreferredSize(new Dimension(40, 20));
		leftPanel.add(new JLabel("From:"));
		leftPanel.add(jYear1);
		jYear2 = new JTextField();
		jYear2.setPreferredSize(new Dimension(40, 20));
		leftPanel.add(new JLabel("to:"));
		leftPanel.add(jYear2);
		largeRadioBtn = new JRadioButton("Large");
		largeRadioBtn.setMargin(new Insets(0, 0, 0, 0));
		largeRadioBtn.addActionListener(this);
		largeRadioBtn.setSelected(true);
		leftPanel.add(largeRadioBtn);
		smallRadioBtn = new JRadioButton("Small");
		smallRadioBtn.setMargin(new Insets(0, 0, 0, 0));
		smallRadioBtn.addActionListener(this);
		leftPanel.add(smallRadioBtn);
		ButtonGroup grp = new ButtonGroup();
		grp.add(largeRadioBtn);
		grp.add(smallRadioBtn);
		jDescription = new JLabel();
		jDescription.setFont(SwingUtils.getBoldFont());
		leftPanel.add(jDescription);
		JPanel rightPanel = new JPanel();
		jSportList = new JComboBox<>();
		jSportList.addItemListener(this);
		JCustomButton jFirstButton = new JCustomButton(null, "first.png", "First");
		jFirstButton.addActionListener(this);
		jFirstButton.setActionCommand("first");
		JCustomButton jPreviousButton = new JCustomButton(null, "previous.png", "Previous");
		jPreviousButton.addActionListener(this);
		jPreviousButton.setActionCommand("previous");
		JCustomButton jFindButton = new JCustomButton(null, "find.png", "Find");
		jFindButton.addActionListener(this);
		jFindButton.setActionCommand("find");
		JCustomButton jNextButton = new JCustomButton(null, "next.png", "Next");
		jNextButton.addActionListener(this);
		jNextButton.setActionCommand("next");
		JCustomButton jLastButton = new JCustomButton(null, "last.png", "Last");
		jLastButton.addActionListener(this);
		jLastButton.setActionCommand("last");
		rightPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
		rightPanel.add(jSportList, null);
		rightPanel.add(jFirstButton, null);
		rightPanel.add(jPreviousButton, null);
		rightPanel.add(jFindButton, null);
		rightPanel.add(jNextButton, null);
		rightPanel.add(jLastButton, null);

		JPanel p = new JPanel(new BorderLayout(0, 0));
		p.setPreferredSize(new Dimension(0, 26));
		p.add(leftPanel, BorderLayout.WEST);
		p.add(rightPanel, BorderLayout.EAST);

		return p;
	}

	private JPanel getImagePanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEmptyBorder());

		JPanel pTop = new JPanel(new BorderLayout(5, 5));
		pTop.setBorder(BorderFactory.createTitledBorder(null, "Local File", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		JPanel p_ = new JPanel();
		jLocalPanel = new JImagePanel();
		p_.add(jLocalPanel);
		pTop.add(p_, BorderLayout.CENTER);
		p_ = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		jLocalFile = new JTextField(60);
		p_.add(jLocalFile);
		JCustomButton btn = new JCustomButton(null, "common/folder.png", null);
		btn.setToolTipText("Browse");
		btn.setActionCommand("browse");
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.addActionListener(this);
		p_.add(btn);
		pTop.add(p_, BorderLayout.SOUTH);
		p.add(pTop, BorderLayout.NORTH);

		JPanel pMiddle = new JPanel(new GridBagLayout());
		pMiddle.setBorder(BorderFactory.createEmptyBorder());
		pMiddle.setPreferredSize(new Dimension(500, 50));
		JCustomButton downloadBtn = new JCustomButton("Download", "download.png", null);
		downloadBtn.setActionCommand("download");
		downloadBtn.addActionListener(this);
		pMiddle.add(downloadBtn);
		JCustomButton uploadBtn = new JCustomButton("Upload", "upload.png", null);
		uploadBtn.setActionCommand("upload");
		uploadBtn.addActionListener(this);
		pMiddle.add(uploadBtn);
		JCustomButton removeBtn = new JCustomButton("Remove", "remove.png", null);
		removeBtn.setActionCommand("remove");
		removeBtn.addActionListener(this);
		pMiddle.add(removeBtn);
		p.add(pMiddle, BorderLayout.CENTER);

		JPanel pBottom = new JPanel(new BorderLayout(5, 5));
		pBottom.setBorder(BorderFactory.createTitledBorder(null, "Remote File", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		p_ = new JPanel();
		jRemotePanel = new JImagePanel();
		p_.add(jRemotePanel);
		pBottom.add(p_, BorderLayout.CENTER);
		p_ = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		jRemoteFile = new JTextField(40);
		jRemoteFile.setEditable(false);
		p_.add(jRemoteFile);
		jRemoteList = new JList<>(new DefaultListModel<>());
		jRemoteList.setName("remotelist");
		jRemoteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jRemoteList.setLayoutOrientation(JList.VERTICAL);
		jRemoteList.setSelectedIndex(0);
		jRemoteList.addListSelectionListener(this);
		JScrollPane sp = new JScrollPane(jRemoteList);
		sp.setPreferredSize(new Dimension(230, 100));
		p_.add(sp);
		pBottom.add(p_, BorderLayout.SOUTH);
		p.add(pBottom, BorderLayout.SOUTH);

		return p;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			Object data = null;
			Integer sportValue = ((PicklistItem)jSportList.getSelectedItem()).getValue();
			String alias_ = alias;
			String id_ = currentId;
			if (alias.matches(Championship.alias + "|" + Event.alias) && sportValue != null && sportValue > 0) {
				alias_ = Sport.alias + alias;
				id_ = sportValue + "-" + currentId;
			}
			if (e.getActionCommand().matches("first|previous|next|last")) {
				if (!alias.equals(Team.alias))
					jSportList.setSelectedIndex(0);
				HashMap<String, Short> hLocs = new HashMap<String, Short>();
				hLocs.put("first", DatabaseManager.FIRST);
				hLocs.put("previous", DatabaseManager.PREVIOUS);
				hLocs.put("next", DatabaseManager.NEXT);
				hLocs.put("last", DatabaseManager.LAST);
				String filter = (alias.equals(Team.alias) && sportValue != null && sportValue > 0 ? "id_sport=" + sportValue : null);
				Class<? extends AbstractEntity> c = DatabaseManager.getClassFromAlias(alias);;
				data = DatabaseManager.move(c, currentId, hLocs.get(e.getActionCommand()), filter);
				if (data != null) {
					currentId = String.valueOf(c.getMethod("getId").invoke(data, new Object[0]));
					loadImage(alias, currentId);
				}
			}
			else if (e.getActionCommand().equals("find")) {
				JFindEntityDialog dlg = JMainFrame.getFindDialog();
				dlg.open(alias, null);
				if (dlg.getSelectedItem() != null) {
					Class<? extends AbstractEntity> c = DatabaseManager.getClassFromAlias(alias);
					data = DatabaseManager.loadEntity(c, dlg.getSelectedItem().getValue());
					currentId = String.valueOf(c.getMethod("getId").invoke(data, new Object[0]));
					loadImage(alias, currentId);
				}
			}
			else if (e.getActionCommand().equals("browse")) {
				if (jFileChooser.showOpenDialog(this) == 0) {
					File f = jFileChooser.getSelectedFile();
					if (f != null) {
						jLocalFile.setText(f.getPath());
						jLocalPanel.setImage(new File(f.getPath()));
					}
				}
			}
			else if (e.getActionCommand().equals("download")) {
				if (StringUtils.notEmpty(jRemoteFile.getText())) {
					if (jFileChooser.showOpenDialog(this) == 0) {
						File f = jFileChooser.getSelectedFile();
						if (f != null) {
							HttpURLConnection conn_ = (HttpURLConnection) new URL(jRemoteFile.getText()).openConnection();
							if (conn_.getResponseCode() == 200) {
								BufferedInputStream in = new BufferedInputStream(conn_.getInputStream());
								BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
								int i;
								while ((i = in.read()) != -1) {
								    out.write(i);
								}
								out.flush();
								out.close();
							}
						}
					}
				}
			}
			else if (e.getActionCommand().equals("upload")) {
				String params = "upload&entity=" + alias_ + "&size=" + (largeRadioBtn.isSelected() ? "L" : "S") + (StringUtils.notEmpty(jYear1.getText()) ? "&y1=" + jYear1.getText() + "&y2=" + jYear2.getText() : "");
				URL url = new URL(ConfigUtils.getProperty("url") + "ImageServlet?" + params);
				String bnd = "==" + System.currentTimeMillis() + "==";
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + bnd);
				OutputStream out = conn.getOutputStream();
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"), true);
				writer.append("--" + bnd).append("\r\n").append("Content-Disposition: form-data; name=\"" + alias_ + "-id\"").append("\r\n");
				writer.append("Content-Type: text/plain; charset=UTF-8").append("\r\n\r\n");
				writer.append(id_).append("\r\n").flush();
				writer.append("--" + bnd).append("\r\n").append("Content-Disposition: form-data; name=\"" + alias_ + "-file\"; filename=\"" + jLocalFile.getText() + "\"").append("\r\n");
				writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(jLocalFile.getText())).append("\r\n");
				writer.append("Content-Transfer-Encoding: binary").append("\r\n\r\n").flush();
				FileInputStream inputStream = new FileInputStream(new File(jLocalFile.getText()));
				byte[] buffer = new byte[4096];
				int bytesRead = -1;
				while ((bytesRead = inputStream.read(buffer)) != -1)
					out.write(buffer, 0, bytesRead);
				out.flush();
				inputStream.close();
				writer.append("\r\n").flush(); 
				writer.append("\r\n").flush();
				writer.append("--" + bnd + "--").append("\r\n").close();
		        int status = conn.getResponseCode();
		        if (status == 200) {
		        	jYear1.setText("");
		        	jYear2.setText("");
		        	loadImage(alias_, id_);
		        }
			}
			else if (e.getActionCommand().equals("remove")) {
				String params = "remove=1&name=" + jRemoteList.getSelectedValue();
				URL url = new URL(ConfigUtils.getProperty("url") + "ImageServlet?" + params);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        int status = conn.getResponseCode();
		        if (status == 200)
		        	loadImage(alias_, id_);
			}
			else
				loadImage(alias_, id_);
			
			if (e.getActionCommand().matches("first|previous|next|last|find") && data != null) {
				String desc = null;
				if (alias.equalsIgnoreCase(Championship.alias))
					desc = ((Championship) data).getLabel();
				else if (alias.equalsIgnoreCase(Country.alias))
					desc = ((Country) data).getLabel() + " (" + ((Country) data).getCode() + ")";
				else if (alias.equalsIgnoreCase(Event.alias))
					desc = ((Event) data).getLabel();
				else if (alias.equalsIgnoreCase(Olympics.alias))
					desc = ((Olympics) data).getYear() + " - " + ((Olympics) data).getCity().getLabel();
				else if (alias.equalsIgnoreCase(Sport.alias))
					desc = ((Sport) data).getLabel();
				else if (alias.equalsIgnoreCase(State.alias))
					desc = ((State) data).getLabel() + " (" + ((State) data).getCode() + ")";
				else if (alias.equalsIgnoreCase(Team.alias))
					desc = ((Team) data).getLabel();
				jDescription.setText("   " + desc);
			}
		}
		catch (Exception e_) {
			log.log(Level.WARNING, e_.getMessage(), e_);
		}
	}

	private void loadImage(String alias, String currentId) {
//		try {
//			jRemoteFile.setText("");
//			URL url_ = new URL(ConfigUtils.getProperty("url") + "ImageServlet?list=1&type=" + ImageUtils.getIndex(alias) + "&id=" + currentId + "&size=" + (largeRadioBtn.isSelected() ? "L" : "S"));
//			HttpURLConnection conn_ = (HttpURLConnection) url_.openConnection();
//			if (conn_.getResponseCode() == 200) {
//				PlainTextInputStream pis_ = (PlainTextInputStream) conn_.getContent();
//				DataInputStream dis_ = new DataInputStream(pis_);
//				String s_ = dis_.readLine();
//				DefaultListModel model = (DefaultListModel) jRemoteList.getModel();
//				model.clear();
//				if (StringUtils.notEmpty(s_)) {
//					for (String s__ : s_.split(","))
//						if (StringUtils.notEmpty(s__))
//							model.addElement(s__);
//					if (model.getSize() > 0)
//						jRemoteList.setSelectedIndex(0);
//				}
//			}
//		}
//		catch (IOException e) {
//			log.log(Level.WARNING, e.getMessage());
//		}
	}

	public void valueChanged(ListSelectionEvent e) {
		int index = 0;
		if (e != null && e.getSource() instanceof JList) {
			index = ((JList<?>)e.getSource()).getSelectedIndex();
			boolean isMainList = ((JList<?>)e.getSource()).getName().equalsIgnoreCase("mainlist");
			if (isMainList) {
				switch (index) {
					case 0: alias = Championship.alias; break;
					case 1: alias = Country.alias; break;
					case 2: alias = Event.alias; break;
					case 3: alias = Olympics.alias; break;
					case 4: alias = Sport.alias; break;
					case 5: alias = State.alias; break;
					case 6: alias = Team.alias; break;
				}
				jSportList.setVisible(alias.matches(Championship.alias + "|" + Event.alias + "|" + Team.alias));
				actionPerformed(new ActionEvent(this, 0, "last"));
			}
			else {
				try {
					String value = String.valueOf(((JList<?>)e.getSource()).getSelectedValue());
					if (StringUtils.notEmpty(value) && !value.equals("null")) {
						jRemoteFile.setText(ConfigUtils.getProperty("img.url") + value);
						jRemotePanel.setImage(new URL(jRemoteFile.getText()));						
					}
					else
						throw new MalformedURLException();
				}
				catch (MalformedURLException e_) {
					jRemoteFile.setText("");
					jRemotePanel.setImage(null);
				}
			}
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (alias.matches(Championship.alias + "|" + Event.alias)) {
			Integer sportValue = ((PicklistItem)jSportList.getSelectedItem()).getValue();
			if (sportValue != null && sportValue > 0) {
				String alias_ = Sport.alias + alias;
				String id_ = sportValue + "-" + currentId;
				loadImage(alias_, id_);	
			}
		}
	}

}