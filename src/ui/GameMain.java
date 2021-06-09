package ui;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;

public class GameMain extends JFrame{

	public static JLabel grfAmount = null;
	public static JLabel grfNeckAverage = null;
	public static int treeAverage = 11; // 나무의 평균 높이 (-+ 2) - 조정가능 
	public static int mutantProb = 3; // 돌연변이 확률 (x10) 0~10 - 조정가능  
	public static float sizeScale = (float) 0.5; // 기린 이미지 크기 - 조정가능  
	public static boolean subjectInfo = true;
	public static DefaultTableModel giraffesTableModel;
	
	public static boolean treePlacing = false;
	public static int treePlaceHeight = 0;
	GameField gameField = null;
	
	class ControlPanelTemp extends JPanel {
		String[] tdlr = {"TOP","DOWN","LEFT","RIGHT","UPLEFT","UPRIGHT","DOWNLEFT","DOWNRIGHT"};
		int distance = 10;
		ControlPanelTemp(GameField gf) {
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
	
	class ControlPanel extends JPanel {
		int controlPanelWidth;
		JLabel lbSummonTreeCount;
		JSlider treeSlider;
		ControlPanel(GameField gf) {
			this.setBackground(new Color(0x4E455D));
			controlPanelWidth = this.getWidth();
			this.setLayout(getLayout());
			
			JLabel lbAmount = new GameComponents.ControlLabel("개체 수");
			grfAmount = new GameComponents.ControlLabel("");
			grfAmount.setHorizontalAlignment(JLabel.RIGHT);
			JLabel lbNeckAverage = new GameComponents.ControlLabel("목 길이 평균");
			grfNeckAverage = new GameComponents.ControlLabel("");
			grfNeckAverage.setHorizontalAlignment(JLabel.RIGHT);
			JLabel lbShowInfo = new GameComponents.ControlLabel("기린 정보 보기");
			JCheckBox grfShowInfo = new JCheckBox();
			grfShowInfo.setSelected(subjectInfo);
			grfShowInfo.setHorizontalAlignment(JLabel.RIGHT);
			grfShowInfo.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						subjectInfo = true;
					} else {
						subjectInfo = false;
					}
				}
			});
			
			JLabel lbSummonTree = new GameComponents.ControlLabel("소환할 나무 길이");
			lbSummonTreeCount = new GameComponents.ControlLabel("10");
			treeSlider = new JSlider(5,20,10);
			treeSlider.addChangeListener(new TreeSummonSliderChanged());
			JButton btnTreeSummon = new JButton("소환");
			btnTreeSummon.addActionListener(new TreeSummonAction());
			
