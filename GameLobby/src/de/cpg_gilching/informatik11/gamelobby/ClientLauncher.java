package de.cpg_gilching.informatik11.gamelobby;

import javax.swing.UnsupportedLookAndFeelException;

import de.cpg_gilching.informatik11.gamelobby.client.FensterLogin;

public class ClientLauncher {
	
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		//		try {
		//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		//		} catch (Exception ignored) {
		//		}
		
		
		FensterLogin fensterLogin = new FensterLogin();
		
		if (args.length == 3) {
			fensterLogin.verbindungHerstellen(args[0], Integer.parseInt(args[1]), args[2]);
		}
	}
	
}
