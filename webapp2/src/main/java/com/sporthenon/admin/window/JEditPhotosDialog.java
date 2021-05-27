package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.IOUtils;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.container.tab.JDataPanel;
import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.meta.Picture;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;

public class JEditPhotosDialog extends JDialog implements ActionListener, KeyListener {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JEditPhotosDialog.class.getName());
	
	Container parent = null;
	private JFileChooser jFileChooser;
	private JTextField jFile;
	private JTextArea jEmbeddedHtml;
	private JTextField jSource;
	private JCustomButton jAddButton;
	private JPanel jPhotos;
	private JLabel jEmptyLabel;
	private Map<Integer, Picture> photos = new HashMap<>();
	private List<Picture> photosDeleted = new ArrayList<>();
	private int index;
	private String alias;
	private Integer id;
	
	public JEditPhotosDialog(Container owner) {
		super();
		this.parent = owner;
		initialize();
	}

	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(700, 455));
		this.setSize(this.getPreferredSize());
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setContentPane(jContentPane);
		
		jEmptyLabel = new JLabel("(empty)");
		jEmptyLabel.setPreferredSize(new Dimension(155, 155));
		jEmptyLabel.setHorizontalAlignment(JLabel.CENTER);
		jEmptyLabel.setVerticalAlignment(JLabel.CENTER);
		
		JDialogButtonBar jButtonBar = new JDialogButtonBar(this);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(new BorderLayout());
		
		jContentPane.add(getAddPhotoPanel(), BorderLayout.NORTH);
		jContentPane.add(getPhotosPanel(), BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}

	private JPanel getAddPhotoPanel() {
		JPanel photoPanel = new JPanel();
		photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.Y_AXIS));
		photoPanel.setBorder(BorderFactory.createTitledBorder(null, "Add Photo", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 4));
		JLabel label = new JLabel("File:");
		label.setPreferredSize(new Dimension(90, 21));;
		panel.add(label);
		jFile = new JTextField(55);
		jFile.addKeyListener(this);
		panel.add(jFile);
		JCustomButton btn = new JCustomButton(null, "folderimg.png", null);
		btn.setToolTipText("Browse");
		btn.setActionCommand("browse");
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.addActionListener(this);
		panel.add(btn);
		photoPanel.add(panel);
		
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 4));
		label = new JLabel("Embedded HTML:");
		label.setPreferredSize(new Dimension(90, 21));
		panel.add(label);
		jEmbeddedHtml = new JTextArea(4, 45);
		jEmbeddedHtml.setFont(SwingUtils.getDefaultFont());
		jEmbeddedHtml.addKeyListener(this);
		panel.add(new JScrollPane(jEmbeddedHtml));
		photoPanel.add(panel);
		
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 4));
		label = new JLabel("Source:");
		label.setPreferredSize(new Dimension(90, 21));
		panel.add(label);
		jSource = new JTextField(55);
		panel.add(jSource);
		photoPanel.add(panel);
		
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 4));
		jAddButton = new JCustomButton("Add photo", "upload.png", null);
		jAddButton.setPreferredSize(new Dimension(90, 24));
		jAddButton.setActionCommand("add");
		jAddButton.addActionListener(this);
		panel.add(jAddButton);
		photoPanel.add(panel);
		
		return photoPanel;
	}

	private JPanel getPhotosPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setBorder(BorderFactory.createTitledBorder(null, "Current Photos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jPhotos = new JPanel();
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setBorder(BorderFactory.createEmptyBorder());
		jScrollPane.setViewportView(jPhotos);
		p.add(jScrollPane);
		return p;
	}
	
	private void enableAddButton() {
		jAddButton.setEnabled((StringUtils.notEmpty(jFile.getText()) && jFile.getText().matches(".*\\.(png|jpg|gif)$")) || StringUtils.notEmpty(jEmbeddedHtml.getText()));
	}
	
	private void addPhoto(Picture pic) {
		Integer key = (pic.getId() != null ? pic.getId() : index);
		JLabel photoLabel = new JLabel();
		try {
			if (!pic.isEmbedded()) {
				BufferedImage img = null;
				if (pic.getId() != null) {
					img = ImageIO.read(new URL(ImageUtils.getUrl() + pic.getValue()));
				}
				else {
					img = ImageIO.read(new FileInputStream(pic.getValue()));
				}
				photoLabel.setPreferredSize(new Dimension(155, 155));
				photoLabel.setIcon(SwingUtils.resizeIcon(new ImageIcon(img), 155, 155));
			}
			else {
				photoLabel.setText("[Embedded HTML]");
			}
		}
		catch (Exception e_) {
			log.log(Level.WARNING, e_.getMessage(), e_);
			photoLabel.setText("Picture with URL Error");
		}
		finally {
			photoLabel.setToolTipText(pic.getSource());
			jPhotos.add(photoLabel);
			JCustomButton delBtn = new JCustomButton(null, "remove.png", "Remove");
			delBtn.addActionListener(this);
			delBtn.setActionCommand("remove" + key);
			jPhotos.remove(jEmptyLabel);
			jPhotos.add(delBtn);
			jPhotos.repaint();
			photos.put(key, pic);
			index--;
		}
	}
	
	public void open() {
		this.setTitle("Edit Photos");
		jFile.setText("");
		jEmbeddedHtml.setText("");
		jSource.setText("");
		jPhotos.removeAll();
		jPhotos.add(jEmptyLabel);
		jAddButton.setEnabled(false);
		photosDeleted.clear();
		index = 0;
		for (Picture pic : photos.values()) {
			addPhoto(pic);
		}
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("browse")) {
			jFileChooser = new JFileChooser();
			jFileChooser.setDialogTitle("Select Photo File");
			jFileChooser.setFileFilter(new FileNameExtensionFilter("Picture files (*.png, *.jpg, *.gif)", "jpg", "png", "gif"));
			if (jFileChooser.showOpenDialog(this) == 0) {
				File f = jFileChooser.getSelectedFile();
				if (f != null) {
					jFile.setText(f.getPath());
					enableAddButton();
				}
			}
		}
		else if (e.getActionCommand().equals("add")) {
			Picture pic = new Picture();
			if (StringUtils.notEmpty(jFile.getText())) {
				pic.setValue(jFile.getText());
				pic.setEmbedded(false);
			}
			else {
				pic.setValue(jEmbeddedHtml.getText());
				pic.setEmbedded(true);
			}
			if (StringUtils.notEmpty(jSource.getText())) {
				pic.setSource(jSource.getText());
			}
			addPhoto(pic);
			jFile.setText("");
			jEmbeddedHtml.setText("");
			jSource.setText("");
			jAddButton.setEnabled(false);
		}
		else if (e.getActionCommand().startsWith("remove")) {
			Integer key = Integer.valueOf(e.getActionCommand().replace("remove", ""));
			if (key > 0) {
				photosDeleted.add(photos.get(key));
			}
			photos.remove(key);
			boolean found = false;
			for (int i = jPhotos.getComponentCount() - 1 ; i >= 0 ; i--) {
				Component comp = jPhotos.getComponent(i);
				if (comp.equals(e.getSource())) {
					jPhotos.remove(comp);
					found = true;
				}
				else if (found && comp instanceof JLabel) {
					jPhotos.remove(comp);
					break;
				}
			}
			jPhotos.revalidate();
			jPhotos.repaint();
			if (jPhotos.getComponentCount() == 0) {
				jPhotos.add(jEmptyLabel);
			}
		}
		else if (e.getActionCommand().equals("ok")) {
			try {
				int n = photos.size();
				for (Picture pic : photos.values()) {
					if (pic.getId() != null) {
						continue;
					}
					pic.setEntity(this.alias);
					pic.setIdItem(this.id);
					
					if (!pic.isEmbedded()) {
						try(FileInputStream inputStream = new FileInputStream(new File(pic.getValue()))) {
							String ext = "." + pic.getValue().substring(pic.getValue().lastIndexOf(".") + 1).toLowerCase();
							String fileName = "P" + StringUtils.encode(pic.getEntity() + "-" + this.id);
							if (n > 1) {
								fileName += n;
							}
							fileName += ext;
							byte[] content = IOUtils.toByteArray(inputStream);
							ImageUtils.uploadImage(null, fileName, content, JMainFrame.getOptionsDialog().getCredentialsFile().getText());
							pic.setValue(fileName);
						}
						n++;
					}
					
					DatabaseManager.saveEntity(pic, null);
				}
				for (Picture pic : photosDeleted) {
					if (!pic.isEmbedded()) {
						ImageUtils.removeImage(pic.getValue(), JMainFrame.getOptionsDialog().getCredentialsFile().getText());
					}
					DatabaseManager.removeEntity(pic);
					photos.remove(pic.getId());
				}
				if (parent instanceof JEditResultDialog) {
					((JEditResultDialog)parent).getPhotos().clear();
					((JEditResultDialog)parent).getPhotos().addAll(photos.values());
				}
				else {
					((JDataPanel)parent).getPhotos().clear();
					((JDataPanel)parent).getPhotos().addAll(photos.values());
				}
			}
			catch (Exception e_) {
				log.log(Level.WARNING, e_.getMessage(), e_);
			}
		}
		this.setVisible(!e.getActionCommand().matches("ok|cancel"));
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		enableAddButton();
	}

	public Map<Integer, Picture> getPhotos() {
		return photos;
	}

	public void setPhotos(Map<Integer, Picture> photos) {
		this.photos = photos;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}