package de.cpg_gilching.informatik11.gamelobby.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionSpielerStatus;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionStarten;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionVerlassen;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

/**
 * Die Server-Repräsentation einer laufenden Session.
 */
public class Session {
	
	/**
	 * Ein statischer Counter, damit jede Session ihre eigene ID bekommt.
	 */
	private static int idZähler = 0;
	
	
	private ControllerServer server;
	
	// die ID der Session
	private int id;
	
	// das vom Host eingestellte Punktelimit
	private int punktelimit;
	
	// die API-Beschreibung des Spiels, das gestartet werden soll
	private SpielBeschreibung beschreibung;
	
	// Spieler und wer schon fertig ist
	private Set<LobbySpieler> teilnehmer;
	private Set<LobbySpieler> fertigeSpieler = new HashSet<LobbySpieler>();
	
	public Session(ControllerServer server, SpielBeschreibung beschreibung, List<LobbySpieler> teilnehmerListe, int punktelimit) {
		this.server = server;
		this.id = ++idZähler;
		this.punktelimit = punktelimit;
		this.beschreibung = beschreibung;
		this.teilnehmer = new HashSet<LobbySpieler>(teilnehmerListe);
		
		System.out.format("Session %d für Spiel %s mit Teilnehmern %s und Punktelimit %d angelegt!%n", id, beschreibung.getBezeichnung(), Helfer.verketten(teilnehmer, ", "), punktelimit);
		
		// alle Teilnehmer über die Session informieren
		// dazu zuerst die zu sendende Liste mit Namen generieren
		List<String> teilnehmerNamen = new ArrayList<String>(teilnehmer.size());
		for (LobbySpieler spieler : teilnehmer)
			teilnehmerNamen.add(spieler.getName());
		
		// Packets an alle versenden
		for (LobbySpieler spieler : teilnehmer) {
			spieler.packetSenden(new PacketSessionStarten(id, beschreibung.getSpielId(), teilnehmerNamen, punktelimit));
		}
	}
	
	// Methode, die aufgerufen wird, wenn ein Spieler auf "Annehmen" geklickt hat
	public void spielerBereit(LobbySpieler spieler) {
		// Spieler zur Menge hinzufügen
		fertigeSpieler.add(spieler);
		
		// alle Spieler darüber informieren, sodass der "Bereit"-Zustand angezeigt wird
		for (LobbySpieler anderer : teilnehmer) {
			anderer.packetSenden(new PacketSessionSpielerStatus(id, spieler.getName(), true));
		}
		
		// sind alle bereit? --> Spiel starten
		if (fertigeSpieler.containsAll(teilnehmer)) {
			spielStarten();
		}
	}
	
	// Methode, die aufgerufen wird, wenn ein Spieler auf "Lobby verlassen" geklickt hat 
	public void spielerVerlassen(LobbySpieler spieler) {
		// aus Teilnehmer- und Fertige-Spieler-Menge löschen
		if (teilnehmer.remove(spieler)) {
			fertigeSpieler.remove(spieler);
			
			spieler.packetSenden(new PacketSessionVerlassen(id));
			
			// Prüfen, ob Anforderungen des Spiels noch gegeben sind
			if (teilnehmer.size() < beschreibung.minimalspielerGeben()) {
				// wenn nicht, Session abbrechen
				beenden();
			}
			else {
				// wenn schon, alle über das Verlassen des Spielers informieren
				for (LobbySpieler anderer : teilnehmer) {
					anderer.packetSenden(new PacketSessionSpielerStatus(id, spieler.getName(), false));
				}
				
				if (fertigeSpieler.containsAll(teilnehmer)) {
					// alle sind bereit (der letzte nicht-bereite Spieler hat die Session verlassen)
					spielStarten();
				}
			}
		}
	}
	
	// das Spiel soll gestartet werden
	public void spielStarten() {
		System.out.println("Spiel wird gestartet mit Spiel " + beschreibung.getBezeichnung() + " und Teilnehmern " + Helfer.verketten(teilnehmer, ", "));
		
		// diese Session beenden
		beenden();
		
		// den Starten-Aufruf an den Server weitergeben
		server.spielStarten(id, beschreibung, teilnehmer, punktelimit);
	}
	
	// die Session wird beendet
	public void beenden() {
		System.out.println("Session " + id + " beendet!");
		
		// alle Spieler informieren
		for (LobbySpieler anderer : teilnehmer) {
			anderer.packetSenden(new PacketSessionVerlassen(id));
		}
		
		// Session vom Server abmelden
		server.sessionLöschen(this);
	}
	
	public int getId() {
		return id;
	}
	
}
