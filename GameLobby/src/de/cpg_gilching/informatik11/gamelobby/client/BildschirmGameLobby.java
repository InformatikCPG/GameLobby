package de.cpg_gilching.informatik11.gamelobby.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.Wachhund;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielVerlassen;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

class SpielerIngameZustand implements Comparable<SpielerIngameZustand> {
	final String name;
	volatile int punkte = 0;
	volatile int tempPunkte = Integer.MIN_VALUE;
	volatile int farbe = -1;
	
	SpielerIngameZustand(String name) {
		this.name = name;
	}
	
	@Override
	public int compareTo(SpielerIngameZustand anderer) {
		int tempA = anderer.tempPunkte;
		if (tempA == Integer.MIN_VALUE)
			tempA = 0;
		int tempS = this.tempPunkte;
		if (tempS == Integer.MIN_VALUE)
			tempS = 0;
		
		return (anderer.punkte + tempA) - (this.punkte + tempS); // absteigend sortieren
	}
}

public class BildschirmGameLobby {
	
	private ControllerClient client;
	private int spielId;
	private SpielBeschreibung beschreibung;
	private ClientSpiel clientSpiel;
	private int msVergangen = 0;
	
	// Die Spieler-Liste wird manuell nach Punktestand sortiert
	private List<SpielerIngameZustand> spielerListe = new ArrayList<SpielerIngameZustand>();
	
	private SpielOberfläche spielView = new SpielOberfläche(this);
	private FensterGameLobby oberfläche;
	
	public BildschirmGameLobby(ControllerClient client, int spielId, SpielBeschreibung beschreibung) {
		this.client = client;
		this.spielId = spielId;
		this.beschreibung = beschreibung;
		
		this.oberfläche = new FensterGameLobby(this, beschreibung.getBezeichnung(), spielView);
		
		clientSpiel = beschreibung.clientInstanzErstellen();
		clientSpiel._init(spielView);
	}
	
	public void tick(int ms) {
		msVergangen += ms;
		int tickAlleMs = 1000 / beschreibung.tickrateGeben();
		
		if (msVergangen >= tickAlleMs) {
			
			if (spielView.hatCanvas()) {
				spielView.canvasRendern(clientSpiel);
			}
			
			msVergangen -= tickAlleMs;
		}
	}
	
	public void jetztBeenden() {
		client.spielLöschen(this);
		oberfläche.fensterSchliessen();
	}
	
	public void spielerHinzufügen(String spielerName) {
		spielerListe.add(new SpielerIngameZustand(spielerName));
		Collections.sort(spielerListe);

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
	
	public void spielerPunkteSetzen(String spielerName, int neueFarbe, int neuePunkte, int neueTempPunkte) {
		for (SpielerIngameZustand zustand : spielerListe) {
			if (zustand.name.equals(spielerName)) {
				zustand.punkte = neuePunkte;
				zustand.tempPunkte = neueTempPunkte;
				zustand.farbe = neueFarbe;
				Collections.sort(spielerListe);

				oberfläche.spielerListeAktualisieren(spielerListe);
				break;
			}
		}
	}

	public void chatNachrichtAnzeigen(String nachricht) {
		oberfläche.chatNachrichtAnzeigen(nachricht);
	}
	
	public void chatNachrichtSenden(String nachricht) {
		client.chatNachrichtSenden(spielId, nachricht);
	}
	
	public void spielAbbrechen() {
		client.getVerbindung().sendPacket(new PacketSpielVerlassen(spielId));
		new Wachhund(oberfläche.getFenster()).start();
	}
	
	public void packetSenden(SpielPacket packet) {
		packet.spielId = spielId;
		client.getVerbindung().sendPacket(packet);
	}
	
	public void packetVerarbeiten(SpielPacket packet) {
		PaketManager manager = clientSpiel.getPaketManager();
		
		if (manager == null) {
			System.err.println("Spiel-Paket wurde von Spiel " + beschreibung.getBezeichnung() + " nicht entgegengenommen: kein PaketManager aktiviert!");
		}
		else {
			try {
				Method m = manager.getClass().getDeclaredMethod("verarbeiten", packet.getClass());
				m.invoke(manager, packet);
			} catch (NoSuchMethodException e) {
				System.err.println("Spiel-Paket wurde von " + beschreibung.getBezeichnung() + " nicht entgegengenommen: " + packet.getClass().getSimpleName());
			} catch (InvocationTargetException e) {
				System.err.println("Exception beim Verarbeiten des Spiel-Pakets von " + beschreibung.getBezeichnung() + ": " + packet.getClass().getSimpleName());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean istEigenerSpieler(String spielername) {
		return client.getUsername().equals(spielername);
	}

	public int getSpielId() {
		return spielId;
	}
	
	public ControllerClient getClient() {
		return client;
	}
	
	public ClientSpiel getSpiel() {
		return clientSpiel;
	}
	
}
