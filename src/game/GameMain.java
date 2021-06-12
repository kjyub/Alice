package game;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import components.GameComponents;
import components.GridBagPanel;
import components.ControlLabel;
import components.RoundedButton;

import java.awt.*;
import java.awt.event.*;

import db.AliceDAO;

public class GameMain extends JFrame{
	
	AliceDAO dao = null;
	
	GameField gameField = null;
	
	public static int user_pk;

	public static JLabel grfAmount = null;
	public static JLabel grfNeckAverage = null;
	
	// DB 로드시 업데이트
	public static JLabel timeLabel = null;
	public static JLabel lbMutantProbValue = null;
	public static JLabel lbAgeRateValue = null;
	public static JLabel lbBreedValueValue = null;
	public static JLabel lbBreedReadyValueValue = null;
	public static JLabel lbMaxIndependenceValue = null;
	public static JLabel lbSearchScaleValue = null;
	
	public static JTextField tfMutantProb = null;
	public static JTextField tfAgeRate = null;
	public static JTextField tfBreedValue = null;
	public static JTextField tfBreedReadyValue = null;
	public static JTextField tfMaxIndependence = null;
	public static JTextField tfSearchScale = null;
	
	// 상태 값들 - db 저장
	public static int mutantProb = 30; // 돌연변이 확률  0~100% - 조정가능  
	public static float sizeScale = (float) 0.5; // 기린 이미지 크기 - 조정가능  
	public static boolean subjectInfo = true;
	
	public static DefaultTableModel giraffesTableModel;
	
	public static boolean treePlacing = false;
	public static int treePlaceHeight = 0;

	public static void updatePanel() {
		timeLabel.setText(GameField.getTimeStampToString());
		lbMutantProbValue.setText(Integer.toString(mutantProb));
		tfMutantProb.setText(Integer.toString(mutantProb));
		lbAgeRateValue.setText(Integer.toString(GameField.ageRate));
		tfAgeRate.setText(Integer.toString(GameField.ageRate));
		lbBreedValueValue.setText(Integer.toString(GameField.breedValue));
		tfBreedValue.setText(Integer.toString(GameField.breedValue));
		lbBreedReadyValueValue.setText(Integer.toString(GameField.breedReadyValue));
		tfBreedReadyValue.setText(Integer.toString(GameField.breedReadyValue));
		lbMaxIndependenceValue.setText(Integer.toString(GameField.maxIndependence));
		tfMaxIndependence.setText(Integer.toString(GameField.maxIndependence));
		lbSearchScaleValue.setText(Integer.toString(GameField.searchScale));
		tfSearchScale.setText(Integer.toString(GameField.searchScale));
		System.out.println("update panel");
	}
	
