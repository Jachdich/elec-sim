package com.cospox.elecsim;

import java.util.ArrayList;

public class HistorySave {
	ArrayList<Component> components = new ArrayList<Component>();
	ArrayList<Wire>      wires      = new ArrayList<Wire>();
	
	ArrayList<Component> selectedComponents = new ArrayList<Component>();
	ArrayList<Wire>      selectedWires      = new ArrayList<Wire>();
	public HistorySave(ArrayList<Component> components, ArrayList<Wire> wires, ArrayList<Component> selectedComponents, ArrayList<Wire> selectedWires) {
		for (Component c: components) {
			this.components.add(c.copy());
		}
		for (Wire w: wires) {
			this.wires.add(w.copy());
		}
		
		for (Component c: selectedComponents) {
			this.selectedComponents.add(c.copy());
		}
		for (Wire w: selectedWires) {
			this.selectedWires.add(w.copy());
		}
	}
}
