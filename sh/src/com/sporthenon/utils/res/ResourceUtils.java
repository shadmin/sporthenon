package com.sporthenon.utils.res;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

public class ResourceUtils {

	private static Properties res_properties = null;
	
	static {
		try {
			res_properties = new Properties();
			InputStream stream = ResourceUtils.class.getResourceAsStream("/com/sporthenon/utils/res/resource.properties");
			if (stream != null)
				res_properties.load(stream);
		}
		catch (IOException e) {
			Logger.getLogger("sh").error(e.getMessage(), e);
		}
	}
	
	public static String get(String key) {
		return (res_properties != null && res_properties.containsKey(key) ? String.valueOf(res_properties.get(key)) : "");
	}
	
	public static ImageIcon getIcon(String name) {
		return new ImageIcon(ResourceUtils.class.getResource("/com/sporthenon/utils/res/img/" + name));
	}
	
}
