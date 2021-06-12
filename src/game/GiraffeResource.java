package game;

import java.awt.Image;

import libs.utils;

public class GiraffeResource {
	private String imgSrc = "resource/subjects/giraffe01/";
	public int headFrameCount = 1;
	public int headEatFrameCount = 3;
	public int neckFrameCount = 1;
	public int bodyFrameCount = 15;
	public int babyFrameCount = 4;
	
	public static final int TotalWidth = (int) (150 * GameMain.sizeScale);
	public static final int TotalHeight = (int) (200 * GameMain.sizeScale);
	public static final int HeadHeight = (int) (37 * GameMain.sizeScale);
	public static final int NeckHeight = (int) (45 * GameMain.sizeScale);
	public static final int BodyHeight = (int) (118 * GameMain.sizeScale);

	public static final int NeckHeightUnit = (int) (10 * GameMain.sizeScale);
	public static final int WithoutNeckHeight = (int) (150 * GameMain.sizeScale);
	
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
			bodyImgs[i] = utils.getImage(imgSrc+"body3/"+Integer.toString(i)+".png");
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
