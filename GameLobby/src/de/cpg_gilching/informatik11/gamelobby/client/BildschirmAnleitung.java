package de.cpg_gilching.informatik11.gamelobby.client;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielBeschreibung;

/**
 * Die Controller- und Model-Komponente des Anleitungs-Bildschirms.
 */
public class BildschirmAnleitung {
	
	private ControllerClient client;
	private SpielBeschreibung beschreibung;
	
	private FensterAnleitung oberfläche;

	public BildschirmAnleitung(ControllerClient client, SpielBeschreibung beschreibung) {
		this.client = client;
		this.beschreibung = beschreibung;
		
		String anleitungString = Helfer.anleitungLaden(beschreibung.getAnleitungDateiname());

		oberfläche = new FensterAnleitung(this, this.beschreibung.getBezeichnung(), anleitungString);
	}
	
	public void schließen() {
		oberfläche.fensterSchliessen();
		client.anleitungEntfernen(this);
	}
	
}
