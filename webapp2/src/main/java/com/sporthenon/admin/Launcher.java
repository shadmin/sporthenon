package com.sporthenon.admin;

import com.sporthenon.admin.window.JMainFrame;

public class Launcher {

	public static void main(String[] args) {
		try {
//				FileAppender app = new FileAppender();
//				app.setName("sh");
//				app.setFile(configDir + "\\sh.log");
//				app.setLayout(new PatternLayout("%d %5p - %m%n"));
//				app.setThreshold(Level.INFO);
//				app.setAppend(true);
//				app.activateOptions();
//				Logger.getRootLogger().addAppender(app);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		new JMainFrame();
	}

}