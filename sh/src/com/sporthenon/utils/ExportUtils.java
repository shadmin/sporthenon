package com.sporthenon.utils;

import java.io.OutputStream;
import java.util.ArrayList;
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
		public String toString() {
			return "MergedCell [row=" + row + ", cell=" + cell + ", span=" + span + "]";
		}
	}

	private static void buildExcel(OutputStream out, String title, List<ArrayList<String>> lTh, List<ArrayList<String>> lTd, List<MergedCell> lMerge , boolean[] tBold) throws Exception {
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		sheet = hwb.createSheet(title);
		short rowIndex = 0;
		// Styles
		HSSFCellStyle headerStyle = hwb.createCellStyle();
		Font boldFont = hwb.createFont();
		boldFont.setFontName("Verdana");
		boldFont.setFontHeightInPoints((short)8);
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
		defaultFont.setFontHeightInPoints((short)8);
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
						(cell = row.createCell(i++)).setCellValue(s);
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
					boolean isAlignCenter = (s != null && s.matches("^\\#ALIGN_CENTER\\#.*"));
					(cell = row.createCell(i++)).setCellValue(s.replaceAll("^\\#.*\\#", ""));
					HSSFCellStyle st = (l.size() == 1 ? headerStyle : (tBold != null && tBold.length > i - 1 && tBold[i - 1] ? boldStyle : normalStyle));
					if (isAlignCenter)
						st.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(st);
					if (l.size() == 1) {
						(cell = row.createCell(i++)).setCellValue("");
						cell.setCellStyle(headerStyle);
					}
					n_++;
				}
				if (n_ > cols)
					cols = n_;
			}
		}
		// Auto-Sizing
		for (int j = 0 ; j < cols ; j++)
			sheet.autoSizeColumn(j);
		// Merging
		for (MergedCell mc : lMerge) {
			int offset = 0;
			for (Short sh : lBlankRow)
				if (mc.getRow() + offset >= sh)
					offset++;
			sheet.addMergedRegion(new CellRangeAddress(mc.getRow() + offset, mc.getRow() + offset, mc.getCell(), mc.getCell() + mc.getSpan() - 1));
		}
		hwb.write(out);
	}
	
	private static void buildText(OutputStream out, List<ArrayList<String>> lTh, List<ArrayList<String>> lTd) throws Exception {
		StringBuffer sbText = new StringBuffer();
		int n = 0;
		int[] tMaxLength = null;
		StringBuffer sbSep = null;
		for (List<String> l : lTd) {
			// Header
			if (l != null && l.size() == 1 && l.get(0).equalsIgnoreCase("--NEW--")) {
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
					String s = lTh_.get(i);
					sbText.append(s);
					for (int j = s.length() ; j < tMaxLength[i] ; j++)
						sbText.append(" ");
					sbText.append("|");
				}
				sbText.append("\r\n").append(sbSep);
			}
			else {
				sbText.append("\r\n|");
				for (int i = 0 ; i < l.size() ; i++) {
					String s = l.get(i).replaceAll("^\\#.*\\#", "");
					sbText.append(s);
					for (int j = s.length() ; j < tMaxLength[i] ; j++)
						sbText.append(" ");
					sbText.append("|");
				}
			}
		}
		if (sbSep != null)
			sbText.append("\r\n").append(sbSep);
		out.write(sbText.toString().getBytes());
	}
	
	public static String toHtml(Document doc) throws Exception {
		String html = doc.toString();
		html = html.replaceAll("<head>", "<head>\r\n<style>*{font:11px Verdana;}</style>\r\n<link rel='stylesheet' type='text/css' href='" + ConfigUtils.getProperty("url") + "css/sh.css'/>\r\n<link rel='stylesheet' type='text/css' href='" + ConfigUtils.getProperty("url") + "css/render.css'/>\r\n");
		html = html.replaceAll("<body>", "<body class='link'><div id='content'><div class='tc'>");
		html = html.replaceAll("<\\/body>", "</div></div></body>");
		html = html.replaceAll("img/render", ConfigUtils.getProperty("url") + "img/render");
		html = html.replaceAll("\\</?a.*?>|\\sclass=\"srt\"|onclick\\=\".*?\"", "");
		return html;
	}

	public static void toExcelOrText(OutputStream out, Document doc, boolean isExcel) throws Exception {
		ArrayList<MergedCell> lMerge = new ArrayList<MergedCell>();
		ArrayList<ArrayList<String>> lTh = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> lTd = new ArrayList<ArrayList<String>>();
		Element title = doc.getElementsByAttributeValue("class", "shorttitle").first();
		int row = 0;
		Elements theaders = doc.getElementsByClass("header");
		if (theaders == null || theaders.isEmpty())
			theaders = doc.getElementsByClass("info");
		if (theaders != null && !theaders.isEmpty()) {
			Element header = theaders.get(0);
			ArrayList<String> lTd_ = new ArrayList<String>();
			lTd_.add("--NEW--");
			lTd.add(lTd_);
			Element th = header.getElementsByTag("th").get(0);
			ArrayList<String> lTh_ = new ArrayList<String>();
			lTh_.add(th.text());
			if (isExcel)
				lTh_.add("");
			lTh.add(lTh_);
			lMerge.add(new MergedCell(row, 0, 2));
			row++;
			for (Element td : header.getElementsByTag("td")) {
				if (td.className() == null || !td.className().matches("^logo.*")) {
					lTd_ = new ArrayList<String>();
					lTd_.add("#ALIGN_CENTER#" + td.text());
					lTd.add(lTd_);
					if (isExcel)
						lTd_.add("");
					lMerge.add(new MergedCell(row, 0, 2));
					row++;
				}
			}
		}
		Elements tsorts = doc.getElementsByClass("tsort");
		for (Element table : tsorts) {
			ArrayList<String> lTd_ = new ArrayList<String>();
			lTd_.add("--NEW--");
			lTd.add(lTd_);
			Element thead = table.getElementsByTag("thead").get(0);
			Element tbody = thead.nextElementSibling();
			Element th = thead.getElementsByTag("th").get(0);
			int cell = 0;
			ArrayList<String> lTh_ = new ArrayList<String>();
			while(th != null) {
				Integer span = (StringUtils.notEmpty(th.attr("colspan")) ? new Integer(th.attr("colspan")) : 1);
				lTh_.add(th.text());
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
					lTd_.add(td.text());
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
		if (isExcel)
			buildExcel(out, title.text(), lTh, lTd, lMerge, new boolean[]{false});
		else
			buildText(out, lTh, lTd);
	}

	public static void export(HttpServletResponse response, StringBuffer html, String format) throws Exception {
		try {
			String html_ = html.toString();
			if (format.matches("excel|text"))
				html_ = html_.replaceAll("&nbsp;", " ").replaceAll("<br/>", "&nbsp;/&nbsp;");
			Document doc = Jsoup.parse(html_);
			Element elTitle = doc.getElementsByAttributeValue("class", "shorttitle").first();
			String title = elTitle.text().replaceAll("\\,\\s", "_");
			response.setCharacterEncoding("utf-8");
			if (format.equalsIgnoreCase("html")) {
				response.setContentType("text/html");
				response.setHeader("Content-Disposition", "attachment;filename=Sporthenon.com - " + title + ".html");
				response.getWriter().write(toHtml(doc));
			}
			else if (format.equalsIgnoreCase("excel")) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment;filename=Sporthenon.com - " + title + ".xls");
				toExcelOrText(response.getOutputStream(), doc, true);
			}
			else if (format.equalsIgnoreCase("pdf")) {
			}
			else if (format.equalsIgnoreCase("text")) {
				response.setContentType("text/plain");
				response.setHeader("Content-Disposition", "attachment;filename=Sporthenon.com - " + title + ".txt");
				toExcelOrText(response.getOutputStream(), doc, false);
			}
		}
		finally {
			//response.getOutputStream().close();
		}
	}
	
}