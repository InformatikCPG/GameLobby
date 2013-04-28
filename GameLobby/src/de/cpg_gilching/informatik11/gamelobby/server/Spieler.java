package de.cpg_gilching.informatik11.gamelobby.server;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Connection;

public class Spieler {
	
	private Connection verbindung;
	private String name;
	private ControllerServer server;
	
	public Spieler(Connection verbindung, String name, ControllerServer server) {
		this.verbindung = verbindung;
		this.name = name;
		this.server = server;
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
	
}
