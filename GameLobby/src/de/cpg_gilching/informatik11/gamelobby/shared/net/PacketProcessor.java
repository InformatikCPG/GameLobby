package de.cpg_gilching.informatik11.gamelobby.shared.net;

/**
 * The base class for something that is able to process {@link Packet}s.
 * <p/>
 * In addition to implementing all abstract methods, implementations of this class have to provide a separate method for each packet type that can be received. Those methods have to follow the following specification:
 * <ul>
 * <li>It has to be called "handle".</li>
 * <li>It has to be public.</li>
 * <li>It has to accept a single parameter of the exact type of the packet that is handled by this method.</li>
 * <li>It <b>may</b> throw flagged or unflagged exceptions, which will be delegated to the {@link PacketProcessor#onException(Exception, Packet)} method.</li>
 * </ul>
 * 
 */
public abstract class PacketProcessor {
	
	/**
	 * Called when a packet type was received that didn't have any matching handler method.
	 * 
	 * @param packet the packet instance that couldn't be processed
	 */
	public abstract void onUnhandledPacket(Packet packet);
	
	/**
	 * Called when an exception in the method that handles an incoming packet occured.
	 * 
	 * @param exception the exception that was thrown
	 * @param packet the packet that was supposed to be processed
	 */
	public abstract void onException(Exception exception, Packet packet);
	
}
