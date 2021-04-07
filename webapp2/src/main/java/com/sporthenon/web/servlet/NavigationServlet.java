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
    urlPatterns = {"/index", "/results/*", "/browse/*", "/calendar/*", "/olympics/*", "/usleagues/*", "/search/*", "/project/*", "/contribute/*", "/login/*", "/update/*", "/android/*",
    		"/athlete/*", "/championship/*", "/city/*", "/complex/*", "/contributor/*", "/country/*", "/entity/*", "/event/*", "/olympicgames/*", "/result/*", "/sport/*", "/usstate/*", "/team/*", "/year/*",
    		"/fr/*", "/en/*"}
)
public class NavigationServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	private static Map<String, String> hPages;
	private static Map<String, String> hServlet;
	private static Map<String, String> hTitle;
	
	private static final String CONTEXT_ROOT = "/sporthenon";
	
	static {
		hPages = new HashMap<>();
		hPages.put("index", "index.jsp");
		hPages.put("results", "db/results.jsp");
		hPages.put("browse", "db/browse.jsp");
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
		hPages.put("update-errors", "update/errors.jsp");
		hPages.put("update-redirections", "update/redirections.jsp");
		hPages.put("update-admin", "update/admin.jsp");
		hServlet = new HashMap<>();
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
		hServlet.put("update-errors", "/UpdateServlet");
		hServlet.put("update-redirections", "/UpdateServlet");
		hServlet.put("update-admin", "/UpdateServlet");
		hServlet.put("android", "/AndroidServlet");
		hTitle = new HashMap<>();
		hTitle.put("index", "title");
		hTitle.put("results", "menu.results.2");
		hTitle.put("olympics", "menu.olympics.2");
		hTitle.put("calendar", "menu.calendar.2");
		hTitle.put("usleagues", "menu.usleagues.2");
		hTitle.put("project", "menu.project");
		hTitle.put("contribute", "menu.contribute");
		hTitle.put("search", "menu.search.2");
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
		hTitle.put("update-errors", "update.errors");
		hTitle.put("update-redirections", "update.redirections");
		hTitle.put("update-admin", "Admin");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String newURI = null;
		try {
			// Get info on URL and parameters
			String url = ServletHelper.getURL(request);
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
			request.setAttribute("urlLogin", "http://" + url.replaceFirst("http(|s)\\:\\/\\/", "").replaceAll("\\/.*", "") + "/login");
			request.setAttribute("urlEN", url.replaceFirst(".+\\.sporthenon\\.com", "//en.sporthenon.com"));
			request.setAttribute("urlFR", url.replaceFirst(".+\\.sporthenon\\.com", "//fr.sporthenon.com"));
			request.setAttribute("title", StringUtils.getTitle(ResourceUtils.getText(hTitle.containsKey(key) ? hTitle.get(key) : "title", getLocale(request)) + (key.startsWith("update-") ? " | " + ResourceUtils.getText("menu.cbarea", getLocale(request)) : "")));
			request.setAttribute("desc", ResourceUtils.getText(key.equals("index") ? "desc" : "desc." + key, getLocale(request)));
			
			// Specific behaviours
			if (mapParams.containsKey("lang")) {
				request.getSession().setAttribute("locale", String.valueOf(mapParams.get("lang")));
			}
			if (key.equals("project")) {
				mapParams.put("p", 1);
			}
				
			// Determine if it is :
			//		- a search query => servlet call
			// 		- a direct access to JSP
			boolean isRunServlet = (tURI.length > 1 || mapParams.containsKey("p"));
			if (key.equals("update")) {
				if (hPages.containsKey("update-" + tURI[1])) {
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
				StringBuffer url_ = new StringBuffer(hServlet.containsKey(key) ? hServlet.get(key) : "/InfoRefServlet");
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
					dispatcher = request.getRequestDispatcher("/jsp/" + hPages.get(key));
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