package de.cpg_gilching.informatik11.gamelobby.client;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	public static final Sound musikServerLobby = new Sound("bgmusik.wav");
	public static final Sound genericFail = new Sound("fail.wav");
	public static final Sound genericWin = new Sound("win.wav");
	public static final Sound pongSchlaegerRechts = new Sound("pong_hit.wav");
	public static final Sound pongSchlaegerLinks = pongSchlaegerRechts;
	public static final Sound pongWall = new Sound("pong_wall.wav");
	public static final Sound pongScore = new Sound("pong_score.wav");
	public static final Sound snakeEat = new Sound("pong_hit.wav");
	public static final Sound snakeDeath = new Sound("snake_death.wav");
	public static final Sound rotmsBasic = new Sound("rotms_shot.wav");
	public static final Sound rotmsSpecial = new Sound("rotms_special.wav");
	public static final Sound rotmsDeath = new Sound("rotms_death.wav");
	public static final Sound osmosBounce = new Sound("pong_hit.wav");
	public static final Sound osmosAbsorbed = new Sound("osmos_absorbed.wav");
	public static final Sound keymadScore = new Sound("pong_score.wav");
	public static final Sound keymadFail = new Sound("keymad_fail.wav");
	public static final Sound tictactoeClick = new Sound("tictactoe_click.wav");
	public static final Sound tictactoeWin = new Sound("tictactoe_win.wav");
	public static final Sound tictactoeLose = new Sound("rotms_death.wav");
	
	public static Sound getByName(String name) {
		try {
			Field f = Sound.class.getField(name);
			if (f.getType() != Sound.class)
				throw new NoSuchFieldException(name);
			return (Sound) f.get(null);
		} catch (NoSuchFieldException e) {
			return null;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	private final String datei;
	private Clip clip;
	
	public Sound(String datei) {
		this.datei = datei;

		URL soundUrl = Sound.class.getResource("/sounds/" + datei);
		if (soundUrl == null) {
			File soundDatei = new File("sounds", datei);
			try {
				soundUrl = soundDatei.toURI().toURL();
			} catch (MalformedURLException e) {
				System.err.println("Konnte Sound nicht laden: " + datei);
				e.printStackTrace();
			}
		}
		
		if (soundUrl != null) {
			try {
				clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(soundUrl));
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void play(final boolean schleife) {
		if (clip.isRunning())
			clip.stop();

		if (!schleife) {
			clip.loop(0);
			clip.setFramePosition(0);
			clip.start();
		}
		else {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
		}
	}
	
	public void stop() {
		clip.stop();
	}
	
	public String getDatei() {
		return datei;
	}

}
