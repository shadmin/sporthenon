package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.utils.res.ResourceUtils;

public class NavigationServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	private static HashMap<String, String> hPages;
	private static HashMap<String, String> hServlet;
	private static HashMap<String, String> hTitle;
	
	static {
		hPages = new HashMap<String, String>();
		hPages.put("results", "db/results.jsp");
		hPages.put("olympics", "db/olympics.jsp");
		hPages.put("usleagues", "db/usleagues.jsp");
		hPages.put("search", "db/search.jsp");
		hPages.put("project", "project.jsp");
		hPages.put("contribute", "contribute.jsp");
		hPages.put("login", "login.jsp");
		hPages.put("update", "update.jsp");
		hServlet = new HashMap<String, String>();
		hServlet.put("results", "/ResultServlet");
		hServlet.put("olympics", "/OlympicsServlet");
		hServlet.put("usleagues", "/USLeaguesServlet");
		hServlet.put("search", "/SearchServlet");
		hServlet.put("login", "/LoginServlet");
		hServlet.put("details", "/InfoRefServlet");
		hServlet.put("update", "/UpdateServlet");
		hTitle = new HashMap<String, String>();
		hTitle.put("results", "menu.results.2");
		hTitle.put("olympics", "menu.olympics.2");
		hTitle.put("usleagues", "menu.usleagues.2");
		hTitle.put("project", "menu.project");
		hTitle.put("contribute", "menu.contribute");
		hTitle.put("search", "menu.search");
		hTitle.put("login", "menu.login");
		hTitle.put("update", "menu.update");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String[] tURI = request.getRequestURI().substring(1).split("\\/", -1);
			String key = tURI[0];
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			RequestDispatcher dispatcher = null;
//			if (key != null && key.equals("update") && (request.getSession() == null || request.getSession().getAttribute("user") == null))
//				key = "login";
			if (tURI.length > 1) {
				Object export = hParams.get("export");
				boolean isPrint = hParams.containsKey("print");
				if (export != null)
					hParams.remove("export");
				if (isPrint)
					hParams.remove("print");
				dispatcher = request.getRequestDispatcher(hServlet.get(key) + "?run=1&t=" + System.currentTimeMillis() + "&p=" + tURI[1] + (tURI.length > 2 ? "&p2=" + tURI[2] : "") + (isPrint ? "&print" : "") + (export != null ? "&export=" + export : ""));
			}
			else {
				request.setAttribute("title", ResourceUtils.getText(hTitle.containsKey(key) ? hTitle.get(key) : "title", getLocale(request)) + " | SPORTHENON");
				request.setAttribute("menu", key.substring(1));
				dispatcher = request.getRequestDispatcher("/jsp/" + hPages.get(key));
			}
		    if (dispatcher != null)
		    	dispatcher.forward(request, response);
		}
		catch (Exception e) {
			handleException(e);
		}
	}

}