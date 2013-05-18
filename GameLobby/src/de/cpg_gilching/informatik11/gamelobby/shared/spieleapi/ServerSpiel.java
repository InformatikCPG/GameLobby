package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.server.ControllerServer;
import de.cpg_gilching.informatik11.gamelobby.server.Spieler;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionVerlassen;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielVerlassen;

public abstract class ServerSpiel extends Spiel {
	
	private ControllerServer server;
	private int spielId;
	protected SpielBeschreibung beschreibung;
	protected List<Spieler> teilnehmer = null;
	
	public final void _init(ControllerServer server, SpielBeschreibung beschreibung, int id, Collection<Spieler> teilnehmer) {
		this.server = server;
		this.beschreibung = beschreibung;
		this.spielId = id;
		this.teilnehmer = new ArrayList<Spieler>(teilnehmer);
		Collections.shuffle(this.teilnehmer);
		
		starten();
	}
	
	public final void _teilnehmerVerlassen(Spieler spieler) {
		// TODO spieler bestrafen beim verlassen
		
		System.out.println("Spieler " + spieler.getName() + " hat das Spiel " + beschreibung.getBezeichnung() + " verlassen!");
		
		if (teilnehmer.size() - 1 < beschreibung.minimalspielerGeben()) {
			beenden();
		}
		else {
			spielerVerlassen(spieler);
			teilnehmer.remove(spieler);
			spieler.packetSenden(new PacketSpielVerlassen(spielId));
		}
	}
	
	public final void packetAnAlle(SpielPacket packet) {
		// TOOD spielid aufm Server richtig zuweisen: direkte Packets an die spieler vom implementierenden spiel?!
		packet.spielId = spielId;
		for (Spieler spieler : teilnehmer)
			spieler.packetSenden(packet);
	}
	
	protected abstract void starten();
	
	protected void spielerVerlassen(Spieler spieler) {
	}
	
	public void tick() {
	}
	
	private final void beenden() {
		System.out.println("Spiel " + beschreibung.getBezeichnung() + " wird beendet!");
		
		for (Spieler anderer : teilnehmer) {
			anderer.packetSenden(new PacketSpielVerlassen(spielId));
		}
		server.spielLöschen(this);
	}
	
	public final int getSpielId() {
		return spielId;
	}
	
	public final List<Spieler> getTeilnehmer() {
		return Collections.unmodifiableList(teilnehmer);
	}
	
}
