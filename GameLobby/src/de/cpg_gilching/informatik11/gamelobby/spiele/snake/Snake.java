package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import java.awt.Point;
import java.util.LinkedList;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class Snake {
	private SnakeSpielServer server;
	private LinkedList<Point> elemente;
	private Spieler spieler;
	private int farbe;
	private int richtung = 0; //0=rechts, 1=unten, 2=links, 3=oben
	
	Snake(SnakeSpielServer server, Spieler spieler, Point start) {
		this.server = server;
		this.spieler = spieler;
		farbe = Helfer.zufallsZahl(0xFFFFFF);
		elemente = new LinkedList<Point>();
		
		elemente.add(start);
		elemente.add(new Point(start.x-1,start.y));
		elemente.add(new Point(start.x-2,start.y));
		elemente.add(new Point(start.x-3,start.y));
		for(int i=0;i<4;i++) {
			server.feldUpdaten(elemente.get(i), farbe);
		}
	}
	
	public void tick() {
		Point gelöscht = elemente.removeLast();
		
		Point vorneNeu = null;
		Point vorne = elemente.getFirst();
		switch (richtung) {
		case 0:
			vorneNeu = new Point(vorne.x+1, vorne.y);
			break;
		case 1:
			vorneNeu = new Point(vorne.x, vorne.y+1);
			break;
		case 2:
			vorneNeu = new Point(vorne.x-1, vorne.y);
			break;
		case 3:
			vorneNeu = new Point(vorne.x, vorne.y-1);
			break;
		}
		elemente.addFirst(vorneNeu);
		
		server.feldUpdaten(elemente.getFirst(), farbe);
		server.feldUpdaten(gelöscht, -1);
		server.feldPrüfen(vorneNeu, this);
	}
	
	public void setRichtung(int richtungNeu) {
		richtung = richtungNeu;
	}
	
	public void wachsen(int anzahl) {
		for (int i=0;i<anzahl;i++) {
			elemente.addLast(elemente.getLast());
		}
	}
}
