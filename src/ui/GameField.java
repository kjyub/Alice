package ui;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class GameField extends JPanel {
	public Vector<Giraffe> giraffes = new Vector<Giraffe>();
	private GiraffeResource gSrc = new GiraffeResource();
	GameField(int sizeX,int sizeY) {
		int paddingSize = 20;
		this.setLocation(paddingSize, paddingSize);
		this.setBackground(new Color(0xEFE9BB));
		this.setLayout(null);
//		g.setBackground(null);
//		g.setLocation(30, 30);
//		this.add(g);
		gSrc = new GiraffeResource();
	}
	void summon(int numGiraffes) {
		for(int i=0; i<numGiraffes; i++) {
			Giraffe grf = new Giraffe(gSrc);
			giraffes.add(grf);
			int x = (int) (Math.random()*1350) + 1;
			int y = (int) (Math.random()*600) + 1;
			grf.setLocation(x, y);
			this.add(grf);
			grf.move();
		}
	}
}
