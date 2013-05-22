package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import java.awt.Point;
import java.util.ArrayList;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class SnakeSpielServer extends ServerSpiel {
	
	private ArrayList<Snake> snakes;
	private ArrayList<Snake> toteSnakes;
	private ArrayList<Point> essen;
	private ArrayList<Point> startPunkte;
	private int zähler;
	private int speed;
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new SnakePaketManagerServer(this, spieler);
	}

	@Override
	protected void starten() {
		speed = 5;
		snakes = new ArrayList<Snake>();
		startPunkte = new ArrayList<Point>();
		essen = new ArrayList<Point>();
		
		for(int i=0;i<10;i++) {
			startPunkte.add(new Point(15,10+5*i));
		}
		
		for(int i=0;i<teilnehmer.size();i++) {
			snakes.add(new Snake(this, teilnehmer.get(i), Helfer.zufallsElement(startPunkte, true)));
		}
	}
	
	@Override
	public void tick() {
		zähler++;
		if (zähler >= speed) {
			for(int i=0;i<snakes.size();i++) {
				snakes.get(i).tick();
			}
			zähler = 0;
		}
		if (Helfer.zufallsZahl(50) == 42) {
			int x = Helfer.zufallsZahl(60);
			int y = Helfer.zufallsZahl(60);
			Point p = new Point(x,y);
			if(feldZustandPrüfen(p) == 0) {
				essen.add(p);
				feldUpdaten(p,0xFFFFFF);
			}
		}
		if(snakes.size() == toteSnakes.size() + 1) {
			reset();
		}
	}
	
	public void essenPrüfen(Point p, Snake s) {
		for(int i=0;i<essen.size();i++) {
			if (essen.get(i).x == p.x && essen.get(i).y == p.y) {
				s.wachsen(1);
				essen.remove(i);
				break;
			}
		}
	}
	
	public int feldZustandPrüfen(Point p) { // 1=Essen liegt auf p; 2=p liegt auf tötlichen Koordinaten; 0=p ist leer
		for(int j=0;j<essen.size();j++) {
			if (essen.get(j).x == p.x && essen.get(j).y == p.y) {
				return 1;
			}
		}
		for(int i=0;i<snakes.size();i++) {
			if(snakes.get(i).feldPrüfen(p)) {
				return 2;
			}
		}
		if(p.x >= 60 || p.x < 0 || p.y >= 60 || p.y < 0) {
			return 2;
		}
		return 0;
	}
	
	public void feldUpdaten(Point p, int farbe) {
		packetAnAlle(new PacketFeldSetzen(p.x,p.y,farbe));
	}
	
	public void nachrichtSenden(Spieler spieler, String msg) {
		packetAnSpieler(spieler, new PacketNachrichtSenden(msg));
	}
	
	public Snake sucheSnake(Spieler spieler) {
		int Index = teilnehmer.indexOf(spieler);
		return snakes.get(Index);
	}
	
	public void setSpeed(int speed) {
		if(this.speed < speed) {
			this.speed = speed;
		}
	}
	
	public void toteSnakeEinfügen(Snake s) {
		toteSnakes.add(s);
	}
	
	public void reset() {
		//Score muss noch berechnet werden
		for (int i = 0; i < 60; i++) {
			for (int j = 0; j < 60; j++) {
				Point p = new Point(i,j);
				feldUpdaten(p,-1);
			}
		}
		starten();
	}
}
