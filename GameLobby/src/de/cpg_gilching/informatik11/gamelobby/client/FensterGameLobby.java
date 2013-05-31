package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;
import javax.swing.border.LineBorder;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;


/**
 * Die View-Komponente der "Server-Lobby".
 */
public class FensterGameLobby {
	
	private BildschirmGameLobby gameLobby;
	
	private JFrame fenster;
	
	private JPanel panelGame;
	private JPanel panelSidebar;
	private JLabel labelSpielname;
	private JPanel panelSpieler;
	private JTextArea chatTextArea;
	private JScrollPane chatTextScroller;
	private JTextField chatTextField;
	
	public FensterGameLobby(BildschirmGameLobby lobby, final String spielBezeichnung, final SpielOberfläche spielView) {
		this.gameLobby = lobby;
		
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				JPanel hauptPanel = new JPanel();
				hauptPanel.setLayout(new BorderLayout());
				hauptPanel.setPreferredSize(new Dimension(880, 600));
				
				panelGame = new JPanel();
				panelGame.setLayout(null);
				panelGame.add(spielView);
				hauptPanel.add(panelGame, BorderLayout.CENTER);
				
				panelSidebar = new JPanel();
				panelSidebar.setBackground(Color.gray);
				panelSidebar.setPreferredSize(new Dimension(280, 1));
				panelSidebar.setLayout(null);
				hauptPanel.add(panelSidebar, BorderLayout.WEST);
				
				labelSpielname = new JLabel();
				labelSpielname.setText(spielBezeichnung);
				labelSpielname.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
				labelSpielname.setBounds(0, 0, 280, 30);
				labelSpielname.setHorizontalAlignment(JLabel.CENTER);
				panelSidebar.add(labelSpielname);
				
				panelSpieler = new JPanel();
				panelSpieler.setLayout(new BoxLayout(panelSpieler, BoxLayout.Y_AXIS));
				panelSpieler.setBounds(5, 40, 270, 210);
				panelSidebar.add(panelSpieler);
				
				JButton abbrechenBtn = new JButton("Abbrechen");
				abbrechenBtn.setBounds(5, 555, 270, 40);
				abbrechenBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						gameLobby.spielAbbrechen();
					}
				});
				panelSidebar.add(abbrechenBtn);
				
				
				chatTextArea = new JTextArea();
				chatTextArea.setEditable(false);
				chatTextArea.setLineWrap(true);
				chatTextArea.setMargin(new Insets(5, 5, 5, 5));
				
				chatTextScroller = new JScrollPane(chatTextArea);
				chatTextScroller.setBounds(5, 255, 270, 260);
				chatTextScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				panelSidebar.add(chatTextScroller);
				
				chatTextField = new JTextField();
				chatTextField.setBounds(5, 520, 270, 30);
				chatTextField.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						nachrichtAbsenden();
					}
				});
				panelSidebar.add(chatTextField);
				
				
				
				fenster = new JFrame("Spiel: " + spielBezeichnung);
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
						gameLobby.spielAbbrechen();
					}
				});
				
				spielView.requestFocusInWindow();
			}
		});
	}
	
	public void spielerListeAktualisieren(Collection<SpielerIngameZustand> spielerListe) {
		// unabhängige Listen erstellen
		final Collection<SpielerIngameZustand> alleSpieler = new ArrayList<SpielerIngameZustand>(spielerListe);
		
		// auf Event-Thread ausführen
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				panelSpieler.removeAll();
				
				panelSpieler.add(Box.createVerticalStrut(10));
				
				int platz = 1;
				for (SpielerIngameZustand zustand : alleSpieler) {
					JLabel nameLabel = new JLabel(platz + ". " + zustand.name);
					nameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
					
					JLabel punkteLabel = new JLabel(Integer.toString(zustand.punkte));
					punkteLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
					
					Box box = Box.createHorizontalBox();
					box.add(Box.createHorizontalStrut(10));
					box.add(nameLabel);
					box.add(Box.createHorizontalGlue());
					
					if (zustand.tempPunkte != Integer.MIN_VALUE) {
						JLabel tempPunkteLabel = new JLabel();
						tempPunkteLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

						if (zustand.tempPunkte > 0) {
							tempPunkteLabel.setText("+" + zustand.tempPunkte);
							tempPunkteLabel.setForeground(Color.green);
						}
						else if (zustand.tempPunkte == 0) {
							tempPunkteLabel.setText("+0");
							tempPunkteLabel.setForeground(Color.gray);
						}
						else {
							assert zustand.tempPunkte < 0;
							tempPunkteLabel.setText("" + zustand.tempPunkte);
							tempPunkteLabel.setForeground(Color.red);
							
						}
						
						box.add(tempPunkteLabel);
						box.add(Box.createHorizontalStrut(15));
					}

					box.add(punkteLabel);
					box.add(Box.createHorizontalStrut(10));
					
					panelSpieler.add(box);
					panelSpieler.add(Box.createVerticalStrut(10));
					
					platz++;
				}
				
				panelSpieler.revalidate();
				panelSpieler.repaint();
			}
		});
	}
	
	public void chatNachrichtAnzeigen(final String nachricht) {
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				chatTextArea.append(nachricht);
				chatTextArea.append("\n");
				chatTextScroller.getVerticalScrollBar().setValue(chatTextScroller.getVerticalScrollBar().getMaximum());
			}
		});
	}
	
	private void nachrichtAbsenden() {
		String nachricht = chatTextField.getText();
		chatTextField.setText("");
		gameLobby.chatNachrichtSenden(nachricht);
	}
	
	public void fensterSchliessen() {
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				fenster.dispose();
			}
		});
	}
	
}
