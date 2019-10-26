package com.cospox.elecsim;

public class Vector {
	public float x;
	public float y;
	public Vector() {
		this.x = 0;
		this.y = 0;
	}
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public String toString() {
		return "new Vector(" + Float.toString(this.x) + ", " + Float.toString(this.y) + ")";
	}
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector copy() {
		return new Vector(this.x, this.y);
	}
}
