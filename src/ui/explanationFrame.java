package ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.*;

public class explanationFrame extends JFrame{
	Container c;
	
	explanationFrame(){
		
		this.setTitle("게임설명");
	
		JPanel jp = new JPanel();
		
		ImageIcon img = new ImageIcon("images/explanation.jpg"); //배경이미지
		JLabel Logo = new JLabel(img);
		
		jp.add(Logo);
		this.add(jp,BorderLayout.CENTER);
		this.setVisible(true);	
		this.setSize(735,448);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new explanationFrame();
	}
}
