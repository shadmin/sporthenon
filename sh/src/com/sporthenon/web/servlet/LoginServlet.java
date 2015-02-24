package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.meta.Member;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;

public class LoginServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			String msg = "";
			boolean isMsg = true;
			if (hParams.containsKey("auth")) {
				String login = String.valueOf(hParams.get("login"));
				String password = String.valueOf(hParams.get("password"));
				String hql = "from Member where lower(login)='" + login.toLowerCase() + "' and password='" + StringUtils.toMD5(password) + "'";
				ArrayList<Member> lstMember = (ArrayList<Member>) DatabaseHelper.execute(hql);
				if (lstMember != null && lstMember.size() > 0) {
					Member m = lstMember.get(0);
					if (m.getActive() != null && m.getActive()) {
						request.getSession().setAttribute("user", m);
						response.sendRedirect(ConfigUtils.getProperty("url"));
						isMsg = false;
					}
					else
						msg = ResourceUtils.getText("msg.login.err1", getLocale(request));
				}
				else
					msg = ResourceUtils.getText("msg.login.err2", getLocale(request));
			}
			else if (hParams.containsKey("create")) {
				List l = DatabaseHelper.execute("from Member where login='" + hParams.get("rlogin") + "'");
				if (l != null && l.size() > 0)
					ServletHelper.writeText(response, "ERR|" + ResourceUtils.getText("msg.login.err3", getLocale(request)));
				else {
					Member m = new Member();
					m.setLogin(String.valueOf(hParams.get("rlogin")));
					m.setPassword(StringUtils.toMD5(String.valueOf(hParams.get("rpassword"))));
					m.setEmail(String.valueOf(hParams.get("remail")));
					m.setLastName(String.valueOf(hParams.get("rlastname")));
					m.setFirstName(String.valueOf(hParams.get("rfirstname")));
					m.setActive(false);
					DatabaseHelper.saveEntity(m, null);
					ServletHelper.writeText(response, ResourceUtils.getText("msg.registered", getLocale(request)) + "&nbsp;<a href='javascript:' onclick='rauth()'>" + ResourceUtils.getText("menu.login", getLocale(request)) + "</a>");					
				}
				isMsg = false;
			}
			else if (hParams.containsKey("logout")) {
				request.getSession().removeAttribute("user");
				msg = ResourceUtils.getText("msg.logout", getLocale(request));
				isMsg = false;
				response.sendRedirect(ConfigUtils.getProperty("url"));
			}
			if (isMsg) {
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/login.jsp");
				if (dispatcher != null)
					dispatcher.forward(request, response);
			}
		}
		catch (Exception e) {
			handleException(e);
		}
	}

}