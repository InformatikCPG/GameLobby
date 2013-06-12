package de.cpg_gilching.informatik11.gamelobby;

import java.util.Arrays;

import de.cpg_gilching.informatik11.gamelobby.server.ServerMain;

/**
 * Der Einstiegspunkt des Servers.
 * <p>
 * Start-Argumente:
 * </p>
 * <ol>
 * <li>[port]: der Port, auf dem der Server gestartet werden soll; Standard ist 80</li>
 * <li>"bots": wenn gegeben, werden nach Start automatisch 3 Bots hinzugefügt</li>
 * </ol>
 */
public class ServerLauncher {
	
	public static void main(String[] args) {
		ServerMain serverMain = new ServerMain(args.length > 0 ? Integer.parseInt(args[0]) : 80);
		
		if (Arrays.asList(args).contains("bots")) {
			serverMain.connectClient(serverMain.createAISocket());
			serverMain.connectClient(serverMain.createAISocket());
			serverMain.connectClient(serverMain.createAISocket());
		}
	}
	
}
