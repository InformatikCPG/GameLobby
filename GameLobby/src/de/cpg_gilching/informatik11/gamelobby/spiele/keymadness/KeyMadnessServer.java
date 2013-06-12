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
	int targetSpawnActivate;
	int level;
	double[] geschwindigkeit = {1.5, 1.9, 2.4, 2.8, 3.5, 4, 5.5, 6.5};
	int[] spawnrate = {40, 35, 30, 20, 12, 8, 6, 4};
	int[] radius = {8, 8, 9, 10, 12, 14, 17, 18};
	
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new KMPaketM(spieler, this);
	}
	
	@Override
	protected void starten() {
		daten = new KeyMadnessPunkteDaten(teilnehmer.size());
		targets = new ArrayList<KeyMadnessTarget>();
		tracker = new EntityTracker(this);
		level = 0;
		chat.nachrichtAnAlleTeilnehmer("Level 1!");
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
		if(targetSpawnActivate <= 0){
			int p = Helfer.zufallsZahl(daten.pfade.length);
			KeyMadnessTarget target = new KeyMadnessTarget(this,  daten.pfade[p]);
			targets.add(target);
			tracker.trackTarget(target);
			targetSpawnActivate = Helfer.zufallsZahl(spawnrate[level] / teilnehmer.size()) + 25;
		}
		
		for(int i = 0; i <= (targets.size() - 1); i++){
			targets.get(i).tick();
			if(targets.get(i).tot){
				tracker.untrackTarget(targets.get(i));
				targets.remove(i);
				i = i -1;
			}
		}
		tracker.tick();
		targetSpawnActivate = targetSpawnActivate - 1;
	}
	
	public void prüfen(int tastencode, Spieler spieler){
		int spielerIndex = teilnehmer.indexOf(spieler);
		Point checkpoint = daten.checkpoints[spielerIndex];
		Vektor v = new Vektor();
		boolean getroffen = false;
		for(int i = 0; i <= (targets.size() - 1); i++){
			if(v.kopiere(targets.get(i).x, targets.get(i).y).sub(checkpoint.x, checkpoint.y).länge() <= radius[level]){
				if (targets.get(i).tastencode == tastencode && targets.get(i).valid) {
					scoreboard.punktHinzufügen(spieler);
					targets.get(i).tot = true;
					getroffen = true;
				}
			}
		}
		if(!getroffen){
			scoreboard.punkteÄndern(spieler, -1);
		}
		
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
