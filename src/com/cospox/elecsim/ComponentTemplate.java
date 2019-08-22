package com.cospox.elecsim;

import processing.core.PApplet;

public class ComponentTemplate extends Component {
	public ComponentTemplate(Vector pos, int posInArray) {
		super(pos, posInArray);
		this.TYPE = "componentTemplate";
		this.connections = new Connection[1];
		this.connections[0] = new Connection(new Vector(), new Vector(posInArray, 0));
	}
	
	@Override
	public void draw(PApplet applet) {
		if (this.selected) { applet.stroke(255, 20, 20); }
		else { applet.noStroke(); }
		applet.fill(100);
		//draw code here
		for (Connection c: this.connections) {
			c.draw(applet);
		}
	}

	@Override
	public void update() {
		//store input connections
		for (Connection c: this.connections) {
			c.off();
		}
		//update output connections
	}

	@Override
	public boolean isMouseIntersecting(Vector pos, float zoom, Vector translate) {
		//use HelperFunctions isInside functions to test if the mouse is inside component area
		//convert coords like this:
		//float screenX = translate.x + this.pos.x * zoom;
		//float screenY = translate.y + this.pos.y * zoom;
		//to convert 'real' coords to 'screen' coords;
		return false;
	}
}
