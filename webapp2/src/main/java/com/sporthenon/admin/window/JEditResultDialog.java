package com.sporthenon.admin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.sporthenon.admin.component.JCustomButton;
import com.sporthenon.admin.component.JDialogButtonBar;
import com.sporthenon.admin.component.JEntityPicklist;
import com.sporthenon.admin.container.tab.JResultsPanel;
import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.ExternalLink;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.SwingUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class JEditResultDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(JEditResultDialog.class.getName());
	
	private JDialogButtonBar jButtonBar = null;
	private JEntityPicklist jYear = null;
	private JEntityPicklist jCity1 = null;
	private JEntityPicklist jComplex1 = null;
	private JEntityPicklist jCity2 = null;
	private JEntityPicklist jComplex2 = null;
	private JTextField jDate1 = null;
	private JTextField jDate2 = null;
	private JTextArea jComment = null;
	private JTextField jExa = null;
	private JCheckBox[] jExaCheckbox = new JCheckBox[10];
	private JEntityPicklist[] jRanks = new JEntityPicklist[10];
	private JTextField[] jRes = new JTextField[10];
	private JCommentDialog jCommentDialog = null;
	
	private JResultsPanel parent;
	private Integer id;
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
		jButtonBar.getOptional().setIcon("draw.png");
		jButtonBar.getOptional().setVisible(true);
		jButtonBar.getOptional().setActionCommand("draw");
		jButtonBar.getOptional().addActionListener(this);
		jContentPane.add(getEventPanel(), BorderLayout.NORTH);
		jContentPane.add(jButtonBar, BorderLayout.SOUTH);
		jContentPane.add(getStandingsPanel(), BorderLayout.CENTER);
		
		jCommentDialog = new JCommentDialog(this);
		
		this.setSize(new Dimension(685, 425));
        this.setContentPane(jContentPane);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModal(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
	}

	private JPanel getEventPanel() {
		jYear = new JEntityPicklist(this, Year.alias);
		jYear.setPreferredSize(new Dimension(130, 21));
		jCity1 = new JEntityPicklist(this, City.alias);
		jCity1.setPreferredSize(new Dimension(250, 21));
		jComplex1 = new JEntityPicklist(this, Complex.alias);
		jComplex1.setPreferredSize(new Dimension(280, 21));
		jCity2 = new JEntityPicklist(this, City.alias);
		jCity2.setPreferredSize(new Dimension(250, 21));
		jComplex2 = new JEntityPicklist(this, Complex.alias);
		jComplex2.setPreferredSize(new Dimension(280, 21));
		jDate1 = new JTextField();
		jDate1.setPreferredSize(new Dimension(72, 21));
		jDate2 = new JTextField();
		jDate2.setPreferredSize(new Dimension(72, 21));
		JCustomButton jToday = new JCustomButton(null, "today.png", null);
		jToday.setMargin(new Insets(0, 0, 0, 0));
		jToday.setToolTipText("Today");
		jToday.setActionCommand("today");
		jToday.addActionListener(this);
		jComment = new JTextArea();
		jComment.setFont(SwingUtils.getDefaultFont());
		JScrollPane jCommentPane = new JScrollPane(jComment);
		jCommentPane.setPreferredSize(new Dimension(150, 60));
		JCustomButton jCommentDlg = new JCustomButton("...", null, null);
		jCommentDlg.setMargin(new Insets(0, 0, 0, 0));
		jCommentDlg.setToolTipText("Edit Comment");
		jCommentDlg.setActionCommand("comment");
		jCommentDlg.addActionListener(this);
		jExa = new JTextField();
		jExa.setPreferredSize(new Dimension(60, 21));
		JCustomButton jExtLinksButton = new JCustomButton("Ext. Links", "weblinks.png", "External Links");
		jExtLinksButton.addActionListener(this);
		jExtLinksButton.setActionCommand("extlinks");
		
		JSeparator jSeparator1 = new JSeparator(JSeparator.HORIZONTAL);
		jSeparator1.setPreferredSize(new Dimension(250, 0));
		JPanel jEventPanel = new JPanel();
		jEventPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 4));
		jEventPanel.setPreferredSize(new Dimension(0, 200));
		jEventPanel.setBorder(BorderFactory.createTitledBorder(null, "Event Info", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black));
		jEventPanel.add(new JLabel("Year:"), null);
		jEventPanel.add(jYear, null);
		jEventPanel.add(new JLabel("From:"), null);
		jEventPanel.add(jDate1, null);
		jEventPanel.add(new JLabel("to:"), null);
		jEventPanel.add(jDate2, null);
		jEventPanel.add(jToday, null);
		jEventPanel.add(jSeparator1);
		jEventPanel.add(new JLabel("Complex #1:"), null);
		jEventPanel.add(jComplex1, null);
		jEventPanel.add(new JLabel("City #1:"), null);
		jEventPanel.add(jCity1, null);
		jEventPanel.add(new JLabel("Complex #2:"), null);
		jEventPanel.add(jComplex2, null);
		jEventPanel.add(new JLabel("City #2:"), null);
		jEventPanel.add(jCity2, null);
		jEventPanel.add(new JLabel("Tie:"), null);
		for (int i = 0 ; i < 10 ; i++) {
			jExaCheckbox[i] = new JCheckBox(String.valueOf(i + 1));
			jEventPanel.add(jExaCheckbox[i], null);
			jExaCheckbox[i].setActionCommand("exacb-" + (i + 1));
			jExaCheckbox[i].addActionListener(this);
		}
		jEventPanel.add(jExa, null);
		jEventPanel.add(new JLabel("Comment:"), null);
		jEventPanel.add(jCommentPane, null);
		jEventPanel.add(jCommentDlg, null);
		jEventPanel.add(jExtLinksButton, null);

		return jEventPanel;
	}

	private JPanel getStandingsPanel() {
		JLabel[] labels = new JLabel[10];
		for (int i = 0 ; i < jRanks.length ; i++) {
			jRanks[i] = new JEntityPicklist(this, "EN");
			jRanks[i].setPreferredSize(new Dimension(220, 21));
			labels[i] = new JLabel(ResourceUtils.getText("rank." + (i + 1), ResourceUtils.LGDEFAULT) + ":");
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

	@SuppressWarnings("unchecked")
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
//		else if (cmd.equals("draw")) {
//			jEditDrawDialog.clear();
//			try {
//				if (drawId != null) {
//					Draw dr = (Draw) DatabaseHelper.loadEntity(Draw.class, drawId);
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[0], dr.getId1Qf1());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[1], dr.getId2Qf1());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[2], dr.getId1Qf2());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[3], dr.getId2Qf2());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[4], dr.getId1Qf3());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[5], dr.getId2Qf3());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[6], dr.getId1Qf4());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[7], dr.getId2Qf4());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[8], dr.getId1Sf1());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[9], dr.getId2Sf1());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[10], dr.getId1Sf2());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[11], dr.getId2Sf2());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[12], dr.getId1Thd());
//					SwingUtils.selectValue(jEditDrawDialog.getEntity()[13], dr.getId2Thd());
//					jEditDrawDialog.getRes()[0].setText(dr.getResult_qf1());
//					jEditDrawDialog.getRes()[1].setText(dr.getResult_qf2());
//					jEditDrawDialog.getRes()[2].setText(dr.getResult_qf3());
//					jEditDrawDialog.getRes()[3].setText(dr.getResult_qf4());
//					jEditDrawDialog.getRes()[4].setText(dr.getResult_sf1());
//					jEditDrawDialog.getRes()[5].setText(dr.getResult_sf2());
//					jEditDrawDialog.getRes()[6].setText(dr.getResult_thd());
//				}
//			}
//			catch (Exception e_) {
//				log.log(Level.WARNING, e_.getMessage(), e_);
//			}
//			jEditDrawDialog.open();
//		}
		else if (cmd.equals("today")) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			jDate2.setText(df.format(Calendar.getInstance().getTime()));
		}
		else if (cmd.equals("comment")) {
			jCommentDialog.getComment().setText(getComment().getText());
			jCommentDialog.open();
		}
		else if (cmd.equals("extlinks")) {
			StringBuffer sbLinks = new StringBuffer();
			try {
				List<ExternalLink> list = (List<ExternalLink>) DatabaseManager.executeSelect("SELECT * FROM _external_link where entity = '" + Result.alias + "' and id_item = " + id + " order by id", ExternalLink.class);
				for (ExternalLink link : list)
					sbLinks.append(link.getUrl()).append("\r\n");
			}
			catch (Exception e_) {
				log.log(Level.WARNING, e_.getMessage(), e_);
			}
			JCommentDialog dialog = JMainFrame.getCommentDialog();
			dialog.setAlias(Result.alias);
			dialog.setId(id);
			dialog.setTitle("External Links (#" + id + ")");
			dialog.getComment().setText(sbLinks.toString());
			dialog.setSize(new Dimension(600, 250));
			dialog.open();
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
			String msg = null;
			boolean err = false;
			try {
				rs = (Result)(mode == EDIT ? DatabaseManager.loadEntity(Result.class, id) : new Result());
				rs.setSport((Sport)DatabaseManager.loadEntity(Sport.class, JResultsPanel.getIdSport()));
				rs.setChampionship((Championship)DatabaseManager.loadEntity(Championship.class, JResultsPanel.getIdChampionship()));
				rs.setEvent((Event)DatabaseManager.loadEntity(Event.class, JResultsPanel.getIdEvent()));
				rs.setSubevent((Event)DatabaseManager.loadEntity(Event.class, JResultsPanel.getIdSubevent() != null ? JResultsPanel.getIdSubevent() : 0));
				rs.setSubevent2((Event)DatabaseManager.loadEntity(Event.class, JResultsPanel.getIdSubevent2() != null ? JResultsPanel.getIdSubevent2() : 0));
				rs.setYear((Year)DatabaseManager.loadEntity(Year.class, SwingUtils.getValue(jYear)));
				rs.setCity1((City)DatabaseManager.loadEntity(City.class, SwingUtils.getValue(jCity1)));
				rs.setComplex1((Complex)DatabaseManager.loadEntity(Complex.class, SwingUtils.getValue(jComplex1)));
				rs.setCity2((City)DatabaseManager.loadEntity(City.class, SwingUtils.getValue(jCity2)));
				rs.setComplex2((Complex)DatabaseManager.loadEntity(Complex.class, SwingUtils.getValue(jComplex2)));
				rs.setDate1(StringUtils.notEmpty(jDate1.getText()) ? jDate1.getText() : null);
				rs.setDate2(StringUtils.notEmpty(jDate2.getText()) ? jDate2.getText() : null);
				rs.setComment(StringUtils.notEmpty(jComment.getText()) ? jComment.getText() : null);
				rs.setExa(StringUtils.notEmpty(jExa.getText()) ? jExa.getText() : null);
				for (int i = 0 ; i < jRanks.length ; i++) {
					Integer id = SwingUtils.getValue(jRanks[i]);
					Result.class.getMethod("setIdRank" + (i + 1), Integer.class).invoke(rs, id > 0 ? id : null);
					Result.class.getMethod("setResult" + (i + 1), String.class).invoke(rs, StringUtils.notEmpty(jRes[i].getText()) ? jRes[i].getText() : null);
				}
				rs = (Result) DatabaseManager.saveEntity(rs, JMainFrame.getContributor());
				msg = "Result #" + rs.getId() + (isDraw() ? "/Draw #" /*+dr.getId()*/ : "") + " has been successfully " + (mode == EDIT ? "updated" : "created") + ".";
			}
			catch (Exception e_) {
				err = true;
				msg = e_.getMessage();
				log.log(Level.WARNING, e_.getMessage(), e_);
			}
			finally {
				parent.resultCallback(mode, getDataVector(rs), msg, err);
			}
		}
		this.setVisible(cmd.matches("draw|comment|extlinks|today|exacb.*"));
	}
	
	private Vector<Object> getDataVector(Result rs) {
		Vector<Object> v = new Vector<Object>();
		v.add(rs.getId());
		v.add(SwingUtils.getText(jYear));
		for (int i = 0 ; i < jRanks.length ; i++)
			v.add(SwingUtils.getText(jRanks[i]) + (StringUtils.notEmpty(jRes[i].getText()) ? " - " + jRes[i].getText() : ""));
		v.add(StringUtils.notEmpty(jDate1.getText()) ? jDate1.getText() : "");
		v.add(StringUtils.notEmpty(jDate2.getText()) ? jDate2.getText() : "");
		v.add(StringUtils.notEmpty(SwingUtils.getText(jComplex1)) ? SwingUtils.getText(jComplex1) : SwingUtils.getText(jCity1));
		v.add(StringUtils.notEmpty(SwingUtils.getText(jComplex2)) ? SwingUtils.getText(jComplex2) : SwingUtils.getText(jCity2));
		return v;
	}
	
	public void clear() {
		jYear.getPicklist().setSelectedIndex(-1);
		jComplex1.getPicklist().setSelectedIndex(-1);
		jCity1.getPicklist().setSelectedIndex(-1);
		jComplex2.getPicklist().setSelectedIndex(-1);
		jCity2.getPicklist().setSelectedIndex(-1);
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

	public JEntityPicklist getCity1() {
		return jCity1;
	}

	public JEntityPicklist getComplex1() {
		return jComplex1;
	}

	public JEntityPicklist getCity2() {
		return jCity2;
	}

	public JEntityPicklist getComplex2() {
		return jComplex2;
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

	public boolean isDraw() {
		return draw;
	}

	public void setDraw(boolean draw) {
		this.draw = draw;
	}

}