package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sporthenon.utils.res.ResourceUtils;

public class NavigationServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	private static HashMap<String, String> hPages;
	private static HashMap<String, String> hServlet;
	private static HashMap<String, String> hMenu;
	private static HashMap<String, String> hTitle;
	
	static {
		hPages = new HashMap<String, String>();
//		hPages.put("/home", "index.jsp");
		hPages.put("/results", "db/results.jsp");
		hPages.put("/olympics", "db/olympics.jsp");
		hPages.put("/usleagues", "db/usleagues.jsp");
		hPages.put("/search", "db/search.jsp");
		hPages.put("/project", "project.jsp");
		hServlet = new HashMap<String, String>();
		hServlet.put("/results", "/ResultServlet");
		hServlet.put("/olympics", "/OlympicsServlet");
		hServlet.put("/usleagues", "/USLeaguesServlet");
		hServlet.put("/search", "/SearchServlet");
		hServlet.put("/ref", "/InfoRefServlet");
//		hServlet.put("/re", "/ResultServlet");
//		hServlet.put("/ol", "/OlympicsServlet");
//		hServlet.put("/ul", "/USLeaguesServlet");
//		hServlet.put("/se", "/SearchServlet");
//		hServlet.put("/rf", "/InfoRefServlet");
		hMenu = new HashMap<String, String>();
//		hMenu.put("/home", "home");
		hMenu.put("/results", "results");
		hMenu.put("/olympics", "olympics");
		hMenu.put("/usleagues", "usleagues");
		hMenu.put("/search", "search");
		hMenu.put("/project", "project");
		hTitle = new HashMap<String, String>();
		hTitle.put("/results", "menu.results.2");
		hTitle.put("/olympics", "menu.olympics.2");
		hTitle.put("/usleagues", "menu.usleagues.2");
		hTitle.put("/search", "menu.search.2");
		hTitle.put("/project", "menu.project.2");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			String uri = request.getRequestURI();
			String key = uri.substring(uri.lastIndexOf("/"));
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			logger.info("Loaded: " + key);
			session.setAttribute("menu", hMenu.get(key));
			session.setAttribute("title", hTitle.containsKey(key) ? ResourceUtils.getText(hTitle.get(key), getLocale(request)) + " | SPORTHENON" : null);
			RequestDispatcher dispatcher = null;
			if (hParams != null && !hParams.isEmpty()) {
				Object export = hParams.get("export");
				boolean isPrint = hParams.containsKey("print");
				if (export != null)
					hParams.remove("export");
				if (isPrint)
					hParams.remove("print");
				//String param = new ArrayList<String>(hParams.keySet()).get(0);
				Object param = new ArrayList<Object>(hParams.values()).get(0);
				dispatcher = request.getRequestDispatcher(hServlet.get(key) + "?run=1&p=" + param + (isPrint ? "&print" : "") + (export != null ? "&export=" + export : ""));
			}
			else
				dispatcher = request.getRequestDispatcher("/jsp/" + hPages.get(key));
		    if (dispatcher != null)
		    	dispatcher.forward(request, response);
		}
		catch (Exception e) {
			handleException(e);
		}
	}

}