package de.cpg_gilching.informatik11.gamelobby.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;

/**
 * Ein Thread, der auf Eingaben von der Konsole wartet. Da er als Daemon-Thread deklariert ist, verhindert er nicht das Programmende.
 */
public class ServerConsoleReader extends Thread {
	
	private final ServerMain server;
	private List<String> pendingCommands = Collections.synchronizedList(new LinkedList<String>());
	
	public ServerConsoleReader(ServerMain server) {
		this.server = server;
		setDaemon(true);
		setName("Console-Reader-Thread");
	}
	
	@Override
	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				pendingCommands.add(line.trim());
			}
			
			ServerMain.log("Console input EOF detected!");
		} catch (IOException e) {
			ServerMain.logError("Console input threw an exception!", e);
		}
	}
	
	public void processCommands() {
		while (!pendingCommands.isEmpty()) {
			String cmd = pendingCommands.remove(0);
			List<String> tokens = Helfer.tokenize(cmd, "\\s", false);
			
			server.onCommand(tokens.remove(0), tokens);
		}
	}

}
