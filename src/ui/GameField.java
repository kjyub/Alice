package ui;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class GameField extends JPanel {
	public Vector<Giraffe> giraffes = new Vector<Giraffe>();
	public Vector<Subject> trees = new Vector<Subject>();
	
	private GiraffeResource gSrc = null;
	private TreeResource tSrc = null;
	GameField(int sizeX,int sizeY) {
		int paddingSize = 20;
		this.setLocation(paddingSize, paddingSize);
		this.setBackground(new Color(0xEFE9BB));
		this.setLayout(null);
		gSrc = new GiraffeResource();
		tSrc = new TreeResource();
	}
	void summon(int numGiraffes) {
		for(int i=0; i<numGiraffes; i++) {
			Giraffe grf = new Giraffe(gSrc,trees);
			giraffes.add(grf);
			int x = (int) (Math.random()*1350) + 1;
			int y = (int) (Math.random()*600) + 1;
			grf.setLocation(x, y);
			this.add(grf);
			grf.move();
		}
		
	}
	void treeSummon(int numTree) {
		for(int i=0; i<numTree; i++) {
			Tree tree = new Tree(tSrc);
			trees.add(tree);
			int x = (int) (Math.random()*1350) + 1;
			int y = (int) (Math.random()*600) + 1;
			tree.setLocation(x,y);
			this.add(tree);
		}
	}
	
}
