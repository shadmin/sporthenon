package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.function.ContributorBean;
import com.sporthenon.utils.HtmlUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class ProjectServlet extends AbstractServlet {

private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			init(request);
			// Contributors
			StringBuffer html = new StringBuffer();
			Collection c = DatabaseHelper.call("Contributors", null);
			for (Object obj : c) {
				ContributorBean bean = (ContributorBean) obj;
				String sports = null;
				if (StringUtils.notEmpty(bean.getSports())) {
					List<String> l = Arrays.asList((getLocale(request).equalsIgnoreCase("fr") ? bean.getSportsFR() : bean.getSports()).split("\\|"));
					Collections.sort(l);
					sports = StringUtils.implode(l, "<br/>");
				}
				html.append("<tr><td><a href='" + HtmlUtils.writeLink(Contributor.alias, bean.getId(), null, bean.getLogin()) + "'>" + bean.getLogin() + "</a></td>");
				html.append("<td>" + (StringUtils.notEmpty(bean.getName()) ? bean.getName() : "-") + "</td>");
				html.append("<td>" + (StringUtils.notEmpty(sports) ? sports : "-") + "</td>");
				html.append("<td><img style='vertical-align:middle;padding-bottom:2px;' alt='adds' title='" + ResourceUtils.getText("co.adds", getLocale(request)) + "' src='/img/project/adds.png'/>&nbsp;" + bean.getCountA() + "&nbsp;<img style='vertical-align:middle;padding-bottom:2px;' alt='updates' title='" + ResourceUtils.getText("co.updates", getLocale(request)) + "' src='/img/project/updates.png'/>&nbsp;" + bean.getCountU() + "</td></tr>");
			}
			request.setAttribute("contributors", html.toString());
			request.setAttribute("t2", System.currentTimeMillis());
			request.getRequestDispatcher("/jsp/project.jsp").forward(request, response);
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}
	
}