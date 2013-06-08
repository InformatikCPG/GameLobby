package de.cpg_gilching.informatik11.gamelobby.spiele.pong;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class PongBeschreibung extends SpielBeschreibung {
	
	public static final int BILDSCHIRM_BREITE = 600;
	public static final int BILDSCHIRM_HÖHE = 600;
	public static final int GRENZE_LINKS = 80;
	public static final int GRENZE_RECHTS = 520;
	public static final int SCHLÄGER_BREITE = 20;
	public static final int SCHLÄGER_HÖHE = 88; // 80 Bildhöhe + 8 Ballradius
	public static final int BALL_RADIUS = 8;
	
	@Override
	public int maximalspielerGeben() {
		return 2;
	}
	
	@Override
	public int minimalspielerGeben() {
		return 2;
	}
	
	@Override
	public String getBezeichnung() {
		return "Pong";
	}
	
	@Override
	public int tickrateGeben() {
		return 30;
	}
	
	@Override
	public void paketeAnmelden(PaketLexikon lexikon) {
		lexikon.anmelden(PacketSchlägerBewegen.class);
		lexikon.anmelden(PacketBallBewegen.class);
	}
	
	@Override
	public ClientSpiel clientInstanzErstellen() {
		return new PongSpielClient();
	}
	
	@Override
	public ServerSpiel serverInstanzErstellen() {
		return new PongSpielServer();
	}
	
}
