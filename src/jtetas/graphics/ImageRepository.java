package jtetas.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class ImageRepository {
	
	//BLOCKS
	public static final String IMG_BLOCK_BLACK = "src/jtetas/resources/images/b_black.png";
	public static final String IMG_BLOCK_BLUE = "src/jtetas/resources/images/b_blue.png";
	public static final String IMG_BLOCK_CYAN = "src/jtetas/resources/images/b_cyan.png";
	public static final String IMG_BLOCK_GREEN = "src/jtetas/resources/images/b_green.png";
	public static final String IMG_BLOCK_ORANGE = "src/jtetas/resources/images/b_orange.png";
	public static final String IMG_BLOCK_PURPLE = "src/jtetas/resources/images/b_purple.png";
	public static final String IMG_BLOCK_RED = "src/jtetas/resources/images/b_red.png";
	public static final String IMG_BLOCK_WHITE = "src/jtetas/resources/images/b_white.png";
	public static final String IMG_BLOCK_YELLOW = "src/jtetas/resources/images/b_yellow.png";
	
	public BufferedImage getImageRepo (String imageLocation) {
		try {
			return ImageIO.read(new File(imageLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
