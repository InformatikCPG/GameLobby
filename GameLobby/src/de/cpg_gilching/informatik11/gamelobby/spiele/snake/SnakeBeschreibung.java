package de.cpg_gilching.informatik11.gamelobby.spiele.snake;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;
import de.cpg_gilching.informatik11.gamelobby.spiele.pong.PacketBallBewegen;
import de.cpg_gilching.informatik11.gamelobby.spiele.pong.PacketSchlägerBewegen;

public class SnakeBeschreibung extends SpielBeschreibung {

	@Override
	public String getBezeichnung() {
		return "Multiplayer Snake";
	}
	
	@Override
	public int maximalspielerGeben() {
		return 10;
	}

	@Override
	public int tickrateGeben() {
		return 20;
	}

	@Override
	public ClientSpiel clientInstanzErstellen() {
		// TODO Auto-generated method stub
		return new SnakeSpielClient();
	}

	@Override
	public ServerSpiel serverInstanzErstellen() {
		// TODO Auto-generated method stub
		return new SnakeSpielServer();
	}
	
	@Override
	public void paketeAnmelden(PaketLexikon lexikon) {
		lexikon.anmelden(PacketFeldSetzen.class);
	}
}
