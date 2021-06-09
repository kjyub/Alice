package ui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameField extends JPanel {
	public Vector<Giraffe> giraffes = new Vector<Giraffe>();
	public Vector<Tree> trees = new Vector<Tree>();
	public static int maxGiraffeID = 0;
	
	public static Dimension FieldSize = new Dimension(1350,700);
	
	private boolean started = false;

	private AlphaComposite alphaComposite;
	private int treePlaceX,treePlaceY;
	private GiraffeResource gSrc = null;
	private TreeResource tSrc = null;
	private GameField gf;
	
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
			Giraffe grf = new Giraffe(this);
			int x = (int) (Math.random()*FieldSize.width) + 1;
			int y = (int) (Math.random()*(FieldSize.height - grf.getHeight())) + 1;
			grf.setLocation(x, y);
			this.add(grf);
			grf.move();
		}
	}
	void treeSummon(int numTree) {
		for(int i=0; i<numTree; i++) {
			Tree tree = new Tree(this,tSrc);
			trees.add(tree);
			int x = (int) (Math.random()*FieldSize.width) + 1;
			int y = (int) (Math.random()*(FieldSize.height - tree.getHeight())) + 1;
			tree.setLocation(x,y);
			this.add(tree);
		}
	}
	void treeSummon(int x, int y) {
		Tree tree = new Tree(this,tSrc,GameMain.treePlaceHeight);
		trees.add(tree);
		int treeWidth = tSrc.TotalWidth;
		int treeHeight = GameMain.treePlaceHeight*tSrc.TreeLengthUnit;
		tree.setLocation(treePlaceX-(treeWidth/2),treePlaceY-(treeHeight/2));
		this.add(tree);
		tree.repaint();
	}
	GiraffeResource getResource() {
		return this.gSrc;
	}
	Vector<Tree> getFeeds() {
		return this.trees;
	}
	void updateAmount() {
		GameMain.grfAmount.setText(Integer.toString(this.giraffes.size()));
		GameMain.giraffesTableModel.setNumRows(0);
		int neckSum = 0;
		for (Giraffe grf:giraffes) {
			neckSum += grf.neck;
			Vector<String> tableRow = new Vector<String>();
			tableRow.add(Integer.toString(grf.id));
			tableRow.add(grf.birthDate.toString());
			tableRow.add(Integer.toString(grf.neck));
			GameMain.giraffesTableModel.addRow(tableRow);
		}
		System.out.println("업데이트 ! "+neckSum+","+giraffes.size());
		if (giraffes.size() > 0) {
			GameMain.grfNeckAverage.setText(String.format("%.1f", (float) neckSum/giraffes.size()));
		}
		
	}
	
	boolean getStarted() {
		return this.started;
	}
	
	void start() {
		this.summon(5);
		this.started = true;
	}
	
	void reset() {
		giraffes = new Vector<Giraffe>();
		trees = new Vector<Tree>();
		this.removeAll();
		this.repaint();
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
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon imgIcon = new ImageIcon("resource/FieldBg.jpg");
		g.drawImage(imgIcon.getImage(),0,0,this.getWidth(),this.getHeight(),this);
		if(GameMain.treePlacing) {
			alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1/2);
			g = (Graphics2D)g;
		    ((Graphics2D) g).setComposite(alphaComposite);
		    
			System.out.println("placing");
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
