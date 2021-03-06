package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import de.cpg_gilching.informatik11.gamelobby.server.LobbySpieler;
import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.PaketManager;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.ServerSpiel;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielChat;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.Spieler;

public class OsmosServer extends ServerSpiel {
	
	public final Random rand = new Random();
	
	private double weltRadius;
	private List<Blase> blasen = new ArrayList<Blase>();
	private Tracker tracker = new Tracker(this);
	
	private List<Blase> tempBlasen = new ArrayList<Blase>();
	private List<Blase> modBlasen = blasen;
	
	public int toteSpieler;
	private int restartTicks;
	
	@Override
	protected PaketManager paketManagerErstellen(Spieler spieler) {
		return new SpielerController(this, spieler);
	}
	
	@Override
	protected void starten() {
		toteSpieler = 0;
		restartTicks = -1;
		
		double teilnehmerFaktor = Math.log(teilnehmer.size() + Math.E);
		
		weltRadius = 1500.0 * teilnehmerFaktor;
		
		double winkelSchritt = 2 * Math.PI / teilnehmer.size();
		double winkel = 0;
		
		for (Spieler spieler : teilnehmer) {
			int farbe = (Helfer.zufallsZahl(0xCC) << 16) | (Helfer.zufallsZahl(0x99, 0xFF) << 8) | (Helfer.zufallsZahl(0xCC));
			scoreboard.anzeigefarbeSetzen(spieler, farbe);
			
			Blase b = new Blase(this, 50.0, farbe, spieler.getName());
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
			while (kollidiert) {
				npc.getPosition().x = (rand.nextDouble() * 2 - 1) * (weltRadius - npcRadius);
				npc.getPosition().y = (rand.nextDouble() * 2 - 1) * (weltRadius - npcRadius);
				
				// außerhalb des Kreises
				if (npc.getPosition().längeQuadrat() > (weltRadius - npcRadius) * (weltRadius - npcRadius))
					continue;
				
				kollidiert = false;
				for (Blase andere : blasen)
					if (Blase.kollidiert(andere, npc)) {
						kollidiert = true;
						break;
					}
			}
			
			blaseHinzufügen(npc);
		}
		
		chat.befehlRegistrieren("kill", new SpielChat.ChatBefehl() {
			@Override
			public void ausführen(Spieler sender, String[] argumente) {
				for (Blase b : blasen) {
					if (b.getController() != null && b.getController().getSpieler().getName().startsWith(argumente[0])) {
						b.vergrößern(-1000000);
						break;
					}
				}
			}
		});
	}
	
	@Override
	protected void spielerVerlassen(LobbySpieler spieler) {
		for (Blase b : blasen) {
			if (b.getController() != null && b.getController().getSpieler() == spieler) {
				b.setTot(true);
				toteSpieler--; // die Anzahl der toten Spieler muss wieder reduziert werden, da der Spieler nicht mehr dazuzählt
				break;
			}
		}
	}
	
	@Override
	public void tick() {
		modBlasen = tempBlasen;
		
		Iterator<Blase> it = blasen.iterator();
		while (it.hasNext()) {
			Blase b = it.next();
			
			// im letzten Tick gestorben
			if (b.istTot()) {
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
					
					
					double alteFläche = Math.PI * k.getRadius() * k.getRadius();
					k.vergrößern(-schnitt);
					double deltaFläche = alteFläche - Math.PI * k.getRadius() * k.getRadius(); // neueFläche - alteFläche
					
					double vergrößerteFläche = Math.PI * g.getRadius() * g.getRadius() + deltaFläche;
					double vergrößerterRadius = Math.sqrt(vergrößerteFläche / Math.PI);
					g.vergrößern(vergrößerterRadius - g.getRadius());
					
					
					// Spieler wurde von Spieler absorbiert
					if (k.istTot() && k.getController() != null && g.getController() != null) {
						chat.nachrichtAnAlleTeilnehmer(k.getController().getSpieler().getName() + " wurde von " + g.getController().getSpieler().getName() + " absorbiert!");
					}
				}
			}
		}
		
		tracker.tick();
		
		// wartet gerade auf Neustart
		if (restartTicks > 0) {
			restartTicks--;
		}
		// soll jetzt neustarten
		else if (restartTicks == 0) {
			neustarten();
			return;
		}
		// noch genau ein Spieler am Leben
		else if (teilnehmer.size() == toteSpieler + 1) {
			for (Blase b : blasen) {
				if (b.getController() != null && !b.istTot()) {
					scoreboard.punkteVorbereiten(b.getController().getSpieler(), toteSpieler);
					chat.nachrichtAnAlleTeilnehmer(b.getController().getSpieler().getName() + " hat die Runde gewonnen!");
					restartTicks = 60;
				}
			}
		}
		
		//		double sum = 0;
		//		for (Blase b : blasen) {
		//			sum += b.getRadius() * b.getRadius();
		//		}
		//		
		//		if (Helfer.zufallsZahl(30) == 0)
		//			chat.nachrichtAnAlleTeilnehmer("Gesamtfläche: " + sum);
	}
	
	private void neustarten() {
		scoreboard.punkteAnwenden();
		
		tracker.untrackAll();
		blasen.clear();
		tempBlasen.clear();
		starten();
	}
	
	public void blaseHinzufügen(Blase b) {
		modBlasen.add(b);
		tracker.track(b);
	}
	
	public void spielerTot(Spieler spieler) {
		scoreboard.punkteVorbereiten(spieler, toteSpieler);
		toteSpieler++;
		soundAnAlle("osmosAbsorbed");
	}
	
	public double getWeltRadius() {
		return weltRadius;
	}
	
}
