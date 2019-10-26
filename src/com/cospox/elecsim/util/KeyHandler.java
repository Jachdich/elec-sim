package com.cospox.elecsim.util;

import java.util.HashMap;

public class KeyHandler {
	private HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();
	private HashMap<Character, Boolean> keyChars = new HashMap<Character, Boolean>();
	
	public KeyHandler() {
		for (int i = 0; i < 256; i++) { this.keys.put(i, false); }
	}
	public void pressed(int keyCode, char key) {
		this.keys.put(keyCode, true);
		this.keyChars.put(key, true);
	}
	public void released(int keyCode, char key) {
		this.keys.put(keyCode, false);
		this.keyChars.put(key, false);
	}
	
	public boolean get(int keyCode) {
		return this.keys.get(keyCode);
	}
	
	public boolean get(char key) {
		if (this.keyChars.containsKey(key)) {
			return this.keyChars.get(key);
		} else {
			return false;
		}
	}
	
	public boolean shift() {
		return this.keys.get(16);
	}
	public boolean ctrl() {
		return this.keys.get(17);
	}
}
