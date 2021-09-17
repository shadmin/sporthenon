package com.sporthenon.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.meta.Config;

public class ConfigUtils {

	private static final Logger log = Logger.getLogger(ConfigUtils.class.getName());
	
	private static Properties properties = null;
	private static String env = null;
	private static String credFile = null;
	private static Map<String, String> mapValues = new HashMap<>();
	
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
			if (mapValues.containsKey(key)) {
				value = mapValues.get(key);
			}
			else {
				Config cfg = (Config) DatabaseManager.loadEntity("SELECT * FROM _config WHERE key = ?", Arrays.asList(key), Config.class);
				if (cfg != null) {
					value = cfg.getKey().startsWith("html") ? cfg.getValueHtml() : cfg.getValue();
					mapValues.put(key, value);
				}
			}
		}
		catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
		return value;
	}
	
	public static boolean isProd() {
		return (env != null && env.equalsIgnoreCase("prod"));
	}
	
	public static String getCredFile() {
		if (credFile == null) {
			credFile = System.getenv("SH_CRED_FILE");
		}
		return credFile;
	}
	
}