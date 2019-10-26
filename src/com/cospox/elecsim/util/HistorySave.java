package com.cospox.elecsim.util;

import java.util.ArrayList;

import com.cospox.elecsim.Wire;
import com.cospox.elecsim.components.Component;

public class HistorySave {
	public ArrayList<Component> components = new ArrayList<Component>();
	public ArrayList<Wire>      wires      = new ArrayList<Wire>();
	
	public HistorySave(ArrayList<Component> components, ArrayList<Wire> wires) {
		for (Component c: components) {
			this.components.add(c.copy());
		}
		for (Wire w: wires) {
			this.wires.add(w.copy());
		}
	}
}
