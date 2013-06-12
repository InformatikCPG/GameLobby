package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;


/**
 * Die View-Komponente der "Server-Lobby".
 */
public class FensterServerLobby implements Runnable {
	
	private BildschirmServerLobby serverLobby;
	private JFrame fenster;
	
	private JTextArea chatTextArea;
	private JScrollPane chatTextScroller;
	private JTextField chatTextField;
	private JPanel panelAlleSpieler;
	private JComboBox gameDropdown;
	private JLabel gameAusgewähltLabel;
	private JComboBox punktelimitDropdown;
	private JButton gameStartBtn;
	private JButton gameHelpBtn;
	
	public FensterServerLobby(BildschirmServerLobby dieServerLobby) {
		this.serverLobby = dieServerLobby;
		
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				JPanel hauptPanel = new PanelLobbyBg();
				hauptPanel.setLayout(null);
				hauptPanel.setPreferredSize(new Dimension(800, 600));
				
				panelAlleSpieler = new JPanel();
				panelAlleSpieler.setLayout(null);
				
				JButton trennenBtn = new JButton("Verbindung trennen");
				trennenBtn.setFocusable(false);
				trennenBtn.setBounds(590, 550, 200, 40);
				trennenBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						serverLobby.verbindungTrennen();
					}
				});
				hauptPanel.add(trennenBtn);
				
				
				JScrollPane spielerScroller = new JScrollPane(panelAlleSpieler);
				spielerScroller.setBounds(10, 10, 460, 400);
				hauptPanel.add(spielerScroller);
				
				
				JLabel gameDropdownLabel = new JLabel("Spiel :");
				gameDropdownLabel.setBounds(10, 420, 40, 25);
				hauptPanel.add(gameDropdownLabel);
				
				gameDropdown = new JComboBox();
				gameDropdown.setBounds(50, 420, 250, 25);
				gameDropdown.setBackground(Color.white);
				gameDropdown.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							Object ausgewählt = e.getItem();
							final SpielEintrag derEintrag = (ausgewählt instanceof SpielEintrag ? (SpielEintrag) ausgewählt : null);
							
							serverLobby.getClient().getScheduler().taskHinzufügen(new Runnable() {
								@Override
								public void run() {
									serverLobby.spielAusgewählt(derEintrag);
								}
							});
						}
					}
				});
				hauptPanel.add(gameDropdown);
				
				JLabel ausgewähltBeschriftung = new JLabel("Ausgewählte Spieler:", SwingConstants.RIGHT);
				ausgewähltBeschriftung.setBounds(10, 460, 140, 20);
				ausgewähltBeschriftung.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
				hauptPanel.add(ausgewähltBeschriftung);

				gameAusgewähltLabel = new JLabel();
				gameAusgewähltLabel.setBounds(160, 460, 100, 20);
				gameAusgewähltLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
				hauptPanel.add(gameAusgewähltLabel);
				
				//				JPanel ausgewähltAnzeige = new JPanel();
				//				ausgewähltAnzeige.setBounds(10, 460, 300, 20);
				//				ausgewähltAnzeige.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
				//				ausgewähltAnzeige.add(ausgewähltBeschriftung);
				//				ausgewähltAnzeige.add(gameAusgewähltLabel);
				//				hauptPanel.add(ausgewähltAnzeige);
				
				
				JLabel punktelimitBeschriftung = new JLabel("Punktelimit:", SwingConstants.RIGHT);
				punktelimitBeschriftung.setBounds(10, 490, 140, 30);
				punktelimitBeschriftung.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
				hauptPanel.add(punktelimitBeschriftung);
				
				punktelimitDropdown = new JComboBox();
				punktelimitDropdown.setBounds(160, 492, 80, 25);
				punktelimitDropdown.setBackground(Color.white);

				punktelimitDropdown.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							final int punktelimit = (Integer) e.getItem();
							serverLobby.getClient().getScheduler().taskHinzufügen(new Runnable() {
								@Override
								public void run() {
									serverLobby.punktelimitSetzen(punktelimit);
								}
							});
						}
					}
				});
				
				for (int limit : serverLobby.getPunktelimitAuswahl())
					punktelimitDropdown.addItem(limit);

				hauptPanel.add(punktelimitDropdown);

				gameStartBtn = new JButton("Session starten");
				gameStartBtn.setBounds(10, 540, 200, 40);
				gameStartBtn.addActionListener(new SynchronerListener(FensterServerLobby.this, serverLobby.getClient()));
				hauptPanel.add(gameStartBtn);
				
				gameHelpBtn = new JButton("Spielanleitung");
				gameHelpBtn.setBounds(220, 540, 150, 40);
				gameHelpBtn.addActionListener(new SynchronerListener(new Runnable() {
					@Override
					public void run() {
						serverLobby.spielanleitungAnzeigen();
					}
				}, serverLobby.getClient()));
				hauptPanel.add(gameHelpBtn);
				
				
				
				// Chat
				chatTextArea = new JTextArea();
				chatTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
				chatTextArea.setEditable(false);
				chatTextArea.setLineWrap(true);
				chatTextArea.setMargin(new Insets(5, 5, 5, 5));
				
				chatTextScroller = new JScrollPane(chatTextArea);
				chatTextScroller.setBounds(490, 10, 300, 480);
				chatTextScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				hauptPanel.add(chatTextScroller);
				
				chatTextField = new JTextField();
				chatTextField.setBounds(490, 500, 210, 30);
				chatTextField.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						nachrichtAbsenden();
					}
				});
				hauptPanel.add(chatTextField);
				
				JButton chatSendenBtn = new JButton("Senden");
				chatSendenBtn.setBounds(705, 500, 85, 30);
				chatSendenBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						nachrichtAbsenden();
					}
				});
				hauptPanel.add(chatSendenBtn);
				// Chat ENDE
				
				
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
	
	@Override
	public void run() {
		// wenn auf "Session starten" geklickt wurde (synchroner Task)
		serverLobby.sessionStartAnfragen();
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
	
	public void spielerListeAktualisieren(Collection<SpielerZustand> spieler) {
		// unabhängige Liste der Spieler erstellen
		final Collection<SpielerZustand> alleSpieler = new ArrayList<SpielerZustand>(spieler);
		
		// auf Event-Thread ausführen
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				panelAlleSpieler.removeAll();
				
				int hoehe = 5;
				for (SpielerZustand spieler : alleSpieler) {
					PanelSpielerEintrag spielerPanel = new PanelSpielerEintrag(spieler, serverLobby);
					spielerPanel.setBounds(5, hoehe, panelAlleSpieler.getWidth() - 10, spielerPanel.getPreferredSize().height);
					
					panelAlleSpieler.add(spielerPanel);
					
					hoehe += spielerPanel.getPreferredSize().height + 5;
				}
				
				panelAlleSpieler.revalidate();
				panelAlleSpieler.repaint();
				
				panelAlleSpieler.setPreferredSize(new Dimension(1, hoehe));
			}
		});
	}
	
	public void spielDropdownFüllen(final Object[] items) {
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				gameDropdown.setModel(new DefaultComboBoxModel(items));
			}
		});
	}
	
	public void spielFormularAktualisieren(final boolean istEtwasAusgewählt, final int anzahl, final int maximum, final boolean gültig) {
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				if (maximum < 0)
					gameAusgewähltLabel.setText(anzahl + "");
				else
					gameAusgewähltLabel.setText(anzahl + " / " + maximum);
				gameAusgewähltLabel.setForeground(gültig ? Color.black : Color.red);
				
				gameStartBtn.setEnabled(gültig);
				gameHelpBtn.setEnabled(istEtwasAusgewählt);
			}
		});
	}
	
	private void nachrichtAbsenden() {
		String nachricht = chatTextField.getText();
		chatTextField.setText("");
		
		// LAF abfangen
		if (nachricht.equalsIgnoreCase("!laf system")) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				SwingUtilities.updateComponentTreeUI(fenster);
				return;
			} catch (Exception ignored) {
			}
		}
		else if (nachricht.equalsIgnoreCase("!laf java")) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				SwingUtilities.updateComponentTreeUI(fenster);
				return;
			} catch (Exception ignored) {
			}
		}
		
		serverLobby.chatNachrichtSenden(nachricht);
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
