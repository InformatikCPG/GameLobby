package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketTargetTot extends SpielPacket {
	
	public int id;
	
	public PacketTargetTot() {
	}
	
	public PacketTargetTot(int id) {
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
