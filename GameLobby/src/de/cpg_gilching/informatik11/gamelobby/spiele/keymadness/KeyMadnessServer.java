package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.util.ArrayList;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class KeyMadnessServer extends ServerSpiel {
	
	KeyMadnessPunkteDaten daten;
	ArrayList<KeyMadnessTarget> targets;
	
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new KMPaketM(spieler, this);
	}
	
	@Override
	protected void starten() {
		daten = new KeyMadnessPunkteDaten(teilnehmer.size());
	}

	public void tick(){
		if(Helfer.zufallsZahl(30) == 0){
			targets.add(new KeyMadnessTarget(this));
		}
		
		for(int i = 0; i <= (targets.size() - 1); i++){
			targets.get(i).tick();
			if(targets.get(i).tot){
				targets.remove(i);
				i = i -1;
			}
		}
		
	}
	
	
	
}
