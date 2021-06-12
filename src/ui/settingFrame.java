package ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class settingFrame extends JFrame{
	Container c;
	settingPanel settingPanel = new settingPanel();
	
	settingFrame(){
		setLayout(new BorderLayout());
		
		this.setTitle("환경설정");
		
		
		c= this.getContentPane();
		

		ImageIcon Backscreen = new ImageIcon("images/setScreen.jpg");
		JLabel Logo = new JLabel(Backscreen);
		this.add(Logo);

		c.add(settingPanel,BorderLayout.CENTER);
		
		this.setVisible(true);
		this.setSize(600,100);
		
	}
	
public static void main(String[] args) {
	// TODO Auto-generated method stub
	settingFrame set = new settingFrame();
	}
}
