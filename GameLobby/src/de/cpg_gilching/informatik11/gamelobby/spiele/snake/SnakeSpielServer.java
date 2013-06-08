package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import de.cpg_gilching.informatik11.gamelobby.server.LobbySpieler;
import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielChat.ChatBefehl;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class SnakeSpielServer extends ServerSpiel {
	
	private ArrayList<Snake> snakes;
	private ArrayList<Snake> toteSnakes;
	private ArrayList<Point> essen;
	private ArrayList<Point> startPunkte;
	private int zähler;
	private int sleep = 10;
	private int speed = 3;
	private int essenSpawnrate = 5;
	public int mode = 0; //0 = Snake; 1 = CurveFever
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new SnakePaketManagerServer(this, spieler);
	}

	@Override
	protected void starten() {
		snakes = new ArrayList<Snake>();
		startPunkte = new ArrayList<Point>();
		essen = new ArrayList<Point>();
		toteSnakes = new ArrayList<Snake>();
		
		for(int i=0;i<10;i++) {
			startPunkte.add(new Point(15,10+5*i));
		}
		
		for(int i=0;i<teilnehmer.size();i++) {
			snakes.add(new Snake(this, teilnehmer.get(i), Helfer.zufallsElement(startPunkte, true), mode));
		}
		
		commandsInitialisieren();
	}
	
	@Override
	protected void spielerVerlassen(LobbySpieler spieler) {
		Snake entfernt = sucheSnake(spieler);
		
		for (Point p : entfernt.elementeGeben()) {
			feldUpdaten(p, -1);
		}
		
		// die Snake muss aus der Liste entfernt werden, damit die Indizes wieder übereinstimmen
		snakes.remove(entfernt);
	}
	
	@Override
	public void tick() {
		
		if(snakes.size() - 1 == toteSnakes.size() ) {
			for(int i=0;i<snakes.size();i++){
				if(snakes.get(i).tot == false) {
					toteSnakeEinfügen(snakes.get(i));
					snakes.get(i).tot = true;
				}
			}
		}
		//counter, um Pause zwischen Spielende und Spielstart zu schaffen
		if(snakes.size() == toteSnakes.size()) {
			sleep++;
			if(sleep >= 40) {
				reset();
			}
			return;
		}
		
		zähler++;
		if (zähler >= speed) {
			for(int i=0;i<snakes.size();i++) {
				snakes.get(i).tick();
			}
			zähler = 0;
		}
		if (Helfer.zufallsZahl(100) < essenSpawnrate) {
			int x = Helfer.zufallsZahl(60);
			int y = Helfer.zufallsZahl(60);
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
		scoreboard.punkteVorbereiten(s.spielerGeben(), toteSnakes.size());
		toteSnakes.add(s);
		if(toteSnakes.size() != snakes.size()) {
			chat.nachrichtAnAlleTeilnehmer(s.spielerGeben().getName() + " ist gestorben!");
		}
		else {
			chat.nachrichtAnAlleTeilnehmer(s.spielerGeben().getName() + " hat gewonnen!");			
		}
	}
	
	public void reset() {
		for (int i = 0; i < 60; i++) {
			for (int j = 0; j < 60; j++) {
				Point p = new Point(i,j);
				feldUpdaten(p,-1);
			}
		}
		for(int i=0;i<teilnehmer.size();i++) {
			nachrichtSenden(teilnehmer.get(i),"");
		}
		scoreboard.punkteAnwenden();
		starten();
	}
	
	public void commandsInitialisieren() {
		//speed ändern
		chat.befehlRegistrieren("speed", new ChatBefehl() {
			@Override
			public void ausführen(Spieler sender, String[] argumente) {
				if (argumente.length >= 1) {
					try {
						speed = Integer.parseInt(argumente[0]);
					} catch (NumberFormatException e) {
						chat.nachrichtAnSpieler(sender, "Du musst eine Zahl eingeben!");
					}
				}
			}
		});
		//Spielmodus ändern
		chat.befehlRegistrieren("mode", new ChatBefehl() {
			@Override
			public void ausführen(Spieler sender, String[] argumente) {
				if (argumente.length >= 1) {
					try {
						mode = Integer.parseInt(argumente[0]);
					} catch (NumberFormatException e) {
						chat.nachrichtAnSpieler(sender, "Du musst eine Zahl eingeben!");
					}
				}
			}
		});
		//Essen-Spawnrate ändern
		chat.befehlRegistrieren("essen", new ChatBefehl() {
			@Override
			public void ausführen(Spieler sender, String[] argumente) {
				if (argumente.length >= 1) {
					try {
						essenSpawnrate = Integer.parseInt(argumente[0]);
					} catch (NumberFormatException e) {
						chat.nachrichtAnSpieler(sender, "Du musst eine Zahl eingeben!");
					}
				}
			}
		});
	}
}
