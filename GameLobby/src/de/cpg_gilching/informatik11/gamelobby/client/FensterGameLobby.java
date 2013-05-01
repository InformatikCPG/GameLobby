package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
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
public class FensterGameLobby {
	
	private JFrame fenster;
	
	private JPanel panelGame;
	private JPanel panelSidebar;
	private JLabel labelSpielname;
	private JPanel panelSpieler;
	private JTextArea chatTextArea;
	private JScrollPane chatTextScroller;
	private JTextField chatTextField;
	
	
	public FensterGameLobby() {
		
		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				JPanel hauptPanel = new JPanel();
				hauptPanel.setLayout(new BorderLayout());
				hauptPanel.setPreferredSize(new Dimension(880, 600));
				
				panelGame = new JPanel();
				panelGame.setBackground(Color.black);
				panelGame.setLayout(null);
				hauptPanel.add(panelGame, BorderLayout.CENTER);
				
				panelSidebar = new JPanel();
				panelSidebar.setBackground(Color.gray);
				panelSidebar.setPreferredSize(new Dimension(280, 1));
				panelSidebar.setLayout(null);
				hauptPanel.add(panelSidebar, BorderLayout.WEST);
				
				labelSpielname = new JLabel();
				labelSpielname.setText("Spiel Nr. 1337");
				labelSpielname.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
				labelSpielname.setBounds(0, 0, 280, 30);
				labelSpielname.setHorizontalAlignment(JLabel.CENTER);
				panelSidebar.add(labelSpielname);
				
				panelSpieler = new JPanel();
				panelSpieler.setBackground(Color.blue);
				panelSpieler.setBounds(5, 40, 270, 210);
				panelSidebar.add(panelSpieler);
				
				JButton abbrechenBtn = new JButton("Abbrechen");
				abbrechenBtn.setBounds(5, 555, 270, 40);
				abbrechenBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("abgebrochen");
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
						System.out.println("Message sent");
					}
				});
				panelSidebar.add(chatTextField);
				
				
				
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
						fenster.dispose();
					}
				});
			}
		});
	}
	
	public void fensterSchliessen() {
		fenster.dispose();
	}
	
}
