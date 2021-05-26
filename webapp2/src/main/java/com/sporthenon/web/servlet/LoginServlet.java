package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.sporthenon.web.ServletHelper;

@WebServlet(
    name = "LoginServlet",
    urlPatterns = {"/LoginServlet"}
)
public class LoginServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> mapParams = ServletHelper.getParams(request);
			String lang = getLocale(request);
			String msg = "";
			boolean isMsg = true;
			if (mapParams.containsKey("auth")) {
				String userName = String.valueOf(mapParams.get("username"));
				String password = String.valueOf(mapParams.get("password"));
				String sql = "SELECT * FROM _contributor WHERE LOWER(login) = ? and password = ?";
				List<Contributor> contributors = (List<Contributor>) DatabaseManager.executeSelect(sql, Arrays.asList(new Object[]{userName.toLowerCase(), StringUtils.toMD5(password)}), Contributor.class);
				if (contributors != null && contributors.size() > 0) {
					Contributor m = contributors.get(0);
					if (Boolean.TRUE.equals(m.getActive())) {
						request.getSession().setAttribute("user", m);
						redirect(request, response, "/update/overview", ConfigUtils.isProd());
						isMsg = false;
					}
					else {
						msg = ResourceUtils.getText("msg.login.err1", lang);
					}
				}
				else {
					msg = ResourceUtils.getText("msg.login.err2", lang);
				}
				if (isMsg) {
					request.setAttribute("title", ResourceUtils.getText("login.error", lang) + " | Sporthenon");
				}
			}
			else if (mapParams.containsKey("create")) {
				final int MAX_SPORTS = Integer.parseInt(ConfigUtils.getValue("max_contributor_sports"));
				List<Contributor> l = (List<Contributor>) DatabaseManager.executeSelect("SELECT * FROM _contributor WHERE login = ?", Arrays.asList(mapParams.get("rlogin")), Contributor.class);
				if (l != null && l.size() > 0) {
					ServletHelper.writeText(response, "ERR|" + ResourceUtils.getText("msg.login.err3", lang));
				}
				else {
					String rsports = String.valueOf(mapParams.get("rsports"));
					if (rsports != null && rsports.split("\\,").length > MAX_SPORTS) {
						ServletHelper.writeText(response, "ERR|" + ResourceUtils.getText("msg.login.err4", lang).replaceFirst("\\{1\\}", String.valueOf(MAX_SPORTS)));
					}
					else {
						Contributor m = new Contributor();
						m.setLogin(String.valueOf(mapParams.get("rlogin")));
						m.setPassword(StringUtils.toMD5(String.valueOf(mapParams.get("rpassword"))));
						m.setEmail(String.valueOf(mapParams.get("remail")));
						m.setPublicName(String.valueOf(mapParams.get("rpublicname")));
						m.setSports(rsports);
						m.setActive(true);
						m.setAdmin(false);
						DatabaseManager.saveEntity(m, null);
						ServletHelper.writeText(response, "OK");	
					}
				}
				isMsg = false;
			}
			else if (mapParams.containsKey("logout")) {
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