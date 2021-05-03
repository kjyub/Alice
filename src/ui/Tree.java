package ui;

import java.awt.*;
import javax.swing.*;
import libs.utils;

class Tree extends Subject {
	TreeResource rsrc = null;
	public Tree(TreeResource rsrc) {
		super("Tree","TREE",0, new Dimension(150,200));
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
		g.drawImage(rsrc.getTreeImg(0),this.size.width,0,-this.size.width,this.size.height,this);

		g.drawString("X,Y : "+this.getCenterPoint().x+","+this.getCenterPoint().y, 0, 10);
	}
}
