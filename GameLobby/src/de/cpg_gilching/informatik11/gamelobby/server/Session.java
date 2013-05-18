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

public class Session {
	
	private static int idZähler = 0;
	
	
	private ControllerServer server;
	private int id;
	private SpielBeschreibung beschreibung;
	private Set<Spieler> teilnehmer;
	private Set<Spieler> fertigeSpieler = new HashSet<Spieler>();
	
	public Session(ControllerServer server, SpielBeschreibung beschreibung, List<Spieler> teilnehmerListe) {
		this.server = server;
		this.id = ++idZähler;
		this.beschreibung = beschreibung;
		this.teilnehmer = new HashSet<Spieler>(teilnehmerListe);
		
		System.out.println("Session " + id + " für Spiel " + beschreibung.getBezeichnung() + " mit Teilnehmern " + Helfer.verketten(teilnehmer, ", ") + " angelegt!");
		
		// alle Teilnehmer über die Session informieren
		List<String> teilnehmerNamen = new ArrayList<String>(teilnehmer.size());
		for (Spieler spieler : teilnehmer)
			teilnehmerNamen.add(spieler.getName());
		
		for (Spieler spieler : teilnehmer) {
			spieler.packetSenden(new PacketSessionStarten(id, beschreibung.getSpielId(), teilnehmerNamen));
		}
	}
	
	public void spielerBereit(Spieler spieler) {
		fertigeSpieler.add(spieler);
		
		for (Spieler anderer : teilnehmer) {
			anderer.packetSenden(new PacketSessionSpielerStatus(id, spieler.getName(), true));
		}
		
		// alle sind bereit
		if (fertigeSpieler.containsAll(teilnehmer)) {
			spielStarten();
		}
	}
	
	public void spielerVerlassen(Spieler spieler) {
		if (teilnehmer.remove(spieler)) {
			fertigeSpieler.remove(spieler);
			
			spieler.packetSenden(new PacketSessionVerlassen(id));
			
			if (teilnehmer.size() < beschreibung.minimalspielerGeben()) {
				beenden();
			}
			else {
				for (Spieler anderer : teilnehmer) {
					anderer.packetSenden(new PacketSessionSpielerStatus(id, spieler.getName(), false));
				}
			}
		}
	}
	
	public void spielStarten() {
		System.out.println("Spiel wird gestartet mit Spiel " + beschreibung.getBezeichnung() + " und Teilnehmern " + Helfer.verketten(teilnehmer, ", "));
		beenden();
		server.spielStarten(id, beschreibung, teilnehmer);
	}
	
	private void beenden() {
		System.out.println("Session " + id + " beendet!");
		
		for (Spieler anderer : teilnehmer) {
			anderer.packetSenden(new PacketSessionVerlassen(id));
		}
		server.sessionLöschen(this);
	}
	
	public int getId() {
		return id;
	}
	
}
