package com.cospox.elecsim;

import com.cospox.elecsim.components.Component;
import com.cospox.elecsim.util.HelperFunctions;
import com.cospox.elecsim.util.Vector;

import processing.core.PApplet;

public class Wire {
	public Connection s, e;
	public Component refs, refe;
	public boolean on = false;
	public boolean selected = false;
	public boolean wireMode = false;
	public Wire(Connection s, Connection e, Component refs, Component refe) {
		this.s = s;
		this.e = e;
		this.refs = refs; //To be able to translate wire's connection back to the
		this.refe = refe; //component it originated from
	}

	public void draw(PApplet applet, Vector translate, float zoom) {
		if (this.on) {
			applet.stroke(128);
		} else {
			applet.stroke(0);
		}

		if (this.selected) {
			applet.stroke(255, 0, 0);
		}
		if (this.wireMode) {
			applet.line(this.s.pos.x, this.s.pos.y, this.e.pos.x, this.s.pos.y);
			applet.line(this.e.pos.x, this.s.pos.y, this.e.pos.x, this.e.pos.y);
		} else {
			applet.line(this.s.pos.x, this.s.pos.y, this.e.pos.x, this.e.pos.y);
		}
		applet.stroke(0);
	}

	public void update() {
		if (this.s.on || this.e.on) {
			this.on = true;
			this.s.on();
			this.e.on();
		} else {
			this.on = false;
			this.e.off();
			this.s.off();
		}
	}
	
	public String getPosition(Connection c) {
		return Integer.toString(c.posInComponents) + " " +  Integer.toString(c.posInComponent);
	}

	public boolean isMouseIntersecting(PApplet applet, Vector translate, float zoom) {
		Vector poss = new Vector(translate.x + this.s.pos.x * zoom,
								 translate.y + this.s.pos.y * zoom);
		Vector pose = new Vector(translate.x + this.e.pos.x * zoom,
								 translate.y + this.e.pos.y * zoom);
		
		//check which point is *really* the start/end
		Vector a = poss.x >= pose.x ? poss : pose;
		Vector b = new Vector(applet.mouseX, applet.mouseY);
		Vector c = poss.x > pose.x ? pose : poss;
		
		if (this.wireMode) {
			Vector x = new Vector(a.x, a.y);
			Vector y = new Vector(c.x, a.y);
			Vector z = new Vector(c.x, a.y);
			Vector w = new Vector(c.x, c.y);
			
			return HelperFunctions.isPointOnLine(x, b, y, (float)0.8 * zoom) ||
				   HelperFunctions.isPointOnLine(z, b, w, (float)0.8 * zoom);
		} else {
			return HelperFunctions.isPointOnLine(a, b, c, (float)0.8 * zoom);
		}
	}
	
	public void select() {
		this.selected = true;
	}
	public void deSelect() {
		this.selected = false;
	}
	
	public Wire copy() {
		Wire n =  new Wire(this.s, this.e, this.refs, this.refe);
		n.selected = this.selected;
		n.wireMode = this.wireMode;
		n.on = this.on;
		return n;
	}
}
