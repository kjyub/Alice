package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;

public class ControlLabel extends JLabel {
	public ControlLabel(String s) {
		super(s);
		this.setForeground(Color.WHITE);
		this.setFont(new Font("굴림체",Font.PLAIN,14));
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
//		g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
	}
}