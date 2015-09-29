package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class NavigationServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	private static HashMap<String, String> hPages;
	private static HashMap<String, String> hServlet;
	private static HashMap<String, String> hTitle;
	
	static {
		hPages = new HashMap<String, String>();
		hPages.put("index", "index.jsp");
		hPages.put("results", "db/results.jsp");
		hPages.put("calendar", "db/calendar.jsp");
		hPages.put("olympics", "db/olympics.jsp");
		hPages.put("usleagues", "db/usleagues.jsp");
		hPages.put("search", "db/search.jsp");
		hPages.put("project", "project.jsp");
		hPages.put("contribute", "contribute.jsp");
		hPages.put("login", "login.jsp");
		hPages.put("update-overview", "update/overview.jsp");
		hPages.put("update-results", "update/results.jsp");
		hPages.put("update-data", "update/data.jsp");
		hPages.put("update-pictures", "update/pictures.jsp");
		hPages.put("update-folders", "update/folders.jsp");
		hPages.put("update-import", "update/import.jsp");
		hPages.put("update-extlinks", "update/extlinks.jsp");
		hPages.put("update-translations", "update/translations.jsp");
		hPages.put("update-query", "update/query.jsp");
		hPages.put("update-admin", "update/admin.jsp");
		hServlet = new HashMap<String, String>();
		hServlet.put("results", "/ResultServlet");
		hServlet.put("calendar", "/CalendarServlet");
		hServlet.put("olympics", "/OlympicsServlet");
		hServlet.put("usleagues", "/USLeaguesServlet");
		hServlet.put("project", "/ProjectServlet");
		hServlet.put("search", "/SearchServlet");
		hServlet.put("login", "/LoginServlet");
		hServlet.put("update", "/UpdateServlet");
		hServlet.put("update-overview", "/UpdateServlet");
		hServlet.put("update-results", "/UpdateServlet");
		hServlet.put("update-data", "/UpdateServlet");
		hServlet.put("update-pictures", "/UpdateServlet");
		hServlet.put("update-folders", "/UpdateServlet");
		hServlet.put("update-import", "/UpdateServlet");
		hServlet.put("update-extlinks", "/UpdateServlet");
		hServlet.put("update-translations", "/UpdateServlet");
		hServlet.put("update-query", "/UpdateServlet");
		hServlet.put("update-admin", "/UpdateServlet");
		hServlet.put("android", "/AndroidServlet");
		hTitle = new HashMap<String, String>();
		hTitle.put("index", "title");
		hTitle.put("results", "menu.results.2");
		hTitle.put("olympics", "menu.olympics.2");
		hTitle.put("calendar", "menu.calendar.2");
		hTitle.put("usleagues", "menu.usleagues.2");
		hTitle.put("project", "menu.project");
		hTitle.put("contribute", "menu.contribute");
		hTitle.put("search", "menu.search");
		hTitle.put("login", "menu.login");
		hTitle.put("update-overview", "update.overview");
		hTitle.put("update-results", "update.results");
		hTitle.put("update-data", "data");
		hTitle.put("update-pictures", "update.pictures");
		hTitle.put("update-folders", "update.folders");
		hTitle.put("update-import", "update.import");
		hTitle.put("update-extlinks", "update.extlinks");
		hTitle.put("update-translations", "update.translations");
		hTitle.put("update-query", "update.query");
		hTitle.put("update-admin", "Admin");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURL().toString();
		try {
			if (url.matches(".*\\/(athletes|championships|cities|complexes|countries|events|sports|usstates|teams|years)\\/.*"))
				throw new OldPatternException();
			if (!url.contains("/ajax") && !url.contains("/load") && !url.contains("/check-progress-import"))
				logger.fatal("URL: " + url);
			String[] tURI = request.getRequestURI().substring(1).split("\\/", 0);
			String key = tURI[0];
			if (key.isEmpty())
				key = "index";
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			request.setAttribute("url", url);
			request.setAttribute("urlEN", url.replaceAll("\\?.*", "") + "?lang=en");
			request.setAttribute("urlFR", url.replaceFirst("(www\\.|test\\.|fr\\.|)sporthenon\\.com", "fr.sporthenon.com"));
			RequestDispatcher dispatcher = null;
			if (ConfigUtils.getProperty("env").matches("test|prod"))
				if (key != null && key.equals("update") && (request.getSession() == null || request.getSession().getAttribute("user") == null))
					throw new NotLoggedInException();
			if (hParams.containsKey("lang"))
				request.getSession().setAttribute("locale", String.valueOf(hParams.get("lang")));
			if (key != null && key.equals("project"))
				hParams.put("p", 1);
			boolean isRun = (tURI.length > 1 || hParams.containsKey("p"));
			if (key != null && key.equals("update")) {
				if (hPages.containsKey("update-" + tURI[1])) {
					key += "-" + tURI[1];
					isRun = (tURI.length > 2);
				}
				else	
					isRun = true;
			}
			request.setAttribute("title", StringUtils.getTitle(ResourceUtils.getText(hTitle.containsKey(key) ? hTitle.get(key) : "title", getLocale(request))));
			request.setAttribute("desc", ResourceUtils.getText(key.equals("index") ? "desc" : "desc." + key, getLocale(request)));
			if (isRun) {
				Object export = hParams.get("export");
				boolean isPrint = hParams.containsKey("print");
				if (export != null)
					hParams.remove("export");
				if (isPrint)
					hParams.remove("print");
				StringBuffer url_ = new StringBuffer(hServlet.containsKey(key) ? hServlet.get(key) : "/InfoRefServlet");
				url_.append("?run=1&t=" + System.currentTimeMillis());
				url_.append("&p=" + (hParams.containsKey("p") ? hParams.get("p") : tURI[tURI.length - 1]));
				url_.append(tURI.length > 2 ? "&p2=" + tURI[tURI.length - 2] : "");
				url_.append(isPrint ? "?print" : "");
				url_.append(export != null ? "?export=" + export : "");
				dispatcher = request.getRequestDispatcher(url_.toString());
			}
			else {
				if (key.matches("en|fr")) {
					request.getSession().setAttribute("locale", key);
					dispatcher = request.getRequestDispatcher("/jsp/index.jsp");
				}
				else {
					request.setAttribute("menu", key);
					dispatcher = request.getRequestDispatcher("/jsp/" + hPages.get(key));
				}
			}
		    if (dispatcher != null)
		    	dispatcher.forward(request, response);
		}
		catch (OldPatternException e) {
			response.sendRedirect(url.replaceAll("\\/athletes", "/athlete").replaceAll("\\/championships", "/championship").replaceAll("\\/cities", "/city").replaceAll("\\/complexes", "/complex").replaceAll("\\/countries", "/country").replaceAll("\\/events", "/event").replaceAll("\\/sports", "/sport").replaceAll("\\/usstates", "/usstate").replaceAll("\\/teams", "/team").replaceAll("\\/years", "/year"));
		}
		catch (NotLoggedInException e) {
			response.sendRedirect("/login");
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
	class OldPatternException extends Exception{
		private static final long serialVersionUID = 1L;
	}
	class NotLoggedInException extends Exception{
		private static final long serialVersionUID = 1L;
	}

}