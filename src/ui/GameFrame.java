package ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.*;

public class GameFrame extends JFrame{
	GamePanel GamePanel = new GamePanel();
	Container c;
	
	GameFrame(){
		this.setTitle("동물 진화 시뮬레이터");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						
		c= this.getContentPane();
		c.add(GamePanel,BorderLayout.CENTER);
		
		this.setVisible(true);	
		this.setSize(1650,1050);
				
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GameFrame();
		
	}

}
