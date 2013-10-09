package com.sporthenon.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class JCustomTab extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextPane textPane;
	
	public JCustomTab() {
		super();
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		initTextPane();
	}
	
	public void initTextPane() {
		try {
			HTMLEditorKit kit = new HTMLEditorKit();
			Document doc = kit.createDefaultDocument();
			StyleSheet styleSheet = kit.getStyleSheet();
			styleSheet.importStyleSheet(Utils.getFileURL("css/default.css"));
			
			textPane = new JTextPane();			
			textPane.setEditable(false);
			textPane.setEditorKit(kit);
			textPane.setDocument(doc);
		    textPane.setBackground(new Color(240, 240, 240));
			this.add(textPane);
			
			StringBuffer html = new StringBuffer();
			html.append("<div class='content'>");
			html.append("<table class='loading'><tr><td>");
			html.append("<img src='" + Utils.getFileURL("img/loading.gif") + "'>");
			html.append("<br>Loading...</td></tr></table>");
			html.append("</div>");
			setHtml(html.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setHtml(Object obj) throws IOException {
		if (obj != null) {
			if (obj instanceof String)
				textPane.setText(String.valueOf(obj));
			else if (obj instanceof URL)
				textPane.setPage((URL) obj);
		}
	}

	public JTextPane getTextPane() {
		return textPane;
	}

	public void setTextPane(JTextPane textPane) {
		this.textPane = textPane;
	}

}
