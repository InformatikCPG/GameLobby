package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketSpielPunkte extends Packet {
	
	public int spielId;
	public String spielerName;
	public int punkte;
	public int tempPunkte;
	
	public PacketSpielPunkte() {
	}
	
	public PacketSpielPunkte(int spielernId, String spielerName, int punkte, int tempPunkte) {
		this.spielId = spielernId;
		this.spielerName = spielerName;
		this.punkte = punkte;
		this.tempPunkte = tempPunkte;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(spielId);
		out.writeUTF(spielerName);
		out.writeInt(punkte);
		out.writeInt(tempPunkte);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		spielId = in.readInt();
		spielerName = in.readUTF();
		punkte = in.readInt();
		tempPunkte = in.readInt();
	}
	
}
