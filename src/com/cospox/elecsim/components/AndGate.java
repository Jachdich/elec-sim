package com.cospox.elecsim.components;

import com.cospox.elecsim.Connection;
import com.cospox.elecsim.util.HelperFunctions;
import com.cospox.elecsim.util.Vector;

import processing.core.PApplet;
import processing.core.PConstants;

public class AndGate extends Component {
	public AndGate(Vector pos, int posInArray) {
		super(pos, posInArray);
		this.TYPE = "AndGate";
		this.connections = new Connection[3];
		
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		
		this.connections[0] = new Connection(new Vector(x, y - Connection.HEIGHT),
											 new Vector(posInArray, 0)); //input A
		this.connections[1] = new Connection(new Vector(x + 30 - Connection.WIDTH, y - Connection.HEIGHT),
											 new Vector(posInArray, 1)); //input B
		this.connections[2] = new Connection(new Vector(x + 15 - Connection.WIDTH / 2, y + 30),
											 new Vector(posInArray, 2)); //output
	}
	
	@Override
	public void updateConnectionsPos() {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		this.connections[0].pos = new Vector(x, y - Connection.HEIGHT);
		this.connections[1].pos = new Vector(x + 30 - Connection.WIDTH, y - Connection.HEIGHT);
		this.connections[2].pos = new Vector(x + 15 - Connection.WIDTH / 2, y + 30);
	}

	@Override
	public void draw(PApplet applet) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		if (this.selected) { applet.stroke(255, 20, 20); }
		else { applet.noStroke(); }
		applet.fill(100);
		applet.rect(x, y, 30, 15);
		applet.arc(x + 15, y + 15, 30, 30, 0, PConstants.PI);
		for (Connection c: this.connections) {
			c.draw(applet);
		}
	}

	@Override
	public void update() {
		boolean output = this.connections[0].on && this.connections[1].on;
		for (Connection c: this.connections) {
			c.off();
		}
		if (output) {
			this.connections[2].on();
		}
	}

	@Override
	public boolean isMouseIntersecting(Vector pos, float zoom, Vector translate) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		x = translate.x + x * zoom;
		y = translate.y + y * zoom;
		return HelperFunctions.isInsideRect(pos.x, pos.y, x, y, 30 * zoom, 30 * zoom);
	}
	
	@Override
	public Component copy() {
		Component c = new AndGate(this.pos.copy(), this.posInArray);
		c.connections = this.connections; //TODO cannot copy connections (because wires) but they don't move with component
		c.selected = this.selected;
		return c;
	}
}
