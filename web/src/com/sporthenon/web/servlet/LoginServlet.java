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
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.ServletHelper;

public class LoginServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			String lang = getLocale(request);
			String msg = "";
			boolean isMsg = true;
			if (hParams.containsKey("auth")) {
				String login = String.valueOf(hParams.get("login"));
				String password = String.valueOf(hParams.get("password"));
				String hql = "from Contributor where lower(login)='" + login.toLowerCase() + "' and password='" + StringUtils.toMD5(password) + "'";
				ArrayList<Contributor> lstContributor = (ArrayList<Contributor>) DatabaseHelper.execute(hql);
				if (lstContributor != null && lstContributor.size() > 0) {
					Contributor m = lstContributor.get(0);
					if (m.getActive() != null && m.getActive()) {
						request.getSession().setAttribute("user", m);
						redirect(request, response, "/update/overview", ConfigUtils.getProperty("env").matches("test|prod"));
						isMsg = false;
					}
					else
						msg = ResourceUtils.getText("msg.login.err1", lang);
				}
				else
					msg = ResourceUtils.getText("msg.login.err2", lang);
				if (isMsg)
					request.setAttribute("title", ResourceUtils.getText("login.error", lang) + " | Sporthenon");
			}
			else if (hParams.containsKey("create")) {
				final int MAX_SPORTS = Integer.parseInt(ConfigUtils.getValue("max_contributor_sports"));
				List l = DatabaseHelper.execute("from Contributor where login='" + hParams.get("rlogin") + "'");
				if (l != null && l.size() > 0)
					ServletHelper.writeText(response, "ERR|" + ResourceUtils.getText("msg.login.err3", lang));
				else {
					String rsports = String.valueOf(hParams.get("rsports"));
					if (rsports != null && rsports.split("\\,").length > MAX_SPORTS)
						ServletHelper.writeText(response, "ERR|" + ResourceUtils.getText("msg.login.err4", lang).replaceFirst("\\{1\\}", String.valueOf(MAX_SPORTS)));
					else {
						Contributor m = new Contributor();
						m.setLogin(String.valueOf(hParams.get("rlogin")));
						m.setPassword(StringUtils.toMD5(String.valueOf(hParams.get("rpassword"))));
						m.setEmail(String.valueOf(hParams.get("remail")));
						m.setPublicName(String.valueOf(hParams.get("rpublicname")));
						m.setSports(rsports);
						m.setActive(true);
						m.setAdmin(false);
						DatabaseHelper.saveEntity(m, null);
						ServletHelper.writeText(response, "OK");	
					}
				}
				isMsg = false;
			}
			else if (hParams.containsKey("logout")) {
				request.getSession().removeAttribute("user");
				msg = ResourceUtils.getText("msg.logout", lang);
				isMsg = false;
				redirect(request, response, "/", true);
			}
			if (isMsg) {
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/login.jsp");
				if (dispatcher != null)
					dispatcher.forward(request, response);
			}
		}
		catch (Exception e) {
			handleException(request, response, e);
		}
	}

}