/*
 * TODO
 * 
 * Some issues here are mirrored from GitHub. These are mostly user-level issues/bugs.
 * Code style bugs/issues/anything else goes here.
 *
 *move more things from game class to seperate classes?
 *refactor/clean up Game class and other classes - especially now that the wire copying works, it's very messy
 *comment all classes - IMPORTANT now that I won't be sole dev
 *done: None
 *
 *Taken from GitHub - close issue and delete line once 1000% fixed. Some are not on GitHub (IDK work it out yourself)
 *
 *make connections easier to click on? #2
 *
 *When dragging multiple components in snap to grid mode, its not smooth at all
 *snap to grid mode setting
 *
 *WE NEED A FUDGING SETTINGS MENU - Possibly consider second window, or even seperate library to make it look profesh.
 *
 *Add 'changes were made do u want to save pwese' on file open #16
 *When selecting component, first time does not show wire to mouse #11 NEEDS VERIFICATION VERIFIED, not just first time (snap to grid created issue)
 *In wire mode false, wire click detection is slightly off #19
 *Minor visual bug where sometimes the wrong/no connections outlined in red NEEDS VERIFICATION --sorta-verified
 *Some UI elements don't respond to font size changes #21
 *
 *Magnify icons when hovering in component selection menu
 *When hovering over components show tooltip - make a custom "onHover" event, where if there have been say 60 frames and the mouse hasn't moved it resets a timer and calls a function inside hud.
 *
 *these issues need verification to show if they have been fixed
 *
 *Sometimes wires don't get copied - AFTER CTRL-Z -MAYBE FIXED
 *Sometimes the selection square does not appear -MAYBE FIXED IDK
 *Wires STILL don't get selected properly with ctrl-a -MAYBE FIXED
 *
 *Component suggestions:
 *Clock - adjustable freq?????? HOW
 *Logic blocks/ICs/logic mode and (existing)IC mode/User-created ICs/packages/things like rsnor IDK
 *
 *Feature suggestion:
 *Snap to grid mode #22, added! needs work? add setting to show how course the grid is?
 *Settings menu & logic iterations per frame setting #9
 *Add ignore button to path error message
 *Keyboard shortcut menu (ctrl-z and others that aren't intrinsically shown)
 *LABELS/COMMENTS! COOL
 *
*/

package com.cospox.elecsim;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import com.cospox.elecsim.components.Component;
import com.cospox.elecsim.ui.hud;
import com.cospox.elecsim.util.OnLoadManager;
import com.cospox.elecsim.util.ComponentEncoder;
import com.cospox.elecsim.util.ComponentGenerator;
import com.cospox.elecsim.util.FileHandler;
import com.cospox.elecsim.util.HelperFunctions;
import com.cospox.elecsim.util.HistorySave;
import com.cospox.elecsim.util.KeyHandler;
import com.cospox.elecsim.util.Vector;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.event.MouseEvent;

public class Game {
	public ArrayList<Component> components = new ArrayList<Component>();
	public ArrayList<Wire> wires = new ArrayList<Wire>();
	
	public ArrayList<Component> selectedComponents = new ArrayList<Component>();
	public ArrayList<Wire>      selectedWires      = new ArrayList<Wire>();
	
	private ArrayList<Object> copyBuffer = new ArrayList<Object>();
	
	public String[] selectedTool = {"select", "wire", "component"}; //TODO make it just a string IDK
	public String selectedComponent = "AndGate";
	public Connection selectedConnection = null;
	private Component selectedConComponent = null;
	public String loadedFileName = null;
	public ArrayList<HistorySave> undoHistory = new ArrayList<HistorySave>();
	public ArrayList<HistorySave> redoHistory = new ArrayList<HistorySave>();
	
	public Vector translate = new Vector();
	public float zoom = 1;
	private hud hud;
	private PApplet parent;
	
	private KeyHandler keys = new KeyHandler();
	public HashMap<String, Boolean> states = new HashMap<String, Boolean>();
	
	private String gameDataDir;
	
	public static PFont FONT;
	public static String FONT_FAMILY = "SansSerif";
	public static int FONT_SIZE = 12;
	
	public static float FONT_HEIGHT;
	
	private int framesSinceMouseMove = 0;
	
