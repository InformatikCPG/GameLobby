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
	public Vektor position;
	
	public PacketNeueBlase() {
	}
	
	public PacketNeueBlase(int id, boolean neu, int spielerfarbe, String label, Vektor position) {
		this.id = id;
		this.neu = neu;
		this.spielerfarbe = spielerfarbe;
		this.label = label;
		this.position = position;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeBoolean(neu);
		out.writeInt(spielerfarbe);
		out.writeUTF(label);
		out.writeDouble(position.x);
		out.writeDouble(position.y);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		id = in.readInt();
		neu = in.readBoolean();
		spielerfarbe = in.readInt();
		label = in.readUTF();
		position = new Vektor(in.readDouble(), in.readDouble());
	}
	
}
