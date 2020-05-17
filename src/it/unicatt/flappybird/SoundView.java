package it.unicatt.flappybird;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import it.unicatt.flappybird.FlappyBirdModel.GameEvent;

public class SoundView implements IView {

	private FlappyBirdModel model;
	private Clip hitClip;
	private Clip pointClip;
	private Clip wingClip;
	
	public SoundView(FlappyBirdModel model) {
		this.model = model;
		this.hitClip = this.loadClip("res/hit.wav");
		this.pointClip = this.loadClip("res/point.wav");
		this.wingClip = this.loadClip("res/wing.wav");
	}
	
	private Clip loadClip(String nomeFile) {
		URL soundURL = this.getClass().getResource(nomeFile);
		Clip clip = null;
		AudioInputStream audioStream;
		try {
			audioStream = AudioSystem.getAudioInputStream(soundURL);
			clip = AudioSystem.getClip();
			clip.open(audioStream);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (LineUnavailableException e) {
			System.err.println(e.getMessage());
		}
		return clip;

	}
	
	@Override
	public void update() {
		for (GameEvent e : model.currentEvents) {
			Clip clip = null;
			switch(e) {
			case POINT:
				clip = pointClip;
				break;
			case HIT:
				clip = hitClip;
				break;
			case WING:
				clip = wingClip;
				break;
			}
			
			if (clip != null) {
				clip.setFramePosition(0);
				clip.start();
			}
		}
	}

}
