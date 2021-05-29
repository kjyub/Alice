package ui;

import java.awt.Image;

import libs.utils;

public class GiraffeResource {
	private String imgSrc = "src/resource/subjects/girrafe/";
	public int headFrameCount = 1;
	public int headEatFrameCount = 3;
	public int neckFrameCount = 1;
	public int bodyFrameCount = 4;
	public int babyFrameCount = 4;
	private Image[] headImgs = new Image[headFrameCount];
	private Image[] headEatImgs = new Image[headEatFrameCount];
	private Image[] neckImgs = new Image[neckFrameCount];
	private Image[] bodyImgs = new Image[bodyFrameCount];
	private Image[] babyImgs = new Image[babyFrameCount];
	
	GiraffeResource() {
		for(int i=0; i<headFrameCount; i++) {
			headImgs[i] = utils.getImage(imgSrc+"head"+Integer.toString(i)+".png");
		}
		for(int i=0; i<headEatFrameCount; i++) {
			headEatImgs[i] = utils.getImage(imgSrc+"eat"+Integer.toString(i)+".png");
		}
		for(int i=0; i<neckFrameCount; i++) {
			neckImgs[i] = utils.getImage(imgSrc+"neck"+Integer.toString(i)+".png");
		}
		for(int i=0; i<bodyFrameCount; i++) {
			bodyImgs[i] = utils.getImage(imgSrc+"body"+Integer.toString(i)+".png");
		}
		for(int i=0; i<babyFrameCount; i++) {
			babyImgs[i] = utils.getImage(imgSrc+"baby"+Integer.toString(i)+".png");
		}
	}
	public Image getHeadImg(int frame) {
		return headImgs[frame];
	}
	public Image getHeadEatImg(int frame) {
		return headEatImgs[frame];
	}
	public Image getNeckImg(int frame) {
		return neckImgs[frame];
	}
	public Image getBodyImg(int frame) {
		if (frame>=bodyFrameCount) {
			System.out.println(frame);
			return bodyImgs[0];
		}
		return bodyImgs[frame];
	}
	public Image getBabyImg(int frame) {
		return babyImgs[frame];
	}
}
