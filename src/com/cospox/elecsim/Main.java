package com.cospox.elecsim;

import com.cospox.elecsim.util.Global;
import com.cospox.elecsim.util.HelperFunctions;

import processing.core.PApplet;
import processing.event.MouseEvent;

public class Main extends PApplet{
	public Game game;
	public static void main(String[] args) {
		PApplet.main("com.cospox.elecsim.Main");
	}

	@Override
	public void settings() {
		//int offset = 50;
		//int h = 720 - offset;
		this.size(480, 640, P2D);
	}

	@Override
	public void setup() {
		this.frameRate(60);
		surface.setResizable(true);
		this.game = new Game(this);
	}

	@Override
	public void draw() {
		this.game.draw(this);
		if (Global.debug) {
			
			this.fill(255, 0, 0);
			this.text(this.frameRate, 2, 38);
			Runtime rt = Runtime.getRuntime();
			double usedMiB = HelperFunctions.round((rt.totalMemory() - rt.freeMemory()) / 1024.0 / 1024.0, 3);
			double totalMiB = HelperFunctions.round(rt.totalMemory() / 1024.0 / 1024.0, 3);
			this.text(Double.toString(usedMiB) + "/" + Double.toString(totalMiB), 2, 50);
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
