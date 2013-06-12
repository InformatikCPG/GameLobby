package de.cpg_gilching.informatik11.gamelobby;

import javax.swing.UnsupportedLookAndFeelException;

import de.cpg_gilching.informatik11.gamelobby.client.FensterLogin;

/**
 * Der Einstiegspunkt des Clients.
 * Optional kann das Programm mit 3 Argumenten gestartet werden, um automatisch zu einem Server zu verbinden:
 * 
 * <pre>
 * [adresse] [port] [benutzername]
 * </pre>
 */
public class ClientLauncher {
	
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		FensterLogin fensterLogin = new FensterLogin();
		
		// automatisches Verbinden
		if (args.length == 3) {
			fensterLogin.verbindungHerstellen(args[0], Integer.parseInt(args[1]), args[2]);
		}
	}
	
}
