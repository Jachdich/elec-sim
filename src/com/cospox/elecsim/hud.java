package com.cospox.elecsim;

import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PImage;

public class hud {
	private HashMap<String, PImage> images = new HashMap<String, PImage>();
	private final int NUM_COMPONENTS = 6;
	private Vector mouseStart = new Vector();
	public boolean canSelect = false;
	private boolean[] buttonsPressed = new boolean[10];
	public hud(PApplet applet) {
		//Add new components
		this.images.put("selectImage", applet.loadImage("assets/images/mouse.png"));
		this.images.put("AndGate",     applet.loadImage("assets/images/and_gate.png"));
		this.images.put("Switch",      applet.loadImage("assets/images/switch.png"));
		this.images.put("OrGate",      applet.loadImage("assets/images/or_gate.png"));
		this.images.put("XorGate",     applet.loadImage("assets/images/xor_gate.png"));
		this.images.put("NotGate",     applet.loadImage("assets/images/not_gate.png"));
		this.images.put("Joint",       applet.loadImage("assets/images/joint.png"));

		//Add new components
		this.addSmallImage("OrGate");
		this.addSmallImage("AndGate");
		this.addSmallImage("Switch");
		this.addSmallImage("XorGate");
		this.addSmallImage("NotGate");
		this.addSmallImage("Joint");
	}

	private void addSmallImage(String imageName) {
		PImage temp = this.images.get(imageName).copy();
		temp.resize(20, 20);
		this.images.put(imageName + "S", temp);
	}

	public void draw(PApplet applet, Game game) {
		if (applet.mousePressed
				&& game.selectedTool[0] == "select"
				&& this.canSelect
				&& applet.mouseButton == 37 /*left button*/) {
			//draw 'selection' rectangle
			applet.stroke(255, 150, 150, 128);
			applet.fill(255, 100, 100, 50);
			applet.strokeWeight(2);
			applet.rectMode(PApplet.CORNERS);
			applet.rect(this.mouseStart.x, this.mouseStart.y, applet.mouseX, applet.mouseY);
			applet.strokeWeight(1);
			applet.rectMode(PApplet.CORNER);
			game.selectComponentsInRect(this.mouseStart, new Vector(applet.mouseX, applet.mouseY));
		}
		
		this.drawButtons(applet, game);
		
		applet.fill(0);
		applet.text("Tool (t)", applet.width - 46, 10);
		if (game.selectedTool[0] == "wire") {
			applet.stroke(0);
			applet.line(applet.width - 20, 30, applet.width - 10, 20);
		} else if (game.selectedTool[0] == "component") {
			PImage i = this.images.get(game.selectedComponent + "S").copy();
			i.resize(20, 20);
			applet.image(i, applet.width - 20, 20);
		} else if (game.selectedTool[0] == "select") {
			applet.image(this.images.get("selectImage"), applet.width - 20, 20);
		}
		
		applet.stroke(0);
		applet.fill(0);
		applet.text("Wire mode (y)", applet.width - 87, 54);
		if (game.states.get("wireMode")) {
			applet.line(applet.width - 20, 65, applet.width - 5, 65);
			applet.line(applet.width - 5, 65, applet.width - 5, 75);
		} else { applet.line(applet.width - 20, 65, applet.width - 5, 75); }

		//Add new components
		applet.image(this.images.get("Switch"),  4 + 32 * 0, applet.height - 32);
		applet.image(this.images.get("AndGate"), 4 + 32 * 1, applet.height - 32);
		applet.image(this.images.get("OrGate"),  4 + 32 * 2, applet.height - 32);
		applet.image(this.images.get("XorGate"), 4 + 32 * 3, applet.height - 32);
		applet.image(this.images.get("NotGate"), 4 + 32 * 4, applet.height - 32);
		applet.image(this.images.get("Joint"),   4 + 32 * 5, applet.height - 32);
		
		if (Global.debug) {
			this.drawDebug(applet);
		}
	}
	
	private void drawDebug(PApplet applet) {
		//HUD areas(green)
		applet.stroke(150, 255, 150, 128);
		applet.fill(100, 255, 100, 50);
		applet.strokeWeight(2);
		
		applet.rect(applet.width - 30, 0, 30, 80); //top-right menu
		applet.rect(0, 0, 164, 14); //top-left menu
		applet.rect(0, applet.height - 30, 4 + 32 * NUM_COMPONENTS, 30); //bottom-left menu
		
		//click areas(blue)
		applet.stroke(150, 150, 255, 12);
		applet.fill(100, 100, 255, 50);
		
		//top-right click areas
		applet.rect(applet.width - 20, 20, 20, 20);
		applet.rect(applet.width - 20, 60, 15, 20);
		
		//reset
		applet.strokeWeight(1);
	}
	