	public class ControlPanel extends JPanel {
		int controlPanelWidth;
		public JLabel lbSummonTreeCount;
		JSlider treeSlider;
		ControlPanel(GameField gf) {
			this.setBackground(new Color(0x4E455D));
			controlPanelWidth = this.getWidth();
			this.setLayout(getLayout());
			
			timeLabel = new ControlLabel(GameField.getTimeStampToString());
			
			JLabel lbAmount = new ControlLabel("개체 수");
			grfAmount = new ControlLabel("");
			grfAmount.setHorizontalAlignment(JLabel.RIGHT);
			JLabel lbNeckAverage = new ControlLabel("목 길이 평균");
			grfNeckAverage = new ControlLabel("");
			grfNeckAverage.setHorizontalAlignment(JLabel.RIGHT);
			JLabel lbShowInfo = new ControlLabel("기린 정보 보기");
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
			
			JLabel lbSummonTree = new ControlLabel("소환할 나무 길이");
			lbSummonTreeCount = new ControlLabel("10");
			treeSlider = new JSlider(5,20,10);
			treeSlider.addChangeListener(new TreeSummonSliderChanged());
			JButton btnTreeSummon = new RoundedButton("소환");
			btnTreeSummon.addActionListener(new TreeSummonAction());
			
			JButton btnSummonGiraffe = new RoundedButton("기린 소환 (5마리)");
			btnSummonGiraffe.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameField.summon(5);
				}
			});
			
			JLabel lbMutantProb = new ControlLabel("돌연변이 확률 (0~100) ");
			lbMutantProbValue = new ControlLabel(Integer.toString(GameMain.mutantProb));
			tfMutantProb = new JTextField(3);
			tfMutantProb.setText(lbMutantProbValue.getText());
			JButton btnMutantProb = new RoundedButton("적용");
			btnMutantProb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String value = tfMutantProb.getText();
					try {
						int intValue = Integer.parseInt(value);
						if (intValue < 0 || intValue > 100) {
							JOptionPane.showMessageDialog(null, "0~100 사이의 값을 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
							return;
						}
						GameMain.mutantProb = intValue;
						lbMutantProbValue.setText(value);
					} catch(NumberFormatException err) {
						lbMutantProbValue.setText("ERR");
					}
				}
			});
			
			JLabel lbAgeRate = new ControlLabel("포만감 배율 ");
			lbAgeRateValue = new ControlLabel(Integer.toString(GameField.ageRate));
			tfAgeRate = new JTextField(3);
			tfAgeRate.setText(lbAgeRateValue.getText());
			JButton btnAgeRate = new RoundedButton("적용");
			btnAgeRate.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String value = tfAgeRate.getText();
					try {
						int intValue = Integer.parseInt(value);
						if (intValue < 0) {
							JOptionPane.showMessageDialog(null, "0이상의 값을 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
							return;
						}
						GameField.ageRate = intValue;
						lbAgeRateValue.setText(value);
					} catch(NumberFormatException err) {
						lbAgeRateValue.setText("ERR");
					}
				}
			});
			
			JLabel lbBreedValue = new ControlLabel("출산하기 위해 채워야하는 포만감 % ");
//			lbBreedValue.setFont(new Font("굴림체",Font.PLAIN,12));
			lbBreedValueValue = new ControlLabel(Integer.toString(GameField.breedValue));
			tfBreedValue = new JTextField(3);
			tfBreedValue.setText(lbBreedValueValue.getText());
			JButton btnBreedValue = new RoundedButton("적용");
			btnBreedValue.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String value = tfBreedValue.getText();
					try {
						int intValue = Integer.parseInt(value);
						if (intValue < 0 || intValue > 100) {
							JOptionPane.showMessageDialog(null, "0~100 사이의 값을 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
							return;
						}
						GameField.breedValue = intValue;
						lbBreedValueValue.setText(value);
					} catch(NumberFormatException err) {
						lbBreedValueValue.setText("ERR");
					}
				}
			});
			
			JLabel lbBreedReadyValue = new ControlLabel("번식 준비 기준 ~100% ");
			lbBreedReadyValueValue = new ControlLabel(Integer.toString(GameField.breedReadyValue));
			tfBreedReadyValue = new JTextField(3);
			tfBreedReadyValue.setText(lbBreedReadyValueValue.getText());
			JButton btnBreedReadyValue = new RoundedButton("적용");
			btnBreedReadyValue.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String value = tfBreedReadyValue.getText();
					try {
						int intValue = Integer.parseInt(value);
						if (intValue < 0 || intValue > 100) {
							JOptionPane.showMessageDialog(null, "0~100 사이의 값을 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
							return;
						}
						GameField.breedReadyValue = intValue;
						lbBreedReadyValueValue.setText(value);
					} catch(NumberFormatException err) {
						lbBreedReadyValueValue.setText("ERR");
					}
				}
			});

			JLabel lbMaxIndependence = new ControlLabel("새끼가 독립하기 까지 먹이 수 ");
			lbMaxIndependenceValue= new ControlLabel(Integer.toString(GameField.maxIndependence));
			tfMaxIndependence = new JTextField(3);
			tfMaxIndependence.setText(lbMaxIndependenceValue.getText());
			JButton btnMaxIndependence = new RoundedButton("적용");
			btnMaxIndependence.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String value = tfMaxIndependence.getText();
					try {
						int intValue = Integer.parseInt(value);
						if (intValue < 0 ) {
							JOptionPane.showMessageDialog(null, "0 이상의 값을 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
							return;
						}
						GameField.maxIndependence = intValue;
						lbMaxIndependenceValue.setText(value);
					} catch(NumberFormatException err) {
						lbMaxIndependenceValue.setText("ERR");
					}
				}
			});
			
			JLabel lbSearchScale = new ControlLabel("먹이 탐색 배율 ");
			lbSearchScaleValue= new ControlLabel(Integer.toString(GameField.searchScale));
			tfSearchScale = new JTextField(3);
			tfSearchScale.setText(lbSearchScaleValue.getText());
			JButton btnSearchScale = new RoundedButton("적용");
			btnSearchScale.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String value = tfSearchScale.getText();
					try {
						int intValue = Integer.parseInt(value);
						if (intValue < 0 ) {
							JOptionPane.showMessageDialog(null, "0 이상의 값을 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
							return;
						}
						GameField.searchScale = intValue;
						lbSearchScaleValue.setText(value);
					} catch(NumberFormatException err) {
						lbSearchScaleValue.setText("ERR");
					}
				}
			});
			
			JButton resetButton = new RoundedButton("초기화");
			resetButton.setBackground(new Color(0xcf433c));
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
			JButton startButton = new RoundedButton("시작");
			startButton.setBackground(new Color(0x52de91));
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
			
			GridBagPanel control1 = new GridBagPanel();
			control1.setBackground(null);
			control1.setSize(controlPanelWidth,control1.getHeight());
