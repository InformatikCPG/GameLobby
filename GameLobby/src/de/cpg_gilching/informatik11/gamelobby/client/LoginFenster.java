package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.cpg_gilching.informatik11.gamelobby.shared.AdapterPaketLexikon;
import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;
import de.cpg_gilching.informatik11.gamelobby.shared.net.Connection;
import de.cpg_gilching.informatik11.gamelobby.shared.net.NetSocket;

/**
 * Das Fenster des Clients, das den Login-Bildschirm enthält.
 * Stellt die Verbindung zum Server her und startet bei Erfolg den {@link ControllerClient}.
 */
public class LoginFenster {
	
	private JFrame fenster;
	private JTextField eingabefeldBenutzername;
	private JTextField eingabefeldIPAdresse;
	
	public LoginFenster() {
		
		JPanel hauptPanel = new JPanel();
		hauptPanel.setLayout(new BoxLayout(hauptPanel, BoxLayout.Y_AXIS));
		hauptPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		loginScreenFüllen(hauptPanel);
		
		
		
		fenster = new JFrame("Lobby-System Client");
		//		frame.setResizable(false);
		fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenster.setLayout(new BorderLayout());
		fenster.add(hauptPanel, BorderLayout.NORTH);
		fenster.pack();
		fenster.setLocationRelativeTo(null);
		fenster.setVisible(true);
		
	}
	
	private void loginScreenFüllen(JPanel hauptScreen) {
		//		JPanel hauptScreen = new JPanel();
		
		// =========== BENUTZERNAME ===============
		Box usernameBox = Box.createHorizontalBox();
		usernameBox.add(new JLabel("Benutzername:"));
		usernameBox.add(Box.createHorizontalStrut(10));
		
		eingabefeldBenutzername = new JTextField("Spieler" + Helfer.zufallsZahl(1000, 10000));
		eingabefeldBenutzername.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginAbschicken();
			}
		});
		
		usernameBox.add(eingabefeldBenutzername);
		// ========= BENUTZERNAME end =============
		
		// =========== IP-Adresse ===============
		Box ipBox = Box.createHorizontalBox();
		ipBox.add(new JLabel("IP-Adresse:"));
		ipBox.add(Box.createHorizontalStrut(10));
		
		eingabefeldIPAdresse = new JTextField(25);
		eingabefeldIPAdresse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginAbschicken();
			}
		});
		
		ipBox.add(eingabefeldIPAdresse);
		// ========= IP-Adresse end =============
		
		
		// =========== Verbinden =================
		Box submitBox = Box.createHorizontalBox();
		
		JButton verbindenBtn = new JButton("Verbinden");
		verbindenBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginAbschicken();
			}
		});
		
		submitBox.add(Box.createHorizontalGlue());
		submitBox.add(verbindenBtn);
		submitBox.add(Box.createHorizontalGlue());
		// ======== Verbinden end ================
		
		
		
		usernameBox.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		ipBox.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		submitBox.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		
		hauptScreen.add(usernameBox);
		hauptScreen.add(Box.createVerticalStrut(10));
		hauptScreen.add(ipBox);
		hauptScreen.add(Box.createVerticalStrut(10));
		hauptScreen.add(submitBox);
		
		//		hauptScreen.add(hauptScreen, BorderLayout.CENTER);
	}
	
	private void loginAbschicken() {
		String username = eingabefeldBenutzername.getText().trim();
		String ipAdresse = eingabefeldIPAdresse.getText().trim();
		
		if (username.isEmpty()) {
			Helfer.meldungAnzeigen("Du musst einen Benutzernamen eingeben!", true);
		}
		else if (ipAdresse.isEmpty()) {
			Helfer.meldungAnzeigen("Du musst eine IP-Adresse eingeben!", true);
		}
		else {
			// eingegebene IP in Teile zerlegen (um den Port nach dem : einzeln herauszufinden
			StringTokenizer ipTeile = new StringTokenizer(ipAdresse, ":");
			String ip = ipTeile.nextToken();
			int port = 80; // Port 80 ist Standard
			if (ipTeile.hasMoreTokens()) {
				try {
					port = Integer.parseInt(ipTeile.nextToken());
					if (port < 0)
						throw new NumberFormatException();
				} catch (NumberFormatException e) {
					Helfer.meldungAnzeigen("Dieser Port ist nicht gültig", true);
					return;
				}
			}
			
			verbindungHerstellen(ip, port, username);
		}
		
	}
	
	public void verbindungHerstellen(String adresse, int port, String username) {
		System.out.println("Verbinde zu " + adresse + " auf Port " + port + " mit Namen " + username + " ...");
		
		try {
			NetSocket socket = new NetSocket(Inet4Address.getByName(adresse), port);
			socket.connect();
			
			AdapterPaketLexikon lexikonAdapter = new AdapterPaketLexikon();
			Connection verbindung = new Connection(socket, lexikonAdapter);
			
			fenster.dispose();
			new ControllerClient(verbindung, username, lexikonAdapter);
		} catch (IOException e) {
			Helfer.meldungAnzeigen("Es trat ein Fehler beim Herstellen der Verbindung auf!\n" + e.toString(), true);
			return;
		}
	}
	
}
