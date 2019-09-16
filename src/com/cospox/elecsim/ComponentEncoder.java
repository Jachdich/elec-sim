package com.cospox.elecsim;

import java.util.ArrayList;

public class ComponentEncoder {
	public static Wire unpackWireCall(String call, ArrayList<Component> components) {
		//convert 'Wire(args)' into needed info to construct a Wire object
		String data = call.replace("Wire(", "").replace(")", "");
		String s = data.split(",")[0].replace("[", "").replace("]", "");
		String e = data.split(",")[1].replace("[", "").replace("]", "");
		Vector spos = new Vector(Integer.parseInt(s.split(" ")[0]),
								 Integer.parseInt(s.split(" ")[1]));
		Vector epos = new Vector(Integer.parseInt(e.split(" ")[0]),
				 				 Integer.parseInt(e.split(" ")[1]));
		return new Wire(components.get((int)spos.x).connections[(int)spos.y],
								components.get((int)epos.x).connections[(int)epos.y],
								components.get((int)spos.x),
								components.get((int)epos.x));
	}
	
	public static Component unpackComponentCall(String call, ArrayList<Component> components) {
		//convert 'Component(args)' into needed data to reconstruct a Component object
		String vectorCall = call.split("\\(")[2].split("\\)")[0];
		String type = call.split("\\(")[0];
		float x = Float.parseFloat(vectorCall.split(",")[0].replace(" ", ""));
		float y = Float.parseFloat(vectorCall.split(",")[1].replace(" ", ""));
		return ComponentGenerator.generateNewComponent(type, new Vector(x, y), components.size());
	}
	
	public static String componentToString(Component c) {
		String out = "";
		out += c.TYPE + "(";
		out += c.pos.toString();
		out += ");";
		return out;
	}
	
	public static String wireToString(Wire w) {
		String out = "";
		out += "Wire(";
		out += "[" + w.getPosition(w.s) + "],";
		out += "[" + w.getPosition(w.e) + "]);";
		return out;
	}
}
