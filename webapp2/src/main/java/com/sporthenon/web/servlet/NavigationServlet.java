package com.sporthenon.web.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.entity.meta.Redirection;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.ServletHelper;

@WebServlet(
    name = "NavigationServlet",
    urlPatterns = {"/index", "/results/*", "/browse/*", "/calendar/*", "/olympics/*", "/usleagues/*", "/search/*", "/contribute/*", "/login/*", "/update/*", "/android/*",
    		"/athlete/*", "/championship/*", "/city/*", "/complex/*", "/contributor/*", "/country/*", "/entity/*", "/event/*", "/olympicgames/*", "/result/*", "/sport/*", "/usstate/*", "/team/*", "/year/*",
    		"/fr/*", "/en/*"}
)
public class NavigationServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	private static Map<String, String> mapPages;
	private static Map<String, String> mapServlet;
	private static Map<String, String> mapTitle;
	
	private static final String CONTEXT_ROOT = "/sporthenon";
	
	static {
		mapPages = new HashMap<>();
		mapPages.put("index", "index.jsp");
		mapPages.put("results", "db/results.jsp");
		mapPages.put("browse", "db/browse.jsp");
		mapPages.put("calendar", "db/calendar.jsp");
		mapPages.put("olympics", "db/olympics.jsp");
		mapPages.put("usleagues", "db/usleagues.jsp");
		mapPages.put("search", "db/search.jsp");
		mapPages.put("contribute", "contribute.jsp");
		mapPages.put("login", "login.jsp");
		mapPages.put("update-overview", "update/overview.jsp");
		mapPages.put("update-results", "update/results.jsp");
		mapPages.put("update-data", "update/data.jsp");
		mapPages.put("update-pictures", "update/pictures.jsp");
		mapPages.put("update-folders", "update/folders.jsp");
		mapPages.put("update-import", "update/import.jsp");
		mapPages.put("update-extlinks", "update/extlinks.jsp");
		mapPages.put("update-translations", "update/translations.jsp");
		mapPages.put("update-query", "update/query.jsp");
		mapPages.put("update-errors", "update/errors.jsp");
		mapPages.put("update-redirections", "update/redirections.jsp");
		mapPages.put("update-admin", "update/admin.jsp");
		mapServlet = new HashMap<>();
		mapServlet.put("results", "/SearchResults");
		mapServlet.put("calendar", "/SearchCalendar");
		mapServlet.put("olympics", "/SearchOlympics");
		mapServlet.put("usleagues", "/SearchUSLeagues");
		mapServlet.put("search", "/Search");
		mapServlet.put("login", "/LoginServlet");
		mapServlet.put("update", "/UpdateServlet");
		mapServlet.put("update-overview", "/UpdateServlet");
		mapServlet.put("update-results", "/UpdateServlet");
		mapServlet.put("update-data", "/UpdateServlet");
		mapServlet.put("update-pictures", "/UpdateServlet");
		mapServlet.put("update-folders", "/UpdateServlet");
		mapServlet.put("update-import", "/UpdateServlet");
		mapServlet.put("update-extlinks", "/UpdateServlet");
		mapServlet.put("update-translations", "/UpdateServlet");
		mapServlet.put("update-query", "/UpdateServlet");
		mapServlet.put("update-errors", "/UpdateServlet");
		mapServlet.put("update-redirections", "/UpdateServlet");
		mapServlet.put("update-admin", "/UpdateServlet");
		mapServlet.put("android", "/AndroidServlet");
		mapTitle = new HashMap<>();
		mapTitle.put("index", "title");
		mapTitle.put("results", "menu.results.2");
		mapTitle.put("browse", "menu.browse.2");
		mapTitle.put("olympics", "menu.olympics.2");
		mapTitle.put("calendar", "menu.calendar.2");
		mapTitle.put("usleagues", "menu.usleagues.2");
		mapTitle.put("contribute", "menu.contribute");
		mapTitle.put("search", "menu.search.2");
		mapTitle.put("login", "menu.login");
		mapTitle.put("update-overview", "update.overview");
		mapTitle.put("update-results", "update.results");
		mapTitle.put("update-data", "data");
		mapTitle.put("update-pictures", "update.pictures");
		mapTitle.put("update-folders", "update.folders");
		mapTitle.put("update-import", "update.import");
		mapTitle.put("update-extlinks", "update.extlinks");
		mapTitle.put("update-translations", "update.translations");
		mapTitle.put("update-query", "update.query");
		mapTitle.put("update-errors", "update.errors");
		mapTitle.put("update-redirections", "update.redirections");
		mapTitle.put("update-admin", "Admin");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String newURI = null;
		try {
			// Get info on URL and parameters
			String url = ServletHelper.getURL(request).replaceFirst("(\\&|\\?)lang\\=..", "");
			String uri = request.getRequestURI().replace(CONTEXT_ROOT, "");
			HashMap<String, Object> mapParams = ServletHelper.getParams(request);
			
			// Check NULL parameter
			if (url.matches(".*(\\/null)$")) {
				throw new NullParameterException();
			}
			
			// Handle redirections
			String sql = "SELECT * FROM _redirection WHERE previous_path = ? ORDER BY id DESC";
			Redirection re = (Redirection) DatabaseManager.loadEntity(sql, Arrays.asList(URLDecoder.decode(request.getRequestURI(), "UTF-8").replaceAll("'", "''")), Redirection.class);
			if (re != null) {
				newURI = re.getCurrentPath();
				throw new ObsoleteURLException();
			}
			
			// Get key from request URL
			String[] tURI = uri.substring(1).split("\\/", 0);
			String key = tURI[0];
			if (key.isEmpty()) {
				key = "index";
			}
			
			// Handle user in session (check if user logged in when accessing "update" content)
			boolean isUserSession = (request.getSession() != null && request.getSession().getAttribute("user") != null);
			if (key != null && key.equals("update") && !isUserSession) {
				throw new NotLoggedInException();
			}
			if (key != null && key.equals("update") && url.matches(".*\\/admin$") && isUserSession) {
				Contributor cb = (Contributor) request.getSession().getAttribute("user");
				if (!cb.isAdmin()) {
					throw new NotAdminException();
				}
			}
			
			// Set attributes with URL
			url = url.replaceAll("\\&", "&amp;");
			request.setAttribute("url", url);
			request.setAttribute("urlEN", url + (url.contains("?") ? "&" : "?") + "lang=en");
			request.setAttribute("urlFR", url + (url.contains("?") ? "&" : "?") + "lang=fr");
			request.setAttribute("title", StringUtils.getTitle(ResourceUtils.getText(mapTitle.containsKey(key) ? mapTitle.get(key) : "title", getLocale(request)) + (key.startsWith("update-") ? " | " + ResourceUtils.getText("menu.cbarea", getLocale(request)) : "")));
			request.setAttribute("desc", ResourceUtils.getText(key.equals("index") ? "desc" : "desc." + key, getLocale(request)));
			
			// Specific behaviours
			if (mapParams.containsKey("lang")) {
				request.getSession().setAttribute("locale", mapParams.get("lang"));
			}
				
			// Determine if it is :
			//		- a search query => servlet call
			// 		- a direct access to JSP
			boolean isRunServlet = (tURI.length > 1 || mapParams.containsKey("p"));
			if (key.equals("update")) {
				if (mapPages.containsKey("update-" + tURI[1])) {
					key += "-" + tURI[1];
					isRunServlet = (tURI.length > 2);
				}
				else {
					isRunServlet = true;
				}
			}
			
			// Redirect to Servlet or JSP
			RequestDispatcher dispatcher = null;
			if (isRunServlet) {
				Object export = mapParams.get("export");
				boolean isPrint = mapParams.containsKey("print");
				if (export != null) {
					mapParams.remove("export");
				}
				if (isPrint) {
					mapParams.remove("print");
				}
				StringBuffer url_ = new StringBuffer(mapServlet.containsKey(key) ? mapServlet.get(key) : "/InfoRefServlet");
				url_.append("?run=1&t=" + System.currentTimeMillis());
				url_.append("&p=" + (mapParams.containsKey("p") ? mapParams.get("p") : tURI[tURI.length - 1]));
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
					dispatcher = request.getRequestDispatcher("/jsp/" + mapPages.get(key));
				}
			}
		    if (dispatcher != null) {
		    	dispatcher.forward(request, response);
		    }
		}
		catch (NotLoggedInException e) {
			redirect(request, response, "/login", true);
		}
		catch (NotAdminException e) {
			redirect(request, response, "/update/overview", true);
		}
		catch (HttpsException e) {
			redirect(request, response, request.getRequestURI(), true);
		}
		catch (ObsoleteURLException e) {
			redirect(request, response, newURI, false);
		}
		catch (NullParameterException e) {
			response.sendRedirect("/error");
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
	class NotLoggedInException extends Exception{
		private static final long serialVersionUID = 1L;
	}
	class NotAdminException extends Exception{
		private static final long serialVersionUID = 1L;
	}
	class HttpsException extends Exception{
		private static final long serialVersionUID = 1L;
	}
	class ObsoleteURLException extends Exception{
		private static final long serialVersionUID = 1L;
	}
	class NullParameterException extends Exception{
		private static final long serialVersionUID = 1L;
	}

}