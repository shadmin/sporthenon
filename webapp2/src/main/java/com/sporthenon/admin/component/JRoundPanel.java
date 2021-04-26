package com.sporthenon.admin.component;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.RoundType;

public class JRoundPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Container parent;
	private JEntityPicklist jRoundType;
	private JEntityPicklist[] jRanks = new JEntityPicklist[5];
	private JTextField[] jRes = new JTextField[5];
	private JTextField jDate1;
	private JTextField jDate2;
	private JEntityPicklist jComplex1;
	private JEntityPicklist jComplex2;
	private JEntityPicklist jCity1;
	private JEntityPicklist jCity2;
	private JTextField jExa;
	private JTextField jComment;
	
	public JRoundPanel(Container parent) {
		super();
		this.parent = parent;
		initialize();
	}

	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setHgap(1);
		flowLayout.setVgap(1);
		
		
		//this.setLayout(new GridLayout(1, 18, 0, 0));
		this.setMinimumSize(new Dimension(4000, 21));
		
		jRoundType = new JEntityPicklist(this, RoundType.alias);
		jRoundType.setMinimumSize(new Dimension(180, 21));
		
		for (int i = 0 ; i < jRanks.length ; i++) {
			jRanks[i] = new JEntityPicklist(this, "EN");
			jRanks[i].setMinimumSize(new Dimension(260, 21));
			jRes[i] = new JTextField();
			jRes[i].setMinimumSize(new Dimension(75, 21));
			parent.add(jRanks[i], null);
			parent.add(jRes[i], null);
		}

		jComplex1 = new JEntityPicklist(this, Complex.alias);
		jComplex1.setMinimumSize(new Dimension(300, 21));
		jComplex2 = new JEntityPicklist(this, Complex.alias);
		jComplex2.setMinimumSize(new Dimension(300, 21));
		jCity1 = new JEntityPicklist(this, City.alias);
		jCity1.setMinimumSize(new Dimension(220, 21));
		jCity2 = new JEntityPicklist(this, City.alias);
		jCity2.setMinimumSize(new Dimension(220, 21));
		jDate1 = new JTextField();
		jDate1.setMinimumSize(new Dimension(70, 21));
		jDate2 = new JTextField();
		jDate2.setMinimumSize(new Dimension(70, 21));
		jExa = new JTextField();
		jExa.setMinimumSize(new Dimension(50, 21));
		jComment = new JTextField();
		jComment.setMinimumSize(new Dimension(250, 21));
		
		parent.add(jDate1, null);
		parent.add(jDate2, null);
		parent.add(jComplex1, null);
		parent.add(jComplex2, null);
		parent.add(jCity1, null);
		parent.add(jCity2, null);
		parent.add(jExa, null);
		parent.add(jComment, null);
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

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}