package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel{	
	JButton animalchoice;
	JButton set1;
	
	BackgroundPanel(){
		//로고
		setLayout(new BorderLayout());
		
		ImageIcon img = new ImageIcon("images/ExMain.jpg");
		JLabel Logo = new JLabel(img);

		ImageIcon animal = new ImageIcon("images/animal.jpg");
		ImageIcon set = new ImageIcon("images/set.jpg");
		
		animalchoice = new JButton(animal);
		set1 = new JButton(set);
		
		set1.setSize(50,50);
		set1.setLocation(10,10);
		animalchoice.setSize(200,50);
		animalchoice.setLocation(250,230);
		
		ButtonActionListener Action = new ButtonActionListener();
		animalchoice.addActionListener(Action);
		
		SettingButtonActionListener setting = new SettingButtonActionListener();
		set1.addActionListener(setting);
		
		add(set1);
		add(animalchoice);
		add(Logo);
		
		
	}
	class ButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			MainFrame Frame = (MainFrame)b.getTopLevelAncestor();			
			if(e.getSource() == animalchoice) {
				Frame.viewPanel(new BackgroundPanel2());			

			}		
		}		
	}
	class SettingButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			new settingFrame();		
		}		
	}
}