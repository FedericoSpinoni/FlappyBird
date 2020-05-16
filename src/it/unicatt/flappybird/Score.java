package it.unicatt.flappybird;

import org.dyn4j.dynamics.Body;

public class Score extends Body {
	private int score = 0;
	
	public void point() {
		this.score ++;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
