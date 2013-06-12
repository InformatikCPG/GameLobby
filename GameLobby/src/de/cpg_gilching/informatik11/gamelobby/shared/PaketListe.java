package de.cpg_gilching.informatik11.gamelobby.shared;

import de.cpg_gilching.informatik11.gamelobby.shared.packets.*;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielMaus;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielTaste;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielerAnzahl;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;

/**
 * Enthält eine Liste aller spielunabhängigen Netzwerk-Pakete des Programms.
 */
public class PaketListe {
	
	public static void normalePaketeAnmelden(PaketLexikon lexikon) {
		// beide Richtungen
		lexikon.anmelden(PacketKeepAlive.class);
		lexikon.anmelden(PacketDisconnect.class);
		lexikon.anmelden(PacketChatNachricht.class);
		lexikon.anmelden(PacketSessionStarten.class);
		lexikon.anmelden(PacketSessionVerlassen.class);
		lexikon.anmelden(PacketSpielVerlassen.class);
		
		// Client -> Server
		lexikon.anmelden(PacketHallo.class);
		lexikon.anmelden(PacketSessionAnnehmen.class);
		lexikon.anmelden(PacketSpielTaste.class);
		lexikon.anmelden(PacketSpielMaus.class);
		lexikon.anmelden(PacketSpielerAnzahl.class);
		
		// Server -> Client
		lexikon.anmelden(PacketSpielerListe.class);
		lexikon.anmelden(PacketServerSpielAnmelden.class);
		lexikon.anmelden(PacketSessionSpielerStatus.class);
		lexikon.anmelden(PacketSpielStarten.class);
		lexikon.anmelden(PacketSpielTeilnehmer.class);
		lexikon.anmelden(PacketSpielTeilnehmerDaten.class);
	}
}
