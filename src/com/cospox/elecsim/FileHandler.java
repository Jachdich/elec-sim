package com.cospox.elecsim;

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
		FileReader in = null;
		String out = null;
		try {
			in = new FileReader(filename);
			int c;
			out = "";
			while ((c = in.read()) != -1) {
				out += (char)c;
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return out;
	}
}
