package de.cpg_gilching.informatik11.gamelobby.spiele.realmofthemadschool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketEntityTot extends SpielPacket {
	
	public int id;
	
	public PacketEntityTot() {
	}
	
	public PacketEntityTot(int id) {
		this.id = id;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(id);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		id = in.readInt();
	}
	
}
