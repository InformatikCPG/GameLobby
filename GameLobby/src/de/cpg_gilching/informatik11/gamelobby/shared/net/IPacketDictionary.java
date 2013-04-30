package de.cpg_gilching.informatik11.gamelobby.shared.net;

public interface IPacketDictionary {
	
	/**
	 * Returns the packet class associated with a given id.
	 */
	public Class<? extends Packet> getPacketById(int id);
	
	/**
	 * Returns the id of a packet.
	 */
	public int getPacketId(Packet p);
	
}
