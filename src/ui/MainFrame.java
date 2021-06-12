package ui;

import java.awt.BorderLayout;
import java.awt.*;

import javax.swing.*;
	
public class MainFrame extends JFrame{
	
	BackgroundPanel BackgroundPanel = new BackgroundPanel();
	Container c;

	
	void viewPanel(JPanel p) {
		
		c.removeAll();
		
		if(p instanceof BackgroundPanel2)
			c.add((BackgroundPanel2)p, BorderLayout.CENTER);
		
		revalidate();
		
	}
	
	MainFrame(){
		this.setTitle("시뮬레이터");
	//	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						
		c= this.getContentPane();
		c.add(BackgroundPanel,BorderLayout.CENTER);
		
		this.setVisible(true);	
		this.setSize(700,450);	
		
	}
	
	
			
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame MainFrame = new MainFrame();
	}

}