	public Game(PApplet applet) {
		this.parent = applet;
		this.hud = new hud(applet);
		
		Game.FONT = applet.createFont(Game.FONT_FAMILY, Game.FONT_SIZE);
		
		applet.textAlign(PConstants.LEFT, PConstants.TOP);
		applet.textFont(Game.FONT);
		
		Game.FONT_HEIGHT = applet.textAscent() + applet.textDescent();
		
		try {
			this.gameDataDir = new File(
					Game.class.getProtectionDomain().getCodeSource()
					.getLocation().toURI()).getParentFile().getAbsolutePath()
					+ File.separator + "assets"
					+ File.separator + "gamedata";
		} catch (URISyntaxException e) {
			this.states.put("pathError", true);
			this.gameDataDir = null;
			e.printStackTrace();
		}
		
		this.states.put("canExit", true);
		this.states.put("saving", false);
		this.states.put("wireMode", false);
		this.states.put("exitAfterSave", false);
		this.states.put("halt", false);
		this.states.put("draggingComponents", false);
		this.states.put("dialogueOpen", false);
		if (this.states.get("pathError") == null) {
			this.states.put("pathError", false);
		}
			
		
		//load previously loaded filename && open that file on startup
		boolean createConfig = !FileHandler.fileExists(this.gameDataDir + File.separator + "save.txt");
		if (createConfig) {
			File config = new File(this.gameDataDir + File.separator + "save.txt");
			config.getParentFile().mkdirs();
			try {
				config.createNewFile();
			} catch (IOException e) {
				this.states.put("pathError", true);
				e.printStackTrace();
			}
		}
		String filename = FileHandler.read(this.gameDataDir + File.separator + "save.txt");
		System.out.println("LOADING FROM: " + filename);
		if (filename != "" && filename != "\n" && filename != " ") {
			//this.loadFromFile(filename);
			//this.loadedFileName = filename;
		}
		this.doOnLoad();
	}
	
