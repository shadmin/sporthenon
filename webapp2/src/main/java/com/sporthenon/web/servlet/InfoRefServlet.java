package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.HtmlConverter;
import com.sporthenon.web.ServletHelper;

@WebServlet(
    name = "InfoRefServlet",
    urlPatterns = {"/InfoRefServlet"}
)
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
			HashMap<String, Object> mapParams = ServletHelper.getParams(request);
			String lang = getLocale(request);
			String[] params = StringUtils.decode(String.valueOf(mapParams.get("p"))).split("-");
			StringBuffer html = new StringBuffer();
			boolean isExport = mapParams.containsKey("export");
			boolean isResult1 = (params[0].equals(Result.alias) && params.length == 3);
			boolean isResultX = (params[0].equals(Result.alias) && params.length == 2);
			if (isResultX) {
				String p = "";
				if (params.length == 2) {
					Result rs = (Result) DatabaseManager.loadEntity(Result.class, params[1]);
					p = rs.getSport().getId() + "-" + rs.getChampionship().getId() + "-" + rs.getEvent().getId() + "-" + (rs.getSubevent() != null ? rs.getSubevent().getId() : "") + "-" + (rs.getSubevent2() != null ? rs.getSubevent2().getId() : "") + "-0";
				}
				else
					p = params[1] + "-" + params[2] + "-" + params[3] + "-" + (params.length > 4 ? params[4] : "") + "-" + (params.length > 5 ? params[5] : "") + "-0";
				redirect(request, response, "/results/" + StringUtils.encode(p), false);
			}
			else {
				int id = (params.length > 1 ? StringUtils.toInt(params[1]) : 0);
				if (id == 0)
					throw new EmptyIdException();
				List<Object> params_ = new ArrayList<Object>();
				params_.add(params[0]);
				params_.add(id);
				params_.add(params.length > 2 ? params[2] : "");
				params_.add(params.length > 3 ? params[3] : ConfigUtils.getValue("default_ref_limit"));
				params_.add(params.length > 4 ? StringUtils.toInt(params[4]) : 0);
				params_.add(ResourceUtils.getLocaleParam(lang));
				if (isExport) {
					params_.set(3, "1000");
					params_.set(4, 0);
				}
				
				// Info
				if (params.length == 2 || isResult1) {
					StringBuffer ri = HtmlConverter.getRecordInfo(request, params[0], id, lang);
					if (ri == null) {
						throw new RemovedEntityException("Entity removed: " + params[0] + "-" + id);
					}
					params_.add(ri.substring(0, ri.indexOf("</span>")).replaceFirst(".*title'\\>", ""));
					html.append(HtmlConverter.getHeader(request, HtmlConverter.HEADER_REF, params_, getUser(request), ResourceUtils.getLocaleParam(lang)));
					html.append(ri);
					params_.remove(6);
				}

				// References
				if (!isResult1) {
					html.append(HtmlConverter.getRecordRef(request, params_, DatabaseManager.callFunctionSelect("entity_ref", params_, RefItem.class), isExport, getUser(request), ResourceUtils.getLocaleParam(lang)));
				}

				// Load HTML results or export
				HtmlUtils.setHeadInfo(request, html.toString());
				if (isExport) {
					ExportUtils.export(response, html, String.valueOf(mapParams.get("export")), lang);
				}
				else {
					ServletHelper.writePageHtml(request, response, html, lang, mapParams.containsKey("print"));
				}
			}
		}
		catch (EmptyIdException e) {
			response.sendRedirect("/error");
		}
		catch (RemovedEntityException e) {
			log.log(Level.WARNING, e.getMessage());
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