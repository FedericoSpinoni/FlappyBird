package it.unicatt.flappybird;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import javafx.scene.transform.Scale;

public class FlappyBirdModel extends World {
	private List<IView> views;
	private Bird bird;
	private List<Pipe> pipes;
	
	public FlappyBirdModel() {
		views = new ArrayList<IView>();
		bird = new Bird();
		pipes = new ArrayList<Pipe>();
		
		initializeWorld();
	}
	
	private void initializeWorld() {
		Bird bird = new Bird();
		bird.addFixture(Geometry.createCircle(0.5));
		bird.setMass(MassType.NORMAL);
		this.addBody(bird);
		
		Ground ground = new Ground();
		ground.translate(new Vector2(0, - (Globals.HEIGHT/2)/Globals.SCALE + 0.5));
		ground.addFixture(Geometry.createRectangle(Globals.WIDHT/Globals.SCALE, 1));
		ground.setMass(MassType.INFINITE);
		this.addBody(ground);
		
		this.setGravity(EARTH_GRAVITY);
	}
	
	public boolean update(double elapsedTime) {
		super.update(elapsedTime);
		notifyViews();
		return true;
	}
	
	public void addView(IView view) {
		views.add(view);
	}
	
	public void notifyViews() {
		for (IView view : views) {
			view.update();
		}
	}
}
