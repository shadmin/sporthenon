package com.sporthenon.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class RegUtils {

	public static final String readRegistry(String location, String key){
		try {
			Process process = Runtime.getRuntime().exec("reg query " + '"'+ location + "\" /v " + key);
			StreamReader reader = new StreamReader(process.getInputStream());
			reader.start();
			process.waitFor();
			reader.join();
			String[] parsed = reader.getResult().split("\\s+");
			StringBuffer sbValue = new StringBuffer();
			boolean isRegSZ = false;
			for (String s : parsed) {
				if (isRegSZ)
					sbValue.append(sbValue.toString().length() > 0 ? " " : "").append(s);
				isRegSZ |= s.trim().equalsIgnoreCase("reg_sz");
			}
			return sbValue.toString();
		}
		catch (Exception e) {}
		return null;
	}

	private static class StreamReader extends Thread {
		private InputStream is;
		private StringWriter sw= new StringWriter();
		public StreamReader(InputStream is) {
			this.is = is;
		}
		public void run() {
			try {
				int c;
				while ((c = is.read()) != -1)
					sw.write(c);
			} catch (IOException e) {}
		}

		public String getResult() {
			return sw.toString();
		}
	}
	
}