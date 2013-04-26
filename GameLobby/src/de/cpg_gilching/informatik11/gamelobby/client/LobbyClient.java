package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;

/**
 * Hauptklasse des Clients; dient als Einstiegspunkt
 */
public class LobbyClient {
	
	private JTextField eingabefeldBenutzername;
	private JTextField eingabefeldIPAdresse;
	
	public LobbyClient() {
		
		JPanel hauptPanel = new JPanel();
		hauptPanel.setLayout(new BoxLayout(hauptPanel, BoxLayout.Y_AXIS));
		hauptPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		verbindungsScreenFüllen(hauptPanel);
		
		
		
		JFrame frame = new JFrame("Lobby-System Client");
		//		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(hauptPanel, BorderLayout.NORTH);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	private void verbindungsScreenFüllen(JPanel hauptScreen) {
		//		JPanel hauptScreen = new JPanel();
		
		// =========== BENUTZERNAME ===============
		Box usernameBox = Box.createHorizontalBox();
		usernameBox.add(new JLabel("Benutzername:"));
		usernameBox.add(Box.createHorizontalStrut(10));
		
		eingabefeldBenutzername = new JTextField("Spieler" + Helfer.zufallsZahl(1000, 10000));
		eingabefeldBenutzername.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				verbindungHerstellen();
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
				verbindungHerstellen();
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
				verbindungHerstellen();
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
	
	private void verbindungHerstellen() {
		String username = eingabefeldBenutzername.getText();
		String ipAdresse = eingabefeldIPAdresse.getText();
		
		System.out.println("Verbinde zu " + ipAdresse + " mit Namen " + username + " ...");
	}
	
}
