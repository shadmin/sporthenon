package com.sporthenon.utils;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sporthenon.utils.res.ResourceUtils;

public class ExportUtils {
	
	protected static class MergedCell {
		private int row;
		private int cell;
		private int span;
		
		protected MergedCell(int row, int cell, int span) {
			this.row = row;
			this.cell = cell;
			this.span = span;
		}
		
		protected int getRow() {
			return row;
		}
		protected int getCell() {
			return cell;
		}
		protected int getSpan() {
			return span;
		}
		protected void setRow(int row) {
			this.row = row;
		}
		protected void setCell(int cell) {
			this.cell = cell;
		}
		protected void setSpan(int span) {
			this.span = span;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof MergedCell) {
				MergedCell mc = (MergedCell) o;
				return (mc.getRow() == getRow() && mc.getCell() == getCell());
			}
			return false;
		}

		@Override
		public String toString() {
			return "MergedCell [row=" + row + ", cell=" + cell + ", span=" + span + "]";
		}
	}

	public static void buildXLS(OutputStream out, String title, List<ArrayList<String>> lTh, List<ArrayList<String>> lTd, List<MergedCell> lMerge , boolean[] tBold) throws Exception {
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		sheet = hwb.createSheet(title != null ? title.replaceAll("\\[", "(").replaceAll("\\]", ")") : "Untitled");
		short rowIndex = 0;
		// Styles
		HSSFCellStyle headerStyle = hwb.createCellStyle();
		Font boldFont = hwb.createFont();
		boldFont.setFontName("Verdana");
		boldFont.setFontHeightInPoints((short)10);
		boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerStyle.setFont(boldFont);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerStyle.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		HSSFCellStyle normalStyle = hwb.createCellStyle();
		Font defaultFont = hwb.createFont();
		defaultFont.setFontName("Verdana");
		defaultFont.setFontHeightInPoints((short)10);
		defaultFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		normalStyle.setFont(defaultFont);
		normalStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		normalStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		normalStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		normalStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		normalStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		normalStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
		HSSFCellStyle boldStyle = hwb.createCellStyle();
		boldStyle.setFont(boldFont);
		boldStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		boldStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		boldStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		boldStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		boldStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		boldStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
		// Content
		ArrayList<Short> lBlankRow = new ArrayList<Short>();
		int cols = 0;
		int n = 0;
		for (List<String> l : lTd) {
			int i = 0;
			// TH
			if (l != null && l.size() == 1 && l.get(0).equalsIgnoreCase("--NEW--")) {
				if (rowIndex > 0) {
					lBlankRow.add(rowIndex);
					row = sheet.createRow(rowIndex++);
				}
				row = sheet.createRow(rowIndex++);
				if (n < lTh.size()) {
					for (String s : lTh.get(n)) {
						(cell = row.createCell(i++)).setCellValue(s.replaceAll("^\\#.*\\#", ""));
						cell.setCellStyle(headerStyle);
					}
					n++;
				}
				if (n > cols)
					cols = n;
			}
			// TD
			else {
				int n_ = 0;
				row = sheet.createRow(rowIndex++);
				for (String s : l) {
					boolean isCaption = (s != null && s.matches("^\\#CAPTION\\#.*"));
					boolean isAlignLeft = (s != null && s.matches(".*\\#ALIGN_LEFT\\#.*"));
					boolean isAlignCenter = (s != null && s.matches(".*\\#ALIGN_CENTER\\#.*"));
					boolean isAlignRight = (s != null && s.matches(".*\\#ALIGN_RIGHT\\#.*"));
					(cell = row.createCell(i++)).setCellValue(s.replaceAll("^\\#.*\\#", ""));
					HSSFCellStyle st = (l.size() == 1 ? headerStyle : (tBold != null && tBold.length > i - 1 && tBold[i - 1] ? boldStyle : normalStyle));
					if (isAlignLeft)
						st.setAlignment(HSSFCellStyle.ALIGN_LEFT);
					else if (isAlignCenter)
						st.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					else if (isAlignRight)
						st.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(st);
					if (l.size() == 1) {
						(cell = row.createCell(i++)).setCellValue("");
						cell.setCellStyle(headerStyle);
					}
					if (isCaption)
						cell.setCellStyle(headerStyle);
					n_++;
				}
				if (n_ > cols)
					cols = n_;
			}
		}
		// Merging
		if (lMerge != null) {
			for (MergedCell mc : lMerge) {
				int offset = 0;
				for (Short sh : lBlankRow)
					if (mc.getRow() + offset >= sh)
						offset++;
				sheet.addMergedRegion(new CellRangeAddress(mc.getRow() + offset, mc.getRow() + offset, mc.getCell(), mc.getCell() + mc.getSpan() - 1));
			}
		}
		// Auto-Sizing
		for (int j = 0 ; j < cols ; j++)
			sheet.autoSizeColumn(j);
		hwb.write(out);
	}
	
	private static void buildCSV(PrintWriter pw, List<ArrayList<String>> lTh, List<ArrayList<String>> lTd) throws Exception {
		StringBuffer sbCSV = new StringBuffer();
		int n = 0;
		for (List<String> l : lTd) {
			// HEADER
			if (l != null && l.size() == 1 && l.get(0).equalsIgnoreCase("--NEW--")) {
				ArrayList<String> lTh_ = lTh.get(n++);
				for (int i = 0 ; i < lTh_.size() ; i++) {
					String s = lTh_.get(i);
					sbCSV.append(i > 0 ? "," : "").append(s.replaceAll("^\\#.*\\#", ""));
				}
				sbCSV.append("\r\n");
			}
			// INFO
			else if (l != null && !l.isEmpty() && l.get(0).matches("^\\#CAPTION\\#.*")) {
				String s1 = l.get(0).replaceAll("^\\#.*\\#", "");
				String s2 = l.get(1).replaceAll("^\\#.*\\#", "");
				sbCSV.append(s1).append(",").append(s2).append("\r\n");
			}
			// TABLE
			else {
				for (int i = 0 ; i < l.size() ; i++) {
					if (l.get(i) != null) {
						String s = l.get(i).replaceAll("^\\#.*\\#", "");
						sbCSV.append(i > 0 ? "," : "").append(s.contains(",") ? "\"" + s + "\"" : s);
					}
				}
				sbCSV.append("\r\n");
			}
		}
		pw.write(sbCSV.toString());
	}
	
	private static void buildText(PrintWriter pw, List<ArrayList<String>> lTh, List<ArrayList<String>> lTd) throws Exception {
		StringBuffer sbText = new StringBuffer();
		int n = 0;
		int[] tMaxLength = null;
		StringBuffer sbSep = null;
		for (List<String> l : lTd) {
			// HEADER
			if (l != null && l.size() > 1 && l.get(0).equalsIgnoreCase("--INFO--")) {
				
			}
			else if (l != null && l.size() == 1 && l.get(0).equalsIgnoreCase("--NEW--")) {
				if (sbSep != null)
					sbText.append("\r\n").append(sbSep).append("\r\n\r\n");
				ArrayList<String> lTh_ = lTh.get(n++);
				tMaxLength = new int[lTh_.size()];
				for (int i = 0 ; i < lTh_.size() ; i++)
					tMaxLength[i] = lTh_.get(i).replaceAll("^\\#.*\\#", "").length();
				for (List<String> l_ : lTd) {
					for (int i = 0 ; i < l_.size() ; i++) {
						String s = l_.get(i).replaceAll("^\\#.*\\#", "");
						if (i < tMaxLength.length && s.length() > tMaxLength[i])
							tMaxLength[i] = s.length() + 1;
						if (l_ != null && l_.size() == 1 && l_.get(0).equalsIgnoreCase("--NEW--"))
							break;
					}
				}
				sbSep = new StringBuffer("+");
				for (int i = 0 ; i < tMaxLength.length ; i++) {
					for (int j = 0 ; j < tMaxLength[i] ; j++)
						sbSep.append("-");
					sbSep.append("+");
				}
				sbText.append(sbSep).append("\r\n|");
				for (int i = 0 ; i < lTh_.size() ; i++) {
					String s = lTh_.get(i).replaceAll("^\\#.*\\#", "");
					sbText.append(s);
					for (int j = s.length() ; j < tMaxLength[i] ; j++)
						sbText.append(" ");
					sbText.append("|");
				}
				sbText.append("\r\n").append(sbSep);
			}
			// INFO
			else if (l != null && !l.isEmpty() && l.get(0).matches("^\\#CAPTION\\#.*")) {
				String s1 = l.get(0).replaceAll("^\\#.*\\#", "");
				String s2 = l.get(1).replaceAll("^\\#.*\\#", "");
				sbText.append(s1).append(" : ").append(s2).append("\r\n\r\n");
			}
			// TABLE
			else {
				sbText.append("\r\n|");
				for (int i = 0 ; i < l.size() ; i++) {
					if (l.get(i) != null) {
						String s = l.get(i).replaceAll("^\\#.*\\#", "");
						sbText.append(s);
						for (int j = s.length() ; j < tMaxLength[i] ; j++)
							sbText.append(" ");
						sbText.append("|");
					}
				}
			}
		}
		if (sbSep != null)
			sbText.append("\r\n").append(sbSep);
		pw.write(sbText.toString());
	}
	
	public static String buildHTML(Document doc) throws Exception {
		String html = doc.toString();
		html = html.replaceAll("<head>", "<head>\r\n<style>*{font:12px Verdana;}</style>\r\n<link rel='stylesheet' type='text/css' href='" + ConfigUtils.getProperty("url") + "css/sh.css'/>\r\n<link rel='stylesheet' type='text/css' href='" + ConfigUtils.getProperty("url") + "css/render.css'/>\r\n");
		html = html.replaceAll("<body>", "<body class='print'><div id='content'><div class='tc'>");
		html = html.replaceAll("<\\/body>", "</div></div></body>");
		html = html.replaceAll("/img/", ConfigUtils.getProperty("url") + "img/");
		html = html.replaceAll("\\</?a.*?>|\\sclass=\"srt\"|onclick\\=\".*?\"", "");
		html = html.replaceAll("class\\=\"toolbar\"", "class=\"toolbar\" style=\"display:none;\"");
		return html;
	}
	
	public static void buildPDF(OutputStream output, List lTh, List lTd, List lMerge, String lang) throws Exception {
		PdfPCell cell = null;
		com.itextpdf.text.Document doc = new com.itextpdf.text.Document(PageSize.A4.rotate());
		doc.setMargins(20.0f, 20.0f, 20.0f, 20.0f);
		PdfWriter.getInstance(doc, output);
		doc.open();
		
		BaseFont bf = BaseFont.createFont(ConfigUtils.getProperty("font.folder") + "verdana.ttf", BaseFont.IDENTITY_H, true);
		com.itextpdf.text.Font font = new com.itextpdf.text.Font(bf, 9);
		com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 9);
		fontBold.setStyle(com.itextpdf.text.Font.BOLD);
		BaseColor thcolor = new BaseColor(255, 255, 220);
		Float padding = 5.0f;
		PdfPTable t = new PdfPTable(new float[] { 1.0f });
		t.setWidthPercentage(100.0f);

		PdfPTable table = null;
		int n = 0;
		HashMap<String, Float> hWidth = new HashMap<String, Float>();
		hWidth.put("", 0.5f);
		hWidth.put(ResourceUtils.getText("entity.EV.1", lang), 0.6f);
		hWidth.put(ResourceUtils.getText("entity.SP.1", lang), 0.3f);
		hWidth.put(ResourceUtils.getText("entity.RS.1", lang), 0.4f);
		hWidth.put(ResourceUtils.getText("entity.YR.1", lang), 0.25f);
		hWidth.put(ResourceUtils.getText("result.detail", lang), 1.0f);
		hWidth.put(ResourceUtils.getText("score", lang), 0.7f);
		hWidth.put(ResourceUtils.getText("date", lang), 0.5f);
		hWidth.put(ResourceUtils.getText("place", lang), 1.4f);
		for (List<String> l : (List<List<String>>) lTd) {
			if (l != null && l.size() > 1 && l.get(0).equalsIgnoreCase("--INFO--")) {
				table = new PdfPTable(2);
				table.setWidthPercentage(50.0f);
				for (int i = 1 ; i < l.size() ; i++) {
					cell = new PdfPCell(new Phrase(l.get(i).replaceAll("^\\#.*\\#", ""), i % 2 == 1 ? fontBold : font));
					cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
					cell.setPadding(padding);
					if (i % 2 == 1) {
						cell.setBackgroundColor(new BaseColor(255, 255, 220));
						cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
					}
					table.addCell(cell);
				}
				doc.add(table);
				doc.add(new Phrase(" ", font));
				table = null;
			}
			else if (l != null && l.size() == 1 && l.get(0).equalsIgnoreCase("--NEW--")) {
				if (table != null) {
					PdfPCell c = new PdfPCell(table);
					c.setBorderWidth(2);
					t.addCell(c);
				}
				if (n < lTh.size()) {
					List<String> l_ = (List<String>) lTh.get(n);
					float[] tf = new float[l_.size()];
					for (int i = 0 ; i < l_.size() ; i++)
						tf[i] = (hWidth.containsKey(l_.get(i)) ? hWidth.get(l_.get(i).replaceAll("^\\#.*\\#", "")) : 0.5f);
					table = new PdfPTable(tf);
					table.setWidthPercentage(100.0f);
					for (int i = 0 ; i < l_.size() ; i++) {
						if (i == 0 && l_.size() > 1 && l_.get(0).startsWith("#")) {
							cell = new PdfPCell(new Phrase(l_.get(0).replaceAll("^\\#.*\\#", ""), fontBold));
							cell.setBackgroundColor(thcolor);
							cell.setPadding(padding);
							cell.setColspan(l_.size());
							cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
							table.addCell(cell);
						}
						String s = l_.get(i).replaceAll("^\\#.*\\#", "");
						int mcindex = lMerge.indexOf(new MergedCell(n, i, 0));
						cell = new PdfPCell(new Phrase(s, fontBold));
						cell.setBackgroundColor(thcolor);
						cell.setPadding(padding);
						if (mcindex > -1) {
							int span = ((MergedCell)lMerge.get(mcindex)).getSpan();
							cell.setColspan(span);
							i += (span - 1);
						}
						else
							cell.setColspan(1);
						cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
						table.addCell(cell);
					}
					n++;
				}
			}
			else {
				for (String s : l) {
					String txt = s.replaceAll("^\\#.+\\#", "");
					cell = new PdfPCell(new Phrase(txt, font));
					cell.setPadding(2.0f);
					cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
					PdfPTable table_ = null;
					if (s.matches("^\\#.+\\.png\\#.+")) {
						String src = s.substring(1, s.lastIndexOf("#"));
						if (!src.contains("details.png")) {
							Image img = Image.getInstance(src);
							PdfPCell cell_ = new PdfPCell(img);
							cell_.setPadding(padding);
							cell_.setBorder(0);
							cell_.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
							cell.setBorder(0);
							table_ = new PdfPTable(txt.length() > 3 ? new float[]{0.15f, 1.0f} : new float[]{0.5f, 0.5f});
							table_.addCell(cell_);
							table_.addCell(cell);
						}
					}
					if (table_ != null)
						table.addCell(table_);
					else
						table.addCell(cell);
				}
			}
		}
		PdfPCell c = new PdfPCell(table);
		c.setBorderWidth(2);
		t.addCell(c);
		doc.add(t);
		if (doc.isOpen())
			doc.close();
	}

	public static void parseHTML(Document doc, List lTh, List lTd, List lMerge) throws Exception {
//		Element title = doc.getElementsByAttributeValue("class", "title").first();
		int row = 0;
		
		// INFO
		Elements tinfo = doc.getElementsByClass("info");
		if (tinfo != null && !tinfo.isEmpty()) {
			ArrayList<String> lTd_ = new ArrayList<String>();
			lTd_.add("--INFO--");
			Element info = tinfo.get(0);
			for (Element tr : info.getElementsByTag("tr")) {
				List<Element> lCaption = tr.getElementsByClass("caption");
				if (lCaption != null && !lCaption.isEmpty()) {
					Element th = lCaption.get(0);
					Element td = tr.getElementsByTag("td").get(0);
					if (td.className() == null || !td.className().matches("^(logo|flag|otherflags|otherlogos|record|extlinks).*")) {
						lTd_.add("#CAPTION#" + th.text());
						lTd_.add("#ALIGN_LEFT#" + td.text());
						row++;
					}
				}
			}
			lTd.add(lTd_);
		}
		// TABLE
		Elements tsorts = doc.getElementsByClass("tsort");
		for (Element table : tsorts) {
			ArrayList<String> lTd_ = new ArrayList<String>();
			lTd_.add("--NEW--");
			lTd.add(lTd_);
			Element thead = table.getElementsByTag("thead").get(0);
			Element tbody = thead.nextElementSibling();
			Elements htr = thead.getElementsByTag("tr");
			Element th1 = htr.first().getElementsByTag("th").get(0);
			Element th = (htr.size() > 1 ? htr.get(1) : htr.first()).getElementsByTag("th").get(0);
			int cell = 0;
			ArrayList<String> lTh_ = new ArrayList<String>();
			while(th != null) {
				Integer span = (StringUtils.notEmpty(th.attr("colspan")) ? new Integer(th.attr("colspan")) : 1);
				lTh_.add((th1 != null && th1.nextElementSibling() == null && lTh_.isEmpty() ? "#" + th1.text() + "#" : "") + th.text());
				if (span > 1) {
					lMerge.add(new MergedCell(row, cell, span));
					cell += span;
					span--;
					while (span > 0) {
						lTh_.add("");
						span--;
					}
				}
				else
					cell++;
				th = th.nextElementSibling();
			}
			row++;
			lTh.add(lTh_);
			Element tr = tbody.getElementsByTag("tr").get(0);
			while(tr != null) {
				lTd_ = new ArrayList<String>();
				Element td = tr.getElementsByTag("td").get(0);
				cell = 0;
				while (td != null) {
					Integer span = (StringUtils.notEmpty(td.attr("colspan")) ? new Integer(td.attr("colspan")) : 1);
					String title_ = td.attr("title");
					if (StringUtils.notEmpty(title_))
						lTd_.add(title_);
					else {
						String img = "";
						Elements imgs = td.getElementsByTag("img");
						if (!imgs.isEmpty())
							img = "#" + imgs.get(0).attr("src") + "#";
						lTd_.add(img + td.text());
					}
					if (span > 1) {
						lMerge.add(new MergedCell(row, cell, span));
						cell += span;
						span--;
						while (span > 0) {
							lTd_.add("");
							span--;
						}
					}
					else
						cell++;
					td = td.nextElementSibling();
				}
				lTd.add(lTd_);
				tr = tr.nextElementSibling();
				row++;
			}
		}
	}

	public static void export(HttpServletResponse response, StringBuffer html, String format, String lang) throws Exception {
		try {
			String html_ = html.toString();
			if (format.matches("csv|xls|txt"))
				html_ = html_.replaceAll("&nbsp;", " ").replaceAll("<br/>", "&nbsp;/&nbsp;");
			Document doc = Jsoup.parse(html_);
			Element elTitle = doc.getElementsByAttributeValue("class", "title").first();
			String title = elTitle.text().replaceAll("\\,\\s", "_");
			List lTh = new ArrayList<ArrayList<String>>();
			List lTd = new ArrayList<ArrayList<String>>();
			List lMerge = new ArrayList<MergedCell>();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + title + " [Sporthenon]." + format);
			if (format.equalsIgnoreCase("html")) {
				response.setContentType("text/html");
				response.getWriter().write(buildHTML(doc));
			}
			else if (format.equalsIgnoreCase("csv")) {
				response.setContentType("text/csv");
				parseHTML(doc, lTh, lTd, lMerge);
				buildCSV(response.getWriter(), lTh, lTd);
			}	
			else if (format.equalsIgnoreCase("xls")) {
				response.setContentType("application/vnd.ms-excel");
				parseHTML(doc, lTh, lTd, lMerge);
				buildXLS(response.getOutputStream(), null, lTh, lTd, lMerge, new boolean[]{false});
			}
			else if (format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				parseHTML(doc, lTh, lTd, lMerge);
				buildPDF(response.getOutputStream(), lTh, lTd, lMerge, lang);
			}
			else if (format.equalsIgnoreCase("txt")) {
				response.setContentType("text/plain");
				parseHTML(doc, lTh, lTd, lMerge);
				buildText(response.getWriter(), lTh, lTd);
			}
		}
		finally {
			//response.getOutputStream().close();
		}
	}
	
}