package jtetas.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public final class ImageRepository {
	
	//BLOCKS
	public static final String IMG_BLOCK_BLACK = "/images/b_black.png";
	public static final String IMG_BLOCK_BLUE = "/images/b_blue.png";
	public static final String IMG_BLOCK_CYAN = "/images/b_cyan.png";
	public static final String IMG_BLOCK_GREEN = "/images/b_green.png";
	public static final String IMG_BLOCK_ORANGE = "/images/b_orange.png";
	public static final String IMG_BLOCK_PURPLE = "/images/b_purple.png";
	public static final String IMG_BLOCK_RED = "/images/b_red.png";
	public static final String IMG_BLOCK_WHITE = "/images/b_white.png";
	public static final String IMG_BLOCK_YELLOW = "/images/b_yellow.png";
	
	public BufferedImage getImageRepo (String imageLocation) {
		try {
			InputStream file = getClass().getResourceAsStream(imageLocation);
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
