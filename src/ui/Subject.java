package ui;
import javax.swing.*;

import java.awt.*;
import java.awt.color.*;
public abstract class Subject extends JPanel {
	final static int ToUp = 11;
	final static int ToDown = 12;
	final static int ToLeft = 13;
	final static int ToRight = 14;
	final static int ToUpLeft = 15;
	final static int ToUpRight = 16;
	final static int ToDownLeft = 17;
	final static int ToDownRight = 18;
	private String name,feed;
	private int id,speed;
	private int lastDirection = 13;
	protected boolean isMove,isEating;
	protected boolean isReflected = false;
	private Point location;
	protected Runnable moveMotionThread;
	protected Runnable eatingMotionThread;
	Subject(String name, String feed,int speed,Dimension size) {
		this.name = name;
		this.feed = feed;
		this.speed = speed;
		this.setSize(size);
	}
	void move() {
		if (this.isMove || this.isEating) {
			System.out.println("이미 움직이는중!");
			return;
		} else {
			isMove = true;
		}
		MoveThread threadex = new MoveThread(this);
		Thread thread = new Thread(threadex);
		Thread motionThread = new Thread(moveMotionThread);
		thread.start();
		motionThread.start();
		this.isMove = true;
	}
	void eat() {
		if (this.isMove || this.isEating) {
			System.out.println("이미 움직이는중!");
			return;
		} else {
			isEating = true;
		}
		Thread thread = new Thread(new EatThread(this));
		Thread motionThread = new Thread(eatingMotionThread);
		thread.start();
		motionThread.start();
		
	};
	abstract void death();
	abstract void breeding();
	void setSpeed(int speed) {
		this.speed = speed;
	}
	
	class EatThread implements Runnable {
		Subject sub;
		EatThread(Subject sub) {
			this.sub = sub;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(3000);
				sub.isEating=false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class MoveThread implements Runnable {
		private Subject sub;
		private int direction,distance;
		private Dimension fieldSize = new Dimension(1650,1000);
		private int feet = 1;
		MoveThread(Subject sub) {
			this.sub = sub;
//			this.distance = (int)(Math.random()*100)+200;
//			this.direction = (int)(Math.random()*8)+11;
//			// 객체 이미지 반전
//			if ((direction>12)) {
//				if(sub.lastDirection%2 != direction%2) {
//					isReflected = !isReflected;
//				}
//				lastDirection = direction;
//			}
		}
		
		void toGo() {
			Point p = sub.getLocation();
			if (direction==ToUp) {
				sub.setLocation(p.x, p.y-feet);
				if(p.y < 0) {
					this.direction = ToDown;
					sub.isReflected = !sub.isReflected;
					sub.lastDirection++;
				}
			} else if (direction==ToDown) {
				sub.setLocation(p.x, p.y+feet);
				if(p.y > fieldSize.height-sub.getSize().height) {
					direction = ToUp;
					sub.isReflected = !sub.isReflected;
					sub.lastDirection++;
				}
			} else if (direction==ToLeft) {
				sub.setLocation(p.x-feet, p.y);
				if(p.x < 0) {
					direction = ToRight;
					sub.isReflected = !sub.isReflected;
					sub.lastDirection++;
				}
			} else if (direction==ToRight) {
				sub.setLocation(p.x+feet, p.y);
				if(p.x > fieldSize.width-sub.getSize().width) {
					direction = ToLeft;
					sub.isReflected = !sub.isReflected;
					sub.lastDirection++;
				}
			} else if (direction==ToUpLeft) {
				sub.setLocation(p.x-feet, p.y-feet);
				if(p.y < 0 || p.x < 0) {
					this.direction = ToDownRight;
					sub.isReflected = !sub.isReflected;
					sub.lastDirection++;
				}
			} else if (direction==ToUpRight) {
				sub.setLocation(p.x+feet, p.y-feet);
				if(p.y < 0 || p.x > fieldSize.width-sub.getSize().width) {
					this.direction = ToDownLeft;
					sub.isReflected = !sub.isReflected;
					sub.lastDirection++;
				}
			} else if (direction==ToDownLeft) {
				sub.setLocation(p.x-feet, p.y+feet);
				if(p.y > fieldSize.height-sub.getSize().height || p.x < 0) {
					this.direction = ToUpRight;
					sub.isReflected = !sub.isReflected;
					sub.lastDirection++;
				}
			} else if (direction==ToDownRight) {
				sub.setLocation(p.x+feet, p.y+feet);
				if(p.y > fieldSize.height-sub.getSize().height || p.x > fieldSize.width-sub.getSize().width) {
					this.direction = ToUpLeft;
					sub.isReflected = !sub.isReflected;
					sub.lastDirection++;
				}
			}
		}
		@Override
		public synchronized void run() {
			while(true) {
				if(sub.isMove) {
					this.distance = (int)(Math.random()*100)+200;
					this.direction = (int)(Math.random()*8)+11;
					// 객체 이미지 반전
					if ((direction>12)) {
						if(sub.lastDirection%2 != direction%2) {
							isReflected = !isReflected;
						}
						lastDirection = direction;
					}
					for(int i=1; i<distance+1; i++) {
						try {
							if(!sub.isMove) {
								break;
							}
							toGo();
							Thread.sleep(1000/speed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
//			sub.move();
//			System.out.println("Move Thread done");
			
//			return;
		}
	}
}

