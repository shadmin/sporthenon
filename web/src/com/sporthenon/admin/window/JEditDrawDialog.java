package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.component.JEntityPicklist;

public class JEditDrawDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	JEditResultDialog parent = null;
	private JDialogButtonBar jButtonBar = null;
	private JEntityPicklist[] jEntity = null;
	private JTextField[] jRes = null;

	public JEditDrawDialog(JEditResultDialog owner) {
		super(owner);
		parent = (JEditResultDialog) this.getOwner();
		initialize();
	}

	private void initialize() {
		JPanel jContentPane = new JPanel();
		BorderLayout layout = new BorderLayout();
		layout.setVgap(5);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(layout);
		jButtonBar = new JDialogButtonBar(this);
		jContentPane.add(getDrawPanel(), BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);

		this.setSize(new Dimension(780, 440));
		this.setContentPane(jContentPane);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}

	private JPanel getDrawPanel() {
		JPanel jDrawPanel = new JPanel();
		jDrawPanel.setLayout(new GridLayout(0, 3));

		jEntity = new JEntityPicklist[14];
		jRes = new JTextField[7];

		JPanel jQf1Panel = new JPanel();
		jQf1Panel.setBorder(BorderFactory.createTitledBorder(null, "Quarterfinal 1", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jQf1Panel.setLayout(new GridLayout(3, 0, 2, 2));
		jEntity[0] = new JEntityPicklist(parent, "EN");
		jEntity[1] = new JEntityPicklist(parent, "EN");
		jRes[0] = new JTextField();
		jQf1Panel.add(jEntity[0]);
		jQf1Panel.add(jEntity[1]);
		jQf1Panel.add(jRes[0]);

		JPanel jQf2Panel = new JPanel();
		jQf2Panel.setBorder(BorderFactory.createTitledBorder(null, "Quarterfinal 2", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jQf2Panel.setLayout(new GridLayout(3, 0, 2, 2));
		jEntity[2] = new JEntityPicklist(parent, "EN");
		jEntity[3] = new JEntityPicklist(parent, "EN");
		jRes[1] = new JTextField();
		jQf2Panel.add(jEntity[2]);
		jQf2Panel.add(jEntity[3]);
		jQf2Panel.add(jRes[1]);

		JPanel jQf3Panel = new JPanel();
		jQf3Panel.setBorder(BorderFactory.createTitledBorder(null, "Quarterfinal 3", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jQf3Panel.setLayout(new GridLayout(3, 0, 2, 2));
		jEntity[4] = new JEntityPicklist(parent, "EN");
		jEntity[5] = new JEntityPicklist(parent, "EN");
		jRes[2] = new JTextField();
		jQf3Panel.add(jEntity[4]);
		jQf3Panel.add(jEntity[5]);
		jQf3Panel.add(jRes[2]);

		JPanel jQf4Panel = new JPanel();
		jQf4Panel.setBorder(BorderFactory.createTitledBorder(null, "Quarterfinal 4", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jQf4Panel.setLayout(new GridLayout(3, 0, 2, 2));
		jEntity[6] = new JEntityPicklist(parent, "EN");
		jEntity[7] = new JEntityPicklist(parent, "EN");
		jRes[3] = new JTextField();
		jQf4Panel.add(jEntity[6]);
		jQf4Panel.add(jEntity[7]);
		jQf4Panel.add(jRes[3]);

		JPanel jSf1Panel = new JPanel();
		jSf1Panel.setBorder(BorderFactory.createTitledBorder(null, "Semifinal 1", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jSf1Panel.setLayout(new GridLayout(3, 0, 2, 2));
		jEntity[8] = new JEntityPicklist(parent, "EN");
		jEntity[9] = new JEntityPicklist(parent, "EN");
		jRes[4] = new JTextField();
		jSf1Panel.add(jEntity[8]);
		jSf1Panel.add(jEntity[9]);
		jSf1Panel.add(jRes[4]);

		JPanel jSf2Panel = new JPanel();
		jSf2Panel.setBorder(BorderFactory.createTitledBorder(null, "Semifinal 2", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jSf2Panel.setLayout(new GridLayout(3, 0, 2, 2));
		jEntity[10] = new JEntityPicklist(parent, "EN");
		jEntity[11] = new JEntityPicklist(parent, "EN");
		jRes[5] = new JTextField();
		jSf2Panel.add(jEntity[10]);
		jSf2Panel.add(jEntity[11]);
		jSf2Panel.add(jRes[5]);
		
		JPanel jThdPanel = new JPanel();
		jThdPanel.setBorder(BorderFactory.createTitledBorder(null, "Third Place", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jThdPanel.setLayout(new GridLayout(3, 0, 2, 2));
		jEntity[12] = new JEntityPicklist(parent, "EN");
		jEntity[13] = new JEntityPicklist(parent, "EN");
		jRes[6] = new JTextField();
		jThdPanel.add(jEntity[12]);
		jThdPanel.add(jEntity[13]);
		jThdPanel.add(jRes[6]);

		jDrawPanel.add(jQf1Panel);jDrawPanel.add(new Panel());jDrawPanel.add(jQf2Panel);
		jDrawPanel.add(new Panel());jDrawPanel.add(jSf1Panel);jDrawPanel.add(new Panel());
		jDrawPanel.add(new Panel());jDrawPanel.add(jSf2Panel);jDrawPanel.add(jThdPanel);
		jDrawPanel.add(jQf3Panel);jDrawPanel.add(new Panel());jDrawPanel.add(jQf4Panel);

		return jDrawPanel;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("ok")) {
			parent.setDraw(true);
		}
		this.setVisible(false);
	}
	
	public void clear() {
		JComboBox cb = parent.getRanks()[0].getPicklist();
		for (JEntityPicklist epl : jEntity)
			epl.getPicklist().removeAllItems();
		for (JTextField tf : jRes)
			tf.setText("");
		for (int i = 0 ; i < cb.getItemCount() ; i++)
			for (JEntityPicklist epl : jEntity)
				epl.getPicklist().addItem(cb.getItemAt(i));
	}

	public void open() {
		this.setTitle("Edit Draw (Result #" + parent.getId() + ")");
		this.setVisible(true);
	}

	public JEntityPicklist[] getEntity() {
		return jEntity;
	}

	public JTextField[] getRes() {
		return jRes;
	}

}