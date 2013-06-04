package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ClientSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

public class RealmofthemadschoolBeschreibung extends SpielBeschreibung {
	
	@Override
	public String getBezeichnung() {
		return "Realm of the mad School";
	}
	
	@Override
	public int tickrateGeben() {
		return 30;
	}
	
	@Override
	public ClientSpiel clientInstanzErstellen() {
		return new RealmofthemadschoolClient();
	}
	
	@Override
	public ServerSpiel serverInstanzErstellen() {
		return new RealmofthemadschoolServer();
	}
	
	@Override
	public int minimalspielerGeben() {
		return 2;
	}
	
	
	@Override
	public void paketeAnmelden(PaketLexikon lexikon) {

	}
	
}
