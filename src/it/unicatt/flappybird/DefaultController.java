package it.unicatt.flappybird;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import it.unicatt.flappybird.FlappyBirdModel.GamePhase;

public class DefaultController {
	private FlappyBirdModel model;
	private FlappyBirdView view;
	
	private Timer timer;
	
	public DefaultController(FlappyBirdModel model, FlappyBirdView view) {
		this.model = model;
		this.view = view;
		
		this.timer = new Timer(1000/Globals.FPS, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.currentPhase == GamePhase.RUNNING) {
					model.update(1.0/Globals.FPS);
					DefaultController.this.view.requestFocus();
				} else if (model.currentPhase == GamePhase.GAMEOVER) {
					timer.stop();
				}
			}
		});
		
    	this.view.addKeyListener(new KeyListener() {
			
    		private boolean actived = false;
    		
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(timer.isRunning()) {
					if (e.getKeyChar() == ' ' && !actived) {
						actived = true;
						DefaultController.this.model.jump();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				actived = false;
			}
		});
	}
	
	public void start() {
		timer.start();
	}
}
