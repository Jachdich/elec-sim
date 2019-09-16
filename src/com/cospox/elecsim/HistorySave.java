package com.cospox.elecsim;

import java.util.ArrayList;

public class HistorySave {
	ArrayList<Component> components = new ArrayList<Component>();
	ArrayList<Wire>      wires      = new ArrayList<Wire>();
	public HistorySave(ArrayList<Component> components, ArrayList<Wire> wires) {
		for (Component c: components) {
			this.components.add(c.copy());
		}
		for (Wire w: wires) {
			this.wires.add(w.copy());
		}
	}
}
