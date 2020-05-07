package it.unicatt.flappybird;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class FlappyBirdMain {

	private static void createWorld() {
		FlappyBirdModel model = new FlappyBirdModel();
		FlappyBirdView view = new FlappyBirdView(model);
		DefaultController controller = new DefaultController(model, view);
		
		model.addView(view);
		model.notifyViews();
		
		JFrame frame = new JFrame("FlappyBird");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		
		controller.start();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createWorld();
			}
		});
	}

}
