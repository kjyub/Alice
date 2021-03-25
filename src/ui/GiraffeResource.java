package ui;

import java.awt.Image;

import libs.utils;

public class GiraffeResource {
	private String imgSrc = "src/resource/subjects/girrafe/";
	public int headFrameCount = 1;
	public int neckFrameCount = 1;
	public int bodyFrameCount = 4;
	private Image[] headImgs = new Image[headFrameCount];
	private Image[] neckImgs = new Image[neckFrameCount];
	private Image[] bodyImgs = new Image[bodyFrameCount];
	
	GiraffeResource() {
		for(int i=0; i<headFrameCount; i++) {
			headImgs[i] = utils.getImage(imgSrc+"head"+Integer.toString(i)+".png");
		}
		for(int i=0; i<neckFrameCount; i++) {
			neckImgs[i] = utils.getImage(imgSrc+"neck"+Integer.toString(i)+".png");
		}
		for(int i=0; i<bodyFrameCount; i++) {
			bodyImgs[i] = utils.getImage(imgSrc+"body"+Integer.toString(i)+".png");
		}
	}
	public Image getHeadImg(int frame) {
		return headImgs[frame];
	}
	public Image getNeckImg(int frame) {
		return neckImgs[frame];
	}
	public Image getBodyImg(int frame) {
		return bodyImgs[frame];
	}
}
