package com.cospox.elecsim;

import java.util.ArrayList;

public class HistorySave {
	ArrayList<Component> components;
	ArrayList<Wire>      wires;
	public HistorySave(ArrayList<Component> components_, ArrayList<Wire> wires_) {
		this.wires = wires_;
		this.components = components_;
	}
}