	private void doOnLoad() {
		try {
			OnLoadManager.doOnLoadPriority();
			OnLoadManager.doOnLoad();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void draw(PApplet applet) {
		if (this.states.get("halt")) { return; }
		if (this.states.get("pathError")) {
			//display path error message
			applet.background(0);
			applet.text("Error: A path error was thrown when trying to access the config files.", 10, 1 * 15);
			applet.text("This probably means that either a folder disappeared unexpectedly, or", 10, 2 * 15);
			applet.text("the folder path has special characters in it that aren't allowed.", 10, 3 * 15);
			applet.text("Try starting the program again, and if the problem persists,", 10, 4 * 15);
			applet.text("check the folders for special characters like ' . and £.", 10, 5 * 15);
			return;
		}
		//update the logic 10 times per frame, reduces noticable lag
		//TODO May cause performance issues, maybe add setting to change updates per frame?
		for (int i = 0; i < 10; i++) { this.update(); }
		
		applet.pushMatrix();
		applet.translate(this.translate.x, this.translate.y);
		applet.scale(this.zoom);
		applet.background(255);
		
		for (Wire w: this.wires) { w.draw(applet, this.translate, this.zoom); }
		
		//draw line from selectedConnection to mouse
		if (this.selectedConnection != null) {
			applet.line((applet.mouseX - this.translate.x) / this.zoom,
					    (applet.mouseY - this.translate.y) / this.zoom, 
					    this.selectedConnection.pos.x,
					    this.selectedConnection.pos.y);
		}
		
		for (Component c: this.components) {
			//calculate screen x and y values
			int adjusted_x = (int)(this.translate.x + c.pos.x * this.zoom);
			int adjusted_y = (int)(this.translate.y + c.pos.y * this.zoom);
			//check if component is on screen, otherwise don't draw it
			if (adjusted_x > -60 * this.zoom &&
					adjusted_x < applet.width + 60 * this.zoom &&
					adjusted_y > -60 * this.zoom &&
					adjusted_y < applet.height + 60 * this.zoom) {
				//applet.rotate((float) Math.toRadians(applet.frameCount / 5 % 360));
				c.draw(applet);
			}	
		}
		applet.popMatrix();
		this.hud.draw(applet, this);
		
		if (this.states.get("saving")) {
			applet.noStroke();
			applet.fill(200, 200, 200, 150);
			applet.rect(0, 0, applet.width, applet.height);
			applet.fill(255);
			applet.stroke(50);
			int bw = 80;
			int bh = 30;
			applet.rect(applet.width / 2 - bw / 2, applet.height / 2 - bh / 2, bw, bh, 12);
			applet.rect(applet.width / 2 - bw / 2, applet.height / 2 + 20, bw, bh, 12);
			applet.rect(applet.width / 2 - bw / 2, applet.height / 2 + 56, bw, bh, 12);
			applet.textSize(20);
			applet.fill(0);
			int px = -bw / 2 + 15; //padding x
			int py = -bh / 2 + 21; //padding y
			applet.text("Save", applet.width / 2 + px, applet.height / 2 + py - 18);
			applet.text("Exit", applet.width / 2 + px + 5, applet.height / 2 + py + 18);
			applet.text("Cancel", applet.width / 2 + px - 8, applet.height / 2 + py + 52);
			applet.textSize(12);
		}
		
		if (applet.mouseX == applet.pmouseX && applet.mouseY == applet.pmouseY) {
			this.framesSinceMouseMove += 1;
			if (this.framesSinceMouseMove == 60) {
				this.onMouseHover();
			}
		} else {
			if (this.framesSinceMouseMove > 60) {
				this.unMouseHover();
			}
			this.framesSinceMouseMove = 0;
		}
	}
	
	public boolean dispose() {
		//run on quit by main class 
		//write last filename loaded to file. Return whether the sketch should actually exit or not
		FileHandler.write(this.gameDataDir + File.separator + "save.txt", (this.loadedFileName == null ? "" : this.loadedFileName));
		System.out.println("assets/gamedata/save.txt is: " + (this.loadedFileName == null ? "" : this.loadedFileName));
		if (!this.states.get("canExit")) {
			System.out.println("Prompted to save");
			this.promptToSave();
			return false;
		} else { return true; }
	}
	
	public Component getComponentByUUID(long uuid) {
		for (Component c: this.components) {
			if (c.getUUID() == uuid) {
				return c;
			}
		}
		return null;
	}
	
	public void setWireMode(boolean mode) {
		this.states.put("wireMode", mode);
		for (Wire w: this.wires) {
			w.wireMode = mode;
		}
	}
	
	public boolean getWireMode() {
		return this.states.get("wireMode");
	}
	
	public void promptToSave() {
		this.states.put("saving", true);
	}
	
	public void error(String text) {
		System.out.println(text);
	}
	
	public void saveAs() {
		if (!this.states.get("dialogueOpen")) {
			this.parent.selectOutput("Select file...", "saveAsCallback", null, this);
			this.states.put("dialogueOpen", true);
		}
	}
	
	public void open() {
		//TODO prompt to save
		if (!this.states.get("dialogueOpen")) {
			this.parent.selectInput("Select file...", "openFileCallback", null, this);
			this.states.put("dialogueOpen", true);
		}
	}
	
	public void saveAsCallback(File selection) {
		this.states.put("dialogueOpen", false);
		if (selection == null) { return; }
		String fileName = selection.getAbsoluteFile().toString();
		this.saveToFile(fileName);
		this.loadedFileName = fileName;
		this.states.put("canExit", true);
		if (this.states.get("saving")) { //TODO messy IDK
			this.parent.exit();
		}
	}
	
	public void openFileCallback(File selection) {
		this.states.put("dialogueOpen", false);
		if (selection == null) { return; }
		String fileName = selection.getAbsoluteFile().toString();
		this.states.put("halt", true);
		this.loadFromFile(fileName);
		this.loadedFileName = fileName;
		this.states.put("halt", false);
	}
	
	public void newFile() {
		//TODO prompt to save
		this.clear();
		this.loadedFileName = null;
	}
	
	public void clear() {
		this.components.clear();
		this.wires.clear();
	}
	
	public void updateUndoHistory() {
		this.undoHistory.add(new HistorySave(this.components, this.wires));
		this.redoHistory.clear();
	}
	
	public void updateRedoHistory() {
		this.redoHistory.add(new HistorySave(this.components, this.wires));
	}
	
	public void undo() {
		//get last history point and replace components and wires
		int size = this.undoHistory.size();
		if (size - 1 < 0) { return; }
		this.updateRedoHistory();
		HistorySave h = this.undoHistory.get(size - 1);
		this.undoHistory.remove(size - 1);
		this.wires = h.wires;
		this.components = h.components;
		//as the components have been copied, selectedComponents is inacurate.
		this.selectedComponents.clear();
		
		for (Component c: this.components) {
			c.updateConnectionsPos();
			if (c.selected) {
				this.selectedComponents.add(c);
			}
		}
	}
	
	public void redo() {
		//Yes, this is a copy/paste of void undo().
		//get last history point and replace components and wires
		int size = this.redoHistory.size();
		if (size - 1 < 0) { return; }
		this.undoHistory.add(new HistorySave(this.components, this.wires));
		HistorySave h = this.redoHistory.get(size - 1);
		this.redoHistory.remove(size - 1);
		this.wires = h.wires;
		this.components = h.components;
		//as the components have been copied, selectedComponents is inacurate.
		this.selectedComponents.clear();
		
		for (Component c: this.components) {
			c.updateConnectionsPos();
			if (c.selected) {
				this.selectedComponents.add(c);
			}
		}
	}
	
	public String generateText() {
		//generate string version of every component to save to file
		String file = "";
		for (Component c: this.components) {
			file += ComponentEncoder.componentToString(c) + "\n";
		}
		for (Wire w: this.wires) {
			file += ComponentEncoder.wireToString(w, this) + "\n";
		}
		return file;
	}
	
	public void save() {
		if (this.loadedFileName == null) {
			this.states.put("canExit", false);
			this.saveAs();
		} else {
			this.saveToFile(this.loadedFileName);
			this.states.put("canExit", true);
		}
	}
	
	public void saveToFile(String fileName) {
		//TODO GZIP - compress file
		String file = this.generateText();
		FileHandler.write(fileName, file);
	}
	
	public void load(String file) {
		this.components.clear();
		this.wires.clear();
		this.undoHistory.clear();
		this.redoHistory.clear();
		for (String line: file.replace("\n", "").split(";")) {
			if (line == "") { continue; }
			switch (line.split("\\(")[0]) {
			case "Wire":
				this.wires.add(ComponentEncoder.unpackWireCall(line, this));
				break;
			default:
				this.components.add(ComponentEncoder.unpackComponentCall(line, this.components));
			}
		}
	}
	
	public void loadFromFile(String filename) {
		//TODO GZIP - compress file
		//TODO check if file is in the correct format
		this.load(FileHandler.read(filename));
	}
	
	private void selectAll() {
		this.selectedComponents.clear();
		for (Component c: this.components) {
			this.selectedComponents.add(c);
			c.select();
		}
		
		this.selectedWires.clear();
		for (Wire w: this.wires) {
			this.selectedWires.add(w);
			w.select();
		}
	}
	
	private void deSelectAll() {
		this.selectedComponents.clear();
		for (Component c: this.components) {
			this.selectedComponents.remove(c);
			c.deSelect();
		}
		
		this.selectedWires.clear();
		for (Wire w: this.wires) {
			this.selectedWires.remove(w);
			w.deSelect();
		}
	}
	
	private void copy() {
		this.copyBuffer.clear();
		for (Component c: this.selectedComponents) {
			this.copyBuffer.add(c);
		}
		for (Wire w: this.selectedWires) {
			this.copyBuffer.add(w);
		}
	}

	private void paste() {
		//deselect the currently selected components & wires
		
		this.updateUndoHistory();
		
		for (Component c: this.selectedComponents) {
			c.deSelect();
		}
		for (Wire w: this.selectedWires) {
			w.deSelect();
		}
		
		this.selectedComponents.clear();
		this.selectedWires.clear();
		
		ArrayList<Component> queue = new ArrayList<Component>(); //we can't remove the referances (in old component) to the
		//new component immediately, more than one wire may need to use it. So add to queue to remove in bulk.
		
		//Iterate over every object in the buffer
		for (Object o: this.copyBuffer) {
			if (o.getClass().getSuperclass() == Component.class) {
				//do stuff with o as a component
				Component c = (Component)o;
				
				//add new component at +20x +20y from the original
				String name = c.getClass().getSimpleName();
				Vector pos = new Vector(c.pos.x + 20, c.pos.y + 20);
				this.addNewComponent(name, pos);
				this.selectedComponents.add(this.components.get(this.components.size() - 1));
				this.components.get(this.components.size() - 1).select();
				c.externalFlags.put("newcopy", this.components.get(this.components.size() - 1));
				
			} else if (o.getClass() == Wire.class) {
				//do stuff with o as wire
				Wire w = (Wire)o;

				Component c1 = w.refs;
				Component c2 = w.refe;
				Component newc1 = (Component) c1.externalFlags.get("newcopy");
				Component newc2 = (Component) c2.externalFlags.get("newcopy");
				if (newc1 == null || newc2 == null) {
					//at least one of the start/end component wasn't copied,
					//so we can't copy the wire
					continue;
				}
				Wire n = new Wire(newc1.connections[w.s.posInComponent], newc2.connections[w.e.posInComponent], newc1, newc2);
				
				this.wires.add(n);
				this.selectedWires.add(n);
				n.select();
				
				//add to queue to remove the referance
				queue.add(c1);
				queue.add(c2);
				
			} else {
				//we don't know what it is so error
				this.error("Error: expected type Class<Wire> or Class<Component>, got " + o.getClass());
			}
		}
		for (Component c: queue) {
			c.externalFlags.put("newcopy", null);
		}
		queue.clear();
		
		this.copy();
	}

	private void componentInteraction(PApplet applet) {
		//add wires & components to screen
		if (this.selectedTool[0] == "wire") {
			Connection conn = null;
			Component  comp = null;
			
			for (Component c: this.components) {
				conn = c.getClickedConnection(new Vector(applet.mouseX, applet.mouseY),
						this.zoom, this.translate);
				if (conn != null) { comp = c; break; }
			}
			
			if (conn != null) {
				if (this.selectedConnection == null) {
					this.selectedConnection = conn;
					this.selectedConComponent = comp;
				} else {
					if (!conn.equals(this.selectedConnection)) {
						this.updateUndoHistory(); //TODO check
						this.wires.add(new Wire(conn, this.selectedConnection, comp, this.selectedConComponent));
						this.wires.get(this.wires.size() - 1).wireMode = this.states.get("wireMode");
						this.states.put("canExit", false);
						this.selectedConnection = null;
						//this.updateUndoHistory();
					}
				}
			}
			
			
		} else if (this.selectedTool[0] == "component" && !this.hud.isInsideHUDArea(
				new Vector(applet.mouseX, applet.mouseY), 
				new Vector(applet.width, applet.height))) {
			Vector pos = new Vector((applet.mouseX - this.translate.x) / this.zoom, (applet.mouseY - this.translate.y) / this.zoom);
			this.states.put("canExit", false);
			this.updateUndoHistory();
			this.addNewComponent(this.selectedComponent, pos);
		}
	}
	
	public void selectComponentsInRect(Vector a, Vector b) {
		Vector x = new Vector((a.x - this.translate.x) / this.zoom,
				(a.y - this.translate.y) / this.zoom);
		Vector y = new Vector((b.x - this.translate.x) / this.zoom,
				(b.y - this.translate.y) / this.zoom);
		
		//find (x, y) of top left corner
		//and the width and height of the rectangle
		//by brute force: we don't know which corner 
		//Vector a and Vector b represent.applet
		float rx, ry, w, h;
		if (y.x > x.x) {
			w  = y.x - x.x;
			rx = x.x;
		} else {
			w  = x.x - y.x;
			rx = y.x;
		}
		if (y.y > x.y) {
			h  = y.y - x.y;
			ry = x.y;
		} else {
			h  = x.y - y.y;
			ry = y.y;
		}
		
		for (Component c: this.components) {
			//if it's inside the rectangle & it hasn't been selected yet,
			//select it. Else deselect it.
			if (HelperFunctions.isInsideRect(c.pos.x, c.pos.y, rx, ry, w, h)) {
				c.select();
				if (!this.selectedComponents.contains(c)) {
					this.selectedComponents.add(c);
				}
			} else {
				c.deSelect();
				this.selectedComponents.remove(c);
			}
		}
		for (Wire wire: this.wires) {
			//do the same for the wires
			if (HelperFunctions.isInsideRect(wire.s.pos.x, wire.s.pos.y, rx, ry, w, h)) {
				wire.select();
				if (!this.selectedWires.contains(wire)) {
					this.selectedWires.add(wire);
				}
			} else {
				wire.deSelect();
				this.selectedWires.remove(wire);
			}
		}
	}
	
	private void addNewComponent(String name, Vector pos) {
		int posInArray = this.components.size(); //end of array - pos that new component will be in
		this.components.add(ComponentGenerator.generateNewComponent(name, pos, posInArray));
	}
	
	public void mouseClicked(PApplet applet) {
		this.hud.mouseClicked(applet, this);
		
		//right-click clears selected component
		if (applet.mouseButton == PConstants.RIGHT) {
			this.selectedConnection = null;
		}
		
		if (this.states.get("saving")) {
			int bw = 80;
			int bh = 30;
			if (HelperFunctions.isInsideRect(applet.mouseX, applet.mouseY, 
					applet.width / 2 - bw / 2, applet.height / 2 - bh / 2, bw, bh)) {
				
				//save & exit
				this.states.put("canExit", true);
				this.save();
				applet.exit();
			}
			if (HelperFunctions.isInsideRect(applet.mouseX, applet.mouseY,
					applet.width / 2 - bw / 2, applet.height / 2 + 20, bw, bh)) {
				this.states.put("canExit", true);
				//exit
				applet.exit();
			}
			if (HelperFunctions.isInsideRect(applet.mouseX, applet.mouseY,
					applet.width / 2 - bw / 2, applet.height / 2 + 56, bw, bh)) {
				//cancel
				this.states.put("saving", false);
			}
			return;
		}
		
		if (this.selectedTool[0] == "select") {
			//try to select components/wires
			for (Wire w: this.wires) {
				boolean shouldSelect = w.isMouseIntersecting(applet, this.translate, this.zoom);
				if (shouldSelect) {
					w.select();
					this.selectedWires.add(w);
				} else {
					if (!this.keys.ctrl()) {
						w.deSelect();
						this.selectedWires.remove(w);
					}
				}
			}
			
			for (Component c: this.components) {
				if (c.isMouseIntersecting(new Vector(applet.mouseX, applet.mouseY), this.zoom, this.translate)) {
					c.select();
					if (!this.selectedComponents.contains(c)) {
						this.selectedComponents.add(c);
					}
				} else {
					if (!this.keys.ctrl()) {
						c.deSelect();
						this.selectedComponents.remove(c);
					}
				}
			}
		}

		for (Component c: this.components) {
			c.onMousePressed(applet, this.zoom, this.translate);
		}

		this.componentInteraction(applet);
	}
	
	public void onMouseHover() {
		System.out.println("hovered");
	}
	
	public void unMouseHover() {
		System.out.println("unhovered");
	}

	public void mouseDragged(MouseEvent event, PApplet applet) {
		//if it's the right click button then pan
		if (event.getButton() == 39) {
			this.translate.x += applet.mouseX - applet.pmouseX;
			this.translate.y += applet.mouseY - applet.pmouseY;
			
		//otheriwse, if the select tool is selected and the HUD isn't blocking component movements
		//i.e. the HUD is drawing the 'selection square'
		} else if (this.selectedTool[0] == "select" && !this.hud.canSelect
				&& event.getButton() == 37) {
			this.states.put("canExit", false);
			
			if (!this.states.get("draggingComponents")) {
				this.updateUndoHistory();
				this.states.put("draggingComponents", true);
			}
			
			for (Component c: this.selectedComponents) {
				//move components with mousepointer
				c.setX(c.pos.x + (applet.mouseX - applet.pmouseX) * 1 / this.zoom);
				c.setY(c.pos.y + (applet.mouseY - applet.pmouseY) * 1 / this.zoom);
			}
		}
	}
	
	public void mousePressed(PApplet applet) {
		this.hud.mousePressed(applet, this);
	}
	
	public void mouseReleased(PApplet applet) {
		this.hud.mouseReleased();
		if (this.states.get("draggingComponents")) {
			this.states.put("draggingComponents", false);
		}
	}

	public void mouseWheel(MouseEvent event, PApplet applet) {
		//magic
		this.translate.x -= applet.mouseX;
		this.translate.y -= applet.mouseY;
		float delta = (float)(event.getCount() < 0 ? 1.05 : event.getCount() > 0 ? 1.0/1.05 : 1.0);
		this.zoom *= delta;
		this.translate.x *= delta;
		this.translate.y *= delta;
		this.translate.x += applet.mouseX;
		this.translate.y += applet.mouseY;
	}
	
	public void keyReleased(char key, int keyCode) {
		this.keys.released(keyCode, key);
	}
	
	public void keyPressed(char key, int keyCode) {
		this.keys.pressed(keyCode, key);
		
		if (this.keys.get('u') && !this.keys.shift() && !this.keys.ctrl()) { this.update(); }				//u = update
		if (this.keys.get('t') && !this.keys.shift() && !this.keys.ctrl()) { this.switchSelectedTool(); }   //t = toggle tool
		if (this.keys.ctrl() && !this.keys.shift() && this.keys.get(83)) { this.save(); }					//ctrl+s = save
		if (this.keys.ctrl() && this.keys.shift() && this.keys.get(83)) { this.saveAs(); }					//ctrl+shift+s = save as
		if (this.keys.ctrl() && this.keys.shift() && this.keys.get(65)) { this.deSelectAll(); }				//ctrl+shift+a = deselect all
		if (this.keys.ctrl() && !this.keys.shift() && this.keys.get(80)) { this.clear(); }					//ctrl+p = clear
		if (this.keys.ctrl() && !this.keys.shift() && this.keys.get(79)) { this.open(); }					//ctrl+o = open
		if (this.keys.ctrl() && !this.keys.shift() && this.keys.get(90)) { this.undo(); }					//ctrl+z = undo
		if (this.keys.ctrl() && !this.keys.shift() && this.keys.get(89)) { this.redo(); }					//ctrl+y = redo
		if (this.keys.ctrl() && !this.keys.shift() && this.keys.get(67)) { this.copy(); }					//ctrl+c = copy
		if (this.keys.ctrl() && !this.keys.shift() && this.keys.get(86)) { this.paste(); }					//ctrl+v = paste
		if (this.keys.ctrl() && !this.keys.shift() && this.keys.get(65)) { this.selectAll(); }				//ctrl+a = select all
		if (this.keys.ctrl() && !this.keys.shift() && this.keys.get(78)) { this.newFile(); }				//ctrl+n = new
		
		if (this.keys.get('b')) { this.breakFunc(); } //break on demand
		//if (this.keys.get('g')) { this.components.get(0).rotate(15); }
		//if (this.keys.get('r')) { this.addNewComponent("AT28C256", new Vector()); }
		
		if (this.keys.get('y') && !this.keys.shift() && !this.keys.ctrl()) {								//y = switch wire mode
			this.states.put("wireMode", !this.states.get("wireMode"));
			for (Wire w: this.wires) {
				w.wireMode = this.states.get("wireMode");
			}
		}

		if (this.keys.get(8) || this.keys.get(147) && !this.keys.shift() && !this.keys.ctrl()) {
			//del or backspace = delete selected components
			this.updateUndoHistory();
			for (Component c: this.selectedComponents) {
				this.components.remove(c);
			}
			for (Wire w: this.selectedWires) {
				this.wires.remove(w);
			}
			this.selectedWires.clear();
			this.selectedComponents.clear();
		}
	}

	public void switchSelectedTool() {
		//rotate the selected tool array round 1 place
		String temp = this.selectedTool[0];
		this.selectedTool[0] = this.selectedTool[1];
		this.selectedTool[1] = this.selectedTool[2];
		this.selectedTool[2] = temp;
	}

	private void update() {
		//update logic on all components
		for (Component c: this.components) { c.update(); }
		for (int i = 0; i < 5; i++) { //TODO WHY SO MANY TIMES???
			for (Wire w: this.wires) { w.update(); }
		}
	}
	
	private void breakFunc() {
		//In case anyone's wondering,
		//this is a do-nothing function so that I can set a breakpoint inside
		//and bind a ket to run the function, effectively having a 'break' key
		//which is useful for debugging a GUI program.
		int xasd = 0;
		xasd += 1;
		System.out.println(xasd);
	}

	public boolean isOrphand(Wire w) {
		if (!this.components.contains(w.refe) || !this.components.contains(w.refs)) {
			//this.wires.remove(w);
			return true;
		}
		return false;
	}
}
