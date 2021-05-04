package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.db.entity.meta.Picture;
import com.sporthenon.utils.ImageUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;

public class JEditPhotosDialog extends JDialog implements ActionListener, KeyListener {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JEditPhotosDialog.class.getName());
	
	JEditResultDialog parent = null;
	private JFileChooser jFileChooser;
	private JTextField jFile;
	private JTextArea jEmbeddedHtml;
	private JTextField jSource;
	private JCustomButton jAddButton;
	private JPanel jPhotos;
	private List<Picture> pictures = new ArrayList<>();
	private int nbPhotos = 0;
	private Set<Integer> photosDeleted = new HashSet<>();
	
	public JEditPhotosDialog(JEditResultDialog owner) {
		super(owner);
		parent = (JEditResultDialog) this.getOwner();
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

		JDialogButtonBar jButtonBar = new JDialogButtonBar(this);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(new BorderLayout());
		
		jContentPane.add(getAddPhotoPanel(), BorderLayout.NORTH);
		jContentPane.add(getPhotosPanel(), BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
	}

	private JPanel getAddPhotoPanel() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.setBorder(BorderFactory.createTitledBorder(null, "Add Photo", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		p.setPreferredSize(new Dimension(0, 180));
		JLabel label = new JLabel("File:");
		label.setPreferredSize(new Dimension(90, 21));;
		p.add(label);
		jFile = new JTextField(50);
		jFile.addKeyListener(this);
		p.add(jFile);
		JCustomButton btn = new JCustomButton(null, "folderimg.png", null);
		btn.setToolTipText("Browse");
		btn.setActionCommand("browse");
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.addActionListener(this);
		p.add(btn);
		JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
		sep.setPreferredSize(new Dimension(50, 0));
		p.add(sep);
		label = new JLabel("Embedded HTML:");
		label.setPreferredSize(new Dimension(90, 21));
		p.add(label);
		jEmbeddedHtml = new JTextArea(4, 45);
		jEmbeddedHtml.setFont(SwingUtils.getDefaultFont());
		jEmbeddedHtml.addKeyListener(this);
		p.add(new JScrollPane(jEmbeddedHtml));
		sep = new JSeparator(JSeparator.HORIZONTAL);
		sep.setPreferredSize(new Dimension(50, 0));
		p.add(sep);
		label = new JLabel("Source:");
		label.setPreferredSize(new Dimension(90, 21));
		p.add(label);
		jSource = new JTextField(50);
		p.add(jSource);
		sep = new JSeparator(JSeparator.HORIZONTAL);
		sep.setPreferredSize(new Dimension(50, 0));
		p.add(sep);
		jAddButton = new JCustomButton("Add photo", "upload.png", null);
		jAddButton.setPreferredSize(new Dimension(90, 24));
		jAddButton.setActionCommand("add");
		jAddButton.addActionListener(this);
		p.add(jAddButton);
		return p;
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
	
	private void addPhoto(Picture pic, boolean isURL) {
		try {
			JLabel photoLabel = new JLabel();
			if (!pic.isEmbedded()) {
				BufferedImage img = null;
				if (isURL) {
					img = ImageIO.read(new URL(ImageUtils.getUrl() + pic.getValue()));
				}
				else {
					img = ImageIO.read(new FileInputStream(pic.getValue()));
				}
				photoLabel.setPreferredSize(new Dimension(150, 150));
				photoLabel.setIcon(SwingUtils.resizeIcon(new ImageIcon(img), 150, 150));
			}
			else {
				photoLabel.setText("[Embedded HTML]");
			}
			jPhotos.add(photoLabel);
			JCustomButton delBtn = new JCustomButton(null, "remove.png", "Remove Photo #" + (nbPhotos + 1));
			delBtn.addActionListener(this);
			delBtn.setActionCommand("remove-" + nbPhotos);
			jPhotos.add(delBtn);
			JSeparator sep = new JSeparator();
			sep.setPreferredSize(new Dimension(10, 0));
			jPhotos.add(sep);
			nbPhotos++;
		}
		catch (Exception e_) {
			log.log(Level.WARNING, e_.getMessage(), e_);
		}
	}
	
	public void open() {
		this.setTitle("Edit Photos");
		jFile.setText("");
		jEmbeddedHtml.setText("");
		jSource.setText("");
		jPhotos.removeAll();
		jAddButton.setEnabled(false);
		nbPhotos = 0;
		photosDeleted.clear();
		for (Picture pic : getPictures()) {
			addPhoto(pic, true);
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
			addPhoto(pic, false);
			pictures.add(pic);
			jFile.setText("");
			jEmbeddedHtml.setText("");
			jSource.setText("");
			jAddButton.setEnabled(false);
		}
		else if (e.getActionCommand().matches("remove-\\d+")) {
			int index = Integer.valueOf(e.getActionCommand().replace("remove-", ""));
			jPhotos.remove((index * 2) + 1);
			jPhotos.remove(index * 2);
			jPhotos.revalidate();
			jPhotos.repaint();
			Picture pic = pictures.get(index);
			if (pic.getId() != null) {
				photosDeleted.add(pic.getId());
			}
			nbPhotos--;
		}
		else if (e.getActionCommand().equals("ok")) {
			parent.setPhotosAdded(pictures);
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

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

}