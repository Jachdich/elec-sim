package com.cospox.elecsim;

import processing.core.PApplet;
import processing.core.PConstants;

public class OrGate extends Component {
	public OrGate(Vector pos, int posInArray) {
		super(pos, posInArray);
		this.TYPE = "OrGate";
		this.connections = new Connection[3];
		this.connections[0] = new Connection(new Vector(this.pos.x + 5, this.pos.y - Connection.HEIGHT + 9), 
											 new Vector(posInArray, 0)); // input A
		this.connections[1] = new Connection(new Vector(this.pos.x + 25 - Connection.WIDTH, this.pos.y - Connection.HEIGHT + 9),
											 new Vector(posInArray, 1)); // input B
		this.connections[2] = new Connection(new Vector(this.pos.x + 15 - Connection.WIDTH / 2, this.pos.y + 30), 
											 new Vector(posInArray, 2)); // output
	}
	
	@Override
	public void updateConnectionsPos() {
		this.connections[0].pos = new Vector(this.pos.x + 5, this.pos.y - Connection.HEIGHT + 9); // input A
		this.connections[1].pos = new Vector(this.pos.x + 25 - Connection.WIDTH, this.pos.y - Connection.HEIGHT + 9); // input B
		this.connections[2].pos = new Vector(this.pos.x + 15 - Connection.WIDTH / 2, this.pos.y + 30);
	}

	@Override
	public void draw(PApplet applet) {
		applet.noStroke();
		applet.fill(100);
		applet.rect(this.pos.x, this.pos.y, 30, 15);
		if (this.selected) {
			applet.stroke(255, 20, 20);
		} else {
			applet.noStroke();
		}
		applet.line(this.pos.x, this.pos.y, this.pos.x, this.pos.y + 15);
		applet.line(this.pos.x + 30, this.pos.y, this.pos.x + 30,
				this.pos.y + 15);
		applet.arc(this.pos.x + 15, this.pos.y + 15, 30, 30, 0, PConstants.PI,
				PConstants.OPEN);
		applet.fill(255);
		applet.arc(this.pos.x + 15, this.pos.y - 4, 30, 30, (float) 0.26,
				PConstants.PI - (float) 0.26, PConstants.OPEN);
		for (Connection c : this.connections) {
			c.draw(applet);
		}
	}

	@Override
	public void update() {
		boolean output = this.connections[0].on || this.connections[1].on;
		for (Connection c : this.connections) {
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
		return HelperFunctions.isInsideRect(pos.x, pos.y, x, y, 30
				* zoom, 30 * zoom);
	}
	
	@Override
	public Component copy() {
		Component c = new OrGate(this.pos.copy(), this.posInArray);
		c.connections = this.connections;
		c.selected = this.selected;
		return c;
	}
}