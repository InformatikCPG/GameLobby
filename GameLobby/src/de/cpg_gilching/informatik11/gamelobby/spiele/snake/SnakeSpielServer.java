package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import java.awt.Point;
import java.util.ArrayList;

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
	private int sleep = -1;
	private int speed = 3;
	private int essenSpawnrate = 5;
	public int mode = 0; //0 = Snake; 1 = CurveFever
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new SnakePaketManagerServer(this, spieler);
	}

	//wird beim Erstellen des Objekts aufgerufen; wie ein Konstruktor
	@Override
	protected void starten() {
		//Initialisierung der Attribute
		snakes = new ArrayList<Snake>();
		startPunkte = new ArrayList<Point>();
		essen = new ArrayList<Point>();
		toteSnakes = new ArrayList<Snake>();
		
		//Setzen aller Startpunkte
		for(int i=0;i<10;i++) {
			startPunkte.add(new Point(15,10+5*i));
		}
		
		//So viele Snake in die Liste "snakes" einfügen, wie Spieler teilnehmen; Auswählen eines zufälligen Startpunkts
		for(int i=0;i<teilnehmer.size();i++) {
			snakes.add(new Snake(this, teilnehmer.get(i), Helfer.zufallsElement(startPunkte, true), mode));
		}
		
		//Initialisierung der Commands
		commandsInitialisieren();
	}
	
	//Wenn ein Spieler das Spiel verlässt: Snake wird von der Oberfläche entfernt; Snake des Spieler aus den Listen "snakes" und "toteSnakes" entfernen
	@Override
	protected void spielerVerlassen(LobbySpieler spieler) {
		Snake entfernt = sucheSnake(spieler);
		
		for (Point p : entfernt.elementeGeben()) {
			feldUpdaten(p, -1);
		}
		
		//Die Snake muss aus der Liste entfernt werden, damit die Indizes wieder übereinstimmen
		snakes.remove(entfernt);
		toteSnakes.remove(entfernt);
	}
	
	@Override
	public void tick() {
		
		//Wenn alle Spieler bis auf einen tot sind, dann wird die Snake vom letzten Spieler zur Liste "toteSnakes" hinzugefügt
		if(snakes.size() - 1 == toteSnakes.size() ) {
			for(int i=0;i<snakes.size();i++){
				if(snakes.get(i).tot == false) {
					toteSnakeEinfügen(snakes.get(i));
					snakes.get(i).tot = true;
				}
			}
		}
		
		//Counter, um Pause zwischen Spielende, Reset und Spielstart zu schaffen
		if (sleep > -1) {
			sleep--;
			if (sleep == 40) {
				packetAnAlle(new PacketReset());
				reset();
			}
			return;
		}
		else if (snakes.size() == toteSnakes.size()) {
			sleep = 80;
			return;
		}
		
		//Speed wird festgelegt
		zähler++;
		if (zähler >= speed) {
			for(int i=0;i<snakes.size();i++) {
				snakes.get(i).tick();
			}
			zähler = 0;
		}
		
		//Essen Spwanrate wird festgelegt und Punkt p für Essen an den Client geschickt
		if (Helfer.zufallsZahl(100) < essenSpawnrate) {
			int x = Helfer.zufallsZahl(60);
			int y = Helfer.zufallsZahl(60);
			Point p = new Point(x,y);
			//Prüfen, ob an der Stelle des Punktes p schon eine Snake oder Essen ist
			if(feldZustandPrüfen(p) == 0) {
				essen.add(p);
				feldUpdaten(p,0xFFFFFF);
			}
		}
		
	}
	
	//Prüfen, ob an dem Punkt p Essen ist; Wenn ja, dann wächst die Snake
	public void essenPrüfen(Point p, Snake s) {
		for(int i=0;i<essen.size();i++) {
			if (essen.get(i).x == p.x && essen.get(i).y == p.y) {
				s.wachsen(1);
				essen.remove(i);
				break;
			}
		}
	}
	
	//Prüfen des Zustands des Punktes p
	public int feldZustandPrüfen(Point p) { // 1=Essen liegt auf p; 2=p liegt auf tötlichen Koordinaten; 0=p ist leer
		//Wenn auf Punkt p Essen ist, dann return 1
		for(int j=0;j<essen.size();j++) {
			if (essen.get(j).x == p.x && essen.get(j).y == p.y) {
				return 1;
			}
		}
		//Wenn auf Punkt p eine Snake ist, dann return 2
		for(int i=0;i<snakes.size();i++) {
			if(snakes.get(i).feldPrüfen(p)) {
				return 2;
			}
		}
		//Wenn Punkt p ausserhalb des Spielfelds liegt, dann return 2
		if(p.x >= 60 || p.x < 0 || p.y >= 60 || p.y < 0) {
			return 2;
		}
		//Wenn auf Punkt p nichts ist, dann return 0
		return 0;
	}
	
	//Senden von einem Packet "PacketFeldSetzen"; Packet wird an alle Clients geschickt
	public void feldUpdaten(Point p, int farbe) {
		packetAnAlle(new PacketFeldSetzen(p.x,p.y,farbe));
	}
	
	//Senden von einem Packet "PacketNachrichtSenden"; Packet wird an den Spieler "spieler" geschickt
	public void nachrichtSenden(Spieler spieler, String msg) {
		packetAnSpieler(spieler, new PacketNachrichtSenden(msg));
	}
	
	//die zu einem Spieler zugehörige Snake suchen und returnen
	public Snake sucheSnake(Spieler spieler) {
		int Index = teilnehmer.indexOf(spieler);
		return snakes.get(Index);
	}
	
	//Eine Snake in die Liste "toteSnakes" einfügen
	public void toteSnakeEinfügen(Snake s) {
		scoreboard.punkteVorbereiten(s.spielerGeben(), toteSnakes.size());
		toteSnakes.add(s);
		//Wenn die hinzuzufügende Snake nicht die letzte ist, dann "Nachricht1" senden, sonst "Nachricht2" senden
		if(toteSnakes.size() != snakes.size()) {
			chat.nachrichtAnAlleTeilnehmer(s.spielerGeben().getName() + " ist gestorben!");
		}
		else {
			chat.nachrichtAnAlleTeilnehmer(s.spielerGeben().getName() + " hat gewonnen!");			
		}
	}
	
	//Reset des kompletten Spiels
	public void reset() {
		for(int i=0;i<teilnehmer.size();i++) {
			nachrichtSenden(teilnehmer.get(i),"");
		}
		scoreboard.punkteAnwenden();
		starten();
	}
	
	//Beschreibt die Commands
	public void commandsInitialisieren() {
		//Speed ändern
		chat.befehlRegistrieren("speed", new ChatBefehl() {
			@Override
			public void ausführen(Spieler sender, String[] argumente) {
				if (argumente.length >= 1) {
					try {
						speed = Integer.parseInt(argumente[0]);
						chat.nachrichtAnAlleTeilnehmer(sender.getName() + " hat den Speed auf " + speed + " gesetzt!");
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
						chat.nachrichtAnSpieler(sender, sender.getName() + " hat den Spielmodus auf " + mode + " gesetzt!");
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
						chat.nachrichtAnSpieler(sender, sender.getName() + " hat die Spawnrate von Essen auf " + essenSpawnrate + " gesetzt!");
					} catch (NumberFormatException e) {
						chat.nachrichtAnSpieler(sender, "Du musst eine Zahl eingeben!");
					}
				}
			}
		});
		//help Command
		chat.befehlRegistrieren("help", new ChatBefehl() {
			@Override
			public void ausführen(Spieler sender, String[] argumente) {
				if (argumente.length >= 0) {
					try {
						//Liste aller Commands + Erklärung
						chat.nachrichtAnSpieler(sender, "!speed -> reguliert die Geschwindigkeit der Snakes");
						chat.nachrichtAnSpieler(sender, "!mode	-> verändert den Spielmodus");
						chat.nachrichtAnSpieler(sender, "!essen -> reguliert die Spawnrate von Essen");
					} catch (NumberFormatException e) {
						chat.nachrichtAnSpieler(sender, "Du musst eine Zahl eingeben!");
					}
				}
			}
		});
	}
}
