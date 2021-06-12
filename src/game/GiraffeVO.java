package game;

public class GiraffeVO {
	private int pk,id,neck,lastDirection,lastHeadDirection,hungry,breed,independence,x,y;
	private String birthDate;
	private boolean isMove,isEating,isBreeded,isReflected,isDetected,died;
	public int getPk() {
		return pk;
	}
	public void setPk(int pk) {
		this.pk = pk;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNeck() {
		return neck;
	}
	public void setNeck(int neck) {
		this.neck = neck;
	}
	public int getLastDirection() {
		return lastDirection;
	}
	public void setLastDirection(int lastDirection) {
		this.lastDirection = lastDirection;
	}
	public int getLastHeadDirection() {
		return lastHeadDirection;
	}
	public void setLastHeadDirection(int lastHeadDirection) {
		this.lastHeadDirection = lastHeadDirection;
	}
	public int getHungry() {
		return hungry;
	}
	public void setHungry(int hungry) {
		this.hungry = hungry;
	}
	public int getBreed() {
		return breed;
	}
	public void setBreed(int breed) {
		this.breed = breed;
	}
	public int getIndependence() {
		return independence;
	}
	public void setIndependence(int independence) {
		this.independence = independence;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public boolean isMove() {
		return isMove;
	}
	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}
	public boolean isEating() {
		return isEating;
	}
	public void setEating(boolean isEating) {
		this.isEating = isEating;
	}
	public boolean isBreeded() {
		return isBreeded;
	}
	public void setBreeded(boolean isBreeded) {
		this.isBreeded = isBreeded;
	}
	public boolean isReflected() {
		return isReflected;
	}
	public void setReflected(boolean isReflected) {
		this.isReflected = isReflected;
	}
	public boolean isDetected() {
		return isDetected;
	}
	public void setDetected(boolean isDetected) {
		this.isDetected = isDetected;
	}
	public boolean isDied() {
		return died;
	}
	public void setDied(boolean died) {
		this.died = died;
	}
	
}
