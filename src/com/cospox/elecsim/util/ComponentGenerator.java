package com.cospox.elecsim.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.cospox.elecsim.components.Component;

public class ComponentGenerator {
	public static long currentUUID = 0;
	
	public static long nextUUID() {
		return ComponentGenerator.currentUUID++;
	}
	
	public static Component generateNewComponent(String name, Vector pos, int posInArray, long uuid) {
		try {
			Constructor<?> ctor = Class.forName("com.cospox.elecsim.components." + name).getConstructor(Vector.class, long.class);
			Object object = ctor.newInstance(new Object[] {pos, uuid} );
			return (Component)object;
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			Errors.error("Unknown/broken component '" + name + "'");
			return null;
		}
	}
	
	public static Component generateNewComponent(String name, Vector pos, int posInArray) {
		return ComponentGenerator.generateNewComponent(name, pos, posInArray, ComponentGenerator.nextUUID());
	}
}
