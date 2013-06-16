package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class SnakeBeschreibung extends SpielBeschreibung {

	//Name des Spiels festlegen
	@Override
	public String getBezeichnung() {
		return "Multiplayer Snake";
	}
	
	//Maximale Spieleranzahl festlegen
	@Override
	public int maximalspielerGeben() {
		return 10;
	}

	//Tickrate festlegen
	@Override
	public int tickrateGeben() {
		return 30;
	}

	//Snake-Client starten
	@Override
	public ClientSpiel clientInstanzErstellen() {
		return new SnakeSpielClient();
	}

	//Snake-Server starten
	@Override
	public ServerSpiel serverInstanzErstellen() {
		return new SnakeSpielServer();
	}
	
	//Anmelden der benötigten Packete
	@Override
	public void paketeAnmelden(PaketLexikon lexikon) {
		lexikon.anmelden(PacketFeldSetzen.class);
		lexikon.anmelden(PacketNachrichtSenden.class);
		lexikon.anmelden(PacketReset.class);
	}
}
