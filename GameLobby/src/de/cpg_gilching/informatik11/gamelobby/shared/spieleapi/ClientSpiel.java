package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import de.cpg_gilching.informatik11.gamelobby.client.SpielOberfläche;

/**
 * Die Basis-Klasse der Spiele-API für die Client-Seite der Spiele.<br>
 * Bereitgestellt werden einige Hilfsmethoden, die die Verwaltung von Input und Rendering vereinfachen.
 */
public abstract class ClientSpiel {
	
	private PaketManager paketManager = null;
	private SpielOberfläche spielView = null;
	
	/**
	 * Wird vom Lobby-System aufgerufen, um das Spiel zu initialisieren.
	 * 
	 * <p>
	 * Achtung: Sollte nicht von einem implementierenden Spiel aufgerufen werden!
	 * </p>
	 */
	public final void _init(SpielOberfläche spielView) {
		this.spielView = spielView;
		starten();
	}
	
	/**
	 * Setzt den PaketManager, der die Pakete für dieses Spiel empfängt.
	 * 
	 * @param manager der PaketManager, oder null, wenn er ausgeschaltet werden soll
	 */
	protected final void setPaketManager(PaketManager manager) {
		paketManager = manager;
	}
	
	/**
	 * Registriert eine Taste, deren Druck automatisch an den Server übermittelt werden soll. Dafür wird das Paket {@link PacketSpielTaste} verwendet.
	 * 
	 * @param tastencode der virtuelle Tastencode der Taste
	 * @see KeyEvent
	 */
	protected final void netzwerkTasteRegistrieren(int tastencode) {
		netzwerkTasteRegistrieren(tastencode, tastencode);
	}
	
	/**
	 * Registriert eine Taste, deren Druck automatisch an den Server übermittelt werden soll. Dafür wird das Paket {@link PacketSpielTaste} verwendet.<br>
	 * Vor dem Senden wird der Tastencode in einen anderen Wert umgewandelt.
	 * 
	 * @param tastencode der virtuelle Tastencode der Taste
	 * @see KeyEvent
	 */
	protected final void netzwerkTasteRegistrieren(int tastencode, final int alsTaste) {
		tasteRegistrieren(tastencode, new ITastaturListener() {
			@Override
			public void onTasteGeändert(KeyEvent event, boolean zustand) {
				spielPacketSenden(new PacketSpielTaste(alsTaste, zustand));
			}
		});
	}
	
	/**
	 * Registriert eine Taste, deren Druck an den gegebenen {@link ITastaturListener} übermittelt wird.
	 * 
	 * @param tastencode der virtuelle Tastencode der Taste
	 * @param listener der Listener, der das Drücken und Loslassen der Taste verarbeitet
	 * @see KeyEvent
	 */
	protected final void tasteRegistrieren(int tastencode, ITastaturListener listener) {
		spielView.getInputListener().tasteRegistrieren(tastencode, listener);
	}
	
	/**
	 * Registriert einen Maus-Listener, der Mausklicks automatisch an den Server übermittelt. Dafür wird das Paket {@link PacketSpielMaus} verwendet.
	 */
	protected final void netzwerkMausRegistrieren() {
		mausRegistrieren(new IMausListener() {
			@Override
			public void onMaustasteGeändert(MouseEvent event, boolean zustand) {
				spielPacketSenden(new PacketSpielMaus(event.getButton(), zustand));
			}
		});
	}
	
	/**
	 * Registriert einen Maus-Listener, der Mausklicks verarbeitet.
	 * 
	 * @param listener der Maus-Listener
	 */
	protected final void mausRegistrieren(IMausListener listener) {
		spielView.getInputListener().mausRegistrieren(listener);
	}
	

	/**
	 * Registriert einen Mausrad-Listener, der Scrollen des Mausrads verarbeitet.
	 * 
	 * @param listener der Mausrad-Listener
	 */
	protected final void mausradRegistrieren(IMausradListener listener) {
		spielView.getInputListener().mausradRegistrieren(listener);
	}

	/**
	 * Aktiviert eine Leinwand ({@link Canvas}), auf die gerendert werden kann.<br>
	 * Wenn die Größe nicht quadratisch ist, werden schwarze Ränder hinzugefügt.
	 * 
	 * @param breite die Breite der Leinwand
	 * @param höhe die Höhe der Leinwand
	 */
	protected final void leinwandAktivieren(int breite, int höhe) {
		spielView.canvasHinzufügen(breite, höhe);
	}
	
	/**
	 * Gibt die derzeit aktivierte Leinwand als {@link Canvas}-Objekt zurück, um eigene Einstellungen daran vorzunehmen.
	 */
	protected final Canvas getLeinwand() {
		return spielView.getCanvas();
	}
	
	/**
	 * Sendet ein {@link SpielPacket} an den Server.
	 * 
	 * @param packet das Packet, das gesendet werden soll
	 */
	protected final void spielPacketSenden(SpielPacket packet) {
		spielView.getLobbyBildschirm().packetSenden(packet);
	}
	
	/**
	 * Wird aufgerufen, sobald das Spiel gestartet wird. Diese Methode ersetzt in der Regel den Konstruktor, da erst bei diesem Aufruf das Spiel vollständig initialisiert wurde.
	 */
	protected abstract void starten();
	
	/**
	 * Diese Methode wird bei aktivierter Leinwand je nach Tickrate des Spiels automatisch aufgerufen. Das {@link Graphics2D}-Objekt kann zum Rendern auf die Leinwand hergenommen werden.
	 * 
	 * @param g der "Pinsel", mit dem auf die Leinwand gerendert wird
	 */
	public void leinwandRendern(Graphics2D g) {
	}
	
	/**
	 * Gibt den derzeit aktivierten PaketManager zurück.
	 */
	public final PaketManager getPaketManager() {
		return paketManager;
	}
	
}