//			control1.insert(timeLabel, 0, 0, 5, 1);
			control1.insert(lbAmount,0,1,3,1);
			control1.insert(grfAmount,3,1,2,1);
			control1.insert(lbNeckAverage,0,2,3,1);
			control1.insert(grfNeckAverage,3,2,2,1);
			control1.insert(lbShowInfo,0,3,3,1);
			control1.insert(grfShowInfo,3,3,2,1);
			JTextField padding = new JTextField(23);
			padding.setEnabled(false);
			padding.setBackground(null);
			padding.setForeground(null);
			padding.setBorder(null);
			control1.insert(padding, 0, 3, 5, 1,new Insets(0,3,0,3));
			
			GridBagPanel summonPanel = new GridBagPanel();
			summonPanel.setBackground(null);
			summonPanel.setSize(controlPanelWidth,summonPanel.getHeight());
			summonPanel.insert(lbSummonTree,0,0,2,1);
			summonPanel.insert(lbSummonTreeCount,2,0,2,1,new Insets(0,5,0,5));
			summonPanel.insert(btnTreeSummon,4,0,1,1);
			summonPanel.insert(treeSlider,0,1,5,1);
			
			int y1 = 2;
			summonPanel.insert(lbMutantProb,0,0+y1,2,1);
			summonPanel.insert(lbMutantProbValue,3,0+y1,1,1);
			summonPanel.insert(tfMutantProb,0,1+y1,3,1);
			summonPanel.insert(btnMutantProb,4,0+y1,1,2,new Insets(5,0,5,0));
			y1 += 2;
			
			summonPanel.insert(lbAgeRate,0,0+y1,2,1);
			summonPanel.insert(lbAgeRateValue,3,0+y1,1,1);
			summonPanel.insert(tfAgeRate,0,1+y1,3,1);
			summonPanel.insert(btnAgeRate,4,0+y1,1,2,new Insets(5,0,5,0));
			y1 += 2;
			
			summonPanel.insert(lbBreedValue,0,0+y1,2,1);
			summonPanel.insert(lbBreedValueValue,3,0+y1,1,1);
			summonPanel.insert(tfBreedValue,0,1+y1,3,1);
			summonPanel.insert(btnBreedValue,4,0+y1,1,2,new Insets(5,0,5,0));
			y1 += 2;
			
			summonPanel.insert(lbBreedReadyValue,0,0+y1,2,1);
			summonPanel.insert(lbBreedReadyValueValue,3,0+y1,1,1);
			summonPanel.insert(tfBreedReadyValue,0,1+y1,3,1);
			summonPanel.insert(btnBreedReadyValue,4,0+y1,1,2,new Insets(5,0,5,0));
			y1 += 2;
			
			summonPanel.insert(lbMaxIndependence,0,0+y1,2,1);
			summonPanel.insert(lbMaxIndependenceValue,3,0+y1,1,1);
			summonPanel.insert(tfMaxIndependence,0,1+y1,3,1);
			summonPanel.insert(btnMaxIndependence,4,0+y1,1,2,new Insets(5,0,5,0));
			y1 += 2;

			summonPanel.insert(lbSearchScale,0,0+y1,2,1);
			summonPanel.insert(lbSearchScaleValue,3,0+y1,1,1);
			summonPanel.insert(tfSearchScale,0,1+y1,3,1);
			summonPanel.insert(btnSearchScale,4,0+y1,1,2,new Insets(5,0,5,0));
			y1 += 2;
			

			summonPanel.insert(resetButton,0,100,2,1,new Insets(0,4,0,4));
			summonPanel.insert(startButton,3,100,2,1,new Insets(0,4,0,4));
			
			GridBagPanel giraffeControlPanel = new GridBagPanel();
			giraffeControlPanel.setBackground(null);
			giraffeControlPanel.setSize(controlPanelWidth,summonPanel.getHeight());
			
