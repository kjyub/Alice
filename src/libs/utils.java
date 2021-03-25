package libs;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class utils {
	public static Image getImage(String src) {
		ImageIcon g = new ImageIcon(src);
		return g.getImage();
	}
//	public static Image getImage(String src) {
//		ImageIcon originalIcon = new ImageIcon(src);
////		BufferedImage bi = (BufferedImage)originalIcon.getImage();
//		File file = new File(src);
//		BufferedImage bi = null;
//		try {
//			bi = ImageIO.read(file);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		for (int i = 0; i < bi.getHeight(); i++) {
//	        for (int j = 0; j < bi.getWidth(); j++) {
//	            Color c = new Color(bi.getRGB(j, i));
//	            int r = c.getRed();
//	            int b = c.getBlue();
//	            int g = c.getGreen();
//	            if ((r == 255 && b == 255 && g == 255)) {
//	                bi.setRGB(j, i, Color.TRANSLUCENT);
//	            }
//	        }
//	    }
////		Image img = (Image)bi;
//		return (Image)bi ;
//	}
}
