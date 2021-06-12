package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginFrame extends JFrame{
	
	Container c;
	ImageIcon img = new ImageIcon("images/Login.jpg");
	Image im=img.getImage();
	RoundButton LoginButton;
	
	LoginFrame(){

		this.setTitle("Login");
		
		MyPanel panel = new MyPanel();
        
		
		
		
        JTextField id = new HintTextField("ID");
        JTextField pwd = new HintTextField("Password");                
        LoginButton = new RoundButton("로그인");
        
        
        
        id.setBounds(25, 110, 450, 50);
        pwd.setBounds(25, 170, 450, 50);
        LoginButton.setBounds(25, 230, 450, 50);

        LoginButtonActionListener Login = new LoginButtonActionListener();
        
        LoginButton.addActionListener(Login);
        
        add(LoginButton);
        add(id);
        add(pwd);
        
        
        
        this.add(panel);
		this.setVisible(true);	
		this.setSize(520,340);	
			
	}
	
	 class MyPanel extends JPanel{
			
	        public void paintComponent(Graphics g){
	        	setLayout(new BorderLayout());
	            super.paintComponent(g);
	            g.drawImage(im,0,0,getWidth(),getHeight(),this);	            
	        }
	    }
	 
	 class LoginButtonActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				new MainFrame();
				
				}				
		}
	
	
	public static void main(String[] args) {
		new LoginFrame();

	}

}
