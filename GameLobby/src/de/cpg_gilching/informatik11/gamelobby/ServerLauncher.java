package de.cpg_gilching.informatik11.gamelobby;

import de.cpg_gilching.informatik11.gamelobby.server.ServerMain;

public class ServerLauncher {
	
	public static void main(String[] args) {
		new ServerMain(80);
	}
	
}