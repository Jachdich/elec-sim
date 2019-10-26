package com.cospox.elecsim.components;

import java.util.HashMap;

import com.cospox.elecsim.Connection;
import com.cospox.elecsim.HelperFunctions;
import com.cospox.elecsim.Vector;

import processing.core.PApplet;

public class Component {
	public Vector pos;
	public boolean selected = false;
	public Connection[] connections;
	public String TYPE;
	public int posInArray;
	
	public HashMap<String, Object> externalFlags = new HashMap<String, Object>();
	public Component(Vector pos, int posInArray) {
		this.pos = pos;
		this.posInArray = posInArray;
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
		//for (Connection c: this.connections) {
		//	c.pos.x += x - this.pos.x;
		//}
		
		this.pos.x = x;
		this.updateConnectionsPos();
	}

	public void setY(float y) {
		//for (Connection c: this.connections) {
		//	c.pos.y += y - this.pos.y;
		//}
		
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
}
