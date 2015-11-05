package com.sporthenon.admin;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.sporthenon.admin.window.JMainFrame;
import com.sporthenon.utils.RegUtils;
import com.sporthenon.utils.StringUtils;

public class Launcher {

	public static void main(String[] args) {
		// Congigure Log4J
		try {
			String configDir = RegUtils.readRegistry("HKEY_CURRENT_USER\\Software\\Sporthenon", "configDir");
			if (StringUtils.notEmpty(configDir)) {
				FileAppender app = new FileAppender();
				app.setName("sh");
				app.setFile(configDir + "\\sh.log");
				app.setLayout(new PatternLayout("%d %5p - %m%n"));
				app.setThreshold(Level.INFO);
				app.setAppend(true);
				app.activateOptions();
				Logger.getRootLogger().addAppender(app);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		new JMainFrame();
	}

}