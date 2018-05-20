package animator;

public class Animation {
	private int ticks;
	private int tickCount = 0;
	private Runnable job;
	private Animation next;
	private Runnable callback = null;

	protected Animation(Runnable job, int ticks) {
		this.job = job;
		this.ticks = ticks;
	}
	protected void play() {
		job.run();
		tickCount++;
	}
	protected boolean isDone() {
		return ticks < tickCount;
	}
	protected void setCallback(Runnable callback){
		this.callback = callback;
	}
	protected Runnable getCallback(){
		return callback;
	}
	protected Animation getNext(){
		return next;
	}
	public void setNext(Animation next) {
		this.next = next;
	}
	public void resetTickCount() {
		tickCount = 0;
	}
}
