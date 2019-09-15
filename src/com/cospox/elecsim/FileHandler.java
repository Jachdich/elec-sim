package com.cospox.elecsim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
	public static void write(String fileName, String file) {
		FileWriter out = null;
		try {
			out = new FileWriter(fileName);
			out.write(file);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String read(String filename) {
		BufferedReader reader = null;
		String out = "";
		try {
			String line = null;
			reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				out += line;
			}
		} catch (IOException e) {
			
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
					//TODO AAAHHHH HELP
				}
			}
		}
		return out;
	}
}
