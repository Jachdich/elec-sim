package com.cospox.elecsim.ui;

import java.util.ArrayList;

import com.cospox.elecsim.Game;
import com.cospox.elecsim.util.HelperFunctions;
import com.cospox.elecsim.util.Vector;

import processing.core.PApplet;
import processing.core.PImage;

public class HUDCategory {
	private PImage icon;
	private float x;
	protected int y;
	public boolean shown = false;
	private int numButtons = 0;
	private int PADDING = 3;
	private String displayName;
	protected ArrayList<HUDButton> buttons = new ArrayList<HUDButton>();
	public HUDCategory(PImage icon, float x, String displayName) {
		this.icon = icon;
		this.x = x;
		this.displayName = displayName;
	}
	
	public void draw(PApplet applet) {
		applet.image(this.icon, this.x, applet.height - hud.IMAGE_WIDTH);
		applet.text(this.displayName, this.x + (hud.IMAGE_WIDTH - applet.textWidth(this.displayName)) / 2, applet.height - 32 - Game.FONT_HEIGHT - PADDING);
		if (this.shown) {
			float y = applet.height - hud.IMAGE_WIDTH - hud.IMAGE_WIDTH * (this.y) - Game.FONT_HEIGHT - PADDING;
			for (HUDButton b: this.buttons) {
				b.draw(applet, y);
			}
		}
	}
	
	public void addButton(HUDButton button) {
		this.buttons.add(button);
		button.setPos(4 + hud.IMAGE_WIDTH * this.numButtons);
		this.numButtons++;
	}
	
	protected boolean mouseClicked(Vector pos, Vector screenSize, Game game) {
		if (HelperFunctions.isInsideRect(pos.x, pos.y, this.x, screenSize.y - hud.IMAGE_WIDTH, hud.IMAGE_WIDTH, hud.IMAGE_WIDTH)) {
			this.shown = !this.shown;
			if (this.shown) {
				this.y = hud.allocateY();
			} else {
				hud.free(this.y);
			}
		}
		
		if (!this.shown) { return this.shown; }
		System.out.println((screenSize.y - (this.y + 1) * hud.IMAGE_WIDTH) + " " + (screenSize.y - hud.IMAGE_WIDTH * this.y));
		System.out.print(pos.y);
		//float y = screenSize.y - hud.IMAGE_WIDTH - hud.IMAGE_WIDTH * (this.y) - Game.FONT_HEIGHT - PADDING;
		if (pos.y >= screenSize.y - (this.y + 1) * hud.IMAGE_WIDTH - PADDING - Game.FONT_HEIGHT &&
			pos.y <= screenSize.y - hud.IMAGE_WIDTH * this.y - PADDING - Game.FONT_HEIGHT) {
			for (HUDButton b: this.buttons) {
				if (pos.x >= b.x && pos.x <= b.x + hud.IMAGE_WIDTH) {
					//mouse was over b
					game.selectedComponent = b.name;
				}
			}
		}
		return this.shown;
	}
}