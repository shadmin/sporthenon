package com.sporthenon.utils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelUtils {

	public static void buildExcel(OutputStream out, String title, Collection<String> cHeader, Collection<ArrayList<String>> cContent, boolean[] tBold) throws Exception {
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		sheet = hwb.createSheet(title);
		short rowIndex = 0;
		ArrayList<Integer> lMerge = new ArrayList<Integer>();
		// Styles
		HSSFCellStyle headerStyle = hwb.createCellStyle();
		Font boldFont = hwb.createFont();
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
			if (l.size() == 1)
				lMerge.add(rowIndex - 1);
		}
		// Sizing
		int n = (cHeader != null ? cHeader.size() : (tBold != null ? tBold.length : 2));
		for (int j = 1 ; j <= n ; j++)
			sheet.autoSizeColumn(j - 1);
		for (Integer i_ : lMerge)
			sheet.addMergedRegion(new CellRangeAddress(i_, i_, 0, n - 1));
		hwb.write(out);
		out.close();
	}

}