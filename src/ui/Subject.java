package ui;
import javax.swing.*;

import libs.utils;

import java.awt.*;
import java.awt.color.*;
import java.util.Vector;

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
	final static Dimension DefaultSize = new Dimension(150,200);
	
	protected static int searchWidth = 600;
	protected static int searchHeight = 200;

	protected int id;
	protected int lastDirection = 13;
	protected int lastHeadDirection = 13;
	
	protected GameField field;
	
	protected String name;
	protected String feed;
	protected int speed = 1;
	protected int eatTime = 3000; // 먹이 먹는 시간
	protected int eatCoolTime = 10*1000; // 먹이먹고 포만감 꺼지는 시간 - 조절 가능
	protected boolean eatReady = true; // eatCoolTime 끝나는걸 알
	protected int ageRate = 30; // (중요) 수치들 배수 - 조절 가능
	protected int cal = 3*ageRate; // 칼로리, 포만감 상승 수치 (1~100)
	protected int age = 0; // 
	protected int hungry = 100*ageRate; // 초기 포만감 수치 (기본값 : 100*ageRate)
	protected int breedReadyValue = 1;  // 몇번 먹이를 먹어야 출산을 할수 있는지 - 조절 가능
	protected int breedValue = 30; // 0~100  조절 가능
	protected int breed = 0;
	protected int maxIndependence = 2; // 조절 가능
	protected int independence = 0;
	protected Dimension size;
	protected Vector<Tree> feeds = null;
	protected boolean isMove,isEating;
	protected boolean isBreeded = false;
	protected boolean isReflected = false;
	protected boolean isDetected = false;
	protected boolean died = false;
	protected Runnable moveMotionThread;
	protected Runnable eatingMotionThread;
	protected Runnable dieMotionThread;
	Subject(GameField gf,String name, Vector<Tree> feeds,int speed) {
		this.field = gf;
		this.name = name;
		this.feeds = feeds;
		this.speed = speed;
		this.size = DefaultSize;
		this.setSize(DefaultSize);
		this.setBackground(null);
		this.setOpaque(false);
		this.setVisible(true);
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
	void eat(Subject feed) {
		if (this.isEating) {
			return;
		} else {
			isEating = true;
		}
		this.isEating=true;
		this.isMove=false;
		Thread thread = new Thread(new EatThread(this,feed));
		Thread motionThread = new Thread(eatingMotionThread);
		thread.start();
		motionThread.start();
	};
	abstract void death();
	abstract void breeding();
	void setSpeed(int speed) {
		this.speed = speed;
	}
	void setAgeRate(int rate) {
		this.ageRate = rate;
	}
	void setCal(int cal) {
		this.cal = cal;
	}
	void die() {
		if(this.died) {
			return;
		}
		this.died = true;
		this.isMove = false;
		this.repaint();
		Thread motionThread = new Thread(dieMotionThread);
		motionThread.start();
	}
	// 새끼 독립시키기
	void independent(Point spawnPoint) {
		Giraffe parent = (Giraffe) this;
		this.breed = 0;
		this.independence = 0;
		this.isBreeded = false;
		Giraffe newGiraffe = new Giraffe(this.field,parent.neck);
		this.field.giraffes.add(newGiraffe);
		newGiraffe.setLocation(spawnPoint);
		newGiraffe.move();
		this.field.add(newGiraffe);
		this.field.repaint();
	}
	
	class EatThread implements Runnable {
		Subject sub;
		Tree feed;
		EatThread(Subject sub,Subject feed) {
			this.sub = sub;
			this.feed = (Tree)feed;
		}
		@Override
		public void run() {
			try {
				sub.eatReady = false;
				sub.isEating=true;
				sub.isMove=false;
				// 식사 시간 
				Thread.sleep(eatTime);
				feed.repaint();
				// 포만감 상승
				sub.hungry += cal*ageRate;
				// 포만감 최대 도달 
				if (sub.hungry > 100*ageRate) {
					sub.hungry = 100*ageRate;
				}
				// 새끼를 낳은 상태면 새끼 성장
				if (sub.isBreeded) {
					sub.independence+=1;
					System.out.println("새끼 나이 "+sub.independence);
				}
				// 새끼가 성체까지 성장하면 독립시키기
				if (sub.independence >= sub.maxIndependence) {
					sub.independent(sub.getLocation());
				}
				sub.isEating=false;
				sub.isMove=true;
				sub.isDetected=false;
				Thread.sleep(eatCoolTime);
				sub.eatReady = true;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class MoveThread implements Runnable {
		private Giraffe grf;
		private int direction,distance;
		private Dimension fieldSize = new Dimension(1650,1000);
		private int feet = 1;
		MoveThread(Subject sub) {
			this.grf = (Giraffe) sub;
		}
		int getDirection(int lastDirection) {
			int[] dirs = utils.getDetectableRange(lastDirection,2);
			int[] probs = {12,29,71,88,100}; // Ȯ��
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
			this.direction = getDirection(grf.lastDirection);
			// 객체 이미지 반전
			if ((this.direction!=Subject.ToUp)&&(this.direction!=Subject.ToDown)) {
				if((this.direction<Subject.ToDown) && (grf.lastHeadDirection==Subject.ToRight)) {
					grf.lastHeadDirection = Subject.ToLeft;			
					grf.isReflected = !grf.isReflected;
				} else if((this.direction>Subject.ToDown) && (grf.lastHeadDirection==Subject.ToLeft)) {
					grf.lastHeadDirection = Subject.ToRight;			
					grf.isReflected = !grf.isReflected;
				}
			}
			grf.lastDirection = this.direction;
		}
		void reverseDirection(int d) {
			this.direction = d;
			grf.lastDirection = d;
			if (!((d==Subject.ToUp) || (d==Subject.ToUp))) {
				grf.isReflected = !grf.isReflected;
				if(grf.lastHeadDirection == Subject.ToLeft) {
					grf.lastHeadDirection = Subject.ToRight;
				} else {
					grf.lastHeadDirection = Subject.ToLeft;
				}
			}
		}
		void goWithCheckLimit() {
			Point p = grf.getLocation();		
 			if (direction==ToUp) {
 				grf.setLocation(p.x, p.y-feet);
				if(p.y < 0) {
					reverseDirection(ToDown);
				}
			} else if (direction==ToDown) {
				grf.setLocation(p.x, p.y+feet);
				if(p.y > fieldSize.height-grf.getSize().height) {
					reverseDirection(ToUp);
				}
			} else if (direction==ToLeft) {
				grf.setLocation(p.x-feet, p.y);
				if(p.x < 0) {
					reverseDirection(ToRight);
				}
			} else if (direction==ToRight) {
				grf.setLocation(p.x+feet, p.y);
				if(p.x > fieldSize.width-grf.getSize().width) {
					reverseDirection(ToLeft);
				}
			} else if (direction==ToUpLeft) {
				grf.setLocation(p.x-feet, p.y-feet);
				if(p.y < 0 || p.x < 0) {
					reverseDirection(ToDownRight);
				}
			} else if (direction==ToUpRight) {
				grf.setLocation(p.x+feet, p.y-feet);
				if(p.y < 0 || p.x > fieldSize.width-grf.getSize().width) {
					reverseDirection(ToDownLeft);
				}
			} else if (direction==ToDownLeft) {
				grf.setLocation(p.x-feet, p.y+feet);
				if(p.y > fieldSize.height-grf.getSize().height || p.x < 0) {
					reverseDirection(ToUpRight);
				}
			} else if (direction==ToDownRight) {
				grf.setLocation(p.x+feet, p.y+feet);
				if(p.y > fieldSize.height-grf.getSize().height || p.x > fieldSize.width-grf.getSize().width) {
					reverseDirection(ToUpLeft);
				}
			}
		}
		
		boolean searchVertical(int d,Point treeP) {
			Point center = grf.getCenterPoint();
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
		boolean searchHorizon(int d,Point treeP) {
			Point center = grf.getCenterPoint();
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
		Subject searchFeed() {
			if (grf.isDetected) {
				Point center = grf.getCenterPoint();
				if (center.getX() <= 800-(searchHeight/2) && center.getX() >= 800+(searchHeight/2)) {
					grf.isDetected = false;
				}
				if (center.getY() <= 600-(searchWidth/2) && center.getY() >= 600+(searchWidth/2)) {
					grf.isDetected = false;
				}
			}
			boolean isFind = false;
			for (Tree feed : feeds) {
				if (grf.neck < feed.length-1) {
					return null;
				}
				Point feedP = new Point(feed.getX()+(feed.getWidth()/2),feed.getY()+(feed.getHeight()/2));
				if (direction == ToUp) {
					isFind = searchHorizon(-1,feedP);
				} else if (direction >= ToUpLeft && direction <= ToDownLeft) {
					isFind = searchVertical(-1,feedP);
				} else if (direction == ToDown) {
					isFind = searchHorizon(1,feedP);
				} else if (direction >= ToDownRight && direction <= ToUpRight) {
					isFind = searchVertical(1,feedP);
				}
				if (isFind) {
					grf.isDetected = true;
					Object[] leafHeights = feed.leafs.keySet().toArray();
					for (int i=leafHeights.length-1; i>-1;i--) {
						int height = (int) leafHeights[i];
						int leaf = feed.leafs.get(height);
						System.out.println(height);
						if (grf.neck >= height && leaf > 0) {
							feed.leafs.put(height, leaf - 1);
							feed.repaint();
							break;
						} else {
							System.out.println("Too Long ,"+grf.neck +","+ height);
						}
					}
					return feed;
				}
			}
			grf.isDetected = false;
			return null;
		}
		void toFeedX(int xd,Subject feed) {
			int xdd ;
			if (xd>0) {
				if(grf.lastHeadDirection == Subject.ToLeft) {
					grf.isReflected = !grf.isReflected;
				}
				grf.lastHeadDirection = Subject.ToRight;
				xdd = 1;
			} else if (xd < 0) {
				if(grf.lastHeadDirection == Subject.ToRight) {
					grf.isReflected = !grf.isReflected;
				}
				grf.lastHeadDirection = Subject.ToLeft;
				xdd = -1;
			} else {
				xdd = 0;
			}
			for(int i=0; i<(Math.abs(xd)/feet); i++) {
				try {
					if(!grf.isMove) {
						break;
					}
					Point p = grf.getLocation();
					grf.setLocation(p.x+(feet*xdd),p.y);
					Thread.sleep(1000/speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		void toFeedY(int yd,Subject feed) {
			int ydd = (yd > 0) ? 1 : -1;
			for(int i=0; i<(Math.abs(yd)/feet); i++) {
				try {
					if(!grf.isMove) {
						break;
					}
					Point p = grf.getLocation();
					grf.setLocation(p.x,p.y+(feet*ydd));
					Thread.sleep(1000/speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		void toFeed(Subject feed) {
			boolean feedSideLeft = true;
			Point subCenter = grf.getCenterPoint();
			int xd;
			if (feed.getX()+(feed.getWidth()/2) > subCenter.getX()) {
				xd = (int) (feed.getX()-subCenter.getX()-(feed.getWidth()/2));
			} else {
				feedSideLeft = false;
				xd = (int) ((feed.getX()+feed.getWidth())-subCenter.getX());
			}
			int yd = (feed.getY()-grf.getY());
			if (Math.abs(yd)>Math.abs(xd)) {
				toFeedY(yd,feed);
				toFeedX(xd,feed);
			} else if (Math.abs(yd)<=Math.abs(xd)) {
				toFeedX(xd,feed);
				toFeedY(yd,feed);
			} else {
//				System.out.println("gr : "+feed.getY()+","+sub.getY()+","+Math.abs(feed.getY()-sub.getY()));
//				int xdd = (xd > 0) ? 1 : -1;
//				int ydd = (yd < 0) ? 1 : -1;
//				for(int i=0; i<Math.abs(feed.getY()-sub.getY()); i++) {
//					try {
//						if(!sub.isMove) {
//							break;
//						}
//						Point p = sub.getLocation();
//						sub.setLocation(p.x+(feet*xdd),p.y+(feet*ydd));
//						System.out.println("gr");
//						Thread.sleep(1000/speed);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
			}
			if (feedSideLeft) {
				if(grf.lastHeadDirection == Subject.ToLeft) {
					grf.isReflected = !grf.isReflected;
				}
				grf.lastHeadDirection = Subject.ToRight;
			} else {
				if(grf.lastHeadDirection == Subject.ToRight) {
					grf.isReflected = !grf.isReflected;
				}
				grf.lastHeadDirection = Subject.ToLeft;
			}
			grf.eat(feed);
		}
		@Override
		public synchronized void run() {
			while(true) {
				Subject searchedFeed = null;
				if (grf.died) {
					grf.isMove=false;
					break;
				}
				if(grf.isMove) {
					setDirection();
					for(int i=1; i<distance+1; i++) {
						try {
							if(!grf.isMove) {
								break;
							}
							goWithCheckLimit();
							grf.hungry--;
							if(grf.hungry <= 0 && !grf.died) {
								grf.die();
								break;
							}
							// 포만감이 채워지면 출산 준비
							if(!isBreeded) {
								// 포만감이 일정 이상 올라가면 출산 준비
								if(grf.hungry >= breedReadyValue*ageRate) {
									grf.breed++;
									// 출산 준비가 채워지면 출산
									if(grf.breed > breedValue*ageRate) {
										grf.setSize(grf.getWidth()*2,grf.getHeight());
										grf.isBreeded = true;
										grf.breed = 0;
									}
								} else {
									grf.breed = 0;
								}
							}
							// 먹이 탐색
							if(grf.eatReady) {								
								searchedFeed = searchFeed();
								if(searchedFeed != null) {
									toFeed(searchedFeed);
									Thread.sleep(grf.eatTime);
									break;
								}
							}
							Thread.sleep(1000/speed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} else {
					try {
						Thread.sleep(500);
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

