package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.util.ArrayList;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class RealmofthemadschoolServer extends ServerSpiel {
	
	private List<Dude> Dudeliste;
	private List<Entity> Entityliste;

	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new ROMSPmanager(spieler, this);
	}
	
	@Override
	protected void starten() {
		Dudeliste = new ArrayList<Dude>();
		Entityliste = new ArrayList<Entity>();
		for (int i=0;i<teilnehmer.size();i++) {
			Dudeliste.add(new Dude(this));
		}
		
}
	public Dude sucheDude(Spieler s) {
		int index = teilnehmer.indexOf(s);
		return Dudeliste.get(index);
	}
	
	@Override
	public void tick() {
		for (int i=0;i<Dudeliste.size();i++) {
			Dudeliste.get(i).tick();
		}
	}
	
	public void einfügen (Entity e) {
       Entityliste.add(e);
	}
	
}