package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sporthenon.utils.ConfigUtils;

public class NavigationServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	private static HashMap<String, String> hPages;
	private static HashMap<String, String> hServlet;
	private static HashMap<String, String> hMenu;
	
	static {
		hPages = new HashMap<String, String>();
		hPages.put("/home", "home.jsp");
		hPages.put("/results", "db/results.jsp");
		hPages.put("/olympics", "db/olympics.jsp");
		hPages.put("/usleagues", "db/usleagues.jsp");
		hPages.put("/search", "db/search.jsp");
		hPages.put("/project", "project.jsp");
		hPages.put("/login", "login.jsp");
		hPages.put("/logout", "login.jsp");
		hServlet = new HashMap<String, String>();
		hServlet.put("/results", "/ResultServlet");
		hServlet.put("/olympics", "/OlympicsServlet");
		hServlet.put("/usleagues", "/USLeaguesServlet");
		hServlet.put("/search", "/SearchServlet");
		hServlet.put("/ref", "/InfoRefServlet");
		hMenu = new HashMap<String, String>();
		hMenu.put("/home", "home");
		hMenu.put("/results", "results");
		hMenu.put("/olympics", "olympics");
		hMenu.put("/usleagues", "usleagues");
		hMenu.put("/search", "search");
		hMenu.put("/project", "project");
		hMenu.put("/login", "login");
		hMenu.put("/logout", "login");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String env = ConfigUtils.getProperty("env");
			HttpSession session = request.getSession();
			String context = "/sh" + (env != null && env.equalsIgnoreCase("test") ? "test" : "");
			String key = request.getRequestURI().replaceAll(context, "");
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			logger.info("Loaded: " + key);
			if (key.matches(".*update.*") && session.getAttribute("user") == null) {
				key = "/login";
				hParams.clear();
			}
			session.setAttribute("menu", hMenu.get(key));
			RequestDispatcher dispatcher = null;
			if (hParams != null && !hParams.isEmpty()) {
				boolean isPrint = hParams.containsKey("print");
				if (isPrint)
					hParams.remove("print");
				String param = new ArrayList<String>(hParams.keySet()).get(0);
				dispatcher = request.getRequestDispatcher(hServlet.get(key) + "?run=1&p=" + param + (isPrint ? "&print" : ""));
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
