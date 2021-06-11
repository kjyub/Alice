package ui;

import java.awt.*;
import javax.swing.*;
import libs.utils;
import java.util.*;

public class Tree extends Subject {
	TreeResource rsrc = null;
	static final int LEAF_HEIGHTS = 3;
	protected HashMap<Integer,Integer> leafs = new HashMap<Integer,Integer>();
	protected int length;
	GameField gameField;
	private int dieFrame = 0;
	private Giraffe executor;

	private AlphaComposite alphaComposite;
	
	public Tree(GameField gf,TreeResource rsrc) {
		super(gf,null,0);
		this.setSize(new Dimension(rsrc.TotalWidth,length*rsrc.TreeLengthUnit));
		this.rsrc = rsrc;
	}
	
	public Tree(GameField gf,TreeResource rsrc,int length) {
		this(gf,rsrc);
		leafs.put(length-1, 1);
		leafs.put(length, 1);
		leafs.put(length+1, 0);
		this.length = length;
		this.setSize(new Dimension(rsrc.TotalWidth,length*rsrc.TreeLengthUnit));
	}
	
	public Tree(GameField gf,TreeResource rsrc,TreeVO vo) {
		this(gf, rsrc);
		this.length = vo.getLength();
		this.setLocation(vo.getX(),vo.getY());
		this.leafs.put(this.length-1,vo.getLeaf0());
		this.leafs.put(this.length,vo.getLeaf1());
		this.leafs.put(this.length+1,vo.getLeaf2());
		this.setSize(new Dimension(rsrc.TotalWidth,length*rsrc.TreeLengthUnit));
	}
	
	public TreeVO parseVO() {
		TreeVO vo = new TreeVO();
		vo.setLength(this.length);
		vo.setLeaf0(this.leafs.get(this.length-1));
		vo.setLeaf1(this.leafs.get(this.length));
		vo.setLeaf2(this.leafs.get(this.length+1));
		vo.setX(this.getX());
		vo.setY(this.getY());
		return vo;
	}
	
	@Override
	void death() {
		// TODO Auto-generated method stub
		
	}
	@Override
	void breeding() {
		// TODO Auto-generated method stub
		
	}
	
	boolean checkLeaf(Giraffe grf) {
		for (int height=this.length+1; height>this.length-2;height--) {
			int leaf = this.leafs.get(height);
			if (grf.neck >= height && leaf > 0) {
				this.leafs.put(height, leaf - 1);
				this.repaint();
				checkDie(grf);
				return true;
			}
		}
		return false;
	}
	
	void checkDie(Giraffe grf) {
		int die = 0;
		Iterator keys = leafs.keySet().iterator();
		while(keys.hasNext()) {
			if(leafs.get(keys.next()) <= 0) {
				die++;
			}
		}
		if (die == 3) {
			this.field.trees.remove(this);
			this.executor = grf;
//			return true;
		}
//		return false;
	}
	
	boolean checkExecutor(Giraffe grf) {
		if (this.executor == grf) {
			return true;
		}
		return false;
	}
	
	void dieTree(Giraffe grf) throws InterruptedException {
		if (this.executor != grf) {
			return;
		}
		this.died = true;
		Thread.sleep(Subject.eatTime);
		DieMotionThread dmt = new DieMotionThread(this);
		Thread motionThread = new Thread(dmt);
		motionThread.start();
	}
	
	class DieMotionThread implements Runnable {
		private Tree t;
		DieMotionThread(Tree t) {
			this.t = t;
		}
		@Override
		public synchronized void run() {
			// 이동이 멈추면 중단
			while(t.dieFrame < 10){
				t.dieFrame++;
				t.repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			t.field.remove(t);
//			t.field.trees.remove(t);
			t.field.repaint();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.size == null) {
			System.out.println("tree size null?");
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
