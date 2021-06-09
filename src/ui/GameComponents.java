package ui;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class GameComponents {
	static Font defaultFont = new Font("굴림체",Font.PLAIN,12);
	public static class ControlLabel extends JLabel {
		ControlLabel(String s) {
			super(s);
			this.setForeground(Color.WHITE);
			Font font = defaultFont;
			this.setFont(new Font("굴림체",Font.PLAIN,18));
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
//			g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
		}
	}
}
