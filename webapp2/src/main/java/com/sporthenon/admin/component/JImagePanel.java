package com.sporthenon.admin.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel label = null;
	private JTextField text = null;
	private int imageWidth;
	private int imageHeight;

	public JImagePanel() {
		super();
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(240, 140));
		setBackground(Color.WHITE);
		label = new JLabel();
		label.setBorder(BorderFactory.createBevelBorder(1));
		label.setHorizontalAlignment(JLabel.CENTER);
		add(label, BorderLayout.CENTER);
		text = new JTextField("-");
		text.setEditable(false);
		text.setHorizontalAlignment(JTextField.CENTER);
		add(text, BorderLayout.SOUTH);
	}

	public void setImage(Object o) {
		try {
			BufferedImage img = null;
			if (o instanceof URL) {
				img = ImageIO.read((URL) o);
			}
			else {
				img = ImageIO.read((File) o);
			}
			if (img != null) {
				ImageIcon icon = new ImageIcon(img);
				label.setIcon(icon);
				imageWidth = img.getWidth();
				imageHeight = img.getHeight();
			}
		}
		catch (Exception e) {
			//Logger.getLogger("sh").error(e.getMessage());
			try {
				label.setIcon(new ImageIcon(ImageIO.read(JImagePanel.class.getResourceAsStream("/com/sporthenon/utils/res/img/noimage.png"))));
			}
			catch (Exception e_) {}
		}
	}
	
	public String getText() {
		return text.getText();
	}

	public void setText(String s) {
		text.setText(s);
	}
	
	public void setAlphaTest(boolean enabled) {
		this.setBackground(enabled ? Color.LIGHT_GRAY : Color.WHITE);
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

}