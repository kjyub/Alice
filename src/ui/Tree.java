package ui;

import java.awt.*;
import javax.swing.*;
import libs.utils;
import java.util.*;

class Tree extends Subject {
	TreeResource rsrc = null;
	protected HashMap<Integer,Integer> leafs = new HashMap<Integer,Integer>();
	protected int length;
	GameField gameField;
	private int dieFrame = 0;

	private AlphaComposite alphaComposite;
	
	public Tree(GameField gf,TreeResource rsrc) {
		super(gf,"Tree",null,0);
		length = ((int)(Math.random()*5)) + GameMain.treeAverage - 2;
		leafs.put(length-1, 10);
		leafs.put(length, 12);
		leafs.put(length+1, 10);
		this.setSize(new Dimension(rsrc.TotalWidth,length*rsrc.TreeLengthUnit));
		this.rsrc = rsrc;
		this.gameField = gf;
	}
	public Tree(GameField gf,TreeResource rsrc,int length) {
		super(gf,"Tree",null,0);
		leafs.put(length-1, 10);
		leafs.put(length, 12);
		leafs.put(length+1, 10);
		this.setSize(new Dimension(rsrc.TotalWidth,length*rsrc.TreeLengthUnit));
		this.rsrc = rsrc;
	}
	@Override
	void death() {
		// TODO Auto-generated method stub
		
	}
	@Override
	void breeding() {
		// TODO Auto-generated method stub
		
	}
	
	void checkDie() {
		boolean die = false;
		if(leafs.get(this.length-1) <= 0 && leafs.get(this.length) <= 0 && leafs.get(this.length+1) <= 0) {
			die = true;
		}
		if (die) {
			this.died = true;
			gameField.remove(this);
			gameField.trees.remove(this);
			gameField.repaint();
		}
	}
	
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
		super.paintComponent(g);
		if (this.size == null) {
			return;
		}
		if (this.died) {
			float alpha = 1 - (float) dieFrame/10;
			alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			g = (Graphics2D)g;
		    ((Graphics2D) g).setComposite(alphaComposite);
		}
		g.drawImage(rsrc.getTreeImg(0),this.getWidth(),0,-this.getWidth(),this.getHeight(),this);
//		g.drawString("X,Y : "+this.getCenterPoint().x+","+this.getCenterPoint().y, 0, 10);
		Object[] leafHeights = this.leafs.keySet().toArray();
		for (int i=1; i<leafHeights.length+1;i++) {
			int height = (int) leafHeights[i-1];
			g.drawString(Integer.toString(height)+" : "+this.leafs.get(height), 0, (i*20));
		}
	}
}
