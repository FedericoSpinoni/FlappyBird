package it.unicatt.flappybird;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Shape;

public class FlappyBirdView extends JPanel implements IView {

	private FlappyBirdModel model;
	private double scale = 50;
	
	public FlappyBirdView(FlappyBirdModel model) {
		this.model = model;
		
		setPreferredSize(new Dimension(Globals.WIDHT, Globals.HEIGHT));
		setBackground(Color.CYAN);
		setDoubleBuffered(true);
	}
	
	protected void transform(Graphics2D g2d) {
		int widht = getWidth();
		int height = getHeight();
		AffineTransform transformation = new AffineTransform();
		
		transformation.concatenate(AffineTransform.getScaleInstance(1, -1));
		transformation.concatenate(AffineTransform.getTranslateInstance(widht/2, - height/2));
		g2d.transform(transformation);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		transform(g2d);
		
		for (Body body : model.getBodies()) {
			AffineTransform ot = g2d.getTransform();
			AffineTransform lt = new AffineTransform();
			
			lt.translate(body.getTransform().getTranslationX() * scale, body.getTransform().getTranslationY() * scale);
			g2d.transform(lt);
			
			Shape shape = body.getFixture(0).getShape();
			
			if (body.getClass() == Ground.class) {
				Graphics2DRenderer.render(g2d, shape, scale, Color.GREEN);
			}
			if (body.getClass() == Bird.class) {
				Graphics2DRenderer.render(g2d, shape, scale, Color.YELLOW);
			}
			
			g2d.setTransform(ot);
		}
	}
	
	@Override
	public void update() {
		repaint();
	}

}
