package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketSpielTeilnehmerDaten extends Packet {
	
	public int spielId;
	public String spielerName;
	public int farbe;
	public int punkte;
	public int tempPunkte;
	
	public PacketSpielTeilnehmerDaten() {
	}
	
	public PacketSpielTeilnehmerDaten(int spielernId, String spielerName, int farbe, int punkte, int tempPunkte) {
		this.spielId = spielernId;
		this.spielerName = spielerName;
		this.farbe = farbe;
		this.punkte = punkte;
		this.tempPunkte = tempPunkte;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(spielId);
		out.writeUTF(spielerName);
		out.writeInt(farbe);
		out.writeInt(punkte);
		out.writeInt(tempPunkte);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		spielId = in.readInt();
		spielerName = in.readUTF();
		farbe = in.readInt();
		punkte = in.readInt();
		tempPunkte = in.readInt();
	}
	
}
