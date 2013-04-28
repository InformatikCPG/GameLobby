package de.cpg_gilching.informatik11.gamelobby;

import javax.swing.UnsupportedLookAndFeelException;

import de.cpg_gilching.informatik11.gamelobby.client.LoginFenster;

public class ClientLauncher {
	
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		//		try {
		//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		//		} catch (Exception ignored) {
		//		}
		
		
		LoginFenster loginFenster = new LoginFenster();
		
		if (args.length == 3) {
			loginFenster.verbindungHerstellen(args[0], Integer.parseInt(args[1]), args[2]);
		}
	}
	
}
