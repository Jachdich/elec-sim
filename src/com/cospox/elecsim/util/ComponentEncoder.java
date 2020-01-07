package com.cospox.elecsim.util;

import java.util.ArrayList;

import com.cospox.elecsim.Game;
import com.cospox.elecsim.Wire;
import com.cospox.elecsim.components.Component;

public class ComponentEncoder {
	public static Wire unpackWireCall(String call, Game game) {
		//convert 'Wire(args)' into needed info to construct a Wire object
		String data = call.replace("Wire(", "").replace(")", "");
		String s = data.split(",")[0].replace("[", "").replace("]", "");
		String e = data.split(",")[1].replace("[", "").replace("]", "");
		
		long sposx = Integer.parseInt(s.split(" ")[0]);
		int sposy  = Integer.parseInt(s.split(" ")[1]);
		long eposx = Integer.parseInt(e.split(" ")[0]);
		int eposy  = Integer.parseInt(e.split(" ")[1]);
		
		Component a = game.getComponentByUUID(sposx);
		Component b = game.getComponentByUUID(eposx);
		if (a == null || b == null) {
			System.out.println("Oopsie, did a bad");
			return new Wire(game.components.get(0).connections[0], game.components.get(1).connections[0], game.components.get(0), game.components.get(1));
		}
		
		return new Wire(a.connections[(int)sposy],
								b.connections[eposy],
								a, b);
	}
	
	public static Component unpackComponentCall(String call, ArrayList<Component> components) {
		//convert 'Component(args)' into needed data to reconstruct a Component object
		String vectorCall = call.split("\\(")[2].split("\\)")[0];
		System.out.println(call);
		long uuid = Long.parseLong(call.split("\\)")[1].replace(" ", "").replace(",", ""));
		String type = call.split("\\(")[0];
		float x = Float.parseFloat(vectorCall.split(",")[0].replace(" ", ""));
		float y = Float.parseFloat(vectorCall.split(",")[1].replace(" ", ""));
		return ComponentGenerator.generateNewComponent(type, new Vector(x, y), components.size(), uuid);
	}
	
	public static String componentToString(Component c) {
		String out = "";
		out += c.getClass().getSimpleName() + "(";
		out += c.pos.toString() + ",";
		out += Long.toString(c.getUUID());
		out += ");";
		return out;
	}
	
	public static String wireToString(Wire w, Game game) {
		if (game.isOrphand(w)) {
			return "";
		}
		String out = "";
		out += "Wire(";
		out += "[" + w.getPosition(w.s) + "],";
		out += "[" + w.getPosition(w.e) + "]);";
		return out;
	}
}
