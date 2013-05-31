package de.cpg_gilching.informatik11.gamelobby;

import java.util.Arrays;

import de.cpg_gilching.informatik11.gamelobby.server.ServerMain;

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
