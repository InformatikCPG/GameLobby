package de.cpg_gilching.informatik11.gamelobby.shared.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Represents a "real" socket that is connected over a network.
 */
public class NetSocket extends PotentialSocket {
	
	private InetAddress address = null;
	private int port = 0;
	private Socket socket = null;
	
	public NetSocket(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}
	
	public NetSocket(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void connect() throws IOException {
		if (socket != null)
			//			throw new IllegalStateException("already connected to socket");
			return; // not actually an exception
			
		socket = new Socket(address, port);
		socket.setTcpNoDelay(true);
	}
	
	@Override
	public void close() throws IOException {
		socket.close();
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}
	
	@Override
	public String getRepresentation() {
		return socket.getInetAddress().toString();
	}
	
}
