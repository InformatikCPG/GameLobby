package de.cpg_gilching.informatik11.gamelobby.shared.net;

public interface IPacketDictionary {
	
	/**
	 * Returns the packet class associated with a given id.
	 * 
	 * @return the matching class, or null if it wasn't found
	 */
	public Class<? extends Packet> getPacketById(int id);
	
	/**
	 * Returns the id of a packet. Valid ids must be positive.
	 * 
	 * @return the id, or -1 if it wasn't found
	 */
	public int getPacketId(Packet p);
	
}
