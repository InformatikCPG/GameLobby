package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import javax.swing.DefaultComboBoxModel;
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
	private JPanel panelAlleSpieler;
	private JComboBox gameDropdown;
	private JLabel gameAusgewähltLabel;
	private JButton gameStartBtn;
	
	public FensterServerLobby(BildschirmServerLobby dieServerLobby) {
		this.serverLobby = dieServerLobby;
		
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				JPanel hauptPanel = new JPanel();
				hauptPanel.setLayout(null);
				hauptPanel.setPreferredSize(new Dimension(800, 600));
				
				panelAlleSpieler = new JPanel();
				panelAlleSpieler.setLayout(null);
				
				JButton trennenBtn = new JButton("Verbindung trennen");
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
				
				JLabel ausgewähltBeschriftung = new JLabel("Ausgewählte Spieler:  ");
				ausgewähltBeschriftung.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
				gameAusgewähltLabel = new JLabel();
				gameAusgewähltLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
				
				JPanel ausgewähltAnzeige = new JPanel();
				ausgewähltAnzeige.setBounds(10, 460, 300, 30);
				ausgewähltAnzeige.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
				ausgewähltAnzeige.add(ausgewähltBeschriftung);
				ausgewähltAnzeige.add(gameAusgewähltLabel);
				hauptPanel.add(ausgewähltAnzeige);
				
				gameStartBtn = new JButton("Session starten");
				gameStartBtn.setBounds(10, 500, 200, 40);
				gameStartBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Session started");
					}
				});
				hauptPanel.add(gameStartBtn);
				
				
				
				// Chat
				chatTextArea = new JTextArea();
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
	
	public void spielFormularAktualisieren(final int anzahl, final int maximum, final boolean gültig) {
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				if (maximum < 0)
					gameAusgewähltLabel.setText(anzahl + "");
				else
					gameAusgewähltLabel.setText(anzahl + " / " + maximum);
				gameAusgewähltLabel.setForeground(gültig ? Color.black : Color.red);
				
				gameStartBtn.setEnabled(gültig);
			}
		});
	}
	
	private void nachrichtAbsenden() {
		String nachricht = chatTextField.getText();
		chatTextField.setText("");
		
		serverLobby.chatNachrichtSenden(nachricht);
	}
	
	public void fensterSchliessen() {
		fenster.dispose();
	}
	
}
