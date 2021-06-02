package ui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameMain extends JFrame{

	public static JLabel grfAmount = null;
	public static int treeAverage = 11; // 나무의 평균 높이 (-+ 2) - 조정가능 
	public static int mutantProb = 3; // 돌연변이 확률 (x10) 0~10 - 조정가능  
	
	GameField gameField = null;
	
	class ControlPanel extends JPanel {
		String[] tdlr = {"TOP","DOWN","LEFT","RIGHT","UPLEFT","UPRIGHT","DOWNLEFT","DOWNRIGHT"};
		int distance = 10;
		ControlPanel(GameField gf) {
			this.setBackground(new Color(0x4E455D));
			JLabel numberLabel = new JLabel("summon");
			numberLabel.setForeground(Color.white);
			
			JLabel grfAmountLabel = new JLabel("개체 수 : ");
			grfAmountLabel.setForeground(Color.white);
			this.add(grfAmountLabel);
			grfAmount = new JLabel("0");
			grfAmount.setForeground(Color.white);
			this.add(grfAmount);
			
			JTextField grfTf = new JTextField(5);
			grfTf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						JTextField tf = (JTextField)e.getSource();
						int num = Integer.parseInt(tf.getText());
						gf.summon(num);
					} catch (NumberFormatException e1) {
						System.out.println("Wrong Input");
					}
					gf.repaint();
				}
			});
			this.add(numberLabel);
			this.add(grfTf);
			JLabel distanceLabel = new JLabel("DISTANCE");
			distanceLabel.setForeground(Color.white);
			this.add(distanceLabel);
			JTextField tf = new JTextField(5);
			tf.setText(Integer.toString(distance));
			tf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						JTextField tf = (JTextField)e.getSource();
						distance = Integer.parseInt(tf.getText());
					} catch (NumberFormatException e1) {
						System.out.println("Wrong Input");
					}
				}
			});
			this.add(tf);
			
			JLabel speedLabel = new JLabel("SPEED");
			speedLabel.setForeground(Color.white);
			this.add(speedLabel);
			JTextField speedTf = new JTextField(5);
			speedTf.setText("50");
			speedTf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						JTextField tf = (JTextField)e.getSource();
						int speed = Integer.parseInt(tf.getText());
						for(int i=0;i<gf.giraffes.size();i++) {
							Giraffe grf = gf.giraffes.get(i);
							grf.setSpeed(speed);
						}
					} catch (NumberFormatException e1) {
						System.out.println("Wrong Input");
					}
				}
			});
			this.add(speedTf);
			
			JLabel rateLabel = new JLabel("Age Rate");
			rateLabel.setForeground(Color.white);
			this.add(rateLabel);
			JTextField rateTf = new JTextField(5);
			rateTf.setText("50");
			rateTf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						JTextField tf = (JTextField)e.getSource();
						int rate = Integer.parseInt(tf.getText());
						for(int i=0;i<gf.giraffes.size();i++) {
							Giraffe grf = gf.giraffes.get(i);
							grf.setAgeRate(rate);
						}
					} catch (NumberFormatException e1) {
						System.out.println("Wrong Input");
					}
				}
			});
			this.add(rateTf);

			JLabel calLabel = new JLabel("calorie");
			calLabel.setForeground(Color.white);
			this.add(calLabel);
			JTextField calTf = new JTextField(5);
			calTf.setText("50");
			calTf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						JTextField tf = (JTextField)e.getSource();
						int cal = Integer.parseInt(tf.getText());
						for(int i=0;i<gf.giraffes.size();i++) {
							Giraffe grf = gf.giraffes.get(i);
							grf.setCal(cal);
						}
					} catch (NumberFormatException e1) {
						System.out.println("Wrong Input");
					}
				}
			});
			this.add(calTf);
			
			JButton eating = new JButton("EAT");
			eating.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for(int i=0;i<gf.giraffes.size();i++) {
						Giraffe grf = gf.giraffes.get(i);
//						grf.eat();
					}
				}
			});
			this.add(eating);
			JButton startBtn = new JButton("START");
			startBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for(int i=0;i<gf.giraffes.size();i++) {
						Giraffe grf = gf.giraffes.get(i);
						grf.isMove=true;
					}
				}
			});
			this.add(startBtn);
			JButton stopBtn = new JButton("REMOVE");
			stopBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for(int i=0;i<gf.giraffes.size();i++) {
						Giraffe grf = gf.giraffes.get(i);
						System.out.println(grf);
						grf.die();
						gf.giraffes.remove(i);
					}
					gf.trees.removeAllElements();
					gf.removeAll();
					gf.repaint();
				}
			});
			this.add(stopBtn);
		}
	}
	
	GameMain() {
		this.setTitle("ALICE");
		this.setSize(1650,1050);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBackground(new Color(0xF4F6FC));
//		this.setBackground(Color.BLUE);
//		this.setResizable(false);
		this.setLayout(new BorderLayout());
		gameField = new GameField(this.getSize().width,this.getSize().height);
		gameField.treeSummon(3);
		this.add(gameField,BorderLayout.CENTER);
		this.add(new ControlPanel(gameField),BorderLayout.NORTH);
		this.setVisible(true);
	}
 	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GameMain();
	}

}
