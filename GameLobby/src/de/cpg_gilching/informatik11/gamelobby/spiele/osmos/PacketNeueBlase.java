package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketNeueBlase extends SpielPacket {
	
	public int id;
	public boolean neu;
	public int spielerfarbe;
	public String label;
	
	public PacketNeueBlase() {
	}
	
	public PacketNeueBlase(int id, boolean neu, int spielerfarbe, String label) {
		this.id = id;
		this.neu = neu;
		this.spielerfarbe = spielerfarbe;
		this.label = label;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeBoolean(neu);
		out.writeInt(spielerfarbe);
		out.writeUTF(label);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		id = in.readInt();
		neu = in.readBoolean();
		spielerfarbe = in.readInt();
		label = in.readUTF();
	}
	
}
