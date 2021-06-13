package libs;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

import game.Subject;

public class utils {
	public static Image getImage(String src) {
		ImageIcon g = new ImageIcon(src);
		return g.getImage();
	}public static Image getImage(URL src) {
		ImageIcon g = new ImageIcon(src);
		return g.getImage();
	}
	public Point getCenterPoint(int x, int y, int width, int height) {
		return new Point(x+(width/2),y+(height/2));
	}
	public static int[] getDetectableRange(int sd,int range) {
		int[] values = new int[1+(range*2)];
		int v_cnt = 0;
		int leftLimit = 11;
		int rightLimit = 18;
		int[] leftSide = new int[range];
		int[] rightSide = new int[range];
		// leftSide
		for(int i=range; i>0; i--) {
			int value = 0;
			int count = i;
			if (sd-count >= leftLimit) {
				value = sd - count;
			} else {
				value = rightLimit - (leftLimit-(sd-count)) + 1;
			}
			leftSide[range-i] = value;
		}
		// rightSide
		// 
		for(int i=0;i<range;i++) {
			int value = 0;
			int count = i+1;
			if (sd+count <=rightLimit) {
				value = sd + count;
			} else {
				value = leftLimit + (sd+count-rightLimit)- 1;
			}
			rightSide[i] = value;
		}
		for(int i:leftSide) {
			values[v_cnt++] = i;
		}
		values[v_cnt++] = sd;
		for(int i:rightSide) {
			values[v_cnt++] = i;
		}
		return values;
	}
}
