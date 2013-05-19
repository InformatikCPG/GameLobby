package de.cpg_gilching.informatik11.gamelobby.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.server.fakeplayer.ArtificialSocket;
import de.cpg_gilching.informatik11.gamelobby.shared.AdapterPaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Connection;
import de.cpg_gilching.informatik11.gamelobby.shared.net.NetSocket;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Packet;
import de.cpg_gilching.informatik11.gamelobby.shared.net.PotentialSocket;
import de.cpg_gilching.informatik11.gamelobby.shared.packets.PacketDisconnect;


public class ServerMain implements Runnable {
	
	private static final int SERVER_TPS = 20;
	private static final int MS_PER_TICK = 1000 / SERVER_TPS;
	
	private List<Connection> connectedClients = Collections.synchronizedList(new ArrayList<Connection>());
	private List<Connection> newClients = Collections.synchronizedList(new ArrayList<Connection>());
	private volatile boolean notYetStarted = true;
	private volatile boolean started = false;
	private int ticks = 0;
	
	private AdapterPaketLexikon dictionary = new AdapterPaketLexikon();
	private ControllerServer controller;
	
	private Thread mainServerThread;
	private Thread clientAcceptThread;
	
	private ServerSocket serverSocket = null;
	private final int port;
	
	public ServerMain(int port) {
		this.port = port;
		mainServerThread = new Thread(this, "Main-Server-Thread");
		mainServerThread.start();
	}
	
	public void kickClient(Connection connection, String reason) {
		log("Disconnecting " + connection.getSocket().getRepresentation() + ": " + reason);
		
		connection.sendPacket(new PacketDisconnect(reason));
		connection.close();
	}
	
	public void kickAll(String reason) {
		synchronized (connectedClients) {
			for (Connection c : connectedClients) {
				kickClient(c, reason); // only sets the remove flag, will be removed on next tick
			}
		}
	}
	
	public void broadcast(Packet p) {
		synchronized (connectedClients) {
			for (Connection c : connectedClients)
				c.sendPacket(p);
		}
	}
	
	private void tick(int ms) {
		ticks++;
		
		List<Connection> toAdd = null;
		synchronized (newClients) {
			if (!newClients.isEmpty()) {
				toAdd = new ArrayList<Connection>(newClients);
				newClients.clear();
			}
		}
		
		if (toAdd != null) {
			for (Connection c : toAdd) {
				connectedClients.add(c);
				controller.onSpielerVerbinden(c);
			}
			
		}
		
		synchronized (connectedClients) {
			Iterator<Connection> it = connectedClients.iterator();
			while (it.hasNext()) {
				Connection c = it.next();
				c.processPackets();
				
				if (c.isClosed()) {
					controller.onSpielerVerlassen(c);
					it.remove();
				}
			}
		}
		
		controller.tick(ms);
	}
	
	@Override
	public void run() {
		notYetStarted = false;
		
		if (port > 0) {
			try {
				serverSocket = new ServerSocket();
				//		serverSocket.setReuseAddress(true);
				serverSocket.setSoTimeout(5000);
				serverSocket.bind(new InetSocketAddress(port));
			} catch (IOException e) {
				logError("Error while binding server socket to port " + port + "!", e);
				return;
			}
		}
		
		controller = new ControllerServer(this, dictionary);
		
		started = true;
		
		if (serverSocket != null) {
			clientAcceptThread = new Thread(new ClientAcceptTask(), "Client-Accept-Thread");
			clientAcceptThread.start();
		}
		
		log("Server started on port " + port + ". Waiting for clients ...");
		
		mainLoop();
	}
	
