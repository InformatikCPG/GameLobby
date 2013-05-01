package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketSessionSpielerStatus extends Packet {
	
	public int sessionId;
	public String spielerName;
	/**
	 * true: der Spieler hat auf "Annehmen" geklickt<br>
	 * false: der Spieler hat die Session verlassen
	 */
	public boolean istBereit;
	
	public PacketSessionSpielerStatus() {
	}
	
	public PacketSessionSpielerStatus(int sessionId, String spielerName, boolean istBereit) {
		this.sessionId = sessionId;
		this.spielerName = spielerName;
		this.istBereit = istBereit;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(sessionId);
		out.writeUTF(spielerName);
		out.writeBoolean(istBereit);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		sessionId = in.readInt();
		spielerName = in.readUTF();
		istBereit = in.readBoolean();
	}
	
}
