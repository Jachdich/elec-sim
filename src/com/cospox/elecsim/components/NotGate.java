package com.cospox.elecsim.components;

import com.cospox.elecsim.Connection;
import com.cospox.elecsim.ui.hud;
import com.cospox.elecsim.util.HelperFunctions;
import com.cospox.elecsim.util.Vector;

import processing.core.PApplet;

public class NotGate extends Component {
	public NotGate(Vector pos, int posInArray) {
		super(pos, posInArray);
		this.connections = new Connection[2];
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		this.connections[0] = new Connection(new Vector(x + 14, y - 10), new Vector(posInArray, 0));
		this.connections[1] = new Connection(new Vector(x + 14, y + 34), new Vector(posInArray, 1));
	}
	
	@Override
	public void updateConnectionsPos() {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		this.connections[0].pos = new Vector(x + 14, y - 10);
		this.connections[1].pos = new Vector(x + 14, y + 34);
	}

	@Override
	public void draw(PApplet applet) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		if (this.selected) { applet.stroke(255, 0, 0); }
		else { applet.noStroke(); }
		applet.fill(100);
		applet.triangle(x, y, x + 30, y, x + 15, y + 30);
		applet.arc(x + 15, y + 30, 8, 8, (float)-1.11, (float)4.25);
		for (Connection c: this.connections) {
			c.draw(applet);
		}
	}

	@Override
	public void update() {
		boolean on = this.connections[0].on;
		for (Connection c: this.connections) {
			c.off();
		}
		if (on) {
			this.connections[1].off();
		} else {
			this.connections[1].on();
		}
	}
	
	@Override
	public boolean isMouseIntersecting(Vector pos, float zoom, Vector translate) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		x = translate.x + x * zoom;
		y = translate.y + y * zoom;
		if (HelperFunctions.isInsideTriangle(pos,
				new Vector(x, y),
				new Vector(x + 30 * zoom, y),
				new Vector(x + 15 * zoom, y + 30 * zoom))) {
			return true;
		}
		if (HelperFunctions.isInsideCircle(pos, new Vector(x + 15 * zoom, y + 30 * zoom), 8 * zoom)) {
			return true;
		}
		return false;
	}
	
	@Override
	public Component copy() {
		Component c = new NotGate(this.pos.copy(), this.posInArray);
		c.connections = this.connections;
		c.selected = this.selected;
		return c;
	}
	
	public static void onLoad() {
		hud.addImage("NotGate");
		hud.addNewComponentButton("NotGate", "BasicGates");
	}
}
