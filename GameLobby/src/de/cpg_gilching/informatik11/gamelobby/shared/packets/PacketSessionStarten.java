package de.cpg_gilching.informatik11.gamelobby.shared.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;

public class PacketSessionStarten extends Packet {
	
	public int sessionId;
	public int spielId;
	public List<String> eingeladeneSpieler;
	public int punktelimit;
	
	public PacketSessionStarten() {
	}
	
	public PacketSessionStarten(int sessionId, int spielId, Collection<String> eingeladeneSpieler, int punktelimit) {
		this.sessionId = sessionId;
		this.spielId = spielId;
		this.eingeladeneSpieler = Collections.unmodifiableList(new ArrayList<String>(eingeladeneSpieler));
		this.punktelimit = punktelimit;
	}
	
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(sessionId);
		out.writeInt(spielId);
		out.writeInt(eingeladeneSpieler.size());
		for (String str : eingeladeneSpieler)
			out.writeUTF(str);
		out.writeInt(punktelimit);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException {
		sessionId = in.readInt();
		spielId = in.readInt();
		
		int spielerAnzahl = in.readInt();
		List<String> namen = new ArrayList<String>(spielerAnzahl);
		for (int i = 0; i < spielerAnzahl; i++)
			namen.add(in.readUTF());
		
		eingeladeneSpieler = Collections.unmodifiableList(namen);
		
		punktelimit = in.readInt();
	}
	
}
