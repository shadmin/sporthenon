package com.sporthenon.updater.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class JDialogButtonBar extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JCustomButton jOk;
	private JCustomButton jCancel;
	private JCustomButton jOptional;

	public JDialogButtonBar(ActionListener listener) {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(0, 26));

		JPanel pLeft = new JPanel();
		FlowLayout layout1 = new FlowLayout();
		layout1.setAlignment(FlowLayout.LEFT);
		layout1.setVgap(0);
		layout1.setHgap(3);
		pLeft.setLayout(layout1);
		
		JPanel pRight = new JPanel();
		FlowLayout layout2 = new FlowLayout();
		layout2.setAlignment(FlowLayout.RIGHT);
		layout2.setVgap(0);
		layout2.setHgap(3);
		pRight.setLayout(layout2);
		
		jOk = new JCustomButton("OK", "ok.png", null);
		jCancel = new JCustomButton("Cancel", "cancel.png", null);
		jOptional = new JCustomButton(".", null, null);
		jOptional.setVisible(false);
		jOk.setActionCommand("ok");
		jCancel.setActionCommand("cancel");
		jOk.addActionListener(listener);
		jCancel.addActionListener(listener);
		pLeft.add(jOptional);
		pRight.add(jCancel);
		pRight.add(jOk);
		this.add(pLeft, BorderLayout.WEST);
		this.add(pRight, BorderLayout.EAST);
	}

	public JCustomButton getOk() {
		return jOk;
	}

	public JCustomButton getCancel() {
		return jCancel;
	}
	
	public JCustomButton getOptional() {
		return jOptional;
	}
	
}
