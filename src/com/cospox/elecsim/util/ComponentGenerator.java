package com.cospox.elecsim.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.cospox.elecsim.components.Component;

public class ComponentGenerator {
	public static Component generateNewComponent(String name, Vector pos, int posInArray) {
		try {
			Constructor<?> ctor = Class.forName("com.cospox.elecsim.components." + name).getConstructor(Vector.class, int.class);
			Object object = ctor.newInstance(new Object[] {pos, posInArray} );
			return (Component)object;
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			Errors.error("Unknown/broken component '" + name + "'");
			return null;
		}
		
//		switch (name) {
//		//Add new components
//		case "Switch":
//			return new Switch(pos, posInArray);
//		case "AndGate":
//			return new AndGate(pos, posInArray);
//		case "OrGate":
//			return new OrGate(pos, posInArray);
//		case "XorGate":
//			return new XorGate(pos, posInArray);
//		case "NotGate":
//			return new NotGate(pos, posInArray);
//		case "Joint":
//			return new Joint(pos, posInArray);
//		case "HighSource":
//			return new HighSource(pos, posInArray);
//		case "LowSource":
//			return new LowSource(pos, posInArray);
//		case "AT28C256":
//			return new AT28C256(pos, posInArray);
//		default:
//			Errors.error("Unknown component '" + name + "'");
//			return null;
//		}
	}
}
