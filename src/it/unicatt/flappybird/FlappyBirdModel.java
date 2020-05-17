package it.unicatt.flappybird;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactAdapter;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class FlappyBirdModel extends World {
	private List<IView> views;
	private Bird bird;
	private Ground ground;
	private Sky sky;
	private List<Pipe> pipes;
	private Score score;
	private Title title;
	
	private int timeUpdate;
	
	enum GamePhase {
		READY,
		RUNNING,
		GAMEOVER
	}
	
	public GamePhase currentPhase;
	
	enum GameEvent {
		HIT,
		POINT,
		WING
	}
	
	public List<GameEvent> currentEvents;
	
	public FlappyBirdModel() {
		views = new ArrayList<IView>();
		bird = new Bird();
		pipes = new ArrayList<Pipe>();
		score = new Score();
		ground = new Ground();
		sky = new Sky();
		title = new Title();
		
		initializeWorld();
	}
	
	private void initializeWorld() {
		currentPhase = GamePhase.READY;
		currentEvents = new ArrayList<GameEvent>();
		
		score.translate(new Vector2(-0.25, 5));
		this.addBody(score);
		
		title.translate(new Vector2(-3.55, -2));
		this.addBody(title);
		
		bird.addFixture(Geometry.createCircle(0.5));
		bird.setMass(MassType.NORMAL);
		this.addBody(bird);
		
		ground.translate(new Vector2(0, - (Globals.HEIGHT/2)/Globals.SCALE + 0.5));
		ground.addFixture(Geometry.createRectangle(Globals.WIDHT/Globals.SCALE, 1));
		ground.setMass(MassType.INFINITE);
		this.addBody(ground);
		
   		sky.translate(new Vector2(0, (Globals.HEIGHT/2)/Globals.SCALE));
		sky.addFixture(Geometry.createRectangle(Globals.WIDHT/Globals.SCALE, 0.1));
		sky.setMass(MassType.INFINITE);
		this.addBody(sky);
		
		this.setGravity(EARTH_GRAVITY);
		
		this.addListener(new ContactAdapter() {
			
			@Override
			public boolean begin(ContactPoint point) {
				if (bird == point.getBody1() && ground == point.getBody2() || bird == point.getBody1() && pipes.contains(point.getBody2()) || bird == point.getBody1() && sky == point.getBody2()) {
					currentEvents.add(GameEvent.HIT);
					currentPhase = GamePhase.GAMEOVER;
				}
				return true;
			}
		});
	}
	
	private void addPipe() {
		Random rand = new Random();
		int x = rand.nextInt(6) + 2;
		Pipe pipeUp = new Pipe();
		Pipe pipeDown = new Pipe();
		
		pipeUp.addFixture(Geometry.createRectangle(2, x));
		pipeDown.addFixture(Geometry.createRectangle(2, 9 - x));
		
		pipeUp.setMass(MassType.INFINITE);
		pipeDown.setMass(MassType.INFINITE);
		
		double top = ((Globals.HEIGHT/2)/Globals.SCALE);
		double down = -((Globals.HEIGHT/2)/Globals.SCALE);
  		double h_top = (double) x/2;
		double h_down = (double) (9 - x)/2;
		
		pipeUp.translate(new Vector2((Globals.WIDHT/2)/Globals.SCALE + 1.5, top - h_top));
		pipeDown.translate(new Vector2((Globals.WIDHT/2)/Globals.SCALE + 1.5, down + h_down + 1));
		
		this.pipes.add(pipeUp);
		this.pipes.add(pipeDown);
		
		this.addBody(pipeUp);
		this.addBody(pipeDown);
	}

	public void ready() {
		currentPhase = GamePhase.READY;
		this.addBody(title);
		notifyViews();
	}
	
	public void startGame() {
		this.removeBody(title);
		score.setScore(0);
		bird.translateToOrigin();
		for (Pipe p : pipes) {
			this.removeBody(p);
		}
		pipes.clear();
		currentPhase = GamePhase.RUNNING;
	}
	
	public void jump() {
		currentEvents.add(GameEvent.WING);
		bird.setLinearVelocity(0, 5);
	}
	
	private void generatePipe(double t) {
		int tmp = (int) (t * 100);
		timeUpdate += tmp;
		if ((timeUpdate % (tmp * 120)) == 0) {
			addPipe();
		}
	}
	
	private void deletePipe() {
		double endLine = - ((Globals.WIDHT/2)/Globals.SCALE) - 1;
		List<Pipe> deletePipes = new ArrayList<Pipe>();
		for (Pipe p : pipes) {
  			if (p.getWorldCenter().x <= endLine) {
 				this.removeBody(p);
 				deletePipes.add(p);
			}
		}
		for (Pipe p : deletePipes) {
			pipes.remove(p);
		}
	}
	
	private void movePipe() {
		for (Pipe p : pipes) {
			p.translate(new Vector2(p.getLocalCenter().x - 0.05, p.getLocalCenter().y));
		}
	}
	
	private void addPoint() {
		for (Pipe p : pipes) {
			if (p.getTransform().getTranslationX() <= -1 && p.getTransform().getTranslationY() >= 0) {
				if (!p.getPassed()) {
					p.setPassed(true);
					score.point();
					currentEvents.add(GameEvent.POINT);
				}
			}
		}
	}
	
	public boolean update(double elapsedTime) {
		super.update(elapsedTime);
		addPoint();
		deletePipe();
		generatePipe(elapsedTime);
		movePipe();
		notifyViews();
		this.currentEvents.clear();
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
