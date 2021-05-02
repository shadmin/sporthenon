package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.component.JPersonPanel;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.meta.PersonList;
import com.sporthenon.utils.SwingUtils;

public class JEditPersonListDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	JEditResultDialog parent = null;
	private JDialogButtonBar jButtonBar = null;
	private JScrollPane jScrollPane = null;
	private JPanel personsPanel = null;
	private List<JPersonPanel> prPanels = new ArrayList<>();
	private int nbPersons = 0;
	private Object param;
	private Integer rank;
	private static final int INITIAL_COUNT = 30;

	public JEditPersonListDialog(JEditResultDialog owner) {
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
		jButtonBar.getOptional().setText("Add 10 lines");
		jButtonBar.getOptional().setIcon("addmultiple.png");
		jButtonBar.getOptional().setVisible(true);
		jButtonBar.getOptional().setActionCommand("add");
		jButtonBar.getOptional().addActionListener(this);
		jContentPane.add(getScrollPane(), BorderLayout.CENTER);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);

		this.setContentPane(jContentPane);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}

	private JScrollPane getScrollPane() {
		jScrollPane = new JScrollPane();
		jScrollPane.setBorder(BorderFactory.createEmptyBorder());
		jScrollPane.setViewportView(getPersonsPanel());
		return jScrollPane;
	}
	
	private JPanel getPersonsPanel() {
		personsPanel = new JPanel();
		personsPanel.setLayout(new BoxLayout(personsPanel, BoxLayout.Y_AXIS));
		return personsPanel;
	}
	
	private JPanel getLabelsPanel() {
		JPanel labelsPanel = new JPanel();
		labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.X_AXIS));
		
		String[] tLabel = { "", "Note", "Name"};
		int[] tWidth = { 20, 80, 260 };
		
		for (int i = 0 ; i < tLabel.length ; i++) {
			JTextField tf = new JTextField();
			tf.setText(tLabel[i]);
			tf.setPreferredSize(new Dimension(tWidth[i], 21));
			tf.setEnabled(false);
			tf.setHorizontalAlignment(JTextField.CENTER);
			if (i == 0) {
				tf.setBorder(null);
			}
			labelsPanel.add(tf);
		}
		
		return labelsPanel;
	}
	
	private void addPersonLists(int n) {
		for (int i = 0 ; i < n ; i++) {
			JPersonPanel pp = new JPersonPanel(this);
			SwingUtils.fillPicklist(pp.getPerson(), JMainFrame.getPicklists().get(Athlete.alias), param);
			prPanels.add(pp);
			personsPanel.add(pp);
		}
		nbPersons += n;
		personsPanel.revalidate();
		personsPanel.repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.matches("\\D\\D\\-(add|find)")) {
			SwingUtils.openAddFindDialog(e, parent.getResultType().getNumber());
			return;
		}
		else if (cmd.equals("remove")) {
			JPersonPanel pp = (JPersonPanel)((JCustomButton) e.getSource()).getParent();
			prPanels.remove(pp);
			personsPanel.remove(pp);
			parent.getPersonListDeleted().add(pp.getId());
		}
		else if (cmd.equals("ok")) {
			List<PersonList> personlists = new ArrayList<>();
			for (JPersonPanel prpanel : prPanels) {
				Integer idPerson = SwingUtils.getValue(prpanel.getPerson());
				if (idPerson != null && idPerson > 0) {
					PersonList plist = new PersonList();
					plist.setId(prpanel.getId());
					plist.setIdPerson(idPerson);
					plist.setRank(rank);
					plist.setIndex(prpanel.getNote().getText());
					personlists.add(plist);
				}
			}
			parent.getMapPersonList().put(rank, personlists);
			parent.getPersonListModified().add(rank);
		}
		else if (cmd.equals("add")) {
			addPersonLists(10);
		}
		this.setVisible(!cmd.matches("ok|cancel"));
	}
	
	public void clear() {
		prPanels.clear();
		personsPanel.removeAll();
		personsPanel.add(getLabelsPanel());
		nbPersons = 0;
		addPersonLists(INITIAL_COUNT);
	}

	public void open(Integer rank, Object param, List<PersonList> personlists) {
		this.rank = rank;
		this.param = param;
		clear();
		setValues(personlists);
		this.setSize(new Dimension(410, 350));
		this.setTitle("Rank " + rank);
		this.setVisible(true);
	}
	
	private void setValues(List<PersonList> personlists) {
		int i = 0;
		for (PersonList plist : personlists) {
			JPersonPanel prpanel = prPanels.get(i);
			prpanel.setId(plist.getId());
			SwingUtils.selectValue(prpanel.getPerson(), plist.getIdPerson());
			prpanel.getNote().setText(plist.getIndex());
			if (++i >= nbPersons) {
				addPersonLists(10);
			}
		}
	}

	public List<JPersonPanel> getPersonLists() {
		return prPanels;
	}

}