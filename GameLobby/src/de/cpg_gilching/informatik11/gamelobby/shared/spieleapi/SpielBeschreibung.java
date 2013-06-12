package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

/**
 * Dies ist die Basis-Klasse für jedes implementierende Spiel der Spiele-API. Hier werden grundlegende Informationen zum Spiel festgelegt sowie die Client- und Server-Instanzen erstellt.
 */
public abstract class SpielBeschreibung {
	
	private int spielId = -1;
	
	public final void setSpielId(int spielId) {
		this.spielId = spielId;
	}
	
	public final int getSpielId() {
		return spielId;
	}
	
	/**
	 * Gibt die minimale Anzahl der Spieler zurück, die dieses Spiel unterstüzt. Standard: 2
	 */
	public int minimalspielerGeben() {
		return 2;
	}
	
	/**
	 * Gibt die maximale Anzahl der Spieler zurück, die dieses Spiel unterstüzt. Standard: -1 (also keine Begrenzung)
	 */
	public int maximalspielerGeben() {
		return -1;
	}
	
	/**
	 * Gibt eine leserliche Bezeichnung des Spiels zurück.
	 */
	public abstract String getBezeichnung();
	
	/**
	 * Gibt die Tickrate des Spiels zurück. Diese gibt an, wie oft ein Server-Spiel tickt und ein Client-Spiel gerendert wird.<br>
	 * Werte von über 30 werden nicht unterstützt.
	 */
	public abstract int tickrateGeben();
	
	/**
	 * Gibt den Dateinamen der Anleitung für dieses Spiel zurück. Standard ist der technische Name der Bezeichnung mit .txt-Endung.
	 */
	public String getAnleitungDateiname() {
		return getBezeichnung().replaceAll("\\s", "").toLowerCase() + ".txt";
	}

	/**
	 * Hier müssen die Klassen aller Netzwerk-Pakete angemeldet werden, die von diesem Spiel benutzt werden.
	 * 
	 * @param lexikon das PaketLexikon, bei dem die Pakete angemeldet werden.
	 */
	public void paketeAnmelden(PaketLexikon lexikon) {
	}
	
	/**
	 * Erstellt ein neues implementierendes {@link ClientSpiel} für diesen Spiel-Typ und gibt es zurück.
	 */
	public abstract ClientSpiel clientInstanzErstellen();
	
	/**
	 * Erstellt ein neues implementierendes {@link ServerSpiel} für diesen Spiel-Typ und gibt es zurück.
	 */
	public abstract ServerSpiel serverInstanzErstellen();
	
}
