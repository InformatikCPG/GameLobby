package de.cpg_gilching.informatik11.gamelobby.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketSessionStarten;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

/**
 * Eine Model-Klasse für ein dargestelles Spiel in der Combobox.
 */
class SpielEintrag {
	String bezeichner;
	int spielId;
	
	public SpielEintrag(SpielBeschreibung spiel) {
		this.bezeichner = spiel.getBezeichnung();
		this.spielId = spiel.getSpielId();
	}
	
	@Override
	public String toString() {
		return bezeichner;
	}
}

/**
 * Die Controller- und Model-Komponente der "Server-Lobby".
 */
public class BildschirmServerLobby {
	
	private FensterServerLobby oberfläche = new FensterServerLobby(this);
	private ControllerClient client;
	
	private Map<String, SpielerZustand> spielerTabelle = new TreeMap<String, SpielerZustand>();
	private int ausgewähltAnzahl = 0;
	private SpielBeschreibung spielAusgewählt = null;
	private int punktelimit = 10;
	private int[] punktelimitAuswahl = { 10, 20, 50, 100, 200, 500 };
	
	public BildschirmServerLobby(ControllerClient clientController) {
		this.client = clientController;
		
		oberfläche.spielerListeAktualisieren(spielerTabelle.values());
		spielFormularFüllen();
		
		setMusikAn(true);
	}
	
	/**
	 * Zeigt eine eingehende Chat-Nachricht auf der Oberfläche an.
	 */
	public void chatNachrichtAnzeigen(String nachricht) {
		oberfläche.chatNachrichtAnzeigen(nachricht);
	}
	
	/**
	 * Schickt eine ausgehende Chat-Nachricht an den Server.
	 */
	public void chatNachrichtSenden(String nachricht) {
		client.chatNachrichtSenden(-1, nachricht);
	}
	
	/**
	 * Schließt das Fenster der Oberfläche.
	 */
	public void verlassen() {
		oberfläche.fensterSchliessen();
		setMusikAn(false);
	}
	
	/**
	 * Informiert den Server, dass die Verbindung beendet werden soll.
	 */
	public void verbindungTrennen() {
		client.verbindungTrennen();
	}
	
	/**
	 * Fügt einen Spieler auf der Anzeigeliste hinzu.
	 * 
	 * @param username der Name des Spielers
	 */
	public void spielerAnlegen(String username) {
		spielerTabelle.put(username, new SpielerZustand(username));
		oberfläche.spielerListeAktualisieren(spielerTabelle.values());
	}
	
	/**
	 * Entfernt einen Spieler von der Anzeigeliste. Wenn er gerade ausgewählt ist, wird er davor noch abgewählt.
	 * 
	 * @param username der Name des Spielers
	 */
	public void spielerEntfernen(String username) {
		SpielerZustand entfernt = spielerTabelle.remove(username);
		if (entfernt != null && entfernt.istAusgewählt()) {
			spielerAuswahlUmschalten(entfernt);
		}
		
		oberfläche.spielerListeAktualisieren(spielerTabelle.values());
	}
	
	public void spielerAuswahlUmschalten(SpielerZustand spieler) {
		if (spieler.istAusgewählt()) {
			spieler.setAusgewählt(false);
			ausgewähltAnzahl--;
		}
		else {
			spieler.setAusgewählt(true);
			ausgewähltAnzahl++;
		}
		
		spielFormularFüllen();
	}
	
	/**
	 * Zeigt die Anleitung des gerade ausgewählten Spiels an.
	 */
	public void spielanleitungAnzeigen() {
		if (spielAusgewählt != null) {
			client.anleitungÖffnen(spielAusgewählt);
		}
	}
	
	/**
	 * Initialisiert das Formular mit den korrekten Daten für das Spiel und der richtigen Markierung anhand der ausgewählten Spieler-Anzahl.
	 */
	private void spielFormularFüllen() {
		if (spielAusgewählt == null) {
			oberfläche.spielFormularAktualisieren(false, ausgewähltAnzahl, -1, false);
		}
		else {
			int minSpieler = spielAusgewählt.minimalspielerGeben();
			int maxSpieler = spielAusgewählt.maximalspielerGeben();
			int anzahl = ausgewähltAnzahl + 1;
			
			boolean gültig = true;
			
			if (anzahl < minSpieler)
				gültig = false;
			if (maxSpieler > -1 && anzahl > maxSpieler)
				gültig = false;
			
			oberfläche.spielFormularAktualisieren(true, ausgewähltAnzahl, maxSpieler - 1, gültig);
		}
	}
	
	/**
	 * Ändert, welcher Eintrag gerade ausgewählt ist.
	 * 
	 * @param eintrag der neue SpielEintrag
	 */
	public void spielAusgewählt(SpielEintrag eintrag) {
		if (eintrag == null) {
			spielAusgewählt = null;
		}
		else {
			spielAusgewählt = client.getBeschreibungNachId(eintrag.spielId);
		}
		
		spielFormularFüllen();
	}
	
	public void spielAuswahlAktualisieren(Collection<SpielBeschreibung> spiele) {
		Object[] dropDownEinträge = new Object[spiele.size() + 1];
		dropDownEinträge[0] = "== Spiel auswählen ==";
		
		int i = 1;
		for (SpielBeschreibung spiel : spiele) {
			dropDownEinträge[i++] = new SpielEintrag(spiel);
		}
		
		oberfläche.spielDropdownFüllen(dropDownEinträge);
	}
	
	public void punktelimitSetzen(int punktelimit) {
		this.punktelimit = punktelimit;
	}
	
	/**
	 * Informiert den Server, dass eine Session mit den ausgewählten Einstellungen gestartet werden soll.
	 */
	public void sessionStartAnfragen() {
		if (spielAusgewählt == null)
			return;
		
		int beschreibungId = spielAusgewählt.getSpielId();
		
		List<String> spielerNamen = new ArrayList<String>();
		for (SpielerZustand spieler : spielerTabelle.values()) {
			if (spieler.istAusgewählt()) {
				spielerNamen.add(spieler.getName());
				
				// abwählen
				spielerAuswahlUmschalten(spieler);
			}
		}
		
		client.getVerbindung().sendPacket(new PacketSessionStarten(-1, beschreibungId, spielerNamen, punktelimit));
	}
	
	public void setMusikAn(boolean tonAn) {
		if (tonAn)
			Sound.musikServerLobby.play(true);
		else
			Sound.musikServerLobby.stop();
	}
	
	public void setTonAn(boolean tonAn) {
		client.tonAngeschaltet = tonAn;
	}
	
	public ControllerClient getClient() {
		return client;
	}
	
	public int[] getPunktelimitAuswahl() {
		return punktelimitAuswahl;
	}

}
