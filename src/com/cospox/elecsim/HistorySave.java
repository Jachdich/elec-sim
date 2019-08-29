package com.cospox.elecsim;

import java.util.ArrayList;

public class HistorySave {
	ArrayList<Component> components = new ArrayList<Component>();
	ArrayList<Wire>      wires      = new ArrayList<Wire>();
	public HistorySave(ArrayList<Component> components_, ArrayList<Wire> wires_) {
		for (Component c: components_) {
			this.components.add(c.copy());
		}
		for (Wire w: this.wires) {
			this.wires.add(w.copy());
		}
	}
}
