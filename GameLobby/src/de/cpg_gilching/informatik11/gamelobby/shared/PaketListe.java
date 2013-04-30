package de.cpg_gilching.informatik11.gamelobby.shared;

import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketHallo;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielerListe;

public class PaketListe {
	
	public static void normalePaketeAnmelden(AdapterPaketLexikon lexikon) {
		lexikon.anmelden(PacketHallo.class);
		lexikon.anmelden(PacketDisconnect.class);
		lexikon.anmelden(PacketChatNachricht.class);
		lexikon.anmelden(PacketSpielerListe.class);
	}
}
