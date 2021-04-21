package com.sporthenon.admin.container.entity;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sporthenon.utils.SwingUtils;

public class JConfigPanel extends JAbstractEntityPanel {

private static final long serialVersionUID = 1L;
	
	public JTextField jKey;
	public JTextArea jValue;
	public JTextArea jValueHtml;
	
	public JConfigPanel() {
		super(4);
		initialize();
	}

	protected void initialize() {
        //Key
        JLabel lKey = new JLabel(" Key:");
        lKey.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lKey);
        jKey = new JTextField();
        gridPanel.add(jKey);
        
        //Value
        JLabel lValue = new JLabel(" Value:");
        lValue.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lValue);
        jValue = new JTextArea();
		jValue.setFont(SwingUtils.getDefaultFont());
		JScrollPane jValuePane = new JScrollPane(jValue);
		jValuePane.setPreferredSize(new Dimension(0, 21));
        gridPanel.add(jValuePane);

        //Value HTML
        JLabel lValueHtml = new JLabel(" Value (HTML):");
        lValueHtml.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lValueHtml);
        jValueHtml = new JTextArea();
		jValueHtml.setFont(SwingUtils.getDefaultFont());
		JScrollPane jValueHtmlPane = new JScrollPane(jValueHtml);
		jValueHtmlPane.setPreferredSize(new Dimension(0, 21));
        gridPanel.add(jValueHtmlPane);
	}
	
	public JTextField getKey() {
		return jKey;
	}

	public JTextArea getValue() {
		return jValue;
	}

	public JTextArea getValueHtml() {
		return jValueHtml;
	}

	public void setKey(String s) {
		jKey.setText(s);
	}
	
	public void setValue(String s) {
		jValue.setText(s);
	}
	
	public void setValueHtml(String s) {
		jValueHtml.setText(s);
	}
	
	public void clear() {
		jId.setText("");
		jKey.setText("");
		jValue.setText("");
		jValueHtml.setText("");
	}
	
	public void focus() {
		jKey.requestFocus();
	}
	
}