package com.cospox.elecsim;

import processing.core.PApplet;
import processing.event.MouseEvent;
import com.cospox.elecsim.Global;

public class Main extends PApplet{
	public Game game;
	public static void main(String[] args) {
		PApplet.main("com.cospox.elecsim.Main");
	}

	@Override
	public void settings() {
		//int offset = 50;
		//int h = 720 - offset;
		this.size(480, 640, P3D);
	}

	@Override
	public void setup() {
		this.frameRate(1000);
		surface.setResizable(true);
		this.game = new Game(this);
	}

	@Override
	public void draw() {
		this.game.draw(this);
		if (Global.debug) {
			this.fill(255, 0, 0);
			this.text(this.frameRate, 2, 38);
			this.fill(255);
		}
	}

	@Override
	public void mouseClicked() {
		this.game.mouseClicked(this);
	}

	@Override
	public void keyPressed() {
		//println("'" + key + "' " + keyCode);
		this.game.keyPressed(this.key, this.keyCode);
	}
	@Override
	public void keyReleased() {
		this.game.keyReleased(this.key, this.keyCode);
	}

	@Override
	public void mouseWheel(MouseEvent event) {
		this.game.mouseWheel(event, this);
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		this.game.mouseDragged(event, this);
	}
	
	@Override
	public void mousePressed() {
		this.game.mousePressed(this);
	}
	
	@Override
	public void mouseReleased() {
		this.game.mouseReleased(this);
	}
	
	@Override
	public void exit() {
		if (this.game.dispose()) {
			super.exit();
		}
	}
}
