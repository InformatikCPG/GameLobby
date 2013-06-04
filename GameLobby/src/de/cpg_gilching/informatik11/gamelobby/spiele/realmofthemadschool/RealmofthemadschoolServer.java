package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.util.ArrayList;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
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
			Dude dude = new Dude(this, teilnehmer.get(i));
			dude.x = Helfer.zufallsZahl(600);
			dude.y = Helfer.zufallsZahl(600);
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
		for (int i=0;i<Entityliste.size();i++) {
			Entity entity = Entityliste.get(i);
			entity.tick();
			if(entity.dead == true) {
				Entityliste.remove(i);
				tracker.untrackTarget(entity);
				i--; //ähmähmähm damit mit Schleife richtig (heueheue) damit Schleife richtig geht, well this thing is amazing at recognizing circles and everything *chuckle* awww... dont mind me ..
			}
		}
		
		for (int i = 0; i < Dudeliste.size(); i++) {
			if (Dudeliste.get(i).dead) {
				scoreboard.punkteVorbereiten(Dudeliste.get(i).spieler, teilnehmer.size() - Dudeliste.size());
				Dudeliste.remove(i);
				i--;
			}
		}
		
		if (Dudeliste.size() == 1) {
			scoreboard.punkteVorbereiten(Dudeliste.get(0).spieler, teilnehmer.size() - 1);
			scoreboard.punkteAnwenden();
			
			tracker.untrackAll();
			starten();
		}

		tracker.tick();
	}
	
	public void einfügen (Entity e) {
		Entityliste.add(e);
		tracker.trackTarget(e);
	}

	public void collisiondetecting(Bullet bullet) {
		for (int i=0;i<Dudeliste.size();i++) {
			Dude d = Dudeliste.get(i);
			int abstand = 35;
			if(d!=bullet.dude && (d.x-bullet.x)*(d.x-bullet.x) + (d.y-bullet.y)*(d.y-bullet.y) < abstand * abstand) {
				bullet.dead = true;
                d.damage();
			}
		}
		if(bullet.x <=0 || bullet.x>=600||bullet.y <=0 || bullet.y>=600) {
			bullet.dead = true;
		}
	}
	
}