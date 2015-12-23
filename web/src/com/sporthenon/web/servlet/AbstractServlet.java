package com.sporthenon.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.ServletHelper;

public abstract class AbstractServlet extends HttpServlet {

	public static final long serialVersionUID = 1L;
	
	protected static Logger logger = Logger.getLogger("sh");
	
	@Override
	protected abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	@Override
	protected abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	protected void init(HttpServletRequest request) {
		request.setAttribute("t1", System.currentTimeMillis());
	}

	protected String getLocale(HttpServletRequest request) {
		ResourceUtils.setLocale(request);
		return String.valueOf(request.getSession().getAttribute("locale"));
	}
	
	protected Contributor getUser(HttpServletRequest request) {
		return (request.getSession().getAttribute("user") != null ? (Contributor) request.getSession().getAttribute("user") : null);
	}
	
	protected void handleException(HttpServletRequest request, HttpServletResponse response, Throwable e) throws ServletException, IOException {
		logger.error(e.getMessage(), e);
		request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
	}
	
	protected void redirect(HttpServletRequest request, HttpServletResponse response, String path, boolean ssl) throws ServletException, IOException {
		String url = ServletHelper.getURL(request);
		String protocol = url.replaceFirst("\\:.*", "");
		url = url.replaceFirst(protocol + "\\:\\/\\/", "").replaceAll("\\/.*", "");
		url = (ssl ? "https" : protocol) + "://" + url + path;
		response.sendRedirect(url);
	}
	
}