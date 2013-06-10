package de.cpg_gilching.informatik11.gamelobby.client;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.cpg_gilching.informatik11.gamelobby.shared.Wachhund;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionAnnehmen;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionVerlassen;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class BildschirmSessionLobby {
	
	private ControllerClient client;
	private int sessionId;
	private SpielBeschreibung beschreibung;
	private int punktelimit;
	
	private Set<String> teilnehmer;
	private Set<String> fertigeSpieler = new HashSet<String>();
	
	private FensterSessionLobby oberfläche;
	
	public BildschirmSessionLobby(ControllerClient client, int sessionId, SpielBeschreibung beschreibung, List<String> eingeladeneSpieler, int punktelimit) {
		this.client = client;
		this.sessionId = sessionId;
		this.beschreibung = beschreibung;
		this.punktelimit = punktelimit;
		this.teilnehmer = new TreeSet<String>(eingeladeneSpieler);
		
		this.oberfläche = new FensterSessionLobby(this);
		
		oberfläche.spielerListeAktualisieren(teilnehmer, fertigeSpieler);
	}
	
	public void lobbyVerlassen() {
		client.getVerbindung().sendPacket(new PacketSessionVerlassen(sessionId));
		new Wachhund(oberfläche.getFenster()).start();
	}
	
	public void lobbyAnnehmen() {
		client.getVerbindung().sendPacket(new PacketSessionAnnehmen(sessionId));
	}
	
	public void jetztBeenden() {
		client.sessionLöschen(this);
		oberfläche.fensterSchliessen();
	}
	
	public void spielerBereitSetzen(String spielerName) {
		fertigeSpieler.add(spielerName);
		oberfläche.spielerListeAktualisieren(teilnehmer, fertigeSpieler);
	}
	
	public void spielerEntfernen(String spielerName) {
		fertigeSpieler.remove(spielerName);
		teilnehmer.remove(spielerName);
		oberfläche.spielerListeAktualisieren(teilnehmer, fertigeSpieler);
	}
	
	public SpielBeschreibung getBeschreibung() {
		return beschreibung;
	}
	
	public int getSessionId() {
		return sessionId;
	}
	
	public int getPunktelimit() {
		return punktelimit;
	}

}
