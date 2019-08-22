package com.cospox.elecsim;

import processing.core.PApplet;

public class Joint extends Component {
	public Joint(Vector pos, int posInArray) {
		super(pos, posInArray);
		this.TYPE = "Joint";
		this.connections = new Connection[1];
		this.connections[0] = new Connection(new Vector(this.pos.x, this.pos.y),
											 new Vector(posInArray, 0));
	}
	
	@Override
	public void draw(PApplet applet) {
		if (this.connections[0].on) { applet.fill(200); }
		else { applet.fill(100); }
		
		if (this.selected) { applet.stroke(255, 20, 20); }
		else { applet.noStroke(); }
		
		applet.circle(this.pos.x, this.pos.y, 8);
	}
	
	@Override
	public boolean isMouseIntersecting(Vector pos, float zoom, Vector translate) {
		float x = translate.x + this.pos.x * zoom;
		float y = translate.y + this.pos.y * zoom;
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
}
