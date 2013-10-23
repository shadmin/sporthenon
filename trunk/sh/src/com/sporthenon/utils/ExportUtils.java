package com.sporthenon.utils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
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

	private static void buildExcel(OutputStream out, String title, Collection<String> cHeader, Collection<ArrayList<String>> cContent, List<Integer> lMerge , boolean[] tBold) throws Exception {
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
		// Title
		//		if (title != null) {
		//			row = sheet.createRow(rowIndex++);
		//			(cell = row.createCell(0)).setCellValue(title);
		//			cell.setCellStyle(headerStyle);
		//		}
		// Header
		int i = 0;
		if (cHeader != null) {
			row = sheet.createRow(rowIndex++);
			for (String s : cHeader) {
				(cell = row.createCell(i++)).setCellValue(s);
				cell.setCellStyle(headerStyle);
			}
		}
		// Content
		for (List<String> l : cContent) {
			i = 0;
			row = sheet.createRow(rowIndex++);
			for (String s : l) {
				(cell = row.createCell(i++)).setCellValue(s);
				cell.setCellStyle(l.size() == 1 ? headerStyle : (tBold != null && tBold.length > i - 1 && tBold[i - 1] ? boldStyle : normalStyle));
				if (l.size() == 1) {
					(cell = row.createCell(i++)).setCellValue("");
					cell.setCellStyle(headerStyle);
				}
			}
		}
		// Auto-Sizing
		for (int j = 1 ; j <= cHeader.size() ; j++)
			sheet.autoSizeColumn(j - 1);
		// Merging
		int index = 0;
		for (int i_ = 0 ; i_ < lMerge.size() ; i_++) {
			Integer m = lMerge.get(i_);
			if (m != null && m > 1)
				sheet.addMergedRegion(new CellRangeAddress(0, 0, index, index + m - 1));
			index += m;
		}
		hwb.write(out);
	}
	
	private static void buildText(OutputStream out, List<String> lHeader, List<ArrayList<String>> lContent) throws Exception {
		StringBuffer sbText = new StringBuffer();
		int[] tMaxLength = new int[lHeader.size()];
		for (int i = 0 ; i < lHeader.size() ; i++)
			tMaxLength[i] = lHeader.get(i).length();
		for (List<String> l : lContent) {
			for (int i = 0 ; i < l.size() ; i++) {
				if (l.get(i).length() > tMaxLength[i])
					tMaxLength[i] = l.get(i).length() + 1;
			}
		}
		StringBuffer sbSep = new StringBuffer("+");
		for (int i = 0 ; i < tMaxLength.length ; i++) {
			for (int j = 0 ; j < tMaxLength[i] ; j++)
				sbSep.append("-");
			sbSep.append("+");
		}
		sbText.append(sbSep).append("\r\n|");
		for (int i = 0 ; i < lHeader.size() ; i++) {
			String s = lHeader.get(i);
			sbText.append(s);
			for (int j = s.length() ; j < tMaxLength[i] ; j++)
				sbText.append(" ");
			sbText.append("|");
		}
		sbText.append("\r\n").append(sbSep);
		for (List<String> l : lContent) {
			sbText.append("\r\n|");
			for (int i = 0 ; i < l.size() ; i++) {
				String s = l.get(i);
				sbText.append(s);
				for (int j = s.length() ; j < tMaxLength[i] ; j++)
					sbText.append(" ");
				sbText.append("|");
			}
		}
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
		ArrayList<Integer> lMerge = new ArrayList<Integer>();
		ArrayList<String> lHeader = new ArrayList<String>();
		ArrayList<ArrayList<String>> lContent = new ArrayList<ArrayList<String>>();
		Element title = doc.getElementsByAttributeValue("class", "shorttitle").first();
		Elements tsorts = doc.getElementsByClass("tsort");
		for (Element table : tsorts) {
			Element thead = table.getElementsByTag("thead").get(0);
			Element tbody = thead.nextElementSibling();
			Element th = thead.getElementsByTag("th").get(0);
			while(th != null) {
				Integer cs = (StringUtils.notEmpty(th.attr("colspan")) ? new Integer(th.attr("colspan")) : 1);
				lHeader.add(th.text());
				lMerge.add(cs);
				if (cs != null && cs > 1) {
					cs--;
					while (cs > 0) {
						lHeader.add("");
						cs--;
					}
				}
				th = th.nextElementSibling();
			}
			Element tr = tbody.getElementsByTag("tr").get(0);
			while(tr != null) {
				ArrayList<String> lContent_ = new ArrayList<String>();
				Element td = tr.getElementsByTag("td").get(0);
				while (td != null) {
					lContent_.add(td.text());
					td = td.nextElementSibling();
				}
				lContent.add(lContent_);
				tr = tr.nextElementSibling();
			}
		}
		if (isExcel)
			buildExcel(out, title.text(), lHeader, lContent, lMerge, new boolean[]{false});
		else
			buildText(out, lHeader, lContent);
	}

	public static void export(HttpServletResponse response, StringBuffer html, String format) throws Exception {
		try {
			Document doc = Jsoup.parse(html.toString().replaceAll("&nbsp;", " "));
			Element title = doc.getElementsByAttributeValue("class", "shorttitle").first();
			response.setCharacterEncoding("utf-8");
			if (format.equalsIgnoreCase("html")) {
				response.setContentType("text/html");
				response.setHeader("Content-Disposition", "attachment;filename=Sporthenon.com - " + title.text() + ".html");
				response.getWriter().write(toHtml(doc));
			}
			else if (format.equalsIgnoreCase("excel")) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment;filename=Sporthenon.com - " + title.text() + ".xls");
				toExcelOrText(response.getOutputStream(), doc, true);
			}
			else if (format.equalsIgnoreCase("pdf")) {

			}
			else if (format.equalsIgnoreCase("text")) {
				response.setContentType("text/plain");
				response.setHeader("Content-Disposition", "attachment;filename=Sporthenon.com - " + title.text() + ".txt");
				toExcelOrText(response.getOutputStream(), doc, false);
			}
		}
		finally {
			response.getOutputStream().close();
		}
	}

}