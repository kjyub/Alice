package ui;

import java.awt.*;

import javax.swing.*;
import libs.utils;

public class Giraffe extends Subject{
	GiraffeResource resource;
	private int neck,skinColor;
	private static String name = "GIRRAFE";
	private static String feed = "TREE";
	private static int SPEED = 200;
	private static Dimension SIZE = new Dimension(150,200);
	private String imgSrc = "src/resource/subjects/girrafe/";
	protected int headFrame = 0;
	protected int neckFrame = 0;
	protected int bodyFrame = 0;
	Giraffe(GiraffeResource resource) {
		super(name,feed,SPEED,SIZE);
		this.resource = resource;
		this.moveMotionThread = new Thread(new MoveMotionThread(this));
		this.eatingMotionThread = new Thread(new EatingMotionThread(this));
		this.setBackground(null);
		this.setOpaque(false);
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
		EatingMotionThread(Giraffe g) {
			this.g = g;
		}
		@Override
		public void run() {
			while(true) {
				// 이동이 멈추면 중단
				if(!g.isEating) {
					headFrame = 0;
					g.repaint();
					g.isEating = false;
					return;
				}
				g.headFrame++;
				// 모션이 끝나면 0으로 다시 초기
				if(g.headFrame == resource.headFrameCount) {
					g.headFrame = 0;
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
//		Image headImg = utils.getImage(imgSrc+"head"+Integer.toString(headFrame)+".png");
//		Image neckImg = utils.getImage(imgSrc+"neck"+Integer.toString(neckFrame)+".png");
//		Image bodyImg = utils.getImage(imgSrc+"body"+Integer.toString(bodyFrame)+".png");
		if (this.isReflected) {
			g.drawImage(resource.getHeadImg(headFrame),SIZE.width,0,-SIZE.width,SIZE.height,this);
			g.drawImage(resource.getNeckImg(neckFrame),SIZE.width,0,-SIZE.width,SIZE.height,this);
			g.drawImage(resource.getBodyImg(bodyFrame),SIZE.width,0,-SIZE.width,SIZE.height,this);
		} else {
			g.drawImage(resource.getHeadImg(headFrame),0,0,SIZE.width,SIZE.height,this);
			g.drawImage(resource.getNeckImg(neckFrame),0,0,SIZE.width,SIZE.height,this);
			g.drawImage(resource.getBodyImg(bodyFrame),0,0,SIZE.width,SIZE.height,this);
		}
//		g.setColor(Color.red);
//		g.drawRect(0, 0, this.getSize().width-1, this.getSize().height-1);
	}
}