//			this.setLayout(new FlowLayout(FlowLayout.LEFT,5,10));
			this.setSize(300, 1050);
			this.add(timeLabel);
			this.add(control1);
			this.add(summonPanel);
			JButton loginBtn = new JButton("login");
			loginBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String id = JOptionPane.showInputDialog("ID 입력");
					String pw = JOptionPane.showInputDialog("PW 입력");
					dao.login(id,pw);
					gameField.startTime();
				}
			});
			JButton logoutBtn = new JButton("logout");
			logoutBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane saveMsg = new JOptionPane();
//					saveMsg.showMessageDialog(null, "데이터 저장 중 ....");
					dao.logout();
					System.exit(0);
				}
			});
			this.add(loginBtn);
			this.add(logoutBtn);
//			this.add(giraffeControlPanel);
//			this.insert(lbAmount, 0, 0, 1, 1);
//			this.insert(grfAmount, 1, 0, 3, 1);
//			this.insert(lbNeckAverage, 0, 1, 1, 1);
//			this.insert(grfNeckAverage, 1, 1, 3, 1);
		}
		
		public void updateControlPanel() {
			System.out.println("update control panel");
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
			
			this.setLayout(new GridLayout(1,3));
			JPanel status1 = new JPanel();
			status1.setBackground(null);
			JPanel status2 = new JPanel();
			status2.setBackground(null);
			this.add(new JScrollPane(giraffesTable));
			
			this.add(status1);
			this.add(status2);
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
		
		gameField = new GameField(this.getSize().width,this.getSize().height);
		
		JPanel cp = new ControlPanel(gameField);
		cp.setLocation(0, 0);
		cp.setSize(300, 1050);
		JPanel sp = new StatusPanel();
		sp.setLocation(300,GameField.FieldSize.height);
		sp.setSize(1650-300,300);
//		gameField.summon(5);
//		gameField.treeSummon(5);
		gameField.setLocation(300, 0);
		this.add(gameField);
		this.add(cp);
		this.add(sp);
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dao.logout();
				System.exit(0);
			}
		});
		
		dao = new AliceDAO(this,gameField);
	}
 	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GameMain();
	}

}
