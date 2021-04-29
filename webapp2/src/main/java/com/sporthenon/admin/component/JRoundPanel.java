package com.sporthenon.admin.component;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.RoundType;

public class JRoundPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Integer id = null;
	private JEntityPicklist jRoundType;
	private JEntityPicklist[] jRanks = new JEntityPicklist[5];
	private JTextField[] jRes = new JTextField[5];
	private JEntityPicklist jComplex1;
	private JEntityPicklist jComplex2;
	private JEntityPicklist jCity1;
	private JEntityPicklist jCity2;
	private JTextField jDate1;
	private JTextField jDate2;
	private JTextField jExa;
	private JTextField jComment;
	private ActionListener listener;
	
	public JRoundPanel(ActionListener listener) {
		super();
		this.listener = listener;
		initialize();
	}

	private void initialize() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		jRoundType = new JEntityPicklist(listener, RoundType.alias);
		jRoundType.setPreferredSize(new Dimension(180, 21));
		this.add(jRoundType, null);
		
		for (int i = 0 ; i < jRanks.length ; i++) {
			jRanks[i] = new JEntityPicklist(listener, "EN");
			jRanks[i].setPreferredSize(new Dimension(260, 21));
			jRes[i] = new JTextField();
			jRes[i].setPreferredSize(new Dimension(75, 21));
			this.add(jRanks[i], null);
			this.add(jRes[i], null);
		}

		jComplex1 = new JEntityPicklist(listener, Complex.alias);
		jComplex1.setPreferredSize(new Dimension(300, 21));
		jComplex2 = new JEntityPicklist(listener, Complex.alias);
		jComplex2.setPreferredSize(new Dimension(300, 21));
		jCity1 = new JEntityPicklist(listener, City.alias);
		jCity1.setPreferredSize(new Dimension(220, 21));
		jCity2 = new JEntityPicklist(listener, City.alias);
		jCity2.setPreferredSize(new Dimension(220, 21));
		jDate1 = new JTextField();
		jDate1.setPreferredSize(new Dimension(70, 21));
		jDate2 = new JTextField();
		jDate2.setPreferredSize(new Dimension(70, 21));
		jExa = new JTextField();
		jExa.setPreferredSize(new Dimension(50, 21));
		jComment = new JTextField();
		jComment.setPreferredSize(new Dimension(250, 21));
		
		this.add(jComplex1, null);
		this.add(jComplex2, null);
		this.add(jCity1, null);
		this.add(jCity2, null);
		this.add(jDate1, null);
		this.add(jDate2, null);
		this.add(jExa, null);
		this.add(jComment, null);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public JEntityPicklist getRoundType() {
		return jRoundType;
	}

	public JEntityPicklist[] getRanks() {
		return jRanks;
	}

	public JTextField[] getRes() {
		return jRes;
	}

	public JTextField getDate1() {
		return jDate1;
	}

	public JTextField getDate2() {
		return jDate2;
	}

	public JEntityPicklist getComplex1() {
		return jComplex1;
	}

	public JEntityPicklist getComplex2() {
		return jComplex2;
	}

	public JEntityPicklist getCity1() {
		return jCity1;
	}

	public JEntityPicklist getCity2() {
		return jCity2;
	}

	public JTextField getExa() {
		return jExa;
	}

	public JTextField getComment() {
		return jComment;
	}

}