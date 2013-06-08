package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketSpielTeilnehmer extends Packet {
	
	/**
	 * Eine Aktion, die angibt, dass ein Spieler dem Spiel beigetreten ist.
	 */
	public static final int BEIGETRETEN = 1;
	/**
	 * Eine Aktion, die angibt, dass ein Spieler das Spiel verlassen haben.
	 */
	public static final int VERLASSEN = 2;
	
	public int spielId;
	public String spielerName;
	public int aktion;
	
	public PacketSpielTeilnehmer() {
	}
	
	public PacketSpielTeilnehmer(int spielId, String spielerName, int aktion) {
		this.spielId = spielId;
		this.spielerName = spielerName;
		this.aktion = aktion;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(spielId);
		out.writeUTF(spielerName);
		out.write(aktion);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		spielId = in.readInt();
		spielerName = in.readUTF();
		aktion = in.read();
	}
	
}
