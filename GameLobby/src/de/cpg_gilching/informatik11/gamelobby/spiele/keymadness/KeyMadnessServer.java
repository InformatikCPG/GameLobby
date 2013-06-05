package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.awt.Point;
import java.util.ArrayList;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielerAnzahl;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.EntityTracker;
import de.cpg_gilching.informatik11.gamelobby.spiele.osmos.Vektor;

public class KeyMadnessServer extends ServerSpiel {
	
	KeyMadnessPunkteDaten daten;
	ArrayList<KeyMadnessTarget> targets;
	EntityTracker tracker;
	int targetSpawnActivate;
	
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new KMPaketM(spieler, this);
	}
	
	@Override
	protected void starten() {
		daten = new KeyMadnessPunkteDaten(teilnehmer.size());
		targets = new ArrayList<KeyMadnessTarget>();
		tracker = new EntityTracker(this);
		
		packetAnAlle(new PacketSpielerAnzahl(teilnehmer.size()));
	}

	public void tick(){
		if(targetSpawnActivate <= 0){
			int p = Helfer.zufallsZahl(daten.pfade.length);
			KeyMadnessTarget target = new KeyMadnessTarget(this,  daten.pfade[p]);
			targets.add(target);
			tracker.trackTarget(target);
			targetSpawnActivate = Helfer.zufallsZahl(25) + 30;
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
			if(v.kopiere(targets.get(i).x, targets.get(i).y).sub(checkpoint.x, checkpoint.y).länge() <= 10){
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
	}
}
