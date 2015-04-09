package com.sporthenon.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.utils.res.ResourceUtils;

public abstract class AbstractServlet extends HttpServlet {

	public static final long serialVersionUID = 1L;
	
	protected static Logger logger = Logger.getLogger("sh");
	
	@Override
	protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

	@Override
	protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
	
	protected void init(HttpServletRequest req) {
		req.setAttribute("t1", System.currentTimeMillis());
	}

	protected String getLocale(HttpServletRequest req) {
		ResourceUtils.setLocale(req);
		return String.valueOf(req.getSession().getAttribute("locale"));
	}
	
	protected Contributor getUser(HttpServletRequest req) {
		return (req.getSession().getAttribute("user") != null ? (Contributor) req.getSession().getAttribute("user") : null);
	}
	
	protected void handleException(Throwable e) {
		logger.error(e.getMessage(), e);
	}
	
}