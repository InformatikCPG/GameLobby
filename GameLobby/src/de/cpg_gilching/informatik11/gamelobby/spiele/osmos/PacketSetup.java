package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.cpg_gilching.informatik11.gamelobby.shared.spieleapi.SpielPacket;

public class PacketSetup extends SpielPacket {
	
	public double weltRadius;
	public int aktivId;
	
	public PacketSetup() {
	}
	
	public PacketSetup(double weltRadius, int aktivId) {
		this.weltRadius = weltRadius;
		this.aktivId = aktivId;
	}
	
	@Override
	public void ausgeben(DataOutputStream out) throws IOException {
		out.writeDouble(weltRadius);
		out.writeInt(aktivId);
	}
	
	@Override
	public void einlesen(DataInputStream in) throws IOException {
		weltRadius = in.readDouble();
		aktivId = in.readInt();
	}
	
}
