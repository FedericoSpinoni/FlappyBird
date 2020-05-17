package it.unicatt.flappybird;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JPanel;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Shape;

@SuppressWarnings("serial")
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
		
		Hashtable<Body, String> bodies = new Hashtable<Body, String>();
		
		for (Body body : model.getBodies()) {
			AffineTransform ot = g2d.getTransform();
			AffineTransform lt = new AffineTransform();
			
			lt.translate(body.getTransform().getTranslationX() * scale, body.getTransform().getTranslationY() * scale);
			g2d.transform(lt);
			
			if (body.getClass() == Score.class) {
				bodies.put(body, "score");
			} else if (body.getClass() == Title.class) {
				bodies.put(body, "title");
			} else {
				Shape shape = body.getFixture(0).getShape();
				
				if (body.getClass() == Ground.class) {
					Graphics2DRenderer.render(g2d, shape, scale, Color.GREEN);
				}
				if (body.getClass() == Bird.class) {
					Graphics2DRenderer.render(g2d, shape, scale, Color.YELLOW);
				}
				if (body.getClass() == Pipe.class) {
					Graphics2DRenderer.render(g2d, shape, scale, new Color(40, 170, 60));
				}
			}
			
			g2d.setTransform(ot);
		}
		
		bodies.forEach((k, v) -> { 
			if (v == "score") {
				scoreTransform(k, g2d);
			} else if (v == "title") {
				titleTransform(k, g2d);
			}
        }); 
		bodies.clear();
	}
	
	private void titleTransform(Body body, Graphics2D g2d) {
		AffineTransform ot = g2d.getTransform();
		AffineTransform tt = new AffineTransform();
		tt.translate(body.getTransform().getTranslationX() * scale, body.getTransform().getTranslationY() * scale);
		tt.concatenate(AffineTransform.getScaleInstance(1, -1));
		g2d.transform(tt);
		
		String text = "" + ((Title) body).getTitle();
		Stroke originalStroke = g2d.getStroke();
		g2d.setColor(Color.black);
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout tl = new TextLayout(text, new Font("MONOSPACED", Font.BOLD, 30), frc);
        java.awt.Shape shape = tl.getOutline(null);
        
		g2d.setStroke(new BasicStroke(4f));
        g2d.draw(shape);
        g2d.setColor(Color.white);
        g2d.fill(shape);
        g2d.setStroke(originalStroke);
        
        g2d.setTransform(ot);
	}

	private void scoreTransform(Body body, Graphics2D g2d) {
		AffineTransform ot = g2d.getTransform();
		AffineTransform tt = new AffineTransform();
		tt.translate(body.getTransform().getTranslationX() * scale, body.getTransform().getTranslationY() * scale);
		tt.concatenate(AffineTransform.getScaleInstance(1, -1));
		g2d.transform(tt);
		
		String text = "" + ((Score) body).getScore();
		Stroke originalStroke = g2d.getStroke();
		g2d.setColor(Color.black);
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout tl = new TextLayout(text, new Font("MONOSPACED", Font.BOLD, 50), frc);
        java.awt.Shape shape = tl.getOutline(null);
        
		g2d.setStroke(new BasicStroke(4f));
        g2d.draw(shape);
        g2d.setColor(Color.white);
        g2d.fill(shape);
        g2d.setStroke(originalStroke);
        
        g2d.setTransform(ot);
	}

	@Override
	public void update() {
		repaint();
	}

}
