package com.cospox.elecsim;

import processing.core.PApplet;

public class NotGate extends Component {
	public NotGate(Vector pos, int posInArray) {
		super(pos, posInArray);
		this.TYPE = "NotGate";
		this.connections = new Connection[2];
		this.connections[0] = new Connection(new Vector(pos.x + 14, pos.y - 10), new Vector(posInArray, 0));
		this.connections[1] = new Connection(new Vector(pos.x + 14, pos.y + 34), new Vector(posInArray, 1));
	}
	
	@Override
	public void updateConnectionsPos() {
		this.connections[0].pos = new Vector(pos.x + 14, pos.y - 10);
		this.connections[1].pos = new Vector(pos.x + 14, pos.y + 34);
	}

	@Override
	public void draw(PApplet applet) {
		if (this.selected) { applet.stroke(255, 0, 0); }
		else { applet.noStroke(); }
		applet.fill(100);
		applet.triangle(this.pos.x, this.pos.y, this.pos.x + 30, this.pos.y, this.pos.x + 15, this.pos.y + 30);
		applet.arc(this.pos.x + 15, this.pos.y + 30, 8, 8, (float)-1.11, (float)4.25);
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
		float x = translate.x + this.pos.x * zoom;
		float y = translate.y + this.pos.y * zoom;
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
}
