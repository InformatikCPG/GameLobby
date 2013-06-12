package de.cpg_gilching.informatik11.gamelobby.shared.spieleapi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Ein {@link SpielPacket}, das von der automatischen Netzwerk-Maus des Clients gesendet wird, um Mausklicks zu signalisieren.
 * 
 * Es muss vom PaketManager des Servers manuell verarbeitet werden.
 * 
 * @see ClientSpiel#netzwerkMausRegistrieren()
 */
public class PacketSpielMaus extends SpielPacket {
	
	public int maustaste;
	public boolean zustand;
	
	public PacketSpielMaus() {
	}
	
	public PacketSpielMaus(int maustaste, boolean zustand) {
		this.maustaste = maustaste;
		this.zustand = zustand;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(maustaste);
		out.writeBoolean(zustand);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		maustaste = in.readInt();
		zustand = in.readBoolean();
	}
	
}
