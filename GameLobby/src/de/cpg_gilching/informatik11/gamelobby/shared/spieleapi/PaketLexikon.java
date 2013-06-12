package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

/**
 * Interface, um die Netzwerk-Pakete eines Spiels anmelden zu können.<br>
 * Alle Pakete, die ein Spiel verwenden will, müssen zuerst beim entsprechenden Lexikon angemeldet werden.
 */
public interface PaketLexikon {
	
	public void anmelden(Class<? extends Packet> klasse);
	
}
