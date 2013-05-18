package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.client.SpielOberfläche; // TODO abhängigkeit von beidseitiger SpieleAPI vom client überdenken?

public abstract class ClientSpiel {
	
	private PaketManager paketManager = null;
	private SpielOberfläche spielView = null;
	
	public final void _init(SpielOberfläche spielView) {
		this.spielView = spielView;
		starten();
	}
	
	protected final void setPaketManager(PaketManager manager) {
		paketManager = manager;
	}
	
	protected final void netzwerkTasteRegistrieren(int tastencode) {
		netzwerkTasteRegistrieren(tastencode, tastencode);
	}
	
	protected final void netzwerkTasteRegistrieren(int tastencode, final int alsTaste) {
		tasteRegistrieren(tastencode, new ITastaturListener() {
			@Override
			public void onTasteGeändert(KeyEvent event, boolean zustand) {
				spielPacketSenden(new PacketSpielTaste(alsTaste, zustand));
			}
		});
	}
	
	protected final void tasteRegistrieren(int tastencode, ITastaturListener listener) {
		spielView.getInputListener().tasteRegistrieren(tastencode, listener);
	}
	
	protected final void leinwandAktivieren(int breite, int höhe) {
		spielView.canvasHinzufügen(breite, höhe);
	}
	
	protected final void spielPacketSenden(SpielPacket packet) {
		spielView.getLobbyBildschirm().packetSenden(packet);
	}
	
	
	protected abstract void starten();
	
	public void leinwandRendern(Graphics2D g) {
	}
	
	public final PaketManager getPaketManager() {
		return paketManager;
	}
	
}
