package com.sporthenon.admin.container.entity;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.component.JLinkTextField;
import com.sporthenon.admin.window.JFindEntityDialog;
import com.sporthenon.admin.window.JMainFrame;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.utils.SwingUtils;


public abstract class JAbstractEntityPanel extends JPanel implements ActionListener, IEntityPanel {

	private static final long serialVersionUID = 1L;
	
	protected static final int LINE_HEIGHT = 20;
	protected static final int LINE_SPACING = 3;
	protected static final int LABEL_ALIGNMENT = JLabel.LEFT;
	protected static final Dimension TEXT_SIZE = new Dimension(0, 21);
	
	protected JPanel gridPanel;
	public JTextField jId;
	
	protected JAbstractEntityPanel() {
	}
	
	protected JAbstractEntityPanel(int n) {
		setLayout(new BorderLayout());
        gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(n * 2, 1, 0, 0));
		setPreferredSize(new Dimension(310, LINE_HEIGHT * n * 2));
		add(gridPanel, BorderLayout.NORTH);
		
        //ID
        JLabel lId = new JLabel(" ID:");
        lId.setHorizontalAlignment(LABEL_ALIGNMENT);
        gridPanel.add(lId);
        jId = new JTextField();
        jId.setEditable(false);
        gridPanel.add(jId);
	}

	protected abstract void initialize();

	public JTextField getId() {
		return jId;
	}

	public void setId(String s) {
		jId.setText(s);
	}
	
	public void actionPerformed(ActionEvent e) {
		String alias = e.getActionCommand().substring(0, 2);
		Object parent = ((JCustomButton)e.getSource()).getParent().getParent();
		JEntityPicklist srcPicklist = null;
		if (parent instanceof JEntityPicklist)
			srcPicklist = (JEntityPicklist)parent;
		else
			srcPicklist = (alias.equals(Athlete.alias) ? JMainFrame.getAllAthletes() : JMainFrame.getAllTeams());
		if (e.getActionCommand().matches(".*\\-add")) {
			JMainFrame.getEntityDialog().open(alias, srcPicklist);
			return;
		}
		else if (e.getActionCommand().matches(".*\\-find")) {
			JFindEntityDialog dlg = JMainFrame.getFindDialog();
			dlg.open(alias, parent instanceof JEntityPicklist ? srcPicklist : null);
			if (dlg.getSelectedItem() != null) {
				int value = dlg.getSelectedItem().getValue();
				if (parent instanceof JEntityPicklist)
					SwingUtils.selectValue(srcPicklist, value);
				else
					((JLinkTextField)parent).setText(String.valueOf(value));
			}
		}
	}

}