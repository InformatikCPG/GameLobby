package de.cpg_gilching.informatik11.gamelobby.server.fakeplayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.net.IPacketDictionary;
import de.cpg_gilching.informatik11.gamelobby.shared.net.PotentialSocket;

/**
 * Eine Socket-Implementierung für die Bots.
 */
public class ArtificialSocket extends PotentialSocket {
	
	private final String repr = "AI-" + Helfer.zufallsZahl(1000, 10000);
	
	private IPacketDictionary dictionary;
	private final PipedInputStream pipeIn = new PipedInputStream();
	private final PipedOutputStream pipeOut = new PipedOutputStream();
	
	public ArtificialSocket(IPacketDictionary dictionary) {
		this.dictionary = dictionary;
	}
	
	@Override
	public void connect() throws IOException {
		PipedInputStream clientIn = new PipedInputStream();
		pipeOut.connect(clientIn);
		
		PipedOutputStream clientOut = new PipedOutputStream();
		pipeIn.connect(clientOut);
		
		ArtificialWorker worker = new ArtificialWorker(clientIn, clientOut, dictionary);
		
		Thread t = new Thread(worker);
		t.setName(repr);
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return pipeIn;
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return pipeOut;
	}
	
	@Override
	public void close() throws IOException {
	}
	
	@Override
	public String getRepresentation() {
		return repr;
	}
	
}
