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
import com.sporthenon.utils.StringUtils;


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
						response.sendRedirect("home");
						isMsg = false;
					}
					else
						msg = "Login failed. The user is not active.";
				}
				else
					msg = "Login failed. Please check your login and password.";
			}
			else if (hParams.containsKey("create")) {
				List l = DatabaseHelper.execute("from Member where login='" + hParams.get("r-login") + "'");
				if (l != null && l.size() > 0)
					ServletHelper.writeText(response, "ERR|Login already in use. Please choose another one.");
				else {
					Member m = new Member();
					m.setLogin(String.valueOf(hParams.get("r-login")));
					m.setPassword(StringUtils.toMD5(String.valueOf(hParams.get("r-password"))));
					m.setEmail(String.valueOf(hParams.get("r-email")));
					m.setLastName(String.valueOf(hParams.get("r-lastname")));
					m.setFirstName(String.valueOf(hParams.get("r-firstname")));
					m.setActive(false);
					DatabaseHelper.saveEntity(m, null);
					ServletHelper.writeText(response, "Account created successfully. <a href='javascript:' onclick='rauth()'>Click here</a> to log in.");					
				}
				isMsg = false;
			}
			else if (hParams.containsKey("logout")) {
				request.getSession().removeAttribute("user");
				msg = "You were logged out.";
				isMsg = true;
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
