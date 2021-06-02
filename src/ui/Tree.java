package ui;

import java.awt.*;
import javax.swing.*;
import libs.utils;
import java.util.*;

class Tree extends Subject {
	TreeResource rsrc = null;
	protected HashMap<Integer,Integer> leafs = new HashMap<Integer,Integer>();
	protected int	length;
	
	public Tree(GameField gf,TreeResource rsrc) {
		super(gf,"Tree",null,0);
		length = ((int)(Math.random()*5)) + GameMain.treeAverage - 2;
		leafs.put(length-1, 10);
		leafs.put(length, 12);
		leafs.put(length+1, 10);
		this.setSize(new Dimension(150,length*20));
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
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.size == null) {
			return;
		}
		g.drawImage(rsrc.getTreeImg(0),this.size.width,0,-this.size.width,this.size.height,this);
		g.drawString("X,Y : "+this.getCenterPoint().x+","+this.getCenterPoint().y, 0, 10);
		Object[] leafHeights = this.leafs.keySet().toArray();
		for (int i=1; i<leafHeights.length+1;i++) {
			int height = (int) leafHeights[i-1];
			g.drawString(Integer.toString(height)+" : "+this.leafs.get(height), 0, 10+(i*20));
		}
	}
}
