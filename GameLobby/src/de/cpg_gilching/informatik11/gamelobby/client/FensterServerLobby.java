package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;


/**
 * Die View-Komponente der "Server-Lobby".
 */
public class FensterServerLobby {
	
	private BildschirmServerLobby serverLobby;
	private JFrame fenster;
	
	private JTextArea chatTextArea;
	private JScrollPane chatTextScroller;
	private JTextField chatTextField;
	private JPanel panelSpieler;
	private JComboBox gameDropdown;
	
	public FensterServerLobby(BildschirmServerLobby dieServerLobby) {
		this.serverLobby = dieServerLobby;
		
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				JPanel hauptPanel = new JPanel();
				hauptPanel.setLayout(null);
				hauptPanel.setPreferredSize(new Dimension(800, 600));
				
				panelSpieler = new JPanel();
				panelSpieler.setLayout(null);
				
				JButton trennenBtn = new JButton("Verbindung trennen");
				trennenBtn.setBounds(590, 550, 200, 40);
				trennenBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						serverLobby.verbindungTrennen();
					}
				});
				hauptPanel.add(trennenBtn);
				
				JScrollPane spielerScroller = new JScrollPane(panelSpieler);
				spielerScroller.setBounds(10, 10, 460, 400);
				hauptPanel.add(spielerScroller);
				
				JLabel gameDropdownLabel = new JLabel("Spiel :");
				gameDropdownLabel.setBounds(10, 420, 40, 25);
				hauptPanel.add(gameDropdownLabel);
				
				gameDropdown = new JComboBox();
				gameDropdown.setBounds(50, 420, 250, 25);
				hauptPanel.add(gameDropdown);
				
				JButton gameStartBtn = new JButton("Session starten");
				gameStartBtn.setBounds(270, 550, 200, 40);
				gameStartBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Session started");
					}
				});
				hauptPanel.add(gameStartBtn);
				
				
				chatTextArea = new JTextArea();
				chatTextArea.setEditable(false);
				chatTextArea.setLineWrap(true);
				chatTextArea.setMargin(new Insets(5, 5, 5, 5));
				
				chatTextScroller = new JScrollPane(chatTextArea);
				chatTextScroller.setBounds(490, 10, 300, 470);
				chatTextScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				hauptPanel.add(chatTextScroller);
				
				chatTextField = new JTextField();
				chatTextField.setBounds(490, 490, 210, 40);
				chatTextField.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("message sent");
					}
				});
				hauptPanel.add(chatTextField);
				
				JButton chatSendenBtn = new JButton("Senden");
				chatSendenBtn.setBounds(705, 490, 85, 40);
				chatSendenBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("message sent");
					}
				});
				hauptPanel.add(chatSendenBtn);
				
				
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
						serverLobby.verbindungTrennen();
					}
				});
			}
		});
	}
	
	public void chatNachrichtAnzeigen(String nachricht) {
		chatTextArea.append(nachricht);
		chatTextArea.append("\n");
		chatTextScroller.getVerticalScrollBar().setValue(chatTextScroller.getVerticalScrollBar().getMaximum());
	}
	
	public void spielerListeAktualisieren(Collection<SpielerZustand> spieler) {
		// unabhängige Liste der Spieler erstellen
		final Collection<SpielerZustand> alleSpieler = new ArrayList<SpielerZustand>(spieler);
		
		// auf Event-Thread ausführen
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				panelSpieler.removeAll();
				
				int hoehe = 5;
				for (SpielerZustand spieler : alleSpieler) {
					JPanel spielerPanel = spielerPanelErstellen(spieler, new Rectangle(5, hoehe, panelSpieler.getWidth() - 10, 25));
					
					panelSpieler.add(spielerPanel);
					
					hoehe += 30;
				}
				
				panelSpieler.revalidate();
				panelSpieler.repaint();
				
				panelSpieler.setPreferredSize(new Dimension(1, hoehe));
			}
		});
	}
	
	private JPanel spielerPanelErstellen(SpielerZustand spieler, Rectangle bounds) {
		JPanel p = new JPanel();
		p.setBounds(bounds);
		p.setBackground(new Color(200, 200, 200));
		p.setLayout(new BorderLayout());
		p.add(new JLabel(spieler.name), BorderLayout.WEST);
		p.add(new JButton("Auswählen"), BorderLayout.EAST);
		
		return p;
	}
	
	public void fensterSchliessen() {
		fenster.dispose();
	}
	
}
