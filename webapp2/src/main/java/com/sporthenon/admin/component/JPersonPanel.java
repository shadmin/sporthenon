package com.sporthenon.admin.component;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sporthenon.db.entity.Athlete;

public class JPersonPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Integer id = null;
	private JCustomButton jRemoveButton;
	private JTextField jNote;
	private JEntityPicklist jPerson;
	private ActionListener listener;
	
	public JPersonPanel(ActionListener listener) {
		super();
		this.listener = listener;
		initialize();
	}

	private void initialize() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		jRemoveButton = new JCustomButton(null, "remove.png", "Remove");
		jRemoveButton.setActionCommand("remove");
		jRemoveButton.addActionListener(listener);
		this.add(jRemoveButton, null);
		
		jNote = new JTextField();
		jNote.setPreferredSize(new Dimension(80, 21));
		this.add(jNote, null);
		
		jPerson = new JEntityPicklist(listener, Athlete.alias, true);
		jPerson.setPreferredSize(new Dimension(260, 21));
		this.add(jPerson, null);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public JTextField getNote() {
		return jNote;
	}
	
	public JEntityPicklist getPerson() {
		return jPerson;
	}

}