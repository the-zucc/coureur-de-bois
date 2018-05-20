package animator;

import java.util.ArrayList;

public class Animator {
	private Animation currentAnimation;
	public Animator animate(Runnable function, int ticks){
		currentAnimation = new Animation(function,ticks); 
		animations.add(currentAnimation);
		return this;
	}
	public Animator then(Runnable function, int ticks) {
		Animation nextAnimation = new Animation(function,ticks);
		currentAnimation.setNext(nextAnimation);
		currentAnimation = nextAnimation;
		return this;
	}
	public void loop() {
		if(currentAnimation != null) {
			currentAnimation.setNext(currentAnimation);
		}
	}
	private ArrayList<Animation> animations = new ArrayList<Animation>();
	public void playAnimations() {
		for (int i = 0; i < animations.size(); i++) {
			Animation tmp = animations.get(i);
			tmp.play();
			if(tmp.isDone()) {
				animations.remove(i);
				if(tmp.getCallback() != null){
					tmp.getCallback().run();
				}
				if(tmp.getNext() != null) {
					if(tmp.getNext() == tmp) {
						tmp.resetTickCount();
					}
					animations.add(tmp.getNext());
				}
				i--;
			}
		}
	}

    public Animator done(Runnable callback) {
		currentAnimation.setCallback(callback);
		return this;
    }
}
