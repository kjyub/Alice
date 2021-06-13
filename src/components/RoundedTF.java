package components;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

public class RoundedTF extends JTextField {
	
	private boolean isPassword = false;
	private boolean focus = false;
	private String hintString = "";
	
	Font gainFont = new Font("굴림체", Font.PLAIN, 11); 
	Font lostFont = new Font("굴림체", Font.ITALIC, 11); 
	
	public RoundedTF(String s) {
		this.hintString = s;
		setFont(lostFont);
		setForeground(Color.BLACK); 
		this.setOpaque(false);
		
		this.addFocusListener(new FocusAdapter() {  
			@Override  
			public void focusGained(FocusEvent e) {
				focus = true;
			}  
			@Override  
			public void focusLost(FocusEvent e) {  
				focus = false;
			}  
		});
	}
	
	public void setPassword(boolean b) {
		isPassword = b;
	}
	
	private String getPasswordString(int length) {
		String s = "";
		for(int i=0; i<length; i++) {
			s+="*";
		}
		return s;
	}

	
	@Override 
	protected void paintComponent(Graphics g) { 
		int width = getWidth(); 
		int height = getHeight(); 
		Graphics2D graphics = (Graphics2D) g; 
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(getBackground()); 
		graphics.fillRoundRect(0, 0, width, height, 10, 10); 
		
		if(focus) {
			graphics.setColor(Color.BLUE); 
			graphics.drawRoundRect(0, 0, width-1, height-1, 10, 10); 
		}
		
		FontMetrics fontMetrics = graphics.getFontMetrics(); 
		Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds(); 
		int textX = (width - stringBounds.width) / 2; 
		int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent(); 
		if(this.getText().length() == 0) {			
			graphics.setFont(lostFont);
			graphics.setColor(Color.GRAY); 
			graphics.drawString(this.hintString, 10, textY); 
		} else {		
			graphics.setFont(gainFont);
			graphics.setColor(getForeground()); 
			graphics.drawString(this.isPassword ? this.getPasswordString(this.getText().length()) : getText(), 10, textY); 
		}
		graphics.dispose(); 
		super.paintComponent(g); 
	}
}