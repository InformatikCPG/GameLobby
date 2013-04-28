package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * Die View-Komponente der "Server-Lobby".
 */
public class LobbyServerFenster {
	
	private ControllerClient client;
	private JFrame fenster;
	
	public LobbyServerFenster(ControllerClient clientController) {
		this.client = clientController;
		
		JPanel hauptPanel = new JPanel();
		hauptPanel.setLayout(null);
		hauptPanel.setPreferredSize(new Dimension(800, 600));
		
		JButton einladenBtn = new JButton("Hallo Welt");
		einladenBtn.setBounds(190, 250, 200, 40);
		hauptPanel.add(einladenBtn);
		
		JButton trennenBtn = new JButton("Verbindung trennen");
		trennenBtn.setBounds(590, 550, 200, 40);
		hauptPanel.add(trennenBtn);
		
		fenster = new JFrame("Server-Lobby");
		fenster.setResizable(false);
		fenster.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		fenster.setLayout(new BorderLayout());
		fenster.add(hauptPanel, BorderLayout.NORTH);
		fenster.pack();
		fenster.setLocationRelativeTo(null);
		fenster.setVisible(true);
		
		fenster.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				client.verbindungTrennen();
			}
		});
	}
	
	public void fensterSchliessen() {
		fenster.dispose();
	}
	
}
