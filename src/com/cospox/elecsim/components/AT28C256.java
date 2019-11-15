package com.cospox.elecsim.components;

import com.cospox.elecsim.Connection;
import com.cospox.elecsim.ui.hud;
import com.cospox.elecsim.util.Vector;

public class AT28C256 extends IC {
	
	private byte[] memory = new byte[32768];
	
	private final static int[] IO_PINS = new int[]{18, 17, 16, 15, 14, 12, 11, 10};
	private final static int[] ADDR_PINS = new int[]{0, 25, 1, 22, 20, 23, 24, 2, 3, 4, 5, 6, 7, 8, 9};
	
	private boolean previousWE;
	private boolean previousCE;
	
	public AT28C256(Vector pos, int posInArray) {
		super(pos, 28, posInArray);

	}
	
	@Override
	public void update() {
		boolean we = this.connections[26].on;
		boolean oe = this.connections[21].on;
		boolean ce = this.connections[19].on;
		
		boolean[] output = new boolean[this.numPins];
		
		if (((we && ce && !this.previousWE) || (we && ce && !this.previousCE)) && !oe) {
			//rising edge of either WE or CE, with the other one high
			int address = 0;
			for (int pin: ADDR_PINS) {
				address = address << 1 | (this.connections[pin].on ? 1 : 0);
			}
			byte data = 0;
			for (int pin: IO_PINS) {
				data = (byte)(data << 1 | (this.connections[pin].on ? 1 : 0));
			}
			this.memory[address] = data;
			
		}
		
		if (oe && ce && !we) {
			int address = 0;
			for (int pin: ADDR_PINS) {
				address = address << 1 | (this.connections[pin].on ? 1 : 0);
			}
			byte data = this.memory[address];
			for (int i = 0; i < 8; i++) {
				boolean bit = ((data >> i) & 0x1) == 0 ? false : true;
				if (bit) {
					output[IO_PINS[7 - i]] = true;
				} else {
					output[IO_PINS[7 - i]] = false;
				}
			}
		}
		
		for (int i = 0; i < this.connections.length; i++) {
			Connection c = this.connections[i];
			boolean x    = output[i];
			if (x) {
				c.on();
			} else {
				c.off();
			}
		}
		
		this.previousWE = we;
		this.previousCE = ce;
	}
	
	public static void onLoad() {
		hud.addImage("AT28C256");
		hud.addNewComponentButton("AT28C256", "ICs");
	}
}
