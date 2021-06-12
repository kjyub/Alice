package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ui.MainFrame;
import javax.swing.*;

public class BackgroundPanel2 extends JPanel{
	JButton newgamebtn;
	JButton editgamebtn;
	JButton set1;
	MainFrame MainFrame;
	BackgroundPanel2(){
		//로고
		setLayout(new BorderLayout());
		
		ImageIcon img = new ImageIcon("images/ExMain.jpg"); //배경이미지
		JLabel Logo = new JLabel(img);

		ImageIcon newgame = new ImageIcon("images/newgame.jpg"); 	//새로만들버튼기이미지
		ImageIcon editgame = new ImageIcon("images/editgame.jpg"); 	//불러오기버튼이미지
		ImageIcon set = new ImageIcon("images/set.jpg");
		
		newgamebtn = new JButton(newgame);		//새로만들기버튼
		editgamebtn = new JButton(editgame);	//불러오기버튼
		set1 = new JButton(set);
		
		set1.setSize(50,50);
		set1.setLocation(10,10);
		newgamebtn.setSize(200,50);
		editgamebtn.setSize(200,50);
		newgamebtn.setLocation(250,230);
		editgamebtn.setLocation(250,300);
		
		SettingButtonActionListener setting = new SettingButtonActionListener();
		set1.addActionListener(setting);
		
		NewGameButtonActionListener newgame1 = new NewGameButtonActionListener();
		newgamebtn.addActionListener(newgame1);
		
		add(set1);
		add(newgamebtn);
		add(editgamebtn);		
		add(Logo);
		
		
	}
	class SettingButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			new settingFrame();		
		}
	}
	class NewGameButtonActionListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			new GameFrame();	
			
		}
	}
}
