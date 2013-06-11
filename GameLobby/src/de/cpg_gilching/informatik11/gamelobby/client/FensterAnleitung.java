package de.cpg_gilching.informatik11.gamelobby.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import de.cpg_gilching.informatik11.gamelobby.shared.Helfer;

public class FensterAnleitung {
	
	private BildschirmAnleitung bildschirm;
	private JFrame fenster;
	
	public FensterAnleitung(BildschirmAnleitung bildschirm, final String anleitungTitel, final String anleitungText) {
		this.bildschirm = bildschirm;

		Helfer.alsSwingTask(new Runnable() {
			@Override
			public void run() {
				JTextPane textPane = new JTextPane();
				textPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
				textPane.setEditable(false);
				textPane.setHighlighter(null);
				textPane.setBackground(null);
				textPane.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
				textPane.setText(anleitungText);
				
				textPane.setSize(400, 100); // nur die Breite relevant, um preferredSize.height richtig zu setzen
				textPane.setPreferredSize(new Dimension(textPane.getWidth(), textPane.getPreferredSize().height));

				JPanel textContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
				textContainer.setBackground(null);
				textContainer.add(textPane);

				JLabel überschrift = new JLabel(anleitungTitel, SwingConstants.CENTER);
				überschrift.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
				überschrift.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

				fenster = new JFrame("Anleitung: " + anleitungTitel);
				fenster.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				fenster.setResizable(false);
				fenster.setLayout(new BorderLayout());
				fenster.add(textContainer);
				fenster.add(überschrift, BorderLayout.NORTH);
				fenster.pack();
				fenster.setLocationRelativeTo(null);
				fenster.setVisible(true);
				
				fenster.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						FensterAnleitung.this.bildschirm.schließen();
					}
				});
				
				fenster.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							FensterAnleitung.this.bildschirm.schließen();
						}
					}
				});
				
				fenster.requestFocusInWindow(); // damit der KeyListener funktioniert
			}
		});
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
