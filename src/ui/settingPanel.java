package ui;

import java.awt.*;
import javax.swing.*;

public class settingPanel extends JPanel{
		
	settingPanel(){
		BorderLayout c = new BorderLayout();
		GridLayout grid = new GridLayout();				
		c.setVgap(50);

		JSlider volume = new JSlider(JSlider.HORIZONTAL,0,100,100);
						
		volume.setPaintLabels(true);
		volume.setPaintTicks(true);
		volume.setPaintTrack(true);
		volume.setMajorTickSpacing(20);
		volume.setMinorTickSpacing(10);
		volume.setLocation(50,50);
		
		JCheckBox full = new JCheckBox("전체화면",true);
		JCheckBox window = new JCheckBox("창모드");
		
		ButtonGroup bg = new ButtonGroup(); 
		bg.add(full);
		bg.add(window);
		String [] resolution = {"1024x768","1280x800","1600x1200","1800x1440","1920x1080"};
		JComboBox<String> resolution1 = new JComboBox<String>(resolution);
				
		
		add(full);
		add(window);
		add(new JLabel("해상도"));
		add(resolution1);
		add(new JLabel("배경음악"));
		add(volume);

	}
}
