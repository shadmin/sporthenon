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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.IOUtils;

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
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;

public class JPicturesPanel extends JSplitPane implements ActionListener, ListSelectionListener, ItemListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JPicturesPanel.class.getName());
	
	private JList<String> jList = null;
	private String alias = Championship.alias;
	private String currentId;
	JCustomButton downloadBtn = null;
	JCustomButton uploadBtn = null;
	JCustomButton removeBtn = null;
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
	private JCheckBox jTestAlpha1 = null;
	private JCheckBox jTestAlpha2 = null;
	
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
		jFileChooser.setDialogTitle("Select Picture File");
		jFileChooser.setFileFilter(new FileNameExtensionFilter("PNG files (*.png)", "png"));
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
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 1));
		jYear1 = new JTextField();
		jYear1.setPreferredSize(new Dimension(50, 22));
		leftPanel.add(new JLabel("From:"));
		leftPanel.add(jYear1);
		jYear2 = new JTextField();
		jYear2.setPreferredSize(new Dimension(50, 22));
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
		jTestAlpha1 = new JCheckBox("Test transparency");
		jTestAlpha1.addActionListener(this);
		jTestAlpha1.setActionCommand("testalpha1");
		p_.add(jTestAlpha1);
		pTop.add(p_, BorderLayout.CENTER);
		p_ = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		jLocalFile = new JTextField(60);
		p_.add(jLocalFile);
		JCustomButton btn = new JCustomButton(null, "folderimg.png", null);
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
		downloadBtn = new JCustomButton("Download", "download.png", null);
		downloadBtn.setEnabled(false);
		downloadBtn.setActionCommand("download");
		downloadBtn.addActionListener(this);
		pMiddle.add(downloadBtn);
		uploadBtn = new JCustomButton("Upload", "upload.png", null);
		uploadBtn.setEnabled(false);
		uploadBtn.setActionCommand("upload");
		uploadBtn.addActionListener(this);
		pMiddle.add(uploadBtn);
		removeBtn = new JCustomButton("Remove", "remove.png", null);
		removeBtn.setEnabled(false);
		removeBtn.setActionCommand("remove");
		removeBtn.addActionListener(this);
		pMiddle.add(removeBtn);
		p.add(pMiddle, BorderLayout.CENTER);

		JPanel pBottom = new JPanel(new BorderLayout(5, 5));
		pBottom.setBorder(BorderFactory.createTitledBorder(null, "Remote File", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		p_ = new JPanel();
		jRemotePanel = new JImagePanel();
		p_.add(jRemotePanel);
		jTestAlpha2 = new JCheckBox("Test transparency");
		jTestAlpha2.addActionListener(this);
		jTestAlpha2.setActionCommand("testalpha2");
		p_.add(jTestAlpha2);
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
				if (!alias.equals(Team.alias)) {
					jSportList.setSelectedIndex(0);
				}
				Map<String, Short> hLocs = new HashMap<String, Short>();
				hLocs.put("first", DatabaseManager.FIRST);
				hLocs.put("previous", DatabaseManager.PREVIOUS);
				hLocs.put("next", DatabaseManager.NEXT);
				hLocs.put("last", DatabaseManager.LAST);
				String filter = (alias.equals(Team.alias) && sportValue != null && sportValue > 0 ? "id_sport=" + sportValue : null);
				Class<? extends AbstractEntity> c = DatabaseManager.getClassFromAlias(alias);;
				data = DatabaseManager.move(c, currentId, hLocs.get(e.getActionCommand()), filter);
				if (data != null) {
					currentId = String.valueOf(c.getMethod("getId").invoke(data, new Object[0]));
					loadImageList(alias, currentId);
				}
			}
			else if (e.getActionCommand().equals("find")) {
				JFindEntityDialog dlg = JMainFrame.getFindDialog();
				dlg.open(alias, null);
				if (dlg.getSelectedItem() != null) {
					Class<? extends AbstractEntity> c = DatabaseManager.getClassFromAlias(alias);
					data = DatabaseManager.loadEntity(c, dlg.getSelectedItem().getValue());
					currentId = String.valueOf(c.getMethod("getId").invoke(data, new Object[0]));
					loadImageList(alias, currentId);
				}
			}
			else if (e.getActionCommand().equals("browse")) {
				if (jFileChooser.showOpenDialog(this) == 0) {
					File f = jFileChooser.getSelectedFile();
					if (f != null) {
						jLocalFile.setText(f.getPath());
						jLocalPanel.setImage(new File(f.getPath()));
						uploadBtn.setEnabled(true);
					}
				}
			}
			else if (e.getActionCommand().equals("download")) {
				downloadImage();
			}
			else if (e.getActionCommand().equals("upload")) {
				uploadImage(alias_, id_);
			}
			else if (e.getActionCommand().equals("remove")) {
				Object[] options = {"Yes", "No"};
				int n = JOptionPane.showOptionDialog(this, "Remove image " + jRemoteList.getSelectedValue() + "?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (n != 0) {
					return;
				}
				ImageUtils.removeImage(jRemoteList.getSelectedValue(), JMainFrame.getOptionsDialog().getCredentialsFile().getText());
				loadImageList(alias_, id_);
			}
			else if (e.getActionCommand().equals("testalpha1")) {
				jLocalPanel.setAlphaTest(jTestAlpha1.isSelected());
			}
			else if (e.getActionCommand().equals("testalpha2")) {
				jRemotePanel.setAlphaTest(jTestAlpha2.isSelected());
			}
			else {
				loadImageList(alias_, id_);
			}
			
			if (e.getActionCommand().matches("first|previous|next|last|find") && data != null) {
				String desc = null;
				if (alias.equalsIgnoreCase(Championship.alias)) {
					desc = ((Championship) data).getLabel();
				}
				else if (alias.equalsIgnoreCase(Country.alias)) {
					desc = ((Country) data).getLabel() + " (" + ((Country) data).getCode() + ")";
				}
				else if (alias.equalsIgnoreCase(Event.alias)) {
					desc = ((Event) data).getLabel();
				}
				else if (alias.equalsIgnoreCase(Olympics.alias)) {
					desc = ((Olympics) data).getYear() + " - " + ((Olympics) data).getCity().getLabel();
				}
				else if (alias.equalsIgnoreCase(Sport.alias)) {
					desc = ((Sport) data).getLabel();
				}
				else if (alias.equalsIgnoreCase(State.alias)) {
					desc = ((State) data).getLabel() + " (" + ((State) data).getCode() + ")";
				}
				else if (alias.equalsIgnoreCase(Team.alias)) {
					desc = ((Team) data).getLabel();
				}
				jDescription.setText("   " + desc);
			}
		}
		catch (Exception e_) {
			log.log(Level.WARNING, e_.getMessage(), e_);
		}
	}
	
	private void downloadImage() throws Exception {
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

	private void uploadImage(String alias_, String id_) throws Exception {
		short idx = ImageUtils.getIndex(alias_);
		char size = (largeRadioBtn.isSelected() ? ImageUtils.SIZE_LARGE : ImageUtils.SIZE_SMALL);
		String y1 = jYear1.getText();
		String y2 = jYear2.getText();
		String ext = ".png";
		
		// Set file name
		String key = idx + "-" + id_ + "-" + size;
		String fileName = key + (StringUtils.notEmpty(y1) ? "_" + y1 + "-" + y2 : "");
		int index = -1;
		Collection<String> lExisting = ImageUtils.getImages(idx, id_, size);
		for (String s : lExisting) {
			if (s.indexOf(fileName) == 0) {
				index = 0;
				if (s.matches(".*\\_\\d+\\.png$")) {
					index = Integer.parseInt(s.replaceAll(".*\\_|\\.png$", ""));
				}
				index++;
				break;
			}
		}
		fileName = fileName + (index > -1 ? "_" + index : "") + ext;
		
		// Upload to bucket
		try(FileInputStream inputStream = new FileInputStream(new File(jLocalFile.getText()))) {
			byte[] content = IOUtils.toByteArray(inputStream);
			ImageUtils.uploadImage(key, fileName, content, JMainFrame.getOptionsDialog().getCredentialsFile().getText());
		}

		// Refresh UI
		jYear1.setText("");
		jYear2.setText("");
		loadImageList(alias_, id_);
	}

	private void loadImageList(String alias, String currentId) {
		jRemoteFile.setText("");
		DefaultListModel<String> model = (DefaultListModel<String>) jRemoteList.getModel();
		model.clear();
		for (String s : ImageUtils.getImages(ImageUtils.getIndex(alias), currentId, (largeRadioBtn.isSelected() ? ImageUtils.SIZE_LARGE : ImageUtils.SIZE_SMALL))) {
			model.addElement(s);
		}
		if (model.getSize() > 0) {
			jRemoteList.setSelectedIndex(0);
		}
		downloadBtn.setEnabled(model.getSize() > 0);
		removeBtn.setEnabled(model.getSize() > 0);
	}
	
	public void changeEntity(int index) {
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
		actionPerformed(new ActionEvent(this, 0, "first"));
	}

	public void valueChanged(ListSelectionEvent e) {
		int index = 0;
		if (e != null && e.getSource() instanceof JList) {
			index = ((JList<?>)e.getSource()).getSelectedIndex();
			boolean isMainList = ((JList<?>)e.getSource()).getName().equalsIgnoreCase("mainlist");
			if (isMainList) {
				changeEntity(index);
			}
			else {
				try {
					String value = String.valueOf(((JList<?>)e.getSource()).getSelectedValue());
					if (StringUtils.notEmpty(value) && !value.equals("null")) {
						jRemoteFile.setText(ImageUtils.getUrl() + value);
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
		if (alias.matches(Championship.alias + "|" + Event.alias) && jSportList.getSelectedItem() != null) {
			Integer sportValue = ((PicklistItem)jSportList.getSelectedItem()).getValue();
			if (sportValue != null && sportValue > 0) {
				String alias_ = Sport.alias + alias;
				String id_ = sportValue + "-" + currentId;
				loadImageList(alias_, id_);	
			}
		}
	}

}