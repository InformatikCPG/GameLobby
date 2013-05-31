package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class OsmosServer extends ServerSpiel {
	
	public final Random rand = new Random();

	private double weltRadius;
	private List<Blase> blasen = new ArrayList<Blase>();
	private Tracker tracker = new Tracker(this);
	
	private List<Blase> tempBlasen = new ArrayList<Blase>();
	private List<Blase> modBlasen = blasen;
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new SpielerController(this, spieler);
	}
	
	@Override
	protected void starten() {
		double teilnehmerFaktor = Math.log(teilnehmer.size() + Math.E);
		
		weltRadius = 1500.0 * teilnehmerFaktor;
		
		double winkelSchritt = 2 * Math.PI / teilnehmer.size();
		double winkel = 0;
		
		for (Spieler spieler : teilnehmer) {
			Blase b = new Blase(this, 50.0);
			b.setController((SpielerController) getPaketManagerFür(spieler));
			b.teleport(new Vektor(Math.sin(winkel) * weltRadius / 2, Math.cos(winkel) * weltRadius / 2));
			blaseHinzufügen(b);
			
			packetAnSpieler(spieler, new PacketSetup(weltRadius, b.id));
			
			winkel += winkelSchritt;
		}
		
		int npcBlasen = (int) (Helfer.zufallsZahl(40, 50) * teilnehmerFaktor);
		for (int i = 0; i < npcBlasen; i++) {
			double npcRadius = rand.nextDouble() * 40.0 + 5.0;
			
			Blase npc = new Blase(this, npcRadius);
			npc.getGeschwindigkeit().kopiere(rand.nextDouble(), rand.nextDouble());
			
			boolean kollidiert = true;
			while(kollidiert) {
				npc.getPosition().x = (rand.nextDouble() - 0.5) * (weltRadius - npcRadius);
				npc.getPosition().y = (rand.nextDouble() - 0.5) * (weltRadius - npcRadius);
				
				kollidiert = false;
				for(Blase andere : blasen)
					if (Blase.kollidiert(andere, npc)) {
						kollidiert = true;
						break;
					}
			}
			
			blaseHinzufügen(npc);
		}
	}
	
	@Override
	public void tick() {
		modBlasen = tempBlasen;

		Iterator<Blase> it = blasen.iterator();
		while(it.hasNext()) {
			Blase b = it.next();
			
			// im letzten Tick gestorben
			if(b.istTot()) {
				it.remove();
				tracker.untrack(b);
				continue;
			}
			
			b.tick();

			// in diesem Tick gestorben
			if (b.istTot()) {
				it.remove();
				tracker.untrack(b);
			}
		}
		
		modBlasen = blasen;
		
		blasen.addAll(tempBlasen);
		tempBlasen.clear();

		// jede Blase mit jeder auf Kollision prüfen
		Blase b1, b2;
		Vektor v = new Vektor();
		
		for (int i = 0; i < blasen.size(); i++) {
			b1 = blasen.get(i);
			for (int j = i + 1; j < blasen.size(); j++) {
				b2 = blasen.get(j);
				
				if (Blase.kollidiert(b1, b2, v)) {
					double schnitt = b1.getRadius() + b2.getRadius() - v.länge();
					
					Blase g, k;
					
					// entscheiden, wer wen absorbiert
					if (b1.getRadius() > b2.getRadius()) {
						g = b1;
						k = b2;
					}
					else {
						g = b2;
						k = b1;
					}
					
					g.vergrößern(Math.sqrt(-k.vergrößern(-schnitt)));
				}
			}
		}

		tracker.tick();
		
		double sum = 0;
		for (Blase b : blasen) {
			sum += b.getRadius();
		}
		System.out.println("Gesamtradius: " + sum);
	}
	
	public void blaseHinzufügen(Blase b) {
		modBlasen.add(b);
		tracker.track(b);
	}
	
	public double getWeltRadius() {
		return weltRadius;
	}

}
