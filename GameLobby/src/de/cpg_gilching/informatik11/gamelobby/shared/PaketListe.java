package de.cpg_gilching.informatik11.gamelobby.shared;

import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketChatNachricht;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketHallo;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketKeepAlive;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketServerSpielAnmelden;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionAnnehmen;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionSpielerStatus;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionStarten;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionVerlassen;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSpielerListe;

public class PaketListe {
	
	public static void normalePaketeAnmelden(AdapterPaketLexikon lexikon) {
		lexikon.anmelden(PacketKeepAlive.class);
		lexikon.anmelden(PacketHallo.class);
		lexikon.anmelden(PacketDisconnect.class);
		lexikon.anmelden(PacketChatNachricht.class);
		lexikon.anmelden(PacketSpielerListe.class);
		lexikon.anmelden(PacketServerSpielAnmelden.class);
		lexikon.anmelden(PacketSessionStarten.class);
		lexikon.anmelden(PacketSessionAnnehmen.class);
		lexikon.anmelden(PacketSessionVerlassen.class);
		lexikon.anmelden(PacketSessionSpielerStatus.class);
	}
}
