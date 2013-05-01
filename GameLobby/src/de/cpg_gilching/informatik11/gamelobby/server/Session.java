package de.cpg_gilching.informatik11.gamelobby.server;

import java.util.ArrayList;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionStarten;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class Session {
	
	private static int idZähler = 0;
	
	private int id;
	private SpielBeschreibung beschreibung;
	private List<Spieler> teilnehmer;
	
	public Session(SpielBeschreibung beschreibung, List<Spieler> teilnehmer) {
		this.id = ++idZähler;
		this.beschreibung = beschreibung;
		this.teilnehmer = teilnehmer;
		
		// alle Teilnehmer über die Session informieren
		List<String> teilnehmerNamen = new ArrayList<String>(teilnehmer.size());
		for (Spieler spieler : teilnehmer)
			teilnehmerNamen.add(spieler.getName());
		
		for (Spieler spieler : teilnehmer) {
			spieler.packetSenden(new PacketSessionStarten(id, beschreibung.getSpielId(), teilnehmerNamen));
		}
	}
	
	public int getId() {
		return id;
	}
	
}
