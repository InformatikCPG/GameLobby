package de.cpg_gilching.informatik11.gamelobby.client;

import java.util.HashSet;
import java.util.Set;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

class SpielerIngameZustand {
	String name;
	int punkte = 0;
	
	SpielerIngameZustand(String name) {
		this.name = name;
	}
}

public class BildschirmGameLobby {
	
	private ControllerClient client;
	private int spielId;
	private SpielBeschreibung beschreibung;
	private ClientSpiel clientSpiel;
	private int msVergangen = 0;
	
	private Set<SpielerIngameZustand> spielerListe = new HashSet<SpielerIngameZustand>();
	
	private SpielOberfläche spielView = new SpielOberfläche();
	private FensterGameLobby oberfläche;
	
	public BildschirmGameLobby(ControllerClient client, int spielId, SpielBeschreibung beschreibung) {
		this.client = client;
		this.spielId = spielId;
		this.beschreibung = beschreibung;
		
		this.oberfläche = new FensterGameLobby(beschreibung.getBezeichnung(), spielView);
		
		ClientSpiel.konstruktorSpielView = spielView;
		clientSpiel = beschreibung.clientInstanzErstellen();
		ClientSpiel.konstruktorSpielView = null;
		clientSpiel.viewZuweisen(spielView);
		
		if (1000 % beschreibung.tickrateGeben() != 0) {
			System.err.println("WARNUNG: Spiel " + beschreibung.getBezeichnung() + " hat ungünstige Tickrate: " + beschreibung.tickrateGeben());
		}
	}
	
	public void tick(int ms) {
		msVergangen += ms;
		int tickAlleMs = 1000 / beschreibung.tickrateGeben();
		
		while (msVergangen >= tickAlleMs) {
			
			if (spielView.hatCanvas()) {
				spielView.canvasRendern(clientSpiel);
			}
			
			msVergangen -= tickAlleMs;
		}
	}
	
	public void spielerHinzufügen(String spielerName) {
		spielerListe.add(new SpielerIngameZustand(spielerName));
		oberfläche.spielerListeAktualisieren(spielerListe);
	}
	
	public void spielerEntfernen(String spielerName) {
		for (SpielerIngameZustand zustand : spielerListe) {
			if (zustand.name.equals(spielerName)) {
				spielerListe.remove(zustand);
				oberfläche.spielerListeAktualisieren(spielerListe);
				break;
			}
		}
	}
	
	public int getSpielId() {
		return spielId;
	}
	
}
