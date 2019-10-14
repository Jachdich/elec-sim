package com.cospox.elecsim;

import processing.core.PApplet;

public class HelperFunctions {
	public static boolean isInsideRect(int x, int y, int rx, int ry, int rw, int rh) {
		if (x >= rx && y >= ry) {
			if (x <= rx + rw && y <= ry + rh) {
				return true;
			}
		}
		return false;
	}

	public static boolean isInsideRect(float x, float y, float rx, float ry, float rw, float rh) {
		if (x >= rx && y >= ry) {
			if (x <= rx + rw && y <= ry + rh) {
				return true;
			}
		}
		return false;
	}

	public static boolean isPointOnLine(Vector a, Vector b, Vector c, float threshhold) {
		double top = Math.abs((c.y - a.y) * b.x
							- (c.x - a.x) * b.y
						 	+ c.x * a.y - c.y * a.x);
		
		double bottom = Math.sqrt(Math.pow(c.y - a.y, 2)
				                + Math.pow(c.x - a.x, 2));
		
		double dist = top / bottom;
		
		//if (a.x == c.x) { 
		//	System.out.println(a.toString() + b.toString() + c.toString());
		//	return true; 
		//} 
		if (!(dist < threshhold)) { return false; }
		if (b.x < c.x) { return false; }
		if (b.y > (a.y >= c.y ? a : c).y) { return false; }
		return true;
	}

	public static double gradient(Vector x, Vector y) {
		return (y.y - x.y) / (y.x - x.x);
	}

	public static double round(double value, int places) {
		if (places < 0) { throw new IllegalArgumentException(); }
		long factor = (long) Math.pow(10, places);
		value *= factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static boolean isInsideCircle(Vector point, Vector centre, float radius) {
		if (PApplet.dist(point.x, point.y, centre.x, centre.y) <= radius) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isInsideTriangle(Vector s, Vector a, Vector b, Vector c) {
		float as_x = s.x - a.x;
		float as_y = s.y - a.y;
		boolean s_ab = (b.x - a.x) * as_y - (b.y - a.y) * as_x > 0;
		if ((c.x - a.x) * as_y - (c.y - a.y) * as_x > 0 == s_ab) { return false; }
		if ((c.x - b.x) * (s.y - b.y) - (c.y - b.y) * (s.x - b.x) > 0 != s_ab) { return false; }
		return true;
	}

	public static boolean XOR(boolean a, boolean b) {
		if (a | b && !(a && b)) {
			return true;
		}
		return false;
	}
	
	public static float snap(float x) {
		return Math.round(x * 8) / 8;
	}
}