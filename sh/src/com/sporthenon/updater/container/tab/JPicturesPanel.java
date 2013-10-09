package com.sporthenon.updater.container.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
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

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.updater.component.JCustomButton;
import com.sporthenon.updater.component.JImagePanel;
import com.sporthenon.updater.window.JFindEntityDialog;
import com.sporthenon.updater.window.JMainFrame;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.SwingUtils;

public class JPicturesPanel extends JSplitPane implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;

	private JList jList = null;
	private String alias = Championship.alias;
	private String currentId;
	private JRadioButton largeRadioBtn = null;
	private JRadioButton smallRadioBtn = null;
	private JImagePanel jLocalPanel = null;
	private JImagePanel jRemotePanel = null;
	private JTextField jLocalFile = null;
	private JTextField jRemoteFile = null;
	private JFileChooser jFileChooser = null;
	private JLabel jDescription = null;

	public JPicturesPanel(JMainFrame parent) {
		initialize();
	}

	public JList getList() {
		return jList;
	}

	private void initialize() {
		initList();
		jFileChooser = new JFileChooser();
		JScrollPane leftPanel = new JScrollPane(jList);
		leftPanel.setPreferredSize(new Dimension(150, 0));
		leftPanel.setBorder(BorderFactory.createEmptyBorder());
		leftPanel.setMinimumSize(new Dimension(0, 0));
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 5));
		rightPanel.setLayout(new BorderLayout(50, 50));
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
		jList = new JList(v);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setSelectedIndex(0);
		jList.addListSelectionListener(this);
	}

	private JPanel getButtonPanel() {
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 8));
		largeRadioBtn = new JRadioButton("Large Size");
		largeRadioBtn.setMargin(new Insets(0, 0, 0, 0));
		largeRadioBtn.addActionListener(this);
		largeRadioBtn.setSelected(true);
		leftPanel.add(largeRadioBtn);
		smallRadioBtn = new JRadioButton("Small Size");
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
		JCustomButton jFirstButton = new JCustomButton("First", "updater/first.png");
		jFirstButton.addActionListener(this);
		jFirstButton.setActionCommand("first");
		JCustomButton jPreviousButton = new JCustomButton("Previous", "updater/previous.png");
		jPreviousButton.addActionListener(this);
		jPreviousButton.setActionCommand("previous");
		JCustomButton jFindButton = new JCustomButton("Find", "updater/find.png");
		jFindButton.addActionListener(this);
		jFindButton.setActionCommand("find");
		JCustomButton jNextButton = new JCustomButton("Next", "updater/next.png");
		jNextButton.addActionListener(this);
		jNextButton.setActionCommand("next");
		JCustomButton jLastButton = new JCustomButton("Last", "updater/last.png");
		jLastButton.addActionListener(this);
		jLastButton.setActionCommand("last");
		rightPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
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
		JCustomButton btn = new JCustomButton(null, "common/folder.png");
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
		JCustomButton uploadBtn = new JCustomButton("Upload", "updater/upload.png");
		uploadBtn.setActionCommand("upload");
		uploadBtn.addActionListener(this);
		pMiddle.add(uploadBtn);
		p.add(pMiddle, BorderLayout.CENTER);

		JPanel pBottom = new JPanel(new BorderLayout(5, 5));
		pBottom.setBorder(BorderFactory.createTitledBorder(null, "Remote File", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		p_ = new JPanel();
		jRemotePanel = new JImagePanel();
		p_.add(jRemotePanel);
		pBottom.add(p_, BorderLayout.CENTER);
		p_ = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		jRemoteFile = new JTextField(30);
		jRemoteFile.setEditable(false);
		p_.add(jRemoteFile);
		pBottom.add(p_, BorderLayout.SOUTH);
		p.add(pBottom, BorderLayout.SOUTH);

		return p;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			Object data = null;
			if (e.getActionCommand().matches("first|previous|next|last")) {
				HashMap<String, Short> hLocs = new HashMap<String, Short>();
				hLocs.put("first", DatabaseHelper.FIRST);
				hLocs.put("previous", DatabaseHelper.PREVIOUS);
				hLocs.put("next", DatabaseHelper.NEXT);
				hLocs.put("last", DatabaseHelper.LAST);
				Class c = JDataPanel.getClassFromAlias(alias);
				data = DatabaseHelper.move(c, currentId, hLocs.get(e.getActionCommand()));
				if (data != null) {
					currentId = String.valueOf(c.getMethod("getId").invoke(data, new Object[0]));
					loadImage(alias, currentId);
				}

			}
			else if (e.getActionCommand().equals("find")) {
				JFindEntityDialog dlg = JMainFrame.getFindDialog();
				dlg.open(alias, null);
				if (dlg.getSelectedItem() != null) {
					Class c = JDataPanel.getClassFromAlias(alias);
					data = DatabaseHelper.loadEntity(c, dlg.getSelectedItem().getValue());
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
			else if (e.getActionCommand().equals("upload")) {
				String params = "upload&entity=" + alias + "&size=" + (largeRadioBtn.isSelected() ? "L" : "S");
				URL url = new URL(ConfigUtils.getProperty("url") + "ImageServlet?" + params);
				String bnd = "==" + System.currentTimeMillis() + "==";
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + bnd);
				OutputStream out = conn.getOutputStream();
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"), true);
				writer.append("--" + bnd).append("\r\n").append("Content-Disposition: form-data; name=\"" + alias + "-id\"").append("\r\n");
				writer.append("Content-Type: text/plain; charset=UTF-8").append("\r\n\r\n");
				writer.append(currentId).append("\r\n").flush();
				writer.append("--" + bnd).append("\r\n").append("Content-Disposition: form-data; name=\"" + alias + "-file\"; filename=\"" + jLocalFile.getText() + "\"").append("\r\n");
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
		        if (status == 200)
		        	loadImage(alias, currentId);
			}
			else
				loadImage(alias, currentId);
			
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
			Logger.getLogger("sh").error(e_.getMessage(), e_);
		}
	}

	private void loadImage(String alias, String currentId) throws MalformedURLException {
		String ext = (alias.matches("CN|ST") ? ".gif" : ".png");
		String url = ImageUtils.getUrl() + ImageUtils.getIndex(alias) + "-" + currentId + "-" + (largeRadioBtn.isSelected() ? "L" : "S") + ext + "?" + System.currentTimeMillis();
		jRemoteFile.setText(url);
		jRemotePanel.setImage(new URL(url));
	}

	public void valueChanged(ListSelectionEvent e) {
		int index = 0;
		if (e != null && e.getSource() instanceof JList)
			index = ((JList)e.getSource()).getSelectedIndex();
		switch (index) {
		case 0: alias = Championship.alias; break;
		case 1: alias = Country.alias; break;
		case 2: alias = Event.alias; break;
		case 3: alias = Olympics.alias; break;
		case 4: alias = Sport.alias; break;
		case 5: alias = State.alias; break;
		case 6: alias = Team.alias; break;
		}
		actionPerformed(new ActionEvent(this, 0, "last"));
	}

}