			JButton btnSummonGiraffe = new JButton("기린 소환 (5마리)");
			btnSummonGiraffe.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameField.summon(5);
				}
			});
			
			JLabel lbAgeRate = new GameComponents.ControlLabel("AgeRate ");
			JLabel lbAgeRateValue = new GameComponents.ControlLabel(Integer.toString(Subject.ageRate));
			JTextField tfAgeRate = new JTextField(3);
			tfAgeRate.setText(lbAgeRateValue.getText());
			JButton btnAgeRate = new JButton("적용");
			btnAgeRate.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String value = tfAgeRate.getText();
					try {
						int intValue = Integer.parseInt(value);
						Subject.ageRate = intValue;
						lbAgeRateValue.setText(value);
					} catch(NumberFormatException err) {
						lbAgeRateValue.setText("ERR");
					}
				}
			});
			
			JLabel lbBreedValue = new GameComponents.ControlLabel("BreedValue (1~100) ");
			JLabel lbBreedValueValue = new GameComponents.ControlLabel(Integer.toString(Subject.breedValue));
			JTextField tfBreedValue = new JTextField(3);
			tfBreedValue.setText(lbBreedValueValue.getText());
			JButton btnBreedValue = new JButton("적용");
			btnBreedValue.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String value = tfBreedValue.getText();
					try {
						int intValue = Integer.parseInt(value);
						Subject.breedValue = intValue;
						lbBreedValueValue.setText(value);
					} catch(NumberFormatException err) {
						lbBreedValueValue.setText("ERR");
					}
				}
			});
			
			JLabel lbBreedReadyValue = new GameComponents.ControlLabel("BreedReadyValue ");
			JLabel lbBreedReadyValueValue = new GameComponents.ControlLabel(Integer.toString(Subject.breedReadyValue));
			JTextField tfBreedReadyValue = new JTextField(3);
			tfBreedReadyValue.setText(lbBreedReadyValueValue.getText());
			JButton btnBreedReadyValue = new JButton("적용");
			btnBreedReadyValue.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String value = tfBreedReadyValue.getText();
					try {
						int intValue = Integer.parseInt(value);
						Subject.breedReadyValue = intValue;
						lbBreedReadyValueValue.setText(value);
					} catch(NumberFormatException err) {
						lbBreedReadyValueValue.setText("ERR");
					}
				}
			});

			JLabel lbMaxIndependence = new GameComponents.ControlLabel("maxIndependence ");
			JLabel lbMaxIndependenceValue= new GameComponents.ControlLabel(Integer.toString(Subject.maxIndependence));
			JTextField tfMaxIndependence = new JTextField(3);
			tfMaxIndependence.setText(lbMaxIndependenceValue.getText());
			JButton btnMaxIndependence = new JButton("적용");
			btnMaxIndependence.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String value = tfMaxIndependence.getText();
					try {
						int intValue = Integer.parseInt(value);
						Subject.maxIndependence = intValue;
						lbMaxIndependenceValue.setText(value);
					} catch(NumberFormatException err) {
						lbMaxIndependenceValue.setText("ERR");
					}
				}
			});
			
			JLabel lbSearchScale = new GameComponents.ControlLabel("SearchScale ");
			JLabel lbSearchScaleValue= new GameComponents.ControlLabel(Integer.toString(Subject.searchScale));
			JTextField tfSearchScale = new JTextField(3);
			tfSearchScale.setText(lbSearchScaleValue.getText());
			JButton btnSearchScale = new JButton("적용");
			btnSearchScale.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String value = tfSearchScale.getText();
					try {
						int intValue = Integer.parseInt(value);
						Subject.searchScale = intValue;
						lbSearchScaleValue.setText(value);
					} catch(NumberFormatException err) {
						lbSearchScaleValue.setText("ERR");
					}
				}
			});
			
			JButton resetButton = new JButton("초기화");
			resetButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(gameField.getStarted()) {						
						gameField.reset();
					} else {
						JOptionPane.showMessageDialog(null,"이미 초기화 되었습니다.", "경고",JOptionPane.WARNING_MESSAGE);
					}
				}
			});
			JButton startButton = new JButton("시작");
			startButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!gameField.getStarted()) {
						gameField.start();
					} else {
						JOptionPane.showMessageDialog(null,"이미 시작 되었습니다.", "경고",JOptionPane.WARNING_MESSAGE);
					}
				}
			});
			
			JPanel control1 = new JPanel();
			control1.setBackground(null);
			control1.setLayout(new GridLayout(3,2,2,2));
			control1.add(lbAmount);
			control1.add(grfAmount);
			control1.add(lbNeckAverage);
			control1.add(grfNeckAverage);
			control1.add(lbShowInfo);
			control1.add(grfShowInfo);
			
			GridBagPanel summonPanel = new GridBagPanel();
			summonPanel.setBackground(null);
			summonPanel.setSize(controlPanelWidth,summonPanel.getHeight());
			summonPanel.insert(lbSummonTree,0,0,2,1);
			summonPanel.insert(lbSummonTreeCount,2,0,2,1,new Insets(0,5,0,5));
			summonPanel.insert(btnTreeSummon,4,0,1,1);
			summonPanel.insert(treeSlider,0,1,5,1);
			
			int y1 = 2;
			summonPanel.insert(lbAgeRate,0,0+y1,2,1);
			summonPanel.insert(lbAgeRateValue,3,0+y1,1,1);
			summonPanel.insert(tfAgeRate,0,1+y1,3,1);
			summonPanel.insert(btnAgeRate,4,0+y1,1,2);
			y1 += 2;
			
			summonPanel.insert(lbBreedValue,0,0+y1,2,1);
			summonPanel.insert(lbBreedValueValue,3,0+y1,1,1);
			summonPanel.insert(tfBreedValue,0,1+y1,3,1);
			summonPanel.insert(btnBreedValue,4,0+y1,1,2);
			y1 += 2;
			
			summonPanel.insert(lbBreedReadyValue,0,0+y1,2,1);
			summonPanel.insert(lbBreedReadyValueValue,3,0+y1,1,1);
			summonPanel.insert(tfBreedReadyValue,0,1+y1,3,1);
			summonPanel.insert(btnBreedReadyValue,4,0+y1,1,2);
			y1 += 2;
			
			summonPanel.insert(lbMaxIndependence,0,0+y1,2,1);
			summonPanel.insert(lbMaxIndependenceValue,3,0+y1,1,1);
			summonPanel.insert(tfMaxIndependence,0,1+y1,3,1);
			summonPanel.insert(btnMaxIndependence,4,0+y1,1,2);
			y1 += 2;

			summonPanel.insert(lbSearchScale,0,0+y1,2,1);
			summonPanel.insert(lbSearchScaleValue,3,0+y1,1,1);
			summonPanel.insert(tfSearchScale,0,1+y1,3,1);
			summonPanel.insert(btnSearchScale,4,0+y1,1,2);
			y1 += 2;
			

			summonPanel.insert(resetButton,0,100,2,1);
			summonPanel.insert(startButton,3,100,2,1);
			
			GridBagPanel giraffeControlPanel = new GridBagPanel();
			giraffeControlPanel.setBackground(null);
			giraffeControlPanel.setSize(controlPanelWidth,summonPanel.getHeight());
			
			this.add(control1);
			this.add(summonPanel);