	private void drawButtons(PApplet applet, Game game) {
		applet.stroke(0);
		if (this.buttonsPressed[0]) applet.fill(100); else applet.noFill();
		applet.rect(0, 0, 36, 13);
		if (this.buttonsPressed[1]) applet.fill(100); else applet.noFill();
		applet.rect(36, 0, 40, 13);
		if (this.buttonsPressed[2]) applet.fill(100); else applet.noFill();
		applet.rect(76, 0, 50, 13);
		if (this.buttonsPressed[3]) applet.fill(100); else applet.noFill();
		applet.rect(126, 0, 38, 13);
		applet.fill(0);
		applet.text("Open", 40, 10);
		applet.text("Save", 2, 10);
		applet.text("SaveAs", 78, 10);
		applet.text("Clear", 128, 10);
		applet.text("Ctrl-o", 40, 25);
		applet.text("Ctrl-s", 2, 25);
		applet.text("Ctrl-S", 84 , 25);
		applet.text("Ctrl-p", 128, 25);
	}
	
	private void checkTopButtons(PApplet applet, Game game) {
		int x = applet.mouseX;
		int y = applet.mouseY;
		for (int i = 0; i < 10; i++) {
			this.buttonsPressed[i] = false;
		}
		if (x > 00 && x < 36 && y <= 13) { game.save(); this.buttonsPressed[0] = true; }
		if (x > 36 && x < 76 && y <= 13) { game.open(); this.buttonsPressed[1] = true; }
		if (x > 76 && x < 126 && y <= 13) { game.saveAs(); this.buttonsPressed[2] = true; }
		if (x > 126 && x < 164 && y <= 13) { game.clear(); this.buttonsPressed[3] = true; }
	}
	
	public void mousePressed(PApplet applet, Game game) {
		this.checkTopButtons(applet, game);
		if (game.selectedComponents.size() == 0 && game.selectedWires.size() == 0) {
			this.mouseStart.set(applet.mouseX, applet.mouseY);
			this.canSelect = true;
		} else {
			this.canSelect = false;
		}
	}
	
	public void mouseReleased() {
		for (int i = 0; i < 10; i++) {
			this.buttonsPressed[i] = false;
		}
	}

	public void mouseClicked(PApplet applet, Game game) {
		if (applet.mouseX >= applet.width - 20 && applet.mouseY <= 40 && applet.mouseY >= 20) {
			game.switchSelectedTool();
		}
		if (applet.mouseX >= applet.width - 20 && applet.mouseY <= 80 && applet.mouseY >= 60) {
			game.setWireMode(!game.getWireMode());
		}
		
		//Add new components
		if (HelperFunctions.isInsideRect(applet.mouseX, applet.mouseY, 4, applet.height - 32, 32, 32)) {
			game.selectedComponent = "Switch";
		}
		if (HelperFunctions.isInsideRect(applet.mouseX, applet.mouseY, 4 + 32 * 1, applet.height - 32, 32, 32)) {
			game.selectedComponent = "AndGate";
		}
		if (HelperFunctions.isInsideRect(applet.mouseX, applet.mouseY, 4 + 32 * 2, applet.height - 32, 32, 32)) {
			game.selectedComponent = "OrGate";
		}
		if (HelperFunctions.isInsideRect(applet.mouseX, applet.mouseY, 4 + 32 * 3, applet.height - 32, 32, 32)) {
			game.selectedComponent = "XorGate";
		}
		if (HelperFunctions.isInsideRect(applet.mouseX, applet.mouseY, 4 + 32 * 4, applet.height - 32, 32, 32)) {
			game.selectedComponent = "NotGate";
		}
		if (HelperFunctions.isInsideRect(applet.mouseX, applet.mouseY, 4 + 32 * 5, applet.height - 32, 32, 32)) {
			game.selectedComponent = "Joint";
		}
	}

	public boolean isInsideHUDArea(Vector pos, Vector winSize) {
		if (pos.x >= 0 && pos.x < 164 && pos.y >= 0 && pos.y < 14) { return true; } //top-left menu
		if (pos.x >= winSize.x - 30 && pos.y <= 80) { return true; } //top right menu
		if (pos.x < 4 + 32 * NUM_COMPONENTS && pos.x >= 0 && pos.y >= winSize.y - 32 && pos.y <= winSize.y) { return true; } //bottom left menu
		return false;
	}
}
