package com.sporthenon.web.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.entity.meta.Redirection;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.ServletHelper;

@WebServlet(
    name = "NavigationServlet",
    urlPatterns = {"/index", "/results/*", "/calendar/*", "/olympics/*", "/usleagues/*", "/search/*", "/project/*", "/contribute/*", "/login/*", "/update/*", "/android/*",
    		"/athlete/*", "/championship/*", "/city/*", "/complex/*", "/contributor/*", "/country/*", "/entity/*", "/event/*", "/olympicgames/*", "/result/*", "/sport/*", "/usstate/*", "/team/*", "/year/*",
    		"/fr/*", "/en/*"}
)
public class NavigationServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	private static Map<String, String> hPages;
	private static Map<String, String> hServlet;
	private static Map<String, String> hTitle;
	
	static {
		hPages = new HashMap<>();
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
		String url = ServletHelper.getURL(request);
		String newURI = null;
		try {
			String ua = request.getHeader("user-agent");
			boolean isTestProd = ConfigUtils.getProperty("env").matches("test|prod");
			boolean isUserSession = (request.getSession() != null && request.getSession().getAttribute("user") != null);
			if (url.matches(".*(\\/null)$")) {
				throw new NullParameterException();
			}
			if (url.matches(".*\\/(athletes|championships|cities|complexes|countries|events|sports|usstates|teams|years)\\/.*")) {
				throw new OldPatternException();
			}
			String sql = "SELECT * FROM _redirection WHERE previous_path = ? ORDER BY id DESC";
			Redirection re = (Redirection) DatabaseManager.loadEntity(sql, Arrays.asList(URLDecoder.decode(request.getRequestURI(), "UTF-8").replaceAll("'", "''")), Redirection.class);
			if (re != null) {
				newURI = re.getCurrentPath();
				throw new ObsoleteURLException();
			}
//			if (!isBot(request) && !url.contains("/ajax") && !url.contains("/load") && !url.contains("/check-progress-import"))
//				log.log(Level.WARNING, "[" + ua + "] " + url);
			if (ConfigUtils.getProperty("env").matches("local|test")) {
				Enumeration<String> hn = request.getHeaderNames();
				while (hn.hasMoreElements()) {
					String hn_ = (String) hn.nextElement();
					log.log(Level.WARNING, hn_+ ": " + request.getHeader(hn_));
				}
			}
			String[] tURI = request.getRequestURI().substring(1).split("\\/", 0);
			String key = tURI[0];
			if (key.isEmpty()) {
				key = "index";
			}
			if (isTestProd) {
				if (key != null && key.equals("update") && !isUserSession)
					throw new NotLoggedInException();
				else if (isUserSession) {
//					String protocol = request.getHeader("x-forwarded-proto");
//					if (!protocol.equals("https"))
//						throw new HttpsException();
				}
			}
			if (key != null && key.equals("update") && url.matches(".*\\/admin$") && isUserSession) {
				Contributor cb = (Contributor) request.getSession().getAttribute("user");
				if (!cb.isAdmin())
					throw new NotAdminException();
			}
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			url = url.replaceAll("\\&", "&amp;");
			request.setAttribute("url", url);
//			request.setAttribute("urlLogin", isTestProd ? "https://" + url.replaceFirst("http(|s)\\:\\/\\/", "").replaceAll("\\/.*", "") + "/login" : "http://localhost/login");
			request.setAttribute("urlLogin", isTestProd ? "http://" + url.replaceFirst("http(|s)\\:\\/\\/", "").replaceAll("\\/.*", "") + "/login" : "http://localhost/login");
			request.setAttribute("urlEN", url.replaceFirst(".+\\.sporthenon\\.com", "//en.sporthenon.com"));
			request.setAttribute("urlFR", url.replaceFirst(".+\\.sporthenon\\.com", "//fr.sporthenon.com"));
			RequestDispatcher dispatcher = null;
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
			request.setAttribute("title", StringUtils.getTitle(ResourceUtils.getText(hTitle.containsKey(key) ? hTitle.get(key) : "title", getLocale(request)) + (key.startsWith("update-") ? " | " + ResourceUtils.getText("menu.cbarea", getLocale(request)) : "")));
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
			redirect(request, response, url.replaceAll("\\/athletes", "/athlete").replaceAll("\\/championships", "/championship").replaceAll("\\/cities", "/city").replaceAll("\\/complexes", "/complex").replaceAll("\\/countries", "/country").replaceAll("\\/events", "/event").replaceAll("\\/sports", "/sport").replaceAll("\\/usstates", "/usstate").replaceAll("\\/teams", "/team").replaceAll("\\/years", "/year"), false);
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
	
	class OldPatternException extends Exception{
		private static final long serialVersionUID = 1L;
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