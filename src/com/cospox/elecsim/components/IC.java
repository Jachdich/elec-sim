package com.cospox.elecsim.components;

import com.cospox.elecsim.Connection;
import com.cospox.elecsim.ui.hud;
import com.cospox.elecsim.util.HelperFunctions;
import com.cospox.elecsim.util.Vector;

import processing.core.PApplet;

public class IC extends Component {
	
	protected int numPins;
	private final static int HEIGHT = 30;
	private final static int PIN_SPACING = 7;
	private final static boolean DRAW_PIN_NUMBERS = false;
	
	public IC(Vector pos, int numPins, int posInArray, long uuid) {
		super(pos, posInArray, uuid);
		this.numPins = numPins;
		
		if (this.numPins % 2 != 0) {
			throw new IllegalArgumentException("Number of pins must be divisible by two");
		}
		
		this.connections = new Connection[numPins];
		for (int i = 0; i < numPins; i++) {
			this.connections[i] = new Connection(new Vector(), new Vector(posInArray, i));
		}
		this.updateConnectionsPos();
	}
	
	@Override
	public void draw(PApplet applet) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		applet.fill(0);
		applet.stroke(0, 255, 0);
		applet.rect(x, y, (this.numPins - 1) * PIN_SPACING, HEIGHT);
		int i = 0;
		for (Connection c: this.connections) {
			c.draw(applet);
			if (DRAW_PIN_NUMBERS) {
				applet.fill(0, 0, 255);
				applet.stroke(255, 0, 0);
				applet.text(Integer.toString(i), c.pos.x, c.pos.y);
				i++;
			}
		}
	}
	
	@Override
	public void updateConnectionsPos() {

		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		
		float OFFSET = PIN_SPACING / 2;
		
		//I don't know why this works, but it does
		for (int i = 0; i < numPins / 2; i++) {
			this.connections[i].pos = new Vector(x + OFFSET + (i * 2) * PIN_SPACING,
					                             y + HEIGHT);
		}
		
		for (int i = numPins / 2; i < numPins; i++) {
			this.connections[numPins - (i - numPins / 2 + 1)].pos = new Vector(x + OFFSET + (i - numPins / 2) * 2 * PIN_SPACING,
					                             y - Connection.HEIGHT);
		}
	}
	
	@Override
	public boolean isMouseIntersecting(Vector pos, float zoom, Vector translate) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		x = translate.x + x * zoom;
		y = translate.y + y * zoom;
		return HelperFunctions.isInsideRect(pos.x, pos.y, x, y, (this.numPins - 1) * PIN_SPACING * zoom, HEIGHT * zoom);
	}
	
	public static void onLoadPriority() {
		hud.addImage("ICs");
		hud.addNewComponentCategory("ICs", "ICs");
	}
}
