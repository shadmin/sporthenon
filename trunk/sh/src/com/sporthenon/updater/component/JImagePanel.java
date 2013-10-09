package com.sporthenon.updater.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel label = null;

	public JImagePanel() {
		super();
		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(145, 125));
		setBorder(BorderFactory.createBevelBorder(1));
		setBackground(Color.white);
		label = new JLabel();
		add(label);
	}

	public void setImage(Object o) {
		try {
			BufferedImage img = null;
			if (o instanceof URL)
				img = ImageIO.read((URL) o);
			else
				img = ImageIO.read((File) o);
			if (img != null)
				label.setIcon(new ImageIcon(img));
		}
		catch (Exception e) {
			//Logger.getLogger("sh").error(e.getMessage());
			try {
				label.setIcon(new ImageIcon(ImageIO.read(JImagePanel.class.getResourceAsStream("/com/sporthenon/utils/res/img/updater/noimage.png"))));
			}
			catch (Exception e_) {}
		}
	}

}