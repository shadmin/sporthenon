package com.sporthenon.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.meta.Config;

public class ConfigUtils {

	private static Properties properties = null;
	
	static {
		try {
			properties = new Properties();
			InputStream stream = ConfigUtils.class.getResourceAsStream("/config.xml");
			if (stream != null)
				properties.loadFromXML(stream);
		}
		catch (IOException e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
	}
	
	public static String getProperty(String key) {
		return (properties != null && properties.containsKey(key) ? String.valueOf(properties.get(key)) : "");
	}
	
	public static String getValue(String key) {
		String value = "";
		try {
			Config c = (Config) DatabaseHelper.loadEntity(Config.class, key);
			if (c != null)
				return c.getValue();
		}
		catch (Exception e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
		return value;
	}
	
}