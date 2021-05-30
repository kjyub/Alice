package ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import libs.utils;

public class Giraffe extends Subject{
	GiraffeResource resource;
	private int neck;
	private static final int DefaultNeck = 10;
	private static final int NeckHeightUnit = 7;
	private static final int WithoutNeckHeight = 150;
	protected int headFrame = 0;
	protected int neckFrame = 0;
	protected int bodyFrame = 0;
	protected int dieFrame = 0;
	private AlphaComposite alphaComposite;
	Giraffe(GameField gf) {
		super(gf,"GIRRAFE",gf.getFeeds(),50);
		this.neck = (int) (Math.random()*10)+5;
		this.setSize(new Dimension(150,130+(this.neck*NeckHeightUnit)));
		this.resource = gf.getResource();
		this.moveMotionThread = new Thread(new MoveMotionThread(this));
		this.eatingMotionThread = new Thread(new EatingMotionThread(this));
		this.dieMotionThread = new Thread(new DieMotionThread(this));
		explore();
	}
	Giraffe(GameField gf,int gene) {
		super(gf,"GIRRAFE",gf.getFeeds(),50);
		this.neck = neckMutant(gene);
		this.setSize(new Dimension(150,130+(this.neck*NeckHeightUnit)));
		this.resource = gf.getResource();
		this.moveMotionThread = new Thread(new MoveMotionThread(this));
		this.eatingMotionThread = new Thread(new EatingMotionThread(this));
		this.dieMotionThread = new Thread(new DieMotionThread(this));
		explore();
	}
	int neckMutant(int neck) {
		int p = (int) (Math.random() * 10) + 1;
		if (p>7) {
			int[] changes = {-1,-2,1,2};
			int pick = (int) (Math.random() * 4);
			return neck + changes[pick];
		} else {
			return neck;
		}
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
					System.out.println(distance+" ��ŭ �����δ�.");
				}
			}
		}
	}
	
	// ��ü�� �Ļ� ��� ������
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
				// �̵��� ���߸� �ߴ�
				if(!g.isEating) {
					headFrame = 0;
					g.repaint();
					g.isEating = false;
					g.isMove = true;
					return;
				}
				g.headFrame++;
				// ����� ������ 0���� �ٽ� �ʱ�
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
	
	// ��ü�� �̵� ��� ������
	class MoveMotionThread implements Runnable {
		private Giraffe g;
		MoveMotionThread(Giraffe g) {
			this.g = g;
		}
		@Override
		public synchronized void run() {
			while(true) {
				// �̵��� ���߸� �ߴ�
				if(!g.isMove) {
					g.bodyFrame = 0;
					g.repaint();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//					break;
				}
				g.bodyFrame++;
				// ����� ������ 0���� �ٽ� �ʱ�
				if(g.bodyFrame == resource.bodyFrameCount) {
					g.bodyFrame = 0;
				}
				g.repaint();
				try {
					Thread.sleep(66);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// ��ü�� ��� ��� ������
	class DieMotionThread implements Runnable {
		private Giraffe g;
		DieMotionThread(Giraffe g) {
			this.g = g;
		}
		@Override
		public synchronized void run() {
			// �̵��� ���߸� �ߴ�
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
		if (this.isBreeded) {
			Image baby = resource.getBabyImg(bodyFrame);
			g.drawString(
				"���� �� ",
				0, 90
			);
			if (this.isReflected) {
				g.drawImage(head,this.size.width+(this.size.width/2),0,-this.size.width,this.size.height,this);
				g.drawImage(neck,this.size.width+(this.size.width/2),0,-this.size.width,WithoutNeckHeight+(this.neck*NeckHeightUnit),this);
				g.drawImage(body,this.size.width+(this.size.width/2),((this.neck-DefaultNeck)*NeckHeightUnit),-this.size.width,this.size.height,this);
				g.drawImage(baby,this.size.width/2,this.size.height/2,-this.size.width/2,this.size.height/2,this);
			} else {
				g.drawImage(head,(this.size.width/2),0,this.size.width,this.size.height,this);
				g.drawImage(neck,(this.size.width/2),0,this.size.width,WithoutNeckHeight+(this.neck*NeckHeightUnit),this);
				g.drawImage(body,(this.size.width/2),((this.neck-DefaultNeck)*NeckHeightUnit),this.size.width,this.size.height,this);
				g.drawImage(baby,this.size.width+(this.size.width/2),this.size.height/2,this.size.width/2,this.size.height/2,this);
			}
		} else {
			if (this.isReflected) {
				g.drawImage(head,this.size.width,0,-this.size.width,this.size.height,this);
				g.drawImage(neck,this.size.width,0,-this.size.width,WithoutNeckHeight+(this.neck*NeckHeightUnit),this);
				g.drawImage(body,this.size.width,((this.neck-DefaultNeck)*NeckHeightUnit),-this.size.width,this.size.height,this);
			} else {
				g.drawImage(head,0,0,this.size.width,this.size.height,this);
				g.drawImage(neck,0,0,this.size.width,WithoutNeckHeight+(this.neck*NeckHeightUnit),this);
				g.drawImage(body,0,((this.neck-DefaultNeck)*NeckHeightUnit),this.size.width,this.size.height,this);
			}
		}
		if (this.isDetected) {
			g.setColor(Color.RED);
			g.drawString("���� �߰� !!", 0, 50);
		}
		if (this.eatReady) {
			g.setColor(Color.BLUE);
			g.drawString("�Ļ� �غ� !!", 0, 70);
		} else {
			g.setColor(Color.BLUE);
			g.drawString("�Ļ� ��� !!", 0, 70);
		}
		g.setColor(Color.black);
		g.drawString(
			"X,Y : "+this.getCenterPoint().x+","+this.getCenterPoint().y+", �� : "+this.neck,
			0, 10
		);
		g.drawString(
			"����� : "+this.hungry+",���� : "+this.breed,
			0, 30
		);
	}
	@Override
	public String toString() {
		return "Giraffe xy : ["+this.getX()+","+this.getY()+"]";
	}
	
}
