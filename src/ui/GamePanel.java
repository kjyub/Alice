package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import ui.BackgroundPanel2.SettingButtonActionListener;

public class GamePanel extends JPanel{
	JButton set1;
	JButton play1;
	JButton mutants1;
	ImageIcon pause;
	ImageIcon play;
	GamePanel(){
		setLayout(new BorderLayout());
		
		ImageIcon set = new ImageIcon("images/set1.png");
		ImageIcon game = new ImageIcon("images/Game.jpg");
		ImageIcon play = new ImageIcon("images/play.png");
		ImageIcon pause = new ImageIcon("images/pause.png");
		ImageIcon mutants = new ImageIcon("images/mutants.png");

		set1 = new JButton(set);			//환경설정버튼
		play1 = new JButton(play);			//재생버튼
		mutants1 = new JButton(mutants);	//게임설명(유전자모양)
		JLabel Logo = new JLabel(game);		//배경
		
		
		SettingButtonActionListener setting = new SettingButtonActionListener();
		mutantsButtonActionListener explantion  = new mutantsButtonActionListener();
		PlayButtonActionListener play2 = new PlayButtonActionListener();

		set1.addActionListener(setting);
		mutants1.addActionListener(explantion);
		play1.addActionListener(play2);
		
		set1.setSize(100,100);
		set1.setLocation(1500,25);
		set1.setBorderPainted(false);
		set1.setFocusPainted(false);
		set1.setContentAreaFilled(false);
		
		play1.setSize(100,100);
		play1.setLocation(1400,25);
		play1.setBorderPainted(false);
		play1.setFocusPainted(false);
		play1.setContentAreaFilled(false);
		
		mutants1.setSize(100,100);
		mutants1.setLocation(1300,25);
		mutants1.setBorderPainted(false);
		mutants1.setFocusPainted(false);
		mutants1.setContentAreaFilled(false);
		
		
		
		add(set1);
		add(play1);
		add(mutants1);
		add(Logo);
		
	}
	class SettingButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			new settingFrame();		
		}		
	}
	
	class mutantsButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			new explanationFrame();
			}				
	}
	
	class PlayButtonActionListener implements ActionListener{
		ImageIcon play = new ImageIcon("images/play.png");
		ImageIcon pause = new ImageIcon("images/pause.png");
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			if(b.getIcon().equals(play)) 
				play1.setIcon(pause);
			else
				play1.setIcon(play);
			}
			
	}
}
