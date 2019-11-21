package com.cospox.elecsim.ui;

import processing.core.PApplet;
import processing.core.PImage;

public class HUDButton {
	protected String name;
	private PImage icon;
	protected float x;
	public HUDButton(String name, PImage icon) {
		this.name = name;
		this.icon = icon;
	}
	
	public void setPos(float x) {
		this.x = x;
	}
	
	public void draw(PApplet applet, float y) {
		applet.image(this.icon, x, y);
	}

}
