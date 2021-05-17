package ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import libs.utils;

public class Giraffe extends Subject{
	GiraffeResource resource;
	private int neck,skinColor;
	private String imgSrc = "src/resource/subjects/girrafe/";
	protected int headFrame = 0;
	protected int neckFrame = 0;
	protected int bodyFrame = 0;
	Giraffe(GiraffeResource resource,Vector<Subject> trees) {
		super("GIRRAFE",trees,200, new Dimension(150,200));
		this.resource = resource;
		this.moveMotionThread = new Thread(new MoveMotionThread(this));
		this.eatingMotionThread = new Thread(new EatingMotionThread(this));
		explore();
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
		this.eat();
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
					System.out.println(distance+" 만큼 움직인다.");
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
				}
				g.bodyFrame++;
				// 모션이 끝나면 0으로 다시 초기
				if(g.bodyFrame == resource.bodyFrameCount) {
					g.bodyFrame = 0;
				}
				g.repaint();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image head = null;
		if (headFrame<10) {
			head = resource.getHeadImg(headFrame);
		} else if (headFrame>=10)  {
			head = resource.getHeadEatImg(headFrame-10);
		}
		Image neck = resource.getNeckImg(neckFrame);
		Image body = resource.getBodyImg(bodyFrame);
		if (this.isReflected) {
			g.drawImage(head,this.size.width,0,-this.size.width,this.size.height,this);
			g.drawImage(neck,this.size.width,0,-this.size.width,this.size.height,this);
			g.drawImage(body,this.size.width,0,-this.size.width,this.size.height,this);
		} else {
			g.drawImage(head,0,0,this.size.width,this.size.height,this);
			g.drawImage(neck,0,0,this.size.width,this.size.height,this);
			g.drawImage(body,0,0,this.size.width,this.size.height,this);
		}
		if (this.isDetected) {
			g.setColor(Color.RED);
			g.drawString("먹이 발견 !!", 0, 30);
		}
		if (this.eatReady) {
			g.setColor(Color.BLUE);
			g.drawString("배고픔 !!", 0, 50);
		} else {
			g.setColor(Color.BLUE);
			g.drawString("배부름 !!", 0, 50);
		}
		g.setColor(Color.black);
		g.drawString("X,Y : "+this.getCenterPoint().x+","+this.getCenterPoint().y, 0, 10);
	}
}
