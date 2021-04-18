package com.sporthenon.admin.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class JDialogButtonBar extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JCustomButton jOk;
	private JCustomButton jCancel;
	private JCustomButton jOptional1;
	private JCustomButton jOptional2;
	private JCustomButton jOptional3;
	private JCustomButton jOptional4;
	
	public JDialogButtonBar(ActionListener listener) {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(0, 26));

		JPanel pLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		
		jOk = new JCustomButton("OK", "ok.png", null);
		jCancel = new JCustomButton("Cancel", "cancel.png", null);
		jOptional1 = new JCustomButton(".", null, null);
		jOptional1.setVisible(false);
		jOptional2 = new JCustomButton(".", null, null);
		jOptional2.setVisible(false);
		jOptional3 = new JCustomButton(".", null, null);
		jOptional3.setVisible(false);
		jOptional4 = new JCustomButton(".", null, null);
		jOptional4.setVisible(false);
		jOk.setActionCommand("ok");
		jCancel.setActionCommand("cancel");
		jOk.addActionListener(listener);
		jCancel.addActionListener(listener);
		pLeft.add(jOptional1);
		pLeft.add(jOptional2);
		pLeft.add(jOptional3);
		pLeft.add(jOptional4);
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
		return jOptional1;
	}
	
	public JCustomButton getOptional2() {
		return jOptional2;
	}
	
	public JCustomButton getOptional3() {
		return jOptional3;
	}
	
	public JCustomButton getOptional4() {
		return jOptional4;
	}
	
}
