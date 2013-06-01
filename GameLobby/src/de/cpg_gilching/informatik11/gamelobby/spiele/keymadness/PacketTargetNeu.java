package de.cpg_gilching.informatik11.gamelobby.spiele.keymadness;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketTargetNeu extends SpielPacket {
	
	public int id;
	public int tastencode;
	public boolean valid;
	
	public PacketTargetNeu() {
	}
	
	public PacketTargetNeu(int id, KeyMadnessTarget target) {
		this.id = id;
		this.tastencode = target.tastencode;
		this.valid = target.valid;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeInt(tastencode);
		out.writeBoolean(valid);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		id = in.readInt();
		tastencode = in.readInt();
		valid = in.readBoolean();
	}
	
}
