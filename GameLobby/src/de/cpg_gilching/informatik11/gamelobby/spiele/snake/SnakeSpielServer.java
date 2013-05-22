package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import java.awt.Point;
import java.util.ArrayList;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class SnakeSpielServer extends ServerSpiel {
	
	private ArrayList<Snake> snakes;
	private ArrayList<Point> essen;
	private ArrayList<Point> startPunkte;
	private int zähler;
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new SnakePaketManagerServer(this, spieler);
	}

	@Override
	protected void starten() {
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
		if (zähler == 5) {
			for(int i=0;i<snakes.size();i++) {
				snakes.get(i).tick();
			}
			zähler = 0;
		}
		if (Helfer.zufallsZahl(10) == 1) {
			int x = Helfer.zufallsZahl(70);
			int y = Helfer.zufallsZahl(70);
			Point p = new Point(x,y);
			if(feldZustandPrüfen(p) == 0) {
				essen.add(p);
				feldUpdaten(p,0xFFFFFF);
			}
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
		if(p.x > 64 || p.x < 5 || p.y > 64 || p.y < 5) {
			return 2;
		}
		return 0;
	}
	
	public void feldUpdaten(Point p, int farbe) {
		packetAnAlle(new PacketFeldSetzen(p.x,p.y,farbe));
	}
	
	public Snake sucheSnake(Spieler spieler) {
		int Index = teilnehmer.indexOf(spieler);
		return snakes.get(Index);
	}
	
}
