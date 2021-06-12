package game;

import java.util.*;
import javax.swing.*;

import game.GameMain.ControlPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameField extends JPanel {
	
	public static final int AverageTimeUnit = 10;
	
	// 상태 값들 - db 저장
	public static long timeStamp = 0;
	public static boolean timeStopFlag = true;
	
	public static Vector<Giraffe> giraffes = new Vector<Giraffe>();
	public static Vector<Tree> trees = new Vector<Tree>();
	public static Vector<Float> giraffeAverages = new Vector<Float>();
	public static float giraffeAverage = (float)0.0;
	
	public static int maxGiraffeID = 0;
	
	public static int searchScale = 10; // 먹이 탐색 범위 - 조절 가능
	public static int ageRate = 20; // (중요) 수치들 배수 - 조절 가능
	public static int breedReadyValue = 70;  // 몇번 먹이를 먹어야 출산을 할수 있는지 - 조절 가능
	public static int breedValue = 30; // 0~100  조절 가능
	public static int maxIndependence = 3; // 조절 가능
	public static boolean started = false;
	
	public static Dimension FieldSize = new Dimension(1350,700);
	

	private AlphaComposite alphaComposite;
	private int treePlaceX,treePlaceY;
	private GiraffeResource gSrc = null;
	private TreeResource tSrc = null;
	private static GameField gf;
	
	GameField(int sizeX,int sizeY) {
		int paddingSize = 20;
		this.setLocation(paddingSize, paddingSize);
		this.setBackground(new Color(0xf59342));
		this.setSize(FieldSize);
//		this.setBackground(Color.);
		this.setLayout(null);
		gSrc = new GiraffeResource();
		tSrc = new TreeResource();
		
		this.addMouseListener(new TreePlaceCursor());
		this.addMouseMotionListener(new TreePlaceCursor());
		this.gf = this;
	}
	void summon(int numGiraffes) {
		for(int i=0; i<numGiraffes; i++) {
			Giraffe grf = new Giraffe(this,10);
			int x = (int) (Math.random()*FieldSize.width) + 1;
			int y = (int) (Math.random()*(FieldSize.height - grf.getHeight())) + 1;
			grf.setLocation(x, y);
			this.add(grf);
			grf.move();
		}
	}
	public void summon(ArrayList<GiraffeVO> grfs) {
		for(GiraffeVO vo : grfs) {
			Giraffe grf = new Giraffe(this,vo);
			this.add(grf);
			grf.move_force();
		}
		this.repaint();
		this.updateAmount();
	}
	void treeSummon(int x, int y) {
		Tree tree = new Tree(this,tSrc,GameMain.treePlaceHeight);
		trees.add(tree);
		int treeWidth = tSrc.TotalWidth;
		int treeHeight = GameMain.treePlaceHeight*tSrc.TreeLengthUnit;
		tree.setLocation(treePlaceX-(treeWidth/2),treePlaceY-(treeHeight/2));
		this.add(tree);
		tree.repaint();
		this.repaint();
	}
	public void treeSummon(ArrayList<TreeVO> trees) {
		for(TreeVO vo : trees) {
			Tree tree = new Tree(this,tSrc,vo);
			this.trees.add(tree);
			this.add(tree);
			tree.repaint();
		}
		this.repaint();
	}
	GiraffeResource getResource() {
		return this.gSrc;
	}
	Vector<Tree> getFeeds() {
		return this.trees;
	}
	public void updateAmount() {
		GameMain.grfAmount.setText(Integer.toString(this.giraffes.size()));
		GameMain.giraffesTableModel.setNumRows(0);
		int neckSum = 0;
		System.out.println(this.giraffes.size());
		for (Giraffe grf:giraffes) {
			neckSum += grf.neck;
			Vector<String> tableRow = new Vector<String>();
			tableRow.add(Integer.toString(grf.id));
			tableRow.add(grf.birthDate.toString());
			tableRow.add(Integer.toString(grf.neck));
			GameMain.giraffesTableModel.addRow(tableRow);
		}
		
		System.out.println("업데이트 ! "+neckSum+","+giraffes.size());
		giraffeAverage = (float) neckSum/giraffes.size();
		if (giraffes.size() > 0) {
			GameMain.grfNeckAverage.setText(String.format("%.1f", giraffeAverage));
		}
	}
	
	
	void updateAverages() {
		giraffeAverages.add(giraffeAverage);
		Vector<String> tableRow = new Vector<String>();
		tableRow.add(getTimeStampToString());
		tableRow.add(Float.toString(giraffeAverage));
		GameMain.giraffesHeightTableModel.addRow(tableRow);
	}
	
	boolean getStarted() {
		return this.started;
	}
	
	void start() {
		this.summon(5);
		this.started = true;
		this.timeStopFlag = true;
		this.startTime();
	}
	
	void reset() {
		giraffes = new Vector<Giraffe>();
		trees = new Vector<Tree>();
		giraffeAverages = new Vector<Float>();
		GameMain.giraffesTableModel.setNumRows(0);
		GameMain.giraffesHeightTableModel.setNumRows(0);
		GameMain.grfAmount.setText(Integer.toString(this.giraffes.size()));
		GameMain.grfNeckAverage.setText("0");
		this.started = false;
		this.removeAll();
		this.repaint();
		this.timeStopFlag=true;
		this.maxGiraffeID = 0;
	}
	
	class TreePlaceCursor extends MouseAdapter {
		@Override
		public void mouseMoved(MouseEvent e){
			if(GameMain.treePlacing) {
				treePlaceX = e.getX();
				treePlaceY = e.getY();
				gf.repaint();
			}
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(GameMain.treePlacing) {
				treeSummon(treePlaceX,treePlaceY);
				GameMain.treePlacing = false;
				gf.repaint();
			}
		}
	}
	
	public void startTime() {
		if(!timeStopFlag) {
			return;
		}
		timeStopFlag = false;
		Thread timeThread = new Thread(new StartTime());
		timeThread.start();
	}
	public static String getTimeStampToString() {
		int s = (int) timeStamp%60;
		int m = (int) timeStamp/60;
		int h = (int) timeStamp/3600;
		String ss = s < 10 ? "0"+Integer.toString(s) : Integer.toString(s);
		String ms = m < 10 ? "0"+Integer.toString(m) : Integer.toString(m);
		String hs = h < 10 ? "0"+Integer.toString(h) : Integer.toString(h);
		
		return hs+":"+ms+":"+ss;
	}
	public static String getTimeStampToString(int time) {
		int s = (int) time%60;
		int m = (int) time/60;
		int h = (int) time/3600;
		String ss = s < 10 ? "0"+Integer.toString(s) : Integer.toString(s);
		String ms = m < 10 ? "0"+Integer.toString(m) : Integer.toString(m);
		String hs = h < 10 ? "0"+Integer.toString(h) : Integer.toString(h);
		
		return hs+":"+ms+":"+ss;
	}
	
	// 시계 시작
	class StartTime implements Runnable {
		@Override
		public synchronized void run() {
			while(!GameField.timeStopFlag){
				GameField.timeStamp++;
				// AverageTimeUnit 초 마다 한번씩 목 길이 평균 데이터 업데이트
				if (GameField.timeStamp % AverageTimeUnit == 0) {
					updateAverages();
				}
				GameMain.timeLabel.setText(getTimeStampToString());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
//			GameField.timeStopFlag = false;
			GameField.timeStamp = 0;
			GameMain.timeLabel.setText(getTimeStampToString());
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon imgIcon = new ImageIcon("resource/FieldBg.jpg");
		g.drawImage(imgIcon.getImage(),0,0,this.getWidth(),this.getHeight(),this);
		if(GameMain.treePlacing) {
			alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1/2);
			g = (Graphics2D)g;
		    ((Graphics2D) g).setComposite(alphaComposite);

			int treeWidth = tSrc.TotalWidth;
			int treeHeight = GameMain.treePlaceHeight*tSrc.TreeLengthUnit;
			g.drawImage(tSrc.getTreeImg(0),
					treePlaceX-(treeWidth/2),treePlaceY-(treeHeight/2),
					treeWidth,treeHeight,
					this
					);
//			g.fillRect(
//					treePlaceX-(treeWidth/2),treePlaceY-(treeHeight/2),
//					treeWidth,treeHeight
//					);
		}
	}
}
