package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.Result;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.web.HtmlConverter;
import com.sporthenon.web.ServletHelper;

public class InfoRefServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	public InfoRefServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			String lang = getLocale(request);
			String[] params = StringUtils.decode(String.valueOf(hParams.get("p"))).split("-");
			StringBuffer html = new StringBuffer();
			boolean isLink = (hParams.containsKey("run") && !String.valueOf(hParams.get("p2")).equals("more"));
			boolean isExport = hParams.containsKey("export");
			boolean isResult1 = (params[0].equals(Result.alias) && params.length == 3);
			boolean isResultX = (params[0].equals(Result.alias) && params.length == 2);
			if (isResultX) {
				String p = "";
				if (params.length == 2) {
					Result rs = (Result) DatabaseHelper.loadEntity(Result.class, new Integer(params[1]));
					p = rs.getSport().getId() + "-" + rs.getChampionship().getId() + "-" + rs.getEvent().getId() + "-" + (rs.getSubevent() != null ? rs.getSubevent().getId() : "") + "-" + (rs.getSubevent2() != null ? rs.getSubevent2().getId() : "") + "-0";
				}
				else
					p = params[1] + "-" + params[2] + "-" + params[3] + "-" + (params.length > 4 ? params[4] : "") + "-" + (params.length > 5 ? params[5] : "") + "-0";
				redirect(request, response, "/results/" + StringUtils.encode(p), false);
			}
			else {
				int id = (params.length > 1 ? new Integer(params[1]) : 0);
				if (id == 0)
					throw new EmptyIdException();
				ArrayList<Object> lFuncParams = new ArrayList<Object>();
				lFuncParams.add(params[0]);
				lFuncParams.add(id);
				lFuncParams.add(params.length > 2 ? params[2] : "");
				lFuncParams.add(params.length > 3 ? params[3] : ConfigUtils.getValue("default_ref_limit"));
				lFuncParams.add(params.length > 4 ? new Integer(params[4]) : 0);
				lFuncParams.add("_" + lang);
				if (isExport) {
					lFuncParams.set(3, "ALL");
					lFuncParams.set(4, 0);
				}
				
				// Info
				if (params.length == 2 || isResult1) {
					StringBuffer ri = HtmlConverter.getRecordInfo(request, params[0], id, lang);
					if (ri == null)
						throw new RemovedEntityException("Entity removed: " + params[0] + "-" + id);
					lFuncParams.add(ri.substring(0, ri.indexOf("</span>")).replaceFirst(".*title'\\>", ""));
					html.append(HtmlConverter.getHeader(request, HtmlConverter.HEADER_REF, lFuncParams, getUser(request), lang));
					html.append(ri);
					lFuncParams.remove(6);
				}

				// References
				if (!isResult1)
					html.append(HtmlConverter.getRecordRef(lFuncParams, DatabaseHelper.call("EntityRef", lFuncParams), isExport, getUser(request), lang));

				if (isLink) {
					HtmlUtils.setHeadInfo(request, html.toString());
					if (isExport)
						ExportUtils.export(response, html, String.valueOf(hParams.get("export")), lang);
					else
						ServletHelper.writePageHtml(request, response, html, lang, hParams.containsKey("print"));
				}
				else
					ServletHelper.writeTabHtml(request, response, html, lang);				
			}
		}
		catch (EmptyIdException e) {
			response.sendRedirect("/error");
		}
		catch (RemovedEntityException e) {
			logger.error(e.getMessage());
			request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
	class EmptyIdException extends Exception{
		private static final long serialVersionUID = 1L;
	}
	class RemovedEntityException extends Exception{
		private static final long serialVersionUID = 1L;
		public RemovedEntityException(String message) {
			super(message);
		}
	}

}