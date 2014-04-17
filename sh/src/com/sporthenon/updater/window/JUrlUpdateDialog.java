package com.sporthenon.updater.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.updater.component.JCustomButton;
import com.sporthenon.updater.component.JDialogButtonBar;
import com.sporthenon.utils.StringUtils;

public class JUrlUpdateDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextArea jResult = null;
	private JTextField jMax = null;
	private JCheckBox[] jEntity = null;
	private JLabel jStatus = null;
	
	public JUrlUpdateDialog(JFrame owner) {
		super(owner);
		initialize();
	}

	private void initialize() {
		JPanel jContentPane = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(700, 550));
		this.setSize(this.getPreferredSize());
		this.setModal(false);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setTitle("URL Update");
		this.setContentPane(jContentPane);

		BorderLayout layout = new BorderLayout();
		layout.setVgap(5);
		JDialogButtonBar jButtonBar = new JDialogButtonBar(this);
		jButtonBar.getCancel().setVisible(false);
		jContentPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2));
		jContentPane.setLayout(layout);
		jContentPane.add(getButtonPanel(), BorderLayout.NORTH);
		jResult = new JTextArea();
		JScrollPane jScrollPane = new JScrollPane(jResult);
		jContentPane.add(jScrollPane, BorderLayout.CENTER);
	}

	private JPanel getButtonPanel() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.setBorder(BorderFactory.createEtchedBorder());
		p.setPreferredSize(new Dimension(0, 70));
		
		jEntity = new JCheckBox[10];
		jEntity[0] = new JCheckBox("Athlete");
		jEntity[1] = new JCheckBox("Championship");
		jEntity[2] = new JCheckBox("City");
		jEntity[3] = new JCheckBox("Complex");
		jEntity[4] = new JCheckBox("Country");
		jEntity[5] = new JCheckBox("Event");
		jEntity[6] = new JCheckBox("Olympics");
		jEntity[7] = new JCheckBox("Sport");
		jEntity[8] = new JCheckBox("State");
		jEntity[9] = new JCheckBox("Team");
		for (JCheckBox cb : jEntity) {
			cb.setSelected(true);
			p.add(cb);
		}
		
		p.add(new JLabel("Max:"));
		jMax = new JTextField();
		jMax.setText("50");
		jMax.setPreferredSize(new Dimension(50, 20));
		p.add(jMax);
		
		JCustomButton jExecuteButton = new JCustomButton("Execute", "updater/ok.png", null);
		jExecuteButton.setActionCommand("execute");
		jExecuteButton.addActionListener(this);
		p.add(jExecuteButton);
		JCustomButton jCloseButton = new JCustomButton("Close", "updater/cancel.png", null);
		jCloseButton.setActionCommand("close");
		jCloseButton.addActionListener(this);
		p.add(jCloseButton);
		
		jStatus = new JLabel();
		p.add(jStatus);

		return p;
	}

	public void open() {
		jResult.setText("");
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equalsIgnoreCase("execute")) {
			new Thread (new Runnable() {
				public void run() {
					jStatus.setText("In progress...");
					executeUpdate();
					jStatus.setText("Finished.");
				}
			}).start();
		}
		else if (cmd.equalsIgnoreCase("close"))
			this.setVisible(false);
	}
	
	private void executeUpdate() {
		try {
			System.getProperties().setProperty("http.proxyHost" ,"globalproxy-emea.pharma.aventis.com");
			System.getProperties().setProperty("http.proxyPort","3129");
			int MAX = (StringUtils.notEmpty(jMax.getText()) ? Integer.parseInt(jMax.getText()) : Integer.MAX_VALUE);
			StringBuffer sbUpdateSql = new StringBuffer();
			List<String> lHql = new ArrayList<String>();
			lHql.add("from Athlete where (urlWiki is null or urlOlyref is null) order by id");
			lHql.add("from Championship where (urlWiki is null) order by id");
			lHql.add("from City where (urlWiki is null) order by id");
			lHql.add("from Complex where (urlWiki is null) order by id");
			lHql.add("from Country where (urlWiki is null or urlOlyref is null) order by id");
			lHql.add("from Event where (urlWiki is null) order by id");
			lHql.add("from Olympics where (urlWiki is null or urlOlyref is null) order by id");
			lHql.add("from Sport where (urlWiki is null or urlOlyref is null) order by id");
			lHql.add("from State where (urlWiki is null) order by id");
			lHql.add("from Team where (urlWiki is null) order by id");
			int i = 0;
			for (String hql : lHql) {
				if (!jEntity[i++].isSelected())
					continue;
				List<Object> l = DatabaseHelper.execute(hql);
				int n = 0;
				for (Object o : l) {
					n++;
					sbUpdateSql.append(getUrlUpdate(o));
					if (n > MAX)
						break;
				}
			}
			DatabaseHelper.executeUpdate(sbUpdateSql.toString());
		}
		catch (Exception e_) {
			Logger.getLogger("sh").error(e_.getMessage(), e_);
		}
	}

	private String getUrlUpdate(Object o) throws Exception {
		String sql = "";
		String str1 = "", str2 = "", str3 = "", table = "";
		if (o instanceof Athlete) {
			Athlete a = (Athlete) o;
			str1 = a.getFirstName() + " " + a.getLastName();
			str2 = a.getLastName();
			str3 = a.getSport().getWikiPattern();
			table = "\"PERSON\"";
		}
		else if (o instanceof Championship) {
			Championship c = (Championship) o;
			str1 = c.getLabel();
			table = "\"CHAMPIONSHIP\"";
		}
		else if (o instanceof City) {
			City c = (City) o;
			str1 = c.getLabel();
			str2 = c.getCountry().getLabel();
			table = "\"CITY\"";
		}
		else if (o instanceof Complex) {
			Complex c = (Complex) o;
			str1 = c.getLabel();
			table = "\"COMPLEX\"";
		}
		else if (o instanceof Country) {
			Country c = (Country) o;
			str1 = c.getLabel();
			str2 = c.getCode();
			table = "\"COUNTRY\"";
		}
		else if (o instanceof Event) {
			Event e = (Event) o;
			str1 = e.getLabel();
			table = "\"EVENT\"";
		}
		else if (o instanceof Olympics) {
			Olympics o_ = (Olympics) o;
			str1 = o_.getYear().getLabel() + " " + (o_.getType() == 0 ? "Winter" : "Summer") + " Olympics";
			str2 = (o_.getType() == 0 ? "Winter" : "Summer") + "/" + o_.getYear().getLabel() + "/";
			table = "\"OLYMPICS\"";
		}
		else if (o instanceof Sport) {
			Sport s = (Sport) o;
			str1 = s.getLabel();
			table = "\"SPORT\"";
		}
		else if (o instanceof State) {
			State s = (State) o;
			str1 = s.getLabel();
			table = "\"STATE\"";
		}
		else if (o instanceof Team) {
			Team t = (Team) o;
			str1 = t.getLabel();
			table = "\"TEAM\"";
		}
		Integer id = (Integer) o.getClass().getMethod("getId").invoke(o);
		// WIKIPEDIA
		String url = "http://en.wikipedia.org/wiki/" + str1.replaceAll("\\s", "_").replaceAll("'", "%27");
		URL url_ = new URL(url + "_(disambiguation)");
		HttpURLConnection conn = (HttpURLConnection) url_.openConnection();
		conn.setDoOutput(true);
		if (conn.getResponseCode() == 200) {
			StringWriter writer = new StringWriter();
			IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
			Document doc = Jsoup.parse(writer.toString());
			for (Element e : doc.getElementsByTag("a")) {
				if (e.attr("href") != null && e.attr("href").matches("^/wiki/.*")) {
					Element parent = e.parent();
					if (parent != null && parent.tagName().equalsIgnoreCase("b"))
						parent = parent.parent();
					boolean b = false;
					b |= (o instanceof Athlete && parent != null && parent.text().matches(".*(" + str3 + ").*"));
					b |= (o instanceof City && parent != null && parent.text().matches(".*(city|town|capital).*"));
					b |= (o instanceof Complex && parent != null && parent.text().matches(".*(stadium|arena|venue).*"));
					b |= (o instanceof Country && parent != null && parent.text().matches(".*(country).*"));
					b |= (o instanceof Sport && parent != null && parent.text().matches(".*(sport).*"));
					b |= (o instanceof State && parent != null && parent.text().matches(".*(state).*"));
					b |= (o instanceof Team && parent != null && parent.text().matches(".*(team).*"));
					if (b) {
						sql += "UPDATE " + table + " SET url_wiki='" + url.replaceAll("/wiki.+$", e.attr("href")) + "' WHERE id=" + id + ";\r\n";
						break;				
					}
				}
			}
		}
		else {
			url_ = new URL(url);
			conn = (HttpURLConnection) url_.openConnection();
			conn.setDoOutput(true);
			if (conn.getResponseCode() == 200)
				sql += "UPDATE " + table + " SET url_wiki='" + url + "' WHERE id=" + id + ";\r\n";
		}
		// OLYMPICS-REFERENCE
		url = null;
		if (o instanceof Athlete) {
			url = "http://www.sports-reference.com/olympics/athletes/" + str2.substring(0, 2) + "/" + str1.replaceAll("\\s", "-") + "-1.html";	
		}
		else if (o instanceof Country) {
			url = "http://www.sports-reference.com/olympics/countries/" + str2;
		}
		else if (o instanceof Olympics) {
			url = "http://www.sports-reference.com/olympics/" + str2;
		}
		if (url != null) {
			url_ = new URL(url);
			conn = (HttpURLConnection) url_.openConnection();
			conn.setDoOutput(true);
			if (conn.getResponseCode() == 200)
				sql += "UPDATE " + table + " SET url_olyref='" + url + "' WHERE id=" + id + ";\r\n";
		}
		jResult.setText(jResult.getText() + sql);
		return sql;
	}

}