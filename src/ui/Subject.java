package ui;
import javax.swing.*;

import libs.utils;

import java.awt.*;
import java.awt.color.*;
public abstract class Subject extends JPanel {
	final static int ToStop = 10;
	final static int ToUp = 11;
	final static int ToUpLeft = 12;
	final static int ToLeft = 13;
	final static int ToDownLeft = 14;
	final static int ToDown = 15;
	final static int ToDownRight = 16;
	final static int ToRight = 17;
	final static int ToUpRight = 18;
	
	private static int searchWidth = 600;
	private static int searchHeight = 200;

	private int id;
	private int lastDirection = 13;
	private int lastHeadDirection = 13;
	
	protected String name;
	protected String feed;
	protected int speed = 1;
	protected Dimension size;
	
	protected boolean isMove,isEating;
	protected boolean isReflected = false;
	protected boolean isDetected = false;
	private Point location;
	protected Runnable moveMotionThread;
	protected Runnable eatingMotionThread;
	Subject(String name, String feed,int speed,Dimension size) {
		this.name = name;
		this.feed = feed;
		this.speed = speed;
		this.size = size;
		this.setBackground(null);
		this.setOpaque(false);
		this.setVisible(true);
		this.setSize(size);
	}
	Point getCenterPoint() {
		Point l = this.getLocation();
		return new Point((int)l.getX()+this.size.width/2,(int)l.getY()+this.size.height/2);
	}
	void move() {
		if (this.isMove || this.isEating) {
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
		if (this.isEating) {
			return;
		} else {
			isEating = true;
		}
		System.out.println("Eat Start");
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
		}
		int getDirection(int lastDirection) {
			int[] dirs = utils.getDetectableRange(lastDirection,2);
			int[] probs = {12,29,71,88,100}; // 확률
			int num = (int) (Math.random()*100+1);
			// 40/35/25  12/29/71/88/100
			for(int i=0; i<dirs.length; i++) {
				if (num<=probs[i]) {
//					System.out.println(dirs[i]);
					return dirs[i];
				}
			}
			return dirs[dirs.length-1];
		}
		void setDirection() {
			this.distance = (int)(Math.random()*100)+200;
			// 마지막 방향의 시야 범위 찾기
			this.direction = getDirection(sub.lastDirection);
			// 객체 이미지 반전
			if ((this.direction!=Subject.ToUp)&&(this.direction!=Subject.ToDown)) {
				if((this.direction<Subject.ToDown) && (sub.lastHeadDirection==Subject.ToRight)) {
					sub.lastHeadDirection = Subject.ToLeft;			
					sub.isReflected = !sub.isReflected;
				} else if((this.direction>Subject.ToDown) && (sub.lastHeadDirection==Subject.ToLeft)) {
					sub.lastHeadDirection = Subject.ToRight;			
					sub.isReflected = !sub.isReflected;
				}
			}
			sub.lastDirection = this.direction;
		}
		void reverseDirection(int d) {
			this.direction = d;
			sub.lastDirection = d;
			if (!((d==Subject.ToUp) || (d==Subject.ToUp))) {
				sub.isReflected = !sub.isReflected;
				if(sub.lastHeadDirection == Subject.ToLeft) {
					sub.lastHeadDirection = Subject.ToRight;
				} else {
					sub.lastHeadDirection = Subject.ToLeft;
				}
			}
		}
		void goWithCheckLimit() {
			Point p = sub.getLocation();		
 			if (direction==ToUp) {
				sub.setLocation(p.x, p.y-feet);
				if(p.y < 0) {
					reverseDirection(ToDown);
				}
			} else if (direction==ToDown) {
				sub.setLocation(p.x, p.y+feet);
				if(p.y > fieldSize.height-sub.getSize().height) {
					reverseDirection(ToUp);
				}
			} else if (direction==ToLeft) {
				sub.setLocation(p.x-feet, p.y);
				if(p.x < 0) {
					reverseDirection(ToRight);
				}
			} else if (direction==ToRight) {
				sub.setLocation(p.x+feet, p.y);
				if(p.x > fieldSize.width-sub.getSize().width) {
					reverseDirection(ToLeft);
				}
			} else if (direction==ToUpLeft) {
				sub.setLocation(p.x-feet, p.y-feet);
				if(p.y < 0 || p.x < 0) {
					reverseDirection(ToDownRight);
				}
			} else if (direction==ToUpRight) {
				sub.setLocation(p.x+feet, p.y-feet);
				if(p.y < 0 || p.x > fieldSize.width-sub.getSize().width) {
					reverseDirection(ToDownLeft);
				}
			} else if (direction==ToDownLeft) {
				sub.setLocation(p.x-feet, p.y+feet);
				if(p.y > fieldSize.height-sub.getSize().height || p.x < 0) {
					reverseDirection(ToUpRight);
				}
			} else if (direction==ToDownRight) {
				sub.setLocation(p.x+feet, p.y+feet);
				if(p.y > fieldSize.height-sub.getSize().height || p.x > fieldSize.width-sub.getSize().width) {
					reverseDirection(ToUpLeft);
				}
			}
		}
		
		boolean searchVertical(int d) {
			Point treeP = new Point (800+75,600+100);
			Point center = sub.getCenterPoint();
			if (treeP.getY() >= (center.getY() - (searchWidth/2)) && treeP.getY() <= (center.getY() + (searchWidth/2))) {
				if (d>0) {
					if(treeP.getX() >= center.getX() && treeP.getX() <= center.getX() + searchHeight) {
						return true;
					}
				} else {
					if(treeP.getX() <= center.getX() && treeP.getX() >= center.getX() - searchHeight) {
						return true;
					}
				}
			}
			return false;
		}
		boolean searchHorizon(int d) {
			Point treeP = new Point (800+75,600+100);
			Point center = sub.getCenterPoint();
			if(treeP.getX() >= (center.getX() - (searchWidth/2)) && treeP.getX() <= (center.getX() + (searchWidth/2))) {
				if (d>0) {
					if (treeP.getY() >= center.getY() && treeP.getY() <= center.getY() + searchHeight) {
						return true;
					}
				} else {
					if (treeP.getY() <= center.getY() && treeP.getY() >= center.getY() - searchHeight) {
						return true;
					}
				}
			}
			return false;
		}
		void searchFeed() {
			if (sub.isDetected) {
				Point center = sub.getCenterPoint();
				if (center.getX() <= 800-(searchHeight/2) && center.getX() >= 800+(searchHeight/2)) {
					sub.isDetected = false;
				}
				if (center.getY() <= 600-(searchWidth/2) && center.getY() >= 600+(searchWidth/2)) {
					sub.isDetected = false;
				}
			}
			boolean isFind = false;
			if (direction == ToUp) {
				isFind = searchHorizon(-1);
			} else if (direction >= ToUpLeft && direction <= ToDownLeft) {
				isFind = searchVertical(-1);
			} else if (direction == ToDown) {
				isFind = searchHorizon(1);
			} else if (direction >= ToDownRight && direction <= ToUpRight) {
				isFind = searchVertical(1);
			} else {
				return;
			}
			if (isFind) {
				sub.isDetected = true;
			} else {
				sub.isDetected = false;
			}
		}
		@Override
		public synchronized void run() {
			while(true) {
				if(sub.isMove) {
					setDirection();
					for(int i=1; i<distance+1; i++) {
						try {
							if(!sub.isMove) {
								break;
							}
							goWithCheckLimit();
							searchFeed();
							Thread.sleep(1000/speed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	class ExploreThread implements Runnable {
		@Override
		public synchronized void run() {
			
		}
	}
}

