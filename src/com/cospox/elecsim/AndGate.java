package com.cospox.elecsim;

import processing.core.PApplet;
import processing.core.PConstants;

public class AndGate extends Component {
	public AndGate(Vector pos, int posInArray) {
		super(pos, posInArray);
		this.TYPE = "AndGate";
		this.connections = new Connection[3];
		this.connections[0] = new Connection(new Vector(this.pos.x, this.pos.y - Connection.HEIGHT),
											 new Vector(posInArray, 0)); //input A
		this.connections[1] = new Connection(new Vector(this.pos.x + 30 - Connection.WIDTH, this.pos.y - Connection.HEIGHT),
											 new Vector(posInArray, 1)); //input B
		this.connections[2] = new Connection(new Vector(this.pos.x + 15 - Connection.WIDTH / 2, this.pos.y + 30),
											 new Vector(posInArray, 2)); //output
	}

	@Override
	public void draw(PApplet applet) {
		if (this.selected) { applet.stroke(255, 20, 20); }
		else { applet.noStroke(); }
		applet.fill(100);
		applet.rect(this.pos.x, this.pos.y, 30, 15);
		applet.arc(this.pos.x + 15, this.pos.y + 15, 30, 30, 0, PConstants.PI);
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
		float x = translate.x + this.pos.x * zoom;
		float y = translate.y + this.pos.y * zoom;
		return HelperFunctions.isInsideRect(pos.x, pos.y, x, y, 30 * zoom, 30 * zoom);
	}
}
