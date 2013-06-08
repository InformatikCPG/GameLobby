package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PacketSpielTaste;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class ROMSPmanager implements PaketManager {

	private Spieler spieler;
	private RealmofthemadschoolServer server;

	public ROMSPmanager(Spieler spieler,
			RealmofthemadschoolServer realmofthemadschoolServer) {
				this.spieler = spieler;
		// TODO Auto-generated constructor stub
				this.server = realmofthemadschoolServer;
	}
	
	public void verarbeiten(PacketSpielTaste packet) {
		if(packet.tastencode==KeyEvent.VK_W) {
			server.sucheDude(spieler).Wgedrückt=packet.zustand;
		}
		if(packet.tastencode==KeyEvent.VK_A) {
			server.sucheDude(spieler).Agedrückt=packet.zustand;
		}
		if(packet.tastencode==KeyEvent.VK_S) {
			server.sucheDude(spieler).Sgedrückt=packet.zustand;
		}
		if(packet.tastencode==KeyEvent.VK_D) {
			server.sucheDude(spieler).Dgedrückt=packet.zustand;
		}
		if(packet.tastencode==KeyEvent.VK_SPACE && packet.zustand) {
			server.sucheDude(spieler).attack();
		}
	}

}
