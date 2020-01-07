package com.cospox.elecsim.components;

import java.util.HashMap;

import com.cospox.elecsim.Connection;
import com.cospox.elecsim.ui.hud;
import com.cospox.elecsim.util.HelperFunctions;
import com.cospox.elecsim.util.Vector;

import processing.core.PApplet;

public class Component {
	public Vector pos;
	public boolean selected = false;
	public Connection[] connections;
	public int rotationDegrees = 0;
	private long UUID = 0;
	//public PShape shape;
	
	public HashMap<String, Object> externalFlags = new HashMap<String, Object>();
	
	public Component(Vector pos, long uuid) {
		this.UUID = uuid;
		this.pos = pos;
	}

	public Connection getConnections(int index) {
		return this.connections[index];
	}

	public boolean isMouseIntersecting(Vector mousePos, float zoom, Vector translate) { return false; }
	public void draw(PApplet applet) {}
	public void update() {}
	public void onMousePressed(PApplet applet, float zoom, Vector translate) {}

	public Connection getClickedConnection(Vector pos, float zoom, Vector translate) {
		for (Connection c: this.connections) {
			float cx = translate.x + c.pos.x * zoom;
			float cy = translate.y + c.pos.y * zoom;
			float cw = Connection.WIDTH  * zoom;
			float ch = Connection.HEIGHT * zoom;
			if (HelperFunctions.isInsideRect(pos.x, pos.y, cx, cy, cw, ch)) {
				return c;
			}
		}
		return null;
	}
	public void setX(float x) {
		this.pos.x = x;
		this.updateConnectionsPos();
	}

	public void setY(float y) {
		this.pos.y = y;
		this.updateConnectionsPos();
	}

	public void select() {
		this.selected = true;
		for (Connection c: this.connections) {
			c.selected = true;
		}
	}

	public void deSelect() {
		this.selected = false;
		for (Connection c: this.connections) {
			c.selected = false;
		}
	}
	
	public Component copy() {
		return null;
	}

	public void updateConnectionsPos() {
	}

	public static void onLoadPriority() {
		hud.addImage("BasicGates");
		hud.addImage("IO");
		hud.addNewComponentCategory("Gates", "BasicGates");
		hud.addNewComponentCategory("IO", "IO");
	}
	
	public long getUUID() {
		return this.UUID;
	}
	
//	public void rotate(int degrees) {
//		this.rotationDegrees += degrees;
//	}
}
