
public class Cell {

	private boolean dead;
	private int intValue;
	private int w;
	private int h;
	public int age;
	private boolean infected;
	
	public Cell(boolean e, int x, int y) {
		this.w = x;
		this.h= y;
		this.dead = e;
		if(dead) {
			age = 0;
			intValue = 0;
		} else if(infected){
			age = 0;
			intValue = 2;
		} else {
			age = 0;
			intValue = 1;
		}
	}
	
	public int getIntValue() {
		return this.intValue;
	}
	public boolean isDead() {
		return dead;
	}
	
	public boolean isInfected() {
		return infected;
	}
	
	public boolean isAlive() {
		return !this.dead;
	}
	
	public int getAge() {
		return age;
	}
	
	public void infect() {
		age = 0;
		intValue = 2;
		this.infected = true;
	}
	
	public void defect() {
		this.infected = false;
		this.kill();
	}
	public void kill() {
		this.infected = false;
		age = 0;
		intValue = 0;
		this.dead = true;
	}
	
	public void spawn() {
		age = 0;
		intValue = 1;
		this.dead = false;
	}
	
	public void addAge() {
		this.age++;
	}
	
	public int getWidth() {
		return w;
	}
	
	public int getHeight() {
		return h;
	}
	
}
