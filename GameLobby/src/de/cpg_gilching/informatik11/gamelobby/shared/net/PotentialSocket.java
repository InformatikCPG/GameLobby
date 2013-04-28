package de.cpg_gilching.informatik11.gamelobby.shared.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents an end of a two-sided connection that may or may not be a network socket.
 * <p/>
 * The end can be connected and disconnected manually after creation.
 */
public abstract class PotentialSocket {
	
	public abstract InputStream getInputStream() throws IOException;
	
	public abstract OutputStream getOutputStream() throws IOException;
	
	public abstract void close() throws IOException;
	
	public abstract void connect() throws IOException;
	
	public abstract String getRepresentation();
	
}
