package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Ein {@link SpielPacket}, das vom Server gesendet werden kann, um die aktuelle Anzahl der Spieler im Spiel zu übertragen.
 * 
 * Es muss vom PaketManager des Clients verarbeitet werden.
 */
public class PacketSpielerAnzahl extends SpielPacket {
	
	public int anzahl;
	
	public PacketSpielerAnzahl() {
	}
	
	public PacketSpielerAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(anzahl);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		anzahl = in.readInt();
	}
	
}
