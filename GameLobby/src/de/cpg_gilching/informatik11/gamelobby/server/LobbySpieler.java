package de.cpg_gilching.informatik11.gamelobby.server;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Connection;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class LobbySpieler implements Spieler {
	
	private Connection verbindung;
	private String name;
	private ControllerServer server;
	
	public LobbySpieler(Connection verbindung, String name, ControllerServer server) {
		this.verbindung = verbindung;
		this.name = name;
		this.server = server;
	}
	
	public void packetSenden(Packet p) {
		verbindung.sendPacket(p);
	}
	
	public Connection getVerbindung() {
		return verbindung;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public ControllerServer getServer() {
		return server;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
