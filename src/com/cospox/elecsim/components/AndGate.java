package com.cospox.elecsim.components;

import com.cospox.elecsim.Connection;
import com.cospox.elecsim.ui.hud;
import com.cospox.elecsim.util.HelperFunctions;
import com.cospox.elecsim.util.Vector;

import processing.core.PApplet;
import processing.core.PConstants;

public class AndGate extends Component {
	//public boolean first = true;
	public AndGate(Vector pos, int posInArray, long uuid) {
		super(pos, posInArray, uuid);
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
	
	//run when the application loads
	public static void onLoad() {
		hud.addImage("AndGate");
		hud.addNewComponentButton("AndGate", "BasicGates");
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
		int SIZE = 30;
		/*
		if (first) {
			first = false;
			this.shape = applet.createShape(PConstants.GROUP);
			this.shape.setFill(applet.color(100));
			this.shape.setStroke(false);
			this.shape.addChild(applet.createShape(PConstants.RECT, 0, 0, SIZE, SIZE / 2));
			this.shape.addChild(applet.createShape(PConstants.ARC, SIZE / 2, SIZE / 2, SIZE, SIZE, 0, PConstants.PI));
			
			if (this.selected) {
				this.shape.setStroke(applet.color(255, 20, 20));
				this.shape.addChild(applet.createShape(PConstants.LINE, 0, 0, SIZE, 0));
				this.shape.addChild(applet.createShape(PConstants.LINE, 0, 0, 0, SIZE / 2));
				this.shape.addChild(applet.createShape(PConstants.LINE, SIZE, 0, SIZE, SIZE / 2));

				this.shape.setFill(false);
				this.shape.addChild(applet.createShape(PConstants.ARC, SIZE / 2, SIZE / 2, SIZE, SIZE, 0, PConstants.PI));
			}
		}
		PGraphics graphics = applet.createGraphics(50, 50, PConstants.P2D);
		graphics.beginDraw();
		graphics.rotate((float)Math.toRadians(this.rotationDegrees));
		graphics.background(255);
		graphics.noStroke();
		graphics.fill(100);
		graphics.rect(0, 0, SIZE, SIZE / 2);
		graphics.arc(SIZE / 2, SIZE / 2, SIZE, SIZE, 0, PConstants.PI);
		
		if (this.selected) { graphics.stroke(255, 20, 20); }
		graphics.line(0, 0, SIZE, 0);
		graphics.line(0, 0, 0, SIZE / 2);
		graphics.line(SIZE, 0, SIZE, SIZE / 2);
		graphics.noFill();
		graphics.arc(SIZE / 2, SIZE / 2, SIZE, SIZE, 0, PConstants.PI);
		graphics.endDraw();
		applet.image(graphics, x, y);
		*/
		applet.noStroke();
		applet.fill(100);
		applet.rect(x, y, SIZE, SIZE / 2);
		applet.arc(x + SIZE / 2, y + SIZE / 2, SIZE, SIZE, 0, PConstants.PI);
		
		if (this.selected) { applet.stroke(255, 20, 20); }
		applet.line(x, y, x + SIZE, y);
		applet.line(x, y, x, y + SIZE / 2);
		applet.line(x + SIZE, y, x + SIZE, y + SIZE / 2);
		applet.noFill();
		applet.arc(x + SIZE / 2, y + SIZE / 2, SIZE, SIZE, 0, PConstants.PI);
		//this.shape.rotate((float)Math.toRadians(this.rotationDegrees));
		//applet.shape(this.shape, x, y);
		for (Connection c: this.connections) {
			c.draw(applet);
		}
	}
	
//	@Override
//	public void rotate(int degrees) {
//		this.shape.rotate((float) Math.toRadians(degrees));
//	}

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
		Component c = new AndGate(this.pos.copy(), this.posInArray, this.getUUID());
		c.connections = this.connections; //TODO cannot copy connections (because wires) but they don't move with component
		c.selected = this.selected;
		return c;
	}
}
