package jtetas.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class TesteImage implements Concreto {
	
	public BufferedImage iconeNave;
	
//	public ImageIcon getResource(String image) {
//		try {
////			InputStream file = getClass().getResourceAsStream("resources/images/b_black.png");
//			BufferedImage bufferedImage = ImageIO.read(new File("resources/images/b_black.png"));
////			BufferedImage bufferedImageRounded = makeRoundedCorner(bufferedImage, 20);
//			ImageIcon imageIcon = new ImageIcon(bufferedImage);
//			return imageIcon;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//    }
	

    
    public TesteImage() {//   /raventools/main/ui/images/backgrounds/RVT-Background-wagonOP10.png
    	try {
			this.iconeNave = ImageIO.read(new File("resources/images/b_black.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	@Override
	public void render(Graphics2D graphics2d, int janelaWidth, int janelaHeight) {
		
//		Graphics2D graphics2D = (Graphics2D) g;
		
		int x = 40;
		int y = 40;
		double radius = 0;
		double angle = 0;
		
		AffineTransform at = new AffineTransform();
		
//		at.translate((int)x + radius/2.5,(int)y + radius/2.5);
		
//		at.rotate(Math.PI/2 + angle);
		
//      at.translate(-iconeNave.getWidth()/2, -iconeNave.getHeight()/2);
		
		graphics2d.drawImage(iconeNave, at, null);
		
	}
    
//    public void paint(Graphics2D g2){
//        AffineTransform at = new AffineTransform();
//        int x = 0;
//		double radius = 0;
//		int y = 0;
//		at.translate((int)x + radius/2.5,(int)y + radius/2.5);
//        double angle = 0;
//		at.rotate(Math.PI/2 + angle);
//        at.translate(-iconeNave.getWidth()/2, -iconeNave.getHeight()/2);
//        g2.drawImage(iconeNave, at, null);
//    }




}
