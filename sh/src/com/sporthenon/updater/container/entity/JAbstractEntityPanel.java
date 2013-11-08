package com.sporthenon.updater.container.entity;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sporthenon.updater.component.JCustomButton;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.updater.window.JFindEntityDialog;
import com.sporthenon.updater.window.JMainFrame;
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
		JEntityPicklist srcPicklist = (JEntityPicklist)((JCustomButton)e.getSource()).getParent().getParent();
		if (e.getActionCommand().matches(".*\\-add")) {
			JMainFrame.getEntityDialog().open(alias, srcPicklist);
			return;
		}
		else if (e.getActionCommand().matches(".*\\-find")) {
			JFindEntityDialog dlg = JMainFrame.getFindDialog();
			dlg.open(alias, srcPicklist);
			if (dlg.getSelectedItem() != null)
				SwingUtils.selectValue(srcPicklist, dlg.getSelectedItem().getValue());
		}
	}

}