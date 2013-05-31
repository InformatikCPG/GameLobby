package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.cpg_gilching.informatik11.gamelobby.server.ControllerServer;
import de.cpg_gilching.informatik11.gamelobby.server.LobbySpieler;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielVerlassen;

public abstract class ServerSpiel {
	
	private ControllerServer server = null;
	private int spielId = -1;
	private int msVergangen = 0;
	protected SpielBeschreibung beschreibung = null;
	protected List<Spieler> teilnehmer = null;
	
	// Speichert alle PaketManager der Spieler
	private Map<Spieler, PaketManager> paketManagerMap = new HashMap<Spieler, PaketManager>();
	
	public final void _init(ControllerServer server, SpielBeschreibung beschreibung, int id, Collection<LobbySpieler> teilnehmer) {
		this.server = server;
		this.beschreibung = beschreibung;
		this.spielId = id;
		this.teilnehmer = new ArrayList<Spieler>(teilnehmer);
		Collections.shuffle(this.teilnehmer);
		
		for (Spieler spieler : this.teilnehmer) {
			paketManagerMap.put(spieler, paketManagerErstellen(spieler));
		}
		
		starten();
	}
	
	public final void _tick(int ms) {
		msVergangen += ms;
		int tickAlleMs = 1000 / beschreibung.tickrateGeben();
		
		if (msVergangen >= tickAlleMs) {
			tick();
			msVergangen -= tickAlleMs;
		}
	}
	
	public final PaketManager getPaketManagerFür(Spieler spieler) {
		return paketManagerMap.get(spieler);
	}
	
	public final void _teilnehmerVerlassen(LobbySpieler spieler) {
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
	
	public final void packetAnSpieler(Spieler spieler, SpielPacket packet) {
		packet.spielId = spielId;
		((LobbySpieler) spieler).packetSenden(packet);
	}
	
	public final void packetAnAlle(SpielPacket packet) {
		for (Spieler spieler : teilnehmer)
			packetAnSpieler(spieler, packet);
	}
	
	protected abstract PaketManager paketManagerErstellen(Spieler spieler);
	
	protected abstract void starten();
	
	protected void spielerVerlassen(LobbySpieler spieler) {
	}
	
	public void tick() {
	}
	
	public final void beenden() {
		System.out.println("Spiel " + beschreibung.getBezeichnung() + " wird beendet!");
		
		for (Spieler anderer : teilnehmer) {
			((LobbySpieler) anderer).packetSenden(new PacketSpielVerlassen(spielId));
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
