package com.cospox.elecsim;

import processing.core.PApplet;

public class Connection {
	public boolean on;
	public Vector  pos;
	public boolean selected;
	public static int WIDTH  = 2;
	public static int HEIGHT = 10;
	public int posInComponents;
	public int posInComponent;
	public Connection(Vector pos, Vector posInArrays) {
		this.pos = pos;
		this.posInComponents = (int)posInArrays.x;
		this.posInComponent  = (int)posInArrays.y;
	}

	public void draw(PApplet applet) {
		if (this.selected) { applet.stroke(255, 20, 20); }
		else { applet.noStroke(); }
		if (this.on) { applet.fill(200);
		} else { applet.fill(100); }
		applet.rect(this.pos.x,
				this.pos.y,
				WIDTH, HEIGHT);
	}

	public void update() {

	}

	public void on() {
		this.on = true;
	}

	public void off() {
		this.on = false;
	}
}
