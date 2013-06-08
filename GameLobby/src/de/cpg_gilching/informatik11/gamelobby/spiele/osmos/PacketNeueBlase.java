package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketNeueBlase extends SpielPacket {
	
	public int id;
	public boolean neu;
	public boolean spielerblase;
	
	public PacketNeueBlase() {
	}
	
	public PacketNeueBlase(int id, boolean neu, boolean spielerblase) {
		this.id = id;
		this.neu = neu;
		this.spielerblase = spielerblase;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeBoolean(neu);
		out.writeBoolean(spielerblase);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		id = in.readInt();
		neu = in.readBoolean();
		spielerblase = in.readBoolean();
	}
	
}
