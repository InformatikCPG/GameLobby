package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketNeueBlase extends SpielPacket {
	
	public int id;
	public boolean neu;
	public int spielerfarbe;
	
	public PacketNeueBlase() {
	}
	
	public PacketNeueBlase(int id, boolean neu, int spielerfarbe) {
		this.id = id;
		this.neu = neu;
		this.spielerfarbe = spielerfarbe;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeBoolean(neu);
		out.writeInt(spielerfarbe);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		id = in.readInt();
		neu = in.readBoolean();
		spielerfarbe = in.readInt();
	}
	
}
