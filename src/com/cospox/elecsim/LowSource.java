package com.cospox.elecsim;

import processing.core.PApplet;

public class LowSource extends Component {
	public LowSource(Vector pos, int posInArray) {
		super(pos, posInArray);
		this.TYPE = "LowSource";
		this.connections = new Connection[1];
		this.connections[0] = new Connection(new Vector(this.pos.x + 15 - Connection.WIDTH / 2.0F,
											            this.pos.y + 20),
											 new Vector(posInArray, 0)); //output
	}
	
	@Override
	public void updateConnectionsPos() {
		this.connections[0].pos = new Vector(this.pos.x + 15 - Connection.WIDTH / 2.0F,
	            							 this.pos.y + 20);
	}

	@Override
	public void draw(PApplet applet) {
		if (this.selected) { applet.stroke(255, 20, 20); }
		else { applet.noStroke(); }
		applet.fill(100);
		applet.rect(this.pos.x, this.pos.y, 30, 20);
		//applet.textSize(10);
		applet.fill(20);
		applet.text("0", this.pos.x + 12, this.pos.y + 13);
		//applet.textSize(11.9F);
		for (Connection c: this.connections) {
			c.draw(applet);
		}
	}

	@Override
	public void update() {
		this.connections[0].off();
	}

	@Override
	public boolean isMouseIntersecting(Vector pos, float zoom, Vector translate) {
		float x = translate.x + this.pos.x * zoom;
		float y = translate.y + this.pos.y * zoom;
		return HelperFunctions.isInsideRect(pos.x, pos.y, x, y, 30 * zoom, 20 * zoom);
	}
	
	@Override
	public Component copy() {
		//change class from ComponentTemplate
		Component c = new LowSource(this.pos.copy(), this.posInArray);
		c.connections = this.connections; //TODO cannot copy connections (because wires) but they don't move with component
		c.selected = this.selected;
		return c;
	}
}
