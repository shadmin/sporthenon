package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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

import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.component.JRoundPanel;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Round;
import com.sporthenon.db.entity.RoundType;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;

public class JEditRoundsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	JEditResultDialog parent = null;
	private JDialogButtonBar jButtonBar = null;
	private JScrollPane jScrollPane = null;
	private JPanel roundsPanel = null;
	private List<JRoundPanel> rdPanels = new ArrayList<>();
	private int nbRounds = 0;
	private String alias;
	private Object param;
	private static final int INITIAL_COUNT = 15;

	public JEditRoundsDialog(JEditResultDialog owner) {
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

		this.setSize(new Dimension(850, 400));
		this.setContentPane(jContentPane);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
	}

	private JScrollPane getScrollPane() {
		jScrollPane = new JScrollPane();
		jScrollPane.setBorder(BorderFactory.createEmptyBorder());
		jScrollPane.setViewportView(getRoundsPanel());
		return jScrollPane;
	}
	
	private JPanel getRoundsPanel() {
		roundsPanel = new JPanel();
		roundsPanel.setLayout(new GridLayout(1, 1, 3, 3));
		return roundsPanel;
	}
	
	private JPanel getLabelsPanel() {
		JPanel labelsPanel = new JPanel();
		labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.X_AXIS));
		
		String[] tLabel = { "Round type", "Rank #1", "Result/Score", "Rank #2", "Result", "Rank #3", "Result", "Rank #4", "Result", "Rank #5", "Result",
				"Complex #1", "Complex #2", "City #1", "City #2", "Start date", "End date", "Tie", "Comment"};
		int[] tWidth = { 180, 260, 75, 260, 75, 260, 75, 260, 75, 260, 75, 300, 300, 220, 220, 70, 70, 50, 250 };
		
		for (int i = 0 ; i < tLabel.length ; i++) {
			JTextField tf = new JTextField();
			tf.setText(tLabel[i]);
			tf.setPreferredSize(new Dimension(tWidth[i], 21));
			tf.setEnabled(false);
			tf.setHorizontalAlignment(JTextField.CENTER);
			labelsPanel.add(tf);
		}
		
		return labelsPanel;
	}
	
	private void addRounds(int n) {
		for (int i = 0 ; i < n ; i++) {
			JRoundPanel rp = new JRoundPanel();
			for (int j = 0 ; j < rp.getRanks().length ; j++) {
				SwingUtils.fillPicklist(rp.getRanks()[j], JMainFrame.getPicklists().get(alias), param);				
			}
			SwingUtils.fillPicklist(rp.getRoundType(), JMainFrame.getPicklists().get(RoundType.alias), null);
			SwingUtils.fillPicklist(rp.getComplex1(), JMainFrame.getPicklists().get(Complex.alias), null);
			SwingUtils.fillPicklist(rp.getComplex2(), JMainFrame.getPicklists().get(Complex.alias), null);
			SwingUtils.fillPicklist(rp.getCity1(), JMainFrame.getPicklists().get(City.alias), null);
			SwingUtils.fillPicklist(rp.getCity2(), JMainFrame.getPicklists().get(City.alias), null);
			rdPanels.add(rp);
			roundsPanel.add(rp);
		}
		nbRounds += n;
		((GridLayout)roundsPanel.getLayout()).setRows(nbRounds + 1);
		roundsPanel.revalidate();
		roundsPanel.repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("ok")) {
			List<Round> rounds = new ArrayList<>();
			for (JRoundPanel rdpanel : rdPanels) {
				Integer idRoundType = SwingUtils.getValue(rdpanel.getRoundType());
				if (idRoundType != null && idRoundType > 0) {
					Round round = new Round();
					round.setId(rdpanel.getId());
					round.setIdRoundType(idRoundType);
					round.setIdRank1(SwingUtils.getValue(rdpanel.getRanks()[0]));
					round.setIdRank2(SwingUtils.getValue(rdpanel.getRanks()[1]));
					round.setIdRank3(SwingUtils.getValue(rdpanel.getRanks()[2]));
					round.setIdRank4(SwingUtils.getValue(rdpanel.getRanks()[3]));
					round.setIdRank5(SwingUtils.getValue(rdpanel.getRanks()[4]));
					round.setResult1(StringUtils.notEmpty(rdpanel.getRes()[0].getText()) ? rdpanel.getRes()[0].getText() : null);
					round.setResult2(StringUtils.notEmpty(rdpanel.getRes()[1].getText()) ? rdpanel.getRes()[1].getText() : null);
					round.setResult3(StringUtils.notEmpty(rdpanel.getRes()[2].getText()) ? rdpanel.getRes()[2].getText() : null);
					round.setResult4(StringUtils.notEmpty(rdpanel.getRes()[3].getText()) ? rdpanel.getRes()[3].getText() : null);
					round.setResult5(StringUtils.notEmpty(rdpanel.getRes()[4].getText()) ? rdpanel.getRes()[4].getText() : null);
					round.setIdComplex1(SwingUtils.getValue(rdpanel.getComplex1()));
					round.setIdComplex(SwingUtils.getValue(rdpanel.getComplex2()));
					round.setIdCity1(SwingUtils.getValue(rdpanel.getCity1()));
					round.setIdCity(SwingUtils.getValue(rdpanel.getCity2()));
					round.setDate1(StringUtils.notEmpty(rdpanel.getDate1().getText()) ? rdpanel.getDate1().getText() : null);
					round.setDate(StringUtils.notEmpty(rdpanel.getDate2().getText()) ? rdpanel.getDate2().getText() : null);
					round.setExa(StringUtils.notEmpty(rdpanel.getExa().getText()) ? rdpanel.getExa().getText() : null);
					round.setComment(StringUtils.notEmpty(rdpanel.getComment().getText()) ? rdpanel.getComment().getText() : null);
					rounds.add(round);
				}
			}
			parent.setRounds(rounds);
			parent.setRoundsModified(true);
		}
		else if (cmd.equals("add")) {
			addRounds(10);
		}
		this.setVisible(!cmd.matches("ok|cancel"));
	}
	
	public void clear() {
		rdPanels.clear();
		roundsPanel.removeAll();
		roundsPanel.add(getLabelsPanel());
		nbRounds = 0;
		addRounds(INITIAL_COUNT);
	}

	public void open(String alias, Object param, List<Round> rounds) {
		this.alias = alias;
		this.param = param;
		clear();
		setValues(rounds);
		this.setTitle("Edit Rounds");
		this.setVisible(true);
	}
	
	private void setValues(List<Round> rounds) {
		int i = 0;
		for (Round round : rounds) {
			JRoundPanel rdpanel = rdPanels.get(i);
			rdpanel.setId(round.getId());
			SwingUtils.selectValue(rdpanel.getRoundType(), round.getIdRoundType());
			SwingUtils.selectValue(rdpanel.getRanks()[0], round.getIdRank1());
			SwingUtils.selectValue(rdpanel.getRanks()[1], round.getIdRank2());
			SwingUtils.selectValue(rdpanel.getRanks()[2], round.getIdRank3());
			SwingUtils.selectValue(rdpanel.getRanks()[3], round.getIdRank4());
			SwingUtils.selectValue(rdpanel.getRanks()[4], round.getIdRank5());
			rdpanel.getRes()[0].setText(round.getResult1());
			rdpanel.getRes()[1].setText(round.getResult2());
			rdpanel.getRes()[2].setText(round.getResult3());
			rdpanel.getRes()[3].setText(round.getResult4());
			rdpanel.getRes()[4].setText(round.getResult5());
			SwingUtils.selectValue(rdpanel.getComplex1(), round.getIdComplex1());
			SwingUtils.selectValue(rdpanel.getComplex2(), round.getIdComplex());
			SwingUtils.selectValue(rdpanel.getCity1(), round.getIdCity1());
			SwingUtils.selectValue(rdpanel.getCity2(), round.getIdCity());
			rdpanel.getDate1().setText(round.getDate1());
			rdpanel.getDate2().setText(round.getDate());
			rdpanel.getExa().setText(round.getExa());
			rdpanel.getComment().setText(round.getComment());
			if (++i >= nbRounds) {
				addRounds(10);
			}
		}
	}

	public List<JRoundPanel> getRounds() {
		return rdPanels;
	}

}