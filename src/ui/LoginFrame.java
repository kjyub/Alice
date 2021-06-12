package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import components.RoundedButton;
import components.RoundedTF;

import javax.swing.*;
import db.*;
import game.GameMain;
import libs.ActionTypes;

public class LoginFrame extends JFrame{
	
	AliceDAO dao = null;
	Image bgImg = null;
	
	public LoginFrame(){
		this.setTitle("Login");
//		this.setLayout(null);
		this.setBackground(null);
		ImageIcon img = new ImageIcon("images/ExMain.jpg");
		bgImg = img.getImage();
		
		dao = new AliceDAO();
        
        JPanel loginPanel = new LoginPanel();
        loginPanel.setBounds(0, 0, this.getWidth(),this.getHeight());
        
        this.add(loginPanel);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(700,450);
        this.setResizable(false);
		this.setVisible(true);
	}
	
	class LoginPanel extends JPanel{
		JTextField tfID;
		JTextField tfPW;
		LoginPanel() {
			tfID = new RoundedTF("ID");
			tfPW = new RoundedTF("Password");  
	        JTextField dummy = new RoundedTF("");               
	        RoundedButton btnLogin = new RoundedButton("로그인");
	        btnLogin.setBackground(new Color(255,140,80));
	        btnLogin.setForeground(new Color(255,255,255));
	        btnLogin.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String id = tfID.getText();
					String pw = tfPW.getText();
					String loginResult = dao.login(id, pw);
					if (loginResult == ActionTypes.LOGIN_SUCCESS || loginResult == ActionTypes.REGIST_SUCCESS) {
						if (loginResult == ActionTypes.REGIST_SUCCESS) {
							JOptionPane.showMessageDialog(null, "회원가입에 성공했습니다.", "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
						}
						new GameMain();
					} 
					if (loginResult == ActionTypes.LOAD_FAILED) {
						JOptionPane.showMessageDialog(null, "로그인에 실패했습니다.", "로그인 오류", JOptionPane.WARNING_MESSAGE);
					}
					if (loginResult == ActionTypes.WRONG_PASSWORD) {
						JOptionPane.showMessageDialog(null, "비밀번호가 틀렸습니다.", "로그인 오류", JOptionPane.WARNING_MESSAGE);
					}
					if (loginResult == ActionTypes.LOGGED_IN) {
						JOptionPane.showMessageDialog(null, "이미 접속 중입니다.", "로그인 오류", JOptionPane.WARNING_MESSAGE);
					}
					if (loginResult == ActionTypes.INVALID_INPUT) {
						JOptionPane.showMessageDialog(null, "유효하지 않는 입력입니다.", "로그인 오류", JOptionPane.WARNING_MESSAGE);
					}
				}
	        });
	        
	        
	        this.setLayout(null);
	        tfID.setBounds(25, 150, 450, 50);
	        tfPW.setBounds(25, 210, 450, 50);
	        btnLogin.setBounds(25, 290, 450, 50);
	        dummy.setBounds(701, 451, 1, 1);
	        
	        this.add(dummy);
	        this.add(tfID);
	        this.add(tfPW);
	        this.add(btnLogin);
	        
	        this.setOpaque(false);
		}
		
		public void paintComponent(Graphics g) { 
			g.drawImage(bgImg,0,0,this.getWidth(),this.getHeight(),null);
		}
	}
	
	 

	
	public static void main(String[] args) {
		new LoginFrame();

	}

}
