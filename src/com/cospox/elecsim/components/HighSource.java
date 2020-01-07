package com.cospox.elecsim.components;

import com.cospox.elecsim.Connection;
import com.cospox.elecsim.ui.hud;
import com.cospox.elecsim.util.HelperFunctions;
import com.cospox.elecsim.util.Vector;

import processing.core.PApplet;

public class HighSource extends Component {
	public HighSource(Vector pos, long uuid) {
		super(pos, uuid);
		this.connections = new Connection[1];
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		this.connections[0] = new Connection(new Vector(x + 15 - Connection.WIDTH / 2.0F, y + 20),
											 new Vector(uuid, 0)); //output
	}
	
	@Override
	public void updateConnectionsPos() {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		this.connections[0].pos = new Vector(x + 15 - Connection.WIDTH / 2.0F, y + 20);
	}

	@Override
	public void draw(PApplet applet) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		if (this.selected) { applet.stroke(255, 20, 20); }
		else { applet.noStroke(); }
		
		float T_HEIGHT = applet.textAscent() + applet.textDescent();
		
		applet.fill(100);
		applet.rect(x, y, 30, 20);
		//applet.textSize(10);
		applet.fill(20);
		applet.text("1", x + 12, y + T_HEIGHT - 12);
		//applet.textSize(11.9F);
		for (Connection c: this.connections) {
			c.draw(applet);
		}
	}

	@Override
	public void update() {
		this.connections[0].on();
	}

	@Override
	public boolean isMouseIntersecting(Vector pos, float zoom, Vector translate) {
		float x = HelperFunctions.snap(this.pos.x);
		float y = HelperFunctions.snap(this.pos.y);
		x = translate.x + x * zoom;
		y = translate.y + y * zoom;
		return HelperFunctions.isInsideRect(pos.x, pos.y, x, y, 30 * zoom, 20 * zoom);
	}
	
	@Override
	public Component copy() {
		//change class from ComponentTemplate
		Component c = new HighSource(this.pos.copy(), this.getUUID());
		c.connections = this.connections; //TODO cannot copy connections (because wires) but they don't move with component
		c.selected = this.selected;
		return c;
	}
	
	public static void onLoad() {
		hud.addImage("HighSource");
		hud.addNewComponentButton("HighSource", "IO");
	}
}
