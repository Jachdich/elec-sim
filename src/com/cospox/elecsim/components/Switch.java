package com.cospox.elecsim.components;

import com.cospox.elecsim.Connection;
import com.cospox.elecsim.util.HelperFunctions;
import com.cospox.elecsim.util.Vector;

import processing.core.PApplet;

public class Switch extends Component {
	public boolean on = false;
	public int radius = 15;

	public Switch(Vector pos, int posInArray) {
		super(pos, posInArray);
		this.TYPE = "Switch";
		this.connections = new Connection[1];
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		this.connections[0] = new Connection(new Vector(x, y + this.radius),
											 new Vector(posInArray, 0));
	}
	
	@Override
	public void updateConnectionsPos() {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		this.connections[0].pos = new Vector(x, y + this.radius);
	}

	@Override
	public void draw(PApplet applet) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		if (this.on) { applet.fill(200); }
		else { applet.fill(100); }

		if (this.selected) { applet.stroke(255, 20, 20); }
		else { applet.noStroke(); }
		applet.circle(x, y, this.radius * 2);
		this.connections[0].draw(applet);
	}

	@Override
	public void onMousePressed(PApplet applet, float zoom, Vector translate) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		x = translate.x + x * zoom;
		y = translate.y + y * zoom;
		if (PApplet.dist(x, y, applet.mouseX, applet.mouseY) < this.radius * zoom) {
			this.on = !this.on;
		}
	}

	@Override
	public void update() {
		if (this.on) {
			this.connections[0].on();
		} else {
			this.connections[0].off();
		}
	}

	@Override
	public boolean isMouseIntersecting(Vector pos, float zoom, Vector translate) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		x = translate.x + x * zoom;
		y = translate.y + y * zoom;
		if (HelperFunctions.isInsideCircle(pos, new Vector(x, y), this.radius * zoom)) {
			return true;
		}
		return false;
	}
	
	@Override
	public Component copy() {
		Component c = new Switch(this.pos.copy(), this.posInArray);
		c.connections = this.connections;
		c.selected = this.selected;
		return c;
	}
}
