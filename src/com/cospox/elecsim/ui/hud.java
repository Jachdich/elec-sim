package com.cospox.elecsim.ui;

import java.util.HashMap;

import com.cospox.elecsim.Game;
import com.cospox.elecsim.util.Global;
import com.cospox.elecsim.util.Vector;

import processing.core.PApplet;
import processing.core.PImage;

public class hud {
	private static HashMap<String, PImage> images = new HashMap<String, PImage>();
	protected static int NUM_COMPONENTS = 0;
	private Vector mouseStart = new Vector();
	public boolean canSelect = false;
	private boolean[] buttonsPressed = new boolean[10];
	protected static int categoriesOpen = 0;
	
	private static PApplet parent;
	
	private final static int PADDING_FACTOR = 4;
	protected final static int IMAGE_WIDTH = 32;
	
	private static HashMap<String, HUDCategory> categories = new HashMap<String, HUDCategory>();
	
	public hud(PApplet applet) {
		hud.parent = applet;
		hud.images.put("selectImage", applet.loadImage("assets/images/mouse.png"));
	}
	
	public static void addImage(String name) {
		hud.images.put(name, parent.loadImage("assets/images/" + name + ".png"));
		hud.addSmallImage(name);
	}

	private static void addSmallImage(String imageName) {
		PImage temp = hud.images.get(imageName).copy();
		temp.resize(20, 20);
		hud.images.put(imageName + "S", temp);
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
		for (HUDCategory c: hud.categories.values()) {
			c.draw(applet);
		}
		
		applet.fill(0);
		applet.text("Tool (t)", applet.width - 46, 5);
		if (game.selectedTool[0] == "wire") {
			applet.stroke(0);
			applet.line(applet.width - 20, 30, applet.width - 10, 20);
		} else if (game.selectedTool[0] == "component") {
			PImage i = hud.images.get(game.selectedComponent + "S");
			applet.image(i, applet.width - 20, 20);
		} else if (game.selectedTool[0] == "select") {
			applet.image(hud.images.get("selectImage"), applet.width - 20, 20);
		}
		
		applet.stroke(0);
		applet.fill(0);
		applet.text("Wire mode (y)", applet.width - 87, 42);
		if (game.states.get("wireMode")) {
			applet.line(applet.width - 20, 65, applet.width - 5, 65);
			applet.line(applet.width - 5, 65, applet.width - 5, 75);
		} else { applet.line(applet.width - 20, 65, applet.width - 5, 75); }
		
		if (Global.debug) {
			this.drawDebug(applet);
		}
		
		for (HUDCategory c: hud.categories.values()) {
			c.draw(applet);
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
		//applet.textFont(applet.createFont("v", 12));
		float T_HEIGHT = applet.textAscent() + applet.textDescent();
		int T_WIDTH = 0;
		int PADDING = (int) (T_HEIGHT / hud.PADDING_FACTOR);
		
		applet.stroke(0);
		
		float width_save = applet.textWidth("Save");
		float width_open = applet.textWidth("Open");
		float width_saveas = applet.textWidth("SaveAs");
		float width_clear = applet.textWidth("Clear");
		float width_new = applet.textWidth("New");
		
		if (this.buttonsPressed[0]) applet.fill(100); else applet.noFill();
		applet.rect(T_WIDTH, 0, width_save + PADDING * 2, T_HEIGHT);
		
		if (this.buttonsPressed[1]) applet.fill(100); else applet.noFill();
		applet.rect(T_WIDTH += (width_save + PADDING * 2), 0, width_open + PADDING * 2, T_HEIGHT);
		
		if (this.buttonsPressed[2]) applet.fill(100); else applet.noFill();
		applet.rect(T_WIDTH += (width_open + PADDING * 2), 0, width_saveas + PADDING * 2, T_HEIGHT);
		
		if (this.buttonsPressed[3]) applet.fill(100); else applet.noFill();
		applet.rect(T_WIDTH += (width_saveas + PADDING * 2), 0, width_clear + PADDING * 2, T_HEIGHT);
		
		if (this.buttonsPressed[4]) applet.fill(100); else applet.noFill();
		applet.rect(T_WIDTH += (width_clear + PADDING * 2), 0, width_new + PADDING * 2, T_HEIGHT);
		
		T_WIDTH = 0;
		applet.fill(0);
		applet.text("Save", T_WIDTH += PADDING, 0);
		applet.text("Open", T_WIDTH += (width_save) + PADDING * 2, 0);
		applet.text("SaveAs", T_WIDTH += (width_open) + PADDING * 2, 0);
		applet.text("Clear", T_WIDTH += (width_saveas) + PADDING * 2, 0);
		applet.text("New", T_WIDTH += (width_clear) + PADDING * 2, 0);
		
		T_WIDTH = 0;
		applet.text("Ctrl-o", T_WIDTH += PADDING, T_HEIGHT);
		applet.text("Ctrl-s", T_WIDTH += (width_save) + PADDING * 2, T_HEIGHT);
		applet.text("Ctrl-S", T_WIDTH += (width_open) + PADDING * 2, T_HEIGHT);
		applet.text("Ctrl-p", T_WIDTH += (width_saveas) + PADDING * 2, T_HEIGHT);
		applet.text("Ctrl-n", T_WIDTH += (width_clear) + PADDING * 2, T_HEIGHT);
	}
	
	private void checkTopButtons(PApplet applet, Game game) {
		float T_HEIGHT = applet.textAscent() + applet.textDescent();
		int T_WIDTH = 0;
		int PADDING = (int) (T_HEIGHT / hud.PADDING_FACTOR);
		
		float width_save = applet.textWidth("Save");
		float width_open = applet.textWidth("Open");
		float width_saveas = applet.textWidth("SaveAs");
		float width_clear = applet.textWidth("Clear");
		float width_new = applet.textWidth("New");
		
		int x = applet.mouseX;
		int y = applet.mouseY;
		for (int i = 0; i < 10; i++) {
			this.buttonsPressed[i] = false;
		}
		
		if (x > (T_WIDTH)                           && x < T_WIDTH + width_save + PADDING * 2 && y <= T_HEIGHT) { game.save(); this.buttonsPressed[0] = true; }
		if (x > (T_WIDTH += (width_save + PADDING * 2)) && x < T_WIDTH + width_open + PADDING * 2 && y <= T_HEIGHT) { game.open(); this.buttonsPressed[1] = true; }
		if (x > (T_WIDTH += (width_open + PADDING * 2))   && x < T_WIDTH + width_saveas + PADDING * 2 && y <= T_HEIGHT) { game.saveAs(); this.buttonsPressed[2] = true; }
		if (x > (T_WIDTH += (width_saveas + PADDING * 2)) && x < T_WIDTH + width_clear + PADDING * 2 && y <= T_HEIGHT) { game.clear(); this.buttonsPressed[3] = true; }
		if (x > (T_WIDTH += (width_clear + PADDING * 2)) && x < T_WIDTH + width_new + PADDING * 2 && y <= T_HEIGHT) { game.newFile(); this.buttonsPressed[4] = true; }
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
		int categoriesOpen = 0;
		for (HUDCategory h: hud.categories.values()) {
			boolean open = h.mouseClicked(
					new Vector(applet.mouseX, applet.mouseY),
					new Vector(applet.width,  applet.height), game);
			
			if (open) {
				categoriesOpen++;
			}
		}
		hud.categoriesOpen = categoriesOpen;
		
		if (applet.mouseX >= applet.width - 20 && applet.mouseY <= 40 && applet.mouseY >= 20) {
			game.switchSelectedTool();
		}
		if (applet.mouseX >= applet.width - 20 && applet.mouseY <= 80 && applet.mouseY >= 60) {
			game.setWireMode(!game.getWireMode());
		}
		
		hud.NUM_COMPONENTS = hud.categories.size();
		for (HUDCategory c: hud.categories.values()) {
			if (c.buttons.size() > hud.NUM_COMPONENTS) {
				hud.NUM_COMPONENTS = c.buttons.size();
			}
		}
	}

	public boolean isInsideHUDArea(Vector pos, Vector winSize) {
		if (pos.x >= 0 && pos.x < 204 && pos.y >= 0 && pos.y < 14) { return true; } //top-left menu
		if (pos.x >= winSize.x - 30 && pos.y <= 80) { return true; } //top right menu
		if (pos.x < 4 + 32 * NUM_COMPONENTS &&
			pos.x >= 0 &&
			pos.y >= winSize.y - 32 * (hud.categoriesOpen + 1) &&
			pos.y <= winSize.y) { return true; } //bottom left menu
		return false;
	}
	
	public static void addNewComponentCategory(String name, String iconName) {
		hud.categories.put(iconName, new HUDCategory(hud.images.get(iconName), 4 + hud.IMAGE_WIDTH * hud.categories.size(), name));
	}

	public static void addNewComponentButton(String name, String categoryName) {
		hud.categories.get(categoryName).addButton(new HUDButton(name, hud.images.get(name)));
	}
	
	public static int allocateY() {
		int x = -1;
		for (HUDCategory c: hud.categories.values()) {
			if (c.y > x) {
				x = c.y;
			}
		}
		return x + 1;
	}
	
	public static void free(int y) {
		for (HUDCategory c: hud.categories.values()) {
			if (c.y >= y) {
				c.y -= 1;
			}
		}
	}
}