//			this.add(giraffeControlPanel);
//			this.insert(lbAmount, 0, 0, 1, 1);
//			this.insert(grfAmount, 1, 0, 3, 1);
//			this.insert(lbNeckAverage, 0, 1, 1, 1);
//			this.insert(grfNeckAverage, 1, 1, 3, 1);
		}
		class TreeSummonAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				treePlacing = true;
				treePlaceHeight = treeSlider.getValue();
			}
		}
		class TreeSummonSliderChanged implements ChangeListener {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider sl = (JSlider) e.getSource();
				String text = "";
				if (sl.getValue() < 10) {
					text = "  ";
				}
				text += Integer.toString(sl.getValue());
				lbSummonTreeCount.setText(text);
			}
		}
	}
	
	class StatusPanel extends JPanel {
		StatusPanel() {
			String[] giraffesTableHeaders = {"ID","BIRTH DATE","NECK"};
			giraffesTableModel = new DefaultTableModel(null,giraffesTableHeaders);
			JTable giraffesTable = new JTable(giraffesTableModel);
			
			this.setLayout(new GridLayout(1,2));
			JPanel status1 = new JPanel();
			status1.setBackground(null);
			this.add(new JScrollPane(giraffesTable));
			
			this.add(status1);
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
		}
	}
	
	GameMain() {
		this.setTitle("ALICE");
		this.setSize(1650,1050);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBackground(new Color(0xF4F6FC));
//		this.setBackground(Color.BLUE);
//		this.setResizable(false);
		this.setLayout(null);
		JPanel cp = new ControlPanel(gameField);
		cp.setLocation(0, 0);
		cp.setSize(300, 1050);
		JPanel sp = new StatusPanel();
		sp.setLocation(300,GameField.FieldSize.height);
		sp.setSize(1650-300,300);
		gameField = new GameField(this.getSize().width,this.getSize().height);
//		gameField.summon(5);
//		gameField.treeSummon(5);
		gameField.setLocation(300, 0);
		this.add(gameField);
		this.add(cp);
		this.add(sp);
		this.setVisible(true);
	}
 	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GameMain();
	}

}
