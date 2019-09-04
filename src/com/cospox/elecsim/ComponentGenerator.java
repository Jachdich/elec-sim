package com.cospox.elecsim;

public class ComponentGenerator {
	public static Component generateNewComponent(String name, Vector pos, int posInArray) {
		switch (name) {
		//Add new components
		case "Switch":
			return new Switch(pos, posInArray);
		case "AndGate":
			return new AndGate(pos, posInArray);
		case "OrGate":
			return new OrGate(pos, posInArray);
		case "XorGate":
			return new XorGate(pos, posInArray);
		case "NotGate":
			return new NotGate(pos, posInArray);
		case "Joint":
			return new Joint(pos, posInArray);
		default:
			Errors.error("Unknown component '" + name + "'");
			return null;
		}
	}
}
