package com.sporthenon.updater.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Draw;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.Year;
import com.sporthenon.updater.component.JCustomButton;
import com.sporthenon.updater.component.JDialogButtonBar;
import com.sporthenon.updater.component.JEntityPicklist;
import com.sporthenon.updater.container.tab.JResultsPanel;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class JEditResultDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JDialogButtonBar jButtonBar = null;
	private JEntityPicklist jYear = null;
	private JEntityPicklist jCity = null;
	private JEntityPicklist jComplex = null;
	private JTextField jDate1 = null;
	private JTextField jDate2 = null;
	private JTextArea jComment = null;
	private JTextField jExa = null;
	private JCheckBox[] jExaCheckbox = new JCheckBox[10];
	private JEntityPicklist[] jRanks = new JEntityPicklist[10];
	private JTextField[] jRes = new JTextField[10];
	private JCommentDialog jCommentDialog = null;
	private JEditDrawDialog jEditDrawDialog = null;
	
	private JResultsPanel parent;
	private Integer id;
	private Integer drawId;
	private short mode;
	private int entityType;
	private boolean draw;
	public static final short NEW = 1;
	public static final short EDIT = 2;
	public static final short COPY = 3;
	
	public JEditResultDialog(JFrame owner) {
		super(owner);
		initialize();
	}

	private void initialize() {
		JPanel jContentPane = new JPanel();
		BorderLayout layout = new BorderLayout();
		layout.setVgap(5);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 4));
		jContentPane.setLayout(layout);
		jButtonBar = new JDialogButtonBar(this);
		jButtonBar.getOptional().setText("Draw");
		jButtonBar.getOptional().setIcon("updater/draw.png");
		jButtonBar.getOptional().setVisible(true);
		jButtonBar.getOptional().setActionCommand("draw");
		jButtonBar.getOptional().addActionListener(this);
		jContentPane.add(getEventPanel(), BorderLayout.NORTH);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
		jContentPane.add(getStandingsPanel(), BorderLayout.CENTER);
		
		jCommentDialog = new JCommentDialog(this);
		jEditDrawDialog = new JEditDrawDialog(this);
		
		this.setSize(new Dimension(685, 385));
        this.setContentPane(jContentPane);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModal(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
	}

	private JPanel getEventPanel() {
		jYear = new JEntityPicklist(this, Year.alias);
		jYear.setPreferredSize(new Dimension(130, 21));
		jCity = new JEntityPicklist(this, City.alias);
		jCity.setPreferredSize(new Dimension(240, 21));
		jComplex = new JEntityPicklist(this, Complex.alias);
		jComplex.setPreferredSize(new Dimension(280, 21));
		jDate1 = new JTextField();
		jDate1.setPreferredSize(new Dimension(72, 21));
		jDate2 = new JTextField();
		jDate2.setPreferredSize(new Dimension(72, 21));
		JCustomButton jToday = new JCustomButton(null, "updater/today.png");
		jToday.setMargin(new Insets(0, 0, 0, 0));
		jToday.setToolTipText("Today");
		jToday.setActionCommand("today");
		jToday.addActionListener(this);
		jComment = new JTextArea();
		jComment.setFont(SwingUtils.getDefaultFont());
		JScrollPane jCommentPane = new JScrollPane(jComment);
		jCommentPane.setPreferredSize(new Dimension(240, 60));
		JCustomButton jCommentDlg = new JCustomButton("...", null);
		jCommentDlg.setMargin(new Insets(0, 0, 0, 0));
		jCommentDlg.setToolTipText("Edit Comment");
		jCommentDlg.setActionCommand("comment");
		jCommentDlg.addActionListener(this);
		jExa = new JTextField();
		jExa.setPreferredSize(new Dimension(60, 21));
		
		JPanel jEventPanel = new JPanel();
		jEventPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 4));
		jEventPanel.setPreferredSize(new Dimension(0, 150));
		jEventPanel.setBorder(BorderFactory.createTitledBorder(null, "Event Info", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jEventPanel.add(new JLabel("Year:"), null);
		jEventPanel.add(jYear, null);
		jEventPanel.add(new JLabel("City:"), null);
		jEventPanel.add(jCity, null);
		jEventPanel.add(new JLabel("From:"), null);
		jEventPanel.add(jDate1, null);
		jEventPanel.add(new JLabel("to:"), null);
		jEventPanel.add(jDate2, null);
		jEventPanel.add(jToday, null);
		jEventPanel.add(new JLabel("Complex:"), null);
		jEventPanel.add(jComplex, null);
		jEventPanel.add(new JLabel("Comment:"), null);
		jEventPanel.add(jCommentPane, null);
		jEventPanel.add(jCommentDlg, null);
		jEventPanel.add(new JLabel("Tie:"), null);
		for (int i = 0 ; i < 10 ; i++) {
			jExaCheckbox[i] = new JCheckBox(String.valueOf(i + 1));
			jEventPanel.add(jExaCheckbox[i], null);
			jExaCheckbox[i].setActionCommand("exacb-" + (i + 1));
			jExaCheckbox[i].addActionListener(this);
		}
		jEventPanel.add(jExa, null);

		return jEventPanel;
	}

	private JPanel getStandingsPanel() {
		JLabel[] labels = new JLabel[10];
		for (int i = 0 ; i < jRanks.length ; i++) {
			jRanks[i] = new JEntityPicklist(this, "EN");
			jRanks[i].setPreferredSize(new Dimension(220, 21));
			labels[i] = new JLabel(ResourceUtils.get("rank.short." + (i + 1)) + ":");
			labels[i].setPreferredSize(new Dimension(28, 21));
			jRes[i] = new JTextField();
			jRes[i].setPreferredSize(new Dimension(70, 21));
		}
		
		JPanel jStandingsPanel = new JPanel();
		jStandingsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 4));
		jStandingsPanel.setPreferredSize(new Dimension(0, 80));
		jStandingsPanel.setBorder(BorderFactory.createTitledBorder(null, "Standings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jStandingsPanel.add(labels[0]); jStandingsPanel.add(jRanks[0]); jStandingsPanel.add(jRes[0]);
		jStandingsPanel.add(labels[5]); jStandingsPanel.add(jRanks[5]); jStandingsPanel.add(jRes[5]);
		jStandingsPanel.add(labels[1]); jStandingsPanel.add(jRanks[1]); jStandingsPanel.add(jRes[1]);
		jStandingsPanel.add(labels[6]); jStandingsPanel.add(jRanks[6]); jStandingsPanel.add(jRes[6]);
		jStandingsPanel.add(labels[2]); jStandingsPanel.add(jRanks[2]); jStandingsPanel.add(jRes[2]);
		jStandingsPanel.add(labels[7]); jStandingsPanel.add(jRanks[7]); jStandingsPanel.add(jRes[7]);
		jStandingsPanel.add(labels[3]); jStandingsPanel.add(jRanks[3]); jStandingsPanel.add(jRes[3]);
		jStandingsPanel.add(labels[8]); jStandingsPanel.add(jRanks[8]); jStandingsPanel.add(jRes[8]);
		jStandingsPanel.add(labels[4]); jStandingsPanel.add(jRanks[4]); jStandingsPanel.add(jRes[4]);
		jStandingsPanel.add(labels[9]); jStandingsPanel.add(jRanks[9]); jStandingsPanel.add(jRes[9]);
		
		return jStandingsPanel;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.matches("\\D\\D\\-(add|find)")) {
			String alias = cmd.substring(0, 2);
			JEntityPicklist srcPicklist = (JEntityPicklist)((JCustomButton)e.getSource()).getParent().getParent();
			if (cmd.matches("\\D\\D\\-add")) {
				if (alias.equalsIgnoreCase("EN"))
					alias = (entityType < 10 ? "PR" : (entityType == 50 ? "TM" : "CN"));
				JMainFrame.getEntityDialog().open(alias, srcPicklist);
			}
			else {
				JFindEntityDialog dlg = JMainFrame.getFindDialog();
				dlg.open(alias, srcPicklist);
				if (dlg.getSelectedItem() != null) {
					SwingUtils.selectValue(srcPicklist, dlg.getSelectedItem().getValue());
					srcPicklist.requestFocus();
				}
			}
			return;
		}
		else if (cmd.equals("draw")) {
			jEditDrawDialog.clear();
			try {
				if (drawId != null) {
					Draw dr = (Draw) DatabaseHelper.loadEntity(Draw.class, drawId);
					SwingUtils.selectValue(jEditDrawDialog.getEntity()[0], dr.getId1Qf1());
					SwingUtils.selectValue(jEditDrawDialog.getEntity()[1], dr.getId2Qf1());
					SwingUtils.selectValue(jEditDrawDialog.getEntity()[2], dr.getId1Qf2());
					SwingUtils.selectValue(jEditDrawDialog.getEntity()[3], dr.getId2Qf2());
					SwingUtils.selectValue(jEditDrawDialog.getEntity()[4], dr.getId1Qf3());
					SwingUtils.selectValue(jEditDrawDialog.getEntity()[5], dr.getId2Qf3());
					SwingUtils.selectValue(jEditDrawDialog.getEntity()[6], dr.getId1Qf4());
					SwingUtils.selectValue(jEditDrawDialog.getEntity()[7], dr.getId2Qf4());
					SwingUtils.selectValue(jEditDrawDialog.getEntity()[8], dr.getId1Sf1());
					SwingUtils.selectValue(jEditDrawDialog.getEntity()[9], dr.getId2Sf1());
					SwingUtils.selectValue(jEditDrawDialog.getEntity()[10], dr.getId1Sf2());
					SwingUtils.selectValue(jEditDrawDialog.getEntity()[11], dr.getId2Sf2());
					jEditDrawDialog.getRes()[0].setText(dr.getResult_qf1());
					jEditDrawDialog.getRes()[1].setText(dr.getResult_qf2());
					jEditDrawDialog.getRes()[2].setText(dr.getResult_qf3());
					jEditDrawDialog.getRes()[3].setText(dr.getResult_qf4());
					jEditDrawDialog.getRes()[4].setText(dr.getResult_sf1());
					jEditDrawDialog.getRes()[5].setText(dr.getResult_sf2());
				}
			}
			catch (Exception e_) {
				Logger.getLogger("sh").error(e_.getMessage(), e_);
			}
			jEditDrawDialog.open();
		}
		else if (cmd.equals("today")) {
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			jDate2.setText(df.format(Calendar.getInstance().getTime()));
		}
		else if (cmd.equals("comment")) {
			jCommentDialog.getComment().setText(getComment().getText());
			jCommentDialog.open();
		}
		else if (cmd.matches("exacb.*")) {
			int min = 100;
			int max = -1;
			for (int i = 0 ; i < 10 ; i++) {
				if (jExaCheckbox[i].isSelected() && i < min)
					min = i;
				if (jExaCheckbox[i].isSelected() && i > max)
					max = i;
			}
			if (max > -1) {
				for (int i = min ; i <= max ; i++)
					jExaCheckbox[i].setSelected(true);
				jExa.setText(String.valueOf(min != max ? (min + 1) + "-" + (max + 1) : (min + 1)));
			}
			else
				jExa.setText(null);
		}
		else if (cmd.equals("ok")) {
			Result rs = null;
			Draw dr = null;
			String msg = null;
			boolean err = false;
			try {
				rs = (Result)(mode == EDIT ? DatabaseHelper.loadEntity(Result.class, id) : new Result());
				rs.setSport((Sport)DatabaseHelper.loadEntity(Sport.class, JResultsPanel.getIdSport()));
				rs.setChampionship((Championship)DatabaseHelper.loadEntity(Championship.class, JResultsPanel.getIdChampionship()));
				rs.setEvent((Event)DatabaseHelper.loadEntity(Event.class, JResultsPanel.getIdEvent()));
				rs.setSubevent((Event)DatabaseHelper.loadEntity(Event.class, JResultsPanel.getIdSubevent() != null ? JResultsPanel.getIdSubevent() : 0));
				rs.setYear((Year)DatabaseHelper.loadEntity(Year.class, SwingUtils.getValue(jYear)));
				rs.setCity((City)DatabaseHelper.loadEntity(City.class, SwingUtils.getValue(jCity)));
				rs.setComplex((Complex)DatabaseHelper.loadEntity(Complex.class, SwingUtils.getValue(jComplex)));
				rs.setDate1(StringUtils.notEmpty(jDate1.getText()) ? jDate1.getText() : "");
				rs.setDate2(StringUtils.notEmpty(jDate2.getText()) ? jDate2.getText() : "");
				rs.setComment(StringUtils.notEmpty(jComment.getText()) ? jComment.getText() : "");
				rs.setExa(StringUtils.notEmpty(jExa.getText()) ? jExa.getText() : "");
				for (int i = 0 ; i < jRanks.length ; i++) {
					Integer id = SwingUtils.getValue(jRanks[i]);
					Result.class.getMethod("setIdRank" + (i + 1), Integer.class).invoke(rs, id > 0 ? id : null);
					Result.class.getMethod("setResult" + (i + 1), String.class).invoke(rs, StringUtils.notEmpty(jRes[i].getText()) ? jRes[i].getText() : null);
				}
				rs = (Result) DatabaseHelper.saveEntity(rs, JMainFrame.getMember());
				if (isDraw()) {
					dr = (Draw)(drawId != null && drawId > 0 ? DatabaseHelper.loadEntity(Draw.class, drawId) : new Draw());
					dr.setIdResult(rs.getId());
					int i = 0;
					for (String m : new String[]{"Id1Qf1", "Id2Qf1", "Id1Qf2", "Id2Qf2", "Id1Qf3", "Id2Qf3", "Id1Qf4", "Id2Qf4", "Id1Sf1", "Id2Sf1", "Id1Sf2", "Id2Sf2"}) {
						Integer value = SwingUtils.getValue(jEditDrawDialog.getEntity()[i++]);
						Draw.class.getMethod("set" + m, Integer.class).invoke(dr, value > 0 ? value : null);
					}
					dr.setResult_qf1(jEditDrawDialog.getRes()[0].getText());
					dr.setResult_qf2(jEditDrawDialog.getRes()[1].getText());
					dr.setResult_qf3(jEditDrawDialog.getRes()[2].getText());
					dr.setResult_qf4(jEditDrawDialog.getRes()[3].getText());
					dr.setResult_sf1(jEditDrawDialog.getRes()[4].getText());
					dr.setResult_sf2(jEditDrawDialog.getRes()[5].getText());
					dr = (Draw) DatabaseHelper.saveEntity(dr, JMainFrame.getMember());
				}
				msg = "Result #" + rs.getId() + (isDraw() ? "/Draw #" + dr.getId() : "") + " has been successfully " + (mode == EDIT ? "updated" : "created") + ".";
			}
			catch (Exception e_) {
				err = true;
				msg = e_.getMessage();
				Logger.getLogger("sh").error(e_.getMessage(), e_);
			}
			finally {
				parent.resultCallback(mode, getDataVector(rs), msg, err);
			}
		}
		this.setVisible(cmd.matches("draw|comment|today|exacb.*"));
	}
	
	private Vector getDataVector(Result rs) {
		Vector<Object> v = new Vector<Object>();
		v.add(rs.getId());
		v.add(SwingUtils.getText(jYear));
		v.add(StringUtils.notEmpty(SwingUtils.getText(jComplex)) ? SwingUtils.getText(jComplex) : SwingUtils.getText(jCity));
		v.add((StringUtils.notEmpty(jDate1.getText()) ? jDate1.getText() + "-" : "") + (StringUtils.notEmpty(jDate2.getText()) ? jDate2.getText() : ""));
		for (int i = 0 ; i < jRanks.length ; i++)
			v.add(SwingUtils.getText(jRanks[i]) + (StringUtils.notEmpty(jRes[i].getText()) ? " - " + jRes[i].getText() : ""));
		return v;
	}
	
	public void clear() {
		jYear.getPicklist().setSelectedIndex(-1);
		jComplex.getPicklist().setSelectedIndex(-1);
		jCity.getPicklist().setSelectedIndex(-1);
		jDate1.setText("");
		jDate2.setText("");
		jComment.setText("");
		for (JEntityPicklist pl : jRanks)
			pl.getPicklist().setSelectedIndex(-1);
		for (JTextField tf : jRes)
			tf.setText("");
	}
	
	public void open(JResultsPanel parent, Integer id, Integer drawId, short mode, int entityType) {
		this.parent = parent;
		this.id = id;
		this.drawId = drawId;
		this.mode = mode;
		this.entityType = entityType;
		this.setTitle(mode == NEW || mode == COPY ? "New Result" : "Edit Result #" + id);
		this.setDraw(false);
		this.setVisible(true);
		for (int i = 0 ; i < 10 ; i++)
			jExaCheckbox[i].setSelected(false);
	}
	
	public Integer getId() {
		return id;
	}
	
	public JEntityPicklist getYear() {
		return jYear;
	}

	public JEntityPicklist getCity() {
		return jCity;
	}

	public JEntityPicklist getComplex() {
		return jComplex;
	}
	
	public JEntityPicklist[] getRanks() {
		return jRanks;
	}

	public JTextField getDate1() {
		return jDate1;
	}

	public JTextField getDate2() {
		return jDate2;
	}

	public JTextArea getComment() {
		return jComment;
	}
	
	public JTextField getExa() {
		return jExa;
	}
	
	public JCheckBox[] getExaCheckbox() {
		return jExaCheckbox;
	}

	public JTextField[] getRes() {
		return jRes;
	}

	public void setDrawId(Integer drawId) {
		this.drawId = drawId;
	}

	public boolean isDraw() {
		return draw;
	}

	public void setDraw(boolean draw) {
		this.draw = draw;
	}

}