package ui;

import java.awt.Image;

import libs.utils;

public class TreeResource {
	private String imgSrc = "src/resource/subjects/tree/";
	public int treeFrameCount = 1;
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
