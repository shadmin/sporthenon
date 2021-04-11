package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.utils.ExportUtils;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.HtmlConverter;
import com.sporthenon.web.ServletHelper;

@WebServlet(
	name = "CalendarServlet",
	urlPatterns = {"/CalendarServlet", "/SearchCalendar"}
)
public class CalendarServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			HashMap<String, Object> mapParams = ServletHelper.getParams(request);
			String lang = getLocale(request);
			if (request.getRequestURI().contains("/SearchCalendar")) {
				if (mapParams.containsKey("p") && !mapParams.containsKey("redirect")) {
					String p = String.valueOf(mapParams.get("p"));
					p = StringUtils.decode(p);
					String[] t = p.split("\\-");
					mapParams.put("dt1", t[0]);
					mapParams.put("dt2", t.length > 1 && StringUtils.notEmpty(t[1]) ? t[1] : t[0]);
					mapParams.put("sp", t.length > 2 ? t[2] : "");
				}
				List<Object> params = new ArrayList<Object>();
				params.add(StringUtils.notEmpty(mapParams.get("dt1")) ? String.valueOf(mapParams.get("dt1")) : "18500101");
				params.add(StringUtils.notEmpty(mapParams.get("dt2")) ? String.valueOf(mapParams.get("dt2")) : "21001231");
				params.add(StringUtils.notEmpty(mapParams.get("sp")) ? StringUtils.toInt(mapParams.get("sp")) : 0);
				params.add(ResourceUtils.getLocaleParam(lang));
				Collection<RefItem> c = (Collection<RefItem>) DatabaseManager.callFunctionSelect("get_calendar_results", params, RefItem.class, "entity DESC, date2 DESC");
				StringBuffer html = new StringBuffer();
				html.append(HtmlConverter.getHeader(request, HtmlConverter.HEADER_CALENDAR, params, getUser(request), lang));
				html.append(HtmlConverter.convertCalendarResults(request, c, getUser(request), lang));
				
				// Load HTML results or export
				HtmlUtils.setHeadInfo(request, html.toString());
				if (mapParams.containsKey("export"))
					ExportUtils.export(response, html, String.valueOf(mapParams.get("export")), lang);
				else {
					request.setAttribute("menu", "calendar");
					ServletHelper.writePageHtml(request, response, html, lang, mapParams.containsKey("print"));
				}
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
}