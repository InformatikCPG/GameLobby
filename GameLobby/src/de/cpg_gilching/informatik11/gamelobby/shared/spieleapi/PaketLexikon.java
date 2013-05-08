package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;


public interface PaketLexikon {
	
	public void anmelden(Class<? extends Packet> klasse);
	
}
