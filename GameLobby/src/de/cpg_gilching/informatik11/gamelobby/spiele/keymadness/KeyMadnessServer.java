package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.awt.Point;
import java.util.ArrayList;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielerAnzahl;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.EntityTracker;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielChat.ChatBefehl;
import de.cpg_gilching.informatik11.gamelobby.spiele.osmos.Vektor;

public class KeyMadnessServer extends ServerSpiel {
	
	KeyMadnessPunkteDaten daten;
	ArrayList<KeyMadnessTarget> targets;
	EntityTracker tracker;
	// Variable um Intervall zum Spawnen festzulegen:
	int targetSpawnActivate;
	// Aktuelles Level:
	int level;
	// Geschwindigkeit mit der sich die Targets bewegen:
	double[] geschwindigkeit = {1.5, 1.9, 2.4, 2.8, 3.5, 4, 5.5, 6.5};
	// Spawnrate mit der die nächsten Targets spawnen(siehe tick()):
	int[] spawnrate = {40, 35, 30, 20, 12, 8, 6, 4};
	// Radius in dem eine Target um einen Checkpoint registriert wird und ein Tastendruck als gültig gewertet wird:
	int[] radius = {10, 10, 12, 12, 14, 16, 18, 20};
	
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new KMPaketM(spieler, this);
	}
	
	@Override
	protected void starten() {
		// daten werden erstellt je nach Teilnehmeranzahl:
		daten = new KeyMadnessPunkteDaten(teilnehmer.size());
		// Targets können in ArrayList eingespeichert werden:
		targets = new ArrayList<KeyMadnessTarget>();
		tracker = new EntityTracker(this);
		// Level wird auf 0 gesetzt:
		level = 0;
		chat.nachrichtAnAlleTeilnehmer("Level 1!");
		
		// den Teilnehmern werden je nach Position in der Liste Farben im Scoreboard zugewiesen:
		switch(teilnehmer.size()) {
		case 4:
		    scoreboard.anzeigefarbeSetzen(teilnehmer.get(3), 0x00FFFF);
		case 3:
			scoreboard.anzeigefarbeSetzen(teilnehmer.get(2), 0x0000FF);
		case 2:
			scoreboard.anzeigefarbeSetzen(teilnehmer.get(1), 0x00FF00);
			scoreboard.anzeigefarbeSetzen(teilnehmer.get(0), 0xFF0000);
		    break;
		}
		
		// Packet um über den Chat Level zu setzen:
		packetAnAlle(new PacketSpielerAnzahl(teilnehmer.size()));
		chat.befehlRegistrieren("level", new ChatBefehl() {
			@Override
			public void ausführen(Spieler sender, String[] argumente) {
				if (argumente.length >= 1) {
					try {
						int i = Integer.parseInt(argumente[0]);
						if(i < 9){
							level = i - 1;
							chat.nachrichtAnAlleTeilnehmer(sender.getName() + " hat Level auf " + i + " gestellt!");
						}
						else{
							chat.nachrichtAnSpieler(sender, "Es gibt nur 8 Level!");
						}
					} catch (NumberFormatException e) {
						chat.nachrichtAnSpieler(sender, "Du musst eine Zahl eingeben!");
					}
				}
			}
		});
	}

	
	
	public void tick(){
		
		// Bedingung zum Spawnen von Targets, damit nicht jeden Tick Targets gespawnt werden:
		if(targetSpawnActivate <= 0){
			// ein zufälliger Wert für einen Pfad wird aus der Länge des Arrays aus Pfaden berechnet:
			int p = Helfer.zufallsZahl(daten.pfade.length);
			// neue Target wird erstellt mit Verweis auf Server und dem zufälligen Pfad p, der aus Punkten besteht:
			KeyMadnessTarget target = new KeyMadnessTarget(this,  daten.pfade[p]);
			targets.add(target);
			tracker.trackTarget(target);
			// Spawn-Variable wird neu gesetzt:
			// Grundintervall (damit Targets nicht überlappern) + Levelabhängiger int geteilt durch Anzahl der Spieler:
			// Mehr Spieler -> kleinere Spawn-Variable -> mehr Targets!
			targetSpawnActivate = Helfer.zufallsZahl(spawnrate[level] / teilnehmer.size()) + 25;
		}
		
		// Entfernen von deaktivierten Targets:
		for(int i = 0; i <= (targets.size() - 1); i++){
			targets.get(i).tick();
			if(targets.get(i).tot){
				tracker.untrackTarget(targets.get(i));
				targets.remove(i);
				i = i -1;
			}
		}
		tracker.tick();
		// Spawn-Variable wird verringert:
		targetSpawnActivate = targetSpawnActivate - 1;
	}
	
	// bei Tastendruck von bestimmtem Spieler:
	public void prüfen(int tastencode, Spieler spieler){
		int spielerIndex = teilnehmer.indexOf(spieler);
		// jeweiliger Checkpoint wird aus den Daten für den Spieler ausgelesen, die Stelle des Teilnehmers wird mit
		//der Stelle des Checkpoints gleichgestellt!
		Point checkpoint = daten.checkpoints[spielerIndex];
		Vektor v = new Vektor();
		// getroffen Variable grundsätzlich auf false:
		boolean getroffen = false;
		
		// Alle Targets werden überprüft:
		for(int i = 0; i <= (targets.size() - 1); i++){
			// 1.Bedingung: Target befindet sich im Radius von Checkpoint:
			if(v.kopiere(targets.get(i).x, targets.get(i).y).sub(checkpoint.x, checkpoint.y).länge() <= radius[level]){
				// 2.Bedingung: Tastencode stimmt überein und Target ist nicht rot:
				if (targets.get(i).tastencode == tastencode && targets.get(i).valid) {
					//Punkt wird dem Spieler auf dem Scoreboard hinzugefügt:
					scoreboard.punktHinzufügen(spieler);
					// Target wird deaktiviert und beim nächsten tick entfernt:
					targets.get(i).tot = true;
					// getroffen wird auf true gesetzt, damit kein Punkt abgezogen wird:
					getroffen = true;
				}
			}
		}
		// Wenn keine Target gefunden wurde für die die Bedingungen erfüllt sind:
		if(!getroffen){
			scoreboard.punkteÄndern(spieler, -1);
		}
		
		// Bei Tastendruck von einem Spieler wird auch überprüft ob eine gewisse Anzahl von Punkten erreicht wurde, 
		// damit das Level erhöht wird (Das Level springt nichtmehr zurück!):
		if(scoreboard.getPunkte(spieler) == 8){
			if(level < 1){
				level = 1;
				chat.nachrichtAnAlleTeilnehmer("Level up! (2)");
			}
		}
		else if(scoreboard.getPunkte(spieler) == 15) {
			if(level < 2){
				level = 2;
				chat.nachrichtAnAlleTeilnehmer("Level up! (3)");
			}
		}
		else if(scoreboard.getPunkte(spieler) == 20) {
			if(level < 3){
				level = 3;
				chat.nachrichtAnAlleTeilnehmer("Level up! (4)");
			}
		}
		else if(scoreboard.getPunkte(spieler) == 30) {
			if(level < 4){
				level = 4;
				chat.nachrichtAnAlleTeilnehmer("Level up! (5)");
			}
		}
		else if(scoreboard.getPunkte(spieler) == 50) {
			if(level < 5){
				level = 5;
				chat.nachrichtAnAlleTeilnehmer("Level up! (6)");
			}
		}
		else if(scoreboard.getPunkte(spieler) == 80) {
			if(level < 6){
				level = 6;
				chat.nachrichtAnAlleTeilnehmer("Level up! (7)");
			}
		}
		else if(scoreboard.getPunkte(spieler) == 95) {
			if(level < 7){
				level = 7;
				chat.nachrichtAnAlleTeilnehmer("Finales Level! (8)");
			}
		}
	}
	
	
}
