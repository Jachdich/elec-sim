package com.cospox.elecsim;

import com.cospox.elecsim.components.Component;

import processing.core.PApplet;

public class ComponentTemplate extends Component {
	public ComponentTemplate(Vector pos, int posInArray) {
		super(pos, posInArray);
		this.TYPE = "ComponentTemplate";
		this.connections = new Connection[1];
		this.connections[0] = new Connection(new Vector(this.pos.x, this.pos.y - Connection.HEIGHT),
											 new Vector(posInArray, 0)); //input A
	}
	
	@Override
	public void updateConnectionsPos() {
		this.connections[0].pos = new Vector(this.pos.x, this.pos.y - Connection.HEIGHT);
	}

	@Override
	public void draw(PApplet applet) {
		if (this.selected) { applet.stroke(255, 20, 20); }
		else { applet.noStroke(); }
		//draw code
		for (Connection c: this.connections) {
			c.draw(applet);
		}
	}

	@Override
	public void update() {
		boolean output = this.connections[0].on; //replace with logic
		for (Connection c: this.connections) {
			c.off();
		}
		if (output) {
			//set outputs
		}
	}

	@Override
	public boolean isMouseIntersecting(Vector pos, float zoom, Vector translate) {
		float x = translate.x + this.pos.x * zoom;
		float y = translate.y + this.pos.y * zoom;
		//IDK i mean keep it if u like
		return HelperFunctions.isInsideRect(pos.x, pos.y, x, y, 30 * zoom, 30 * zoom);
	}
	
	@Override
	public Component copy() {
		//change class from ComponentTemplate
		Component c = new ComponentTemplate(this.pos.copy(), this.posInArray);
		c.connections = this.connections; //TODO cannot copy connections (because wires) but they don't move with component
		c.selected = this.selected;
		return c;
	}
}
