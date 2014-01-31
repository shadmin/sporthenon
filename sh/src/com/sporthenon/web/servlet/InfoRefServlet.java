package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.converter.HtmlConverter;
import com.sporthenon.db.entity.Draw;
import com.sporthenon.db.entity.Result;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.web.RenderOptions;

public class InfoRefServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	public InfoRefServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			RenderOptions opts = ServletHelper.buildOptions(hParams);
			String[] params = String.valueOf(hParams.get("p")).split("-");
			StringBuffer html = new StringBuffer();
			boolean isLink = hParams.containsKey("run");
			boolean isExport = hParams.containsKey("export");
			boolean isDraw = params[0].equals(Draw.alias);
			boolean isResult = params[0].equals(Result.alias);
			
			if (isResult) {
				Result rs = (Result) DatabaseHelper.loadEntity(Result.class, new Integer(params[1]));
				response.sendRedirect("results?redirect=1&run=1&sp=" + rs.getSport().getId() + "&cp=" + rs.getChampionship().getId() + "&ev=" + rs.getEvent().getId() + "&se=" + (rs.getSubevent() != null ? rs.getSubevent().getId() : ""));
			}
			else {
				// Info
				if (params.length == 2)
					html.append(HtmlConverter.getRecordInfo(params[0], new Integer(params[1]), opts));
				
				// References
				if (!isDraw) {
					ArrayList<Object> lFuncParams = new ArrayList<Object>();
					lFuncParams.add(params[0]);
					lFuncParams.add(new Integer(params[1]));
					lFuncParams.add(params.length > 2 ? params[2] : "");
					html.append(HtmlConverter.getRecordRef(lFuncParams, DatabaseHelper.call("EntityRef", lFuncParams), isExport, opts));
				}
				
				if (isLink) {
					if (isExport)
						ExportUtils.export(response, html, String.valueOf(hParams.get("export")));
					else
						ServletHelper.writeLinkHtml(request, response, html);
				}
				else
					ServletHelper.writeHtml(response, html);				
			}
		}
		catch (Exception e) {
			handleException(e);
		}
	}

}