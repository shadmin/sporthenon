package com.sporthenon.explorer;

import java.io.IOException;
import java.net.URL;

public class Utils {

	public static String getInstallFolder() {
		return "C:\\TEMP\\~SPORTHENON\\explorer\\";
	}
	
	public static URL getFileURL(String s) throws IOException {
		return new URL("file:///" + getInstallFolder().replaceAll("\\\\", "/") + s);
	}
	
}
