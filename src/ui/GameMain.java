package ui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameMain extends JFrame{

	GameField gameField = null;
	
	class ControlPanel extends JPanel {
		String[] tdlr = {"TOP","DOWN","LEFT","RIGHT","UPLEFT","UPRIGHT","DOWNLEFT","DOWNRIGHT"};
		int distance = 10;
		ControlPanel(GameField gf) {
			this.setBackground(new Color(0x4E455D));
//			for (int i=0; i<tdlr.length; i++) {
//				JButton btn = new JButton(tdlr[i]);
//				btn.addActionListener(new ActionListener() {
//					@Override
//					public void actionPerformed(ActionEvent e) {
////						JButton btn = (JButton)e.getSource();
////						String direction = btn.getText();
////						Giraffe g1 = gf.g;
////						if (direction.equals("TOP")) {
////							g1.moving(Subject.ToUp,distance);
////						} else if (direction.equals("DOWN")) {
////							g1.moving(Subject.ToDown,distance);
////						} else if (direction.equals("LEFT")) {
////							g1.moving(Subject.ToLeft,distance);
////						} else if (direction.equals("RIGHT")) {
////							g1.moving(Subject.ToRight,distance);
////						} else if (direction.equals("UPLEFT")) {
////							g1.moving(Subject.ToUpLeft,distance);
////						} else if (direction.equals("UPRIGHT")) {
////							g1.moving(Subject.ToUpRight,distance);
////						} else if (direction.equals("DOWNLEFT")) {
////							g1.moving(Subject.ToDownLeft,distance);
////						} else if (direction.equals("DOWNRIGHT")) {
////							g1.moving(Subject.ToDownRight,distance);
////						}
//					}
//				});
//				this.add(btn);
//			}
			JLabel numberLabel = new JLabel("number of giraffe and summon");
			numberLabel.setForeground(Color.white);
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
			speedTf.setText("100");
			speedTf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						JTextField tf = (JTextField)e.getSource();
						int speed = Integer.parseInt(tf.getText());
//						Giraffe g1 = gf.g;
//						g1.setSpeed(speed);
					} catch (NumberFormatException e1) {
						System.out.println("Wrong Input");
					}
				}
			});
			this.add(speedTf);
			JButton eating = new JButton("EAT");
			eating.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
//					gf.g.eating();
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
			JButton stopBtn = new JButton("STOP");
			stopBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for(int i=0;i<gf.giraffes.size();i++) {
						Giraffe grf = gf.giraffes.get(i);
						grf.isMove=false;
					}
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
//		this.setResizable(false);
		this.setLayout(new BorderLayout());
		gameField = new GameField(this.getSize().width,this.getSize().height);
		this.add(gameField,BorderLayout.CENTER);
		this.add(new ControlPanel(gameField),BorderLayout.NORTH);
		
		this.setVisible(true);
	}
 	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GameMain();
	}

}
