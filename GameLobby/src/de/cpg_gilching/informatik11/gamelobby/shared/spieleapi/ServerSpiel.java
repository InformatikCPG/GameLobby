package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.cpg_gilching.informatik11.gamelobby.server.ControllerServer;
import de.cpg_gilching.informatik11.gamelobby.server.LobbySpieler;
import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSound;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielTeilnehmer;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielVerlassen;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielChat.ChatBefehl;

/**
 * Dies ist die Basis-Klasse für die Serverseite eines Spiels.<br>
 * Sie vererbt wichtige Attribute und Methoden zur Steuerung des Spiels under Kommunikation mit den Clients.
 * <p>
 * Die wichtigsten Attribute und Methoden:
 * </p>
 * <ul>
 * <li>{@link #teilnehmer}: Eine {@link List}e von allen teilnehmenden Spielern</li>
 * <li>{@link #scoreboard}: Eine API zur Verwaltung des Punktestandes von Spielern</li>
 * <li>{@link #chat}: Eine API zum Senden von Chatnachrichten und Registrieren von Chat-Befehlen ("!befehl")</li>
 * <li>{@link #packetAnSpieler(Spieler, SpielPacket)}: Sendet ein Paket an einen einzelnen Spieler</li>
 * <li>{@link #packetAnAlle(SpielPacket)}: Sendet ein Paket an alle Teilnehmer, z.B. um den Spielzustand an alle zu übermitteln.</li>
 * </ul>
 * <p>
 * Folgende Methoden können/müssen überschrieben werden:
 * </p>
 * <ul>
 * <li>{@link #starten()}: Wird beim Start des Spiels aufgerufen und soll den Startzustand des Spiels herstellen. Diese Methode ersetzt in der Regel den Konstruktor.</li>
 * <li>{@link #paketManagerErstellen(Spieler)}: Wird vor Start des Spiels für jeden Teilnehmer aufgerufen und erzeugt einen {@link PaketManager} für diesen, der Pakete empfängt.</li>
 * <li>{@link #spielerVerlassen(LobbySpieler)}:
 * </ul>
 * 
 * @see Scoreboard
 * @see SpielChat
 * @see SpielPacket
 * @see Spieler
 */
public abstract class ServerSpiel {
	
	private ControllerServer server = null;
	private int spielId = -1;
	private int punktelimit = 10;

	private int msVergangen = 0;
	private boolean beendenSperre = false;
	private boolean beendet = false;

	protected SpielBeschreibung beschreibung = null;
	protected List<Spieler> teilnehmer = null;
	protected Scoreboard scoreboard;
	protected SpielChat chat;
	
	// Speichert alle PaketManager der Spieler
	private Map<Spieler, PaketManager> paketManagerMap = new HashMap<Spieler, PaketManager>();
	
	public final void _init(ControllerServer server, SpielBeschreibung beschreibung, int id, Collection<LobbySpieler> teilnehmer, int punktelimit) {
		this.server = server;
		this.beschreibung = beschreibung;
		this.spielId = id;
		this.punktelimit = punktelimit;
		this.teilnehmer = new ArrayList<Spieler>(teilnehmer);
		Collections.shuffle(this.teilnehmer);
		this.scoreboard = new Scoreboard(this);
		this.chat = new SpielChat(this);
		
		for (Spieler spieler : this.teilnehmer) {
			paketManagerMap.put(spieler, paketManagerErstellen(spieler));
		}
		
		chat.befehlRegistrieren("stopgame", new ChatBefehl() {
			@Override
			public void ausführen(Spieler sender, String[] argumente) {
				beenden(argumente.length > 0 ? Helfer.verketten(Arrays.asList(argumente), " ").toString() : null);
			}
		});

		starten();
	}
	
	public final void _tick(int ms) {
		msVergangen += ms;
		int tickAlleMs = 1000 / beschreibung.tickrateGeben();
		
		if (msVergangen >= tickAlleMs) {
			beendenSperre = true;
			tick();
			beendenSperre = false;
			msVergangen -= tickAlleMs;
		}
	}
	
	public final PaketManager getPaketManagerFür(Spieler spieler) {
		return paketManagerMap.get(spieler);
	}
	
	public final void _teilnehmerVerlassen(LobbySpieler spieler) {
		if (!teilnehmer.contains(spieler))
			return;

		// TODO spieler bestrafen beim verlassen
		
		System.out.println("Spieler " + spieler.getName() + " hat das Spiel " + beschreibung.getBezeichnung() + " verlassen!");
		
		if (teilnehmer.size() - 1 < beschreibung.minimalspielerGeben()) {
			// dem verlassenen Spieler das Packet ohne angegebenen Grund schicken
			teilnehmer.remove(spieler);
			spieler.packetSenden(new PacketSpielVerlassen(spielId, null));
			
			// den Rest über das Verlassen informieren
			beenden("Ein Gegner hat das Spiel verlassen!");
		}
		else {
			spielerVerlassen(spieler);
			teilnehmer.remove(spieler);
			spieler.packetSenden(new PacketSpielVerlassen(spielId, null));
			
			Packet verlassenPacket = new PacketSpielTeilnehmer(spielId, spieler.getName(), PacketSpielTeilnehmer.VERLASSEN);
			for (Spieler anderer : teilnehmer) {
				((LobbySpieler) anderer).packetSenden(verlassenPacket);
			}
			
			chat.nachrichtAnAlleTeilnehmer(spieler.getName() + " hat das Spiel verlassen!");
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
	
	public final void soundAnAlle(String name) {
		PacketSound packet = new PacketSound(name);
		
		for (Spieler spieler : teilnehmer)
			((LobbySpieler) spieler).packetSenden(packet);
	}
	
	public final void soundAnSpieler(Spieler spieler, String name) {
		((LobbySpieler) spieler).packetSenden(new PacketSound(name));
	}

	protected abstract PaketManager paketManagerErstellen(Spieler spieler);
	
	protected abstract void starten();
	
	protected void spielerVerlassen(LobbySpieler spieler) {
	}
	
	public void tick() {
	}
	
	public final void beenden(String grund) {
		System.out.println("Spiel " + beschreibung.getBezeichnung() + " wird beendet!");
		
		for (Spieler anderer : teilnehmer) {
			((LobbySpieler) anderer).packetSenden(new PacketSpielVerlassen(spielId, grund));
		}
		
		beendet = true;
		if (!beendenSperre)
			server.spielLöschen(this);
	}
	
	final void spielEndeSounds(Spieler gewinner) {
		for (Spieler spieler : teilnehmer)
			if (spieler == gewinner)
				soundAnSpieler(spieler, "genericWin");
			else
				soundAnSpieler(spieler, "genericFail");
	}
	
	public final int getSpielId() {
		return spielId;
	}
	
	public final List<Spieler> getTeilnehmer() {
		return Collections.unmodifiableList(teilnehmer);
	}
	
	public final SpielChat getChat() {
		return chat;
	}
	
	public final Scoreboard getScoreboard() {
		return scoreboard;
	}
	
	public final boolean istBeendet() {
		return beendet;
	}

	public final int getPunktelimit() {
		return punktelimit;
	}

}
