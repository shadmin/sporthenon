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
import com.sporthenon.web.HtmlConverter;
import com.sporthenon.web.ServletHelper;

@WebServlet(
	name = "CalendarServlet",
	urlPatterns = {"/CalendarServlet"}
)
public class CalendarServlet extends AbstractServlet {

private static final long serialVersionUID = 1L;
	
	public CalendarServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			String lang = getLocale(request);
			if (hParams.containsKey("run")) { // View results
				boolean isLink = false;
				if (hParams.containsKey("p") && !hParams.containsKey("redirect")) {
					String p = String.valueOf(hParams.get("p"));
					p = StringUtils.decode(p);
					String[] t = p.split("\\-");
					hParams.put("dt1", t[0]);
					hParams.put("dt2", t.length > 1 && StringUtils.notEmpty(t[1]) ? t[1] : t[0]);
					hParams.put("sp", t.length > 2 ? t[2] : "");
					isLink = true;
				}
				List<Object> lFuncParams = new ArrayList<Object>();
				lFuncParams.add(StringUtils.notEmpty(hParams.get("dt1")) ? String.valueOf(hParams.get("dt1")) : "18500101");
				lFuncParams.add(StringUtils.notEmpty(hParams.get("dt2")) ? String.valueOf(hParams.get("dt2")) : "21001231");
				lFuncParams.add(StringUtils.notEmpty(hParams.get("sp")) ? StringUtils.toInt(hParams.get("sp")) : 0);
				lFuncParams.add("_" + lang);
				Collection<RefItem> c = (Collection<RefItem>) DatabaseManager.callFunction("get_calendar_results", lFuncParams, RefItem.class);
				StringBuffer html = new StringBuffer();
				html.append(HtmlConverter.getHeader(request, HtmlConverter.HEADER_CALENDAR, lFuncParams, getUser(request), lang));
				html.append(HtmlConverter.convertCalendarResults(request, c, getUser(request), lang));
				if (isLink) {
					HtmlUtils.setHeadInfo(request, html.toString());
					if (hParams.containsKey("export"))
						ExportUtils.export(response, html, String.valueOf(hParams.get("export")), lang);
					else {
						request.setAttribute("menu", "calendar");
						ServletHelper.writePageHtml(request, response, html, lang, hParams.containsKey("print"));
					}
				}
				else
					ServletHelper.writeTabHtml(request, response, html.append(isLink ? "</div>" : ""), lang);
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
}