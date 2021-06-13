package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import components.RoundedButton;
import components.RoundedTF;

import javax.swing.*;
import db.*;
import game.GameField;
import game.GameMain;
import libs.ActionTypes;

public class LoginFrame extends JFrame{
	
	AliceDAO dao = null;
	Image bgImg = null;
	RoundedTF tfID;
	RoundedTF tfPW;
	
	public LoginFrame(){
		this.setTitle("Login");
//		this.setLayout(null);
		this.setBackground(null);
		ImageIcon img = new ImageIcon("images/ExMain.jpg");
		bgImg = img.getImage();
		
		dao = new AliceDAO();
        
        JPanel loginPanel = new LoginPanel(this);
        loginPanel.setBounds(0, 0, this.getWidth(),this.getHeight());
        
        this.add(loginPanel);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(700,450);
        this.setResizable(false);
		this.setVisible(true);
	}
	
	class LoginPanel extends JPanel{
		LoginPanel(LoginFrame frame) {
			tfID = new RoundedTF("ID");
			tfID.addActionListener(new LoginAction(frame));
			tfPW = new RoundedTF("Password");  
			tfPW.setPassword(true);
			tfPW.addActionListener(new LoginAction(frame));
	        JTextField dummy = new RoundedTF("s");               
	        RoundedButton btnLogin = new RoundedButton("로그인");
	        btnLogin.setBackground(new Color(255,140,80));
	        btnLogin.setForeground(new Color(255,255,255));
	        btnLogin.addActionListener(new LoginAction(frame));
	        
	        
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
	
	class LoginAction implements ActionListener {
		LoginFrame frame;
		LoginAction(LoginFrame frame) {
			this.frame = frame;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (GameField.started) {
				JOptionPane.showMessageDialog(null, "게임이 실행 중 입니다.", "로그인 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			String id = tfID.getText();
			String pw = tfPW.getText();
			String loginResult = dao.login(id, pw);
			if (loginResult == ActionTypes.LOGIN_SUCCESS || loginResult == ActionTypes.REGIST_SUCCESS) {
				if (loginResult == ActionTypes.REGIST_SUCCESS) {
					JOptionPane.showMessageDialog(null, "회원가입에 성공했습니다.", "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
				}
				new GameMain();
				this.frame.dispose();
			} 
			if (loginResult == ActionTypes.LOAD_FAILED) {
				JOptionPane.showMessageDialog(null, "로그인에 실패했습니다.", "로그인 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (loginResult == ActionTypes.WRONG_PASSWORD) {
				JOptionPane.showMessageDialog(null, "비밀번호가 틀렸습니다.", "로그인 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (loginResult == ActionTypes.LOGGED_IN) {
				int loggedInResult = JOptionPane.showConfirmDialog(null, "이미 접속 중입니다. \n접속 중인 계정을 로그아웃 하시겠습니까?", "로그인 오류", JOptionPane.WARNING_MESSAGE);
				if(loggedInResult == 0) {
					boolean setLogoutResult = dao.setLogout();
					if(setLogoutResult) {
						JOptionPane.showMessageDialog(null, "로그아웃에 성공했습니다.\n다시 로그인해주세요.", "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
						return;
					} else {
						JOptionPane.showMessageDialog(null, "로그아웃에 실패했습니다.\n다시 시도해주세요.", "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}
			}
			if (loginResult == ActionTypes.INVALID_INPUT) {
				JOptionPane.showMessageDialog(null, "유효하지 않는 입력입니다.", "로그인 오류", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	
	public static void main(String[] args) {
		new LoginFrame();

	}

}
