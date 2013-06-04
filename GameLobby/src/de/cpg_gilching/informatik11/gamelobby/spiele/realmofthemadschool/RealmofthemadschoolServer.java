package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.util.ArrayList;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.EntityTracker;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class RealmofthemadschoolServer extends ServerSpiel {
	
	private List<Dude> Dudeliste;
	private List<Entity> Entityliste;
	private EntityTracker tracker;

	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new ROMSPmanager(spieler, this);
	}
	
	@Override
	protected void starten() {
		Dudeliste = new ArrayList<Dude>();
		Entityliste = new ArrayList<Entity>();
		tracker = new EntityTracker(this);

		for (int i=0;i<teilnehmer.size();i++) {
			Dude dude = new Dude(this);
			Dudeliste.add(dude);
			einfügen(dude);
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
		
		tracker.tick();
	}
	
	public void einfügen (Entity e) {
		Entityliste.add(e);
		tracker.trackTarget(e);
	}
	
}