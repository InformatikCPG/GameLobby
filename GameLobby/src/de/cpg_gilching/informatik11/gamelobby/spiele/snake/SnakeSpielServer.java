package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import java.awt.Point;
import java.util.ArrayList;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class SnakeSpielServer extends ServerSpiel {
	
	private ArrayList<Snake> snakes;
	private ArrayList<Point> startPunkte;
	private int zähler;
	private ArrayList<Point> essen;
	
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
		if (Helfer.zufallsZahl(100) == 42) {
			int x = Helfer.zufallsZahl(70);
			int y = Helfer.zufallsZahl(70);
			Point p = new Point(x,y);
			essen.add(p);
			feldUpdaten(p,0xFFFFFF);
		}
	}
	
	public void feldPrüfen(Point p, Snake s) {
		for (int i=0;i<essen.size();i++) {
			if (essen.get(i).x == p.x && essen.get(i).y == p.y) {
				s.wachsen(1);
				essen.remove(i);
				break;
			}
		}
	}
	
	public void feldUpdaten(Point p, int farbe) {
		packetAnAlle(new PacketFeldSetzen(p.x,p.y,farbe));
	}
	
	public Snake sucheSnake(Spieler spieler) {
		int Index = teilnehmer.indexOf(spieler);
		return snakes.get(Index);
	}
	
	
}
