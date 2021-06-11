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
	public static class RoundedButton extends JButton {
		public RoundedButton(String s) {
			super(s);
		}
		@Override 
		protected void paintComponent(Graphics g) { 
			int width = getWidth(); 
			int height = getHeight(); 
			Graphics2D graphics = (Graphics2D) g; 
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
			if (getModel().isArmed()) { 
				graphics.setColor(getBackground().darker()); 
			} else if (getModel().isRollover()) { 
				graphics.setColor(getBackground().brighter()); 
			} else { 
				graphics.setColor(getBackground()); 
			}
			graphics.fillRoundRect(0, 0, width, height, 10, 10); 
			FontMetrics fontMetrics = graphics.getFontMetrics(); 
			Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds(); 
			int textX = (width - stringBounds.width) / 2; 
			int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent(); 
			graphics.setColor(getForeground()); 
			graphics.setFont(getFont()); 
			graphics.drawString(getText(), textX, textY); 
			graphics.dispose(); 
			super.paintComponent(g); 
		}
	}


}
