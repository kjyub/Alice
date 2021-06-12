package components;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

public class RoundedTF extends JTextField {
	Font gainFont = new Font("굴림체", Font.PLAIN, 11); 
	Font lostFont = new Font("굴림체", Font.ITALIC, 11); 
	
	public RoundedTF(String s) {
		setText(s);
		setFont(lostFont);
		setForeground(Color.GRAY); 
		
		this.addFocusListener(new FocusAdapter() {  
			@Override  
			public void focusGained(FocusEvent e) {  
				if (getText().equals(s)) {  
					setText("");  
					setFont(gainFont);  
				} else {  
					setText(getText());  
					setFont(gainFont);  
				}  
			}  
			@Override  
			public void focusLost(FocusEvent e) {  
				if (getText().equals(s)|| getText().length()==0) {  
					setText(s);  
					setFont(lostFont);  
					setForeground(Color.GRAY);  
				} else {  
					setText(getText());  
					setFont(gainFont);  
					setForeground(Color.BLACK);  
				}  
			}  
		});  
		this.setHorizontalAlignment(JTextField.LEFT);
	}
	@Override 
	protected void paintComponent(Graphics g) { 
		int width = getWidth(); 
		int height = getHeight(); 
		Graphics2D graphics = (Graphics2D) g; 
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(getBackground()); 
		graphics.fillRoundRect(0, 0, width, height, 10, 10); 
		FontMetrics fontMetrics = graphics.getFontMetrics(); 
		Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds(); 
		int textX = (width - stringBounds.width) / 2; 
		int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent(); 
		graphics.setColor(getForeground()); 
		graphics.setFont(getFont()); 
		graphics.drawString(getText(), 10, textY); 
		graphics.dispose(); 
		super.paintComponent(g); 
	}
}