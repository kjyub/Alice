package game;

import java.awt.Image;

import libs.utils;

public class TreeResource {
	private String imgSrc = "resource/subjects/tree/";
	
	public static final int TotalWidth = (int) (150 * GameMain.sizeScale);
	public static final int TotalHeight = (int) (200 * GameMain.sizeScale);
	
	static final int LEAF0_AMOUNT = 10;
	static final int LEAF1_AMOUNT = 12;
	static final int LEAF2_AMOUNT = 10;
	
	private Image[] treeImgs = new Image[1];
	private Image[] treeNeckImgs = new Image[1];
	private Image[] treeLeafImgs = new Image[4];

	public static final int NeckHeight = (int) (128 * GameMain.sizeScale);
	public static final int NeckHeightUnit = (int) (10 * GameMain.sizeScale);
	public static final int WithoutNeckHeight = (int) (128 * GameMain.sizeScale);
	
	TreeResource() {
		treeImgs[0] = utils.getImage(imgSrc+"tree"+Integer.toString(0)+".png");
		treeNeckImgs[0] = utils.getImage(imgSrc+"neck"+Integer.toString(0)+".png");
		for(int i=0; i<4; i++) {
			treeLeafImgs[i] = utils.getImage(imgSrc+"leaf"+Integer.toString(i)+".png");
		}
	}
	public Image getTreeImg(int frame) {
		return treeImgs[frame];
	}
	public Image getTreeNeckImg(int frame) {
		return treeNeckImgs[frame];
	}
	public Image getTreeLeafImg(int leafs) {
		int frame = 0;
		if (leafs>28) {
			frame = 3;
		} else if (leafs <= 28 && leafs > 15) {
			frame = 2;
		} else if (leafs <= 15 && leafs > 5) {
			frame = 1;
		}
		return treeLeafImgs[frame];
	}
}
