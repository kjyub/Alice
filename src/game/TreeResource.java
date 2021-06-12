package game;

import java.awt.Image;

import libs.utils;

public class TreeResource {
	private String imgSrc = "resource/subjects/tree/";
	public  int treeFrameCount = 1;
	
	public static final int TotalWidth = (int) (150 * GameMain.sizeScale);
	public static final int TotalHeight = (int) (200 * GameMain.sizeScale);
	public static final int TreeLengthUnit = (int) (20 * GameMain.sizeScale);
	
	private Image[] treeImgs = new Image[treeFrameCount];
	
	TreeResource() {
		for(int i=0; i<treeFrameCount; i++) {
			treeImgs[i] = utils.getImage(imgSrc+"tree"+Integer.toString(i)+".png");
		}
	}
	public Image getTreeImg(int frame) {
		return treeImgs[frame];
	}
}