	private void mainLoop() {
		long lastTPS = System.nanoTime();
		int lastTPSTick = 0;
		long nsSpent = 0;
		long lastTickTime = lastTPS;
		
		while (isRunning()) {
			long start = System.nanoTime();
			
			try {
				tick((int) ((start - lastTickTime) / 1000000L));
				lastTickTime = start;
				
				long now = System.nanoTime();
				nsSpent += (now - start);
				
				if (now - lastTPS > 5000000000L) {
					int ticksDelta = ticks - lastTPSTick;
					log(ticksDelta / 5 + " TPS (" + (nsSpent / ticksDelta / 1000L) + " microseconds/tick)");
					nsSpent = 0;
					lastTPSTick = ticks;
					lastTPS = now;
				}
				
				Thread.sleep(Math.max(1L, MS_PER_TICK - (System.nanoTime() - start) / 1000000L));
			} catch (Throwable e) {
				logError("Error in main tick!", e);
				started = false;
				break;
			}
		}
		
		
		
		log("Shutting down server ...");
		
		controller.onServerEnde();
		
		kickAll("Server closed");
		
		if (serverSocket != null) {
			log("Closing server socket ...");
			try {
				serverSocket.close();
				clientAcceptThread.join();
			} catch (IOException e) {
				logError("Error closing server socket!", e);
			} catch (InterruptedException e) {
			}
		}
		
		log("Bye!");
	}
	
	/**
	 * Returns whether or not the main server thread has been closed.
	 */
	public boolean isThreadClosed() {
		return !notYetStarted && !mainServerThread.isAlive();
	}
	
	public boolean isRunning() {
		return started;
	}
	
	/**
	 * Asynchronously tell the server to shutdown.
	 */
	public void stop() {
		started = false;
	}
	
	
	/**
	 * Thread-safe method to connect a new client to the server.
	 * The client is validated before it is added as a player.
	 * 
	 * @param socket a {@link PotentialSocket} representing the connection to the client
	 */
	public void connectClient(PotentialSocket socket) {
		new Thread(new ClientValidatorTask(socket)).start();
	}
	
	public PotentialSocket createAISocket() {
		return new ArtificialSocket(dictionary);
	}
	
	private class ClientAcceptTask implements Runnable {
		@Override
		public void run() {
			while (isRunning()) {
				try {
					final Socket socket = serverSocket.accept();
					socket.setTcpNoDelay(true);
					socket.setSoTimeout(10000);
					
					log(socket.getInetAddress() + " connected");
					
					// validate client
					connectClient(new NetSocket(socket));
				} catch (SocketTimeoutException e) { // ignore and retry if still running
				} catch (SocketException e) {
					if (isRunning()) // exception didn't occur because socket was intentionally closed
						logError("Exception in client accepting task!", e);
				} catch (IOException e) {
					logError("Client could not initialize!", e);
				}
			}
			
			log("Client accepting task closed!");
		}
	}
	
	private class ClientValidatorTask implements Runnable {
		private final PotentialSocket socket;
		
		public ClientValidatorTask(PotentialSocket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try {
				socket.connect();
				
				// receive magic number
				long magicNumber = new DataInputStream(socket.getInputStream()).readLong();
				if (magicNumber != Packet.MAGIC_NUMBER) {
					logError("Validation of " + socket.getRepresentation() + " failed: incorrect magic number");
					socket.close();
					return;
				}
				
				log(socket.getRepresentation() + " validated, sending reply ...");
				
				// reply with magic number ACK
				new DataOutputStream(socket.getOutputStream()).writeLong(Packet.MAGIC_NUMBER_ACK);

				log("Connection with " + socket.getRepresentation() + " established!");
				
				Connection connection = new Connection(socket, dictionary);
				connection.startThreads();
				newClients.add(connection);
				
				Thread.sleep(2000L);
			} catch (IOException e) {
				logError("Validation of " + socket.getRepresentation() + " failed: IOException", e);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void log(String msg) {
		System.out.println("[SERVER] " + msg);
	}
	
	public static void logError(String msg) {
		System.err.println("[SERVER error] " + msg);
	}
	
	public static void logError(String msg, Throwable exception) {
		logError(msg);
		exception.printStackTrace();
	}
}
