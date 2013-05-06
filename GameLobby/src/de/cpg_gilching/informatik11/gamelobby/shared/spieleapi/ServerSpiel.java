package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.util.Set;

import de.cpg_gilching.informatik11.gamelobby.server.Spieler;

public abstract class ServerSpiel {
	
	protected Set<Spieler> teilnehmer = null;
	
	public final void _init(Set<Spieler> teilnehmer) {
		this.teilnehmer = teilnehmer;
		starten();
	}
	
	protected abstract void starten();
	
	public void tick() {
	}
	
}
