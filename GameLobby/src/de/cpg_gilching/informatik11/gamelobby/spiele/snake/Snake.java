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
	public boolean tot;
	
	Snake(SnakeSpielServer server, Spieler spieler, Point start) {
		tot = false;
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
		if(tot) {
			return;
		}
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
		
		if(server.feldZustandPrüfen(vorneNeu) == 2) {
			tot = true;
			server.nachrichtSenden(spieler, "TOOOOOOOOOOOOOOOOOT!");
			server.toteSnakeEinfügen(this);
			return;
		}
		
		elemente.addFirst(vorneNeu);
		
		server.feldUpdaten(elemente.getFirst(), farbe);
		server.feldUpdaten(gelöscht, -1);
		server.essenPrüfen(vorneNeu, this);
	}
	
	public void setRichtung(int richtungNeu) {
		int differenz = richtung - richtungNeu;
		if(differenz != 2 && differenz != -2) {
			richtung = richtungNeu;
		}
	}
	
	public void wachsen(int anzahl) {
		for (int i=0;i<anzahl;i++) {
			elemente.addLast(elemente.getLast());
		}
	}
	
	public boolean feldPrüfen(Point p) {
		for(int i=0;i<elemente.size();i++) {
			if(elemente.get(i).x == p.x && elemente.get(i).y == p.y) {
				return true;
			}
		}
		return false;
	}
	
	public void speedAnpassen() {
		int länge = elemente.size();
		switch (länge) {
		case 5:
			server.setSpeed(4);
			break;
		case 10:
			server.setSpeed(3);
			break;
		case 20:
			server.setSpeed(2);
			break;
		case 30:
			server.setSpeed(1);
			break;
		}
	}
	
	public Spieler spielerGeben() {
		return spieler;
	}
}
