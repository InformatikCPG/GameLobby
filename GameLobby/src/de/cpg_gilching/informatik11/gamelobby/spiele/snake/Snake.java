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
	private LinkedList<Integer> warteschlangeRichtung;
	
	Snake(SnakeSpielServer server, Spieler spieler, Point start, int mode) {
		tot = false;
		this.server = server;
		this.spieler = spieler;
		farbe = Helfer.zufallsZahl(0xFFFFFF);
		elemente = new LinkedList<Point>();
		warteschlangeRichtung = new LinkedList<Integer>();
		
		elemente.add(start);
		elemente.add(new Point(start.x-1,start.y));
		elemente.add(new Point(start.x-2,start.y));
		elemente.add(new Point(start.x-3,start.y));
		for(int i=0;i<4;i++) {
			server.feldUpdaten(elemente.get(i), farbe);
		}
		
		server.getScoreboard().anzeigefarbeSetzen(spieler, farbe);
	}
	
	public void tick() {
		//wenn Schlange tot, dann nichts mehr tun
		if(tot) {
			return;
		}
		
		//Richtung verändern
		if(!warteschlangeRichtung.isEmpty()) {
			setRichtung(warteschlangeRichtung.removeFirst());
		}
		
		//auf Spielmodus prüfen; wenn mode = 0, dann elemente hinten entfernen
		if(server.mode == 0) {
			Point gelöscht = elemente.removeLast();
			server.feldUpdaten(gelöscht, -1);
		}
		
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
			server.nachrichtSenden(spieler, "Du bist gestorben!");
			server.toteSnakeEinfügen(this);
			return;
		}
		
		elemente.addFirst(vorneNeu);
		
		server.feldUpdaten(elemente.getFirst(), farbe);
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
	
	public Spieler spielerGeben() {
		return spieler;
	}
	
	public LinkedList<Point> elementeGeben() {
		return elemente;
	}
	
	public void richtungSpeichern(int richtungNeu) {
		warteschlangeRichtung.addLast(richtungNeu);
	}
}
