package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import de.cpg_gilching.informatik11.gamelobby.client.SpielOberfläche; // TODO abhängigkeit von beidseitiger SpieleAPI vom client überdenken?

public abstract class ClientSpiel {
	
	private SpielOberfläche spielView = null;
	private PaketManager paketManager = null;
	
	public final void _init(SpielOberfläche spielView) {
		this.spielView = spielView;
		starten();
	}
	
	public final void setPaketManager(PaketManager manager) {
		paketManager = manager;
	}
	
	public final PaketManager getPaketManager() {
		return paketManager;
	}
	
	protected final void netzwerkTasteRegistrieren(int tastencode) {
		tasteRegistrieren(tastencode, new ITastaturListener() {
			@Override
			public void onTasteGeändert(KeyEvent event, boolean zustand) {
				spielPacketSenden(new PacketSpielTaste(event.getKeyCode(), zustand));
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
		spielView.getLobbyBildschirm().getClient().getVerbindung().sendPacket(packet);
	}
	
	
	protected abstract void starten();
	
	public void leinwandRendern(Graphics2D g) {
	}
	
}
