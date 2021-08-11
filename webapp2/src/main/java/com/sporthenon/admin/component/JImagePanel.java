package com.sporthenon.admin.component;

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
		setPreferredSize(new Dimension(240, 130));
		setBorder(BorderFactory.createBevelBorder(1));
		setBackground(Color.WHITE);
		label = new JLabel();
		add(label);
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
				label.setIcon(new ImageIcon(img));
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
	
	public void setAlphaTest(boolean enabled) {
		this.setBackground(enabled ? Color.LIGHT_GRAY : Color.WHITE);
	}

}