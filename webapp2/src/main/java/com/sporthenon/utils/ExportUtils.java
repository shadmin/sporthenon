package com.sporthenon.utils;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
		private int cspan;
		private int rspan;
		
		protected MergedCell(int row, int cell, int cspan, int rspan) {
			this.row = row;
			this.cell = cell;
			this.cspan = cspan;
			this.rspan = rspan;
		}
		
		protected int getRow() {
			return row;
		}
		protected int getCell() {
			return cell;
		}
		protected int getCspan() {
			return cspan;
		}
		protected void setRow(int row) {
			this.row = row;
		}
		protected void setCell(int cell) {
			this.cell = cell;
		}
		protected void setCspan(int cspan) {
			this.cspan = cspan;
		}
		public int getRspan() {
			return rspan;
		}
		public void setRspan(int rspan) {
			this.rspan = rspan;
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
			return "MergedCell [row=" + row + ", cell=" + cell + ", cspan=" + cspan + ", rspan=" + rspan + "]";
		}
	}

	public static void buildXLS(OutputStream out, String title, List<List<String>> lTh, List<List<String>> lTd, List<MergedCell> lMerge , boolean[] tBold) throws Exception {
		try(HSSFWorkbook hwb = new HSSFWorkbook();) {
			HSSFSheet sheet = null;
			HSSFRow row = null;
			HSSFCell cell = null;
			sheet = hwb.createSheet(title != null ? title.replaceAll("\\[", "(").replaceAll("\\]", ")") : "Untitled");
			short rowIndex = 0;
			// Styles
			HSSFCellStyle normalStyle = hwb.createCellStyle();
			Font defaultFont = hwb.createFont();
			defaultFont.setFontName("Verdana");
			defaultFont.setFontHeightInPoints((short)10);
			defaultFont.setBold(false);
			normalStyle.setFont(defaultFont);
			normalStyle.setAlignment(HorizontalAlignment.LEFT);
			normalStyle.setVerticalAlignment(VerticalAlignment.TOP);
			normalStyle.setBorderBottom(BorderStyle.THIN);
			normalStyle.setBorderTop(BorderStyle.THIN);
			normalStyle.setBorderRight(BorderStyle.THIN);
			normalStyle.setBorderLeft(BorderStyle.THIN);
			normalStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
			
			HSSFCellStyle headerStyle = hwb.createCellStyle();
			headerStyle.cloneStyleFrom(normalStyle);
			Font boldFont = hwb.createFont();
			boldFont.setFontName("Verdana");
			boldFont.setFontHeightInPoints((short)10);
			boldFont.setBold(true);
			headerStyle.setFont(boldFont);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setVerticalAlignment(VerticalAlignment.TOP);
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setFillForegroundColor(HSSFColorPredefined.LIGHT_YELLOW.getIndex());
			
			HSSFCellStyle boldStyle = hwb.createCellStyle();
			boldStyle.cloneStyleFrom(normalStyle);
			boldStyle.setFont(boldFont);
			
			HSSFCellStyle blueStyle = hwb.createCellStyle();
			blueStyle.cloneStyleFrom(normalStyle);
			blueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			blueStyle.setFillForegroundColor(HSSFColorPredefined.LIGHT_BLUE.getIndex());
			
			HSSFCellStyle greenStyle = hwb.createCellStyle();
			greenStyle.cloneStyleFrom(normalStyle);
			greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			greenStyle.setFillForegroundColor(HSSFColorPredefined.LIGHT_GREEN.getIndex());
			
			HSSFCellStyle orangeStyle = hwb.createCellStyle();
			orangeStyle.cloneStyleFrom(normalStyle);
			orangeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			orangeStyle.setFillForegroundColor(HSSFColorPredefined.LIGHT_ORANGE.getIndex());
			
			// Content
			ArrayList<Short> lBlankRow = new ArrayList<Short>();
			int cols = 0;
			int n = 0;
			for (List<String> l : lTd) {
				int i = 0;
				// HEADER (ref)
				if (l != null && l.size() > 1 && l.get(0).equalsIgnoreCase("--INFO--")) {
					for (int j = 1 ; j < l.size() ; j++) {
						if (l.get(j).startsWith("#TITLENAME#")) {
							row = sheet.createRow(rowIndex++);
							(cell = row.createCell(0)).setCellValue(l.get(j).replaceAll("^\\#.*\\#", ""));
							cell.setCellStyle(headerStyle);
							lMerge.add(new MergedCell(0, 0, 2, 1));
						}
						else {
							if (j % 2 == 0) {
								row = sheet.createRow(rowIndex++);
							}
							(cell = row.createCell(j % 2)).setCellValue(l.get(j).replaceAll("^\\#.*\\#", ""));
							cell.setCellStyle(j % 2 == 0 ? headerStyle : normalStyle);
						}
					}
				}
				// TH
				else if (l != null && l.size() == 1 && l.get(0).equalsIgnoreCase("--NEW--")) {
					if (rowIndex > 0) {
						lBlankRow.add(rowIndex);
						row = sheet.createRow(rowIndex++);
					}
					row = sheet.createRow(rowIndex++);
					if (n < lTh.size()) {
						List<String> lTh_ = lTh.get(n);
						if (lTh_ != null && !lTh_.isEmpty() && lTh_.get(0).equalsIgnoreCase("--TTEXT--")) {
							row = sheet.createRow(rowIndex++);
							(cell = row.createCell(i)).setCellValue(lTh_.get(1));
							cell.setCellStyle(headerStyle);
							lTh_.remove(0);
							lTh_.remove(0);
							if (!lTh_.isEmpty()) {
								row = sheet.createRow(rowIndex++);
							}
						}					
						for (String s : lTh.get(n)) {
							(cell = row.createCell(i++)).setCellValue(s.replaceAll("^\\#.*\\#", ""));
							cell.setCellStyle(headerStyle);
						}
						n++;
					}
					if (n > cols) {
						cols = n;
					}
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
						if (s.matches(".*\\#color\\-.+\\#.*")) {
							String color = s.substring(1).split("\\#")[0].replaceFirst("color\\-", "");
							st = (color.equalsIgnoreCase("blue") ? blueStyle : (color.equalsIgnoreCase("green") ? greenStyle : orangeStyle));
						}
						if (isAlignLeft) {
							st.setAlignment(HorizontalAlignment.LEFT);
						}
						else if (isAlignCenter) {
							st.setAlignment(HorizontalAlignment.CENTER);
						}
						else if (isAlignRight) {
							st.setAlignment(HorizontalAlignment.RIGHT);
						}
						cell.setCellStyle(st);
						if (l.size() == 1) {
							(cell = row.createCell(i++)).setCellValue("");
							cell.setCellStyle(headerStyle);
						}
						if (isCaption) {
							cell.setCellStyle(headerStyle);
						}
						n_++;
					}
					if (n_ > cols) {
						cols = n_;
					}
				}
			}
			// Merging
			if (lMerge != null) {
				for (MergedCell mc : lMerge) {
					int offset = 0;
					for (Short sh : lBlankRow) {
						if (mc.getRow() + offset >= sh) {
							offset++;
						}
					}
					try {
						sheet.addMergedRegion(new CellRangeAddress(mc.getRow() + offset, mc.getRow() + mc.getRspan() - 1 + offset, mc.getCell(), mc.getCell() + mc.getCspan() - 1));	
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			// Auto-Sizing
			for (int j = 0 ; j < cols ; j++) {
				sheet.autoSizeColumn(j);
			}
			hwb.write(out);
		}
	}
	
	private static void buildCSV(PrintWriter pw, List<List<String>> lTh, List<List<String>> lTd) throws Exception {
		final String SEPARATOR = ";";
		StringBuffer sbCSV = new StringBuffer();
		int n = 0;
		for (List<String> l : lTd) {
			// HEADER (ref)
			if (l != null && l.size() > 1 && l.get(0).equalsIgnoreCase("--INFO--")) {
				for (int j = 1 ; j < l.size() ; j++) {
					if (l.get(j).startsWith("#TITLENAME#"))
						sbCSV.append(l.get(j).replaceAll("^\\#.*\\#", ""));
					else {
						if (j % 2 == 0)
							sbCSV.append("\r\n");
						sbCSV.append(j % 2 == 1 ? SEPARATOR : "").append(l.get(j).replaceAll("^\\#.*\\#", ""));
					}
				}
				sbCSV.append("\r\n");
			}
			// HEADER
			else if (l != null && l.size() == 1 && l.get(0).equalsIgnoreCase("--NEW--")) {
				List<String> lTh_ = lTh.get(n++);
				if (lTh_ != null && !lTh_.isEmpty() && lTh_.get(0).equalsIgnoreCase("--TTEXT--")) {
					sbCSV.append("[" + lTh_.get(1) + "]").append("\r\n");
					lTh_.remove(0);
					lTh_.remove(0);
				}
				for (int i = 0 ; i < lTh_.size() ; i++) {
					String s = lTh_.get(i);
					sbCSV.append(i > 0 ? SEPARATOR : "").append(s.replaceAll("^\\#.*\\#", ""));
				}
				if (!lTh_.isEmpty())
					sbCSV.append("\r\n");
			}
			// INFO
			else if (l != null && !l.isEmpty() && l.get(0).matches("^\\#CAPTION\\#.*")) {
				String s1 = l.get(0).replaceAll("^\\#.*\\#", "");
				String s2 = l.get(1).replaceAll("^\\#.*\\#", "");
				sbCSV.append(s1).append(SEPARATOR).append(s2).append("\r\n");
			}
			// TABLE
			else {
				for (int i = 0 ; i < l.size() ; i++) {
					if (l.get(i) != null) {
						String s = l.get(i).replaceAll("^\\#.*\\#", "");
						sbCSV.append(i > 0 ? SEPARATOR : "").append(s.contains(SEPARATOR) ? "\"" + s + "\"" : s);
					}
				}
				sbCSV.append("\r\n");
			}
		}
		pw.write(sbCSV.toString());
	}
	
	private static void buildText(PrintWriter pw, List<List<String>> lTh, List<List<String>> lTd) throws Exception {
		StringBuffer sbText = new StringBuffer();
		int index = 0;
		int nth = 0;
		int[] tMaxLength = null;
		StringBuffer sbSep = null;
		for (List<String> l : lTd) {
			// HEADER (ref)
			if (l != null && l.size() > 1 && l.get(0).equalsIgnoreCase("--INFO--")) {
				String titleName = "";
				List<String> l1 = new ArrayList<String>();
				List<String> l2 = new ArrayList<String>();
				for (int j = 1 ; j < l.size() ; j++) {
					String s = l.get(j).replaceAll("^\\#.*\\#", "");
					if (l.get(j).startsWith("#TITLENAME#")) {
						titleName = s;
					}
					else if (j % 2 == 0) {
						l1.add(s);
					}
					else {
						l2.add(s);
					}
				}
				int maxlength = (titleName != null ? titleName.length() : 0);
				for (int i = 0 ; i < l1.size() ; i++) {
					int n_ = l1.get(i).length() + l2.get(i).length() + 2;
					if (n_ > maxlength) {
						maxlength = n_;
					}
				}
				sbSep = new StringBuffer("+");
				for (int i = 0 ; i < maxlength + 2 ; i++) {
					sbSep.append("-");
				}
				sbSep.append("+").append("\r\n");
				sbText.append(sbSep).append("| ").append(titleName);
				for (int i = titleName.length() + 1 ; i < maxlength + 2 ; i++) {
					sbText.append(" ");
				}
				sbText.append("|").append("\r\n").append(sbSep);
				for (int i = 0 ; i < l1.size() ; i++) {
					int n_ = l1.get(i).length() + l2.get(i).length() + 2;
					sbText.append("| ").append(l1.get(i)).append(": ").append(l2.get(i));
					for (int j = n_ + 1 ; j < maxlength + 2 ; j++) {
						sbText.append(" ");
					}
					sbText.append("|").append("\r\n");
				}
				sbText.append(sbSep).append("\r\n");
				sbSep = null;
			}
			else if (l != null && l.size() == 1 && l.get(0).equalsIgnoreCase("--NEW--")) {
				if (sbSep != null) {
					sbText.append("\r\n").append(sbSep).append("\r\n\r\n");
				}
				List<String> lTh_ = lTh.get(nth++);
				if (lTh_ != null && !lTh_.isEmpty() && lTh_.get(0).equalsIgnoreCase("--TTEXT--")) {
					sbText.append("[" + lTh_.get(1) + "]");
					lTh_.remove(0);
					lTh_.remove(0);
					if (!lTh_.isEmpty()) {
						sbText.append("\r\n");
					}
				}
				tMaxLength = new int[!lTh_.isEmpty() ? lTh_.size() : 2];
				for (int i = 0 ; i < lTh_.size() ; i++) {
					tMaxLength[i] = lTh_.get(i).replaceAll("^\\#.*\\#", "").length();
				}
				for (int i = index + 1 ; i < lTd.size() ; i++) {
					List<String> l_ = lTd.get(i);
					if (l_ != null && l_.size() == 1 && l_.get(0).equalsIgnoreCase("--NEW--")) {
						break;
					}
					for (int j = 0 ; j < l_.size() ; j++) {
						String s = l_.get(j).replaceAll("^\\#.*\\#", "");
						if (j < tMaxLength.length && s.length() > tMaxLength[j]) {
							tMaxLength[j] = s.length() + 1;
						}
					}
				}
				sbSep = new StringBuffer("+");
				for (int i = 0 ; i < tMaxLength.length ; i++) {
					for (int j = 0 ; j < tMaxLength[i] ; j++) {
						sbSep.append("-");
					}
					sbSep.append("+");
				}
				if (!lTh_.isEmpty()) {
					sbText.append(sbSep).append("\r\n|");
				}
				for (int i = 0 ; i < lTh_.size() ; i++) {
					String s = lTh_.get(i).replaceAll("^\\#.*\\#", "");
					sbText.append(s);
					for (int j = s.length() ; j < tMaxLength[i] ; j++) {
						sbText.append(" ");
					}
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
						if (tMaxLength.length > i) {
							for (int j = s.length() ; j < tMaxLength[i] ; j++) {
								sbText.append(" ");
							}
						}
						sbText.append("|");
					}
				}
			}
			index++;
		}
		if (sbSep != null) {
			sbText.append("\r\n").append(sbSep);
		}
		pw.write(sbText.toString());
	}
	
	public static String buildHTML(Document doc) throws Exception {
		String html = doc.toString();
		html = html.replace("<head>", "<head>\r\n<style>*{font:12px Verdana;}</style>\r\n<link rel='stylesheet' type='text/css' href='" + ConfigUtils.getProperty("url") + "css/sh.css'/>\r\n<link rel='stylesheet' type='text/css' href='" + ConfigUtils.getProperty("url") + "css/render.css'/>\r\n");
		html = html.replace("<body>", "<body class='print render' style='background:#FFF;'><div id='content'><div class='tc'>");
		html = html.replace("</body>", "</div></div></body>");
		html = html.replace("/img/", ConfigUtils.getProperty("url") + "img/");
		html = html.replaceAll("\\</?a.*?>|\\sclass=\"srt\"|onclick\\=\".*?\"", "");
		html = html.replace("id=\"topbar\"", "id=\"topbar\" style=\"display:none;\"");
		html = html.replace("alt=\"fav\"", "alt=\"fav\" style=\"display:none;\"");
		html = html.replace("alt=\"details\"", "alt=\"details\" style=\"display:none;\"");
		html = html.replace("alt=\"note\"", "alt=\"note\" style=\"display:none;\"");
		html = html.replace("alt=\"toggle\"", "alt=\"toggle\" style=\"display:none;\"");
		html = html.replace("class=\"rendertip\" style=\"display:none;\"", "class=\"rendertip\"");
		html = html.replace("class=\"tsort\"", "class=\"tsort\" style=\"clear:both;\"");
		html = html.replace("id=\"winrecmore\"", "style=\"display:none;\"");
		html = html.replace("class=\"hidden\"", "");
		return html;
	}
	
	public static void buildPDF(OutputStream output, List<List<String>> lTh, List<List<String>> lTd, List<MergedCell> lMerge, String lang) throws Exception {
		PdfPCell cell = null;
		com.itextpdf.text.Document doc = new com.itextpdf.text.Document(PageSize.A4.rotate());
		doc.setMargins(20.0f, 20.0f, 20.0f, 20.0f);
		PdfWriter.getInstance(doc, output);
		doc.open();
		
		BaseFont bf = BaseFont.createFont();
		com.itextpdf.text.Font font = new com.itextpdf.text.Font(bf, 8);
		com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 8);
		fontBold.setStyle(com.itextpdf.text.Font.BOLD);
		BaseColor thcolor = BaseColor.LIGHT_GRAY;
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
		hWidth.put(ResourceUtils.getText("entity.YR.1", lang), 0.3f);
		hWidth.put(ResourceUtils.getText("result.detail", lang), 1.0f);
		hWidth.put(ResourceUtils.getText("score", lang), 0.7f);
		hWidth.put(ResourceUtils.getText("date", lang), 0.5f);
		hWidth.put(ResourceUtils.getText("place", lang), 1.5f);
		int row = -1;
		int currentRspan = 1;
		for (List<String> l : lTd) {
			if (l != null && l.size() > 1 && l.get(0).equalsIgnoreCase("--INFO--")) {
				table = new PdfPTable(2);
				table.setWidthPercentage(50.0f);
				for (int i = 1 ; i < l.size() ; i++) {
					boolean isTitleName = l.get(i).startsWith("#TITLENAME#");
					boolean isCaption = l.get(i).startsWith("#CAPTION#");
					cell = new PdfPCell(new Phrase(l.get(i).replaceAll("^\\#.*\\#", ""), isTitleName || isCaption ? fontBold : font));
					cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
					cell.setPadding(padding);
					if (isCaption) {
						cell.setBackgroundColor(thcolor);
						cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
					}
					if (isTitleName) {
						cell.setBackgroundColor(thcolor);
						cell.setColspan(2);
						cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
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
					List<String> l_ = lTh.get(n);
					if (l_ != null && !l_.isEmpty() && l_.get(0).equalsIgnoreCase("--TTEXT--")) {
						//l_.remove(0);
						l_.set(0, "-");
					}
					if (l_ == null || l_.isEmpty()) {
						continue;
					}
					float[] tf = new float[l_.size()];
					for (int i = 0 ; i < l_.size() ; i++) {
						if (i == 0 && "".equals(l_.get(i))) {
							tf[i] = 0f;
						}
						else {
							tf[i] = (hWidth.containsKey(l_.get(i)) ? hWidth.get(l_.get(i).replaceAll("^\\#.*\\#", "")) : 0.5f);
						}
					}
					table = new PdfPTable(tf);
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
						cell = new PdfPCell(new Phrase(s, fontBold));
						cell.setBackgroundColor(thcolor);
						cell.setPadding(padding);
						int mcindex = lMerge.indexOf(new MergedCell(n, i, 0, 0));
						if (mcindex > -1) {
							int cspan = lMerge.get(mcindex).getCspan();
							cell.setColspan(cspan);
							i += (cspan - 1);
						}
						cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
						table.addCell(cell);
					}
					n++;
					row = 0;
				}
			}
			else {
				if (currentRspan > 1) {
					for (int i = l.size() - 1 ; i >= 0 ; i--) {
						if ("".equals(l.get(i))) {
							l.remove(i);
						}
					}
					currentRspan--;
				}
				int col = 0;
				while (col < l.size()) {
					String s = l.get(col);
					String txt = s.replaceAll("^\\#.+\\#", "");
					cell = new PdfPCell(new Phrase(txt, font));
					cell.setPadding(2.0f);
					cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
					PdfPTable table_ = null;
					if (s.matches("^\\#.+\\.png\\#.+")) {
						String src = s.substring(1, s.lastIndexOf("#"));
						if (!src.contains("details.png") && !src.contains("note.png")) {
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
					if (table_ != null) {
						cell = new PdfPCell(table_);
					}
					int mcindex = lMerge.indexOf(new MergedCell(row, col, 0, 0));
					int cspan = 1;
					if (mcindex > -1) {
						MergedCell mc = lMerge.get(mcindex);
						cspan = mc.getCspan();
						cell.setColspan(cspan);
						int rspan = mc.getRspan();
						cell.setRowspan(rspan);
						currentRspan = rspan;
					}
					table.addCell(cell);
					col += cspan;
				}
			}
			row++;
		}
		PdfPCell c = new PdfPCell(table);
		c.setBorderWidth(2);
		t.addCell(c);
		doc.add(t);
		if (doc.isOpen()) {
			doc.close();
		}
	}

	public static void parseHTML(Document doc, List<List<String>> lTh, List<List<String>> lTd, List<MergedCell> lMerge) throws Exception {
		int row = 0;
		
		// INFO
		Elements tinfo = doc.getElementsByClass("info");
		if (tinfo != null && !tinfo.isEmpty()) {
			List<String> lTd_ = new ArrayList<String>();
			lTd_.add("--INFO--");
			Element info = tinfo.get(0);
			Element titleName = info.getElementById("titlename");
			if (titleName != null)
				lTd_.add("#TITLENAME#" + titleName.text());
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
				if (!tr.getElementsByClass("extlinks").isEmpty()) {
					Element td = tr.getElementsByTag("td").get(0);
					if (td.className().equals("extlinks")) {
						for (Element e : td.select("th,a")) {
							lTd_.add((e.tagName().equals("th") ? "#CAPTION#" : "#ALIGN_LEFT#") + e.text());
							row++;
						}
					}
				}
			}
			lTd.add(lTd_);
		}
		// TABLE
		Elements tsorts = doc.getElementsByClass("tsort");
		for (Element table : tsorts) {
			if (table.getElementsByTag("thead").isEmpty())
				continue;
			List<String> lTd_ = new ArrayList<String>();
			lTd_.add("--NEW--");
			lTd.add(lTd_);
			Element thead = table.getElementsByTag("thead").get(0);
			Element tbody = thead.nextElementSibling();
			Element tr = (thead.childNodeSize() > 1 ? thead.child(1) : thead.child(0));
			Element th1 = tr.getElementsByTag("th").get(0);
			Element th = tr.getElementsByTag("th").get(0);
			Elements ttext = thead.getElementsByClass("toggletext");
			int cell = 0;
			List<String> lTh_ = new ArrayList<String>();
			if (th != null && !ttext.isEmpty()) {
				lTh_.add("--TTEXT--");
				lTh_.add(ttext.text());
			}
			while(th != null) {
				Integer span = (StringUtils.notEmpty(th.attr("colspan")) ? StringUtils.toInt(th.attr("colspan")) : 1);
				lTh_.add((th1 != null && th1.nextElementSibling() == null && lTh_.isEmpty() ? "#" + th1.text() + "#" : "") + th.text());
				if (span > 1) {
					lMerge.add(new MergedCell(row, cell, span, 1));
					cell += span;
					span--;
					while (span > 0) {
						lTh_.add("");
						span--;
					}
				}
				else {
					cell++;
				}
				th = th.nextElementSibling();
			}
			row++;
			lTh.add(lTh_);
			tr = tbody.getElementsByTag("tr").get(0);
			int[] trspan = new int[100];
			Arrays.fill(trspan, 1);
			while(tr != null) {
				lTd_ = new ArrayList<String>();
				Element td = tr.getElementsByTag("td").get(0);
				boolean rowspan = false;
				cell = 0;
				while (td != null) {
					Integer cspan = (StringUtils.notEmpty(td.attr("colspan")) ? StringUtils.toInt(td.attr("colspan")) : 1);
					Integer rspan = (StringUtils.notEmpty(td.attr("rowspan")) ? StringUtils.toInt(td.attr("rowspan")) : 1);
					String title_ = td.attr("title");
					if (StringUtils.notEmpty(title_)) {
						lTd_.add(title_);
					}
					else {
						String img = "";
						Elements imgs = td.getElementsByTag("img");
						if (!imgs.isEmpty()) {
							img = "#" + imgs.get(0).attr("src") + "#";
						}
						lTd_.add(img + td.text());
					}
					// Colpsan
					if (cspan > 1) {
						if (rspan == 1) {
							lMerge.add(new MergedCell(row, cell, cspan, 1));	
						}
						cell += cspan;
						for (int i = cspan - 1 ; i > 0 ; i--) {
							lTd_.add("");
						}
					}
					else {
						cell++;
					}
					// Rowspan
					if (rspan > 1) {
						lMerge.add(new MergedCell(row, cell - cspan, cspan, rspan));
						for (int i = cspan ; i > 0 ; i--) {
							trspan[cell - i] = rspan;
						}
						rowspan = true;
					}
					td = td.nextElementSibling();
				}
				// Check rowspans to apply on row
				if (!rowspan) {
					int max = 0;
					for (int i = 0 ; i < trspan.length ; i++) {
						max = trspan[i] > 1 ? i : max;
					}
					for (int i = 0 ; i <= max ; i++) {
						int rspan = trspan[i];
						if (rspan > 1) {
							if (i <= lTd_.size()) {
								lTd_.add(i, "");
							}
							else {
								lTd_.add("");
							}
							trspan[i] = rspan - 1;
						}
					}
				}
				lTd.add(lTd_);
				tr = tr.nextElementSibling();
				row++;
			}
		}
		// WINREC
		Element twinrec = doc.getElementById("winrec");
		if (twinrec != null) {
			List<String> lTd_ = new ArrayList<String>();
			lTd_.add("--NEW--");
			lTd.add(lTd_);
			List<String> lTh_ = new ArrayList<String>();
			lTh_.add("--TTEXT--");
			lTh_.add(twinrec.getElementsByTag("th").get(0).text());
			lTh.add(lTh_);
			lMerge.add(new MergedCell(row + 1, 0, 2, 1));
			for (Element tr : twinrec.getElementsByTag("tr")) {
				List<Element> lCaption = tr.getElementsByClass("caption");
				if (lCaption != null && !lCaption.isEmpty()) {
					List<Element> lCount = tr.getElementsByClass("count");
					lTd_ = new ArrayList<String>();
					lTd_.add(lCaption.get(0).text());
					lTd_.add(lCount.get(0).text());
					lTd.add(lTd_);
					row++;
				}
			}
		}
	}

	public static void export(HttpServletResponse response, StringBuffer html, String format, String lang) throws Exception {
		try {
			String html_ = html.toString();
			html_ = html_.replaceAll("\\&ndash\\;", "-").replaceAll("\\>" + ResourceUtils.getText("details", lang) + "\\<", "><");
			if (format.matches("csv|xls|txt")) {
				html_ = html_.replaceAll("&nbsp;", " ").replaceAll("<br/>", "&nbsp;/&nbsp;");
			}
			Document doc = Jsoup.parse(html_, "utf-8");
			Element elTitle = doc.getElementsByAttributeValue("class", "title").first();
			String title = elTitle.text().replaceAll("\\,\\s", "_");
			title = URLEncoder.encode(title, "UTF-8").replaceAll("\\+", " ").replaceAll("\\s\\-\\s", "_");
			List<List<String>> lTh = new ArrayList<>();
			List<List<String>> lTd = new ArrayList<>();
			List<MergedCell> lMerge = new ArrayList<>();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + title + " (sporthenon.com)." + format);
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