package it.unicatt.flappybird;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

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
				model.update(1.0/Globals.FPS);
			}
		});
	}
	
	public void start() {
		timer.start();
	}
}
