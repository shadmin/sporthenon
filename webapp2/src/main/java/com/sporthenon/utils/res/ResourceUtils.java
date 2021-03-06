package com.sporthenon.utils.res;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.ImageIcon;

import com.sporthenon.web.ServletHelper;

public class ResourceUtils {

	private static final Logger log = Logger.getLogger(ResourceUtils.class.getName());
	
	private static HashMap<String, Properties> HP = null;
	public static final String LGDEFAULT = "en";
	
	static {
		init();
	}
	
	private static void init() {
		try {
			HP = new HashMap<String, Properties>();
			for (String s : new String[] {"en", "fr"}) {
				Properties p = new Properties();
				InputStream stream = ResourceUtils.class.getResourceAsStream("/com/sporthenon/utils/res/TEXT." + s.toUpperCase() + ".xml");
				if (stream != null) {
					p.loadFromXML(stream);
					HP.put(s, p);
				}
			}
		}
		catch (IOException e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	public static String getText(String key, String lang) {
		lang = lang.replace("_", "");
		if (!HP.containsKey(lang.toLowerCase())) {
			lang = LGDEFAULT;
		}
		Properties p = HP.get(lang.toLowerCase());
		return (p != null && p.containsKey(key) ? String.valueOf(p.get(key)) : key);
	}
	
	public static ImageIcon getIcon(String name) {
		return new ImageIcon(ResourceUtils.class.getResource("/com/sporthenon/utils/res/img/" + name));
	}
	
	public static void setLocale(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("locale") == null) {
			String lang = LGDEFAULT;
			try {
				String url = ServletHelper.getURL(request);
				if (url.matches(".*\\/\\/[a-z]{2}\\..*"))
					lang = url.substring(url.indexOf("//") + 2).substring(0, 2);
				else {
					if (request.getLocale() != null)
						lang = request.getLocale().toString().substring(0, 2);
					if (HP == null || !HP.containsKey(lang.toLowerCase())) {
						init();
						lang = LGDEFAULT;
					}
				}
			}
			catch (Exception e) {
				log.log(Level.WARNING, e.getMessage(), e);
				
			}
			finally {
				session.setAttribute("locale", lang);
			}
		}
	}
	
	public static String getLocaleParam(String lang) {
		return (lang != null && !lang.equalsIgnoreCase(LGDEFAULT) ? "_" + lang : "");
	}
	
}