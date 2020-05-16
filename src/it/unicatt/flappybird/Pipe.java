package it.unicatt.flappybird;

import org.dyn4j.dynamics.Body;

public class Pipe extends Body {

	private boolean passed = false;

	public boolean getPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}
}
