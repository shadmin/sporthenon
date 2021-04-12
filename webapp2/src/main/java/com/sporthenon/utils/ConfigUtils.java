package com.sporthenon.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.meta.Config;

public class ConfigUtils {

	private static final Logger log = Logger.getLogger(ConfigUtils.class.getName());
	
	private static Properties properties = null;
	private static String env = null;
	
	static {
		try {
			properties = new Properties();
			InputStream stream = ConfigUtils.class.getResourceAsStream("/com/sporthenon/utils/res/config.xml");
			if (stream != null) {
				properties.loadFromXML(stream);
			}
			env = System.getenv("SH_ENV");
		}
		catch (IOException e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	public static String getProperty(String key) {
		return (properties != null && properties.containsKey(key) ? String.valueOf(properties.get(key)) : "");
	}
	
	public static String getValue(String key) {
		String value = "";
		try {
			Config c = (Config) DatabaseManager.loadEntity(Config.class, key);
			if (c != null)
				return (c.getKey().startsWith("html") ? c.getValueHtml() : c.getValue());
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
		return value;
	}
	
	public static boolean isProd() {
		return (env != null && env.equalsIgnoreCase("prod"));
	}
	
}