package com.cospox.elecsim;

import processing.core.PApplet;

public class Joint extends Component {
	public Joint(Vector pos, int posInArray) {
		super(pos, posInArray);
		this.TYPE = "Joint";
		this.connections = new Connection[1];
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		this.connections[0] = new Connection(new Vector(x, y),
											 new Vector(posInArray, 0));
	}
	
	@Override
	public void updateConnectionsPos() {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		this.connections[0].pos = new Vector(x, y);
	}
	
	@Override
	public void draw(PApplet applet) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		if (this.connections[0].on) { applet.fill(200); }
		else { applet.fill(100); }
		
		if (this.selected) { applet.stroke(255, 20, 20); }
		else { applet.noStroke(); }
		
		applet.circle(x, y, 8);
	}
	
	@Override
	public boolean isMouseIntersecting(Vector pos, float zoom, Vector translate) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		x = translate.x + x * zoom;
		y = translate.y + y * zoom;
		return HelperFunctions.isInsideCircle(new Vector(pos.x, pos.y), new Vector(x, y), 6);
	}
	
	@Override
	public Connection getClickedConnection(Vector pos, float zoom, Vector translate) {
		if (this.isMouseIntersecting(pos, zoom, translate)) {
			return this.connections[0];
		}
		return null;
	}
	
	@Override
	public void update() {
		this.connections[0].off();
	}
	
	@Override
	public Component copy() {
		Component c = new Joint(this.pos.copy(), this.posInArray);
		c.connections = this.connections;
		c.selected = this.selected;
		return c;
	}
}
