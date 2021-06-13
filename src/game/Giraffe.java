package game;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import libs.utils;

public class Giraffe extends Subject{
	GiraffeResource resource;
	public final int DefaultNeck = 10;
	public final int MinNeck = 5;
	public final int MaxNeck = 30;
	protected int neck;
	protected int headFrame = 0;
	protected int neckFrame = 0;
	protected int bodyFrame = 0;
	protected int dieFrame = 0;
	private AlphaComposite alphaComposite;
	Giraffe(GameField gf) {
		super(gf,gf.getFeeds(),50);

//		this.neck = neckMutant(10);
		this.neck = DefaultNeck;
		this.setSize(new Dimension(resource.TotalWidth,
				(resource.HeadHeight+resource.BodyHeight+this.getNeckHeight())
				));
		
		this.resource = gf.getResource();
		this.moveMotionThread = new Thread(new MoveMotionThread(this));
		this.eatingMotionThread = new Thread(new EatingMotionThread(this));
		this.dieMotionThread = new Thread(new DieMotionThread(this));
		explore();
		gf.giraffes.add(this);
	}
	Giraffe(GameField gf,int gene) {
		this(gf);
		this.neck = neckMutant(gene);
		gf.maxGiraffeID++;
		this.id = gf.maxGiraffeID;
		this.setSize(new Dimension(resource.TotalWidth,
				(resource.HeadHeight+resource.BodyHeight+this.getNeckHeight())
				));
		gf.updateAmount();
	}
	Giraffe(GameField gf,GiraffeVO vo) {
		this(gf);
		this.id = vo.getId();
		this.neck = vo.getNeck();
		this.birthDate = vo.getBirthDate();
		this.lastDirection = vo.getLastDirection();
		this.lastHeadDirection = vo.getLastHeadDirection();
		this.hungry = vo.getHungry();
		this.breed = vo.getBreed();
		this.independence = vo.getIndependence();
		this.setLocation(vo.getX(),vo.getY());
		this.isMove = vo.isMove();
//		this.isEating = vo.isEating();
		this.isEating = false;
		this.isBreeded = vo.isBreeded();
		this.isReflected = vo.isReflected();
		this.isDetected = vo.isDetected();
		this.died = vo.isDied();
		this.setSize(new Dimension(resource.TotalWidth,
				(resource.HeadHeight+resource.BodyHeight+this.getNeckHeight())
				));
	}
	public GiraffeVO parseVO() {
		GiraffeVO vo = new GiraffeVO();
		vo.setId(this.id);
		vo.setNeck(this.neck);
		vo.setBirthDate(this.birthDate);
		vo.setLastDirection(this.lastDirection);
		vo.setLastHeadDirection(this.lastHeadDirection);
		vo.setHungry(this.hungry);
		vo.setBreed(this.breed);
		vo.setIndependence(this.independence);
		vo.setX(this.getX());
		vo.setY(this.getY());
		vo.setMove(this.isMove);
		vo.setEating(this.isEating);
		vo.setBreeded(this.isBreeded);
		vo.setReflected(this.isReflected);
		vo.setDetected(this.isDetected);
		vo.setDied(this.died);
		return vo;
	}
	int neckMutant(int neck) {
		int p = (int) (Math.random() * 100) + 1;
		if (p>100-GameMain.mutantProb) {
			int[] changes = {-1,-2,1,2};
//			int[] changes = {-3,-6,3,6};
			int pick = (int) (Math.random() * 4);
			int newNeck = neck + changes[pick];
			if (newNeck > MaxNeck) {
				newNeck = MaxNeck;
			} 
			if (newNeck < MinNeck){
				newNeck = MinNeck;
			}
			return newNeck; 
		} else {
			return neck;
		}
	}
	int getNeckHeight() {
		return (resource.NeckHeight+((this.neck-DefaultNeck)*resource.NeckHeightUnit));
	}
	void explore() {
		Thread thread = new Thread(new ExploreThread(this));
//		thread.start();
	}
	void moving(int d,int distance) {
		Thread thread = new Thread(new MoveMotionThread(this));
		this.move();
	}
	void eating() {
		Thread thread = new Thread(new EatingMotionThread(this));
//		this.eat();
	}
	void death() {
		
	}
	void breeding() {
		
	}
	
	void test() {
		System.out.println("walking");
	}
	
	class ExploreThread implements Runnable {
		Giraffe grf;
		ExploreThread(Giraffe g){
			this.grf = g;
		}
		@Override
		public void run() {
			System.out.println("start");
			while(true) {
				if(grf.isMove) {
//					System.out.println("walking");
					continue;
				} else {
					int distance = (int)Math.random()*10+5;
//					grf.moving(distance, Subject.ToRight);
					System.out.println(distance+" ??? ?????δ?.");
				}
			}
		}
	}
	
