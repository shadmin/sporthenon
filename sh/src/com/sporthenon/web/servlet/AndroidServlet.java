package com.sporthenon.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.PicklistBean;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.utils.res.ResourceUtils;

public class AndroidServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;

	public AndroidServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HashMap<String, Object> hParams = ServletHelper.getParams(request);
			String lang = (hParams.containsKey("lang") ? String.valueOf(hParams.get("lang")) : ResourceUtils.LGDEFAULT);
			Collection<PicklistBean> cPicklist = new ArrayList<PicklistBean>();
			String p = String.valueOf(hParams.get("p"));
			String p2 = String.valueOf(hParams.get("p2"));
			if (p2.equalsIgnoreCase(Sport.alias)) {
				cPicklist.addAll(DatabaseHelper.getEntityPicklist(Sport.class, null, null, lang));
			}
			else if (p2.equalsIgnoreCase(Championship.alias)) {
				String filter = "sport.id=" + p;
				cPicklist.addAll(DatabaseHelper.getPicklist(Result.class, "championship", filter, null, (short) 1, lang));
			}
			else if (p2.equalsIgnoreCase(Event.alias)) {
				String[] t = p.split("\\-");
				String filter = "sport.id=" + t[0] + " and championship.id=" + t[1];
				cPicklist.addAll(DatabaseHelper.getPicklist(Result.class, "event", filter, null, (short) 1, lang));
			}
			for (PicklistBean plb : cPicklist)
				plb.setText(plb.getText().replaceAll("^false\\-", "").replaceAll("^true\\-", "&dagger;&nbsp;"));
			ServletHelper.writePicklist(response, cPicklist, p2);
		}
		catch (Exception e) {
			handleException(e);
		}
	}

}
