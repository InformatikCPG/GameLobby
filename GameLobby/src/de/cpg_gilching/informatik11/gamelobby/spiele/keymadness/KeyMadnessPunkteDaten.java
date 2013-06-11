package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class KeyMadnessPunkteDaten {

	public Point[][] pfade;
	public Point[] checkpoints;
	public List<Integer> tastencodes;
	
	public KeyMadnessPunkteDaten(int spieleranzahl){
		tastencodes = new ArrayList<Integer>();
		tastencodes.add(KeyEvent.VK_UP);
		tastencodes.add(KeyEvent.VK_DOWN);
		tastencodes.add(KeyEvent.VK_LEFT);
		tastencodes.add(KeyEvent.VK_RIGHT);
		switch (spieleranzahl) {
		case 2:
			pfade = new Point[][]{
					new Point[]{new Point(100, -50), new Point(100, 300), new Point(500, 300), new Point(500, 650)},
					new Point[]{new Point(500, 650), new Point(500, 300), new Point(100, 300), new Point(100, -50)}};
			checkpoints = new Point[]{new Point(100, 300), new Point(500, 300)};
			break;
		case 3:
			pfade = new Point[][]{
					new Point[]{new Point(762, 100), new Point(416, 300), new Point(300, 100), new Point(69, 500), new Point(531, 500), new Point(416, 300), new Point(762, 100)},
					new Point[]{new Point(-162, 100), new Point(185, 300), new Point(69, 500), new Point(531, 500), new Point(300, 100), new Point(185, 300), new Point(-162, 100)},
					new Point[]{new Point(300, 900), new Point(300, 500), new Point(531, 500), new Point(300, 100), new Point(69, 500), new Point(300, 500), new Point(300, 900)}};
			checkpoints = new Point[]{new Point(300, 100), new Point(69, 500), new Point(531, 500)};
			break;
		case 4:
			pfade = new Point[][]{
			new Point[]{new Point(-100, 300), new Point(100, 300), new Point(100, 500), new Point(500, 500), new Point(500, 100), new Point(100, 100), new Point(100, 300), new Point(-100, 300)},
			new Point[]{new Point(300, 700), new Point(300, 500), new Point(500, 500), new Point(500, 100), new Point(100, 100), new Point(100, 500), new Point(300, 500), new Point(300, 700)},
			new Point[]{new Point(700, 300), new Point(500, 300), new Point(500, 100), new Point(100, 100), new Point(100, 500), new Point(500, 500), new Point(500, 300), new Point(700, 300)},
			new Point[]{new Point(300, -100), new Point(300, 100), new Point(100, 100), new Point(100, 500), new Point(500, 500), new Point(500, 100), new Point(300, 100), new Point(300, -100)}};
			checkpoints = new Point[]{new Point(100, 100), new Point(100, 500), new Point(500, 500), new Point(500, 100)};
			break;
		}
	}
	
}