	// 개체의 식사 모션 스레드
	class EatingMotionThread implements Runnable {
		private Giraffe g;
		private int headEatStartFrame = 10;
		EatingMotionThread(Giraffe g) {
			this.g = g;
		}
		@Override
		public void run() {
			headFrame = headEatStartFrame;
			g.isMove = false;
			g.bodyFrame = 0;
			for(int i=0; i<8; i++) {
				// 이동이 멈추면 중단
				if(!g.isEating) {
					headFrame = 0;
					g.repaint();
					g.isEating = false;
					g.isMove = true;
					return;
				}
				g.headFrame++;
				// 모션이 끝나면 0으로 다시 초기
				if(g.headFrame == resource.headEatFrameCount + headEatStartFrame) {
					g.headFrame = 0 + headEatStartFrame;
				}
				g.repaint();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// 개체의 이동 모션 스레드
	class MoveMotionThread implements Runnable {
		private Giraffe g;
		MoveMotionThread(Giraffe g) {
			this.g = g;
		}
		@Override
		public synchronized void run() {
			while(true) {
				// 이동이 멈추면 중단
				if(!g.isMove) {
					g.bodyFrame = 0;
					g.repaint();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//					break;
				} else {
					g.bodyFrame++;
					// 모션이 끝나면 0으로 다시 초기
					if(g.bodyFrame == resource.bodyFrameCount) {
						g.bodyFrame = 0;
					}
					g.repaint();
					try {
						Thread.sleep(80);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	// 개체의 사망 모션 스레드
	class DieMotionThread implements Runnable {
		private Giraffe g;
		DieMotionThread(Giraffe g) {
			this.g = g;
		}
		@Override
		public synchronized void run() {
			// 이동이 멈추면 중단
			while(g.dieFrame < 10){
				g.dieFrame++;
				g.repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			g.field.remove(g);
			g.field.giraffes.remove(g);
			g.field.repaint();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (this.size == null) {
			return;
		}
		super.paintComponent(g);
		Image head = null;
		if (headFrame<10) {
			head = resource.getHeadImg(headFrame);
		} else if (headFrame>=10)  {
			head = resource.getHeadEatImg(headFrame-10);
		}
		Image neck = resource.getNeckImg(neckFrame);
		Image body = resource.getBodyImg(bodyFrame);

		if (this.died) {
			float alpha = 1 - (float) dieFrame/10;
			alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			g = (Graphics2D)g;
		    ((Graphics2D) g).setComposite(alphaComposite);
		}
		g.setFont(new Font("굴림체",Font.PLAIN,10));
		int neckX = 2;
		int neckHeight = this.getNeckHeight();
		int bodyY = resource.HeadHeight + neckHeight;
		if (this.isReflected) {
			g.drawImage(head,resource.TotalWidth,0,-resource.TotalWidth,resource.HeadHeight,this);
			g.drawImage(neck,resource.TotalWidth-neckX,resource.HeadHeight,-resource.TotalWidth,neckHeight,this);
			g.drawImage(body,resource.TotalWidth,bodyY,-resource.TotalWidth,resource.BodyHeight,this);
			if (this.isBreeded) {
				int babyX = resource.TotalWidth/2;
				int babyY = resource.TotalHeight/2;
				g.drawImage(head,babyX+resource.TotalWidth,babyY+0,-resource.TotalWidth/2,resource.HeadHeight/2,this);
				g.drawImage(neck,babyX+resource.TotalWidth-neckX,babyY+(resource.HeadHeight/2),-resource.TotalWidth/2,neckHeight/2,this);
				g.drawImage(body,babyX+resource.TotalWidth,babyY+(bodyY/2),-resource.TotalWidth/2,resource.BodyHeight/2,this);
				//g.drawImage(baby,resource.TotalWidth/2,resource.TotalHeight/2,-resource.TotalWidth/2,resource.TotalHeight/2,this);
			}
		} else {
			g.drawImage(head,0,0,resource.TotalWidth,resource.HeadHeight,this);
			g.drawImage(neck,0+neckX,resource.HeadHeight,resource.TotalWidth,neckHeight,this);
			g.drawImage(body,0,bodyY,resource.TotalWidth,resource.BodyHeight,this);
			if (this.isBreeded) {
				int babyX = (int) (resource.TotalWidth);
				int babyY = resource.TotalHeight/2;
				g.drawImage(head,babyX+0,babyY+0,resource.TotalWidth/2,resource.HeadHeight/2,this);
				g.drawImage(neck,babyX+0+neckX,babyY+(resource.HeadHeight/2),resource.TotalWidth/2,neckHeight/2,this);
				g.drawImage(body,babyX+0,babyY+(bodyY/2),resource.TotalWidth/2,resource.BodyHeight/2,this);
				//g.drawImage(baby,(int) (resource.TotalWidth*(1.5)),resource.TotalHeight/2,resource.TotalWidth/2,resource.TotalHeight/2,this);
			}
		}
		if (GameMain.subjectInfo) {
			if (this.isDetected) {
				g.setColor(Color.RED);
				g.drawString("먹이 발견 !!", 0, 40);
			}
			if (this.eatReady) {
				g.setColor(Color.BLUE);
				g.drawString("식사 준비 !!", 0, 50);
			} else {
				g.setColor(Color.BLUE);
				g.drawString("식사 대기 !!", 0, 50);
			}
			if (this.isBreeded) {
//				Image baby = resource.getBabyImg(bodyFrame);
				Image baby = resource.getBabyImg(0);
				g.drawString(
					"번식 됨 ",
					0, 90
				);
			}
			g.setColor(Color.black);
//			g.drawString(
//				"X,Y : "+this.getCenterPoint().x+","+this.getCenterPoint().y,
//				0, 10
//			);
			g.drawString("목 : "+this.neck, 0, 10);
			g.drawString(
				"배고픔 : "+this.hungry,
				0, 20
			);
			g.drawString("번식 : "+this.breed, 0, 30);
			g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
		}
		
	}
	@Override
	public String toString() {
		return "Giraffe xy : ["+this.getX()+","+this.getY()+"]";
	}
	
}
